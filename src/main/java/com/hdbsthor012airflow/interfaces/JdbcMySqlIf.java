package com.hdbsthor012airflow.interfaces;

public interface JdbcMySqlIf {

    /**
     * 更新数据库表中的一条记录。
     *
     * @param tableName 目标表名
     * @param columnName 要更新的列名
     * @param columnValue 新的列值
     * @param whereColumnName 过滤条件的列名
     * @param whereColumnValue 过滤条件的列值
     */
    void updateTable(String tableName, String columnName, String columnValue, String whereColumnName, String whereColumnValue);
}

