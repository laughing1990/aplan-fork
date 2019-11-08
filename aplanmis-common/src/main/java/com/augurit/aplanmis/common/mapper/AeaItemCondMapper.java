package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemCond;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项前提条件表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemCondMapper {

    void insertAeaItemCond(AeaItemCond aeaItemCond);

    void updateAeaItemCond(AeaItemCond aeaItemCond);

    void deleteAeaItemCond(@Param("id") String id);

    List<AeaItemCond> listAeaItemCond(AeaItemCond aeaItemCond);

    AeaItemCond getAeaItemCondById(@Param("id") String id);

    Long getMaxSortNo(@Param("rootOrgId")String rootOrgId);

    void batchDeleteAeaItemCond(@Param("ids") String[] ids);

    void changIsActiveState(@Param("id") String id);

    List<AeaItemCond> listItemCondTopListByItemId(@Param("itemVerId") String itemVerId,
                                                 @Param("rootOrgId")String rootOrgId);

    List<AeaItemCond> listChildrenItemCondById(@Param("id") String id,
                                              @Param("rootOrgId")String rootOrgId);

    List<AeaItemCond> listChildAeaItemCondByParentCondId(@Param("itemVerId") String itemVerId,
                                                         @Param("parentCondId") String parentCondId,
                                                         @Param("rootOrgId")String rootOrgId);
}
