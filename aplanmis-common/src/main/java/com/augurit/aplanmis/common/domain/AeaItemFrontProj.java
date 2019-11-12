package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * AeaItemFrontProj class
 *
 * @author jjt
 * @date 2019/11/05
 *
 */
@Data
@ApiModel(description="事项项目信息前置检测")
public class AeaItemFrontProj implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String frontProjId;

    @ApiModelProperty("事项版本ID")
    private String itemVerId;

    @ApiModelProperty("规则名称")
    private String ruleName;

    @ApiModelProperty("条件表达式，XM_YDMJ>1000，启用EL后以表达式为主")
    private String ruleEl;

    @ApiModelProperty("排列顺序号")
    private Long sortNo;

    @ApiModelProperty("是否启用，0表示禁用，1表示启用")
    private String isActive;

    @ApiModelProperty("备注说明")
    private String frontProjMemo;

    @ApiModelProperty("创建人")
    private String creater;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty("修改人")
    private String modifier;

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date modifyTime;

    @ApiModelProperty("根组织ID")
    private String rootOrgId;


    //非表字段
    //查询关键字
    private String keyword;
}
