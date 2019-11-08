package com.augurit.aplanmis.front.approve.controller;

import com.augurit.agcloud.bpm.front.service.BpmProcessFrontService;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.front.approve.service.ApproveService;
import com.augurit.aplanmis.front.approve.service.OfficialDocumentService;
import com.augurit.aplanmis.front.approve.vo.AeaHiItemInfoVo;
import com.augurit.aplanmis.front.approve.vo.BpmApproveStateVo;
import com.augurit.aplanmis.front.approve.vo.MatinstVo;
import com.augurit.aplanmis.front.approve.vo.item.AeaHiIteminstVo;
import com.augurit.aplanmis.front.approve.vo.item.AeaItemStateVo;
import com.augurit.aplanmis.front.approve.vo.official.OfficialDocumentInfoVo;
import com.augurit.aplanmis.front.approve.vo.parState.AeaParStateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/rest/approve")
@Api(value = "审批详情页", tags = "审批详情页")
@Slf4j
public class ApproveController {

    @Autowired
    private UploaderFactory uploaderFactory;
    @Autowired
    private BpmProcessFrontService bpmProcessFrontService;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private OfficialDocumentService officialDocumentService;

    @GetMapping("/check/task")
    @ApiOperation(value = "业务审批 --> 查看任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", readOnly = true, dataType = "string", paramType = "query", required = true)
            , @ApiImplicitParam(name = "viewId", value = "视图id", readOnly = true, dataType = "string", paramType = "query", required = true)
            , @ApiImplicitParam(name = "busRecordId", value = "业务记录id", defaultValue = "undefined", readOnly = true, dataType = "string", paramType = "query", required = true)
    })
    public ModelAndView checkTask(String taskId, String viewId, String busRecordId, ModelMap modelMap) {
        if (StringUtils.isNotBlank(taskId)) {
            try {
                this.bpmProcessFrontService.checkTask(modelMap, taskId, viewId, true);
                modelMap.put("busRecordId", busRecordId);
            } catch (Exception e) {
                e.printStackTrace();
                modelMap.put("initError", e.getMessage());
            }
        } else {
            modelMap.put("initError", "任务节点id[taskId]不能为空！");
        }
        return new ModelAndView("agcloud/bpm/front/process/index");
    }

    @GetMapping("/init/process")
    @ApiOperation(value = "业务审批 --> 办理任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", readOnly = true, dataType = "string", paramType = "query", required = true)
            , @ApiImplicitParam(name = "viewId", value = "视图id", readOnly = true, dataType = "string", paramType = "query", required = true)
            , @ApiImplicitParam(name = "busRecordId", value = "业务记录id", defaultValue = "undefined", readOnly = true, dataType = "string", paramType = "query", required = true)
    })
    public ModelAndView initProcessTurning(ModelMap modelMap, String taskId, String viewId, String busRecordId) {
        if (StringUtils.isNotBlank(taskId)) {
            try {
                bpmProcessFrontService.initProcessTurning(modelMap, taskId, viewId);
                modelMap.put("busRecordId", busRecordId);
            } catch (Exception e) {
                e.printStackTrace();
                modelMap.put("initError", e.getMessage());
            }
        } else {
            modelMap.put("initError", "任务节点id[taskId]不能为空！");
        }
        return new ModelAndView("agcloud/bpm/front/process/index");
    }

    @GetMapping("/type/and/state")
    @ApiOperation(value = "审批详情页 --> 获取申报方式和状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申报实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ContentResultForm<BpmApproveStateVo> getApplyStylesAndState(String applyinstId, String taskId) {
        if (log.isDebugEnabled()) {
            log.debug("获取申报方式和状态, applyinstId: {}, taskId: {}", applyinstId, taskId);
        }
        try {
            BpmApproveStateVo pmApproveStateVo = approveService.getApplyStylesAndState(applyinstId, taskId);
            return new ContentResultForm<>(true, pmApproveStateVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "获取申报方式和状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/parallel/selected/states")
    @ApiOperation(value = "审批详情页 --> 查看并联申报已选择情形")
    @ApiImplicitParam(name = "applyinstId", value = "申报实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    public ResultForm detail(@RequestParam String applyinstId) throws Exception {

        //List<Map<String, Object>> parStateByApply = approveService.getParStateByApply(applyinstId);
        //List<ParStateByApplyVo> parStateByApplyVos = ParStateByApplyVo.toParStateByApplyVo(parStateByApply);
        List<AeaParState> hiParStateinstByApplyinstId = approveService.getHiParStateinstByApplyinstId(applyinstId);

        List<AeaParStateVo> aeaParStateVos = AeaParStateVo.toListAeaParStateVo(hiParStateinstByApplyinstId);
        return new ContentResultForm<>(true, aeaParStateVos);
    }

    @GetMapping("/series/selected/states")
    @ApiOperation(value = "审批详情页 --> 查看串联申报已选择情形")
    @ApiImplicitParam(name = "applyinstId", value = "申报实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    public ResultForm getStateList(String applyinstId) throws Exception {
        List<AeaItemState> selectedStates4Item = approveService.getSelectedStates4Item(applyinstId);
        List<AeaItemStateVo> aeaItemStateVos = AeaItemStateVo.toListAeaParStateVo(selectedStates4Item);
        return new ContentResultForm<>(true, aeaItemStateVos);
    }

    @GetMapping("/iteminst/list")
    @ApiOperation(value = "审批详情页 --> 查看并联申报事项列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申报实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "busRecordId", value = "业务记录id, 当isItemSeek为true时， approveOrgId(审批部门); 当isItemSeek为false时， iteminstId（事项）", required = true, dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "isItemSeek", value = "是否意见征集，默认false", required = true, dataType = "boolean", paramType = "query", readOnly = true)
    })
    public ContentResultForm<List<AeaHiIteminstVo>> getItemisntList(String applyinstId, String busRecordId, boolean isItemSeek) throws Exception {
        if (StringUtils.isBlank(applyinstId)) {
            return new ContentResultForm(false, "applyinstId为空！");
        }
        List<AeaHiIteminst> itemisntList = approveService.getItemisntList(applyinstId, busRecordId, isItemSeek);
        if (!itemisntList.isEmpty()) {
            List<AeaHiIteminstVo> aeaHiIteminstVos = AeaHiIteminstVo.toAeaHiIteminstVo(itemisntList);
            return new ContentResultForm<>(true, aeaHiIteminstVos);
        }
        return new ContentResultForm<>(true, null, "Empty result");
    }

    @GetMapping("/docs/matinst")
    @ApiOperation(value = "审批详情页 --> 查询单个申报材料")
    @ApiImplicitParam(name = "matinstId", value = "材料实例ID", dataType = "string", required = true)
    public ContentResultForm<OfficialDocumentInfoVo> getOne(String matinstId) {
        if (StringUtils.isBlank(matinstId)) {
            return new ContentResultForm<>(false, null, "材料实例ID为空");
        }
        OfficialDocumentInfoVo officialDocumentInfoVo = officialDocumentService.getOne(matinstId);
        return new ContentResultForm<>(true, officialDocumentInfoVo);
    }

    @GetMapping("/series/iteminst")
    @ApiOperation(value = "审批详情页 --> 申报项目信息")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "applyinstId", value = "申请实例", dataType = "string")
            , @ApiImplicitParam(name = "busRecordId", value = "业务id", dataType = "string")
            , @ApiImplicitParam(name = "isItemSeek", dataType = "string")
            , @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "string")
    })
    public ContentResultForm<AeaHiItemInfoVo> getItemisntAndUnitInfo(String applyinstId, String busRecordId, boolean isItemSeek, String taskId) throws Exception {
        if (StringUtils.isBlank(applyinstId)) {
            return new ContentResultForm<>(false, null, "applyinstId为空！");
        }
        AeaHiItemInfoVo aeaHiItemInfoVo = approveService.projectDeclareInfo(applyinstId, busRecordId, isItemSeek, taskId);
        return new ContentResultForm<>(true, aeaHiItemInfoVo);
    }

    @GetMapping("/series/matinst/list")
    @ApiOperation(value = "根据事项实例ID查询已申报事项材料列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "iteminstId", value = "事项实例ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "applyinstId", value = "申请实例ID", dataType = "string", required = false),
//            @ApiImplicitParam(name = "isSeries", value = "单项 1||并联 0", dataType = "string", allowableValues = "0,1", defaultValue = "1")
    })
    public ResultForm getSeriesMatinstByIteminstId(String iteminstId, String applyinstId) throws Exception {
        List<MatinstVo> matinstVoList = approveService.getSeriesMatinstByIteminstId(iteminstId, applyinstId);
        return new ContentResultForm<>(true, matinstVoList, "success");
    }
}
