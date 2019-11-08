package com.augurit.aplanmis.common.service.window.impl;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowStageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author yinlf
 * @Date 2019/7/29
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AeaServiceWindowStageServiceImpl implements AeaServiceWindowStageService {

    private static Logger logger = LoggerFactory.getLogger(AeaServiceWindowStageServiceImpl.class);

    @Autowired
    AeaServiceWindowStageMapper aeaServiceWindowStageMapper;

    @Autowired
    AeaServiceWindowService aeaServiceWindowService;

    @Autowired
    AeaParThemeService aeaParThemeService;

    @Autowired
    AeaParStageMapper aeaParStageMapper;

    @Override
    public String findCurrentUserAeaServiceWindowStage(String stageId) {
        AeaServiceWindow currentUserWindow = aeaServiceWindowService.getCurrentUserWindow();
        if(currentUserWindow!=null&&"1".equals(currentUserWindow.getIsAllStage())){
            return currentUserWindow.getRegionRange();
        }
        logger.debug("当前人员所在窗口下阶段是否属地办理");
        String currentUserId = SecurityContext.getCurrentUserId();
        String currentOrgId = SecurityContext.getCurrentOrgId();
        List<AeaServiceWindowStage> aeaServiceWindowStageList = aeaServiceWindowStageMapper.findCurrentUserWindowStage(stageId,currentUserId,currentOrgId);
        if(aeaServiceWindowStageList!=null&&aeaServiceWindowStageList.size()>0){
            AeaServiceWindowStage aeaServiceWindowStage = aeaServiceWindowStageList.get(0);
            return aeaServiceWindowStage.getRegionRange()==null?"1": aeaServiceWindowStage.getRegionRange();
        }
        return "1";
    }

    @Override
    public List<AeaServiceWindowStage> findCurrentUserAeaServiceWindowStage() {
        logger.debug("当前人员所在窗口下阶段是否属地办理");
        String currentUserId = SecurityContext.getCurrentUserId();
        String currentOrgId = SecurityContext.getCurrentOrgId();
        List<AeaServiceWindowStage> aeaServiceWindowStageList = aeaServiceWindowStageMapper.findCurrentUserWindowStage(null,currentUserId,currentOrgId);
        return aeaServiceWindowStageList;
    }

    @Override
    public List<AeaParStage> findCurrentUserWindowStage() {

        logger.debug("查询当前窗口人员可办理阶段");
        String currentUserId = SecurityContext.getCurrentUserId();
        String currentOrgId = SecurityContext.getCurrentOrgId();
        List<AeaParStage> list = aeaServiceWindowStageMapper.findWindowStageByUserId(currentUserId,null,currentOrgId);
        return list;
    }

    @Override
    public boolean hasAuthOnCurrentUser(String... stageId) {

        logger.debug("当前窗口人员是否有办理阶段的权限");
        String currentUserId = SecurityContext.getCurrentUserId();
        String currentOrgId = SecurityContext.getCurrentOrgId();
        List<AeaParStage> list = aeaServiceWindowStageMapper.findWindowStageByUserId(currentUserId,null,currentOrgId);
        List<String> authStageList = new ArrayList<>();
        for (AeaParStage stage : list) {
            authStageList.add(stage.getStageId());
        }
        return authStageList.containsAll(Arrays.asList(stageId));
    }

    @Override
    public List<AeaParStage> findWindowStageByWindowId(String windowId) {

        logger.debug("查询窗口阶段");
        List<AeaParStage> list =  aeaServiceWindowStageMapper.findWindowStageByWindowIdAndKeywordAndIsActive(windowId,null,null);
        return list;
    }

    @Override
    public PageInfo<AeaParStage> listWindowStageByWindowId(String windowId, Page page) {

        logger.debug("分页查询窗口阶段");
        PageHelper.startPage(page);
        List<AeaParStage> list =  aeaServiceWindowStageMapper.findWindowStageByWindowIdAndKeywordAndIsActive(windowId,null,null);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaParStage> findWindowStageByKeyword(String windowId, String keyword) {

        logger.debug("搜索窗口可办理阶段（包含未启用阶段）");
        List<AeaParStage> list =  aeaServiceWindowStageMapper.findWindowStageByWindowIdAndKeywordAndIsActive(windowId,keyword,null);
        return list;
    }

    @Override
    public PageInfo<AeaParStage> listWindowStageByKeyword(String windowId, String keyword, Page page) {

        logger.debug("分页搜索窗口阶段（包含未启用阶段）");
        PageHelper.startPage(page);
        List<AeaParStage> list =  aeaServiceWindowStageMapper.findWindowStageByWindowIdAndKeywordAndIsActive(windowId,keyword,null);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaParStage> findActiveWindowStageByWindowId(String windowId) {

        logger.debug("查询窗口可办理阶段");
        List<AeaParStage> list =  aeaServiceWindowStageMapper.findWindowStageByWindowIdAndKeywordAndIsActive(windowId,null,ActiveStatus.ACTIVE.getValue());
        return list;
    }

    @Override
    public PageInfo<AeaParStage> listActiveWindowStageByWindowId(String windowId, Page page) {

        logger.debug("分页查询窗口可办理阶段");
        PageHelper.startPage(page);
        List<AeaParStage> list =  aeaServiceWindowStageMapper.findWindowStageByWindowIdAndKeywordAndIsActive(windowId,null,ActiveStatus.ACTIVE.getValue());
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaParStage> findActiveWindowStageByKeyword(String windowId, String keyword) {

        logger.debug("搜索窗口可办理阶段");
        List<AeaParStage> list =  aeaServiceWindowStageMapper.findWindowStageByWindowIdAndKeywordAndIsActive(windowId,keyword,ActiveStatus.ACTIVE.getValue());
        return list;
    }

    @Override
    public PageInfo<AeaParStage> listActiveWindowStageByKeyword(String windowId, String keyword, Page page) {

        logger.debug("分页搜索窗口可办理阶段");
        PageHelper.startPage(page);
        List<AeaParStage> list =  aeaServiceWindowStageMapper.findWindowStageByWindowIdAndKeywordAndIsActive(windowId,keyword,ActiveStatus.ACTIVE.getValue());
        return new PageInfo<>(list);
    }

    @Override
    public void delAeaServiceWindowStageById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaServiceWindowStageMapper.delAeaServiceWindowStageById(id);
    }

    @Override
    public void batchDelAeaServiceWindowStageByIds(String[] ids) {

        if(ids==null){
            throw new InvalidParameterException("参数ids为空!");
        }
        if(ids!=null&&ids.length==0){
            throw new InvalidParameterException("参数ids为空!");
        }
        aeaServiceWindowStageMapper.batchDelAeaServiceWindowStageByIds(ids);
    }

    /**
     * 列表获取 (后端)
     *
     * @param aeaServiceWindowStage
     * @return
     */
    @Override
    public List<AeaServiceWindowStage> listAeaServiceWindowStage(AeaServiceWindowStage aeaServiceWindowStage){

        List<AeaServiceWindowStage> list = aeaServiceWindowStageMapper.listAeaServiceWindowStage(aeaServiceWindowStage);
        logger.debug("成功执行查询list！！");
        return list;
    }

    /**
     * 分页获取(后端)
     *
     * @param aeaServiceWindowStage
     * @return
     */
    @Override
    public PageInfo<AeaServiceWindowStage> listAeaServiceWindowStage(AeaServiceWindowStage aeaServiceWindowStage, Page page){

        aeaServiceWindowStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaServiceWindowStage> list = aeaServiceWindowStageMapper.listAeaServiceWindowStage(aeaServiceWindowStage);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaServiceWindowStage>(list);
    }

    /**
     * 批量保存阶段与窗口关联(后端)
     *
     * @param stageId
     * @param windIds
     */
    @Override
    public void batchSaveStageWindows(String stageId, String[] windIds){

        if(StringUtils.isBlank(stageId)){
            throw new InvalidParameterException("参数stageId为空!");
        }
        if(windIds==null){
            throw new InvalidParameterException("参数windIds为空!");
        }
        if(windIds!=null && windIds.length==0){
            throw new InvalidParameterException("参数windIds为空!");
        }
        AeaServiceWindowStage windowStage;
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        for(String windId : windIds){
            windowStage = new AeaServiceWindowStage();
            windowStage.setWindStageId(UuidUtil.generateUuid());
            windowStage.setRootOrgId(rootOrgId);
            windowStage.setStageId(stageId);
            windowStage.setWindId(windId);
            windowStage.setIsActive(Status.ON);
            windowStage.setCreater(userId);
            windowStage.setCreateTime(new Date());
            aeaServiceWindowStageMapper.insertAeaServiceWindowStage(windowStage);
        }
    }

    @Override
    public void saveWindowStage(String windowId, String... stageId) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaServiceWindowStageMapper.delAeaServiceWindowStageByWindowId(windowId, rootOrgId);
        List<AeaServiceWindowStage> windowStageList = new ArrayList<>();
        for(String id : stageId) {
            AeaServiceWindowStage windowStage = new AeaServiceWindowStage();
            windowStage.setWindId(windowId);
            windowStage.setStageId(id);
            windowStage.setWindStageId(UUID.randomUUID().toString());
            windowStage.setIsActive(ActiveStatus.ACTIVE.getValue());
            windowStage.setCreater(SecurityContext.getCurrentUserId());
            windowStage.setCreateTime(new Date());
            windowStage.setRootOrgId(rootOrgId);
            windowStageList.add(windowStage);
        }
        aeaServiceWindowStageMapper.batchInsertAeaServiceWindowStage(windowStageList);
        logger.debug("新增窗口阶段");
    }

    @Override
    public List<ElementUiRsTreeNode> listAllStageTree(){
        List<ElementUiRsTreeNode> rootNodeList = new ArrayList<>();
        try {
            List<AeaParTheme> maxVerAeaParThemeList = aeaParThemeService.getMaxVerAeaParTheme(null, null);
            for(AeaParTheme aeaParTheme:maxVerAeaParThemeList ){
                List<AeaParStage> aeaParStages = aeaParStageMapper.listAeaParStageByThemeVerId(aeaParTheme.getThemeVerId());
                ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
                rootNode.setId(aeaParTheme.getThemeId());
                rootNode.setLabel(aeaParTheme.getThemeName());
                rootNode.setState(ElementUiRsTreeNode.STATE_OPEN);
                rootNode.setType("THEME");
                rootNode.setData(aeaParTheme);
                List<ElementUiRsTreeNode> childNodeList = new ArrayList<>();
                for (AeaParStage stage:aeaParStages){
                    ElementUiRsTreeNode node = new ElementUiRsTreeNode();
                    node.setId(stage.getStageId());
                    node.setLabel(stage.getStageName());
                    node.setState(ElementUiRsTreeNode.STATE_OPEN);
                    node.setType("STAGE");
                    node.setData(stage);
                    childNodeList.add(node);
                }
                rootNode.setChildren(childNodeList);
                rootNodeList.add(rootNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return rootNodeList;
    }

    @Override
    public List<AeaParStage> findCurrentUserWindowStageByThemeId(String themeId) throws Exception {
        List<AeaParTheme> maxVerAeaParThemeList = aeaParThemeService.getMaxVerAeaParTheme(null, themeId);
        if(maxVerAeaParThemeList!=null&&maxVerAeaParThemeList.size()>0){
            logger.debug("查询当前窗口人员某个主题下可以办理的阶段");
            String currentUserId = SecurityContext.getCurrentUserId();
            String currentOrgId = SecurityContext.getCurrentOrgId();
            List<AeaParStage> list = aeaServiceWindowStageMapper.findWindowStageByUserId(currentUserId,maxVerAeaParThemeList.get(0).getThemeVerId(),currentOrgId);
            return list;
        }
        return null;
    }

    @Override
    public void saveWindowStage(String windowId, List<AeaServiceWindowStage> aeaServiceWindowStageList) {
        if(aeaServiceWindowStageList ==null|| aeaServiceWindowStageList.size()==0){
            return;
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaServiceWindowStageMapper.delAeaServiceWindowStageByWindowId(windowId, rootOrgId);
        for (AeaServiceWindowStage windowStage: aeaServiceWindowStageList){
            windowStage.setWindId(windowId);
            windowStage.setRootOrgId(rootOrgId);
            windowStage.setWindStageId(UUID.randomUUID().toString());
            windowStage.setIsActive(ActiveStatus.ACTIVE.getValue());
            windowStage.setCreater(SecurityContext.getCurrentUserId());
            windowStage.setCreateTime(new Date());
        }
        aeaServiceWindowStageMapper.batchInsertAeaServiceWindowStage(aeaServiceWindowStageList);
        logger.debug("新增窗口阶段");
    }

    @Override
    public List<AeaServiceWindowStage> findAeaServiceWindowStageByWindowId(String windowId, String keyword) {
        List<AeaServiceWindowStage> list = aeaServiceWindowStageMapper.findAeaServiceWindowStageByWindowId(windowId, keyword);
        return list;
    }
}
