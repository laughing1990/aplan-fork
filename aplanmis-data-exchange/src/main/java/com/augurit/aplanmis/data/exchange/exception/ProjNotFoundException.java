package com.augurit.aplanmis.data.exchange.exception;

import com.augurit.aplanmis.data.exchange.constant.EtlError;

/**
 * @author yinlf
 * @Date 2019/10/18
 */
public class ProjNotFoundException extends EtlTransException {

    public ProjNotFoundException() {
        super(EtlError.PROJ_NOT_FOUND);
    }

    public ProjNotFoundException(String errValue) {
        super(EtlError.PROJ_NOT_FOUND);
        this.errValue = errValue;
    }
}