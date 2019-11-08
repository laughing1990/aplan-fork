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
    /**
     * 中介事项主键
     */
    private String agentItemBasicId;
    /**
     * 中介事项版本ID
     */
    @ApiModelProperty(value = "中介事项版本ID")
    private String agentItemVerId;
    /**
     * 中介事项ID
     */
    @ApiModelProperty(value = "中介事项ID")
    private String agentItemId;
    /**
     * 中介事项编码
     */
    @ApiModelProperty(value = "中介事项编码")
    private String agentItemCode;
    /**
     * 中介事项名称
     */
    @ApiModelProperty(value = "中介事项名称")
    private String agentItemName;
    /**
     * 中介事项对应的部门ID
     */
    @ApiModelProperty(value = "中介事项对应的部门ID")
    private String agentOrgId;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String agentOrgName;
    /**
     * 对应的行政事项名称
     */
    @ApiModelProperty(value = "对应的行政事项名称")
    private String itemName;

    @ApiModelProperty(value = "服务类型")
    private String serviceName;

    /**
     * 服务内容
     */
    @ApiModelProperty(value = "服务内容")
    private String serviceContent;
    /**
     * 服务结果
     */
    @ApiModelProperty(value = "服务结果")
    private String serviceResult;
    /**
     * 时限说明
     */
    @ApiModelProperty(value = "时限说明")
    private String timeLimitExplain;
    /**
     * 服务时限要求
     */
    @ApiModelProperty(value = "服务时限要求")
    private String serviceTimeLimit;

    @ApiModelProperty(value = "是否需要资质要求",notes = "1 需要，0 不需要")
    private String isQualRequire; // (是否需要资质要求：1 需要，0 不需要)
    @ApiModelProperty(value = "资质要求",notes = "1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项")
    private String qualRequireType; // (资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项)
    @ApiModelProperty(value = "资质要求说明",notes = "当IS_QUAL_REQUIRE =1 时，必填")
    private String qualRequireExplain; // (资质要求说明（当IS_QUAL_REQUIRE =1 时，必填）)
    @ApiModelProperty(value = "资质备案说明")
    private String qualRecordRequire; // (资质备案说明)
    @ApiModelProperty(value = "是否仅承诺服务",notes = "1 是，0 否")
    private String promiseService; // (是否仅承诺服务：1 是，0 否)
    @ApiModelProperty(value = "其他要求说明")
    private String otherRequireExplain; // (其他要求说明)
    @ApiModelProperty(value = "是否需要执业/职业人员要求",notes = "1 需要，0 不需要")
    private String isRegisterRequire; // (是否需要执业/职业人员要求：1 需要，0 不需要)
    @ApiModelProperty(value = "是否需要执业/执业/职业人员总数",notes = "当IS_REGISTER_REQUIRE =1 时，必填")
    private String registerTotal; // (执业/职业人员总数（当IS_REGISTER_REQUIRE =1 时，必填）)
    @ApiModelProperty(value = "执业/职业人员要求",notes = "当IS_REGISTER_REQUIRE =1 时，必填")
    private String registerRequire; // (执业/职业人员要求（当IS_REGISTER_REQUIRE =1 时，必填）)
    @ApiModelProperty(value = "是否需要备案要求",notes = "1 需要，0 不需要")
    private String isRecordRequire; // (是否需要备案要求：1 需要，0 不需要)
    @ApiModelProperty(value = "备案要求说明",notes = "当IS_RECORD_REQUIRE =1 时，必填")
    private String recordRequireExplain; // (备案要求说明（当IS_RECORD_REQUIRE =1 时，必填）)


}
