package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParentProj;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:43:26</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaParentProjMapper {

    int insertAeaParentProj(AeaParentProj aeaParentProj) throws Exception;

    int updateAeaParentProj(AeaParentProj aeaParentProj) throws Exception;

    int deleteAeaParentProj(@Param("id") String id) throws Exception;

    List<AeaParentProj> listAeaParentProj(AeaParentProj aeaParentProj) throws Exception;

    AeaParentProj getAeaParentProjById(@Param("id") String id) throws Exception;

    AeaParentProj getParentProjByProjInfoId(@Param("projInfoId") String projInfoId);

    List<AeaParentProj> listChildProjByProjInfoIds(@Param("projInfoIds") List<String> projInfoId);
}
