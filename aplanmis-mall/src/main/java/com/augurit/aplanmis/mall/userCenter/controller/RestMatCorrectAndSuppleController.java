package com.augurit.aplanmis.mall.userCenter.controller;


import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.dto.MatCorrectAndSuppleDto;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestMatCorrectAndSuppleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("rest/user")
@Api(value = "", tags = "法人空间 --> 材料补齐补全相关接口")
public class RestMatCorrectAndSuppleController {


    @Autowired
    RestMatCorrectAndSuppleService restMatCorrectAndSuppleService;


    @GetMapping("matCorrectAndSupply/list")
    @ApiOperation(value = "材料补正 --> 材料补齐补正列表查询接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "int"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "int"),
            @ApiImplicitParam(value = "搜索关键字",name = "keyword",required = false,dataType = "string")})
    public ContentResultForm<MatCorrectAndSuppleDto>getMatCorrectAndSuppleList(int pageNum, int pageSize, String keyword, HttpServletRequest request ){
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        if (loginInfoVo==null) return new ContentResultForm(false,"","获取登录信息失败");
        try {
            return new ContentResultForm(true,restMatCorrectAndSuppleService.getCorrectAndSuppleList(loginInfoVo.getUnitId(),loginInfoVo.getUserId(),keyword,pageNum,pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","查询出错");
        }
    }
}
