package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 * 企业中标服务项目及服务评价实体
 */
@Data
public class AeaUnitBiddingAndEvaluation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String unitBiddingId;
    private String unitServiceId;
    private String serviceTypeId;
    /**
     * 项目发布ID
     */
    private String projPurchaseId;
    /**
     * 竞价单位ID
     */
    private String unitInfoId;
    /**
     * 价格（万元）
     */
    private String realPrice;
    /**
     * 是否中标：1 已中标，0 未中标
     */
    private String isWonBid;
    /**
     * 审核状态：0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成,6 已过时
     */
    private String auditFlag;
    /**
     * 备注
     */
    private String memo;
    /**
     * 项目名称
     */
    private String projName;
    /**项目采购单位ID*/
    private String publishUnitInfoId;
    /**
     * 项目发布单位
     */
    private String applicant;
    /**服务质量*/
    private String serviceQuality;
    /**服务时效*/
    private String servicePrescription;
    /**服务态度*/
    private String serviceAttitude;
    /**服务收费*/
    private String serviceCharge;
    /**服务规范*/
    private String serviceStandard;
    /**综合评价*/
    private String compEvaluation;
    /**平均评价*/
    private Double avgCompEvaluation;
    /***/
    private String biddingType;

}
