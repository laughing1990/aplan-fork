package com.augurit.aplanmis.rest.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaParStageItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("阶段下的事项与对应的表单")
public class StageItemFormVo {

    @ApiModelProperty(value = "事项版本id", dataType = "string")
    private String itemVerId;

    @ApiModelProperty(value = "智能表单id", dataType = "string")
    private String subFormId;

    @ApiModelProperty(value = "排序字段", dataType = "string")
    private Long sortNo;

    public static StageItemFormVo from(AeaParStageItem aeaParStageItem) {
        StageItemFormVo stageItemFormVo = new StageItemFormVo();
        stageItemFormVo.setItemVerId(aeaParStageItem.getItemVerId());
        stageItemFormVo.setSubFormId(aeaParStageItem.getSubFormId());
        stageItemFormVo.setSortNo(aeaParStageItem.getSortNo());
        return stageItemFormVo;
    }
}
