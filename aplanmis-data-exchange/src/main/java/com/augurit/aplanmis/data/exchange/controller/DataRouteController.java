package com.augurit.aplanmis.data.exchange.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.data.exchange.service.aplanmis.AplanmisDataService;
import com.augurit.aplanmis.data.exchange.vo.ApplyProjTransVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 数据传输路径
 *
 * @author yinlf
 * @Date 2019/11/8
 */
@RestController
@RequestMapping("/data/route")
public class DataRouteController {

    @Autowired
    AplanmisDataService aplanmisDataService;

    @Autowired
    AeaProjInfoService aeaProjInfoService;

    @GetMapping("/index")
    public ModelAndView index(){
        return new ModelAndView("route/index");
    }

    /**
     * 申报项目总体情况
     * */
    @GetMapping("/apply_proj/all_count")
    public ResultForm getAplanmisProjAllCount(){
        ApplyProjTransVo applyProjTransVo = new ApplyProjTransVo();
        Long applyProjCount = aplanmisDataService.getAplanmisProjCount();
        applyProjTransVo.setApplyProjCount(applyProjCount);
        Long nonAcceptAplanmisProjCount = aplanmisDataService.getNonAcceptAplanmisProjCount();
        applyProjTransVo.setNonAcceptCount(nonAcceptAplanmisProjCount);
        Long aplanmisProjCountHasItemStateLog = aplanmisDataService.getAplanmisProjCountHasItemStateLog();
        applyProjTransVo.setHasBlxxxxCount(aplanmisProjCountHasItemStateLog);
        Long onlyA2ProjCount = aplanmisDataService.getOnlyA2ProjCount();
        applyProjTransVo.setOnlyA2Count(onlyA2ProjCount);
        return new ContentResultForm<>(true,applyProjTransVo);
    }

    /**
    * 申报项目数
    * */
    @GetMapping("/aplanmis/apply_proj/count")
    public ResultForm getAplanmisProjCount(){
        Long projCount = aplanmisDataService.getAplanmisProjCount();
        return new ContentResultForm<>(true,projCount);
    }

    /**
     * 申报项目列表
     * */
    @GetMapping("/aplanmis/apply_proj/list")
    public ResultForm findAplanmisProjList(){
        List<String> aplanmisProjLocalCodeList = aplanmisDataService.findAplanmisProjLocalCodeList();
        List<AeaProjInfo> projs = aeaProjInfoService.getProjListAndChildProjsByParent(aplanmisProjLocalCodeList.toArray(new String[aplanmisProjLocalCodeList.size()]));
        return new ContentResultForm<>(true,projs);
    }

    /**
     * 未受理项目数
     * */
    @GetMapping("/aplanmis/apply_proj/count/non_accept")
    public ResultForm getNonAcceptAplanmisProjCount(){
        Long projCount = aplanmisDataService.getNonAcceptAplanmisProjCount();
        return new ContentResultForm<>(true,projCount);
    }

    /**
     * 申报项目列表
     * */
    @GetMapping("/aplanmis/apply_proj/list/non_accept")
    public ResultForm findNonAcceptAplanmisProjList(){
        List<String> aplanmisProjLocalCodeList = aplanmisDataService.findNonAcceptAplanmisProjLocalCodeList();
        List<AeaProjInfo> projs = aeaProjInfoService.getProjListAndChildProjsByParent(aplanmisProjLocalCodeList.toArray(new String[aplanmisProjLocalCodeList.size()]));
        return new ContentResultForm<>(true,projs);
    }

    /**
     * 视图中有办理信息的项目数
     * */
    @GetMapping("/aplanmis/apply_proj/count/has_blxxxx")
    public ResultForm getAplanmisProjCountHasItemStateLog(){
        Long projCount = aplanmisDataService.getAplanmisProjCountHasItemStateLog();

        return new ContentResultForm<>(true,projCount);
    }

    /**
     * 申报项目列表
     * */
    @GetMapping("/aplanmis/apply_proj/list/has_blxxxx")
    public ResultForm findAplanmisProjListHasItemStateLog(){
        List<String> aplanmisProjLocalCodeList = aplanmisDataService.findAplanmisProjLocalCodeListHasItemStateLog();
        List<AeaProjInfo> projs = aeaProjInfoService.getProjListAndChildProjsByParent(aplanmisProjLocalCodeList.toArray(new String[aplanmisProjLocalCodeList.size()]));
        return new ContentResultForm<>(true,projs);
    }

    /**
     * 只申报预先服务协同的项目数
     * */
    @GetMapping("/aplanmis/apply_proj/count/only_a2")
    public ResultForm getOnlyA2ProjCount(){
        Long projCount = aplanmisDataService.getOnlyA2ProjCount();
        return new ContentResultForm<>(true,projCount);
    }

    /**
     * 申报项目列表
     * */
    @GetMapping("/aplanmis/apply_proj/list/only_a2")
    public ResultForm getOnlyA2ProjList(){
        List<String> aplanmisProjLocalCodeList = aplanmisDataService.getOnlyA2ProjLocalCodeList();
        List<AeaProjInfo> projs = aeaProjInfoService.getProjListAndChildProjsByParent(aplanmisProjLocalCodeList.toArray(new String[aplanmisProjLocalCodeList.size()]));
        return new ContentResultForm<>(true,projs);
    }


    /**
     * 已受理项目数
     * */

    /**
     * 入库失败
     * */

    /**
     * 入库项目数
     * */
}
