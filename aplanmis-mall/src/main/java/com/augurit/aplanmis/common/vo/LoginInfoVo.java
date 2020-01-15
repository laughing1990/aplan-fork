package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("登录信息")
public class LoginInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String userId;//用户ID
    @ApiModelProperty(value = "单位ID")
    private String unitId;//单位ID
    @ApiModelProperty(value = "是否是管理员",notes = "1 是， 0 否")
    private String isAdministrators;
    @ApiModelProperty(value = "是否为个人账号",notes = "1 是， 0 否")
    private String isPersonAccount;
    @ApiModelProperty(value = "登录账号")
    private String loginName;
    @ApiModelProperty(value = "个人名称")
    private String personName;
    @ApiModelProperty(value = "单位名称")
    private String unitName;
    @ApiModelProperty(value = "sessionId")
    private String sid; //  sessionId
    @ApiModelProperty(value = "是否业主",notes = "1 是， 0 否")
    private String isOwner; // 是否业主：1 是，0 否
    @ApiModelProperty(value = "绑定的单位列表(中介超市使用)")
    private List<BindUnitInfoVo> unitInfos;
    @ApiModelProperty(value = "绑定的单位列表(网厅使用)")
    private List<AeaUnitInfo> unitInfoList;
    @ApiModelProperty(value = "图片地址")
    private String imgUrl; //图片地址
    @ApiModelProperty(value = "用户身份证号")
    private String idCard;//用户身份证号
    @ApiModelProperty(value = "企业统一社会信用代码")
    private String unifiedSocialCreditCode;//企业统一社会信用代码
    @ApiModelProperty(value = "信用等级")
    private String creditLevel;//信用等级
}
