package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImQualLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* -Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaImQualLevelMapper {

    public void insertAeaImQualLevel(AeaImQualLevel aeaImQualLevel) throws Exception;
    public void updateAeaImQualLevel(AeaImQualLevel aeaImQualLevel) throws Exception;
    public void deleteAeaImQualLevel(@Param("id") String id) throws Exception;
    public List<AeaImQualLevel> listAeaImQualLevel(AeaImQualLevel aeaImQualLevel) throws Exception;
    public AeaImQualLevel getAeaImQualLevelById(@Param("id") String id) throws Exception;
}
