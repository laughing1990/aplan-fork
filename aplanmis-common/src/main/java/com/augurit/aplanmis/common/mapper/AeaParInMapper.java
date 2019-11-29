package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段输入定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParInMapper {

    void insertAeaParIn(AeaParIn aeaParIn);

    void updateAeaParIn(AeaParIn aeaParIn);

    void deleteAeaParIn(@Param("id") String id);

    List<AeaParIn> listAeaParIn(AeaParIn aeaParIn);

    AeaParIn getAeaParInById(@Param("id") String id);

    Long getMaxSortNoByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    void updateSortNo(@Param("inId") String inId, @Param("sortNo") Long sortNo);

    /**
     * 获取阶段情形材料集合
     *
     * @param stageId
     * @param keyword
     * @param rootOrgId
     * @return
     */
    List<AeaParIn> listInStateMatByStageId(@Param("stageId") String stageId,
                                           @Param("keyword") String keyword,
                                           @Param("rootOrgId") String rootOrgId);

    List<AeaParIn> listInStateMatNewByStageId(@Param("stageId") String stageId,
                                              @Param("keyword") String keyword,
                                              @Param("rootOrgId") String rootOrgId,
                                              @Param("matProp")String matProp,
                                              @Param("matProps")List<String> matProps);
    /**
     * 获取阶段情形电子证照集合
     *
     * @param stageId
     * @param keyword
     * @return
     */
    List<AeaParIn> listInStateCertByStageId(@Param("stageId") String stageId,
                                            @Param("keyword") String keyword,
                                            @Param("rootOrgId") String rootOrgId);

    List<AeaParIn> listStageMat(@Param("stageId") String stageId,
                                @Param("parStateId") String parStateId,
                                @Param("isCommon") String isCommon,
                                @Param("isStateIn") String isStateIn,
                                @Param("keyword") String keyword,
                                @Param("rootOrgId") String rootOrgId);

    List<AeaParIn> listStageMatNew( @Param("stageId") String stageId,
                                    @Param("parStateId") String parStateId,
                                    @Param("isCommon") String isCommon,
                                    @Param("isStateIn") String isStateIn,
                                    @Param("keyword") String keyword,
                                    @Param("rootOrgId") String rootOrgId,
                                    @Param("matProp")String matProp,
                                    @Param("matProps")List<String> matProps);

    /**
     * 获取阶段材料证照
     *
     * @param aeaParIn
     * @return
     */
    List<AeaParIn> listStageInMatCert(AeaParIn aeaParIn);

    /**
     * 获取阶段非情形电子证照集合
     *
     * @param stageId
     * @param keyword
     * @param rootOrgId
     * @return
     */
    List<AeaParIn> listNoStateInCertByStageId(@Param("stageId") String stageId,
                                              @Param("keyword") String keyword,
                                              @Param("rootOrgId") String rootOrgId);

    /**
     * 获取阶段某情形材料集合
     *
     * @param stageId
     * @param stateId
     * @param keyword
     * @return
     */
    List<AeaParIn> listInStateMatByStageIdAndStateId(@Param("stageId") String stageId,
                                                     @Param("stateId") String stateId,
                                                     @Param("keyword") String keyword,
                                                     @Param("rootOrgId") String rootOrgId);

    List<AeaParIn> listInStateMatNewByStageIdAndStateId(@Param("stageId") String stageId,
                                                        @Param("stateId") String stateId,
                                                        @Param("keyword") String keyword,
                                                        @Param("rootOrgId") String rootOrgId,
                                                        @Param("matProp")String matProp);
    /**
     * 获取阶段某情形电子证照集合
     *
     * @param stageId
     * @param stateId
     * @param keyword
     * @return
     */
    List<AeaParIn> listInStateCertByStageIdAndStateId(@Param("stageId") String stageId,
                                                      @Param("stateId") String stateId,
                                                      @Param("keyword") String keyword,
                                                      @Param("rootOrgId") String rootOrgId);

    /**
     * 获取阶段非情形电子证照集合
     *
     * @param stageId
     * @param keyword
     * @return
     */
    List<AeaParIn> listNoStateInMatByStageId(@Param("stageId") String stageId,
                                             @Param("keyword") String keyword,
                                             @Param("rootOrgId") String rootOrgId);

    List<AeaParIn> listNoStateInCertAndMatByStageId(@Param("stageId") String stageId,
                                                    @Param("keyword") String keyword,
                                                    @Param("rootOrgId") String rootOrgId);

    List<AeaParIn> lisCertAndMatByStageId(@Param("stageId") String stageId,
                                          @Param("keyword") String keyword,
                                          @Param("rootOrgId") String rootOrgId);

    List<String> listCertListByStageId(@Param("stageId") String stageId,
                                       @Param("rootOrgId") String rootOrgId);

    List<String> getGlobalMatListByStateId(@Param("parStateId") String parStateId,
                                           @Param("rootOrgId") String rootOrgId);

    List<AeaParIn> listInStateStageMat(@Param("stageId") String stageId,
                                       @Param("parStateId") String parStateId,
                                       @Param("isCommon") String isCommon,
                                       @Param("keyword") String keyword,
                                       @Param("rootOrgId") String rootOrgId);

    void updateParStateIdNull(@Param("inId") String inId);

    void updateParStateId(@Param("inId") String inId, @Param("parStateId") String parStateId);

    List<String> getGlobalMatListByStageIdAndParStateId(@Param("stageId") String stageId,
                                                        @Param("parStateId") String parStateId,
                                                        @Param("rootOrgId") String rootOrgId);

    /**
     * 获取阶段材料证照表单
     * ALTER TABLE `AEA_HI_PAR_STATEINST`
     * ADD INDEX(`STAGEINST_ID`);
     *
     * @param aeaParIn
     * @return
     */
    List<AeaParIn> listStageInMatCertForm(AeaParIn aeaParIn);

    List<AeaParIn> listStoMatByCondition(AeaParIn aeaParIn);

    /**
     * 判断输入是否属于阶段下的某个事项
     *
     * @param stageId
     * @param itemVerId
     * @param inId
     * @return
     */
    Integer isCheckParInBelong2Item(@Param("stageId") String stageId,
                                    @Param("itemVerId") String itemVerId,
                                    @Param("inId") String inId);

    /**
     * 获取阶段事项下的输入输入
     *
     * @param stageId
     * @param itemVerId
     * @return
     */
    List<AeaParIn> listItemAeaParIn(@Param("stageId") String stageId, @Param("itemVerId") String itemVerId, @Param("rootOrgId") String rootOrgId);

    /**
     * 获取阶段实例下的所有的材料定义
     *
     * @param stageinstId
     * @param stageId
     * @param rootOrgId
     * @return
     */
    List<AeaParIn> listAeaParInByStageinstId(@Param("stageinstId") String stageinstId, @Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);
}
