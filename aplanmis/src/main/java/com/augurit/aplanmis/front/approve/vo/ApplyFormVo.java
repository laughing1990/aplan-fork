package com.augurit.aplanmis.front.approve.vo;

import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.commonInterface.FlowObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class ApplyFormVo {
    @ApiModelProperty(name = "applySubject", value = "申办主体：1 单位，0 个人")
    private String applySubject;

    @ApiModelProperty(name = "iteminstList", value = "事项列表")
    private List<AeaHiIteminst> iteminstList;

    @ApiModelProperty(name = "projInfoList", value = "项目列表")
    private List<AeaProjInfo> projInfoList;

    @ApiModelProperty(name = "unitInfoList", value = "单位列表-单位申报主体")
    private List<UnitInfoVo> unitInfoVoList;

    @ApiModelProperty(name = "linkmanVoList", value = "个人申报主体联系人列表")
    private List<LinkmanInfoVo> linkmanVoList;

    private LinkmanInfoVo linkmanInfoVo;
    private ApplyInfoVo applyInfoVo;

    public ApplyFormVo() {
        this.unitInfoVoList = new ArrayList<>();
        this.linkmanVoList = new ArrayList<>();
    }

    public List<LinkmanInfoVo> changeProjLinkman2vo(List<AeaProjLinkman> projLinkmanList) {
        List<LinkmanInfoVo> vo = new ArrayList<>();
        for (AeaProjLinkman man : projLinkmanList) {
            LinkmanInfoVo tempVo = new LinkmanInfoVo();
            BeanUtils.copyProperties(man, tempVo);
            vo.add(tempVo);
        }
        return vo;
    }

    public void changeLinkman2UnitVo(UnitInfoVo unitVo, AeaLinkmanInfo linkmanInfo) {
        unitVo.setLinkmanCertNo(linkmanInfo.getLinkmanCertNo());
        unitVo.setLinkmanMail(linkmanInfo.getLinkmanMail());
        unitVo.setLinkmanName(linkmanInfo.getLinkmanName());
        unitVo.setLinkmanMobilePhone(linkmanInfo.getLinkmanMobilePhone());
    }

    public ApplyFormVo.LinkmanInfoVo changeLinkman2vo(AeaLinkmanInfo linkmanInfo) {
        LinkmanInfoVo vo = new LinkmanInfoVo();
        if(linkmanInfo != null)
            BeanUtils.copyProperties(linkmanInfo, vo);
        return vo;
    }


    @Data
    @ApiModel(value = "联系人信息")
    public class LinkmanInfoVo {
        @ApiModelProperty(name = "linkmanInfo", value = "联系人ID")
        private String linkmanInfoId;
        @ApiModelProperty(name = "linkmanType", value = "联系人类型。c表示个人联系人，u表示企业联系人")
        private String linkmanType;

        @ApiModelProperty(name = "type", value = "类型：link 联系人，apply 申请人")
        private String type;

        @ApiModelProperty("是否失信人")
        private String isBlack;

        @ApiModelProperty(name = "linkmanName", value = "联系人")
        private String linkmanName;

        @ApiModelProperty(name = "linkmanMobilePhone", value = "电话")
        private String linkmanMobilePhone;

        @ApiModelProperty(name = "linkmanMail", value = "邮箱")
        private String linkmanMail;
        @ApiModelProperty(name = "linkmanCertNo", value = "证件号")
        private String linkmanCertNo;

    }

    public List<UnitInfoVo> changeUnit2vo(List<AeaUnitInfo> unitInfos) {
        List<UnitInfoVo> vo = new ArrayList<>();
        for (AeaUnitInfo unitInfo : unitInfos) {
            UnitInfoVo tempVo = new UnitInfoVo();
            BeanUtils.copyProperties(unitInfo, tempVo);
            vo.add(tempVo);

        }
        return vo;
    }

    @Data
    @ApiModel(value = "单位信息")
    public static class UnitInfoVo {
        @ApiModelProperty(name = "linkmanInfo", value = "主键")
        private String unitInfoId;
        @ApiModelProperty("单位名称")
        private String applicant; // (项目（法人）单位名称)
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
        @ApiModelProperty(value = "(行政区（园区）")
        private String applicantDistrict;

        @ApiModelProperty(value = "公司地址")
        private String applicantDetailSite;

        @ApiModelProperty(value = "法人姓名")
        private String idrepresentative;
        @ApiModelProperty(value = "法人手机号码")
        private String idmobile;
        @ApiModelProperty(value = "法人证件号")
        private String idno;

        @ApiModelProperty(value = "邮政编码")
        private String postalCode;

        @ApiModelProperty(value = "单位类型，来自于数据字典，包括：1 建设单位、2 施工单位、3 勘察单位、4 设计单位、5 监理单位、6 代建单位、7 经办单位、8 其他、9审图机构", dataType = "string")
        private String unitType;
        private String managementScope; // (经营范围)
        private String registeredCapital; // (注册资本)
        private String registerAuthority; // (注册登记机关)
        private String unitNature; // (单位性质：1 企业，2 事业单位，3 社会组织)
        @ApiModelProperty("是否黑名单，0表示红名单，1表示黑名单")
        private String isBlack;
        private String redblackId;//红黑名单主键
        private Boolean creditType;//信用类型，true表示正常，false 失信


        @ApiModelProperty(value = "联系人姓名", dataType = "string")
        private String linkmanName;
        @ApiModelProperty(value = "联系人证件号")
        private String linkmanCertNo;
        @ApiModelProperty(value = "联系人手机号码", dataType = "string")
        private String linkmanMobilePhone; // (手机号码)
        private String linkmanFax; // (传真)
        @ApiModelProperty(value = "联系人电子邮箱", dataType = "string")
        private String linkmanMail;

        List<LinkmanTypeVo> linkmanTypes;

    }

    public void setLinkmanTypes(UnitInfoVo unitInfoVo, List<AeaUnitProjLinkman> aeaUnitProjLinkmen) {
        List<LinkmanTypeVo> list = new ArrayList<>();
        if (null != aeaUnitProjLinkmen) {

            for (AeaUnitProjLinkman linkman : aeaUnitProjLinkmen) {
                LinkmanTypeVo vo = new LinkmanTypeVo();
                vo.setLinkmanInfoId(linkman.getLinkmanInfoId());
                vo.setLinkmanName(linkman.getLinkmanName());
                vo.setLinkmanType(linkman.getLinkmanType());
                list.add(vo);
            }
        }
        unitInfoVo.setLinkmanTypes(list);
    }

    @Data
    @ApiModel(value = "项目单位联系人类型")
    public class LinkmanTypeVo {
        @ApiModelProperty(value = "联系人类型，数据字典获取")
        private String linkmanType;
        @ApiModelProperty(value = "联系人ID")
        private String linkmanInfoId;
        @ApiModelProperty(value = "联系人名")
        private String linkmanName;
    }

    public ApplyInfoVo changeApply2vo(AeaHiApplyinst aeaHiApplyinst) {
        ApplyInfoVo vo = new ApplyInfoVo();
        BeanUtils.copyProperties(aeaHiApplyinst, vo);
        return vo;
    }


    @Data
    @ApiModel(value = "单位信息")
    public class ApplyInfoVo implements FlowObject {
        private String applyinstId; // (主键)
        private String applyinstCode; // (申请编号)
        private String applyinstSource; // (申报来源，net表示网上申报，win表示窗口申报)
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;//(申报时间)
        private String queueNo; // (排队号【当APPLY_SOURCE=win】)
        private String isSeriesApprove; // (是否串联审批，0表示并联审批，1表示串联审批)
        private String applyinstMemo; // (备注说明)
        private String applyinstState; // (申请状态)申报状态：0已接件未审核（适用于网厅）、1已接件并审核、2已受理、3待补全、4已补全、5不予受理、6已办结
        private String isDeleted; // (逻辑删除标记。0表示正常记录，1表示已删除记录。)
        private String linkmanInfoId; // (联系人ID)
        private String branchOrg; // (分局承办组织ID)
        private String applySubject; // (申办主体：1 单位，0 个人)
        private String isServiceCooperative; // (【当IS_SERIES_APPROVE=1】是否关联服务协同：1 是，0 否)
        private String isSmsSend; // (是否已出件，0表示未出件，1表示已出件)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private java.util.Date smsSendTime; // (最新出件时间)
        private String rootOrgId;//根组织ID
        private String themeName;//主题名称
        private String stageName;//阶段/辅线名称

        @Override
        public String getMasterRecordId() {
            return this.getApplyinstId();
        }
    }

}
