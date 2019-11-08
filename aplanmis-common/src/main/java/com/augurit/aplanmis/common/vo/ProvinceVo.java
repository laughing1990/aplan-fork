package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 省份
 */
@Data
@NoArgsConstructor
@ApiModel("省份")
public class ProvinceVo implements Serializable {
    @ApiModelProperty(value = "省份编码", required = true, dataType="string")
    private String code;
    @ApiModelProperty(value = "省份名称", required = true, dataType="string")
    private String name;
    @ApiModelProperty(value = "所属省份城市", required = true, dataType = "java.util.List")
    private List<CityVo> cityList;

    public ProvinceVo(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
