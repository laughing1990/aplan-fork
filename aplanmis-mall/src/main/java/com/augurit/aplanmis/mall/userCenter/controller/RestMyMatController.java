package com.augurit.aplanmis.mall.userCenter.controller;


import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestMyMatService;
import com.github.pagehelper.PageInfo;
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
@RequestMapping("rest/user/mat")
@Api(value = "我的材料列表" ,tags = "法人空间 --> 我的材料库相关接口")
public class RestMyMatController {

    @Autowired
    RestMyMatService restMyMatService;

    @GetMapping("toMyMatListPage")
    @ApiOperation("跳转法人空间我的材料列表相关页面")
    public String toMyMatListPage(){
        return "";
    }

    @GetMapping("list")
    @ApiOperation("获取我的材料列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "int"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "int"),
            @ApiImplicitParam(value = "搜索关键字",name = "keyword",required = false,dataType = "string")})
    public PageInfo<BscAttFileAndDir> getMyMatList(HttpServletRequest request, String keyword, int pageNum, int pageSize){
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                return restMyMatService.getMyMatListByUser("",loginInfo.getUserId(),keyword,pageNum,pageSize);
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                return restMyMatService.getMyMatListByUser(loginInfo.getUnitId(),loginInfo.getUserId(),keyword,pageNum,pageSize);
            }else{//企业
                return restMyMatService.getMyMatListByUser(loginInfo.getUnitId(),"",keyword,pageNum,pageSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
