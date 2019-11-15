package com.augurit.aplanmis.front.blueprint.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author ZhangXinhui
 * @date 2019/10/8 008 9:37
 * @desc
 **/
@RestController
@Api(value = "一张蓝图", tags = "一张蓝图")
@RequestMapping("/blueprint")
@Slf4j
public class AeaBlueprintController {

    @Value(value = "${aea.blueprint.user}")
    private String user;
    @Value(value = "${aea.blueprint.map.url}")
    private String mapUrl;
    @Value(value = "${aea.blueprint.domain}")
    private String domain;

    @RequestMapping(value = "/mapPage.do", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("一张蓝图地图页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xmdm", value = "项目代码", required = true, dataType = "String")
    })
    public ModelAndView mapPage(String xmdm) {
        String url = mapUrl + "?userName=" + user;
        ModelAndView modelAndView = new ModelAndView("blueprint/map_page");
        modelAndView.addObject("url", url);
        modelAndView.addObject("domain", domain);
        modelAndView.addObject("xmdm", xmdm);
        return modelAndView;
    }
}
