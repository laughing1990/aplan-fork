package com.augurit.aplanmis.common.vo.guide;

import com.augurit.aplanmis.common.apply.item.GuideComputedItem;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel(value = "部门辅导详情vo")
public class GuideDetailVo {

    @ApiModelProperty(value = "项目类型是否有更改")
    private boolean themeChanged;

    private String newThemeId;

    private String newThemeName;

    private String newStageId;

    @ApiModelProperty(value = "是否牵头部门")
    private boolean leaderDept;

    @ApiModelProperty(value = "审批部门id", notes = "当 leaderDept=false时使用")
    private String approveOrgId;

    @ApiModelProperty(value = "部门辅导主记录")
    private AeaHiGuide aeaHiGuide;

    @ApiModelProperty(value = "并联事项")
    private List<GuideComputedItem> parallelItems;

    @ApiModelProperty(value = "并行事项")
    private List<GuideComputedItem> optionItems;

    public GuideDetailVo() {
        this.parallelItems = new ArrayList<>();
        this.optionItems = new ArrayList<>();
    }
}
