package com.augurit.aplanmis.common.service.window;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaServiceWindowStage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/25
 */
public interface AeaServiceWindowStageService {

    /**
     * 当前人员所在窗口下阶段是否属地办理
     *
     * @param stageId
     * @return 办理范围，0表示全市通办，1表示属地办理
     */
    String findCurrentUserAeaServiceWindowStage(String stageId);

    /**
     * 当前人员所在窗口下阶段是否属地办理
     *
     * @return 办理范围，0表示全市通办，1表示属地办理
     */
    List<AeaServiceWindowStage> findCurrentUserAeaServiceWindowStage();

    /**
     * 查询当前窗口人员可以办理的阶段
     *
     * @return 阶段列表
     */
    List<AeaParStage> findCurrentUserWindowStage();

    /**
     * 当前窗口人员是否有办理阶段的权限。多个阶段只要有一个没有权限则返回false
     *
     * @param stageId 阶段ID
     * @return true：当前窗口人员有办理权限，false：当前窗口人员没有办理权限
     */
    boolean hasAuthOnCurrentUser(String... stageId);

    /**
     * 查询窗口可办理阶段
     *
     * @param windowId 窗口ID
     * @return 阶段列表
     */
    List<AeaParStage> findWindowStageByWindowId(String windowId);

    /**
     * 分页查询窗口可办理阶段
     *
     * @param windowId 窗口ID
     * @param page     分页对象
     * @return 阶段列表
     */
    PageInfo<AeaParStage> listWindowStageByWindowId(String windowId, Page page);

    /**
     * 搜索窗口可办理阶段
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（阶段名称）
     * @return 阶段列表
     */
    List<AeaParStage> findWindowStageByKeyword(String windowId, String keyword);

    /**
     * 分页搜索窗口可办理阶段
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（阶段名称）
     * @param page     分页对象
     * @return 阶段列表
     */
    PageInfo<AeaParStage> listWindowStageByKeyword(String windowId, String keyword, Page page);

    /**
     * 查询窗口可办理阶段
     *
     * @param windowId 窗口ID
     * @return 阶段列表
     */
    List<AeaParStage> findActiveWindowStageByWindowId(String windowId);

    /**
     * 分页查询窗口可办理阶段
     *
     * @param windowId 窗口ID
     * @param page     分页对象
     * @return 阶段列表
     */
    PageInfo<AeaParStage> listActiveWindowStageByWindowId(String windowId, Page page);

    /**
     * 搜索窗口可办理阶段
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（阶段名称，阶段编码）
     * @return 阶段列表
     */
    List<AeaParStage> findActiveWindowStageByKeyword(String windowId, String keyword);

    /**
     * 分页搜索窗口可办理阶段
     *
     * @param windowId 窗口ID
     * @param keyword  查询关键词（阶段名称）
     * @param page     分页对象
     * @return 阶段列表
     */
    PageInfo<AeaParStage> listActiveWindowStageByKeyword(String windowId, String keyword, Page page);

    /**
     * 通过id删除阶段窗口关联关系(后端)
     *
     * @param id
     */
    void delAeaServiceWindowStageById(String id);

    /**
     * 批量删除阶段窗口关联关系(后端)
     *
     * @param ids
     */
    void batchDelAeaServiceWindowStageByIds(String[] ids);

    /**
     * 列表获取 (后端)
     *
     * @param aeaServiceWindowStage
     * @return
     */
    List<AeaServiceWindowStage> listAeaServiceWindowStage(AeaServiceWindowStage aeaServiceWindowStage);

    /**
     * 分页获取(后端)
     *
     * @param aeaServiceWindowStage
     * @return
     */
    PageInfo<AeaServiceWindowStage> listAeaServiceWindowStage(AeaServiceWindowStage aeaServiceWindowStage, Page page) ;

    /**
     * 批量保存阶段与窗口关联(后端)
     *
     * @param stageId
     * @param windIds
     */
    void batchSaveStageWindows(String stageId, String[] windIds);

    /**
     * 保存窗口与阶段关联
     * @param windowId
     * @param stageIds
     */
    void saveWindowStage(String windowId, String... stageIds);

    /**
     * 列出所有的主题和阶段（树结构）
     *
     * @return
     */
    List<ElementUiRsTreeNode> listAllStageTree();

    /**
     * 查询当前窗口人员某个主题下可以办理的阶段
     * @param themeId 主题ID
     * @return 阶段列表
     */
    List<AeaParStage> findCurrentUserWindowStageByThemeId(String themeId) throws Exception;

    /**
     * 批量保存窗口阶段
     *
     * @param windowId
     * @param aeaServiceWindowStageList
     */
    void saveWindowStage(String windowId, List<AeaServiceWindowStage> aeaServiceWindowStageList);

    /**
     * 查询窗口阶段
     *
     * @param windowId
     * @return
     */
    List<AeaServiceWindowStage> findAeaServiceWindowStageByWindowId(String windowId, String keyword);
}
