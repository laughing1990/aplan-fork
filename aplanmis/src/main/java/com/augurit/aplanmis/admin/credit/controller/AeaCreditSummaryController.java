package com.augurit.aplanmis.admin.credit.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaCreditSummary;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditSummaryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 信用管理-红黑名单管理-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/credit/summary")
public class AeaCreditSummaryController {

    private static Logger logger = LoggerFactory.getLogger(AeaCreditSummaryController.class);

    @Autowired
    private AeaCreditSummaryService aeaCreditSummaryService;

    @RequestMapping("/listAeaCreditSummary.do")
    public PageInfo<AeaCreditSummary> listAeaCreditSummary(AeaCreditSummary aeaCreditSummary, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCreditSummary);
        return aeaCreditSummaryService.listAeaCreditSummary(aeaCreditSummary, page);
    }

    @RequestMapping("/getAeaCreditSummary.do")
    public AeaCreditSummary getAeaCreditSummary(String id) throws Exception {
        if (id != null) {
            logger.debug("根据ID获取AeaCreditSummary对象，ID为：{}", id);
            return aeaCreditSummaryService.getAeaCreditSummaryById(id);
        } else {
            logger.debug("构建新的AeaCreditSummary对象");
            return new AeaCreditSummary();
        }
    }

    @RequestMapping("/updateAeaCreditSummary.do")
    public ResultForm updateAeaCreditSummary(AeaCreditSummary aeaCreditSummary) throws Exception {
        logger.debug("更新信用详情管理Form对象，对象为：{}", aeaCreditSummary);
        aeaCreditSummaryService.updateAeaCreditSummary(aeaCreditSummary);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑信用管理-信用详情管理
     *
     * @param aeaCreditSummary 信用管理-信用详情管理
     * @param result           校验对象
     * @return 返回结果对象 包含结果信息
     * @throws Exception
     */
    @ApiOperation(value = "保存或编辑信用管理-信用详情管理", notes = "保存或编辑信用管理-信用详情管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaCreditSummary", value = "非必填", dataType = "AeaCreditSummary", paramType = "query"),
    })
    @RequestMapping("/saveAeaCreditSummary.do")
    public ResultForm saveAeaCreditSummary(AeaCreditSummary aeaCreditSummary, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            logger.error("保存信用管理-信用详情管理Form对象出错");
            throw new InvalidParameterException(aeaCreditSummary);
        }

        if (aeaCreditSummary.getSummaryId() != null && !"".equals(aeaCreditSummary.getSummaryId())) {
            aeaCreditSummaryService.updateAeaCreditSummary(aeaCreditSummary);
        } else {
            if (aeaCreditSummary.getSummaryId() == null || "".equals(aeaCreditSummary.getSummaryId()))
                aeaCreditSummary.setSummaryId(UUID.randomUUID().toString());
            aeaCreditSummaryService.saveAeaCreditSummary(aeaCreditSummary);
        }

        return new ContentResultForm<AeaCreditSummary>(true, aeaCreditSummary);
    }

    @ApiOperation(value = "通过id删除信用信息数据", notes = "通过id删除信用信息数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "summaryIds", value = "必填", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping("/deleteAeaCreditSummaryById.do")
    public ResultForm deleteAeaCreditSummaryById(String summaryIds) throws Exception {
        logger.debug("删除信用管理-信用详情管理Form对象，对象id为：{}", summaryIds);
        if (summaryIds != null)
            aeaCreditSummaryService.deleteAeaCreditSummaryById(summaryIds);
        return new ResultForm(true);
    }

    @ApiOperation(value = "查询信用信息列表,带分页", notes = "查询信用信息列表,带分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaCreditRedblack", value = "非必填", dataType = "AeaCreditRedblack", paramType = "body"),
            @ApiImplicitParam(name = "page", value = "分页信息", dataType = "PageParam")
    })
    @RequestMapping(value = "/page.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaCreditSummary> listByPage(AeaCreditSummary aeaCreditSummary, @ModelAttribute PageParam page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCreditSummary);
        PageInfo<AeaCreditSummary> pageInfo = aeaCreditSummaryService.listAeaCreditSummary(aeaCreditSummary, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }


}
