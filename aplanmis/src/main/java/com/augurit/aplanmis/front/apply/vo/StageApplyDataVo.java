package com.augurit.aplanmis.front.apply.vo;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.utils.BusinessUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 并联申报参数实体
 */
@Getter
@Setter
@ApiModel("并联申报vo")
public class StageApplyDataVo extends ApplyDataVo {
    @ApiModelProperty(value = "并行申请实例ID列表")
    private String[] applyinstIds;

    @ApiModelProperty(value = "并联申请实例ID")
    private String parallelApplyinstId;

    @ApiModelProperty(value = "主题版本ID")
    private String themeVerId;

    @ApiModelProperty(value = "并联申报事项版本ID", required = true)
    private List<String> itemVerIds;

    @ApiModelProperty(value = "简单合并申报，选择的并联事项情形", dataType = "string")
    private List<ParallelItemStateVo> parallelItemStateIds;

    @ApiModelProperty(value = "并行推进事项版本ID", required = true)
    private List<String> propulsionItemVerIds;

    @ApiModelProperty(value = "并行推进事项分局承办，格式为：[{\"itemVerId\":\"111\",\"branchOrg\":\"222\"}]", required = true)
    private String propulsionBranchOrgMap;

    @ApiModelProperty(value = "并行事项情形Map集合，key为itemVerId,格式为[{itemVerId:[stateId1,stateId2]}]", required = true)
    private List<PropulsionItemStateVo> propulsionItemStateIds;

    @ApiModelProperty(value = "是否实例化过回执 1是 0否", required = true)
    private String isPrintReceive;

    public StageApplyDataVo() {
        applyType = ApplyType.UNIT;
    }

    /**
     * 并行事项申报，vo转换
     *
     * @param stageApplyDataVo 并联参数vo
     * @return 单项申报参数
     */
    public static List<SeriesApplyDataVo> toSeriesApplyDataVosWhenPropulsionApply(StageApplyDataVo stageApplyDataVo, String parallelApplyinstId) {
        List<String> itemVerIds = stageApplyDataVo.getPropulsionItemVerIds();
        List<SeriesApplyDataVo> seriesApplyDataVos = new ArrayList<>();
        if (CollectionUtils.isEmpty(itemVerIds)) {
            return seriesApplyDataVos;
        }

        // 事项与情形映射
        Map<String, List<String>> propulsionItemStateVoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(stageApplyDataVo.getPropulsionItemStateIds())) {
            propulsionItemStateVoMap = stageApplyDataVo.getPropulsionItemStateIds()
                    .stream().collect(Collectors.toMap(PropulsionItemStateVo::getItemVerId, PropulsionItemStateVo::getStateIds));
        }

        String branchOrgMap = stageApplyDataVo.getPropulsionBranchOrgMap();
        for (String itemVerId : itemVerIds) {
            SeriesApplyDataVo vo = new SeriesApplyDataVo();
            BeanUtils.copyProperties(stageApplyDataVo, vo);

            vo.setItemVerId(itemVerId);
            String orgId = BusinessUtil.getOrgIdFromBranchOrgMap(branchOrgMap, itemVerId);
            if (StringUtils.isNotBlank(orgId)) {
                Map<String, String> map = new HashMap<>();
                map.put("branchOrg", orgId);
                map.put("itemVerId", itemVerId);
                List<Map<String, String>> branchOrg = new ArrayList<>();
                branchOrg.add(map);
                vo.setBranchOrgMap(JSON.toJSONString(branchOrg));
            } else {
                vo.setBranchOrgMap("");
            }
            List<String> itemStateIds = propulsionItemStateVoMap.get(itemVerId);
            vo.setStateIds(CollectionUtils.isNotEmpty(itemStateIds) ? itemStateIds.toArray(new String[0]) : new String[]{});
            vo.setParallelApplyinstId(parallelApplyinstId);
            vo.setIsParallel(Status.ON);
            vo.setApplyType(ApplyType.SERIES);
            seriesApplyDataVos.add(vo);
        }
        return seriesApplyDataVos;
    }
}
