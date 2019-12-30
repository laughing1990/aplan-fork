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
    @ApiModelProperty(value = "阶段名称")
    String stageName;
    @ApiModelProperty(value = "工作日")
    Double dueNum;
}
