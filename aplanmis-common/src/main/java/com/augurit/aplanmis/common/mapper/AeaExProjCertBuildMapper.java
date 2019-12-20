package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaExProjCertBuild;
import io.swagger.annotations.ApiModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaExProjCertBuildMapper {
    /**
     * 新增
     *
     * @param aeaExProjCertBuild
     * @return
     * @throws Exception
     */
    int insertAeaExProjCertBuild(AeaExProjCertBuild aeaExProjCertBuild) throws Exception;

    /**
     * 删除
     *
     * @param buildId
     * @return
     * @throws Exception
     */
    void delAeaExProjCertBuild(String buildId) throws Exception;

    /**
     * 修改
     *
     * @param aeaExProjCertBuild
     * @return
     * @throws Exception
     */
    void updateAeaExProjCertBuild(AeaExProjCertBuild aeaExProjCertBuild) throws Exception;

    /**
     * 查询
     *
     * @param aeaExProjCertBuild
     * @return
     */
    List<AeaExProjCertBuild> listAeaExProjCertBuild(AeaExProjCertBuild aeaExProjCertBuild) throws Exception;

    /**
     * 根据项目ID查询
     */
    AeaExProjCertBuild findAeaExProjCertBuildByProjId(String projInfoId) throws Exception;
}
