package com.augurit.aplanmis.common.service.admin.credit.impl;

import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaCreditUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaCreditUnitInfoMapper;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditUnitInfoService;
import com.augurit.aplanmis.common.utils.GeneratePasswordUtils;
import com.augurit.aplanmis.common.utils.Md5Utils;
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

/**
 * 单位表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaCreditUnitInfoServiceImpl implements AeaCreditUnitInfoService {

    private static Logger logger = LoggerFactory.getLogger(AeaCreditUnitInfoServiceImpl.class);

    @Autowired
    private AeaCreditUnitInfoMapper aeaCreditUnitInfoMapper;

    @Override
    public void saveAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo) {

        if (StringUtils.isBlank(aeaCreditUnitInfo.getLoginName())) {
            //证照号作为登录名
            aeaCreditUnitInfo.setLoginName(aeaCreditUnitInfo.getUnifiedSocialCreditCode());
        }
        if (StringUtils.isBlank(aeaCreditUnitInfo.getLoginPwd())) {
            aeaCreditUnitInfo.setLoginPwd(Md5Utils.encrypt32(GeneratePasswordUtils.generatePassword(8)));
        }else {
            aeaCreditUnitInfo.setLoginPwd(Md5Utils.encrypt32(aeaCreditUnitInfo.getLoginPwd()));
        }
        aeaCreditUnitInfo.setCreater(SecurityContext.getCurrentUserId());
        aeaCreditUnitInfo.setCreateTime(new Date());
        aeaCreditUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaCreditUnitInfo.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaCreditUnitInfo.setUnitInfoSeq(getUnitSeq(aeaCreditUnitInfo));

        aeaCreditUnitInfoMapper.insertAeaCreditUnitInfo(aeaCreditUnitInfo);
    }

    @Override
    public void updateAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo) {

        aeaCreditUnitInfo.setModifier(SecurityContext.getCurrentUserId());
        aeaCreditUnitInfo.setModifyTime(new Date());
        if (StringUtils.isNotBlank(aeaCreditUnitInfo.getLoginPwd())) {
            aeaCreditUnitInfo.setLoginPwd(Md5Utils.encrypt32(aeaCreditUnitInfo.getLoginPwd()));
        }
        aeaCreditUnitInfoMapper.updateAeaCreditUnitInfo(aeaCreditUnitInfo);
    }

    @Override
    public void deleteAeaCreditUnitInfoById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaCreditUnitInfoMapper.deleteAeaCreditUnitInfo(id, SecurityContext.getCurrentOrgId());
    }

    @Override
    public PageInfo<AeaCreditUnitInfo> listAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo, Page page) {

        PageHelper.startPage(page);
        aeaCreditUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaCreditUnitInfo> list = aeaCreditUnitInfoMapper.listAeaCreditUnitInfo(aeaCreditUnitInfo);
        //不传密码到前端
        list.forEach(item -> {
            item.setLoginPwd(null);
        });
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaCreditUnitInfo>(list);
    }

    @Override
    public PageInfo<AeaCreditUnitInfo> listAeaCreditUnitInfoWithGlobalUnit(AeaCreditUnitInfo aeaCreditUnitInfo, Page page) {

        PageHelper.startPage(page);
        aeaCreditUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaCreditUnitInfo> list = aeaCreditUnitInfoMapper.listAeaCreditUnitInfoWithGlobalUnit(aeaCreditUnitInfo);
        //不传密码到前端
        list.forEach(item -> {
            item.setLoginPwd(null);
        });
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaCreditUnitInfo>(list);
    }

    @Override
    public AeaCreditUnitInfo getAeaCreditUnitInfoById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        AeaCreditUnitInfo aeaCreditUnitInfo = aeaCreditUnitInfoMapper
                .getAeaCreditUnitInfoWithAeaUnitById(id, SecurityContext.getCurrentOrgId());
        aeaCreditUnitInfo.setLoginPwd(null);

        return aeaCreditUnitInfo;
    }

    @Override
    public List<AeaCreditUnitInfo> listAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo) {

        List<AeaCreditUnitInfo> list = aeaCreditUnitInfoMapper.listAeaCreditUnitInfo(aeaCreditUnitInfo);
        //不传密码到前端
        list.forEach(item -> {
            item.setLoginPwd(null);
        });
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<ElementUiRsTreeNode> gtreeUnitInfoForEui(String keyword, String currentOrgId) {
        List<ElementUiRsTreeNode> allNodes = new ArrayList<ElementUiRsTreeNode>();
        ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
        rootNode.setId("root");
        rootNode.setLabel("全局单位库");
        rootNode.setType("root");
        AeaCreditUnitInfo aeaCreditUnitInfo = new AeaCreditUnitInfo();
        aeaCreditUnitInfo.setKeyword(keyword);
        aeaCreditUnitInfo.setRootOrgId(currentOrgId);
        List<AeaCreditUnitInfo> list = aeaCreditUnitInfoMapper.listAeaCreditUnitInfo(aeaCreditUnitInfo);
        if (list != null && list.size() > 0) {
            ElementUiRsTreeNode node;
            for (AeaCreditUnitInfo item : list) {
                node = new ElementUiRsTreeNode();
                node.setId(item.getCreditUnitInfoId());
                node.setLabel(item.getApplicant());
                node.setType("credit_unit");
                node.setData(item.getUnitInfoId());
                rootNode.getChildren().add(node);
            }
        }
        allNodes.add(rootNode);
        return allNodes;
    }

    @Override
    public void batchDelSelfAndAllChildById(String id) {

        aeaCreditUnitInfoMapper.batchDelSelfAndAllChildById(id, SecurityContext.getCurrentOrgId());
    }

    @Override
    public void batchDelSelfAndAllChildByIds(String[] ids) {

        for (int i = 0; i < ids.length; i++) {
            batchDelSelfAndAllChildById(ids[i]);
        }
    }

    private String getUnitSeq(AeaCreditUnitInfo aeaCreditUnitInfo) {

        String parentId = aeaCreditUnitInfo.getParentId();
        String rootOrgId = aeaCreditUnitInfo.getRootOrgId();
        String suffix = aeaCreditUnitInfo.getCreditUnitInfoId() + CommonConstant.SEQ_SEPARATOR;
        if (StringUtils.isBlank(parentId)) {
            return CommonConstant.SEQ_SEPARATOR + suffix;
        }
        //获取上级的序列
        AeaCreditUnitInfo acui = aeaCreditUnitInfoMapper.getAeaCreditUnitInfoById(parentId, rootOrgId);
        if (acui == null) {
            return CommonConstant.SEQ_SEPARATOR + suffix;
        }
        String parentSeq = acui.getUnitInfoSeq();
        return StringUtils.isBlank(parentSeq) ? CommonConstant.SEQ_SEPARATOR + suffix : parentSeq + suffix;

    }
}

