package com.augurit.aplanmis.common.service.window;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaServiceWindowItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/25
 */
public interface AeaServiceWindowItemService {

    /**
     * 查询当前窗口人员可办理事项
     *
     * @return 事项列表
     */
    List<AeaItemBasic> findCurrentUserWindowItem();

    /**
     * 当前窗口人员是否有办理事项的权限。多个事项版本只要有一个没有权限则返回false
     *
     * @param itemVerId 事项版本ID
     * @return true：当前窗口人员有办理权限，false：当前窗口人员没有办理权限
     */
    boolean hasAuthOnCurrentUser(String... itemVerId);

    /**
     * 查询窗口事项（包含未启用事项）
     *
     * @param windowId 窗口ID
     * @return 事项列表
     */
    List<AeaItemBasic> findWindowItemByWindowId(String windowId);

    /**
     * 分页查询窗口事项（包含未启用事项）
     *
     * @param windowId 窗口ID
     * @param page     分页对象
     * @return 事项列表
     */
    PageInfo<AeaItemBasic> listWindowItemByWindowId(String windowId, Page page);

    /**
     * 搜索窗口事项（包含未启用事项）
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（事项名称，事项编码）
     * @return 事项列表
     */
    List<AeaItemBasic> findWindowItemByKeyword(String windowId, String keyword);

    /**
     * 分页搜索窗口事项（包含未启用事项）
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（事项名称，事项编码）
     * @param page     分页对象
     * @return 事项列表
     */
    PageInfo<AeaItemBasic> listWindowItemByKeyword(String windowId, String keyword, Page page);

    /**
     * 查询窗口可办理事项
     *
     * @param windowId 窗口ID
     * @return 事项列表
     */
    List<AeaItemBasic> findActiveWindowItemByWindowId(String windowId);

    /**
     * 分页查询窗口可办理事项
     *
     * @param windowId 窗口ID
     * @param page     分页对象
     * @return 事项列表
     */
    PageInfo<AeaItemBasic> listActiveWindowItemByWindowId(String windowId, Page page);

    /**
     * 搜索窗口可办理事项
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（事项名称，事项编码）
     * @return 事项列表
     */
    List<AeaItemBasic> findActiveWindowItemByKeyword(String windowId, String keyword);

    /**
     * 分页搜索窗口可办理事项
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（事项名称，事项编码）
     * @param page     分页对象
     * @return 事项列表
     */
    PageInfo<AeaItemBasic> listActiveWindowItemByKeyword(String windowId, String keyword, Page page);

    /**
     * 模糊查询窗口事项
     *
     * @param windowId
     * @param keyword
     * @return
     */
    List<AeaServiceWindowItem> listWindowItemByWindowId(String windowId, String keyword);

    /**
     * 删除
     *
     * @param id
     */
    void delAeaServiceWindowItemById(String id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelAeaServiceWindowItemByIds(String[] ids);

    /**
     * 获取列表
     *
     * @param aeaServiceWindowItem
     * @return
     */
    List<AeaServiceWindowItem> listAeaServiceWindowItem(AeaServiceWindowItem aeaServiceWindowItem);

    /**
     * 分页获取
     *
     * @param aeaServiceWindowItem
     * @param page
     * @return
     */
    PageInfo<AeaServiceWindowItem> listAeaServiceWindowItem(AeaServiceWindowItem aeaServiceWindowItem, Page page);

    /**
     * 保存事项与窗口关系
     *
     * @param itemVerId
     * @param windowIds
     */
    void batchSaveItemWindows(String itemVerId, String[] windowIds);
}
