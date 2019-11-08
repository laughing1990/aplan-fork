package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemPartform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemPartformMapper {

     void insertAeaItemPartform(AeaItemPartform aeaItemPartform);
     void updateAeaItemPartform(AeaItemPartform aeaItemPartform);
     void deleteAeaItemPartform(@Param("id") String id);
     List <AeaItemPartform> listAeaItemPartform(AeaItemPartform aeaItemPartform);
     AeaItemPartform getAeaItemPartformById(@Param("id") String id);
}
