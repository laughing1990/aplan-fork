package com.augurit.aplanmis.data.exchange.exception;

import com.augurit.aplanmis.data.exchange.constant.EtlError;

/**
 * @author yinlf
 * @Date 2019/10/18
 */
public class ProjUnbindThemeException extends EtlTransException {

    public ProjUnbindThemeException() {
        super(EtlError.PROJ_UNBIND_THEME);
    }

    public ProjUnbindThemeException(String errValue) {
        super(EtlError.PROJ_UNBIND_THEME);
        this.errValue = errValue;
    }
}