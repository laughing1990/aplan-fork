package com.augurit.aplanmis.front.supermarket.vo;

import com.augurit.aplanmis.front.apply.vo.SaveMatinstVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("服务结果纸质件")
public class ReceiveServiceResult {

    @ApiModelProperty(value = "材料id与材料份数", dataType = "string")
    private List<SaveMatinstVo.MatCountVo> matCountVos;

    @ApiModelProperty(value = "企业单位id", dataType = "string", notes = "个人申报时没有值")
    private String unitInfoId;

    @ApiModelProperty(value = "申报实例ID", dataType = "string", required = true)
    private String applyinstId;

    @ApiModelProperty(value = "事项实例ID", dataType = "string", required = true)
    private String iteminstId;

    @ApiModelProperty(value = "联系人id")
    private String linkmanInfoId;

    public ReceiveServiceResult() {
        matCountVos = new ArrayList<>();
    }

    @Data
    public static class MatCountVo {

        @ApiModelProperty(value = "材料实例ID纸质件")
        private String paperMatinstId;

        @ApiModelProperty(value = "材料实例ID纸质件")
        private String copyMatinstId;

        @ApiModelProperty(value = "材料id")
        private String matId;

        @ApiModelProperty(value = "纸质件份数")
        private int paperCnt;

        @ApiModelProperty(value = "复印件份数")
        private int copyCnt;

        @ApiModelProperty(value = "材料性质", notes = "m: 普通材料; c: 证照材料; f: 在线表单")
        private String matProp;

        @ApiModelProperty(value = "材料绑定的事项版本id", notes = "证照材料需要这个字段去获取证照库的证照列表")
        private String itemVerId;
    }
}
