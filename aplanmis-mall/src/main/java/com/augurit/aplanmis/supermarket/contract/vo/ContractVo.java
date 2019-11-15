package com.augurit.aplanmis.supermarket.contract.vo;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaImContract;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("合同信息vo")
@Data
public class ContractVo {
    @ApiModelProperty(value = "服务合同ID")
    private String contractId;

    @ApiModelProperty(value = "合同名称", required = true)
    @NotBlank
    private String contractName;

    @ApiModelProperty(value = "合同编码", required = true)
    @NotBlank
    private String contractCode;

    @ApiModelProperty(value = "合同金额", required = true)
    @NotBlank
    private String price;

    @ApiModelProperty(value = "服务开始时间 yyyy-MM-dd", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @NotNull
    private java.util.Date serviceStartTime;

    @ApiModelProperty(value = "服务结束时间 yyyy-MM-dd", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @NotNull
    private java.util.Date serviceEndTime;

    @ApiModelProperty(value = "服务内容", required = true)
    @NotBlank
    private String serviceContent;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "签订时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private java.util.Date signTime;
    @ApiModelProperty(value = "采购项目ID")
    private String projPurchaseId;

    public AeaImContract createAeaImContract(String serviceUnitInfoId, String ownerUnitInfoId, String unitBiddingId) {
        AeaImContract contract = new AeaImContract();
        BeanUtils.copyProperties(this, contract);
        if (StringUtils.isEmpty(this.contractId)) {
            contract.setIsDelete("0");
            contract.setAuditFlag("0");
            contract.setIsConfirm("0");
            contract.setRootOrgId(SecurityContext.getCurrentOrgId());
            contract.setUnitBiddingId(unitBiddingId);
            contract.setOwnerUnitInfoId(ownerUnitInfoId);
            contract.setServiceUnitInfoId(serviceUnitInfoId);
            contract.setCreater(SecurityContext.getCurrentUserName());
        }
        return contract;
    }
}
