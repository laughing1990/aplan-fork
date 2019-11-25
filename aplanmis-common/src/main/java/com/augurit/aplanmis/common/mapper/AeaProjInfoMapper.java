package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParentProj;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.vo.AeaProjInfoVo;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryAeaProjInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目库信息-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:43:27</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaProjInfoMapper {

    int insertAeaProjInfo(AeaProjInfo aeaProjInfo);

    int updateAeaProjInfo(AeaProjInfo aeaProjInfo);

    int deleteAeaProjInfo(@Param("id") String id);

    int batchDeleteAeaProjInfo(@Param("ids") String[] ids);

    List<AeaProjInfo> listAeaProjInfo(AeaProjInfo aeaProjInfo);

    AeaProjInfo getAeaProjInfoById(@Param("id") String id);

    AeaProjInfo getAeaProjInfoByGcbm(@Param("gcbm") String gcbm);

    List<AeaProjInfo> findAeaProjInfoByKeyword(@Param("keyword") String keyword, @Param("rootOrgId") String rootOrgId);

    List<AeaProjInfo> findApplyProj(@Param("applyinstId") String applyinstId);

    List<AeaProjInfo> findChildProj(@Param("projInfoId") String projInfoId);

    AeaProjInfo findParentProj(@Param("projInfoId") String projInfoId);

    List<AeaProjInfo> findAeaProjInfoByApplyLinkmanInfoId(@Param("linkmanInfoId") String linkmanInfoId);

    List<AeaProjInfo> findRootAeaProjInfoByLinkmanInfoId(@Param("linkmanInfoId") String linkmanInfoId);

    List<AeaProjInfo> findRootAeaProjInfoByUnitInfoId(@Param("unitInfoId") String unitInfoId);

    List<AeaProjInfo> findRootAeaProjInfoByLinkmanInfoIdAndUnitInfoId(@Param("linkmanInfoId") String linkmanInfoId, @Param("unitInfoId") String unitInfoId);

    AeaParentProj getAeaParentProjByChildId(@Param("projInfoId") String projInfoId);

    List<AeaParentProj> listAeaParentProj(AeaParentProj aeaParentProj);

    /**
     * 根据主键查询单个项目信息
     *
     * @param id
     * @return
     */
    AeaProjInfo getOnlyAeaProjInfoById(@Param("id") String id);

    /**
     * 根据ids批量查询
     *
     * @param ids
     * @return
     */
    List<AeaProjInfo> listAeaProjInfoByIds(@Param("ids") String[] ids);

    List<AeaProjInfo> listProjByApplyinstId(String applyinstId);

    /*
    根据projInfoId查询子项目
     */
    List<AeaProjInfo> getChildProjListById(@Param("projInfoId") String projInfoId);

    /**
     * 插入父子项目表
     */
    int insertAeaParentProj(AeaParentProj aeaParentProj);

    /**
     * 通过编码获取项目信息
     */
    List<AeaProjInfo> getProjInfoByCode(@Param("localCode") String localCode);

    /**
     * 跟据parentid || childid || nodeId 删除 关联表
     *
     * @return 删除的行数
     */
    int delParentProjByCondition(AeaParentProj aeaParentProj);

    /**
     * 修改父子项目表
     */
    void updateAeaParentProj(AeaParentProj aeaParentProj);

    /**
     * 获取业主单位未发布的项目列表
     *
     * @param localCode
     * @param unitInfoId
     * @return
     */
    List<AeaProjInfo> getUnpublishedProjInfoList(@Param("localCode") String localCode, @Param("unitInfoId") String unitInfoId);

    /**
     * 根据子项目id获取父项目信息
     *
     * @param projInfoId
     * @return
     */
    AeaProjInfo getAeaProjInfoByChildProjId(String projInfoId);

    /**
     * 根据applyinstId查询项目信息
     *
     * @param applyinstId
     * @return
     */
    AeaProjInfo getAeaProjInfoByApplyinstId(@Param("applyinstId") String applyinstId);

    void physicsDeleteAeaProjInfoByProjInfoId(@Param("id") String id);

    /**
     * 11 将删除节点下的子节点加载到删除节点的父节点下
     *
     * @param parentProjId 父节点ID
     * @param childProjIds 子节点集合
     */
    int updateParentProjByChildProjIds(@Param("parentProjId") String parentProjId, @Param("childProjIds") String[] childProjIds);

    /**
     * 12 根据childId 更新projSeq
     */
    int updateProjSeqByChildId(AeaParentProj parentProj);

    /**
     * 2 查询节点下的所有子节点-oracle
     */
    List<AeaParentProj> listAllChildOfNodeTree(@Param("childProjId") String childProjId);

    /**
     * 5 根据节点查询所有节点下的子节点的 AeaProjInfo
     */
    List<AeaProjInfo> listAllChildNodeOfNode(@Param("childProjId") String childProjId);

    /**
     * 根据childId更新父ID和projSeq-
     *
     * @param aeaParentProj
     * @return
     */
    int updateParentProjByChildId(AeaParentProj aeaParentProj);


    /**
     * 6 根据子节点查询所有的父节点AeaProjInfo
     */
    List<AeaProjInfo> listAllParentOfNode(@Param("childProjId") String childProjId);

    List<AeaProjInfo> getProjInfoByLocalCodes(@Param("localCodes") String[] localCodes);

    /**
     * 根据申请实例id获取项目信息
     *
     * @param applyinstId 申请实例id
     * @return
     */
    List<AeaProjInfo> listAeaProjInfoByApplyinstId(String applyinstId);

    String getParentOrgName(String orgId);

    /**
     * 多查询条件查询项目列表
     * @param conditionalQueryAeaProjInfo
     * @return
     */
    List<AeaProjInfoVo> conditionalQueryAeaProjInfo(ConditionalQueryAeaProjInfo conditionalQueryAeaProjInfo);

    /**
     * 根据父项目id查找下一级项目列表
     * @param projInfoId
     * @return
     */
    List<AeaProjInfoVo>  listChildProjInfoByProjInfoId(@Param("projInfoId") String projInfoId);

    /**
     * 批量删除项目及其所有子孙类项目
     * @param ids
     * @return
     */
    int batchDeleteProjInfoAndChildProjInfo(@Param("ids") String[] ids);

    /**
     * 查询用户所属行政区划及其所有子孙类行政区划
     * @param userId
     * @return
     */
    List<String> listBelongRegionAndChildRegionsByUserId(@Param("userId") String userId);

}
