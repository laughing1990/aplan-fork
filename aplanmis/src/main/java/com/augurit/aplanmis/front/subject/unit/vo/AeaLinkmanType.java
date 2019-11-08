package com.augurit.aplanmis.front.subject.unit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "项目 单位联系人类型", description = "项目 单位联系人类型")
public class AeaLinkmanType {
    @ApiModelProperty(value = "联系人类型")
    private String linkmanType;
    @ApiModelProperty(value = "联系人ID")
    private String linkmanInfoId;
    @ApiModelProperty(value = "联系人姓名")
    private String linkmanName;

    public AeaLinkmanType() {

    }

    public AeaLinkmanType(String linkmanInfoId, String linkmanName, String linkmanType) {
        this.linkmanInfoId = linkmanInfoId;
        this.linkmanName = linkmanName;
        this.linkmanType = linkmanType;
    }
}
