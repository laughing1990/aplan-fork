package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
* -服务评价
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>日期：2019-06-03 13:59:35</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:thinkpad</li>
    <li>创建时间：2019-06-03 13:59:35</li>
    <li>修改人1：</li>
    <li>修改时间1：</li>
    <li>修改内容1：</li>
</ul>
*/
@Data
@ApiModel("服务评价")
public class AeaImServiceEvaluation implements Serializable{

        private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "服务评价信息ID")
        private String serviceEvaluationId; // (服务评价信息ID)

        @ApiModelProperty(value = "企业报价ID")
        @NotBlank
        private String unitBiddingId; // (企业报价ID)

        @ApiModelProperty(value = "服务质量")
        @NotBlank
        private String serviceQuality; // (服务质量)

        @ApiModelProperty(value = "服务时效")
        @NotBlank
        private String servicePrescription; // (服务时效)

        @ApiModelProperty(value = "服务态度")
        @NotBlank
        private String serviceAttitude; // (服务态度)

        @ApiModelProperty(value = "服务收费")
        @NotBlank
        private String serviceCharge; // (服务收费)

        @ApiModelProperty(value = "服务规范")
        @NotBlank
        private String serviceStandard; // (服务规范)

        @ApiModelProperty(value = "评价单位ID")
        @NotBlank
        private String evaluateUnitId; // (评价单位ID)

        @ApiModelProperty(value = "综合评价")
        @NotBlank
        private String compEvaluation; // (综合评价)

        private String creater; // ()

        @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // ()

        @ApiModelProperty(value = "备注")
        private String memo; // (备注)

        private String isDelete; // (是否删除：1 已删除，0 未删除)

        @ApiModelProperty(value = "审核时间")
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private java.util.Date auditTime; // (审核时间)

        @ApiModelProperty(value = "评价状态：0 审核失败，1 已评价 ，2 审核中")
        private String auditFlag; // (评价状态：0 审核失败，1 已评价 ，2 审核中)

        @ApiModelProperty(value = "审核意见")
        private String auditOpinion;

        private String rootOrgId;//根组织ID

        //扩展字段
        private String keyword;

        private String evaluateUnitName;//评价单位名称

        private String projName;//项目名称

}
