package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemAcceptRange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 受理范围-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemAcceptRangeMapper {

    void insertAeaItemAcceptRange(AeaItemAcceptRange aeaItemAcceptRange);

    void updateAeaItemAcceptRange(AeaItemAcceptRange aeaItemAcceptRange);

    void deleteAeaItemAcceptRange(@Param("id") String id);

    List<AeaItemAcceptRange> listAeaItemAcceptRange(AeaItemAcceptRange aeaItemAcceptRange);

    AeaItemAcceptRange getAeaItemAcceptRangeById(@Param("id") String id);

    //AeaItemAcceptRange getAeaItemAcceptRangeByItemId(@Param("itemId") String itemId);

    AeaItemAcceptRange getAeaItemAcceptRangeByItemBasicId(@Param("itemBasicId") String itemBasicId);
}
