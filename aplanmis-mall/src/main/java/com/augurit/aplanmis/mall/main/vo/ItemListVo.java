package com.augurit.aplanmis.mall.main.vo;


import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "事项一单清VO")
public class ItemListVo {

    @ApiModelProperty(value = "并联事项列表")
    List<ParallelApproveItemVo> parallelItemList;
    @ApiModelProperty(value = "并行事项列表")
    List<ParallelApproveItemVo> coreItemList;
    @ApiModelProperty(value = "情形列表")
    List<AeaParState> stateList;
    @ApiModelProperty(value = "主题名称")
    String themeName;
    @ApiModelProperty(value = "主题ID")
    String themeId;
    @ApiModelProperty(value = "主题版本ID")
    String themeVerId;
    @ApiModelProperty(value = "阶段名称")
    String stageName;
    @ApiModelProperty(value = "阶段ID")
    String stageId;
    @ApiModelProperty(value = "流程图ID")
    String detailId;
    @ApiModelProperty(value = "工作日")
    Double dueNum;
    @ApiModelProperty("这个字段为1，说明阶段配了情形和材料，为0，说明就直接用事项的")
    private String handWay;//这个字段为1，说明阶段配了情形和材料，为0，说明就直接用事项的
}
