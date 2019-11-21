package com.augurit.aplanmis.common.vo;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@ApiModel("我的材料库列表vo")
public class MyMatFilesVo {
    @ApiModelProperty(value = "电子附件列表")
    private List<BscAttFileAndDir> bscAttFileAndDir;
    @ApiModelProperty(value = "材料实例名称")
    private String matinstName;
    @ApiModelProperty(value = "材料实例id")
    private String matinstId;
    @ApiModelProperty(value = "材料id")
    private String matId;
    @ApiModelProperty(value = "材料实例编码")
    private String matinstCode;
    @ApiModelProperty(value = "电子件数目")
    private Long attCount;
    @ApiModelProperty(value = "创建人")
    private String creater; // (创建人)
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
}
