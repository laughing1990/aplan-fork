package com.augurit.aplanmis.common.service.diagram.dto;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.service.diagram.component.DiagramItem;
import com.augurit.aplanmis.common.service.diagram.component.DiagrameStage;
import com.augurit.aplanmis.common.service.diagram.constant.HandleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ApiModel("全景流程图")
public class DiagramStatusDto {

    @ApiModelProperty(value = "主题版本id", dataType = "string")
    private String themeVerId;

    @ApiModelProperty(value = "全景图json", dataType = "string")
    private String diagramJson;

    @ApiModelProperty(value = "阶段状态信息", notes = "阶段信息, 列表")
    List<DiagramStageDto> diagramStageStatusList;

    public DiagramStatusDto() {
        this.diagramStageStatusList = new ArrayList<>();
    }

    @Getter
    @Setter
    @ApiModel("全景图阶段")
    public static class DiagramStageDto {

        @ApiModelProperty(value = "阶段id", dataType = "string", notes = "阶段id")
        private String stageId;

        @ApiModelProperty(value = "申报状态", dataType = "string", notes = "申报状态")
        private String statusValue = HandleStatus.UN_FINISHED.getValue();

        @ApiModelProperty(value = "申报状态中文名称", dataType = "string", notes = "申报状态中文名称")
        private String statusName = HandleStatus.UN_FINISHED.getName();

        @ApiModelProperty("阶段办理开始时间")
        private Date stageStartTime;

        @ApiModelProperty("阶段办理结束时间")
        private Date stageEndTime;

        @ApiModelProperty(value = "阶段办理时长", notes = "以 天 为单位")
        private String duringTime;

        @ApiModelProperty(value = "1 立项用地规划许可 2 立项用地规划许可 3 施工许可 4 竣工验收 5 并行推进")
        private String dybzspjdxh;

        @ApiModelProperty(value = "标准并联事项状态信息", notes = "标准并联事项状态信息， 列表")
        private Set<DiagramItemDto> diagramItemList;

        DiagramStageDto() {
            this.diagramItemList = new HashSet<>();
        }

    }

    @Getter
    @Setter
    @ApiModel("全景图事项")
    public static class DiagramItemDto {

        @ApiModelProperty(value = "事项版本id", dataType = "string", notes = "事项版本id")
        private String itemVerId;

        @ApiModelProperty(value = "事项id", dataType = "string", notes = "事项id")
        private String itemId;

        @ApiModelProperty(value = "申报状态", dataType = "string", notes = "申报状态")
        private String statusValue;

        @ApiModelProperty(value = "申报状态中文名称", dataType = "string", notes = "申报状态中文名称")
        private String statusName;

        @ApiModelProperty(value = "事项id", dataType = "string[]", notes = "事项id")
        private String itemIds;

        private String iteminstId;
        @ApiModelProperty("事项实例历时")
        private Double iteminstRunTime;
        @ApiModelProperty(value = "承诺办结时限数字")
        private Double dueNum;
        @ApiModelProperty("事项实例开始时间")
        private Date iteminstStartTime;
        @ApiModelProperty("事项实例结束时间")
        private Date iteminstEndTime;
        @ApiModelProperty("实施主体")
        private String orgName;
        @ApiModelProperty("事项名称")
        private String itemName;
        @ApiModelProperty("事项编码")
        private String itemCode;

        @ApiModelProperty(value = "是否并行事项", notes = "1: 并行， 0: 并联")
        private String isParallel;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DiagramItemDto that = (DiagramItemDto) o;

            if (!Objects.equals(itemVerId, that.itemVerId)) return false;
            return Objects.equals(itemId, that.itemId);

        }

        @Override
        public int hashCode() {
            int result = itemVerId != null ? itemVerId.hashCode() : 0;
            result = 31 * result + (itemId != null ? itemId.hashCode() : 0);
            return result;
        }
    }

    public static DiagramStageDto build(DiagrameStage diagrameStage) {
        DiagramStageDto diagramStageDto = new DiagramStageDto();
        diagramStageDto.setStageId(diagrameStage.getCurrentStage().getStageId());
        diagramStageDto.setDybzspjdxh(diagrameStage.getCurrentStage().getDybzspjdxh());

        Set<DiagramItemDto> requiredItemDtos = diagrameStage.getParallelStageDiagramItem().getRequiredStageDiagramItems()
                .stream().map(item -> buildDiagramItem(item, "0")).collect(Collectors.toSet());
        Set<DiagramItemDto> stateItemDtos = diagrameStage.getParallelStageDiagramItem().getStateStageDiagramItems()
                .stream().map(item -> buildDiagramItem(item, "0")).collect(Collectors.toSet());
        Set<DiagramItemDto> optionItemDtos = diagrameStage.getOptionStageDiagramItem().getOptionStageDiagramItems()
                .stream().map(item -> buildDiagramItem(item, "1")).collect(Collectors.toSet());

        diagramStageDto.getDiagramItemList().addAll(requiredItemDtos);
        diagramStageDto.getDiagramItemList().addAll(stateItemDtos);
        diagramStageDto.getDiagramItemList().addAll(optionItemDtos);
        return diagramStageDto;
    }

    private static DiagramItemDto buildDiagramItem(DiagramItem diagramItem, String isParallel) {
        DiagramItemDto diagramItemDto = new DiagramItemDto();
        diagramItemDto.setItemVerId(diagramItem.getItemVerId());
        diagramItemDto.setItemId(diagramItem.getItemId());
        diagramItemDto.setStatusValue(diagramItem.getFinished().getValue());
        diagramItemDto.setStatusName(diagramItem.getFinished().getName());
        AeaHiIteminst hiIteminst = diagramItem.getAeaHiIteminst();
        diagramItemDto.setIsParallel(isParallel);
        if(hiIteminst != null){
            diagramItemDto.setIteminstStartTime(hiIteminst.getStartTime());
            diagramItemDto.setIteminstEndTime(hiIteminst.getEndTime());
            diagramItemDto.setIteminstId(hiIteminst.getIteminstId());
            diagramItemDto.setOrgName(hiIteminst.getApproveOrgName());
            diagramItemDto.setItemName(diagramItem.getItemName());
            diagramItemDto.setItemCode(diagramItem.getItemCode());
        }

        return diagramItemDto;
    }
}
