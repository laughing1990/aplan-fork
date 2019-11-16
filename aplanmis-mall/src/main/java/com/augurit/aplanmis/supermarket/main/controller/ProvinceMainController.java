package com.augurit.aplanmis.supermarket.main.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Api(description = "中介超市省首页相关接口")
@RequestMapping("/supermarket/main")
@RestController
public class ProvinceMainController {

    //==============================省首页跳转  start ===================================
    @ApiOperation(value = "省中介超市首页", notes = "省中介超市首页")
    @GetMapping("/province/index.html")
    public ModelAndView provinceMainIndex() throws Exception {
        return new ModelAndView("zjcs/provincePage/index.html");
    }

    @ApiOperation(value = "中介超市首页尾部页面", notes = "中介超市首页尾部页面")
    @GetMapping("/province/footer.html")
    public ModelAndView provinceFooterIndex() throws Exception {
        return new ModelAndView("zjcs/common/footer");
    }
    //==============================省首页跳转  end ===================================

}
