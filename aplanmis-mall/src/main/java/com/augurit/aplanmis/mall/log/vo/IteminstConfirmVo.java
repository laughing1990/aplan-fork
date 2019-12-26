package com.augurit.aplanmis.mall.log.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel("事项确认时事项返回结果VO")
public class IteminstConfirmVo {
    @ApiModelProperty(value = "事项ID")
    private String itemId;
    @ApiModelProperty(value = "事项版本ID")
    private String itemVerId;
    @ApiModelProperty(value = "事项实例ID")
    private String iteminstId;
    @ApiModelProperty(value = "事项名称")
    private String itemName;
    @ApiModelProperty(value = "承诺审批时限")
    private String dueNum;
    @ApiModelProperty(value = "是否必选")
    private String isMustSelected;
    @ApiModelProperty(value = "是否申请人选择")
    private String isApplySelected;
    @ApiModelProperty(value = "是否部门辅导选择")
    private String isDeptSelected;
    @ApiModelProperty(value = "情形列表")
    private List<Map> stateList;
    @ApiModelProperty(value = "部门意见")
    private String deptComments;
    @ApiModelProperty(value = "结果物")
    private List<String> isThemeChange;

}
