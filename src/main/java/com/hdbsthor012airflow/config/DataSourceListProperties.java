package com.hdbsthor012airflow.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "datasource-list")
public class DataSourceListProperties {
    private String version;
    private String date;
    private String remarks;
    private List<String> pfList;
    @PostConstruct
    public void init() {
        System.out.println("DataSourceListProperties 初始化");
        System.out.println("version: " + version);
        System.out.println("date: " + date);
        System.out.println("remarks: " + remarks);
        System.out.println("pfList: " + pfList);
    }
}