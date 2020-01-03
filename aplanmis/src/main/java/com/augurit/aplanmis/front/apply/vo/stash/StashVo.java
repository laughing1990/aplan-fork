package com.augurit.aplanmis.front.apply.vo.stash;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplySource;
import com.augurit.aplanmis.common.utils.BusinessUtil;
import com.augurit.aplanmis.front.apply.vo.ParallelItemStateVo;
import com.augurit.aplanmis.front.apply.vo.PropulsionItemApplyinstVo;
import com.augurit.aplanmis.front.apply.vo.PropulsionItemStateVo;
import com.augurit.aplanmis.front.apply.vo.StageApplyDataVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApiModel("暂存vo")
@Getter
@Setter
public class StashVo {

    @ApiModelProperty(value = "主题版本id")
    protected String themeVerId;

    @ApiModelProperty(value = "阶段id")
    protected String stageId;

    @ApiModelProperty(value = "项目id")
    protected String projInfoId;

    @ApiModelProperty(value = "申报实例id", notes = "为空的时候说明之前没有进行实例化")
    protected String applyinstId;

    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    protected String applySubject;

    @ApiModelProperty(value = "联系人ID", required = true)
    protected String linkmanInfoId;

    @ApiModelProperty(value = "分局承办，格式为：[{\"itemVerId\":\"111\",\"branchOrg\":\"222\"}]", required = true)
    protected String branchOrgMap;

    @ApiModelProperty(value = "材料实例ID集合")
    protected String[] matinstsIds;

    @ApiModelProperty(value = "是否绿色通道", notes = "1: 是, 0: 否")
    protected String isGreenWay;

    @ApiModel("并联申报暂存vo")
    @Getter
    @Setter
    public static class ParallelStashVo extends StashVo {

        @ApiModelProperty(value = "并联申报事项版本ID")
        private List<String> itemVerIds;

        @ApiModelProperty(value = "情形ID集合", required = true)
        private String[] stateIds;

        @ApiModelProperty(value = "简单合并申报，选择的并联事项情形", dataType = "string")
        private List<ParallelItemStateVo> parallelItemStateIds;

        @ApiModelProperty(value = "并行推进事项版本ID", dataType = "java.util.List")
        private List<String> propulsionItemVerIds;

        @ApiModelProperty(value = "并行事项情形Map集合")
        private List<PropulsionItemStateVo> propulsionItemStateIds;

        @ApiModelProperty(value = "并行推进事项分局承办", dataType = "string")
        private String propulsionBranchOrgMap;

        @ApiModelProperty(value = "并行事项申报实例id")
        private List<PropulsionItemApplyinstVo> seriesApplyinstIds;

        public List<SeriesStashVo> toSeriesStashVos() {
            List<SeriesStashVo> seriesStashVos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(this.getPropulsionItemVerIds())) {
                Map<String, String> itemApplyinstMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(this.seriesApplyinstIds)) {
                    itemApplyinstMap = seriesApplyinstIds.stream().collect(Collectors.toMap(PropulsionItemApplyinstVo::getItemVerId, PropulsionItemApplyinstVo::getSeriesApplyinstId));
                }
                Map<String, List<String>> itemStateMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(this.propulsionItemStateIds)) {
                    itemStateMap = propulsionItemStateIds.stream().collect(Collectors.toMap(PropulsionItemStateVo::getItemVerId, PropulsionItemStateVo::getStateIds));
                }
                for (String propulsionItemVerId : this.propulsionItemVerIds) {
                    String[] propulsionStateIds = itemStateMap.get(propulsionItemVerId) == null ? null : itemStateMap.get(propulsionItemVerId).toArray(new String[0]);
                    String propulsionBranchOrgMap = BusinessUtil.getOrgIdFromBranchOrgMap(this.propulsionBranchOrgMap, propulsionItemVerId);
                    if (StringUtils.isNotBlank(propulsionBranchOrgMap)) {
                        propulsionBranchOrgMap = "[{\"itemVerId\":\"" + propulsionItemVerId + "\", \"branchOrg\": \"" + propulsionBranchOrgMap + "\"}]";
                    }
                    String seriesApplyinstId = itemApplyinstMap.get(propulsionItemVerId);

                    SeriesStashVo seriesStashVo = new SeriesStashVo();
                    seriesStashVo.setItemVerId(propulsionItemVerId);
                    seriesStashVo.setStateIds(propulsionStateIds);
                    seriesStashVo.setIsParallel(Status.ON);
                    seriesStashVo.setThemeVerId(this.themeVerId);
                    seriesStashVo.setStageId(this.stageId);
                    seriesStashVo.setProjInfoId(this.projInfoId);
                    seriesStashVo.setApplyinstId(seriesApplyinstId);
                    seriesStashVo.setApplySubject(this.applySubject);
                    seriesStashVo.setLinkmanInfoId(this.linkmanInfoId);
                    seriesStashVo.setBranchOrgMap(StringUtils.isNotBlank(propulsionBranchOrgMap) ? propulsionBranchOrgMap : null);
                    seriesStashVo.setMatinstsIds(this.matinstsIds);
                    seriesStashVo.setParallelApplyinstId(this.applyinstId);
                    seriesStashVos.add(seriesStashVo);
                }
            }
            return seriesStashVos;
        }

        public StageApplyDataVo toStageApplyDataVo() {
            StageApplyDataVo stageApplyDataVo = new StageApplyDataVo();
            stageApplyDataVo.setApplySource(ApplySource.WIN.getValue());
            stageApplyDataVo.setApplySubject(this.applySubject);
            stageApplyDataVo.setLinkmanInfoId(this.linkmanInfoId);
            stageApplyDataVo.setBranchOrgMap(this.branchOrgMap);
            stageApplyDataVo.setStageId(this.stageId);
            stageApplyDataVo.setThemeVerId(this.themeVerId);
            return stageApplyDataVo;
        }
    }

    @ApiModel("单项申报暂存vo")
    @Getter
    @Setter
    public static class SeriesStashVo extends StashVo {

        @ApiModelProperty(value = "事项版本id")
        private String itemVerId;

        @ApiModelProperty(value = "情形ID集合", required = true)
        private String[] stateIds;

        @ApiModelProperty(value = "是否并行推行", notes = "0: 否，1: 是")
        private String isParallel;

        @ApiModelProperty(value = "并联申报实例id", notes = "并联申报时， 并行申报需要与并联申报关联", hidden = true)
        private String parallelApplyinstId;

    }
}




