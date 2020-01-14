package com.augurit.aplanmis.front.subject.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("子工程审核vo")
@Getter
@Setter
public class SplitedProjApproveVo {

    @ApiModelProperty(value = "子工程拆分记录id")
    private String applySplitId;

    @ApiModelProperty(value = "子工程审核通过后需要用子工程进行申报")
    private String guideId;

    @ApiModelProperty(value = "原因描述")
    private String reason;

    @ApiModelProperty(value = "是否通过", notes = "true: 通过， false：不通过")
    private boolean passed;

    @ApiModelProperty(value = "是否需要发送短信", notes = "true: 是, false: 否")
    private boolean needSms;
}
