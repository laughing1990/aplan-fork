package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImItemExplain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* -Mapper数据与持久化接口类
*/
@Mapper
public interface AeaImItemExplainMapper {

    public void insertAeaImItemExplain(AeaImItemExplain aeaImItemExplain) throws Exception;
    public void updateAeaImItemExplain(AeaImItemExplain aeaImItemExplain) throws Exception;
    public void deleteAeaImItemExplain(@Param("id") String id) throws Exception;
    public List <AeaImItemExplain> listAeaImItemExplain(AeaImItemExplain aeaImItemExplain) throws Exception;
    public AeaImItemExplain getAeaImItemExplainById(@Param("id") String id) throws Exception;

    public AeaImItemExplain getAeaImItemExplainByItemVerId(@Param("itemVerId") String itemVerId) throws Exception;

}
