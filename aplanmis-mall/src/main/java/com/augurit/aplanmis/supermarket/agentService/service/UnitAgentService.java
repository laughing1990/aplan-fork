package com.augurit.aplanmis.supermarket.agentService.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.supermarket.agentService.vo.AgentUnitDetailVo;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xioahut
 * 中介机构服务
 */
@Service
public class UnitAgentService {
    @Autowired
    private AeaImUnitServiceMapper aeaImUnitServiceMapper;

    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;

    @Autowired
    private AeaImServiceMajorMapper aeaImServiceMajorMapper;

    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String topOrgId;

    public AgentUnitDetailVo getAgentUnitDetail(String unitInfoId) {
        AgentUnitDetailVo vo = new AgentUnitDetailVo();
        AeaUnitInfo aeaUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);

        if (aeaUnitInfo != null) {
            List<AeaLinkmanInfo> linkmanInfos = aeaLinkmanInfoMapper.findAllUnitLinkman(unitInfoId);
            BeanUtils.copyProperties(aeaUnitInfo, vo);

            if (linkmanInfos != null && linkmanInfos.size() > 0) {
                vo.setContact(linkmanInfos.get(0).getLinkmanName());
                vo.setMobile(linkmanInfos.get(0).getLinkmanMobilePhone());
                vo.setEmail(linkmanInfos.get(0).getLinkmanMail());
            }
            BscDicCodeItem itemByTypeCodeAndItemCode = bscDicCodeService.getItemByTypeCodeAndItemCode("IDTYPE", vo.getIdtype(), "0368948a-1cdf-4bf8-a828-71d796ba89f6");
            if (itemByTypeCodeAndItemCode != null) {
                vo.setIdTypeName(itemByTypeCodeAndItemCode.getItemName());
            }
        }
        return vo;
    }

    /**
     * 机构中选记录列表
     *
     * @param page
     * @return
     */
    public List<AeaUnitBiddingAndEvaluation> listWinBidService(String unitInfoId, String projName, String serviceId, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaUnitBiddingAndEvaluation> list = aeaImUnitBiddingMapper.listWinBidService(unitInfoId, projName, serviceId);
        if (list.size() > 0) {
            int size = list.size();
            int total = 0;
            String[] comEva = list.stream().map(bidding -> bidding.getCompEvaluation()).toArray(String[]::new);
            for (String s : comEva) {
                if (StringUtils.isNotBlank(s)) {
                    total += Integer.valueOf(s);
                }

            }
            double i = (double) (total / size);
            for (AeaUnitBiddingAndEvaluation bid : list) {
                bid.setAvgCompEvaluation(i);
            }
        }

        return list;
    }

    /**
     * 查询单位人员资格证书
     */
    public List<AgentLinkmanCert> listAgentLinkmanCertinst(String unitInfoId, String linkmanName, Page page) {
        PageHelper.startPage(page);
        List<AgentLinkmanCert> agentLinkmanCerts = aeaLinkmanInfoMapper.listAgentLinkmanCertinst(unitInfoId, linkmanName);
        return agentLinkmanCerts;
    }

    /**
     * 查询中介机构下资格证书列表
     */
    public List<AgentCertinst> listAgentCertinst(String unitInfoId, String unitServiceId) {
        List<AgentCertinst> list = aeaHiCertinstMapper.listAgentCertinst(unitInfoId, unitServiceId);
        return list;
    }


    /**
     * 查询证照资质证书及对应的专业树
     *
     * @param certinstId
     * @return
     */
    public AgentCertinst getCertinstAndMajorTree(String certinstId) {
        AgentCertinst agentCertinst = aeaHiCertinstMapper.getAgentCertinstDetail(certinstId);
        if (agentCertinst != null) {
            List<AeaImServiceMajor> majorList = aeaImServiceMajorMapper.listMajorByCertinstId(certinstId);

            if (majorList != null && majorList.size() > 0) {
                List<AeaImServiceMajor> majorTree = new ArrayList<AeaImServiceMajor>();

                for (AeaImServiceMajor aeaImServiceMajor : majorList) {
                    if (aeaImServiceMajor.getParentMajorId() == null) {
                        aeaImServiceMajor.setChildren(getMajorChildrenList(aeaImServiceMajor.getMajorId(), majorList));
                        majorTree.add(aeaImServiceMajor);
                    }
                }

                agentCertinst.setMajorTree(majorTree);
            }


            try {
                List<BscAttFileAndDir> fileList = bscAttDetailMapper.searchFileAndDirsSimple(null, topOrgId, "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstId});
                agentCertinst.setBscAttFileAndDirs(fileList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return agentCertinst;
    }


    /**
     * 查询中介机构综合评价
     *
     * @param unitInfoId
     * @return
     */
    public AeaUnitBiddingAndEvaluation getUnitServiceEvaluation(String unitInfoId) throws Exception {
        return aeaImUnitBiddingMapper.getUnitServiceEvaluation(unitInfoId);
    }

    /**
     * 查询中介机构业务授权人信息
     */
    public List listAgentHeadLinkman(String unitInfoId) {
        List list = new ArrayList();
        List<AgentLinkmanCert> agentLinkmanCerts = aeaLinkmanInfoMapper.listAgentHeadLinkman(unitInfoId);
        if (agentLinkmanCerts != null && agentLinkmanCerts.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (AgentLinkmanCert agentLinkmanCert : agentLinkmanCerts) {
                Map<String, Object> serviceMap = (Map<String, Object>) map.get(agentLinkmanCert.getServiceId());
                if (serviceMap == null) {
                    serviceMap = new HashMap<String, Object>();
                    serviceMap.put("serviceId", agentLinkmanCert.getServiceId());
                    serviceMap.put("serviceName", agentLinkmanCert.getServiceName());
                    serviceMap.put("linkmanList", new ArrayList<Map<String, String>>());
                    map.put(agentLinkmanCert.getServiceId(), serviceMap);
                    list.add(serviceMap);
                }

                Map<String, String> linkmanMap = new HashMap<String, String>();
                linkmanMap.put("linkmanName", agentLinkmanCert.getLinkmanName());
                linkmanMap.put("linkmanMobilePhone", agentLinkmanCert.getLinkmanMobilePhone());
                List<Map<String, String>> linkmanList = (List<Map<String, String>>) serviceMap.get("linkmanList");
                linkmanList.add(linkmanMap);
            }
        }
        return list;
    }

    /**
     * 查询中介机构发布服务详情
     */
    public AeaImUnitService getAeaImUnitServiceDetail(String unitServiceId) throws Exception {
        AeaImUnitService aeaImUnitService = aeaImUnitServiceMapper.getAeaImUnitServiceDetail(unitServiceId);
        return aeaImUnitService;
    }

    private List<AeaImServiceMajor> getMajorChildrenList(String parentMajorId, List<AeaImServiceMajor> majorList) {
        List<AeaImServiceMajor> children = new ArrayList<AeaImServiceMajor>();

        for (AeaImServiceMajor aeaImServiceMajor : majorList) {
            if (parentMajorId != null && parentMajorId.equals(aeaImServiceMajor.getParentMajorId())) {
                aeaImServiceMajor.setChildren(getMajorChildrenList(aeaImServiceMajor.getMajorId(), majorList));
                children.add(aeaImServiceMajor);
            }
        }
        return children;
    }
}
