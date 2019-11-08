package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class AeaOneform implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String oneformId;

    /**
     * 表单名称
     */
    private String oneformName;

    /**
     * 表单描述
     */
    private String oneformDesc;

    /**
     * 表单地址
     */
    private String oneformUrl;

    /**
     * 表单排序
     */
    private Long sortNo;

    /**
     * 是否启用
     */
    private String isActive;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 根组织
     */
    private String rootOrgId;

    /**
     * 扩展字段：关键字查询
     */
    private String keyword;

    /**
     * 扩展字段：阶段id
     */
    private String parStageId;
}
