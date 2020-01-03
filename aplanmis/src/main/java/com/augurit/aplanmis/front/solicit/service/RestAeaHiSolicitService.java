package com.augurit.aplanmis.front.solicit.service;

import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import com.augurit.aplanmis.common.domain.AeaHiSolicitDetailUser;
import com.augurit.aplanmis.common.vo.solicit.AeaHiSolicitVo;
import com.augurit.aplanmis.common.vo.solicit.QueryCondVo;
import com.augurit.aplanmis.front.solicit.vo.AeaHiSolicitInfo;
import com.github.pagehelper.Page;

import javax.servlet.http.HttpServletRequest;
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
     * @param condVo 查询条件
     * @param page   分页参数
     * @return List<SolicitListVo>
     */
    List<AeaHiSolicitVo> listSolicit(QueryCondVo condVo, Page page) throws Exception;

    public void createSolicit(AeaHiSolicit aeaHiSolicit, String type, String detailInfo, String busType) throws Exception;

    public void createSolicitOpinion(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception;

    public void createSolicitCollectOpinion(AeaHiSolicit aeaHiSolicit) throws Exception;

    /**
     * 根据申报编号获取所有的征求信息
     *
     * @param applyinstId 申报实例ID
     * @param busType     业务类型(来自数据字典)
     * @return
     * @throws Exception
     */
    public List<AeaHiSolicitInfo> listAeaHiSolicitByApplyinstId(String applyinstId, String busType) throws Exception;

    /**
     * 签收功能
     *
     * @param detailUserId 签收人主键
     * @throws Exception e
     */
    void signSolicitDetail(String detailUserId) throws Exception;

    public List<AeaHiIteminst> getApplyItems(String applyinstId) throws Exception;

    public String uploadAttFile(String solicitId, String tableName,String pkName, HttpServletRequest request) throws Exception;

    public OpuOmUser getCurrentUser() throws Exception;
}
