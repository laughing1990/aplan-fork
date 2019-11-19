package com.augurit.aplanmis.front.apply.controller;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.front.apply.service.AeaSeriesService;
import com.augurit.aplanmis.front.apply.service.RestApplyMatService;
import com.augurit.aplanmis.front.apply.vo.Mat2MatInstVo;
import com.augurit.aplanmis.front.apply.vo.ParallelApplyHandleVo;
import com.augurit.aplanmis.front.apply.vo.SaveMatinstVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyHandleVo;
import com.augurit.aplanmis.front.apply.vo.attach.ApplyAbstractQueryVo;
import com.augurit.aplanmis.front.apply.vo.attach.ApplyImportListVo;
import com.augurit.aplanmis.front.apply.vo.attach.ApplyImportVo;
import com.augurit.aplanmis.front.apply.vo.attach.ApplyMatUploadVo;
import com.augurit.aplanmis.front.apply.vo.attach.AttImportQueryVo;
import com.augurit.aplanmis.front.apply.vo.attach.AutoImportVo;
import com.augurit.aplanmis.front.apply.vo.attach.UploadMatReturnVo;
import com.github.pagehelper.Page;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/rest/mats")
@Api(tags = "申报-[情形 材料 附件]")
public class RestApplyMatAndAttachmentController {

    @Autowired
    private FileUtilsService fileUtilsService;

    @Autowired
    private RestApplyMatService restApplyMatService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaSeriesService aeaSeriesService;

