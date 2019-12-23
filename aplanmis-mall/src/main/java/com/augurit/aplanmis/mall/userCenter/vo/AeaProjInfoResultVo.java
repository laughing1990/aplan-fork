package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("项目信息返回结果VO")
public class AeaProjInfoResultVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "项目ID")
    private String projInfoId;
    @ApiModelProperty(value = "项目/工程名称")
    private String projName;
    @ApiModelProperty(value = "项目代码")
    private String localCode;
    @ApiModelProperty(value = "工程编码")
    private String gcbm;
    @ApiModelProperty(value = "项目类型")
    private String themeName;
    @ApiModelProperty(value = "项目类型ID")
    private String themeId;
//    @ApiModelProperty("是否有子项目")
//    private boolean hasChildren;
    @ApiModelProperty("子项目列表")
    private List<AeaProjInfoResultVo> children=new ArrayList<>();

    public static AeaProjInfoResultVo build(AeaProjInfo aeaProjInfo) {
        AeaProjInfoResultVo aeaProjInfoResultVo = new AeaProjInfoResultVo();
        BeanUtils.copyProperties(aeaProjInfo, aeaProjInfoResultVo);
        return aeaProjInfoResultVo;
    }
}
