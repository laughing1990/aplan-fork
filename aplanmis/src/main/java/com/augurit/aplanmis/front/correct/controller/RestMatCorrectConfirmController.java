package com.augurit.aplanmis.front.correct.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.vo.MatCorrectConfirmVo;
import com.augurit.aplanmis.front.correct.service.RestMatCorrectConfirmService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@RestController
@RequestMapping("/rest/matCorrectConfirm")
@Slf4j
@Api(value = "材料补正待确认")
public class RestMatCorrectConfirmController {
    @Autowired
    private RestMatCorrectConfirmService matCorrectConfirmService;

    @RequestMapping("/index.html")
    @ApiOperation(value = "跳转到补正待确认列表")
    public ModelAndView goConfirmedIndex(){
        ModelAndView view = new ModelAndView("matCorrect/stayConfirmIndex");
        view.addObject("currentOrgId",SecurityContext.getCurrentOrgId());
        return view;
    }
    @PostMapping("/search")
    @ApiOperation(value = "查询补正待确认列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value ="当前页码",defaultValue = "1",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "pageSize" ,value = "每页数据量" ,defaultValue = "10",required = true,dataType = "int",paramType = "query"),
            @ApiImplicitParam(name = "keyword" ,value = "查询关键字" ,required = true , dataType = "string" ,paramType = "query")
    })
    public ResultForm search(int pageNum,int pageSize,String keyword ){
        try {
            Page page = new Page();
            page.setPageNum(pageNum);
            page.setPageSize(pageSize);
            PageInfo<MatCorrectConfirmVo> pageList = matCorrectConfirmService.searchStayMattCorrect(page,keyword);
            return new ContentResultForm<>(true,pageList,"查询待确认补正列表成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false,new PageInfo<MatCorrectConfirmVo>(new ArrayList<>()),"查询待确认补正列表失败");
        }
    }
}
