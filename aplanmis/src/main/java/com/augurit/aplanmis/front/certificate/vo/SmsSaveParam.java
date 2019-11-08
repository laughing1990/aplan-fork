package com.augurit.aplanmis.front.certificate.vo;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiSmsSendBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/8/5/005 10:13
 */
@Data
@ApiModel("领证保存信息实体vo")
public class SmsSaveParam {

    @ApiModelProperty(value = "申请实例id")
    private String applyinstId;
    @ApiModelProperty(value = "是否并联【0并联1单项】")
    private String isApprove;

    private AeaHiSmsSendBean sendBean;
    private AeaHiSmsSendBean  consignerForm;
    private List<AeaHiIteminst> iteminsts;

    @ApiModelProperty(value = "是否一次领件0否1是")
    private String isOnceSend;// 是否一次领件0否1是



}
