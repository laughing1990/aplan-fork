package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.mapper.AeaHiItemStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiItemStateinstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author xiaohutu
 */
@Service
public class AeaHiItemStateinstServiceImpl implements AeaHiItemStateinstService {

    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;
    private AeaItemStateMapper aeaItemStateMapper;

    /**
     * 批量插入选择的情形实例
     *
     * @param applyinstId  申请实例ID
     * @param seriesinstId
     * @param itemStateIds 选择的情形ID
     * @return 插入的条数
     */
    @Override
    public int batchInsertAeaHiItemStateinst(String applyinstId, String seriesinstId, String stageinstId, String[] itemStateIds, String creater) throws Exception {
        int i = 0;
        if(itemStateIds==null||itemStateIds.length==0) return 0;
        List<AeaItemState> stateList = aeaItemStateMapper.listAeaItemStateByIds(itemStateIds);
        for (AeaItemState state : stateList) {
            AeaHiItemStateinst stateinst = new AeaHiItemStateinst();
            stateinst.setItemStateinstId(UUID.randomUUID().toString());
            stateinst.setApplyinstId(applyinstId);
            stateinst.setSeriesinstId(seriesinstId);
            stateinst.setStageinstId(stageinstId);
            stateinst.setExecStateId(state.getItemStateId());
            stateinst.setParentStateinstId(state.getParentStateId());
            stateinst.setStateinstSeq(String.valueOf(i));
            stateinst.setCreater(creater);
            stateinst.setCreateTime(new Date());
            stateinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiItemStateinstMapper.insertAeaHiItemStateinst(stateinst);
            i += 1;
        }
        return i;
    }

    /**
     * 根据事情实例或阶段实例ID查询已选择的情形列表
     *
     * @param applyinstId  申请实例ID
     * @param seriesinstId 单项实例ID
     * @return List<Map < String, String>> 现实的数据
     * @throws Exception e
     */
    @Override
    public List<Map<String, String>> listSelectedAeaItemStateinstBySeriesinstIdOrApplyinstId(String applyinstId, String seriesinstId) throws Exception {
        List<AeaHiItemStateinst> stateinstList = aeaHiItemStateinstMapper.listAeaHiItemStateinstByApplyinstIdOrSeriesinstId(applyinstId, seriesinstId);
        String[] parentStateIds = stateinstList.stream().map(AeaHiItemStateinst::getParentStateinstId).toArray(String[]::new);
        String[] stateIds = stateinstList.stream().map(AeaHiItemStateinst::getExecStateId).toArray(String[]::new);
        List<Map<String, String>> list = new ArrayList<>();
        if (parentStateIds.length==0||stateIds.length==0){
            return list;
        }
        List<AeaItemState> parentStateList = aeaItemStateMapper.listAeaItemStateByIds(parentStateIds);
        List<AeaItemState> stateList = aeaItemStateMapper.listAeaItemStateByIds(stateIds);
        for (AeaItemState parentState : parentStateList) {
            for (AeaItemState state : stateList) {
                if (StringUtils.isNotBlank(state.getParentStateId()) && state.getParentStateId().equals(parentState.getItemStateId())) {
                    Map<String, String> map = new HashMap<>(4);
                    map.put("question", parentState.getStateName());
                    map.put("answer", state.getStateName());
                    map.put("questionId",parentState.getItemStateId());
                    map.put("answerId",state.getItemStateId());
                    list.add(map);
                }
            }
        }
        return list;
    }

    @Override
    public List<AeaItemState> listAeaItemStateByApplyinstIdOrSeriesinstId(String applyinstId, String seriesinstId) throws Exception{
        List<AeaItemState> list = new ArrayList<AeaItemState>();
        List<AeaHiItemStateinst> stateinstList = aeaHiItemStateinstMapper.listAeaHiItemStateinstByApplyinstIdOrSeriesinstId(applyinstId, seriesinstId);
        if(stateinstList.size()==0) return list;
        String[] parentStateIds = stateinstList.stream().map(AeaHiItemStateinst::getParentStateinstId).toArray(String[]::new);
        if (parentStateIds.length==0) return list;
        list.addAll(aeaItemStateMapper.listAeaItemStateByIds(parentStateIds));
        String[] stateIds = stateinstList.stream().map(AeaHiItemStateinst::getExecStateId).toArray(String[]::new);
        if (stateIds.length==0) return list;
        list.addAll(aeaItemStateMapper.listAeaItemStateByIds(stateIds));
        return list;
    }
    @Override
    public void batchDeleteAeaItemState(String[] itemStateIds){
        if(itemStateIds.length>0)
            aeaItemStateMapper.batchDeleteAeaItemState(itemStateIds);
    }

    @Autowired
    public void setAeaHiItemStateinstMapper(AeaHiItemStateinstMapper aeaHiItemStateinstMapper) {
        this.aeaHiItemStateinstMapper = aeaHiItemStateinstMapper;
    }

    @Autowired
    public void setAeaItemStateMapper(AeaItemStateMapper aeaItemStateMapper) {
        this.aeaItemStateMapper = aeaItemStateMapper;
    }

}
