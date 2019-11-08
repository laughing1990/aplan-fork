package com.augurit.aplanmis.supermarket.main.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.augurit.aplanmis.supermarket.main.service.SupMainService;
import com.augurit.aplanmis.supermarket.main.vo.IndexDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SupMainServiceImpl implements SupMainService {

    private static Logger logger = LoggerFactory.getLogger(SupMainServiceImpl.class);

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;

    @Autowired
    private IBscAttService bscAttService;

    @Autowired
    private UploaderFactory uploaderFactory;

    @Autowired
    private AgentUnitServiceMapper agentUnitServiceMapper;

    @Autowired
    private AeaImContractMapper aeaImContractMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String topOrgId;

    @Override
    public List<AeaUnitInfo> listAgencyOrg(String agencyName, String serviceType, String stimeOrder, String countOrder, String commentOrder) {
        List<AeaUnitInfo> list = aeaUnitInfoMapper.listAgencyOrg(agencyName, serviceType, stimeOrder, countOrder, commentOrder);
        return list;
    }

    @Override
    public List<AeaUnitInfo> oneAgency(String agencyName) {
        List<AeaUnitInfo> list = aeaUnitInfoMapper.oneAgency(agencyName);
        return list;
    }

    @Override
    public List<AeaItemBasic> listService(String serviceType) {
        List<AeaItemBasic> list = aeaItemBasicMapper.listService(serviceType);
        return list;
    }

    @Override
    public List<AeaItemBasic> listItemName(String itemName) {
        List<AeaItemBasic> list = aeaItemBasicMapper.listItemName(itemName);
        return list;
    }


    @Override
    public IndexDataVo getIndexData() throws Exception {
        IndexDataVo vo = new IndexDataVo();
        //入驻中介机构的
        List<AgentUnitService> agentUnitServices = agentUnitServiceMapper.listCheckinUnit(new AgentUnitService());
        vo.setAgentUnitCount(agentUnitServices.size());

        //已发布的中介服务事项
        List<AeaItemService> aeaItemBasics = aeaItemBasicMapper.listAgentItemService(new AeaItemService());
        vo.setItemServiceCount(aeaItemBasics.size());

        //已完成的采购需求
        List<AeaImProjPurchase> finishPurchases = aeaImProjPurchaseMapper.listAeaImProjPurchase(new AeaImProjPurchase( "2", topOrgId));
        vo.setFinishedPurchaseCount(finishPurchases.size());

        //项目采购
        List<AeaImProjPurchase> projPurchaseVos = aeaImProjPurchaseMapper.listPublicProjPurchaseByQueryProjPurchaseVo(new QueryProjPurchaseVo());
        vo.changeToProjPurchase(projPurchaseVos);
        vo.setPublishedPurchaseCount(projPurchaseVos.size());

        // 中选公告
        List<AeaImProjPurchase> selectionNoticeList = aeaImProjPurchaseMapper.listSelectionNotice(new QueryProjPurchaseVo());
        vo.changeToPurchaseSelect(selectionNoticeList);

        // 合同公告
        List<AeaImContract> contracts = aeaImContractMapper.listAuditAeaImContract(new AeaImContract("1","1", topOrgId));

        vo.changeToPurchaseContract(contracts);

        //服务列表
        List<AeaImService> aeaImServices = aeaImServiceMapper.listAeaImService(new AeaImService("1", topOrgId));
        vo.changeToImService(aeaImServices);


        return vo;

    }

    @Override
    public Map getFileStream(String detailId) throws Exception {
        Map map = new HashMap();
        if (StringUtils.isNotBlank(detailId)) {
            BscAttDetail att = bscAttService.getAttDetailByDetailId(detailId);
            if (att != null) {
                String atName = att.getAttName();
                String attFormat = att.getAttFormat();
                if (!((StringUtils.isBlank(atName)) && !UploadType.DATABASE.getValue().equals(att.getStoreType()))) {
                    if (UploadType.MONGODB.getValue().equals(att.getStoreType())) {
                        //从MongoDB上下载
                        MongoDbAchieve mongoDbAchieve = (MongoDbAchieve) uploaderFactory.create(att.getStoreType());
                        byte[] content = mongoDbAchieve.getDownloadBytes(att.getDetailId());
                        if (content != null && content.length > 0) {
//                            String base64Content = new BASE64Encoder().encode(content);
                            Base64.Encoder encoder = Base64.getEncoder();
                            String base64Content = encoder.encodeToString(content);
                            base64Content = base64Content.replaceAll("\r\n|\n", "");
                            String fullBase64 = "data:image/" + attFormat + ";base64," + base64Content;
                            map.put("fullBase64", fullBase64);
                            map.put("fileName", atName);
                            map.put("fileType", attFormat);
                        }
                    }
                }
            }
        }
        return map;
    }
}

