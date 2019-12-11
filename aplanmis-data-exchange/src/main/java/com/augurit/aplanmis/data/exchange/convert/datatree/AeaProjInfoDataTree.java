package com.augurit.aplanmis.data.exchange.convert.datatree;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AeaProjInfoDataTree extends DataTree {
    @ApiModelProperty("主键ID")
    private java.lang.String projInfoId; // (主键)
    @ApiModelProperty("地方编码")
    private java.lang.String localCode; // (地方编码)
    @ApiModelProperty("项目名称")
    private java.lang.String projName; // (项目名称)
    @ApiModelProperty("工程编码")
    private java.lang.String gcbm; // (工程编码)
}
