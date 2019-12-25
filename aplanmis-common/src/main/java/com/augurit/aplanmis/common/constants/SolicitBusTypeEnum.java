package com.augurit.aplanmis.common.constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
@ApiModel(value = "意见征求业务类型枚举类", description = "意见征求业务类型枚举类")
public enum SolicitBusTypeEnum {
    @ApiModelProperty(value = "SOLICIT_BUSTYPE_LHPS", name = "联合评审", allowableValues = "SOLICIT_BUSTYPE_LHPS")
    SOLICIT_BUSTYPE_LHPS("联合评审", "LHPS"),

    @ApiModelProperty(value = "SOLICIT_BUSTYPE_YCZX", name = "一次征询", allowableValues = "SOLICIT_BUSTYPE_YCZX")
    SOLICIT_BUSTYPE_YCZX("一次征询", "YCZX"),

    @ApiModelProperty(value = "SOLICIT_BUSTYPE_YJZQ", name = "意见征求", allowableValues = "SOLICIT_BUSTYPE_YJZQ")
    SOLICIT_BUSTYPE_YJZQ("意见征求", "YJZQ");

    private String name;
    private String value;

    SolicitBusTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
