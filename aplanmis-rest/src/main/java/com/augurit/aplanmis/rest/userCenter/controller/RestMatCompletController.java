package com.augurit.aplanmis.rest.userCenter.controller;

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
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.userCenter.service.RestApproveService;
import com.augurit.aplanmis.rest.userCenter.vo.ApplyDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/rest/user/matComplete")
@Api(tags = "法人空间 --> 材料补全相关接口")
public class RestMatCompletController {
    Logger logger = LoggerFactory.getLogger(RestMatCompletController.class);


    @Autowired
    RestApproveService restApproveService;
    @Autowired
    private AeaHiApplyinstCorrectService aeaHiApplyinstCorrectService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @GetMapping("/matCompletDetail/{applyinstId}/{projInfoId}/{isSeriesApprove}")
    @ApiOperation(value = "材料补全详情信息接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例Id", name = "applyinstId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "1:单项 0:并联", name = "isSeriesApprove", required = true, dataType = "string")})
    public ContentResultForm<ApplyDetailVo> getApplyDetailByApplyinstIdAndProjInfoId(@PathVariable("projInfoId") String projInfoId, @PathVariable("applyinstId") String applyinstId, @PathVariable("isSeriesApprove") String isSeriesApprove, HttpServletRequest request) {
        try {
            return new ContentResultForm<>(true, restApproveService.getApplyDetailByApplyinstIdAndProjInfoId(applyinstId, projInfoId, isSeriesApprove, null, request));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "查询材料补全详情信息接口发生异常");
        }
    }

    /*@GetMapping("tomatCompletionListPage")
    @ApiOperation(value = "跳转材料补全页面")
    public ModelAndView toMatCompletPage() {
        return new ModelAndView("mall/userCenter/components/matCompletionList");
    }*/

    @GetMapping("/matComplet/list")
    @ApiOperation(value = "材料补全 --> 材料补全列表查询接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "页面数量", name = "pageNum", required = true, dataType = "string"),
            @ApiImplicitParam(value = "页面页数", name = "pageSize", required = true, dataType = "string")})
    public ResultForm searchMatComplet(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        try {
            AuthUser loginInfo = AuthContext.getCurrentUser();
            if (loginInfo.isPersonalAccount()) {//个人
                return new ContentResultForm<>(true, restApproveService.searchMatComplet("", loginInfo.getLinkmanInfoId(), pageNum, pageSize));
            } else if (StringUtils.isNotBlank(loginInfo.getLinkmanInfoId())) {//委托人
                return new ContentResultForm<>(true, restApproveService.searchMatComplet(loginInfo.getUnitInfoId(), loginInfo.getLinkmanInfoId(), pageNum, pageSize));
            } else {//企业
                return new ContentResultForm<>(true, restApproveService.searchMatComplet(loginInfo.getUnitInfoId(), "", pageNum, pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "查材料补全列表查询接口异常");
        }
    }

    @GetMapping("/getLackMats")
    @ApiOperation("材料补全 --> 查询少交和已交的材料清单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id,存在多个用逗号分隔", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getLackMatsByApplyinstIdAndIteminstId(@RequestParam String applyinstId) {

        Assert.notNull(applyinstId, "申请实例ID为空！");
        try {
            AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if ("3".equals(applyinst.getApplyinstState()) || "4".equals(applyinst.getApplyinstState())) {
                AeaHiApplyinstCorrect aeaHiApplyinstCorrect = aeaHiApplyinstCorrectService.getAeaHiApplyinstCorrectById(applyinstId);
                if (aeaHiApplyinstCorrect == null) throw new Exception("找不到补全记录！");
                return new ContentResultForm<>(false, aeaHiApplyinstCorrectService.getMatCorrectInfo(aeaHiApplyinstCorrect.getApplyinstCorrectId()), "该事项当前状态处于补件中");
            }

            MatCorrectInfoDto matCorrectInfoDto = aeaHiApplyinstCorrectService.getLackMatsByApplyinstId(applyinstId);
            return new ContentResultForm<>(true, matCorrectInfoDto, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "获取材料清单失败！");
        }
    }


    @PostMapping("/createMatCorrectinst")
    @ApiOperation(value = "材料补全 --> 创建材料补全实例", httpMethod = "POST")
    public ResultForm createMatCorrectinst(ApplyinstCorrectinstDto correctinstDto) {

        Assert.notNull(correctinstDto.getApplyinstId(), "申请实例ID为空！");
        Assert.notNull(correctinstDto.getIsFlowTrigger(), "流程触发标志为空！");
        Assert.notNull(correctinstDto.getCorrectDueDays(), "补全天数为空！");
        Assert.notNull(correctinstDto.getMatCorrectDtosJson(), "材料补全清单为空！");

        try {
            aeaHiApplyinstCorrectService.createMatCorrectinst(correctinstDto);
            return new ResultForm(true, "已创建材料补全实例！");
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
            return new ContentResultForm<>(false, null, "获取材料补全实例信息失败！");
        }
    }


    @PostMapping("/saveMatCorrectInfo")
    @ApiOperation(value = "材料补全 --> 保存材料补全实例", httpMethod = "POST")
    public ResultForm saveMatCorrectInfo(MatCorrectinstDto matCorrectinstDto) {

        Assert.notNull(matCorrectinstDto.getCorrectId(), "材料补全实例ID为空！");
        Assert.notNull(matCorrectinstDto.getCorrectState(), "材料补全实例状态为空！");

        try {
            aeaHiApplyinstCorrectService.saveMatCorrectInfo(matCorrectinstDto);
            return new ResultForm(true, "已保存材料补全实例！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "保存材料补全实例失败！");
        }
    }

    @GetMapping("/getItemCorrectRealIninst")
    @ApiOperation("材料补全 --> 获取待确认的补全材料列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstCorrectId", value = "材料补全实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ContentResultForm getItemCorrectRealIninst(@RequestParam String applyinstCorrectId) {
        Assert.notNull(applyinstCorrectId, "材料补全实例id为空！");
        try {
            return new ContentResultForm<>(true, aeaHiApplyinstCorrectService.getApplyinstCorrectRealIninst(applyinstCorrectId), "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "获取待确认的补全材料列表失败！");
        }
    }

    @PostMapping("/matCorrectConfirm")
    @ApiOperation(value = "材料补全 --> 确认补全材料列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "correctId", value = "材料补全实例ID")
            , @ApiImplicitParam(name = "correctState", value = "材料补全实例状态")
            , @ApiImplicitParam(name = "correctMemo", value = "备注")
            , @ApiImplicitParam(name = "matCorrectDtosJson", value = "材料补全列表")
    })
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

    @DeleteMapping("/att/delelte")
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
    public ContentResultForm<List<BscAttFileAndDir>> attFileList(@RequestParam String attRealIninstId) {
        try {
            if (StringUtils.isBlank(attRealIninstId)) throw new Exception("attRealIninstId为空！");
            return new ContentResultForm<>(true, aeaHiApplyinstCorrectService.getAttFiles(attRealIninstId), "Query attachment list success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "Query attachment list failed");
        }
    }
}
