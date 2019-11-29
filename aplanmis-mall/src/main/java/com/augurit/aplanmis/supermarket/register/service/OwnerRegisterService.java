package com.augurit.aplanmis.supermarket.register.service;

import com.augurit.aplanmis.common.vo.OwnerRegisterVo;

import javax.servlet.http.HttpServletRequest;

public interface OwnerRegisterService {
    /**
     * 新建单位
     *
     * @param agentRegisterVo
     */
    void saveAgentRegisterInfo(OwnerRegisterVo agentRegisterVo, HttpServletRequest request) throws Exception;
}
