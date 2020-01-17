package com.augurit.aplanmis.common.service.oneForm;
import com.augurit.agcloud.bpm.common.sfengine.config.SFRenderConfig;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.constants.TimeruleInstState;
import com.augurit.aplanmis.common.utils.ExcelUtils;
import com.augurit.aplanmis.common.vo.conditional.ApplyInfo;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.front.basis.stage.vo.OneFormItemRequest;
import com.augurit.aplanmis.front.basis.stage.vo.OneFormStageRequest;
import com.augurit.aplanmis.front.basis.stage.vo.FormFrofileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

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
    public void renderPage(OneFormStageRequest oneFormStageRequest, SFRenderConfig sFRenderConfig) {
        oneFormCommonService.renderPage(oneFormStageRequest,sFRenderConfig);
    }
    /********************并联申报接口 end ********************************/

    /********************单事项申报接口 begin ********************************/
    @RequestMapping(value = "/getListForm4ItemOneForm", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("根据事项，返回表单列表(包括智能表单，开发表单)")
    public ResultForm getListForm4ItemOneForm(OneFormItemRequest oneFormItemRequest, SFRenderConfig sFRenderConfig) {
        try {
            List<FormFrofileVo> result = oneFormCommonService.getListForm4ItemOneForm(oneFormItemRequest);
            return new ContentResultForm(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, e.getMessage());
        }
    }

    /********************单事项申报接口 end ********************************/

    @RequestMapping(value = "/exportAllFormDatas", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("导出智能表单设计相关表数据到Excel")
    public ResultForm exportAllFormDatas(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return oneFormCommonService.exportAllFormTableDatas(request, response);
    }

    @RequestMapping(value = "/exportOneFormDatas", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("导出智能表单数据到Excel")
    public void exportOneFormDatas(HttpServletRequest request, HttpServletResponse response) throws Exception {
        oneFormCommonService.exportOneFormTableDatas(request, response);
    }

    @PostMapping(value = "/importOneFormDatas")
    @ApiOperation("导入智能表单设计相关表数据")
    public ResultForm importOneFormDatas(HttpServletRequest request) throws Exception {
        oneFormCommonService.importOneFormTableDatas(request);
        return new ContentResultForm<>(true, null, "success");
    }

}