package com.augurit.aplanmis.front.form.controller;


import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicCodeType;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.aplanmis.common.domain.AeaExProjCertLand;
import com.augurit.aplanmis.common.domain.AeaExProjCertProject;
import com.augurit.aplanmis.common.domain.AeaExProjContract;
import com.augurit.aplanmis.common.domain.AeaExProjSite;
import com.augurit.aplanmis.common.service.dic.BscDicCodeItemService;
import com.augurit.aplanmis.common.service.form.AeaExProjCertLandService;
import com.augurit.aplanmis.common.service.form.AeaExProjCertProjectService;
import com.augurit.aplanmis.common.service.form.AeaExProjSiteService;
import com.augurit.aplanmis.common.vo.AeaCertiVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
* 两证一书 -Controller 页面控制转发类
*/
@RestController
@RequestMapping("/rest/form/tceop")
public class AeaExProjCertLandController {

private static Logger logger = LoggerFactory.getLogger(AeaExProjCertLandController.class);

    @Autowired
    private AeaExProjCertLandService aeaExProjCertLandService;

    @Autowired
    private AeaExProjCertProjectService aeaExProjCertProjectService;

    @Autowired
    private AeaExProjSiteService aeaExProjSiteService;

    @Autowired
    private BscDicCodeItemService bscDicCodeItemService;

    @Autowired
    private BscDicCodeService bscDicCodeService;


