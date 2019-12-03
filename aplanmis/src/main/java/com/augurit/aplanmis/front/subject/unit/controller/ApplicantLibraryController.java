package com.augurit.aplanmis.front.subject.unit.controller;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
@RequestMapping("/rest/applicant/lib")
@Api(tags = "菜单入口")
public class ApplicantLibraryController {

    public static final String BACKEND_APPLICANT_GLOBAL_VIEW = "global/applicantview";//全局单位库

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @GetMapping("/index")
    @ApiOperation("全局单位库-主页列表")
    public ModelAndView index() {
        return new ModelAndView(BACKEND_APPLICANT_GLOBAL_VIEW);
    }

    @GetMapping("/detail")
    @ApiOperation("/企业单位详情")
    public ModelAndView detail(@RequestParam(required = false) String unitInfoId) {
        ModelAndView modelAndView = new ModelAndView("global/unitsDetail");
        AeaUnitInfo aeaUnitInfo = null;
        if (StringUtils.isNotBlank(unitInfoId)) {
            aeaUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);
        }
        modelAndView.addObject("aeaUnitInfo", aeaUnitInfo);
        return modelAndView;
    }
}
