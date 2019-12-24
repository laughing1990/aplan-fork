package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiSolicitDetailUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

    /**
    * 征求意见详情用户任务表-Mapper数据与持久化接口类
    */
    @Mapper
    public interface AeaHiSolicitDetailUserMapper {

    public void insertAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception;
    public void updateAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception;
    public void deleteAeaHiSolicitDetailUser(@Param("id") String id) throws Exception;
    public List <AeaHiSolicitDetailUser> listAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception;
    public List <AeaHiSolicitDetailUser> getAeaHiSolicitDetailUserBySolicitDetailId(@Param("solicitDetailId") String solicitDetailId) throws Exception;
    public List <AeaHiSolicitDetailUser> getAeaHiSolicitDetailUserBySolicitId(@Param("solicitId") String solicitId) throws Exception;
    public AeaHiSolicitDetailUser getAeaHiSolicitDetailUserById(@Param("id") String id) throws Exception;
    }
