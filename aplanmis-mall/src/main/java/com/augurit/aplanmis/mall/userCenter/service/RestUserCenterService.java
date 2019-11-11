package com.augurit.aplanmis.mall.userCenter.service;


import com.augurit.aplanmis.common.domain.AeaProjInfo;

import javax.servlet.http.HttpServletRequest;

public interface RestUserCenterService {


    String saveChildProject(HttpServletRequest request, AeaProjInfo aeaProjInfo) throws Exception;

    public String saveProjectInfo(HttpServletRequest request, AeaProjInfo aeaProjInfo) throws Exception;

    /**
     * 撤回办件,即将申报实例记录标志为逻辑删除
     * @param applyInstId
     */
    void withDrawProject(String applyInstId) throws Exception;

}
