package com.augurit.aplanmis.common.service.projPurchase.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserMapper;
import com.augurit.aplanmis.common.constants.CertinstSource;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.projPurchase.AeaImProjPurchaseService;
import com.augurit.aplanmis.common.utils.BusinessUtils;
import com.augurit.aplanmis.common.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/***
 * @description 中介服务接口实现类
 * @author mohaoqi
 * @date 2019/11/4 0004
 ***/
@Service
public class AeaImProjPurchaseServiceImpl implements AeaImProjPurchaseService {


    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaImPurchaseinstMapper aeaImPurchaseinstMapper;

    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;

    @Autowired
    private AeaImQualLevelMapper aeaImQualLevelMapper;

    @Autowired
    private AeaImQualMapper aeaImQualMapper;
    @Autowired
    private AeaImServiceEvaluationMapper aeaImServiceEvaluationMapper;

    @Autowired
    private AeaImServiceMajorMapper aeaImServiceMajorMapper;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private BscAttMapper bscAttMapper;

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private OpuOmUserMapper opuOmUserMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    @Override
    public void updateProjPurchaseStateAndInsertPurchaseinstState(String purchaseId, String newState, String operateAction, String opsLinkmanInfoId, String option, String taskId) throws Exception {

        if (StringUtils.isBlank(purchaseId) || StringUtils.isBlank(newState)) return;
        aeaImProjPurchaseMapper.updateProjPurchaseState(newState, SecurityContext.getCurrentUserId(), new Date(), purchaseId);

        AeaImPurchaseinst oldAeaImPurchaseinst = aeaImPurchaseinstMapper.getlastestPurchaseinstByProjPurchaseId(purchaseId);

        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(UUID.randomUUID().toString());
        aeaImPurchaseinst.setProjPurchaseId(purchaseId);
        aeaImPurchaseinst.setNewPurchaseFlag(newState);
        aeaImPurchaseinst.setOperateAction(operateAction);
        aeaImPurchaseinst.setOperateDescribe(option);
        aeaImPurchaseinst.setTaskId(taskId);
        aeaImPurchaseinst.setLinkmanInfoId(opsLinkmanInfoId);
        aeaImPurchaseinst.setParentPurchaseinstId(oldAeaImPurchaseinst.getPurchaseinstId());
        aeaImPurchaseinst.setOldPurchaseFlag(oldAeaImPurchaseinst.getNewPurchaseFlag());
        aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserId());
        aeaImPurchaseinst.setCreateTime(new Date());
        aeaImPurchaseinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);

    }

    @Override
    public void updateProjPurchaseStateAndInsertPurchaseinstStateAndApplyData(String purchaseId, String newState, String operateAction, String opsLinkmanInfoId, String option, String isOwnFile, String applyDataJson, String taskId) throws Exception {

        if (StringUtils.isBlank(purchaseId) || StringUtils.isBlank(newState)) return;
        aeaImProjPurchaseMapper.updateProjPurchaseState(newState, SecurityContext.getCurrentUserId(), new Date(), purchaseId);

        AeaImPurchaseinst oldAeaImPurchaseinst = aeaImPurchaseinstMapper.getlastestPurchaseinstByProjPurchaseId(purchaseId);

        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(UUID.randomUUID().toString());
        aeaImPurchaseinst.setProjPurchaseId(purchaseId);
        aeaImPurchaseinst.setNewPurchaseFlag(newState);
        aeaImPurchaseinst.setIsOwnFile(isOwnFile);
        aeaImPurchaseinst.setOperateAction(operateAction);
        aeaImPurchaseinst.setOperateDescribe(option);
        aeaImPurchaseinst.setTaskId(taskId);
        aeaImPurchaseinst.setLinkmanInfoId(opsLinkmanInfoId);
        aeaImPurchaseinst.setParentPurchaseinstId(oldAeaImPurchaseinst.getPurchaseinstId());
        aeaImPurchaseinst.setOldPurchaseFlag(oldAeaImPurchaseinst.getNewPurchaseFlag());
        aeaImPurchaseinst.setApplyData(applyDataJson);
        aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserId());
        aeaImPurchaseinst.setCreateTime(new Date());
        aeaImPurchaseinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);

    }

    @Override
    public void insertAeaImProjPurchaseAndInsertPurchaseinst(AeaImProjPurchase aeaImProjPurchase) throws Exception {
        aeaImProjPurchase.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaImProjPurchaseMapper.insertAeaImProjPurchase(aeaImProjPurchase);

        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(UUID.randomUUID().toString());
        aeaImPurchaseinst.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
        aeaImPurchaseinst.setNewPurchaseFlag(aeaImProjPurchase.getAuditFlag());
        aeaImPurchaseinst.setIsOwnFile(aeaImProjPurchase.getIsOwnFile());
        aeaImPurchaseinst.setOperateAction(aeaImProjPurchase.getOperateAction());
        aeaImPurchaseinst.setOperateDescribe(aeaImProjPurchase.getOperateDescribe());
        aeaImPurchaseinst.setTaskId(aeaImProjPurchase.getTaskId());
        aeaImPurchaseinst.setProcessinstId(aeaImProjPurchase.getProcessinstId());
        aeaImPurchaseinst.setLinkmanInfoId(aeaImProjPurchase.getLinkmanInfoId());
        aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserId());
        aeaImPurchaseinst.setCreateTime(new Date());
        aeaImPurchaseinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);
    }

    @Override
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

    @Override
    public UploadResult uploadFiles(HttpServletRequest request, String detailId) throws Exception {
        if (StringUtils.isBlank(detailId)) {
            detailId = UUID.randomUUID().toString();
        }

        /*StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> officialRemarkFiles = req.getFiles("officialRemarkFile");
        List<BscAttForm> bscAttForms = new ArrayList<>();
        if (officialRemarkFiles != null && !officialRemarkFiles.isEmpty()) {
            fileUtilsService.uploadAttachments("AEA_IM_PROJ_PURCHASE", "OFFICIAL_REMARK_FILE", detailId, officialRemarkFiles);
            bscAttForms.addAll(fileUtilsService.getAttachmentsByRecordId(new String[]{detailId}, "AEA_IM_PROJ_PURCHASE", "OFFICIAL_REMARK_FILE"));
        }
        List<MultipartFile> requireExplainFiles = req.getFiles("requireExplainFile");
        if (requireExplainFiles != null && !requireExplainFiles.isEmpty()) {
            fileUtilsService.uploadAttachments("AEA_IM_PROJ_PURCHASE", "REQUIRE_EXPLAIN_FILE", detailId, requireExplainFiles);
            bscAttForms.addAll(fileUtilsService.getAttachmentsByRecordId(new String[]{detailId}, "AEA_IM_PROJ_PURCHASE", "REQUIRE_EXPLAIN_FILE"));
        }*/

        List<BscAttForm> bscAttForms = new ArrayList<>();
        if (request instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            /***要求说明文件***/
            List<MultipartFile> requireExplainFiles = req.getFiles("requireExplainFile");
            if (requireExplainFiles != null && !requireExplainFiles.isEmpty()) {
                fileUtilsService.uploadAttachments("AEA_IM_PROJ_PURCHASE", "REQUIRE_EXPLAIN_FILE", detailId, requireExplainFiles);
                bscAttForms.addAll(fileUtilsService.getAttachmentsByRecordId(new String[]{detailId}, "AEA_IM_PROJ_PURCHASE", "REQUIRE_EXPLAIN_FILE"));
            }
            /***批文文件***/
            List<MultipartFile> officialRemarkFiles = req.getFiles("officialRemarkFile");
            if (officialRemarkFiles != null && !officialRemarkFiles.isEmpty()) {
                fileUtilsService.uploadAttachments("AEA_IM_PROJ_PURCHASE", "OFFICIAL_REMARK_FILE", detailId, officialRemarkFiles);
                bscAttForms.addAll(fileUtilsService.getAttachmentsByRecordId(new String[]{detailId}, "AEA_IM_PROJ_PURCHASE", "OFFICIAL_REMARK_FILE"));
            }
        }
        UploadResult result = new UploadResult(detailId, bscAttForms);
        return result;
    }

    public List<AgentUnitInfoVo> getAgentUnitInfoList(QueryAgentUnitInfoVo queryAgentUnitInfo) throws Exception {

        List<AgentUnitInfoVo> agentUnitInfoVos = new ArrayList<>();

        if (queryAgentUnitInfo == null) {
            return agentUnitInfoVos;
        }

        //默认数据为选中的节点
        if (StringUtils.isBlank(queryAgentUnitInfo.getOnlySelected())) {
            queryAgentUnitInfo.setOnlySelected("1");
        }

        String serviceId = queryAgentUnitInfo.getServiceId();

        List<QueryAgentUnitInfoVo.Qual> quals = queryAgentUnitInfo.getQuals();

        //false为将多选一应用于同个父id的专业,true为每个专业必须完全符合
        boolean matchAll = "0".equals(queryAgentUnitInfo.getQualRequireType());

        //false为将多选一应用于资质,true为每个资质必须完全符合
        //boolean matchAllQual = "0".equals(queryAgentUnitInfo.getQualRequireType());
        boolean matchAllQual = true;

        AgentUnitInfoVo.Require require = new AgentUnitInfoVo.Require(true);

        //true为有资质要求，需要筛选
        if ("1".equals(queryAgentUnitInfo.getIsQualRequire())) {

            //true为要求列表只包含选中的节点，false为要求列表为一整个树结构
            if ("1".equals(queryAgentUnitInfo.getOnlySelected())) {
                agentUnitInfoVos = getMatchUnitInfoByOnlySelectedData(require, serviceId, quals, matchAll, matchAllQual);
            } else {
                agentUnitInfoVos = getMatchUnitInfo(require, serviceId, quals, matchAll, matchAllQual);
            }
        }

        //true为没有勾选资质要求或者传上来的要求列表都没有勾选专业
        if (require.isNoRequire()) {
            agentUnitInfoVos = aeaImProjPurchaseMapper.listUnitInfoIdByServiceId(serviceId);
        }

        //计算中介机构的综合评价
        computeServiceEvaluation(agentUnitInfoVos, serviceId);

        return agentUnitInfoVos;
    }

    /**
     * 单个或批量删除
     *
     * @param recordId  关联id
     * @param detailIds 附件ID
     */
    @Override
    public UploadResult batchDelete(String recordId, String detailIds) throws Exception {

        if (StringUtils.isNotBlank(detailIds)) {
            String[] split = detailIds.split(",");
            fileUtilsService.deleteAttachments(split);
        }
        if (StringUtils.isBlank(recordId)) return new UploadResult(recordId, new ArrayList<>());
        BscAttLink link = new BscAttLink();
        link.setRecordId(recordId);

        List<BscAttLink> bscAttLinks = bscAttMapper.listBscAttLink(link);
        if (!bscAttLinks.isEmpty()) {
            BscAttLink attLink = bscAttLinks.get(0);
            List<BscAttForm> attachments = fileUtilsService.getAttachmentsByRecordId(recordId.split(","), attLink.getTableName(), attLink.getPkName());
            return new UploadResult(recordId, attachments);
        }

        return new UploadResult(recordId, new ArrayList<>());
    }


    /**
     * 获取采购项目材料及附件列表
     *
     * @param iteminstId  事项实例ID
     * @param applyinstId 申请实例ID
     * @return List<MatinstVo>
     * @throws Exception E
     *                   todo 待优化
     */
    @Override
    public List<MatinstVo> listPurchaseMatinst(String iteminstId, String applyinstId) throws Exception {
        List<MatinstVo> matinstVos = new ArrayList();

        String itemVerIds = "";
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (null == applyinst) throw new Exception("can not find applyinst");
        if ("0".equals(applyinst.getIsSeriesApprove())) throw new Exception("not support stageinst");

        if (StringUtils.isBlank(iteminstId)) {
            List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            if (iteminsts.size() == 0) return matinstVos;
            iteminstId = iteminsts.get(0).getIteminstId();
        }
        List<AeaHiItemMatinst> matinstList = aeaHiItemMatinstService.getMatinstListByIteminstIds(new String[]{iteminstId}, "1");

        List<AeaItemMat> matList = aeaItemMatService.getMatListByApplyinstId(applyinstId, iteminstId);
        for (AeaItemMat mat : matList) {
            List<AeaHiItemMatinst> attInstList = new ArrayList<>();
            List<AeaHiItemMatinst> pageInstList = new ArrayList<>();
            List<AeaHiItemMatinst> copyInstList = new ArrayList<>();
            List<AeaHiItemMatinst> forminstList = new ArrayList<>();
            List<AeaHiItemMatinst> certinstList = new ArrayList<>();

            for (AeaHiItemMatinst matinst : matinstList) {
                if (mat.getMatCode().equals(matinst.getMatinstCode())) {

                    if ("f".equals(matinst.getMatProp()) && StringUtils.isNotBlank(matinst.getStoFormId())) {
                        forminstList.add(matinst);
                        continue;
                    }

                    if ("c".equals(matinst.getMatProp()) && StringUtils.isNotBlank(matinst.getCertinstId())) {
                        certinstList.add(matinst);
                        continue;
                    }

                    if (matinst.getAttCount() != null && matinst.getAttCount() > 0)
                        attInstList.add(matinst);
                    if (matinst.getRealPaperCount() != null && matinst.getRealPaperCount() > 0)
                        pageInstList.add(matinst);
                    if (matinst.getRealCopyCount() != null && matinst.getRealCopyCount() > 0)
                        copyInstList.add(matinst);
                }
            }
            mat.setAttMatinstList(attInstList);
            mat.setCopyMatinstList(copyInstList);
            mat.setPageMatinstList(pageInstList);
            mat.setForminstList(forminstList);
            mat.setCertinstList(certinstList);

            MatinstVo matinstVo = new MatinstVo();
            BeanUtils.copyProperties(mat, matinstVo);
            if (mat.getPageMatinstList().size() > 0) {
                AeaHiItemMatinst matinst = mat.getPageMatinstList().get(0);
                matinstVo.setPaperMatinstId(matinst.getMatinstId());
                matinstVo.setRealPaperCount(matinst.getRealPaperCount());
            }

            if (mat.getCopyMatinstList().size() > 0) {
                AeaHiItemMatinst matinst = mat.getCopyMatinstList().get(0);
                matinstVo.setCopyMatinstId(matinst.getMatinstId());
                matinstVo.setRealCopyCount(matinst.getRealCopyCount());
            }

            if (mat.getAttMatinstList().size() > 0) {
                AeaHiItemMatinst matinst = mat.getAttMatinstList().get(0);
                matinstVo.setAttMatinstId(matinst.getMatinstId());
                matinstVo.setAttCount(matinst.getAttCount());
                matinstVo.setFileList(fileUtilsService.getMatAttDetailByMatinstId(matinst.getMatinstId()));
            }

            if (mat.getCertinstList().size() > 0) {
                List<Map> certinstList1 = new ArrayList();
                Map obj = null;
                for (AeaHiItemMatinst certMatinst : mat.getCertinstList()) {
                    obj = new HashMap();
                    //来自于本地系统的证照实例
                    if (CertinstSource.LOCAL.getValue().equals(certMatinst.getCertinstSource())) {
                        obj.put("certFileList", fileUtilsService.getMatAttDetailByMatinstId(certMatinst.getCertinstId()));
                    } else if (CertinstSource.EXTERNAL.getValue().equals(certMatinst.getCertinstSource())) {//来自于外部系统的证照实例
                        OpuOmUser user = opuOmUserMapper.getUserByUserId(certMatinst.getCreater());
                        List<BscAttFileAndDir> bscAttFileAndDirs = new ArrayList();
                        BscAttFileAndDir fileAndDir = new BscAttFileAndDir();
                        fileAndDir.setFileId(certMatinst.getAuthCode());
                        fileAndDir.setFileName(certMatinst.getCertisntName());
                        fileAndDir.setCreaterName(user != null ? user.getUserName() : certMatinst.getCreater());
                        fileAndDir.setUpdateTime(certMatinst.getCreateTime());
                        bscAttFileAndDirs.add(fileAndDir);
                        obj.put("certFileList", bscAttFileAndDirs);
                    }

                    obj.put("certMatinstId", certMatinst.getMatinstId());
                    obj.put("certinstSource", certMatinst.getCertinstSource());
                    certinstList1.add(obj);
                }
                matinstVo.setCertinstList(certinstList1);
            }
            matinstVo.setItemVerIds(itemVerIds);
            if (mat.getForminstList().size() > 0) {
                matinstVo.setForminstId(mat.getForminstList().get(0).getStoForminstId());
            }

            matinstVos.add(matinstVo);
        }
        return matinstVos;
    }

    /**
     * 获取中介机构服务综合评价
     *
     * @param agentUnitInfoVos
     * @param serviceId
     * @throws Exception
     */
    private void computeServiceEvaluation(List<AgentUnitInfoVo> agentUnitInfoVos, String serviceId) throws Exception {
        if (!agentUnitInfoVos.isEmpty()) {
            for (AgentUnitInfoVo agentUnitInfoVo : agentUnitInfoVos) {
                List<AeaImServiceEvaluation> list = aeaImServiceEvaluationMapper.listServiceEvaluationByUnitInfoIdAndServiceId(agentUnitInfoVo.getUnitInfoId(), serviceId);
                if (list != null && !list.isEmpty()) {
                    int count = list.size();
                    BigDecimal total = new BigDecimal(0);
                    for (AeaImServiceEvaluation aeaImServiceEvaluation : list) {
                        if (StringUtils.isNotBlank(aeaImServiceEvaluation.getCompEvaluation())) {
                            try {
                                BigDecimal bigDecimal = new BigDecimal(aeaImServiceEvaluation.getCompEvaluation());
                                total = total.add(bigDecimal);
                            } catch (Exception e) {
                                count--;
                            }
                        } else {
                            count--;
                        }
                    }

                    if (count > 0) {
                        BigDecimal result = total.divide(new BigDecimal(count));
                        agentUnitInfoVo.setCompEvaluation(result.setScale(2, RoundingMode.HALF_UP).toString());
                    }
                }
            }
        }
    }

    private List<AgentUnitInfoVo> getMatchUnitInfoByOnlySelectedData(AgentUnitInfoVo.Require require, String serviceId, List<QueryAgentUnitInfoVo.Qual> quals, boolean matchAll, boolean matchAllQual) {
        List<AgentUnitInfoVo> matchUnitInfos = new ArrayList<>();

        if (quals != null) {

            for (QueryAgentUnitInfoVo.Qual qual : quals) {
                List<QueryAgentUnitInfoVo.MajorQualRequire> majorQualRequires = qual.getMajorQualRequires();
                if (majorQualRequires != null && !majorQualRequires.isEmpty()) {

                    //有专业要求
                    require.setNoRequire(false);

                    List<AgentUnitInfoVo> matchQualUnitInfos = new ArrayList<>();

                    //本轮状态
                    AgentUnitInfoVo.Require currentRequire = new AgentUnitInfoVo.Require(true);

                    if (matchAll) {
                        matchQualUnitInfos = getMatchAllUnitInfo(serviceId, matchQualUnitInfos, majorQualRequires, require, currentRequire);
                    } else {
                        matchQualUnitInfos = getMatchOneUnitInfoByOnlySelectedData(serviceId, matchQualUnitInfos, majorQualRequires, require, currentRequire);
                    }

                    //本轮提前结束，没有达标的
                    if (currentRequire.isFinish()) {
                        matchUnitInfos.clear();
                        return matchUnitInfos;
                    }

                    if (!checkMatchQual(matchUnitInfos, matchQualUnitInfos, currentRequire, matchAllQual)) {
                        matchUnitInfos.clear();
                        return matchUnitInfos;
                    }
                }
            }
        }

        return matchUnitInfos;
    }

    private List<AgentUnitInfoVo> getMatchUnitInfo(AgentUnitInfoVo.Require require, String serviceId, List<QueryAgentUnitInfoVo.Qual> quals, boolean matchAll, boolean matchAllQual) {
        List<AgentUnitInfoVo> matchUnitInfos = new ArrayList<>();

        if (quals != null) {

            for (QueryAgentUnitInfoVo.Qual qual : quals) {
                List<QueryAgentUnitInfoVo.MajorQualRequire> majorQualRequires = qual.getMajorQualRequires();
                if (majorQualRequires != null && !majorQualRequires.isEmpty()) {
                    List<AgentUnitInfoVo> matchQualUnitInfos = new ArrayList<>();

                    AgentUnitInfoVo.Require currentRequire = new AgentUnitInfoVo.Require(true);

                    if (matchAll) {
                        matchQualUnitInfos = getMatchAllUnitInfo(serviceId, matchQualUnitInfos, majorQualRequires, require, currentRequire);
                    } else {
                        matchQualUnitInfos = getMatchOneUnitInfo(serviceId, matchQualUnitInfos, majorQualRequires, require, currentRequire);
                    }

                    if (currentRequire.isFinish()) {
                        matchUnitInfos.clear();
                        return matchUnitInfos;
                    }

                    if (!checkMatchQual(matchUnitInfos, matchQualUnitInfos, currentRequire, matchAllQual)) {
                        matchUnitInfos.clear();
                        return matchUnitInfos;
                    }
                }
            }
        }

        return matchUnitInfos;
    }

    private boolean checkMatchQual(List<AgentUnitInfoVo> matchUnitInfos, List<AgentUnitInfoVo> currentMatchQualUnitInfos, AgentUnitInfoVo.Require currentRequire, boolean matchAllQual) {
        if (matchAllQual) {
            if (currentMatchQualUnitInfos.isEmpty()) {
                //本轮符合的机构为空且本轮的专业有要求，判定最终为没有符合的
                if (!currentRequire.isNoRequire()) {
                    return false;
                }
            } else {
                if (matchUnitInfos.isEmpty()) {
                    matchUnitInfos.addAll(currentMatchQualUnitInfos);
                } else {
                    removeAgentUnitInfoVo(matchUnitInfos, currentMatchQualUnitInfos);
                }
            }
        } else {
            addAgentUnitInfoVo(matchUnitInfos, currentMatchQualUnitInfos);
        }

        return true;
    }

    private List<AgentUnitInfoVo> getMatchAllUnitInfo(String serviceId, List<AgentUnitInfoVo> matchUnitInfos, List<QueryAgentUnitInfoVo.MajorQualRequire> majorQualRequires, AgentUnitInfoVo.Require require, AgentUnitInfoVo.Require currentRequire) {

        if (matchUnitInfos == null) {
            matchUnitInfos = new ArrayList<>();
        }

        if (currentRequire.isFinish()) {
            finishQuery(matchUnitInfos, require, currentRequire);
            return matchUnitInfos;
        }

        if (majorQualRequires != null) {

            for (QueryAgentUnitInfoVo.MajorQualRequire majorQualRequire : majorQualRequires) {

                if ("1".equals(majorQualRequire.getSelected())) {

                    List<AgentUnitInfoVo> agentUnitInfoVos = aeaImProjPurchaseMapper.listUnitInfoIdByMajorQualRequire(serviceId, majorQualRequire.getMajorId(), majorQualRequire.getQualLevelId());

                    if (agentUnitInfoVos.isEmpty()) {
                        finishQuery(matchUnitInfos, require, currentRequire);
                        return matchUnitInfos;
                    } else {
                        if (checkAgentUnitInfoVo(matchUnitInfos, agentUnitInfoVos, require, currentRequire)) {
                            return matchUnitInfos;
                        }
                    }

                    require.setNoRequire(false);
                    currentRequire.setNoRequire(false);

                    if (majorQualRequire.getChild() != null && !majorQualRequire.getChild().isEmpty()) {
                        matchUnitInfos = getMatchAllUnitInfo(serviceId, matchUnitInfos, majorQualRequire.getChild(), require, currentRequire);
                    }
                }

            }
        }

        return matchUnitInfos;
    }

    private List<AgentUnitInfoVo> getMatchOneUnitInfoByOnlySelectedData(String serviceId, List<AgentUnitInfoVo> matchUnitInfos, List<QueryAgentUnitInfoVo.MajorQualRequire> majorQualRequires, AgentUnitInfoVo.Require require, AgentUnitInfoVo.Require currentRequire) {

        if (matchUnitInfos == null) {
            matchUnitInfos = new ArrayList<>();
        }

        Map<String, Map<String, List<QueryAgentUnitInfoVo.MajorQualRequire>>> map = new HashMap<>();
        for (QueryAgentUnitInfoVo.MajorQualRequire majorQualRequire : majorQualRequires) {
            String qualLevelId = StringUtils.isNotBlank(majorQualRequire.getQualLevelId()) ? majorQualRequire.getQualLevelId() : "不分级";

            Map<String, List<QueryAgentUnitInfoVo.MajorQualRequire>> majorQualRequireMap = map.get(qualLevelId);
            if (majorQualRequireMap == null) {
                map.put(qualLevelId, new HashMap<>());
            }

            String parentMajorId = majorQualRequire.getParentMajorId();

            if (StringUtils.isBlank(parentMajorId)) {
                parentMajorId = "root";
            }

            if (majorQualRequireMap.containsKey(parentMajorId)) {
                majorQualRequireMap.get(parentMajorId).add(majorQualRequire);
            } else {
                List<QueryAgentUnitInfoVo.MajorQualRequire> list = new ArrayList<>();
                list.add(majorQualRequire);
                majorQualRequireMap.put(parentMajorId, list);
            }
        }

        for (String qualLevelId : map.keySet()) {
            Map<String, List<QueryAgentUnitInfoVo.MajorQualRequire>> majorQualRequireMap = map.get(qualLevelId);

            if (majorQualRequireMap != null) {
                for (String parentMajorId : majorQualRequireMap.keySet()) {

                    List<QueryAgentUnitInfoVo.MajorQualRequire> list = majorQualRequireMap.get(parentMajorId);

                    if (!list.isEmpty()) {

                        List<AgentUnitInfoVo> matchQualUnitInfos = new ArrayList<>();

                        matchQualUnitInfos = getMatchOneUnitInfo(serviceId, matchQualUnitInfos, list, require, currentRequire);

                        if (currentRequire.isFinish() || matchQualUnitInfos.isEmpty()) {
                            finishQuery(matchUnitInfos, require, currentRequire);
                            return matchUnitInfos;
                        } else {
                            if (checkAgentUnitInfoVo(matchUnitInfos, matchQualUnitInfos, require, currentRequire)) {
                                return matchUnitInfos;
                            }
                        }

                        require.setNoRequire(false);
                        currentRequire.setNoRequire(false);
                    }
                }
            }
        }

        return matchUnitInfos;
    }

    private List<AgentUnitInfoVo> getMatchOneUnitInfo(String serviceId, List<AgentUnitInfoVo> matchUnitInfos, List<QueryAgentUnitInfoVo.MajorQualRequire> majorQualRequires, AgentUnitInfoVo.Require require, AgentUnitInfoVo.Require currentRequire) {

        if (matchUnitInfos == null) {
            matchUnitInfos = new ArrayList<>();
        }

        if (currentRequire.isFinish()) {
            finishQuery(matchUnitInfos, require, currentRequire);
            return matchUnitInfos;
        }

        if (majorQualRequires != null) {

            Map<String, AgentUnitInfoVo> currentMap = new HashMap<>();

            boolean selected = false;

            for (QueryAgentUnitInfoVo.MajorQualRequire majorQualRequire : majorQualRequires) {

                if ("1".equals(majorQualRequire.getSelected())) {

                    selected = true;

                    List<AgentUnitInfoVo> agentUnitInfoVos = aeaImProjPurchaseMapper.listUnitInfoIdByMajorQualRequire(serviceId, majorQualRequire.getMajorId(), majorQualRequire.getQualLevelId());

                    if (!agentUnitInfoVos.isEmpty()) {
                        for (AgentUnitInfoVo agentUnitInfoVo : agentUnitInfoVos) {
                            currentMap.put(agentUnitInfoVo.getUnitInfoId(), agentUnitInfoVo);
                        }
                    }
                }

            }

            if (!currentMap.isEmpty()) {
                List<AgentUnitInfoVo> list = getAgentUnitInfoVoListFromMap(currentMap);
                if (checkAgentUnitInfoVo(matchUnitInfos, list, require, currentRequire)) {
                    return matchUnitInfos;
                }
            } else {
                finishQuery(matchUnitInfos, require, currentRequire);
                return matchUnitInfos;
            }

            if (selected) {
                require.setNoRequire(false);
                currentRequire.setNoRequire(false);
            }

            for (QueryAgentUnitInfoVo.MajorQualRequire majorQualRequire : majorQualRequires) {
                if (majorQualRequire.getChild() != null && !majorQualRequire.getChild().isEmpty()) {
                    matchUnitInfos = getMatchOneUnitInfo(serviceId, matchUnitInfos, majorQualRequire.getChild(), require, currentRequire);
                }
            }

        }

        return matchUnitInfos;
    }

    private void removeAgentUnitInfoVo(List<AgentUnitInfoVo> target, List<AgentUnitInfoVo> from) {
        Iterator<AgentUnitInfoVo> iterator = target.iterator();
        while (iterator.hasNext()) {
            boolean remove = true;
            AgentUnitInfoVo current = iterator.next();
            for (AgentUnitInfoVo agentUnitInfoVo : from) {
                if (agentUnitInfoVo.getUnitInfoId().equals(current.getUnitInfoId())) {
                    remove = false;
                    break;
                }
            }

            if (remove) {
                iterator.remove();
            }
        }

    }

    private void addAgentUnitInfoVo(List<AgentUnitInfoVo> matchUnitInfos, List<AgentUnitInfoVo> currentMatchQualUnitInfos) {
        Iterator<AgentUnitInfoVo> iterator = matchUnitInfos.iterator();
        while (iterator.hasNext()) {
            AgentUnitInfoVo current = iterator.next();
            for (AgentUnitInfoVo agentUnitInfoVo : currentMatchQualUnitInfos) {
                if (agentUnitInfoVo.getUnitInfoId().equals(current.getUnitInfoId())) {
                    iterator.remove();
                    break;
                }
            }
        }
        matchUnitInfos.addAll(currentMatchQualUnitInfos);
    }

    private boolean checkAgentUnitInfoVo(List<AgentUnitInfoVo> matchUnitInfos, List<AgentUnitInfoVo> queryResultList, AgentUnitInfoVo.Require require, AgentUnitInfoVo.Require currentRequire) {
        if (matchUnitInfos.isEmpty()) {
            matchUnitInfos.addAll(queryResultList);
        } else {
            removeAgentUnitInfoVo(matchUnitInfos, queryResultList);
            if (matchUnitInfos.isEmpty()) {
                finishQuery(matchUnitInfos, require, currentRequire);
                return true;
            }
        }

        return false;
    }

    private void finishQuery(List<AgentUnitInfoVo> matchUnitInfos, AgentUnitInfoVo.Require require, AgentUnitInfoVo.Require currentRequire) {
        matchUnitInfos.clear();
        require.setNoRequire(false);
        currentRequire.setNoRequire(false);
        currentRequire.setFinish(true);
    }

    private List<AgentUnitInfoVo> getAgentUnitInfoVoListFromMap(Map<String, AgentUnitInfoVo> map) {
        List<AgentUnitInfoVo> list = new ArrayList<>();

        if (map != null && !map.isEmpty()) {
            for (String key : map.keySet()) {
                list.add(map.get(key));
            }
        }

        return list;
    }


}
