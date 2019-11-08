package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemCorrectDueIninst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项输入输出实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-08-03 10:29:47</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiItemCorrectDueIninstMapper {

    public void insertAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst) throws Exception;

    public void batchInsertAeaHiItemCorrectDueIninst(@Param("list") List<AeaHiItemCorrectDueIninst> aeaHiItemCorrectDueIninsts) throws Exception;

    public void updateAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst) throws Exception;

    public void deleteAeaHiItemCorrectDueIninst(@Param("id") String id) throws Exception;

    public List<AeaHiItemCorrectDueIninst> listAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst) throws Exception;

    public AeaHiItemCorrectDueIninst getAeaHiItemCorrectDueIninstById(@Param("id") String id) throws Exception;

    List<AeaHiItemCorrectDueIninst> getCorrectDueIninstByCorrectId(@Param("correctId") String correctId, @Param("topOrgId") String topOrgId) throws Exception;

}
