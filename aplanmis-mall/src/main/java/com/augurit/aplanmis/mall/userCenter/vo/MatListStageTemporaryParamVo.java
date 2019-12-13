package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("暂存阶段材料数据传值vo")
public class MatListStageTemporaryParamVo extends MatListCommonTemporaryParamVo{
    @ApiModelProperty(value = "第一步项目信息暂存及第二步事项一单清暂存实体")
    private ItemListTemporaryParamVo itemListTemporaryParamVo;
}
