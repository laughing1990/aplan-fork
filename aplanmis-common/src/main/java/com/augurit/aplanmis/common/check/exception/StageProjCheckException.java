package com.augurit.aplanmis.common.check.exception;

import lombok.Getter;

/**
 * 前置检查异常类
 */
@Getter
public class StageProjCheckException extends CheckException {

    protected String message = "阶段项目信息检查不完整";

    public StageProjCheckException(String message) {
        super(message);
    }
}
