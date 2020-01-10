package com.augurit.aplanmis.front.certificate.vo;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiSmsSendBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("领证保存信息实体vo")
public class CertReceivedVo {

    @ApiModelProperty(value = "申请实例id")
    private String applyinstId;

    @ApiModelProperty(value = "是否并联, 1: 串联; 0: 并联")
    private String isSeriesApprove;

    @ApiModelProperty(value = "领件人、出件人相关信息")
    private AeaHiSmsSendBean sendBean;

    @ApiModelProperty(value = "委托人信息", notes = "当 sendBean.isConsigner=1 时有值")
    private AeaHiSmsSendBean consignerForm;

    @ApiModelProperty(value = "事项实例")
    private List<AeaHiIteminst> iteminsts;

    @ApiModelProperty(value = "是否一次领件, 0: 否; 1: 是")
    private String isOnceSend;


}
