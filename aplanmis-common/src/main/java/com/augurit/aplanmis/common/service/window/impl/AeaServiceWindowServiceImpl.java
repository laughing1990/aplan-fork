package com.augurit.aplanmis.common.service.window.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.mapper.AeaServiceWindowMapper;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author yinlf
 * @Date 2019/8/1
 */
@Service
@Transactional
public class AeaServiceWindowServiceImpl implements AeaServiceWindowService {

    private static Logger LOGGER = LoggerFactory.getLogger(AeaServiceWindowServiceImpl.class);

    @Autowired
    AeaServiceWindowMapper aeaServiceWindowMapper;

    @Override
    public AeaServiceWindow getAeaServiceWindowByWindowId(String windowId) {
        LOGGER.debug("获取窗口信息");
        return aeaServiceWindowMapper.getAeaServiceWindowById(windowId);
    }

    @Override
    public List<AeaServiceWindow> findAllAeaServiceWindow() {
        LOGGER.debug("查询窗口信息");
        return aeaServiceWindowMapper.findAllAeaServiceWindow();
    }

    @Override
    public PageInfo<AeaServiceWindow> listAllAeaServiceWindow(Page page) {
        LOGGER.debug("分页查询窗口信息");
        PageHelper.startPage();
        List<AeaServiceWindow> list = aeaServiceWindowMapper.findAllAeaServiceWindow();
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaServiceWindow> findAeaServiceWindowByRegionId(String regionId) {
        LOGGER.debug("查询区域窗口信息");
        List<AeaServiceWindow> list = aeaServiceWindowMapper.findAeaServiceWindowByRegionId(regionId);
        return list;
    }

    @Override
    public PageInfo<AeaServiceWindow> listAeaServiceWindowByRegionId(String regionId, Page page) {
        LOGGER.debug("分页查询区域窗口信息");
        PageHelper.startPage();
        List<AeaServiceWindow> list = aeaServiceWindowMapper.findAeaServiceWindowByRegionId(regionId);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaServiceWindow> findWindowByItemVerId(String itemVerId) {
        LOGGER.debug("查询事项可办理窗口");
        List<AeaServiceWindow> list = aeaServiceWindowMapper.findWindowByItemVerId(itemVerId);
        return list;
    }

    @Override
    public AeaServiceWindow getCurrentUserWindow() {
        LOGGER.debug("获取当前用户所在窗口");
        String userId = SecurityContext.getCurrentUserId();
        List<AeaServiceWindow> aeaServiceWindowList = aeaServiceWindowMapper.findAeaServiceWindowByUserId(userId);
        if(aeaServiceWindowList == null || aeaServiceWindowList.size()<=0){
            return null;
        }
        return aeaServiceWindowList.get(0);
    }

    @Override
    public List<AeaServiceWindow> findAeaServiceWindow(AeaServiceWindow aeaServiceWindow) {
        LOGGER.debug("窗口搜索");
        List<AeaServiceWindow> list = aeaServiceWindowMapper.listAeaServiceWindow(aeaServiceWindow);
        return list;
    }

    @Override
    public PageInfo<AeaServiceWindow> listAeaServiceWindow(AeaServiceWindow aeaServiceWindow, Page page) {
        LOGGER.debug("窗口分页搜索");
        PageHelper.startPage();
        List<AeaServiceWindow> list = aeaServiceWindowMapper.listAeaServiceWindow(aeaServiceWindow);
        return new PageInfo<>(list);
    }
}
