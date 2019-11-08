package com.augurit.aplanmis.front.apply.vo;

import com.augurit.aplanmis.common.domain.AeaParStageItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ApiModel("单项申报前置检查vo")
public class SeriesApplyCheckVo {

    @ApiModelProperty(value = "阶段id", dataType = "string")
    private String stageId;

    @ApiModelProperty(value = "是否并行事项", dataType = "string", notes = "0: 并联, 1: 并行")
    private String isParallel;

    @ApiModelProperty(value = "主题阶段", notes = "列表数据")
    private Set<ThemeStageVo> themeStageList;

    public SeriesApplyCheckVo() {
    }

    public SeriesApplyCheckVo(String stageId, String isParallel) {
        this.stageId = stageId;
        this.isParallel = isParallel;
    }

    public void buildThemeStageList(List<AeaParStageItem> aeaParStageItems, String projInfoId) {
        if (aeaParStageItems != null && aeaParStageItems.size() > 0) {
            Set<ThemeStageVo> themeStageList = new HashSet<>(aeaParStageItems.size());
            aeaParStageItems.forEach(item -> {
                if (projInfoId.equals(item.getProjInfoId())) {
                    this.stageId = item.getStageId();
                    this.isParallel = item.getIsOptionItem();
                }
                themeStageList.add(new ThemeStageVo(item.getThemeId(), item.getThemeName(), item.getStageId(), item.getIsOptionItem()));
            });
            this.themeStageList = themeStageList;
        }
    }

    @Data
    @ApiModel("主题阶段")
    private class ThemeStageVo {

        @ApiModelProperty(value = "主题id", dataType = "string")
        private String themeId;

        @ApiModelProperty(value = "主题名称", dataType = "string")
        private String themeName;

        @ApiModelProperty(value = "阶段id", dataType = "string")
        private String StageId;

        @ApiModelProperty(value = "是否并行事项", dataType = "string", notes = "0: 并联, 1: 并行")
        private String isParallel;

        ThemeStageVo(String themeId, String themeName, String stageId, String isParallel) {
            this.themeId = themeId;
            this.themeName = themeName;
            StageId = stageId;
            this.isParallel = isParallel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ThemeStageVo that = (ThemeStageVo) o;

            if (!themeId.equals(that.themeId)) return false;
            return StageId.equals(that.StageId);

        }

        @Override
        public int hashCode() {
            int result = themeId.hashCode();
            result = 31 * result + StageId.hashCode();
            return result;
        }
    }
}
