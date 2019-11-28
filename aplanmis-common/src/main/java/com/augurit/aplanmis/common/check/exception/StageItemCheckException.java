package com.augurit.aplanmis.common.check.exception;

import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import lombok.Getter;

import java.util.List;

/**
 * 前置检查异常类
 */
@Getter
public class StageItemCheckException extends CheckException {

    private List<AeaParFrontItem> frontItems;

    protected String message = "阶段前置事项检查不通过";

    public StageItemCheckException(String message) {
        super(message);
    }

    public StageItemCheckException(String message, List<AeaParFrontItem> frontItems) {
        super(message);
        this.frontItems = frontItems;
        this.message = message;
    }
}
