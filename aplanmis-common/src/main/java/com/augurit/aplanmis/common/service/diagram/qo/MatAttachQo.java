package com.augurit.aplanmis.common.service.diagram.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("材料附件查询对象")
public class MatAttachQo {

    @ApiModelProperty(value = "事项实例", dataType = "string")
    private String iteminstId;

    @ApiModelProperty(value = "附件类型", notes = "APPLY: 申报材料, OFFICE: 批文批复, ADVICE: 意见汇总, CERT: 电子证照, ALL: 所有")
    private AttachType attachType;

    @ApiModelProperty(value = "搜索关键字", dataType = "string")
    private String keyword;
}

enum AttachType {
    APPLY // 申报材料

    , OFFICE // 批文批复

    , CERT // 电子证照

    , ADVICE // 意见汇总

    , ALL // 所有
    ;
}
