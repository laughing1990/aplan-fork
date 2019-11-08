package com.augurit.aplanmis.common.mapper;
import com.augurit.aplanmis.common.domain.AeaImServiceLinkman;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* -中介服务与联系人关联Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaImServiceLinkmanMapper {

public void insertAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;
public void updateAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;
public void deleteAeaImServiceLinkman(@Param("id") String id) throws Exception;
public List <AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;
public AeaImServiceLinkman getAeaImServiceLinkmanById(@Param("id") String id) throws Exception;
public AeaImServiceLinkman getAeaImServiceLinkmanByServiceId(@Param("serviceId") String serviceId, @Param("linkmanInfoId") String linkmanInfoId) throws Exception;
public List <AeaLinkmanInfo> listAeaImServiceLinkmanByServiceId(@Param("serviceId") String serviceId) throws Exception;
public List <AeaLinkmanInfo> listAeaImServiceLinkmanByUnitInfoId(@Param("unitInfoId") String unitInfoId, @Param("cardNo") String cardNo, @Param("auditFlag") String auditFlag) throws Exception;
public AeaImServiceLinkman getAeaImServiceLinkmanDetailById(@Param("serviceLinkmanId") String serviceLinkmanId) throws Exception;
}
