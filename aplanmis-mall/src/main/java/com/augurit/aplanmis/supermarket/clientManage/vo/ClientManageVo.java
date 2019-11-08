package com.augurit.aplanmis.supermarket.clientManage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "委托人管理", description = "委托人管理")
public class ClientManageVo {
    @ApiModelProperty(value = "所属单位ID")
    private String unitInfoId;
    @ApiModelProperty(value = "联系人ID")
    private String linkmanInfoId;
    @ApiModelProperty(value = "登录名")
    private String loginName;
    @ApiModelProperty(value = "登录密码")
    private String loginPwd;
    @ApiModelProperty(value = "是否为管理员：1 是，0 否")
    private String isAdministrators;
    @ApiModelProperty(value = "是否绑定账号：1 是， 0  否")
    private String isBindAccount;
    @ApiModelProperty(value = "查询关键词")
    private String keyword;
    @ApiModelProperty(value = "中介发布服务ID")
    private String unitServiceIds;
}
