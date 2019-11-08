package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImPurchaseinst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Augurit</li>
 * <li>创建时间：2019-06-26 09:20:12</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaImPurchaseinstMapper {

    void insertAeaImPurchaseinst(AeaImPurchaseinst aeaImPurchaseinst) throws Exception;

    void updateAeaImPurchaseinst(AeaImPurchaseinst aeaImPurchaseinst) throws Exception;

    void deleteAeaImPurchaseinst(@Param("id") String id) throws Exception;

    List<AeaImPurchaseinst> listAeaImPurchaseinst(AeaImPurchaseinst aeaImPurchaseinst) throws Exception;

    AeaImPurchaseinst getAeaImPurchaseinstById(@Param("id") String id) throws Exception;

    AeaImPurchaseinst getlastestPurchaseinstByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId) throws Exception;

    void insertPurchaseinst(AeaImPurchaseinst aeaImPurchaseinst) throws Exception;

    AeaImPurchaseinst getlastestPurchaseinstForContract(@Param("projPurchaseId") String projPurchaseId)throws Exception;
}
