package com.augurit.aplanmis.province.vo;

import com.augurit.aplanmis.common.domain.AeaMatinst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xioahutu
 * 单项或并联事项下材料列表实例
 */
@Data
@ApiModel(value = "单项或并联事项下材料列表实例", description = "单项或并联事项下材料列表实例")
public class AeaMatinstAndIteminstVo {
    @ApiModelProperty(value = "阶段名称，单项时为事项名称", name = "")
    private String stageName;
    @ApiModelProperty(value = "通用材料列表", name = "通用材料列表")
    private List<AeaMatinst> commonMatList;
    @ApiModelProperty(value = "事项材料列表", name = "事项材料列表")
    private Map<String, Object> itemMatinst;
    @ApiModelProperty(name = "批文批复材料")
    private List<AeaMatinst> officeMatList;
}