    @GetMapping("/parallel/states/mats")
    @Transactional(readOnly = true)
    @ApiOperation(value = "并联申报--> 根据阶段获取情形和材料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stageId", value = "阶段id", readOnly = true, dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "projInfoId", value = "项目id，用来查询事项是否已办", readOnly = true, dataType = "string", paramType = "String")
            , @ApiImplicitParam(name = "parentId", value = "情形父id", readOnly = true, defaultValue = "ROOT", dataType = "string", paramType = "query", required = true)
    })
    public ContentResultForm<ParallelApplyHandleVo> getParStatesAndMatsByParentId(@RequestParam String stageId, @RequestParam String parentId, @RequestParam String projInfoId) {
        Assert.notNull(stageId, "stageId不能为空");

        try {
            AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
            if (null == aeaParStage) {
                return new ContentResultForm<>(false, null, "查询不到阶段信息");
            }
            ParallelApplyHandleVo applyStates = new ParallelApplyHandleVo();
            //需要情形
            if ("1".equals(aeaParStage.getIsNeedState())) {
                applyStates = restApplyMatService.listStageNeedStateApplyStates(stageId, parentId, projInfoId);
            }
            return new ContentResultForm<>(true, applyStates, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询阶段下的材料失败， stageId:" + stageId + ", parentId: " + parentId, e);
        }
    }

    @GetMapping("/parallel/neednot/state/mats")
    @Transactional(readOnly = true)
    @ApiOperation(value = "并联申报--> 根据阶段获取不分情形下的材料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stageId", value = "阶段id", readOnly = true, dataType = "string", paramType = "String", required = true),
            @ApiImplicitParam(name = "itemVerId", value = "事项版本id")
    })
    public ContentResultForm<ParallelApplyHandleVo> getParStatesAndMatsByParentId(@RequestParam String stageId, @RequestParam List<String> itemVerIds) {
        Assert.notNull(stageId, "stageId不能为空");

        ParallelApplyHandleVo applyStates = new ParallelApplyHandleVo();
        if (itemVerIds == null || itemVerIds.size() < 1) {
            return new ContentResultForm<>(true, applyStates, "无数据");
        }
        try {
            applyStates = restApplyMatService.listStageNoStateApplyStates(stageId, itemVerIds);

            return new ContentResultForm<>(true, applyStates, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询阶段下的不分情形时的材料失败， stageId:" + stageId + ", itemVerIds: " + itemVerIds, e);
        }
    }

    @GetMapping("/states/mats/{itemVerId}/{parentId}")
    @Transactional(readOnly = true)
    @ApiOperation("现场登记 --> 情形和材料列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemVerId", value = "事项版本id", required = true, dataType = "string", paramType = "path", readOnly = true)
            , @ApiImplicitParam(name = "parentId", value = "情形父id", defaultValue = "ROOT", dataType = "string", paramType = "query", required = true)
    })
    public ContentResultForm<SeriesApplyHandleVo> getSeriesMatList(@PathVariable String itemVerId, @PathVariable String parentId) throws Exception {
        Assert.notNull(itemVerId, "itemVerId不能为空");

        AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
        if (aeaItemBasic == null) {
            return new ContentResultForm<>(false, null, "没有找到对应的事项信息，请联系管理员");
        }
        SeriesApplyHandleVo applyStates;
        // 分情形
        if ("1".equals(aeaItemBasic.getIsNeedState())) {
            try {
                applyStates = restApplyMatService.listSeriesNeedStateApplyMats(itemVerId, parentId);
                return new ContentResultForm<>(true, applyStates, "Query success");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("查询阶段下的材料失败， itemVerId:" + itemVerId + ", parentId: " + parentId, e);
            }
        } else {
            // 不分情形，获取所有材料和情形
            applyStates = restApplyMatService.listSeriesNostateApplyMats(itemVerId);
        }
        return new ContentResultForm<>(true, applyStates, "Query states and mats success");
    }

    @GetMapping("/att/list")
    @ApiOperation("申报页面--> 查询已上传附件列表")
    @ApiImplicitParam(name = "matinstId", value = "材料实例id")
    public ContentResultForm<List<BscAttFileAndDir>> attFileList(@RequestParam(required = false) String matinstId) {
        if (StringUtils.isBlank(matinstId)) {
            return new ContentResultForm<>(true, new ArrayList<>(), "empty list");
        }
        try {
            List<BscAttFileAndDir> fileAndDirs = fileUtilsService.getMatAttDetailByMatinstId(matinstId);
            return new ContentResultForm<>(true, fileAndDirs, "Query attachment list success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, new ArrayList<>(), "Query attachment list failed");
        }
    }

    @GetMapping("/att/download")
    @ApiOperation(value = "申报页面--> 下载电子件")
    @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", required = true)
    public ContentResultForm downloadAttachment(String detailIds, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(detailIds)) {
                return new ContentResultForm<>(false, null, "找不到文件!");
            }
            String[] str = detailIds.split(",");
            fileUtilsService.downloadAttachment(str, response, request, false);
            return new ContentResultForm<>(true);
        } catch (Exception e) {
            return new ContentResultForm<>(false, null, "找不到文件!");
        }
    }

    @GetMapping("/att/downloadCovertPdf")
    @ApiOperation(value = "申报页面--> 下载电子件转换的pdf文件")
    @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", required = true)
    public ContentResultForm downloadCovertPdf(String detailIds, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(detailIds)) {
                return new ContentResultForm<>(false, null, "找不到文件!");
            }
            String[] str = detailIds.split(",");
            fileUtilsService.downloadAttachment(str, response, request, true);
            return new ContentResultForm<>(true);
        } catch (Exception e) {
            return new ContentResultForm<>(false, null, "找不到文件!");
        }
    }


    @PostMapping("/att/delelte")
    @ApiOperation(value = "申报页面||材料补全页面--> 删除电子件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", paramType = "query", required = true)
            , @ApiImplicitParam(name = "matinstId", value = "材料实例ID", dataType = "string", paramType = "query", required = true)
    })
    public ContentResultForm<String> delelteAttFile(String detailIds, String matinstId) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");
        Assert.isTrue(StringUtils.isNotBlank(matinstId), "matinstId is null");
        restApplyMatService.delelteAttFile(detailIds, matinstId);
        return new ContentResultForm<>(true, matinstId, "Matinst is deleted.");
    }

    @ApiOperation(value = "申报页面--> 上传电子件")
    @PostMapping("/att/upload")
    public ContentResultForm<String> uploadFile(ApplyMatUploadVo applyMatUploadVo, HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("utf-8");//设置编码，防止附件名称乱码
        AeaHiItemMatinst itemMatinst = new AeaHiItemMatinst();
        BeanUtils.copyProperties(applyMatUploadVo, itemMatinst);
        String matinstId = aeaHiItemMatinstService.saveAeaHiItemMatinst(itemMatinst, request);
        return new ContentResultForm<>(true, matinstId, "Mat attachment upload success");
    }

    @GetMapping("/att/preview")
    @ApiOperation(value = "申报页面--> 预览电子件")
    @ApiImplicitParam(name = "detailId", value = "附件ID", dataType = "string", required = true)
    public ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView modelAndView = fileUtilsService.preview(detailId, request, response, redirectAttributes);
        return modelAndView;
    }

    @GetMapping("/att/read")
    @ApiOperation(value = "申报页面--> 读取电子件")
    @ApiImplicitParam(name = "detailId", value = "附件ID", dataType = "string", required = true)
    public void readFile(String detailId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        fileUtilsService.readFile(detailId, request, response);
    }

    @GetMapping("/att/import/list")
    @ApiOperation(value = "申报页面--> 查询要导入的电子件列表")
    public ContentResultForm<List<ApplyImportListVo>> importAttList(AttImportQueryVo attImportQueryVo, Page page) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(attImportQueryVo.getMatCode()), "MatCode is null");
        List<ApplyImportListVo> importList = restApplyMatService.getImportList(attImportQueryVo, page);
        return new ContentResultForm<>(true, importList, "Query attach list success.");
    }

    @PostMapping("/att/import")
    @ApiOperation(value = "申报页面--> 导入电子件")
    public ContentResultForm<String> importAtt(@ModelAttribute ApplyImportVo applyImportVo) {
        Assert.isTrue(StringUtils.isNotBlank(applyImportVo.getMatId()), "matId is null");
        Assert.isTrue(StringUtils.isNotBlank(applyImportVo.getFileIds()), "fileIds is null");
        String matinstId = restApplyMatService.importAtt(applyImportVo);
        return new ContentResultForm<>(true, matinstId, "Import attach success");
    }

    @GetMapping("/att/extract")
    @ApiOperation(value = "申报页面--> 一键提取")
    public ContentResultForm<List<UploadMatReturnVo>> extract(@ModelAttribute ApplyAbstractQueryVo applyAbstractQueryVo) throws Exception {
        Assert.notNull(applyAbstractQueryVo, "applyAbstractQueryVo is null");
        Assert.isTrue(StringUtils.isNotBlank(applyAbstractQueryVo.getProjInfoIds()), "projInfoId is null");
        Assert.notNull(applyAbstractQueryVo.getMatCodes(), "matCodes is null");
        List<UploadMatReturnVo> list = restApplyMatService.getAeaHiItemMatinstFile(applyAbstractQueryVo);
        return new ContentResultForm<>(true, list, "success");
    }

    @PostMapping("/att/upload/auto")
    @ApiOperation(value = "申报页面--> 一键自动分拣")
    public ContentResultForm<List<UploadMatReturnVo>> saveFilesAuto(@ModelAttribute AutoImportVo autoImportVo, HttpServletRequest request) throws Exception {
        List<UploadMatReturnVo> list = restApplyMatService.saveFilesAuto(autoImportVo, request);
        return new ContentResultForm<>(true, list, "success");
    }

    @PostMapping("/matinst/delete")
    @ApiOperation(value = "申报页面--> 根据matinsId删除材料实例")
    @ApiImplicitParam(name = "matinstId", value = "材料实例id, 多个用逗号隔开", dataType = "string", required = true)
    public ContentResultForm deleteMatinst(@RequestParam String matinstId) {
        Assert.isTrue(StringUtils.isNotBlank(matinstId), "matinstId is null");

        restApplyMatService.deleteMatinst(matinstId);
        return new ContentResultForm<>(true, null, "Matinst is Deleted");
    }

    @PostMapping("/completion")
    @ApiOperation(value = "申报页面 --> 材料补全")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申报实例id", dataType = "string", required = true)
            , @ApiImplicitParam(name = "comments", value = "意见", dataType = "string", required = true)
            , @ApiImplicitParam(name = "flag", value = "start：开始补全，end:补全结束", dataType = "string", required = true, allowableValues = "start, end")
    })
    public ContentResultForm matCompletion(String applyinstId, String comments, String flag) {
        try {
            aeaSeriesService.materialCompletion(applyinstId, comments, flag);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, "Mat completion failed.");
        }
        return new ContentResultForm<>(true, "Mat completion success");
    }

    @PostMapping("/bind/cert")
    @ApiOperation(value = "关联电子证照材料")
    @ApiImplicitParam(value = "电子证照")
    public ContentResultForm<AeaHiCertinst> bindCertinst(@RequestBody AeaHiCertinst aeaHiCertinst) {
        try {
            if (StringUtils.isNotBlank(aeaHiCertinst.getMatId())) {
                aeaHiCertinst = aeaHiItemMatinstService.bindCertinst(aeaHiCertinst, SecurityContext.getCurrentUserId());
                Assert.hasText(aeaHiCertinst.getCertinstId(), "certinstId is null");
                return new ContentResultForm<>(true, aeaHiCertinst, "Bind success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "关联证照库材料失败: " + e.getMessage());
        }
        return null;
    }

    @PostMapping("/unbind/cert")
    @ApiImplicitParam(value = "证照材料实例id", name = "matinstId")
    @ApiOperation(value = "解除关联电子证照材料")
    public ResultForm bindCertinst(String matinstId) {
        Assert.hasText(matinstId, "证照材料实例id不能为空");

        try {
            aeaHiItemMatinstService.unbindCertinst(matinstId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "解绑失败: " + e.getMessage());
        }
        return new ResultForm(true, "解绑成功");
    }

    @PostMapping("/matinst/batch/save")
    @ApiOperation("申报页面根据材料定义生成材料实例id")
    public ContentResultForm<List<Mat2MatInstVo>> saveMatinsts(@RequestBody SaveMatinstVo saveMatinstVo) {
        List<Mat2MatInstVo> mat2MatInstVos = restApplyMatService.saveMatinsts(saveMatinstVo);
        return new ContentResultForm<>(true, mat2MatInstVos, "Batch save matinst success");
    }

    @Autowired
    private AeaItemStateService aeaItemStateService;

    @GetMapping("/test/rootStateAndAns")
    public ResultForm getrootStateAndAns(String stageId, String parentStateId) throws Exception {
        List<AeaItemState> aeaParStates = aeaItemStateService.listAeaItemStateByParentId(stageId, null, parentStateId, SecurityContext.getCurrentOrgId());

        return new ContentResultForm<>(true, aeaParStates, "success");

    }

    @PostMapping("/getOfficeMats")
    public ResultForm getOfficeMats(String stageId, String itemVerIds) {
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
        List<AeaItemMat> mats;
        try {
            // 阶段合并方式办理， 材料是根据 aea_par_in来关联的， 不是 aea_item_inout
            if ("1".equals(aeaParStage.getHandWay())) {
                mats = restApplyMatService.getMatsByStageIdAndItemVerIds(stageId, itemVerIds);
            } else {
                mats = restApplyMatService.getOfficeMatsByStageItemVerIds(stageId, itemVerIds);
            }
            return new ContentResultForm<>(true, mats, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "事项输入材料查询失败！");
        }
    }

    @PostMapping("/getHistoryAttMatList")
    @ApiOperation("根据材料编码和单位ID、申办人ID获取历史电子附件")
    public ResultForm getHistoryAttMatList(String matCode, String projInfoId) {
        try {

            if (StringUtils.isBlank(matCode) || StringUtils.isBlank(projInfoId)) return new ResultForm(false, "缺少参数!");
            return new ContentResultForm(true, restApplyMatService.getHistoryAttMatList(matCode, projInfoId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取电子附件列表失败！");
        }
    }


}
