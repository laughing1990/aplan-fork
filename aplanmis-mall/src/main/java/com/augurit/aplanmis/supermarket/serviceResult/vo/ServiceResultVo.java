package com.augurit.aplanmis.supermarket.serviceResult.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("服务结果vo")
public class ServiceResultVo {

    @ApiModelProperty(value = "服务结果ID")
    private String serviceResultId;

    @ApiModelProperty(value = "上传时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date uploadTime;

    @ApiModelProperty(value = "结束时间")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTime;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "采购需求ID")
    @NotBlank
    private String projPurchaseId;

    private String auditFlag;

    @ApiModelProperty(value = "中介机构委托人")
    private String linkmanInfoId;

    @ApiModelProperty(value = "企业报价信息ID")
    @NotBlank
    private String unitBiddingId;

    @ApiModelProperty(value = "是否外部上传")
    private String isExternalUpload;
}
