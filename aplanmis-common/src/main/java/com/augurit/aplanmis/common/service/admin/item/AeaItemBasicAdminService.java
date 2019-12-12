package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.vo.ActTplAppTriggerAdminVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.flowable.bpmn.model.FlowElement;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 行政审批事项表-Service服务调用接口类
 */
public interface AeaItemBasicAdminService {

    void saveAeaItemBasic(AeaItemBasic aeaItemBasic);

    void updateAeaItemBasic(AeaItemBasic aeaItemBasic);

    void updateItemNeedCondType(AeaItemBasic aeaItemBasic);

    void deleteAeaItemBasicById(String id);

    PageInfo<AeaItemBasic> listAeaItemBasic(AeaItemBasic aeaItemBasic, Page page);

    PageInfo<AeaItemBasic> listLatestAeaItemBasic(AeaItemBasic aeaItemBasic, Page page);

    PageInfo<AeaItemBasic> latestAeaItemBasicTree(AeaItemBasic aeaItemBasic, Page page);

    EasyuiPageInfo<AeaItemBasic> listUsedAeaItemBasicTreeByPage(AeaItemBasic aeaItemBasic, Page page);

    List<AeaItemBasic> listUsedAeaItemBasicTree(AeaItemBasic aeaItemBasic);

    EasyuiPageInfo<AeaItemBasic> listAeaItemBasicForSupermaket(AeaItemBasic aeaItemBasic, Page page) throws Exception;

    List<AeaItemBasic> listAeaItemBasicForSupermaketNoPage(String itemId) throws Exception;

    ResultForm saveConfigItem(String itemId, String[] saveItemIds, String[] cancelItemIds) throws Exception;

    AeaItemBasic getAeaItemBasicById(String id);

    Long getItemAcceptMode(String parentItemId);

    List<AeaItemBasic> listAeaItemBasic(AeaItemBasic aeaItemBasic);

    List<AeaItemBasic> listAeaItemBasicByThemeVerId(String themeVerId);

    void batchDeleteAeaItemBasic(String[] ids);

    // 异步获取元数据
    List<ZtreeNode> gtreeTableColumnAsyncZTree(String id);

    // 同步获取元数据
    List<ZtreeNode> gtreeTableColumnSyncZTree();

    AeaItemBasic getAeaItemBasicByItemCode(String itemCode);

    AeaItemBasic viewItemOperaGuideByItemIdOrCode(String itemId, String itemCode);

    void handleMatAttachments(AeaItemMat aeaItemMat) throws Exception;

    List<ActTplAppTriggerAdminVo> getActTplAppTriggerByAppFlowDefIds(String appFlowDefId, String appId, String nodeId, String keyword);

    //保存触发条件
    void saveOrUpdateTrigger(ActTplAppTrigger actTplAppTrigger) throws Exception;

    Collection<FlowElement> getTaskNodeList(String defKey);

    Map<String, String> getTriggerAppFlowdefName(String flowDefId, String triggerFlowDefId, String appId) throws Exception;

    void batchDelSubTrigger(String[] triggerIds) throws Exception;

    ActTplAppTrigger getSubTriggerById(String id) throws Exception;

    void delSubTrigger(String id) throws Exception;

    PageInfo<AeaItemBasic> listAeaItemBasicByOrgIds(AeaItemBasic aeaItemBasic, Page page);

    List<AeaItemCond> gtreeItemCond(String itemId);

    AeaItemBasic getOneByItemVerId(String itemVerId, String rootOrgId);

    List<AeaItemBasic> findCarryOutItemsByItemId(String parentItemId, String rootOrgId);

    boolean checkUniqueItemCode(String itemBasicId, String itemCode, String rootOrgId);

    boolean checkUniqueItemCategoryMark(String itemId, String isCatalog, String itemCategoryMark, String rootOrgId);

    String getItemSeq(String itemId, String parentItemId);

    String createAppIdByItemName(String itemName);

    List<AeaItemBasic> getAeaItemBasicsListByOutputMatId(String matId) throws Exception;

    List<AeaItemBasic> getCompletedItemBasicByProjInfoId(String projInfoId);
}
