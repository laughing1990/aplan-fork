package com.augurit.aplanmis.supermarket.agentService.service;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.dto.AeaCreditSummaryAllDto;
import com.augurit.aplanmis.common.dto.AeaCreditSummaryDto;
import com.augurit.aplanmis.common.mapper.AeaImServiceItemMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaItemServiceBasicMapper;
import com.augurit.aplanmis.common.mapper.AgentUnitServiceMapper;
import com.augurit.aplanmis.supermarket.agentService.vo.AeaItemServiceBasicVo;
import com.augurit.aplanmis.supermarket.agentService.vo.AgentServiceItemDetailVo;
import com.augurit.aplanmis.supermarket.agentService.vo.ItemDetailVo;
import com.augurit.aplanmis.supermarket.credit.service.SuperMarketCreditSummaryService;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentServiceService {
    @Autowired
    private AgentUnitServiceMapper agentUnitServiceMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaItemServiceBasicMapper aeaItemServiceBasicMapper;

    @Autowired
    private AeaImServiceItemMapper aeaImServiceItemMapper;

    @Autowired
    private SuperMarketCreditSummaryService summaryService;

    /**
     * 入住机构，入住公示
     *
     * @param agentUnitService 查询条件  [applicant,serviceId,publishTimeOrderSort,auditFlag,startOrderSort]
     * @param page             分页参数 pageNum pageSize
     * @return List<AgentUnitService>
     */
    public List<AgentUnitService> listCheckinUnit(AgentUnitService agentUnitService, Page page) {
        PageHelper.startPage(page);

        String serviceId = agentUnitService != null ? agentUnitService.getServiceId() : null;
        if (StringUtils.isNotBlank(serviceId)) {
            try {
                JSONArray jsonArray = JSONArray.parseArray(serviceId);
                if (jsonArray.size() > 0)
                    agentUnitService.setServiceIds(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return agentUnitServiceMapper.listCheckinUnit(agentUnitService);
    }

    /**
     * 入住服务
     *
     * @param agentUnitService 询条件  [applicant,serviceId,publishTimeOrderSort,auditFlag,startOrderSort]
     * @param page             分页参数 pageNum pageSize
     * @return List<AgentUnitService>
     */
    public List<AgentUnitService> listCheckinService(AgentUnitService agentUnitService, Page page) {
        PageHelper.startPage(page);
        if (agentUnitService != null && StringUtils.isNotBlank(agentUnitService.getServiceId())) {
            try {
                JSONArray jsonArray = JSONArray.parseArray(agentUnitService.getServiceId());
                if (jsonArray.size() > 0)
                    agentUnitService.setServiceIds(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return agentUnitServiceMapper.listCheckinService(agentUnitService);
    }

    /**
     * 中介服务事项
     *
     * @param aeaItemService 查询参数 agentItemName oagentOrgId 对应中介事项的部门ID，用逗号分隔
     * @param page           分页参数 pageSize pageNo
     * @return
     */
    public List<AeaItemService> listServiceItem(AeaItemService aeaItemService, Page page) {
        if (aeaItemService != null && StringUtils.isNotBlank(aeaItemService.getAgentOrgId())) {
            aeaItemService.setOrgIds(aeaItemService.getAgentOrgId().split(","));
        }

        if (aeaItemService != null && StringUtils.isNotBlank(aeaItemService.getServiceId()) && !"[]".equals(aeaItemService.getServiceId())) {
            try {
                JSONArray jsonArray = JSONArray.parseArray(aeaItemService.getServiceId());
                if (jsonArray.size() > 0)
                    aeaItemService.setServiceIds(jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PageHelper.startPage(page);
        return aeaItemBasicMapper.listAgentItemService(aeaItemService);
    }

    /**
     * 查询中介服务事项对应的部门
     *
     * @return
     */
    public List<AeaItemService> listServiceItemOrg() {
        return aeaItemBasicMapper.listServiceItemOrg();
    }

    /**
     * 获取中介事项详情
     *
     * @param itemBasicId
     * @return
     */
    public AgentServiceItemDetailVo serviceItemDetail(String itemBasicId) throws Exception {
        AgentServiceItemDetailVo agentServiceItemDetailVo = new AgentServiceItemDetailVo();
        AgentItemDetail agentItemDetail = aeaItemBasicMapper.getAgentItemBasicById(itemBasicId);

        if (agentItemDetail != null) {
            BeanUtils.copyProperties(agentItemDetail, agentServiceItemDetailVo);

            //查询中介事项设立依据列表
            AeaItemServiceBasic aeaItemServiceBasic = new AeaItemServiceBasic();
            aeaItemServiceBasic.setTableName("AEA_ITEM_VER");
            aeaItemServiceBasic.setPkName("ITEM_VER_ID");
            aeaItemServiceBasic.setRecordId(agentItemDetail.getItemVerId());
            List<AeaItemServiceBasic> aeaItemServiceBasics = aeaItemServiceBasicMapper.listAeaItemServiceBasic(aeaItemServiceBasic);
            agentServiceItemDetailVo.setAeaItemServiceBasicList(AeaItemServiceBasicVo.convert2list(aeaItemServiceBasics));

            //查询中介事项对应的行政事项列表
            List<AeaItemBasic> aeaItemBasics = aeaItemBasicMapper.listItemBasicByAgentItemId(agentItemDetail.getItemId());
            agentServiceItemDetailVo.setAeaItemBasicList(ItemDetailVo.conver2List(aeaItemBasics));
        }

        return agentServiceItemDetailVo;
    }


    public void getCredCreditSummary(List<AgentUnitService> agentUnitServices) throws Exception {
        List<AeaCreditSummaryDto>  acsds = new ArrayList<AeaCreditSummaryDto>();
        for(AgentUnitService agentUnitService:agentUnitServices){
            String bizType = "u";
            String bizCode = agentUnitService.getUnifiedSocialCreditCode();
            List<AeaCreditSummaryAllDto> creditList = summaryService.listCreditSummaryDetailByByBizCode(bizType, bizCode);
            for(AeaCreditSummaryAllDto aeaCreditSummaryAllDto:creditList){
                if("bad".equals(aeaCreditSummaryAllDto.getItemCode())){
                    AeaCreditSummaryAllDto acsad = new AeaCreditSummaryAllDto();
                    acsad.setItemCode(aeaCreditSummaryAllDto.getItemCode());
                    acsad.setItemName(aeaCreditSummaryAllDto.getItemName());
                    acsad.setSummaries(aeaCreditSummaryAllDto.getSummaries());
                    agentUnitService.setBreakFaithRecord(acsad);
                }
            }

        }
    }

}
