package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("个人信息")
public class UserInfoVo {

    @ApiModelProperty("单位列表")
    List<com.augurit.aplanmis.mall.userCenter.vo.AeaUnitInfoVo> aeaUnitList;
    @ApiModelProperty("联系人列表")
    List<AeaLinkmanInfoVo> linkmanInfoList;
    @ApiModelProperty("联系人信息")
    private AeaLinkmanInfoVo aeaLinkmanInfo;
    @ApiModelProperty("单位信息")
    private AeaUnitInfoVo aeaUnitInfo;
    @ApiModelProperty("角色0:个人,1:委托人,2:单位")
    private String role;
}
