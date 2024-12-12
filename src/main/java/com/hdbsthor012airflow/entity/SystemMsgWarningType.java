package com.hdbsthor012airflow.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "system_msg_warning_type")
public class SystemMsgWarningType implements Serializable {

    // 主键
    @Id
    @Column(nullable = false, name = "ID", length = 32)
    private String id;

    // 预警类型名称
    @Column(nullable = false, name = "TYPE_NAME", length = 100, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private String typeName;

    // 流程引擎过滤管道
    @Column(nullable = false, name = "CAMUNDA_ID", length = 100)
    private String camundaId;

    // 幻视 DAG 编号
    @Column(name = "DAG_ID", length = 100)
    private String dagId;

    // 备注信息
    @Column(nullable = false, name = "REMARK", length = 255, columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String remark;

    // 更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(nullable = false, name = "U_TIME", columnDefinition = "datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    // 删除
    @Column(nullable = false, name = "U_DELETE", columnDefinition = "int unsigned default 1")
    private int uDelete;
    
}
