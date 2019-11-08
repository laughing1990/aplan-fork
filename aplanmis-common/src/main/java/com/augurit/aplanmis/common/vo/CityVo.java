package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 地级市
 */
@Data
@NoArgsConstructor
@ApiModel("城市")
public class CityVo implements Serializable {
    @ApiModelProperty(value = "城市编码", required = true, dataType="string")
    private String code;
    @ApiModelProperty(value = "城市名称", required = true, dataType="string")
    private String name;
    @ApiModelProperty(value = "地区", required = true, dataType="array")
    private List<AreaVo> areaList;

    public CityVo(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
