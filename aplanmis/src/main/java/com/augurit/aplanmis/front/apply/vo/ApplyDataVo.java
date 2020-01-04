package com.augurit.aplanmis.front.apply.vo;

import com.augurit.aplanmis.common.constants.ApplyType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApplyDataVo {

    @ApiModelProperty(value = "申报来源，网上申报：net、窗口申报：win", required = true, allowableValues = "net, win")
    protected String applySource;

    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    protected String applySubject;

    @ApiModelProperty(value = "联系人ID", required = true)
    protected String linkmanInfoId;

    @ApiModelProperty(value = "领件人信息对象id", required = true)
    protected String smsInfoId;

    @ApiModelProperty(value = "阶段ID")
    protected String stageId;

    @ApiModelProperty(value = "分局承办，格式为：[{\"itemVerId\":\"111\",\"branchOrg\":\"222\"}]", required = true)
    protected String branchOrgMap;

    @ApiModelProperty(value = "项目ID集合", required = true)
    protected String[] projInfoIds;

    @ApiModelProperty(value = "经办单位ID集合", required = true)
    protected String[] handleUnitIds;

    @ApiModelProperty(value = "建设单位Map集合，key为projInfoId,格式为[{projInfoId1:[unitId1,unitId1]}]", required = true)
    protected List<BuildProjUnitVo> buildProjUnitMap;

    @ApiModelProperty(value = "材料实例ID集合", required = true)
    protected String[] matinstsIds;

    @ApiModelProperty(value = "办理意见", required = true)
    protected String comments;

    @ApiModelProperty(value = "申请联系人ID", required = true)
    protected String applyLinkmanId;

    @ApiModelProperty(value = "情形ID集合", required = true)
    protected String[] stateIds;

    @ApiModelProperty(value = "是否只实例化了申报实例", required = true, notes = "0: 初始化, 1: 一张表单暂存后仅仅生成了申报实例, 2: 申报/打印回执/不受理后")
    protected String isJustApplyinst;

    @ApiModelProperty(value = "是否绿色通道", notes = "1: 是, 0: 否")
    protected String isGreenWay;

    @ApiModelProperty(value = "申报类型", hidden = true)
    protected ApplyType applyType;

}
