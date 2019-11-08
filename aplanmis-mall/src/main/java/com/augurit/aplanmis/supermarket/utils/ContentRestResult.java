package com.augurit.aplanmis.supermarket.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tiantian
 * @date 2019/6/13
 */
@Data
@ApiModel("带数据的请求返回")
public class ContentRestResult<T>  extends RestResult{

    @ApiModelProperty(value = "请求结果数据")
    private T content;

    public ContentRestResult(boolean success) {
        super(success);
    }

    public ContentRestResult(boolean success, T content) {
        super(success);
        this.content = content;
    }

    public ContentRestResult(boolean success, T content, String message) {
        super(success, message);
        this.content = content;
    }

    public T getContent() {
        return this.content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
