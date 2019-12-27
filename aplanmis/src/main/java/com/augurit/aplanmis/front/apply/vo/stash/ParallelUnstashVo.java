package com.augurit.aplanmis.front.apply.vo.stash;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.constants.ThemeCategory;
import com.augurit.aplanmis.common.domain.AeaParStage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApiModel("并联回显vo")
@Getter
@Setter
public class ParallelUnstashVo extends UnStashVo {

    @ApiModelProperty(value = "阶段所选情形")
    private Set<String> stateIds;

    @ApiModelProperty(value = "简单合并申报，选择的并联事项情形", dataType = "string")
    private Set<String> parallelItemStateIds;

    @ApiModelProperty(value = "并联事项与审批部门对应关系")
    private Map<String, String> branchOrg;

    @ApiModelProperty(value = "并行推进事项暂存信息")
    private List<SeriesUnstashVo> seriesUnstashList;

    @ApiModelProperty(value = "阶段主辅线编码", notes = "")
    private String themeCategoryValue;

    public ParallelUnstashVo() {
        seriesUnstashList = new ArrayList<>();
    }

    public void addSeriesUnstashVo(SeriesUnstashVo seriesUnstashVo) {
        this.seriesUnstashList.add(seriesUnstashVo);
    }

    public void judgeStageCategory(AeaParStage aeaParStage) {
        if (Status.ON.equals(aeaParStage.getIsNode())) {
            this.themeCategoryValue = ThemeCategory.MAINLINE.getValue();
            return;
        }
        ThemeCategory themeCategory = ThemeCategory.fromValue(aeaParStage.getDygjbzfxfw());
        this.themeCategoryValue = themeCategory.getValue();
    }
}
