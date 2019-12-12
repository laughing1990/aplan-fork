package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.framework.security.SecurityContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@ApiModel("中标单位与申报实例关联")
public class AeaImApplyinstUnitBidding {

    private String applyinstUnitBiddingId;
    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;
    @ApiModelProperty(value = "单位竞价ID")
    private String unitBiddingId;
    private String isDelete;
    private String creater;
    private Date createTime;
    private String modifier;
    private Date modifyTime;

    public AeaImApplyinstUnitBidding(String applyinstId, String unitBiddingId) {
        this.applyinstUnitBiddingId = UUID.randomUUID().toString();
        this.applyinstId = applyinstId;
        this.unitBiddingId = unitBiddingId;
        this.isDelete = "0";
        this.creater = SecurityContext.getCurrentUserName();
        this.createTime = new Date();
    }

    public void modifyOne(String applyinstId, String unitBiddingId) {
        this.applyinstId = applyinstId;
        this.unitBiddingId = unitBiddingId;
        this.modifier = SecurityContext.getCurrentUserName();
        this.modifyTime = new Date();
    }
}
