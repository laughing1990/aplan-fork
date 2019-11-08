package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tiantian
 * @date 2019/6/14
 */
@Data
@ApiModel("专业资质要求")
public class AeaImMajorQualVo extends BusinessZtreeNode<AeaImMajorQualVo> {

    @ApiModelProperty(value = "专业资质关联ID")
    private String majorQualId;
    @ApiModelProperty(value = "专业ID")
    private String majorId;
    @ApiModelProperty(value = "资质等级ID")
    private String qualLevelId;
    @ApiModelProperty(value = "父ID")
    private String parentMajorId;
    @ApiModelProperty(value = "专业名称")
    private String majorName;
    @ApiModelProperty(value = "专业序列")
    private String majorSeq;

    private String qualId;
    private String qualName;
    private String qualLevelName;
}
