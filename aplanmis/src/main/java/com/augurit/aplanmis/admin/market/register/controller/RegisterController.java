package com.augurit.aplanmis.admin.market.register.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/supermarket/register")
public class RegisterController {

    @RequestMapping("/index.html")
    public ModelAndView registerIndex() {
        return new ModelAndView("admin/supermarket/register/register-index");
    }

    @RequestMapping("/detail.html")
    public ModelAndView registerDetail() {
        return new ModelAndView("admin/supermarket/register/register_detail");
    }


}
