package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiSolicitDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 征求意见详情表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaHiSolicitDetailMapper {

    void insertAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception;

    void updateAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception;

    void deleteAeaHiSolicitDetail(@Param("id") String id) throws Exception;

    List<AeaHiSolicitDetail> listAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception;

    AeaHiSolicitDetail getAeaHiSolicitDetailById(@Param("id") String id) throws Exception;

    List<AeaHiSolicitDetail> getAeaHiSolicitDetailBySolicitId(@Param("solicitId") String solicitId) throws Exception;

    //根据征求ID集合获取征询明细集合
    List<AeaHiSolicitDetail> listAeaHiSolicitDetailBySolicitIds(@Param("solicitIds") List<String> solicitIds) throws Exception;
}
