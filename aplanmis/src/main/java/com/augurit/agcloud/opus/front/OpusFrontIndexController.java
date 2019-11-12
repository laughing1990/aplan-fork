package com.augurit.agcloud.opus.front;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.opus.front.config.OpusFontIndexConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/")
@RestController(value = "FrontIndexController")
public class OpusFrontIndexController {

    @Autowired
    private OpusFontIndexConfig opusFontIndexConfig;

    /**
     * 用户端首页
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("opus/front/index.html")
    public ModelAndView index(ModelMap modelMap){

//        if(opusFontIndexConfig.getLogoUrl()!=null)
//            modelMap.put("logoUrl",opusFontIndexConfig.getLogoUrl());
//        if(opusFontIndexConfig.getTitle()!=null)
//            modelMap.put("title",opusFontIndexConfig.getTitle());
//        return new ModelAndView("agcloud/opus/front/common/header",modelMap);
        return new ModelAndView("redirect:/");
    }

    /**
     * 用户端首页
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/")
    public ModelAndView index_default(ModelMap modelMap){

        if(opusFontIndexConfig.getLogoUrl()!=null) {
            modelMap.put("logoUrl", opusFontIndexConfig.getLogoUrl());
        }
        if(opusFontIndexConfig.getTitle()!=null) {
            modelMap.put("title", opusFontIndexConfig.getTitle());
        }
        return new ModelAndView("agcloud/framework/ui-schemes/default/index",modelMap);
    }

    /**
     * 用户端首页
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("opus/front/blue/index.html")
    public ModelAndView index_blue(ModelMap modelMap){

        if(opusFontIndexConfig.getLogoUrl()!=null) {
            modelMap.put("logoUrl", opusFontIndexConfig.getLogoUrl());
        }
        if(opusFontIndexConfig.getTitle()!=null) {
            modelMap.put("title", opusFontIndexConfig.getTitle());
        }
        return new ModelAndView("agcloud/framework/ui-schemes/dark-blue/index",modelMap);
    }

    @RequestMapping("opus/front/config")
    public ResultForm fronIndexConfig() {

        if(opusFontIndexConfig!=null){
            return new ContentResultForm(true,opusFontIndexConfig);
        }
        return new ResultForm(false,"门户配置信息为空！");
    }
}
