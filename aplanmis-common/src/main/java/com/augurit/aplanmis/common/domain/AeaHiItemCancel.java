package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * 办件撤件实例表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>日期：2019-12-12 14:18:59</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:59</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaHiItemCancel implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String iteminstCancelId; // (主键)
    private String applyinstCancelId; // (申报撤件实例ID)
    private String iteminstId; // (事项实例ID)
    private String itemStateHistId; // (事项实例历史状态ID)
    private String attId; // (电子附件关联ID)
    private String iteminstName; // (事项实例名称)
    private String approvalOpinion; // (审批意见)
    private String approvalOrgId; // (审批部门ID)
    private String approvalOrgName; // (审批部门名称)
    private String approvalUserId; // (审批人ID)
    private String approvalUserName; // (审批人姓名)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date approvalSignTime; // (办理签收时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date approvalEndTime; // (办理结束时间)
    private String cancelState; // (状态：0 已提交，1 已接收，2 已通过，3 未通过，4 无效)
    private String cancelMemo; // (备注)
    private String isSuspendedBefore; // (撤销之前是否有挂起：1 是，0 否（如果不同意撤销，将恢复流程实例之前的状态）)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (根组织ID)
    private String isConcluding;// 流程是否已办结：1 是，0 否
    private String procInstId;
    private String signState;// 签收状态：1 已签收，0 未签收

    //扩展字段
    private String applyinstId;
    private List<BscAttFileAndDir> bscAttFileAndDirs;
}
