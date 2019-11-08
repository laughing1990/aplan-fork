package com.augurit.aplanmis.common.service.state;

import com.augurit.aplanmis.common.domain.AeaParState;

import java.util.List;

/**
 * @author xiaohutu
 */
public interface AeaParStateService {

    /**
     * 根据阶段ID获取阶段下所有的情形列表
     *
     * @param stageId 阶段ID
     * @return List<AeaParState>
     */
    List<AeaParState> listAeaParStateByStageId(String stageId) throws Exception;

    /**
     * 根据阶段ID获取阶段下所有的情形列表
     *
     * @param stageId 阶段ID
     * @return List<AeaParState>
     */
    List<AeaParState> listTreeAeaParStateByStageId(String stageId) throws Exception;

    /**
     * 根据父情形ID查找子情形及子情形答案
     *
     * @param parentStateId 父情形ID ==ROOT 时查询的root情形
     * @param stageId       阶段ID
     * @param rootOrgId     顶级机构ID
     * @return List<AeaParState>
     */
    List<AeaParState> listAeaParStateByParentStateId(String stageId, String parentStateId, String rootOrgId) throws Exception;

    /**
     * 根据阶段实例ID或申请实例ID查询情形定义列表
     *
     * @param stageinstId
     * @param applyinstId
     * @return List<AeaParState>
     */
    List<AeaParState> listAeaParStateByStageinstIdORApplyinstId(String applyinstId, String stageinstId) throws Exception;

    //获取当前阶段的流程情形
    List<AeaParState> getProcStartCondStageStates(String stageId) throws Exception;
}
