package com.augurit.aplanmis.common.vo;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "附件上传结果", description = "附件上传结果")
public class UploadResult {
    @ApiModelProperty(value = "附件关联ID")
    String recordId;
    @ApiModelProperty(value = "附件列表")
    List<AttForm> attForms = new ArrayList<>();

    public UploadResult(String recordId, List<BscAttForm> bscAttForms) {
        this.recordId = recordId;
        for (BscAttForm form : bscAttForms) {
            AttForm attForm = new AttForm();
            BeanUtils.copyProperties(form, attForm);
            this.attForms.add(attForm);
        }
    }

    @ApiModel
    @Data
    public class AttForm {
        @ApiModelProperty(value = "附件ID")
        private String detailId;
        private String attCode;
        @ApiModelProperty(value = "附件名字")
        private String attName;
    }
}
