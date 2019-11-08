package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author tiantian
 * @date 2019/6/20
 */
@Data
@ApiModel("服务的项目")
public class ServiceProjInfoVo {
    @ApiModelProperty(value = "项目需求ID")
    private String projPurchaseId;
    @ApiModelProperty(value = "项目名称")
    private String projName;
    @ApiModelProperty(value = "金额")
    private String price;
    @ApiModelProperty(value = "服务开始时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date serviceStartTime;

    @ApiModelProperty(value = "服务结束时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date serviceEndTime;

    @ApiModelProperty(value = "项目状态 0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效")
    private String projAuditFlag;

    @ApiModelProperty(value = "服务合同ID")
    private String contractId;

    @ApiModelProperty(value = "服务结果ID")
    private String serviceResultId;

    @ApiModelProperty(value = "企业报价信息ID")
    private String unitBiddingId;

    @ApiModelProperty(value = "服务结果状态 服务结果状态：0 待确定，1 已确定 ，2 已退回")
    private String resultAuditFlag;
}
