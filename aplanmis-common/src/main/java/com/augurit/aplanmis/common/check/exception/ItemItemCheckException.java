package com.augurit.aplanmis.common.check.exception;

/**
 * 前置检查异常类
 */
public class ItemItemCheckException extends CheckException {

    private static final String message = "事项的前置事项检查不通过";

    public ItemItemCheckException() {
        super(message);
    }

    public ItemItemCheckException(String message) {
        super(message);
    }
}
