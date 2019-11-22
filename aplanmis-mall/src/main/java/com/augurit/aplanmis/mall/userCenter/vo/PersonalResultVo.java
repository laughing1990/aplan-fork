package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("保存个人申报联系人和申报人成功后返回的对象")
public class PersonalResultVo {

    @ApiModelProperty(value = "联系人id", required = true, dataType = "string")
    private String linkmanInfoId;

    @ApiModelProperty(value = "申报人id", required = true, dataType = "string")
    private String applyInfoId;
}
