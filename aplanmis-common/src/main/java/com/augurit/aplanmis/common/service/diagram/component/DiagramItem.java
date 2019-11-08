package com.augurit.aplanmis.common.service.diagram.component;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.service.diagram.constant.HandleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel("事项")
public abstract class DiagramItem implements Serializable {

    @ApiModelProperty(value = "事项实体", notes = "表中的具体事项")
    protected AeaItemBasic currentItemBasic;

    @ApiModelProperty(value = "事项实例")
    protected AeaHiIteminst aeaHiIteminst;

    @ApiModelProperty(value = "标准事项id", dataType = "string")
    protected String catalogItemId;

    @ApiModelProperty(value = "标准事项版本id", dataType = "string")
    protected String catalogItemVerId;

    @ApiModelProperty(value = "事项版本id", dataType = "string", notes = "aea_item_ver中的主键")
    protected String itemVerId;

    @ApiModelProperty(value = "事项id", dataType = "string", notes = "aea_item中的主键")
    protected String itemId;

    @ApiModelProperty(value = "基本事项id", dataType = "string", notes = "aea_item_basic中的主键")
    protected String itemBasicId;

    @ApiModelProperty(value = "事项名称", dataType = "string")
    protected String itemName;

    @ApiModelProperty(value = "事项编码", dataType = "string")
    protected String itemCode;

    @ApiModelProperty(value = "行政区划id", dataType = "string", notes = "bsc_dic_region中的主键")
    protected String regionId;

    @ApiModelProperty(value = "行政组织id", dataType = "string", notes = "opu_om_org中的主键")
    protected String orgId;

    @ApiModelProperty(value = "办理状态", notes = "详情查看 HandleStatus 枚举")
    protected HandleStatus finished;

    @ApiModelProperty(value = "是否标准事项", notes = "true: 标准事项, false: 实施事项")
    protected boolean catalog;

    @ApiModelProperty(value = "是否里程碑事项", notes = "true: 是, false: 否")
    protected boolean milestone;

    public DiagramItem(AeaItemBasic currentItemBasic, AeaHiIteminst aeaHiIteminst) {
        this.aeaHiIteminst = aeaHiIteminst;
        this.currentItemBasic = currentItemBasic;
        this.itemVerId = currentItemBasic.getItemVerId();
        this.itemId = currentItemBasic.getItemId();
        this.itemBasicId = currentItemBasic.getItemBasicId();
        this.itemName = currentItemBasic.getItemName();
        this.itemCode = currentItemBasic.getItemCode();
        this.regionId = currentItemBasic.getRegionId();
        this.orgId = currentItemBasic.getOrgId();
        this.catalog = Status.ON.equals(currentItemBasic.getIsCatalog());
        this.milestone = Status.ON.equals(currentItemBasic.getIsMilestoneItem());
    }

    /**
     * 获取办理状态
     *
     * @return 办理状态
     */
    public HandleStatus acquireHandleStatus() {
        finished = HandleStatus.UNKNOWN;
        if (aeaHiIteminst == null) {
            finished = HandleStatus.UN_FINISHED;
        } else {
            String iteminstState = aeaHiIteminst.getIteminstState();
            if (ItemStatus.AGREE.getValue().equals(iteminstState)) {
                finished = HandleStatus.FINISHED;
            } else if (ItemStatus.DISAGREE.getValue().equals(iteminstState)) {
                finished = HandleStatus.NOT_PASS;
            } else if (ItemStatus.AGREE_TOLERANCE.getValue().equals(iteminstState)) {
                finished = HandleStatus.TOLERENCE_PASS;
            } else if (ItemStatus.CORRECT_MATERIAL_START.getValue().equals(iteminstState)) {
                finished = HandleStatus.CORRECT_MATERIAL_START;
            } else if (ItemStatus.CORRECT_MATERIAL_END.getValue().equals(iteminstState)) {
                finished = HandleStatus.CORRECT_MATERIAL_END;
            } else if (ItemStatus.SPECIFIC_PROC_START.getValue().equals(iteminstState)) {
                finished = HandleStatus.SPECIFIC_PROC_START;
            } else if (ItemStatus.SPECIFIC_PROC_END.getValue().equals(iteminstState)) {
                finished = HandleStatus.SPECIFIC_PROC_END;
            } else if (ItemStatus.ACCEPT_DEAL.getValue().equals(iteminstState)
                    || ItemStatus.DEPARTMENT_DEAL_START.getValue().equals(iteminstState)
            ) {
                finished = HandleStatus.NORMAL_ACCEPT;
            } else if (ItemStatus.RECEIVE_APPLY.getValue().equals(iteminstState)) {
                finished = HandleStatus.HANDLING;
            } else if (ItemStatus.BACK_APPLY.getValue().equals(iteminstState)) {
                finished = HandleStatus.BACK_APPLY;
            } else if (ItemStatus.REFUSE_DEAL.getValue().equals(iteminstState)) {
                finished = HandleStatus.REFUSE_DEAL;
            } else if (ItemStatus.OUT_SCOPE.getValue().equals(iteminstState)) {
                finished = HandleStatus.OUT_SCOPE;
            } else if (ItemStatus.RECALL.getValue().equals(iteminstState)) {
                finished = HandleStatus.RECALL;
            } else if (ItemStatus.REVOKE.getValue().equals(iteminstState)) {
                finished = HandleStatus.REVOKE;
            } else {
                finished = HandleStatus.NEED_HANDLE_BUT_UNDO;
            }
        }
        return finished;
    }

    /**
     *
     */
    public void fillCatalogInfo(AeaItemBasic currentCatalogItemBasic) {
        if (currentCatalogItemBasic != null && "1".equals(currentCatalogItemBasic.getIsCatalog())) {
            this.catalogItemId = currentCatalogItemBasic.getItemId();
            this.catalogItemVerId = currentCatalogItemBasic.getItemVerId();
        }
    }

}
