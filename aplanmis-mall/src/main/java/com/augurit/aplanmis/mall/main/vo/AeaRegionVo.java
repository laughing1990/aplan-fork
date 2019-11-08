package com.augurit.aplanmis.mall.main.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("区划结果集类")
public class AeaRegionVo {
    @ApiModelProperty("第一级单位（省或市）")
    AeaBasicOrgVo firAeaOrgVo;
    @ApiModelProperty("第二级单位（市或县）")
    List<AeaOrgVo> secAeaOrgVo;
}
