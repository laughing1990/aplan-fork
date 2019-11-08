package com.augurit.aplanmis.common.exception;

import com.augurit.agcloud.framework.exception.AgCloudRuntimeException;

/**
 * @author yinlf
 * @Date 2019/7/9
 */
public class ProjectCodeDuplicateException extends AgCloudRuntimeException {

    public static final String CODE = "201";

    public ProjectCodeDuplicateException(String errorMsg, String solution) {
        super(CODE, errorMsg, solution);
    }

    public ProjectCodeDuplicateException(String errorMsg, String solution, Throwable cause) {
        super(CODE, errorMsg, solution, cause);
    }
}
