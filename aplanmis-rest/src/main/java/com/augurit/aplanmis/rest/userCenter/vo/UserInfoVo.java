package com.augurit.aplanmis.rest.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("个人信息")
public class UserInfoVo {

    @ApiModelProperty("单位列表")
    List<AeaUnitInfo> aeaUnitList;
    @ApiModelProperty("联系人列表")
    List<AeaLinkmanInfo> linkmanInfoList;
    @ApiModelProperty("联系人信息")
    private AeaLinkmanInfo aeaLinkmanInfo;
    @ApiModelProperty("单位信息")
    private AeaUnitInfo aeaUnitInfo;
    @ApiModelProperty("角色0:个人,1:委托人,2:单位")
    private String role;
}
