package com.augurit.aplanmis.front.subject.linkman.controller;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/rest/linkman/lib")
@Slf4j
@Api(tags = "菜单入口")
public class LinkmanLibraryController {

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @GetMapping("/index")
    @ApiOperation("全局联系人库-主页列表")
    public ModelAndView index() {
        return new ModelAndView("global/linkmanview");
    }

    @GetMapping("/detail")
    @ApiOperation("/联系人详情")
    public ModelAndView detail(@RequestParam(required = false) String linkmanInfoId) {
        ModelAndView modelAndView = new ModelAndView("global/linkmanInfoDetail");
        AeaLinkmanInfo aeaLinkmanInfo = null;
        if (StringUtils.isNotBlank(linkmanInfoId)) {
            aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);
        }
        modelAndView.addObject("aeaLinkmanInfo", aeaLinkmanInfo);
        return modelAndView;
    }
}
