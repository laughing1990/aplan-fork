package com.augurit.aplanmis.mall.guide.vo;


import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaParStageGuide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel("")
public class RestGuideVo {

    @ApiModelProperty("并联事项列表")
    private List<AeaItemBasic> concList;
    @ApiModelProperty("并行推进事项")
    private List<AeaItemBasic> parallelList;
    @ApiModelProperty("这个字段为1，说明阶段配了情形和材料，为0，说明就直接用事项的")
    private String handWay;//这个字段为1，说明阶段配了情形和材料，为0，说明就直接用事项的
    @ApiModelProperty("材料列表")
    private List<AeaItemMat> matList;
    @ApiModelProperty("阶段基础信息")
    private AeaParStageGuide aeaParStageGuide;
    @ApiModelProperty("审批对象")
    private String applyObj;
    @ApiModelProperty("申请条件")
    private String acceptCond;
    @ApiModelProperty("申报方式")
    private List<String> sbfsList;
    @ApiModelProperty("设立依据")
    private List<Map> basicList;
    @ApiModelProperty("办理程序")
    private String handleFlow;
    @ApiModelProperty("收费标准")
    private String byLaw;
    @ApiModelProperty("收费依据")
    private String feeStand;
    @ApiModelProperty("办理时限")
    private String handleTimelimitDesc;
    @ApiModelProperty("办理地点")
    private List<String> blddList;
    @ApiModelProperty("受理时间")
    private String slsj;
    @ApiModelProperty("投诉监督电话")
    private String comsupTel;
    @ApiModelProperty("备注")
    private String guideDemo;
    @ApiModelProperty("温馨提示")
    private String warmPrompt;
    @ApiModelProperty("流程图")
    private BscAttForm guideAtt;

}
