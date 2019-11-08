package com.augurit.aplanmis.front.apply.vo.attach;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api("一键提取获取所有材料附件")
public class ApplyAbstractQueryVo {
    @ApiModelProperty(value = "项目ids,兼容多项目申报，用，拼接", required = true)
    private String projInfoIds;
    @ApiModelProperty(value = "材料编码ids,用，拼接", required = true)
    private String matCodes;
    @ApiModelProperty(value = "需要导入的材料实例,用逗号拼接，当前材料已经上传过附件才有值；==null，会查询历史及当前的所有附件" +
            "!=null 过滤掉当前材料实例下的附件，只查询历史上传的数据")
    private String matinstIds;
    @ApiModelProperty(value = "企业单位IDS, 逗号拼接, 当传入值时,只查询当前单位下的材料")
    private String unitInfoIds;
}
