package com.augurit.aplanmis.common.service.admin.solicit.impl;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import com.augurit.aplanmis.common.mapper.AeaSolicitOrgMapper;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* 按组织征求配置表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaSolicitOrgServiceImpl implements AeaSolicitOrgService {

    private static Logger logger = LoggerFactory.getLogger(AeaSolicitOrgServiceImpl.class);

    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;

    @Autowired
    private AeaSolicitOrgMapper aeaSolicitOrgMapper;

    @Override
    public void saveAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) {

        aeaSolicitOrg.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaSolicitOrg.setCreater(SecurityContext.getCurrentUserId());
        aeaSolicitOrg.setCreateTime(new Date());
        aeaSolicitOrgMapper.insertAeaSolicitOrg(aeaSolicitOrg);
    }

    @Override
    public void updateAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) {

        aeaSolicitOrg.setModifier(SecurityContext.getCurrentUserId());
        aeaSolicitOrg.setModifyTime(new Date());
        aeaSolicitOrgMapper.updateAeaSolicitOrg(aeaSolicitOrg);
    }

    @Override
    public void deleteAeaSolicitOrgById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaSolicitOrgMapper.deleteAeaSolicitOrg(id);
    }

    @Override
    public void batchDelSolicitOrgByIds(String[] ids){

        if(ids!=null&&ids.length>0){
            aeaSolicitOrgMapper.batchDelSolicitOrgByIds(ids);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public PageInfo<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg,Page page) {

        aeaSolicitOrg.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaSolicitOrg> list = aeaSolicitOrgMapper.listAeaSolicitOrg(aeaSolicitOrg);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitOrg>(list);
    }

    @Override
    public AeaSolicitOrg getAeaSolicitOrgById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitOrgMapper.getAeaSolicitOrgById(id);
    }

    @Override
    public AeaSolicitOrg getSolicitOrgRelOrgInfoById(String id){

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitOrgMapper.getSolicitOrgRelOrgInfoById(id);
    }

    @Override
    public List<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) {

        aeaSolicitOrg.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaSolicitOrg> list = aeaSolicitOrgMapper.listAeaSolicitOrg(aeaSolicitOrg);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaSolicitOrg> listAeaSolicitOrgRelOrgInfo(AeaSolicitOrg aeaSolicitOrg,Page page) {

        aeaSolicitOrg.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaSolicitOrg> list = aeaSolicitOrgMapper.listAeaSolicitOrgRelOrgInfo(aeaSolicitOrg);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitOrg>(list);
    }

    @Override
    public List<AeaSolicitOrg> listAeaSolicitOrgRelOrgInfo(AeaSolicitOrg aeaSolicitOrg) {

        aeaSolicitOrg.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaSolicitOrg> list = aeaSolicitOrgMapper.listAeaSolicitOrgRelOrgInfo(aeaSolicitOrg);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void batchSaveSolicitOrg(String isBusSolicit, String stageId, String busType, String solicitType, String[] orgIds){

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        if(orgIds!=null&&orgIds.length>0) {
            AeaSolicitOrg sSolicitOrg = new AeaSolicitOrg();
            sSolicitOrg.setRootOrgId(rootOrgId);
            for (String orgId:orgIds) {
                sSolicitOrg.setOrgId(orgId);
                sSolicitOrg.setIsBusSolicit(isBusSolicit);
                // 业务征求方式
                if(isBusSolicit.equals(Status.ON)){
                    sSolicitOrg.setBusType(busType);
                // 阶段牵头部门征求方式
                }else{
                    sSolicitOrg.setStageId(stageId);
                }
                List<AeaSolicitOrg> orgList = aeaSolicitOrgMapper.listAeaSolicitOrg(sSolicitOrg);
                if(orgList!=null&&orgList.size()>0){
                    for(AeaSolicitOrg org:orgList){
                        org.setSolicitType(solicitType);
                        org.setModifier(userId);
                        org.setModifyTime(new Date());
                        aeaSolicitOrgMapper.updateAeaSolicitOrg(org);
                    }
                }else{
                    sSolicitOrg.setSolicitOrgId(UUID.randomUUID().toString());
                    sSolicitOrg.setSolicitType(solicitType);
                    sSolicitOrg.setCreater(userId);
                    sSolicitOrg.setCreateTime(new Date());
                    sSolicitOrg.setRootOrgId(rootOrgId);
                    aeaSolicitOrgMapper.insertAeaSolicitOrg(sSolicitOrg);
                }
            }
        }
    }

    @Override
    public void batchDelSolicitOrgByRootOrgId(String rootOrgId){

        if(StringUtils.isNotBlank(rootOrgId)){
            aeaSolicitOrgMapper.batchDelSolicitOrgByRootOrgId(rootOrgId);
        }
    }

    @Override
    public List<ZtreeNode> gtreeSolicitOrg(String rootOrgId){

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        List<ZtreeNode> allNodes = new ArrayList<>();
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("征求部门");
        rootNode.setType("root");
        rootNode.setOpen(true);
        rootNode.setIsParent(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);

        AeaSolicitOrg sSolicitOrg = new AeaSolicitOrg();
        sSolicitOrg.setRootOrgId(rootOrgId);
        sSolicitOrg.setIsBusSolicit(Status.ON);
        List<AeaSolicitOrg> list = aeaSolicitOrgMapper.listAeaSolicitOrgRelOrgInfo(sSolicitOrg);
        if(list!=null&&list.size()>0){
            List<BscDicCodeItem> codeItemList = bscDicCodeMapper.getActiveItemsByTypeCode("SOLICIT_BUS_TYPE", rootOrgId);
            for(AeaSolicitOrg org : list){
                allNodes.add(convertSolicitOrgNode(org, codeItemList));
            }
        }
        return allNodes;
    }

    private ZtreeNode convertSolicitOrgNode(AeaSolicitOrg org, List<BscDicCodeItem> codeItemList){

        ZtreeNode node = new ZtreeNode();
        node.setId(org.getSolicitOrgId());
        if(codeItemList!=null&&codeItemList.size()>0){
            for(BscDicCodeItem codeItem:codeItemList){
                if(codeItem.getItemCode().equals(org.getBusType())){
                    org.setOrgName(org.getOrgName() + "【" + codeItem.getItemName() + "】");
                }
            }
        }
        node.setName(org.getOrgName());
        node.setpId("root");
        node.setType("org");
        node.setOpen(true);
        node.setIsParent(false);
        node.setNocheck(false);
        String orgProperty = org.getOrgProperty();
        if ("u".equals(orgProperty)) {
            node.setIconSkin("unit");
        } else if ("d".equals(orgProperty)) {
            node.setIconSkin("department");
        } else if ("g".equals(orgProperty)) {
            node.setIconSkin("group");
        }
        return node;
    }
}

