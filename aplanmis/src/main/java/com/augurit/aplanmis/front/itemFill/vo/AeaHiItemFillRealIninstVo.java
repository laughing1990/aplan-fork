package com.augurit.aplanmis.front.itemFill.vo;

import com.augurit.aplanmis.common.domain.AeaHiItemFillDueIninst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@ApiModel("容缺审核已经补齐的材料数据模型")
public class AeaHiItemFillRealIninstVo {

    @ApiModelProperty(value = "主键", required = false, dataType="string")
    private String realIninstId; // (ID)

    @ApiModelProperty(value = "补齐主表ID", required = false, dataType="string")
    private String fillId;

    @ApiModelProperty(value = "要求补齐输入ID", required = false, dataType="string")
    private String dueIninstId;

    @ApiModelProperty(value = "输入输出实例ID", required = false, dataType="string")
    private String inoutinstId;

    @ApiModelProperty(value = "是否通过，0表示未通过，1表示通过", required = false, dataType="string")
    private String isPass;

    @ApiModelProperty(value = "附件总数", required = false, dataType="string")
    private Long attCount;

    @ApiModelProperty(value = "实际纸质数", required = false, dataType="string")
    private Long realPaperCount;

    @ApiModelProperty(value = "实际复印件数", required = false, dataType="string")
    private Long realCopyCount;

    @ApiModelProperty(value = "创建人", required = false, dataType="string")
    private String creater;

    @ApiModelProperty(value = "创建时间", required = false, dataType="string")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改人", required = false, dataType="string")
    private String modifier;

    @ApiModelProperty(value = "修改时间", required = false, dataType="string")
    private java.util.Date modifyTime;

    @ApiModelProperty(value = "根组织ID", required = false, dataType="string")
    private String rootOrgId;

    public AeaHiItemFillDueIninst convertToAeaHiItemFillDueIninstVo(){
        AeaHiItemFillDueIninst temp = new AeaHiItemFillDueIninst();
        BeanUtils.copyProperties(this,temp);
        return temp;
    }

    public void createByAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst){
        BeanUtils.copyProperties(aeaHiItemFillDueIninst,this);
    }
}
