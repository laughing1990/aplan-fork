package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:43:27</li>
 * </ul>
 */
@Data
public class AeaProjLinkman implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String projLinkmanId; // ()
    private String projInfoId; // (项目信息ID)
    private String linkmanInfoId; // (联系人信息ID)
    private String type; // (类型：link 联系人，apply 申请人)
    private String applyinstId; // (申请实例ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)

    // 扩展字段
    private String linkmanType;
    private String linkmanName;
    private String linkmanMobilePhone;
    private String linkmanCertNo;
    private String linkmanMail;

    private String isBlack;//是否黑名单
    private String redblackId;//

    public AeaProjLinkman() {
    }

    public AeaProjLinkman(String projInfoId, String linkmanInfoId, String type, String applyinstId, String creater) {
        this.projLinkmanId = UUID.randomUUID().toString();
        this.projInfoId = projInfoId;
        this.linkmanInfoId = linkmanInfoId;
        this.type = type;
        this.applyinstId = applyinstId;
        this.creater = creater;
        this.createTime = new Date();
    }
}
