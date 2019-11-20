package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.domain.AeaItemFrontPartform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tiantian
 * @date 2019/11/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "事项前置扩展表检测Vo类")
public class AeaItemFrontPartformVo extends AeaItemFrontPartform {

    @ApiModelProperty("扩展属性：是否智能表单")
    private String isSmartForm;

    @ApiModelProperty("扩展属性：阶段扩展表单名称")
    private String partformName;

    @ApiModelProperty("扩展属性：表单ID")
    private String stoFormId;

}
