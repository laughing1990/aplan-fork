package com.augurit.aplanmis.supermarket.main.controller;

import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.supermarket.main.service.AgentUserCenterService;
import com.augurit.aplanmis.supermarket.main.service.OwnerUserCenterService;
import com.augurit.aplanmis.supermarket.main.service.ZjcsAeaProjInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/aea/supermarket/ownerUserCenter")
@RestController
public class OwnerUserCenterController {
    @Autowired
    private ZjcsAeaProjInfoService zjcsAeaProjInfoService;

    @Autowired
    private OwnerUserCenterService ownerUserCenterService;

    @Autowired
    private AgentUserCenterService agentUserCenterService;

    public static Logger logger = LoggerFactory.getLogger(OwnerUserCenterController.class);

    @GetMapping("/ownerUserIndex.do")
    public ModelAndView ownerUserIndex(ModelMap modelMap) {
        modelMap.put("currentPage", "ownerUserIndex");
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-index");
    }

    //查询（页面跳转）
    @RequestMapping("/scheduleInquire.do")
    public ModelAndView scheduleInquire(ModelMap modelMap) {
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        return new ModelAndView("ui-jsp/aplanmis/mall/pages/schedule-inquire");
    }

    @RequestMapping("/projinfo.do")
    public ModelAndView projinfo(ModelMap modelMap, HttpServletRequest request) throws Exception {
        String projInfoId = request.getParameter("projInfoId");
        String projType = request.getParameter("projType");
        String localCode1 = request.getParameter("localCode1");
        String projName = request.getParameter("projName");
        String projAddr = request.getParameter("projAddr");
        String applicant = request.getParameter("applicant");
        String unitApplicant = request.getParameter("unitApplicant");
        String idcard = request.getParameter("idcard");
        String idrepresentative = request.getParameter("idrepresentative");
        String idno = request.getParameter("idno");
        String idmobile = request.getParameter("idmobile");
        String xmYdmj = request.getParameter("xmYdmj");
        String buildAreaSum = request.getParameter("buildAreaSum");
        String investSum = request.getParameter("investSum");
        String scaleContent = request.getParameter("scaleContent");
        String unitInfoId = request.getParameter("unitInfoId");
        String handleUnitId = request.getParameter("handleUnitId");
        String linkmanName = request.getParameter("linkmanName");
        String linkmanMobilePhone = request.getParameter("linkmanMobilePhone");
        String linkmanCertNo = request.getParameter("linkmanCertNo");
        modelMap.put("linkmanName", linkmanName);
        modelMap.put("linkmanMobilePhone", linkmanMobilePhone);
        modelMap.put("linkmanCertNo", linkmanCertNo);
        modelMap.put("projInfoId", projInfoId);
        modelMap.put("projType", projType);
        modelMap.put("localCode", localCode1);
        modelMap.put("projName", projName);
        modelMap.put("projAddr", projAddr);
        modelMap.put("applicant", applicant);
        modelMap.put("unitApplicant", unitApplicant);
        modelMap.put("idcard", idcard);
        modelMap.put("idrepresentative", idrepresentative);
        modelMap.put("idno", idno);
        modelMap.put("idmobile", idmobile);
        modelMap.put("xmYdmj", xmYdmj);
        modelMap.put("buildAreaSum", buildAreaSum);
        modelMap.put("investSum", investSum);
        modelMap.put("scaleContent", scaleContent);
        modelMap.put("unitInfoId", unitInfoId);
        modelMap.put("handleUnitId", handleUnitId);
        modelMap.put("currentPage", "userCenterJsp");

        //================中介服务
        AeaItemBasic aeaItemBasic = new AeaItemBasic();
        List<AeaItemBasic> aeaItemBasicList = new ArrayList<AeaItemBasic>();
        aeaItemBasic.setItemNature("2");
        aeaItemBasicList = ownerUserCenterService.listAeaItemBasicAll(aeaItemBasic);
        modelMap.put("aeaItemBasicList", aeaItemBasicList);
        //===============中介机构
        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        List<AeaUnitInfo> aeaUnitInfoList = new ArrayList<AeaUnitInfo>();
        aeaUnitInfo.setUnitType("8");
        aeaUnitInfoList = ownerUserCenterService.listAeaUnitInfo(aeaUnitInfo);
        modelMap.put("aeaUnitInfoList", aeaUnitInfoList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-projInfo-purchase");
    }

    @RequestMapping("/getProjectInfo.do")
    public AeaProjInfo getProjectInfo(String projectCode, HttpServletRequest request) {
        AeaProjInfo aeaProjInfo = null;//zjcsAeaProjInfoService.getProjInfoByCodes(projectCode);
        return aeaProjInfo;
    }

    @RequestMapping("/getAeaItemBasiList.do")
    public List<AeaItemBasic> getAeaItemBasiList(String serviceTypeId, HttpServletRequest request) {
        List<AeaItemBasic> aeaItemBasic = new ArrayList<AeaItemBasic>();
        if (serviceTypeId != null || serviceTypeId != "") {
            aeaItemBasic = ownerUserCenterService.listAeaItemBasicAll(serviceTypeId);
        } else {
            aeaItemBasic = ownerUserCenterService.listAeaItemBasicAll("1");
        }
        return aeaItemBasic;
    }

    @RequestMapping("/getAeaImUnitServiceList.do")
    public List<AeaImUnitService> getAeaImUnitServiceList(String itemVerId, HttpServletRequest request) throws Exception {
        AeaImUnitService aeaImUnitService = new AeaImUnitService();
//        aeaImUnitService.setItemVerId(itemVerId);
        List<AeaImUnitService> aeaImUnitServiceList = agentUserCenterService.listAeaImUnitService(aeaImUnitService);
        return aeaImUnitServiceList;
    }

    //================================采购==========================================

    /**
     * 跳转到采购页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/addPurchase.do")
    public ModelAndView addPurchase(ModelMap modelMap) {
        modelMap.put("currentPage", "addPurchase");
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-addPurchase");
    }

    @RequestMapping("/getAeaImProjPurchaseList.do")
    public List<AeaImProjPurchase> getAeaImProjPurchaseList(AeaImProjPurchase aeaImProjPurchase) throws Exception {
        List<AeaImProjPurchase> list = ownerUserCenterService.listAeaImProjPurchase(aeaImProjPurchase);
        return null;
    }

    @RequestMapping("/saveAeaImProjPurchase.do")
    public ModelAndView saveAeaImProjPurchase(ModelMap modelMap, HttpServletRequest request) throws Exception {
        // String serviceTypeId = request.getParameter("serviceTypeId");
        String itemVerId = request.getParameter("itemVerId");//中介服务事项id
        String localCode = request.getParameter("localCode");//项目编码
        String projInfoId = request.getParameter("projInfoId");//项目id
        String contacts = request.getParameter("contacts");//联系人
        String moblie = request.getParameter("moblie");//联系电话
        String projName = request.getParameter("projName");//项目名称
        String projScale = request.getParameter("projScale");//项目规模
        String projScaleContent = request.getParameter("projScaleContent");//规模内容
        String financialSource = request.getParameter("financialSource");//资金来源
        String serviceTimeExplain = request.getParameter("serviceTimeExplain");//服务时限说明
        String serviceContent = request.getParameter("serviceContent");//服务内容
        String biddingType = request.getParameter("biddingType");//选取中介服务机构方式
        String basePrice = request.getParameter("basePrice");//最低价
        String highestPrice = request.getParameter("highestPrice");//最高价
        String bidNum = request.getParameter("bidNum");//竞价天数
        String unitInfoId = request.getParameter("unitInfoId");//中介机构ID
        String unitServiceId = request.getParameter("unitServiceId");//采购表id
        System.out.println(unitServiceId + "===========================================================+unitServiceId");

        //----------------------------------------项目信息表
        AeaProjInfo aeaProjInfo = zjcsAeaProjInfoService.getAeaProjInfoById(projInfoId);
        aeaProjInfo.setProjInfoId(UUID.randomUUID().toString());
        aeaProjInfo.setLocalCode(localCode);//新生成的 项目编码
        aeaProjInfo.setProjName(projName);
        aeaProjInfo.setFinancialSource(financialSource);//资金来源
        zjcsAeaProjInfoService.insertAeaProjInfo(aeaProjInfo);
        String newProjInfoID = aeaProjInfo.getProjInfoId();
        //------------------------------------- 采购表
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();


        aeaImProjPurchase.setProjPurchaseId(UUID.randomUUID().toString());//主键
        aeaImProjPurchase.setProjInfoId(newProjInfoID);//项目id
        aeaImProjPurchase.setPublishUnitInfoId("2de1dd98-1de1-4525-af4a-0ea8fcb5fa99");//业务单位id===================================
//        aeaImProjPurchase.setItemVerId(itemVerId);//中介服务事项版本id
        //aeaImProjPurchase.setServiceTypeId("0");//serviceTypeId=============
        aeaImProjPurchase.setCreater("安徽通济环保科技有限公司");//创建人=================================================
        aeaImProjPurchase.setContacts(contacts);//联系人
        aeaImProjPurchase.setMoblie(moblie);//联系电话
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        aeaImProjPurchase.setCreateTime(df.parse(df.format(new Date())));//创建时间
        // aeaImProjPurchase.setIsDelete("0"); //是否删除IS_DELETE
        //aeaImProjPurchase.setIsActive("1");//是否启用
        aeaImProjPurchase.setBiddingType(serviceContent);//竞价类型：1 随机中标，2 自主选择
        aeaImProjPurchase.setBasePrice(basePrice);//最低价格
        aeaImProjPurchase.setHighestPrice(highestPrice);//最高价格
        //aeaImProjPurchase.setAuditFlag();//审核状态：0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成,6 已过时
        //aeaImProjPurchase.setBidNum("10");//竞价天数===========================================
        aeaImProjPurchase.setBjType("天");//竞价时间单位=====================================
        aeaImProjPurchase.setBiddingType(biddingType);//竞价类型：1 竞价选取，2 随机选取 3.直接选择
        aeaImProjPurchase.setServiceTimeExplain(serviceTimeExplain);//服务时间说明
        aeaImProjPurchase.setServiceContent(serviceContent);//服务内容
        //aeaImProjPurchase.setProjScale(projScale);//项目规模
        //aeaImProjPurchase.setProjScaleContent(projScaleContent);//项目规模内容
        aeaImProjPurchase.setBidNum(bidNum);//竞价天数
        ownerUserCenterService.saveAeaImProjPurchase(aeaImProjPurchase);
        //----------------------------------------企业报价表
        if (biddingType.equals("1")) {//竞价方式选取
            return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-index");
        } else if (biddingType.equals("3")) {//机构竞价方式 直接选取中介机构
            AeaImUnitService aeaImUnitService = new AeaImUnitService();
            aeaImUnitService.setUnitServiceId(unitServiceId);
            //fixme
//            aeaImUnitService = agentUserCenterService.getAeaImUnitServiceForm(aeaImUnitService);
            AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
            aeaImUnitBidding.setUnitBiddingId(UUID.randomUUID().toString());//主键
            String newProjPurchaseId = aeaImProjPurchase.getProjPurchaseId();
            aeaImUnitBidding.setProjPurchaseId(newProjPurchaseId);//项目ID-->采购表ID
            aeaImUnitBidding.setUnitInfoId(aeaImUnitService.getUnitInfoId());//竞价机构id
            aeaImUnitBidding.setRealPrice(aeaImUnitService.getPrice()); //价格（万元）realPrice
            aeaImUnitBidding.setIsWonBid("1");//是否中标 1.已中标  0 未中标
            aeaImUnitBidding.setAuditFlag("");//审核状态：0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成,6 已过时
            aeaImUnitBidding.setCreater("安徽通济环保科技有限公司");//创建人=================================================
            aeaImUnitBidding.setCreateTime(df.parse(df.format(new Date())));//创建时间
            aeaImUnitBidding.setLinkmanInfoId(contacts);//企业联系人
            aeaImUnitBidding.setMemo("");//备注
            aeaImUnitBidding.setUnitServiceId(unitServiceId);//中介机构服务项目id

            agentUserCenterService.insertAeaImUnitBidding(aeaImUnitBidding);
            return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-index");
        } else {
            List<AeaUnitInfo> unitList = new ArrayList<>();
            unitList = ownerUserCenterService.listUnit(itemVerId);
            Random random = new Random();
            int n = random.nextInt(unitList.size());
            String unitName = unitList.get(n).getApplicant();
            String unitPrice = unitList.get(n).getPrice();
            modelMap.put("unitName", unitName);
            modelMap.put("unitPrice", unitPrice);
            return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-randomChoice");
        }


    }

    //===========================================================================
    @RequestMapping("/updataeaProjInfo.do")
    public void updataeaProjInfo(AeaProjInfo aeaProjInfo) throws Exception {
        zjcsAeaProjInfoService.updateAeaProjInfo(aeaProjInfo);
    }

    //我的项目（页面跳转）
    @RequestMapping("/myProject.do")
    public ModelAndView myProject(ModelMap modelMap) {
        modelMap.put("currentPage", "myProject");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        //   return new ModelAndView("aplanmis/mall/pages/schedule-inquire");
        //return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner-user-myProject");
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        List<AeaImProjPurchase> aeaImProjPurchaseList = ownerUserCenterService.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
        for (AeaImProjPurchase ap : aeaImProjPurchaseList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tiemFormat = sdf.format(ap.getCreateTime());
//            ap.setCreateTimeString(tiemFormat);
        }
        modelMap.put("aeaImProjPurchaseList", aeaImProjPurchaseList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProject");
    }

    //所有项目 跳转 详细页
    @RequestMapping("/myProjectInfo.do")
    public ModelAndView myProjectInfo(String projServiceId, ModelMap modelMap) throws Exception {

        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");

        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        aeaImProjPurchase.setProjPurchaseId(projServiceId);
        aeaImProjPurchase = ownerUserCenterService.aeaImProjPurchaseForMyProj(aeaImProjPurchase);
        modelMap.put("aeaImProjPurchaseFrom", aeaImProjPurchase);

        AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
        aeaImUnitBidding.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
        List<AeaImUnitBidding> listAeaImUnitBidding = agentUserCenterService.listAeaImUnitBidding(aeaImUnitBidding);

        modelMap.put("listAeaImUnitBidding", listAeaImUnitBidding);

        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProjectInfo");
    }

    //所有项目 跳转 选中 中标项目
    @RequestMapping("/myProjectWinningBid.do")
    public ModelAndView myProjectWinningBid(String unitBiddingId, String projServiceId, ModelMap modelMap) throws Exception {
        AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
        aeaImUnitBidding.setUnitBiddingId(unitBiddingId);
        aeaImUnitBidding.setIsWonBid("1");//中标
        agentUserCenterService.updateAeaImUnitBidding(aeaImUnitBidding);

        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        aeaImProjPurchase.setProjPurchaseId(projServiceId);
        aeaImProjPurchase = ownerUserCenterService.aeaImProjPurchaseForMyProj(aeaImProjPurchase);
        modelMap.put("aeaImProjPurchaseFrom", aeaImProjPurchase);

        AeaImUnitBidding aeaImUnitBidding2 = new AeaImUnitBidding();
        aeaImUnitBidding.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
        List<AeaImUnitBidding> listAeaImUnitBidding = agentUserCenterService.listAeaImUnitBidding(aeaImUnitBidding2);

        modelMap.put("listAeaImUnitBidding", listAeaImUnitBidding);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProjectInfo");
    }

    //待处理（页面跳转）
    @RequestMapping("/myProjectPendingDisposal.do")
    public ModelAndView myProjectPendingDisposal(ModelMap modelMap) {
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        //   return new ModelAndView("aplanmis/mall/pages/schedule-inquire");
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        List<AeaImProjPurchase> aeaImProjPurchaseList = ownerUserCenterService.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
        for (AeaImProjPurchase ap : aeaImProjPurchaseList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tiemFormat = sdf.format(ap.getCreateTime());
//            ap.setCreateTimeString(tiemFormat);
        }
        modelMap.put("aeaImProjPurchaseList", aeaImProjPurchaseList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProject-dcl");
    }

    //待发布（页面跳转）
    @RequestMapping("/myProjectToReleased.do")
    public ModelAndView myProjectToReleased(ModelMap modelMap) {
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        //   return new ModelAndView("aplanmis/mall/pages/schedule-inquire");
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        List<AeaImProjPurchase> aeaImProjPurchaseList = ownerUserCenterService.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
        for (AeaImProjPurchase ap : aeaImProjPurchaseList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tiemFormat = sdf.format(ap.getCreateTime());
//            ap.setCreateTimeString(tiemFormat);
        }
        modelMap.put("aeaImProjPurchaseList", aeaImProjPurchaseList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProject-dfb");
    }

    //已回退（页面跳转）
    @RequestMapping("/myProjectRegression.do")
    public ModelAndView myProjectRegression(ModelMap modelMap) {
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        //   return new ModelAndView("aplanmis/mall/pages/schedule-inquire");
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        List<AeaImProjPurchase> aeaImProjPurchaseList = ownerUserCenterService.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
        for (AeaImProjPurchase ap : aeaImProjPurchaseList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tiemFormat = sdf.format(ap.getCreateTime());
//            ap.setCreateTimeString(tiemFormat);
        }
        modelMap.put("aeaImProjPurchaseList", aeaImProjPurchaseList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProject-yht");
    }

    //已发布（页面跳转）
    @RequestMapping("/myProjectHasBeenReleased.do")
    public ModelAndView myProjectHasBeenReleased(ModelMap modelMap) {
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        //   return new ModelAndView("aplanmis/mall/pages/schedule-inquire");
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        List<AeaImProjPurchase> aeaImProjPurchaseList = ownerUserCenterService.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
        for (AeaImProjPurchase ap : aeaImProjPurchaseList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tiemFormat = sdf.format(ap.getCreateTime());
//            ap.setCreateTimeString(tiemFormat);
        }
        modelMap.put("aeaImProjPurchaseList", aeaImProjPurchaseList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProject-yfb");
    }

    //已中选（页面跳转）
    @RequestMapping("/myProjectHaveWonTheBid.do")
    public ModelAndView myProjectHaveWonTheBid(ModelMap modelMap) {
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        //   return new ModelAndView("aplanmis/mall/pages/schedule-inquire");
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        List<AeaImProjPurchase> aeaImProjPurchaseList = ownerUserCenterService.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
        for (AeaImProjPurchase ap : aeaImProjPurchaseList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tiemFormat = sdf.format(ap.getCreateTime());
//            ap.setCreateTimeString(tiemFormat);
        }
        modelMap.put("aeaImProjPurchaseList", aeaImProjPurchaseList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProject-yxz");
    }

    //无效项目（页面跳转）
    @RequestMapping("/myProjectInvalid.do")
    public ModelAndView myProjectInvalid(ModelMap modelMap) {
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        //   return new ModelAndView("aplanmis/mall/pages/schedule-inquire");
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        List<AeaImProjPurchase> aeaImProjPurchaseList = ownerUserCenterService.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
        for (AeaImProjPurchase ap : aeaImProjPurchaseList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tiemFormat = sdf.format(ap.getCreateTime());
//            ap.setCreateTimeString(tiemFormat);
        }
        modelMap.put("aeaImProjPurchaseList", aeaImProjPurchaseList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProject-wx");
    }

    //服务中项目（页面跳转）
    @RequestMapping("/myProjectInService.do")
    public ModelAndView myProjectInService(ModelMap modelMap) {
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        //   return new ModelAndView("aplanmis/mall/pages/schedule-inquire");
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        List<AeaImProjPurchase> aeaImProjPurchaseList = ownerUserCenterService.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
        for (AeaImProjPurchase ap : aeaImProjPurchaseList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tiemFormat = sdf.format(ap.getCreateTime());
//            ap.setCreateTimeString(tiemFormat);
        }
        modelMap.put("aeaImProjPurchaseList", aeaImProjPurchaseList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProject-fwz");
    }

    //已完成项目（页面跳转）
    @RequestMapping("/myProjectComplete.do")
    public ModelAndView myProjectComplete(ModelMap modelMap) {
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        //   return new ModelAndView("aplanmis/mall/pages/schedule-inquire");
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        List<AeaImProjPurchase> aeaImProjPurchaseList = ownerUserCenterService.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
        for (AeaImProjPurchase ap : aeaImProjPurchaseList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tiemFormat = sdf.format(ap.getCreateTime());
//            ap.setCreateTimeString(tiemFormat);
        }
        modelMap.put("aeaImProjPurchaseList", aeaImProjPurchaseList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-user-myProject-yfb");
    }

   /* @RequestMapping("/myProjectByPage.do")
    public PageInfo<AeaProjInfo> myProjectByPage(String localCode, String projName, Integer pageNum, HttpServletRequest request, Page page) {
        try {
            page.setPageNum(pageNum);
            page.setPageSize(10);
            // 当前登录企业用户id
            String unitInfoId = "e8f88812-43b0-49ab-9caf-048c1888aec0";
            PageInfo<AeaProjInfo> list = zjcsAeaProjInfoService.myProjectByPage(localCode, projName, unitInfoId, page);
            return list;
        } catch (Exception e) {
            return null;
        }
    }*/

    //---------------------------------------------添加委托人

    /**
     * 跳转到采购页面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/clienteleManage.do")
    public ModelAndView clienteleManage(ModelMap modelMap, HttpServletRequest request) {
        try {
            String unitId = request.getSession().getAttribute("SESSION_UNIT_KEY").toString();
            AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
            modelMap.put("currentPage", "addClientele");
            modelMap.put("unitId", unitId);
            return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-clienteleManage");
        } catch (Exception e) {
            return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/login");
        }

    }

    @GetMapping("/addClientele.do")
    public ModelAndView addClientele(ModelMap modelMap) {
        modelMap.put("currentPage", "addClientele");
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/owner/owner-addClientele");
    }
}
