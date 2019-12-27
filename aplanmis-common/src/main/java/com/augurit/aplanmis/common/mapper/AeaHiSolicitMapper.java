package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import com.augurit.aplanmis.common.vo.solicit.AeaHiSolicitVo;
import com.augurit.aplanmis.common.vo.solicit.QueryCondVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 征求意见主表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaHiSolicitMapper {

    public void insertAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception;

    public void updateAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception;

    public void deleteAeaHiSolicit(@Param("id") String id) throws Exception;

    public List<AeaHiSolicit> listAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception;

    public AeaHiSolicit getAeaHiSolicitById(@Param("id") String id) throws Exception;

    /**
     * 根据意见征求流水号获取意见征求信息
     *
     * @param solicitCode 意见征求流水号
     * @return AeaHiSolicit
     * @throws Exception e
     */
    AeaHiSolicit getAeaHiSolicitBySolicitCode(@Param("solicitCode") String solicitCode) throws Exception;

    /**
     * 征询列表
     *
     * @param condVo 查询条件
     * @return List<AeaHiSolicitVo>
     */
    List<AeaHiSolicitVo> listSolicit(QueryCondVo condVo) throws Exception;

    /**
     * 获取当前申报下所有的意见征询数量
     *
     * @param applyinstId 申报实例
     * @return List<AeaHiSolicit>
     */
    List<AeaHiSolicit> getAeaHiSolicitByApplyinstId(String applyinstId) throws Exception;
}
