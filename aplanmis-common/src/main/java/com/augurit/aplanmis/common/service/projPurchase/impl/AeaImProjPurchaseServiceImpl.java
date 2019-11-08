package com.augurit.aplanmis.common.service.projPurchase.impl;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaImPurchaseinst;
import com.augurit.aplanmis.common.domain.AeaImQualLevel;
import com.augurit.aplanmis.common.domain.AeaImServiceMajor;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.projPurchase.AeaImProjPurchaseService;
import com.augurit.aplanmis.common.utils.BusinessUtils;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.vo.AeaImQualVo;
import com.augurit.aplanmis.common.vo.AeaImServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private AeaImServiceMajorMapper aeaImServiceMajorMapper;

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
    public String uploadFiles(HttpServletRequest request) throws Exception {

        Boolean uploadFlag = false;
        String resultRecordId = "";
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        if (request instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;

            /***要求说明文件***/
            List<MultipartFile> requireExplainFiles = req.getFiles("requireExplainFile");
            if (requireExplainFiles != null && !requireExplainFiles.isEmpty()) {
                String recordId = UuidUtil.generateUuid();
                uploadFlag = FileUtils.uploadFile("AEA_IM_PROJ_PURCHASE", "REQUIRE_EXPLAIN_FILE", recordId, requireExplainFiles);
                if (uploadFlag) {
                    resultRecordId = recordId;
                }
            }
            /***批文文件***/
            List<MultipartFile> officialRemarkFiles = req.getFiles("officialRemarkFile");
            if (officialRemarkFiles != null && !officialRemarkFiles.isEmpty()) {
                String recordId = UuidUtil.generateUuid();
                ;
                uploadFlag = FileUtils.uploadFile("AEA_IM_PROJ_PURCHASE", "OFFICIAL_REMARK_FILE", recordId, officialRemarkFiles);
                if (uploadFlag) {
                    resultRecordId = recordId;
                }
            }
        }

        return resultRecordId;
    }

}
