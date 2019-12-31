package com.augurit.aplanmis.mall.guide.vo;

import com.augurit.aplanmis.common.domain.AeaItemMat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("办事指南材料VO")
public class RestGuideMatVo {

    @ApiModelProperty("必选材料")
    List<AeaItemMat> requireMat;
    @ApiModelProperty("可选材料")
    List<AeaItemMat> noRequireMat;
}
