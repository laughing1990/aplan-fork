package com.augurit.aplanmis.common.service.form.impl;

import com.augurit.agcloud.bpm.admin.sto.service.impl.AbstractFormDataOptManager;
import com.augurit.agcloud.bpm.common.constant.EDataOpt;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.ActStoForminst;
import com.augurit.agcloud.bpm.common.domain.vo.FormDataOptResult;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaExProjCertLand;
import com.augurit.aplanmis.common.domain.AeaExProjCertProject;
import com.augurit.aplanmis.common.domain.AeaExProjSite;
import com.augurit.aplanmis.common.service.form.AeaExProjCertLandService;
import com.augurit.aplanmis.common.service.form.AeaExProjCertProjectService;
import com.augurit.aplanmis.common.service.form.AeaExProjSiteService;
import com.augurit.aplanmis.common.vo.AeaCertiVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AeaExCertiService extends AbstractFormDataOptManager {
    @Autowired
    private AeaExProjCertLandService aeaExProjCertLandService;

    @Autowired
    private AeaExProjCertProjectService aeaExProjCertProjectService;

    @Autowired
    private AeaExProjSiteService aeaExProjSiteService;

    public AeaCertiVo  index ( String projInfoId ){
        AeaCertiVo aeaCertiVo = new AeaCertiVo();


        try {
            AeaExProjCertLand landQuery = new AeaExProjCertLand();
            landQuery.setProjInfoId(projInfoId);
            List<AeaExProjCertLand> aeaExProjCertLands = aeaExProjCertLandService.listAeaExProjCertLand(landQuery);
            if (aeaExProjCertLands.size() > 0) {
                AeaExProjCertLand aeaExProjCertLand = aeaExProjCertLands.get(0);
                aeaCertiVo.setCertLandId(aeaExProjCertLand.getCertLandId());
                aeaCertiVo.setCertLandCode(aeaExProjCertLand.getCertLandCode());
                aeaCertiVo.setLandNature(aeaExProjCertLand.getLandNature());
                aeaCertiVo.setLandAreaValue(aeaExProjCertLand.getLandAreaValue());
                aeaCertiVo.setLandAreaUnit(aeaExProjCertLand.getLandAreaUnit());
                aeaCertiVo.setGovOrgCodeLand(aeaExProjCertLand.getGovOrgCode());
                aeaCertiVo.setGovOrgNameLand(aeaExProjCertLand.getGovOrgName());
                aeaCertiVo.setPublishTimeLand(aeaExProjCertLand.getPublishTime());
            }
            AeaExProjCertProject projectQuery = new AeaExProjCertProject();
            projectQuery.setProjInfoId(projInfoId);
            List<AeaExProjCertProject> aeaExProjCertProjects = aeaExProjCertProjectService.listAeaExProjCertProject(projectQuery);
            if (aeaExProjCertProjects.size() > 0) {

                AeaExProjCertProject aeaExProjCertProject = aeaExProjCertProjects.get(0);
                aeaCertiVo.setCertProjectId(aeaExProjCertProject.getCertProjectId());
                aeaCertiVo.setCertProjectCode(aeaExProjCertProject.getCertProjectCode());
                aeaCertiVo.setPublishOrgCodeProject(aeaExProjCertProject.getPublishOrgCode());
                aeaCertiVo.setPublishOrgNameProject(aeaExProjCertProject.getPublishOrgName());
                aeaCertiVo.setPublishTimeProject(aeaExProjCertProject.getPublishTime());
            }

            //建设项目选址意见书
            AeaExProjSite siteQuery = new AeaExProjSite();
            siteQuery.setProjInfoId(projInfoId);
            List<AeaExProjSite> aeaExProjSites = aeaExProjSiteService.listAeaExProjSite(siteQuery);
            if (aeaExProjSites.size() > 0) {
                AeaExProjSite aeaExProjSite = aeaExProjSites.get(0);
                aeaCertiVo.setSiteCode(aeaExProjSite.getSiteCode());
                aeaCertiVo.setSiteId(aeaExProjSite.getSiteId());
                aeaCertiVo.setLandAreaValueSite(aeaExProjSite.getLandAreaValue());
                aeaCertiVo.setLandAreaUnitSite(aeaExProjSite.getLandAreaUnit());
                aeaCertiVo.setConstructionSize(aeaExProjSite.getConstructionSize());
                aeaCertiVo.setGovOrgCodeSite(aeaExProjSite.getGovOrgCode());
                aeaCertiVo.setGovOrgNameSite(aeaExProjSite.getGovOrgName());
                aeaCertiVo.setPublishTimeSite(aeaExProjSite.getPublishTime());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return aeaCertiVo;
    }


    public void save(AeaCertiVo aeaCertiVo) {

        try {
            if (StringUtils.isBlank(aeaCertiVo.getFormId())) throw new Exception("缺少formId");
            AeaExProjCertLand aeaExProjCertLand = new AeaExProjCertLand();
            if (aeaCertiVo.getCertLandId() != null && !"".equals(aeaCertiVo.getCertLandId())) {
                aeaExProjCertLand.setCertLandId(aeaCertiVo.getCertLandId());
                aeaExProjCertLand.setProjInfoId(aeaCertiVo.getProjInfoId());
                aeaExProjCertLand.setCertLandCode(aeaCertiVo.getCertLandCode());
                aeaExProjCertLand.setLandNature(aeaCertiVo.getLandNature());
                aeaExProjCertLand.setLandAreaValue(aeaCertiVo.getLandAreaValue());
                aeaExProjCertLand.setLandAreaUnit(aeaCertiVo.getLandAreaUnit());
                aeaExProjCertLand.setGovOrgCode(aeaCertiVo.getGovOrgCodeLand());
                aeaExProjCertLand.setGovOrgName(aeaCertiVo.getGovOrgNameLand());
                aeaExProjCertLand.setPublishTime(aeaCertiVo.getPublishTimeLand());
                aeaExProjCertLandService.updateAeaExProjCertLand(aeaExProjCertLand);
            } else {
                if (aeaCertiVo.getCertLandId() == null || "".equals(aeaCertiVo.getCertLandId())) {
                    if(aeaCertiVo.getCertLandCode() != null&&!"".equals(aeaCertiVo.getCertLandCode())){
                        aeaExProjCertLand.setCertLandId(UUID.randomUUID().toString());
                        aeaExProjCertLand.setProjInfoId(aeaCertiVo.getProjInfoId());
                        aeaExProjCertLand.setCertLandCode(aeaCertiVo.getCertLandCode());
                        aeaExProjCertLand.setLandNature(aeaCertiVo.getLandNature());
                        aeaExProjCertLand.setLandAreaValue(aeaCertiVo.getLandAreaValue());
                        aeaExProjCertLand.setLandAreaUnit(aeaCertiVo.getLandAreaUnit());
                        aeaExProjCertLand.setGovOrgCode(aeaCertiVo.getGovOrgCodeLand());
                        aeaExProjCertLand.setGovOrgName(aeaCertiVo.getGovOrgNameLand());
                        aeaExProjCertLand.setPublishTime(aeaCertiVo.getPublishTimeLand());
                        aeaExProjCertLandService.saveAeaExProjCertLand(aeaExProjCertLand);
                        this.formSave(aeaCertiVo.getFormId(), aeaExProjCertLand.getCertLandId(), EDataOpt.INSERT.getOpareteType(), null);
                    }
                }
            }

            //建设工程规划许可证
            AeaExProjCertProject aeaExProjCertProject = new AeaExProjCertProject();
            if (aeaCertiVo.getCertProjectId() != null && !"".equals(aeaCertiVo.getCertProjectId())) {
                aeaExProjCertProject.setCertProjectId(aeaCertiVo.getCertProjectId());
                aeaExProjCertProject.setProjInfoId(aeaCertiVo.getProjInfoId());
                aeaExProjCertProject.setCertProjectCode(aeaCertiVo.getCertProjectCode());
                aeaExProjCertProject.setPublishOrgCode(aeaCertiVo.getPublishOrgCodeProject());
                aeaExProjCertProject.setPublishOrgName(aeaCertiVo.getPublishOrgNameProject());
                aeaExProjCertProject.setPublishTime(aeaCertiVo.getPublishTimeProject());
                aeaExProjCertProjectService.updateAeaExProjCertProject(aeaExProjCertProject);
            } else {
                if (aeaCertiVo.getCertProjectId() == null || "".equals(aeaCertiVo.getCertProjectId())){
                    if (aeaCertiVo.getCertProjectCode() != null && !"".equals(aeaCertiVo.getCertProjectCode())) {
                        aeaExProjCertProject.setCertProjectId(UUID.randomUUID().toString());
                        aeaExProjCertProject.setProjInfoId(aeaCertiVo.getProjInfoId());
                        aeaExProjCertProject.setCertProjectCode(aeaCertiVo.getCertProjectCode());
                        aeaExProjCertProject.setPublishOrgCode(aeaCertiVo.getPublishOrgCodeProject());
                        aeaExProjCertProject.setPublishOrgName(aeaCertiVo.getPublishOrgNameProject());
                        aeaExProjCertProject.setPublishTime(aeaCertiVo.getPublishTimeProject());
                        aeaExProjCertProjectService.saveAeaExProjCertProject(aeaExProjCertProject);
                        this.formSave(aeaCertiVo.getFormId(), aeaExProjCertProject.getCertProjectId(), EDataOpt.INSERT.getOpareteType(), null);
                    }
                }
            }

            //建设项目选址意见书
            AeaExProjSite aeaExProjSite = new AeaExProjSite();
            if (aeaCertiVo.getSiteId() != null && !"".equals(aeaCertiVo.getSiteId())) {
                aeaExProjSite.setSiteId(aeaCertiVo.getSiteId());
                aeaExProjSite.setProjInfoId(aeaCertiVo.getProjInfoId());
                aeaExProjSite.setSiteCode(aeaCertiVo.getSiteCode());
                aeaExProjSite.setLandAreaValue(aeaCertiVo.getLandAreaValueSite());
                aeaExProjSite.setLandAreaUnit(aeaCertiVo.getLandAreaUnit());
                aeaExProjSite.setConstructionSize(aeaCertiVo.getConstructionSize());
                aeaExProjSite.setGovOrgCode(aeaCertiVo.getGovOrgCodeSite());
                aeaExProjSite.setGovOrgName(aeaCertiVo.getGovOrgNameSite());
                aeaExProjSite.setPublishTime(aeaCertiVo.getPublishTimeSite());
                aeaExProjSiteService.updateAeaExProjSite(aeaExProjSite);
            } else {
                if (aeaCertiVo.getSiteId() == null || "".equals(aeaCertiVo.getSiteId())){
                    if(aeaCertiVo.getSiteCode() != null&&!"".equals(aeaCertiVo.getSiteCode())){
                        aeaExProjSite.setSiteId(UUID.randomUUID().toString());
                        aeaExProjSite.setProjInfoId(aeaCertiVo.getProjInfoId());
                        aeaExProjSite.setSiteCode(aeaCertiVo.getSiteCode());
                        aeaExProjSite.setLandAreaValue(aeaCertiVo.getLandAreaValueSite());
                        aeaExProjSite.setLandAreaUnit(aeaCertiVo.getLandAreaUnit());
                        aeaExProjSite.setConstructionSize(aeaCertiVo.getConstructionSize());
                        aeaExProjSite.setGovOrgCode(aeaCertiVo.getGovOrgNameSite());
                        aeaExProjSite.setGovOrgName(aeaCertiVo.getGovOrgNameSite());
                        aeaExProjSite.setPublishTime(aeaCertiVo.getPublishTimeSite());
                        aeaExProjSiteService.saveAeaExProjSite(aeaExProjSite);

                        this.formSave(aeaCertiVo.getFormId(), aeaExProjSite.getSiteId(), EDataOpt.INSERT.getOpareteType(), null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public FormDataOptResult doformSave(String formId, String metaTableId, Integer opType, Object dataEntity) throws Exception {
        FormDataOptResult result = new FormDataOptResult();
        result.setSuccess(true);
        ActStoForminst actStoForminst = new ActStoForminst();
        actStoForminst.setFormId(formId);
        actStoForminst.setFormPrimaryKey(metaTableId);
        result.setActStoForminst(actStoForminst);
        return result;
    }

    @Override
    public FormDataOptResult doformDelete(ActStoForm formVo, Object dataEntity) throws Exception {
        return null;
    }
}
