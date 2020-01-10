package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AeaRegionOptionVo {

    @ApiModelProperty("区域ID")
    private String value;

    @ApiModelProperty("区域名称")
    private String label;

    @ApiModelProperty("下级区域")
    private List<AeaRegionOptionVo> children;
}
