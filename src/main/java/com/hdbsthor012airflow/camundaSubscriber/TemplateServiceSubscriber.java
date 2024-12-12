package com.hdbsthor012airflow.camundaSubscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdbsthor012airflow.config.CamundaTopicProperties;
import com.hdbsthor012airflow.datasource.DataSourceContextHolder;
import com.hdbsthor012airflow.interfaces.SystemThorLogIf;
import com.hdbsthor012airflow.interfaces.TemplateServiceIf;
import com.hdbsthor012airflow.util.BaseUtil;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class TemplateServiceSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(TemplateServiceSubscriber.class);

    @Autowired
    private SystemThorLogIf systemThorLogIf; // 注入日志服务

    @Autowired
    CamundaTopicProperties camundaTopicProperties;

    @Bean
    @ExternalTaskSubscription(topicName = "hdbsthor-011-third-logistics.templateService",lockDuration = 120000) // 设置锁定时间为120秒
    public ExternalTaskHandler templateServiceNew(@Autowired Environment environment, @Autowired TemplateServiceIf templateServiceIf) {
        return (externalTask, externalTaskService) -> {

            boolean isSuccess = false; // 标记是否成功
            String result = null; // 用于存储服务返回结果

            // 创建 ObjectMapper 实例
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                logger.info("Starting external task with id: " + externalTask.getId());
                Map<String, Object> variables = externalTask.getAllVariables();

                if (!variables.containsKey("templateParams")) {
                    throw new IllegalArgumentException("缺少templateParams变量.");
                }

                if ((!(variables.get("templateParams") instanceof Map)) && (!(variables.get("templateParams") instanceof ArrayList))) {
                    throw new IllegalArgumentException("变量templateParams不是Map或ArrayList类型");
                }

                if (!variables.containsKey("流程变量-文档模板填充")) {
                    throw new IllegalArgumentException("在流程调中调用模板操作服务，必须设置 流程变量-文档模板填充 变量");
                }

                Map<String, Object> templateParams;
                // 从TOPIC中订阅的数据，templateParams以流程变量-文档模板填充变量为准
                templateParams = (Map<String, Object>) variables.get("流程变量-文档模板填充");

                // 参数检查
                if (!BaseUtil.Base_HasValue(templateParams.get("nacosPF"))) {
                    throw new IllegalArgumentException("变量nacosPF不能为空");
                }

                // 如果不在数组之中，那么直接返回 null ，不参与执行，交给 其他的服务进行执行。
                // 检查 nacosPF 是否在 topicNacosList 中
                // 获取配置的 topicNacosList
                List<String> topicNacosList = camundaTopicProperties.getTopicNacosList();
                String nacosPF = templateParams.get("nacosPF").toString();
                logger.info("camundaTopicProperties.getTopicNacosList(): {}", topicNacosList);
                logger.info("nacosPF: '{}' ", nacosPF);
//                if (!topicNacosList.contains(nacosPF)) {
//                    logger.info("nacosPF '{}' 不在配置的 topicNacosList 中，跳过执行。", nacosPF);
//                    // 可以选择不完成任务，让其他订阅者处理
//                    externalTaskService.unlock(externalTask);
//                    return;
//                } else {
//                    logger.info("nacosPF '{}' 在配置的 topicNacosList 中，继续执行。", nacosPF);
//                }

                if (!BaseUtil.Base_HasValue(templateParams.get("documentsId"))) {
                    throw new IllegalArgumentException("变量documentsId不能为空");
                }

                if (!BaseUtil.Base_HasValue(templateParams.get("wordConfigId"))) {
                    throw new IllegalArgumentException("变量wordConfigId不能为空");
                }

                if (!BaseUtil.Base_HasValue(templateParams.get("userId"))) {
                    throw new IllegalArgumentException("变量userId不能为空");
                }

                // 将 Map 转换为 JSON 字符串
                String jsonString = objectMapper.writeValueAsString(templateParams);
                result = templateServiceIf.buildWord(jsonString);
                Map<String, Object> outputVariables = new HashMap<>();
                logger.info("------------------------------------------------------------------------");
                logger.info("buildWord params: " + jsonString);
                logger.info("buildWord result: " + result);
                logger.info("------------------------------------------------------------------------");
                // 检查 result 中的 errcode
                if (result != null) {
                    Map<String, Object> resultMap = objectMapper.readValue(result, Map.class);
                    if (resultMap.containsKey("errcode") && "500".equals(resultMap.get("errcode").toString())) {
                        isSuccess = false; // 标记为失败
                    } else {
                        isSuccess = true; // 标记为成功
                    }
                } else {
                    isSuccess = false; // 如果 result 为 null，标记为失败
                }

                outputVariables.put("TemplateServiceSubscriber." + templateParams.get("wordConfigId"), result);
                externalTaskService.complete(externalTask, outputVariables);
            } catch (Exception e) {
                isSuccess = false; // 捕获异常时标记为失败
                System.err.println("Error while Fill the hdbsthor-011-third-logistics.TemplateService " + e.getMessage());
                externalTaskService.handleFailure(externalTask, e.getMessage(), e.getStackTrace().toString(), 0, 1000);
            } finally {
                String url = "/TemplateController/buildWord"; // 日志 URL
                String httpMethod = "POST"; // 请求方法
                String classMethod = "TemplateService.buildWord"; // 类方法

                // 将 externalTask 的变量和 result 转换为 JSON 格式
                Map<String, Object> logData = new HashMap<>();
                logData.put("args", externalTask.getAllVariables());
                try {
                    logData.put("result", result != null ? objectMapper.readValue(result, Map.class) : "无结果"); // 将 result 转换为 JSON 格式
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String logMessage = null; // 转换为 JSON 字符串
                try {
                    logMessage = objectMapper.writeValueAsString(logData);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // 切换数据源的逻辑（这里硬编码）
                DataSourceContextHolder.setDataSourceKey("assetsTestDataResources");

                // 记录日志
                if (isSuccess) {
                    systemThorLogIf.saveLog(url, httpMethod, classMethod, logMessage, "成功", 0); // 0 表示无异常
                } else {
                    systemThorLogIf.saveLog(url, httpMethod, classMethod, logMessage, "失败", 1); // 1 表示有异常
                }
            }
        };
    }
}