package com.hdbsthor012airflow.service;

import com.hdbsthor012airflow.camundaSubscriber.StandardModificationTableStatusService;
import com.hdbsthor012airflow.interfaces.JdbcMySqlIf;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// 这里帮我修改成为，实现 JdbcMySqlIf 接口
public class JdbcMySqlService implements JdbcMySqlIf {
    private static final Logger logger = LoggerFactory.getLogger(StandardModificationTableStatusService.class);
    @PersistenceContext
    private EntityManager em;

    /**
     * 更新数据库表中的一条记录。
     *
     * @param tableName 目标表名
     * @param columnName 要更新的列名
     * @param columnValue 新的列值
     * @param whereColumnName 过滤条件的列名
     * @param whereColumnValue 过滤条件的列值
     */
    @Transactional
    public void updateTable(String tableName, String columnName, String columnValue, String whereColumnName, String whereColumnValue) {
        String query = "UPDATE " + tableName + " SET " + columnName + " = :columnValue WHERE " + whereColumnName + " = :whereColumnValue";
        logger.info("Executing query: " + query);
        try {
            Query updateQuery = em.createNativeQuery(query);
            updateQuery.setParameter("columnValue", columnValue);
            updateQuery.setParameter("whereColumnValue", whereColumnValue);
            int rowsAffected = updateQuery.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Update successful.");
            } else {
                System.out.println("No rows were updated.");
            }
        } catch (Exception e) {
            System.err.println("Error while updating the table: " + e.getMessage());
            throw e;  // Rethrow the exception to handle it in the calling method
        }
    }
}
