package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaImQualLevel;
import com.augurit.aplanmis.common.domain.AeaImServiceMajor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/11
 */
@Data
@ApiModel("中介服务对应的资质信息")
public class AeaImQualVo {

    @ApiModelProperty(value = "服务资质关联ID")
    private String serviceQualId;
    @ApiModelProperty(value = "资质ID")
    private String qualId;
    @ApiModelProperty(value = "资质编码")
    private String qualCode; // (资质编码)
    @ApiModelProperty(value = "资质名称")
    private String qualName; // (资质名称)
    @ApiModelProperty(value = "资质等级ID")
    private String qualLevelId; // (资质等级ID)
    @ApiModelProperty(value = "资质等级列表")
    private List<AeaImQualLevel> aeaImQualLevels;
    @ApiModelProperty(value = "资质专业列表")
    private List<AeaImServiceMajor> aeaImServiceMajors;
}
