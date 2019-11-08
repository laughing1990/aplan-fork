package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParState;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主题情形定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParStateMapper {

    int insertAeaParState(AeaParState aeaParState);

    int updateAeaParState(AeaParState aeaParState);

    int deleteAeaParState(@Param("id") String id);

    List<AeaParState> listAeaParState(AeaParState aeaParState);

    AeaParState getAeaParStateById(@Param("id") String id);

    /**
     * 根据阶段ID获取阶段下所有的情形列表
     *
     * @param stageId 阶段ID
     * @return List<AeaParState>
     */
    List<AeaParState> listAeaParStateByStageId(@Param("stageId") String stageId,
                                               @Param("rootOrgId") String rootOrgId);

    /**
     * 根据阶段ID获取阶段root情形（不包括子情形）
     *
     * @param stageId 阶段ID
     * @return List<AeaParState>
     */
    List<AeaParState> listRootAeaParStateByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    /**
     * 根据父情形ID查找子情形
     *
     * @param parentStateId 父情形ID
     * @return List<AeaParState>
     */
    List<AeaParState> listAeaParStateByParentStateId(@Param("parentStateId") String parentStateId, @Param("rootOrgId") String rootOrgId);

    /**
     * 根据父情形查询其 父子情形
     *
     * @param stageId       阶段ID
     * @param parentStateId ==ROOT 查询的是当前阶段的root情形
     * @param rootOrgId     顶级机构ID
     * @return
     */
    List<AeaParState> listParStateByParentStateId(@Param("stageId") String stageId, @Param("parentStateId") String parentStateId, @Param("rootOrgId") String rootOrgId);

    /**
     * 根据问题情形查找答案
     *
     * @param parentParStateId 父情形ID
     * @return List<AeaParState>
     */
    List<AeaParState> listAnswerStateByQuestionStateId(@Param("parentParStateId") String parentParStateId,
                                                       @Param("rootOrgId") String rootOrgId);

    /**
     * 根据阶段ID查询所有的root节点下的情形列表
     *
     * @param stageId 阶段ID
     * @return List<AeaParState>
     */
    List<AeaParState> listChildAeaParStateByStageId(@Param("stageId") String stageId,
                                                    @Param("rootOrgId") String rootOrgId);

    /**
     * 根据ID批量查询
     *
     * @param stateIds 情形ID列表
     * @return List<AeaItemState>
     */
    List<AeaParState> listAeaItemStateByIds(@Param("stateIds") String[] stateIds);

    /**
     * 根据父节点和关键字列出子情形
     *
     * @param parStateId 父情形ID
     * @param keyword    关键字,支持情形名称
     * @return
     */
    List<AeaParState> listAllChildStates(@Param("parStateId") String parStateId,
                                         @Param("keyword") String keyword,
                                         @Param("rootOrgId") String rootOrgId);

    Long getMaxSortNo(@Param("rootOrgId") String rootOrgId);

    List<AeaParState> listAeaParStateByParentStateIdAndStageId(@Param("stageId") String stageId,
                                                               @Param("parentStateId") String parentStateId,
                                                               @Param("rootOrgId") String rootOrgId);


}
