package com.augurit.aplanmis.front.subject.project.controller;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

/**
 * 项目库
 */
@RestController
@RequestMapping("/rest/project")
@Slf4j
@Api(value = "全局项目库", tags = "菜单入口")
public class ProjectLibraryController {

    @Autowired
    private AeaProjInfoService aeaProjInfoService;

    @GetMapping("/detail")
    @ApiImplicitParam(value = "项目id", name = "projInfoId", paramType = "path", required = true)
    @ApiOperation("全局项目库-编辑")
    public ModelAndView editAcpProjInfo(String projInfoId) {
//        Assert.isTrue(StringUtils.isNotBlank(projInfoId), "projInfoId id null");

        ModelAndView modelAndView = new ModelAndView("global/projectDetail");
        // 根据projInfoId查询项目信息
        AeaProjInfo param = new AeaProjInfo();
        param.setProjInfoId(projInfoId);
        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if (aeaProjInfo != null) {
            modelAndView.addObject("projInfoId", aeaProjInfo.getProjInfoId());
            modelAndView.addObject("projType", aeaProjInfo.getProjType());
            modelAndView.addObject("aeaProjInfo", aeaProjInfo);
        }
        AeaProjInfo parentProj = aeaProjInfoService.findParentProj(projInfoId);
        modelAndView.addObject("parentProjId", Objects.isNull(parentProj) ? null : parentProj.getProjInfoId());

        return modelAndView;
    }

    @GetMapping("/list")
    @ApiOperation("全局项目库-主页列表")
    public ModelAndView index() {
        return new ModelAndView("global/projectlist");
//        return new ModelAndView("global/linkmanview");
    }
}
