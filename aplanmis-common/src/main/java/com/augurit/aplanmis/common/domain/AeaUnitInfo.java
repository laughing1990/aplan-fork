package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * 单位表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:40:48</li>
 * </ul>
 */
@Data
@ApiModel("单位信息")
public class AeaUnitInfo implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private java.lang.String unitInfoId; // (主键)
    @ApiModelProperty("单位名称")
    private java.lang.String applicant; // (项目（法人）单位名称)

    @Deprecated
    private java.lang.String idtype; // (单位证照类型，来自于数据字典，包括：工商登记号、统一社会信用代码、组织机构代码（企业法人、社会团体法人）、组织机构代码（机关、事业单位）)

    @Deprecated
    @ApiModelProperty("企业注册号")
    private java.lang.String idcard; // (单位证照号码)

    @ApiModelProperty(value = "工商登记号", dataType = "string")
    private String induCommRegNum;
    @ApiModelProperty(value = "组织机构代码", dataType = "string")
    private String organizationalCode;
    @ApiModelProperty(value = "统一社会信用代码", dataType = "string")
    private String unifiedSocialCreditCode;
    @ApiModelProperty(value = "纳税人识别号", dataType = "string")
    private String taxpayerNum;
    @ApiModelProperty(value = "信用标记码", dataType = "string")
    private String creditFlagNum;
    private java.lang.String contact; // (联系人姓名)
    private java.lang.String mobile; // (联系人手机号码)
    private java.lang.String email; // (联系人电子邮箱)
    private java.lang.String applicantDistrict; // (行政区（园区）)
    private java.lang.String applicantDetailSite; // (具体地址)
    private java.lang.String idrepresentative; // (法人姓名)
    private java.lang.String idmobile; // (法人手机号码)
    private java.lang.String idno; // (法人身份证号码)
    private java.lang.String isDeleted; // (逻辑删除标记。0表示正常记录，1表示已删除记录。)
    private java.lang.String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private java.lang.String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private java.lang.String isPrimaryUnit; // (是否主单位，0表示副单位，1表示主单位)
    private java.lang.String parentId; // (父ID)
    private java.lang.String unitInfoSeq; // (序列)
    private java.lang.String loginName; // (单位登录名)
    private java.lang.String unitType; // (单位类型，来自于数据字典，包括：1 建设单位、2 施工单位、3 勘察单位、4 设计单位、5 监理单位、6 代建单位、7 经办单位、8 其他、9审图机构)
    private java.lang.String loginPwd; // (登录密码)
    private java.lang.String managementScope; // (经营范围)
    private java.lang.String registeredCapital; // (注册资本)
    private java.lang.String registerAuthority; // (注册登记机关)
    private java.lang.String unitNature; // (单位性质：1 企业，2 事业单位，3 社会组织)
    private java.lang.String postalCode; // (邮政编码)
    private java.lang.String isImUnit; // (是否为中介机构：1 是，0 否)
    private java.lang.String imgUrl; // (图片地址)
    private java.lang.String isOwnerUnit; // (是否为业主单位：1 是，0 否)
    private String rootOrgId;//根组织ID
    private String unitProjUnitType;// 项目的单位类型
    private String isGd;//是否属于省内企业
    private String auditFlag;//审核状态:0 审核失败，1 已审核，2 审核中
    //非表字段
    private String keyword;
    //非表字段
    private String isAdministrators;//是否管理员
    private String isOwner;//是否业主单位，0表示代建单位，1表示业主单位

    private String projInfoId;  //接收值用
    private String unitProjId;  //接收值用

    private List<BscAttFileAndDir> fileList;//证照列表-全局单位库使用

    private List<AeaLinkmanInfo> linkmanInfoList;//单位联系人-全局项目库使用
    private String linkmanInfoId;  //联系人ID-全局项目库使用
    private String linkmanCertNo;  //联系人证件号-全局项目库使用
    private String linkmanName;  //联系人姓名-全局项目库使用
    private String linkmanMobilePhone;  //联系人手机号-全局项目库使用
    private String linkmanMail;  //联系人邮箱-全局项目库使用

    private String price;
    private String applyinstId;
    private String isBlack;//是否黑名单
    private String redblackId;//红黑名单主键
    private List<AeaLinkmanInfo> aeaLinkmanInfoList;

    private String projLinkmanId ;//项目单位负责表主键
    private String projectLeader ;//项目负责人姓名
    private String projectLeaderId;//项目负责人ID
    private String projectLeaderCertNum;//项目负责人 ID Card


    public AeaUnitInfo() {

    }

    public AeaUnitInfo(String isImUnit, String isDeleted) {
        this.isImUnit = isImUnit;
        this.isDeleted = isDeleted;
    }

}
