package com.hdbsthor012airflow.service;

import com.hdbsthor012airflow.entity.SystemThorLog;
import com.hdbsthor012airflow.interfaces.SystemThorLogIf;
import com.hdbsthor012airflow.repository.SystemThorLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SystemThorLogService implements SystemThorLogIf {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SystemThorLogRepository systemThorLogRepository;

	@Override
	public void saveLog(String url, String httpMethod, String classMethod, String args, String excetiopnInfo, Integer exceptionFlag) {
		SystemThorLog log = new SystemThorLog();
		log.setId(UUID.randomUUID().toString().replace("-", "")); // 生成不带 - 的唯一 ID
		log.setUrl(url);
		log.setHttpMethod(httpMethod);
		log.setClassMethod(classMethod);
		log.setArgs(args);
		log.setAddDate(LocalDateTime.now()); // 当前时间
		log.setExcetiopnInfo(excetiopnInfo);
		log.setExceptionFlag(exceptionFlag);

		systemThorLogRepository.save(log);
		logger.info("Log saved: " + log);
	}
}