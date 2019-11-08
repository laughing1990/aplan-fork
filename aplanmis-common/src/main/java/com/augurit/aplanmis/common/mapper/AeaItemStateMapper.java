package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.dto.AeaItemStateDto;
import com.augurit.aplanmis.common.mapper.base.BaseMapper;
import com.augurit.aplanmis.common.qo.state.AeaItemStateQo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项情形定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemStateMapper extends BaseMapper<AeaItemState> {

    int insertAeaItemState(AeaItemState aeaItemState);

    int updateAeaItemState(AeaItemState aeaItemState);

    int deleteAeaItemState(@Param("id") String id);

    List<AeaItemState> listAeaItemState(AeaItemState aeaItemState) throws Exception;

    AeaItemState getAeaItemStateById(@Param("id") String id);

    /**
     * 根据事项版本ID获取情形列表
     *
     * @param itemVerId 事项版本
     * @return List<AeaItemState>
     * @throws Exception e
     */
    List<AeaItemState> listAeaItemStateByItemVerId(@Param("itemVerId") String itemVerId, @Param("stateVerId") String stateVerId) throws Exception;

    /**
     * 根据事项版本ID获取root情形列表
     *
     * @param itemVerId  事项版本
     * @param stateVerId 情形版本
     * @return List<AeaItemState>
     * @throws Exception e
     */
    List<AeaItemState> listRootAeaItemStateByItemVerId(@Param("itemVerId") String itemVerId, @Param("stateVerId") String stateVerId) throws Exception;

    /**
     * 根据父情形ID查找子情形列表
     *
     * @param itemStateId 父情形ID
     * @return List<AeaItemState>
     * @throws Exception e
     */
    List<AeaItemState> listAeaItemStateByParentItemStateId(String itemStateId) throws Exception;

    /**
     * 根据事项情形ID获取答案
     *
     * @param itemStateId 情形ID
     * @return List<AeaItemState>
     * @throws Exception e
     */
    List<AeaItemState> listAnswerStateByQuestionStateId(@Param("itemStateId") String itemStateId) throws Exception;

    /**
     * 根据情形版本ID查找所有情形
     *
     * @param stateVerId 情形版本ID
     * @return List<AeaItemState>
     */
    List<AeaItemState> listAeaItemStateByStateVerId(String stateVerId);

    /**
     * 根据情形ID查询情形实例
     *
     * @param itemStateIds
     * @return
     */
    List<AeaItemState> listAeaItemStateByIds(@Param("itemStateIds") String[] itemStateIds);

    Long getMaxSortNo();

    List<AeaItemState> listAeaItemStateWithStateVer(AeaItemState aeaItemState);

    List<AeaItemState> getAeaItemStateByItemVerId(@Param("itemVerId") String itemVerId);

    void thoroughDeleteAeaItemState(@Param("id") String id);

    void thoroughDeleteAeaItemStateByItemId(@Param("itemId") String itemId);

    List<AeaItemState> listAeaItemStateForIsPublish(AeaItemState aeaItemState);

    List<AeaItemState> listAeaItemStateBySyncKpState(AeaItemState aeaItemState);

    void batchDeleteAeaItemState(@Param("ids") String[] ids);

    /**
     * 批量删除某个事项情形版本情形数据
     *
     * @param itemVerId
     * @param stateVerId
     */
    void batchDelAeaItemStateVerState(@Param("itemVerId") String itemVerId,
                                      @Param("stateVerId") String stateVerId,
                                      @Param("rootOrgId") String rootOrgId);

    void changIsActiveState(@Param("id") String id);

    List<AeaItemState> listAeaItemStateByItemId(@Param("itemId") String itemId);

    List<AeaItemState> listTopAeaItemStateByItemId(@Param("itemId") String itemId);

    List<AeaItemState> listChildAeaItemStateByParentStateId(@Param("itemVerId") String itemVerId,
                                                            @Param("stateVerId") String stateVerId,
                                                            @Param("parentStateId") String parentStateId,
                                                            @Param("rootOrgId") String rootOrgId);

    /**
     * 根据itemVerId 查询AeaItemState实体类
     *
     * @param itemVerId 事项版本号
     * @return AeaItemState
     */
    AeaItemState getOneByItemVerId(String itemVerId);

    /**
     * 根据
     *
     * @param
     * @return
     */
    AeaItemState getPublistOneByItemVerId(@Param("itemVerId") String itemVerId);

    List<AeaItemStateDto> listAeaItemStateByCriteria(AeaItemStateQo aeaItemStateQo);

    /**
     * 查询事项下root情形或选定情形下 情形及答案
     *
     * @param itemVerId     实现版本ID
     * @param stateVerId    情形版本ID
     * @param parentStateId 父情形 =ROOT root情形
     * @param rootOrgId     顶级机构ID
     * @return
     */
    List<AeaItemState> listItemStateByParentItemStateId(@Param("itemVerId") String itemVerId, @Param("stateVerId") String stateVerId, @Param("parentStateId") String parentStateId, @Param("rootOrgId") String rootOrgId);
}
