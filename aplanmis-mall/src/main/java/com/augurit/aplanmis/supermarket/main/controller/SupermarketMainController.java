package com.augurit.aplanmis.supermarket.main.controller;


import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.service.CommonLoginService;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.login.jwx.AccessToken;
import com.augurit.aplanmis.mall.login.jwx.JwtHelper;
import com.augurit.aplanmis.mall.login.jwx.Jwx;
import com.augurit.aplanmis.supermarket.main.service.SupMainService;
import com.augurit.aplanmis.supermarket.main.service.login.LoginService;
import com.augurit.aplanmis.supermarket.main.vo.EditUnitVo;
import com.augurit.aplanmis.supermarket.utils.ContentRestResult;
import com.augurit.aplanmis.supermarket.utils.RestResult;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(description = "中介超市首页相关接口")
@RequestMapping("/supermarket/main")
@RestController
public class SupermarketMainController {

    @Autowired
    private SupMainService supMainService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private CommonLoginService commonLoginService;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    private JwtHelper jwtHelper;


    public static Logger logger = LoggerFactory.getLogger(com.augurit.aplanmis.supermarket.main.controller.SupermarketMainController.class);

    //==============================首页模块跳转  start ===================================

    @ApiOperation(value = "中介超市首页", notes = "中介超市首页")
    @GetMapping("/index.html")
    public ModelAndView mainIndex() throws Exception {
        return new ModelAndView("zjcs/index");
    }

    @ApiOperation(value = "中介超市首页头部页面", notes = "中介超市首页头部页面")
    @GetMapping("/header.html")
    public ModelAndView headerIndex() throws Exception {
        return new ModelAndView("zjcs/common/header");
    }

    @ApiOperation(value = "中介超市首页尾部页面", notes = "中介超市首页尾部页面")
    @GetMapping("/footer.html")
    public ModelAndView footerIndex() throws Exception {
        return new ModelAndView("zjcs/common/footer");
    }

    @ApiOperation(value = "采购需求公告列表页面", notes = "采购需求公告列表页面")
    @GetMapping("/procurementNotice/index.html")
    public ModelAndView procurementNoticeIndex() throws Exception {
        return new ModelAndView("zjcs/procurementNotice/index");
    }

    @ApiOperation(value = "采购需求公告详情页面", notes = "采购需求公告详情页面")
    @GetMapping("/procurementNotice/details.html")
    public ModelAndView procurementNoticeDetails() throws Exception {
        return new ModelAndView("zjcs/procurementNotice/details");
    }

    @ApiOperation(value = "中选公告列表页面", notes = "中选公告列表页面")
    @GetMapping("/beSelectionNotice/index.html")
    public ModelAndView beSelectionNoticeIndex() throws Exception {
        return new ModelAndView("zjcs/beSelectionNotice/index");
    }

    @ApiOperation(value = "中选公告详情页面", notes = "中选公告详情页面")
    @GetMapping("/beSelectionNotice/selectionNoticeDetail.html")
    public ModelAndView selectionNoticeDetail() throws Exception {
        return new ModelAndView("zjcs/beSelectionNotice/selectionNoticeDetail");
    }

    @ApiOperation(value = "合同公告详情页面", notes = "合同公告详情页面")
    @GetMapping("/beSelectionNotice/contractNoticeDetail.html")
    public ModelAndView contractNoticeDetail() throws Exception {
        return new ModelAndView("zjcs/beSelectionNotice/contractNoticeDetail");
    }

    @ApiOperation(value = "中介服务事项详情页面", notes = "中介服务事项详情页面")
    @GetMapping("/imServices.html")
    public ModelAndView index() throws Exception {
        return new ModelAndView("zjcs/serviceMatters/index");
    }

    @ApiOperation(value = "中介机构和入驻服务列表页面", notes = "中介机构和入驻服务列表页面")
    @GetMapping("/imUnits.html")
    public ModelAndView agentList(String type) throws Exception {
        ModelAndView modelAndView = new ModelAndView("zjcs/serviceOrgan/index");
        modelAndView.addObject("type", type);
        return modelAndView;
    }

    @ApiOperation(value = "中介超市指南页面", notes = "中介超市指南页面")
    @GetMapping("/guide.html")
    public ModelAndView guide() throws Exception {
        return new ModelAndView("zjcs/guide/market_guide");
    }

