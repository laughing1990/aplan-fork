package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

    /**
    * 征求意见主表-Mapper数据与持久化接口类
    */
    @Mapper
    public interface AeaHiSolicitMapper {

    public void insertAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception;
    public void updateAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception;
    public void deleteAeaHiSolicit(@Param("id") String id) throws Exception;
    public List <AeaHiSolicit> listAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception;
    public AeaHiSolicit getAeaHiSolicitById(@Param("id") String id) throws Exception;
    }
