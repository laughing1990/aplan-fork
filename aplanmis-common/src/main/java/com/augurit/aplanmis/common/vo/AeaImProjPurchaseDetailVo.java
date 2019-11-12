package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaImMajorQual;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/13
 */
@Data
@ApiModel("采购需求详细信息")
@EqualsAndHashCode(callSuper = true)
public class AeaImProjPurchaseDetailVo extends AeaImProjPurchase {

    @ApiModelProperty(value = "项目规模")
    private String projScale;//项目规模
    @ApiModelProperty(value = "项目规模内容")
    private String projScaleContent;//项目规模内容

    @ApiModelProperty(value = "是否需要资质要求：1 需要，0 不需要")
    private String isQualRequire; // (是否需要资质要求：1 需要，0 不需要)
    @ApiModelProperty(value = "资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项要")
    private String qualRequireType; // (资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项)

    @ApiModelProperty(value = "资质要求序列")
    private String qualRequire; // (资质要求序列)

    @ApiModelProperty(value = "资质要求说明（当IS_QUAL_REQUIRE =1 时，必填）")
    private String qualRequireExplain; // (资质要求说明（当IS_QUAL_REQUIRE =1 时，必填）)
    @ApiModelProperty(value = "资资质备案说明")
    private String qualRecordRequire; // (资质备案说明)
    @ApiModelProperty(value = "是否仅承诺服务：1 是，0 否")
    private String promiseService; // (是否仅承诺服务：1 是，0 否)
    @ApiModelProperty(value = "其他要求说明")
    private String otherRequireExplain; // (其他要求说明)
    @ApiModelProperty(value = "是否需要执业/职业人员要求：1 需要，0 不需要")
    private String isRegisterRequire; // (是否需要执业/职业人员要求：1 需要，0 不需要)
    @ApiModelProperty(value = "执业/职业人员总数（当IS_REGISTER_REQUIRE =1 时，必填）")
    private String registerTotal; // (执业/职业人员总数（当IS_REGISTER_REQUIRE =1 时，必填）)
    @ApiModelProperty(value = "执业/职业人员要求（当IS_REGISTER_REQUIRE =1 时，必填）")
    private String registerRequire; // (执业/职业人员要求（当IS_REGISTER_REQUIRE =1 时，必填）)
    @ApiModelProperty(value = "是否需要备案要求：1 需要，0 不需要")
    private String isRecordRequire; // (是否需要备案要求：1 需要，0 不需要)
    @ApiModelProperty(value = "备案要求说明（当IS_RECORD_REQUIRE =1 时，必填）")
    private String recordRequireExplain; // (备案要求说明（当IS_RECORD_REQUIRE =1 时，必填）)

    private String unitRequire; // 中介服务机构要求

    @ApiModelProperty(value = "报名的中介机构数")
    private Integer unitBiddingCount;

    private String itemBasicId;

    private List<AeaImMajorQual> majorQualList;


}
