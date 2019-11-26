package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel("批量保存材料实例")
public class SaveMatinstVo {

    @ApiModelProperty(value = "材料id与材料份数", dataType = "string")
    private List<MatCountVo> matCountVos;

    @ApiModelProperty(value = "企业单位id", dataType = "string", notes = "个人申报时没有值")
    private String unitInfoId;

    @ApiModelProperty(value = "项目id", dataType = "string", required = true)
    private String projInfoId;

    @ApiModelProperty(value = "联系人id")
    private String linkmanInfoId;

    public SaveMatinstVo() {
        matCountVos = new ArrayList<>();
    }

    @Data
    public static class MatCountVo {
        @ApiModelProperty(value = "材料id")
        private String matId;

        @ApiModelProperty(value = "纸质件份数")
        private int paperCnt;

        @ApiModelProperty(value = "复印件份数")
        private int copyCnt;

        @ApiModelProperty(value = "材料性质", notes = "m: 普通材料; c: 证照材料; f: 在线表单")
        private String matProp;

        @ApiModelProperty(value = "证照定义ID")
        private String certId;

        @ApiModelProperty(value = "证照名称")
        private String certName;

        @ApiModelProperty(value = "表单定义ID")
        private String stoFormId;

        @ApiModelProperty(value = "表单名称")
        private String formName;

        @ApiModelProperty(value = "材料绑定的事项版本id", notes = "证照材料需要这个字段去获取证照库的证照列表")
        private String itemVerId;
    }

    public Map<String, MatCountVo> buildMatCountMap() {
        Map<String, MatCountVo> matMap = new HashMap<>();
        matCountVos.forEach(mat -> matMap.put(mat.getMatId(), mat));
        return matMap;
    }
}
