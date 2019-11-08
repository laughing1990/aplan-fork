package com.augurit.aplanmis.common.service.window;

import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.common.domain.AeaServiceWindowUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/25
 */
public interface AeaServiceWindowUserService {

    /**
     * 查询窗口人员
     *
     * @param windowId 窗口ID
     * @return 人员列表
     */
    List<OpuOmUser> findWindowUserByWindowId(String windowId);

    /**
     * 分页查询窗口人员
     *
     * @param windowId 窗口ID
     * @param page     分页对象
     * @return 人员列表
     */
    PageInfo<OpuOmUser> listWindowUserByWindowId(String windowId, Page page);

    /**
     * 查询行政区划下窗口人员和全市通办人员
     *
     * @param regionId  行政区划ID（为空时查询所有顶级组织下的全市通办窗口人员）
     * @param rootOrgId 顶级组织ID
     * @return 人员列表
     */
    List<OpuOmUser> findWindowUserByRegionIdAndAllItemUser(String regionId, String rootOrgId);

    /**
     * 查询行政区划下窗口人员
     *
     * @param regionId  行政区划ID
     * @param rootOrgId 顶级组织ID
     * @return 人员列表
     */
    public List<OpuOmUser> findWindowUserByRegionId(String regionId, String rootOrgId);

    /**
     * 搜索窗口人员
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（人员名称）
     * @return 人员列表
     */
    List<OpuOmUser> findWindowUserByKeyword(String windowId, String keyword);

    /**
     * 分页搜索窗口人员
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（人员名称）
     * @param page     分页对象
     * @return 人员列表
     */
    PageInfo<OpuOmUser> listWindowUserByKeyword(String windowId, String keyword, Page page);

    /**
     * 查询窗口有效人员
     *
     * @param windowId 窗口ID
     * @return 人员列表
     */
    List<OpuOmUser> findActiveWindowUserByWindowId(String windowId);

    /**
     * 分页查询窗口有效人员
     *
     * @param windowId 窗口ID
     * @param page     分页对象
     * @return 人员列表
     */
    PageInfo<OpuOmUser> listActiveWindowUserByWindowId(String windowId, Page page);

    /**
     * 搜索窗口有效人员
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（人员姓名）
     * @return 人员列表
     */
    List<OpuOmUser> findActiveWindowUserByKeyword(String windowId, String keyword);

    /**
     * 分页搜索窗口有效人员
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（人员姓名）
     * @param page     分页对象
     * @return 人员列表
     */
    PageInfo<OpuOmUser> listActiveWindowUserByKeyword(String windowId, String keyword, Page page);

    /**
     * 模糊查询窗口人员
     *
     * @param windowId
     * @param keyword
     * @return
     */
    List<AeaServiceWindowUser> listWindowUserByWindowId(String windowId, String keyword);
}
