package com.augurit.aplanmis.mall.userCenter.vo;

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
    @ApiModelProperty(value = "单位ID", required = true)
    private String unitInfoId;
    @ApiModelProperty(value = "联系人ID", required = true)
    private String linkmanInfoId;
    @ApiModelProperty(value = "事项版本ID", required = true)
    private String itemVerId;
    @ApiModelProperty(value = "领件人信息对象id", required = true)
    @NotEmpty(message = "smsInfoVo is null")
    private String smsInfoId;
    //@ApiModelProperty(value = "分局承办；并行推进事项分局承办", dataType = "string")
    //private String branchOrgMap;
    @ApiModelProperty(value = "项目ID集合", required = true)
    private String[] projInfoIds;
    //@ApiModelProperty(value = "经办单位ID集合")
    //private String[] handleUnitIds;
   // @ApiModelProperty(value = "建设单位Map集合")
    //private List<BuildProjUnitVo> buildProjUnitMap;
    @ApiModelProperty(value = "材料实例ID集合")
    private String[] matinstsIds;
    //@ApiModelProperty(value = "办理意见", required = true)
    //private String comments;
    //@ApiModelProperty(value = "申请联系人ID")
    //private String applyLinkmanId;
    @ApiModelProperty(value = "情形ID集合")
    private String[] stateIds;
    @ApiModelProperty(value = "申请联系人ID")
    private String applyLinkmanId;
    @ApiModelProperty(value = "建设单位Map集合")
    private List<BuildProjUnitVo> buildProjUnitMap;
    @ApiModelProperty(value = "单位项目关联ID", required = true)
    private List<String> projUnitIds;

    public SeriesApplyDataVo toSeriesApplyDataVo(String appId) {
        SeriesApplyDataVo seriesApplyDataVo = new SeriesApplyDataVo();
        seriesApplyDataVo.setApplyinstId(this.applyinstId);
        seriesApplyDataVo.setApplySource(this.applySource);
        seriesApplyDataVo.setApplySubject(this.applySubject);
        seriesApplyDataVo.setLinkmanInfoId(this.linkmanInfoId);
        seriesApplyDataVo.setAppId(appId);
        seriesApplyDataVo.setItemVerId(this.itemVerId);
        seriesApplyDataVo.setProjInfoIds(this.projInfoIds);
        seriesApplyDataVo.setMatinstsIds(this.matinstsIds);
        seriesApplyDataVo.setStateIds(this.stateIds);
        seriesApplyDataVo.setUnitInfoId(this.unitInfoId);
        seriesApplyDataVo.setApplyLinkmanId(this.applyLinkmanId);
        seriesApplyDataVo.setBuildProjUnitMap(this.buildProjUnitMap);
        seriesApplyDataVo.setProjUnitIds(this.projUnitIds);
        return seriesApplyDataVo;
    }

}
