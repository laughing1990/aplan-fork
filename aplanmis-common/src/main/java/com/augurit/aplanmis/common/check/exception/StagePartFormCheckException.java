package com.augurit.aplanmis.common.check.exception;

/**
 * 前置检查异常类
 */
public class StagePartFormCheckException extends CheckException {

    private static final String message = "阶段前置扩展表单检查不通过";

    public StagePartFormCheckException() {
        super(message);
    }
}
