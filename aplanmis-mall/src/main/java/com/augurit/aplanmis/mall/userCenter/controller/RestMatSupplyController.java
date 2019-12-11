package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrect;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.dto.CorrectinstDto;
import com.augurit.aplanmis.common.dto.MatCorrectInfoDto;
import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.mat.RestMatCorrectCommonService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApproveService;
import com.augurit.aplanmis.mall.userCenter.service.RestFileService;
import com.augurit.aplanmis.mall.userCenter.service.RestMatSupplyService;
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
@Api(value = "", tags = "法人空间 --> 材料补正相关接口")
public class RestMatSupplyController {
    Logger logger= LoggerFactory.getLogger(RestMatSupplyController.class);

    @Autowired
    private RestApproveService restApproveService;
    @Autowired
    private RestMatCorrectCommonService restMatCorrectCommonService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private RestMatSupplyService restMatSupplyService;
    @Autowired
    private RestFileService restFileService;


    @GetMapping("tomatSupplementListPage")
    @ApiOperation(value = "跳转材料补正页面")
    public ModelAndView tomatSupplementListPage(){
        return new ModelAndView("mall/userCenter/components/matSupplementList");
    }


    @GetMapping("matSupply/list")
    @ApiOperation(value = "材料补正 --> 材料补正列表查询接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "页面数量",name = "pageNum",required = true,dataType = "int"),
            @ApiImplicitParam(value = "页面页数",name = "pageSize",required = true,dataType = "int"),
            @ApiImplicitParam(value = "搜索关键字",name = "keyword",required = true,dataType = "string")})
    public ResultForm searchSupplementInfo(int pageNum, int pageSize,String keyword, HttpServletRequest request){
        try {
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            return new ContentResultForm<>(true, restApproveService.searchSupplyInfoList(loginInfo.getUnitId(), loginInfo.getUserId(), pageNum, pageSize,keyword));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"查材料补正列表查询接口异常");
        }
    }

    @GetMapping("/getLackMats")
    @ApiOperation("材料补正 --> 查询少交和已交的材料清单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "iteminstId", value = "事项实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getLackMatsByApplyinstIdAndIteminstId(@RequestParam String applyinstId, @RequestParam String iteminstId) {

        Assert.notNull(applyinstId, "申请实例ID为空！");
        Assert.notNull(iteminstId, "事项实例ID为空！");
        try {

            AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);
            if ("6".equals(iteminst.getIteminstState()) || "7".equals(iteminst.getIteminstState()))
                return new ContentResultForm(false, "", "该事项当前状态处于补件中");
            if ("9".equals(iteminst.getIteminstState()) || "10".equals(iteminst.getIteminstState()))
                return new ContentResultForm(false, null, "该事项当前状态处于特别程序中");
            MatCorrectInfoDto matCorrectInfoDto = restMatCorrectCommonService.getLackMatsByApplyinstIdAndIteminstId(applyinstId, iteminstId);
            return new ContentResultForm<>(true, matCorrectInfoDto, "Query success");
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

        Assert.notNull(correctId, "材料补正实例ID为空！");
        try {
            AeaHiItemCorrect aeaHiItemCorrect = restMatCorrectCommonService.getMatCorrectInfo(correctId);
            return new ContentResultForm<>(true, aeaHiItemCorrect, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null, "获取材料补正实例信息失败！");
        }
    }

    @PostMapping("/createMatCorrectinst")
    @ApiOperation(value = "材料补正 --> 创建材料补正实例", httpMethod = "POST")
    public ResultForm createMatCorrectinst(CorrectinstDto correctinstDto) {

        Assert.notNull(correctinstDto.getApplyinstId(), "申请实例ID为空！");
        Assert.notNull(correctinstDto.getIteminstId(), "事项实例ID为空！");
        Assert.notNull(correctinstDto.getIsFlowTrigger(), "流程触发标志为空！");
        Assert.notNull(correctinstDto.getCorrectDueDays(), "补正天数为空！");
        Assert.notNull(correctinstDto.getMatCorrectDtosJson(), "材料补正清单为空！");

        try {
            restMatCorrectCommonService.createMatCorrectinst(correctinstDto);
            return new ResultForm(true, "已创建材料补正实例！");
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
            restMatSupplyService.saveMatCorrectInfo(matCorrectinstDto);
            return new ResultForm(true, "已保存材料补正实例！");
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
        Assert.notNull(correctId, "材料补正实例id为空！");
        try {
            return new ContentResultForm<>(true, restMatCorrectCommonService.getItemCorrectRealIninst(correctId), "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null, "获取待确认的补正材料列表失败！");
        }
    }

    @PostMapping("/matCorrectConfirm")
    @ApiOperation(value = "材料补正 --> 确认补正材料列表", httpMethod = "POST")
    public ResultForm matCorrectConfirm(String correctId, String correctState, String correctMemo, String matCorrectDtosJson) {
        Assert.notNull(correctId, "材料补正实例ID为空！");
        Assert.notNull(correctState, "材料补正实例状态为空！");
        Assert.notNull(matCorrectDtosJson, "材料补正列表为空！");
        try {
            restMatCorrectCommonService.matCorrectConfirm(correctId, correctState, correctMemo, matCorrectDtosJson);
            return new ResultForm(true, "补正材料确认成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "补正材料确认失败！");
        }
    }

    @GetMapping("/att/list")
    @ApiOperation("材料补正--> 查询已上传附件列表")
    @ApiImplicitParam(name = "attRealIninstId", value = "材料实例id")
    public ContentResultForm<List<BscAttFileAndDir>> attFileList(@RequestParam(required = false) String attRealIninstId) {
        try {
            if (StringUtils.isBlank(attRealIninstId)) throw new Exception("attRealIninstId为空！");
            return new ContentResultForm(true, restMatCorrectCommonService.getAttFiles(attRealIninstId), "Query attachment list success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null, "Query attachment list failed");
        }
    }
    @PostMapping("/att/delelte")
    @ApiOperation(value = "材料补正页面--> 删除电子件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", paramType = "query", required = true)
    })
    public ResultForm delelteAttFile(String detailIds, String attRealIninstId) {
        try {
            Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");

            restMatSupplyService.delelteAttFile(detailIds, attRealIninstId);
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
            if (!restFileService.isAllowFileType(request))return new ResultForm(false, "不允许的文件类型");
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");
            restMatSupplyService.uploadFile(attRealIninstId, request);
            return new ResultForm(true, "文件上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "文件上传失败！");
        }
    }

    @ApiOperation(value = "材料补正页面--> 从我的材料或云盘上传电子件")
    @PostMapping("/att/uploadFileByCloud")
    @ApiImplicitParams({@ApiImplicitParam(name = "attRealIninstId", value = "材料补正实例ID", required = false, type = "string"),
            @ApiImplicitParam(name = "detailIds", value = "文件ID（英文状态逗号分隔）", required = true, type = "string")})
    public ResultForm uploadFileByCloud(String attRealIninstId, String detailIds) {
        try {
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");
            Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");
            restMatSupplyService.uploadFileByCloud(attRealIninstId, detailIds);
            return new ContentResultForm<>(false,attRealIninstId, "文件上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "文件上传失败！");
        }
    }



}
