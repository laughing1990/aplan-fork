package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaParIn;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 阶段输入定义表-Service服务调用接口类
 */
public interface AeaParInAdminService {

    void saveAeaParIn(AeaParIn aeaParIn);

    void updateAeaParIn(AeaParIn aeaParIn);

    void updateSortNo(String inId, Long sortNo);

    void deleteAeaParInById(String id);

    void batchDeleteAeaParInByIds(String[] ids);

    void batchDelMatCertFormByInIdsAndTypes(String[] ids, String[] fileTypes);

    PageInfo<AeaParIn> listAeaParIn(AeaParIn aeaParIn, Page page);

    AeaParIn getAeaParInById(String id);

    List<AeaParIn> listAeaParIn(AeaParIn aeaParIn);

    // 获取阶段情形材料
    List<AeaParIn> listInStateMatByStageId(String stageId, String keyword);

    // 获取阶段单个情形材料
    List<AeaParIn> listInStateMatByStageIdAndStateId(String stageId, String stateId, String keyword);

    // 获取阶段情形证照
    List<AeaParIn> listInStateCertByStageId(String stageId, String keyword);

    // 获取阶段单个情形证照
    List<AeaParIn> listInStateCertByStageIdAndStateId(String stageId, String stateId, String keyword);

    // 保存阶段非情形材料
    void batchSaveStageNoStateMatIn(String stageId, String[] matIds);

    // 保存阶段情形材料
    void batchSaveStageStateMatIn(String stageId, String stageStateId, String[] matIds);

    // 保存阶段非情形材料
    void batchSaveStageNoStateCertIn(String stageId, String[] certIds);

    // 保存阶段非情形材料不删除
    void batchSaveStageNoStateCertInNotDelOld(String stageId, String[] certIds);

    // 保存阶段情形证照
    void batchSaveStageStateCertIn(String stageId, String stageStateId, String[] certIds);

    // 单个保存阶段情形材料
    AeaParIn saveStageNoStateMatIn(HttpServletRequest request, String stageId, String inId, AeaItemMat mat) throws Exception;

    // 单个保存阶段情形材料
    AeaParIn saveStageStateMatIn(HttpServletRequest request, String stageId, String stageStateId, String inId, AeaItemMat mat) throws Exception;

    // 获取阶段情形材料证照
    PageInfo<AeaParIn> listInStateMatCertByStageIdAndStateId(String stageId, String parStateId, String keyword, Page page);

    // 获取阶段非情形材料证照
    PageInfo<AeaParIn> listNoStateInMatCertByStageId(String stageId, String keyword, Page page);

    //获取非情形材料证照与事项的关系
    PageInfo<AeaParIn> listNoStateInAndItemByStageId(String stageId, String keyword, Page page);

    //获取阶段下的所有材料和以勾选的阶段
    List<AeaParIn> listParInByStageItemId(String stageId, String stageItemId, String keyword);

    void addGlobalMatParIn(String[] matIds, String stageId, String isStateIn, String parStateId);

    void addGlobalCertParIn(String[] matIds, String stageId, String isStateIn, String parStateId);

    List<String> getCertListByStateId(String parStateId);

    List<String> listCertListByStageId(String stageId);

    List<String> getGlobalMatListByStateId(String parStateId);

    void deleteAeaParIn(String inId);

    PageInfo<AeaParIn> listStageMatByStageId(String stageId,
                                             String stateId,
                                             String keyword,
                                             Boolean isCommon,
                                             String isStateIn,
                                             Page page);

    void updateStageStateParIn(String stageId, String stateId, String inIds);

    void deleteMatAndParIn(String inId);

    void batchDeleteMatAndParIns(String[] inIds);

    List<String> getGlobalMatListByStageIdAndParStateId(String stageId, String parStateId);

    PageInfo<AeaParIn> listParInAndItemByStageId(String stageId, String keyword, Page page);

    /**
     * 查询阶段下的所有材料与证照以及关联的事项
     *
     * @param aeaParIn
     * @param page
     * @return
     */
    PageInfo<AeaParIn> listStageInMatCert(AeaParIn aeaParIn, Page page);

    /**
     * 查询阶段下的所有材料、证照、表单以及关联的事项
     *
     * @param aeaParIn
     * @param page
     * @return
     */
    PageInfo<AeaParIn> listStageInMatCertForm(AeaParIn aeaParIn, Page page);

    List<AeaParIn> listStageInMatCertForm(AeaParIn aeaParIn);

    List<AeaParIn> listStoMatByCondition(AeaParIn aeaParIn);

    Long getMaxSortNoByStageId(String stageId);
}
