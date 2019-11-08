package com.augurit.aplanmis.admin.credit.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaCreditDetail;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditDetailService;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * 信用管理-信用汇总子表（字段列表）-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/credit/detail")
public class AeaCreditDetailController {

    private static Logger logger = LoggerFactory.getLogger(AeaCreditDetailController.class);

    @Autowired
    private AeaCreditDetailService aeaCreditDetailService;

    @RequestMapping("/indexAeaCreditDetail.do")
    public ModelAndView indexAeaCreditDetail(AeaCreditDetail aeaCreditDetail, String infoType) {
        return new ModelAndView("aea/credit/detail_index");
    }

    @RequestMapping("/listAeaCreditDetail.do")
    public PageInfo<AeaCreditDetail> listAeaCreditDetail(AeaCreditDetail aeaCreditDetail, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCreditDetail);
        return aeaCreditDetailService.listAeaCreditDetail(aeaCreditDetail, page);
    }

    @RequestMapping("/getAeaCreditDetail.do")
    public AeaCreditDetail getAeaCreditDetail(String id) throws Exception {
        if (id != null) {
            logger.debug("根据ID获取AeaCreditDetail对象，ID为：{}", id);
            return aeaCreditDetailService.getAeaCreditDetailById(id);
        } else {
            logger.debug("构建新的AeaCreditDetail对象");
            return new AeaCreditDetail();
        }
    }

    @RequestMapping("/updateAeaCreditDetail.do")
    public ResultForm updateAeaCreditDetail(AeaCreditDetail aeaCreditDetail) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaCreditDetail);
        aeaCreditDetailService.updateAeaCreditDetail(aeaCreditDetail);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑信用管理-信用汇总子表（字段列表）
     *
     * @param aeaCreditDetail 信用管理-信用汇总子表（字段列表）
     * @param result          校验对象
     * @return 返回结果对象 包含结果信息
     * @throws Exception
     */
    @ApiOperation(value = "保存或编辑信用管理-信用汇总子表（字段列表）", notes = "保存或编辑信用管理-信用汇总子表（字段列表）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaCreditDetail", value = "非必填", dataType = "AeaCreditDetail", paramType = "query"),
    })
    @RequestMapping("/saveAeaCreditDetail.do")
    public ResultForm saveAeaCreditDetail(AeaCreditDetail aeaCreditDetail, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            logger.error("保存信用管理-信用汇总子表（字段列表）Form对象出错");
            throw new InvalidParameterException(aeaCreditDetail);
        }

        if (aeaCreditDetail.getDetailId() != null && !"".equals(aeaCreditDetail.getDetailId())) {
            aeaCreditDetailService.updateAeaCreditDetail(aeaCreditDetail);
        } else {
            if (aeaCreditDetail.getDetailId() == null || "".equals(aeaCreditDetail.getDetailId()))
                aeaCreditDetail.setDetailId(UUID.randomUUID().toString());
            aeaCreditDetailService.saveAeaCreditDetail(aeaCreditDetail);
        }

        return new ContentResultForm<AeaCreditDetail>(true, aeaCreditDetail);
    }

    @ApiOperation(value = "通过id删除信用汇总子表（字段列表）", notes = "通过id删除信用汇总子表（字段列表）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "必填", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping("/deleteAeaCreditDetailById.do")
    public ResultForm deleteAeaCreditDetailById(String ids) throws Exception {
        logger.debug("删除信用管理-信用汇总子表（字段列表）Form对象，对象id为：{}", ids);
        if (ids != null)
            aeaCreditDetailService.deleteAeaCreditDetailById(ids);
        return new ResultForm(true);
    }


    @ApiOperation(value = "查询信用详情信息列表,带分页", notes = "查询信用详情信息列表,带分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaCreditDetail", value = "非必填", dataType = "AeaCreditDetail", paramType = "body"),
            @ApiImplicitParam(name = "page", value = "分页信息", dataType = "PageParam")
    })
    @RequestMapping(value = "/page.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaCreditDetail> listByPage(AeaCreditDetail aeaCreditDetail, @ModelAttribute PageParam page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCreditDetail);
        PageInfo<AeaCreditDetail> pageInfo = aeaCreditDetailService.listAeaCreditDetail(aeaCreditDetail, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }


}
