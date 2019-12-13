package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("并联审批返回结果vo")
public class ParallelApplyResultVo {
    @ApiModelProperty(value = "项目名称")
    private String projName;
    @ApiModelProperty(value = "项目类型")
    private String projType;
    @ApiModelProperty(value = "项目代码")
    private String localCode;
    @ApiModelProperty(value = "工程编码")
    private String gcbm;
    //@ApiModelProperty(value = "主题-阶段名称")
    //private String themeStageName;
    @ApiModelProperty(value = "申报流水号")
    private String applyinstCode;

    private List<ApplyInstantiateResult> applyInstantiateResults;
    @ApiModelProperty(value = "并联事项名称列表")
    private List<String> parallelItemNames;
    /*@ApiModelProperty(value = "申报主体")
    private List<String> applySubject;*/
    @ApiModelProperty(value = "并行事项列表")
    private List<AeaCoreItemVo> coreItemList;
    @ApiModelProperty(value = "并行事项列表")
    private List<AeaParaItemVo> paraItemVoList;
    @ApiModelProperty(value = "是否只并行事项申报 1是 0否")
    private String isCoreApply="1";
    @ApiModelProperty(value = "申报实例ID集合")
    private List<String> applyinstIds;
    @ApiModelProperty(value = "并行事项及其申请实例ID集合")
    private List<PropulsionItemApplyinstIdVo> propulsionItemApplyinstIdVos;

//    @ApiModelProperty(value = "回执VO")
//    private ReceiveVo reveiceVo;

}
