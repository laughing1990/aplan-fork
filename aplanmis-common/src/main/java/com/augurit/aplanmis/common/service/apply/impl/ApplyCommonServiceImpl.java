package com.augurit.aplanmis.common.service.apply.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateMapper;
import com.augurit.aplanmis.common.mapper.AeaParStateMapper;
import com.augurit.aplanmis.common.service.apply.ApplyCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class ApplyCommonServiceImpl implements ApplyCommonService {

    @Autowired
    private AeaItemStateMapper aeaItemStateMapper;

    @Autowired
    private AeaParStateMapper aeaParStateMapper;

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Override
    public Map<String, Boolean> filterProcessStartConditions(String[] stateIds, ApplyType applyType) {
        if (stateIds == null || stateIds.length < 1) {
            return null;
        }
        Map<String, Boolean> stateinsts = new HashMap<>(stateIds.length);
        if (applyType == ApplyType.UNIT) {
            List<AeaParState> stateList = aeaParStateMapper.listAeaItemStateByIds(stateIds);
            stateList.forEach(s -> {
                if (Status.ON.equals(s.getIsProcStartCond())) {
                    stateinsts.put(s.getParStateId(), true);
                }
            });
        } else if (applyType == ApplyType.SERIES) {
            List<AeaItemState> stateList = aeaItemStateMapper.listAeaItemStateByIds(stateIds);
            stateList.forEach(s -> {
                if (Status.ON.equals(s.getIsProcStartCond())) {
                    stateinsts.put(s.getItemStateId(), true);
                }
            });
        } else {
            return null;
        }
        return stateinsts;
    }

    @Override
    public void setInformCommit(String[] stateIds, ApplyType applyType, AeaHiIteminst iteminst) throws Exception {
        if (stateIds == null || stateIds.length < 1) {
            return;
        }

        if (applyType == ApplyType.UNIT) {
            List<AeaParState> stateList = aeaParStateMapper.listAeaItemStateByIds(stateIds);
            for (AeaParState state : stateList) {
                //判断事项是否为告知承诺制
                if ("1".equals(state.getIsInformCommit())) {
                    iteminst.setIteminstType("p");
                    aeaHiIteminstMapper.updateAeaHiIteminst(iteminst);
                    break;
                }
            }

        } else if (applyType == ApplyType.SERIES) {
            List<AeaItemState> stateList = aeaItemStateMapper.listAeaItemStateByIds(stateIds);
            for (AeaItemState state : stateList) {
                //判断事项是否为告知承诺制
                if ("1".equals(state.getIsInformCommit())) {
                    iteminst.setIteminstType("p");
                    aeaHiIteminstMapper.updateAeaHiIteminst(iteminst);
                    break;
                }
            }
        }
    }
}
