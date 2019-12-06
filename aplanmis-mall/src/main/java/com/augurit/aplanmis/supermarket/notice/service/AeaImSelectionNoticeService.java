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

    /**
     * 根据采购项目ID查询采购项目详情
     *
     * @param projPurchaseId 采购项目ID
     * @return AeaImProjPurchase
     * @throws Exception E
     */
    SelectionNoticeVo getSelectionNoticeByProjPurchaseId(String projPurchaseId) throws Exception;
}
