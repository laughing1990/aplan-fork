package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImUnitRequire;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaImUnitRequireMapper {

    public void insertAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire) throws Exception;
    public void updateAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire) throws Exception;
    public void deleteAeaImUnitRequire(@Param("id") String id) throws Exception;
    public List <AeaImUnitRequire> listAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire) throws Exception;
    public AeaImUnitRequire getAeaImUnitRequireById(@Param("id") String id) throws Exception;

    public AeaImUnitRequire getAeaImUnitRequireByItemVerId(@Param("itemVerId") String itemVerId) throws Exception;
}