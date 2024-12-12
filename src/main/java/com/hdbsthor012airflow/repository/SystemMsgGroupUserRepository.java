package com.hdbsthor012airflow.repository;

import com.hdbsthor012airflow.entity.SystemMsgGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SystemMsgGroupUserRepository extends JpaRepository<SystemMsgGroupUser, String> {

    List<SystemMsgGroupUser> findAllByGroupIdIn(List<String> groupIds);

}
