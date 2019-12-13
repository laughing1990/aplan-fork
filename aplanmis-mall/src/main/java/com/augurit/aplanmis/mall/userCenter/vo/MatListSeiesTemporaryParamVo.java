package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("暂存单项材料数据传值vo")
public class MatListSeiesTemporaryParamVo extends MatListCommonTemporaryParamVo {

    @ApiModelProperty(value = "第一步项目信息暂存及第二步情形暂存实体")
    private StateListSeriesTemporaryParamVo stateListSeriesTemporaryParamVo;
}
