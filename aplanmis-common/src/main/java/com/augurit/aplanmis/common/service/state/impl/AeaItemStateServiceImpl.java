package com.augurit.aplanmis.common.service.state.impl;

import com.augurit.aplanmis.common.constants.AeaParStateConstants;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaItemStateVer;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateVerMapper;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AeaItemStateServiceImpl implements AeaItemStateService {
    private static Logger logger = LoggerFactory.getLogger(AeaItemStateServiceImpl.class);
    private AeaItemStateMapper aeaItemStateMapper;
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaItemStateVerMapper aeaItemStateVerMapper;

    /**
     * 根据事项版本ID获取指定版本情形列表
     *
     * @param itemVerId      事项版本
     * @param itemStateVerId 情形版本==null 默认查询已发布最大版本情形
     * @return List<AeaItemState>
     */
    @Override
    public List<AeaItemState> listAeaItemStateByItemVerId(String itemVerId, String itemStateVerId) throws Exception {
        if (StringUtils.isEmpty(itemStateVerId)) {
            itemStateVerId = getMaxStateVerId(itemVerId);
        }
        return aeaItemStateMapper.listAeaItemStateByItemVerId(itemVerId, itemStateVerId);
    }

    /**
     * 根据事项版本ID获取指定情形版本列表树
     *
     * @param itemVerId  事项版本
     * @param stateVerId @param stateVerId 情形版本==null 默认查询已发布最大版本情形
     * @return List<AeaItemState>
     * @throws Exception E
     */
    @Override
    public List<AeaItemState> listTreeAeaItemStateByItemVerId(String itemVerId, String stateVerId) throws Exception {
        if (StringUtils.isEmpty(stateVerId)) {
            stateVerId = getMaxStateVerId(itemVerId);
        }
        List<AeaItemState> rootStates = aeaItemStateMapper.listRootAeaItemStateByItemVerId(itemVerId, stateVerId);
        List<AeaItemState> aeaItemStates = aeaItemStateMapper.listAeaItemStateByItemVerId(itemVerId, stateVerId);
        aeaItemStates = aeaItemStates.stream().filter(s -> !StringUtils.isEmpty(s.getParentStateId())).collect(Collectors.toList());
        for (AeaItemState rootState : rootStates) {
            if (AeaParStateConstants.QUESTION.equals(rootState.getIsQuestion())) {
                List<AeaItemState> temp = getTreeNode(rootState.getItemStateId(), aeaItemStates);
                rootState.setAnswerStates(temp);
            }
        }
        return rootStates;
    }

    private List<AeaItemState> getTreeNode(String id, List<AeaItemState> aeaItemStates) {
        List<AeaItemState> list = new ArrayList<>();
        for (AeaItemState state : aeaItemStates) {
            String parentStateId = state.getParentStateId();
            if (StringUtils.isEmpty(parentStateId)) {
                continue;
            }
            if (parentStateId.equalsIgnoreCase(id)) {
                list.add(state);
            }
        }
        for (AeaItemState state : list) {
            state.setAnswerStates(getTreeNode(state.getItemStateId(), aeaItemStates));
        }
        return list;
    }

    /**
     * 根据情形版本ID获取情形列表
     *
     * @param stateVerId 情形版本ID
     * @return List<AeaItemState>
     * @throws Exception E
     */
    @Override
    public List<AeaItemState> listAeaItemStateByStateVerId(String stateVerId) throws Exception {
        return aeaItemStateMapper.listAeaItemStateByStateVerId(stateVerId);
    }

    public List<AeaItemState> listAeaItemStateByParentId(String itemVerId, String stateVerId, String parentStateId, String rootOrgId) throws Exception {
        if (StringUtils.isEmpty(stateVerId) && !StringUtils.isEmpty(itemVerId)) {
            stateVerId = getMaxStateVerId(itemVerId);
        }
        List<AeaItemState> stateList = aeaItemStateMapper.listItemStateByParentItemStateId(itemVerId, stateVerId, parentStateId, rootOrgId);
        //添加父情形id
        stateList.stream().forEach(itemState ->{
            AeaItemState aeaItemStateById = aeaItemStateMapper.getAeaItemStateById(parentStateId);
            if (aeaItemStateById!=null){
                itemState.setParentQuestionStateId(aeaItemStateById.getParentStateId());
            }
        });
        return stateList;

    }

    private String getMaxStateVerId(String itemVerId) {
        if (!StringUtils.isEmpty(itemVerId)) {
            AeaItemStateVer aeaItemStateVer = aeaItemStateVerMapper.getMaxStateVerId(itemVerId);
            if (null != aeaItemStateVer) {
                return aeaItemStateVer.getItemStateVerId();
            }
        }
        return null;
    }

    /**
     * 根据情形ID获取所有的情形列表
     *
     * @param itemStateId 情形ID
     * @return List<AeaItemState>
     */
    @Override
    public List<AeaItemState> listAeaItemStateById(String itemStateId) throws Exception {
        AeaItemState aeaItemStateById = aeaItemStateMapper.getAeaItemStateById(itemStateId);
        if (null == aeaItemStateById) {
            logger.info("没有查到情形信息");
            return new ArrayList<>();
        }
        return aeaItemStateMapper.listAeaItemStateByItemVerId(aeaItemStateById.getItemVerId(), aeaItemStateById.getStateVerId());
    }

    private AeaItemState getRootStateItemVerId(AeaItemState aeaItemState) throws Exception {
        if (null != aeaItemState && !StringUtils.isEmpty(aeaItemState.getParentStateId())) {
            aeaItemState = getRootStateItemVerId(aeaItemStateMapper.getAeaItemStateById(aeaItemState.getParentStateId()));
        }
        return aeaItemState;
    }

    /**
     * 根据事项实例ID获取所有的情形列表
     *
     * @param iteminstId 事项实例ID
     * @return List<AeaItemState>
     */
    @Override
    public List<AeaItemState> listAeaItemStateByIteminstId(String iteminstId) throws Exception {
        AeaHiIteminst aeaHiIteminstById = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
        //保证是串联
        String isSeries = "1";
        if (null != aeaHiIteminstById && isSeries.equals(aeaHiIteminstById.getIsSeriesApprove())) {
            return listAeaItemStateByItemVerId(aeaHiIteminstById.getItemVerId(), null);
        }
        logger.debug("查询不到事项实例或非单项实例");
        return null;
    }

    @Override
    public int deleteAeaItemState(String id) throws Exception {
        int i = aeaItemStateMapper.deleteAeaItemState(id);
        logger.info("删除条数：" + i);
        return i;
    }

    @Autowired
    public void setAeaItemStateMapper(AeaItemStateMapper aeaItemStateMapper) {
        this.aeaItemStateMapper = aeaItemStateMapper;
    }

    @Autowired
    public void setAeaHiIteminstMapper(AeaHiIteminstMapper aeaHiIteminstMapper) {
        this.aeaHiIteminstMapper = aeaHiIteminstMapper;
    }
}
