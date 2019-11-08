package com.augurit.aplanmis.mall.cloud.vo;


import com.augurit.agcloud.bsc.domain.BscAttDir;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("云盘材料库实体")
public class CloudDirListVo {

    @ApiModelProperty("目录名称")
    private String dirName;
    @ApiModelProperty("目录ID")
    private String dirId;
    @ApiModelProperty("子目录列表")
    private List<BscAttDir> childDirs;
}
