package com.augurit.aplanmis.data.exchange.exception;

import com.augurit.aplanmis.data.exchange.constant.EtlError;

/**
 * @author yinlf
 * @Date 2019/12/26
 */
public class ProjPassTimeUploadException extends EtlTransException {

    public ProjPassTimeUploadException() {
        super(EtlError.PASS_TIME_UPLOAD_ERROR);
    }

    public ProjPassTimeUploadException(String errValue) {
        super(EtlError.PASS_TIME_UPLOAD_ERROR);
        this.errValue = errValue;
    }

    public ProjPassTimeUploadException(String errValue, Exception e) {
        super(EtlError.PASS_TIME_UPLOAD_ERROR.getName(), EtlError.PASS_TIME_UPLOAD_ERROR.getValue() + ":" + e.getMessage(), null);
        this.errorMsg = EtlError.PASS_TIME_UPLOAD_ERROR.getValue() + ":" + e.getMessage();
    }
}