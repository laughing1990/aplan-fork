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
