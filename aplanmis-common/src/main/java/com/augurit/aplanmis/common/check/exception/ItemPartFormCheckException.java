package com.augurit.aplanmis.common.check.exception;

/**
 * 前置检查异常类
 */
public class ItemPartFormCheckException extends CheckException {

    private static final String message = "事项的前置扩展表单检查不通过";

    public ItemPartFormCheckException() {
        super(message);
    }
}
