package com.augurit.aplanmis.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 中介机构服务事项表-模型
 */
@Data
public class AeaImUnitService implements Serializable {

    private static final long serialVersionUID = 1L;

    private String unitServiceId; // (主键)
    private String serviceId; // (服务ID)
    private String price; // (价格（万元）)
    private String auditFlag; // (审核状态:0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成)
    private String isDelete; // (是否删除：1 已删除，0 未删除)
    private String isActive; // (是否启用：1 已启用，0 未启用)
    private String creater; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createTime; // ()
    private String modifier; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifyTime; // ()
    private String unitInfoId; // (所属单位ID)
    private String memo; // (备注)
    private String serviceContent; // (服务承诺)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date publishTime; // (发布时间)
    private String feeReference; // (收费参考)

    private String rootOrgId;//根组织ID

    //非表字段
    private String applicant;//从业机构名称

    private String certinstId;

//    @FiledNameIs(filedValue = "事项版本ID")
//    private String itemVerId; //事项版本ID
/*@FiledNameIs(filedValue = "服务类型和中介服务事项关联ID")
 *//*   private String serviceId;
    @FiledNameIs(filedValue = "价格（万元）")
    private String price; //价格（万元）
    @FiledNameIs(filedValue = "审核状态")
    private String auditFlag; //审核状态 :0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成
    @FiledNameIs(filedValue = "是否删除")
    private String isDelete; //是否删除 1 已删除，0 未删除
    @FiledNameIs(filedValue = "是否启用")
    private String isActive; //是否启用：1 已启用，0 未启用
    @FiledNameIs(filedValue = "创建者")
    private String creater; //创建者
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyTime; // (修改时间)
    @FiledNameIs(filedValue = "所属单位ID")
    private String unitInfoId; //所属单位ID(机构ID)
    @FiledNameIs(filedValue = "联系人ID")
    private String linkmanInfoId; //联系人ID
    @FiledNameIs(filedValue = "服务类型ID")
    private String serviceTypeId; //服务类型ID
    @FiledNameIs(filedValue = "备注")
    private String memo; //备注
    @FiledNameIs(filedValue = "服务内容")
    private String serviceContent; //服务内容
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date practiseDate;//从业时间

    //非表字段
    private String itemName;//事项名称
    private String applicant;//中介机构名称*/

    private String serviceName;
    private String imgUrl;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date auditTime; //审核时间
}