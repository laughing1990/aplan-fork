package com.augurit.aplanmis.common.exception;


public class ResultFormException extends BaseRuntimeException {

    public ResultFormException(){};

    public ResultFormException(String errorMessage) { super(errorMessage); }

    public ResultFormException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public ResultFormException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

    public ResultFormException(int errorCode, String errorMessage, Throwable cause) {
        super(errorCode, errorMessage, cause);
    }

    public ResultFormException(Throwable cause) {
        super(cause);
    }

}
