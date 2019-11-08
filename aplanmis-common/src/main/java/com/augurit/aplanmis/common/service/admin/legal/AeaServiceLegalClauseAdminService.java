package com.augurit.aplanmis.common.service.admin.legal;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.aplanmis.common.domain.AeaServiceLegalClause;
import com.github.pagehelper.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/25 025 15:18
 * @desc
 **/
public interface AeaServiceLegalClauseAdminService {
    /**
     * 获取法律法规条款
     * @param id 条款id
     * @return
     * @throws Exception
     */
    AeaServiceLegalClause getAeaServiceLegalClauseById(String id) throws Exception;

    /**
     * 新增法律法规条款
     * @param legalClause
     */
    void saveAeaServiceLegalClause(AeaServiceLegalClause legalClause);

    /**
     * 保存法律法规条款
     * @param legalClause
     */
    void updateAeaServiceLegalClause(AeaServiceLegalClause legalClause);

    /**
     * 保存法律法规条款附件
     * @param request
     * @param legalClause
     * @throws Exception
     */
    void updateAeaServiceLegalClauseAndAtt(HttpServletRequest request, AeaServiceLegalClause legalClause)throws Exception;

    /**
     * 新增法律法规条款附件
     * @param request
     * @param legalClause
     * @throws Exception
     */
    void saveAeaServiceLegalClauseAndAtt(HttpServletRequest request, AeaServiceLegalClause legalClause) throws Exception;

    /**
     * 删除法律法规
     * @param id 条款id
     * @throws Exception
     */
    void deleteAeaServiceLegalClauseById(String id) throws Exception;

    /**
     * 获取最大排序号
     *
     * @return
     * @param rootOrgId
     */
    Long getMaxSortNo(String rootOrgId);

    List<AeaServiceLegalClause> listItemServiceLegalNoPage(String itemVerId) throws Exception;

    EasyuiPageInfo<AeaServiceLegalClause> listAeaItemServiceLegalClause(String itemVerId,String keyword, Page page);
}
