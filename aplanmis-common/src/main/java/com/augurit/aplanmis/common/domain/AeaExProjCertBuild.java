package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Blob;
import java.util.Date;

/**
 * 建设工程施工许可证
 */
@Data
@ApiModel("建设工程施工许可证表")
public class AeaExProjCertBuild {
    @ApiModelProperty("主键ID")
    private String buildId;
    @ApiModelProperty("项目ID")
    private String projInfoId;
    @ApiModelProperty("建设工程规划许可证编号")
    private String certBuildCode;
    @ApiModelProperty("建设工程规划许可证二维码")
    private String certBuildQrcode;
    @ApiModelProperty("核发机关组织机构代码")
    private String govOrgCode;
    @ApiModelProperty("核发机关")
    private String govOrgName;
    @ApiModelProperty("核发日期")
    private Date publishTime;
    @ApiModelProperty("建设单位")
    private String constructionUnit;
    @ApiModelProperty("工程名称")
    private String projName;
    @ApiModelProperty("建设地址")
    private String constructionAddr;
    @ApiModelProperty("建设规模")
    private String constructionsSize;
    @ApiModelProperty("合同价格，单位：万元")
    private String contractPrice;
    @ApiModelProperty("勘察单位")
    private String kcUnit;
    @ApiModelProperty("设计单位")
    private String sjUnit;
    @ApiModelProperty("施工单位")
    private String sgUnit;
    @ApiModelProperty("监理单位")
    private String jlUnit;
    @ApiModelProperty("勘察单位项目负责人")
    private String kcUnitLeader;
    @ApiModelProperty("设计单位项目负责人")
    private String sjUnitLeader;
    @ApiModelProperty("施工单位项目负责人")
    private String sgUnitLeader;
    @ApiModelProperty("总监理工程师")
    private String jlUnitLeader;
    @ApiModelProperty("合同工期")
    private String contractPeriod;
    @ApiModelProperty("备注")
    private String certBuildMemo;
    @ApiModelProperty("创建人")
    private String creater;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("修改人")
    private String modifier;
    @ApiModelProperty("修改时间")
    private Date modifyTime;
    @ApiModelProperty("所属根组织ID")
    private String rootOrgId;
    @ApiModelProperty("打印人ID")
    private String printUserId;
    @ApiModelProperty("打印人姓名")
    private String printUserName;
    @ApiModelProperty("打印时间")
    private Date printTime;
}
