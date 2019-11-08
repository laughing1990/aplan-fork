package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    private java.lang.String projLinkmanId; // ()
    private java.lang.String projInfoId; // (项目信息ID)
    private java.lang.String linkmanInfoId; // (联系人信息ID)
    private java.lang.String type; // (类型：link 联系人，apply 申请人)
    private java.lang.String applyinstId; // (申请实例ID)
    private java.lang.String creater; // (创建人)
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
}
