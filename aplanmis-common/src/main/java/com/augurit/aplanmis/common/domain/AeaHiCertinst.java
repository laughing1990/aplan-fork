package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 电子证照实例表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:10</li>
 * </ul>
 */
@Data
@ApiModel
public class AeaHiCertinst implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "certinstId", value = "证照实例id")
    private String certinstId;

    @ApiModelProperty(name = "certId", value = "证照定义ID", required = true)
    private String certId;

    @ApiModelProperty(name = "unitInfoId", value = "业主单位ID", required = true)
    private String unitInfoId;

    @ApiModelProperty(name = "projInfoId", value = "建设项目ID")
    private String projInfoId;

    @ApiModelProperty(name = "attLinkId", value = "附件关联ID", hidden = true)
    private String attLinkId;

    @ApiModelProperty(name = "creater", value = "创建人", hidden = true)
    private String creater;

    @ApiModelProperty(name = "createTime", value = "创建时间", hidden = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(name = "modifier", value = "修改人", hidden = true)
    private String modifier;

    @ApiModelProperty(name = "modifyTime", value = "修改时间", hidden = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyTime;

    @ApiModelProperty(name = "certinstCode", value = "证照编码", required = true)
    private String certinstCode;

    @ApiModelProperty(name = "certinstName", value = "证照名称", required = true)
    private String certinstName;

    /* @DateTimeFormat(pattern = "yyyy-MM-dd")*/
    @ApiModelProperty(name = "issueDate", value = " (制证时间)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;

    //@DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "termStart", value = "(有效期（起始）)")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date termStart;

    /*@DateTimeFormat(pattern = "yyyy-MM-dd")*/
    @ApiModelProperty(name = "termEnd", value = "有效期（截止）")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date termEnd;

    @ApiModelProperty(name = "caInfo", value = "电子签章信息", hidden = true)
    private String caInfo;

    @ApiModelProperty(name = "certOwner", value = "持证者")
    private String certOwner;

    @ApiModelProperty(name = "issueOrgId", value = "颁发单位ID")
    private String issueOrgId;

    @ApiModelProperty(name = "termNum", value = "有效期限数字", hidden = true)
    @Size(max = 38)
    private Long termNum;

    @ApiModelProperty(name = "termUnit", value = "有效期限单位", hidden = true)
    private String termUnit;

    @ApiModelProperty(name = "flxl", value = "法律效率", hidden = true)
    private String flxl;

    @ApiModelProperty(name = "applyinstId", value = "申请ID", hidden = true)
    private String applyinstId;

    @ApiModelProperty(name = "linkmanInfoId", value = "联系人id", hidden = true)
    private String linkmanInfoId;

    @ApiModelProperty(name = "qualLevelId", value = "资质等级ID", hidden = true)
    private String qualLevelId;

    @ApiModelProperty(name = "rootOrgId", value = "根组织ID", hidden = true)
    private String rootOrgId;

    @ApiModelProperty(name = "memo", value = "备注")
    private String memo;

    private String managementScope;//业务范围

    @ApiModelProperty(value = "用证码")
    private String authCode;

    @ApiModelProperty(value = "证照来源", notes = "local: 本地部门出证; external: 第三方对接证照")
    private String certinstSource;

    //额外字段
    @ApiModelProperty(name = "bscAttDetails", value = "附件列表", hidden = true)
    private List<BscAttDetail> bscAttDetails;

    @ApiModelProperty(name = "issueOrgName", value = "颁发单位", hidden = true)
    private String issueOrgName;

    @ApiModelProperty(name = "applicant", value = "建设单位名称", hidden = true)
    private String applicant;

    @ApiModelProperty(value = "材料定义id")
    private String matId;

    // 扩展字段

    @ApiModelProperty(value = "证照材料实例id")
    private String matinstId;

}
