package com.augurit.aplanmis.common.domain;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
/**
* 项目与代办中心（本质也是窗口）代办关联表-模型
*/
@Data
public class AeaProjWindow implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "projWindowId", value = "主键")
    private String projWindowId;

    @ApiModelProperty(name = "projInfoId", value = "项目ID")
    private String projInfoId;

    @ApiModelProperty(name = "windowId", value = "窗口ID")
    private String windowId;

    @ApiModelProperty(name = "creater", value = "创建人")
    private String creater;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty(name = "rootOrgId", value = "根组织ID")
    private String rootOrgId;

}
