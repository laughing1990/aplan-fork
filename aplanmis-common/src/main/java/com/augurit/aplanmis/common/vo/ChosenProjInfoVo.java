package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author tiantian
 * @date 2019/6/19
 */
@Data
@ApiModel("中选的项目")
public class ChosenProjInfoVo {

    @ApiModelProperty(value = "项目需求ID")
    private String projPurchaseId;
    @ApiModelProperty(value = "项目名称")
    private String projName;
    @ApiModelProperty(value = "金额")
    private String price;
    @ApiModelProperty(value = "中选时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date biddingTime;
    @ApiModelProperty(value = "项目状态 0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效")
    private String projAuditFlag;
    @ApiModelProperty(value = "合同状态 0 待确定，1 已确定 ，2 审核失败，3：审核中")
    private String contractAuditFlag;
    @ApiModelProperty(value = "服务合同ID")
    private String contractId;


}
