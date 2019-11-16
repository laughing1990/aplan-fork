package com.augurit.aplanmis.supermarket.main.service;

import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.augurit.aplanmis.supermarket.main.vo.IndexDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProvinceMainService {

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;

    @Autowired
    private AgentUnitServiceMapper agentUnitServiceMapper;

    @Autowired
    private AeaImContractMapper aeaImContractMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String topOrgId;

    public IndexDataVo getProvinceIndexData() throws Exception {

        IndexDataVo vo = new IndexDataVo();
        //入驻中介机构的
        List<AgentUnitService> agentUnitServices = agentUnitServiceMapper.listCheckinUnit(new AgentUnitService());
        vo.setAgentUnitCount(agentUnitServices.size());

        //已发布的中介服务事项
        List<AeaItemService> aeaItemBasics = aeaItemBasicMapper.listAgentItemService(new AeaItemService());
        vo.setItemServiceCount(aeaItemBasics.size());

        //已完成的采购需求
        List<AeaImProjPurchase> finishPurchases = aeaImProjPurchaseMapper.listAeaImProjPurchase(new AeaImProjPurchase("2", topOrgId));
        vo.setFinishedPurchaseCount(finishPurchases.size());

        //项目采购
        List<AeaImProjPurchase> projPurchaseVos = aeaImProjPurchaseMapper.listPublicProjPurchaseByQueryProjPurchaseVo(new QueryProjPurchaseVo());
        vo.changeToProjPurchase(projPurchaseVos);
        vo.setPublishedPurchaseCount(projPurchaseVos.size());

        // 中选公告
        List<AeaImProjPurchase> selectionNoticeList = aeaImProjPurchaseMapper.listSelectionNotice(new QueryProjPurchaseVo());
        vo.changeToPurchaseSelect(selectionNoticeList);

        // 合同公告
        List<AeaImContract> contracts = aeaImContractMapper.listAuditAeaImContract(new AeaImContract("1", "1", topOrgId));

        vo.changeToPurchaseContract(contracts);

        //服务列表
        List<AeaImService> aeaImServices = aeaImServiceMapper.listAeaImService(new AeaImService("1", topOrgId));
        vo.changeToImService(aeaImServices);


        return vo;
    }
}
