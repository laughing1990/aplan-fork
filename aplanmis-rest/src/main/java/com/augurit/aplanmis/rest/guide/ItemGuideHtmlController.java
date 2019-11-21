package com.augurit.aplanmis.rest.guide;

import com.augurit.agcloud.framework.util.StringUtils;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/rest/html5")
@Api("办事指南-html5页面入口")
public class ItemGuideHtmlController {

    @ApiOperation(value = "办事指南列表", notes = "办事指南列表")
    @GetMapping("/guide/list")
    public String list() {
        return "guide/list";
    }

    @ApiOperation(value = "办事指南-详情", notes = "办事指南详情")
    @GetMapping("/guide/detail")
    public String detail(@RequestParam String itemVerId, Model model) {
        Assert.state(StringUtils.isNotBlank(itemVerId), "itemVerId不能为空");

        model.addAttribute("itemVerId", itemVerId);
        return "guide/detail";
    }
}
