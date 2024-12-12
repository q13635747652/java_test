package com.hdbsthor012airflow;

import com.hdbsthor012airflow.config.CamundaTopicProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CamundaTopicProperties.class)
public class Hdbsthor012airflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(Hdbsthor012airflowApplication.class, args);
    }

}