    @ApiOperation(value = "公告页面", notes = "公告页面")
    @GetMapping("/notice.html")
    public ModelAndView notice(String id) throws Exception {
        ModelAndView modelAndView = new ModelAndView("zjcs/notice/index.html");
        modelAndView.addObject("id", id);
        return modelAndView;
    }

    @ApiOperation(value = "中介机构详情页面", notes = "中介机构详情页面")
    @GetMapping("/serviceInfo.html")
    public ModelAndView serviceInfo() throws Exception {
        return new ModelAndView("zjcs/serviceOrgan/index2.html");
    }


    @ApiOperation(value = "中介服务详情页面", notes = "中介服务详情页面")
    @GetMapping("/agentServiceInfo.html")
    public ModelAndView serviceInfo(String serviceId, String serviceName) throws Exception {
        ModelAndView modelAndView = new ModelAndView("zjcs/serviceOrgan/index2_fromHome.html");
        return modelAndView;
    }

    @ApiOperation(value = "证照详情页面", notes = "证照详情页面")
    @GetMapping("/qualCertInfo.html")
    public ModelAndView qualCertInfo(String certinstId) throws Exception {
        ModelAndView modelAndView = new ModelAndView("zjcs/serviceOrgan/index3.html");
        modelAndView.addObject("certinstId", certinstId);
        return modelAndView;
    }

    @ApiOperation(value = "登录页面", notes = "登录页面")
    @RequestMapping("/login.html")
    public ModelAndView loginIndex() {
        return new ModelAndView("zjcs/login/login");
    }

    //==============================首页机构模块跳转  end ===================================


    //==============================中介机构模块跳转  start ===================================

    @ApiOperation(value = "中介机构企业中心页面", notes = "中介机构企业中心页面")
    @GetMapping("/agentCenter.html")
    public ModelAndView agentCenter() throws Exception {
        return new ModelAndView("zjcs/personCenter/agentCenter");
    }

