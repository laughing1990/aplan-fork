package com.augurit.aplanmis.province.vo;

import com.augurit.aplanmis.common.domain.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ApiModel(value = "省平台申报项目信息")
public class ProviceBaseInfoVo {
    private IteminstInfoVo iteminstInfo;
    private ProjInfoVo projInfo;
    private LinkmanVo linkman;
    private ApplyDataVo applyData;


    @Data
    @ApiModel(value = "事项实例信息")
    public class IteminstInfoVo {

        private String iteminstCode;
        private String iteminstName;

        private String approveOrgId;
        private String approveOrgName;
        private String iteminstState;
        private Double dueNum;
        private Date dueDate;
        private String bjType;
        private String isTiming;
        private Date startTime;
        private Date modifyTime;
        private Date registerTime;
        private Date acceptTime;
        private Date endTime; // (结束时间)
        @ApiModelProperty(value = "办件类型")
        private String itemProperty;

    }

    @Data
    @ApiModel(value = "")
    public class ProjInfoVo {
        private String projName;
        private String localCode;
        private String centralCode;
        private String applicant;
        private String idCard;
    }

    @Data
    @ApiModel(value = "")
    public class LinkmanVo {
        private String linkmanName;
        private String linkmanMobilePhone;
        private String linkmanMail;
        private String linkmanCertNo;
    }

    @Data
    @ApiModel
    public class ApplyDataVo {

        private String applyinstId;
        private String applyinstCode;
        private String applyinstSource;
        private String isSeriesApprove;
        private String applyinstMemo;
        private String applyinstState;

        private Date createTime;
        private Date modifyTime;
        @ApiModelProperty(value = "受理时间")
        private Date acceptTime;
        @ApiModelProperty(value = "事项审批状态")
        private String iteminstState;
    }

    public ProviceBaseInfoVo() {
        this.linkman = new LinkmanVo();
        this.projInfo = new ProjInfoVo();
        this.iteminstInfo = new IteminstInfoVo();
        this.applyData = new ApplyDataVo();
    }

    public void changLinkmanToVo(ProviceBaseInfoVo vo, AeaLinkmanInfo linkmanInfo) {
        if (null != vo && null != linkmanInfo) {
            BeanUtils.copyProperties(linkmanInfo, vo.getLinkman());
        }
    }

    public void changeAppinstToVo(ProviceBaseInfoVo vo, AeaHiApplyinst aeaHiApplyinst) {
        if (vo != null && null != aeaHiApplyinst) {
            BeanUtils.copyProperties(aeaHiApplyinst, vo.getApplyData());
        }
    }


    public void changeProjInfoToVo(ProviceBaseInfoVo vo, List<AeaProjInfo> applyProj, List<AeaUnitInfo> unitInfo) {
        if (null == vo) return;
        ProjInfoVo projInfo = vo.getProjInfo();
        if (null != applyProj) {
            String projName = applyProj.stream().map(AeaProjInfo::getProjName).collect(Collectors.joining(","));
            String localCode = applyProj.stream().map(AeaProjInfo::getLocalCode).collect(Collectors.joining(","));
            String centralCode = applyProj.stream().map(AeaProjInfo::getCentralCode).collect(Collectors.joining(","));
            String applicant = applyProj.stream().map(AeaProjInfo::getApplicant).collect(Collectors.joining(","));
            String idCard = applyProj.stream().map(AeaProjInfo::getIdCard).collect(Collectors.joining(","));


            projInfo.setProjName(projName);
            projInfo.setApplicant(applicant);
            projInfo.setCentralCode(centralCode);
            projInfo.setIdCard(idCard);
            projInfo.setLocalCode(localCode);
        }
        if (null != unitInfo) {
            String applicant = unitInfo.stream().map(AeaUnitInfo::getApplicant).collect(Collectors.joining(","));
            String unifiedSocialCreditCode = unitInfo.stream().map(AeaUnitInfo::getUnifiedSocialCreditCode).collect(Collectors.joining(","));
            projInfo.setApplicant(applicant);
            projInfo.setIdCard(unifiedSocialCreditCode);


        }
    }

    public void changeIteminstToVo(ProviceBaseInfoVo vo, AeaHiIteminst iteminst) {
        if (null != vo && null != iteminst) {
            IteminstInfoVo iteminstInfo = vo.getIteminstInfo();
            BeanUtils.copyProperties(iteminst, iteminstInfo);

        }
    }


}
