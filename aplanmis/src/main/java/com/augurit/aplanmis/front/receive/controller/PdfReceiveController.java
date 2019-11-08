package com.augurit.aplanmis.front.receive.controller;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.front.approve.service.OfficialDocumentService;
import com.augurit.aplanmis.front.receive.service.ReceiveService;
import com.augurit.aplanmis.front.receive.utils.ReceivePDFTemplate;
import com.augurit.aplanmis.front.receive.utils.ReceivePDFUtils;
import com.augurit.aplanmis.front.receive.vo.ConstructPermitVo;
import com.augurit.aplanmis.front.receive.vo.ReceiveBaseVo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@Slf4j
@Api(value = "在线预览pdf文件", tags = "在线预览pdf文件")
@RequestMapping("/rest/receive")
public class PdfReceiveController {
    @Autowired
    private ReceiveService receiveService;

    @Autowired
    private OfficialDocumentService documentService;

    @RequestMapping(value = "/toPDF/{receiveId}", method = RequestMethod.GET)
    @ApiOperation(value = "打印回执的PDF版本")
    @ApiImplicitParams({@ApiImplicitParam(value = "回执ID", name = "receiveId", required = true),
            @ApiImplicitParam(name = "isMakeUp", value = "是否补正材料，0 否；1 是", defaultValue = "0")})
    public void testPrintPdf(@PathVariable String receiveId, String isMakeUp, HttpServletResponse resp) throws Exception {
        //根据回执D查询单个回执
        ReceiveBaseVo receiveBaseVo = null;
        if (!StringUtils.isBlank(receiveId)) {
            receiveBaseVo = receiveService.getOneReceiveByReceiveId(receiveId, "0");
        }
        String str = "";//pdf保存地址
        if (receiveBaseVo != null) {
            str = ReceivePDFUtils.createPDF(receiveBaseVo);
        }
        //读取指定路径下的pdf文件
        ReceivePDFTemplate.readPdf(str, resp);
        File file = new File(str);
        if (file.isFile()) {
            file.delete();//删除pdf文件
        }
    }


    @RequestMapping(value = "/savePasswordPdf/{account}/{password}", method = RequestMethod.GET)
    @ApiOperation(value = "保存密码回执pdf")
    public void PrintPasswordPdf(@PathVariable("account") String account, @PathVariable("password") String password, HttpServletResponse resp) throws Exception {
        ReceivePDFTemplate.createPasswordTemplate(resp, account, password);
    }

    @RequestMapping(value = "/preview/construct/permit", method = RequestMethod.GET)
    @ApiOperation(value = "施工许可证PDF预览")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "证照实例ID", name = "certinstId", required = true)
            , @ApiImplicitParam(value = "true 打印模板：false 预览模板", name = "print", required = true)
    })
    public void PreviewConstructPermitPdf(Boolean print, String certinstId, HttpServletResponse response) throws Exception {
        ConstructPermitVo vo = documentService.getConstructPermitInfoVo(certinstId);
        try {
            ReceivePDFTemplate.createConstructPermitPdf(print, vo, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