    @RequestMapping("/index.html")
    public ModelAndView indexAeaExProjCertLand(String projInfoId){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("projInfoId", projInfoId);
        modelAndView.setViewName("form/cardBookForm");
        return modelAndView;
    }

//数据回显
    @RequestMapping("/getTceop.do")
    public AeaCertiVo getAeaExProjCertLand(String projInfoId) throws Exception {
        AeaCertiVo aeaCertiVo = new AeaCertiVo();
        if (projInfoId != null){
            logger.debug("根据project ID获取vo对象，projInfoId：{}", projInfoId);

            AeaExProjCertLand landQuery = new AeaExProjCertLand();
            landQuery.setProjInfoId(projInfoId);
            List<AeaExProjCertLand> aeaExProjCertLands = aeaExProjCertLandService.listAeaExProjCertLand(landQuery);
            if (aeaExProjCertLands.size()>0) {
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
            if (aeaExProjCertProjects.size()>0){

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
            if (aeaExProjSites.size()>0){
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

            return aeaCertiVo;
        }
        else {
            logger.debug("构建新的aeaCertiVo对象");
            return aeaCertiVo;
        }
    }
/*    @RequestMapping("/listAeaExProjCertLand.do")
    public PageInfo<AeaExProjCertLand> listAeaExProjCertLand(  AeaExProjCertLand aeaExProjCertLand, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaExProjCertLand);
        return aeaExProjCertLandService.listAeaExProjCertLand(aeaExProjCertLand,page);
    }*/
//用地面积单位（数据字典）
    @RequestMapping("/landAreaType")
    public List<BscDicCodeItem> getLandAreaType(){
         String landAreaType = "Land_Area_Type";
        BscDicCodeType typeByTypeCode = bscDicCodeService.getTypeByTypeCode(landAreaType, SecurityContext.getCurrentOrgId());
        if (typeByTypeCode==null){
            return null;
        }
        else {
            List<BscDicCodeItem> areaType = bscDicCodeService.getItemsByTypeCode(typeByTypeCode.getTypeCode());
            return areaType;
        }
    }

    //用地性质
    @RequestMapping("/fieldType")
    public List<BscDicCodeItem> getXM_FIELD_TYPE(){
        String landAreaType = "XM_FIELD_TYPE";
        BscDicCodeType typeByTypeCode = bscDicCodeService.getTypeByTypeCode(landAreaType, SecurityContext.getCurrentOrgId());
        List<BscDicCodeItem> fieldType = bscDicCodeService.getItemsByTypeCode(typeByTypeCode.getTypeCode());
        return fieldType;
    }


/*
    @RequestMapping("/updateTceop.do")
        public ResultForm updateAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaExProjCertLand);
        aeaExProjCertLandService.updateAeaExProjCertLand(aeaExProjCertLand);
        return new ResultForm(true);
    }
*/


    /**
    * 保存或编辑建设项目用地规划许可证
    * @param aeaCertiVo 建设项目用地规划许可证
    */
    @RequestMapping("/saveTceop.do")
    public ResultForm saveAeaExProjCertLand(AeaCertiVo aeaCertiVo) throws Exception {


        //建设项目用地规划许可证
        AeaExProjCertLand aeaExProjCertLand = new AeaExProjCertLand();
        if(aeaCertiVo.getCertLandId()!=null&&!"".equals(aeaCertiVo.getCertLandId())){
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
        }else{
        if(aeaCertiVo.getCertLandId()==null||"".equals(aeaCertiVo.getCertLandId()))
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
        }

        //建设工程规划许可证
        AeaExProjCertProject aeaExProjCertProject = new AeaExProjCertProject();
        if(aeaCertiVo.getCertProjectId()!=null&&!"".equals(aeaCertiVo.getCertProjectId())){
            aeaExProjCertProject.setCertProjectId(aeaCertiVo.getCertProjectId());
            aeaExProjCertProject.setProjInfoId(aeaCertiVo.getProjInfoId());
            aeaExProjCertProject.setCertProjectCode(aeaCertiVo.getCertProjectCode());
            aeaExProjCertProject.setPublishOrgCode(aeaCertiVo.getPublishOrgCodeProject());
            aeaExProjCertProject.setPublishOrgName(aeaCertiVo.getPublishOrgNameProject());
            aeaExProjCertProject.setPublishTime(aeaCertiVo.getPublishTimeProject());
            aeaExProjCertProjectService.updateAeaExProjCertProject(aeaExProjCertProject);
        }else{
            if(aeaCertiVo.getCertProjectId()==null||"".equals(aeaCertiVo.getCertProjectId()))

                aeaExProjCertProject.setCertProjectId(UUID.randomUUID().toString());
                aeaExProjCertProject.setProjInfoId(aeaCertiVo.getProjInfoId());
                aeaExProjCertProject.setCertProjectCode(aeaCertiVo.getCertProjectCode());
                aeaExProjCertProject.setPublishOrgCode(aeaCertiVo.getPublishOrgCodeProject());
                aeaExProjCertProject.setPublishOrgName(aeaCertiVo.getPublishOrgNameProject());
                aeaExProjCertProject.setPublishTime(aeaCertiVo.getPublishTimeProject());
            aeaExProjCertProjectService.saveAeaExProjCertProject(aeaExProjCertProject);
        }

        //建设项目选址意见书
        AeaExProjSite aeaExProjSite = new AeaExProjSite();
        if(aeaCertiVo.getSiteId()!=null&&!"".equals(aeaCertiVo.getSiteId())){
            aeaExProjSite.setSiteId(aeaCertiVo.getSiteId());
            aeaExProjSite.setProjInfoId(aeaCertiVo.getProjInfoId());
            aeaExProjSite.setSiteCode(aeaCertiVo.getSiteCode());
            aeaExProjSite.setLandAreaValue(aeaCertiVo.getLandAreaValue());
            aeaExProjSite.setLandAreaUnit(aeaCertiVo.getLandAreaUnit());
            aeaExProjSite.setConstructionSize(aeaCertiVo.getConstructionSize());
            aeaExProjSite.setGovOrgCode(aeaCertiVo.getGovOrgNameSite());
            aeaExProjSite.setGovOrgName(aeaCertiVo.getGovOrgNameSite());
            aeaExProjSite.setPublishTime(aeaCertiVo.getPublishTimeSite());
            aeaExProjSiteService.updateAeaExProjSite(aeaExProjSite);
        }else{
            if(aeaCertiVo.getSiteId()==null||"".equals(aeaCertiVo.getSiteId()))
                aeaExProjSite.setSiteId(UUID.randomUUID().toString());
                aeaExProjSite.setProjInfoId(aeaCertiVo.getProjInfoId());
                aeaExProjSite.setSiteCode(aeaCertiVo.getSiteCode());
                aeaExProjSite.setLandAreaValue(aeaCertiVo.getLandAreaValue());
                aeaExProjSite.setLandAreaUnit(aeaCertiVo.getLandAreaUnit());
                aeaExProjSite.setConstructionSize(aeaCertiVo.getConstructionSize());
                aeaExProjSite.setGovOrgCode(aeaCertiVo.getGovOrgNameSite());
                aeaExProjSite.setGovOrgName(aeaCertiVo.getGovOrgNameSite());
                aeaExProjSite.setPublishTime(aeaCertiVo.getPublishTimeSite());
            aeaExProjSiteService.saveAeaExProjSite(aeaExProjSite);
        }

        return  new ContentResultForm<AeaCertiVo>(true,aeaCertiVo);
    }

    @RequestMapping("/deleteAeaExProjCertLandById.do")
    public ResultForm deleteAeaExProjCertLandById(String id) throws Exception{
        logger.debug("删除建设项目用地规划许可证Form对象，对象id为：{}", id);
        if(id!=null)
            aeaExProjCertLandService.deleteAeaExProjCertLandById(id);
        return new ResultForm(true);
    }

}
