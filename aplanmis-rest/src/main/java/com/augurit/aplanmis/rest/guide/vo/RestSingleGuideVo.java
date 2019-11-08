package com.augurit.aplanmis.rest.guide.vo;


import com.augurit.aplanmis.common.domain.AeaItemGuide;
import com.augurit.aplanmis.common.domain.AeaItemGuideExtend;
import com.augurit.aplanmis.common.domain.AeaItemGuideMaterials;
import com.augurit.aplanmis.common.domain.AeaItemGuideQuestions;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaServiceWindowItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("单项办事指南")
public class RestSingleGuideVo {


    @ApiModelProperty("设立依据")
    private List<AeaItemServiceBasic> basicList;
    @ApiModelProperty("窗口相关信息")
    private List<AeaServiceWindowItem> windowRelList;
    @ApiModelProperty("常见问题")
    private List<AeaItemGuideQuestions> questions;
    @ApiModelProperty("拓展信息")
    private AeaItemGuideExtend aeaItemGuideExtend;
    @ApiModelProperty("基本信息")
    private AeaItemGuide aeaItemGuide;
    @ApiModelProperty("中介服务")
    private List<AeaItemGuideMaterials> materialsList;
    @ApiModelProperty("情形信息")
    private List<AeaItemState> stateList;
    @ApiModelProperty("材料列表")
    private List<AeaItemMat> matList;

}
