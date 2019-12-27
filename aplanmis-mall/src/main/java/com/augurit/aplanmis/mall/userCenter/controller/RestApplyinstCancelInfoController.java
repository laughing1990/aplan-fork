package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.apply.vo.ApplyinstCancelInfoVo;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyinstCancelService;
import com.augurit.aplanmis.mall.userCenter.service.RestFileService;
import com.augurit.aplanmis.mall.userCenter.vo.CurrentLinkmansVo;
import com.augurit.aplanmis.supermarket.linkmanInfo.service.AeaLinkmanInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rest/apply/cancel")
@Api(value = "撤回申报",tags = "撤回申报 --> 撤回申报接口")
public class RestApplyinstCancelInfoController {
    Logger logger= LoggerFactory.getLogger(RestApplyCommonController.class);

    @Autowired
    private RestApplyinstCancelService applyinstCancelService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private RestFileService restFileService;


    @GetMapping("check/{applyinstId}")
    @ApiOperation(value = "撤回申报 --> 检测申报实例或事项实例是否满足撤件申请接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "申请实例ID", name = "applyinstId", required = true, dataType = "string")
    })
    public ContentResultForm<String> checkApplyinstAndIteminstState(@PathVariable("applyinstId")String applyinstId){
        try {
            String checkResult=applyinstCancelService.checkApplyinstAndIteminstState(applyinstId);
            return new ContentResultForm<>(true,checkResult,"");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","检测申报实例或事项实例是否满足撤件申请接口异常");
        }
    }


    @PostMapping("createApplyinstCancelInfo")
    @ApiOperation(value = "撤回申报 --> 保存撤件申请接口")
    public ContentResultForm<String> createApplyinstCancelInfo(ApplyinstCancelInfoVo applyinstCancelInfoVo){
        try {
            if(StringUtils.isBlank(applyinstCancelInfoVo.getApplyUserId())){
                AeaLinkmanInfo query=new AeaLinkmanInfo();
                query.setLinkmanCertNo(applyinstCancelInfoVo.getApplyUserIdnumber());
                List<AeaLinkmanInfo> linkmans = aeaLinkmanInfoService.listAeaLinkmanInfo(query);
                if(linkmans.size()>0){
                    for (AeaLinkmanInfo linkmanInfo:linkmans){
                        if(linkmanInfo.getLinkmanName().equals(applyinstCancelInfoVo.getApplyUserName())){
                            applyinstCancelInfoVo.setApplyUserId(linkmanInfo.getLinkmanInfoId());
                            break;
                        }
                    }
                }else{
                    AeaLinkmanInfo param=new AeaLinkmanInfo();
                    param.setLinkmanInfoId(UUID.randomUUID().toString());
                    param.setLinkmanType("u");
                    param.setLinkmanName(applyinstCancelInfoVo.getApplyUserName());
                    param.setLinkmanMobilePhone(applyinstCancelInfoVo.getApplyUserPhone());
                    param.setIsDeleted("0");
                    param.setLinkmanCertNo(applyinstCancelInfoVo.getApplyUserIdnumber());
                    param.setRootOrgId(SecurityContext.getCurrentOrgId());
                    param.setIsActive("1");
                    param.setCreateTime(new Date());
                    param.setCreater(SecurityContext.getCurrentUserName());
                    aeaLinkmanInfoService.saveAeaLinkmanInfo(param);
                    applyinstCancelInfoVo.setApplyUserId(param.getLinkmanInfoId());
                }
            }
            String cResult=applyinstCancelService.createApplyinstCancelInfo(applyinstCancelInfoVo);
            if(StringUtils.isBlank(cResult)){
                return new ContentResultForm<>(true,cResult,"");
            }else{
                return new ContentResultForm<>(false,cResult,cResult);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","检测申报实例或事项实例是否满足撤件申请接口异常");
        }
    }




    @GetMapping("uploadAttFile")
    @ApiOperation(value = "撤回申报 --> 文件上传接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "附件ID", name = "attId", required = true, dataType = "string")
    })
    public ContentResultForm<String> uploadAttFile(String attId,HttpServletRequest request){
        try {
            String result=applyinstCancelService.uploadAttFile(attId,"AEA_HI_APPLYINST_CANCEL",request);
            return new ContentResultForm<>(true,result,"");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","文件上传接口异常");
        }
    }

    @GetMapping("deleteAttFile/{detailId}")
    @ApiOperation(value = "撤回申报 --> 删除文件接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件ID", name = "detailId", required = true, dataType = "string")
    })
        public ResultForm deleteAttFile(@PathVariable String detailId){
        try {

            boolean result=applyinstCancelService.deleteAttFile(detailId);
            return new ResultForm(result,"");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"删除文件接口异常");
        }
    }

    @GetMapping("/getAttFiles/{attId}")
    @ApiOperation("撤回申报 --> 获取文件列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "attId", value = "附件ID", required = true, type = "string")})
    public ResultForm getAttFiles(@PathVariable String attId) throws Exception {
        if ("null".equalsIgnoreCase(attId) || "undefined".equalsIgnoreCase(attId) || StringUtils.isBlank(attId))
            return new ContentResultForm<>(true, new ArrayList<>());
        return new ContentResultForm<>(true, restFileService.getAttFiles(attId));
    }

    @GetMapping("/getCurrentLinkmans")
    @ApiOperation("撤回申报 --> 获取当前用户的联系人列表")
    public ResultForm getCurrentLinkmans(HttpServletRequest request) throws Exception {
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
        List<AeaLinkmanInfo> list=new ArrayList<>();
        if(StringUtils.isNotBlank(loginVo.getUserId())){
            AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getOneById(loginVo.getUserId());
            list.add(aeaLinkmanInfo);
        }else{
            list = aeaLinkmanInfoService.getAeaLinkmanInfoByUnitInfoId(loginVo.getUnitId(), null);
        }
        return new ContentResultForm<>(true, list.size()>0?list.stream().map(CurrentLinkmansVo::format).collect(Collectors.toList()):new ArrayList<>());
    }

}
