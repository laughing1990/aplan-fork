package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AeaItemGuide implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // (操作指南id)
    private Double anticipateDay; // (法定办结时限)
    private String anticipateType; // (法定办结时限单位)
    private String catlogCode; // (基本编码)
    private String ckbllc; // (窗口办理流程说明)
    private String ckbllct; // (窗口办理流程图)
    private String deptName; // (实施主体)
    private String deptTypeText; // (实施主体性质文本)
    private String handleTypeText; // (办理形式文本)
    private String isFeeText; // (是否收费文本)
    private String linkway; // (咨询方式)
    private String projectTypeTexy; // (办件类型文本)
    private Double promiseDay; // (承诺办结时限)
    private String promiseType; // (承诺办结时限单位)
    private String serveTypeText; // (服务对象)
    private String taskCode; // (实施编码)
    private String taskName; // (事项名称)
    private String taskTypeText; // (事项类型文本)
    private String tsckaddress; // (投诉窗口地址)
    private String tsdzyx; // (投诉电子邮箱)
    private String tstel; // (投诉电话号码)
    private String wsbllc; // (网上办理流程)
    private String userLevel; // (行使层级)
    private String wsbllct; // (网上办理流程图)
    private String wstswz; // (网上投诉网址)
    private String xhtsdz; // (信函投诉地址)
    private String itemVerId; // (事项版本id)
    private String taskType; // (事项类型)
    private String useLevelText; // (行使层级文本)
    private String deptCode; // (网厅部门编码( 组织机构代码 ))
    private String deptType; // (实施主体性质)
    private String tongyiCode; // (社会统一信用代码)
    private String serveType; // (服务对象)
    private String anticipateTypeText; // (法定办结期限单位文本)
    private String fdblsxsm; // (法定办结时限说明)
    private String promiseTypeText; // (承诺办结期限单位文本)
    private String crbjsxsm; // (承诺办结时限说明)
    private String handleType; // (办理形式)
    private String projectType; // (办件类型)
    private String itemsource; // (权利来源)
    private String itemsourceText; // (权力来源文本)
    private String factdegreeExplain; // (处罚的行为、种类、幅度)
    private String acceptCondition; // (受理条件)
    private String superviseWay; // (监督投诉方式)
    private String isFee; // (是否收费)
    private String chargenode; // (收费环节)
    private String chargetype; // (缴费方式)
    private String chargetypeText; // (缴费方式文本)
    private String onlineCheck; // (是否网上办理)
    private String onlineCheckText; // (是否网上办理文本)
    private String transactWebUrl; // (在线申办地址)
    private String levyKind; // (征收种类)
    private String isLevywaiver; // (是否涉及收（税）费减免的审批)
    private String isLimitText;
    private String rootOrgId; // 根组织id

    // 扩展字段
    private Long ckbllctNum; // 附件数量
    private Long wsbllctNum;  // 附件数量
}
