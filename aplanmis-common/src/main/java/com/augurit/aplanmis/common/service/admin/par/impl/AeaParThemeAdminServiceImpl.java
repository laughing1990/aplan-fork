package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeSeq;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.mapper.AeaParThemeMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeSeqMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeVerMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeAdminService;
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
* 主题表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaParThemeAdminServiceImpl implements AeaParThemeAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParThemeAdminServiceImpl.class);

    @Autowired
    private AeaParThemeMapper themeMapper;

    @Autowired
    private AeaParThemeVerMapper themeVerMapper;

    @Autowired
    private AeaParThemeSeqMapper themeSeqMapper;

    public AeaParThemeVer initAeaParThemeVer(String themeId, String userId, String rootOrgId) {

        if(StringUtils.isBlank(themeId)){
            throw  new IllegalArgumentException("主题themeId不能为空!");
        }

        // 创建版本
        AeaParThemeVer themeVer = AeaParThemeVer.initOne(themeId, userId, rootOrgId);
        themeVerMapper.insertOne(themeVer);

        // 创建序号
        AeaParThemeSeq themeSeq = AeaParThemeSeq.initOne(themeId, userId, rootOrgId);
        themeSeqMapper.insertOne(themeSeq);
        return themeVer;
    }

    @Override
    public void saveAeaParTheme(AeaParTheme aeaParTheme) {

        String userId = SecurityContext.getCurrentUserId();
        aeaParTheme.setSortNo(getMaxSortNo(aeaParTheme.getRootOrgId()));
        aeaParTheme.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaParTheme.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaParTheme.setCreater(userId);
        aeaParTheme.setCreateTime(new Date());
        themeMapper.insertOne(aeaParTheme);

        // 初始化版本序列
        initAeaParThemeVer(aeaParTheme.getThemeId(), userId, aeaParTheme.getRootOrgId());
    }

    @Override
    public void updateAeaParTheme(AeaParTheme aeaParTheme) {

        aeaParTheme.setModifier(SecurityContext.getCurrentUserId());
        aeaParTheme.setModifyTime(new Date());
        themeMapper.updateOne(aeaParTheme);
    }

    @Override
    public void deleteAeaParThemeById(String id) {

        if(StringUtils.isNotBlank(id)){
            themeMapper.deleteById(id);
        }
    }

    @Override
    public PageInfo<AeaParTheme> listAeaParTheme(AeaParTheme aeaParTheme, Page page) {

        aeaParTheme.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParTheme> list = themeMapper.listAeaParTheme(aeaParTheme);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParTheme>(list);
    }

    @Override
    public AeaParTheme getAeaParThemeById(String id) {

        if(StringUtils.isNotBlank(id)){
            logger.debug("根据ID获取Form对象，ID为：{}", id);
            return themeMapper.selectOneById(id);
        }
        return null;
    }

    @Override
    public List<AeaParTheme> listAeaParTheme(AeaParTheme aeaParTheme){

        aeaParTheme.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParTheme.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        List<AeaParTheme> list = themeMapper.listAeaParTheme(aeaParTheme);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public Long getMaxSortNo(String rootOrgId){

        Long sortNo = themeMapper.getMaxSortNo(rootOrgId);
        return sortNo==null?new Long(1):sortNo+1;
    }

    @Override
    public List<ZtreeNode> gtreeTheme(AeaParTheme theme){

        List<ZtreeNode> allNodes = new ArrayList<>();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        // 根节点
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("主题库");
        rootNode.setType("root");
        rootNode.setIsParent(true);
        rootNode.setNocheck(true);
        rootNode.setOpen(true);
        allNodes.add(rootNode);
        // 查询存在可用版本的主题
        List<AeaParThemeVer> verList = themeVerMapper.listOkVerNoRelDiagramInfo(rootOrgId);
        if (verList != null && verList.size() > 0) {
            for (AeaParThemeVer themeVer : verList) {
                ZtreeNode node = new ZtreeNode();
                node.setId(themeVer.getThemeId());
                node.setName(themeVer.getThemeName());
                node.setpId("root");
                node.setType("theme");
                node.setIsParent(false);
                node.setNocheck(false);
                node.setOpen(true);
                allNodes.add(node);
            }
        }
        return allNodes;
    }

    @Override
    public List<ElementUiRsTreeNode> gtreeThemeForEUi(AeaParTheme theme){

        List<ElementUiRsTreeNode> allNodes = new ArrayList<>();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
        rootNode.setId("root");
        rootNode.setLabel("主题库");
        rootNode.setType("root");
        // 查询存在可用版本的主题
        List<AeaParThemeVer> verList = themeVerMapper.listOkVerNoRelDiagramInfo(rootOrgId);
        if (verList != null && verList.size() > 0) {
            for (AeaParThemeVer themeVer : verList) {
                ElementUiRsTreeNode node = new ElementUiRsTreeNode();
                node.setId(themeVer.getThemeId());
                node.setLabel(themeVer.getThemeName());
                node.setType("theme");
                rootNode.getChildren().add(node);
            }
        }
        allNodes.add(rootNode);
        return allNodes;
    }

    @Override
    public void batchSortThemes(String[] themeIds, Long[] sortNos){

        if(themeIds!=null&&themeIds.length>0&&sortNos!=null&&sortNos.length>0){
            AeaParTheme theme;
           for(int i=0;i<themeIds.length;i++){
               theme = new AeaParTheme();
               theme.setThemeId(themeIds[i]);
               theme.setSortNo(sortNos[i]);
               updateAeaParTheme(theme);
           }
        }
    }
}

