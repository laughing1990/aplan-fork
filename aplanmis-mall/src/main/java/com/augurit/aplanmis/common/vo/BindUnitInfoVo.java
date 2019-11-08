package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tiantian
 * @date 2019/6/17
 */
@Data
@ApiModel("绑定的单位信息")
public class BindUnitInfoVo {
    @ApiModelProperty(value = "企业ID")
    private String unitInfoId;

    @ApiModelProperty(value = "项目（法人）单位名称")
    private String applicant;// (项目（法人）单位名称)

    @ApiModelProperty(value = "是否为中介机构",notes = "1 是，0 否")
    private String isImUnit; //是否为中介机构：1 是，0 否

    @ApiModelProperty(value = "是否为业主单位",notes = "1 是，0 否")
    private String isOwnerUnit; //是否为业主单位：1 是，0 否

    private String isAdministrators;

}
