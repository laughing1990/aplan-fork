package com.augurit.aplanmis.mall.guide.vo;

import com.augurit.aplanmis.common.domain.AeaParStage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("单项办事指南")
public class RestStageVo {

    @ApiModelProperty("阶段ID")
    private String stageId;
    @ApiModelProperty("阶段名称")
    private String stageName;

    public static RestStageVo build(AeaParStage aeaParStage){
        RestStageVo restStageVo = new RestStageVo();
        BeanUtils.copyProperties(aeaParStage,restStageVo);
        return restStageVo;
    }
}
