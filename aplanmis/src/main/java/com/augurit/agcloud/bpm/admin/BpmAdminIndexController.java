package com.augurit.agcloud.bpm.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * ui首页地址controller
 */
@Controller
@RequestMapping("/bpm/admin")
public class BpmAdminIndexController {
    /**
     * 视图管理
     * @return
     */
    @RequestMapping(value = "/view/index.html")
    public ModelAndView view(ModelMap modelMap){
        modelMap.addAttribute("isInnerView","0");
        return new ModelAndView("agcloud/bpm/admin/view/index");
    }


    /**
     * 公共视图管理
     * @return
     */
    @RequestMapping(value = "/view/public/index.html")
    public ModelAndView publicView(ModelMap modelMap){
        modelMap.addAttribute("isInnerView","1");
        return new ModelAndView("agcloud/bpm/admin/view/index");
    }

    /**
     * 元素管理
     * @return
     */
    @RequestMapping(value = "/element/index.html")
    public ModelAndView element(){
        return new ModelAndView("agcloud/bpm/admin/element/index");
    }


    /**
     * 结论管理
     * @return
     */
    @RequestMapping(value = "/conclusion/index.html")
    public ModelAndView conclusion( ){

        return new ModelAndView("agcloud/bpm/admin/conclusion/index");
    }


    /**
     * 意见管理管理
     * @return
     */
    @RequestMapping(value = "/opinion/index.html")
    public ModelAndView opinion( ){

        return new ModelAndView("agcloud/bpm/admin/opinion/index");
    }

    /**
     * 表单管理
     * @return
     */
    @RequestMapping(value = "/form/index.html")
    public ModelAndView form(){
        return new ModelAndView("agcloud/bpm/admin/form/index");
    }

    /**
     * 流程模版管理
     * @return
     */
    @RequestMapping(value = "/template/index.html")
    public ModelAndView template(){
        return new ModelAndView("agcloud/bpm/admin/template/index");
    }

    @RequestMapping(value = "/template/authConfigForm.html")
    public ModelAndView authConfigForm(){
        return new ModelAndView("agcloud/bpm/admin/template/authConfigForm");
    }

    /**
     * 元数据管理
     * @return*/
    @RequestMapping(value = "/meta/metadata/index.html")
    public ModelAndView metadata(){
        return new ModelAndView("agcloud/meta/metadata/metadata");
    }

    /**
     * 界面框架管理
     * @return
     */
    @RequestMapping(value = "/sto/index.html")
    public ModelAndView sto(){
        return new ModelAndView("agcloud/bpm/admin/sto/index");
    }

    /**
     * 布局管理
     * @return
     */
    @RequestMapping(value = "/layout/index.html")
    public ModelAndView layout(String techId,ModelMap modelMap){
        modelMap.addAttribute("techId",techId);
        return new ModelAndView("agcloud/bpm/admin/sto/layout");
    }

    /**
     * 部件管理
     * @return
     */
    @RequestMapping(value = "/widget/index.html")
    public ModelAndView widget(String techId,ModelMap modelMap){
        modelMap.addAttribute("techId",techId);
        return new ModelAndView("agcloud/bpm/admin/sto/widget");
    }
    /**
     * 界面框架管理
     * @return
     */
    @RequestMapping(value = "/event/index.html")
    public ModelAndView event(String techId,ModelMap modelMap){
        modelMap.addAttribute("techId",techId);
        return new ModelAndView("agcloud/bpm/admin/sto/event");
    }

    /**
     * 时限计算规则管理
     * @return
     */
    @RequestMapping(value = "/timerule/index.html")
    public ModelAndView timerule(ModelMap modelMap){
        return new ModelAndView("agcloud/bpm/admin/sto/timerule");
    }

    /**
     * 设计器首页
     * @return
     */
    @RequestMapping("/modeler/index.html")
    public ModelAndView modelerIndex(){
        return new ModelAndView("agcloud/bpm/admin/modeler/index");
    }

    /**
     * 监听器管理
     * @return
     */
    @RequestMapping(value = "/listener/index.html")
    public ModelAndView listener(ModelMap modelMap){
        return new ModelAndView("agcloud/bpm/admin/listener/index");
    }
}
