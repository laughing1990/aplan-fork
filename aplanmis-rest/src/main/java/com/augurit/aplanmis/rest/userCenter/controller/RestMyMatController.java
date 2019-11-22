package com.augurit.aplanmis.rest.userCenter.controller;


import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.userCenter.service.RestMyMatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/user/mat")
@Api(value = "我的材料列表", tags = "法人空间 --> 我的材料库相关接口")
public class RestMyMatController {

    @Autowired
    RestMyMatService restMyMatService;

    /*@GetMapping("toMyMatListPage")
    @ApiOperation(value = "跳转法人空间我的材料列表相关页面")
    public String toMyMatListPage() {
        return "";
    }*/

    @GetMapping("/list")
    @ApiOperation(value = "获取我的材料列表")
    public ContentResultForm<List<AeaHiItemMatinst>> getMyMatList() {
        try {
            AuthUser loginInfo = AuthContext.getCurrentUser();
            if (loginInfo.isPersonalAccount()) {//个人
                return new ContentResultForm<>(true, restMyMatService.getMyMatListByUser("", loginInfo.getLinkmanInfoId()));
            } else
            if (StringUtils.isNotBlank(AuthContext.getCurrentLinkmanInfoId())) {//委托人
                return new ContentResultForm<>(true, restMyMatService.getMyMatListByUser(AuthContext.getCurrentUnitInfoId(), AuthContext.getCurrentLinkmanInfoId()));
            } else {//企业
                return new ContentResultForm<>(true, restMyMatService.getMyMatListByUser(AuthContext.getCurrentUnitInfoId(), ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "获取我的材料列表发生异常");
        }
    }
}
