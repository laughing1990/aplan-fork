package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import com.augurit.aplanmis.common.domain.AeaHiParStateinst;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.common.mapper.AeaHiParStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateMapper;
import com.augurit.aplanmis.common.mapper.AeaParStateMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiParStateinstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xiaohutu
 */
@Service
public class AeaHiParStateinstServiceImpl implements AeaHiParStateinstService {
    private AeaHiParStateinstMapper aeaHiParStateinstMapper;
    private AeaParStateMapper aeaParStateMapper;
    @Autowired
    private AeaItemStateMapper aeaItemStateMapper;

    /**
     * 根据申请实例ID或阶段实例ID查询已选择的情形实例ID，只有ID数据
     *
     * @param applyinstId 申请实例ID
     * @param stageinstId 阶段实例ID
     * @return List<AeaHiParStateinst>
     * @throws Exception e
     */
    @Override
    public List<AeaHiParStateinst> listAeaHiParStateinstByApplyinstIdOrStageinstId(String applyinstId, String stageinstId) throws Exception {
        return aeaHiParStateinstMapper.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, stageinstId);
    }

    @Override
    public int batchInsertAeaHiParStateinst(String applyinstId, String stageinstId, String[] stateIds, String creater) throws Exception {
        int i = 0;
        if(stateIds==null||stateIds.length==0) return i;
        List<AeaParState> stateList = aeaParStateMapper.listAeaItemStateByIds(stateIds);
        for (AeaParState state : stateList) {
            AeaHiParStateinst stateinst = new AeaHiParStateinst();
            stateinst.setStageStateinstId(UUID.randomUUID().toString());
            stateinst.setApplyinstId(applyinstId);
            stateinst.setStageinstId(stageinstId);
            stateinst.setExecStateId(state.getParStateId());
            stateinst.setParentStateinstId(state.getParentStateId());
            stateinst.setStateinstSeq(String.valueOf(i));
            stateinst.setCreater(creater);
            stateinst.setCreateTime(new Date());
            stateinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiParStateinstMapper.insertAeaHiParStateinst(stateinst);
            i += 1;
        }
        return i;
    }

    /**
     * 根据事情实例或阶段实例ID查询已选择的情形列表-用来显示数据
     *
     * @param applyinstId 申请实例ID
     * @param stageinstId 阶段实例ID
     * @return List<Map < String, String>>
     * @throws Exception e
     */
    @Override
    public List<Map<String, String>> listSelectedParStateinstByStageinstIdOrApplyinstId(String applyinstId, String stageinstId) throws Exception {
        //先查询改申请实例下的情形实例列表
        List<Map<String, String>> list = new ArrayList<>();
        List<AeaHiParStateinst> aeaHiParStateinsts = aeaHiParStateinstMapper.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, stageinstId);

        String[] parentStateIds = aeaHiParStateinsts.stream().map(AeaHiParStateinst::getParentStateinstId).toArray(String[]::new);
        String[] stateIds = aeaHiParStateinsts.stream().map(AeaHiParStateinst::getExecStateId).toArray(String[]::new);
        System.out.println(parentStateIds.length);
        System.out.println(stateIds.length);
        if(parentStateIds.length==0||stateIds.length==0) return list;
        List<AeaParState> parentStateList = aeaParStateMapper.listAeaItemStateByIds(parentStateIds);
        List<AeaParState> stateList = aeaParStateMapper.listAeaItemStateByIds(stateIds);

        for (AeaParState parentState : parentStateList) {
            for (AeaParState state : stateList) {
                if (state.getParentStateId().equals(parentState.getParStateId())) {
                    Map<String, String> map = new HashMap<>(2);
                    map.put("question", parentState.getStateName());
                    map.put("answer", state.getStateName());
                    list.add(map);
                }
            }
        }
        return list;
    }

    @Override
    public List<AeaItemState> listAeaItemStateByApplyinstIdOrSeriesinstId(String applyinstId, String stageinstId) throws Exception{
        List<AeaItemState> list = new ArrayList<AeaItemState>();
        List<AeaHiParStateinst> parStateinstList =listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId,stageinstId);
        if(parStateinstList.size()==0) return list;
        String[] parentStateIds = parStateinstList.stream().map(AeaHiParStateinst::getParentStateinstId).toArray(String[]::new);
        if (parentStateIds.length==0) return list;
        list.addAll(aeaItemStateMapper.listAeaItemStateByIds(parentStateIds));
        String[] stateIds = parStateinstList.stream().map(AeaHiParStateinst::getExecStateId).toArray(String[]::new);
        if (stateIds.length==0) return list;
        list.addAll(aeaItemStateMapper.listAeaItemStateByIds(stateIds));
        return list;
    }
    @Autowired
    public void setAeaHiParStateinstMapper(AeaHiParStateinstMapper aeaHiParStateinstMapper) {
        this.aeaHiParStateinstMapper = aeaHiParStateinstMapper;
    }

    @Autowired
    public void setAeaParStateMapper(AeaParStateMapper aeaParStateMapper) {
        this.aeaParStateMapper = aeaParStateMapper;
    }
}
