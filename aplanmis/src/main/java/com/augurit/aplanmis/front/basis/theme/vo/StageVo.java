package com.augurit.aplanmis.front.basis.theme.vo;

import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.dto.StageApplyStatusDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("并联申报中根据主题查询阶段信息")
@Data
public class StageVo {
    @ApiModelProperty(name = "stageId", value = "阶段id", dataType = "string")
    private String stageId;
    @ApiModelProperty(name = "themeVerId", value = "主题版本id", dataType = "string")
    private String themeVerId;
    @ApiModelProperty(name = "stageName", value = "阶段名称", dataType = "string")
    private String stageName;
    @ApiModelProperty(name = "appId", value = "阶段流程模板id", dataType = "string")
    private String appId;
    @ApiModelProperty(name = "isNeedState", value = "是否需要分情形", dataType = "string", allowableValues = "0, 1")
    private String isNeedState;
    @ApiModelProperty(name = "middleImgPath", value = "中等图标", dataType = "string")
    private String middleImgPath;
    @ApiModelProperty(name = "bigImgPath", value = "大图标", dataType = "string")
    private String bigImgPath;
    @ApiModelProperty(name = "hugeImgPath", value = "超大图标", dataType = "string")
    private String hugeImgPath;
    @ApiModelProperty(name = "isDeleted", value = "是否删除", dataType = "string", allowableValues = "0, 1")
    private String isDeleted;
    @ApiModelProperty(name = "stageCode", value = "阶段编码", dataType = "string")
    private String stageCode;
    @ApiModelProperty(name = "sortNo", value = "阶段排序", dataType = "string")
    private String sortNo;
    @ApiModelProperty(name = "isSelItem", value = "申报时是否允许窗口人员选择必选事项  1允许 0不允许", dataType = "string")
    private String isSelItem;
    @ApiModelProperty(name = "isOptionItem", value = "是否允许可选事项 0表示不允许 1表示允许", dataType = "string")
    private String isOptionItem;
    @ApiModelProperty(value = "1表示主线，2表示辅线 3技术审查")
    private String isNode;
    @ApiModelProperty(value = "处理方式 0 多事项直接合并办理  1 按阶段多级情形组织事项办理", dataType = "string")
    private String handWay;
    @ApiModelProperty(value = "是否启用一张表单 0 禁用 1允许'", dataType = "string")
    private String useOneForm;
    @ApiModelProperty(value = "1 立项用地规划许可 2 立项用地规划许可 3 施工许可 4 竣工验收 5 并行推进")
    private String dybzspjdxh;

    @ApiModelProperty(value = "阶段办理状态", notes = "0：未申报, 1：进行中, 2：已通过")
    private String stageState;

    @ApiModelProperty(value = "已办理或正在办理的阶段实例信息")
    private StageApplyStatusDto appliedStage;
    @ApiModelProperty(value = "关联的")
    private List<StageApplyStatusDto> helperStages;

    @ApiModelProperty(value = "申报时必选事项显示数量")
    private Long noptItemShowNum;
    @ApiModelProperty(value = "申报时可选事项显示数量")
    private Long optItemShowNum;

    @ApiModelProperty(value = "法定办结时限")
    protected Double anticipateDay;

    @ApiModelProperty(value = "法定办结时限单位")
    protected String anticipateType;

    @ApiModelProperty(value = "是否允许创建子工程：1 允许，0 禁止")
    protected String isAllowChildProjectCreation;

    public StageVo() {
        this.helperStages = new ArrayList<>();
    }

    public static StageVo build(AeaParStage aeaParStage) {
        StageVo stageVo = new StageVo();
        stageVo.setStageId(aeaParStage.getStageId());
        stageVo.setThemeVerId(aeaParStage.getThemeVerId());
        stageVo.setStageName(aeaParStage.getStageName());
        stageVo.setAppId(aeaParStage.getAppId());
        stageVo.setIsNeedState(aeaParStage.getIsNeedState());
        stageVo.setMiddleImgPath(aeaParStage.getMiddleImgPath());
        stageVo.setBigImgPath(aeaParStage.getBigImgPath());
        stageVo.setHugeImgPath(aeaParStage.getHugeImgPath());
        stageVo.setIsDeleted(aeaParStage.getIsDeleted());
        stageVo.setStageCode(aeaParStage.getStageCode());
        stageVo.setSortNo(String.valueOf(aeaParStage.getSortNo()));
        stageVo.setIsSelItem(aeaParStage.getIsSelItem());
        stageVo.setIsOptionItem(aeaParStage.getIsOptionItem());
        stageVo.setIsNode(aeaParStage.getIsNode());
        stageVo.setHandWay(aeaParStage.getHandWay());
        stageVo.setUseOneForm(aeaParStage.getUseOneForm());
        stageVo.setNoptItemShowNum(aeaParStage.getNoptItemShowNum());
        stageVo.setOptItemShowNum(aeaParStage.getOptItemShowNum());
        stageVo.setDybzspjdxh(aeaParStage.getDybzspjdxh());
        stageVo.setAnticipateDay(aeaParStage.getAnticipateDay());
        stageVo.setAnticipateType(aeaParStage.getAnticipateType());
        stageVo.setIsAllowChildProjectCreation(aeaParStage.getIsAllowChildProjectCreation());
        return stageVo;
    }
}
