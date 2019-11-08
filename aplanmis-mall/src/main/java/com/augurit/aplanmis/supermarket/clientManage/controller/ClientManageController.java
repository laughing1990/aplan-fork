package com.augurit.aplanmis.supermarket.clientManage.controller;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.supermarket.clientManage.service.ClientManageService;
import com.augurit.aplanmis.supermarket.clientManage.vo.ClientManageVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/aea/supermarket/clientManage")
@RestController
@Api(value = "委托人管理相关接口", description = "委托人管理相关接口")
public class ClientManageController {

    @Autowired
    private ClientManageService clientManageService;

    @GetMapping("/getLinkmanListByCond")
    @ApiOperation(value = "根据条件查询单位联系人列表", notes = "根据条件查询单位联系人列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "查询页", required = true, dataType = "int")})
    public ResultForm getLinkmanListByCond(ClientManageVo clienManageVo, int pageSize, int pageNum) {
        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
        return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(clientManageService.getLinkmanListByCond(clienManageVo, page))));
    }

    @PostMapping("/putLinkman")
    @ApiOperation(value = "绑定管理员，解绑人员", notes = "绑定管理员，解绑人员", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "clienManageVo", value = "联系人实体{unitInfoId，linkmanInfoId，loginName，loginPwd，isAdministrators，isBindAccount，keyword，unitServiceIds}", required = true)})
    public ResultForm putLinkman(ClientManageVo clienManageVo) {
        try {
            return new ContentResultForm<>(true, clientManageService.updateAeaUnitLink(clienManageVo));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/getLinkmanList")
    @ApiOperation(value = "查询非本单位联系人信息", notes = "查询非本单位联系人信息", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "aeaLinkmanInfo", value = "联系人实体，根据查询要求输入对应字段", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "查询页", required = true, dataType = "int")})
    public ResultForm listAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo, int pageSize, int pageNum) {
        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
        return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(clientManageService.listAeaLinkmanInfo(aeaLinkmanInfo, page))));
    }
}
