package com.augurit.aplanmis.supermarket.projPurchase.vo;

import com.augurit.aplanmis.common.vo.AeaImMajorQualVo;
import com.augurit.aplanmis.common.vo.AeaImQualLevelVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/14
 */
@Data
@ApiModel("要求的资质信息")
public class SelectedQualVo {
    @ApiModelProperty(value = "资质ID")
    private String qualId;

    @ApiModelProperty(value = "资质编码")
    private String qualCode;

    @ApiModelProperty(value = "资质名称")
    private String qualName;

    @ApiModelProperty(value = "资质等级列表")
    private List<AeaImQualLevelVo> aeaImQualLevels;

    @ApiModelProperty(value = "不分等级资质专业")
    private List<AeaImMajorQualVo> majors;



}
