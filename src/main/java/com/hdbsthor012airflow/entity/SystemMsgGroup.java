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
@Table(name = "system_msg_group")
@Schema(description = "信息用户组表")
public class SystemMsgGroup implements Serializable {


    // 组ID
    @Id
    @Column(nullable = false, name = "GROUP_ID", length = 32)
    @Schema(description = "组ID")
    private String groupId;

    // 组名称
    @Column(nullable = false, name = "GROUP_NAME", length = 200, columnDefinition = "VARCHAR(200) DEFAULT ''")
    @Schema(description = "组名称")
    private String groupName;

    // 排序
    @Column(nullable = false, name = "GROUP_ID_ORDER")
    @Schema(description = "排序")
    private int groupIdOrder;

    // 备注信息
    @Column(nullable = false, name = "REMARK", length = 255)
    @Schema(description = "备注信息")
    private String remark;

    // 更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(nullable = false, name = "U_TIME", columnDefinition = "datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Schema(description = "更新时间")
    private LocalDateTime uTime;

    // 删除
    @Column(nullable = false, name = "U_DELETE", columnDefinition = "int default 1")
    @Schema(description = "删除")
    private int uDelete;

}