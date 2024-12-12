package com.hdbsthor012airflow.camundaSubscriber;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import com.hdbsthor012airflow.entity.SystemMsg;
import com.hdbsthor012airflow.entity.SystemMsgGroupUser;
import com.hdbsthor012airflow.entity.SystemMsgGroupWarningType;
import com.hdbsthor012airflow.entity.SystemMsgWarningType;
import com.hdbsthor012airflow.repository.SystemMsgGroupUserRepository;
import com.hdbsthor012airflow.repository.SystemMsgGroupWarningTypeRepository;
import com.hdbsthor012airflow.repository.SystemMsgRepository;
import com.hdbsthor012airflow.repository.SystemMsgWarningTypeRepository;
import com.hdbsthor012airflow.service.JdbcMySqlService;
import com.hdbsthor012airflow.util.UUIDGenerator;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SubscriberSendMessageService {

    private static final Logger logger = LoggerFactory.getLogger(StandardModificationTableStatusService.class);

    private static final String MESSAGE_BODY_KEY = "messageBody";

    @Autowired
    private Environment environment;

    @Autowired
    private JdbcMySqlService jdbcMySqlService;

    @Autowired
    private SystemMsgRepository systemMsgRepository;

    @Autowired
    private SystemMsgGroupUserRepository systemMsgGroupUserRepository;

    @Autowired
    private SystemMsgWarningTypeRepository systemMsgWarningTypeRepository;

    @Autowired
    private SystemMsgGroupWarningTypeRepository systemMsgGroupWarningTypeRepository;

    @Bean(name = "sendMessageBean")
    @ExternalTaskSubscription(topicName = "hdbsthor-011-third-logistics.sendMessage", lockDuration = 500)
    public ExternalTaskHandler sendMessage(@Autowired Environment environment, @Autowired JdbcMySqlService jdbcMySqlService) {
        return (externalTask, externalTaskService) -> {
            Map<String, Object> variables = externalTask.getAllVariables();
            String businessKey = externalTask.getBusinessKey();
            String processDefinitionKey = externalTask.getProcessDefinitionKey();
            logger.info("allvariables: " + variables + " businessKey:" + businessKey + " processDefinitionKey:" + processDefinitionKey);

            try {
                // 处理发送站内信
                String messageBody = String.valueOf(variables.get(MESSAGE_BODY_KEY));
                JSONObject bodyJSON = JSONUtil.parseObj(messageBody);
                // 将 JSON 对象转换成 SystemMsg 对象
                SystemMsg systemMsgOriginal = bodyJSON.toBean(SystemMsg.class);
                if (StringUtils.isNotEmpty(messageBody)) {
                    // step 1 : 通过流程 key 得到对应的告警类型列表
                    List<SystemMsgWarningType> systemMsgWarningTypeList = systemMsgWarningTypeRepository.findByCamundaId(processDefinitionKey);
                    List<String> typeIdList = systemMsgWarningTypeList.stream()
                            .map(SystemMsgWarningType::getId)
                            .collect(Collectors.toList());
                    if (CollectionUtil.isEmpty(typeIdList)) {
                        logger.warn("processDefinitionKey:[" + processDefinitionKey + "] 没有绑定监控类型");
                    } else {
                        // step 2 : 通过告警类型编号列表得到用户组 ID 列表
                        List<SystemMsgGroupWarningType> userGroupList = systemMsgGroupWarningTypeRepository.findByTypeIdIn(typeIdList);
                        List<String> userGroupIdList = userGroupList.stream()
                                .map(SystemMsgGroupWarningType::getGroupId)
                                .collect(Collectors.toList());

                        // step 3 : 找到对应的业务编号对应的用户列表`
                        List<SystemMsgGroupUser> systemMsgGroupUsers = systemMsgGroupUserRepository.findAllByGroupIdIn(userGroupIdList);

                        // step 4 : 将消息插入到站内消息表
                        if (!CollectionUtils.isEmpty(systemMsgGroupUsers)) {
                            for (SystemMsgGroupUser groupUser : systemMsgGroupUsers) {
                                SystemMsg systemMsg = new SystemMsg();
                                BeanUtil.copyProperties(systemMsgOriginal, systemMsg);
                                systemMsg.setId(UUIDGenerator.getUUID());
                                systemMsg.setHasReaded(0);
                                systemMsg.setSendUserId("0");
                                systemMsg.setReceiveUserId(groupUser.getUserId());
                                systemMsg.setAddTime(LocalDateTime.now());
                                systemMsg.setUpdTime(LocalDateTime.now());
                                logger.info("插入一条站内信：" + JSONUtil.toJsonStr(systemMsg));
                                systemMsgRepository.save(systemMsg);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("SubscriberSendMessageService doBusiness error: ", e);
            }

            try {
                externalTaskService.complete(externalTask);
            } catch (Exception e) {
                externalTaskService.handleFailure(externalTask, e.getMessage(), null, 0, 0);
            }
        };
    }

}
