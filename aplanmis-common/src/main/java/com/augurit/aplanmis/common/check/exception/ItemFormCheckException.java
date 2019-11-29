package com.augurit.aplanmis.common.check.exception;

import lombok.Getter;

/**
 * 前置检查异常类
 */
@Getter
public class ItemFormCheckException extends CheckException {

    protected String message = "事项的前置智能表单检查不通过";

    public ItemFormCheckException(String message) {
        super(message);
    }

}
