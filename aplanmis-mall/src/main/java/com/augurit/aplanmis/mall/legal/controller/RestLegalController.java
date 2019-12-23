package com.augurit.aplanmis.mall.legal.controller;


import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaServiceLegal;
import com.augurit.aplanmis.common.service.admin.legal.AeaServiceLegalAdminService;
import com.augurit.aplanmis.mall.legal.service.RestLegalServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("rest/legal")
@Api(value = "法律法规",tags = "法律法规 --> 相关接口")
public class RestLegalController {

    @Autowired
    RestLegalServiceImpl restLegalService;
    @Autowired
    IBscAttService bscAttService;
    @Autowired
    AeaServiceLegalAdminService legalAdminService;

    @GetMapping("/list")
    @ApiOperation(value = "法律法规 --> 法律法规列表查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键词",name = "keyword",required = true,dataType = "string"),
            @ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "int"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "int")})

    public ContentResultForm getLegalListByKeyword(String keyword, int pageNum, int pageSize){
        try {
            return new ContentResultForm(true,restLegalService.getLegalListByKeyword(keyword,pageNum,pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"查询失败");
        }
    }


    @ApiOperation(value = "通过id获取法律法规数据", notes = "通过id获取法律法规数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/att/list", method = {RequestMethod.GET, RequestMethod.POST})
    public AeaServiceLegal getAeaServiceLegal(String id) throws Exception {

        if (StringUtils.isNotBlank(id)) {
            AeaServiceLegal legal = legalAdminService.getAeaServiceLegalById(id);
            if(legal!=null){
                String orgId = SecurityContext.getCurrentOrgId();
                List<BscAttForm> attList = bscAttService.listAttLinkAndDetailNoPage("AEA_SERVICE_LEGAL", "SERVICE_LEGAL_ATT", id, null, orgId, null);
                legal.setLegalAtts((attList == null||attList.size()==0) ?new ArrayList<>(0):attList.subList(0,1));
                legal.setServiceLegalAttCount(attList == null ? 0L : attList.size());
            }
            return legal;
        } else {
            return new AeaServiceLegal();
        }
    }
}
