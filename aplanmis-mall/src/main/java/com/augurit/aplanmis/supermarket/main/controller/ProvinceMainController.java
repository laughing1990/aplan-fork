package com.augurit.aplanmis.supermarket.main.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.supermarket.main.service.ProvinceMainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Api(description = "中介超市省首页相关接口")
@RequestMapping("/supermarket/main/province")
@RestController
public class ProvinceMainController {

    @Autowired
    private ProvinceMainService provinceMainService;

    //==============================省首页跳转  start ===================================
    @ApiOperation(value = "省中介超市首页", notes = "省中介超市首页")
    @GetMapping("/index.html")
    public ModelAndView provinceMainIndex() throws Exception {
        return new ModelAndView("zjcs/provincePage/index.html");
    }

    @ApiOperation(value = "中介超市首页尾部页面", notes = "中介超市首页尾部页面")
    @GetMapping("/footer.html")
    public ModelAndView provinceFooterIndex() throws Exception {
        return new ModelAndView("zjcs/common/footer");
    }

    //==============================省首页跳转  end ===================================
    @ApiOperation(value = "获取省首页展示数据", notes = "获取省首页展示数据。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "查询类型，D 天,M月,Y 年 A 所有", required = true, dataType = "String")
    })
    @GetMapping(value = "/getProvinceIndexData")
    public ResultForm getIndexData(String queryType) {
        try {
            return new ContentResultForm(true, provinceMainService.getProvinceIndexData(queryType));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }
}
