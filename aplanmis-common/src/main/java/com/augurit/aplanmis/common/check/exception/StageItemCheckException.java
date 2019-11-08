package com.augurit.aplanmis.common.check.exception;

/**
 * 前置检查异常类
 */
public class StageItemCheckException extends CheckException {

    private static final String message = "阶段前置事项检查不通过";

    public StageItemCheckException() {
        super(message);
    }
}
