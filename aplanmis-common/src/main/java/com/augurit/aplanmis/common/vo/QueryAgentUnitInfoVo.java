package com.augurit.aplanmis.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/12
 */
@Data
@ApiModel("查询符合条件的中介机构")
public class QueryAgentUnitInfoVo {

    @ApiModelProperty(value = "服务ID")
    private String serviceId; // 服务ID

    @ApiModelProperty(value = "是否需要资质要求：1 需要，0 不需要")
    private String isQualRequire; // 是否需要资质要求：1 需要，0 不需要

    @ApiModelProperty(value = "资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项")
    private String qualRequireType; // 资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项

    @ApiModelProperty(value = "资质")
    private List<Qual> quals;

    @ApiModelProperty(value = "专业要求的数据结构", notes = "0表示作为判断的专业要求数据为整个树结构，1表示作为判断的专业要求数据仅为选中的，默认为1")
    private String onlySelected;


    @Data
    @ApiModel("资质")
    public static class Qual{
        @ApiModelProperty(value = "资质ID")
        private String qualId; // 资质ID
        @ApiModelProperty(value = "专业资质ID")
        private List<MajorQualRequire> majorQualRequires;

    }

    @Data
    public static class MajorQualRequire{
        @ApiModelProperty(value = "专业ID")
        private String majorId; // 专业关联ID
        @ApiModelProperty(value = "资质等级ID")
        private String qualLevelId; // 资质等级ID
        @ApiModelProperty(value = "子专业资质要求")
        private List<MajorQualRequire> child;//子专业资质要求
        @ApiModelProperty(value = "1为资质要求选中状态")
        private String selected;//1为资质要求选中状态
        @ApiModelProperty(value = "资质ID")
        private String qualId; // 资质ID
        @ApiModelProperty(value = "父ID")
        private String parentMajorId;
    }
}
