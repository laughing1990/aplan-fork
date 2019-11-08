package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectRealIninst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 材料补全已收实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:tiantian</li>
 * <li>创建时间：2019-08-28 17:34:16</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiApplyinstCorrectRealIninstMapper {

    void insertAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst) throws Exception;

    void updateAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst) throws Exception;

    void deleteAeaHiApplyinstCorrectRealIninst(@Param("id") String id) throws Exception;

    List<AeaHiApplyinstCorrectRealIninst> listAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst) throws Exception;

    AeaHiApplyinstCorrectRealIninst getAeaHiApplyinstCorrectRealIninstById(@Param("id") String id) throws Exception;

    List<AeaHiApplyinstCorrectRealIninst> getCorrectRealIninstByApplyinstCorrectId(@Param("applyinstCorrectId") String applyinstCorrectId, @Param("topOrgId") String topOrgId);
}
