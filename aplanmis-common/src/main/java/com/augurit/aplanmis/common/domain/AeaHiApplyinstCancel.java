package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * 申报撤件实例表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>日期：2019-12-12 14:18:58</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:58</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaHiApplyinstCancel implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String applyinstCancelId; // (主键)
    private String applyinstId; // (申报实例ID)
    private String projInfoId; // (项目ID)
    private String appinstId; // (所属流程实例ID)
    private String attId; // (电子附件关联ID)
    private String applyReason; // (撤件原因)
    private String applyUserId; // (申请人ID)
    private String applyUserName; // (申请人姓名)
    private String applyUserIdnumber; // (申请人身份证号)
    private String applyUserPhone; // (申请人联系方式)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date applyTime; // (申请时间)
    private String handleOpinion; // (办理意见)
    private String handleOrgId; // (办理部门ID)
    private String handleOrgName; // (办理部门名称)
    private String handleUserId; // (办理人ID)
    private String handleUserName; // (办理人姓名)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date handleSignTime; // (办理签收时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date handleEndTime; // (办理结束时间)
    private String cancelState; // (状态：0 已提交，1 已接收，2 已通过，3 未通过，4 无效)
    private String cancelMemo; // (备注)
    private String isDeleted; // (是否逻辑删除，0表示未删除，1表示已删除)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (根组织ID)
    private String isSuspendedBefore;//撤销之前是否有挂起：1 是，0 否（如果不同意撤销，将恢复流程实例之前的状态）

    //扩展字段
    private String isCancel;
    private List<AeaHiItemCancel> aeaHiItemCancels;
    private List<BscAttFileAndDir> bscAttFileAndDirs;
    private String isOnlyRead;
    private String iteminstCancelId;
}
