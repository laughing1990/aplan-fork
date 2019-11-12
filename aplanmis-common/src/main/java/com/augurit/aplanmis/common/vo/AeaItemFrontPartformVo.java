package com.augurit.aplanmis.common.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author tiantian
 * @date 2019/11/11
 */
@Data
public class AeaItemFrontPartformVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String frontPartformId; // (ID)
    private String itemVerId; // (事项版本ID)
    private String itemPartformId; // (前置事项版本ID)
    private Long sortNo; // (排序字段)
    private String isActive; // ()

    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime; // ()
    private String rootOrgId; // (根组织id)

    private String isSmartForm;//是否智能表单
    private String partformName;//阶段扩展表单名称


}
