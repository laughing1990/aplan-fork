package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/14
 */
@Data
@ApiModel("资质等表级")
public class AeaImQualLevelVo {

    @ApiModelProperty(value = "等级ID")
    private String qualLevelId;
    @ApiModelProperty(value = "资质等级编码")
    private String qualLevelCode;
    @ApiModelProperty(value = "资质等级名称")
    private String qualLevelName;
    @ApiModelProperty(value = "父ID")
    private String parentQualLevelId;
    @ApiModelProperty(value = "序列")
    private String qualLevelSeq;
    @ApiModelProperty(value = "优先级")
    private String priority; // (优先级)
    @ApiModelProperty(value = "专业列表")
    private List<AeaImMajorQualVo> majors;

}
