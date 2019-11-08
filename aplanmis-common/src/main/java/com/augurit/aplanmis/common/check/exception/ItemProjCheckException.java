package com.augurit.aplanmis.common.check.exception;

/**
 * 前置检查异常类
 */
public class ItemProjCheckException extends CheckException {

    private static final String message = "事项项目信息检查不通过";

    public ItemProjCheckException() {
        super(message);
    }
}
