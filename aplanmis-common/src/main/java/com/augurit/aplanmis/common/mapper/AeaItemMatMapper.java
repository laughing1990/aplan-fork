package com.augurit.aplanmis.common.mapper;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项材料定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemMatMapper {

    void insertAeaItemMat(AeaItemMat aeaItemMat);

    void updateAeaItemMat(AeaItemMat aeaItemMat);

    void deleteAeaItemMatById(@Param("id") String id);

    AeaItemMat selectOneById(@Param("id") String id);

    AeaItemMat getAeaItemMatById(@Param("id") String id);

    List<AeaItemMat> listAeaItemMat(AeaItemMat aeaItemMat);

    List<AeaItemMat> getMatListByItemStateIds(@Param("ids") String[] ItemStateId);

    List<AeaItemMat> getMatListByStageStateIds(@Param("ids") String[] StageStateId);

    List<AeaItemMat> getMatListByStageId(@Param("stageId") String stageId,
                                         @Param("isNeedStateMat") String isNeedStateMat);

    List<AeaItemMat> listMatListByStageId(@Param("stageId") String stageId, @Param("itemVerIds") List<String> itemVerIds);

    List<AeaItemMat> getMatListByItemVerIds(@Param("itemVerIds") String[] itemVerIds,
                                            @Param("isInput") String isInput,
                                            @Param("isNeedStateMat") String isNeedStateMat);


    List<AeaItemMat> getMatListByStageStateIdsAndItemVerId(@Param("ids") String[] StageStateId, @Param("itemVerId") String itemVerId);

    List<AeaItemMat> getMatListByStageStateIdsAndMatinstId(@Param("ids") String[] StageStateId, @Param("matinstId") String matinstId);

    List<AeaItemMat> getMatListByItemVerIdAndStageId(@Param("itemVerId") String itemVerId, @Param("stageId") String stageId, @Param("isNeedStateMat") String isNeedStateMat, @Param("rootOrgId") String rootOrgId);

    List<AeaItemMat> getMatListByIteminstId(@Param("iteminstId") String iteminstId, @Param("isInput") String isInput, @Param("isNeedStateMat") String isNeedStateMat);

    /**
     * 批量查询--MAT_ID
     *
     * @param matIds 材料IDS
     * @return List<AeaItemMat>
     */
    List<AeaItemMat> listAeaItemMatByIds(@Param("matIds") String[] matIds);

    Integer checkMatCode(@Param("matId") String matId, @Param("matCode") String matCode, @Param("rootOrgId") String rootOrgId);

    Long getMaxSortNo(@Param("rootOrgId") String rootOrgId);

    void batchDeleteAeaItemMatByIds(@Param("ids") String[] ids);

    List<AeaItemMat> listAeaItemMatByItemId(@Param("itemId") String itemId);

    void deleteAeaItemMatByStateId(@Param("itemStateId") String itemStateId,
                                   @Param("itemVerId") String itemVerId,
                                   @Param("stateVerId") String stateVerId,
                                   @Param("rootOrgId") String rootOrgId);

    void batchDeleteAeaItemMatByInoutIds(@Param("inoutIds") List<String> inoutIds);

    List<AeaItemMat> listAeaItemMatByParInNoState(@Param("stageId") String stageId);

    List<AeaItemMat> listAeaItemMatByParIn(@Param("stageId") String stageId, @Param("stateId") String stateId);

    /**
     * 阶段未选择的材料（这里就不区分分不分情形）
     *
     * @param stageId
     * @param keyword
     * @return
     */
    List<AeaItemMat> listStageNoSelectGlobalMat(@Param("stageId") String stageId,
                                                @Param("keyword") String keyword,
                                                @Param("rootOrgId") String rootOrgId);

    List<AeaItemMat> listItemInOutNoSelectGlobalMat(@Param("rootOrgId") String rootOrgId,
                                                    @Param("itemVerId") String itemVerId,
                                                    @Param("stateVerId") String stateVerId,
                                                    @Param("isInput") String isInput,
                                                    @Param("keyword") String keyword);

    List<AeaItemMat> getMatListByItemVerIdsAndStageId(@Param("ids") String[] itemVerIds, @Param("stageId") String stageId, @Param("isNeedStateMat") String isNeedStateMat);

    List<AeaItemMat> getOfficeMatsByStageItemVerIds(@Param("stageId") String stageId, @Param("itemVerIds") String[] itemVerIds, @Param("rootOrgId") String rootOrgId);

    List<AeaItemMat> getParInMatsByStageItemVerIds(@Param("stageId") String stageId, @Param("itemVerIds") String[] itemVerIds, @Param("rootOrgId") String rootOrgId);

    List<ActStoForm> listActStoForm(ActStoForm stoForm);

    ActStoForm getActStoFormById(@Param("id") String id);
}
