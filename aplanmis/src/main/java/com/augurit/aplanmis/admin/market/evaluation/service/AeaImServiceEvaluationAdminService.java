package com.augurit.aplanmis.admin.market.evaluation.service;

import com.augurit.aplanmis.common.domain.AeaImServiceEvaluation;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/21
 */
public interface AeaImServiceEvaluationAdminService {

    List<AeaImServiceEvaluation> listAuditServiceEvaluationByPage(String keyword, String auditFlag, Page page)throws Exception;

    AeaImServiceEvaluation getAeaImServiceEvaluationByServiceEvaluationId(String serviceEvaluationId)throws Exception;

    void auditEvaluation(String serviceEvaluationId, String auditFlag, String auditOpinion)throws Exception;


}
