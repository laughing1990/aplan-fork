package com.augurit.aplanmis.data.exchange.exception;

import com.augurit.aplanmis.data.exchange.constant.EtlError;

/**
 * @author yinlf
 * @Date 2019/10/18
 */
public class ItemNotFindException extends EtlTransException {

    public ItemNotFindException() {
        super(EtlError.ITEM_NOT_FOUND);
    }

    public ItemNotFindException(String errValue) {
        super(EtlError.ITEM_NOT_FOUND);
        this.errValue = errValue;
    }
}