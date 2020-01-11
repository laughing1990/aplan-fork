package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tiantian
 * @date 2019/10/23
 */
@Data
@ApiModel("项目基础信息类")
public class AeaProjInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private String projInfoId;

    @ApiModelProperty("地方编码")
    private String localCode;

    @ApiModelProperty("工程编码")
    private String gcbm;

    @ApiModelProperty("项目名称")
    private String projName;

    @ApiModelProperty("立项类型")
    private String projType;

    @ApiModelProperty("立项类型文本")
    private String projTypeText;

    @ApiModelProperty("项目级别")
    private String projLevel;

    @ApiModelProperty("项目级别文本")
    private String projLevelText;

    @ApiModelProperty("建筑面积(m2)")
    private Double buildAreaSum; // (建筑面积(m2))

    @ApiModelProperty("建设性质")
    private String projNature;

    @ApiModelProperty("建设性质文本")
    private String projNatureText;

    @ApiModelProperty("所属主题定义ID")
    private String themeId;

    @ApiModelProperty("主题版本id")
    private String themeVerId;

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @ApiModelProperty("行政区划")
    private String regionalism;

    @ApiModelProperty("行政区划文本")
    private String regionText;

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("主题名称")
    private String themeName;

    @ApiModelProperty("是否有子项目")
    private boolean hasChildren;

    @ApiModelProperty("是否代办项目")
    private boolean ifAgentProj;

    @ApiModelProperty("代办中心名称")
    private String agentName;

}
