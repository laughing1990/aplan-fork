package com.augurit.aplanmis.mall.main.vo;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemState;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@ApiModel("阶段下并联审批事项")
public class ParallelApproveItemVo {


    @ApiModelProperty(name = "itemVerId", value = "事项版本id", dataType = "string")
    private String itemVerId;
    @ApiModelProperty(name = "itemBasicId", value = "事项基本信息id", dataType = "string")
    private String itemBasicId;
    @ApiModelProperty(name = "itemName", value = "事项名称", dataType = "string")
    private String itemName;
    @ApiModelProperty(name = "orgName", value = "实施主体", dataType = "string")
    private String orgName;
    @ApiModelProperty(name = "orgId", value = "实施主体id", dataType = "string")
    private String orgId;
    @ApiModelProperty(name = "sortNo", value = "排序字段", dataType = "string")
    private String sortNo;
    @ApiModelProperty(name = "isDone", value = "是否已办事项", dataType = "string")
    private String isDone;
    @ApiModelProperty(name = "isRecommend", value = "是否推荐", dataType = "string")
    private String isRecommend;
    @ApiModelProperty(name = "allowManual", value = "是否允许人工选择下级承办组织，0表示禁止，1表示允许", allowableValues = "0, 1")
    private String allowManual;
    @ApiModelProperty(name = "coreStateList", value = "并行事项情形list")
    private List<AeaItemState> coreStateList;
    @ApiModelProperty(name = "paraStateList", value = "并联事项情形list")
    private List<AeaItemState> paraStateList;
    @ApiModelProperty(name = "isCatalog", value = "是否标准事项  1标准事项 0 实施事项", dataType = "string")
    private String isCatalog;
    @ApiModelProperty(name = "baseItemVerId", value = "标准事项版本ID", dataType = "string")
    private String baseItemVerId;
    @ApiModelProperty(name = "baseItemName", value = "标准事项名称", dataType = "string")
    private String baseItemName;
    @ApiModelProperty(name = "itemId", value = "事项ID", dataType = "string")
    private String itemId;
    @ApiModelProperty(name = "carryOutItems", value = "实施事项列表")
    private List<AeaItemBasic> carryOutItems;
    @ApiModelProperty(name = "currentCarryOutItem", value = "默认实施事项")
    private AeaItemBasic currentCarryOutItem;
    @ApiModelProperty(name = "itemExchangeWay", value = "实施事项换算方式 0 按照审批行政区划和属地行政区划换算 1 仅按照审批行政区划换算", dataType = "string")
    private String itemExchangeWay;
    @ApiModelProperty(name = "regionId", value = "行政区划ID", dataType = "string")
    private String regionId;
    @ApiModelProperty(name = "regionName", value = "行政区划名称", dataType = "string")
    private String regionName;
    @ApiModelProperty(name = "isGreenWay", value = "是否绿色通道", dataType = "string")
    private String isGreenWay;

    public static ParallelApproveItemVo build(AeaItemBasic aeaItemBasic) {
        Assert.notNull(aeaItemBasic, "事项基本信息不能为空");

        ParallelApproveItemVo vo = new ParallelApproveItemVo();
        BeanUtils.copyProperties(aeaItemBasic,vo);
        vo.setIsRecommend(aeaItemBasic.getIsRecommended());
        return vo;
    }
}
