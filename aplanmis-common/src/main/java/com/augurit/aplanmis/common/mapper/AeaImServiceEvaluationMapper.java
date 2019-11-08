package com.augurit.aplanmis.common.mapper;
import com.augurit.aplanmis.common.domain.AeaImServiceEvaluation;
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
    <li>创建时间：2019-06-03 13:59:35</li>
</ul>
*/
@Mapper
@Repository
public interface AeaImServiceEvaluationMapper {
     void insertAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation) throws Exception;
     void updateAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation) throws Exception;
     void deleteAeaImServiceEvaluation(@Param("id") String id) throws Exception;
     List <AeaImServiceEvaluation> listAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation) throws Exception;
     AeaImServiceEvaluation getAeaImServiceEvaluationById(@Param("id") String id) throws Exception;
     List<AeaImServiceEvaluation> listServiceEvaluationByUnitInfoIdAndServiceId(@Param("unitInfoId") String unitInfoId, @Param("serviceId") String serviceId) throws Exception;

    List <AeaImServiceEvaluation> listAuditServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation)throws Exception;
}
