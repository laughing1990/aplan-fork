package com.augurit.aplanmis.front.subject.unit.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
@RequestMapping("/rest/applicant/lib")
@Api(tags = "菜单入口")
public class ApplicantLibraryController {

    public static final String BACKEND_APPLICANT_GLOBAL_VIEW = "global/applicantview";//全局单位库

    @GetMapping("/index")
    @ApiOperation("全局单位库-主页列表")
    public ModelAndView index() {
//        return new ModelAndView("ui-jsp/global_view/global_applicant/global_applicant_view");
        return new ModelAndView(BACKEND_APPLICANT_GLOBAL_VIEW);
    }
}
