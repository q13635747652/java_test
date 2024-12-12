package com.hdbsthor012airflow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Entity
@Table(name = "system_thor_log")
@Schema(description = " ")
public class SystemThorLog implements Serializable {


    // 主键
    @Id
    @Column(nullable = false, name = "ID")
    @Schema(description = "主键")
    private String id;

    // 地址
    @Column(nullable = true, name = "URL")
    @Schema(description = "地址")
    private String url;

    // 请求方法
    @Column(nullable = true, name = "HTTP_METHOD")
    @Schema(description = "请求方法")
    private String httpMethod;

    // IP地址
    @Column(nullable = true, name = "IP")
    @Schema(description = "IP地址")
    private String ip;

    // 类方法
    @Column(nullable = true, name = "CLASS_METHOD")
    @Schema(description = "类方法")
    private String classMethod;

    // 参数
    @Column(nullable = true, name = "ARGS")
    @Schema(description = "参数")
    private String args;

    // 添加时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(nullable = true, name = "ADD_DATE")
    @Schema(description = "添加时间")
    private LocalDateTime addDate;

    // 消耗时间
    @Column(nullable = true, name = "SPEND_TIME")
    @Schema(description = "消耗时间")
    private Long spendTime;

    // 报错信息
    @Column(nullable = true, name = "EXCETIOPN_INFO")
    @Schema(description = "报错信息")
    private String excetiopnInfo;

    // 报错标记
    @Column(nullable = true, name = "EXCEPTION_FLAG")
    @Schema(description = "报错标记")
    private Integer exceptionFlag;

}