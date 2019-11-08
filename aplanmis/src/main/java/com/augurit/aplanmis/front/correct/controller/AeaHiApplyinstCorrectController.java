package com.augurit.aplanmis.front.correct.controller;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrect;
import com.augurit.aplanmis.common.dto.ApplyinstCorrectinstDto;
import com.augurit.aplanmis.common.dto.MatCorrectInfoDto;
import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 材料补全实例表-Controller 页面控制转发类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:tiantian</li>
 * <li>创建时间：2019-08-28 17:33:44</li>
 * </ul>
 */
@RestController
@RequestMapping("/applyinst/correct")
@Api(value = "材料补全", tags = "材料补全")
@Slf4j
public class AeaHiApplyinstCorrectController {

    private static Logger logger = LoggerFactory.getLogger(AeaHiApplyinstCorrectController.class);

    @Autowired
    private AeaHiApplyinstCorrectService aeaHiApplyinstCorrectService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;


    @RequestMapping("/index.html")
    public ModelAndView index() throws Exception {
        ModelAndView modelAndView = new ModelAndView("matMend/index");
        return modelAndView;
    }

    @RequestMapping("/applyinstConfirm.html")
    public ModelAndView matConfirm() throws Exception {
        ModelAndView modelAndView = new ModelAndView("matMend/toBeConfirm");
        return modelAndView;
    }


    @RequestMapping("/getLackMats")
    @ApiOperation("材料补全 --> 查询少交和已交的材料清单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id,存在多个用逗号分隔", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getLackMatsByApplyinstIdAndIteminstId(@RequestParam String applyinstId) {

        Assert.notNull(applyinstId, "申请实例ID为空！");
        try {
            AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if ("3".equals(applyinst.getApplyinstState()) || "4".equals(applyinst.getApplyinstState())) {
                AeaHiApplyinstCorrect aeaHiApplyinstCorrect = aeaHiApplyinstCorrectService.getCurrentCorrectinst(applyinstId);
                if (aeaHiApplyinstCorrect == null) return new ContentResultForm(false, "找不到补全记录！");
                ContentResultForm resultForm = (ContentResultForm) aeaHiApplyinstCorrectService.getMatCorrectInfo(aeaHiApplyinstCorrect.getApplyinstCorrectId());
                AeaHiApplyinstCorrect aeaHiApplyinstCorrect1 = (AeaHiApplyinstCorrect) resultForm.getContent();
                aeaHiApplyinstCorrect1.setCorrectState(aeaHiApplyinstCorrect1.getCorrectState() == null ? "6" : aeaHiApplyinstCorrect1.getCorrectState());
                return new ContentResultForm(false, aeaHiApplyinstCorrect1, "待补全材料清单查询成功");
            }

            MatCorrectInfoDto matCorrectInfoDto = aeaHiApplyinstCorrectService.getLackMatsByApplyinstId(applyinstId);
            return new ContentResultForm<>(true, matCorrectInfoDto, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, "获取材料清单失败！");
        }
    }


    @RequestMapping("/createMatCorrectinst")
    @ApiOperation(value = "材料补全 --> 创建材料补全实例", httpMethod = "POST")
    public ResultForm createMatCorrectinst(ApplyinstCorrectinstDto correctinstDto) {
        try {

            String msg = null;
            if (StringUtils.isBlank(correctinstDto.getApplyinstId())) {
                msg = "申请实例ID为空！";
            } else if (StringUtils.isBlank(correctinstDto.getIsFlowTrigger())) {
                msg = "流程触发标志为空！";
            } else if (correctinstDto.getCorrectDueDays() == null || correctinstDto.getCorrectDueDays() == 0l) {
                msg = "材料补全期限天数为空！";
            } else if (StringUtils.isBlank(correctinstDto.getMatCorrectDtosJson())) {
                msg = "材料补全清单为空！";
            }
            if (msg != null) return new ResultForm(false, msg);

            return new ResultForm(true, aeaHiApplyinstCorrectService.createMatCorrectinst(correctinstDto));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "创建材料补全实例失败！");
        }
    }


