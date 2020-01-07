package com.augurit.aplanmis.common.form.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel("建设项目登记信息表单参数")
public class ExProjFormVo {
    @ApiModelProperty(value = "项目ID", required = true)
    private String projInfoId;

    @ApiModelProperty("项目分类")
    private String buildType; // (项目分类，来自于数据字典)

    @ApiModelProperty("项目性质")
    private String projNature; // (项目性质，来自于数据字典)

    @ApiModelProperty("项目用途")
    private String projFunction; // (项目用途，来自于数据字典)

    @ApiModelProperty("建设规模类型，来自于数据字典，7特大型工程、8大型工程、9中型工程、10小型工程")
    private String scaleType; // (建设规模类型，来自于数据字典，7特大型工程、8大型工程、9中型工程、10小型工程)

    @ApiModelProperty("建设规模")
    private String scaleContent; // (建设规模)

    @ApiModelProperty("立项文号")
    private String projNum; // (立项文号)

    @ApiModelProperty("立项级别")
    private String projLevel; // (立项级别)

    @ApiModelProperty("立项批准机关")
    private String approvalDept; // (立项批准机关)

    @ApiModelProperty("立项批准时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date approvalTime; // (立项批准时间)

    @ApiModelProperty("是否工程总承包模式")
    private String isEpc; // (是否工程总承包模式)

    @ApiModelProperty(value = "各级政府财政资金投资", required = true)
    private String govFinance; // (各级政府财政资金投资)

    @ApiModelProperty(value = "国有企业资金投资", required = true)
    private String stateEnterprice; // (国有企业资金投资)

    @ApiModelProperty(value = "国家融资", required = true)
    private String stateInvestment; // (国家融资)

    @ApiModelProperty(value = "国际组织或外国政府资金", required = true)
    private String internationalInvestment; // (国际组织或外国政府资金)

    @ApiModelProperty(value = "集体经济组织投资", required = true)
    private String collectiveInvestment; // (集体经济组织投资)

    @ApiModelProperty(value = "外商（国）投资", required = true)
    private String foreignInvestment; // (外商（国）投资)

    @ApiModelProperty(value = "项目ID", required = true)
    private String hkInvestment; // (港澳台投资)

    @ApiModelProperty(value = "私（民）营投资", required = true)
    private String privateInvestment; // (私（民）营投资)

    @ApiModelProperty(value = "其他资金来源", required = true)
    private String otherInvestment; // (其他资金来源)

    @ApiModelProperty(value = "建设行业主管部门确认时间", required = true)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date govOrgConfirmTime; // (建设行业主管部门确认时间)

    @ApiModelProperty(value = "建设行业主管部门行政单位组织代码", required = true)
    private String govOrgCode; // (建设行业主管部门行政单位组织代码)

    @ApiModelProperty(value = "建设行业主管部门行政单位名称", required = true)
    private String govOrgName; // (建设行业主管部门行政单位名称)

    @ApiModelProperty(value = "建设行业主管部门行政单位区域码", required = true)
    private String govAreaCode; // (建设行业主管部门行政单位区域码)


    //扩展字段
    private String formId;
    private String refEntityId;  //申请实例ID
}
