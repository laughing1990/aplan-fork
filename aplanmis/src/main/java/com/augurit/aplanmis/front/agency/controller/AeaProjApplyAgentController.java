package com.augurit.aplanmis.front.agency.controller;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AgencyState;
import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import com.augurit.aplanmis.common.domain.AeaServiceWindowUser;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.file.impl.FileUtilsServiceImpl;
import com.augurit.aplanmis.common.service.receive.utils.ReceivePDFTemplate;
import com.augurit.aplanmis.common.service.window.AeaProjApplyAgentService;
import com.augurit.aplanmis.common.vo.agency.AgencyDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
* 项目代办申请表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/proj/apply/agent")
@Api(value = "项目代办接口", tags = "项目代办接口")
public class AeaProjApplyAgentController {

    private static Logger logger = LoggerFactory.getLogger(AeaProjApplyAgentController.class);

    @Autowired
    private AeaProjApplyAgentService aeaProjApplyAgentService;

    @Autowired
    private FileUtilsService fileUtilsService;

    @ApiOperation(value = "项目代办 --> 跳转代办详情页面", notes = "项目代办 --> 跳转代办详情页面")
    @RequestMapping("agencyProjDetail")
    public ModelAndView agencyProjDetail(String applyAgentId) {
        ModelAndView modelAndView = new ModelAndView("agency/agencyProjDetail");
        modelAndView.addObject("applyAgentId",applyAgentId);
        return modelAndView;
    }

