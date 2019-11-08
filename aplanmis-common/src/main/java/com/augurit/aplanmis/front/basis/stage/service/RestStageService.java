package com.augurit.aplanmis.front.basis.stage.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.domain.AeaOneform;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.mapper.AeaItemMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageItemMapper;
import com.augurit.aplanmis.common.service.admin.oneform.AeaOneformService;
import com.augurit.aplanmis.front.basis.stage.vo.StageItemFormVo;
import com.augurit.aplanmis.front.basis.stage.vo.StageOneFormVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Service
public class RestStageService {

    @Autowired
    private AeaOneformService aeaOneformService;

    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;

    @Autowired
    private AeaItemMapper aeaItemMapper;

    /**
     * 根据阶段id查询所有关联的表单
     *
     * @param stageId 阶段id
     * @return 表单list
     */
    public List<StageOneFormVo> findStageOneFormsByStageId(String stageId) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        log.info("Find all forms by stageId: {} and rootOrgId: {}", stageId, rootOrgId);
        List<AeaOneform> aeaParOneforms = aeaOneformService.findAllStageOneFormsByStageId(stageId, rootOrgId);
        log.info("Find {} froms", aeaParOneforms.size());
        return aeaParOneforms.stream().map(StageOneFormVo::from).collect(Collectors.toList());
    }

    /**
     * 根据阶段id查询阶段下事项与智能表单关联关系
     *
     * @param stageId
     * @return itemVerId -> subFormId
     */
    public List<StageItemFormVo> findStageItemFormsByStageId(String stageId) {

        List<AeaParStageItem> aeaParStageItems = aeaParStageItemMapper.listAeaStageItemByStageId(stageId, null, SecurityContext.getCurrentOrgId());
        return aeaParStageItems.stream().map(StageItemFormVo::from).collect(Collectors.toList());
    }


    /**
     * 根据前端传递的阶段id以及事项id获取表单参数
     * 引用到 findStageItemFormsByStageId 接口
     *
     * @param stageId
     * @param itemverIds
     * @return
     */
    public List<StageItemFormVo> findStageItemFormsByStageIdAndItemIds(String stageId, List<String> itemverIds) {

        List<StageItemFormVo> result = new ArrayList<>();
        //获取事项的seq
        List<String> itemSeqList = new ArrayList<>();
        if (itemverIds != null && itemverIds.size() > 0) {
            for (String str : itemverIds) {
                AeaItem item = aeaItemMapper.getAeaItemById(str);
                if (item != null)
                    itemSeqList.add(item.getItemSeq());
            }
        }

        //没有传事项的情况下，直接用阶段下的事项
        if (itemverIds.isEmpty()) {
            result = findStageItemFormsByStageId(stageId);
            return result;
        }

        List<StageItemFormVo> stageItemFormVos = findStageItemFormsByStageId(stageId);
        //传递进来的事项与阶段进行匹配
        // itemverIds的size会大于等于itemSeqList的size
        for (int indexItemVerId = 0; indexItemVerId < itemSeqList.size(); indexItemVerId++) {
            for (int indexStageItemForm = 0; indexStageItemForm < stageItemFormVos.size(); indexStageItemForm++) {
                if (stageItemFormVos.get(indexStageItemForm) != null
                        && stageItemFormVos.get(indexStageItemForm).getItemSeq() != null
                        && (stageItemFormVos.get(indexStageItemForm).getItemSeq().contains(itemverIds.get(indexItemVerId))
                        || itemSeqList.get(indexItemVerId).contains(stageItemFormVos.get(indexStageItemForm).getItemId()))) {
                    result.add(stageItemFormVos.get(indexStageItemForm));
                    break;
                }
            }
        }
        return result;
    }
}
