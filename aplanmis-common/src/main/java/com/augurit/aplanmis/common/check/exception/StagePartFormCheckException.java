package com.augurit.aplanmis.common.check.exception;

import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import lombok.Getter;

import java.util.List;

/**
 * 前置检查异常类
 */
@Getter
public class StagePartFormCheckException extends CheckException {

    protected String message = "阶段前置扩展表单检查不通过";

    private List<AeaParFrontPartform> aeaParFrontPartforms;

    public StagePartFormCheckException(String message) {
        super(message);
    }

    public StagePartFormCheckException(String message, List<AeaParFrontPartform> aeaParFrontPartforms) {
        super(message);
        this.message = message;
        this.aeaParFrontPartforms = aeaParFrontPartforms;
    }
}
