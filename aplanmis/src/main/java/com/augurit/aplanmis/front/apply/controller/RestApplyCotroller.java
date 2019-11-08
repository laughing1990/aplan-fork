package com.augurit.aplanmis.front.apply.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageItemAdminService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.vo.PreItemCheckResultVo;
import com.augurit.aplanmis.front.apply.service.AeaParStageService;
import com.augurit.aplanmis.front.apply.service.AeaSeriesService;
import com.augurit.aplanmis.front.apply.service.RestApplyService;
import com.augurit.aplanmis.front.apply.vo.*;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/apply")
@Api(value = "启动流程接口", tags = "申报-发起申报")
public class RestApplyCotroller {

    @Autowired
    private RestApplyService restStartProcessService;
    @Autowired
    private AeaHiSmsInfoService aeaHiSmsInfoService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaParStageService aeaParStageService;
    @Autowired
    private AeaSeriesService aeaSeriesService;
    @Autowired
    private AeaParStageItemAdminService aeaParStageItemAdminService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;

    @PostMapping("/series/processflow/start")
    @ApiOperation(value = "现场登记 --> 收件，发起申报", httpMethod = "POST")
    public ContentResultForm<String> startSeriesFlow(@RequestBody SeriesApplyDataPageVo seriesApplyDataPageVo) throws Exception {
        String applyinstId = restStartProcessService.startSeriesFlow(seriesApplyDataPageVo);
        return new ContentResultForm<>(true, applyinstId, "Series start process success");
    }

    @PostMapping("/series/inst")
    @ApiOperation(value = "现场登记 --> 生成实例，打印回执", httpMethod = "POST")
    public ContentResultForm<String> instantiateSeriesFlow(@RequestBody SeriesApplyDataPageVo seriesApplyDataVo) throws Exception {
        String applyinstId = restStartProcessService.instantiateSeriesFlow(seriesApplyDataVo);
        return new ContentResultForm<>(true, applyinstId, "Series instantiate process success");
    }

    @PostMapping("/series/inadmissible")
    @ApiOperation(value = "现场登记 --> 不予受理，生成实例，启动流程，打印不受理回执", httpMethod = "POST")
    public ContentResultForm<String> inadmissible(@RequestBody SeriesApplyDataPageVo seriesApplyDataVo) throws Exception {
        String applyinstId = restStartProcessService.inadmissibleSeriesFlow(seriesApplyDataVo);
        return new ContentResultForm<>(true, applyinstId, "Series instantiate process success");
    }

