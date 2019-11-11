package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 施工及监理单位信息
 */
@Data
@ApiModel("施工及监理单位信息")
public class AeaExProjBuild implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private java.lang.String buildId;
    @ApiModelProperty("项目ID")
    private java.lang.String projInfoId;
    @ApiModelProperty("省级项目编号")
    private java.lang.String provinceProjCode;
    @ApiModelProperty("合同开工日期")
    private java.lang.String contractStartBuildTime;
    @ApiModelProperty("合同竣工时间")
    private java.lang.String contractEndBuildTime;
    @ApiModelProperty("施工面积，单位：平方米")
    private java.lang.String buildArea;
    @ApiModelProperty("结构体系，来自于数据字典")
    private java.lang.String structureSystem;
    @ApiModelProperty("质量监督注册号")
    private java.lang.String quaCheckNum;
    @ApiModelProperty("备注")
    private java.lang.String buildMemo;
    @ApiModelProperty("创建人")
    private java.lang.String creater;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("修改人")
    private java.lang.String modifier;
    @ApiModelProperty("修改时间")
    private java.lang.String modifyTime;
    @ApiModelProperty("所属根组织ID")
    private java.lang.String rootOrgId;

    private String aeaExProjBuildUnitInfo;

}
