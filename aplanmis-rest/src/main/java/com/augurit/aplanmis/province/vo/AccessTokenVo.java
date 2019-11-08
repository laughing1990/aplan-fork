package com.augurit.aplanmis.province.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:chendx
 * @date: 2019-06-12
 * @time: 14:45
 */
@Data
@ApiModel(value = "获取token返回结果", description = "获取token返回结果")
public class AccessTokenVo {
    @ApiModelProperty(name = "返回结果true or false", value = "返回结果 true or false")
    private boolean result;
    @ApiModelProperty(name = "token", value = "token")
    private String access_token;

    public boolean isResult() {
        return result;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
