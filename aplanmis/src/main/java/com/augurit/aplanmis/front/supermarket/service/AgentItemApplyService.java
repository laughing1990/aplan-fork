package com.augurit.aplanmis.front.supermarket.service;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.front.apply.service.AeaSeriesService;
import com.augurit.aplanmis.front.apply.vo.BuildProjUnitVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyDataVo;
import com.augurit.aplanmis.front.receive.service.ReceiveService;
import com.augurit.aplanmis.front.supermarket.vo.AgentItemApplyData;
import com.augurit.aplanmis.supermarket.apply.service.RestImApplyService;
import com.augurit.aplanmis.supermarket.apply.vo.ApplyinstResult;
import com.augurit.aplanmis.supermarket.apply.vo.ImItemApplyData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class AgentItemApplyService {
    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private AeaSeriesService aeaSeriesService;
    @Autowired
    AeaImContractMapper aeaImContractMapper;

    @Autowired
    AeaImServiceResultMapper aeaImServiceResultMapper;

    @Autowired
    AeaProjLinkmanMapper aeaProjLinkmanMapper;

    @Autowired
    AeaImBiddingPriceMapper aeaImBiddingPriceMapper;

    @Autowired
    AeaImAvoidUnitMapper aeaImAvoidUnitMapper;
    @Autowired
    private AeaImUnitRequireMapper aeaImUnitRequireMapper;
    @Autowired
    private AeaImMajorQualMapper aeaImMajorQualMapper;

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaImUnitServiceMapper aeaImUnitServiceMapper;

    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private RestImApplyService restImApplyService;

    /**
     * 中介事项现场登记 --> 收件，发起申报
     *
     * @param agentItemApplyData 申报参数
     * @return applyinstId 申请实例ID
     * @throws Exception
     */
    public String startSeriesFlow(AgentItemApplyData agentItemApplyData) throws Exception {
        String applyinstIdParam = agentItemApplyData.getApplyinstId();
        ImItemApplyData applyData = agentItemApplyData.createItemApplyData();
        ApplyinstResult result = restImApplyService.purchaseStartProcess(applyData);
        String applyinstId = result.getApplyinstId();
        String applyinstCode = result.getApplyinstCode();

       /* SeriesApplyDataVo vo = this.changeToSeriesApplyDataVo(agentItemApplyData);
        String applyinstId = aeaSeriesService.stageApplay(vo);
        agentItemApplyData.setApplyinstId(applyinstId);
        agentItemApplyData.setApplyinstCode(vo.getApplyinstCode());
        //保存采购项目信息
        this.savePurchaseProjInfo(agentItemApplyData);*/
        //保存受理回执，物料回执
        if (StringUtils.isBlank(applyinstIdParam)) {
            receiveService.saveReceive(new String[]{applyinstId}, new String[]{"1", "2"}, SecurityContext.getCurrentUserName(), "");
        }
        return applyinstId;
    }

    /**
     * 现场登记 --> 生成实例，打印回执
     *
     * @param agentItemApplyData 申报餐宿
     * @return applyinstId 申请实例ID
     * @throws Exception
     */
    public String instantiateSeriesFlow(AgentItemApplyData agentItemApplyData) throws Exception {
        // 如果是多次打印回执，直接返回申报实例
        if (StringUtils.isNotBlank(agentItemApplyData.getApplyinstId())) {
            return agentItemApplyData.getApplyinstId();
        }
        SeriesApplyDataVo vo = this.changeToSeriesApplyDataVo(agentItemApplyData);
        String applyinstId = aeaSeriesService.stagingApply(vo);
        agentItemApplyData.setApplyinstId(applyinstId);
        agentItemApplyData.setApplyinstCode(vo.getApplyinstCode());
        //保存采购项目信息
        this.savePurchaseProjInfo(agentItemApplyData);
        //保存受理回执，物料回执
        String[] receiveTypes = new String[]{"1", "2"};
        receiveService.saveReceive(new String[]{applyinstId}, receiveTypes, SecurityContext.getCurrentUserName(), "");
        return applyinstId;
    }

    /**
     * 现场登记 --> 不予受理，生成实例，启动流程，打印不受理回执
     *
     * @param agentItemApplyData 申报入参
     * @return applyinstId 申请实例ID
     */
    public String inadmissibleSeriesFlow(AgentItemApplyData agentItemApplyData) throws Exception {
        SeriesApplyDataVo vo = this.changeToSeriesApplyDataVo(agentItemApplyData);
        String applyinstId = aeaSeriesService.inadmissible(vo);
        agentItemApplyData.setApplyinstId(applyinstId);
        agentItemApplyData.setApplyinstCode(vo.getApplyinstCode());
        //保存采购项目信息
        this.savePurchaseProjInfo(agentItemApplyData);
        //保存不受理回执
        String[] receiveTypes = new String[]{"3"};
        receiveService.saveReceive(new String[]{applyinstId}, receiveTypes, SecurityContext.getCurrentUserName(), agentItemApplyData.getComments());
        return applyinstId;
    }

    private SeriesApplyDataVo changeToSeriesApplyDataVo(AgentItemApplyData data) {
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(data.getItemVerId(), SecurityContext.getCurrentOrgId());
        data.setIsParallel("0");
        data.setAppId(aeaItemBasic.getAppId());
        SeriesApplyDataVo vo = new SeriesApplyDataVo();
        BeanUtils.copyProperties(data, vo);
        vo.setProjInfoIds(data.getProjInfoId().split(","));
        return vo;
    }


    //保存采购项目信息
    public void savePurchaseProjInfo(AgentItemApplyData data) throws Exception {
        String projName = data.getProjName();

        AeaProjInfo aeaProjInfoCond = new AeaProjInfo();
        aeaProjInfoCond.setProjName(projName);
        List aeaProjInfoCondList = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfoCond);
        if (!aeaProjInfoCondList.isEmpty()) {
            throw new RuntimeException("项目名称已存在");
        }
        AeaImUnitRequire aeaImUnitRequire = data.getAeaImUnitRequire();

        if (aeaImUnitRequire == null) {
            throw new RuntimeException("缺少中介机构要求信息");
        }
        AeaImProjPurchase purchaseProjInfo = null;//data.createPurchaseProjInfo(data);

        //保存机构要求
        aeaImUnitRequire.setUnitRequireId(UuidUtil.generateUuid());
        aeaImUnitRequire.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaImUnitRequireMapper.insertAeaImUnitRequire(aeaImUnitRequire);


        purchaseProjInfo.setUnitRequireId(aeaImUnitRequire.getUnitRequireId());

        //保存资质专业要求
        List<AeaImMajorQual> aeaImMajorQuals = data.getAeaImMajorQuals();
        if (aeaImMajorQuals != null && "1".equals(aeaImUnitRequire.getIsQualRequire())) {
            for (AeaImMajorQual aeaImMajorQual : aeaImMajorQuals) {
                saveAeaImMajorQual(aeaImMajorQual, aeaImUnitRequire.getUnitRequireId());
            }
        }
        //保存采购项目
        AeaProjInfo aeaProjInfo = null;//data.createProjInfo(data);
        aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);
        purchaseProjInfo.setProjInfoId(aeaProjInfo.getProjInfoId());

        List<BuildProjUnitVo> buildProjUnits = data.getBuildProjUnitMap();
        String unitId = buildProjUnits.get(0).getUnitIds().get(0);
        //保存采购项目与联系人关联
        String linkmanInfoId = data.getLinkmanInfoId();
        if (StringUtils.isBlank(linkmanInfoId)) {
            List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.findAllUnitLinkman(unitId);

            for (AeaLinkmanInfo linkmanInfo : aeaLinkmanInfos) {
                if (linkmanInfo.getLinkmanType() != null && linkmanInfo.getLinkmanType().contains("u")) {
                    linkmanInfoId = linkmanInfo.getLinkmanInfoId();
                    break;
                }
            }
        }

        if (StringUtils.isNotBlank(linkmanInfoId)) {
            AeaProjLinkman aeaProjLinkman = new AeaProjLinkman();
            aeaProjLinkman.setProjLinkmanId(UuidUtil.generateUuid());
            aeaProjLinkman.setProjInfoId(aeaProjInfo.getProjInfoId());
            aeaProjLinkman.setLinkmanInfoId(linkmanInfoId);
            aeaProjLinkman.setType("link");
            aeaProjLinkman.setCreateTime(new Date());
            aeaProjLinkman.setCreater(SecurityContext.getCurrentUserName());
            aeaProjLinkmanMapper.insertAeaProjLinkman(aeaProjLinkman);
        }

        //保存审批项目
        if (StringUtils.isNotBlank(data.getProjInfoId())) {
            saveParentProjInfo(aeaProjInfo.getProjInfoId(), data.getProjInfoId());
        }
        if ("0".equals(data.getApplySubject())) {
            purchaseProjInfo.setPublishLinkmanInfoId(data.getApplyLinkmanId());
        } else {
            purchaseProjInfo.setPublishUnitInfoId(unitId);
        }


        aeaImProjPurchaseMapper.insertAeaImProjPurchase(purchaseProjInfo);

        //保存企业报价
        if (AeaImProjPurchase.BiddingType.自主选择.getType().equals(purchaseProjInfo.getBiddingType())) {
            if (StringUtils.isNotBlank(data.getAgentUnitInfoId())) {
                saveImUnitBidding(data, purchaseProjInfo.getProjPurchaseId());
            }
        }

        // 保存回避单位
        if (Status.ON.equals(data.getIsAvoid())) {
            String avoidUnitInfoIds = data.getAvoidUnitInfoIds();
            for (String avoidUnitInfoId : avoidUnitInfoIds.split(",")) {
                AeaImAvoidUnit aeaImAvoidUnit = new AeaImAvoidUnit();
                aeaImAvoidUnit.setAvoidUnitId(UuidUtil.generateUuid());
                aeaImAvoidUnit.setUnitInfoId(avoidUnitInfoId);
                aeaImAvoidUnit.setProjPurchaseId(purchaseProjInfo.getProjPurchaseId());
                aeaImAvoidUnit.setCreater(SecurityContext.getCurrentUserName());
                aeaImAvoidUnit.setCreateTime(new Date());
                aeaImAvoidUnit.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                aeaImAvoidUnitMapper.insertAeaImAvoidUnit(aeaImAvoidUnit);
            }
        }
    }

    private void saveAeaImMajorQual(AeaImMajorQual aeaImMajorQual, String unitRequireId) throws Exception {
        aeaImMajorQual.setMajorQualId(UuidUtil.generateUuid());
        aeaImMajorQual.setCreater(SecurityContext.getCurrentUserName());
        aeaImMajorQual.setCreateTime(new Date());
        aeaImMajorQual.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
        aeaImMajorQual.setUnitRequireId(unitRequireId);
        aeaImMajorQualMapper.insertAeaImMajorQual(aeaImMajorQual);
    }

    private void saveImUnitBidding(AgentItemApplyData data, String projPurchaseId) throws Exception {
        AeaUnitInfo agentUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(data.getAgentUnitInfoId());

        if (agentUnitInfo != null) {

            AeaImUnitService aeaImUnitService = aeaImUnitServiceMapper.getUnitServiceByUnitInfoIdAndServiceItemId(agentUnitInfo.getUnitInfoId(), data.getServiceItemId());

            if (aeaImUnitService != null) {
                AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
                aeaImUnitBidding.setUnitBiddingId(UuidUtil.generateUuid());
                aeaImUnitBidding.setProjPurchaseId(projPurchaseId);
                aeaImUnitBidding.setAuditFlag("1");
                aeaImUnitBidding.setIsWonBid("1");
                aeaImUnitBidding.setIsCancelSignup("0");
                aeaImUnitBidding.setIsUploadContract("0");
                aeaImUnitBidding.setIsUploadResult("0");
                aeaImUnitBidding.setIsEvaluate("0");
                aeaImUnitBidding.setBiddingTime(new Date());
                aeaImUnitBidding.setUnitInfoId(data.getAgentUnitInfoId());
//                aeaImUnitBidding.setRealPrice(aeaImUnitService.getPrice());
                aeaImUnitBidding.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                aeaImUnitBidding.setCreateTime(new Date());
                aeaImUnitBidding.setCreater(SecurityContext.getCurrentUserName());
                aeaImUnitBidding.setUnitServiceId(aeaImUnitService.getUnitServiceId());

                // 查询已绑定联系人
                List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.listBindLinkmanByUnitId(data.getAgentUnitInfoId(), "1", "1", "");
                if (aeaLinkmanInfos != null && aeaLinkmanInfos.size() > 0) {
                    AeaLinkmanInfo linkmanInfo = aeaLinkmanInfos.get(0);
                    aeaImUnitBidding.setLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
                }
                aeaImUnitBidding.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaImUnitBiddingMapper.insertAeaImUnitBidding(aeaImUnitBidding);

                // 保存竞价价格
                AeaImBiddingPrice aeaImBiddingPrice = new AeaImBiddingPrice();
                aeaImBiddingPrice.setBiddingPriceId(UUID.randomUUID().toString());
                aeaImBiddingPrice.setIsChoice("1");
                aeaImBiddingPrice.setIsDelete("0");
                aeaImBiddingPrice.setUnitBiddingId(aeaImUnitBidding.getUnitBiddingId());
                aeaImBiddingPrice.setCreater(SecurityContext.getCurrentUserName());
                aeaImBiddingPrice.setCreateTime(new Date());
                aeaImBiddingPrice.setPrice(data.getBasePrice());
                aeaImBiddingPrice.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaImBiddingPriceMapper.insertAeaImBiddingPrice(aeaImBiddingPrice);
            }
        }
    }

    private void saveParentProjInfo(String projInfoId, String parentProjId) {
        AeaProjInfo parentProjInfo = aeaProjInfoMapper.getOnlyAeaProjInfoById(parentProjId);

        if (parentProjInfo != null) {
            String projSeq = parentProjInfo.getProjSeq() + "," + projInfoId;
            AeaParentProj aeaParentProj = new AeaParentProj();
            aeaParentProj.setChildProjId(projInfoId);
            aeaParentProj.setParentProjId(parentProjId);
            aeaParentProj.setCreater(SecurityContext.getCurrentUserName());
            aeaParentProj.setCreateTime(new Date());
            aeaParentProj.setNodeProjId(UuidUtil.generateUuid());
            aeaParentProj.setProjSeq(projSeq);
            aeaProjInfoMapper.insertAeaParentProj(aeaParentProj);
        }
    }
}
