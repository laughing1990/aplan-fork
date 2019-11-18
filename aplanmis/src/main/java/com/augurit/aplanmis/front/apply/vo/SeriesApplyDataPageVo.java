package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel("单项申报参数实体vo")
public class SeriesApplyDataPageVo {
    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;
    @ApiModelProperty(value = "申报来源，网上申报：net、窗口申报：win", required = true, allowableValues = "net, win")
    private String applySource;
    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    private String applySubject;
    @ApiModelProperty(value = "联系人ID", required = true)
    private String linkmanInfoId;
    @ApiModelProperty(value = "事项版本ID", required = true)
    private String itemVerId;
    @ApiModelProperty(value = "领件人信息对象id", required = true)
    @NotEmpty(message = "smsInfoVo is null")
    private String smsInfoId;
    @ApiModelProperty(value = "分局承办；并行推进事项分局承办", dataType = "string")
    private String branchOrgMap;
    @ApiModelProperty(value = "项目ID集合", required = true)
    private String[] projInfoIds;
    @ApiModelProperty(value = "经办单位ID集合")
    private String[] handleUnitIds;
    @ApiModelProperty(value = "建设单位Map集合")
    private List<BuildProjUnitVo> buildProjUnitMap;
    @ApiModelProperty(value = "材料实例ID集合")
    private String[] matinstsIds;
    @ApiModelProperty(value = "办理意见", required = true)
    private String comments;
    @ApiModelProperty(value = "申请联系人ID")
    private String applyLinkmanId;
    @ApiModelProperty(value = "情形ID集合")
    private String[] stateIds;
    @ApiModelProperty(value = "是否并行推行", dataType = "string", notes = "0表示否，1表示是")
    private String isParallel;
    @ApiModelProperty(value = "阶段ID", dataType = "string")
    private String stageId;
    @ApiModelProperty(value = "是否只实例化了申报实例", notes = "1: 是; 0: 否")
    private String isJustApplyinst;

    public SeriesApplyDataVo toSeriesApplyDataVo(String appId) {
        SeriesApplyDataVo seriesApplyDataVo = new SeriesApplyDataVo();
        seriesApplyDataVo.setApplyinstId(this.applyinstId);
        seriesApplyDataVo.setApplySource(this.applySource);
        seriesApplyDataVo.setApplySubject(this.applySubject);
        seriesApplyDataVo.setLinkmanInfoId(this.linkmanInfoId);
        seriesApplyDataVo.setAppId(appId);
        seriesApplyDataVo.setItemVerId(this.itemVerId);
        seriesApplyDataVo.setBranchOrgMap(this.branchOrgMap);
        seriesApplyDataVo.setProjInfoIds(this.projInfoIds);
        seriesApplyDataVo.setHandleUnitIds(this.handleUnitIds);
        seriesApplyDataVo.setBuildProjUnitMap(this.buildProjUnitMap);
        seriesApplyDataVo.setMatinstsIds(this.matinstsIds);
        seriesApplyDataVo.setComments(this.comments);
        seriesApplyDataVo.setApplyLinkmanId(this.applyLinkmanId);
        seriesApplyDataVo.setStateIds(this.stateIds);
        seriesApplyDataVo.setStageId(this.stageId);
        seriesApplyDataVo.setIsParallel(this.isParallel);
        seriesApplyDataVo.setIsJustApplyinst(this.isJustApplyinst);
        return seriesApplyDataVo;
    }

}
