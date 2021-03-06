package com.augurit.aplanmis.rest.preview.controller;

import com.augurit.agcloud.bsc.constant.FileType;
import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.rest.preview.service.PreviewPdfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangwn on 2019/8/15.
 * 预览pdf文件
 */
@RestController
@Slf4j
@Api(value = "预览pdf文件", tags = "文件管理 --> 预览pdf文件")
@RequestMapping("/previewPdf")
public class PreViewPdfController {

    @Autowired
    private PreviewPdfService previewPdfService;
    @Autowired
    private IBscAttService bscAttService;


    /*@GetMapping("/view")
    @ApiOperation("在线预览pdf文件")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId", value = "文件detailId", required = true, dataType = "String")
    )
    public ModelAndView previewPdf(String detailId) {
        ModelAndView mav = new ModelAndView("preview/viewPdf");
        mav.addObject("detailId", detailId);
        return mav;
    }*/

    @GetMapping("/pdfIsCoverted")
    @ApiOperation("查询文件转换pdf是否完成")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId", value = "文件detailId", required = true, dataType = "String")
    )
    public ResultForm pdfIsCoverted(String detailId) throws Exception {
        return previewPdfService.checkIsCoverted(detailId);
    }

    @GetMapping("/loadPdf")
    @ApiOperation("pdf.js插件预览")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId", value = "文件detailId", required = true, dataType = "String")
    )
    public ResultForm loadPdf(String detailId, HttpServletResponse response) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        BscAttDetail form = bscAttService.getAttDetailByDetailId(detailId);
        boolean boo = previewPdfService.downLoadPdf(detailId, !FileType.pdf.equals(form.getAttFormat().toLowerCase()), response);
        resultForm.setSuccess(boo);
        return resultForm;
    }

    @GetMapping("/downLoadCovertPdf")
    @ApiOperation("下载一个由上传文件转换出来的pdf文件")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId", value = "文件detailId", required = true, dataType = "String")
    )
    public ResultForm downLoadCovertPdf(String detailId, HttpServletResponse response) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        boolean boo = previewPdfService.downLoadPdf(detailId, true, response);
        resultForm.setSuccess(boo);
        return resultForm;
    }

    @GetMapping("/downLoadPdf")
    @ApiOperation("下载pdf原文件")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId", value = "文件detailId", required = true, dataType = "String")
    )
    public ResultForm downLoadPdf(String detailId, HttpServletResponse response) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        boolean boo = previewPdfService.downLoadPdf(detailId, false, response);
        resultForm.setSuccess(boo);
        return resultForm;
    }

}
