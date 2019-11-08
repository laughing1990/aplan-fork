package com.augurit.aplanmis.rest.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("回执vo")
public class ReceiveVo {
    @ApiModelProperty("阶段回执VO")
    private StageReceiveVo stageReceiveVo;
    @ApiModelProperty("事项回执VO")
    private List<ItemReceiveVo> itemReceiveVos;
}
