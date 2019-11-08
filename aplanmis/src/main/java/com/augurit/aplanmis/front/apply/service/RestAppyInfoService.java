package com.augurit.aplanmis.front.apply.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.handler.ItemPrivilegeComputationHandler;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class RestAppyInfoService {

    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;

    /**
     * 查询阶段必选事项
     *  @param stageId    阶段id
     * @param projInfoId 项目id
     * @return 换算后的实施事项
     */
    public List<ItemPrivilegeComputationHandler.ComputedItem> getRequiredItems(String stageId, String projInfoId) throws Exception {
        List<AeaItemBasic> coreItems = aeaItemBasicService.getAeaItemBasicListByStageId(stageId, "0", projInfoId, SecurityContext.getCurrentOrgId());
        if (coreItems.size() == 0) return new ArrayList<>();
        if (log.isDebugEnabled()) {
            log.debug("Core items size: {}", coreItems.size());
        }

        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
        AeaServiceWindow currentUserWindow = aeaServiceWindowService.getCurrentUserWindow();
        return new ItemPrivilegeComputationHandler(currentUserWindow, projInfoId, SecurityContext.getCurrentOrgId(), aeaParStage, coreItems, true)
                .compute();
    }

    /**
     * 查询阶段可选事项
     *
     * @param stageId    阶段id
     * @param projInfoId 项目id
     */
    public List<ItemPrivilegeComputationHandler.ComputedItem> getOptionalItems(String stageId, String projInfoId) throws Exception {
        List<AeaItemBasic> optionItems = aeaItemBasicService.getAeaItemBasicListByStageId(stageId, "1", projInfoId, SecurityContext.getCurrentOrgId());
        if (optionItems.size() == 0) return new ArrayList<>();
        if (log.isDebugEnabled()) {
            log.debug("Parallel items size: {}", optionItems.size());
        }

        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
        AeaServiceWindow currentUserWindow = aeaServiceWindowService.getCurrentUserWindow();
        List<ItemPrivilegeComputationHandler.ComputedItem> computedItems = new ItemPrivilegeComputationHandler(currentUserWindow, projInfoId, SecurityContext.getCurrentOrgId(), aeaParStage, optionItems, true)
                .compute();
        computedItems.forEach(item -> {
            if (item.getCurrentCarryOutItem() != null) {
                ItemPrivilegeComputationHandler.CarryOutItem currentCarryOutItem = item.getCurrentCarryOutItem();
                currentCarryOutItem.setPreItemCheckPassed(aeaItemBasicService.checkPreItemsPassed(currentCarryOutItem.getItemVerId(), projInfoId).isPassed());
            }
        });
        return computedItems;
    }
}
