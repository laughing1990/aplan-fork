package com.augurit.aplanmis.front.correct.controller;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrect;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.dto.CorrectinstDto;
import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.front.correct.service.RestMatCorrectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/rest/correct")
@Api(value = "材料补正", tags = "材料补正")
@Slf4j
public class RestMatCorrectController {

    @Autowired
    private RestMatCorrectService restMatCorrectService;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    private AeaHiItemCorrectService aeaHiItemCorrectService;

    @RequestMapping("/index.html")
    public ModelAndView index() throws Exception {
        ModelAndView modelAndView = new ModelAndView("matCorrect/index");
        return modelAndView;
    }

    @RequestMapping("/matConfirm.html")
    public ModelAndView matConfirm() throws Exception {
        ModelAndView modelAndView = new ModelAndView("matCorrect/toBeConfirm");
        return modelAndView;
    }

    @GetMapping("/getLackMats")
    @ApiOperation("材料补正 --> 查询少交和已交的材料清单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "iteminstId", value = "事项实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getLackMatsByApplyinstIdAndIteminstId(@RequestParam String applyinstId, @RequestParam String iteminstId) {

        if (StringUtils.isBlank(applyinstId))
            return new ContentResultForm(false, "申请实例ID为空！");
        if (StringUtils.isBlank(iteminstId))
            return new ContentResultForm(false, "事项实例ID为空！");
        try {
            AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);
            if ("6".equals(iteminst.getIteminstState()) || "7".equals(iteminst.getIteminstState())) {
                AeaHiItemCorrect aeaHiItemCorrect = aeaHiItemCorrectService.getCurrentCorrectinst(iteminstId);
                if (aeaHiItemCorrect == null) throw new Exception("找不到补正记录！");
                return new ContentResultForm(false, restMatCorrectService.getMatCorrectInfo(aeaHiItemCorrect.getCorrectId()), "该事项当前状态处于补件中");
            }
            if ("9".equals(iteminst.getIteminstState()) || "10".equals(iteminst.getIteminstState()))
                return new ContentResultForm(false, null, "该事项当前状态处于特别程序中");

            return restMatCorrectService.getLackMatsByApplyinstIdAndIteminstId(applyinstId, iteminstId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null, "获取材料清单失败！");
        }
    }

