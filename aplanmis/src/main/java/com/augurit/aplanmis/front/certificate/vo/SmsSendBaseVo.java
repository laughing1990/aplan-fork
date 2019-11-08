package com.augurit.aplanmis.front.certificate.vo;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/8/7/007 10:56
 */
@Data
@ApiModel("领证相关实体vo")
public class SmsSendBaseVo {
    @ApiModelProperty(value = "事项实例列表")
    private List<AeaHiIteminst> iteminsts;
    @ApiModelProperty(value = "出件发送基本信息")
    private AeaHiSmsInfo smsInfo;
}
