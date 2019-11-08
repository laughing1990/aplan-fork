package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemSeq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项定义序列表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemSeqMapper {

     void insertAeaItemSeq(AeaItemSeq aeaItemSeq) ;

     void updateAeaItemSeq(AeaItemSeq aeaItemSeq) ;

     void deleteAeaItemSeq(@Param("id") String id) ;

     List<AeaItemSeq> listAeaItemSeq(AeaItemSeq aeaItemSeq) throws Exception;

     AeaItemSeq getAeaItemSeqById(@Param("id") String id) throws Exception;

    /**
     * 通过事项id和事项版本获取序列
     *
     * @param itemId
     * @param itemVerId
     * @return
     */
    AeaItemSeq getSeqByItemIdAndVerId(@Param("itemId") String itemId,
                                      @Param("itemVerId") String itemVerId,
                                      @Param("rootOrgId") String rootOrgId);

    /**
     * 获取事项id下的版本并按版本降序排列
     *
     * @param itemId
     * @return
     */
    List<AeaItemSeq> listSeqByItemId(@Param("itemId") String itemId,
                                     @Param("rootOrgId") String rootOrgId);

    /**
     * 通过事项id删除序列
     *
     * @param itemId
     */
    void deleteSeqByItemId(@Param("itemId") String itemId,
                           @Param("rootOrgId")String rootOrgId);

}
