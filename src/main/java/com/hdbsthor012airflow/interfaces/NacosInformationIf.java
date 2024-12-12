package com.hdbsthor012airflow.interfaces;

import org.springframework.http.ResponseEntity;

/**
 * Nacos信息服务接口
 *
 * 该接口定义了从Nacos服务器获取配置信息以及处理Nacos相关数据的方法。
 *
 * @author
 * @since 2024-09-27
 */
public interface NacosInformationIf {

    /**
     * 获取 MysqlPF 字段
     *
     * @param dataId    数据ID
     * @param group     Nacos组
     * @param namespace 命名空间
     * @return MysqlPF 配置信息
     * @throws Exception 如果获取配置失败则抛出异常
     */
    String getMysqlPF(String dataId, String group, String namespace) throws Exception;

    /**
     * 获取 KingHulkServer 字段
     *
     * @param dataId    数据ID
     * @param group     Nacos组
     * @param namespace 命名空间
     * @return KingHulkServer 配置信息
     * @throws Exception 如果获取配置失败则抛出异常
     */
    String getKingHulkServerUrl(String dataId, String group, String namespace) throws Exception;

    /**
     * 获取 Nacos 配置信息
     *
     * @param dataId    数据ID
     * @param group     Nacos组
     * @param namespace 命名空间
     * @return Nacos 配置信息的响应实体
     * @throws Exception 如果获取配置失败则抛出异常
     */
    ResponseEntity<String> getConfigs(String dataId, String group, String namespace) throws Exception;

    /**
     * 获取访问令牌
     *
     * @return 访问令牌
     * @throws Exception 如果获取令牌失败则抛出异常
     */
    String getAccessToken() throws Exception;

}