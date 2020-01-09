package com.augurit.aplanmis.mall.credit.vo;

import com.augurit.aplanmis.mall.userCenter.vo.AeaUnitInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("企业信用列表VO")
public class CreditProjUnitVo {
    @ApiModelProperty(value = "项目名称")
    private String projName;
    @ApiModelProperty(value = "项目代码")
    private String localCode;
    @ApiModelProperty(value = "单位列表")
    private List<CreditUnitVo> unitList;
}
