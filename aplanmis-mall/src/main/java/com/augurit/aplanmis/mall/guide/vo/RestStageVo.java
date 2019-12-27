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
    @ApiModelProperty(value = "承诺办结时限")
    private Double dueNum;
    @ApiModelProperty(value = "承诺办结时限单位")
    private String dueUnit;
    @ApiModelProperty("是否被选中1:是,0:否")
    private String isSelected = "0";
    public static RestStageVo build(AeaParStage aeaParStage){
        RestStageVo restStageVo = new RestStageVo();
        BeanUtils.copyProperties(aeaParStage,restStageVo);
        return restStageVo;
    }
}
