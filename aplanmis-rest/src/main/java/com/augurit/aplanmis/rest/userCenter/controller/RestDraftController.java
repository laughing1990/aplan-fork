package com.augurit.aplanmis.rest.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.dto.ApproveProjInfoDto;
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.userCenter.service.RestApproveService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//草稿箱
@RestController
@RequestMapping("/rest/user")
@Api(tags = "法人空间 --> 草稿箱相关接口")
public class RestDraftController {
    Logger logger = LoggerFactory.getLogger(RestDraftController.class);

    @Autowired
    RestApproveService restApproveService;

    /*@GetMapping("todraftsPage")
    @ApiOperation(value = "跳转草稿箱页面")
    public ModelAndView toDraftPage() {
        return new ModelAndView("mall/userCenter/components/drafts");
    }*/

    @GetMapping("/draftApplyItem/list")
    @ApiOperation(value = "我的草稿 --> 我的草稿项目列表查询接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "项目状态(0:办结1:办理中2:草稿)", name = "state", dataType = "string"),
            @ApiImplicitParam(value = "关键词", name = "keyword", dataType = "string"),
            @ApiImplicitParam(value = "页面数量", name = "pageNum", required = true, dataType = "string"),
            @ApiImplicitParam(value = "页面页数", name = "pageSize", required = true, dataType = "string")})
    public ContentResultForm<PageInfo<ApproveProjInfoDto>> getdraftApplyItemlist(String state, String keyword, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            AuthUser loginInfo = AuthContext.getCurrentUser();
            if (loginInfo.isPersonalAccount()) {//个人
                return new ContentResultForm<>(true, restApproveService.searchApproveProjInfoListByUnitOrLinkman("", loginInfo.getLinkmanInfoId(), state, keyword, pageNum, pageSize));
            } else if (StringUtils.isNotBlank(loginInfo.getLinkmanInfoId())) {//委托人
                return new ContentResultForm<>(true, restApproveService.searchApproveProjInfoListByUnitOrLinkman(loginInfo.getUnitInfoId(), loginInfo.getLinkmanInfoId(), state, keyword, pageNum, pageSize));
            } else {//企业
                return new ContentResultForm<>(true, restApproveService.searchApproveProjInfoListByUnitOrLinkman(loginInfo.getUnitInfoId(), "", state, keyword, pageNum, pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "查询已申报项目列表查询接口异常");
        }
    }
}
