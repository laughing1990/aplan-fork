package com.augurit.aplanmis.common.service.window.impl;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaServiceWindowItem;
import com.augurit.aplanmis.common.mapper.AeaServiceWindowItemMapper;
import com.augurit.aplanmis.common.service.project.impl.AeaProjInofServiceImpl;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/26
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AeaServiceWindowItemServiceImpl implements AeaServiceWindowItemService {

    private static Logger LOGGER = LoggerFactory.getLogger(AeaProjInofServiceImpl.class);

    @Autowired
    AeaServiceWindowItemMapper aeaServiceWindowItemMapper;

    @Override
    public List<AeaItemBasic> findCurrentUserWindowItem() {

        LOGGER.debug("查询当前窗口人员可办理事项");
        String currentUserId = SecurityContext.getCurrentUserId();
        List<AeaItemBasic> list = aeaServiceWindowItemMapper.findWindowItemByUserId(currentUserId);
        return list;
    }

    @Override
    public boolean hasAuthOnCurrentUser(String... itemVerId) {

        LOGGER.debug("当前窗口人员是否有办理事项的权限");
        String currentUserId = SecurityContext.getCurrentUserId();
        List<AeaItemBasic> list = aeaServiceWindowItemMapper.findWindowItemByUserId(currentUserId);
        List<String> authItemList = new ArrayList<>();
        for (AeaItemBasic itemBasic : list) {
            authItemList.add(itemBasic.getItemVerId());
        }
        return authItemList.containsAll(Arrays.asList(itemVerId));
    }

    @Override
    public List<AeaItemBasic> findWindowItemByWindowId(String windowId) {

        LOGGER.debug("查询窗口事项");
        String orgId = SecurityContext.getCurrentOrgId();
        List<AeaItemBasic> list = aeaServiceWindowItemMapper.findWindowItemByWindowIdAndKeywordAndIsActive(windowId, null, null, orgId);
        return list;
    }

    @Override
    public PageInfo<AeaItemBasic> listWindowItemByWindowId(String windowId, Page page) {

        LOGGER.debug("分页查询窗口事项");
        PageHelper.startPage(page);
        String orgId = SecurityContext.getCurrentOrgId();
        List<AeaItemBasic> list = aeaServiceWindowItemMapper.findWindowItemByWindowIdAndKeywordAndIsActive(windowId, null, null, orgId);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaItemBasic> findWindowItemByKeyword(String windowId, String keyword) {
        LOGGER.debug("搜索窗口可办理事项（包含未启用事项）");
        String orgId = SecurityContext.getCurrentOrgId();
        List<AeaItemBasic> list = aeaServiceWindowItemMapper.findWindowItemByWindowIdAndKeywordAndIsActive(windowId, keyword, null, orgId);
        return list;
    }

    @Override
    public PageInfo<AeaItemBasic> listWindowItemByKeyword(String windowId, String keyword, Page page) {
        LOGGER.debug("分页搜索窗口事项（包含未启用事项）");
        PageHelper.startPage(page);
        String orgId = SecurityContext.getCurrentOrgId();
        List<AeaItemBasic> list = aeaServiceWindowItemMapper.findWindowItemByWindowIdAndKeywordAndIsActive(windowId, keyword, null, orgId);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaItemBasic> findActiveWindowItemByWindowId(String windowId) {
        LOGGER.debug("查询窗口可办理事项");
        String orgId = SecurityContext.getCurrentOrgId();
        List<AeaItemBasic> list = aeaServiceWindowItemMapper.findWindowItemByWindowIdAndKeywordAndIsActive(windowId, null, ActiveStatus.ACTIVE.getValue(), orgId);
        return list;
    }

    @Override
    public PageInfo<AeaItemBasic> listActiveWindowItemByWindowId(String windowId, Page page) {
        LOGGER.debug("分页查询窗口可办理事项");
        PageHelper.startPage(page);
        String orgId = SecurityContext.getCurrentOrgId();
        List<AeaItemBasic> list = aeaServiceWindowItemMapper.findWindowItemByWindowIdAndKeywordAndIsActive(windowId, null, ActiveStatus.ACTIVE.getValue(), orgId);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaItemBasic> findActiveWindowItemByKeyword(String windowId, String keyword) {
        LOGGER.debug("搜索窗口可办理事项");
        String orgId = SecurityContext.getCurrentOrgId();
        List<AeaItemBasic> list = aeaServiceWindowItemMapper.findWindowItemByWindowIdAndKeywordAndIsActive(windowId, keyword, ActiveStatus.ACTIVE.getValue(), orgId);
        return list;
    }

    @Override
    public PageInfo<AeaItemBasic> listActiveWindowItemByKeyword(String windowId, String keyword, Page page) {
        LOGGER.debug("分页搜索窗口可办理事项");
        PageHelper.startPage(page);
        String orgId = SecurityContext.getCurrentOrgId();
        List<AeaItemBasic> list = aeaServiceWindowItemMapper.findWindowItemByWindowIdAndKeywordAndIsActive(windowId, keyword, ActiveStatus.ACTIVE.getValue(), orgId);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaServiceWindowItem> listWindowItemByWindowId(String windowId, String keyword){

        if (StringUtils.isBlank(windowId)) {
            throw new InvalidParameterException("参数windowId为空!");
        }
        return aeaServiceWindowItemMapper.listWindowItemByWindowId(windowId, keyword,SecurityContext.getCurrentOrgId());
    }

    @Override
    public void batchSaveItemWindows(String itemVerId, String[] windowIds){

        if(StringUtils.isBlank("请求参数itemVerId不能为空!")){
            throw new InvalidParameterException("请求参数itemVerId不能为空!");
        }
        if(windowIds==null){
            throw new InvalidParameterException("请求参数windowIds不能为空!");
        }
        if(windowIds!=null&&windowIds.length==0){
            throw new InvalidParameterException("请求参数windowIds不能为空!");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaServiceWindowItem item;
        String userId = SecurityContext.getCurrentUserId();
        for(String windId : windowIds){
            item = new AeaServiceWindowItem();
            item.setWindItemId(UuidUtil.generateUuid());
            item.setItemVerId(itemVerId);
            item.setWindId(windId);
            item.setRootOrgId(rootOrgId);
            item.setIsActive(Status.ON);
            item.setCreater(userId);
            item.setCreateTime(new Date());
            aeaServiceWindowItemMapper.insertAeaServiceWindowItem(item);
        }
    }

    @Override
    public void delAeaServiceWindowItemById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("请求参数id不能为空!");
        }
        aeaServiceWindowItemMapper.deleteAeaServiceWindowItemById(id);
    }

    @Override
    public void batchDelAeaServiceWindowItemByIds(String[] ids){

        if(ids==null){
            throw new InvalidParameterException("请求参数ids不能为空!");
        }
        if(ids!=null&&ids.length==0){
            throw new InvalidParameterException("请求参数ids不能为空!");
        }
        aeaServiceWindowItemMapper.batchDelAeaServiceWindowItemByIds(ids);
    }

    @Override
    public PageInfo<AeaServiceWindowItem> listAeaServiceWindowItem(AeaServiceWindowItem aeaServiceWindowItem,Page page) {

        PageHelper.startPage(page);
        List<AeaServiceWindowItem> list = aeaServiceWindowItemMapper.listAeaServiceWindowItem(aeaServiceWindowItem);
        return new PageInfo<AeaServiceWindowItem>(list);
    }

    @Override
    public List<AeaServiceWindowItem> listAeaServiceWindowItem(AeaServiceWindowItem aeaServiceWindowItem) {

        List<AeaServiceWindowItem> list = aeaServiceWindowItemMapper.listAeaServiceWindowItem(aeaServiceWindowItem);
        return list;
    }
}
