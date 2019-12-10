package com.augurit.aplanmis.mall.cert.vo;

import com.augurit.aplanmis.common.domain.AeaCert;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("电子证照实例参数VO")
public class AeaCertinstParamVo {
    @ApiModelProperty(value = "证照实例ID")
    private String certinstId;
    @ApiModelProperty(value = "证照所属，c表示企业，u表示法人")
    private String certHolder;
    @ApiModelProperty(value = "证照类型(下拉)")
    private String certType;
    @ApiModelProperty(value = "证照（下拉，数据根据证照所属和证照类型联动筛选）")
    private String certId;
    @ApiModelProperty(value = "证照名称(填写)")
    private String certinstName;
    @ApiModelProperty(value = "证照编码（接口获取，不可编辑）")
    private String certinstCode;
    @ApiModelProperty(value = "颁发单位(下拉)")
    private String issueOrgId;
    @ApiModelProperty(value = "持证者")
    private String certOwner;
    @ApiModelProperty(name = "termStart", value = "(有效期（起始）)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date termStart;
    @ApiModelProperty(name = "termEnd", value = "有效期（截止）")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date termEnd;
    @ApiModelProperty(value = "资质等级(下拉)")
    private String qualLevelId;
    @ApiModelProperty(value = "业务范围")
    private String managementScope;
    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(name = "attLinkId", value = "附件关联ID")
    private String attLinkId;

}
