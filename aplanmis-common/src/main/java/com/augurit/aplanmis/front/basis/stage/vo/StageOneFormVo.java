package com.augurit.aplanmis.front.basis.stage.vo;

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

    @ApiModelProperty(value = "表单排序", dataType = "long")
    private Long sortNo;

    @ApiModelProperty(value = "是否启用", dataType = "string")
    private String isActive;

    @ApiModelProperty(value = " 根组织", dataType = "string")
    private String rootOrgId;

    @ApiModelProperty(value = "是否已填, 0: 未填, 1: 已填", dataType = "string")
    private String filledOut = "0";

    public static StageOneFormVo from(AeaOneform aeaOneform) {

        StageOneFormVo stageOneFormVo = new StageOneFormVo();
        stageOneFormVo.setParStageId(aeaOneform.getParStageId());
        stageOneFormVo.setOneformId(aeaOneform.getOneformId());
        stageOneFormVo.setOneformName(aeaOneform.getOneformName());
        stageOneFormVo.setOneformDesc(aeaOneform.getOneformDesc());
        stageOneFormVo.setOneformUrl(aeaOneform.getOneformUrl());
        stageOneFormVo.setSortNo(aeaOneform.getSortNo());
        stageOneFormVo.setIsActive(aeaOneform.getIsActive());
        stageOneFormVo.setRootOrgId(aeaOneform.getRootOrgId());
        return stageOneFormVo;
    }

}
