package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImServiceQual;
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
 <li>创建时间：2019-06-10 17:09:13</li>
 </ul>
 */
@Mapper
@Repository
public interface AeaImServiceQualMapper {

    void insertAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception;
    void updateAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception;
    void deleteAeaImServiceQual(@Param("id") String id) throws Exception;
    List <AeaImServiceQual> listAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception;
    AeaImServiceQual getAeaImServiceQualById(@Param("id") String id) throws Exception;
    public List<AeaImServiceQual> getAeaImServiceQualListByServiceId(String serviceId) throws Exception;
    public void deleteServiceQualByServiceId(@Param("serviceId") String id, @Param("qualId") String qualId) throws Exception;
}

