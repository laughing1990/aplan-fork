package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("项目信息返回结果VO")
public class AeaProjInfoResultVo {
    @ApiModelProperty(value = "项目/工程名称")
    private String projName;
    @ApiModelProperty(value = "项目ID")
    private String projInfoId;
    @ApiModelProperty(value = "项目代码")
    private String localCode;
    @ApiModelProperty(value = "工程编码")
    private String gcbm;
    @ApiModelProperty(value = "所属主题")
    private String themeName;
    @ApiModelProperty(value = "所属主题ID")
    private String themeId;

    public static AeaProjInfoResultVo build(AeaProjInfo aeaProjInfo) {
        AeaProjInfoResultVo aeaProjInfoResultVo = new AeaProjInfoResultVo();
        BeanUtils.copyProperties(aeaProjInfo, aeaProjInfoResultVo);
        return aeaProjInfoResultVo;
    }
}
