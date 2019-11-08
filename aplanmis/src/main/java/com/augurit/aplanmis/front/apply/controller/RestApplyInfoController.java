package com.augurit.aplanmis.front.apply.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.handler.ItemPrivilegeComputationHandler;
import com.augurit.aplanmis.front.apply.service.RestAppyInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/apply")
@Api(value = "事项 情形", tags = "申报-[主题 阶段 事项]")
@Slf4j
public class RestApplyInfoController {
    private static final String CORE_ITEMS = "coreItems";
    private static final String PARALLEL_ITEMS = "parallelItems";

    @Autowired
    private RestAppyInfoService restApplyService;

    @GetMapping("/stage/items")
    @ApiOperation("并联申报 --> 查询并联审批事项和并行推进事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stageId", value = "阶段id", required = true, dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "projInfoId", value = "项目id", dataType = "string", paramType = "query", readOnly = true)
    })
    public ContentResultForm<Map<String, List<ItemPrivilegeComputationHandler.ComputedItem>>> getNonCoreItemListByStageId(@RequestParam String stageId, @RequestParam(required = false) String projInfoId) {
        if (log.isDebugEnabled()) {
            log.debug("查询并联审批事项和并行推进事项, stageId: {}", stageId);
        }
        try {
            Map<String, List<ItemPrivilegeComputationHandler.ComputedItem>> items = new HashMap<>();

            List<ItemPrivilegeComputationHandler.ComputedItem> requiredItems = restApplyService.getRequiredItems(stageId, projInfoId);
            List<ItemPrivilegeComputationHandler.ComputedItem> optionalItems = restApplyService.getOptionalItems(stageId, projInfoId);
            items.put(PARALLEL_ITEMS, requiredItems);// 并联
            items.put(CORE_ITEMS, optionalItems);// 并行
            return new ContentResultForm<>(true, items, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询并联审批事项和并行推进事项异常");
        }
    }
}
