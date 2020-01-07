package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.aplanmis.mall.userCenter.vo.ProjStatusTreeVo;

import javax.servlet.http.HttpServletRequest;


public interface RestApplyProjService {

    /**
     * 查询我的项目工程管理获取项目状态树
     * @param projInfoId
     * @param request
     * @return
     * @throws Exception
     */
    ProjStatusTreeVo getProjStatusTreeVo(String projInfoId, HttpServletRequest request)throws Exception;
}
