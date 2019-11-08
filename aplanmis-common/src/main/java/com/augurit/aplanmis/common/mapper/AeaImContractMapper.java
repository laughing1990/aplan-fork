package com.augurit.aplanmis.common.mapper;
import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.aplanmis.common.domain.AeaImContract;
import com.augurit.aplanmis.common.vo.ChosenProjInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 <ul>
 <li>项目名：奥格erp3.0--第一期建设项目</li>
 <li>版本信息：v1.0</li>
 <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 <li>创建人:thinkpad</li>
 <li>创建时间：2019-06-03 17:58:23</li>
 </ul>
 */
@Mapper
@Repository
public interface AeaImContractMapper {

    void insertAeaImContract(AeaImContract aeaImContract) throws Exception;
    void updateAeaImContract(AeaImContract aeaImContract) throws Exception;
    void deleteAeaImContract(@Param("id") String id) throws Exception;
    List <AeaImContract> listAeaImContract(AeaImContract aeaImContract) throws Exception;
    AeaImContract getAeaImContractById(@Param("id") String id) throws Exception;
    List<BscAttDetail> getFilesByRecordIds(@Param("tableName") String tableName, @Param("pkName") String pkName, @Param("keyword") String keyword, @Param("recordIds") String[] recordIds) throws Exception;

    List<ChosenProjInfoVo> getChosenProjInfoList(@Param("unitInfoId") String unitInfoId, @Param("auditFlag") String auditFlag, @Param("keyword") String keyword)throws Exception;

    List <AeaImContract> listAuditAeaImContract(AeaImContract aeaImContract) throws Exception;

    AeaImContract getAeaImContractByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId) throws Exception;
}