    @GetMapping("/getMatCorrectInfo")
    @ApiOperation("材料补全 --> 获取材料补全实例信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstCorrectId", value = "材料补全实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getMatCorrectInfo(@RequestParam String applyinstCorrectId) {

        Assert.notNull(applyinstCorrectId, "材料补全实例ID为空！");
        try {
            return aeaHiApplyinstCorrectService.getMatCorrectInfo(applyinstCorrectId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取材料补全实例信息失败！");
        }
    }


    @RequestMapping("/saveMatCorrectInfo")
    @ApiOperation(value = "材料补全 --> 保存材料补全实例", httpMethod = "POST")
    public ResultForm saveMatCorrectInfo(MatCorrectinstDto matCorrectinstDto) {

        Assert.notNull(matCorrectinstDto.getCorrectId(), "材料补全实例ID为空！");
        Assert.notNull(matCorrectinstDto.getCorrectState(), "材料补全实例状态为空！");

        try {
            return aeaHiApplyinstCorrectService.saveMatCorrectInfo(matCorrectinstDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "保存材料补全实例失败！");
        }
    }

    @RequestMapping("/saveMatCorrectInfoAndConfirm")
    @ApiOperation(value = "材料补全 --> 保存材料补全实例并已确认", httpMethod = "POST")
    public ResultForm saveMatCorrectInfoAndConfirm(MatCorrectinstDto matCorrectinstDto) {

        Assert.notNull(matCorrectinstDto.getCorrectId(), "材料补全实例ID为空！");
        Assert.notNull(matCorrectinstDto.getCorrectState(), "材料补全实例状态为空！");
        try {
            return aeaHiApplyinstCorrectService.saveMatCorrectInfoAndConfirm(matCorrectinstDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "材料补全失败！");
        }
    }

    @GetMapping("/getItemCorrectRealIninst")
    @ApiOperation("材料补全 --> 获取待确认的补全材料列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstCorrectId", value = "材料补全实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getItemCorrectRealIninst(@RequestParam String applyinstCorrectId) {
        Assert.notNull(applyinstCorrectId, "材料补全实例id为空！");
        try {
            return new ContentResultForm(true, aeaHiApplyinstCorrectService.getApplyinstCorrectRealIninst(applyinstCorrectId), "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取待确认的补全材料列表失败！");
        }
    }

    @PostMapping("/matCorrectConfirm")
    @ApiOperation(value = "材料补全 --> 确认补全材料列表", httpMethod = "POST")
    public ResultForm matCorrectConfirm(String correctId, String correctState, String correctMemo, String matCorrectDtosJson) {
        Assert.notNull(correctId, "材料补全实例ID为空！");
        Assert.notNull(correctState, "材料补全实例状态为空！");
        Assert.notNull(matCorrectDtosJson, "材料补全列表为空！");
        try {
            aeaHiApplyinstCorrectService.matCorrectConfirm(correctId, correctState, correctMemo, matCorrectDtosJson);
            return new ResultForm(true, "补全材料确认成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "补全材料确认失败！");
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

            aeaHiApplyinstCorrectService.delelteAttFile(detailIds, attRealIninstId);
            return new ResultForm(true, "文件删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "文件删除失败！");
        }
    }

    @ApiOperation(value = "材料补全--> 上传电子件")
    @PostMapping("/att/upload")
    public ResultForm uploadFile(String attRealIninstId, HttpServletRequest request) {
        try {
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");
            aeaHiApplyinstCorrectService.uploadFile(attRealIninstId, request);
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
            return new ContentResultForm(true, aeaHiApplyinstCorrectService.getAttFiles(attRealIninstId), "Query attachment list success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null, "Query attachment list failed");
        }
    }


}
