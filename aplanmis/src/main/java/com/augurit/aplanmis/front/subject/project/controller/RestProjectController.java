package com.augurit.aplanmis.front.subject.project.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaApplyinstProj;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.service.diagram.dto.DiagramStatusDto;
import com.augurit.aplanmis.common.service.diagram.service.DiagramService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.front.subject.project.service.RestProjectService;
import com.augurit.aplanmis.front.subject.project.vo.ChildProjectAddVo;
import com.augurit.aplanmis.front.subject.project.vo.ChildProjectVo;
import com.augurit.aplanmis.front.subject.project.vo.ProjectDetailVo;
import com.augurit.aplanmis.front.subject.project.vo.ProjectKeywordVo;
import com.augurit.aplanmis.thirdPlatform.service.ProjectCodeService;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/project")
@Api(tags = "申报-项目基本信息")
@Slf4j
public class RestProjectController {

    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private RestProjectService restProjectService;
    @Autowired
    private DiagramService diagramService;
    @Autowired
    private ProjectCodeService projectCodeService;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;

    @ApiOperation(value = "根据项目名称或者编码模糊查询项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "根据项目名称或者编码模糊查询项目", required = true, dataType = "String"),
    })
    @GetMapping("/info")
    public ContentResultForm<List<ProjectKeywordVo>> getProjectInfo(@RequestParam String keyword) {
        Assert.isTrue(StringUtils.isNotBlank(keyword), "keyword is null");
        try {
            List<AeaProjInfo> aeaProjInfos = aeaProjInfoService.findAeaProjInfoByKeyword(keyword);
            if (aeaProjInfos.size() == 0 && !keyword.contains("#") && !keyword.contains("ZBM") && CommonTools.isComplianceWithRules(keyword)) {
                aeaProjInfos.addAll(projectCodeService.getProjInfoFromThirdPlatform(keyword, null));
            }
            List<ProjectKeywordVo> unitsByKeyword = aeaProjInfos.stream()
                    // 过滤掉root项目
                    .filter(proj -> !"r".equals(proj.getProjFlag()))
                    .map(p -> new ProjectKeywordVo(p.getProjInfoId(), p.getLocalCode(), p.getProjName())).collect(Collectors.toList());
            return new ContentResultForm<>(true, unitsByKeyword, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @ApiOperation(value = "根据projInfoId获取项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoId", value = "项目编号", required = true, dataType = "String"),
    })

    /**
     * 通过项目id 精确定位项目信息
     */
    @GetMapping("/one/{projInfoId}")
    public ContentResultForm<ProjectDetailVo> getOne(@PathVariable String projInfoId) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(projInfoId), "projInfoId is null");
        ProjectDetailVo projectDetail1 = restProjectService.getProjectDetail(projInfoId);
        return new ContentResultForm<>(true, projectDetail1);
    }

    @ApiOperation(value = "根据projInfoId获取项目树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoId", value = "项目id", required = true, dataType = "String"),
    })
    @GetMapping("/tree/{projInfoId}")
    public ContentResultForm<List<ZtreeNode>> projectTree(@PathVariable String projInfoId) {
        Assert.isTrue(StringUtils.isNotBlank(projInfoId), "projInfoId is null");
        List<ZtreeNode> ztreeNodes = aeaProjInfoService.buildProjectTree(projInfoId);
        return new ContentResultForm<>(true, ztreeNodes, "Query success");
    }

    @ApiOperation(value = "编辑项目")
    @PostMapping("/edit")
    public ContentResultForm<String> editProject(AeaProjInfo aeaProjInfo) {
        Assert.isTrue(StringUtils.isNotBlank(aeaProjInfo.getProjInfoId()), "projInfoId is null");
        aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);
        return new ContentResultForm<>(true, aeaProjInfo.getProjInfoId(), "Update project success");
    }

    @ApiOperation(value = "保存项目")
    @PostMapping("/save")
    public ContentResultForm<String> addProject(AeaProjInfo aeaProjInfo) {
        Assert.isTrue(StringUtils.isNotBlank(aeaProjInfo.getLocalCode()), "LocalCode is null");
        aeaProjInfoService.insertAeaProjInfo(aeaProjInfo);
        return new ContentResultForm<>(true, "projInfoId", "Add project success");
    }

    @ApiOperation(value = "新增子项目")
    @PostMapping("/add/child")
    public ContentResultForm<ChildProjectVo> addChildProject(ChildProjectAddVo childProjectAddVo) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(childProjectAddVo.getParentProjInfoId()), "parentProjInfoId is null");
        AeaProjInfo child = restProjectService.addChildProject(childProjectAddVo);
        return new ContentResultForm<>(true, ChildProjectVo.from(child, childProjectAddVo.getParentProjInfoId()), "Add child project success.");
    }

    @ApiOperation(value = "删除子项目")
    @PostMapping("/delete/child")
    public ContentResultForm<String> deleteChildProject(String childProjInfoId) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(childProjInfoId), "childProjInfoId is null");
