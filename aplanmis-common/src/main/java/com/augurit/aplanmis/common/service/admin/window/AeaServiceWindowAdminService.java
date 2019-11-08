package com.augurit.aplanmis.common.service.admin.window;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.domain.AeaServiceWindowItem;
import com.augurit.aplanmis.common.domain.AeaServiceWindowStage;
import com.augurit.aplanmis.common.domain.AeaServiceWindowUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/8/1 001 14:25
 * @desc
 **/
public interface AeaServiceWindowAdminService {

    /**
     * 根据机构获取用户
     *
     * @param orgId
     * @return
     */
    List<ElementUiRsTreeNode> listAllUserByOrgId(String orgId);

    /**
     * 分页服务查询窗口
     *
     * @param aeaServiceWindow
     * @param page
     * @return
     */
    PageInfo<AeaServiceWindow> listAeaServiceWindowByPage(AeaServiceWindow aeaServiceWindow, Page page);

    /**
     * 查询窗口
     */
    List<AeaServiceWindow> listAeaServiceWindow(AeaServiceWindow aeaServiceWindow);

    /**
     * 获取最大排序号
     *
     * @param rootOrgId
     * @return
     */
    Long getMaxSortNo(String rootOrgId);

    /**
     * 根据机构获取行政区域
     *
     * @param orgId 机构id
     * @return
     */
    List<ZtreeNode> listSelfAndChildRegionByOrgId(String orgId);

    /**
     * 更新服务窗口
     *
     * @param aeaServiceWindow
     */
    void updateAeaServiceWindow(AeaServiceWindow aeaServiceWindow);

    /**
     * 新增服务窗口
     *
     * @param aeaServiceWindow
     */
    void saveAeaServiceWindow(AeaServiceWindow aeaServiceWindow);

    /**
     * 保存附件
     *
     * @param window
     * @param request
     */
    void saveMapAttFile(AeaServiceWindow window, HttpServletRequest request) throws Exception;

    /**
     * 获取服务窗口对象及附件
     *
     * @param windowId 服务窗口id
     * @return
     */
    AeaServiceWindow getAeaServiceWindowById(String windowId);

    /**
     * 根据服务窗口获取关联用户
     *
     * @param windowId 窗口id
     * @param keyword  查询关键字，支持opus登录用户名、opus用户名、机构名
     * @return
     */
    List<AeaServiceWindowUser> listWindowUserByWindowId(String windowId, String keyword);

    /**
     * 根据服务窗口获取关联事项
     *
     * @param windowId 窗口id
     * @param keyword  查询关键字，支持事项名、事项编码、机构名
     * @return
     */
    List<AeaServiceWindowItem> listWindowItemByWindowId(String windowId, String keyword);

    /**
     * 删除服务窗口
     *
     * @param windowId  窗口id
     * @param rootOrgId
     */
    void deleteAeaServiceWindowById(String windowId, String rootOrgId) throws Exception;


    /**
     * 批量删除服务窗口
     *
     * @param windowIds    窗口ids集合
     * @param currentOrgId
     */
    void batchDeleteAeaServiceWindow(String[] windowIds, String currentOrgId) throws Exception;

    /**
     * 保存服务窗口事项关联信息
     *
     * @param windowId   窗口id
     * @param itemVerIds 事项版本id集
     */
    void saveAeaServiceWindowItem(String windowId, String[] itemVerIds);

    /**
     * 保存服务窗口用户关联信息
     *
     * @param windowId 窗口id
     * @param userIds  用户id集
     */
    void saveAeaServiceWindowUsers(String windowId, String[] userIds);

    /**
     * 保存服务窗口阶段关联信息
     *
     * @param windowId 窗口id
     * @param stageIds 阶段stageIds集合
     */
    void saveAeaServiceWindowStage(String windowId, String[] stageIds);

    /**
     * 分页获取事项操作指南未选择窗口数据
     *
     * @param windowItem
     * @param page
     * @return
     */
    PageInfo<AeaServiceWindow> listItemUnselectedWindowByPage(AeaServiceWindowItem windowItem, Page page);

    /**
     * 分页获取阶段操作指南未选择窗口数据
     *
     * @param windowStage
     * @param page
     * @return
     */
    PageInfo<AeaServiceWindow> listStageUnselectedWindowByPage(AeaServiceWindowStage windowStage, Page page);


    /**
     * 删除窗口附件
     *
     * @param windowId
     * @param detailId
     * @param rootOrgId
     */
    void delServiceWindowAtt(String windowId, String detailId, String rootOrgId) throws Exception;

    /**
     * 分页
     *
     * @param aeaServiceWindowItem
     * @param page
     * @return
     */
    PageInfo<AeaServiceWindowItem> listAeaServiceWindowItemByPage(AeaServiceWindowItem aeaServiceWindowItem, Page page);

    /**
     * 根据ID查询下级行政区划树
     *
     * @param orgId
     * @return
     */
    ElementUiRsTreeNode listRegionTreeByOrgId(String orgId);

    List<BscAttForm> findWindowMapAtt(String windowId);
}
