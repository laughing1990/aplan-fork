package com.augurit.aplanmis.front.solicit.service;

import com.augurit.agcloud.opus.common.domain.OpuOmOrg;

import java.util.List;

/**
 * @author:chendx
 * @date: 2019-12-23
 * @time: 13:54
 */
public interface RestAeaHiSolicitService {
    /**
     * 获取顶级组织或者根据父组织获取子组织
     * @param isRoot 是否为根组织，1表示是，0表示否
     * @param parentOrgId 父组织ID，当isRoot=0时，非空
     * @return
     * @throws Exception
     */
    public List<OpuOmOrg> listOrg(String isRoot,String parentOrgId) throws Exception;
}
