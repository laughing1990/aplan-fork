package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuideAttr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemGuideAttrMapper {

     void insertAeaItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr);

     void updateAeaItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr);

     void deleteAeaItemGuideAttr(@Param("id") String id);

     List<AeaItemGuideAttr> listAeaItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr);

     AeaItemGuideAttr getAeaItemGuideAttrById(@Param("id") String id);

     void batchDeleteItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr);
}