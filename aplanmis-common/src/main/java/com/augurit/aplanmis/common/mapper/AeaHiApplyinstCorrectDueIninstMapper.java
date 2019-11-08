package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectDueIninst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

    /**
    * 材料补全应收实例表-Mapper数据与持久化接口类
    <ul>
        <li>项目名：奥格erp3.0--第一期建设项目</li>
        <li>版本信息：v1.0</li>
        <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
        <li>创建人:tiantian</li>
        <li>创建时间：2019-08-28 17:34:02</li>
    </ul>
    */
    @Mapper
    public interface AeaHiApplyinstCorrectDueIninstMapper {

    public void insertAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst) throws Exception;
    public void batchInsertAeaHiApplyinstCorrectDueIninst(@Param("list") List<AeaHiApplyinstCorrectDueIninst> aeaHiApplyinstCorrectDueIninsts) throws Exception;
    public void updateAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst) throws Exception;
    public void deleteAeaHiApplyinstCorrectDueIninst(@Param("id") String id) throws Exception;
    public List <AeaHiApplyinstCorrectDueIninst> listAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst) throws Exception;
    public AeaHiApplyinstCorrectDueIninst getAeaHiApplyinstCorrectDueIninstById(@Param("id") String id) throws Exception;
    List<AeaHiApplyinstCorrectDueIninst> getCorrectDueIninstByApplyinstCorrectId(@Param("applyinstCorrectId") String applyinstCorrectId, @Param("topOrgId") String topOrgId) throws Exception;

    }
