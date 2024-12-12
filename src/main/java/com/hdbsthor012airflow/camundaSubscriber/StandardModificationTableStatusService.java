package com.hdbsthor012airflow.camundaSubscriber;

import com.hdbsthor012airflow.service.JdbcMySqlService;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;


@Configuration
public class StandardModificationTableStatusService {
    private static final Logger logger = LoggerFactory.getLogger(StandardModificationTableStatusService.class);

    @Autowired
    private Environment environment;
    @Autowired
    private JdbcMySqlService jdbcMySqlService;

    @Bean
    @ExternalTaskSubscription(topicName = "hdbsthor-008-contract.updateTable", lockDuration = 500)
    public ExternalTaskHandler updateTable(@Autowired Environment environment, @Autowired JdbcMySqlService jdbcMySqlService) {
        return (externalTask, externalTaskService) -> {
            Map<String, Object> variables = externalTask.getAllVariables();
            System.out.println("allvariables: " + variables);

            if (!variables.containsKey("updateTable") ||
                    !(variables.get("updateTable") instanceof Map)) {
                throw new IllegalArgumentException("Missing required 'updateTable' variable.");
            }

            Map<String, Object> updateTableVariables = (Map<String, Object>) variables.get("updateTable");
            if (!updateTableVariables.containsKey("tableName") || !updateTableVariables.containsKey("columnName") ||
                    !updateTableVariables.containsKey("columnValue") || !updateTableVariables.containsKey("whereColumnName") ||
                    !updateTableVariables.containsKey("whereColumnValue")) {
                throw new IllegalArgumentException("Missing required variable(s). Ensure all required variables are present.");
            }

            String tableName = (String) updateTableVariables.get("tableName");
            logger.info("Table Name: {}", tableName);
            String columnName = (String) updateTableVariables.get("columnName");
            logger.info("Column Name: {}", columnName);
            String columnValue = (String) updateTableVariables.get("columnValue");
            logger.info("Column Value: {}", columnValue);
            String whereColumnName = (String) updateTableVariables.get("whereColumnName");
            logger.info("Where Column Name: {}", whereColumnName);
            String whereColumnValue = (String) updateTableVariables.get("whereColumnValue");
            logger.info("Where Column Value: {}", whereColumnValue);

            try {
                jdbcMySqlService.updateTable(tableName, columnName, columnValue, whereColumnName, whereColumnValue);
                externalTaskService.complete(externalTask);
            } catch (Exception e) {
                System.err.println("Error while updating the table: " + e.getMessage());
                externalTaskService.handleFailure(externalTask, e.getMessage(), null, 0, 0);
            }
        };
    }

}
