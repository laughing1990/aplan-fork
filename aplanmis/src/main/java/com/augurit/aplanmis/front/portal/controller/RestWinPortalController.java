package com.augurit.aplanmis.front.portal.controller;

import com.augurit.agcloud.bpm.common.domain.ActStoView;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.front.portal.service.WinPortalService;
import com.augurit.aplanmis.front.portal.vo.ApplyCountVo;
import com.augurit.aplanmis.front.queryView.service.ConditionalQueryService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "窗口人员首页相关接口")
@RestController
@RequestMapping("/rest/win/portal")
@Slf4j
public class RestWinPortalController {

    @Autowired
    WinPortalService winPortalService;

    @Autowired
    private ConditionalQueryService conditionalQueryService;

    private static String WIN_PORTAL_INDEX = "effectSupervision/windowPeopleIndex";

    @RequestMapping("/index")
    @ApiIgnore
    public ModelAndView index(){
        return new ModelAndView(WIN_PORTAL_INDEX);
    }

    @RequestMapping("/listDaiBan")
    @ApiOperation("窗口首页待办列表")
    public ContentResultForm listDaiBan(ConditionalQueryRequest conditionalQueryRequest, PageParam page){
        ContentResultForm<PageInfo> resultForm = new ContentResultForm<>(false);
        try {
            PageInfo waitDoTasks = conditionalQueryService.listWaitDoTasksByPage(conditionalQueryRequest, page.convertPage());
            resultForm.setSuccess(true);
            resultForm.setMessage("查询成功。");
            resultForm.setContent(waitDoTasks);
            return resultForm;
        } catch (Exception e) {
            e.printStackTrace();
            resultForm.setMessage("查询待办列表出现错误。");
            return resultForm;
        }
    }

    @RequestMapping("/listWangShangDaiYuShen")
    @ApiOperation("窗口首页网上待预审列表")
    public ContentResultForm listWangShangDaiYuShen(ConditionalQueryRequest conditionalQueryRequest, PageParam page){
        ContentResultForm resultForm = new ContentResultForm<>(false);
        try {
            PageInfo preliminaryTasks = conditionalQueryService.listPreliminaryTasksByPage(conditionalQueryRequest, page.convertPage());
            resultForm.setSuccess(true);
            resultForm.setMessage("查询成功。");
            resultForm.setContent(preliminaryTasks);
            return resultForm;
        } catch (Exception e) {
            e.printStackTrace();
            resultForm.setMessage("查询网上待预审列表出现错误。");
            return resultForm;
        }
    }

    @RequestMapping("/listBuQuanDaiQueRen")
    @ApiOperation("窗口首页补全待确认列表")
    public ContentResultForm listBuQuanDaiQueRen(ConditionalQueryRequest conditionalQueryRequest, PageParam page){
        ContentResultForm resultForm = new ContentResultForm<>(false);
        try {
            PageInfo needConfirmCompletedApply = conditionalQueryService.listNeedConfirmCompletedApplyByPage(conditionalQueryRequest, page.convertPage());
            resultForm.setSuccess(true);
            resultForm.setMessage("查询成功。");
            resultForm.setContent(needConfirmCompletedApply);
            return resultForm;
        } catch (Exception e) {
            e.printStackTrace();
            resultForm.setMessage("查询补全待确认列表出现错误。");
            return resultForm;
        }
    }

    @GetMapping("/count")
    @ApiOperation("窗口首页工作台统计数量")
    public ContentResultForm<ApplyCountVo> countApply(){
        ContentResultForm<ApplyCountVo> resultForm = new ContentResultForm<>(false);
        try {
            ApplyCountVo vo = winPortalService.countApply();
            resultForm.setSuccess(true);
            resultForm.setMessage("请求成功。");
            resultForm.setContent(vo);
            return resultForm;
        } catch (Exception e) {
            e.printStackTrace();
            resultForm.setMessage("统计数量出现错误。" + e.getMessage());
            return resultForm;
        }
    }

    @GetMapping("/getDicViewCode")
    @ApiOperation("窗口首页工作台对应视图编号（数据字典配置）")
    public ContentResultForm<List<BscDicCodeItem>> getDicViewCode(){
        ContentResultForm<List<BscDicCodeItem>> resultForm = new ContentResultForm<>(false);
        try {
            List<BscDicCodeItem> items = winPortalService.getDicViewCode("WIN_WORK_SPACE_VIEW_CODE");
            resultForm.setSuccess(true);
            resultForm.setMessage("请求成功。");
            resultForm.setContent(items);
            return resultForm;
        } catch (Exception e) {
            e.printStackTrace();
            resultForm.setMessage("查询工作台对应视图编号出现错误。");
            return resultForm;
        }
    }

    @RequestMapping("/queryView")
    @ApiOperation("根据视图配置名称查询视图信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "视图名称", required = true, dataType = "String"),

    })
    public ResultForm getViewId(String content){
        try {
            ActStoView view =  winPortalService.getViewByContent(content);
            return new ContentResultForm<>(true,view,"查询视图成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false,null,"查询失败"+e.getMessage());
        }
    }
}
