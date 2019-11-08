package com.augurit.aplanmis.front.diagram.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.diagram.dto.MatAttachDto;
import com.augurit.aplanmis.common.service.diagram.qo.MatAttachQo;
import com.augurit.aplanmis.common.service.diagram.service.DiagramService;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/diagram")
@Api(value = "全景流程图")
public class DiagramController {

    @Autowired
    private DiagramService diagramService;

    @GetMapping("/attachments")
    @ApiOperation(value = "事项的所有附件")
    public ContentResultForm<MatAttachDto> getAllAttachmentsByIteminstId(MatAttachQo matAttachQo) {
        Assert.state(StringUtils.isNotBlank(matAttachQo.getIteminstId()), "iteminstId must not null.");

        // todo

        diagramService.getAllAttachmentsByIteminstId(matAttachQo);

        return null;
    }
}