//        aeaProjInfoService.deleteAeaProjInfo(childProjInfoId);
        aeaProjInfoService.deleteChildChildProj(childProjInfoId);
        return new ContentResultForm<>(true, childProjInfoId, "Delete child project success.");
    }

    @ApiOperation(value = "根据申报实例ID获取项目信息")
    @GetMapping("/getProjectInfoByApplyinstId")
    @ApiImplicitParam(name = "applyinstId", value = "申请实例ID", required = true, dataType = "String")
    public ResultForm getProjectInfoByApplyinstId(String applyinstId) {
        List<AeaProjInfo> applyProj = aeaProjInfoService.findApplyProj(applyinstId);
        if (applyProj.size() > 0) {
            return new ContentResultForm<>(true, applyProj);
        }
        return new ContentResultForm<>(true, new ArrayList<AeaProjInfo>());

    }

    @GetMapping("/diagram/status/projInfo1")
    @ApiOperation("根据项目id查看审批全景图， 两种方式可查看：/diagram/status/projInfo?projInfoId=xxx 或者 /diagram/status/projInfo?applyinstId=xxx")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "projInfoId", value = "项目id", required = false, dataType = "String"),
        @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = false, dataType = "String"),
    })
    public ModelAndView projectDiagramByProjInfoId(String projInfoId, String applyinstId) throws Exception {
        ModelAndView modelAndView = new ModelAndView("ui-jsp/theme/rappid_proj_view");
        String errorMsg = "";
        try{
            AeaProjInfo aeaProjInfo = null;
            if(StringUtils.isNotBlank(applyinstId)){
                AeaApplyinstProj aeaApplyinstProj = new AeaApplyinstProj();
                aeaApplyinstProj.setApplyinstId(applyinstId);
                List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.listAeaApplyinstProj(aeaApplyinstProj);
                if (CollectionUtils.isNotEmpty(aeaApplyinstProjs)) {
                    if(aeaApplyinstProjs.size()<1){
                        errorMsg = "未查找到相关的申报信息！";
                        modelAndView.addObject("errorMsg", errorMsg);
                        return modelAndView;
                    }
                    aeaApplyinstProj = aeaApplyinstProjs.get(0);
                    aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(aeaApplyinstProj.getProjInfoId());
                }
            }else if(StringUtils.isNotBlank(projInfoId)){
                aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
            }

            if(StringUtils.isBlank(projInfoId) && StringUtils.isBlank(applyinstId)){
                errorMsg = "参数不能同时为空！";
                modelAndView.addObject("errorMsg", errorMsg);
                return modelAndView;
            }
            if ((aeaProjInfo != null && StringUtils.isNotBlank(aeaProjInfo.getThemeId()))) {
                DiagramStatusDto globalDiagramStatus = diagramService.getGlobalDiagramStatus(aeaProjInfo.getProjInfoId(), aeaProjInfo.getThemeId());
                modelAndView.addObject("projName", aeaProjInfo.getProjName());
                modelAndView.addObject("projInfoId", aeaProjInfo.getProjInfoId());

                if(globalDiagramStatus == null || StringUtils.isBlank(globalDiagramStatus.getDiagramJson())){
                    errorMsg = "当前项目未绑定主题或所属主题未设置全景图！";
                    modelAndView.addObject("errorMsg", errorMsg);
                    return modelAndView;
                }

                List<ZtreeNode> ztreeNodes = aeaProjInfoService.buildProjectTree(aeaProjInfo.getProjInfoId());
                errorMsg = "";
                if(ztreeNodes != null){
                    modelAndView.addObject("errorMsg", errorMsg);
                    modelAndView.addObject("projTree", JsonUtils.toJson(ztreeNodes));
                }

                modelAndView.addObject("diagramStatusVo", globalDiagramStatus);
                List<DiagramStatusDto.DiagramStageDto> statusList = globalDiagramStatus.getDiagramStageStatusList();
                if (statusList != null && statusList.size() > 0) {
                    modelAndView.addObject("statusList", JsonUtils.toJson(statusList));
                }
                return modelAndView;
            }else{
                errorMsg = "未查找到相关的项目或未绑定主题！";
                modelAndView.addObject("errorMsg", errorMsg);
                log.warn("根据申报实例展示全景流程图失败, projInfoId: {}", projInfoId);
                return modelAndView;
            }
        }catch (Exception e){
            errorMsg = "查询出错！";
            if(StringUtils.isNotBlank(e.getMessage())){
                errorMsg = e.getMessage();
            }
            log.error("", e);
            modelAndView.addObject("errorMsg", errorMsg);
            return modelAndView;
        }
    }

    @GetMapping("/diagram/status/projInfoInter")
    @ApiOperation("根据项目id查看审批全景图， 两种方式可查看：/diagram/status/projInfo?projInfoId=xxx 或者 /diagram/status/projInfo?applyinstId=xxx")
    @CrossOrigin
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoId", value = "项目id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = false, dataType = "String"),
    })
    public ResultForm projectDiagramByProjInfoIdInter(String projInfoId, String applyinstId) throws Exception {
        ModelAndView view = projectDiagramByProjInfoId(projInfoId, applyinstId);
        ContentResultForm form = new ContentResultForm(true);
        try{

            ModelMap modelMap = view.getModelMap();
            form.setContent(view.getModelMap());
            Object errorMsg = modelMap.get("errorMsg");
            if(errorMsg == null || StringUtils.isNotBlank(errorMsg.toString())){
                form.setSuccess(false);
                form.setMessage(errorMsg==null?"":errorMsg.toString());
            }
        }catch (Exception e){
            log.error("",e);
            form.setMessage(e.getMessage());
            form.setSuccess(false);
        }
        return form;
    }

    @GetMapping("/diagram/status/projInfo")
    @ApiOperation("页面-全景流程图")
    public ModelAndView stageApplyIndex() {
        return new ModelAndView("flowChart/rappidProjView");
    }

    @GetMapping("/diagram/status/json")
    @ApiOperation("根据项目id获取审批全景图json状态数据")
    @ApiImplicitParam(name = "projInfoId", value = "项目id", required = true, dataType = "String")
    public ResultForm projectDiagramJsonData(@RequestParam("projInfoId") String projInfoId) throws Exception {
        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        List<DiagramStatusDto.DiagramStageDto> statusList = new ArrayList<>();
        if (StringUtils.isNotBlank(aeaProjInfo.getThemeId())) {
            DiagramStatusDto globalDiagramStatus = diagramService.getGlobalDiagramStatus(projInfoId, aeaProjInfo.getThemeId());
            statusList = globalDiagramStatus.getDiagramStageStatusList();
        }
        log.warn("根据申报实例展示全景流程图失败, projInfoId: {}", projInfoId);
        return new ContentResultForm<>(true, statusList, "项目全景图阶段状态数据");
    }

    @ApiOperation(value = "新增自编码项目")
    @PostMapping("/save/zbm")
    public ContentResultForm<AeaProjInfo> addProject(String projName) {
        Assert.isTrue(StringUtils.isNotBlank(projName), "projName is null");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String num = String.valueOf(date.getTime());
        //zbm表示自编码，r表示root，日期加8位数字（当前时间毫秒值截取后8位）例如：ZBM-R-20181203-56899466
        String code = "ZBM-R-" + sdf.format(date) + "-" + num.substring(num.length() - 8);
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        aeaProjInfo.setProjName(projName);
        aeaProjInfo.setLocalCode(code);
        aeaProjInfo.setGcbm(code);
        aeaProjInfoService.insertAeaProjInfo(aeaProjInfo);
        return new ContentResultForm<>(true, aeaProjInfo, "Add project success");
    }

    @ApiOperation(value = "根据projInfoId获取单体工程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoId", value = "项目id", required = true, dataType = "String"),
    })
    @GetMapping("/childProjectList/{projInfoId}")
    public ContentResultForm<List<AeaProjInfo>> childProjectList(@PathVariable String projInfoId) {
        Assert.isTrue(StringUtils.isNotBlank(projInfoId), "projInfoId is null");
        List<AeaProjInfo> childProjectList = aeaProjInfoService.findChildProj(projInfoId);
        return new ContentResultForm<>(true, childProjectList, "Query child project success");
    }

    @ApiOperation(value = "新增单体工程信息")
    @PostMapping("/save/childProject")
    public ContentResultForm<AeaProjInfo> saveChildProject(@RequestBody AeaProjInfo aeaProjInfo) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(aeaProjInfo.getParentProjId()), "parentProjInfoId is null");
        AeaProjInfo parent = aeaProjInfoService.getAeaProjInfoByProjInfoId(aeaProjInfo.getParentProjId());
        if (parent == null) {
            return new ContentResultForm<AeaProjInfo>(false, null, "Save child project failed because parent project information does not exist.");
        }
        AeaProjInfo child = aeaProjInfoService.addChildProjInfo(aeaProjInfo);
        return new ContentResultForm<AeaProjInfo>(true, child, "Save child project success.");
    }

    @ApiOperation(value = "编辑单体工程信息")
    @PostMapping("/edit/childProject")
    public ContentResultForm<String> editChildProject(@RequestBody AeaProjInfo aeaProjInfo) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(aeaProjInfo.getProjInfoId()), "childProjInfoId is null");
        AeaProjInfo info = aeaProjInfoService.getAeaProjInfoByProjInfoId(aeaProjInfo.getProjInfoId());
        if (info == null) {
            return new ContentResultForm<>(false, aeaProjInfo.getProjInfoId(), "Edit child project failed because project information does not exist.");
        }
        aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);
        return new ContentResultForm<>(true, aeaProjInfo.getProjInfoId(), "Edit child project success.");
    }

    @ApiOperation(value = "删除单体工程信息")
    @PostMapping("/delete/childProject")
    public ContentResultForm<String> deleteChildProjectByParam(String childProjInfoId) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(childProjInfoId), "childProjInfoId is null");
        AeaProjInfo info = aeaProjInfoService.getAeaProjInfoByProjInfoId(childProjInfoId);
        if (info == null) {
            return new ContentResultForm<>(false, childProjInfoId, "Delete child project failed because project information does not exist.");
        }
        aeaProjInfoService.deleteChildChildProj(childProjInfoId);
        return new ContentResultForm<>(true, childProjInfoId, "Delete child project success.");
    }

    @ApiOperation(value = "跳转前端单体工程信息页面")
    @RequestMapping("/childProject/index.html")
    public ModelAndView childProjectIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("form/monomerProject");
        return modelAndView;
    }

}
