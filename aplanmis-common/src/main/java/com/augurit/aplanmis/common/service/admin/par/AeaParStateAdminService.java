package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.bsc.domain.MindBaseNode;
import com.augurit.aplanmis.bsc.domain.MindExportObj;
import com.augurit.aplanmis.common.domain.AeaMindUi;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.common.vo.AeaParStateModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 主题情形定义表-Service服务调用接口类
 */
public interface AeaParStateAdminService {

    void saveAeaParState(AeaParState aeaParState) throws Exception;

    void updateAeaParState(AeaParState aeaParState) throws Exception;

    void deleteAeaParStateById(String id) throws Exception;

    PageInfo<AeaParState> listAeaParState(AeaParState aeaParState, Page page) throws Exception;

    AeaParState getAeaParStateById(String id) throws Exception;

    List<AeaParState> listAeaParState(AeaParState aeaParState) throws Exception;

    Long getMaxSortNo(String rootOrgId) throws Exception;

    List<AeaParState> listAeaParStateByStageId(String stageId, String rootOrgId) throws Exception;

    MindBaseNode listAeaStageStateMindView(String stageId, AeaMindUi aeaMindUi, String rootOrgId) throws Exception;

    void saveOrUpdateAeaStageStateMindView(MindExportObj mindExportObj, String rootOrgId) throws Exception;

    void batchDeleteAeaParStateByIds(String[] ids);

    PageInfo<AeaParState> listAllChildStates(String parStateId, String keyword, String rootOrgId, Page page);

    List<AeaParState> listAllChildStates(String parStateId, String keyword, String rootOrgId);

    List<AeaParStateModel> listAeaParStateByStageIdUsedForStateItem(String stageId);

}
