package com.augurit.aplanmis.supermarket.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tiantian
 * @date 2019/6/13
 */
@Data
@ApiModel("请求返回")
public class RestResult {

    @ApiModelProperty(value = "请求结果true/false")
    protected boolean success;
    @ApiModelProperty(value = "返回信息，success为false时为错误信息")
    protected String message;

    public RestResult(boolean success) {
        this.success = success;
        this.message = "";
    }

    public RestResult(boolean success, String message) {
        this(success);
        this.message = message;
    }
}
