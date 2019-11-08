package com.augurit.aplanmis.front.subject.linkman.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/rest/linkman/lib")
@Slf4j
@Api(tags = "菜单入口")
public class LinkmanLibraryController {

    @GetMapping("/index")
    @ApiOperation("全局联系人库-主页列表")
    public ModelAndView index() {
//        return new ModelAndView("ui-jsp/global_view/global_linkman/global_linkman_view");
        return new ModelAndView("global/linkmanview");
    }
}
