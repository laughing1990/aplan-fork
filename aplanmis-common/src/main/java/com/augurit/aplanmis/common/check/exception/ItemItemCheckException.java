package com.augurit.aplanmis.common.check.exception;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import lombok.Getter;

import java.util.List;

/**
 * 前置检查异常类
 */
@Getter
public class ItemItemCheckException extends CheckException {

    protected String message = "事项的前置事项检查不通过";

    private List<AeaItemBasic> aeaItemFronts;

    public ItemItemCheckException(String message) {
        super(message);
    }

    public ItemItemCheckException(String message, List<AeaItemBasic> aeaItemFronts) {
        super(message);
        this.message = message;
        this.aeaItemFronts = aeaItemFronts;
    }
}
