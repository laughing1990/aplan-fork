package com.augurit.aplanmis.front.form.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.front.form.service.RestExProjFormService;
import com.augurit.aplanmis.front.form.vo.ExProjFormVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/rest/form/ex/project")
@Slf4j
@Api(value = "建设项目登记信息表单")
public class RestExProjFormController {

    @Autowired
    private RestExProjFormService restExProjFormService;

    @RequestMapping("/index")
    @ApiOperation(value = "跳转到建设项目登记信息表单")
    public ModelAndView index(String projInfoId) {
        ModelAndView view = new ModelAndView("form/exProjForm");
        view.addObject("projInfoId", projInfoId);
        return view;
    }

    @GetMapping("/{projInfoId}")
    @ApiOperation(value = "获取建设项目登记信息表单")
    public ResultForm getExProjForm(@PathVariable String projInfoId) {
        try {
            ExProjFormVo exProjFormVo = restExProjFormService.getExProjForm(projInfoId);
            if (exProjFormVo != null) {
                return new ContentResultForm(true, exProjFormVo);
            } else {
                return new ResultForm(false, "项目不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取建设项目登记信息失败！" + e.getMessage());
        }
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存建设项目登记信息表单")
    public ResultForm save(ExProjFormVo exProjFormVo) {
        try {
            if (exProjFormVo == null || StringUtils.isBlank(exProjFormVo.getProjInfoId())) {
                return new ResultForm(false, "projInfoId为空");
            }

            return restExProjFormService.saveExProjForm(exProjFormVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "建设项目登记信息保存失败！" + e.getMessage());
        }
    }
}
