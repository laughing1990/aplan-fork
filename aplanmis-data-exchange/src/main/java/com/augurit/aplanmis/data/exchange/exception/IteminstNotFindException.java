package com.augurit.aplanmis.data.exchange.exception;

import com.augurit.aplanmis.data.exchange.constant.EtlError;

/**
 * @author yinlf
 * @Date 2019/10/18
 */
public class IteminstNotFindException extends EtlTransException {

    public IteminstNotFindException() {
        super(EtlError.ITEMINST_NOT_FOUND);
    }

    public IteminstNotFindException(String errValue) {
        super(EtlError.ITEMINST_NOT_FOUND);
        this.errValue = errValue;
    }
}