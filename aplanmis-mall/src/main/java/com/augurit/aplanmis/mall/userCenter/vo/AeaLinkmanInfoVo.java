package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.utils.DesensitizedUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("人员信息")
public class AeaLinkmanInfoVo {
    @ApiModelProperty("联系人姓名")
    private java.lang.String linkmanName; // (联系人姓名)
    @ApiModelProperty("证件号")
    private java.lang.String linkmanCertNo; // (证件号)
    @ApiModelProperty("手机号码")
    private java.lang.String linkmanMobilePhone; // (手机号码)
    @ApiModelProperty("办公电话")
    private java.lang.String linkmanOfficePhon; // (办公电话)
    @ApiModelProperty("传真")
    private java.lang.String linkmanFax; // (传真)
    @ApiModelProperty("电子邮件")
    private java.lang.String linkmanMail; // (电子邮件)
    @ApiModelProperty("是否绑定 1是 0 否")
    private String isBindAccount;//是否绑定 1是 0 否
    @ApiModelProperty("联系人住址")
    private java.lang.String linkmanAddr; // (联系人住址)
    @ApiModelProperty("备注")
    private java.lang.String linkmanMemo; // (备注)
    @ApiModelProperty("用户登录名")
    private java.lang.String loginName; // (用户登录名)
    @ApiModelProperty("主键")
    private java.lang.String linkmanInfoId; // (主键)


    public static AeaLinkmanInfoVo build(AeaLinkmanInfo AeaLinkmanInfo) {
        AeaLinkmanInfoVo aeaLinkmanIanfoVo = new AeaLinkmanInfoVo();
        BeanUtils.copyProperties(AeaLinkmanInfo, aeaLinkmanIanfoVo);
        aeaLinkmanIanfoVo.setLinkmanMobilePhone(DesensitizedUtil.desensitizedPhoneNumber(aeaLinkmanIanfoVo.getLinkmanMobilePhone()));
        return aeaLinkmanIanfoVo;
    }
}
