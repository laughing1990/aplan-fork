package com.augurit.aplanmis.common.service.window;

import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/26
 */
public interface AeaServiceWindowService {

    /**
     *  根据ID获取窗口
     * @param windowId
     * @return
     */
    AeaServiceWindow getAeaServiceWindowByWindowId(String windowId);

    /**
     * 查询所有窗口列表
     *
     * @return 窗口列表
     */
    List<AeaServiceWindow> findAllAeaServiceWindow();

    /**
     * 分页查询所有窗口列表
     *
     * @param page 分页对象
     * @return 窗口列表
     */
    PageInfo<AeaServiceWindow> listAllAeaServiceWindow(Page page);

    /**
     * 查询区域内所有窗口列表
     *
     * @param regionId 行政区划ID
     * @return 窗口列表
     */
    List<AeaServiceWindow> findAeaServiceWindowByRegionId(String regionId);

    /**
     * 分页查询区域内所有窗口列表
     *
     * @param regionId 行政区划ID
     * @param page     分页对象
     * @return 窗口列表
     */
    PageInfo<AeaServiceWindow> listAeaServiceWindowByRegionId(String regionId, Page page);

    /**
     * 查询事项可办理窗口
     *
     * @param itemVerId 事项版本ID
     * @return 窗口列表
     */
    List<AeaServiceWindow> findWindowByItemVerId(String itemVerId);

    /**
     * 获取当前用户所在窗口
     *
     * @return
     */
    AeaServiceWindow getCurrentUserWindow();

    /**
     *  窗口搜索
     * @param aeaServiceWindow
     * @return 窗口列表
     */
    List<AeaServiceWindow> findAeaServiceWindow(AeaServiceWindow aeaServiceWindow);

    /**
     * 分页搜索
     *
     * @param aeaServiceWindow
     * @return 窗口列表
     */
    PageInfo<AeaServiceWindow> listAeaServiceWindow(AeaServiceWindow aeaServiceWindow,Page page);

}
