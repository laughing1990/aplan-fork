package com.augurit.aplanmis.rest.common.vo;

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
    @ApiModelProperty(value = "是否是管理员", notes = "1 是， 0 否")
    private String isAdministrators; // 是否是管理员：1 是， 0 否
    @ApiModelProperty(value = "是否为个人账号", notes = "1 是， 0 否")
    private String isPersonAccount; // 是否为个人账号：1 是， 0 否
    @ApiModelProperty(value = "登录账号")
    private String loginName;  // 登录账号
    @ApiModelProperty(value = "个人名称")
    private String personName;  // 个人名称
    @ApiModelProperty(value = "单位名称")
    private String unitName; // 单位名称
    @ApiModelProperty(value = "sessionId")
    private String sid; //  sessionId
    @ApiModelProperty(value = "是否业主", notes = "1 是， 0 否")
    private String isOwner; // 是否业主：1 是，0 否
    @ApiModelProperty(value = "绑定的单位列表(中介超市使用)")
    private List<BindUnitInfoVo> unitInfos;
    @ApiModelProperty(value = "绑定的单位列表(网厅使用)")
    private List<AeaUnitInfo> unitInfoList;
    @ApiModelProperty(value = "图片地址")
    private String imgUrl; //图片地址
}
