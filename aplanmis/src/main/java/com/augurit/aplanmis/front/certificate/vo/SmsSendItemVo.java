package com.augurit.aplanmis.front.certificate.vo;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**没用待删除
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/8/5/005 11:30
 */
@Data
@ApiModel("事项出证实体vo")
public class SmsSendItemVo {
    @ApiModelProperty(value = "事项实例列表")
    private List<AeaHiIteminst> iteminsts;
    @ApiModelProperty(value = "出件发送基本信息")
    private AeaHiSmsInfo smsInfo;
}
