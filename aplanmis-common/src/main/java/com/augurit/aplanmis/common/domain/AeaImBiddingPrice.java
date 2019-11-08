package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.diyannotation.FiledNameIs;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 企业报价金额表-模型
 */
@Data
public class AeaImBiddingPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    private String biddingPriceId;//主键
    @FiledNameIs(filedValue = "企业报价信息ID")
    private String unitBiddingId; //企业报价信息ID
    @FiledNameIs(filedValue = "竞价金额（元）")
    private String price; //竞价金额（元）
    @FiledNameIs(filedValue = "是否选中")
    private String isChoice; //是否选中
    @FiledNameIs(filedValue = "是否删除")
    private String isDelete; //是否删除 1 已删除，0 未删除
    @FiledNameIs(filedValue = "创建人")
    private String creater; //创建人
    @FiledNameIs(filedValue = "创建时间")
    private Date createTime; //创建时间

    public AeaImBiddingPrice() {
    }

    private String rootOrgId;//根组织ID

    public AeaImBiddingPrice(String unitBiddingId, String price, String isChoice, String isDelete, String creater, String rootOrgId) {
        this.biddingPriceId = UUID.randomUUID().toString();
        this.unitBiddingId = unitBiddingId;
        this.price = price;
        this.isChoice = isChoice;
        this.isDelete = isDelete;
        this.creater = creater;
        this.createTime = new Date();
        this.rootOrgId = rootOrgId;
    }
}
