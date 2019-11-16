package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tiantian
 * @date 2019/6/10
 */
@Data
@ApiModel("中介服务事项信息和要求")
public class AeaItemServiceVo {

    private String agentItemBasicId;

    @ApiModelProperty(value = "中介事项版本ID")
    private String agentItemVerId;

    @ApiModelProperty(value = "中介事项ID")
    private String agentItemId;

    @ApiModelProperty(value = "中介事项编码")
    private String agentItemCode;

    @ApiModelProperty(value = "中介事项名称")
    private String agentItemName;
    @ApiModelProperty(value = "服务对象")
    private String agentItemFwdx;
    @ApiModelProperty(value = "中介事项对应的部门ID")
    private String agentOrgId;

    @ApiModelProperty(value = "部门名称")
    private String agentOrgName;
    @ApiModelProperty(value = "办件类型")
    private String agentItemProperty;

    private String agentItemPropertyName;

    @ApiModelProperty(value = "办理时限，以工作日为单位 ")
    private String agentItemDueNum;
    private String agentItemDueUnitType;
    @ApiModelProperty(value = "承诺时限单位")
    private String agentItemBjType;
    @ApiModelProperty(value = "对应的行政事项名称")
    private String itemName;

    @ApiModelProperty(value = "服务类型")
    private String serviceName;

    @ApiModelProperty(value = "服务内容")
    private String serviceContent;

    @ApiModelProperty(value = "服务结果")
    private String serviceResult;

    @ApiModelProperty(value = "时限说明")
    private String timeLimitExplain;

    @ApiModelProperty(value = "服务时限要求")
    private String serviceTimeLimit;

    @ApiModelProperty(value = "是否需要资质要求",notes = "1 需要，0 不需要")
    private String isQualRequire;
    @ApiModelProperty(value = "资质要求",notes = "1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项")
    private String qualRequireType;
    @ApiModelProperty(value = "资质要求说明",notes = "当IS_QUAL_REQUIRE =1 时，必填")
    private String qualRequireExplain;
    @ApiModelProperty(value = "资质备案说明")
    private String qualRecordRequire;
    @ApiModelProperty(value = "是否仅承诺服务",notes = "1 是，0 否")
    private String promiseService;
    @ApiModelProperty(value = "其他要求说明")
    private String otherRequireExplain;
    @ApiModelProperty(value = "是否需要执业/职业人员要求",notes = "1 需要，0 不需要")
    private String isRegisterRequire;
    @ApiModelProperty(value = "是否需要执业/执业/职业人员总数",notes = "当IS_REGISTER_REQUIRE =1 时，必填")
    private String registerTotal;
    @ApiModelProperty(value = "执业/职业人员要求",notes = "当IS_REGISTER_REQUIRE =1 时，必填")
    private String registerRequire;
    @ApiModelProperty(value = "是否需要备案要求",notes = "1 需要，0 不需要")
    private String isRecordRequire;
    @ApiModelProperty(value = "备案要求说明",notes = "当IS_RECORD_REQUIRE =1 时，必填")
    private String recordRequireExplain;

    private String rootOrgId;


}
