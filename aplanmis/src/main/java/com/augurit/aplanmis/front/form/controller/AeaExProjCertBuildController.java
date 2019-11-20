package com.augurit.aplanmis.front.form.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaExProjCertBuild;
import com.augurit.aplanmis.common.mapper.AeaExProjCertBuildMapper;
import com.augurit.aplanmis.front.form.service.AeaExProjCertBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/rest/from/certbuild")
@ResponseBody
@Slf4j
public class AeaExProjCertBuildController {
    @Autowired
    private AeaExProjCertBuildService aeaExProjCertBuildService;

    @RequestMapping("/index.html")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("");
        return modelAndView;
    }

    @PostMapping("/saveAeaExProjCertBuild")
    public ContentResultForm<String> saveAeaExProjCertBuild(AeaExProjCertBuild aeaExProjCertBuild){
        try {
            aeaExProjCertBuildService.saveForm(aeaExProjCertBuild);
            return new ContentResultForm<>(true,"保存成功", "Save success");
        }catch (Exception e){
            return new ContentResultForm<>(false,"保存失败", "save fail");
        }
    }

    @GetMapping("/initAeaExProjCertBuild")
    public ResultForm initAeaExProjCertBuild(@RequestParam(required = false) String projInfoId){
        try {
            if(projInfoId == null){
                return new ContentResultForm<AeaExProjCertBuild>(false,null, "项目ID不可以为空");
            }else {
                return new ContentResultForm<AeaExProjCertBuild>(true, aeaExProjCertBuildService.findByProjId(projInfoId), "保存成功");
            }
        }catch (Exception e){
                return new ContentResultForm<AeaExProjCertBuild>(false,null,e.getMessage());
        }
    }
}
