package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* 单位表-模型
*/
@Data
public class AeaCreditUnitInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private String creditUnitInfoId; // (主键)
    private String applicant; // (项目（法人）单位名称)
    private String idtype; // (单位证照类型，来自于数据字典，包括：工商登记号、统一社会信用代码、组织机构代码（企业法人、社会团体法人）、组织机构代码（机关、事业单位）)
    private String idcard; // (单位证照号码)
    private String contact; // (联系人姓名)
    private String mobile; // (联系人手机号码)
    private String email; // (联系人电子邮箱)
    private String applicantDistrict; // (行政区（园区）)
    private String applicantDetailSite; // (具体地址)
    private String idrepresentative; // (法人姓名)
    private String idmobile; // (法人手机号码)
    private String idno; // (法人身份证号码)
    private String isDeleted; // (逻辑删除标记。0表示正常记录，1表示已删除记录。)
    private String isPrimaryUnit; // (是否主单位，0表示副单位，1表示主单位)
    private String parentId; // (父ID)
    private String unitInfoSeq; // (序列)
    private String loginName; // (单位登录名)
    private String unitType; // (单位类型，来自于数据字典，包括：1 建设单位、2 施工单位、3 勘察单位、4 设计单位、5 监理单位、6 代建单位、7 经办单位、8 其他、9审图机构)
    private String loginPwd; // (登录密码)
    private String managementScope; // (经营范围)
    private String registeredCapital; // (注册资本)
    private String registerAuthority; // (注册登记机关)
    private String unitNature; // (单位性质：1 企业，2 事业单位，3 社会组织)
    private String postalCode; // (邮政编码)
    private String isImUnit; // (是否为中介机构：1 是，0 否)
    private String imgUrl; // (图片地址)
    private String isOwnerUnit; // (是否为业主单位：1 是，0 否)
    private String induCommRegNum; // (工商登记号)
    private String organizationalCode; // (组织机构代码)
    private String unifiedSocialCreditCode; // (统一社会信用代码)
    private String taxpayerNum; // (纳税人识别号)
    private String creditFlagNum; // (信用标记码)
    private String unitInfoId; // (所属单位ID)

    private String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; // (修改时间// )
    private String isSync; // (是否同步，0表示手工录入，1表示自动同步)
    private String syncSource; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date syncTime; // (同步时间)
    private String rootOrgId;

    private String keyword;

    //扩展字段
    private boolean hasChildren;
    private String globalUnitInfoName;
}
