package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CurrentLinkmansVo {

    @ApiModelProperty(value = "申请人linkmanInfoId")
    private String applyUserId;
    @ApiModelProperty(value = "申请人")
    private String applyUserName;
    @ApiModelProperty(value = "申请人身份证")
    private String applyUserIdnumber;
    @ApiModelProperty(value = "申请人电话")
    private String applyUserPhone;

    public static CurrentLinkmansVo format(AeaLinkmanInfo aeaLinkmanInfo){
        CurrentLinkmansVo currentLinkmansVo=new CurrentLinkmansVo();
        currentLinkmansVo.setApplyUserId(aeaLinkmanInfo.getLinkmanInfoId());
        currentLinkmansVo.setApplyUserIdnumber(aeaLinkmanInfo.getLinkmanMobilePhone());
        currentLinkmansVo.setApplyUserIdnumber(aeaLinkmanInfo.getLinkmanCertNo());
        currentLinkmansVo.setApplyUserName(aeaLinkmanInfo.getLinkmanName());
        return currentLinkmansVo;
    }
}