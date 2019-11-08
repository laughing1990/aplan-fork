package com.augurit.aplanmis.supermarket.notice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.mapper.AeaImProjPurchaseMapper;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.augurit.aplanmis.supermarket.notice.service.AeaImSelectionNoticeService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/19/019 18:33
 */
@Service
@Transactional
public class AeaImSelectionNoticeServiceImpl implements AeaImSelectionNoticeService {
    @Autowired
    private AeaImProjPurchaseMapper projPurchaseMapper;

    @Override
    public List<AeaImProjPurchase> listSelectionNotice(QueryProjPurchaseVo projPurchase, Page page) {
        PageHelper.startPage(page);
        if (projPurchase.getServiceId() != null) {
            JSONArray jsonArray = JSONArray.parseArray(projPurchase.getServiceId());
            if (jsonArray.size() > 0)
                projPurchase.setServiceIds(jsonArray);
        }
        List<AeaImProjPurchase> list = projPurchaseMapper.listSelectionNotice(projPurchase);
        return list;
    }

    @Override
    public AeaImProjPurchase getSelectionNoticeByProjPurchaseId(String projPurchaseId) {
        AeaImProjPurchase projPurchase = projPurchaseMapper.getSelectionNoticeByProjPurchaseId(projPurchaseId);
        return projPurchase;
    }
}
