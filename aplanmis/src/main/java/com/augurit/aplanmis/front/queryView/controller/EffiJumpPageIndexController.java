package com.augurit.aplanmis.front.queryView.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 效能督查图标跳转页面controller
 */
@RequestMapping("/efficacyJump/page")
@RestController
@Api(tags = "效能督查图标跳转-菜单入口")
public class EffiJumpPageIndexController {

    @GetMapping("/queryItemToleranceAcceptIndex.html")
    @ApiOperation("菜单-容缺受理件")
    public ModelAndView itemToleranceAcceptIndex(@RequestParam(required = false ) boolean entrust ) {

        ModelAndView modelAndView = new ModelAndView("efficiencyJumpPage/orgJumpQueryItemToleranceAcceptIndex");
        modelAndView.addObject("entrust",entrust);
        return modelAndView;
    }

    /**
     * 待补正办件
     *
     * @return
     */
    @GetMapping("/queryNeedCorrectTasksIndex.html")
    @ApiOperation("菜单-待补正办件")
    public ModelAndView queryNeedCorrectTasksIndex(@RequestParam(required = false ) boolean entrust ) {
        ModelAndView modelAndView = new ModelAndView("efficiencyJumpPage/orgJumpQueryNeedCorrectTasksIndex");
        modelAndView.addObject("entrust",entrust);
        return modelAndView;
    }

    @GetMapping("/queryUnConfirmItemCorrectTasksIndex.html")
    @ApiOperation("菜单-补正待确认件")
    public ModelAndView queryUnConfirmItemCorrectTasksIndex(@RequestParam(required = false ) boolean entrust) {
        ModelAndView modelAndView = new ModelAndView("efficiencyJumpPage/orgJumpQueryUnConfirmItemCorrectTasksIndex");
        modelAndView.addObject("entrust",entrust);
        return modelAndView;
    }

    /**
     * 特殊程序件
     *
     * @return
     */
    @GetMapping("/querySpecialProcedureTasksIndex.html")
    @ApiOperation("菜单-特殊程序件")
    public ModelAndView querySpecialProcedureTasksIndex(@RequestParam(required = false ) boolean entrust) {
        ModelAndView modelAndView = new ModelAndView("efficiencyJumpPage/orgJumpQuerySpecialProcedureTasksIndex");
        modelAndView.addObject("entrust",entrust);
        return modelAndView;
    }

    @GetMapping("/queryItemAgreeToleranceIndex.html")
    @ApiOperation("菜单-容缺通过件")
    public ModelAndView itemAgreeToleranceIndex(@RequestParam(required = false ) boolean entrust) {
        ModelAndView modelAndView = new ModelAndView("efficiencyJumpPage/orgJumpQueryItemAgreeToleranceIndex");
        modelAndView.addObject("entrust",entrust);
        return modelAndView;
    }

    @GetMapping("/queryItemDisgreeIndex.html")
    @ApiOperation("菜单-办结（不通过）-部门")
    public ModelAndView itemDisgreeIndex(@RequestParam(required = false) boolean entrust) {

        ModelAndView modelAndView = new ModelAndView("efficiencyJumpPage/orgJumpQueryItemDisgreeIndex");
        modelAndView.addObject("entrust",entrust);
        return modelAndView;
    }

    @GetMapping("/queryItemOutScopeIndex.html")
    @ApiOperation("菜单-不予受理(部门)")
    public ModelAndView itemOutScopeIndex(@RequestParam(required = false ) boolean entrust) {

        ModelAndView modelAndView = new ModelAndView("efficiencyJumpPage/orgJumpQueryItemOutScopeIndex");
        modelAndView.addObject("entrust",entrust);
        return modelAndView;
    }



}
