package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("事项出件domain")
public class AeaHiSmsSendItem extends AeaHiSmsSendBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private String sendItemId;

    @ApiModelProperty(value = "事项出件编号")
    private String sendItemCode;

    @ApiModelProperty(value = "领取方式, 1: 一次领取; 0: 分别领取")
    private String isOnceSend;

    @ApiModelProperty(value = "申报出件domain主键")
    private String sendApplyId;

    @ApiModelProperty(value = "事项实例id")
    private String iteminstId;

    @ApiModelProperty(value = "输入输出实例id")
    private String inoutinstId;

    // 扩展字段

    @ApiModelProperty(value = "是否出件")
    private boolean hadSmsSend = false;

    @ApiModelProperty(value = "事项实例名称")
    private String iteminstName;
}
