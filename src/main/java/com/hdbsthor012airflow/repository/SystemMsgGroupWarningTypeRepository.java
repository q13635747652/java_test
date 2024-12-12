package com.hdbsthor012airflow.repository;

import com.hdbsthor012airflow.entity.SystemMsgGroupWarningType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemMsgGroupWarningTypeRepository extends JpaRepository<SystemMsgGroupWarningType, String> {

    /**
     * 根据组ID查询组预警类型列表。
     *
     * @param groupId 组ID
     * @return 符合条件的组预警类型列表
     */
    List<SystemMsgGroupWarningType> findByGroupId(String groupId);

    /**
     * 根据预警类型ID列表查询组预警类型列表。
     *
     * @param typeIdList 预警类型ID列表
     * @return 符合条件的组预警类型列表
     */
    List<SystemMsgGroupWarningType> findByTypeIdIn(List<String> typeIdList);

    /**
     * 根据预警类型ID查询组预警类型列表。
     *
     * @param typeId 预警类型ID
     * @return 符合条件的组预警类型列表
     */
    List<SystemMsgGroupWarningType> findByTypeId(String typeId);

}

