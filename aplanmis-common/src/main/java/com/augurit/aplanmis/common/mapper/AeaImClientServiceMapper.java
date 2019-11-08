package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImClientService;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
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
 * <li>创建人:Augurit</li>
 * <li>创建时间：2019-07-09 10:20:43</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaImClientServiceMapper {

    public void insertAeaImClientService(AeaImClientService aeaImClientService) throws Exception;

    public void updateAeaImClientService(AeaImClientService aeaImClientService) throws Exception;

    public void deleteAeaImClientService(@Param("id") String id) throws Exception;

    public List<AeaImClientService> listAeaImClientService(AeaImClientService aeaImClientService) throws Exception;

    public AeaImClientService getAeaImClientServiceById(@Param("id") String id) throws Exception;

    void deleteAeaImClientServiceByUnitLinkmanId(@Param("unitLinkmanId") String unitLinkmanId) throws Exception;

    List<AeaLinkmanInfo> listClientServiceLinkmanInfo(@Param("serviceId") String serviceId, @Param("unitInfoId") String unitInfoId) throws Exception;
}
