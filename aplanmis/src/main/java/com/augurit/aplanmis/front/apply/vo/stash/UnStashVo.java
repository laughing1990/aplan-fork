package com.augurit.aplanmis.front.apply.vo.stash;

import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.front.apply.vo.ForminstVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@ApiModel("回显vo")
@Getter
@Setter
public class UnStashVo {

    @ApiModelProperty(value = "申报实例")
    protected AeaHiApplyinst aeaHiApplyinst;

    @ApiModelProperty(value = "项目id")
    protected String projInfoId;

    @ApiModelProperty(value = "主题id")
    protected String themeId;

    @ApiModelProperty(value = "主题版本id")
    protected String themeVerId;

    @ApiModelProperty(value = "阶段id")
    protected String stageId;

    @ApiModelProperty(value = "领件人信息对象id")
    protected String smsInfoId;

    @ApiModelProperty(value = "已填表单列表")
    protected List<ForminstVo> forminstVos;

    UnStashVo() {
        forminstVos = new ArrayList<>();
    }
}
