package com.augurit.aplanmis.common.service.diagram.component;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("可选事项")
public class OptionStageDiagramItem {

    @ApiModelProperty(value = "并行申报事项", notes = "也可理解为可选事项")
    protected List<BasicItemDiagramItem> optionStageDiagramItems;

    public OptionStageDiagramItem(List<BasicItemDiagramItem> optionStageDiagramItems) {
        if (optionStageDiagramItems != null) {
            this.optionStageDiagramItems = optionStageDiagramItems;
        } else {
            this.optionStageDiagramItems = new ArrayList<>();
        }
    }
}
