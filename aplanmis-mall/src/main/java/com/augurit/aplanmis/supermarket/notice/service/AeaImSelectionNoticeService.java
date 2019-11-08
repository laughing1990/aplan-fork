package com.augurit.aplanmis.supermarket.notice.service;

import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/19/019 18:32
 */

public interface AeaImSelectionNoticeService {
    List<AeaImProjPurchase> listSelectionNotice(QueryProjPurchaseVo projPurchase, Page page);

    AeaImProjPurchase getSelectionNoticeByProjPurchaseId(String projPurchaseId);
}
