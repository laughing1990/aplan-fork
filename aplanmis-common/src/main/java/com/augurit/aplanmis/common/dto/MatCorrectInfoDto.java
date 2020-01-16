package com.augurit.aplanmis.common.dto;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MatCorrectInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "applyinstCode", value = "申请流水号")
    private String applyinstCode;

    @ApiModelProperty(name = "iteminstName", value = "事项名称")
    private String iteminstName;
    @ApiModelProperty(name = "itemVerId", value = "事项版本ID")
    private String itemVerId;
    @ApiModelProperty(name = "chargeOrgName", value = "审批部门")
    private String chargeOrgName;

    @ApiModelProperty(name = "owner", value = "业主单位")
    private String owner;

    @ApiModelProperty(name = "localCode", value = "项目代码")
    private String localCode;

    @ApiModelProperty(name = "projInfoId", value = "项目ID")
    private String projInfoId;

    @ApiModelProperty(name = "projInfoName", value = "项目名称")
    private String projInfoName;

    @ApiModelProperty(name = "matCorrectDtos", value = "少交或未交的材料列表")
    private List<MatCorrectDto> matCorrectDtos;

    @ApiModelProperty(name = "submittedMats", value = "已提交的材料列表")
    private List<MatCorrectDto> submittedMats;

    @ApiModelProperty(name = "allMats", value = "所有材料列表")
    private List<AeaItemMat> allMats;

    @ApiModelProperty(name = "iteminstList", value = "所有事项列表")
    private List<AeaHiIteminst> iteminstList;
}
