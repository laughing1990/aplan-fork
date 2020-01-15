package com.augurit.aplanmis.mall.guide.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("部门确认审批事项意见VO")
public class RestGuideApproveItemInfoVo {
    @ApiModelProperty("事项名称")
    private String iteminstName;
    @ApiModelProperty("审批部门")
    private String orgName;
    @ApiModelProperty("部门辅导意见")
    private String guideComments;
    @ApiModelProperty("处理时间")
    private Date startTime;


}
