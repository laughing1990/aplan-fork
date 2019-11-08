package com.augurit.aplanmis.front.subject.project.vo;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.dto.StageApplyStatusDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("项目全景图状态vo")
public class DiagramStatusVo {

    @ApiModelProperty(value = "项目id", dataType = "string", notes = "项目id")
    private String projInfoId;

    @ApiModelProperty(value = "项目编码", dataType = "string", notes = "项目id")
    private String localCode;

    @ApiModelProperty(value = "主题版本id", dataType = "string", notes = "主题版本id")
    private String themeVerId;

    @ApiModelProperty(value = "全景图json", dataType = "string", notes = "全景图json")
    private String diagramJson;

    @ApiModelProperty(value = "阶段状态信息", notes = "阶段信息, 列表")
    List<DiagramStage> diagramStageStatusList;

    @ApiModelProperty(value = "标准事项状态信息", notes = "标准并联事项状态信息， 列表")
    List<DiagramItem> diagramItemList;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ApiModel("全景图阶段")
    public static class DiagramStage {

        @ApiModelProperty(value = "阶段id", dataType = "string", notes = "阶段id")
        private String stageId;

        @ApiModelProperty(value = "阶段实例id", dataType = "string", notes = "阶段实例id")
        private String stageinstId;

        @ApiModelProperty(value = "申报状态", dataType = "string", notes = "申报状态")
        private String statusValue;

        @ApiModelProperty(value = "申报状态中文名称", dataType = "string", notes = "申报状态中文名称")
        private String statusName;

        @ApiModelProperty(value = "标准并联事项状态信息", notes = "标准并联事项状态信息， 列表")
        List<DiagramItem> diagramItemList;

        DiagramStage(String stageId) {
            this.stageId = stageId;
            this.diagramItemList = new ArrayList<>();
        }

        DiagramStage() {
            this(null);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ApiModel("全景图事项")
    public static class DiagramItem {

        @ApiModelProperty(value = "事项版本id", dataType = "string", notes = "事项版本id")
        private String itemVerId;

        @ApiModelProperty(value = "事项id", dataType = "string", notes = "事项id")
        private String itemId;

        @ApiModelProperty(value = "申报状态", dataType = "string", notes = "申报状态")
        private String statusValue;

        @ApiModelProperty(value = "申报状态中文名称", dataType = "string", notes = "申报状态中文名称")
        private String statusName;
    }

    private DiagramStatusVo() {
        this.diagramStageStatusList = new ArrayList<>();
        this.diagramItemList = new ArrayList<>();
    }

    public DiagramStatusVo(String projInfoId) {
        this();
        this.projInfoId = projInfoId;
    }

    /**
     * 根据阶段实例构建阶段的办理状态
     *
     * @param stageApplyStatusDto 阶段历史办理dto
     */
    public static DiagramStage build(StageApplyStatusDto stageApplyStatusDto) {
        DiagramStage diagramStage = new DiagramStage();
        diagramStage.setStageId(stageApplyStatusDto.getStageId());
        diagramStage.setStageinstId(stageApplyStatusDto.getStageinstId());
        diagramStage.setStatusValue(stageApplyStatusDto.getApplyStateValue());
        diagramStage.setStatusName(stageApplyStatusDto.getApplyStateName());
        return diagramStage;
    }

    public static DiagramItem build(AeaHiIteminst iteminst) {
        DiagramItem diagramItem = new DiagramItem();
        diagramItem.setItemVerId(iteminst.getItemVerId());
        diagramItem.setItemId(iteminst.getItemId());
        if (StringUtils.isNotBlank(iteminst.getIteminstState())) {
            ItemStatus itemStatus = ItemStatus.fromValue(iteminst.getIteminstState());
            diagramItem.setStatusValue(itemStatus.getValue());
            diagramItem.setStatusName(itemStatus.getName());
        }
        return diagramItem;
    }
}
