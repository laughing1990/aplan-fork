package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * AeaParStagePartform class
 *
 * @author jjt
 * @date 2019/8/29
 */
@Data
public class AeaParStagePartform implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 阶段定义ID
     */
    private String stagePartformId;

    /**
     * 扩展表名称
     */
    private String partformName;

    /**
     * 阶段定义ID
     */
    private String stageId;

    /**
     * 是否智能表单  1智能表单  0 开发表单
     */
    private String isSmartForm;

    /**
     * 表单定义ID
     */
    private String stoFormId;

    /**
     * 排序字段
     */
    private Long sortNo;

    /**
     * 是否启动EL表达式，0表示禁用，1表示启用
     */
    private String useEl;

    /**
     * EL表达式内容
     */
    private String elContent;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**
     * 扩展字段：表单名称
     */
    private String formName;

    /**
     * 表单编号
     */
    private String formCode;

    /**
     * 表单属性
     */
    private String formProperty;

    /**
     * 表单组织id
     */
    private String formOrgId;

    /**
     * 表单组织名称
     */
    private String orgName;

    /**
     * 表单终端id
     */
    private String formTmnId;

    /**
     * 表单终端名称
     */
    private String formTmnName;

    /**
     * 分类id
     */
    private String categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 是否内置表单
     */
    private String isInnerForm;

    /**
     * 扩展字段：关键字查询
     */
    private String keyword;

    /**
     * 扩展字段：开发表单链接地址
     */
    private String formUrl;
}
