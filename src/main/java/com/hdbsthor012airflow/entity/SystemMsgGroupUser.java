package com.hdbsthor012airflow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "system_msg_group_user")
@Schema(description = "用户组用户表")
public class SystemMsgGroupUser implements Serializable {


    // 主键ID
    @Id
    @Column(nullable = false, name = "ID", length = 32)
    @Schema(description = "主键ID")
    private String ID;

    // 用户组ID
    @Column(nullable = false, name = "GROUP_ID", length = 32)
    @Schema(description = "用户组ID")
    private String groupId;

    // 组名称
    @Column(nullable = false, name = "GROUP_NAME", length = 100)
    @Schema(description = "组名称")
    private String groupName;
    
    // 组成员ID
    @Column(nullable = false, name = "USER_ID", length = 32)
    @Schema(description = "组成员ID")
    private String userId;

    // 删除标记 默认1
    @Column(nullable = false, name = "U_DELETE", columnDefinition = "int unsigned default 1")
    @Schema(description = "删除标记 默认1")
    private Integer uDelete;

    // 修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(nullable = false, name = "U_TIME", columnDefinition = "datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Schema(description = "修改时间")
    private LocalDateTime uTime;

}
