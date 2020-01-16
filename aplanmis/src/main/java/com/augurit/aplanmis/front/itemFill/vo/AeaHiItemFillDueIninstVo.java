package com.augurit.aplanmis.front.itemFill.vo;


import com.augurit.aplanmis.common.domain.AeaHiItemFillDueIninst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("容缺审核待补齐材料数据模型")
public class AeaHiItemFillDueIninstVo {

    @ApiModelProperty(value = "主键", required = false, dataType="string")
    private String dueIninstId;

    @ApiModelProperty(value = "补齐主表ID", required = false, dataType="string")
    private String fillId;

    @ApiModelProperty(value = "输入输出实例ID", required = false, dataType="string")
    private String inoutinstId;

    @ApiModelProperty(value = "补齐意见", required = false, dataType="string")
    private String fillOpinion;

    @ApiModelProperty(value = "补齐截止期限类型，0表示证件领取前，1表示证件签发前，2表示证件领取后3个月", required = false, dataType="string")
    private String timeLimitType;

    @ApiModelProperty(value = "要求纸质材料数量", required = false, dataType="string")
    private Long paperCount;

    @ApiModelProperty(value = "要求复印件材料数量", required = false, dataType="string")
    private Long copyCount;

    @ApiModelProperty(value = "是否需要电子件：1 是，0 否", required = false, dataType="string")
    private String isNeedAtt;

    @ApiModelProperty(value = "创建人", required = false, dataType="string")
    private String creater;

    @ApiModelProperty(value = "创建时间", required = false, dataType="string")
    private java.util.Date createTime;

    @ApiModelProperty(value = "根组织ID", required = false, dataType="string")
    private String rootOrgId;

    public AeaHiItemFillDueIninst convertToAeaHiItemFillDueInst(){
        AeaHiItemFillDueIninst temp = new AeaHiItemFillDueIninst();
        BeanUtils.copyProperties(this,temp);
        return temp;
    }

    public void createByAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst){
        BeanUtils.copyProperties(aeaHiItemFillDueIninst,this);
    }
}
