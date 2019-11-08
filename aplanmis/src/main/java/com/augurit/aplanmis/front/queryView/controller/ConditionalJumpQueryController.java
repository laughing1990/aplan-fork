package com.augurit.aplanmis.front.queryView.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryRequest;
import com.augurit.aplanmis.front.queryView.service.ConditionalJumpService;
import com.augurit.aplanmis.front.queryView.vo.ConditionalQueryDic;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author
 * @date 2019/9/4
 */
@Api(value = "委办局效能督查跳转页面-条件查询相关接口", tags = "条件查询相关接口")
@RestController
@RequestMapping("/rest/conditional/jump")
@Slf4j
public class ConditionalJumpQueryController {

    @Autowired
    private ConditionalJumpService conditionalQueryService;

    private static String applyViewId;

    private static String partsViewId;

    @GetMapping("/listItemToleranceAccept")
    @ApiOperation(value = "根据查询条件获取 容缺受理件")
    public ResultForm listItemToleranceAccept(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listItemToleranceAccept(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }
    @GetMapping("/listNeedCorrectTasks")
    @ApiOperation(value = "根据查询条件获取待补正办件列表")
    public ResultForm listNeedCorrectTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo needCorrectTasks = conditionalQueryService.listNeedCorrectTasksByPage(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, needCorrectTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listUnConfirmItemCorrect")
    @ApiOperation(value = "根据查询条件获取 补正待确认件---事项")
    public ResultForm listUnConfirmItemCorrect(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listUnConfirmItemCorrect(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }


    @GetMapping("/lisSpecialProcedureTasks")
    @ApiOperation(value = "根据查询条件获取特殊程序件列表")
    public ResultForm lisSpecialProcedureTasks(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo preliminaryTasks = conditionalQueryService.lisSpecialProcedureTasks(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, preliminaryTasks);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listItemAgreeTolerance")
    @ApiOperation(value = "根据查询条件获取 容缺通过件")
    public ResultForm listItemAgreeTolerance(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listItemAgreeTolerance(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }
    @GetMapping("/listItemDisgree")
    @ApiOperation(value = "根据查询条件获取-办结（不通过）事项--部门-事项")
    public ResultForm listItemDisgree(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listItemDisgree(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }
    @GetMapping("/listItemOutScope")
    @ApiOperation(value = "根据查询条件获取 不予受理列表-部门-事项")
    public ResultForm listItemOutScope(ConditionalQueryRequest conditionalQueryRequest, Page page) {
        try {
            PageInfo dismissedApply = conditionalQueryService.listItemOutScope(conditionalQueryRequest, page);
            return new ContentResultForm<>(true, dismissedApply);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }
    @GetMapping("/task/dic/list")
    @ApiOperation(value = "获取办理任务相关查询条件的数据列表")
    public ResultForm getTaskConditionalQueryDic() {
        try {
            ConditionalQueryDic conditionalQueryDic = conditionalQueryService.taskConditionalQueryDic();
            return new ContentResultForm<>(true, conditionalQueryDic);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }
    @GetMapping("/queryApplyInfoTaskId")
    @ApiOperation("所有申报-查看")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm queryApplyInfoTaskId(String applyinstId) {

        try {
            String taskId = conditionalQueryService.queryApplyInfoTaskId(applyinstId);
            if (StringUtils.isBlank(taskId)) {
                throw new RuntimeException("找不到对应的taskId");
            }
            String viewId;
            if (StringUtils.isNotBlank(applyViewId)) {
                viewId = applyViewId;
            } else {
                viewId = conditionalQueryService.queryViewId("办结");
                applyViewId = viewId;
            }

            if (StringUtils.isBlank(viewId)) {

                if (StringUtils.isNotBlank(partsViewId)) {
                    viewId = partsViewId;
                } else {
                    viewId = conditionalQueryService.queryViewId("所有办件");
                    partsViewId = viewId;
                }
            }
            if (StringUtils.isBlank(viewId)) {
                throw new RuntimeException("找不到对应的viewId");
            }
            HashMap map = new HashMap();
            map.put("taskId", taskId);
            map.put("viewId", viewId);
            return new ContentResultForm<>(true, map);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/listNeedCorrectTasksOrganizer")
    @ApiOperation(value = "获取有待补正办件的承办单位")
    public ResultForm listNeedCorrectTasksOrganizer() {
        try {
            List<OpuOmOrg> list = conditionalQueryService.listNeedCorrectTasksOrganizer();
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }


}