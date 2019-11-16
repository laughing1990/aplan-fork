package com.augurit.aplanmis.common.service.receive.vo;

import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/***
 * @description 回执实体
 * @author mohaoqi
 * @date 2019/7/29 0029
 ***/
@Data
@ApiModel("回执信息实体vo")
public class ReceiveVo {

    /* AEA_HI_RECEIVE字段 start */

    @ApiModelProperty(value = "回执ID", required = true, dataType="string")
    private java.lang.String receiveId;
    @ApiModelProperty(value = "申请实例ID", required = true, dataType="string")
    private java.lang.String applyinstId; // (申请实例ID)
    @ApiModelProperty(value = "输出实例ID", required = true, dataType="string")
    private java.lang.String outinstId; // (输出实例ID)
    @ApiModelProperty(value = "领取人姓名", required = true, dataType="string")
    private java.lang.String receiveUserName; // (领取人姓名)
    @ApiModelProperty(value = "领取人证件号码", required = true, dataType="string")
    private java.lang.String receiveCertNo; // (领取人证件号码)
    @ApiModelProperty(value = "领取时间", required = true, dataType="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date receiveTime; // (领取时间)
    @ApiModelProperty(value = "领取登记部门所在的行政区域代码", required = true, dataType="string")
    private java.lang.String approveDeptRegion; // (领取登记部门所在的行政区域代码)
    @ApiModelProperty(value = "分发至下级部门的行政区域代码", required = true, dataType="string")
    private java.lang.String subDeptRegion; // (分发至下级部门的行政区域代码)
    @ApiModelProperty(value = "备注", required = true, dataType="string")
    private java.lang.String receiveMemo; // (备注)
    @ApiModelProperty(value = "创建人", required = true, dataType="string")
    private java.lang.String creater; // (创建人)
    @ApiModelProperty(value = "创建时间", required = true, dataType="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    @ApiModelProperty(value = "修改人", required = true, dataType="string")
    private java.lang.String modifier; // (修改人)
    @ApiModelProperty(value = "修改时间", required = true, dataType="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    @ApiModelProperty(value = "领证人手机号码", required = true, dataType="string")
    private java.lang.String receiveUserMobile; // (领证人手机号码)
    @ApiModelProperty(value = "收件地址", required = true, dataType="string")
    private java.lang.String serviceAddress;//收件地址
    @ApiModelProperty(value = "收据类型", required = true, dataType="string")
    private String receiptType;
    @ApiModelProperty(value = "文档编号", required = true, dataType="string")
    private String documentNum;
    @ApiModelProperty(value = "文档名称", required = true, dataType="string")
    private String documentName;
    @ApiModelProperty(value = "公司名称或法人名", required = true, dataType="string")
    private String applicant;//公司名称或法人名
    @ApiModelProperty(value = "应用编码", required = true, dataType="string")
    private String applyinstCode;//
    @ApiModelProperty(value = "申报来源，net表示网上申报，win表示窗口申报", required = true, dataType="string",allowableValues = "net,win")
    private String applyinstSource;//申报来源，net表示网上申报，win表示窗口申报
    @ApiModelProperty(value = "是否串联审批，0表示并联审批，1表示串联审批", required = true, dataType="string",allowableValues = "0,1")
    private String isSeriesApprove;//是否串联审批，0表示并联审批，1表示串联审批
    @ApiModelProperty(value = "工程信息ID", required = true, dataType="string")
    private String projInfoId;
    @ApiModelProperty(value = "工程名称", required = true, dataType="string")
    private String projName;
    @ApiModelProperty(value = "联系人ID", required = true, dataType="string")
    private String linkmanInfoId;
    @ApiModelProperty(value = "项目ID", required = true, dataType="string")
    private String itemId;// 串联一个  ；并联会有多个 改为事项版本后弃用该字段 20190221 小糊涂 使用itemVerId
    @ApiModelProperty(value = "项目实例状态", required = true, dataType="string")
    private String iteminstState;
    @ApiModelProperty(value = "所属主题版本ID", required = true, dataType="string")
    private String themeVerId; // (所属主题版本ID)
    @ApiModelProperty(value = "所属阶段定义ID", required = true, dataType="string")
    private String stageId; // (所属阶段定义ID)
    @ApiModelProperty(value = "状态", required = true, dataType="string")
    private String stageinstState; // (状态)
    @ApiModelProperty(value = "开普返回的回执文件路径", required = true, dataType="string")
    private String fileUrl;//开普返回的回执文件路径
    @ApiModelProperty(value = "事项版本ID", required = true, dataType="string")
    private String itemVerId;//事项版本ID 20190221

    @ApiModelProperty(value = "回执类型名称-查询数据字典获得", required = true, dataType="string")
    private String receiveTypeName;//回执类型名称-查询数据字典获得

    /* AEA_HI_RECEIVE字段 end */
    @ApiModelProperty(value = "项目实例名称", required = true, dataType="string")
    private String iteminstName;
    @ApiModelProperty(value = "打印时间", required = true, dataType="string")
    private String printTime;
    @ApiModelProperty(value = "承诺办结时限数字", required = true, dataType="long")
    private Double dueNum; // (承诺办结时限数字)
    @ApiModelProperty(value = "承诺办结时限单位", required = true, dataType="string")
    private String dueUnit; // (承诺办结时限单位)
    @ApiModelProperty(value = "项目名称", required = true, dataType="string")
    private String itemName;
    @ApiModelProperty(value = "时间限制", required = true, dataType="string")
    private String timeLimit;
    @ApiModelProperty(value = "所有材料", required = true, dataType="list")
    private List<AeaHiItemMatinstVo> allMatList; //所有材料
    @ApiModelProperty(value = "纸质材料", required = true, dataType="list")
    private List<AeaHiItemMatinstVo> paperMatList; //纸质材料
    @ApiModelProperty(value = "电子材料", required = true, dataType="list")
    private List<AeaHiItemMatinstVo> attrMatList; //电子材料
    @ApiModelProperty(value = "收件日期", required = true, dataType="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date getMatDate;//收件日期
    @ApiModelProperty(value = "窗口收件人", required = true, dataType="string")
    private String tellerName;//窗口收件人
    @ApiModelProperty(value = "接收人单位", required = true, dataType="string")
    private String receiveOrgName;//接收人单位

    @ApiModelProperty(value = "经办企业证件号", required = true, dataType="string")
    private String agentIdCard;//经办企业证件号
    @ApiModelProperty(value = "经办企业", required = true, dataType="string")
    private String agentName;//经办企业
    @ApiModelProperty(value = "经办人手机号", required = true, dataType="string")
    private String agentLinkmanTel;//经办人手机号
    @ApiModelProperty(value = "经办人姓名", required = true, dataType="string")
    private String agentLinkmanName;//经办人姓名
    @ApiModelProperty(value = "经办人身份证号", required = true, dataType="string")
    private String agentLinkmanIDCard;//经办人身份证号
    @ApiModelProperty(value = "建设单位证件号", required = true, dataType="string")
    private String applicantIDCard;//建设单位证件号
    @ApiModelProperty(value = "部门简称", required = true, dataType="string")
    private String orgShortName; //部门简称
    @ApiModelProperty(value = "窗口人员签字", required = true, dataType="string")
    private String winName;//窗口人员签字

    @ApiModelProperty(value = "领取方式", required = true, dataType="string")
    private String receiveMode; // (领取方式)
    @ApiModelProperty(value = "领证人", required = true, dataType="string")
    private String issueUserName; // (领证人)
    @ApiModelProperty(value = "领证人手机号码", required = true, dataType="string")
    private String issueUserMobile; // (领证人手机号码)
    //    private String expressNum;//快递单号
//    private String isShowIdcard;//收件时是否需要出示证明
    @ApiModelProperty(value = "寄件人姓名", required = true, dataType="string")
    private String senderName;//寄件人姓名
    @ApiModelProperty(value = "寄件人手机号码", required = true, dataType="string")
    private String senderPhone;//寄件人手机号码
    @ApiModelProperty(value = "寄件人省份", required = true, dataType="string")
    private String senderProvince;//寄件人省份
    @ApiModelProperty(value = "寄件人城市", required = true, dataType="string")
    private String senderCity;//寄件人城市
    @ApiModelProperty(value = "寄件人县区", required = true, dataType="string")
    private String senderCounty;//寄件人县区
    @ApiModelProperty(value = "寄件人地址", required = true, dataType="string")
    private String senderAddr;//寄件人地址

    @ApiModelProperty(value = "收件人名字", required = true, dataType="string")
    private String addresseeName; // (收件人名字)
    @ApiModelProperty(value = "收件人号码", required = true, dataType="string")
    private String addresseePhone; // (收件人号码)
    @ApiModelProperty(value = "收件人身份证", required = true, dataType="string")
    private String addresseeIdcard; // (收件人身份证)

    @ApiModelProperty(value = "法人", required = true, dataType="string")
    private String idrepresentative; //法人
    @ApiModelProperty(value = "法人证件号", required = true, dataType="string")
    private String idno;//法人证件号
    @ApiModelProperty(value = "法人联系方式", required = true, dataType="string")
    private String idmobile;//法人联系方式
    @ApiModelProperty(value = "应用详细网站", required = true, dataType="string")
    private String applicantDetailSite;


    public static ReceiveVo getReceiveVo(AeaHiReceiveVo aeaHiReceiveVo){
        ReceiveVo receiveVo = new ReceiveVo();
        receiveVo.setReceiveId(aeaHiReceiveVo.getReceiveId());
        receiveVo.setApplyinstId(aeaHiReceiveVo.getApplyinstId());
        receiveVo.setOutinstId(aeaHiReceiveVo.getOutinstId());
        receiveVo.setReceiveUserName(aeaHiReceiveVo.getReceiveUserName());
        receiveVo.setReceiveCertNo(aeaHiReceiveVo.getReceiveCertNo());
        receiveVo.setReceiveTime(aeaHiReceiveVo.getReceiveTime());
        receiveVo.setApproveDeptRegion(aeaHiReceiveVo.getApproveDeptRegion());
        receiveVo.setSubDeptRegion(aeaHiReceiveVo.getSubDeptRegion());
        receiveVo.setReceiveMemo(aeaHiReceiveVo.getReceiveMemo());
        receiveVo.setCreater(aeaHiReceiveVo.getCreater());
        receiveVo.setCreateTime(aeaHiReceiveVo.getCreateTime());
        receiveVo.setModifier(aeaHiReceiveVo.getModifier());
        receiveVo.setModifyTime(aeaHiReceiveVo.getModifyTime());
        receiveVo.setReceiveUserMobile(aeaHiReceiveVo.getReceiveUserMobile());
        receiveVo.setServiceAddress(aeaHiReceiveVo.getServiceAddress());
        receiveVo.setReceiptType(aeaHiReceiveVo.getReceiptType());
        receiveVo.setDocumentNum(aeaHiReceiveVo.getDocumentNum());
        receiveVo.setDocumentName(aeaHiReceiveVo.getDocumentName());
        receiveVo.setApplicant(aeaHiReceiveVo.getApplicant());
        receiveVo.setApplyinstCode(aeaHiReceiveVo.getApplyinstCode());
        receiveVo.setApplyinstSource(aeaHiReceiveVo.getApplyinstSource());
        receiveVo.setIsSeriesApprove(aeaHiReceiveVo.getIsSeriesApprove());
        receiveVo.setProjInfoId(aeaHiReceiveVo.getProjInfoId());
        receiveVo.setProjName(aeaHiReceiveVo.getProjName());
        receiveVo.setLinkmanInfoId(aeaHiReceiveVo.getLinkmanInfoId());
        receiveVo.setIteminstState(aeaHiReceiveVo.getIteminstState());
        receiveVo.setThemeVerId(aeaHiReceiveVo.getThemeVerId());
        receiveVo.setStageId(aeaHiReceiveVo.getStageId());
        receiveVo.setStageinstState(aeaHiReceiveVo.getStageinstState());
        receiveVo.setFileUrl(aeaHiReceiveVo.getFileUrl());
        receiveVo.setItemVerId(aeaHiReceiveVo.getItemVerId());
        receiveVo.setReceiveTypeName(aeaHiReceiveVo.getReceiveTypeName());
        receiveVo.setIteminstName(aeaHiReceiveVo.getIteminstName());
        receiveVo.setPrintTime(aeaHiReceiveVo.getPrintTime());
        receiveVo.setDueNum(aeaHiReceiveVo.getDueNum());
        receiveVo.setDueUnit(aeaHiReceiveVo.getDueUnit());
        receiveVo.setItemName(aeaHiReceiveVo.getItemName());
        receiveVo.setTimeLimit(aeaHiReceiveVo.getTimeLimit());
        receiveVo.setAllMatList(getAeaHiItemMatinstVos(aeaHiReceiveVo.getAllMatList()));
        receiveVo.setPaperMatList(getAeaHiItemMatinstVos(aeaHiReceiveVo.getPaperMatList()));
        receiveVo.setAttrMatList(getAeaHiItemMatinstVos(aeaHiReceiveVo.getAttrMatList()));
        receiveVo.setGetMatDate(aeaHiReceiveVo.getGetMatDate());
        receiveVo.setTellerName(aeaHiReceiveVo.getTellerName());
        receiveVo.setReceiveOrgName(aeaHiReceiveVo.getReceiveOrgName());
        receiveVo.setAgentIdCard(aeaHiReceiveVo.getAgentIdCard());
        receiveVo.setAgentName(aeaHiReceiveVo.getAgentName());
        receiveVo.setAgentLinkmanTel(aeaHiReceiveVo.getAgentLinkmanTel());
        receiveVo.setAgentLinkmanName(aeaHiReceiveVo.getAgentLinkmanName());
        receiveVo.setAgentLinkmanIDCard(aeaHiReceiveVo.getAgentLinkmanIDCard());
        receiveVo.setApplicantIDCard(aeaHiReceiveVo.getApplicantIDCard());
        receiveVo.setOrgShortName(aeaHiReceiveVo.getOrgShortName());
        receiveVo.setWinName(aeaHiReceiveVo.getWinName());
        receiveVo.setReceiveMode(aeaHiReceiveVo.getReceiveMode());
        receiveVo.setIssueUserName(aeaHiReceiveVo.getIssueUserName());
        receiveVo.setIssueUserMobile(aeaHiReceiveVo.getIssueUserMobile());
        receiveVo.setSenderName(aeaHiReceiveVo.getSenderName());
        receiveVo.setSenderPhone(aeaHiReceiveVo.getSenderPhone());
        receiveVo.setSenderProvince(aeaHiReceiveVo.getSenderProvince());
        receiveVo.setSenderCity(aeaHiReceiveVo.getSenderCity());
        receiveVo.setSenderCounty(aeaHiReceiveVo.getSenderCounty());
        receiveVo.setSenderAddr(aeaHiReceiveVo.getSenderAddr());
        receiveVo.setAddresseeName(aeaHiReceiveVo.getAddresseeName());
        receiveVo.setAddresseePhone(aeaHiReceiveVo.getAddresseePhone());
        receiveVo.setAddresseeIdcard(aeaHiReceiveVo.getAddresseeIdcard());
        receiveVo.setIdrepresentative(aeaHiReceiveVo.getIdrepresentative());
        receiveVo.setIdno(aeaHiReceiveVo.getIdno());
        receiveVo.setIdmobile(aeaHiReceiveVo.getIdmobile());
        receiveVo.setApplicantDetailSite(aeaHiReceiveVo.getApplicantDetailSite());
        return receiveVo;
    }

    private static List<AeaHiItemMatinstVo> getAeaHiItemMatinstVos(List<AeaHiItemMatinst> aeaHiItemMatinsts){
        List<AeaHiItemMatinstVo> aeaHiItemMatinstVos = new ArrayList<AeaHiItemMatinstVo>();
        for(AeaHiItemMatinst aeaHiItemMatinst:aeaHiItemMatinsts){
            AeaHiItemMatinstVo aeaHiItemMatinstVo = new AeaHiItemMatinstVo();
            aeaHiItemMatinstVo.setMatinstId(aeaHiItemMatinst.getMatinstId());
            aeaHiItemMatinstVo.setMatId(aeaHiItemMatinst.getMatId());
            aeaHiItemMatinstVo.setRealPaperCount(aeaHiItemMatinst.getRealPaperCount());
            aeaHiItemMatinstVo.setRealCopyCount(aeaHiItemMatinst.getRealCopyCount());
            aeaHiItemMatinstVo.setAttCount(aeaHiItemMatinst.getAttCount());
            aeaHiItemMatinstVo.setCreater(aeaHiItemMatinst.getCreater());
            aeaHiItemMatinstVo.setCreateTime(aeaHiItemMatinst.getCreateTime());
            aeaHiItemMatinstVo.setModifier(aeaHiItemMatinst.getModifier());
            aeaHiItemMatinstVo.setModifyTime(aeaHiItemMatinst.getModifyTime());
            aeaHiItemMatinstVo.setMatinstCode(aeaHiItemMatinst.getMatinstCode());
            aeaHiItemMatinstVo.setMatinstName(aeaHiItemMatinst.getMatinstName());
            aeaHiItemMatinstVo.setUnitInfoId(aeaHiItemMatinst.getUnitInfoId());
            aeaHiItemMatinstVo.setProjInfoId(aeaHiItemMatinst.getProjInfoId());
            aeaHiItemMatinstVo.setOfficialDocNo(aeaHiItemMatinst.getOfficialDocNo());
            aeaHiItemMatinstVo.setOfficialDocTitle(aeaHiItemMatinst.getOfficialDocTitle());
            aeaHiItemMatinstVo.setOfficialDocDueDate(aeaHiItemMatinst.getOfficialDocDueDate());
            aeaHiItemMatinstVo.setOfficialDocPublishDate(aeaHiItemMatinst.getOfficialDocPublishDate());
            aeaHiItemMatinstVo.setAttFormList(aeaHiItemMatinst.getAttFormList());
            aeaHiItemMatinstVo.setIsMakeUp(aeaHiItemMatinst.getIsMakeUp());
            aeaHiItemMatinstVo.setOrgShortName(aeaHiItemMatinst.getOrgShortName());
            aeaHiItemMatinstVo.setOrgName(aeaHiItemMatinst.getOrgName());
            aeaHiItemMatinstVos.add(aeaHiItemMatinstVo);
        }
        return aeaHiItemMatinstVos;

    }
}
