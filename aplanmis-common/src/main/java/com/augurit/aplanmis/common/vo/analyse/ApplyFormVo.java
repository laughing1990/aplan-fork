package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("分类申报统计实体")
public class ApplyFormVo implements Serializable {

    @ApiModelProperty(value = "窗口名称", dataType="String")
    private String windowName;
    @ApiModelProperty(value = "申请实例状态" ,dataType = "String")
    private String applyinstState;
    /**
     * net网上申报，win窗口申报
     */
    @ApiModelProperty(value = "申报来源" ,dataType = "String")
    private String applyinstSource;
    @ApiModelProperty(value = "申报类型，0并联，1单项" ,dataType = "String")
    private String isApprove;
    @ApiModelProperty(value = "申报目前状态，1正常、2预警、3逾期")
    private String state;
    @ApiModelProperty(value = "申请实例id")
    private String applyinstId;

    //
    @ApiModelProperty(value = "区域id")
    private String regionId;
    @ApiModelProperty(value = "区域名称")
    private String regionName;


}
