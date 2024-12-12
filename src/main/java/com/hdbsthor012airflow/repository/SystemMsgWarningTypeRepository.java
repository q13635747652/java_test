package com.hdbsthor012airflow.repository;

import com.hdbsthor012airflow.entity.SystemMsgWarningType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemMsgWarningTypeRepository extends JpaRepository<SystemMsgWarningType, String> {

    /**
     * 根据流程引擎过滤管道 ID 查询预警类型列表。
     *
     * @param camundaId 流程引擎过滤管道 ID
     * @return 符合条件的预警类型列表
     */
    List<SystemMsgWarningType> findByCamundaId(String camundaId);

}