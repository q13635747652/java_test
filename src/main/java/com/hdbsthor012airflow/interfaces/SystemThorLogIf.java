package com.hdbsthor012airflow.interfaces;

/**
 *  
 */
public interface SystemThorLogIf {

    void saveLog(String url, String httpMethod, String classMethod, String args, String excetiopnInfo, Integer exceptionFlag);
}
