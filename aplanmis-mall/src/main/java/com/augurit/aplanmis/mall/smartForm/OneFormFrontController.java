//package com.augurit.aplanmis.mall.smartForm;
//
//import com.augurit.agcloud.framework.ui.result.ContentResultForm;
//import com.augurit.aplanmis.common.service.oneForm.OneFormCommonService;
//import com.augurit.aplanmis.front.basis.stage.vo.OneFormStageRequest;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RequestMapping("/rest/userCenter/oneform")
//@RestController
//@Api(value = "智能表单一张表单front接口", tags = "一张表单填报与展现")
//public class OneFormFrontController {
//
//    @Autowired
//    private OneFormCommonService oneFormCommonService;
//
//
//    @RequestMapping(value = "/forms/render2", method = {RequestMethod.GET, RequestMethod.POST})
//    @ApiOperation("根据综合参数获取一张表单的多个form的html片段（正式使用接口,返回封装了HTML片段）")
//    public ContentResultForm<String > renderFormsHtmlPost(OneFormStageRequest oneFormStageRequest) {
//        return oneFormCommonService.renderHtmlFormContainer(oneFormStageRequest);
//    }
//
//}
