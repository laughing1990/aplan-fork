package com.augurit.aplanmis.common.service.admin.window.impl;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserMapper;
import com.augurit.aplanmis.common.convert.BscDicRegionConvert;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.domain.AeaServiceWindowItem;
import com.augurit.aplanmis.common.domain.AeaServiceWindowStage;
import com.augurit.aplanmis.common.domain.AeaServiceWindowUser;
import com.augurit.aplanmis.common.mapper.AeaServiceWindowItemMapper;
import com.augurit.aplanmis.common.mapper.AeaServiceWindowMapper;
import com.augurit.aplanmis.common.mapper.AeaServiceWindowStageMapper;
import com.augurit.aplanmis.common.mapper.AeaServiceWindowUserMapper;
import com.augurit.aplanmis.common.service.admin.window.AeaServiceWindowAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.vo.AeaRegionOptionVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author ZhangXinhui
 * @date 2019/8/1 001 14:30
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaServiceWindowAdminServiceImpl implements AeaServiceWindowAdminService {

    private static Logger log = LoggerFactory.getLogger(AeaServiceWindowAdminServiceImpl.class);

    @Autowired
    private BscAttMapper bscAttMapper;

    @Autowired
    private AeaServiceWindowMapper aeaServiceWindowMapper;

    @Autowired
    private BscDicRegionMapper bscDicRegionMapper;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Autowired
    private OpuOmUserMapper opuOmUserMapper;


    @Autowired
    AeaServiceWindowUserMapper aeaServiceWindowUserMapper;

    @Autowired
    AeaServiceWindowItemMapper aeaServiceWindowItemMapper;

    @Autowired
    AeaServiceWindowStageMapper aeaServiceWindowStageMapper;
    @Autowired
    private FileUtilsService fileUtilsService;

    @Override
    public AeaServiceWindow getAeaServiceWindowById(String windowId) {

        AeaServiceWindow window = aeaServiceWindowMapper.getAeaServiceWindowById(windowId);
        String topOrgId = SecurityContext.getCurrentOrgId();
        List<BscAttForm> mapAttList = bscAttMapper.listAttLinkAndDetail("AEA_SERVICE_WINDOW", "WINDOW_ID", windowId, null, topOrgId, null);
        window.setMapAttNum(mapAttList == null ? 0L : mapAttList.size());
        return window;
    }

    @Override
    public List<AeaServiceWindowUser> listWindowUserByWindowId(String windowId, String keyword) {

        if (StringUtils.isBlank(windowId)) {
            throw new InvalidParameterException("参数windowId为空!");
        }
        return aeaServiceWindowUserMapper.listWindowUserByWindowId(windowId, keyword);
    }

    @Override
    public List<AeaServiceWindowItem> listWindowItemByWindowId(String windowId, String keyword) {

        if (StringUtils.isBlank(windowId)) {
            throw new InvalidParameterException("参数windowId为空!");
        }
        return aeaServiceWindowItemMapper.listWindowItemByWindowId(windowId, keyword, SecurityContext.getCurrentOrgId());
    }

    @Override
    public void deleteAeaServiceWindowById(String windowId, String rootOrgId) throws Exception {

        if (StringUtils.isBlank(windowId)) {
            throw new InvalidParameterException("参数windowId为空!");
        }
        aeaServiceWindowMapper.deleteAeaServiceWindow(windowId);
        // 删除对应的事项
        aeaServiceWindowItemMapper.delAeaServiceWindowItemByWindowIds(new String[]{windowId}, rootOrgId);
        //删除对应的人员
        aeaServiceWindowUserMapper.deleteAeaServiceWindowUserByWindowId(windowId);
        //删除对应的阶段
        aeaServiceWindowStageMapper.delAeaServiceWindowStageByWindowId(windowId, rootOrgId);
        // 删除对应的附件
        List<BscAttForm> mapAttList = bscAttMapper.listAttLinkAndDetail("AEA_SERVICE_WINDOW", "WINDOW_ID", windowId, null, rootOrgId, null);
        for (BscAttForm form : mapAttList) {
//            attachmentAdminService.deleteFileStategy(form.getDetailId());
            fileUtilsService.deleteAttachment(form.getDetailId());
        }
    }

    @Override
    public void batchDeleteAeaServiceWindow(String[] windowIds, String rootOrgId) throws Exception {

        if (windowIds != null && windowIds.length > 0) {
            aeaServiceWindowMapper.batchDeleteAeaServiceWindow(windowIds, rootOrgId);
            // 删除对应的事项
            aeaServiceWindowItemMapper.delAeaServiceWindowItemByWindowIds(windowIds, rootOrgId);
            // 删除对应的附件
            String topOrgId = SecurityContext.getCurrentOrgId();
            for (String windowId : windowIds) {
                List<BscAttForm> mapAttList = bscAttMapper.listAttLinkAndDetail("AEA_SERVICE_WINDOW", "WINDOW_ID", windowId, null, rootOrgId, null);
                for (BscAttForm form : mapAttList) {
//                    attachmentAdminService.deleteFileStategy(form.getDetailId());
                    fileUtilsService.deleteAttachment(form.getDetailId());
                }
            }
        }
    }

    @Override
    public void saveAeaServiceWindowItem(String windowId, String[] itemVerIds) {
        String currentOrgId = SecurityContext.getCurrentOrgId();
        aeaServiceWindowItemMapper.delAeaServiceWindowItemByWindowAndItemIds(windowId, null, currentOrgId);
        if (itemVerIds != null && itemVerIds.length > 0) {
            String userId = SecurityContext.getCurrentUserId();
            for (String itemVerId : itemVerIds) {
                AeaServiceWindowItem aeaServiceWindowItem = new AeaServiceWindowItem();
                aeaServiceWindowItem.setWindItemId(UuidUtil.generateUuid());
                aeaServiceWindowItem.setItemVerId(itemVerId);
                aeaServiceWindowItem.setWindId(windowId);
                aeaServiceWindowItem.setIsActive(Status.ON);
                aeaServiceWindowItem.setCreater(userId);
                aeaServiceWindowItem.setCreateTime(new Date());
                aeaServiceWindowItem.setRootOrgId(currentOrgId);
                aeaServiceWindowItemMapper.insertAeaServiceWindowItem(aeaServiceWindowItem);
            }
        }
    }

    @Override
    public void saveAeaServiceWindowUsers(String windowId, String[] userIds) {

        aeaServiceWindowUserMapper.deleteAeaServiceWindowUserByWindowId(windowId);
        if (userIds != null && userIds.length > 0) {
            String creater = SecurityContext.getCurrentUserId();
            // 注意用户是可能存在重复的，因为不同用户可能存在多组织下的
            for (String userId : userIds) {
                AeaServiceWindowUser windUser = new AeaServiceWindowUser();
                windUser.setWindowUserId(UuidUtil.generateUuid());
                windUser.setUserId(userId);
                windUser.setWindowId(windowId);
                windUser.setIsActive(Status.ON);
                windUser.setCreater(creater);
                windUser.setCreateTime(new Date());
                windUser.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaServiceWindowUserMapper.insertAeaServiceWindowUser(windUser);
            }
        }
    }

    @Override
    public void saveAeaServiceWindowStage(String windowId, String[] stageIds) {

        String currentOrgId = SecurityContext.getCurrentOrgId();
        aeaServiceWindowStageMapper.delAeaServiceWindowStageByWindowId(windowId, currentOrgId);
        if (stageIds != null && stageIds.length > 0) {
            String userId = SecurityContext.getCurrentUserId();
            for (String stageId : stageIds) {
                AeaServiceWindowStage windowStage = new AeaServiceWindowStage();
                windowStage.setWindStageId(UuidUtil.generateUuid());
                windowStage.setStageId(stageId);
                windowStage.setWindId(windowId);
                windowStage.setIsActive(Status.ON);
                windowStage.setCreater(userId);
                windowStage.setCreateTime(new Date());
                windowStage.setRootOrgId(currentOrgId);
                aeaServiceWindowStageMapper.insertAeaServiceWindowStage(windowStage);
            }
        }
    }

    @Override
    public List<ElementUiRsTreeNode> listAllUserByOrgId(String orgId) {
        List<ElementUiRsTreeNode> rootNodeList = new ArrayList<>();
        ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
        OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(orgId);
        rootNode.setId("root");
        rootNode.setLabel(StringUtils.isNotBlank(opuOmOrg.getOrgName()) ? opuOmOrg.getOrgName() : "组织用户树");
        rootNode.setState(ElementUiRsTreeNode.STATE_OPEN);
        rootNode.setType("root");
        rootNode.setData(opuOmOrg);
        List<OpuOmUser> list = opuOmUserMapper.listAllUserRelOrgByOrgId(orgId);
        List<ElementUiRsTreeNode> childNodeList = new ArrayList<>();
        for (OpuOmUser user:list){
            ElementUiRsTreeNode node = new ElementUiRsTreeNode();
            node.setId(user.getUserId());
            node.setLabel(user.getLoginName());
            if (StringUtils.isNotBlank(user.getOrgName())) {
                node.setLabel(user.getLoginName() + "【" + user.getOrgName() + "】");
            }
            node.setState(ElementUiRsTreeNode.STATE_OPEN);
            node.setType("user");
            node.setData(user);
            childNodeList.add(node);
        }
        rootNode.setChildren(childNodeList);
        rootNodeList.add(rootNode);
        return rootNodeList;
    }

    @Override
    public PageInfo<AeaServiceWindow> listAeaServiceWindowByPage(AeaServiceWindow aeaServiceWindow, Page page) {

        PageHelper.startPage(page);
        List<AeaServiceWindow> list = aeaServiceWindowMapper.listAeaServiceWindow(aeaServiceWindow);
        log.info("成功执行分页查询！！");
        return new PageInfo<AeaServiceWindow>(list);
    }

    @Override
    public List<AeaServiceWindow> listAeaServiceWindow(AeaServiceWindow aeaServiceWindow) {

        List<AeaServiceWindow> list = aeaServiceWindowMapper.listAeaServiceWindow(aeaServiceWindow);
        log.info("成功执行分页查询！！");
        return list;
    }

    @Override
    public Long getMaxSortNo(String rootOrgId) {

        Long sortNo = aeaServiceWindowMapper.getMaxSortNo(rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
    }

    @Override
    public List<ZtreeNode> listSelfAndChildRegionByOrgId(String orgId) {

        if (StringUtils.isNotBlank(orgId)) {
            OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(orgId);
            if (opuOmOrg != null) {
                List<BscDicRegion> list = bscDicRegionMapper.listSelfAndChildRegions(opuOmOrg.getRegionId(), null);
                List<ZtreeNode> nodes = new ArrayList<>();
                if (list != null && list.size() > 0) {
                    ZtreeNode node;
                    for (BscDicRegion region : list) {
                        node = new ZtreeNode();
                        node.setId(region.getRegionId());
                        node.setName(region.getRegionName());
                        node.setpId(region.getParentId());
                        node.setOpen(true);
                        nodes.add(node);
                    }
                    return nodes;
                }
            }
        }
        return null;
    }

    @Override
    public void updateAeaServiceWindow(AeaServiceWindow aeaServiceWindow) {

        aeaServiceWindow.setModifier(SecurityContext.getCurrentUserId());
        aeaServiceWindow.setModifyTime(new Date());
        aeaServiceWindowMapper.updateAeaServiceWindow(aeaServiceWindow);
    }

    @Override
    public void saveAeaServiceWindow(AeaServiceWindow aeaServiceWindow) {

        aeaServiceWindow.setCreater(SecurityContext.getCurrentUserId());
        aeaServiceWindow.setCreateTime(new Date());
        aeaServiceWindowMapper.insertAeaServiceWindow(aeaServiceWindow);
    }

    @Override
    public void saveMapAttFile(AeaServiceWindow window, HttpServletRequest request) throws Exception {

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        window.setMapAtt(getMapAttFile(req, "mapAttFile", "AEA_SERVICE_WINDOW", "WINDOW_ID", window.getWindowId()));
        updateAeaServiceWindow(window);
    }


    private String getMapAttFile(StandardMultipartHttpServletRequest request,
                                 String fileName,
                                 String tableName,
                                 String pkName,
                                 String recordId) throws Exception {

        StringBuilder ids = new StringBuilder();
        List<MultipartFile> files = request.getFiles(fileName);
        if (files != null && files.size() > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    BscAttForm form = fileUtilsService.upload(tableName, pkName, recordId, null, file);
                    if (null != form) {
                        ids.append(form.getDetailId()).append(",");
                    }

                    /*UploadFileCmd uploadFileCmd = uploaderFactory
                            .createDefaultUpload(file, recordId, tableName, pkName, UploadType.MONGODB.getValue());
                    String detailId = attachmentAdminService.uploadFileStrategy(uploadFileCmd);
                    ids.append(detailId).append(",");*/
                }
            }
        }
        return ids.toString();
    }

    @Override
    public PageInfo<AeaServiceWindow> listItemUnselectedWindowByPage(AeaServiceWindowItem windowItem, Page page) {

        PageHelper.startPage(page);
        List<AeaServiceWindow> list = aeaServiceWindowMapper.listItemUnselectedWindow(windowItem);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AeaServiceWindow> listStageUnselectedWindowByPage(AeaServiceWindowStage windowStage, Page page) {

        PageHelper.startPage(page);
        List<AeaServiceWindow> list = aeaServiceWindowMapper.listStageUnselectedWindow(windowStage);
        return new PageInfo<>(list);
    }

    @Override
    public void delServiceWindowAtt(String windowId, String detailId, String rootOrgId) throws Exception {

        AeaServiceWindow window = aeaServiceWindowMapper.getAeaServiceWindowById(windowId);
        if (window != null) {
            if (!window.getRootOrgId().equalsIgnoreCase(rootOrgId)) {
                return;
            }

            if (StringUtils.isNotBlank(window.getMapAtt())) {
                String replaceStr = window.getMapAtt().replaceAll(detailId + ",", "");
                window.setMapAtt(replaceStr);
                updateAeaServiceWindow(window);
            }
        }
//        attachmentAdminService.deleteFileStategy(detailId);
        fileUtilsService.deleteAttachment(detailId);
    }

    @Override
    public PageInfo<AeaServiceWindowItem> listAeaServiceWindowItemByPage(AeaServiceWindowItem aeaServiceWindowItem, Page page) {

        PageHelper.startPage(page);
        List<AeaServiceWindowItem> list = aeaServiceWindowItemMapper.listAeaServiceWindowItem(aeaServiceWindowItem);
        return new PageInfo<AeaServiceWindowItem>(list);
    }

    @Override
    public ElementUiRsTreeNode listRegionTreeByOrgId(String orgId) {
        BscDicRegionConvert bscDicRegionConvert = new BscDicRegionConvert();
        if (StringUtils.isNotBlank(orgId)) {
            OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(orgId);
            if (opuOmOrg != null && StringUtils.isNotBlank(opuOmOrg.getRegionId())) {
                BscDicRegion root = bscDicRegionMapper.getBscDicRegionById(opuOmOrg.getRegionId());
                if (root != null) {
                    List<BscDicRegion> list = bscDicRegionMapper.listChildRegions(root.getRegionId(), null);
                    ElementUiRsTreeNode rootNode = bscDicRegionConvert.convertToElementUiNode(root, list);
                    return rootNode;
                }
            }
        }
        return null;
    }

    @Override
    public List<BscAttForm> findWindowMapAtt(String windowId) {
        String orgId = SecurityContext.getCurrentOrgId();
        List<BscAttForm> bscAttForms = bscAttMapper.listAttLinkAndDetailByTablePKRecordId("AEA_SERVICE_WINDOW", "WINDOW_ID", windowId, orgId);
        return bscAttForms;
    }

    @Override
    public void saveAgencyUsers(String windowId, String[] userIds) {
        AeaServiceWindow window = aeaServiceWindowMapper.getAeaServiceWindowById(windowId);
        if(window == null || !window.getRootOrgId().equalsIgnoreCase(SecurityContext.getCurrentOrgId())){
            throw new RuntimeException("找不到该窗口");
        }
        if (userIds != null && userIds.length > 0) {
            List<AeaServiceWindowUser> windowUsers = aeaServiceWindowUserMapper.listWindowUserByWindowId(windowId, null);
            List<String> userIdList = new ArrayList<>();
            if(windowUsers != null && windowUsers.size() > 0){
                for(AeaServiceWindowUser wu: windowUsers){
                    userIdList.add(wu.getUserId());
                }
            }
            if(userIdList.size() > 0){
                List<String> ids = Arrays.asList(userIds);
                for(String userId:userIdList){
                    if(!ids.contains(userId)){
                        aeaServiceWindowUserMapper.deleteAeaServiceWindowUserByWindowIdAndUserId(windowId,userId);
                    }
                }
            }
            for (String userId : userIds) {
                if(userIdList.contains(userId)){
                    continue;
                }
                AeaServiceWindowUser windUser = new AeaServiceWindowUser();
                windUser.setWindowUserId(UuidUtil.generateUuid());
                windUser.setUserId(userId);
                windUser.setWindowId(windowId);
                windUser.setIsActive(Status.ON);
                windUser.setCreater(SecurityContext.getCurrentUserId());
                windUser.setCreateTime(new Date());
                windUser.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaServiceWindowUserMapper.insertAeaServiceWindowUser(windUser);
            }
        }
    }

    @Override
    public void deleteAgencyUser(String windowId, String[] userIds) {
        AeaServiceWindow window = aeaServiceWindowMapper.getAeaServiceWindowById(windowId);
        if(window == null || !window.getRootOrgId().equalsIgnoreCase(SecurityContext.getCurrentOrgId())){
            throw new RuntimeException("找不到该窗口");
        }
        if (userIds != null && userIds.length > 0) {
            aeaServiceWindowUserMapper.batchDeleteWindowUserByWindowIdAndUserId(windowId,userIds);
        }
    }

    @Override
    public List<AeaRegionOptionVo> getRegionOptions() {
        List<BscDicRegion> list = bscDicRegionMapper.listBscDicRegion( null);
        List<AeaRegionOptionVo> voList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            //p表示省份，m表示直辖市，c表示城市，a表示区县
            String[] types = {"p","m","c","a"};
            List<String> typeList = Arrays.asList(types);
            AeaRegionOptionVo vo;
            for (int i=0;i<list.size();i++) {
                BscDicRegion bscDicRegion = list.get(i);
                if(!typeList.contains(bscDicRegion.getRegionType())){
                    list.remove(bscDicRegion);
                    i--;
                }
            }
            for (int i=0;i<list.size();i++) {
                BscDicRegion bscDicRegion = list.get(i);
                if(bscDicRegion.getRegionalLevel() == 3){//省级
                    vo = new AeaRegionOptionVo();
                    vo.setValue(bscDicRegion.getRegionId());
                    vo.setLabel(bscDicRegion.getRegionName()+"省");
                    List<AeaRegionOptionVo> children = getChildrenVo(list,bscDicRegion.getRegionId());
                    vo.setChildren(children);
                    voList.add(vo);
                }
            }
        }
        return voList;
    }

    private List<AeaRegionOptionVo> getChildrenVo(List<BscDicRegion> list,String regionId){
        List<AeaRegionOptionVo> voList = new ArrayList<>();
        for (int i=0;i<list.size();i++) {
            BscDicRegion childRegion = list.get(i);
            if(childRegion.getParentRegionId().equals(regionId)){
                AeaRegionOptionVo optionVo = new AeaRegionOptionVo();
                optionVo.setValue(childRegion.getRegionId());
                optionVo.setLabel(childRegion.getRegionName());
                optionVo.setChildren(getChildrenVo(list,childRegion.getRegionId()));
                voList.add(optionVo);
                list.remove(childRegion);
                i--;
            }
        }
        return voList;
    }
}