    @ApiOperation(value = "项目代办 --> 跳转并联申报页面", notes = "项目代办 --> 跳转并联申报页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoId",value = "项目ID",required = true,dataType = "String"),
            @ApiImplicitParam(name = "applyAgentId",value = "代办申请ID",required = true,dataType = "String")
    })
    @RequestMapping("toApplyIndex")
    public ModelAndView toApplyIndex(String projInfoId,String applyAgentId) {
        ModelAndView modelAndView = new ModelAndView("apply/index");
        modelAndView.addObject("projInfoId",projInfoId);
        modelAndView.addObject("applyAgentId",applyAgentId);
        modelAndView.addObject("isAgentProjApply",true);
        return modelAndView;
    }

    @ApiOperation(value = "项目代办 --> 保存代办信息或签订协议操作校验", notes = "项目代办 --> 保存代办信息或签订协议操作校验")
    @PostMapping("checkOpreatePermit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyAgentId",value = "代办申请ID",required = true,dataType = "String"),
            @ApiImplicitParam(name = "operateType",value = "操作类型，1表示保存代办信息操作，2表示签订协议操作",required = true,dataType = "String")
    })
    public ResultForm checkOpreatePermit(String applyAgentId,String operateType) {
        ResultForm resultForm = new ResultForm(false);
        try {
            Assert.notNull(applyAgentId,"代办申请ID参数不能为空。");
            Assert.notNull(operateType,"操作类型参数不能为空。");
            OpuOmUser currentUser = SecurityContext.getCurrentUser();
            String data = aeaProjApplyAgentService.queryRedisData(applyAgentId);
            if(StringUtils.isNotBlank(data)){
                AeaProjApplyAgent applyAgent = JSONObject.parseObject(data, AeaProjApplyAgent.class);
                if(!currentUser.getUserId().equals(applyAgent.getCurrentUserId())){
                    resultForm.setMessage("代办人员：" + applyAgent.getCurrentUserName() + " 正在操作该代办申请，请稍等。");
                    return resultForm;
                }
            }
            resultForm.setSuccess(true);
            if("2".equals(operateType)){
                //校验是否已保存过代办协议信息
                AeaProjApplyAgent agreementDetail = aeaProjApplyAgentService.getAgencyAgreementDetail(applyAgentId);
                if(agreementDetail == null){
                    resultForm.setSuccess(false);
                    resultForm.setMessage("数据库找不到该代办申请信息。");
                }else if(StringUtils.isBlank(agreementDetail.getAgreementCode())||StringUtils.isBlank(agreementDetail.getAgentUserId())||
                        StringUtils.isBlank(agreementDetail.getAgentUserName())||StringUtils.isBlank(agreementDetail.getAgentUserMobile())||agreementDetail.getAgreementSignTime()==null){
                    resultForm.setSuccess(false);
                    resultForm.setMessage("请先保存代办信息。");
                }
            }
            return resultForm;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            resultForm.setMessage("操作校验出错。" + e.getMessage());
            return resultForm;
        }
    }

    /**
     * 更新项目代办申请信息
     * @param aeaProjApplyAgent 项目代办申请
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveAeaProjApplyAgent")
    public ResultForm saveAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception {
        synchronized (AeaProjApplyAgentController.class) {
            ContentResultForm resultForm = new ContentResultForm(false);
            try {
                String applyAgentId = aeaProjApplyAgent.getApplyAgentId();
                if (StringUtils.isNotBlank(aeaProjApplyAgent.getApplyAgentId())) {
                    ResultForm form = checkOpreatePermit(applyAgentId, "1");
                    if(form.isSuccess()){
                        aeaProjApplyAgentService.updateAeaProjApplyAgent(aeaProjApplyAgent);
                        resultForm.setSuccess(true);
                    }
                    resultForm.setMessage(form.getMessage());
                }
                return resultForm;
            } catch (Exception e) {
                resultForm.setMessage("保存出错。" + e.getMessage());
                return resultForm;
            }
        }
    }

    @GetMapping("/getAgencyDetail")
    @ApiOperation(value = "项目代办 --> 代办详情", notes = "项目代办 --> 代办详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyAgentId",value = "代办申请ID",required = true,dataType = "String")
    })
    public ContentResultForm<AgencyDetailVo> getAgencyDetail(String applyAgentId){
        logger.debug("查询代办详情，查询关键字为{}", applyAgentId);
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            if(StringUtils.isNotBlank(applyAgentId)){
                AgencyDetailVo vo = aeaProjApplyAgentService.getAgencyDetail(applyAgentId);
                resultForm.setSuccess(true);
                resultForm.setContent(vo);
                resultForm.setMessage("查询成功。");
            }else {
                resultForm.setMessage("代办申请ID参数不能为空。");
            }
            return resultForm;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/checkAgreementCodeUnique")
    @ApiOperation(value = "检查协议编号是否唯一，true表示可用，false表示已被使用", notes = "检查协议编号是否唯一，true表示可用，false表示已被使用", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agreementCode",value = "协议编号",required = true,dataType = "String")
    })
    public ResultForm checkAgreementCodeUnique(String agreementCode){
        logger.debug("检查协议编号是否唯一，查询关键字为{}", agreementCode);
        try {
            boolean unique = aeaProjApplyAgentService.checkAgreementCodeUnique(agreementCode);
            return new ResultForm(unique,"查询成功。");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/getAgencyAgreement")
    @ApiOperation(value = "项目代办 --> 获取代办委托协议", notes = "项目代办 --> 获取代办委托协议", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyAgentId",value = "代办申请ID",required = true,dataType = "String")
    })
    public void getAgencyAgreement(String applyAgentId, HttpServletResponse resp){
        logger.debug("获取代办委托协议，查询关键字为{}", applyAgentId);
        try {
            if(StringUtils.isNotBlank(applyAgentId)){
                //先根据agreementCode找detailId，如果有则表示协议已存储进mongodb，根据detailId在mongodb获取协议文件
                AeaProjApplyAgent agreementDetail = aeaProjApplyAgentService.getAgencyAgreementDetail(applyAgentId);
                if(agreementDetail == null){
                    throw new Exception("代办申请不存在。");
                }
                String[] recordIds = {agreementDetail.getAgreementCode()};
                List<BscAttForm> forms = fileUtilsService.getAttachmentsByRecordId(recordIds, "AEA_PROJ_APPLY_AGENT", "AGREEMENT_CODE");
                if(forms != null && forms.size() > 0){
                    fileUtilsService.readFile(forms.get(0).getDetailId(),null,resp);
                }else{
                    //协议没有存储到mongodb，动态生成一份。代办中心（乙方）签章之后要将委托协议存储到mongodb。
                    String str = ReceivePDFTemplate.createAgencyAgreement(agreementDetail);
                    writeFile(str,resp);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    @GetMapping("/getAgencyStopAgreementFile")
    @ApiOperation(value = "项目代办 --> 获取代办委托终止单", notes = "项目代办 --> 获取代办委托终止单", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "aeaProjApplyAgent",value = "代办申请参数接收对象",required = true,dataType = "aeaProjApplyAgent")
    })
    public void getAgencyStopAgreementFile(AeaProjApplyAgent aeaProjApplyAgent, HttpServletResponse resp){
        logger.debug("获取代办委托终止单");
        try {
            Assert.notNull(aeaProjApplyAgent,"参数不能为空");
            String applyAgentId = aeaProjApplyAgent.getApplyAgentId();
            if(StringUtils.isNotBlank(applyAgentId)){
                AeaProjApplyAgent agreementDetail = aeaProjApplyAgentService.getAgencyAgreementDetail(applyAgentId);
                if(agreementDetail == null){
                    throw new Exception("代办申请不存在。");
                }
                String stopAgreementFileId = agreementDetail.getAgentStopAgreementFileId();
                if(StringUtils.isNotBlank(stopAgreementFileId)){
                    fileUtilsService.readFile(stopAgreementFileId,null,resp);
                }else{
                    agreementDetail.setAgreementStopReason(aeaProjApplyAgent.getAgreementStopReason());
                    agreementDetail.setAgreementStopTime(aeaProjApplyAgent.getAgreementStopTime());
                    //协议没有存储到mongodb，动态生成一份。代办中心（乙方）签章之后要将代办委托终止单存储到mongodb。
                    String str = ReceivePDFTemplate.createAgencyStopAgreement(agreementDetail);
                    writeFile(str,resp);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    @PostMapping("uploadAgreementFile")
    @ApiOperation(value = "代办申请 --> 乙方代办协议上传")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代办申请ID", name = "applyAgentId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "代办协议文件", name = "file", required = true, dataType = "MultipartFile")
    })
    public ContentResultForm uploadAgreementFile(String applyAgentId, @RequestParam("file") MultipartFile file) {
        synchronized (AeaProjApplyAgentController.class) {
            ContentResultForm resultForm = new ContentResultForm(false);
            try {
                Assert.notNull(applyAgentId, "代办申请ID不能为空。");
                Assert.notNull(file, "代办协议文件不能为空。");
                AeaProjApplyAgent agreementDetail = aeaProjApplyAgentService.getAgencyAgreementDetail(applyAgentId);
                if (agreementDetail == null) {
                    resultForm.setMessage("代办申请不存在。");
                } else if (Integer.valueOf(agreementDetail.getAgentApplyState()) >= 3) {
                    resultForm.setMessage("当前状态下不允许上传代办协议文件。");
                } else {
                    String agreementCode = agreementDetail.getAgreementCode();
                    aeaProjApplyAgentService.deleteAgreementFile(agreementCode);
                    fileUtilsService.upload("AEA_PROJ_APPLY_AGENT", "AGREEMENT_CODE", agreementCode, null, file);
                    agreementDetail.setAgentApplyState(AgencyState.APPLYER_SIGNING.getValue());
                    aeaProjApplyAgentService.updateAeaProjApplyAgent(agreementDetail);
                    resultForm.setSuccess(true);
                    resultForm.setMessage("上传成功。");
                }
                return resultForm;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return new ContentResultForm(false, "", "代办协议上传接口异常。");
            }
        }
    }

    @PostMapping("uploadAgentStopAgreementFile")
    @ApiOperation(value = "代办申请 --> 代办终止协议文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代办申请ID", name = "applyAgentId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "代办终止协议文件", name = "file", required = true, dataType = "MultipartFile")
    })
    public ContentResultForm uploadAgentStopAgreementFile(String applyAgentId, @RequestParam("file") MultipartFile file) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            Assert.notNull(applyAgentId,"代办申请ID不能为空。");
            Assert.notNull(file,"代办终止协议文件不能为空。");
            AeaProjApplyAgent agreementDetail = aeaProjApplyAgentService.getAgencyAgreementDetail(applyAgentId);
            if(agreementDetail == null){
                resultForm.setMessage("代办申请不存在。");
            }else{
                if(StringUtils.isNotBlank(agreementDetail.getAgentStopAgreementFileId())){
                    //删除原有的文件
                    fileUtilsService.deleteAttachment(agreementDetail.getAgentStopAgreementFileId());
                }
                BscAttForm bscAttForm = fileUtilsService.upload("AEA_PROJ_APPLY_AGENT", "APPLY_AGENT_ID", applyAgentId, null, file);
                agreementDetail.setAgentStopAgreementFileId(bscAttForm.getDetailId());
                aeaProjApplyAgentService.updateAeaProjApplyAgent(agreementDetail);
                resultForm.setSuccess(true);
                resultForm.setMessage("上传成功。");
            }
            return resultForm;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","代办终止协议文件上传接口异常。");
        }
    }

    @PostMapping("uploadAgentEndAgreementFile")
    @ApiOperation(value = "代办申请 --> 代办结束办结单文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代办申请ID", name = "applyAgentId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "代办结束办结单文件", name = "file", required = true, dataType = "MultipartFile")
    })
    public ContentResultForm uploadAgentEndAgreementFile(String applyAgentId, @RequestParam("file") MultipartFile file) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            Assert.notNull(applyAgentId,"代办申请ID不能为空。");
            Assert.notNull(file,"代办结束办结单文件不能为空。");
            AeaProjApplyAgent agreementDetail = aeaProjApplyAgentService.getAgencyAgreementDetail(applyAgentId);
            if(agreementDetail == null){
                resultForm.setMessage("代办申请不存在。");
            }else{
                if(StringUtils.isNotBlank(agreementDetail.getAgentEndAgreementFileId())){
                    //删除原有的文件
                    fileUtilsService.deleteAttachment(agreementDetail.getAgentEndAgreementFileId());
                }
                BscAttForm bscAttForm = fileUtilsService.upload("AEA_PROJ_APPLY_AGENT", "APPLY_AGENT_ID", applyAgentId, null, file);
                agreementDetail.setAgentEndAgreementFileId(bscAttForm.getDetailId());
                aeaProjApplyAgentService.updateAeaProjApplyAgent(agreementDetail);
                resultForm.setSuccess(true);
                resultForm.setMessage("上传成功。");
            }
            return resultForm;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","代办结束办结单文件上传接口异常。");
        }
    }

    @GetMapping("/getCurrAgencyWinUserList")
    @ApiOperation(value = "获取当前登录用户所在代办中心的代办人员", notes = "获取当前登录用户所在代办中心的代办人员", httpMethod = "GET")
    public ContentResultForm<List<AeaServiceWindowUser>> getCurrAgencyWinUserList(){
        logger.debug("获取当前登录用户所在代办中心的代办人员");
        try {
            List<AeaServiceWindowUser> userList = aeaProjApplyAgentService.getCurrAgencyWinUserList();
            return new ContentResultForm<>(true,userList,"查询成功。");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false,null, e.getMessage());
        }
    }

    private void writeFile(String filePath,HttpServletResponse resp)throws Exception{
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream input = new FileInputStream(file);
            FileUtilsServiceImpl.writeContent(resp,input,file.getName());
            if (file.isFile()) {
                file.delete();
            }
        }
    }

}
