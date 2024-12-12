package com.hdbsthor012airflow.service;

import com.hdbsthor012airflow.config.CamundaTopicProperties;
import com.hdbsthor012airflow.entity.SystemVersion;
import com.hdbsthor012airflow.interfaces.SystemVersionIf;
import com.hdbsthor012airflow.repository.SystemVersionRepository;
import com.hdbsthor012airflow.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SystemVersionService implements SystemVersionIf {
    private static final Logger logger = LoggerFactory.getLogger(SystemVersionService.class);

    @Autowired
    SystemVersionRepository systemVersionRepository;

    @Autowired
    Environment environment;

    @Autowired
    CamundaTopicProperties camundaTopicProperties;

    public String getSystemVersion() {
        logger.info("camundaTopicProperties.getTopicNacosList(): {}", camundaTopicProperties.getTopicNacosList());
        return systemVersionRepository.findAll().get(0).getVersionNo();
    }

    public String saveSystemVersion(String versionNo,String comId) {
        SystemVersion systemVersion = new SystemVersion();
        systemVersion.setId(UUIDGenerator.getUUID());
        systemVersion.setComId(comId);
        systemVersion.setVersionTime(new java.util.Date());
        systemVersion.setVersionNo(versionNo);
        systemVersionRepository.save(systemVersion);
        return versionNo;
    }
}
