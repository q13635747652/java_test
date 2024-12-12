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
@Table(name = "system_msg")
@Schema(description = "站内信表")
public class SystemMsg implements Serializable {


    // 主键ID（站内信内容ID）
    @Id
    @Column(nullable = false, name = "ID", length = 32)
    @Schema(description = "主键ID（站内信内容ID）")
    private String id;

    // 消息标题
    @Column(nullable = false, name = "TITLE", length = 255)
    @Schema(description = "消息标题")
    private String title;

    // 发送者用户ID
    @Column(nullable = false, name = "SEND_USER_ID", length = 32)
    @Schema(description = "发送者用户ID")
    private String sendUserId;

    // 接收者用户ID（0 表示所有人）
    @Column(nullable = false, name = "RECEIVE_USER_ID", length = 32)
    @Schema(description = "接收者用户ID（0 表示所有人）")
    private String receiveUserId;

    // 跳转路由URL
    @Column(nullable = true, name = "ROUTER_URL", length = 50)
    @Schema(description = "跳转路由URL")
    private String routerUrl;

    // 路由参数JSON格式
    @Column(nullable = true, name = "ROUTER_PARAM", length = 50)
    @Schema(description = "路由参数JSON格式，比如 {\"CONTRACTID\": \"000ECCA4A7575CD8DA2459D5DAB67A3A\"}")
    private String routerParam;

    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(nullable = false, name = "ADD_TIME", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Schema(description = "创建时间")
    private LocalDateTime addTime;

    // 阅读状态（0: 未读, 1: 已读）
    @Column(nullable = false, name = "HAS_READED", columnDefinition = "TINYINT DEFAULT 0")
    @Schema(description = "阅读状态（0: 未读, 1: 已读）")
    private Integer hasReaded;

    // 更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(nullable = false, name = "UPD_TIME", columnDefinition = "DATETIME ON UPDATE CURRENT_TIMESTAMP")
    @Schema(description = "更新时间")
    private LocalDateTime updTime;

}