package com.augurit.aplanmis.supermarket.projPurchase.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/13
 */
@Data
@ApiModel("资质要求信息")
public class SelectedQualMajorRequire {
    @ApiModelProperty(value = "资质列表")
    private List<SelectedQualVo> selectedQuals;
}
