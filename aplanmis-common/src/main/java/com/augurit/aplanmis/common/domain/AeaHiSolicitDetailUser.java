package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 征求意见详情用户任务表-模型,注意：如果增删改字段要同步修改SolicitDetailUserVo值对象类
 */
@Data
public class AeaHiSolicitDetailUser implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String detailUserId; // (主键)
    private String solicitDetailId; // (征求意见详情ID)
    private String userId; // (用户ID)
    private String userConclusion; // (办理结论，0表示不通过或不同意，1表示通过或同意，2表示不涉及)
    private String userOpinion; // (填写意见)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date signTime; // (签收时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date fillTime; // (填写意见时间)
    private String taskAction; // (任务动作，0表示正常办理，1表示转交给同一委办局的其他人员，2表示添加同一委办局的其他人员进来)
    private String parentId; // (父ID【当TASK_ACTION=1或2时必填】)
    private String isDeleted; // (是否删除：0表示未删除；1表示已删除)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建日期)
    private String modifier; // (更新人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (更新时间)
    private String linkmanName; // (联系人)
    private String linkmanPhone; // (联系人联系方式)

    private String userName; //扩展字段 (用户名)

    //public String getTableName()  {
    //    return "AeaHiSolicitDetailUser";
    //}
}
