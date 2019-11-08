package com.augurit.aplanmis.common.service.admin.par;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
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

    List<AeaParThemeVer> listAeaParThemeVer(AeaParThemeVer aeaParThemeVer);

    List<AeaParThemeVer> listThemeVerSyncZTree(String themeId);

    AeaParThemeVer getMaxNumActiveVerByThemeId(String themeId, String rootOrgId);

    AeaParThemeVer copyThemeVerRelData(String themeId, String themeVerId) throws Exception;

    void testRunOrPublished(String themeId, String themeVerId, Double verNum, String type);

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
}
