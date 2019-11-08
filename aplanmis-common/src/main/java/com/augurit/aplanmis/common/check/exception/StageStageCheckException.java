package com.augurit.aplanmis.common.check.exception;

/**
 * 前置检查异常类
 */
public class StageStageCheckException extends CheckException {

    private static final String message = "阶段的前置阶段检查不通过";

    public StageStageCheckException() {
        super(message);
    }
}
