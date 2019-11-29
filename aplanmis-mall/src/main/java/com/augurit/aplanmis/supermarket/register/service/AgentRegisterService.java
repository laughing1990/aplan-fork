package com.augurit.aplanmis.supermarket.register.service;

import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.aplanmis.common.vo.AgentRegisterVo;

import javax.servlet.http.HttpServletRequest;

public interface AgentRegisterService {
    /**
     * 新建单位
     *
     * @param agentRegisterVo
     */
    void saveAgentRegisterInfo(AgentRegisterVo agentRegisterVo, HttpServletRequest request) throws Exception;

    void generateVirtualUser(OpuOmUser opuOmUser, String orgId);
}
