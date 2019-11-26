package com.augurit.aplanmis.front.apply.vo.attach;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("导入材料附件列表查询参数")
public class AttImportQueryVo {

    @ApiModelProperty(value = "材料编码， 多个用逗号分隔", required = true)
    private String matCode;

    @ApiModelProperty(value = "申报主体：1 为单位，0 为 个人", required = true)
    private String applySubject;

    @ApiModelProperty(value = "企业单位id， 多个用逗号分隔", required = true)
    private String unitInfoId;

    @ApiModelProperty(value = "联系人id", required = true)
    private String linkmanInfoId;

    @ApiModelProperty(value = "查询过滤关键字, 根据材料名称过滤")
    private String keyword;

    @ApiModelProperty(value = "材料实例id, 当传入该参数时， 查询的附件列表不包含该材料实例的附件")
    private String matinstId;

}
