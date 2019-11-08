package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/13
 */
@Data
@ApiModel("中介服务及对应的资质相关信息")
public class AeaImServiceInfoVo {
    @ApiModelProperty(value = "中介服务列表")
    private List<AeaImServiceVo> list;
}