    @GetMapping("/getMatCorrectInfo")
    @ApiOperation("材料补正 --> 获取材料补正实例信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "correctId", value = "材料补正实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ContentResultForm getMatCorrectInfo(@RequestParam String correctId) {

        if (StringUtils.isBlank(correctId))
            return new ContentResultForm(false, "材料补正实例ID为空！");
        try {
            AeaHiItemCorrect aeaHiItemCorrect = restMatCorrectService.getMatCorrectInfo(correctId);
            return new ContentResultForm<>(true, aeaHiItemCorrect, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null, "获取材料补正实例信息失败！");
        }
    }

    @PostMapping("/createMatCorrectinst")
    @ApiOperation(value = "材料补正 --> 创建材料补正实例", httpMethod = "POST")
    public ResultForm createMatCorrectinst(CorrectinstDto correctinstDto) {

        if (StringUtils.isBlank(correctinstDto.getApplyinstId()))
            return new ResultForm(false, "申请实例ID为空！");
        if (StringUtils.isBlank(correctinstDto.getIteminstId()))
            return new ResultForm(false, "事项实例ID为空！");
        if (StringUtils.isBlank(correctinstDto.getIsFlowTrigger()))
            return new ResultForm(false, "流程触发标志为空！");
        if (StringUtils.isEmpty(correctinstDto.getCorrectDueDays()))
            return new ResultForm(false, "补正天数为空");
        if (StringUtils.isBlank(correctinstDto.getMatCorrectDtosJson()) || "[]".equals(correctinstDto.getMatCorrectDtosJson()))
            return new ResultForm(false, "请选择要补正的材料！");

        try {
            return restMatCorrectService.createMatCorrectinst(correctinstDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "创建材料补正实例失败！");
        }
    }

    @PostMapping("/saveMatCorrectInfo")
    @ApiOperation(value = "材料补正 --> 保存材料补正实例", httpMethod = "POST")
    public ResultForm saveMatCorrectInfo(MatCorrectinstDto matCorrectinstDto) {

        Assert.notNull(matCorrectinstDto.getCorrectId(), "材料补正实例ID为空！");
        Assert.notNull(matCorrectinstDto.getCorrectState(), "材料补正实例状态为空！");

        try {
            return restMatCorrectService.saveMatCorrectInfo(matCorrectinstDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "保存材料补正实例失败！");
        }
    }

    @GetMapping("/getItemCorrectRealIninst")
    @ApiOperation("材料补正 --> 获取待确认的补正材料列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "correctId", value = "材料补正实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ContentResultForm getItemCorrectRealIninst(@RequestParam String correctId) {

        if (StringUtils.isBlank(correctId))
            return new ContentResultForm(false, "材料补正实例id为空！");
        try {
            return new ContentResultForm<>(true, restMatCorrectService.getItemCorrectRealIninst(correctId), "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null, "获取待确认的补正材料列表失败！");
        }
    }

    @PostMapping("/matCorrectConfirm")
    @ApiOperation(value = "材料补正 --> 确认补正材料列表", httpMethod = "POST")
    public ResultForm matCorrectConfirm(String correctId, String correctState, String correctMemo, String matCorrectDtosJson) {

        if (StringUtils.isBlank(correctId))
            return new ResultForm(false, "材料补正实例ID为空！");
        if (StringUtils.isBlank(correctState))
            return new ResultForm(false, "材料补正实例状态为空！");
        if (StringUtils.isBlank(matCorrectDtosJson) || "[]".equals(matCorrectDtosJson))
            return new ResultForm(false, "材料补正列表为空！");

        try {
            restMatCorrectService.matCorrectConfirm(correctId, correctState, correctMemo, matCorrectDtosJson);
            return new ResultForm(true, "补正材料确认成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "补正材料确认失败！");
        }
    }

    @PostMapping("/att/delelte")
    @ApiOperation(value = "材料补全页面--> 删除电子件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", paramType = "query", required = true)
    })
    public ResultForm delelteAttFile(String detailIds, String attRealIninstId) {
        try {
            Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");

            restMatCorrectService.delelteAttFile(detailIds, attRealIninstId);
            return new ResultForm(true, "文件删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "文件删除失败！");
        }
    }

    @ApiOperation(value = "材料补正--> 上传电子件")
    @PostMapping("/att/upload")
    public ResultForm uploadFile(String attRealIninstId, HttpServletRequest request) {
        try {
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");
            restMatCorrectService.uploadFile(attRealIninstId, request);
            return new ResultForm(true, "文件上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "文件上传失败！");
        }
    }

    @GetMapping("/att/list")
    @ApiOperation("申报页面--> 查询已上传附件列表")
    @ApiImplicitParam(name = "attRealIninstId", value = "材料实例id")
    public ContentResultForm<List<BscAttFileAndDir>> attFileList(@RequestParam(required = false) String attRealIninstId) {
        try {
            if (StringUtils.isBlank(attRealIninstId)) throw new Exception("attRealIninstId为空！");
            return new ContentResultForm(true, restMatCorrectService.getAttFiles(attRealIninstId), "Query attachment list success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null, "Query attachment list failed");
        }
    }

    @GetMapping("/detail/list")
    @ApiOperation("审批页面--> 查询材料补正列表，可以查询单事项和整个申报过程的补正信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = false, dataType = "string", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "iteminstId", value = "事项实例id", required = false, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getMatCorrectInfoByApplyinstOrIteminst(String applyinstId, String iteminstId) {
        try {
            if (StringUtils.isBlank(applyinstId) && StringUtils.isBlank(iteminstId)) {
                return new ResultForm(false, "参数applyinstId和iteminstId不能同时为空！");
            }
            return new ContentResultForm(true, restMatCorrectService.getMatCorrectInfoByApplyinstOrIteminst(applyinstId, iteminstId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "查询材料补正列表出错！");
        }
    }


}
