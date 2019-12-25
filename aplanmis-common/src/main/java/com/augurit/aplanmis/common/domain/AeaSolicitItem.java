package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@ApiModel(description = "按事项征求配置表")
public class AeaSolicitItem implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String solicitItemId; 

    @ApiModelProperty("事项ID")
    private String itemId; 

    @ApiModelProperty("事项版本ID")
    private String itemVerId;

    @ApiModelProperty("征求业务类型，来自于数据字典")
    private String busType;

    @ApiModelProperty("征求意见模式：0表示多人征求模式，1表示单人征求模式)")
    private String solicitType;

    @ApiModelProperty("创建人")
    private String creater;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    @ApiModelProperty("修改人")
    private String modifier;

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    @ApiModelProperty("根组织ID")
    private String rootOrgId;


    // ====================  以下是扩展字段 ===================

    /**
     * 事项父级id
     */
    @ApiModelProperty("扩展字段：事项父级id")
    private String parentItemId;

    /**
     * 事项基本信息id
     */
    @ApiModelProperty("扩展字段：事项基本信息id")
    private String itemBasicId;

    /**
     * 事项名称
     */
    @ApiModelProperty("扩展字段：事项名称")
    private String itemName;

    /**
     * 事项编号
     */
    @ApiModelProperty("扩展字段：事项编号")
    private String itemCode;

    /**
     * 事项性质
     */
    @ApiModelProperty("扩展字段：事项性质")
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
