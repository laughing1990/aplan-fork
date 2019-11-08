package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuideSpecials;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 特殊程序-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemGuideSpecialsMapper {

     void insertAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials);

     void updateAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials) ;

     void deleteAeaItemGuideSpecials(@Param("id") String id) ;

     List<AeaItemGuideSpecials> listAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials) ;

     AeaItemGuideSpecials getAeaItemGuideSpecialsById(@Param("id") String id) ;

     void batchDeleteGuideSpecialsByItemVerId(@Param("itemVerId") String itemVerId,
                                              @Param("rootOrgId")String rootOrgId);
}

