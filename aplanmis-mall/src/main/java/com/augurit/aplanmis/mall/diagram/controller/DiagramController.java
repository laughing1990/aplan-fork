package com.augurit.aplanmis.mall.diagram.controller;

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
import com.augurit.aplanmis.thirdPlatform.service.ProjectCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/rest/project")
@Api(tags = "申报-项目基本信息")
@Slf4j
public class DiagramController {


    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private DiagramService diagramService;
    @Autowired
    private ProjectCodeService projectCodeService;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;


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
        return new ModelAndView("mall/flowChart/rappidProjView");
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
}
