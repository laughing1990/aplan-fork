package com.augurit.aplanmis.rest.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaOneform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("一张表单vo")
public class StageOneFormVo {

    @ApiModelProperty(value = "阶段id", dataType = "string")
    private String parStageId;

    @ApiModelProperty(value = "表单id", dataType = "string")
    private String oneformId;

    @ApiModelProperty(value = "表单名称", dataType = "string")
    private String oneformName;

    @ApiModelProperty(value = "表单描述", dataType = "string")
    private String oneformDesc;

    @ApiModelProperty(value = "表单地址", dataType = "string")
    private String oneformUrl;

    @ApiModelProperty(value = "表单排序")
    private Long sortNo;

    @ApiModelProperty(value = "是否启用", dataType = "string")
    private String isActive;

    @ApiModelProperty(value = " 根组织", dataType = "string")
    private String rootOrgId;

    @ApiModelProperty(value = "是否已填, 0: 未填, 1: 已填", dataType = "string")
    private String filledOut = "0";

    public static StageOneFormVo from(AeaOneform aeaParOneform) {
        StageOneFormVo stageOneFormVo = new StageOneFormVo();
        stageOneFormVo.setParStageId(aeaParOneform.getParStageId());
        stageOneFormVo.setOneformId(aeaParOneform.getOneformId());
        stageOneFormVo.setOneformName(aeaParOneform.getOneformName());
        stageOneFormVo.setOneformDesc(aeaParOneform.getOneformDesc());
        stageOneFormVo.setOneformUrl(aeaParOneform.getOneformUrl());
        stageOneFormVo.setSortNo(aeaParOneform.getSortNo());
        stageOneFormVo.setIsActive(aeaParOneform.getIsActive());
        stageOneFormVo.setRootOrgId(aeaParOneform.getRootOrgId());
        return stageOneFormVo;
    }

}