    @ApiOperation(value = "中介机构委托人管理页面", notes = "中介机构委托人管理页面")
    @GetMapping("/addClientPeople.html")
    public ModelAndView addClientPeople() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/client/addClientPeople");
    }

    @ApiOperation(value = "中介机构综合数据展示页面", notes = "中介机构综合数据展示页面")
    @GetMapping("/allDataShow.html")
    public ModelAndView allDataShow() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/client/allDataShow");
    }

    @ApiOperation(value = "中介机构基本信息页面", notes = "中介机构基本信息页面")
    @GetMapping("/baseInfo.html")
    public ModelAndView baseInfo() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/client/baseInfo");
    }

    @ApiOperation(value = "从业人员管理页面", notes = "从业人员管理页面")
    @GetMapping("/jobPeopleManage.html")
    public ModelAndView jobPeopleManage() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/client/jobPeopleManage");
    }

    @ApiOperation(value = "中介机构可报名项目页面", notes = "中介机构可报名项目页面")
    @GetMapping("/projectCanSignup.html")
    public ModelAndView projectCanSignup() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/client/projectCanSignup");
    }

    @ApiOperation(value = "中介机构中选项目页面", notes = "中介机构中选项目页面")
    @GetMapping("/projectSelection.html")
    public ModelAndView projectSelection() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/client/projectSelection");
    }

    @ApiOperation(value = "中介机构服务中项目页面", notes = "中介机构服务中项目页面")
    @GetMapping("/projectService.html")
    public ModelAndView projectService() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/client/projectService");
    }

    @ApiOperation(value = "中介机构项目竞价页面", notes = "中介机构项目竞价页面")
    @GetMapping("/projectSignup.html")
    public ModelAndView projectSignup() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/client/projectSignup");
    }

    @ApiOperation(value = "中介服务管理页面", notes = "中介服务管理页面")
    @GetMapping("/serviceMatterManage.html")
    public ModelAndView serviceMatterManage() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/client/serviceMatterManage");
    }

    //==============================中介机构模块跳转  end ===================================


    //==============================业主模块跳转  start ===================================

    @ApiOperation(value = "业主企业中心页面", notes = "业主企业中心页面。")
    @GetMapping("/ownerCenter.html")
    public ModelAndView ownerCenter() throws Exception {
        return new ModelAndView("zjcs/personCenter/ownerCenter");
    }

    @ApiOperation(value = "所有项目页面", notes = "所有项目页面。")
    @GetMapping("/allProject.html")
    public ModelAndView allProject() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/allProject");
    }

    @ApiOperation(value = "待处理项目页面", notes = "待处理项目页面。")
    @GetMapping("/pendingProject.html")
    public ModelAndView pendingProject() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/pendingProject");
    }

    @ApiOperation(value = "待审核项目页面", notes = "待审核项目页面。")
    @GetMapping("/waitAuditProject.html")
    public ModelAndView waitAuditProject() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/waitAuditProject");
    }

    @ApiOperation(value = "待发布项目页面", notes = "待发布项目页面。")
    @GetMapping("/waitAnnounceProject.html")
    public ModelAndView waitAnnounceProject() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/waitAnnounceProject");
    }

    @ApiOperation(value = "已退回项目页面", notes = "已退回项目页面。")
    @GetMapping("/returnProject.html")
    public ModelAndView returnProject() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/returnProject");
    }

    @ApiOperation(value = "已发布项目页面", notes = "已发布项目页面。")
    @GetMapping("/alreadyAnnounceProject.html")
    public ModelAndView alreadyAnnounceProject() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/alreadyAnnounceProject");
    }

    @ApiOperation(value = "已中选项目页面", notes = "已中选项目页面。")
    @GetMapping("/alreadySelectionProject.html")
    public ModelAndView alreadySelectionProject() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/alreadySelectionProject");
    }

    @ApiOperation(value = "无效项目页面", notes = "无效项目页面。")
    @GetMapping("/invalidProject.html")
    public ModelAndView invalidProject() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/invalidProject");
    }

    @ApiOperation(value = "服务中项目页面", notes = "服务中项目页面。")
    @GetMapping("/inServiceProject.html")
    public ModelAndView inServiceProject() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/inServiceProject");
    }

    @ApiOperation(value = "已完成项目页面", notes = "已完成项目页面。")
    @GetMapping("/accomplishProject.html")
    public ModelAndView accomplishProject() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/accomplishProject");
    }

    @ApiOperation(value = "业主综合数据展示页面", notes = "业主综合数据展示。")
    @GetMapping("/ownerAllDataShow.html")
    public ModelAndView ownerAllDataShow() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/ownerAllDataShow");
    }

    @ApiOperation(value = "业主委托人管理页面", notes = "业主委托人管理页面。")
    @GetMapping("/ownerAddClientPeople.html")
    public ModelAndView ownerAddClientPeople() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/ownerAddClientPeople");
    }

    @ApiOperation(value = "发布采购需求项目页面", notes = "发布采购需求项目页面。")
    @GetMapping("/addNeedPaurse.html")
    public ModelAndView addNeedPaurse() throws Exception {
        return new ModelAndView("zjcs/personCenter/components/owner/addNeedPaurse.html");
    }

    //==============================业主模块跳转  end ===================================

    @GetMapping("/showPorjDetail.html")
    public ModelAndView showProjDetail(String projPurchaseId) throws Exception {
        ModelAndView mv = new ModelAndView("zjcs/personCenter/components/owner/waitDealProDetail");
        mv.addObject("projPurchase", projPurchaseId);
        return mv;
    }

    @ApiOperation(value = "获取首页展示数据", notes = "获取首页展示数据。")
    @ApiImplicitParams({})
    @RequestMapping(value = "/getIndexData", method = RequestMethod.GET)
    public ResultForm getIndexData() {
        try {
            return new ContentResultForm(true, supMainService.getIndexData());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

    @ApiOperation(value = "登录验证", notes = "登录验证。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "登录账号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isOwner", value = "是否业主登录", required = true, dataType = "String"),
            @ApiImplicitParam(name = "request", value = "获取session", required = false, dataType = "HttpServletRequest")
    })
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public ResultForm login(String userName, String password, String isOwner, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (StringUtils.isBlank(userName) || StringUtils.isBlank(password) || StringUtils.isBlank(isOwner)) {
                return new ResultForm(false, "缺少请求参数");
            }
            Gson gson = new Gson();
            ContentResultForm crf = commonLoginService.login(userName, password, isOwner, request);
            if (crf.isSuccess()) {
                LoginInfoVo loginInfoVo = (LoginInfoVo) crf.getContent();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("loginName", loginInfoVo.getUnitName());
                String accessToken = JwtHelper.createJWT(gson.toJson(map));
                AccessToken accessTokenEntity = new AccessToken();
                accessTokenEntity.setAccessToken(accessToken);
                accessTokenEntity.setExpiresIn(Jwx.expiration);
                accessTokenEntity.setTokenType(Jwx.CLAIM_TOKEN_TYPE);
                Cookie cookie = new Cookie(URLEncoder.encode(Jwx.CLAIM_KEY_NAME, "utf-8"),
                        URLEncoder.encode(accessToken, "utf-8"));
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                response.addCookie(cookie);
            }
            ;
            return crf;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @ApiOperation(value = "退出登录", notes = "退出登录状态")
    @RequestMapping(value = "/logout", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            jwtHelper.reMoveToken(response);
            return commonLoginService.logout(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @ApiOperation(value = "获取个人信息", notes = "获取个人信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "linkmanInfoId", value = "联系人信息ID", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/getPersonInfo.do", method = RequestMethod.POST)
    public ResultForm getPersonInfo(String linkmanInfoId) {
        try {
            if (StringUtils.isBlank(linkmanInfoId)) return new ResultForm(false);
            return new ContentResultForm(true, aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

    @ApiOperation(value = "保存或修改个人信息", notes = "保存或修改个人信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "linkmanInfoId", value = "联系人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanType", value = "联系人类型。c表示个人联系人，u表示企业联系人，p表示企业项目联系人", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanCate", value = "联系人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanName", value = "联系人姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanAddr", value = "联系人住址", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanOfficePhon", value = "办公电话", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanMobilePhone", value = "手机号码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanFax", value = "传真", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanMail", value = "电子邮件", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanCertNo", value = "证件号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isActive", value = "是否启用，0表示不启用，1表示启用", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isDeleted", value = "是否逻辑删除，0表示未删除，1表示已删除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanMemo", value = "备注", required = false, dataType = "String"),
            @ApiImplicitParam(name = "loginName", value = "登录账号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "loginPwd", value = "登录密码", required = false, dataType = "String")
    })
    @RequestMapping(value = "/savePersonInfo.do", method = RequestMethod.POST)
    public ResultForm savePersonInfo(AeaLinkmanInfo aeaLinkmanInfo) {
        try {

            if (aeaLinkmanInfo == null) return new ResultForm(false);
            if (StringUtils.isBlank(aeaLinkmanInfo.getLinkmanInfoId())) {
                aeaLinkmanInfo.setLinkmanInfoId(UUID.randomUUID().toString());
//                aeaLinkmanInfo.setCreater(SecurityContext.getCurrentUserName());
                aeaLinkmanInfo.setCreater("htry");
                aeaLinkmanInfo.setCreateTime(new Date());
                aeaLinkmanInfo.setIsActive("1");
                aeaLinkmanInfo.setIsDeleted("0");
                aeaLinkmanInfoMapper.insertAeaLinkmanInfo(aeaLinkmanInfo);
            } else {
//                aeaLinkmanInfo.setModifier(SecurityContext.getCurrentUserName());
                aeaLinkmanInfo.setModifier("htry");
                aeaLinkmanInfo.setModifyTime(new Date());
                aeaLinkmanInfoMapper.updateAeaLinkmanInfo(aeaLinkmanInfo);
            }
            return new ResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

    @ApiOperation(value = "获取单位信息", notes = "获取单位信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "untInfoId", value = "单位信息ID", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/getUnitInfo.do", method = RequestMethod.POST)
    public ResultForm getUnitInfo(String untInfoId) {
        try {
            if (StringUtils.isBlank(untInfoId)) return new ResultForm(false);
            AeaUnitInfo info = aeaUnitInfoMapper.getAeaUnitInfoById(untInfoId);
            EditUnitVo vo = new EditUnitVo();
            if (null != info) {

                BeanUtils.copyProperties(info, vo);
            }
            return new ContentResultForm(true, vo, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

    @ApiOperation(value = "保存或修改单位信息", notes = "保存或修改单位信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "untInfoId", value = "单位信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "applicant", value = "项目（法人）单位名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "idtype", value = "单位证照类型，来自于数据字典，包括：统一社会信用代码、企业营业执照、组织机构代码、工商登记号、单位相关证件号等", required = false, dataType = "String"),
            @ApiImplicitParam(name = "idcard", value = "单位证照号码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "contact", value = "联系人姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "联系人手机号码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "联系人电子邮箱", required = false, dataType = "String"),
            @ApiImplicitParam(name = "applicantDistrict", value = "行政区（园区）", required = false, dataType = "String"),
            @ApiImplicitParam(name = "applicantDetailSite", value = "具体地址", required = false, dataType = "String"),
            @ApiImplicitParam(name = "idrepresentative", value = "法人姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "idmobile", value = "法人手机号码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "idno", value = "法人身份证号码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isDeleted", value = "是否逻辑删除，0表示未删除，1表示已删除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isPrimaryUnit", value = "是否主单位，0表示副单位，1表示主单位", required = false, dataType = "String"),
            @ApiImplicitParam(name = "loginName", value = "单位登录名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "unitType", value = "单位类型，来自于数据字典，包括：建设单位、施工单位、勘察单位、设计单位、监理单位、代建单位、其他", required = false, dataType = "String"),
            @ApiImplicitParam(name = "loginPwd", value = "单位密码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "managementScope", value = "经营范围", required = false, dataType = "String"),
            @ApiImplicitParam(name = "registeredCapital", value = "注册资本", required = false, dataType = "String"),
            @ApiImplicitParam(name = "registerAuthority", value = "注册登记机关", required = false, dataType = "String"),
            @ApiImplicitParam(name = "unitNature", value = "单位性质：1 企业，2 事业单位，3 社会组织", required = false, dataType = "String"),
            @ApiImplicitParam(name = "postalCode", value = "邮政编码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isImUnit", value = "是否为中介机构：1 是，0 否", required = false, dataType = "String")
    })
    @RequestMapping(value = "/saveUnitInfo.do", method = RequestMethod.POST)
    public ResultForm saveUnitInfo(EditUnitVo vo) {
        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        BeanUtils.copyProperties(vo, aeaUnitInfo);
        try {
            if (aeaUnitInfo == null) return new ResultForm(false);
            if (StringUtils.isBlank(aeaUnitInfo.getUnitInfoId())) {
                aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
                aeaUnitInfo.setCreater("htry");
                aeaUnitInfo.setCreateTime(new Date());
                aeaUnitInfo.setIsDeleted("0");
                aeaUnitInfoMapper.insertAeaUnitInfo(aeaUnitInfo);
            } else {
                aeaUnitInfo.setModifier("htry");
                aeaUnitInfo.setModifyTime(new Date());
                aeaUnitInfoMapper.updateAeaUnitInfo(aeaUnitInfo);
            }
            return new ResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

    @ApiOperation(value = "获取文件流（BASE64）", notes = "获取文件流（BASE64）。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailId", value = "文件ID", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/getFileStream.do", method = RequestMethod.POST)
    public Map getFileStream(String detailId) {
        try {
            if (StringUtils.isBlank(detailId)) return null;
            return supMainService.getFileStream(detailId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @ApiOperation(value = "切换业主单位或中介机构", notes = "切换业主单位或中介机构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitInfoId", value = "单位或机构ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isOwner", value = "是否为业主单位,0为中介机构，1为业主单位", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/changeLoginUnitInfo", method = RequestMethod.POST)
    public ContentRestResult<LoginInfoVo> changeLoginUnitInfo(String unitInfoId, String isOwner, HttpServletRequest request) {
        try {

            if (StringUtils.isBlank(unitInfoId) || StringUtils.isBlank(isOwner)) {
                return new ContentRestResult(false, null, "缺少必要的请求参数");
            }

            return loginService.changeLoginUnitInfo(unitInfoId, isOwner, request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentRestResult(false, null, e.getMessage());
        }
    }

    @ApiOperation(value = "上传头像", notes = "上传头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "图片", required = true),
    })
    @RequestMapping(value = "/uploadHeadImage", method = RequestMethod.POST)
    public RestResult uploadHeadImage(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request) {
        try {
            return new RestResult(loginService.uploadHeadImage(file, request));
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResult(false, e.getMessage());
        }
    }

    @ApiOperation(value = "获取头像", notes = "获取头像")
    @RequestMapping(value = "/getHeadImage", method = RequestMethod.GET)
    public byte[] getHeadImage(HttpServletRequest request) throws Exception {
        return loginService.getHeadImage(request);
    }

    @ApiOperation(value = "合同范本页面", notes = "合同范本页面")
    @GetMapping("/contactTemplate.html")
    public ModelAndView contactTemplateIndex() throws Exception {
        return new ModelAndView("zjcs/guide/contact_template");
    }


}