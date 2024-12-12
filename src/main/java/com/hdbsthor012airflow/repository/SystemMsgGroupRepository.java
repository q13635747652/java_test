package com.hdbsthor012airflow.repository;

import com.hdbsthor012airflow.entity.SystemMsgGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemMsgGroupRepository extends JpaRepository<SystemMsgGroup, String> {

}

