package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 单位与项目关联表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:40:49</li>
 * </ul>
 */
@Repository
@Mapper
public interface AeaUnitProjMapper {

    int insertAeaUnitProj(AeaUnitProj aeaUnitProj);

    int updateAeaUnitProj(AeaUnitProj aeaUnitProj) throws Exception;

    int deleteAeaUnitProj(@Param("id") String id) throws Exception;

    List<AeaUnitProj> listAeaUnitProj(AeaUnitProj aeaUnitProj) throws Exception;

    AeaUnitProj getAeaUnitProjById(@Param("id") String id) throws Exception;

    int batchInsertAeaUnitProj(@Param("aeaUnitProjList") List<AeaUnitProj> aeaUnitProjList);

    void batchDeleteUnitProj(@Param("projId") String projId, @Param("isOwner") String isOwner, @Param("unitInfoIds") String[] unitInfoIds);

    AeaUnitProj findUnitPorojByProjInfoIdAndUnitInfoId(@Param("projInfoId") String projInfoId, @Param("unitInfoId") String unitInfoId, @Param("isOwner") String isOwner);

    List<AeaUnitProj> findUnitPorojByProjInfoIdsAndUnitInfoIds(@Param("projInfoIds") String[] projInfoIds, @Param("unitInfoIds") String[] unitInfoIds, @Param("isOwner") String isOwner);

    List<AeaUnitInfo> listProjUnitInfo(AeaUnitProj aeaUnitProj);

    void batchDeleteAllUnitProjByProjInfoId(@Param("projId") String projId);

    /**
     * 根据主键批量查询单位信息
     *
     * @param unitProjIds
     * @return
     */
    List<AeaUnitInfo> getAeaUnitProjByUnitProjIds(@Param("unitProjIds") String[] unitProjIds);

    void deleteAeaUnitProjByProjInfoId(String delProjId);

    /**
     * 根据项目ID,单位id,单位类型，查询项目单位关联数据
     *
     * @param projInfoId
     * @param unitInfoId
     * @param unitType
     * @return
     */
    List<AeaUnitProj> findUnitProjByProjIdAndUnitIdAndunitType(@Param("projInfoId") String projInfoId, @Param("unitInfoId") String unitInfoId, @Param("unitType") String unitType);

    /**
     * 根据项目id和单位类型查询单位信息
     * @param projInfoId
     * @param unitType
     * @return
     */
    List<AeaUnitInfo> findUnitInfoByProjIdAndUnitType(@Param("projInfoId") String projInfoId, @Param("unitType") String unitType);

}
