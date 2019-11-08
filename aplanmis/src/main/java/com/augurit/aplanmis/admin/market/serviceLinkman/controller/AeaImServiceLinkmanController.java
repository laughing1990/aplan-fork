package com.augurit.aplanmis.admin.market.serviceLinkman.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.serviceLinkman.service.AeaImServiceLinkmanService;
import com.augurit.aplanmis.common.domain.AeaImServiceLinkman;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.UUID;

@Api(description = "中介服务与从业人员关联关系操作接口")
@RestController
@RequestMapping("/supermarket/serviceLinkman")
public class AeaImServiceLinkmanController {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceLinkmanController.class);

    @Autowired
    private AeaImServiceLinkmanService aeaImServiceLinkmanService;


    @RequestMapping("/indexAeaImServiceLinkman.do")
    public ModelAndView indexAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman, String infoType) {
        return new ModelAndView("ui-jsp/supermarket_manage/serviceLinkman/service_linkman");
    }

    @RequestMapping("/indexAeaImServiceLinkman.html")
    public ModelAndView indexAeaImServiceLinkmanHtml(AeaImServiceLinkman aeaImServiceLinkman, String infoType) {
        return new ModelAndView("admin/supermarket/service_linkman");
    }

    @ApiOperation(value = "查询关联信息列表", notes = "查询关联信息列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceLinkmanId", value = "关联信息ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "从业人员ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isDelete", value = "是否逻辑删除，0表示未删除，1表示已删除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isHead", value = "是否负责人，0表示否，1表示是", required = false, dataType = "String"),
            @ApiImplicitParam(name = "auditFlag", value = "0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成,6 已过时，7 编辑中", required = false, dataType = "String"),
            @ApiImplicitParam(name = "practiseDate", value = "从业时间", required = true, dataType = "Date")
    })
    @RequestMapping("/listAeaImServiceLinkman.do")
    public EasyuiPageInfo<AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImServiceLinkman);
        return aeaImServiceLinkmanService.listAeaImServiceLinkman(aeaImServiceLinkman, page);
    }

    @GetMapping("/getListAeaImServiceLinkman")
    public ResultForm getListAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman, Integer pageNum, Integer pageSize) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImServiceLinkman);
        if (pageNum == null || pageSize == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        Page page = new Page(pageNum, pageSize);
        EasyuiPageInfo<AeaImServiceLinkman> pageInfo = this.listAeaImServiceLinkman(aeaImServiceLinkman, page);
        return new ContentResultForm<>(true, pageInfo, "success");
    }

    @RequestMapping("/getAeaImServiceLinkman.do")
    public ModelAndView getAeaImServiceLinkman(ModelMap modelMap, String serviceLinkmanId) throws Exception {
        modelMap.put("serviceLinkmanId", serviceLinkmanId);
        return new ModelAndView("ui-jsp/supermarket_manage/serviceLinkman/service_linkman_detail");
    }

    @RequestMapping("/serviceLinkmanDetail.html")
    public ModelAndView serviceLinkmanDetail(String serviceLinkmanId) throws Exception {
        ModelAndView mav = new ModelAndView("admin/supermarket/service_linkman_detail");
        mav.addObject("serviceLinkmanId", serviceLinkmanId);
        return mav;
    }

    @RequestMapping("/getAeaImServiceLinkmanById.do")
    public ResultForm getAeaImServiceLinkmanById(String serviceLinkmanId) throws Exception {
        if (StringUtils.isBlank(serviceLinkmanId)) {
            return new ContentResultForm(false, "参数为空");
        }

        AeaImServiceLinkman aeaImServiceLinkman = aeaImServiceLinkmanService.getAeaImServiceLinkmanById(serviceLinkmanId);
        return new ContentResultForm(true, aeaImServiceLinkman);
    }

    @GetMapping("/getServiceLinkmanDetailById")
    @ApiOperation(value = "根据从业人员ID查询详细信息", notes = "根据从业人员ID查询详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String")
    })
    public ResultForm getServiceLinkmanDetailById(String serviceLinkmanId) throws Exception {
        if (StringUtils.isBlank(serviceLinkmanId)) {
            return new ContentResultForm(false, "参数为空");
        }

        AeaImServiceLinkman aeaImServiceLinkman = aeaImServiceLinkmanService.getAeaImServiceLinkmanById(serviceLinkmanId);
        return new ContentResultForm(true, aeaImServiceLinkman);
    }

    @ApiOperation(value = "根据服务Id查询从业人员信息列表", notes = "根据服务Id查询从业人员信息列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String")
    })
    @RequestMapping("/listAeaImServiceLinkmanByServiceId.do")
    public PageInfo<AeaLinkmanInfo> listAeaImServiceLinkmanByServiceId(String serviceId, Page page) throws Exception {

        return aeaImServiceLinkmanService.listAeaImServiceLinkmanByServiceId(serviceId, page);
    }

    @ApiOperation(value = "审核从业人员信息", notes = "审核从业人员信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceLinkmanId", value = "关联信息ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "auditFlag", value = "0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成,6 已过时，7 编辑中", required = false, dataType = "String"),
    })
    @RequestMapping("/updateAeaImServiceLinkman.do")
    public ResultForm updateAeaImServiceLinkman(String serviceLinkmanId, String auditFlag, String memo) throws Exception {

        if (serviceLinkmanId != null && !"".equals(serviceLinkmanId)) {

            aeaImServiceLinkmanService.updateServiceLinkmanAudit(serviceLinkmanId, auditFlag, memo);
        }

        return new ResultForm(true);
    }


    @ApiOperation(value = "保存中介服务与从业人员关联信息", notes = "保存中介服务与从业人员关联信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceLinkmanId", value = "关联信息ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "从业人员ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isDelete", value = "是否逻辑删除，0表示未删除，1表示已删除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isHead", value = "是否负责人，0表示否，1表示是", required = false, dataType = "String"),
            @ApiImplicitParam(name = "auditFlag", value = "0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成,6 已过时，7 编辑中", required = false, dataType = "String"),
            @ApiImplicitParam(name = "practiseDate", value = "从业时间", required = true, dataType = "Date")
    })
    @RequestMapping("/saveAeaImServiceLinkman.do")
    public ResultForm saveAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaImServiceLinkman);
        }

        if (aeaImServiceLinkman.getServiceLinkmanId() != null && !"".equals(aeaImServiceLinkman.getServiceLinkmanId())) {
            aeaImServiceLinkmanService.updateAeaImServiceLinkman(aeaImServiceLinkman);
        } else {
            if (aeaImServiceLinkman.getServiceLinkmanId() == null || "".equals(aeaImServiceLinkman.getServiceLinkmanId()))
                aeaImServiceLinkman.setServiceLinkmanId(UUID.randomUUID().toString());
            aeaImServiceLinkman.setIsDelete("0");
            aeaImServiceLinkman.setCreateTime(new Date());
            aeaImServiceLinkman.setCreater(SecurityContext.getCurrentUserName());
            aeaImServiceLinkmanService.saveAeaImServiceLinkman(aeaImServiceLinkman);
        }

        return new ContentResultForm<AeaImServiceLinkman>(true, aeaImServiceLinkman);
    }

    @ApiOperation(value = "删除关联信息", notes = "删除关联信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceLinkmanId", value = "关联信息ID", required = true, dataType = "String"),
    })
    @RequestMapping("/deleteAeaImServiceLinkmanById.do")
    public ResultForm deleteAeaImServiceLinkmanById(String serviceLinkmanId) throws Exception {
        logger.debug("删除Form对象，对象id为：{}", serviceLinkmanId);
        if (serviceLinkmanId != null)
            aeaImServiceLinkmanService.deleteAeaImServiceLinkmanById(serviceLinkmanId);
        return new ResultForm(true);
    }


}
