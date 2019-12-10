package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.vo.ActTplAppTriggerAdminVo;
import org.flowable.bpmn.model.FlowElement;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ZhangXinhui
 * @date 2019/8/2 002 17:02
 * @desc
 **/
public interface AeaItemAdminService {

    void syncActTplAppDefLimitTime(String appId, Long dueNum, String dueUnit);

    void handleMatAttachments(AeaItemMat aeaItemMat) throws Exception;

    List<AeaItem> gtreeTestRunOrPublishedItem(AeaItemBasic basic);

    List<AeaItem> gtreeLatestItem(AeaItemBasic basic);

    List<AeaItem> gtreeOkVerItemNoRelSelf(AeaItemBasic basic);

    void initItemVerSeq(AeaItemBasic basic);

    void initStandItem(AeaItemBasic basic);

    void syncItemRegion(AeaItemBasic basic);

    AeaItem getAeaItemById(String id);

    //同步开普事项接口
    ResultForm asynKpItems(String taskName, String catalogCode,
                           String taskCode, String taskType,
                           String deptCode, String qxfbqx,
                           String areacode, String taskState,
                           String isLocal, String sftzspsx, String page, String limit) throws Exception;

    ActTplAppTrigger getSubTriggerById(String id) throws Exception;

    List<ActTplAppTriggerAdminVo> getActTplAppTriggerByAppFlowDefIds(String appFlowDefId, String appId, String keyword);

    Map<String, String> getTriggerAppFlowdefName(String flowDefId, String triggerFlowDefId, String appId) throws Exception;

    void batchDelSubTrigger(String[] triggerIds) throws Exception;

    void saveOrUpdateTrigger(ActTplAppTrigger actTplAppTrigger) throws Exception;

    boolean checkHadTrigger(ActTplAppTrigger actTplAppTrigger) throws Exception;

    void delSubTrigger(String id) throws Exception;

    Collection<FlowElement> getTaskNodeList(String defKey);

    List<AeaItem> gtreeItem();

    List<AeaItemCond> gtreeItemCond(String itemId);

    // 节点id 节点名称  节点类型
    List<AeaItem> gtreeItemAsyncZTree(String id, String name, String type);

    // 异步获取元数据
    List<ZtreeNode> gtreeTableColumnAsyncZTree(String id);

    // 同步获取元数据
    List<ZtreeNode> gtreeTableColumnSyncZTree();

    /**
     * 递归查找所有的父级事项id, 不包括初始的自己
     * @param parentItemId
     * @param sets
     * @return
     */
    Set<String> getItemParentIdsByItemId(String parentItemId, Set<String> sets);

    List<AeaItem> getUnSelectedParFrontItemTree(String stageId,String frontItemId);

    List<AeaItem> getUnSelectedItemFrontItemTree(String itemVerId,String frontItemId);
}
