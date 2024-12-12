package com.hdbsthor012airflow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author susir
 * @email sym@Boozinfo.com
 * @date 2021-06-09 17:47:56
 */
@Data
@Entity
public class SystemVersion implements Serializable {

    @Id
    private String id;
    //版本号
    private String versionNo;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date versionTime;
    //
    private String comId;

}
