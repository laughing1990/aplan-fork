package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * -模型
 */
@Data
@ApiModel("机构要求表实体")
public class AeaImUnitRequire implements Serializable{
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String unitRequireId; // ()

    @NotNull(message = "是否需要资质要求不能为空")
    @ApiModelProperty(value = "是否需要资质要求：1 需要，0 不需要")
    private String isQualRequire; // (是否需要资质要求：1 需要，0 不需要)

    @ApiModelProperty(value = "资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项")
    private String qualRequireType; // (资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项)
    private String qualRequire; // (资质要求序列)
    @ApiModelProperty(value = "资质要求说明（当IS_QUAL_REQUIRE =1 时，必填）")
    private String qualRequireExplain; // (资质要求说明（当IS_QUAL_REQUIRE =1 时，必填）)
    @ApiModelProperty(value = "资质备案说明")
    private String qualRecordRequire; // (资质备案说明)

    @NotNull(message = "是否仅承诺服务不能为空")
    @ApiModelProperty(value = "是否仅承诺服务：1 是，0 否")
    private String promiseService; // (是否仅承诺服务：1 是，0 否)
    @ApiModelProperty(value = "其他要求说明")
    private String otherRequireExplain; // (其他要求说明)

    @NotNull(message = "是否需要执业/职业人员要求不能为空")
    @ApiModelProperty(value = "是否需要执业/职业人员要求：1 需要，0 不需要")
    private String isRegisterRequire; // (是否需要执业/职业人员要求：1 需要，0 不需要)
    @ApiModelProperty(value = "执业/职业人员总数（当IS_REGISTER_REQUIRE =1 时，必填）")
    private String registerTotal; // (执业/职业人员总数（当IS_REGISTER_REQUIRE =1 时，必填）)
    @ApiModelProperty(value = "执业/职业人员要求（当IS_REGISTER_REQUIRE =1 时，必填）")
    private String registerRequire; // (执业/职业人员要求（当IS_REGISTER_REQUIRE =1 时，必填）)

    @NotNull(message = "是否需要备案要求不能为空")
    @ApiModelProperty(value = "是否需要备案要求：1 需要，0 不需要")
    private String isRecordRequire; // (是否需要备案要求：1 需要，0 不需要)
    @ApiModelProperty(value = "备案要求说明（当IS_RECORD_REQUIRE =1 时，必填）")
    private String recordRequireExplain; // (备案要求说明（当IS_RECORD_REQUIRE =1 时，必填）)

    private String rootOrgId;//根组织ID
    //非表字段
    private String qualRequireName; //资质要求序列名称
    private String itemVerId; //中介服务事项版本ID

}