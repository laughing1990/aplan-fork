package com.augurit.aplanmis.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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

    @ApiModelProperty(value = "邮件签收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date postSignTime;

    // 扩展字段

    @ApiModelProperty(value = "是否出件")
    private boolean hadSmsSend = false;

    @ApiModelProperty(value = "事项实例名称")
    private String iteminstName;
}
