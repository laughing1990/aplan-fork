package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 法律救济-模型
 */
@Data
public class AeaItemLegalRemedy implements Serializable {
    private static final long serialVersionUID = 1L;

    private String legalRemedyId; // (法律救济ID)
    private String itemBasicId; // (事项ID)
    private String winComplaint; // (窗口投诉)
    private String telComplaint; // (电话投诉)
    private String onlineComplaint; // (网上投诉)
    private String emailComplaint; // (电子邮件投诉)
    private String letterComplaint; // (信函投诉)
    private String replyTimeForm; // (回复时限及形式)
    private String admReconDept; // (部门（行政复议）)
    private String admReconAddress; // (地址（行政复议）)
    private String admReconTel; // (电话（行政复议）)
    private String admReconNetAddress; // (网址（行政复议）)
    private String admProceDept; // (部门（行政诉讼）)
    private String admProceTel; // (电话（行政诉讼）)
    private String admProceAddress; // (地址（行政诉讼）)
    private String admProceNetAddress; // (网址（行政诉讼）)
    private String dataId; // (省的数据ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)

    //非表字段
    private String itemName; // (事项名称)
    private String keyword;  // (查询关键词)
}
