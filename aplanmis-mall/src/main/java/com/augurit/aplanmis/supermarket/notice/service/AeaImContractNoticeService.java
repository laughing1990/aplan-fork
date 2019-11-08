package com.augurit.aplanmis.supermarket.notice.service;

import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.github.pagehelper.Page;

import java.util.List;

public interface AeaImContractNoticeService {
    List<AeaImProjPurchase> listContractNotice(QueryProjPurchaseVo projPurchase, Page page);

    AeaImProjPurchase getContractNoticeByProjPurchaseId(String projPurchaseId);
}
