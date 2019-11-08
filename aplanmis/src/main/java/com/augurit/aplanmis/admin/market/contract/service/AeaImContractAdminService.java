package com.augurit.aplanmis.admin.market.contract.service;

import com.augurit.aplanmis.common.domain.AeaImContract;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/20
 */
public interface AeaImContractAdminService {
    List<AeaImContract> listAuditAeaImContractByPage(String keyword, String auditFlag, Page page)throws Exception;

    AeaImContract getAeaImContractByContractId(String contractId)throws Exception;

    void auditContract(String contractId, String auditFlag, String auditOpinion)throws Exception;
}
