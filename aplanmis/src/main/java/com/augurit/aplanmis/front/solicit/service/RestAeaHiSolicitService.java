package com.augurit.aplanmis.front.solicit.service;

import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import com.augurit.aplanmis.front.solicit.vo.SolicitListVo;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author:chendx
 * @date: 2019-12-23
 * @time: 13:54
 */
public interface RestAeaHiSolicitService {
    /**
     * 获取顶级组织或者根据父组织获取子组织
     *
     * @param isRoot      是否为根组织，1表示是，0表示否
     * @param parentOrgId 父组织ID，当isRoot=0时，非空
     * @return
     * @throws Exception
     */
    public List<OpuOmOrg> listOrg(String isRoot, String parentOrgId) throws Exception;

    /**
     * 意见征求列表
     *
     * @param type 查询类型，0 意见征求，1 一次征求，2 联合评审 ...
     * @param page 分页参数
     * @return List<SolicitListVo>
     */
    List<SolicitListVo> listSolicit(String type, Page page) throws Exception;

    public void createSolicit(AeaHiSolicit aeaHiSolicit, String type, String detailInfo,String busType) throws Exception;

}
