package com.augurit.aplanmis.data.exchange.exception;

import com.augurit.agcloud.framework.exception.AgCloudRuntimeException;
import com.augurit.aplanmis.data.exchange.constant.EtlError;

/**
 * @author yinlf
 * @Date 2019/10/13
 */
public class EtlTransException extends AgCloudRuntimeException {

    protected EtlError etlError;

    protected String errValue;

    protected EtlTransException(EtlError etlError) {
        super(etlError.getName(), etlError.getValue(), null);
        this.etlError = etlError;
    }

    protected EtlTransException(EtlError etlError, String solution) {
        super(etlError.getName(), etlError.getValue(), solution);
        this.etlError = etlError;
    }

    public EtlTransException(String code, String errorMsg, String solution) {
        super(code, errorMsg, solution);
    }

    public EtlTransException(String code, String errorMsg, String solution, Throwable cause) {
        super(code, errorMsg, solution, cause);
    }

    public String getErrValue() {
        return errValue;
    }

    public void setErrValue(String errValue) {
        this.errValue = errValue;
    }
}
