package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.apply.ApplyinstCancelService;
import com.augurit.aplanmis.common.apply.vo.ApplyinstCancelInfoVo;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("rest/apply/cancel")
@Api(value = "撤回申报",tags = "撤回申报 --> 撤回申报接口")
public class RestApplyinstCancelInfoController {
    Logger logger= LoggerFactory.getLogger(RestApplyCommonController.class);

    @Autowired
    private ApplyinstCancelService applyinstCancelService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;


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
    @ApiOperation(value = "撤回申报 --> 检测申报实例或事项实例是否满足撤件申请接口")
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

    @GetMapping("uploadAttFile")
    @ApiOperation(value = "撤回申报 --> 删除文件接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件ID", name = "detailId", required = true, dataType = "string")
    })
        public ContentResultForm<Boolean> deleteAttFile(String detailId,HttpServletRequest request){
        try {
            boolean result=applyinstCancelService.deleteAttFile(detailId);
            return new ContentResultForm<>(result,result,"");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","删除文件接口异常");
        }
    }


}
