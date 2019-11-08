package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaExProjCertProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 建设工程规划许可证-Mapper数据与持久化接口类
 */
@Mapper
public interface AeaExProjCertProjectMapper {

    public void insertAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject) throws Exception;

    public void updateAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject) throws Exception;

    public void deleteAeaExProjCertProject(@Param("id") String id) throws Exception;

    public List<AeaExProjCertProject> listAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject) throws Exception;

    public AeaExProjCertProject getAeaExProjCertProjectById(@Param("id") String id) throws Exception;
}
