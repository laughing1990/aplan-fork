package com.augurit.aplanmis.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseRuntimeException extends RuntimeException {
    //默认异常
    private int errorCode = 601;
    private String errorMsg="业务异常";
    private Object obj;

    public BaseRuntimeException(){}
    public BaseRuntimeException(int errorCode, String errorMsg, Object obj) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.obj = obj;
    }
    public BaseRuntimeException(String errorMessage){
        super(errorMessage);
        this.errorMsg = errorMessage;
    }

    public BaseRuntimeException(int errorCode, String errorMessage){
        super(errorMessage);
        this.errorMsg = errorMessage;
        this.errorCode = errorCode;
    }

    public BaseRuntimeException(int errorCode, String errorMessage, Throwable cause){
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMessage;
    }

    public BaseRuntimeException(String errorMessage, Throwable cause){
        super(errorMessage, cause);
        this.errorMsg = errorMessage;
    }

    public BaseRuntimeException(Throwable cause){
        super(cause);
    }

    @Override
    public String toString(){
        if (getCause() == null) {
            return super.toString();
        }
        return super.toString() + " cause: " + getCause().toString();
    }
}
