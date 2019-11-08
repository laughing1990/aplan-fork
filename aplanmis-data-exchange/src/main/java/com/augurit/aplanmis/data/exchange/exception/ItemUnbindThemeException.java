package com.augurit.aplanmis.data.exchange.exception;

import com.augurit.aplanmis.data.exchange.constant.EtlError;

/**
 * @author yinlf
 * @Date 2019/10/18
 */
public class ItemUnbindThemeException extends EtlTransException {

    public ItemUnbindThemeException() {
        super(EtlError.ITEM_UNBIND_THEME);
    }

    public ItemUnbindThemeException(String errValue) {
        super(EtlError.ITEM_UNBIND_THEME);
        this.errValue = errValue;
    }
}