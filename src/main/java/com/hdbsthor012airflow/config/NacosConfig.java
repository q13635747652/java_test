package com.hdbsthor012airflow.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.PropertyKeyConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class NacosConfig {

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;

    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;

    @Value("${spring.cloud.nacos.config.username}")
    private String username;

    @Value("${spring.cloud.nacos.config.password}")
    private String password;

    @Bean
    public ConfigService configService() throws Exception {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        properties.put(PropertyKeyConst.USERNAME, username);
        properties.put(PropertyKeyConst.PASSWORD, password);
        return NacosFactory.createConfigService(properties);
    }
}