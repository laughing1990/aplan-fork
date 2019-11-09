package com.augurit.aplanmis.common.service.oneForm;
import com.augurit.agcloud.bpm.common.sfengine.config.SFRenderConfig;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.front.basis.stage.vo.OneFormStageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/rest/oneform/common")
@RestController
@Api(value = "智能表单一张表单通用接口，代替各个项目(front/mall/rest等)的一张表单接口", tags = "一张表单填报与展现")
public class OneFormCommonController {
    @Autowired
    private OneFormCommonService oneFormCommonService;
    @RequestMapping(value = "/renderHtmlFormContainer", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("根据参数获取一张表单的开发表单列表和多个form的html片段，该片段用于填充到dom容器")
    public ContentResultForm<Map> renderFormsHtmlPost(OneFormStageRequest oneFormStageRequest, SFRenderConfig sFRenderConfig) {
        try {
            return oneFormCommonService.renderHtmlFormContainer(oneFormStageRequest, sFRenderConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, e.getMessage());
        }
    }
    @RequestMapping(value = "/renderPage", method = {RequestMethod.GET})
    @ApiOperation("根据参数渲染一张表单整个页面")
    public void renderPage(OneFormStageRequest oneFormStageRequest,SFRenderConfig sFRenderConfig) {
        oneFormCommonService.renderPage(oneFormStageRequest,sFRenderConfig);
    }
}