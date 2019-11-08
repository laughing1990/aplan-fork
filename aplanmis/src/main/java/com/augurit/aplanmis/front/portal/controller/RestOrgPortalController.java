package com.augurit.aplanmis.front.portal.controller;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.front.portal.service.OrgPortalService;
import com.augurit.aplanmis.front.portal.vo.ApplyCountVo;
import com.augurit.aplanmis.front.portal.vo.ItemCountVo;
import com.augurit.aplanmis.front.queryView.service.ConditionalQueryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "委办局人员首页相关接口")
@RestController
@RequestMapping("/rest/org/portal")
@Slf4j
public class RestOrgPortalController {
    @Autowired
    private OrgPortalService orgPortalService;

    @Autowired
    private ConditionalQueryService conditionalQueryService;

    private static String ORG_PORTAL_INDEX = "effectSupervision/assignPeopleIndex";

    @RequestMapping("/index")
    @ApiIgnore
    public ModelAndView index(){
        return new ModelAndView(ORG_PORTAL_INDEX);
    }


    @RequestMapping("/listUnConfirmItemCorrect")
    @ApiOperation("补正待确认列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "分页参数：当前页面", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "分页参数：页面大小", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String")
    })
    public ContentResultForm listUnConfirmItemCorrect(String keyword, int pageNum, int pageSize) {
        try {
            ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
            conditionalQueryRequest.setKeyword(keyword);
            Page page = new Page().setPageNum(pageNum).setPageSize(pageSize);
            PageInfo dismissedApply = conditionalQueryService.listUnConfirmItemCorrect(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @RequestMapping("/listHandledItem")
    @ApiOperation("我经办办件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "分页参数：当前页面", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "分页参数：页面大小", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "sort", value = "按时间排序", required = false, dataType = "String")
    })
    public ContentResultForm listHandledItem(String keyword, int pageNum, int pageSize, String sort) {
        try {
            ConditionalQueryRequest conditionalQueryRequest = new ConditionalQueryRequest();
            conditionalQueryRequest.setHandler(true);
            conditionalQueryRequest.setEntrust(false);
            conditionalQueryRequest.setKeyword(keyword);
            if (StringUtils.isBlank(sort)) {
                sort = "asc";
            }
            Page page = new Page().setPageNum(pageNum).setPageSize(pageSize).setOrderBy(" applyTime " + sort);
            PageInfo partList = conditionalQueryService.listPartsByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, partList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }



    @GetMapping("/count")
    @ApiOperation("委办局首页工作台统计数量")
    public ContentResultForm<ApplyCountVo> countApply(){
        try {
            ItemCountVo vo = orgPortalService.countApply();
            return  new ContentResultForm(true,vo,"工作台数据查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
           return new ContentResultForm(false,null,"查询失败！");
        }
    }

    @RequestMapping("/listDaiBan")
    @ApiOperation("委办局首页待办列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "分页参数：当前页面",  required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "分页参数：页面大小", required = true, dataType = "Integer")
    })
    public ContentResultForm listDaiBan(String keyword, int pageNum,int pageSize , String sort){
        try {
            PageInfo pageInfo = orgPortalService.listDaiBan(pageNum,pageSize,keyword,sort);
            return new ContentResultForm(true,pageInfo,"查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
           return new ContentResultForm(false,null,"查询失败"+e.getMessage());
        }
    }

    @GetMapping("/getDicViewCode")
    @ApiOperation("委办局首页工作台对应视图编号（数据字典配置）")
    public ContentResultForm<List<BscDicCodeItem>> getDicViewCode(){
        ContentResultForm<List<BscDicCodeItem>> resultForm = new ContentResultForm<>(false);
        try {
            List<BscDicCodeItem> items = orgPortalService.getDicViewCode("ORG_WORK_SPACE_VIEW_CODE");
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

}
