package com.augurit.aplanmis.admin.market.evaluation.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.evaluation.service.AeaImServiceEvaluationAdminService;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaImServiceEvaluation;
import com.augurit.aplanmis.common.mapper.AeaImServiceEvaluationMapper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/21
 */
@Service
@Transactional
public class AeaImServiceEvaluationAdminServiceImpl implements AeaImServiceEvaluationAdminService {

    @Autowired
    private AeaImServiceEvaluationMapper aeaImServiceEvaluationMapper;

    @Override
    public List<AeaImServiceEvaluation> listAuditServiceEvaluationByPage(String keyword, String auditFlag, Page page)throws Exception{
        AeaImServiceEvaluation aeaImServiceEvaluation = new AeaImServiceEvaluation();

        if(StringUtils.isNotBlank(keyword)) {
            aeaImServiceEvaluation.setKeyword(keyword);
        }
        if(StringUtils.isNotBlank(auditFlag)) {
            aeaImServiceEvaluation.setAuditFlag(auditFlag);
        }
        aeaImServiceEvaluation.setIsDelete(DeletedStatus.NOT_DELETED.getValue());

        PageHelper.startPage(page);

        return aeaImServiceEvaluationMapper.listAuditServiceEvaluation(aeaImServiceEvaluation);
    }

    @Override
    public AeaImServiceEvaluation getAeaImServiceEvaluationByServiceEvaluationId(String serviceEvaluationId)throws Exception{

        if(StringUtils.isBlank(serviceEvaluationId)){
            throw new InvalidParameterException(serviceEvaluationId);
        }

        AeaImServiceEvaluation aeaImServiceEvaluation = new AeaImServiceEvaluation();

        aeaImServiceEvaluation.setServiceEvaluationId(serviceEvaluationId);


        List<AeaImServiceEvaluation> list = aeaImServiceEvaluationMapper.listAuditServiceEvaluation(aeaImServiceEvaluation);

        if(!list.isEmpty()){
            return list.get(0);
        }

        return null;
    }

    @Override
    public void auditEvaluation(String serviceEvaluationId,String auditFlag,String auditOpinion)throws Exception{
        if(StringUtils.isBlank(serviceEvaluationId) || StringUtils.isBlank(auditFlag)){
            throw new InvalidParameterException(serviceEvaluationId,auditFlag);
        }

        AeaImServiceEvaluation aeaImServiceEvaluation = new AeaImServiceEvaluation();
        aeaImServiceEvaluation.setServiceEvaluationId(serviceEvaluationId);
        aeaImServiceEvaluation.setAuditFlag(auditFlag);
        aeaImServiceEvaluation.setAuditTime(new Date());
        aeaImServiceEvaluation.setAuditOpinion(auditOpinion);

        aeaImServiceEvaluationMapper.updateAeaImServiceEvaluation(aeaImServiceEvaluation);
    }
}
