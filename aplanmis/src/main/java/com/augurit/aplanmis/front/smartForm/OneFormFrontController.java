//
//package com.augurit.aplanmis.front.smartForm;
//
//import com.augurit.agcloud.framework.ui.result.ContentResultForm;
//import com.augurit.aplanmis.common.service.oneForm.OneFormCommonService;
//import com.augurit.aplanmis.front.basis.stage.vo.OneFormStageRequest;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RequestMapping("/rest/oneform")
//@RestController
//@Api(value = "智能表单一张表单front接口", tags = "一张表单填报与展现")
//public class OneFormFrontController {
//    @Autowired
//    private OneFormCommonService oneFormCommonService;
//    @GetMapping("/forms/render2")
//    @ApiOperation("根据综合参数获取一张表单的多个form的html片段（正式使用接口,返回封装了HTML片段）")
//    public ContentResultForm<String > renderFormsHtmlPost(OneFormStageRequest oneFormStageRequest) {
//        return oneFormCommonService.renderHtmlFormContainer(oneFormStageRequest);
//    }
//
////    @GetMapping("/redirect/home")
////    @ApiImplicitParam(name = "stageId", value = "阶段id")
////    @ApiOperation("阶段id对应的一张表单")
////    public ModelAndView redirectOneFormHomePage(@RequestParam String stageId) {
////       ModelAndView modelAndView=new ModelAndView();
////       return modelAndView;
////    }
////    @GetMapping("/forms/render")
////    @ApiImplicitParams({@ApiImplicitParam(value = "阶段id", name = "stageId", required = true, dataType = "String")})
////    @ApiOperation("根据阶段id获取一张表单的多个form的html片段,仅允许传递阶段ID参数")
////    public ContentResultForm<String > renderFormsHtml( String stageId) {
////        OneFormStageRequest oneFormStageRequest=new OneFormStageRequest();oneFormStageRequest.setStageId(stageId);
////        return oneFormCommonService.renderHtmlFormContainer(oneFormStageRequest);
////    }
////    protected List<String>  getValidItemIds(List <String >  itemverIds,String itemids){
////        if(StringUtils.isNotBlank(itemids) && itemids.split(",")!=null){
////            itemverIds=Arrays.asList(itemids.split(","));
////            Iterator<String> it = itemverIds.iterator();
////            while (it.hasNext()) {
////                String s = it.next();
////                if (StringUtils.isBlank(s)) {
////                    it.remove();
////                }
////            }
////        }
////        return itemverIds;
////    }
////    @GetMapping("/forms/renderTest")
////    @ApiOperation("根据综合参数获取一张表单的多个form的html片段(用于开发调试,返回HTML片段)")
////    public String renderFormsHtmlPostTest(OneFormStageRequest oneFormStageRequest) {
////        return oneFormCommonService.renderHtmlFormContainer(oneFormStageRequest).getContent();
////    }
//}
