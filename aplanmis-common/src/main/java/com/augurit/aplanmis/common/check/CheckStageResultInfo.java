package com.augurit.aplanmis.common.check;

import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("阶段前置检查结果信息")
@Getter
@Setter
public class CheckStageResultInfo {

    @ApiModelProperty(value = "检查是否通过", notes = "true: 通过; flase: 不通过")
    private boolean passed;

    @ApiModelProperty(value = "前置阶段")
    private List<AeaParFrontStage> frontStages;

    @ApiModelProperty(value = "前置事项")
    private List<AeaParFrontItem> frontItems;

    @ApiModelProperty(value = "前置智能表单")
    private List<AeaParFrontItemform> aeaParFrontItemforms;

    @ApiModelProperty(value = "扩展表单信息")
    private List<AeaParFrontPartform> aeaParFrontPartforms;

    @ApiModelProperty(value = "所有不通过消息")
    private String message;

    public CheckStageResultInfo() {
        this.passed = true;
    }
}
