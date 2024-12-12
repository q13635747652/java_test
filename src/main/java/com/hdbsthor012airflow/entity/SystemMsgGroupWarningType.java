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
@Table(name = "system_msg_group_warning_type")
public class SystemMsgGroupWarningType implements Serializable {


    // 主键
    @Id
    @Column(nullable = false, name = "ID", length = 32)
    private String id;

    // 组ID
    @Column(nullable = false, name = "GROUP_ID", length = 32)
    private String groupId;

    // 组名称
    @Column(nullable = false, name = "GROUP_NAME", length = 100)
    private String groupName;

    // 预警类型ID
    @Column(nullable = false, name = "TYPE_ID", length = 32)
    private String typeId;

    // 预警类型名称
    @Column(nullable = false, name = "TYPE_NAME", length = 100)
    private String typeName;

    // 修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(nullable = false, name = "U_TIME", columnDefinition = "datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

}