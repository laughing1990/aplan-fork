package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaBusCertinst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:12:20</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaBusCertinstMapper {

    public void insertAeaBusCertinst(AeaBusCertinst aeaBusCertinst) throws Exception;

    public void updateAeaBusCertinst(AeaBusCertinst aeaBusCertinst) throws Exception;

    public void deleteAeaBusCertinst(@Param("id") String id) throws Exception;

    public List<AeaBusCertinst> listAeaBusCertinst(AeaBusCertinst aeaBusCertinst) throws Exception;

    public AeaBusCertinst getAeaBusCertinstById(@Param("id") String id) throws Exception;

    void deleteAeaBusCertinstByCertinstId(@Param("certinstId") String certinstId) throws Exception;

    AeaBusCertinst getAeaBusCertinstByCertinstId(@Param("certinstId")String certinstId,@Param("busRecordId")String busRecordId) throws Exception;

    void deleteByCertinstId(@Param("certinstId") String certinstId) throws Exception;

    void deleteAeaBusCertinstByTableNameAndRecordId(@Param("busTableName") String tableName, @Param("busRecordId") String busRecordId);

    void batchInsertBusCertinst(@Param("list") List<AeaBusCertinst> busCertinsts) throws Exception;

    List<AeaBusCertinst> listLinkmanCertinstName(@Param("linkmanIds") String[] linkmanIds) throws Exception;

    void updateAeaBusCertinstByAudit(AeaBusCertinst aeaBusCertinst) throws Exception;
}
