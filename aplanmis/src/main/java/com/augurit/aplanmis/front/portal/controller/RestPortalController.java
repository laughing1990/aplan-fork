package com.augurit.aplanmis.front.portal.controller;

import com.augurit.agcloud.bpm.common.domain.ActStoView;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.front.portal.service.PortalService;
import com.augurit.aplanmis.front.portal.vo.EchartsBarVo;
import com.augurit.aplanmis.front.portal.vo.ItemAndApplyCountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * @author yinlf
 * @Date 2019/8/9
 */
@Api(tags = "首页相关接口")
@RestController
@RequestMapping("/rest/portal")
@Slf4j
public class RestPortalController {

    @Autowired
    PortalService portalService;

    private static String INTEGRATION_INDEX = "aplanmis/front/integration/integrationIndex";
    private static String WINDOWPN_INDEX = "effectSupervision/windowPeopleIndex";//窗口角色权限首页


    @RequestMapping("/index")
    @ApiIgnore
    public ModelAndView index(){
        //判断角色权限，显示不同首页
        if(portalService.isWindowPn()){
            return new ModelAndView(WINDOWPN_INDEX);
        }
        return new ModelAndView(INTEGRATION_INDEX);
    }

    @GetMapping("/count")
    @ApiOperation("统计办件和申报数量")
    public ContentResultForm<ItemAndApplyCountVo> countItemAndApply(){
        try {
            ItemAndApplyCountVo itemAndApplyCountVo = portalService.countItemAndApply();
            return new ContentResultForm<ItemAndApplyCountVo>(true,itemAndApplyCountVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "统计办件和申报数量异常");
        }
    }

    @GetMapping("/total/item/theme")
    @ApiOperation("按项目类型查询办件数量")
    public ContentResultForm<EchartsBarVo> findTotalItemBaseOnTheme(){
        try {
            EchartsBarVo echartsBarVo = portalService.findTotalItemBaseOnTheme();
            return new ContentResultForm<>(true,echartsBarVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "按项目类型查询办件数量异常");
        }
    }

    @GetMapping("/total/item/standard_theme")
    @ApiOperation("按标准项目类型查询办件数量")
    public ContentResultForm<EchartsBarVo> findTotalItemBaseOnStandardTheme(){
        try {
            EchartsBarVo echartsBarVo = portalService.findTotalItemBaseOnStandardTheme();
            return new ContentResultForm<>(true,echartsBarVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "按标准项目类型查询办件数量异常");
        }
    }

    @GetMapping("/total/use_time/stage")
    @ApiOperation("统计阶段用时")
    public ContentResultForm<EchartsBarVo> findStageUseDay(){
        try {
            EchartsBarVo echartsBarVo = portalService.findStageUseDay();
            return new ContentResultForm<>(true,echartsBarVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "统计阶段用时异常");
        }
    }

    @GetMapping("/total/use_time/standard_stage")
    @ApiOperation("统计标准阶段用时")
    public ContentResultForm<EchartsBarVo> findStandardStageUseDay(){
        try {
            EchartsBarVo echartsBarVo = portalService.findStandardStageUseDay();
            return new ContentResultForm<>(true,echartsBarVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "统计标准阶段用时异常");
        }
    }

    /**
     * 集成首页获取视图信息（编号、ID）
     */
    @RequestMapping("/view_code_info")
    public Map<String,ActStoView> getDicViewCodeInfo(){
        return portalService.getDicViewCodeInfo();
    }



}
