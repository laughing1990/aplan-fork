package com.augurit.aplanmis.common.service.window.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.domain.AeaServiceWindowUser;
import com.augurit.aplanmis.common.mapper.AeaServiceWindowUserMapper;
import com.augurit.aplanmis.common.service.project.impl.AeaProjInofServiceImpl;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/26
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AeaServiceWindowUserServiceImpl implements AeaServiceWindowUserService {

    private static Logger LOGGER = LoggerFactory.getLogger(AeaProjInofServiceImpl.class);

    @Autowired
    private AeaServiceWindowUserMapper aeaServiceWindowUserMapper;

    @Override
    public List<OpuOmUser> findWindowUserByWindowId(String windowId) {

        LOGGER.debug("查询窗口人员");
        List<OpuOmUser> list = aeaServiceWindowUserMapper.findWindowUserByWindowIdAndKeywordAndIsActive(windowId, null, null);
        return list;
    }

    @Override
    public PageInfo<OpuOmUser> listWindowUserByWindowId(String windowId, Page page) {

        LOGGER.debug("分页查询窗口人员");
        PageHelper.startPage(page);
        List<OpuOmUser> list = aeaServiceWindowUserMapper.findWindowUserByWindowIdAndKeywordAndIsActive(windowId, null, null);
        return new PageInfo<>(list);
    }

    @Override
    public List<OpuOmUser> findWindowUserByRegionIdAndAllItemUser(String regionId, String rootOrgId) {
        LOGGER.debug("查询行政区划下全市通办窗口人员");
        return aeaServiceWindowUserMapper.findWindowUserByRegionIdAndAllItemUser(regionId,rootOrgId);
    }

    @Override
    public List<OpuOmUser> findWindowUserByRegionId(String regionId, String rootOrgId) {
        LOGGER.debug("查询行政区划下的窗口人员");
        return aeaServiceWindowUserMapper.findWindowUserByRegionId(regionId, rootOrgId);
    }

    @Override
    public List<OpuOmUser> findWindowUserByKeyword(String windowId, String keyword) {

        LOGGER.debug("搜索窗口人员（包含未启用人员）");
        List<OpuOmUser> list = aeaServiceWindowUserMapper.findWindowUserByWindowIdAndKeywordAndIsActive(windowId, keyword, null);
        return list;
    }

    @Override
    public PageInfo<OpuOmUser> listWindowUserByKeyword(String windowId, String keyword, Page page) {

        LOGGER.debug("分页搜索窗口人员（包含未启用人员）");
        PageHelper.startPage(page);
        List<OpuOmUser> list = aeaServiceWindowUserMapper.findWindowUserByWindowIdAndKeywordAndIsActive(windowId, keyword, null);
        return new PageInfo<>(list);
    }

    @Override
    public List<OpuOmUser> findActiveWindowUserByWindowId(String windowId) {

        LOGGER.debug("查询窗口有效人员");
        List<OpuOmUser> list = aeaServiceWindowUserMapper.findWindowUserByWindowIdAndKeywordAndIsActive(windowId, null, ActiveStatus.ACTIVE.getValue());
        return list;
    }

    @Override
    public PageInfo<OpuOmUser> listActiveWindowUserByWindowId(String windowId, Page page) {
        LOGGER.debug("分页查询窗口可办理人员");
        PageHelper.startPage(page);
        List<OpuOmUser> list = aeaServiceWindowUserMapper.findWindowUserByWindowIdAndKeywordAndIsActive(windowId, null, ActiveStatus.ACTIVE.getValue());
        return new PageInfo<>(list);
    }

    @Override
    public List<OpuOmUser> findActiveWindowUserByKeyword(String windowId, String keyword) {

        LOGGER.debug("搜索窗口有效人员");
        List<OpuOmUser> list = aeaServiceWindowUserMapper.findWindowUserByWindowIdAndKeywordAndIsActive(windowId, keyword, ActiveStatus.ACTIVE.getValue());
        return list;
    }

    @Override
    public PageInfo<OpuOmUser> listActiveWindowUserByKeyword(String windowId, String keyword, Page page) {

        LOGGER.debug("分页搜索窗口有效人员");
        PageHelper.startPage(page);
        List<OpuOmUser> list = aeaServiceWindowUserMapper.findWindowUserByWindowIdAndKeywordAndIsActive(windowId, keyword, ActiveStatus.ACTIVE.getValue());
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaServiceWindowUser> listWindowUserByWindowId(String windowId, String keyword){

        if (StringUtils.isBlank(windowId)) {
            throw new InvalidParameterException("参数windowId为空!");
        }
        return aeaServiceWindowUserMapper.listWindowUserByWindowId(windowId, keyword);
    }
}
