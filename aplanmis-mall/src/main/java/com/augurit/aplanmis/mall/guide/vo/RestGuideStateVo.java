package com.augurit.aplanmis.mall.guide.vo;

import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaItemState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("单事项办事指南VO")
public class RestGuideStateVo {

    @ApiModelProperty("受理范围")
    private String acceptCondition;
    @ApiModelProperty("实施部门")
    private String deptName;
    @ApiModelProperty("受理权限")
    private String acceptAccess;
    @ApiModelProperty("情形(包含情形下的材料)列表")
    private List<RestStateInnerVo> states;
    @ApiModelProperty("是否收费")
    private String isFee;
    @ApiModelProperty("收费文本")
    private String chargeTypeText;
    @ApiModelProperty("办理时限")
   private String promissDay;
    @ApiModelProperty("办理结果")
    private String resultName;
    private List<RestStateMatInnerVo> mats;

    @Data
    @ApiModel("单事项办事指南->情形VO")
    public static class RestStateInnerVo{
        @ApiModelProperty("情形名称")
        private String stateName;
        @ApiModelProperty("情形ID")
        private String stateId;
        @ApiModelProperty("情形索引")
        private String index;
        private List<RestStateMatInnerVo> mats;
        static int  indexNum = 1;
        public static RestStateInnerVo build(AeaItemState aeaItemState){
            ++indexNum;
            RestStateInnerVo vo = new RestStateInnerVo();
            vo.setStateName(aeaItemState.getStateName());
            vo.setIndex("情形"+NumToChinese.getChiness(indexNum));
            return vo;
        }
    }

    @Data
    @ApiModel("单事项办事指南->情形VO->材料")
    public static class RestStateMatInnerVo{
        @ApiModelProperty("材料名称")
        private String matName;
        @ApiModelProperty("全程网办接件标准")
        private String reviewKeyPoints;
        @ApiModelProperty("备注")
        private String  matMemo;

        public static RestStateMatInnerVo build(AeaItemMat aeaItemMat){
            RestStateMatInnerVo vo = new RestStateMatInnerVo();
            vo.setMatName(aeaItemMat.getMatName());
            vo.setMatMemo(aeaItemMat.getMatMemo());
            vo.setReviewKeyPoints(aeaItemMat.getReviewKeyPoints());
            return vo;
        }
    }

}
