package com.hdbsthor012airflow.config;

import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.config.ConfigService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.Executor;

@Component
public class NacosDataSourceChangeListener {

    @Autowired
    private ConfigService configService;

    @Autowired
    private DynamicDataSourceConfig dynamicDataSourceConfig;

    @PostConstruct
    public void init() throws Exception {
        String dataId = "mysqlList.json"; // 根据实际情况
        String group = "mysql"; // 根据实际情况

        configService.addListener(dataId, group, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                try {
                    // 重新加载数据源
                    dynamicDataSourceConfig.dataSource();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });
    }
}