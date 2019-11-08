package com.augurit.aplanmis.common.dto;

import com.augurit.aplanmis.common.domain.AeaHiReceive;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AeaHiReceiveDto {
    @ApiModelProperty("申请实例ID")
    private String applyinstId;
    @ApiModelProperty("申请实例code")
    private String applyinstCode;
    @ApiModelProperty("阶段或事项名称")
    private String name;
    @ApiModelProperty("回执列表")
    private List<AeaHiReceive> receiveList;
    @ApiModelProperty("1 并行事项或单项；0 并联")
    private String isSeries;
}
