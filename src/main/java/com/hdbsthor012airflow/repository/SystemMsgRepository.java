package com.hdbsthor012airflow.repository;

import com.hdbsthor012airflow.entity.SystemMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemMsgRepository extends JpaRepository<SystemMsg, String> {


}
