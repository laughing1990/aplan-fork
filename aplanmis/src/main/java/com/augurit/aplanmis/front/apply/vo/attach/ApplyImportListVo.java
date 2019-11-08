package com.augurit.aplanmis.front.apply.vo.attach;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("附件")
public class ApplyImportListVo {
    // 附件id
    @ApiModelProperty(value = "附件id")
    private String fileId;
    // 附件名称
    @ApiModelProperty(value = "附件名称")
    private String fileName;
    // 附件类型
    @ApiModelProperty(value = "附件类型")
    private String fileType;


    public ApplyImportListVo(String fileId, String fileName, String fileType) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
