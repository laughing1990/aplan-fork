package com.augurit.aplanmis.supermarket.main.controller;

import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaImUnitBidding;
import com.augurit.aplanmis.common.domain.AeaImUnitService;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.supermarket.main.service.AgentUserCenterService;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 中介机构控制器
 */
@RequestMapping("/aea/supermarket/agentUserCenter")
@RestController
public class AgentUserCenterController {

    public static Logger logger = LoggerFactory.getLogger(com.augurit.aplanmis.supermarket.main.controller.AgentUserCenterController.class);

    @Autowired
    private AgentUserCenterService agentUserCenterService;

    /**
     * 中介机构中心页
     * @param modelMap
     * @return
     */
    @GetMapping("/agentUserIndex.do")
    public ModelAndView agentUserIndex(ModelMap modelMap) throws Exception{
        return agentBiddingList(modelMap);
    }

    /**
     * 竞价室
     * @param modelMap
     * @return
     */
    @GetMapping("/enterBiddingRoom.do")
    public ModelAndView enterBiddingRoom(ModelMap modelMap) {
        modelMap.put("currentPage","enterBiddingRoom");
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/enter-bidding-room");
    }

    /**
     * 我的竞价
     * @param modelMap
     * @return
     */
    @GetMapping("/agentBiddingList.do")
    public ModelAndView agentBiddingList(ModelMap modelMap) throws Exception{
        modelMap.put("currentPage","agentBiddingList");
        AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
        PageInfo<AeaImUnitBidding> pageList = agentUserCenterService.listAeaUnitBidding(aeaImUnitBidding,new Page().setPageNum(1).setPageSize(20));
        modelMap.put("aeaUnitBiddingList",pageList.getList());
        modelMap.put("pageInfo",pageList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/agent/agent-bidding-list");
    }

    @GetMapping("/agentUserInfo.do")
    public ModelAndView agentUserInfo(ModelMap modelMap) {
        modelMap.put("currentPage","agentUserInfo");
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/agent/agent-user-info");
    }

    /**
     * 服务事项
     * @param modelMap
     * @return
     */
    @GetMapping("/agentItem.do")
    public ModelAndView agentItem(ModelMap modelMap) throws Exception{
        modelMap.put("currentPage","aeaImUnitServiceList");
        AeaImUnitService aeaImUnitService = new AeaImUnitService();
        //PageInfo<AeaImUnitService> pageList = agentUserCenterService.listAeaImUnitServicePage(aeaImUnitService,new Page().setPageNum(1).setPageSize(20));
        List<AeaImUnitService> list = agentUserCenterService.listAeaImUnitService(aeaImUnitService);

        //modelMap.put("aeaImUnitServiceList",pageList.getList());
        modelMap.put("listInfo",list);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/agent/agent-item");
    }
    //（页面跳转）
    @RequestMapping("/aeaImUnitServiceInfo.do")
    public ModelAndView aeaImUnitServiceInfo(ModelMap modelMap) throws Exception{
        modelMap.put("currentPage", "userCenterJsp");
        modelMap.put("currentMyProjectPage", "scheduleInquireJsp");
        AeaImService aeaImService = new AeaImService();
        List<AeaImService> pe =  agentUserCenterService.getAeaImServiceTypeList(aeaImService);
        modelMap.put("AeaImServiceTypeList", pe);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/agent/agent-item-aeaImUnitServiceInfo");
    }
    @GetMapping("/listAeaImUnitServicePage.do")
    public ModelAndView listAeaImUnitServicePage(ModelMap modelMap) throws Exception{
        modelMap.put("currentPage","aeaImUnitServiceList");
        AeaImUnitService aeaImUnitService = new AeaImUnitService();
        PageInfo<AeaImUnitService> pageList = agentUserCenterService.listAeaImUnitServicePage(aeaImUnitService,new Page().setPageNum(1).setPageSize(20));
        modelMap.put("aeaImUnitServiceList",pageList.getList());
        modelMap.put("pageInfo",pageList);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/agent/agent-bidding-list");
    }
    @GetMapping("/getAeaImUnitService.do")
    public ModelAndView getAeaImUnitService(ModelMap modelMap, String unitInfoId) throws Exception{
        AeaImUnitService aeaImUnitService = new AeaImUnitService();
        aeaImUnitService.setUnitInfoId(unitInfoId);
        modelMap.put("aeaImUnitService",agentUserCenterService.getAeaImUnitService(aeaImUnitService));
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/agent/agent-item-aeaImUnitServiceInfo");
    }
    @GetMapping("/listAeaImUnitService.do")
    public  List<AeaImUnitService> listAeaImUnitService(String unitInfoId) throws Exception{
        AeaImUnitService aeaImUnitService = new AeaImUnitService();
        aeaImUnitService.setUnitInfoId(unitInfoId);
        return agentUserCenterService.listAeaImUnitService(aeaImUnitService);
    }
    @RequestMapping("/updateAeaImUnitService.do")
    public void updateAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception {
        agentUserCenterService.updateAeaImUnitService(aeaImUnitService);
    }
    @RequestMapping("/insertAeaImUnitService.do")
    public ModelAndView insertAeaImUnitService(ModelMap modelMap,AeaImUnitService aeaImUnitService) throws Exception {
        aeaImUnitService.setUnitServiceId(UUID.randomUUID().toString());
        //aeaImUnitService.setServiceTypeId(aeaProjService.getServiceTypeId().replaceAll("\\,",""));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        aeaImUnitService.setCreateTime(df.parse(df.format(new Date())));//创建时间
        aeaImUnitService.setIsActive("1");//是否启用
        agentUserCenterService.insertAeaImUnitService(aeaImUnitService);

        modelMap.put("currentPage","aeaImUnitServiceList");
        AeaImUnitService ae = new AeaImUnitService();
        List<AeaImUnitService> list = agentUserCenterService.listAeaImUnitService(ae);
        modelMap.put("listInfo",list);
        return new ModelAndView("ui-jsp/aplanmis/supermarket/pages/agent/agent-item");
    }
    @RequestMapping("/deleteAeaImUnitService.do")
    public void deleteAeaImUnitService(String id) throws Exception {
        agentUserCenterService.deleteAeaImUnitService(id);
    }

    @RequestMapping("/getAeaItemBasiList.do")
    public List<AeaItemBasic> getAeaItemBasiList(String serviceTypeId, HttpServletRequest request) throws Exception {
        AeaItemBasic aeaItemBasic = new AeaItemBasic();
        aeaItemBasic.setItemNature("2");
        List<AeaItemBasic> aeaItemBasicList = agentUserCenterService.getListAeaItemBasic(aeaItemBasic);
        return aeaItemBasicList;
    }

}