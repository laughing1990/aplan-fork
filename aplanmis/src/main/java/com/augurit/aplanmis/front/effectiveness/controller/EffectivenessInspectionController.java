package com.augurit.aplanmis.front.effectiveness.controller;

import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.EffectInspectForm;
import com.augurit.aplanmis.front.effectiveness.service.EffectInspectService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * 效能督查
 */
@RequestMapping("/effect/inspection")
@RestController
public class EffectivenessInspectionController {
    @Autowired
    private EffectInspectService effectInspectService;

    @RequestMapping("/index")
    public ModelAndView index(){
        return new ModelAndView("aplanmis/front/effect/index");
    }

    //预警办件
    @RequestMapping("/yuJingBanJian/page")
    public ResultForm yuJIngBanJianList(PageParam page,String keyword){
        PageInfo<EffectInspectForm> list = effectInspectService.yuJingBanJianList(page.convertPage(),keyword);

        return new ContentResultForm(true,list);
    }

    //预警办件数
    @RequestMapping("/yuJingBanJian/count")
    public ResultForm yuJIngBanJianCount(){
        Integer count = effectInspectService.yuJingBanJianCount();
        if(count==null)
            count = 0;
        return new ContentResultForm(true,count);
    }

    //预期办件
    @RequestMapping("/yuQiBanJian/page")
    public ResultForm yuQiBanJianList(PageParam page,String keyword){
        PageInfo<EffectInspectForm> list = effectInspectService.yuQiBanJianList(page.convertPage(),keyword);

        return new ContentResultForm(true,list);
    }

    //预期办件数
    @RequestMapping("/yuQiBanJian/count")
    public ResultForm yuQiBanJianBanJianCount(){
        Integer count = effectInspectService.yuQiBanJianCount();
        if(count==null)
            count = 0;
        return new ContentResultForm(true,count);
    }

    //网上待预审办件
    @RequestMapping("/wangShangDaiYuShen/page")
    public ResultForm wangShangDaiYuShenList(PageParam page,String keyword){
        PageInfo<EffectInspectForm> list = effectInspectService.wangShangDaiYuShenList(page.convertPage(),keyword);

        return new ContentResultForm(true,list);
    }

    //网上待预审办件数
    @RequestMapping("/wangShangDaiYuShen/count")
    public ResultForm wangShangDaiYuShenCount(){
        Integer count = effectInspectService.wangShangDaiYuShenCount();
        if(count==null)
            count = 0;
        return new ContentResultForm(true,count);
    }

    //材料补全
    @RequestMapping("/caiLiaoBuQuan/page")
    public ResultForm caiLiaoBuQuanBanJianList(PageParam page,String keyword){
        PageInfo<EffectInspectForm> list = effectInspectService.caiLiaoBuQuanBanJianList(page.convertPage(),keyword);

        return new ContentResultForm(true,list);
    }

    //材料补全数
    @RequestMapping("/caiLiaoBuQuan/count")
    public ResultForm caiLiaoBuQuanBanJianCount(){
        Integer count = effectInspectService.caiLiaoBuQuanBanJianCount();
        if(count==null)
            count = 0;
        return new ContentResultForm(true,count);
    }

    //待过程补正
    @RequestMapping("/daiGuoChengBuZheng/page")
    public ResultForm daiGuoChengBuZhengList(PageParam page,String keyword){
        //PageInfo<EffectInspectForm> list = effectInspectService.daiGuoChengBuZhengBanJianList(page.convertPage(), keyword);
        PageInfo<EffectInspectForm> list = new PageInfo<EffectInspectForm>(new ArrayList<>());
        return new ContentResultForm(true,list);
    }

    //待过程补正数
    @RequestMapping("/daiGuoChengBuZheng/count")
    public ResultForm daiGuoChengBuZhengBanJianCount(){
        //Integer count = effectInspectService.daiGuoChengBuZhengBanJianCount();
        //if(count==null)
        //    count = 0;
        Integer count = 0;
        if(count==null)
            count = 0;
        return new ContentResultForm(true,count);
    }

    //进入特别程序列表
    @RequestMapping("/jinRuTeShuChengXu/page")
    public ResultForm jinRuTeShuChengXuList(PageParam page){
        PageInfo<EffectInspectForm> list = new PageInfo<EffectInspectForm>(new ArrayList<>());
        return new ContentResultForm(true,list);
    }

    //进入特别程序数
    @RequestMapping("/jinRuTeShuChengXu/count")
    public ResultForm jinRuTeShuChengXuCount(){
        Integer count = 0;
        if(count==null)
            count = 0;
        return new ContentResultForm(true,count);
    }

    //办理不通过列表
    @RequestMapping("/banLiBuTongGuo/page")
    public ResultForm banLiBuTongGuoXuList(PageParam page){
        PageInfo<EffectInspectForm> list = new PageInfo<EffectInspectForm>(new ArrayList<>());
        return new ContentResultForm(true,list);
    }

    //办理不通过数
    @RequestMapping("/banLiBuTongGuo/count")
    public ResultForm banLiBuTongGuoCount(){
        Integer count = 0;
        if(count==null)
            count = 0;
        return new ContentResultForm(true,count);
    }
}
