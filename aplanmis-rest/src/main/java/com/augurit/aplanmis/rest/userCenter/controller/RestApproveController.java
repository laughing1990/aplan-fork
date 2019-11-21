package com.augurit.aplanmis.rest.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.dto.ApproveProjInfoDto;
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.userCenter.service.RestApproveService;
import com.augurit.aplanmis.rest.userCenter.vo.StatisticsNumVo;
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

//审批情况
@RestController
@RequestMapping("rest/user")
@Api(tags = "法人空间 --> 审批情况相关接口")
public class RestApproveController {
    Logger logger = LoggerFactory.getLogger(RestApproveController.class);

    @Autowired
    RestApproveService restApproveService;

    /*@GetMapping("toapprovePage")
    @ApiOperation(value = "跳转审批情况页面")
    public ModelAndView toApprovePage() {
        return new ModelAndView("mall/userCenter/components/approve");
    }*/

    @GetMapping("approve/list")
    @ApiOperation(value = "审批情况 --> 审批情况列表查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键字", name = "keyword", dataType = "string"),
            @ApiImplicitParam(value = "页面数量", name = "pageNum", required = true, dataType = "string"),
            @ApiImplicitParam(value = "页面页数", name = "pageSize", required = true, dataType = "string")})
    public ContentResultForm<PageInfo<ApproveProjInfoDto>> getApprovelist(String keyword, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            AuthUser loginInfo = AuthContext.getCurrentUser();
            if (loginInfo.isPersonalAccount()) {//个人
                return new ContentResultForm<>(true, restApproveService.searchIteminstApproveInfoListByUnitIdAndUserId(keyword, "", loginInfo.getLinkmanInfoId(), pageNum, pageSize));
            } else if (StringUtils.isNotBlank(loginInfo.getLinkmanInfoId())) {//委托人
                return new ContentResultForm<>(true, restApproveService.searchIteminstApproveInfoListByUnitIdAndUserId(keyword, loginInfo.getUnitInfoId(), loginInfo.getLinkmanInfoId(), pageNum, pageSize));
            } else {//企业
                return new ContentResultForm<>(true, restApproveService.searchIteminstApproveInfoListByUnitIdAndUserId(keyword, loginInfo.getUnitInfoId(), "", pageNum, pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "查询审批情况列表查询接口异常");
        }
    }

    @GetMapping("approvalAndMatCompletion/num")
    @ApiOperation(value = "申报查询->统计数目接口")
    public ContentResultForm<StatisticsNumVo> getApprovalAndMatCompletionNum() {
        try {
            return new ContentResultForm<>(true, restApproveService.getApprovalAndMatCompletionNum());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "申报查询->统计数目接口异常");
        }
    }

}
