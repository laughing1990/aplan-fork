package com.augurit.aplanmis.front.approve.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/***
 * @description 流程类型判断
 * @author mohaoqi
 * @date 2019/7/26 0026
 ***/
@Data
@ApiModel("流程类型")
public class ActFlowParamVo {

    @ApiModelProperty(value = "业务流程应用配置编号", required = true, dataType="string")
    private String appCode;

    @ApiModelProperty(value = "业务流程应用配置名称", required = true, dataType="string")
    private String appComment;

    @ApiModelProperty(value = "所属流程定义KEY", required = true, dataType="string")
    private String procdefKey;

    @ApiModelProperty(value = "表名称", required = false, dataType="string")
    private String tableName;

    @ApiModelProperty(value = "主键名称", required = false, dataType="string")
    private String pkName;

    public static ActFlowParamVo toActFlowParamVo(Map<String,Object> actFlowParam){
        ActFlowParamVo actFlowParamVo = new ActFlowParamVo();
        actFlowParamVo.setAppCode((String) actFlowParam.get("appCode"));
        actFlowParamVo.setAppComment((String) actFlowParam.get("appComment"));
        actFlowParamVo.setProcdefKey((String) actFlowParam.get("procdefKey"));
        actFlowParamVo.setTableName((String) actFlowParam.get("_tableName"));
        actFlowParamVo.setPkName((String) actFlowParam.get("_pkName"));
        return actFlowParamVo;
    }

}
