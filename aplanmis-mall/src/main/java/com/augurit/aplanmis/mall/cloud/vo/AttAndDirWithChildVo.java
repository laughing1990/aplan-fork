package com.augurit.aplanmis.mall.cloud.vo;


import com.augurit.agcloud.bsc.domain.BscAttForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "文件及目录实体")
public class AttAndDirWithChildVo {
    @ApiModelProperty(value = "目录列表含有子目录及文件")
    List<CloudAttDir> dirs;
    @ApiModelProperty(value = "文件列表")
    List<BscAttForm> atts;
}
