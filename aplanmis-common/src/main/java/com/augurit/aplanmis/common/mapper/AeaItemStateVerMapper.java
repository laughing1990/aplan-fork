package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemStateVer;
import com.augurit.aplanmis.common.qo.state.AeaItemStateVersionQo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemStateVerMapper {

     void insertAeaItemStateVer(AeaItemStateVer aeaItemStateVer) ;

     void updateAeaItemStateVer(AeaItemStateVer aeaItemStateVer) ;

     void deleteAeaItemStateVer(@Param("id") String id) ;

     List<AeaItemStateVer> listAeaItemStateVer(AeaItemStateVer aeaItemStateVer);

     AeaItemStateVer getAeaItemStateVerById(@Param("id") String id) ;

    /**
     * 根据事项版本获取改版本下的已发布或试运行的最大的版本号
     * @param itemVerId
     * @return
     */
    AeaItemStateVer getMaxStateVerId(String itemVerId);

    /**
     * 通过事项版本id获取最新情形版本
     *
     * @param itemVerId
     * @return
     */
    List<AeaItemStateVer> listTestRunOrPublishedItemStateVer(@Param("itemVerId") String itemVerId,
                                                             @Param("rootOrgId")String rootOrgId);

    List<AeaItemStateVer> listAeaItemStateVersionByQueryCriteria(AeaItemStateVersionQo aeaItemStateVersionQo);

    List<AeaItemStateVer> listAeaItemStateVersion(AeaItemStateVer aeaItemStateVersion);

    void deprecateAllTestRunAndPublishedVersion(@Param("rootOrgId")String rootOrgId,
                                                @Param("itemVerId")String itemVerId,
                                                @Param("stateVerId") String stateVerId);
}

