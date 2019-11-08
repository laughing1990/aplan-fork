package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImCertinstMajor;
import com.augurit.aplanmis.common.domain.AeaImQual;
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
    <li>创建人:Administrator</li>
    <li>创建时间：2019-06-11 10:44:10</li>
</ul>
*/
@Mapper
@Repository
public interface AeaImCertinstMajorMapper {

public void insertAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor) throws Exception;
public void updateAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor) throws Exception;
public void deleteAeaImCertinstMajor(@Param("id") String id) throws Exception;
public List <AeaImCertinstMajor> listAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor) throws Exception;
public AeaImCertinstMajor getAeaImCertinstMajorById(@Param("id") String id) throws Exception;

    void deleteAeaImCertinstMajorByCertinstId(@Param("certinstId") String certinstId);

    List <AeaImCertinstMajor> listCertinstMajorByUnitInfoId(@Param("unitInfoId") String unitInfoId);

    List <AeaImQual> listQualMajorByCertinstId(@Param("certinstId") String certinstId);

}
