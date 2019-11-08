package com.augurit.aplanmis.front.basis.item.vo;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "现场登记事项信息")
@Data
public class SpotRegistrationItemVo {

    @ApiModelProperty(name = "itemVerId", value = "事项版本id")
    private String itemVerId;
    @ApiModelProperty(name = "appId", value = "关联的流程模板id")
    private String appId;
    @ApiModelProperty(name = "itemName", value = "事项名称")
    private String itemName;
    @ApiModelProperty(name = "itemAlias", value = "事项别名")
    private String itemAlias;
    @ApiModelProperty(name = "itemBasicId", value = "事项基础信息id")
    private String itemBasicId;
    @ApiModelProperty(name = "itemCode", value = "事项编码")
    private String itemCode;
    @ApiModelProperty(name = "orgId", value = "受理机构id")
    private String orgId;
    @ApiModelProperty(name = "orgName", value = "受理机构")
    private String orgName;
    @ApiModelProperty(value = "行政区划ID", dataType = "string")
    private String regionId;
    @ApiModelProperty(value = "是否标准事项", dataType = "string", notes = "1: 标准事项; 0: 实施事项")
    private String isCatalog;
    @ApiModelProperty(value = "行政区划名称", dataType = "string")
    private String regionName;
    @ApiModelProperty(name = "itemNature", value = "事项性质", allowableValues = "0, 6, 8, 9")
    private String itemNature;
    @ApiModelProperty(name = "itemNatureName", value = "事项性质名称", allowableValues = "标准事项, 市政公用服务, 中介服务事项, 服务协同")
    private String itemNatureName;
    @ApiModelProperty(name = "serviceObject", value = "服务对象")
    private String serviceObject;
    @ApiModelProperty(name = "serviceObjectCode", value = "服务对象编码, 默认为 5")
    private String serviceObjectCode = "5";
    @ApiModelProperty(name = "allowManual", value = "是否允许人工选择下级承办组织，0表示禁止，1表示允许", allowableValues = "0, 1")
    private String allowManual;
    @ApiModelProperty(name = "bjType", value = "承诺时限单位")
    private String bjType;
    @ApiModelProperty(name = "dueNum", value = "办理时限")
    private Double dueNum;
    @ApiModelProperty(name = "dueNum", value = "办件类型")
    private String itemProperty;
    @ApiModelProperty(name = "procedureName", value = "行政事项名称")
    private String procedureName;

    public static SpotRegistrationItemVo fromAeaItemBasic(AeaItemBasic aeaItemBasic, String serviceObject) {
        SpotRegistrationItemVo vo = new SpotRegistrationItemVo();
        vo.setItemVerId(aeaItemBasic.getItemVerId());
        vo.setAppId(aeaItemBasic.getAppId());
        vo.setItemName(aeaItemBasic.getItemName());
        vo.setItemAlias(aeaItemBasic.getItemAlias());
        vo.setItemBasicId(aeaItemBasic.getItemBasicId());
        vo.setItemCode(aeaItemBasic.getItemCode());
        vo.setOrgId(aeaItemBasic.getOrgId());
        vo.setOrgName(aeaItemBasic.getOrgName());
        vo.setRegionId(aeaItemBasic.getRegionId());
        vo.setRegionName(aeaItemBasic.getRegionName());
        vo.setIsCatalog(aeaItemBasic.getIsCatalog());
        vo.setItemNature(aeaItemBasic.getItemNature());
        //增加办理时限
        vo.setBjType(aeaItemBasic.getBjType());
        vo.setDueNum(aeaItemBasic.getDueNum());
        vo.setItemProperty(aeaItemBasic.getItemProperty());
        // 服务对象
        if (StringUtils.isNotBlank(aeaItemBasic.getXkdx())) {
            vo.setServiceObjectCode(aeaItemBasic.getXkdx());
        }
        vo.setServiceObject(serviceObject);

        return vo;
    }
}
