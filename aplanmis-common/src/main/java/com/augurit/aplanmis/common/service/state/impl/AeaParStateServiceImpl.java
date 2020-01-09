package com.augurit.aplanmis.common.service.state.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.common.mapper.AeaHiParStageinstMapper;
import com.augurit.aplanmis.common.mapper.AeaParStateMapper;
import com.augurit.aplanmis.common.service.state.AeaParStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaohutu
 */
@Service
public class AeaParStateServiceImpl implements AeaParStateService {

    private AeaParStateMapper aeaParStateMapper;
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;


    /**
     * 根据阶段ID获取阶段下所有的情形列表
     *
     * @param stageId 阶段ID
     * @return List<AeaParState>
     */
    @Override
    public List<AeaParState> listAeaParStateByStageId(String stageId) throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaParState> aeaParStates = aeaParStateMapper.listAeaParStateByStageId(stageId, rootOrgId);
        return aeaParStates;
    }

    /**
     * 根据阶段ID获取阶段下所有的情形列表树
     *
     * @param stageId 阶段ID
     * @return List<AeaParState>
     */
    @Override
    public List<AeaParState> listTreeAeaParStateByStageId(String stageId) throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaParState> aeaParRootStates = aeaParStateMapper.listRootAeaParStateByStageId(stageId, rootOrgId);
        List<AeaParState> aeaParStates = aeaParStateMapper.listChildAeaParStateByStageId(stageId, rootOrgId);
        for (AeaParState rootState : aeaParRootStates) {
            String id = rootState.getParStateId();
            List<AeaParState> aeaParState = getAeaParState(id, aeaParStates);
            rootState.setAnswerStates(aeaParState);
        }
        return aeaParRootStates;
    }


    /**
     * 根据父情形ID查找子情形及答案
     * parentStateId==ROOT是只查询root情形及答案
     *
     * @param parentStateId 父情形ID
     * @return List<AeaParState>
     */
    @Override
    public List<AeaParState> listAeaParStateByParentStateId(String stageId, String parentStateId, String rootOrgId) throws Exception {
        if (StringUtils.isEmpty(rootOrgId)) {
            return new ArrayList<>();
        }
        List<AeaParState> aeaParStates = aeaParStateMapper.listParStateByParentStateId(stageId, parentStateId, rootOrgId);
        //添加父情形id
        aeaParStates.stream().forEach(parState ->{
            AeaParState aeaParStateById = aeaParStateMapper.getAeaParStateById(parentStateId);
            if (aeaParStateById!=null){
                parState.setParentQuestionStateId(aeaParStateById.getParentStateId());
            }
        });

        return aeaParStates;
    }

    /**
     * 根据阶段实例ID或申请实例ID查询情形定义列表
     *
     * @param applyinstId
     * @param stageinstId
     * @return List<AeaParState>
     */
    @Override
    public List<AeaParState> listAeaParStateByStageinstIdORApplyinstId(String applyinstId, String stageinstId) throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        if (!StringUtils.isBlank(stageinstId)) {
            AeaHiParStageinst stageinstById = aeaHiParStageinstMapper.getAeaHiParStageinstById(stageinstId);
            if (null != stageinstById) {
                return aeaParStateMapper.listAeaParStateByStageId(stageinstById.getStageId(), rootOrgId);
            }
        }
        if (!StringUtils.isBlank(applyinstId)) {
            AeaHiParStageinst stageinst = aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
            if (null != stageinst) {
                return aeaParStateMapper.listAeaParStateByStageId(stageinst.getStageId(), rootOrgId);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<AeaParState> getProcStartCondStageStates(String stageId) throws Exception {
        if(StringUtils.isBlank(stageId))
            throw new InvalidParameterException("参数阶段ID不能为空！");
        AeaParState aeaParState = new AeaParState();
        aeaParState.setStageId(stageId);
        aeaParState.setIsProcStartCond("1");
        return aeaParStateMapper.listAeaParState(aeaParState);
    }

    /**
     * 递归查询情形树结构
     *
     * @param id
     * @param childStates
     * @return
     */
    private List<AeaParState> getAeaParState(String id, List<AeaParState> childStates) {

        List<AeaParState> list = new ArrayList<>();
        for (AeaParState state : childStates) {
            if (state.getParentStateId().equalsIgnoreCase(id)) {
                list.add(state);
            }
        }
        for (AeaParState state : list) {
            state.setAnswerStates(getAeaParState(state.getParStateId(), childStates));
        }
        return list;
    }

    @Autowired
    public void setAeaParStateMapper(AeaParStateMapper aeaParStateMapper) {
        this.aeaParStateMapper = aeaParStateMapper;
    }

    @Autowired
    public void setAeaHiParStageinstMapper(AeaHiParStageinstMapper aeaHiParStageinstMapper) {
        this.aeaHiParStageinstMapper = aeaHiParStageinstMapper;
    }
}
