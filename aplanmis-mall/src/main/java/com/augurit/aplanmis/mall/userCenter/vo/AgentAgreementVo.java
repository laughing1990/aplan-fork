package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
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

    @ApiModelProperty("代办协议文件")
    private List<BscAttFileAndDir>  atts;

    @ApiModelProperty("代办协议文件ID,用于下载，预览")
    private String  detailId;

    @ApiModelProperty("代办终止协议文件ID")
    private String  agentStopAgreementFileId;
    @ApiModelProperty("代办结束办结单文件ID")
    private String  agentEndAgreementFileId;
    @ApiModelProperty("协议编号")
    private String  agreementCode;
    @ApiModelProperty("协议签订时间")
    private String  agreementSignTime;
    @ApiModelProperty("代办终止原因")
    private String  agreementStopReason;
    @ApiModelProperty("代办办结时间")
    private Date  agreementEndTime;
    @ApiModelProperty("代办终止时间")
    private Date agreementStopTime;


}
