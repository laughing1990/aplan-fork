package com.augurit.aplanmis.supermarket.notice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaParentProj;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaImProjPurchaseMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaParentProjMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.augurit.aplanmis.supermarket.notice.service.AeaImSelectionNoticeService;
import com.augurit.aplanmis.supermarket.notice.service.SelectionNoticeVo;
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
    @Autowired
    private AeaParentProjMapper aeaParentProjMapper;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

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
    public SelectionNoticeVo getSelectionNoticeByProjPurchaseId(String projPurchaseId) throws Exception {
        AeaImProjPurchase projPurchase = projPurchaseMapper.getSelectionNoticeByProjPurchaseId(projPurchaseId);
        if (null == projPurchase) return null;
        SelectionNoticeVo vo = new SelectionNoticeVo(projPurchase);
        String isApproveProj = projPurchase.getIsApproveProj();
        if (StringUtils.isNotBlank(isApproveProj) && "1".equals(isApproveProj)) {
            //查询关联的投资审批项目信息
            AeaParentProj parentProj = aeaParentProjMapper.getParentProjByProjInfoId(projPurchase.getProjInfoId());
            if (null != parentProj) {
                AeaProjInfo projInfo = aeaProjInfoService.getTransProjInfoDetail(parentProj.getParentProjId());
                vo.change2proj(projInfo);
            }
        }
//        PurchaseDetailVo vo = projPurchaseService.getPurchaseDetail(projPurchaseId);
        return vo;
    }
}
