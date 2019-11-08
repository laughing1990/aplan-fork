package com.augurit.aplanmis.common.service.admin.credit.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaCreditRedblack;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaCreditRedblackMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditRedblackService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
* 信用管理-红黑名单管理-Service服务接口实现类
*/
@Service
@Transactional
public class AeaCreditRedblackServiceImpl implements AeaCreditRedblackService {

    private static Logger logger = LoggerFactory.getLogger(AeaCreditRedblackServiceImpl.class);

    @Autowired
    private AeaCreditRedblackMapper aeaCreditRedblackMapper;

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Override
    public void saveAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack) {

        aeaCreditRedblack.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaCreditRedblack.setCreater(SecurityContext.getCurrentUserId());
        aeaCreditRedblack.setCreateTime(new Date());
        aeaCreditRedblackMapper.insertAeaCreditRedblack(aeaCreditRedblack);
    }

    @Override
    public void updateAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack) {

        aeaCreditRedblack.setModifier(SecurityContext.getCurrentUserId());
        aeaCreditRedblack.setModifyTime(new Date());
        aeaCreditRedblackMapper.updateAeaCreditRedblack(aeaCreditRedblack);
    }

    @Override
    public void deleteAeaCreditRedblackById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaCreditRedblackMapper.deleteAeaCreditRedblack(id, SecurityContext.getCurrentOrgId());
    }

    @Override
    public List<AeaCreditRedblack> listAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack) {

        aeaCreditRedblack.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaCreditRedblack> list = aeaCreditRedblackMapper.listAeaCreditRedblack(aeaCreditRedblack);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaCreditRedblack> listAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack, Page page) {

        aeaCreditRedblack.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCreditRedblack> list = aeaCreditRedblackMapper.listAeaCreditRedblack(aeaCreditRedblack);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaCreditRedblack>(list);
    }

    @Override
    public List<AeaCreditRedblack> listAeaCreditRedblackRelInfo(AeaCreditRedblack aeaCreditRedblack) {

        aeaCreditRedblack.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaCreditRedblack> list = aeaCreditRedblackMapper.listAeaCreditRedblackRelInfo(aeaCreditRedblack);
        logger.debug("成功执行分页查询！！");
        return list;
    }

    @Override
    public PageInfo<AeaCreditRedblack> listAeaCreditRedblackRelInfo(AeaCreditRedblack aeaCreditRedblack, Page page) {

        aeaCreditRedblack.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCreditRedblack> list = aeaCreditRedblackMapper.listAeaCreditRedblackRelInfo(aeaCreditRedblack);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaCreditRedblack>(list);
    }

    @Override
    public AeaCreditRedblack getAeaCreditRedblackById(String id) {
        
        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaCreditRedblackMapper.getAeaCreditRedblackById(id, SecurityContext.getCurrentOrgId());
    }

    @Override
    public List<ElementUiRsTreeNode> gtreeLinkmanForEui(String keyword, String rootOrgId) {

        List<ElementUiRsTreeNode> allNodes = new ArrayList<ElementUiRsTreeNode>();
        ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
        rootNode.setId("root");
        rootNode.setLabel("全局联系人库");
        rootNode.setType("root");
        List<AeaLinkmanInfo> list = aeaLinkmanInfoMapper.findLinkmanInfoByKeyword(keyword, SecurityContext.getCurrentOrgId());
        if(list!=null&&list.size()>0){
            ElementUiRsTreeNode node;
            for(AeaLinkmanInfo item : list){
                node = new ElementUiRsTreeNode();
                node.setId(item.getLinkmanInfoId());
                node.setLabel(item.getLinkmanName());
                node.setType("linkman");
                node.setData(item.getLinkmanInfoId());
                rootNode.getChildren().add(node);
            }
        }
        allNodes.add(rootNode);
        return allNodes;
    }

    @Override
    public List<ElementUiRsTreeNode> gtreeUnitInfoForEui(String keyword, String rootOrgId) {

        List<ElementUiRsTreeNode> allNodes = new ArrayList<ElementUiRsTreeNode>();
        ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
        rootNode.setId("root");
        rootNode.setLabel("全局单位库");
        rootNode.setType("root");
        List<AeaUnitInfo> list = aeaUnitInfoMapper.findAeaUnitInfoByKeyword(keyword, SecurityContext.getCurrentOrgId());
        if(list!=null&&list.size()>0){
            ElementUiRsTreeNode node;
            for(AeaUnitInfo item:list){
                node = new ElementUiRsTreeNode();
                node.setId(item.getUnitInfoId());
                node.setLabel(item.getApplicant());
                node.setType("unit");
                node.setData(item.getUnitInfoId());
                rootNode.getChildren().add(node);
            }
        }
        allNodes.add(rootNode);
        return allNodes;
    }

    @Override
    public void enOrDisableIsValid(String redblackId){

        aeaCreditRedblackMapper.enOrDisableIsValid(redblackId);
    }

    @Override
    public List<AeaCreditRedblack> listPersonOrUnitBlackByBizId(String bizType, String bizId){

        if(StringUtils.isBlank(bizType)){
            throw new InvalidParameterException("参数bizType为空!");
        }
        if(StringUtils.isBlank(bizId)){
            throw new InvalidParameterException("参数bizId为空!");
        }
        AeaCreditRedblack redblack = new AeaCreditRedblack();
        redblack.setIsBlack(Status.ON);
        redblack.setIsValid(Status.ON);
        redblack.setRedblackType(bizType);
        if(bizType.equals("u")){
            redblack.setUnitInfoId(bizId);
        }else if(bizType.equals("l")){
            redblack.setLinkmanInfoId(bizId);
        }
        redblack.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaCreditRedblackMapper.listAeaCreditRedblack(redblack);
    }

    @Override
    public List<AeaCreditRedblack> listPersonOrUnitBlackByBizCode(String bizType, String bizCode){

        if(StringUtils.isBlank(bizType)){
            throw new InvalidParameterException("参数bizType为空!");
        }
        if(StringUtils.isBlank(bizCode)){
            throw new InvalidParameterException("参数bizCode为空!");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaCreditRedblack redblack = new AeaCreditRedblack();
        redblack.setIsBlack(Status.ON);
        redblack.setIsValid(Status.ON);
        redblack.setRedblackType(bizType);
        if(bizType.equals("u")){
            AeaUnitInfo unit = new AeaUnitInfo();
            unit.setRootOrgId(rootOrgId);
            unit.setUnifiedSocialCreditCode(bizCode);
            List<AeaUnitInfo> list = aeaUnitInfoMapper.listAeaUnitInfo(unit);
            if(list!=null&&list.size()>0){
                redblack.setUnitInfoId(list.get(0).getUnitInfoId());
            }else{
                throw new InvalidParameterException("无法在全局单位库中通过统一社会信用代码查询到单位!");
            }
        }else if(bizType.equals("l")){
            AeaLinkmanInfo linkman = new AeaLinkmanInfo();
            linkman.setLinkmanCertNo(bizCode);
            linkman.setRootOrgId(rootOrgId);
            List<AeaLinkmanInfo> list = aeaLinkmanInfoMapper.findAeaLinkmanInfo(linkman);
            if(list!=null&&list.size()>0){
                redblack.setLinkmanInfoId(list.get(0).getLinkmanInfoId());
            }else{
                throw new InvalidParameterException("无法在全局联系人库中通过身份证号查询到联系人!");
            }
        }
        redblack.setRootOrgId(rootOrgId);
        return aeaCreditRedblackMapper.listAeaCreditRedblack(redblack);
    }
}

