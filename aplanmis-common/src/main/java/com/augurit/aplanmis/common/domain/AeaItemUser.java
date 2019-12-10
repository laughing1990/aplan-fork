package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
* 用户事项管理-模型
*/
@Data
@ApiModel("用户事项管理")
public class AeaItemUser implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String itemUserId; 

    @ApiModelProperty(value = "事项id")
    private String itemId;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty("排序字段")
    private Long sortNo;

    @ApiModelProperty("是否启用")
    private String isActive;

    @ApiModelProperty("创建人")
    private String creater;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    @ApiModelProperty("'修改人")
    private String modifier;

    @ApiModelProperty("'修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    @ApiModelProperty(value = "根组织ID")
    private String rootOrgId;


    // ====================  以下是扩展字段 ===================

    /**
     * 事项父级id
     */
    private String parentItemId;

    /**
     * 事项版本id
     */
    private String itemVerId;

    /**
     * 事项基本信息id
     */
    private String itemBasicId;

    /**
     * 事项名称
     */
    private String itemName;

    /**
     * 事项编号
     */
    private String itemCode;

    /**
     * 事项性质
     */
    private String itemNature;

    /**
     *
     */
    private String bjType;

    /**
     *
     */
    private String dueNum;

    /**
     * 版本号
     */
    private String verNum;

    /**
     * 所属组织
     */
    private String orgName;

    /**
     * 是否标准事项  1标准事项 0 实施事项
     */
    private String isCatalog;

    /**
     * 标准事项指导部门
     */
    private String guideOrgName;

    /**
     * 版本状态
     */
    private String itemVerStatus;

    /**
     * 查询关键字
     */
    private String keyword;
}
