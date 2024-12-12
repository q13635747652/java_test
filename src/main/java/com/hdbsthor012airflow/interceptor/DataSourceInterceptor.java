package com.hdbsthor012airflow.interceptor;

import com.hdbsthor012airflow.config.DataSourceListProperties;
import com.hdbsthor012airflow.datasource.DataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DataSourceInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceInterceptor.class);

    @Autowired
    private DataSourceListProperties dataSourceListProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("DataSourceInterceptor preHandle 被调用");
        // 从请求参数中获取 PF 参数
        String dataSourceKey = request.getParameter("PF");
        if (dataSourceKey != null && !dataSourceKey.isEmpty()) {
            if (dataSourceListProperties.getPfList().contains(dataSourceKey)) {
                DataSourceContextHolder.setDataSourceKey(dataSourceKey);
                logger.info("切换到数据源: {}", dataSourceKey);
                return true;
            } else {
                logger.warn("请求中提供的数据源标识不存在: {}", dataSourceKey);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("数据源不存在：" + dataSourceKey);
                return false;
            }
        } else {
            logger.warn("请求中未提供数据源标识 (PF 参数)");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("未提供数据源标识 (PF 参数)");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String currentDataSource = DataSourceContextHolder.getDataSourceKey();
        DataSourceContextHolder.clear();
        logger.info("清除当前数据源: {}", currentDataSource);
    }
}