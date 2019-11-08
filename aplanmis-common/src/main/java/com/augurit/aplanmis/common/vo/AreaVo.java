package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 区，县
 */
@Data
@NoArgsConstructor
@ApiModel("地区")
public class AreaVo implements Serializable {
    @ApiModelProperty(value = "地区编码", required = true, dataType="string")
    private String code;
    @ApiModelProperty(value = "地区名称", required = true, dataType="string")
    private String name;

    public AreaVo(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
