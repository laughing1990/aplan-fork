package com.augurit.aplanmis.front.common.vo;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("行政区划vo")
public class BscDicRegionVo {

    @ApiModelProperty(value = "区划id", dataType = "string")
    private String regionId;

    @ApiModelProperty(value = "区划名称", dataType = "string")
    private String regionName;

    @ApiModelProperty(value = "区划编码", dataType = "string")
    private String regionNum;

    public static BscDicRegionVo from(BscDicRegion region) {
        BscDicRegionVo vo = new BscDicRegionVo();
        vo.setRegionId(region.getRegionId());
        vo.setRegionName(region.getRegionName());
        vo.setRegionNum(region.getRegionNum());
        return vo;
    }
}
