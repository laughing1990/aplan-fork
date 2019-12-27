package com.augurit.aplanmis.front.apply.vo.stash;

import com.augurit.aplanmis.front.apply.vo.PropulsionItemApplyinstVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("并联申报暂存结果")
@Getter
@Setter
public class ParallelStashResultVo {

    @ApiModelProperty(value = "并联申报实例id")
    private String applyinstId;

    @ApiModelProperty(value = "并行申报实例id")
    private List<PropulsionItemApplyinstVo> seriesApplyinstIds;
}
