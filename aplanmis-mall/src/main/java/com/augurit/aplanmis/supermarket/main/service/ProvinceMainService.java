package com.augurit.aplanmis.supermarket.main.service;

import com.augurit.aplanmis.common.domain.AeaImContract;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.augurit.aplanmis.supermarket.main.vo.province.ProvinceIndexData;
import com.augurit.aplanmis.supermarket.main.vo.province.ProvinceRunSituationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Calendar;
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

    public ProvinceIndexData getProvinceIndexData() throws Exception {
//        ProvinceIndexData vo = new ProvinceIndexData();

        //项目采购---
        List<AeaImProjPurchase> projPurchaseVos = aeaImProjPurchaseMapper.listPublicProjPurchaseByQueryProjPurchaseVo(new QueryProjPurchaseVo());
//        vo.changeToProjPurchase(projPurchaseVos);

        // 中选公告
        List<AeaImProjPurchase> selectionNoticeList = aeaImProjPurchaseMapper.listSelectionNotice(new QueryProjPurchaseVo());
//        vo.changeToPurchaseSelect(selectionNoticeList);

        // 合同公告
        List<AeaImContract> contracts = aeaImContractMapper.listAuditAeaImContract(new AeaImContract("1", "1", topOrgId));
//        vo.changeToPurchaseContract(contracts);

        //服务列表
        List<AeaImService> aeaImServices = aeaImServiceMapper.listAeaImService(new AeaImService("1", topOrgId));
//        vo.changeToImService(aeaImServices);


//        return vo;
        return new ProvinceIndexData(projPurchaseVos, selectionNoticeList, contracts, aeaImServices);
    }

    public ProvinceRunSituationData getProvinceRunSituationData(String queryType) throws Exception {
        if (StringUtils.isEmpty(queryType)) {
            queryType = "A";
        }
        queryType = queryType.toUpperCase();
        String startDate;
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String template = "%s%s%s";
        switch (queryType) {
            case "D":
                startDate = String.format(template, year, month, day);
                break;
            case "M":
                startDate = String.format(template, year, month, "01");
                break;
            case "Y":
                startDate = String.format(template, year, "01", "01");
                break;
            default:
                startDate = null;
                break;
        }
        //入驻中介机构数量
        int agentUnitServiceNum = agentUnitServiceMapper.listCheckinUnitNum(startDate);
        //已发布的中介服务事项--
        int agentItemServiceNum = aeaItemBasicMapper.listAgentItemServiceNum(startDate);
        //已完成的采购需求---
        int finishPurchaseNum = aeaImProjPurchaseMapper.listAeaImProjPurchaseNum(startDate);
        //项目采购---
        int projPurchaseNum = aeaImProjPurchaseMapper.listPublicProjPurchaseNum(startDate);
        return new ProvinceRunSituationData(agentUnitServiceNum, agentItemServiceNum, finishPurchaseNum, projPurchaseNum);
    }
}
