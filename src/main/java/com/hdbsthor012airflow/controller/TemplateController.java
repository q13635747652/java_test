package com.hdbsthor012airflow.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hdbsthor012airflow.interfaces.TemplateServiceIf;
import com.hdbsthor012airflow.util.BaseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 合同变更单据审核
 *
 * @author yj
 */
@RestController
@RequestMapping("/TemplateController")
public class TemplateController {
    @Autowired
    private TemplateServiceIf templateServiceIf ;
    @Operation(summary = "合同模板填充", description = "合同模板填充")
    @RequestMapping(value = "/buildWord", method = RequestMethod.POST)
    @ResponseBody
    public String doContractChangeAudit(
            @RequestBody @Parameter(description = "传入参数JSON格式", required = true) String params
    ) throws Exception {
        JSONObject jsonObject = JSONUtil.parseObj(params);
        if (!BaseUtil.Base_HasValue(jsonObject.getStr("documentsId")))
        {
            throw new Exception ("params->documentsId不能为空");
        }
        if (!BaseUtil.Base_HasValue(jsonObject.getStr("wordConfigId")))
        {
            throw new Exception ("params->wordConfigId不能为空");
        }
        if (!BaseUtil.Base_HasValue(jsonObject.getStr("nacosPF")))
        {
            throw new Exception ("params->nacosPF不能为空");
        }
        if (!BaseUtil.Base_HasValue(jsonObject.getStr("userId")))
        {
            throw new Exception ("params->userId不能为空");
        }

        return templateServiceIf.buildWord(params);
    }

}
