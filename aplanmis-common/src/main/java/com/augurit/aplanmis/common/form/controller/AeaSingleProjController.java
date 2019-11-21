package com.augurit.aplanmis.common.form.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 单体工程
 */
@RestController
@RequestMapping("rest/project")
public class AeaSingleProjController {
    @Autowired
    private AeaProjInfoService aeaProjInfoService;


    @ApiOperation(value = "根据projInfoId获取单体工程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoId", value = "项目id", required = true, dataType = "String"),
    })
    @GetMapping("/childProjectList/{projInfoId}")
    public ContentResultForm<List<AeaProjInfo>> childProjectList(@PathVariable String projInfoId) {
        if(StringUtils.isBlank(projInfoId)){
            return new ContentResultForm<>(false, null, "查询失败，主项目id不存在");
        }
        List<AeaProjInfo> childProjectList = aeaProjInfoService.findChildProj(projInfoId);
        return new ContentResultForm<>(true, childProjectList, "查询成功");
    }

    @ApiOperation(value = "新增单体工程信息")
    @PostMapping("/save/childProject")
    public ContentResultForm<AeaProjInfo> saveChildProject(@RequestBody AeaProjInfo aeaProjInfo) throws Exception {
        // 主项目id
        String parentProjId = aeaProjInfo.getParentProjId();
        if(StringUtils.isBlank(parentProjId)){
            return new ContentResultForm<>(false, null, "操作失败，项目id不存在");
        }
        // 查询主项目信息
        AeaProjInfo parent = aeaProjInfoService.getAeaProjInfoByProjInfoId(parentProjId);
        if (parent == null) {
            return new ContentResultForm<AeaProjInfo>(false, null, "操作失败，主项目信息不存在");
        }
        //查询是否有重名的项目工程
        if (StringUtils.isNotBlank(aeaProjInfo.getProjName())) {
            AeaProjInfo info = new AeaProjInfo();
            info.setProjName(aeaProjInfo.getProjName());
            List<AeaProjInfo> checkNameProj = aeaProjInfoService.findAeaProjInfo(info);
            if (checkNameProj != null && checkNameProj.size() > 0) {
                return new ContentResultForm<AeaProjInfo>(false, null, "操作失败，项目名称已存在");
            }
        }

        AeaProjInfo child = aeaProjInfoService.addChildProjInfo(aeaProjInfo);
        return new ContentResultForm<AeaProjInfo>(true, child, "操作成功");
    }

    @ApiOperation(value = "编辑单体工程信息")
    @PostMapping("/edit/childProject")
    public ContentResultForm<String> editChildProject(@RequestBody AeaProjInfo aeaProjInfo) throws Exception {
        String projInfoInId = aeaProjInfo.getProjInfoId();
        if(StringUtils.isBlank(projInfoInId)){
            return new ContentResultForm<>(false, null, "操作失败，项目id不存在");
        }

        AeaProjInfo info = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoInId);
        if (info == null) {
            return new ContentResultForm<>(false, projInfoInId, "操作失败，项目信息不存在");
        }

        //查询是否有重名的项目工程
        if (StringUtils.isNotBlank(aeaProjInfo.getProjName()) && StringUtils.isNotBlank(info.getProjName())) {
            if(!aeaProjInfo.getProjName().equals(info.getProjName())){
                AeaProjInfo entity = new AeaProjInfo();
                info.setProjName(aeaProjInfo.getProjName());
                List<AeaProjInfo> checkNameProj = aeaProjInfoService.findAeaProjInfo(entity);
                if (checkNameProj != null && checkNameProj.size() > 0) {
                    return new ContentResultForm<>(false, projInfoInId, "操作失败，项目名称已存在");
                }
            }
        }

        aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);
        return new ContentResultForm<>(true, projInfoInId, "操作成功");
    }

    @ApiOperation(value = "删除单体工程信息")
    @PostMapping("/delete/childProject")
    public ContentResultForm<String> deleteChildProjectByParam(String childProjInfoId) throws Exception {
        if(StringUtils.isBlank(childProjInfoId)){
            return new ContentResultForm<>(false, null, "操作失败，项目id不存在");
        }

        AeaProjInfo info = aeaProjInfoService.getAeaProjInfoByProjInfoId(childProjInfoId);
        if (info == null) {
            return new ContentResultForm<>(false, childProjInfoId, "操作失败，项目信息不存在");
        }
        aeaProjInfoService.deleteChildChildProj(childProjInfoId);
        return new ContentResultForm<>(true, childProjInfoId, "操作成功");
    }

    @ApiOperation(value = "跳转前端单体工程信息页面")
    @RequestMapping("/childProject/index.html")
    public ModelAndView childProjectIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("form/monomerProject");
        return modelAndView;
    }
}
