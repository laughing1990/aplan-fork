package com.augurit.aplanmis.front.ntko;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.front.preview.service.PreviewPdfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@Api(tags = "ntko-控件")
@Slf4j
@RequestMapping("/rest/ntko")
public class NtkoController {

    @Autowired
    private PreviewPdfService previewPdfService;

    @GetMapping("/ntkoOpenWin")
    @ApiOperation(value = "ntko在线预览文件")
    @ApiImplicitParams({@ApiImplicitParam(value = "跳转地址 ", name = "jumpUrl", required = true),
            @ApiImplicitParam(name = "online", value = "是否在线查看", defaultValue = "false")})
    public ModelAndView ntkoOpenWin(HttpServletRequest request, Boolean online) {
        ModelAndView modelAndView = new ModelAndView("ntko/ntkoOpenWin");
        //online_view_file 需要参数 fileUrl--读取文件流的接口；suffixName---文件类型--FileUtilsServiceImpl  preview方法
        if (online != null && online) {
            modelAndView.setViewName("ntko/online_view_file");
        }
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();
            String value = request.getParameter(name);
            modelAndView.addObject(name, value);
        }
        String ctx = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        modelAndView.addObject("ctx",ctx);
        return modelAndView;
    }

    @GetMapping("/noInstall")
    @ApiOperation(value = "跳转ntko控件提示安装页面")
    public ModelAndView noInstall() {
        return new ModelAndView("ntko/noInstallNtkoTipPage");
    }

    @GetMapping("/downLoadFile")
    @ApiOperation("下载原文件")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId",value = "文件detailId",required = true,dataType = "String")
    )
    public ResultForm downLoadFile(String detailId, HttpServletResponse response, HttpServletRequest request)throws Exception{
        ResultForm resultForm = new ResultForm(false);
        boolean boo = previewPdfService.downLoadPdf(detailId,false, response);
        resultForm.setSuccess(boo);
        return resultForm;
    }
}
