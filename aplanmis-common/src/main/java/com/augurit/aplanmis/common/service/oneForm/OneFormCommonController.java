package com.augurit.aplanmis.common.service.oneForm;
import com.augurit.agcloud.bpm.common.sfengine.config.SFRenderConfig;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.front.basis.stage.vo.OneFormStageRequest;
import com.augurit.aplanmis.front.basis.stage.vo.FormFrofileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/rest/oneform/common")
@RestController
@Api(value = "智能表单一张表单通用接口，代替各个项目(front/mall/rest等)的一张表单接口", tags = "一张表单填报与展现")
public class OneFormCommonController {
    @Autowired
    private OneFormCommonService oneFormCommonService;

    /********************并联申报接口 begin ********************************/
    //并联申报-一张表单(不含开发表单)接口，已经不再采用，通过"包含开发表单的接口"代替
//    @RequestMapping(value = "/renderHtmlFormContainer", method = {RequestMethod.GET, RequestMethod.POST})
//    @ApiOperation("根据参数获取一张表单的开发表单列表和多个form的html片段，该片段用于填充到dom容器")
//    public ContentResultForm<Map> renderFormsHtmlPost(OneFormStageRequest oneFormStageRequest, SFRenderConfig sFRenderConfig) {
//        try {
//            return oneFormCommonService.renderHtmlFormContainer(oneFormStageRequest, sFRenderConfig);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ContentResultForm(false, e.getMessage());
//        }
//    }

    @RequestMapping(value = "/getListForm4StageOneForm", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("根据阶段/事项，返回表单列表(包括智能表单，开发表单)")
    public ResultForm getListForm4StageOneForm(OneFormStageRequest oneFormStageRequest, SFRenderConfig sFRenderConfig) {
        try {
            List<FormFrofileVo> result = oneFormCommonService.getListForm4StageOneForm(oneFormStageRequest);
            return new ContentResultForm(true, result);
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
    /********************并联申报接口 end ********************************/

    /********************单事项申报接口 begin ********************************/

    /********************单事项申报接口 end ********************************/

}