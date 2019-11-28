package com.augurit.aplanmis.common.check.exception;

import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import lombok.Getter;

import java.util.List;

/**
 * 前置检查异常类
 */
@Getter
public class StageStageCheckException extends CheckException {

    private List<AeaParFrontStage> aeaParFrontStages;

    protected String message = "阶段的前置阶段检查不通过";

    public StageStageCheckException(String message) {
        super(message);
    }

    public StageStageCheckException(String message, List<AeaParFrontStage> aeaParFrontStages) {
        super(message);
        this.message = message;
        this.aeaParFrontStages = aeaParFrontStages;
    }
}
