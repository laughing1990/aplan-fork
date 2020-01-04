package com.augurit.aplanmis.front.subject.project.vo;

import com.augurit.aplanmis.common.domain.AeaProjApplySplit;
import com.augurit.aplanmis.front.subject.unit.vo.UnitVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("拆分子工程信息")
@Getter
@Setter
public class SplitedProjVo {

    @ApiModelProperty(value = "子工程拆分申请记录")
    private AeaProjApplySplit aeaProjApplySplit;

    @ApiModelProperty("总投资")
    private Double investSum;

    @ApiModelProperty("建设规模及内容")
    private String scaleContent;

    @ApiModelProperty("用地面积")
    private Double xmYdmj;

    @ApiModelProperty("建筑面积(m2)")
    private java.lang.Double buildAreaSum;

    @ApiModelProperty(value = "企业单位信息")
    private UnitVo unitVo;

}
