package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("部门辅导事项参数")
public class AeaGuideItemVo {
    @ApiModelProperty("事项ID")
    private String itemId;
    @ApiModelProperty("事项版本ID")
    private String itemVerId;
    @ApiModelProperty("标准事项版本ID")
    private String baseItemVerId;

    @ApiModelProperty("申请人意见（申请人）")
    private String applySelOpinion;
    @ApiModelProperty("是否智能引导选择（申请人） 是 1 否0")
    private String isITSel;
    @ApiModelProperty("是否申请人选择（申请人） 是 1 否0")
    private String isApplySel;
    @ApiModelProperty("是否标准事项 1是 0否")
    private String isCatalog;
    @ApiModelProperty("实施事项换算方式 ")
    private String itemExchangeWay;//实施事项换算方式 0 按照审批行政区划和属地行政区划换算 1 仅按照审批行政区划换算

    public static AeaGuideItemVo format(AeaItemBasic aeaItemBasic) {
        AeaGuideItemVo aeaGuideItemVo=new AeaGuideItemVo();
        aeaGuideItemVo.setItemId(aeaItemBasic.getItemId());
        aeaGuideItemVo.setItemVerId(aeaItemBasic.getItemVerId());
        aeaGuideItemVo.setIsCatalog(aeaItemBasic.getIsCatalog());
        aeaGuideItemVo.setItemExchangeWay(aeaItemBasic.getItemExchangeWay());
        return aeaGuideItemVo;
    }
}
