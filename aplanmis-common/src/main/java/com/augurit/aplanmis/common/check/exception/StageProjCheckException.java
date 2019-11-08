package com.augurit.aplanmis.common.check.exception;

/**
 * 前置检查异常类
 */
public class StageProjCheckException extends CheckException {

    private static final String message = "阶段项目信息检查不完整";

    public StageProjCheckException() {
        super(message);
    }
}
