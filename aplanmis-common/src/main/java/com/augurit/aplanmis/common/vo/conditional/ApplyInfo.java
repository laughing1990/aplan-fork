package com.augurit.aplanmis.common.vo.conditional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 申报数据
 * @author tiantian
 * @date 2019/9/4
 */
@Data
@ApiModel(value = "申报查询返回信息")
public class ApplyInfo extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申报状态")
    private String applyState;
}
