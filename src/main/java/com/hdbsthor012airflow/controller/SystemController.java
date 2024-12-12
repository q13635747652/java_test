package com.hdbsthor012airflow.controller;

import com.hdbsthor012airflow.interfaces.SystemVersionIf;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/AppApi/SystemController")
public class SystemController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment environment;

    @Autowired
    private SystemVersionIf systemVersionIf;

    @Operation(summary = "获取服务器时间", description = "获取服务器时间")
    @PostMapping("/getServerDate")
    public Date getServerDate(
            @RequestParam(name = "PF", required = true)
            @Parameter(description = "数据源键", example = "assetsDataResources", required = true)
            String PF
    ) {
        return new Date();
    }

    @Operation(summary = "获取服务器环境", description = "获取服务器环境")
    @PostMapping("/getEnvironment")
    public String getEnvironment(
            @RequestParam(name = "PF", required = true)
            @Parameter(description = "数据源键", example = "assetsDataResources", required = true)
            String PF
    ) {
        String springProfilesActive = environment.getProperty("spring.profiles.active");
        return springProfilesActive;
    }

    @Operation(summary = "获取系统版本号", description = "获取系统版本号")
    @PostMapping("/getSystemVersion")
    public String getSystemVersion(
            @RequestParam(name = "PF", required = true)
            @Parameter(description = "数据源键", example = "assetsDataResources", required = true)
            String PF
    ) {
        return systemVersionIf.getSystemVersion();
    }

    @Operation(summary = "保存系统版本号", description = "保存系统版本号")
    @PostMapping("/saveSystemVersion/{versionNo}/{comId}")
    public String saveSystemVersion(
            @PathVariable(name = "versionNo")
            @Parameter(description = "要保存的系统版本号", example = "1.0.0", required = true)
            String versionNo,

            @PathVariable(name = "comId")
            @Parameter(description = "公司ID", example = "123456", required = true)
            String comId,

            @RequestParam(name = "PF", required = true)
            @Parameter(description = "数据源键", example = "assetsDataResources", required = true)
            String PF
    ) {
        return systemVersionIf.saveSystemVersion(versionNo, comId);
    }
}