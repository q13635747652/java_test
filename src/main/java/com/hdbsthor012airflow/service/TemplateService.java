package com.hdbsthor012airflow.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hdbsthor012airflow.interfaces.NacosInformationIf;
import com.hdbsthor012airflow.interfaces.TemplateServiceIf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TemplateService implements TemplateServiceIf {
    @Autowired
    NacosInformationIf nacosInformationIf;
    private static final Logger logger = LoggerFactory.getLogger(TemplateService.class);

    @Override
    public String buildWord(String params) throws Exception {
        JSONObject jsonObject = JSONUtil.parseObj(params);
        String kingHulkServerUrl = nacosInformationIf.getKingHulkServerUrl(jsonObject.getStr("nacosPF"), "mk50", null);
        logger.info("kingHulkServerUrl:" + kingHulkServerUrl);
        String postURL = kingHulkServerUrl + "/TemplateController/buildWord";

        int maxRetries = 5; // 最大重试次数
        int retryCount = 0;
        long retryInterval = 2000L; // 每次重试间隔1秒
        int timeout = 60000; // 设置超时时间（毫秒）

        while (retryCount < maxRetries) {
            try {
                // 发送 POST 请求
                HttpResponse response = HttpRequest.post(postURL)
                        .contentType("application/json") // 设置请求头
                        .body(params) // 设置请求体
                        .timeout(timeout) // 设置超时时间
                        .execute(); // 执行请求

                // 检查响应状态码
                if (response.isOk()) {
                    return response.body();
                } else {
                    logger.warn("Request failed with status code: " + response.getStatus());
                }
            } catch (Exception e) {
                logger.error("Error occurred during request: ", e);
            }

            // 增加重试计数
            retryCount++;
            if (retryCount < maxRetries) {
                logger.info("Retrying request (" + retryCount + "/" + maxRetries + ")...");
                logger.info("kingHulkServerUrl:" + kingHulkServerUrl);
                logger.info("params:" + params);
                logger.info("------------------------------------------------------------------------------------");
                Thread.sleep(retryInterval); // 等待重试
            }
        }

        throw new Exception("Failed to execute request after " + maxRetries + " retries");
    }

}






