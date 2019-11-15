package com.augurit.aplanmis.supermarket.projPurchase.vo.mat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = " 单事项材料定义")
public class ItemMatVo {
    @ApiModelProperty(value = "主键ID")
    private String matId;

    @ApiModelProperty(value = "材料编号")
    private String matCode;

    @ApiModelProperty(value = "材料名称")
    private String matName;

    @ApiModelProperty(value = "是否全局共性材料，供所有其他事项使用")
    private String isGlobalShare;

    @ApiModelProperty(value = "所属材料类别ID")
    private String matTypeId;
    @ApiModelProperty(value = "原件数")
    private Long duePaperCount;

    @ApiModelProperty(value = "复印件数")
    private Long dueCopyCount;

    @ApiModelProperty(value = "材料来源")
    private String matFrom;

    @ApiModelProperty(value = "样本文档")
    private String sampleDoc;

    @ApiModelProperty(value = "模板文档")
    private String templateDoc;

    @ApiModelProperty(value = "材料要求")
    private String matRequire;
    @ApiModelProperty(value = "纸质材料是否必需，0表示容缺，1表示必须")
    private String paperIsRequire;

    @ApiModelProperty(value = "排序")
    private Long sortNo;

    @ApiModelProperty(value = "备注说明")
    private String matMemo;
    @ApiModelProperty(value = "材料所属，c表示企业，u表示个人，p表示工程项目")
    private String matHolder;

    @ApiModelProperty(value = "是否批复文件，0表示否，1表示是")
    private String isOfficialDoc;

    @ApiModelProperty(value = "电子材料是否必需，0表示容缺，1表示必须")
    private String attIsRequire;

    @ApiModelProperty(value = "是否通用材料  1 是 0 不是")
    private String isCommon;

    @ApiModelProperty(value = "审查要点")
    private String reviewKeyPoints;

    @ApiModelProperty(value = "审查样本")
    private String reviewSampleDoc;

    @ApiModelProperty(value = "审查依据")
    private String reviewBasis;
    @ApiModelProperty(value = "原件验收 0 无 1验 2收")
    private String duePaperType;

    @ApiModelProperty(value = "复印件验收类型 0 无 1验 2收")
    private String dueCopyType;

    @ApiModelProperty(value = "纸质材料类型 0无 1 A3, 2 A4 ,3 A5")
    private String paperMatType;

    @ApiModelProperty(value = "是否支持容缺 0 否 1是")
    private String zcqy;
}
