package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("代办协议VO")
public class AgentAgreementVo {
    @ApiModelProperty("代办协议")
    private String agentAgreementName="佛山市顺德区大良镇南芦村沙地元三路建设工程代办协议书.doc";
    @ApiModelProperty("代办人员")
    private String agentUserName;

    @ApiModelProperty("代办人员联系电话")
    private String agentUserMobile;

    @ApiModelProperty("代办协议文件ID")
    private List<BscAttForm> atts;

}