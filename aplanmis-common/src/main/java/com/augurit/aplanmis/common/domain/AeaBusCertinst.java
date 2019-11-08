package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:12:20</li>
 * </ul>
 */
@Data
public class AeaBusCertinst implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String busCertinstId; // ()
    private String busTableName; // (业务表名)
    private String certinstId; // (电子证照实例ID)
    private String auditFlag; // (审核状态：0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成,6 已过时)
    private String isDelete; // (是否删除：1 已经删，0 未删除)
    private String creater; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // ()
    private String modifier; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // ()
    private String pkName; // (业务表主键)
    private String busRecordId; // (业务实例ID)
    private String memo; // (备注)
    private String rootOrgId;//根组织ID

    private String certinstName;
    private String linkmanInfoId;
}
