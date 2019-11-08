package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ZhangXinhui
 * @date 2019/9/26 026 10:47
 * @desc
 **/
@Data
@ApiModel("办理异常统计")
public class ApplyUnusualStatisticVo {

    private Long sortNo;

    private String orgId;

    private String orgName;

    private String itemId;

    private String itemName;


    @ApiModelProperty(value = "不受理率" ,dataType = "String")
    private String notAcceptRate;

    @ApiModelProperty(value = "逾期率" ,dataType = "String")
    private String overdueRate;


}
