package com.hdbsthor012airflow.config;

import com.alibaba.nacos.api.config.ConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {

    @Autowired
    private DataSourceListProperties dataSourceListProperties;

    @Autowired
    private ConfigService configService;

    @Bean
    @Primary // 确保这是主数据源
    public DataSource dataSource() throws Exception {
        List<String> dataSourceNames = dataSourceListProperties.getPfList();
        System.out.println("dataSourceNames: " + dataSourceNames);

        if (dataSourceNames == null || dataSourceNames.isEmpty()) {
            throw new RuntimeException("dataSourceNames is null or empty");
        }

        Map<Object, Object> targetDataSources = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory()); // 使用 YAML 解析器

        for (String dataSourceName : dataSourceNames) {
            String dataId = dataSourceName; // 确保 DataId 以 .yaml 结尾
            String group = "DEFAULT_GROUP"; // 确保与 Nacos 中的 Group 一致

            try {
                String configContent = configService.getConfig(dataId, group, 5000);
                System.out.println("加载配置文件: " + dataId);
                System.out.println("配置内容: " + configContent);

                if (configContent != null && !configContent.isEmpty()) {
                    // 解析 YAML 内容
                    Map<String, Object> configMap = objectMapper.readValue(configContent, Map.class);

                    // 确保 "spring" 节点存在且为 Map
                    Object springObj = configMap.get("spring");
                    if (!(springObj instanceof Map)) {
                        throw new RuntimeException("'spring' 配置格式错误：" + dataId);
                    }
                    Map<String, Object> springMap = (Map<String, Object>) springObj;

                    // 确保 "datasource" 节点存在且为 Map
                    Object datasourceObj = springMap.get("datasource");
                    if (!(datasourceObj instanceof Map)) {
                        throw new RuntimeException("'datasource' 配置格式错误：" + dataId);
                    }
                    Map<String, Object> datasourceMap = (Map<String, Object>) datasourceObj;

                    String url = (String) datasourceMap.get("url");
                    String username = (String) datasourceMap.get("username");
                    String password = (String) datasourceMap.get("password");
                    String driverClassName = (String) datasourceMap.getOrDefault("driver-class-name", "com.mysql.cj.jdbc.Driver");
                    String type = (String) datasourceMap.getOrDefault("type", "com.alibaba.druid.pool.DruidDataSource"); // 默认 DruidDataSource

                    if (url == null || username == null || password == null) {
                        throw new RuntimeException("数据源配置不完整：" + dataId);
                    }

                    @SuppressWarnings("unchecked")
                    Class<? extends DataSource> typeClass = (Class<? extends DataSource>) Class.forName(type);

                    DataSource dataSource = DataSourceBuilder.create()
                            .url(url)
                            .username(username)
                            .password(password)
                            .driverClassName(driverClassName)
                            .type(typeClass)
                            .build();
                    targetDataSources.put(dataSourceName, dataSource);
                } else {
                    throw new RuntimeException("未获取到数据源配置：" + dataId);
                }
            } catch (Exception e) {
                throw new RuntimeException("加载数据源配置失败：" + dataId, e);
            }
        }

        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        dynamicRoutingDataSource.setDefaultTargetDataSource(targetDataSources.values().iterator().next());
        dynamicRoutingDataSource.setTargetDataSources(targetDataSources);
        dynamicRoutingDataSource.afterPropertiesSet();

        return dynamicRoutingDataSource;
    }
}