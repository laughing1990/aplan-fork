package com.augurit.aplanmis.common.check.exception;

import lombok.Getter;

/**
 * 前置检查异常类
 */
@Getter
public class ItemProjCheckException extends CheckException {

    protected String message = "事项项目信息检查不通过";

    public ItemProjCheckException(String message) {
        super(message);
    }
}
