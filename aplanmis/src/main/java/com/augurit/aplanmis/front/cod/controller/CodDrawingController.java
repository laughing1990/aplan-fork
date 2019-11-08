package com.augurit.aplanmis.front.cod.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.front.cod.service.CodDrawingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;


/**
* 施工图审查
*/
@RestController
@Api(value = "施工图审查",tags = "施工图审查")
@RequestMapping("/cod/drawing")
public class CodDrawingController {

private static Logger logger = LoggerFactory.getLogger(CodDrawingController.class);

    @Autowired
    private CodDrawingService codDrawingService;

    @GetMapping("/drawingCheck")
    @ApiOperation("查看施工图")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId",value = "文件detailId",required = true,dataType = "String")
    )
    public ModelAndView drawingCheck(String detailId){
        ModelAndView mav = new ModelAndView("checkdrawing/checkDrawings");
        mav.addObject("detailId",detailId);
        mav.addObject("assignee", SecurityContext.getCurrentUser().getLoginName());
        return mav;
    }

    @ApiOperation("比对图纸差异点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "baseDrawing", value = "原图纸文件", required = true, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "otherDrawing", value = "比对图纸文件",required = true, dataType = "MultipartFile")
    })
    @PostMapping(value = "/uploadCompareDrawing.do")
    public String uploadImgByte(HttpServletRequest request)throws Exception {

        String pointsString = codDrawingService.markDiffPoint(request,true,3,30,300);
        return pointsString;
    }

    @ApiOperation("比对图纸轮廓线")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "baseDrawing", value = "原图纸文件", required = true, dataType = "MultipartFile"),
            @ApiImplicitParam(name = "otherDrawing", value = "比对图纸文件",required = true, dataType = "MultipartFile")
    })
    @PostMapping(value = "/drawingContours.do")
    public String drawingContours(HttpServletRequest request) throws Exception{

        String pointsString = codDrawingService.compareDrawingContours(
                request,true,30);
        return pointsString;
    }



}
