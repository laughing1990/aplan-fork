package com.augurit.aplanmis.common.service.admin.par;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 主题定义版本表-Service服务调用接口类
 */
public interface AeaParThemeVerAdminService {

    void saveAeaParThemeVer(AeaParThemeVer aeaParThemeVer)throws Exception;

    void updateAeaParThemeVer(AeaParThemeVer aeaParThemeVer);

    void deleteAeaParThemeVerById(String id);

    PageInfo<AeaParThemeVer> listAeaParThemeVer(AeaParThemeVer aeaParThemeVer, Page page);

    AeaParThemeVer getAeaParThemeVerById(String id);

    List<AeaParThemeVer> listAeaParThemeVerNoRelThemeInfo(AeaParThemeVer aeaParThemeVer);

    List<AeaParThemeVer> listAeaParThemeVer(AeaParThemeVer aeaParThemeVer);

    List<AeaParThemeVer> listThemeVerSyncZTree(String themeId);

    AeaParThemeVer getMaxNumActiveVerByThemeId(String themeId, String rootOrgId);

    AeaParThemeVer copyThemeVerRelData(String themeId, String themeVerId) throws Exception;

    void testRunOrPublished(String themeId, String themeVerId, Double verNum, String type, String oldVerStatus);

    /**
     * 上传主题版本附件
     *
     * @param themeVerId
     */
    void handleThemeVerAtt(String themeVerId, HttpServletRequest request) throws Exception;

    /**
     * 保存拖拉拽视图
     *
     * @param themeVerId
     * @param themeVerDiagram
     */
    void saveThemeVerDiagram(String themeVerId, String themeVerDiagram)throws Exception;

    /**
     * 在线生成运行图
     *
     * @param themeVerId
     * @throws Exception
     */
    ResultForm createBpmnDiagram(String themeVerId)throws Exception;
    ResultForm createBpmnDiagram2(String themeVerId)throws Exception;

    /**
     * 获取最大有效主题版本关联阶段数据
     *
     * @param rootOrgId
     */
    List<ZtreeNode> gtreeOkThemeRelStage(String rootOrgId);

    /**
     * 根据id获取对应的cell
     * @param id
     * @param cells
     * @return
     */
    Map getCellsEleById(String id, JSONArray cells);

    /**
     * 更新全景图阶段信息
     * @param aeaParStage
     */
    void updateThemeVerDiagramStage(AeaParStage aeaParStage);

    /**
     * 插入阶段到全景图
     * @param aeaParStage
     */
    void insertThemeVerDiagramStage(AeaParStage aeaParStage);

    /**
     * 绑定辅线阶段，移动或添加辅线及辅线事项
     * @param aeaParStage
     * @throws Exception
     */
    void bindAssitStage(AeaParStage aeaParStage) throws Exception;

    /**
     * 事项插入全景图
     * @param cells
     * @param stage
     * @param list
     * @param isOptionItem
     * @param hPool
     * @param activity
     * @param pool
     */
    void addItemToDiagram(JSONArray cells, AeaParStage stage, AeaParStageItem list, String isOptionItem, Map hPool, Map activity, Map pool);

    /**
     * 全景图中移除事项
     * @param cells
     * @param stage
     * @param stageItemId
     * @param isOptionItem
     */
    void removeEleFromDiagram(JSONArray cells, AeaParStage stage, String stageItemId, String isOptionItem);

    /**
     * 获取themver, 全景图 diagram, cells
     * @param stage
     * @return
     */
    Map<String, Object> getAeaParThemeVerAndCells(AeaParStage stage);

    /**
     * 调整父项高度
     * @param cells
     * @param type
     * @return
     */
    int ajaustPoolHeightAndReturnMaxHeight(JSONArray cells, String type);

    /**
     * 设置主线阶段高度一致
     * @param cells
     * @param type
     * @return
     */
    int setPoolHeightInCommon(JSONArray cells, String type);

    /**
     * 删除阶段
     * @param cells
     * @param stageId
     */
    void removeStageFromDiagram(JSONArray cells, String stageId);

    /**
     * 更新全景图中的事项名称
     * @param aeaItemBasic
     */
    void updateDiagramActivityName(AeaItemBasic aeaItemBasic);

    /**
     * 从所有全景图中移除有该事项的activity
     * @param itemId
     */
    void removeActivityFromDiagramInAllAeaThemeVer(String itemId);

    /**
     * 获取最大主题版本集合
     * 数据
     * @param themeId
     * @param rootOrgId
     * @return
     */
    List<AeaParThemeVer> listMaxThemeVerGroupByThemeId(String themeId, String rootOrgId);

    /**
     * 不包含在线运行图字段
     *
     * @param aeaParThemeVer
     * @return
     */
    List<AeaParThemeVer> listThemeVerNoRelThemeAndDiagramInfo(AeaParThemeVer aeaParThemeVer);
}
