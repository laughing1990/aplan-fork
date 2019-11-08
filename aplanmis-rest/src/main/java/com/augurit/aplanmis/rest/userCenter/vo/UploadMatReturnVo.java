package com.augurit.aplanmis.rest.userCenter.vo;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 导入文件；一键提取；一件分拣 返回vo
 */
@Data
@Api("导入文件；一键提取；自动分拣 返回vo")
public class UploadMatReturnVo {
    @ApiModelProperty(value = "材料matId")
    private String matId;
    @ApiModelProperty(value = "附件列表")
    private List<BscAttFileAndDir> fileList;
    @ApiModelProperty(value = "matinstId")
    private String matinstId;

    public UploadMatReturnVo(String matId, List<BscAttFileAndDir> fileList) {
        this.matId = matId;
        this.fileList = fileList;
    }

    public UploadMatReturnVo(String matId, List<BscAttFileAndDir> fileList, String matinstId) {
        this.matId = matId;
        this.fileList = fileList;
        this.matinstId = matinstId;

    }
}
