package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.diyannotation.FiledNameIs;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 企业报价表-模型
 */
@Data
public class AeaImUnitBidding implements Serializable {

    private static final long serialVersionUID = 1L;

    private String unitBiddingId;//主键
    @FiledNameIs(filedValue = "项目发布id")
    private String projPurchaseId; //项目发布id
    @FiledNameIs(filedValue = "竞价单位id")
    private String unitInfoId; //竞价单位id
    @FiledNameIs(filedValue = "价格（万元）")
    private String realPrice; //价格（万元）
    @FiledNameIs(filedValue = "是否中标")
    private String isWonBid; //是否中标：1 已中标，0 未中标
    @FiledNameIs(filedValue = "审核状态")
    private String auditFlag; //审核状态：0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成,6 已过时
    @FiledNameIs(filedValue = "是否删除")
    private String isDelete; //是否删除 1 已删除，0 未删除
    @FiledNameIs(filedValue = "创建人")
    private String creater; //创建人
    @FiledNameIs(filedValue = "创建时间")
    private Date createTime; //创建时间
    @FiledNameIs(filedValue = "修改人")
    private String modifier; //修改人
    @FiledNameIs(filedValue = "修改时间")
    private Date modifyTime; //修改时间
    @FiledNameIs(filedValue = "企业联系人id")
    private String linkmanInfoId; //企业联系人id
    @FiledNameIs(filedValue = "备注")
    private String memo; //备注
    @FiledNameIs(filedValue = "中介机构服务项目id")
    private String unitServiceId; //中介机构服务项目id
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date biddingTime; // (中标时间)
    private String isCancelSignup; // (是否取消报名：1 已取消，0  未取消)

    private String isUploadContract;//合同是否已上传：1 是，0 否
    private String isUploadResult;//服务结果是否已上传：1 是，0 否
    private String isEvaluate;//是否已评价：1 是， 0 否
    private String rootOrgId;//根组织ID


    //=======非表字段
    private String applicant;
    private String itemName;
    private String projName;
    private String projInfoId;
    private String biddingType;
    private String basePrice;
    private String highestPrice;

    private Date expirationDate; // 截止时间

    private String linkmanName;
    private String linkmanMail;
    private String linkmanMobilePhone;
    private List<AeaImBiddingEmployees> biddingEmployeesList;
    private Date choiceImunitTime;// 选取中介时间
    private Date serviceStartTime;// 服务开始时间
    private Date serviceEndTime;// 服务结束时间
    private String contractFlag;// 合同状态
    private String resultFlag;// 服务结果状态
    private String purchaseAuditFlag;// 采购需求状态
    private String contractId;// 合同ID
    private String isOwnerUpload;// (是否业主上传： 1 是， 0 否（中介机构上传）)
    private String contractPrice;// 合同金额
    private String serviceResultId;// 服务结果ID

    public AeaImUnitBidding(String isWonBid,String isDelete,String auditFlag,String rootOrgId){
        this.isWonBid=isWonBid;
        this.isDelete=isDelete;
        this.auditFlag=auditFlag;
        this.rootOrgId=rootOrgId;
    }


    public AeaImUnitBidding() {

    }
}
