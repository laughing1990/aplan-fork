package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@ApiModel("单位信息")
public class AeaUnitInfoVo {
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
    private java.lang.String unitType; // (单位类型，来自于数据字典，包括：1 建设单位、2 施工单位、3 勘察单位、4 设计单位、5 监理单位、6 代建单位、7 经办单位、8 其他、9审图机构)
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

    private String isBlack;//是否黑名单
    private String redblackId;//红黑名单主键

    @ApiModelProperty(value = "联系人列表", dataType = "list")
    private List<AeaLinkmanInfo> aeaLinkmanInfoList;

    @ApiModelProperty(value = "人员设置列表", dataType = "list")
    private List<LinkmanTypeVo> linkmanTypes;

    //@ApiModelProperty(value = "选中的联系人")
    //private AeaLinkmanInfo selectedLinkman;

    private java.lang.String loginName; // (单位登录名)

    @ApiModelProperty(value = "当前单位选择的联系人ID")
    private String linkmanInfoId;//联系人

    public static AeaUnitInfoVo build(AeaUnitInfo aeaUnitInfo) {
        AeaUnitInfoVo aeaUnitInfoVo = new AeaUnitInfoVo();
        BeanUtils.copyProperties(aeaUnitInfo, aeaUnitInfoVo);
        return aeaUnitInfoVo;
    }

    public static AeaUnitInfo returnForm(AeaUnitInfoVo aeaUnitInfoVo) {
        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        BeanUtils.copyProperties(aeaUnitInfoVo, aeaUnitInfo);
        return aeaUnitInfo;
    }
}
