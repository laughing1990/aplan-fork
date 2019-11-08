package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.diyannotation.FiledNameIs;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业报价联系人表-模型
 */
@Data
public class AeaImBiddingEmployees implements Serializable {

    private static final long serialVersionUID = 1L;

    private String biddingEmployeesId;//主键
    @FiledNameIs(filedValue = "企业报价信息ID")
    private String unitBiddingId; //企业报价信息ID
    @FiledNameIs(filedValue = "从业人员ID")
    private String serviceLinkmanId; //从业人员ID
    @FiledNameIs(filedValue = "是否删除")
    private String isDelete; //是否删除 1 已删除，0 未删除
    @FiledNameIs(filedValue = "创建人")
    private String creater; //创建人
    @FiledNameIs(filedValue = "创建时间")
    private Date createTime; //创建时间

    private String linkmanName;
    private String linkmanMail;
    private String linkmanMobilePhone;

}
