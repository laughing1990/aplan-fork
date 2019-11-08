package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.augurit.agcloud.bsc.util.UuidUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * ActStoForminstLog class
 *
 * @author jjt
 * @date 2019/4/21
 */
@Data
public class AeaItemStateForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 情形表单ID
     */
    private String itemStateFormId;

    /**
     * 事项版本ID
     */
    private String itemVerId;

    /**
     * 情形版本ID
     */
    private String itemStateVerId;

    /**
     * 是否情形表单 0 通用表单 1情形表单（此时情形id要填）
     */
    private String isStateForm;

    /**
     * 事项情形ID
     */
    private String itemStateId;

    /**
     * 表单ID
     */
    private String formId;

    /**
     * 排序字段
     */
    private Long sortNo;

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
     * 是否内置表单
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

    public AeaItemStateForm copyOne() {

        AeaItemStateForm copiedOne = new AeaItemStateForm();
        copiedOne.setItemStateFormId(UuidUtil.generateUuid());
        copiedOne.setItemVerId(this.itemVerId);
        copiedOne.setItemStateVerId(this.itemStateVerId);
        copiedOne.setIsStateForm(this.isStateForm);
        copiedOne.setItemStateId(this.itemStateId);
        copiedOne.setFormId(this.formId);
        copiedOne.setSortNo(this.sortNo);
        copiedOne.setCreater(this.creater);
        copiedOne.setCreateTime(new Date());
        return copiedOne;
    }
}
