package com.augurit.aplanmis.common.service.apply;

import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;

import java.util.Map;

public interface ApplyCommonService {

    /**
     * 根据所选的情形，过滤流程情形
     *
     * @param stateIds  情形id
     * @param applyType 申报类型
     * @return 放入申报实例中的map
     */
    Map<String, Boolean> filterProcessStartConditions(String[] stateIds, ApplyType applyType);

    /**
     * 设置事项实例是否为告知承诺制办理
     *
     * @param stateIds
     * @param applyType
     * @param iteminst
     */
    void setInformCommit(String[] stateIds, ApplyType applyType, AeaHiIteminst iteminst) throws Exception;

    void clearHistoryInst(String applyinstId) throws Exception;

    void bindApplyinstProj(String projInfoId, String applyinstId, String currentUserId) throws Exception;
}
