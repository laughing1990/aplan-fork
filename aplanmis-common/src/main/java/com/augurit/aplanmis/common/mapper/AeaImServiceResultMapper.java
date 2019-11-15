package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImServiceResult;
import com.augurit.aplanmis.common.vo.ServiceProjInfoVo;
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
 <li>创建人:tiantian</li>
 <li>创建时间：2019-06-20 09:10:47</li>
 </ul>
 */
@Mapper
@Repository
public interface AeaImServiceResultMapper {
    void insertAeaImServiceResult(AeaImServiceResult aeaImServiceResult) throws Exception;
    void updateAeaImServiceResult(AeaImServiceResult aeaImServiceResult) throws Exception;
    void deleteAeaImServiceResult(@Param("id") String id) throws Exception;
    List <AeaImServiceResult> listAeaImServiceResult(AeaImServiceResult aeaImServiceResult) throws Exception;
    AeaImServiceResult getAeaImServiceResultById(@Param("id") String id) throws Exception;

    List<ServiceProjInfoVo> getServiceProjInfoList(@Param("unitInfoId") String unitInfoId, @Param("auditFlag") String auditFlag, @Param("keyword") String keyword)throws Exception;

    /**
     * 查询当前采购项目下易服务结果列表
     *
     * @param projPurchaseId
     * @return
     */
    List<AeaImServiceResult> listServiceResultByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId);
}
