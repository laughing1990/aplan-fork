package com.augurit.aplanmis.common.check.exception;

import lombok.Getter;

/**
 * 前置检查异常类
 */
@Getter
public class CheckException extends Exception {

    protected String message;

    public CheckException(String message) {
        super(message);
    }

}
