package com.augurit.aplanmis.admin.market.register.service;

import com.augurit.aplanmis.admin.market.register.vo.OwnerRegisterResultVo;
import com.augurit.aplanmis.admin.market.register.vo.RegisterAuditVo;
import com.augurit.aplanmis.admin.market.register.vo.RegisterResultVo;
import com.augurit.aplanmis.admin.market.register.vo.RegisterSearch;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.github.pagehelper.Page;

import java.util.List;

public interface RegisterService {
    List<AeaUnitInfo> getRegisterUnitList(RegisterSearch registerSearch, Page page) throws Exception;

    RegisterResultVo getRegisterUnitDetail(String unitInfoId) throws Exception;

    void examineService(RegisterAuditVo registerAuditVo) throws Exception;

    OwnerRegisterResultVo getOwnerRegisterDetail(String unitInfoId) throws Exception;

    void examineOwnerUnitService(RegisterAuditVo registerAuditVo) throws Exception;

    /**
     * 删除||启用入住机构
     *
     * @param unitInfoId 机构ID
     */
    void deleteOrEnableAgentUnit(String unitInfoId, String isDeleted) throws Exception;

}
