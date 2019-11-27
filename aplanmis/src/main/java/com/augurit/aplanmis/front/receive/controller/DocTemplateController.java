package com.augurit.aplanmis.front.receive.controller;

import com.augurit.aplanmis.common.service.receive.vo.ConstructPermitVo;
import com.augurit.aplanmis.front.approve.service.OfficialDocumentService;
import com.augurit.aplanmis.front.receive.service.DocTemplateService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@Api(value = "施工许可证模板操作", tags = "施工许可证模板操作")
@RequestMapping("/rest/docTemplate")
public class DocTemplateController {

    @Autowired
    private OfficialDocumentService documentService;

    @Autowired
    private DocTemplateService docTemplateService;

    @RequestMapping("/downLoad")
    public void downLoadDoc(String certinstId, HttpServletResponse response) {

        try {
            ConstructPermitVo vo = documentService.getConstructPermitInfo(certinstId);
//            ConstructPermitVo vo = ConstructPermitVo.buildDemoVo();
            docTemplateService.createDocDocument(vo, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
