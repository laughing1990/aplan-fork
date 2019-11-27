package com.augurit.aplanmis.admin.market.register.controller;


import com.augurit.aplanmis.admin.market.register.service.RegisterService;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/supermarket/register")
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @RequestMapping("/index.html")
    @ApiOperation(value = "列表页面")
    public ModelAndView registerIndex() {
        return new ModelAndView("admin/supermarket/register/register_index");
    }

    @RequestMapping("/detail.html")
    @ApiOperation(value = "详情页面")
    public ModelAndView registerDetail() {
        return new ModelAndView("admin/supermarket/register/register_detail");
    }




}
