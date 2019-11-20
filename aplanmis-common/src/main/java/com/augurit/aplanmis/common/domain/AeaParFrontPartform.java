package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 阶段的扩展表单前置检测表-模型
 */
@Data
public class AeaParFrontPartform implements Serializable {

    private static final long serialVersionUID = 1L;

    private String frontPartformId; // (ID)
    private String stageId; // (阶段ID)
    private String stagePartformId; // (阶段扩展表单ID)
    private Long sortNo; // (排序字段)
    private String isActive; // ()
    private String frontPartformMemo; // ()

    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime; // ()
    private String rootOrgId; // (根组织id)

    //非表字段
    //查询关键字
    private String keyword;
    private String isSmartForm;//是否智能表单
    private String partformName;//阶段扩展表单名称
    private String stoFormId;//表单定义ID
}
