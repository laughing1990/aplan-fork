package com.augurit.aplanmis.thirdPlatform.service;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.vo.ProUnitLinkVo;

import java.util.List;


public interface ProjectCodeService {

    ProUnitLinkVo getProjectInfo(String requestUrl, String username, String password, String projectCode);

    /**
     * @param projCode
     * @param unitName 用于校验，可为空
     * @return
     * @throws Exception
     */
    List<AeaProjInfo> getProjInfoFromThirdPlatform(String projCode, String unitName) throws Exception;

    void saveProjUnitLinkmanInfo(ProUnitLinkVo proUnitLinkVo) throws Exception;

}
