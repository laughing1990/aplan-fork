package com.augurit.aplanmis.supermarket.notice.service.impl;

import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.mapper.AeaImProjPurchaseMapper;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.augurit.aplanmis.supermarket.notice.service.AeaImContractNoticeService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AeaImContractNoticeServiceImpl implements AeaImContractNoticeService {
    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private IBscAttService bscAtService;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String topOrgId;

    @Override
    public List<AeaImProjPurchase> listContractNotice(QueryProjPurchaseVo projPurchase, Page page) {
        PageHelper.startPage(page);
        List<AeaImProjPurchase> list = aeaImProjPurchaseMapper.listContractNotice(projPurchase);
        return list;
    }

    @Override
    public AeaImProjPurchase getContractNoticeByProjPurchaseId(String projPurchaseId) {
        AeaImProjPurchase projPurchase = aeaImProjPurchaseMapper.getContractNoticeByProjPurchaseId(projPurchaseId);
        // 合同附件
        if (projPurchase != null && projPurchase.getContract() != null) {
            projPurchase.getContract().setBscAttForms(bscAtService.listAttLinkAndDetailByTablePKRecordId("AEA_IM_CONTRACT", "CONTRACT_ID", projPurchase.getContractId(), topOrgId));
        }
        return projPurchase;
    }
}
