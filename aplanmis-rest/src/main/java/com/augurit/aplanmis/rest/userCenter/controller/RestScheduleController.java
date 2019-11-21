package com.augurit.aplanmis.rest.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.service.search.ApproveDataService;
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.userCenter.service.RestApproveService;
import com.augurit.aplanmis.rest.userCenter.vo.AeaProjInfoResultVo;
import com.augurit.aplanmis.rest.userCenter.vo.LifeCycleDiagramVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rest/user")
@Api(tags = "法人空间 --> 项目进度相关接口")
public class RestScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(RestScheduleController.class);

    @Autowired
    RestApproveService restApproveService;
    @Autowired
    ApproveDataService approveDataService;

    /*@GetMapping("toscheduleInquirePage")
    @ApiOperation(value = "跳转项目进度页面")
    public ModelAndView toSchedulePage() {
        return new ModelAndView("mall/userCenter/components/scheduleInquire");
    }

    @GetMapping("tolifeCyclePage")
    @ApiOperation(value = "跳转生命周期图页面")
    public ModelAndView toLifeCyclePage(String applyInstId, ModelMap modelMap) {
        modelMap.put("applyInstId", applyInstId);
        return new ModelAndView("mall/userCenter/components/lifeCycle");
    }*/

    @GetMapping("itemSchedule/list")
    @ApiOperation(value = "项目进度 --> 项目进度列表查询接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "关键词", name = "keyword", dataType = "string"),
            @ApiImplicitParam(value = "页面数量", name = "pageNum", required = true, dataType = "string"),
            @ApiImplicitParam(value = "页面页数", name = "pageSize", required = true, dataType = "string")})
    public ContentResultForm<PageInfo<AeaProjInfoResultVo>> getItemSchedulelist(String keyword, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            AuthUser loginInfo = AuthContext.getCurrentUser();
            List<AeaProjInfo> list;
            if (loginInfo.isPersonalAccount()) {//个人
                list = approveDataService.getScheduleProjListByUnitInfoIdOrLinkman("", loginInfo.getLinkmanInfoId(), keyword, pageNum, pageSize);
            } else if (StringUtils.isNotBlank(loginInfo.getLinkmanInfoId())) {//委托人
                list = approveDataService.getScheduleProjListByUnitInfoIdOrLinkman(loginInfo.getUnitInfoId(), loginInfo.getLinkmanInfoId(), keyword, pageNum, pageSize);
            } else {//企业
                list = approveDataService.getScheduleProjListByUnitInfoIdOrLinkman(loginInfo.getUnitInfoId(), "", keyword, pageNum, pageSize);
            }
            return new ContentResultForm<>(true, new PageInfo<>(list.size() > 0 ? list.stream().map(AeaProjInfoResultVo::build).collect(Collectors.toList()) : new ArrayList<>()));

        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询项目进度列表查询接口异常");
        }
    }

    @GetMapping("item/schedule/diagram/{projInfoId}")
    @ApiOperation(value = "进度查询 --> 生命周期图相关数据接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "项目ID", name = "projInfoId", required = true, dataType = "string")})
    public ContentResultForm<LifeCycleDiagramVo> getLiftCycleDiagramInfo(@PathVariable("projInfoId") String projInfoId) {
        try {
            AuthUser loginInfo = AuthContext.getCurrentUser();
            LifeCycleDiagramVo lifeCycleDiagramVo;
            long start = System.currentTimeMillis();
            logger.error("进度查询 --> 生命周期图相关数据接口getLiftCycleDiagramInfo---start");
            if (loginInfo.isPersonalAccount()) {//个人
                lifeCycleDiagramVo = restApproveService.getLiftCycleDiagramInfo(projInfoId, "", loginInfo.getLinkmanInfoId());
            } else if (StringUtils.isNotBlank(loginInfo.getLinkmanInfoId())) {//委托人
                lifeCycleDiagramVo = restApproveService.getLiftCycleDiagramInfo(projInfoId, loginInfo.getUnitInfoId(), loginInfo.getLinkmanInfoId());
            } else {//企业
                lifeCycleDiagramVo = restApproveService.getLiftCycleDiagramInfo(projInfoId, loginInfo.getUnitInfoId(), "");
            }
            logger.error("进度查询 --> 生命周期图相关数据接口getLiftCycleDiagramInfo,耗时:" + (System.currentTimeMillis() - start));
            return new ContentResultForm<>(true, lifeCycleDiagramVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "根据申请实例Id查询生命周期图相关数据异常");
        }
    }

}
