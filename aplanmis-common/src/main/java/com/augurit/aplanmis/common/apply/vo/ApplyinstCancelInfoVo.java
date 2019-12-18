package com.augurit.aplanmis.common.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("撤件申请表单信息")
public class ApplyinstCancelInfoVo implements Serializable {

    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;
    @ApiModelProperty(value = "撤件原因")
    private String applyReason;
    @ApiModelProperty(value = "申请人linkmanInfoId")
    private String applyUserId;
    @ApiModelProperty(value = "附件ID")
    private String attId;
    @ApiModelProperty(value = "申请人")
    private String applyUserName;
    @ApiModelProperty(value = "申请人身份证")
    private String applyUserIdnumber;
    @ApiModelProperty(value = "申请人电话")
    private String applyUserPhone;

}
