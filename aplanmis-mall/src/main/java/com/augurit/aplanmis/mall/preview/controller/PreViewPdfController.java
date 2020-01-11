package com.augurit.aplanmis.mall.preview.controller;

import com.augurit.agcloud.bsc.constant.FileType;
import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.service.CommonCheckService;
import com.augurit.aplanmis.mall.preview.service.PreviewPdfService;
import com.augurit.aplanmis.mall.userCenter.service.RestFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    private static Logger logger = LoggerFactory.getLogger(PreViewPdfController.class);

    @Autowired
    private PreviewPdfService previewPdfService;
    @Autowired
    private IBscAttService bscAttService;
    @Autowired
    private RestFileService restFileService;
    @Autowired
    private CommonCheckService commonCheckService;
    @Value("${aplanmis.mall.skin:skin_v4.1/}")
    private String skin;

    @GetMapping("/view")
    @ApiOperation("在线预览pdf文件")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId", value = "文件detailId", required = true, dataType = "String")
    )
    public ModelAndView previewPdf(String detailId,HttpServletRequest request)throws Exception {
        if (!commonCheckService.isFileBelong(detailId,request)) throw new Exception("预览出错");
        ModelAndView mav = new ModelAndView("preview/viewPdf");
        mav.addObject("detailId", detailId);
        return mav;
    }

    @GetMapping("/pdfIsCoverted")
    @ApiOperation("查询文件转换pdf是否完成")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId", value = "文件detailId", required = true, dataType = "String")
    )
    public ResultForm pdfIsCoverted(String detailId) throws Exception {
        ResultForm resultForm = previewPdfService.checkIsCoverted(detailId);
        return resultForm;
    }

    @GetMapping("/loadPdf")
    @ApiOperation("pdf.js插件预览")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId", value = "文件detailId", required = true, dataType = "String")
    )
    public ResultForm loadPdf(String detailId, HttpServletResponse response, HttpServletRequest request) throws Exception {
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
    public ResultForm downLoadCovertPdf(String detailId, HttpServletResponse response, HttpServletRequest request) throws Exception {
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
    public ResultForm downLoadPdf(String detailId, HttpServletResponse response, HttpServletRequest request) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        boolean boo = previewPdfService.downLoadPdf(detailId, false, response);
        resultForm.setSuccess(boo);
        return resultForm;
    }

}
