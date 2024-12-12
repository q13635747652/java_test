package com.hdbsthor012airflow.config;

public class DataSourceProperties {
    private String url;
    private String username;
    private String password;
    // 如果有其他属性，可以继续添加

    // Getters and Setters

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}