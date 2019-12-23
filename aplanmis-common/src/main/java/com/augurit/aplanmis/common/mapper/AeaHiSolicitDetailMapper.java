package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiSolicitDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

    /**
    * 征求意见详情表-Mapper数据与持久化接口类
    */
    @Mapper
    public interface AeaHiSolicitDetailMapper {

    public void insertAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception;
    public void updateAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception;
    public void deleteAeaHiSolicitDetail(@Param("id") String id) throws Exception;
    public List <AeaHiSolicitDetail> listAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception;
    public AeaHiSolicitDetail getAeaHiSolicitDetailById(@Param("id") String id) throws Exception;
    }
