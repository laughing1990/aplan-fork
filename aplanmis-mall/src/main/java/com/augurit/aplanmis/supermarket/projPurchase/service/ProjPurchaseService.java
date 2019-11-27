package com.augurit.aplanmis.supermarket.projPurchase.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.AuditFlagStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.utils.BusinessUtils;
import com.augurit.aplanmis.common.utils.CommonConstant;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.*;
import com.augurit.aplanmis.common.vo.purchase.PurchaseProjVo;
import com.augurit.aplanmis.supermarket.apply.service.RestImApplyService;
import com.augurit.aplanmis.supermarket.apply.vo.ApplyinstResult;
import com.augurit.aplanmis.supermarket.apply.vo.ImItemApplyData;
import com.augurit.aplanmis.supermarket.apply.vo.ImPurchaseData;
import com.augurit.aplanmis.supermarket.projPurchase.vo.OwnerIndexData;
import com.augurit.aplanmis.supermarket.projPurchase.vo.SelectedQualMajorRequire;
import com.augurit.aplanmis.supermarket.projPurchase.vo.SelectedQualVo;
import com.augurit.aplanmis.supermarket.projPurchase.vo.purchase.PurchaseDetailVo;
import com.augurit.aplanmis.supermarket.projPurchase.vo.purchase.ShowProjPurchaseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjPurchaseService {
    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaImQualMapper aeaImQualMapper;

    @Autowired
    private AeaImQualLevelMapper aeaImQualLevelMapper;

    @Autowired
    private AeaImServiceMajorMapper aeaImServiceMajorMapper;

    @Autowired
    private AeaImUnitRequireMapper aeaImUnitRequireMapper;

    @Autowired
    private AeaImMajorQualMapper aeaImMajorQualMapper;

    @Autowired
    private AeaImUnitServiceMapper aeaImUnitServiceMapper;

    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private BscAttMapper bscAttMapper;

    @Autowired
    private AeaImServiceItemMapper aeaImServiceItemMapper;

    @Autowired
    private AeaImPurchaseinstMapper aeaImPurchaseinstMapper;
    @Autowired
    private AeaImServiceLinkmanMapper aeaImServiceLinkmanMapper;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;
    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

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
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;

    @Autowired
    private RestImApplyService restImApplyService;
    private static final String SERVICE_OBJECT_DICT_NAME = "ITEM_FWJGXZ";
    private static final String SERVICE_OBJECT_CODE = "5";

    @Autowired
    private OpuOmOrgService opuOmOrgService;
    @Autowired
    private BscDicCodeService bscDicCodeService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Autowired
    private AeaParentProjMapper aeaParentProjMapper;
    @Autowired
    private FileUtilsService fileUtilsService;


    public List<AeaImProjPurchase> getProjPurchaseList(AeaImProjPurchase aeaImProjPurchase, Page page) {
        PageHelper.startPage(page);
        return aeaImProjPurchaseMapper.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
    }

    public AeaImProjPurchase getProjPurchaseById(String projPurchaseId) {
        return aeaImProjPurchaseMapper.listbid(projPurchaseId);
    }

    public List<AeaImProjPurchase> getProjListByLocalCode(String keyword, Page page) {
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        if (StringUtils.isNotBlank(keyword)) {
            aeaImProjPurchase.setLocalCode(keyword);
        }
        return getProjPurchaseList(aeaImProjPurchase, page);
    }

    public AeaImProjPurchase saveAeaImProjPurchase(SaveAeaImProjPurchaseVo aeaImProjPurchaseVo, HttpServletRequest request) throws Exception {

        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        checkIsOwner(loginInfoVo);

        aeaImProjPurchaseVo.setLinkmanInfoId(loginInfoVo.getUserId());

        String unitId = loginInfoVo.getUnitId();

        if (aeaImProjPurchaseVo == null) {
            throw new RuntimeException("缺少采购信息");
        }

        AeaImUnitRequire aeaImUnitRequire = aeaImProjPurchaseVo.getAeaImUnitRequire();

        if (aeaImUnitRequire == null) {
            throw new RuntimeException("缺少中介机构要求信息");
        }

        SaveAeaImProjPurchaseVo.SaveAeaProjInfoVo saveAeaProjInfoVo = aeaImProjPurchaseVo.getSaveAeaProjInfoVo();

        if (saveAeaProjInfoVo == null) {
            throw new RuntimeException("缺少采购项目信息");
        }

        if (Status.ON.equals(aeaImProjPurchaseVo.getIsApproveProj())) {
            if (StringUtils.isBlank(saveAeaProjInfoVo.getParentProjId())) {
                throw new RuntimeException("缺少审批项目信息");
            }
        }

        if (Status.ON.equals(aeaImProjPurchaseVo.getIsAvoid())) {
            if (StringUtils.isBlank(aeaImProjPurchaseVo.getAvoidReason())) {
                throw new RuntimeException("缺少回避原因");
            }

            if (StringUtils.isBlank(aeaImProjPurchaseVo.getAvoidUnitInfoIds())) {
                throw new RuntimeException("缺少回避单位");
            }
        }

        // 判断项目名称是否存在
        AeaProjInfo aeaProjInfoCond = new AeaProjInfo();
        aeaProjInfoCond.setProjName(saveAeaProjInfoVo.getProjName());
        List aeaProjInfoCondList = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfoCond);
        if (aeaProjInfoCondList != null && aeaProjInfoCondList.size() > 0) {
            throw new RuntimeException("项目名称已存在");
        }

        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        BeanUtils.copyProperties(aeaImProjPurchaseVo, aeaImProjPurchase);

        //保存机构要求
        aeaImUnitRequire.setUnitRequireId(UuidUtil.generateUuid());
        aeaImUnitRequire.setRootOrgId(topOrgId);
        aeaImUnitRequireMapper.insertAeaImUnitRequire(aeaImUnitRequire);
        aeaImProjPurchase.setUnitRequireId(aeaImUnitRequire.getUnitRequireId());

        List<AeaImMajorQual> aeaImMajorQuals = aeaImProjPurchaseVo.getAeaImMajorQuals();

        //保存资质专业要求
        if (aeaImMajorQuals != null && "1".equals(aeaImUnitRequire.getIsQualRequire())) {
            for (AeaImMajorQual aeaImMajorQual : aeaImMajorQuals) {
                saveAeaImMajorQual(aeaImMajorQual, aeaImUnitRequire.getUnitRequireId());
            }
        }

        //保存采购项目
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        BeanUtils.copyProperties(saveAeaProjInfoVo, aeaProjInfo);
        aeaProjInfo.setProjInfoId(UuidUtil.generateUuid());
        aeaProjInfo.setCreater(SecurityContext.getCurrentUserName());
        aeaProjInfo.setCreateTime(new Date());
        aeaProjInfo.setIsPurchaseProj(Status.ON);
        aeaProjInfo.setIsDeleted("0");
        aeaProjInfo.setRootOrgId(topOrgId);
        aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);

        //保存采购项目与联系人关联
        String linkmanInfoId = aeaImProjPurchaseVo.getLinkmanInfoId();
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


        aeaImProjPurchase.setProjInfoId(aeaProjInfo.getProjInfoId());

        //保存审批目
        if (StringUtils.isNotBlank(saveAeaProjInfoVo.getParentProjId())) {
            saveParentProjInfo(aeaProjInfo.getProjInfoId(), saveAeaProjInfoVo.getParentProjId());
        }

        aeaImProjPurchase.setPublishUnitInfoId(unitId);
        aeaImProjPurchase.setProjPurchaseId(UuidUtil.generateUuid());
        aeaImProjPurchase.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
        aeaImProjPurchase.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaImProjPurchase.setCreateTime(new Date());
        aeaImProjPurchase.setCreater(SecurityContext.getCurrentUserName());
        aeaImProjPurchase.setAuditFlag(AuditFlagStatus.NO_COMMIT);
        aeaImProjPurchase.setRootOrgId(topOrgId);
        //保存文件
        if (request instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            saveOfficialRemarkFiles(req, aeaImProjPurchase);
            saveRequireExplainFiles(req, aeaImProjPurchase);
        }

        aeaImProjPurchaseMapper.insertAeaImProjPurchase(aeaImProjPurchase);

        //保存企业报价
        if (AeaImProjPurchase.BiddingType.自主选择.getType().equals(aeaImProjPurchase.getBiddingType())) {
            if (StringUtils.isNotBlank(aeaImProjPurchaseVo.getAgentUnitInfoId())) {
                saveImUnitBidding(aeaImProjPurchaseVo, aeaImProjPurchase.getProjPurchaseId());
            }
        }

        // 保存回避单位
        if (Status.ON.equals(aeaImProjPurchaseVo.getIsAvoid())) {
            String avoidUnitInfoIds = aeaImProjPurchaseVo.getAvoidUnitInfoIds();
            for (String avoidUnitInfoId : avoidUnitInfoIds.split(",")) {
                AeaImAvoidUnit aeaImAvoidUnit = new AeaImAvoidUnit();
                aeaImAvoidUnit.setAvoidUnitId(UuidUtil.generateUuid());
                aeaImAvoidUnit.setUnitInfoId(avoidUnitInfoId);
                aeaImAvoidUnit.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
                aeaImAvoidUnit.setCreater(SecurityContext.getCurrentUserName());
                aeaImAvoidUnit.setCreateTime(new Date());
                aeaImAvoidUnit.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                aeaImAvoidUnitMapper.insertAeaImAvoidUnit(aeaImAvoidUnit);
            }
        }

        return aeaImProjPurchase;
    }

    private AeaImProjPurchase updateProjPurchase(SaveAeaImProjPurchaseVo aeaImProjPurchaseVo, HttpServletRequest request, String[] auditFlags) throws Exception {
        if (aeaImProjPurchaseVo == null) {
            throw new RuntimeException("缺少采购信息");
        }

        AeaImUnitRequire aeaImUnitRequire = aeaImProjPurchaseVo.getAeaImUnitRequire();

        if (aeaImUnitRequire == null) {
            throw new RuntimeException("缺少中介机构要求信息");
        }

        SaveAeaImProjPurchaseVo.SaveAeaProjInfoVo saveAeaProjInfoVo = aeaImProjPurchaseVo.getSaveAeaProjInfoVo();

        if (saveAeaProjInfoVo == null) {
            throw new RuntimeException("缺少采购项目信息");
        }

        if (Status.ON.equals(aeaImProjPurchaseVo.getIsApproveProj())) {
            if (StringUtils.isBlank(saveAeaProjInfoVo.getParentProjId())) {
                throw new RuntimeException("缺少审批项目信息");
            }
        }

        if (Status.ON.equals(aeaImProjPurchaseVo.getIsAvoid())) {
            if (StringUtils.isBlank(aeaImProjPurchaseVo.getAvoidReason())) {
                throw new RuntimeException("缺少回避原因");
            }

            if (StringUtils.isBlank(aeaImProjPurchaseVo.getAvoidUnitInfoIds())) {
                throw new RuntimeException("缺少回避单位");
            }
        }

        // 判断项目名称是否存在
        AeaProjInfo aeaProjInfoCond = new AeaProjInfo();
        aeaProjInfoCond.setProjName(saveAeaProjInfoVo.getProjName());
        List aeaProjInfoCondList = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfoCond);
        if (aeaProjInfoCondList != null && aeaProjInfoCondList.size() > 0) {
            throw new RuntimeException("项目名称已存在");
        }

        AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(aeaImProjPurchaseVo.getProjPurchaseId());


        if (aeaImProjPurchase == null) {
            throw new RuntimeException("没有对应的采购信息");
        }

        if (auditFlags != null) {
            boolean matchFlag = false;
            for (String auditFlag : auditFlags) {
                if (auditFlag.equals(aeaImProjPurchase.getAuditFlag())) {
                    matchFlag = true;
                }

            }

            if (!matchFlag) {
                throw new RuntimeException("当前状态不可修改");
            }
        }

        //处理企业报价
        dealImUnitBidding(aeaImProjPurchaseVo, aeaImProjPurchase);

        BeanUtils.copyProperties(aeaImProjPurchaseVo, aeaImProjPurchase);

        //更新机构要求
        aeaImUnitRequire.setUnitRequireId(aeaImProjPurchase.getUnitRequireId());
        aeaImUnitRequireMapper.updateAeaImUnitRequire(aeaImUnitRequire);

        //删除旧的专业要求
        aeaImMajorQualMapper.deleteAeaImMajorQualByUnitRequireId(aeaImProjPurchase.getUnitRequireId());

        List<AeaImMajorQual> aeaImMajorQuals = aeaImProjPurchaseVo.getAeaImMajorQuals();

        //保存资质专业要求
        if (aeaImMajorQuals != null && "1".equals(aeaImUnitRequire.getIsQualRequire())) {
            for (AeaImMajorQual aeaImMajorQual : aeaImMajorQuals) {
                saveAeaImMajorQual(aeaImMajorQual, aeaImUnitRequire.getUnitRequireId());
            }
        }

        // 删除旧的回避单位
        AeaImAvoidUnit oldAeaImAvoidUnit = new AeaImAvoidUnit();
        oldAeaImAvoidUnit.setProjPurchaseId(aeaImProjPurchaseVo.getProjPurchaseId());
        oldAeaImAvoidUnit.setIsDelete(DeletedStatus.DELETED.getValue());
        aeaImAvoidUnitMapper.updateAeaImAvoidUnit(oldAeaImAvoidUnit);
        // 保存回避单位
        if (Status.ON.equals(aeaImProjPurchaseVo.getIsAvoid())) {
            String avoidUnitInfoIds = aeaImProjPurchaseVo.getAvoidUnitInfoIds();
            for (String avoidUnitInfoId : avoidUnitInfoIds.split(",")) {
                AeaImAvoidUnit aeaImAvoidUnit = new AeaImAvoidUnit();
                aeaImAvoidUnit.setAvoidUnitId(UuidUtil.generateUuid());
                aeaImAvoidUnit.setUnitInfoId(avoidUnitInfoId);
                aeaImAvoidUnit.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
                aeaImAvoidUnit.setCreater(SecurityContext.getCurrentUserName());
                aeaImAvoidUnit.setCreateTime(new Date());
                aeaImAvoidUnit.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                aeaImAvoidUnitMapper.insertAeaImAvoidUnit(aeaImAvoidUnit);
            }
        }
        //更新采购项目
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        BeanUtils.copyProperties(saveAeaProjInfoVo, aeaProjInfo);
        aeaProjInfo.setProjInfoId(aeaImProjPurchase.getProjInfoId());
        aeaProjInfo.setModifier(SecurityContext.getCurrentUserName());
        aeaProjInfo.setModifyTime(new Date());
        aeaProjInfoMapper.updateAeaProjInfo(aeaProjInfo);

        AeaParentProj aeaParentProj = aeaProjInfoMapper.getAeaParentProjByChildId(aeaProjInfo.getProjInfoId());

        if (StringUtils.isBlank(saveAeaProjInfoVo.getParentProjId()) && aeaParentProj != null) {
            aeaProjInfoMapper.delParentProjByCondition(aeaParentProj);
        } else {
            if (StringUtils.isNotBlank(saveAeaProjInfoVo.getParentProjId())) {
                if (aeaParentProj == null) {
                    saveParentProjInfo(aeaProjInfo.getProjInfoId(), saveAeaProjInfoVo.getParentProjId());
                } else if (!aeaParentProj.getParentProjId().equals(saveAeaProjInfoVo.getParentProjId())) {
                    AeaProjInfo parentProjInfo = aeaProjInfoMapper.getOnlyAeaProjInfoById(saveAeaProjInfoVo.getParentProjId());
                    String projSeq = parentProjInfo.getProjSeq() + "," + aeaProjInfo.getProjInfoId();
                    aeaParentProj.setParentProjId(saveAeaProjInfoVo.getParentProjId());
                    aeaParentProj.setModifier(SecurityContext.getCurrentUserName());
                    aeaParentProj.setModifyTime(new Date());
                    aeaParentProj.setProjSeq(projSeq);
                    aeaProjInfoMapper.updateAeaParentProj(aeaParentProj);
                }
            }
        }

        aeaImProjPurchase.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
        aeaImProjPurchase.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaImProjPurchase.setModifyTime(new Date());
        aeaImProjPurchase.setModifier(SecurityContext.getCurrentUserName());

        //保存文件
        if (request instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            saveOfficialRemarkFiles(req, aeaImProjPurchase);
            saveRequireExplainFiles(req, aeaImProjPurchase);
        }

        aeaImProjPurchaseMapper.updateAeaImProjPurchase(aeaImProjPurchase);

        return aeaImProjPurchase;
    }

    public AeaImProjPurchase updateProjPurchase(SaveAeaImProjPurchaseVo aeaImProjPurchaseVo, HttpServletRequest request) throws Exception {

        return updateProjPurchase(aeaImProjPurchaseVo, request, new String[]{AuditFlagStatus.NO_COMMIT, AuditFlagStatus.AUDIT_RETURN});
    }

    public void revisedProjPurchase(SaveAeaImProjPurchaseVo saveAeaImProjPurchaseVo, String operateDescribe, List<MultipartFile> files, HttpServletRequest request) throws Exception {
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        checkIsOwner(loginInfoVo);
        checkIsPersonAccount(loginInfoVo);

        AeaImProjPurchase aeaImProjPurchase = updateProjPurchase(saveAeaImProjPurchaseVo, request, null);

        saveAeaImPurchaseinst(loginInfoVo, aeaImProjPurchase, AeaImPurchaseinst.OperateAction.修改采购需求.getAction(), operateDescribe, files);

        changeAeaImProjPurchaseAuditFlag(aeaImProjPurchase.getProjPurchaseId(), AuditFlagStatus.AUDIT_PROGRESS);
    }

    private void dealImUnitBidding(SaveAeaImProjPurchaseVo aeaImProjPurchaseVo, AeaImProjPurchase aeaImProjPurchase) throws Exception {
        String oldBiddingType = aeaImProjPurchase.getBiddingType();

        if (AeaImProjPurchase.BiddingType.自主选择.getType().equals(oldBiddingType)) {
            AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
            aeaImUnitBidding.setProjPurchaseId(aeaImProjPurchaseVo.getProjPurchaseId());
            aeaImUnitBidding.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
            aeaImUnitBidding.setRootOrgId(topOrgId);
            List<AeaImUnitBidding> oldUnitBiddings = aeaImUnitBiddingMapper.listAeaImUnitBidding(aeaImUnitBidding);

            for (AeaImUnitBidding bidding : oldUnitBiddings) {
                aeaImUnitBidding = new AeaImUnitBidding();
                aeaImUnitBidding.setProjPurchaseId(aeaImProjPurchaseVo.getProjPurchaseId());
                aeaImUnitBidding.setIsDelete(DeletedStatus.DELETED.getValue());
                aeaImUnitBidding.setModifier(SecurityContext.getCurrentUserName());
                aeaImUnitBidding.setModifyTime(new Date());
                aeaImUnitBidding.setUnitBiddingId(bidding.getUnitBiddingId());
                aeaImUnitBiddingMapper.updateAeaImUnitBidding(aeaImUnitBidding);

                AeaImBiddingPrice aeaImBiddingPrice = new AeaImBiddingPrice();
                aeaImBiddingPrice.setUnitBiddingId(bidding.getUnitBiddingId());
                aeaImBiddingPrice.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                aeaImBiddingPrice.setRootOrgId(topOrgId);
                List<AeaImBiddingPrice> oldBiddingPrices = aeaImBiddingPriceMapper.listAeaImBiddingPrice(aeaImBiddingPrice);

                for (AeaImBiddingPrice oldBiddingPrice : oldBiddingPrices) {
                    oldBiddingPrice.setIsDelete(DeletedStatus.DELETED.getValue());
                    aeaImBiddingPriceMapper.updateAeaImBiddingPrice(oldBiddingPrice);
                }
            }

        }

        //保存企业报价
        if (AeaImProjPurchase.BiddingType.自主选择.getType().equals(aeaImProjPurchaseVo.getBiddingType())) {

            if (StringUtils.isNotBlank(aeaImProjPurchaseVo.getAgentUnitInfoId())) {
                saveImUnitBidding(aeaImProjPurchaseVo, aeaImProjPurchase.getProjPurchaseId());
            }
        }

    }

    private void saveRequireExplainFiles(StandardMultipartHttpServletRequest req, AeaImProjPurchase aeaImProjPurchase) {
        List<MultipartFile> requireExplainFiles = req.getFiles("requireExplainFile");
        if (requireExplainFiles != null && !requireExplainFiles.isEmpty()) {
            String requireExplainFile = StringUtils.isNotBlank(aeaImProjPurchase.getRequireExplainFile()) ? aeaImProjPurchase.getRequireExplainFile() : UuidUtil.generateUuid();
            FileUtils.uploadFile("AEA_IM_PROJ_PURCHASE", "REQUIRE_EXPLAIN_FILE", requireExplainFile, requireExplainFiles);
            aeaImProjPurchase.setRequireExplainFile(requireExplainFile);
        }

    }

    private void saveOfficialRemarkFiles(StandardMultipartHttpServletRequest req, AeaImProjPurchase aeaImProjPurchase) {
        List<MultipartFile> officialRemarkFiles = req.getFiles("officialRemarkFile");
        if (officialRemarkFiles != null && !officialRemarkFiles.isEmpty()) {
            String officialRemarkFile = StringUtils.isNotBlank(aeaImProjPurchase.getOfficialRemarkFile()) ? aeaImProjPurchase.getOfficialRemarkFile() : UuidUtil.generateUuid();
            FileUtils.uploadFile("AEA_IM_PROJ_PURCHASE", "OFFICIAL_REMARK_FILE", officialRemarkFile, officialRemarkFiles);
            aeaImProjPurchase.setOfficialRemarkFile(officialRemarkFile);
        }

    }

    private void saveImUnitBidding(SaveAeaImProjPurchaseVo aeaImProjPurchaseVo, String projPurchaseId) throws Exception {
        AeaUnitInfo agentUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(aeaImProjPurchaseVo.getAgentUnitInfoId());

        if (agentUnitInfo != null) {

            AeaImUnitService aeaImUnitService = aeaImUnitServiceMapper.getUnitServiceByUnitInfoIdAndServiceItemId(agentUnitInfo.getUnitInfoId(), aeaImProjPurchaseVo.getServiceItemId());

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
                aeaImUnitBidding.setUnitInfoId(aeaImProjPurchaseVo.getAgentUnitInfoId());
//                aeaImUnitBidding.setRealPrice(aeaImUnitService.getPrice());
                aeaImUnitBidding.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                aeaImUnitBidding.setCreateTime(new Date());
                aeaImUnitBidding.setCreater(SecurityContext.getCurrentUserName());
                aeaImUnitBidding.setUnitServiceId(aeaImUnitService.getUnitServiceId());

                // 查询已绑定联系人
                List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.listBindLinkmanByUnitId(aeaImProjPurchaseVo.getAgentUnitInfoId(), "1", "1", "");
                if (aeaLinkmanInfos != null && aeaLinkmanInfos.size() > 0) {
                    AeaLinkmanInfo linkmanInfo = aeaLinkmanInfos.get(0);
                    aeaImUnitBidding.setLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
                }
                aeaImUnitBidding.setRootOrgId(topOrgId);
                aeaImUnitBiddingMapper.insertAeaImUnitBidding(aeaImUnitBidding);

                // 保存竞价价格
                AeaImBiddingPrice aeaImBiddingPrice = new AeaImBiddingPrice();
                aeaImBiddingPrice.setBiddingPriceId(UUID.randomUUID().toString());
                aeaImBiddingPrice.setIsChoice("1");
                aeaImBiddingPrice.setIsDelete("0");
                aeaImBiddingPrice.setUnitBiddingId(aeaImUnitBidding.getUnitBiddingId());
                aeaImBiddingPrice.setCreater(SecurityContext.getCurrentUserName());
                aeaImBiddingPrice.setCreateTime(new Date());
                aeaImBiddingPrice.setPrice(aeaImProjPurchaseVo.getBasePrice());
                aeaImBiddingPrice.setRootOrgId(topOrgId);
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

    private void saveAeaImMajorQual(AeaImMajorQual aeaImMajorQual, String unitRequireId) throws Exception {
        aeaImMajorQual.setMajorQualId(UuidUtil.generateUuid());
        aeaImMajorQual.setCreater(SecurityContext.getCurrentUserName());
        aeaImMajorQual.setCreateTime(new Date());
        aeaImMajorQual.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
        aeaImMajorQual.setUnitRequireId(unitRequireId);
        aeaImMajorQualMapper.insertAeaImMajorQual(aeaImMajorQual);
    }

    public List<AeaProjInfo> getUnpublishedProjInfoList(String localCode, Page page, HttpServletRequest request) throws Exception {
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        checkIsOwner(loginInfoVo);

        boolean needSync = false;

        if (StringUtils.isNotBlank(localCode)) {
            List<AeaProjInfo> projInfoList = aeaProjInfoMapper.getProjInfoByCode(localCode);

            if (projInfoList.isEmpty()) {
                needSync = true;
            }
        }

        if (page != null) {
            PageHelper.startPage(page);
        }

        List<AeaProjInfo> list = aeaProjInfoMapper.getUnpublishedProjInfoList(localCode, loginInfoVo.getUnitId());

        if (list.isEmpty() && needSync) {
            //同步项目信息
            AeaProjInfo aeaProjInfo = getAeaProjInfo(localCode, loginInfoVo.getUnitId());
            if (aeaProjInfo != null) {
                list.add(aeaProjInfo);
            }
        }

        return list;
    }

    /**
     * 获取项目信息，没有的话会同步
     *
     * @param localCode
     * @param unitInfoId
     * @return
     * @throws Exception
     */
    private AeaProjInfo getAeaProjInfo(String localCode, String unitInfoId) throws Exception {
        AeaProjInfo aeaProjInfo = null;//  //aeaBusinessService.getProjInfoByCodes(localCode);
        if (aeaProjInfo != null) {
            AeaUnitProj aeaUnitProj = new AeaUnitProj();
            aeaUnitProj.setProjInfoId(aeaProjInfo.getProjInfoId());
            aeaUnitProj.setIsOwner(Status.ON);
            List<AeaUnitProj> aeaUnitProjList = aeaUnitProjMapper.listAeaUnitProj(aeaUnitProj);

            if (!aeaUnitProjList.isEmpty()) {
                for (AeaUnitProj aup : aeaUnitProjList) {
                    if (aup.getUnitInfoId().equals(unitInfoId)) {
                        AeaProjInfo aeaProjInfoVo = new AeaProjInfo();
                        aeaProjInfoVo.setProjName(aeaProjInfo.getProjName());
                        aeaProjInfoVo.setLocalCode(aeaProjInfo.getLocalCode());
                        aeaProjInfoVo.setProjInfoId(aeaProjInfo.getProjInfoId());
                        return aeaProjInfoVo;
                    }
                }
            }
        }

        return null;

    }

    /**
     * 判断是否为业主单位
     *
     * @param loginInfoVo 登录信息
     */
    private void checkIsOwner(LoginInfoVo loginInfoVo) {
        if (loginInfoVo == null) {
            throw new RuntimeException("未登录或登录超时，请重新登录！");
        }

        if (StringUtils.isBlank(loginInfoVo.getUnitId()) || !Status.ON.equals(loginInfoVo.getIsOwner())) {
            throw new RuntimeException("不是业主单位！");
        }
    }

    public ProUnitLinkVo getProUnitLinkInfo(String projInfoId, HttpServletRequest request) {
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        checkIsOwner(loginInfoVo);

        ProUnitLinkVo proUnitLinkVo = new ProUnitLinkVo();

        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        aeaUnitInfo.setUnitInfoId(loginInfoVo.getUnitId());
        aeaUnitInfo.setApplicant(loginInfoVo.getUnitName());

        if (StringUtils.isNotBlank(projInfoId)) {
            AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
            if (aeaProjInfo != null) {
                AeaUnitProj aeaUnitProj = new AeaUnitProj();
                aeaUnitProj.setProjInfoId(aeaProjInfo.getProjInfoId());
                aeaUnitProj.setIsOwner(Status.ON);
                aeaUnitProj.setUnitInfoId(loginInfoVo.getUnitId());
                List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.listProjUnitInfo(aeaUnitProj);
                if (!aeaUnitInfos.isEmpty()) {

                    proUnitLinkVo.setAeaProjInfo(aeaProjInfo);
                    aeaUnitInfo = aeaUnitInfos.get(0);
                }
            }
        }

        proUnitLinkVo.setAeaUnitInfo(aeaUnitInfo);
        AeaLinkmanInfo aeaLinkmanInfo = null;
        if (StringUtils.isNotBlank(loginInfoVo.getUserId())) {
            aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(loginInfoVo.getUserId());

            if (aeaLinkmanInfo != null) {
                proUnitLinkVo.setAeaLinkmanInfo(aeaLinkmanInfo);
                proUnitLinkVo.setOwnerComplaintPhone(aeaLinkmanInfo.getLinkmanMobilePhone());
            }
        }

        if (aeaLinkmanInfo == null) {
            List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.findAllUnitLinkman(aeaUnitInfo.getUnitInfoId());

            for (AeaLinkmanInfo linkmanInfo : aeaLinkmanInfos) {
                if (linkmanInfo.getLinkmanType() != null && linkmanInfo.getLinkmanType().contains("u")) {
                    proUnitLinkVo.setAeaLinkmanInfo(linkmanInfo);
                    proUnitLinkVo.setOwnerComplaintPhone(linkmanInfo.getLinkmanMobilePhone());
                    break;
                }
            }
        }

        return proUnitLinkVo;
    }

    public List<AeaItemServiceVo> getAgentServiceItemList(String keyword, Page page) {

        if (page != null) {
            PageHelper.startPage(page);
        }

        return aeaItemBasicMapper.listItemServiceVo(keyword, null);

    }

    public List<AeaImServiceVo> getItemServiceListByItemVerId(String itemVerId) throws Exception {
        List<AeaImServiceVo> serviceVoList = aeaImServiceMapper.listAeaImServiceVoByItemVerId(itemVerId);

        for (AeaImServiceVo aeaImServiceVo : serviceVoList) {
            List<AeaImQualVo> qualVoList = aeaImQualMapper.listAeaImQualVoByServiceId(aeaImServiceVo.getServiceId());
            aeaImServiceVo.setAeaImQualVos(qualVoList);
            for (AeaImQualVo aeaImQualVo : qualVoList) {
                AeaImQualLevel qualLevel = new AeaImQualLevel();
                qualLevel.setParentQualLevelId(aeaImQualVo.getQualLevelId());
                qualLevel.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                qualLevel.setRootOrgId(topOrgId);
                List<AeaImQualLevel> aeaImQualLevelList = aeaImQualLevelMapper.listAeaImQualLevel(qualLevel);

                BusinessUtils.sort(aeaImQualLevelList);

                aeaImQualVo.setAeaImQualLevels(aeaImQualLevelList);

                AeaImServiceMajor queryServiceMajor = new AeaImServiceMajor();
                queryServiceMajor.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                queryServiceMajor.setQualId(aeaImQualVo.getQualId());
                queryServiceMajor.setRootOrgId(topOrgId);
                List<AeaImServiceMajor> aeaImServiceMajorList = aeaImServiceMajorMapper.listAeaImServiceMajor(queryServiceMajor);

                for (AeaImServiceMajor aeaImServiceMajor : aeaImServiceMajorList) {
                    aeaImServiceMajor.setpId(aeaImServiceMajor.getParentMajorId());
                    aeaImServiceMajor.setName(aeaImServiceMajor.getMajorName());
                    aeaImServiceMajor.setId(aeaImServiceMajor.getMajorId());
                    aeaImServiceMajor.setChildren(null);
                }
                aeaImQualVo.setAeaImServiceMajors(BusinessUtils.listToTree(aeaImServiceMajorList));
            }
        }

        return serviceVoList;
    }

    public List<AeaImProjPurchase> getPublicProjPurchaseList(QueryProjPurchaseVo queryProjPurchaseVo, Page page) {
        if (page != null) {
            PageHelper.startPage(page);
        }

        if (queryProjPurchaseVo.getServiceId() != null) {
            JSONArray jsonArray = JSONArray.parseArray(queryProjPurchaseVo.getServiceId());
            if (jsonArray.size() > 0)
                queryProjPurchaseVo.setServiceIds(jsonArray);
        }
        return aeaImProjPurchaseMapper.listPublicProjPurchaseByQueryProjPurchaseVo(queryProjPurchaseVo);
    }

    public AeaImProjPurchaseDetailVo getPublicProjPurchaseDatail(String projPurchaseId) throws Exception {
        AeaImProjPurchaseDetailVo aeaImProjPurchaseDetailVo = aeaImProjPurchaseMapper.getAeaImProjPurchaseDetailVoById(projPurchaseId);

        if (aeaImProjPurchaseDetailVo != null) {

            AeaImUnitBidding aeaUnitBidding = new AeaImUnitBidding();
            aeaUnitBidding.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
            aeaUnitBidding.setProjPurchaseId(aeaImProjPurchaseDetailVo.getProjPurchaseId());
            aeaUnitBidding.setIsCancelSignup("0");
            aeaUnitBidding.setRootOrgId(topOrgId);
            List<AeaImUnitBidding> aeaUnitBiddings = aeaImUnitBiddingMapper.listAeaImUnitBidding(aeaUnitBidding);

            aeaImProjPurchaseDetailVo.setUnitBiddingCount(aeaUnitBiddings.size());

            return aeaImProjPurchaseDetailVo;
        }

        return null;
    }

    public List<AeaImService> getAllService() throws Exception {
        AeaImService aeaImService = new AeaImService();
        aeaImService.setIsActive("1");
        aeaImService.setIsDelete("0");
        aeaImService.setRootOrgId(topOrgId);
        return aeaImServiceMapper.listAeaImService(aeaImService);
    }

    public List<BscAttForm> getRequireExplainFileList(String requireExplainFile) throws Exception {
        return bscAttMapper.listAttLinkAndDetail("AEA_IM_PROJ_PURCHASE", "REQUIRE_EXPLAIN_FILE",
                requireExplainFile, null, topOrgId, null);
    }

    public List<BscAttForm> getOfficialRemarkFileList(String officialRemarkFile) throws Exception {
        return bscAttMapper.listAttLinkAndDetail("AEA_IM_PROJ_PURCHASE", "OFFICIAL_REMARK_FILE",
                officialRemarkFile, null, topOrgId, null);
    }

    public SelectedQualMajorRequire getSelectedQualMajorRequire(String projPurchaseId) throws Exception {

        SelectedQualMajorRequire selectedQualMajorRequire = new SelectedQualMajorRequire();

        List<SelectedQualVo> qualVos = new ArrayList<>();

        selectedQualMajorRequire.setSelectedQuals(qualVos);

        AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(projPurchaseId);

        if (aeaImProjPurchase != null) {
            List<AeaImQual> aeaImQuals = aeaImQualMapper.listAeaImQualByProjPurchaseId(projPurchaseId);

            for (AeaImQual aeaImQual : aeaImQuals) {

                //查询该资质下所有的专业要求
                List<AeaImMajorQualVo> aeaImMajorQualVos = aeaImMajorQualMapper.listAeaImMajorQualVo(aeaImQual.getQualId(), null, aeaImProjPurchase.getUnitRequireId());

                if (!aeaImMajorQualVos.isEmpty()) {

                    SelectedQualVo selectedQualVo = new SelectedQualVo();
                    selectedQualVo.setQualCode(aeaImQual.getQualCode());
                    selectedQualVo.setQualId(aeaImQual.getQualId());
                    selectedQualVo.setQualName(aeaImQual.getQualName());

                    qualVos.add(selectedQualVo);

                    List<AeaImQualLevelVo> levelVos = new ArrayList<>();
                    selectedQualVo.setAeaImQualLevels(levelVos);

                    //该资质下所有的专业
                    List<AeaImServiceMajor> allMajorList = getServiceMajorListByQualId(aeaImQual.getQualId());

                    //该资质下所有的等级
                    List<AeaImQualLevel> allQualLevelList = getQualLevelListByParentQualLevelId(aeaImQual.getQualLevelId());

                    BusinessUtils.sort(allQualLevelList);

                    Map<String, AeaImServiceMajor> allMajorMap = new HashMap<>();
                    for (AeaImServiceMajor aeaImServiceMajor : allMajorList) {
                        allMajorMap.put(aeaImServiceMajor.getMajorId(), aeaImServiceMajor);
                    }

                    Map<String, Map<String, AeaImMajorQualVo>> majorQualMap = new HashMap<>();

                    for (AeaImMajorQualVo aeaImMajorQualVo : aeaImMajorQualVos) {
                        aeaImMajorQualVo.setNocheck(false);
                        aeaImMajorQualVo.setId(aeaImMajorQualVo.getMajorId());
                        aeaImMajorQualVo.setpId(aeaImMajorQualVo.getParentMajorId());
                        aeaImMajorQualVo.setName(aeaImMajorQualVo.getMajorName());

                        if (StringUtils.isBlank(aeaImMajorQualVo.getQualLevelId())) {
                            putMajorQualMap(allMajorMap, majorQualMap, "不分等级", aeaImMajorQualVo);
                        } else {
                            putMajorQualMap(allMajorMap, majorQualMap, aeaImMajorQualVo.getQualLevelId(), aeaImMajorQualVo);
                        }
                    }

                    for (AeaImQualLevel aeaImQualLevel : allQualLevelList) {

                        Map<String, AeaImMajorQualVo> majorQualVoMap = majorQualMap.get(aeaImQualLevel.getQualLevelId());

                        if (majorQualVoMap != null && !majorQualVoMap.isEmpty()) {
                            AeaImQualLevelVo aeaImQualLevelVo = new AeaImQualLevelVo();
                            BeanUtils.copyProperties(aeaImQualLevel, aeaImQualLevelVo);

                            List<AeaImMajorQualVo> majorQualVos = new ArrayList<>();
                            for (String key : majorQualVoMap.keySet()) {
                                majorQualVos.add(majorQualVoMap.get(key));
                            }

                            aeaImQualLevelVo.setMajors(BusinessUtils.listToTree(majorQualVos));
                            levelVos.add(aeaImQualLevelVo);

                        }
                    }

                    Map<String, AeaImMajorQualVo> majorQualVoMap = majorQualMap.get("不分等级");
                    if (majorQualVoMap != null && !majorQualVoMap.isEmpty()) {
                        List<AeaImMajorQualVo> majorQualVos = new ArrayList<>();
                        for (String key : majorQualVoMap.keySet()) {
                            majorQualVos.add(majorQualVoMap.get(key));
                        }

                        selectedQualVo.setMajors(BusinessUtils.listToTree(majorQualVos));
                    }

                }
            }
        }

        return selectedQualMajorRequire;
    }

    private void putMajorQualMap(Map<String, AeaImServiceMajor> allMajorMap, Map<String, Map<String, AeaImMajorQualVo>> majorQualMap, String qualLevelId, AeaImMajorQualVo aeaImMajorQualVo) {
        String majorSeq = aeaImMajorQualVo.getMajorSeq();
        String majorId = aeaImMajorQualVo.getMajorId();
        String[] majorIds = majorSeq.split(".");
        for (String id : majorIds) {
            if (StringUtils.isNotBlank(id) && !id.equals(majorId)) {
                Map<String, AeaImMajorQualVo> majorQualVoMap = majorQualMap.get(qualLevelId);

                if (majorQualVoMap == null) {
                    majorQualVoMap = new HashMap();
                    majorQualMap.put(qualLevelId, majorQualVoMap);
                }

                AeaImMajorQualVo majorQualVo = majorQualVoMap.get(id);

                if (majorQualVo == null) {
                    AeaImServiceMajor aeaImServiceMajor = allMajorMap.get(id);
                    if (aeaImServiceMajor != null) {
                        majorQualVo = getAeaImMajorQualVo(aeaImServiceMajor);
                        majorQualVoMap.put(id, majorQualVo);

                    }
                }
            }
        }

        Map<String, AeaImMajorQualVo> majorQualVoMap = majorQualMap.get(qualLevelId);

        if (majorQualVoMap == null) {
            majorQualVoMap = new HashMap();
            majorQualMap.put(qualLevelId, majorQualVoMap);
        }

        majorQualVoMap.put(aeaImMajorQualVo.getMajorId(), aeaImMajorQualVo);


    }

    private AeaImMajorQualVo getAeaImMajorQualVo(AeaImServiceMajor aeaImServiceMajor) {
        AeaImMajorQualVo majorQualVo = new AeaImMajorQualVo();
        majorQualVo.setId(aeaImServiceMajor.getId());
        majorQualVo.setName(aeaImServiceMajor.getMajorName());
        majorQualVo.setMajorId(aeaImServiceMajor.getId());
        majorQualVo.setpId(aeaImServiceMajor.getParentMajorId());
        majorQualVo.setParentMajorId(aeaImServiceMajor.getParentMajorId());
        majorQualVo.setMajorName(aeaImServiceMajor.getMajorName());
        majorQualVo.setMajorSeq(aeaImServiceMajor.getMajorSeq());
        majorQualVo.setNocheck(true);
        return majorQualVo;
    }

    /**
     * 通过父id查询等级列表
     *
     * @param parentQualLevelId
     * @return
     * @throws Exception
     */
    private List<AeaImQualLevel> getQualLevelListByParentQualLevelId(String parentQualLevelId) throws Exception {
        AeaImQualLevel qualLevel = new AeaImQualLevel();
        qualLevel.setParentQualLevelId(parentQualLevelId);
        qualLevel.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
        qualLevel.setRootOrgId(topOrgId);
        return aeaImQualLevelMapper.listAeaImQualLevel(qualLevel);
    }


    /**
     * 通过资质id查询专业
     *
     * @param qualId
     * @return
     * @throws Exception
     */
    private List<AeaImServiceMajor> getServiceMajorListByQualId(String qualId) throws Exception {
        AeaImServiceMajor queryMajor = new AeaImServiceMajor();
        queryMajor.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
        queryMajor.setQualId(qualId);
        queryMajor.setRootOrgId(topOrgId);
        return aeaImServiceMajorMapper.listAeaImServiceMajor(queryMajor);
    }

    public List<AeaUnitInfo> getAeaUnitInfoByPage(String keyword, Page page) throws Exception {
        if (page != null) {
            PageHelper.startPage(page);
        }

        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        aeaUnitInfo.setKeyword(keyword);
        aeaUnitInfo.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        return aeaUnitInfoMapper.listAeaUnitInfo(aeaUnitInfo);
    }

    public ShowProjPurchaseVo showProjPurchaseByProjPurchaseId(String projPurchaseId) throws Exception {
        if (StringUtils.isNotBlank(projPurchaseId)) {
            AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(projPurchaseId);

            if (aeaImProjPurchase != null) {
                ShowProjPurchaseVo showProjPurchaseVo = new ShowProjPurchaseVo();
                BeanUtils.copyProperties(aeaImProjPurchase, showProjPurchaseVo);

                AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getOnlyAeaProjInfoById(aeaImProjPurchase.getProjInfoId());

                if (aeaProjInfo != null) {
                    SaveAeaImProjPurchaseVo.SaveAeaProjInfoVo saveAeaProjInfoVo = new SaveAeaImProjPurchaseVo.SaveAeaProjInfoVo();
                    BeanUtils.copyProperties(aeaProjInfo, saveAeaProjInfoVo);
                    if ("1".equals(aeaImProjPurchase.getIsApproveProj())) {
                        AeaProjInfo parentProjInfo = aeaProjInfoMapper.getAeaProjInfoByChildProjId(aeaProjInfo.getProjInfoId());

                        if (parentProjInfo != null) {
                            saveAeaProjInfoVo.setApprovalCode(parentProjInfo.getLocalCode());
                            saveAeaProjInfoVo.setParentProjId(parentProjInfo.getProjInfoId());
                        }

                    }

                    showProjPurchaseVo.setAeaProjInfoVo(saveAeaProjInfoVo);

                }

                AeaImUnitRequire aeaImUnitRequire = aeaImUnitRequireMapper.getAeaImUnitRequireById(aeaImProjPurchase.getUnitRequireId());

                showProjPurchaseVo.setAeaImUnitRequire(aeaImUnitRequire);

                if (StringUtils.isNotBlank(aeaImProjPurchase.getServiceItemId())) {
                    AeaImServiceItem aeaImServiceItem = aeaImServiceItemMapper.getAeaImServiceItemByServiceItemId(aeaImProjPurchase.getServiceItemId());

                    if (aeaImServiceItem != null) {
                        List<AeaItemServiceVo> aeaItemServiceVos = aeaItemBasicMapper.listItemServiceVo(null, aeaImServiceItem.getItemVerId());
                        if (!aeaItemServiceVos.isEmpty()) {
                            AeaItemServiceVo aeaItemServiceVo = aeaItemServiceVos.get(0);
                            showProjPurchaseVo.setAeaItemServiceVo(aeaItemServiceVo);
                            List<AeaImServiceVo> aeaImServices = getItemServiceListByItemVerId(aeaItemServiceVo.getAgentItemVerId());
                            showProjPurchaseVo.setAeaImServices(aeaImServices);
                        }

                    }
                }

                return showProjPurchaseVo;

            }
        }

        return null;
    }

    public void submitProjPurchaseByProjPurchaseId(String projPurchaseId, HttpServletRequest request) throws Exception {

        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        checkIsOwner(loginInfoVo);
//        checkIsPersonAccount(loginInfoVo);

        if (StringUtils.isNotBlank(projPurchaseId)) {
            AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getAeaImProjPurchaseByProjPurchaseId(projPurchaseId);
            if (aeaImProjPurchase != null) {

                if (!AuditFlagStatus.NO_COMMIT.equals(aeaImProjPurchase.getAuditFlag()) && !AuditFlagStatus.AUDIT_RETURN.equals(aeaImProjPurchase.getAuditFlag())) {
                    throw new RuntimeException("当前状态不可提交");
                }

                saveAeaImPurchaseinst(loginInfoVo, aeaImProjPurchase, AeaImPurchaseinst.OperateAction.新增采购需求.getAction(), null, null);
                changeAeaImProjPurchaseAuditFlag(projPurchaseId, AuditFlagStatus.AUDIT_PROGRESS);
            }
        } else {
            throw new RuntimeException("projPurchaseId为空");
        }
    }


    public ContentResultForm listProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        ContentResultForm resultForm = new ContentResultForm(false);
        if (aeaImProjPurchase != null && pageSize > 0 && pageNum > 0 &&
                (StringUtils.isNotBlank(aeaImProjPurchase.getLinkmanInfoId()) || StringUtils.isNotBlank(aeaImProjPurchase.getPublishUnitInfoId()))) {
            Page page = new Page(pageNum, pageSize);
            PageHelper.startPage(page);
            List<AeaImProjPurchase> list = aeaImProjPurchaseMapper.listProjPurchase(aeaImProjPurchase);
            EasyuiPageInfo info = PageHelper.toEasyuiPageInfo(new PageInfo(list));
            resultForm.setSuccess(true);
            resultForm.setContent(info);
        } else {
            resultForm.setMessage("参数有误。");
        }
        return resultForm;
    }

    public ContentResultForm getProjPurchaseInfoByProjPurchaseId(String projPurchaseId) throws Exception {
        ContentResultForm resultForm = new ContentResultForm(false);
        if (StringUtils.isNotBlank(projPurchaseId)) {
            AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getProjPurchaseInfoByProjPurchaseId(projPurchaseId);
            if (aeaImProjPurchase != null && "1".equals(aeaImProjPurchase.getIsApproveProj())) {
                // 获取投资审批项目（父项目）
                AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoByChildProjId(aeaImProjPurchase.getProjInfoId());
                if (aeaProjInfo != null) {
                    aeaImProjPurchase.setParentProjName(aeaProjInfo.getProjName());
                    aeaImProjPurchase.setParentLocalCode(aeaProjInfo.getLocalCode());
                }
            }
            resultForm.setSuccess(true);
            resultForm.setContent(aeaImProjPurchase);
        }
        return resultForm;
    }

    public void applyProjPurchaseInvalid(String projPurchaseId, String memo, List<MultipartFile> files, HttpServletRequest request) throws Exception {

        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        checkIsOwner(loginInfoVo);
        checkIsPersonAccount(loginInfoVo);

        if (StringUtils.isBlank(projPurchaseId)) {
            throw new InvalidParameterException(projPurchaseId);
        }

        AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getProjPurchaseInfoByProjPurchaseId(projPurchaseId);

        if (aeaImProjPurchase != null) {

            saveAeaImPurchaseinst(loginInfoVo, aeaImProjPurchase, AeaImPurchaseinst.OperateAction.采购需求置为无效.getAction(), memo, files);

            //转入审核状态
            changeAeaImProjPurchaseAuditFlag(projPurchaseId, AuditFlagStatus.AUDIT_PROGRESS);
        } else {
            throw new RuntimeException("查询不到对应的采购项目");
        }

    }

    private AeaImPurchaseinst saveAeaImPurchaseinst(LoginInfoVo loginInfoVo, AeaImProjPurchase aeaImProjPurchase, String operateAction, String memo, List<MultipartFile> files) throws Exception {
        String parentPurchaseinstId = null;
        AeaImPurchaseinst parentPurchaseinst = aeaImPurchaseinstMapper.getlastestPurchaseinstByProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());

        if (parentPurchaseinst != null) {
            parentPurchaseinstId = parentPurchaseinst.getPurchaseinstId();
        }

        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(UuidUtil.generateUuid());
        aeaImPurchaseinst.setParentPurchaseinstId(parentPurchaseinstId);
        aeaImPurchaseinst.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
        aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserName());
        aeaImPurchaseinst.setCreateTime(new Date());
        aeaImPurchaseinst.setLinkmanInfoId(aeaImProjPurchase.getLinkmanInfoId());
        aeaImPurchaseinst.setOldPurchaseFlag(aeaImProjPurchase.getAuditFlag());
        aeaImPurchaseinst.setNewPurchaseFlag(aeaImProjPurchase.getAuditFlag());
        aeaImPurchaseinst.setOperateAction(operateAction);
        aeaImPurchaseinst.setOperateDescribe(memo);
        aeaImPurchaseinst.setIsOwnFile(Status.OFF);
        aeaImPurchaseinst.setRootOrgId(topOrgId);
        if (files != null && !files.isEmpty()) {
            FileUtils.uploadFile("AEA_IM_PURCHASEINST", "PURCHASEINST_ID", aeaImPurchaseinst.getPurchaseinstId(), files);
            aeaImPurchaseinst.setIsOwnFile(Status.ON);
        }

        aeaImPurchaseinstMapper.insertAeaImPurchaseinst(aeaImPurchaseinst);

        return aeaImPurchaseinst;
    }

    /**
     * 判断是否为个人账号
     *
     * @param loginInfoVo 登录信息
     */
    private void checkIsPersonAccount(LoginInfoVo loginInfoVo) {
        if (loginInfoVo == null) {
            throw new RuntimeException("未登录或登录超时，请重新登录！");
        }

        if (StringUtils.isBlank(loginInfoVo.getUserId())) {
            throw new RuntimeException("不是个人账号！");
        }
    }

    /**
     * 转换采购需求状态
     *
     * @param projPurchaseId 采购需求ID
     * @param auditFlag      要转入的采购需求状态
     */
    private void changeAeaImProjPurchaseAuditFlag(String projPurchaseId, String auditFlag) {
        AeaImProjPurchase updateAeaImProjPurchase = new AeaImProjPurchase();
        updateAeaImProjPurchase.setAuditFlag(auditFlag);
        updateAeaImProjPurchase.setModifier(SecurityContext.getCurrentUserName());
        updateAeaImProjPurchase.setModifyTime(new Date());
        updateAeaImProjPurchase.setProjPurchaseId(projPurchaseId);
        if (AuditFlagStatus.AUDIT_PROGRESS.equals(auditFlag)) {
            updateAeaImProjPurchase.setPublishTime(new Date());
        }
        aeaImProjPurchaseMapper.updateAeaImProjPurchase(updateAeaImProjPurchase);
    }

    public ContentResultForm getPublishingInfoByProjPurchaseId(String projPurchaseId, String unitInfoId) throws Exception {
        ContentResultForm resultForm = new ContentResultForm(false);
        if (StringUtils.isNotBlank(projPurchaseId)) {
            AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getPublishingInfoByProjPurchaseId(projPurchaseId, unitInfoId);

            if (aeaImProjPurchase != null && "1".equals(aeaImProjPurchase.getIsApproveProj())) {
                List<AeaImUnitBidding> list = new ArrayList<AeaImUnitBidding>();
                list = aeaImUnitBiddingMapper.listUnitBiddingByProjPurchaseId(projPurchaseId);
                if (list.size() > 0) {
                    aeaImProjPurchase.setUnitBiddingList(list);
                }
                // 获取投资审批项目（父项目）
                AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoByChildProjId(aeaImProjPurchase.getProjInfoId());
                if (aeaProjInfo != null) {
                    aeaImProjPurchase.setParentProjName(aeaProjInfo.getProjName());
                    aeaImProjPurchase.setParentLocalCode(aeaProjInfo.getLocalCode());
                    aeaImProjPurchase.setProjScale(aeaProjInfo.getProjScale());
                    aeaImProjPurchase.setFinancialSource(aeaProjInfo.getFinancialSource());
                }
                AeaImContract aeaImContract = aeaImContractMapper.getAeaImContractById(aeaImProjPurchase.getContractId());
                if (aeaImContract != null) {
                    // 合同附件
                    aeaImContract.setBscAttForms(bscAttMapper.listAttLinkAndDetailByTablePKRecordId("AEA_IM_CONTRACT", "CONTRACT_ID", aeaImContract.getContractId(), topOrgId));
                    aeaImProjPurchase.setContract(aeaImContract);
                }
                AeaImServiceResult aeaImServiceResult = aeaImServiceResultMapper.getAeaImServiceResultById(aeaImProjPurchase.getServiceResultId());
                // 服务结果附件
                if (aeaImServiceResult != null) {
                    aeaImServiceResult.setBscAttForms(bscAttMapper.listAttLinkAndDetailByTablePKRecordId("AEA_IM_SERVICE_RESULT", "SERVICE_RESULT_ID", aeaImServiceResult.getServiceResultId(), topOrgId));
                }
                aeaImProjPurchase.setServiceResult(aeaImServiceResult);
            }
            resultForm.setSuccess(true);
            resultForm.setContent(aeaImProjPurchase);
        }
        return resultForm;
    }

    public AeaImProjPurchase reselectProjPurchase(String projPurchaseId, String memo, List<MultipartFile> files, HttpServletRequest request) throws Exception {
        try {
            LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
            checkIsOwner(loginInfoVo);
            checkIsPersonAccount(loginInfoVo);

            if (StringUtils.isBlank(projPurchaseId)) {
                throw new InvalidParameterException(projPurchaseId);
            }

            AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getProjPurchaseInfoByProjPurchaseId(projPurchaseId);

            if (aeaImProjPurchase != null) {

                saveAeaImPurchaseinst(loginInfoVo, aeaImProjPurchase, AeaImPurchaseinst.OperateAction.修改中选机构.getAction(), memo, files);

                changeAeaImProjPurchaseAuditFlag(projPurchaseId, AuditFlagStatus.WAIT_CHOOSE);
            } else {
                throw new RuntimeException("查询不到对应的采购项目");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public List<AeaImService> getServiceTypeList(String unitInfoId) throws Exception {
        try {
            if (StringUtils.isNotBlank(unitInfoId)) {
                List<AeaImService> list = aeaImProjPurchaseMapper.getServiceTypeList(unitInfoId);
                return list;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public void applyPostponeService(ApplyPostponeServiceData applyPostponeServiceData, String memo, List<MultipartFile> files, HttpServletRequest request) throws Exception {
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        checkIsOwner(loginInfoVo);
        checkIsPersonAccount(loginInfoVo);

        if (applyPostponeServiceData == null || StringUtils.isBlank(applyPostponeServiceData.getProjPurchaseId())) {
            throw new InvalidParameterException(applyPostponeServiceData.getProjPurchaseId());
        }

        if (applyPostponeServiceData.getServiceEndTime() == null) {
            throw new InvalidParameterException(applyPostponeServiceData.getServiceEndTime());
        }

        AeaImProjPurchase aeaImProjPurchase = aeaImProjPurchaseMapper.getProjPurchaseInfoByProjPurchaseId(applyPostponeServiceData.getProjPurchaseId());

        if (aeaImProjPurchase != null) {

            AeaImPurchaseinst aeaImPurchaseinst = saveAeaImPurchaseinst(loginInfoVo, aeaImProjPurchase, AeaImPurchaseinst.OperateAction.修改合同.getAction(), memo, files);
            updatePurchaseinstApplyData(aeaImPurchaseinst.getPurchaseinstId(), applyPostponeServiceData);

            AeaImContract aeaImContract = aeaImContractMapper.getAeaImContractByProjPurchaseId(aeaImPurchaseinst.getPurchaseinstId());

            if (aeaImContract == null) {
                throw new RuntimeException("查询不到对应的服务合同");
            }

            aeaImContract.setAuditFlag(AeaImContract.ContractAuditFlag.审核中.getAuditFlag());

            aeaImContractMapper.updateAeaImContract(aeaImContract);
        } else {
            throw new RuntimeException("查询不到对应的采购项目");
        }
    }

    private void updatePurchaseinstApplyData(String purchaseinstId, Object obj) throws Exception {
        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(purchaseinstId);
        aeaImPurchaseinst.setApplyData(JSON.toJSONString(obj));
        aeaImPurchaseinstMapper.updateAeaImPurchaseinst(aeaImPurchaseinst);
    }

    public OwnerIndexData showProjPurchaseData(String unitInfoId, String linkmanInfoId) throws Exception {
        OwnerIndexData ownerIndexData = new OwnerIndexData();
        try {
            if (StringUtils.isNotBlank(unitInfoId) || StringUtils.isNotBlank(linkmanInfoId)) {
                AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
                aeaImProjPurchase.setPublishUnitInfoId(unitInfoId);
                aeaImProjPurchase.setLinkmanInfoId(linkmanInfoId);
                List<AeaImProjPurchase> list = aeaImProjPurchaseMapper.listProjPurchase(aeaImProjPurchase);
                ownerIndexData.changeToSum(list);
                return ownerIndexData;
            }
        } catch (Exception e) {
            return ownerIndexData;
        }

        return ownerIndexData;

    }

    public AeaProjInfo getProjInfoByLocalCode(String localCode) throws Exception {
        List<AeaProjInfo> aeaProjInfoList = aeaProjInfoMapper.getProjInfoByCode(localCode);
        if (aeaProjInfoList != null && aeaProjInfoList.size() > 0) {
            return aeaProjInfoList.get(0);
        }

        return null;
    }

    public ContentResultForm queryIntermediaryList(String projPurchaseId, int pageSize, int pageNum) {
        ContentResultForm resultForm = new ContentResultForm(false);
        if (pageSize > 0 && pageNum > 0) {
            Page page = new Page(pageNum, pageSize);
            PageHelper.startPage(page);
            List<Map<String, Object>> list = aeaImProjPurchaseMapper.queryIntermediaryList(projPurchaseId);
            EasyuiPageInfo info = PageHelper.toEasyuiPageInfo(new PageInfo(list));
            resultForm.setSuccess(true);
            resultForm.setContent(info);
        } else {
            resultForm.setMessage("参数有误。");
        }
        return resultForm;
    }

    public ContentResultForm updateIntermediaryWonBidStatus(String unitBiddingId, String projPurchaseId) throws Exception {
        ContentResultForm resultForm = new ContentResultForm(false);
        aeaImProjPurchaseMapper.updateIntermediaryWonBidStatus(unitBiddingId);
        AeaImProjPurchase aeaimprojpurchase = new AeaImProjPurchase();
        aeaimprojpurchase.setProjPurchaseId(projPurchaseId);
        aeaimprojpurchase.setAuditFlag("9");
        aeaimprojpurchase.setModifier(SecurityContext.getCurrentUserName());
        aeaimprojpurchase.setModifyTime(new Date());
        this.updateAeaImProjPurchase(aeaimprojpurchase);
        resultForm.setSuccess(true);
        return resultForm;
    }

    public void updateAeaImProjPurchase(AeaImProjPurchase aeaImProjPurchase) throws Exception {
        aeaImProjPurchaseMapper.updateAeaImProjPurchase(aeaImProjPurchase);
    }

    /**
     * 发布项目采购需求并启动流程---唐山模式
     *
     * @param purchaseVo
     * @return
     */
    public ResultForm startProjPurchaseAndProcess(SaveAeaImProjPurchaseVo purchaseVo, HttpServletRequest request) throws Exception {
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        checkIsOwner(loginInfoVo);
        this.validParams(purchaseVo);
        String isPersonAccount = loginInfoVo.getIsPersonAccount();
        purchaseVo.setRootOrgId(topOrgId);
        purchaseVo.setCreater(SecurityContext.getCurrentUserName());
        if ("1".equals(isPersonAccount)) {//个人账号
            purchaseVo.setApplySubject("0");
            purchaseVo.setCreater(loginInfoVo.getPersonName());
        } else {
            purchaseVo.setCreater(loginInfoVo.getUnitName());
            purchaseVo.setApplySubject("1");
        }
        //需要先保存 采购项目信息，发起事项流程时关联的是采购项目信息
        AeaProjInfo aeaProjInfo = purchaseVo.createAeaProjInfo();
        aeaProjInfo.setIsDeleted("0");
        aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);
        String projInfoId = aeaProjInfo.getProjInfoId();//采购项目 项目ID
        ImItemApplyData applyData = purchaseVo.createItemApplyData();
        applyData.setProjInfoId(projInfoId);//回填采购的项目ID
        String linkmanId = "";
        String publishUnitInfoId = null;
        String publishLinkmanInfoId = null;
        if (StringUtils.isNotBlank(isPersonAccount) && "0".equals(isPersonAccount)) {
            //单位
            String unitId = loginInfoVo.getUnitId();
            publishUnitInfoId = unitId;
            applyData.setConstructionUnitId(loginInfoVo.getUnitId());
            List<AeaLinkmanInfo> aeaLinkmanInfos = aeaImServiceLinkmanMapper.listAeaImServiceLinkmanByUnitInfoId(unitId, null, null);
            if (aeaLinkmanInfos.isEmpty()) throw new Exception("can not find linkman");
            linkmanId = aeaLinkmanInfos.get(0).getLinkmanInfoId();
        } else {
            linkmanId = loginInfoVo.getUserId();
            publishLinkmanInfoId = linkmanId;
            applyData.setApplyLinkmanId(loginInfoVo.getUserId());
        }
        applyData.setLinkmanInfoId(linkmanId);
        //发起中介事项流程
        ApplyinstResult result = restImApplyService.purchaseStartProcess(applyData);
        String applyinstId = result.getApplyinstId();
        String applyinstCode = result.getApplyinstCode();
        //保存采购信息
        ImPurchaseData purchaseData = purchaseVo.createPurchaseData(applyinstId, applyinstCode);
        //采购项目 项目ID
        purchaseData.setProjInfoId(projInfoId);
        purchaseData.setLinkmanInfoId(linkmanId);
        purchaseData.setPublishLinkmanInfoId(publishLinkmanInfoId);
        purchaseData.setPublishUnitInfoId(publishUnitInfoId);
        //设置审批项目ID
        purchaseData.setApproveProjInfoId(purchaseVo.getSaveAeaProjInfoVo().getParentProjId());
        restImApplyService.savePurchaseProjInfo(purchaseData);

//        //保存受理回执，物料回执
//        if (StringUtils.isBlank(applyinstIdParam)) {
//            receiveService.saveReceive(new String[]{applyinstId}, new String[]{"1", "2"}, SecurityContext.getCurrentUserName(), "");
//        }
        return new ContentResultForm<>(true, applyinstId, "Series start process success");
    }

    public void validParams(SaveAeaImProjPurchaseVo purchaseVo) throws Exception {

        if (purchaseVo == null) {
            throw new RuntimeException("缺少采购信息");
        }

        AeaImUnitRequire aeaImUnitRequire = purchaseVo.getAeaImUnitRequire();

        if (aeaImUnitRequire == null) {
            throw new RuntimeException("缺少中介机构要求信息");
        }

        SaveAeaImProjPurchaseVo.SaveAeaProjInfoVo saveAeaProjInfoVo = purchaseVo.getSaveAeaProjInfoVo();

        if (saveAeaProjInfoVo == null) {
            throw new RuntimeException("缺少采购项目信息");
        }

        if (Status.ON.equals(purchaseVo.getIsApproveProj())) {
            if (StringUtils.isBlank(saveAeaProjInfoVo.getParentProjId())) {
                throw new RuntimeException("缺少审批项目信息");
            }
        }

        if (Status.ON.equals(purchaseVo.getIsAvoid())) {
            if (StringUtils.isBlank(purchaseVo.getAvoidReason())) {
                throw new RuntimeException("缺少回避原因");
            }

            if (StringUtils.isBlank(purchaseVo.getAvoidUnitInfoIds())) {
                throw new RuntimeException("缺少回避单位");
            }
        }
        AeaProjInfo aeaProjInfoCond = new AeaProjInfo();
        aeaProjInfoCond.setProjName(purchaseVo.getSaveAeaProjInfoVo().getProjName());
        List aeaProjInfoCondList = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfoCond);
        if (!aeaProjInfoCondList.isEmpty()) {
            throw new RuntimeException("项目名称已存在:" + purchaseVo.getSaveAeaProjInfoVo().getProjName());
        }
    }


    /**
     * 获取项目采购详情
     *
     * @param projPurchaseId
     * @return
     * @throws Exception
     */
    public PurchaseDetailVo getPurchaseDetail(String projPurchaseId) throws Exception {
        PurchaseDetailVo form = new PurchaseDetailVo();
        //查询采购项目,中介服务，机构要求等 信息
        PurchaseProjVo purchaseProj = aeaImProjPurchaseMapper.getProjPurchaseInfoByApplyinstCode(null, projPurchaseId);
        if (null == purchaseProj) throw new Exception("can not find purchase proj");
        //查询附件
        List<BscAttFileAndDir> officialRemarkFileList = fileUtilsService.getBscAttFileAndDirListByinstId(purchaseProj.getOfficialRemarkFile(), "OFFICIAL_REMARK_FILE", "AEA_IM_PROJ_PURCHASE");
        List<BscAttFileAndDir> requireExplainFileList = fileUtilsService.getBscAttFileAndDirListByinstId(purchaseProj.getRequireExplainFile(), "REQUIRE_EXPLAIN_FILE", "AEA_IM_PROJ_PURCHASE");
        purchaseProj.setOfficialRemarkFileList(officialRemarkFileList);
        purchaseProj.setRequireExplainFileList(requireExplainFileList);

        String isApproveProj = purchaseProj.getIsApproveProj();
        if (StringUtils.isNotBlank(isApproveProj) && "1".equals(isApproveProj)) {
            //查询关联的投资审批项目信息
            AeaParentProj parentProj = aeaParentProjMapper.getParentProjByProjInfoId(purchaseProj.getProjInfoId());
            if (null != parentProj) {
                AeaProjInfo projInfo = aeaProjInfoService.getTransProjInfoDetail(parentProj.getParentProjId());
                form.setAeaProjInfo(projInfo);
            }
        }
        //查询资质信息
        String unitRequireId = purchaseProj.getUnitRequireId();
        String isQualRequire = purchaseProj.getIsQualRequire();
        if (StringUtils.isNotBlank(unitRequireId) && StringUtils.isNotBlank(isQualRequire) && "1".equals(isQualRequire)) {
            List<AeaImMajorQualVo> aeaImMajorQualVos = aeaImMajorQualMapper.listAeaImMajorQualByUnitRequireId(unitRequireId);
            String collect = aeaImMajorQualVos.stream().map((major -> {
                String majorName = major.getMajorName();
                String qualName = major.getQualName();
                String qualLevelName = major.getQualLevelName();
                return qualName + "（" + qualLevelName + "）";
            })).collect(Collectors.joining(","));
            purchaseProj.setQualRequire(collect);
        } else {
            purchaseProj.setQualRequire("无");
        }
        form.setPurchaseProj(purchaseProj);
        AeaHiApplyinst applyinst = aeaHiApplyinstMapper.getAeaHiApplyinstByCode(purchaseProj.getApplyinstCode());
        if (null == applyinst) throw new Exception("can not find applyisnt");
        List<AeaHiIteminst> itemisntList = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applyinst.getApplyinstId());
        //中介事项信息
        if (itemisntList.isEmpty()) throw new Exception("can not find iteminst info ");
        AeaHiIteminst iteminst = itemisntList.get(0);
        String itemProperty = iteminst.getItemProperty();//办件类型
        String dueNumUnit = iteminst.getDueNumUnit();//办理时限单位
        //设置事项办件类型
        BscDicCodeItem item_property = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("ITEM_PROPERTY", itemProperty, "012aa547-7104-418d-87cc-824f24f1a278");
        if (null != item_property) {
            iteminst.setItemProperty(item_property.getItemName());
        }
        //办理时限单位
        BscDicCodeItem dunNumUnit = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("ITEM_PROPERTY", dueNumUnit, "012aa547-7104-418d-87cc-824f24f1a278");
        if (null != item_property) {
            iteminst.setDueNumUnit(item_property.getItemName());
        }
        //服务对象

        String currentOrgId = SecurityContext.getCurrentOrgId();
        String itemVerId = iteminst.getItemVerId();
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, currentOrgId);
        String serviceObjectCode = StringUtils.isNotBlank(aeaItemBasic.getXkdx()) ? aeaItemBasic.getXkdx() : SERVICE_OBJECT_CODE;
        String serviceObject = this.getServiceObject(SERVICE_OBJECT_DICT_NAME, serviceObjectCode, currentOrgId);

        form.changeToIteminst(iteminst, serviceObject);
        return form;
    }

    private String getServiceObject(String dicName, String code, String currentOrgId) {
        if (org.apache.commons.lang3.StringUtils.isBlank(code)) {
            return "";
        }
        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(currentOrgId);
        if (topOrg != null) {
            List<BscDicCodeItem> activeItemsByTypeCode = bscDicCodeService.getActiveItemsByTypeCode(dicName, topOrg.getOrgId());
            if (code.contains(CommonConstant.COMMA_SEPARATOR)) {
                String[] split = code.split(CommonConstant.COMMA_SEPARATOR);
                StringBuilder str = new StringBuilder();
                for (int j = 0; j < split.length; j++) {
                    for (BscDicCodeItem bscDicCodeItem : activeItemsByTypeCode) {
                        if (bscDicCodeItem.getItemCode().equals(split[j])) {
                            if (j != split.length - 1) {
                                str.append(bscDicCodeItem.getItemName()).append(CommonConstant.COMMA_SEPARATOR);
                            } else {
                                str.append(bscDicCodeItem.getItemName());
                            }
                        }
                    }
                }
                return str.toString();
            } else {
                for (BscDicCodeItem bscDicCodeItem : activeItemsByTypeCode) {
                    if (bscDicCodeItem.getItemCode().equals(code)) {
                        return bscDicCodeItem.getItemName();
                    }
                }
            }
        }
        return null;
    }
}
