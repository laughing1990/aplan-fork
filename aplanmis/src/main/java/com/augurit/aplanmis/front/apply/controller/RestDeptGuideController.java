package com.augurit.aplanmis.front.apply.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideService;
import com.augurit.aplanmis.common.vo.guide.GuideDetailVo;
import com.augurit.aplanmis.front.apply.vo.guide.GuideFinishVo;
import com.augurit.aplanmis.front.apply.vo.guide.GuideQueryVo;
import com.augurit.aplanmis.front.apply.vo.guide.GuideVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dept/guide")
@Slf4j
@Api(value = "部门辅导", tags = "部门辅导")
public class RestDeptGuideController {

    @Autowired
    private AeaHiGuideService aeaHiGuideService;

    @GetMapping("/list")
    @ApiOperation(value = "部门辅导列表")
    public ContentResultForm<PageInfo<AeaHiGuide>> list(GuideQueryVo guideQueryVo, Page page) {
        try {
            AeaHiGuide param = guideQueryVo.toAeaHiGuide();
            param.setCurrentUserId(SecurityContext.getCurrentUserId());
            PageInfo<AeaHiGuide> aeaHiGuidePageInfo = aeaHiGuideService.list(param, page);
            return new ContentResultForm<>(true, aeaHiGuidePageInfo, "success.");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation(value = "部门辅导详情")
    @ApiImplicitParam(value = "部门辅导id", name = "guideId", required = true)
    public ContentResultForm<GuideDetailVo> detail(String guideId) {
        try {
            return new ContentResultForm<>(true, aeaHiGuideService.detail(guideId), "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @PostMapping("/guide")
    @ApiOperation("辅导")
    public ResultForm guide(@RequestBody GuideVo guideVo) {
        try {
            aeaHiGuideService.guide(guideVo.toAeaHiGuideDetails(SecurityContext.getCurrentUserId()));
            return new ResultForm(true, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @PostMapping("/solicit")
    @ApiOperation("部门征求")
    public ResultForm solicitDept(@RequestBody GuideVo guideSolicitVo) {
        try {
            aeaHiGuideService.solicitDept(guideSolicitVo.toAeaHiGuideDetails(null));
            return new ResultForm(true, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    @PostMapping("/finish")
    @ApiOperation("辅导完成")
    public ResultForm finish(@RequestBody GuideFinishVo guideFinishVo) {
        try {
            aeaHiGuideService.finish(guideFinishVo.toAeaHiGuide(SecurityContext.getCurrentUserId()));
            return new ResultForm(true, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }
}
