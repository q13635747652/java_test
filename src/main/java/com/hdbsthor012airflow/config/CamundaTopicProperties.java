package com.hdbsthor012airflow.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@ConfigurationProperties(prefix = "spring")
public class CamundaTopicProperties {

    private static final Logger logger = LoggerFactory.getLogger(CamundaTopicProperties.class);

    private List<String> topicNacosList;

    public List<String> getTopicNacosList() {
        return topicNacosList;
    }

    public void setTopicNacosList(List<String> topicNacosList) {
        this.topicNacosList = topicNacosList;
    }

    @PostConstruct
    public void init() {
        logger.info("Loaded camunda.topic-nacos-list: {}", topicNacosList);
    }
}