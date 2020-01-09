package com.augurit.aplanmis.mall.credit.vo;

import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("企业简单信息VO")
public class CreditUnitVo {
    @ApiModelProperty(value = "单位ID")
    private String unitInfoId;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "企业统一信用代码")
    private String unifiedSocialCreditCode;

    public static CreditUnitVo format(AeaUnitInfo aeaUnitInfo){
        CreditUnitVo vo=new CreditUnitVo();
        vo.setUnifiedSocialCreditCode(aeaUnitInfo.getUnifiedSocialCreditCode());
        vo.setUnitInfoId(aeaUnitInfo.getUnitInfoId());
        vo.setUnitName(aeaUnitInfo.getApplicant());
        return vo;
    }
}