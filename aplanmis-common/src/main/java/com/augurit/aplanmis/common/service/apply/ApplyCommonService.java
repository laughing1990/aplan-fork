package com.augurit.aplanmis.common.service.apply;

import com.augurit.aplanmis.common.constants.ApplyType;

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
}
