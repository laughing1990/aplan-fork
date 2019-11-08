package com.augurit.aplanmis.admin.credit.controller;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaCreditDetail;
import com.augurit.aplanmis.common.domain.AeaCreditRedblack;
import com.augurit.aplanmis.common.domain.AeaCreditSummary;
import com.augurit.aplanmis.common.dto.AeaCreditSummaryAllDto;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditDetailService;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditRedblackService;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditSummaryService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 信用管理-红黑名单管理-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/credit/redblack")
@Api(value = "信用管理接口", tags = "信用管理接口")
public class AeaCreditRedblackController {

    private static Logger logger = LoggerFactory.getLogger(AeaCreditRedblackController.class);

    @Autowired
    private AeaCreditRedblackService aeaCreditRedblackService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaCreditSummaryService summaryService;

    @Autowired
    private AeaCreditDetailService detailService;

    @ApiOperation(value = "查询信用管理列表,带分页", notes = "查询信用管理列表,带分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaCreditRedblack", value = "非必填", dataType = "AeaCreditRedblack", paramType = "body"),
            @ApiImplicitParam(name = "page", value = "分页信息", dataType = "PageParam")
    })
    @RequestMapping(value = "/page.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaCreditRedblack> listByPage(AeaCreditRedblack aeaCreditRedblack, @ModelAttribute PageParam page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCreditRedblack);
        PageInfo<AeaCreditRedblack> pageInfo = aeaCreditRedblackService.listAeaCreditRedblackRelInfo(aeaCreditRedblack, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @ApiOperation(value = "查询信用管理列表,不带分页", notes = "查询信用管理列表,不带分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaCreditRedblack", value = "非必填", dataType = "AeaCreditRedblack", paramType = "body"),
    })
    @RequestMapping(value = "/noPage.do", method = {RequestMethod.POST, RequestMethod.GET})
    public List<AeaCreditRedblack> listByNoPage(AeaCreditRedblack aeaCreditRedblack) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCreditRedblack);
        return aeaCreditRedblackService.listAeaCreditRedblack(aeaCreditRedblack);
    }

    @ApiOperation(value = "通过id获取红黑名单数据", notes = "通过id获取红黑名单数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "必填", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/getById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public AeaCreditRedblack getAeaCreditRedblack(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取AeaCreditRedblack对象，ID为：{}", id);
            return aeaCreditRedblackService.getAeaCreditRedblackById(id);
        } else {
            logger.debug("构建新的AeaCreditRedblack对象");
            return new AeaCreditRedblack();
        }
    }

    @ApiOperation(value = "保存或编辑信用管理-红黑名单管理", notes = "保存或编辑信用管理-红黑名单管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaCreditRedblack", value = "非必填", dataType = "AeaCreditRedblack", paramType = "query"),
    })
    @PostMapping(value = "/save.do")
    public ResultForm saveAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack) {

        if (StringUtils.isNotBlank(aeaCreditRedblack.getRedblackId())) {
            aeaCreditRedblackService.updateAeaCreditRedblack(aeaCreditRedblack);
        } else {
            aeaCreditRedblack.setRedblackId(UUID.randomUUID().toString());
            aeaCreditRedblackService.saveAeaCreditRedblack(aeaCreditRedblack);
        }
        return new ContentResultForm<AeaCreditRedblack>(true, aeaCreditRedblack);
    }

    @ApiOperation(value = "通过id删除红黑名单数据", notes = "通过id删除红黑名单数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "必填", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/deleteById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm deleteAeaCreditRedblackById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除信用管理-红黑名单管理Form对象，对象id为：{}", id);
            aeaCreditRedblackService.deleteAeaCreditRedblackById(id);
            return new ResultForm(true);
        } else {
            throw new InvalidParameterException("参数id为空!");
        }
    }

    @ApiOperation(value = "通过id删除红黑名单数据", notes = "通过id删除红黑名单数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "必填", dataType = "String", required = true, allowMultiple = true, paramType = "query"),
    })
    @RequestMapping(value = "/deleteByIds.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm batchDelAeaCreditRedblackByIds(String[] ids) {

        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                aeaCreditRedblackService.deleteAeaCreditRedblackById(id);
            }
            return new ResultForm(true);
        } else {
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @ApiOperation(value = "获取联系人信息树结构", notes = "获取联系人信息树结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", dataType = "String", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/gtreeLinkmanForEui.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm gtreeLinkmanForEui(String keyword) {
        List<ElementUiRsTreeNode> elementUiRsTreeNodes = aeaCreditRedblackService.gtreeLinkmanForEui(keyword, SecurityContext.getCurrentOrgId());
        return new ContentResultForm<>(true, elementUiRsTreeNodes, "查询成功！");
    }

    @ApiOperation(value = "获取单位信息树结构", notes = "获取单位信息树结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", dataType = "String", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/gtreeUnitInfoForEui.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm gtreeUnitInfoForEui(String keyword) {
        List<ElementUiRsTreeNode> elementUiRsTreeNodes = aeaCreditRedblackService.gtreeUnitInfoForEui(keyword, SecurityContext.getCurrentOrgId());
        return new ContentResultForm<>(true, elementUiRsTreeNodes, "查询成功！");
    }

    @ApiOperation(value = "启用禁用数据", notes = "启用禁用数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "redblackId", value = "关键字", dataType = "String", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/enOrDisableIsValid.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm enOrDisableIsValid(String redblackId) {

        aeaCreditRedblackService.enOrDisableIsValid(redblackId);
        return new ResultForm(true);
    }

    // ================================= 信用管理接口  ============================

    @ApiOperation(value = "根据企业或者个人id查询是否存在黑名单", notes = "根据企业或者个人id查询是否存在黑名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizType", value = "业务类型,如: u 企业 l 联系人", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bizId", value = "企业或者联系人id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/listPersonOrUnitBlackByBizId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm listPersonOrUnitBlackByBizId(String bizType, String bizId) {

        if (StringUtils.isBlank(bizType)) {
            throw new InvalidParameterException("参数bizType为空!");
        }
        if (StringUtils.isBlank(bizId)) {
            throw new InvalidParameterException("参数bizId为空!");
        }
        List<AeaCreditRedblack> list = aeaCreditRedblackService.listPersonOrUnitBlackByBizId(bizType, bizId);
        return new ContentResultForm<>(true, list, "查询成功！");
    }

    @ApiOperation(value = "根据企业或者个人编号是否存在黑名单", notes = "根据企业或者个人编号是否存在黑名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizType", value = "业务类型,如: u 企业 l 联系人", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bizCode", value = "企业统一社会信用代码或者个人身份证号", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/listPersonOrUnitBlackByBizCode", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm listPersonOrUnitBlackByBizCode(String bizType, String bizCode) {

        if (StringUtils.isBlank(bizType)) {
            throw new InvalidParameterException("参数bizType为空!");
        }
        if (StringUtils.isBlank(bizCode)) {
            throw new InvalidParameterException("参数bizCode为空!");
        }
        List<AeaCreditRedblack> list = aeaCreditRedblackService.listPersonOrUnitBlackByBizCode(bizType, bizCode);
        return new ContentResultForm<>(true, list, "查询成功！");
    }

    @ApiOperation(value = "获取数据字典信用类型", notes = "获取数据字典信用类型")
    @RequestMapping(value = "/listCreditType", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm listCreditType() {

        List<BscDicCodeItem> list = bscDicCodeService.getActiveItemsByTypeCode("AEA_CREDIT_TYPE", SecurityContext.getCurrentOrgId());
        return new ContentResultForm<>(true, list, "查询成功！");
    }

    @ApiOperation(value = "通过企业或者联系人id获取信用汇总列表数据", notes = "通过企业或者联系人id获取信用汇总列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizType", value = "业务类型,如: u 企业 l 联系人", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "creditType", value = "信用类型:bad表示失信,good表示守信,reg表示登记注册备案,cert表示资质认证许可", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bizId", value = "企业或者联系人id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/listCreditSummaryByBizId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm listCreditSummaryByBizId(String bizType, String creditType, String bizId) {

        if (StringUtils.isBlank(bizType)) {
            throw new InvalidParameterException("参数bizType为空!");
        }
        if (StringUtils.isBlank(creditType)) {
            throw new InvalidParameterException("参数creditType为空!");
        }
        if (StringUtils.isBlank(bizId)) {
            throw new InvalidParameterException("参数bizId为空!");
        }
        List<AeaCreditSummary> list = summaryService.listCreditSummaryByBizId(bizType, creditType, bizId);
        return new ContentResultForm<>(true, list, "查询成功！");
    }

    @ApiOperation(value = "通过企业统一社会信用代码或者联系人身份证编号获取信用汇总列表数据", notes = "通过企业统一社会信用代码或者联系人身份证编号获取信用汇总列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizType", value = "业务类型,如: u 企业 l 联系人", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "creditType", value = "信用类型:bad表示失信,good表示守信,reg表示登记注册备案,cert表示资质认证许可", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bizCode", value = "企业统一社会信用代码或者个人身份证号", dataType = "String", required = true, paramType = "query"),
    })
    @GetMapping(value = "/listCreditSummaryByBizCode")
    public ResultForm listCreditSummaryByBizCode(String bizType, String creditType, String bizCode) {

        if (StringUtils.isBlank(bizType)) {
            throw new InvalidParameterException("参数bizType为空!");
        }
        if (StringUtils.isBlank(creditType)) {
            throw new InvalidParameterException("参数creditType为空!");
        }
        if (StringUtils.isBlank(bizCode)) {
            throw new InvalidParameterException("参数bizCode为空!");
        }
        List<AeaCreditSummary> list = summaryService.listCreditSummaryByBizCode(bizType, creditType, bizCode);
        return new ContentResultForm<>(true, list, "查询成功！");
    }

    @ApiOperation(value = "通过信用汇总id获取信用详情数据", notes = "通过信用汇总id获取信用详情数据")
    @RequestMapping(value = "/listCreditDetailBySummaryId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm listCreditDetailBySummaryId(AeaCreditDetail creditDetail) {

        creditDetail.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaCreditDetail> list = detailService.listAeaCreditDetail(creditDetail);
        return new ContentResultForm<>(true, list, "查询成功！");
    }

    @ApiOperation(value = "通过企业或者联系人id获取汇总与详情信息数据", notes = "通过企业或者联系人id获取汇总与详情信息数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "bizType", value = "业务类型,如: u 企业 l 联系人", dataType = "String", required = true, paramType = "query"),
        @ApiImplicitParam(name = "bizId", value = "企业或者联系人id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/listCreditSummaryDetailByBizId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm listCreditSummaryDetailByBizId(String bizType, String bizId) throws Exception {

        if (StringUtils.isBlank(bizType)) {
            throw new InvalidParameterException("参数bizType为空!");
        }
        if (StringUtils.isBlank(bizId)) {
            throw new InvalidParameterException("参数bizId为空!");
        }
        List<AeaCreditSummaryAllDto> list = summaryService.listCreditSummaryDetailByBizId(bizType, bizId);
        return new ContentResultForm<>(true, list, "查询成功！");
    }

    @ApiOperation(value = "通过业务code获取汇总与详情信息数据", notes = "通过业务code获取汇总与详情信息数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "bizType", value = "业务类型,如: u 企业 l 联系人", dataType = "String", required = true, paramType = "query"),
        @ApiImplicitParam(name = "bizCode", value = "企业统一社会信用代码或者个人身份证号", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/listCreditSummaryDetailByBizCode", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm listCreditSummaryDetailByBizCode(String bizType, String creditType, String bizCode) throws Exception {

        if (StringUtils.isBlank(bizType)) {
            throw new InvalidParameterException("参数bizType为空!");
        }
        if (StringUtils.isBlank(bizCode)) {
            throw new InvalidParameterException("参数bizCode为空!");
        }
        List<AeaCreditSummaryAllDto> list = summaryService.listCreditSummaryDetailByByBizCode(bizType, bizCode);
        return new ContentResultForm<>(true, list, "查询成功！");
    }
}
