package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaApplyinstProj;
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
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:12:18</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaApplyinstProjMapper {

    public void insertAeaApplyinstProj(AeaApplyinstProj aeaApplyinstProj) throws Exception;

    public void updateAeaApplyinstProj(AeaApplyinstProj aeaApplyinstProj) throws Exception;

    public void deleteAeaApplyinstProj(@Param("id") String id) throws Exception;

    public List<AeaApplyinstProj> listAeaApplyinstProj(AeaApplyinstProj aeaApplyinstProj) throws Exception;

    public AeaApplyinstProj getAeaApplyinstProjById(@Param("id") String id) throws Exception;

    /**
     * 根据申请实例获取申报项目列表信息
     *
     * @param applyinstId
     * @return
     * @throws Exception
     */
    List<AeaApplyinstProj> getAeaApplyinstProjByApplyinstId(@Param("applyinstId") String applyinstId) throws Exception;

    List<AeaApplyinstProj> getAeaApplyinstProjCascadeProjByApplyinstId(@Param("applyinstId") String applyinstId) throws Exception;
}
