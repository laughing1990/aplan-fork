package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImServiceMajor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaImServiceMajorMapper {
    void insertAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor) throws Exception;

    void updateAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor) throws Exception;

    void deleteAeaImServiceMajor(@Param("id") String id) throws Exception;

    List<AeaImServiceMajor> listAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor) throws Exception;

    AeaImServiceMajor getAeaImServiceMajorById(@Param("id") String id) throws Exception;

    Integer checkUniqueMajorTypeCode(@Param("majorId") String majorId, @Param("majorCode") String majorCode);

    List<AeaImServiceMajor> listMajorTree(AeaImServiceMajor aeaImServiceMajor) throws Exception;

    List<AeaImServiceMajor> listMajorByCertinstId(@Param("certinstId") String certinstId);

    /**
     * 根据专业ID查询其子专业类别（不包含自身）
     * @param majorId
     * @return
     * @throws Exception
     */
    List<AeaImServiceMajor> listChildrenAeaImServiceMajorByMajorId(@Param("majorId") String majorId) throws Exception;

    /**
     * 根据资质ID删除专业树
     * @param qualId
     * @throws Exception
     */
    void deleteAeaImServiceMajorTreeByQualId(@Param("qualId") String qualId) throws Exception;

    /**
     * 根据资质ID查找专业树根节点（一个资质对应一个根节点）
     * @param qualId
     * @return
     * @throws Exception
     */
    AeaImServiceMajor getAeaImServiceMajorRootNodeByQualId(@Param("qualId") String qualId)throws Exception;

    List<AeaImServiceMajor> getServiceMajorListByQualId(String qualId);

    List<AeaImServiceMajor> getServiceMajorListByCertinstId(@Param("certinstId") String certinstId);
}
