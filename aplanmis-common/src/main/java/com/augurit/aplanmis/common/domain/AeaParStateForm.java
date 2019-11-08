package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 情形表单关联表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>日期：2019-07-04 16:18:35</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:chenzh</li>
 * <li>创建时间：2019-07-04 16:18:35</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaParStateForm implements Serializable {

    private static final long serialVersionUID = 1L;
    private String stateFormId; // (主键ID)
    private String parStateId; // (情形定义ID)
    private String formId; // (BPM表单ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    @Size(max = 10)
    private Long sortNo; // (排序字段)
    private String parStageId; // (阶段ID)
    private String isStateForm; // (是否情形表单 0 阶段通用表单 1情形表单（此时情形id要填）)

    //拓展字段
    /**
     * 表单名称
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
     *  是否内置表单
     */
    private String isInnerForm;

    /**
     * 关键字查询
     */
    private String keyword;

    /**
     * 是否勾选
     */
    private Boolean isCheck;

    /*
     * 表单加载地址
     */
    private String formLoadUrl;
}
