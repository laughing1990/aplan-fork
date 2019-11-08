package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemCorrectRealIninst;
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
 * <li>创建时间：2019-08-03 10:31:32</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiItemCorrectRealIninstMapper {

    public void insertAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst) throws Exception;

    public void updateAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst) throws Exception;

    public void deleteAeaHiItemCorrectRealIninst(@Param("id") String id) throws Exception;

    public List<AeaHiItemCorrectRealIninst> listAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst) throws Exception;

    public AeaHiItemCorrectRealIninst getAeaHiItemCorrectRealIninstById(@Param("id") String id) throws Exception;

    List<AeaHiItemCorrectRealIninst> getCorrectRealIninstByCorrectId(@Param("correctId") String correctId, @Param("topOrgId") String topOrgId);
}
