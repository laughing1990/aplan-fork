package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel("批量保存材料实例")
public class SaveMatinstVo {

    @ApiModelProperty(value = "材料id与材料份数", dataType = "string")
    private List<MatCountVo> matCountVos;

    @ApiModelProperty(value = "企业单位id", dataType = "string", notes = "个人申报时没有值", required = false)
    //@NotEmpty(message = "unitInfoId is null")
    private String unitInfoId;

    @ApiModelProperty(value = "项目id", dataType = "string", required = true)
    @NotEmpty(message = "projInfoId is null")
    private String projInfoId;

    public SaveMatinstVo() {
        matCountVos = new ArrayList<>();
    }

    @Data
    public static class MatCountVo {
        @ApiModelProperty(value = "材料id")
        @NotEmpty(message = "matId is null")
        private String matId;

        @ApiModelProperty(value = "纸质件份数")
        private int paperCnt;

        @ApiModelProperty(value = "复印件份数")
        private int copyCnt;
    }

    public Map<String, MatCountVo> buildMatCountMap() {
        Map<String, MatCountVo> matMap = new HashMap<>();
        matCountVos.forEach(mat -> matMap.put(mat.getMatId(), mat));
        return matMap;
    }
}
