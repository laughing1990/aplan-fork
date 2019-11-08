package com.augurit.aplanmis.common.vo.conditional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tiantian
 * @date 2019/9/7
 */
@Data
@ApiModel(value = "办件查询返回信息")
public class PartsInfo extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "事情实例id")
    private String iteminstId;

    @ApiModelProperty(value = "事项实例状态")
    private String iteminstState;

}
