package com.augurit.aplanmis.front.basis.stage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("阶段与一张表单显示与渲染的参数对象")
@Data
public class OneFormStageRequest {


    @ApiModelProperty(value = "当前申报/审批实例id", dataType = "String", required = true)
    String applyinstId;

    @ApiModelProperty(value = "当前主题下阶段实例id", dataType = "String", required = true, example = "be2e337e-1164-42a6-bb3b-af8d1df6d898")
    String stageId;

    @ApiModelProperty(value = "前端传递过来的指定事项ids", position = 2, dataType = "List", required = false, example = "[\"cc86750551274c97a796d36a0b165e5a\", \"146892023bbf4c29b38daedea695676e\"]")
    List<String> itemids=new ArrayList<>();

}
