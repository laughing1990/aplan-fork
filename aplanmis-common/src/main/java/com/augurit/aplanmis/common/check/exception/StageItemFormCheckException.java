package com.augurit.aplanmis.common.check.exception;

import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import lombok.Getter;

import java.util.List;

/**
 * 前置检查异常类
 */
@Getter
public class StageItemFormCheckException extends CheckException {

    protected String message = "阶段前置事项表单检查不通过";

    private List<AeaParFrontItemform> aeaParFrontItemforms;

    public StageItemFormCheckException(String message) {
        super(message);
    }

    public StageItemFormCheckException(String message, List<AeaParFrontItemform> aeaParFrontItemforms) {
        super(message);
        this.message = message;
        this.aeaParFrontItemforms = aeaParFrontItemforms;
    }
}
