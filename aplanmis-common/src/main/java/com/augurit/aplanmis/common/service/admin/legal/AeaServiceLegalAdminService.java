package com.augurit.aplanmis.common.service.admin.legal;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaServiceLegal;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/25 025 13:49
 * @desc
 **/
public interface AeaServiceLegalAdminService {

    /**
     * 删除法律法规
     * @param id id
     * @throws Exception
     */
    void deleteAeaServiceLegalById(String id) throws Exception;

    /**
     * 获取法律法规树
     * @param aeaServiceLegal
     * @return
     * @throws Exception
     */
    List<ZtreeNode> getListLegalZtreeNode(AeaServiceLegal aeaServiceLegal) throws Exception;

    /**
     * 保存法律法规附件
     * @param request
     * @param aeaServiceLegal
     * @throws Exception
     */
    void updateAeaServiceLegalAndAtt(HttpServletRequest request, AeaServiceLegal aeaServiceLegal) throws Exception;

    /**
     * 获取单个法律法规
     * @param id id
     * @return
     * @throws Exception
     */
    AeaServiceLegal getAeaServiceLegalById(String id) throws Exception;

    /**
     * 保存法律法规
     * @param aeaServiceLegal
     * @throws Exception
     */
    void updateAeaServiceLegal(AeaServiceLegal aeaServiceLegal) throws Exception;

    /**
     * 新增法律法规
     * @param aeaServiceLegal
     * @throws Exception
     */
    void saveAeaServiceLegal(AeaServiceLegal aeaServiceLegal) throws Exception;

    /**
     * 新增法律法规附件
     * @param request
     * @param aeaServiceLegal
     * @throws Exception
     */
    void saveAeaServiceLegalAndAtt(HttpServletRequest request, AeaServiceLegal aeaServiceLegal) throws Exception;

    /**
     * 删除附件
     *
     * @param type
     * @param bizId
     * @param detailId
     * @throws Exception
     */
    void delelteFile(String type, String bizId, String detailId)throws Exception;

    /**
     * 获取eUI形式法律法规树
     *
     * @param aeaServiceLegal
     * @return
     */
    List<ElementUiRsTreeNode> gtreeLegalAndClauseForEui(AeaServiceLegal aeaServiceLegal);

    /**
     * 获取某组织下的所有法律法规（不包括自己以及包含的自己子法律法规）
     *
     * @param legalId
     * @return
     */
    List<ElementUiRsTreeNode> listOtherLegalByLegalId(String legalId);

    /**
     * 设置父级
     *
     * @param curLegalId
     * @param targetLegalId
     * @return
     */
    AeaServiceLegal setParentLegal(String curLegalId, String targetLegalId);
}
