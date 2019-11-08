package com.augurit.aplanmis.mall.credit.vo;

import com.augurit.aplanmis.common.dto.AeaCreditSummaryAllDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("信用管理-信用信息VO")
public class CreditRedblackVo {
    @ApiModelProperty(value = "是否黑名单 0表示红名单，1表示黑名单")
    private String isBlack;
    @ApiModelProperty(value = "列入红黑名单原因")
    private String redblackReason;
    @ApiModelProperty(value = "列入红黑名单时间")
    private Date includeTime;
    @ApiModelProperty(value = "程度，例如：一般失信")
    private String redblackLevel;
    @ApiModelProperty(value = "认证机关")
    private String creditUnit;
    @ApiModelProperty(value = "认证依据")
    private String creditBasis;
    @ApiModelProperty(value = "认证时间")
    private Date affirmTime;
    @ApiModelProperty(value = "对象类型，u表示单位，l表示联系人")
    private String redblackType;
    @ApiModelProperty(value = "企业ID")
    private String unitInfoId;
    @ApiModelProperty(value = "联系人ID")
    private String linkmanInfoId;
    @ApiModelProperty(value = "失信记录,守信记录,登记注册备案记录,资质认证许可记录列表")
    private List<AeaCreditSummaryAllDto> creditList;

}