    @GetMapping("/series/check")
    @ApiOperation(value = "现场登记 --> 申报前置检查", notes = "申报的项目绑定的主题阶段下有对应的事项才可以进行申报")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoId", value = "申报的项目")
            , @ApiImplicitParam(name = "itemVerId", value = "实施事项")
    })
    public ContentResultForm<SeriesApplyCheckVo> checkBeforeSeriesFlow(String projInfoId, String itemVerId) {
        Assert.notNull(projInfoId, "projInfoId can not null");
        Assert.notNull(itemVerId, "itemVerId can not null");

        SeriesApplyCheckVo seriesApplyCheckVo = new SeriesApplyCheckVo();
        // 获取申报事项所绑定的主题， 如果没有，则返回主题列表给前端选择
        List<AeaParStageItem> aeaParStageItems = aeaParStageItemAdminService.checkBeforeSeriesFlow(projInfoId, itemVerId, SecurityContext.getCurrentOrgId());
        seriesApplyCheckVo.buildThemeStageList(aeaParStageItems, projInfoId);
        return new ContentResultForm<>(true, seriesApplyCheckVo, "success");
    }

    @GetMapping("/option/item/check")
    @ApiOperation(value = "现场登记 --> 申报前置检查", notes = "并行事项或者单项的前置事项审批通过了才可以申报")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoId", value = "申报的项目")
            , @ApiImplicitParam(name = "itemVerId", value = "实施事项")
    })
    public ResultForm checkPreItems(String projInfoId, String itemVerId) {
        Assert.notNull(projInfoId, "projInfoId can not null");
        Assert.notNull(itemVerId, "itemVerId can not null");

        // 前置事项检查
        PreItemCheckResultVo preItemCheckResultVo = aeaItemBasicService.checkPreItemsPassed(itemVerId, projInfoId);
        if (!preItemCheckResultVo.isPassed()) {
            return new ResultForm(false, "存在前置事项没有审批通过");
        }
        return new ResultForm(true, "通过");
    }

    @PostMapping("/parallel/inst")
    @ApiOperation(value = "并联申报 --> 生成实例，打印回执", httpMethod = "POST")
    public ContentResultForm<ApplyinstIdVo> instantiateStageProcess(@Valid @RequestBody StageApplyDataPageVo StageApplyDataPageVo) throws Exception {
        ApplyinstIdVo applyinstIdVo = restStartProcessService.instantiateStageProcess(StageApplyDataPageVo);
        return new ContentResultForm<>(true, applyinstIdVo, "Parallel instantiate process success.");
    }

    @PostMapping("/parallel/processflow/start")
    @ApiOperation(value = "并联申报 --> 发起申报", notes = "并联申报 --> 发起申报", httpMethod = "POST")
    public ContentResultForm<ApplyinstIdVo> startStageProcess(@Valid @RequestBody StageApplyDataPageVo stageApplyDataPageVo) throws Exception {
        ApplyinstIdVo applyinstIdVo = restStartProcessService.startStageProcess(stageApplyDataPageVo);
        return new ContentResultForm<>(true, applyinstIdVo, "Parall start process success.");
    }

    @PostMapping("/parallel/onlyInstApply")
    @ApiOperation(value = "并联申报 --> 仅保存申报实例", notes = "并联申报 --> 仅保存申报实例", httpMethod = "POST")
    public ContentResultForm<String> onlyInstApply(@RequestBody StageApplyDataPageVo stageApplyDataPageVo) throws Exception {
        String applyinstId = restStartProcessService.onlyInstApply(stageApplyDataPageVo);
        return new ContentResultForm<>(true, applyinstId, "Inst apply success.");
    }

    @PostMapping("/parallel/processflow/inadmissible")
    @ApiOperation(value = "并联申报 --> 发起申报，不予受理", notes = "并联申报 --> 发起申报，不予受理", httpMethod = "POST")
    public ContentResultForm<ApplyinstIdVo> inadmissibleStageProcess(@Valid @RequestBody StageApplyDataPageVo stageApplyDataPageVo) throws Exception {
        ApplyinstIdVo applyinstIdVo = restStartProcessService.inadmissibleStageProcess(stageApplyDataPageVo);
        return new ContentResultForm<>(true, applyinstIdVo, "Parall start process success.");
    }

    @PostMapping("/save/or/update/smsinfo")
    @ApiOperation(value = "并联申报 --> 保存领件人信息", httpMethod = "POST")
    public ContentResultForm<String> saveOrUpdateSmsInfo(@Valid SmsInfoVo smsInfoVo) throws Exception {
        String id;
        if (StringUtils.isBlank(smsInfoVo.getId())) {
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoService.createAeaHiSmsInfo(smsInfoVo.toSmsInfo());
            id = aeaHiSmsInfo.getId();
        } else {
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoService.getAeaHiSmsInfoById(smsInfoVo.getId());
            aeaHiSmsInfoService.updateAeaHiSmsInfo(smsInfoVo.merge(aeaHiSmsInfo));
            id = smsInfoVo.getId();
        }
        return new ContentResultForm<>(true, id, "Save aeaHiSmsInfo success");
    }

    @PostMapping("/common/processflow/start")
    @ApiOperation(value = "草稿箱视图的申报 --> 发起申报", notes = "草稿箱视图的申报 --> 发起申报", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true, dataType = "string", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "comment", value = "办理意见", required = false, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm startProcessFlow(String applyinstId, String comment) throws Exception {
        if (StringUtils.isBlank(applyinstId)) {
            return new ResultForm(false, "发起申报失败，参数申请实例id不能为空！");
        }
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (aeaHiApplyinst != null) {
            try {
                String isSeriesApprove = aeaHiApplyinst.getIsSeriesApprove();
                if ("1".equals(isSeriesApprove)) {
                    SeriesApplyDataVo seriesApplyDataVo = new SeriesApplyDataVo();
                    seriesApplyDataVo.setApplyinstId(applyinstId);
                    seriesApplyDataVo.setComments(comment);
                    aeaSeriesService.stageApplay(seriesApplyDataVo);
                } else {
                    StageApplyDataVo stageApplyDataVo = new StageApplyDataVo();
                    stageApplyDataVo.setApplyinstIds(new String[]{applyinstId});
                    stageApplyDataVo.setComments(comment);
                    stageApplyDataVo.setIsJustApplyinst("2");
                    aeaParStageService.stageApply(stageApplyDataVo);
                }
                return new ResultForm(true, "发起申报成功！");
            } catch (Exception e) {
                e.printStackTrace();
                return new ResultForm(false, "发起申报失败！");
            }
        } else {
            return new ResultForm(false, "发起申报失败，查询不到申请实例信息！");
        }
    }

    @PostMapping("/getServiceItemsInfo")
    @ApiOperation(value = "根据材料ID获取相关中介服务事项信息和项目信息", notes = "根据材料ID获取相关中介服务事项信息和项目信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true, dataType = "string", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "matId", value = "材料定义ID", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getImServiceItemsAndProjInfo(String matId, String applyinstId) {
        try {

            if (StringUtils.isBlank(applyinstId)) return new ResultForm(false, "缺少参数：applyinstId");
            if (StringUtils.isBlank(matId)) return new ResultForm(false, "缺少参数：matId");
            return new ContentResultForm(true, restStartProcessService.getImServiceItemsAndProjInfo(matId, applyinstId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取中介服务事项信息失败！");
        }

    }

    @PostMapping("/createImServiceItemPurchase")
    @ApiOperation(value = "创建项目采购需求", notes = "创建项目采购需求", httpMethod = "POST")
    public ResultForm createImServiceItemPurchase(ImServiceItemPurchaseVo imServiceItemPurchaseVo) throws Exception {
        return restStartProcessService.createImServiceItemPurchase(imServiceItemPurchaseVo);
    }
}
