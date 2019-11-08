package com.augurit.aplanmis.common.check.exception;

/**
 * 前置检查异常类
 */
public class ItemFormCheckException extends CheckException {

    private static final String message = "事项的前置智能表单检查不通过";

    public ItemFormCheckException() {
        super(message);
    }
}
