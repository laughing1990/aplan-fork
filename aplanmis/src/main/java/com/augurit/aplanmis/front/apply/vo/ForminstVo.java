package com.augurit.aplanmis.front.apply.vo;

import com.augurit.aplanmis.common.domain.AeaApplyinstForminst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@ApiModel("已填表单")
@Getter
@Setter
public class ForminstVo {

    @ApiModelProperty(value = "表单定义id")
    private String stoFormId;

    @ApiModelProperty(value = "表单实例id")
    private String forminstId;

    @ApiModelProperty(value = "表单填写时间")
    private Date modifyTime;

    @ApiModelProperty(value = "表单创建时间")
    private Date createTime;

    public static ForminstVo from(AeaApplyinstForminst aeaApplyinstForminst) {
        ForminstVo forminstVo = new ForminstVo();
        forminstVo.setStoFormId(aeaApplyinstForminst.getStoFormId());
        forminstVo.setForminstId(aeaApplyinstForminst.getForminstId());
        forminstVo.setModifyTime(aeaApplyinstForminst.getModifierTime());
        forminstVo.setCreateTime(aeaApplyinstForminst.getCreateTime());
        return forminstVo;
    }
}
