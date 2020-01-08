package com.augurit.aplanmis.front.agency.controller;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 保存或编辑项目代办申请
     * @param aeaProjApplyAgent 项目代办申请
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveAeaProjApplyAgent")
    public ResultForm saveAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception {
        try {
            if(StringUtils.isNotBlank(aeaProjApplyAgent.getApplyAgentId())){
                aeaProjApplyAgentService.updateAeaProjApplyAgent(aeaProjApplyAgent);
            }else {
                aeaProjApplyAgentService.saveAeaProjApplyAgent(aeaProjApplyAgent);
            }
        }catch (Exception e){
            return  new ContentResultForm(false,null,e.getMessage());
        }
        return  new ContentResultForm(true,aeaProjApplyAgent);
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
    @ApiOperation(value = "项目代办 --> 代办委托协议", notes = "项目代办 --> 代办委托协议", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyAgentId",value = "代办申请ID",required = true,dataType = "String")
    })
    public void getAgencyAgreement(String applyAgentId, HttpServletResponse resp){
        logger.debug("获取代办委托协议，查询关键字为{}", applyAgentId);
        try {
            if(StringUtils.isNotBlank(applyAgentId)){
                //先根据agreementCode找detailId，如果有则表示协议已存储进mongodb，根据detailId在mongodb获取协议文件
                AeaProjApplyAgent agreement = aeaProjApplyAgentService.getAgencyAgreementDetail(applyAgentId);
                if(agreement == null){
                    throw new Exception("代办申请不存在。");
                }
                String[] recordIds = {agreement.getAgreementCode()};
                List<BscAttForm> forms = fileUtilsService.getAttachmentsByRecordId(recordIds, "AEA_PROJ_APPLY_AGENT", "AGREEMENT_CODE");
                if(forms != null && forms.size() > 0){
                    fileUtilsService.readFile(forms.get(0).getDetailId(),null,resp);
                }else{
                    //协议没有存储到mongodb，动态生成一份。代办中心（乙方）签章之后要将委托协议存储到mongodb。
                    AeaProjApplyAgent agreementDetail = aeaProjApplyAgentService.getAgencyAgreementDetail(applyAgentId);
                    String str = ReceivePDFTemplate.createAgencyAgreement(agreementDetail);
                    //读取指定路径下的pdf文件
                    File file = new File(str);
                    if (file.exists()) {
                        FileInputStream input = new FileInputStream(file);
                        FileUtilsServiceImpl.writeContent(resp,input,file.getName());
                        if (file.isFile()) {
                            file.delete();
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    @PostMapping("uploadAgreementFile")
    @ApiOperation(value = "代办申请 --> 代办协议上传")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "代办申请ID", name = "applyAgentId", required = true, dataType = "string")
    })
    public ContentResultForm uploadFile(String applyAgentId, HttpServletRequest request) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            AeaProjApplyAgent agreementDetail = aeaProjApplyAgentService.getAgencyAgreementDetail(applyAgentId);
            if(agreementDetail == null){
                resultForm.setMessage("代办申请不存在。");
            }else{
                String agreementCode = agreementDetail.getAgreementCode();
                aeaProjApplyAgentService.deleteAgreementFile(agreementCode);
                fileUtilsService.uploadAttachments("AEA_PROJ_APPLY_AGENT", "AGREEMENT_CODE", agreementCode,null, request);
                resultForm.setSuccess(true);
                resultForm.setMessage("上传成功。");
            }
            return resultForm;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","代办协议上传接口异常。");
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


}
