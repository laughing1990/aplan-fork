package com.augurit.aplanmis.front.specialProcedures.vo;

import com.augurit.aplanmis.common.domain.AeaHiItemSpecial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel(value = "特殊程序Vo信息")
@EqualsAndHashCode(callSuper = true)
public class SpecialParamVo extends AeaHiItemSpecial implements Serializable {
    @ApiModelProperty(name = "customer", value = "客户名称")
    private String customer;//客户名称
    @ApiModelProperty(name = "linkman", value = "联系人名称")
    private String linkman;
    @ApiModelProperty(name = "linkAddr", value = "联系地址")
    private String linkAddr;
    @ApiModelProperty(name = "linkPhone", value = "联系人电话")
    private String linkPhone;
    @ApiModelProperty(name = "iteminstCode", value = "事项实例代码")
    private String iteminstCode;
    @ApiModelProperty(name = "iteminstName", value = "事项实例名称")
    private String iteminstName;
    @ApiModelProperty(name = "taskId", value = "任务实例id")
    private String taskId;
    @ApiModelProperty(name = "appinstId", value = "流程实例id")
    private String appinstId;//流程实例id

    //改为弹窗方式顺带传当前用户信息回去去
    @ApiModelProperty(name = "currentUserId", value = "当前用户UserID")
    private String currentUserId;
    @ApiModelProperty(name = "currentUserName", value = "当前用户UserName")
    private String currentUserName;
    @ApiModelProperty(name = "windowPhone", value = "当前用户phone")
    private String currentUserPhone;

}
