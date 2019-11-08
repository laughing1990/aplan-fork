package com.augurit.aplanmis.admin.market.purchase.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.purchase.service.PurchaseService;
import com.augurit.aplanmis.admin.market.purchase.vo.UnitRequireQualVo;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaImPurchaseinst;
import com.augurit.aplanmis.common.domain.AeaImUnitBidding;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.vo.AeaImMajorQualVo;
import com.augurit.aplanmis.common.vo.AeaImProjPurchaseDetailVo;
import com.augurit.aplanmis.common.vo.AeaImQualLevelVo;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
* -Service服务接口实现类
*/
@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaImMajorQualMapper aeaImMajorQualMapper;

    @Autowired
    private AeaImPurchaseinstMapper aeaImPurchaseinstMapper;

    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;

    @Autowired
    private BscAttMapper bscAttMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    @Override
    public List<AeaImProjPurchase> listAeaImProPurchase(AeaImProjPurchase aeaImProjPurchase, Page page) throws Exception {
        PageHelper.startPage(page);
        return aeaImProjPurchaseMapper.listAuditProjPurchase(aeaImProjPurchase);
    }

    @Override
    public AeaImProjPurchaseDetailVo getAeaImProPurchase(String projPurchaseId) throws Exception {
        AeaImProjPurchaseDetailVo aeaImProjPurchaseDetailVo = aeaImProjPurchaseMapper.getAuditProjPurchaseDetail(projPurchaseId);

        if (aeaImProjPurchaseDetailVo != null) {
            if ("1".equals(aeaImProjPurchaseDetailVo.getIsApproveProj())) {
                // 获取投资审批项目（父项目）
                AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoByChildProjId(aeaImProjPurchaseDetailVo.getProjInfoId());
                if (aeaProjInfo != null) {
                    aeaImProjPurchaseDetailVo.setParentProjName(aeaProjInfo.getProjName());
                    aeaImProjPurchaseDetailVo.setParentLocalCode(aeaProjInfo.getLocalCode());
                }

            }

            // 指定中介机构
            if ("2".equals(aeaImProjPurchaseDetailVo.getBiddingType())) {
                List<AeaImUnitBidding> aeaImUnitBiddingList = aeaImUnitBiddingMapper.listUnitBiddingByProjPurchaseId(projPurchaseId);
                if (aeaImUnitBiddingList != null && aeaImUnitBiddingList.size() > 0) {
                    for (AeaImUnitBidding aeaImUnitBidding : aeaImUnitBiddingList) {
                        if ("1".equals(aeaImUnitBidding.getIsWonBid()) && "0".equals(aeaImUnitBidding.getIsCancelSignup())) {
                            aeaImProjPurchaseDetailVo.setSelectedApplicant(aeaImUnitBidding.getApplicant());
                            aeaImProjPurchaseDetailVo.setSelectedUnitInfoId(aeaImUnitBidding.getUnitInfoId());
                            break;
                        }
                    }
                }
            }

            // 中介服务机构要求
            String unitRequire = "";
            if ("1".equals(aeaImProjPurchaseDetailVo.getIsQualRequire())) {
                unitRequire += "资质（资格）要求";
            }
            if ("1".equals(aeaImProjPurchaseDetailVo.getIsRegisterRequire())) {
                unitRequire += (unitRequire != "" ? "、执业/职业人员要求" : "执业/职业人员要求");
            }
            if ("1".equals(aeaImProjPurchaseDetailVo.getIsRecordRequire())) {
                unitRequire += (unitRequire != "" ? "、备案要求" : "备案要求");
            }
            if ("1".equals(aeaImProjPurchaseDetailVo.getPromiseService())) {
                unitRequire = "仅承诺服务即可";
            }
            aeaImProjPurchaseDetailVo.setUnitRequire(unitRequire);

            // 获取附件
            if (StringUtils.isNotBlank(aeaImProjPurchaseDetailVo.getOfficialRemarkFile())) {
                List<BscAttForm> officialRemarkBscAttForms = bscAttMapper.listAttLinkAndDetail("AEA_IM_PROJ_PURCHASE", "OFFICIAL_REMARK_FILE",
                        aeaImProjPurchaseDetailVo.getOfficialRemarkFile(), null, topOrgId, null);
                aeaImProjPurchaseDetailVo.setOfficialRemarkBscAttForms(officialRemarkBscAttForms);
            }

            if (StringUtils.isNotBlank(aeaImProjPurchaseDetailVo.getRequireExplainFile())) {
                List<BscAttForm> requireExplainBscAttForms = bscAttMapper.listAttLinkAndDetail("AEA_IM_PROJ_PURCHASE", "REQUIRE_EXPLAIN_FILE",
                        aeaImProjPurchaseDetailVo.getRequireExplainFile(), null, topOrgId, null);
                aeaImProjPurchaseDetailVo.setRequireExplainBscAttForms(requireExplainBscAttForms);
            }
        }
        return aeaImProjPurchaseDetailVo;
    }

    @Override
    public void audit(AeaImProjPurchase aeaImProjPurchase) throws Exception {
        AeaImProjPurchase projPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
        if (projPurchase != null) {
            String auditFlag = "5";
            if ("1".equals(aeaImProjPurchase.getAuditFlag())) {
                // 直接选取 审核通过状态为9
                auditFlag = "2".equals(aeaImProjPurchase.getBiddingType()) ? "9" : "6";
            }

            Date date = new Date();
            String[] auditFlags = {"4", auditFlag};
            aeaImProjPurchase.setAuditFlags(auditFlags);
            aeaImProjPurchase.setAuditTime(date);
            aeaImProjPurchaseMapper.updateAeaImProjPurchaseAuditFlag(aeaImProjPurchase);

            AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
            aeaImPurchaseinst.setPurchaseinstId(UuidUtil.generateUuid());
            aeaImPurchaseinst.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
            aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserName());
            aeaImPurchaseinst.setCreateTime(date);
            aeaImPurchaseinst.setNewPurchaseFlag(auditFlag);
            aeaImPurchaseinst.setOperateDescribe("采购需求项目审核");
            aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);
        }
    }

    @Override
    public List<UnitRequireQualVo> getMajorTreeByUnitRequireId(String unitRequireId) throws Exception {
        List<UnitRequireQualVo> list = new ArrayList<UnitRequireQualVo>();
        if (StringUtils.isNotBlank(unitRequireId)) {
            List<AeaImMajorQualVo> majorQualList = aeaImMajorQualMapper.listAeaImMajorQualByUnitRequireId(unitRequireId);

            if (majorQualList != null && majorQualList.size() > 0) {
                Map<String, UnitRequireQualVo> qualMap = new HashMap<String, UnitRequireQualVo>();
                Map<String, AeaImQualLevelVo> qualLevelMap = new HashMap<String, AeaImQualLevelVo>();
                Map<String, AeaImMajorQualVo> majorMap = new HashMap<String, AeaImMajorQualVo>();


                for (AeaImMajorQualVo aeaImMajorQualVo : majorQualList) {
                    String qualId = aeaImMajorQualVo.getQualId();
                    String qualLevelId = aeaImMajorQualVo.getQualLevelId();
                    String majorId = aeaImMajorQualVo.getMajorId();

                    UnitRequireQualVo unitRequireQualVo = qualMap.get(qualId);

                    // 获取资质
                    if (unitRequireQualVo == null) {
                        unitRequireQualVo = new UnitRequireQualVo();
                        unitRequireQualVo.setQualName(aeaImMajorQualVo.getQualName());
                        unitRequireQualVo.setQualId(aeaImMajorQualVo.getQualId());
                        List<AeaImQualLevelVo> qualLevelList = new ArrayList<AeaImQualLevelVo>();
                        unitRequireQualVo.setQualLevelList(qualLevelList);

                        qualMap.put(qualId, unitRequireQualVo);
                        list.add(unitRequireQualVo);
                    }

                    // 获取资质等级
                    AeaImQualLevelVo aeaImQualLevelVo = qualLevelMap.get(qualId + qualLevelId);
                    if (aeaImQualLevelVo == null) {
                        aeaImQualLevelVo = new AeaImQualLevelVo();
                        aeaImQualLevelVo.setQualLevelId(aeaImMajorQualVo.getQualLevelId());
                        aeaImQualLevelVo.setQualLevelName(aeaImMajorQualVo.getQualLevelName());
                        List<AeaImMajorQualVo> majorList = new ArrayList<AeaImMajorQualVo>();
                        aeaImQualLevelVo.setMajors(majorList);

                        unitRequireQualVo.getQualLevelList().add(aeaImQualLevelVo);
                        qualLevelMap.put(qualId + qualLevelId, aeaImQualLevelVo);
                    }

                    // 获取专业树
                    if (StringUtils.isBlank(aeaImMajorQualVo.getParentMajorId())) {
                        AeaImMajorQualVo major = majorMap.get(qualId + qualLevelId + majorId);

                        if (major == null) {
                            major = new AeaImMajorQualVo();
                            major.setMajorId(aeaImMajorQualVo.getMajorId());
                            major.setMajorName(aeaImMajorQualVo.getMajorName());
                            major.setChildren(getMajorChildrenList(qualId, qualLevelId, majorId, majorQualList));

                            aeaImQualLevelVo.getMajors().add(major);
                            majorMap.put(qualId + qualLevelId + majorId, major);
                        }
                    }
                }
            }
        }
        return list;
    }

    private List getMajorChildrenList(String qualId, String qualLevelId, String parentMajorId, List<AeaImMajorQualVo> majorQualList) {
        List children = new ArrayList();

        if (StringUtils.isBlank(qualId) || StringUtils.isBlank(qualLevelId) || StringUtils.isBlank(parentMajorId)) {
            return children;
        }

        for (AeaImMajorQualVo aeaImMajorQualVo : majorQualList) {
            String childQualId = aeaImMajorQualVo.getQualId();
            String childQualLevelId = aeaImMajorQualVo.getQualLevelId();
            String childMajorId = aeaImMajorQualVo.getMajorId();
            if (parentMajorId.equals(aeaImMajorQualVo.getParentMajorId()) && qualId.equals(childQualId)
                    && qualLevelId.equals(childQualLevelId)) {
                aeaImMajorQualVo.setChildren(getMajorChildrenList(childQualId, childQualLevelId, childMajorId, majorQualList));
                children.add(aeaImMajorQualVo);
            }
        }
        return children;
    }


}

