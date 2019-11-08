package com.augurit.aplanmis.front.supermarket;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/market")
@RestController
@Api(tags = "菜单入口")
public class SuperMarketPageIndex {

    /**
     * 中介事项申报页
     *
     * @return
     */
    @GetMapping("/agentApplyIndex.html")
    @ApiOperation("菜单-中介事项申报页")
    public ModelAndView agentItemIndex(String itemVerId) {
        ModelAndView modelAndView = new ModelAndView("supermarket/applyIndex");
        modelAndView.addObject("itemVerId", itemVerId);
        return modelAndView;
    }
}
