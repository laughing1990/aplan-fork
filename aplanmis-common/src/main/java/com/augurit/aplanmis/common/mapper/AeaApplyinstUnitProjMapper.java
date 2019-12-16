package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaApplyinstUnitProj;
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
 * <li>创建人:chenzh</li>
 * <li>创建时间：2019-07-04 19:24:00</li>
 * </ul>
 */
@Repository
@Mapper
public interface AeaApplyinstUnitProjMapper {

    int insertAeaApplyinstUnitProj(AeaApplyinstUnitProj aeaApplyinstUnitProj);

    int updateAeaApplyinstUnitProj(AeaApplyinstUnitProj aeaApplyinstUnitProj);

    int deleteAeaApplyinstUnitProj(@Param("id") String id);

    List<AeaApplyinstUnitProj> listAeaApplyinstUnitProj(AeaApplyinstUnitProj aeaApplyinstUnitProj);

    AeaApplyinstUnitProj getAeaApplyinstUnitProjById(@Param("id") String id);

    int batchInsertAeaApplyinstUnitProjMapper(@Param("aeaApplyinstUnitProjList") List<AeaApplyinstUnitProj> aeaApplyinstUnitProjList);

    /**
     * 根据申请实例查询项目单位信息关联；
     * @param applyinstId
     * @return
     */
    List<AeaApplyinstUnitProj> getAeaApplyinstUnitProjByApplyinstId(@Param("applyinstId") String applyinstId);

    void batchDeleteAeaApplyinstUnitProj(@Param("ids") String[] ids);
}
