package com.augurit.aplanmis.mall.userCenter.controller;

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
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApproveService;
import com.augurit.aplanmis.mall.userCenter.service.RestFileService;
import com.augurit.aplanmis.mall.userCenter.service.RestMatCompletService;
import com.augurit.aplanmis.mall.userCenter.vo.ApplyDetailVo;
import com.augurit.aplanmis.mall.userCenter.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("rest/user")
@Api(value = "", tags = "法人空间 --> 材料补全相关接口")
public class RestMatCompletController {
    Logger logger= LoggerFactory.getLogger(RestMatCompletController.class);


    @Autowired
    RestApproveService restApproveService;
    @Autowired
    private AeaHiApplyinstCorrectService aeaHiApplyinstCorrectService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private RestFileService restFileService;
    @Autowired
    private RestMatCompletService restMatCompletService;

    @GetMapping("matCompletDetail/{applyinstId}/{projInfoId}/{isSeriesApprove}")
    @ApiOperation(value = "材料补全详情信息接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例Id",name = "applyinstId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "项目ID",name = "projInfoId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "1:单项 0:并联",name = "isSeriesApprove",required = true,dataType = "string")})
    public ContentResultForm<ApplyDetailVo> getApplyDetailByApplyinstIdAndProjInfoId(@PathVariable("projInfoId") String projInfoId, @PathVariable("applyinstId") String applyinstId, @PathVariable("isSeriesApprove")String isSeriesApprove, HttpServletRequest request){
        try {
            return new ContentResultForm<>(true,restApproveService.getApplyDetailByApplyinstIdAndProjInfoId(applyinstId,projInfoId,isSeriesApprove,null,request));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","查询材料补全详情信息接口发生异常");
        }
    }

    @GetMapping("tomatCompletionListPage")
    @ApiOperation(value = "跳转材料补全页面")
    public ModelAndView toMatCompletPage(){
        return new ModelAndView("mall/userCenter/components/matCompletionList");
    }

    @GetMapping("matComplet/list")
    @ApiOperation(value = "材料补全 --> 材料补全列表查询接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "int"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "int"),
            @ApiImplicitParam(value = "搜索关键词",name = "keyword",required = false,dataType = "String")})
    public ResultForm searchMatComplet(int pageNum, int pageSize,String keyword,HttpServletRequest request){
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                return new ContentResultForm<>(true,restApproveService.searchMatComplet("",loginInfo.getUserId(),keyword,pageNum,pageSize));
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                return new ContentResultForm<>(true,restApproveService.searchMatComplet(loginInfo.getUnitId(),loginInfo.getUserId(),keyword,pageNum,pageSize));
            }else{//企业
                return new ContentResultForm<>(true,restApproveService.searchMatComplet(loginInfo.getUnitId(),"",keyword,pageNum,pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"查材料补全列表查询接口异常");
        }
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
                AeaHiApplyinstCorrect aeaHiApplyinstCorrect = aeaHiApplyinstCorrectService.getAeaHiApplyinstCorrectById(applyinstId);
                if (aeaHiApplyinstCorrect == null) throw new Exception("找不到补全记录！");
                return new ContentResultForm(false, aeaHiApplyinstCorrectService.getMatCorrectInfo(aeaHiApplyinstCorrect.getApplyinstCorrectId()), "该事项当前状态处于补件中");
            }

            MatCorrectInfoDto matCorrectInfoDto = aeaHiApplyinstCorrectService.getLackMatsByApplyinstId(applyinstId);
            return new ContentResultForm<>(true, matCorrectInfoDto, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null, "获取材料清单失败！");
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


    @GetMapping("/getMatCompCorrectInfo")
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

    @RequestMapping("/saveMatCompCorrectInfo")
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

    @GetMapping("/getItemCompCorrectRealIninst")
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

    @PostMapping("/matCompCorrectConfirm")
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

    @PostMapping("/comp/att/delelte")
    @ApiOperation(value = "材料补全页面--> 删除电子件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", paramType = "query", required = true)
    })
    public ResultForm delelteAttFile(String detailIds, String attRealIninstId) {
        try {
            Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");

            restMatCompletService.delelteAttFile(detailIds, attRealIninstId);
            return new ResultForm(true, "文件删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "文件删除失败！");
        }
    }

    @ApiOperation(value = "材料补全--> 上传电子件")
    @PostMapping("/comp/att/upload")
    public ResultForm uploadFile(String attRealIninstId, HttpServletRequest request) {
        try {
            if (!restFileService.isAllowFileType(request))return new ResultForm(false, "不允许的文件类型");
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");
            aeaHiApplyinstCorrectService.uploadFile(attRealIninstId, request);
            return new ResultForm(true, "文件上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "文件上传失败！");
        }
    }

    @GetMapping("/comp/att/list")
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

    @ApiOperation(value = "材料补正页面--> 从我的材料或云盘上传电子件")
    @PostMapping("comp/att/uploadFileByCloud")
    @ApiImplicitParams({@ApiImplicitParam(name = "attRealIninstId", value = "材料补正实例ID", required = false, type = "string"),
            @ApiImplicitParam(name = "detailIds", value = "文件ID（英文状态逗号分隔）", required = true, type = "string")})
    public ResultForm uploadFileByCloud(String attRealIninstId, String detailIds) {
        try {
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");
            Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");
            restMatCompletService.uploadFileByCloud(attRealIninstId, detailIds);
            return new ContentResultForm<>(false,attRealIninstId, "文件上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "文件上传失败！");
        }
    }

    @GetMapping("comp/allApplyObject")
    @ApiOperation(value = "材料补全 --> 根据材料补全实例ID查询所有申报主体及建设单位或个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "材料补全实例ID", name = "applyinstCorrectId", required = false, dataType = "string")
    })
    public ContentResultForm<UserInfoVo> getAllApplyObject(HttpServletRequest request, String applyinstCorrectId) {
        try {
            return new ContentResultForm<>(true, restMatCompletService.getAllApplyObject(request,applyinstCorrectId));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","查询申报主体接口发生异常");
        }
    }
}
