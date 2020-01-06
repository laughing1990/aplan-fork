package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("项目信息类")
public class AeaRootProjInfoVo {
    @ApiModelProperty("主键ID")
    private java.lang.String projInfoId; // (主键)
    @ApiModelProperty("地方编码")
    private java.lang.String localCode; // (地方编码)
    @ApiModelProperty("项目名称")
    private java.lang.String projName; // (项目名称)
    @ApiModelProperty("建设地点")
    private java.lang.String projectAddress; // (建设地点)
    @ApiModelProperty("逻辑删除标记：0表示正常记录，1表示已删除记录")
    private java.lang.String isDeleted; // (逻辑删除标记。0表示正常记录，1表示已删除记录。)
    @ApiModelProperty("所属主题定义ID")
    private java.lang.String themeId; // (所属主题定义ID（项目类型），包括：)
    @ApiModelProperty(value = "主题版本id", dataType = "string")
    private String themeVerId;
    @ApiModelProperty("项目标识，r表示ROOT项目，p表示发改项目，c表示子项目或子子项目")
    private java.lang.String projFlag; // (项目标识，r表示ROOT项目，p表示发改项目，c表示子项目或子子项目)
    @ApiModelProperty("工程编码")
    private java.lang.String gcbm; // (工程编码)
    private String rootOrgId;//根组织ID
    @ApiModelProperty(value = "行政区划", dataType = "string")
    private java.lang.String regionalism;
    @ApiModelProperty("是否代办项目")
    private String isAgentProj;
    @ApiModelProperty("代办状态")
    private String projAgentState;
    @ApiModelProperty("代办申请ID")
    private String applyAgentId;
    @ApiModelProperty("主题名称（非表字段）")
    private java.lang.String themeName; // (主题名称（非表字段）)


    public static AeaRootProjInfoVo format(AeaProjInfo aeaProjInfo) {
        AeaRootProjInfoVo vo=new AeaRootProjInfoVo();
        BeanUtils.copyProperties(aeaProjInfo,vo);
        return vo;
    }
}
