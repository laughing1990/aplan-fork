package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyCommonService;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyMatService;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyService;
import com.augurit.aplanmis.mall.userCenter.service.RestFileService;
import com.augurit.aplanmis.mall.userCenter.vo.*;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("rest/apply/common")
@Api(value = "申报通用接口",tags = "申报 --> 申报通用接口")
public class RestApplyCommonController {
    Logger logger= LoggerFactory.getLogger(RestApplyCommonController.class);

    @Autowired
    AeaProjInfoService aeaProjInfoService;
    @Autowired
    RestApplyService restApplyService;
    @Autowired
    FileUtilsService fileUtilsService;
    @Autowired
    AeaHiSmsInfoService aeaHiSmsInfoService;
    @Autowired
    AeaItemStateService aeaItemStateService;
    @Autowired
    RestApplyMatService restApplyMatService;
    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private RestApplyCommonService restApplyCommonService;
    @Autowired
    private RestFileService restFileService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaParThemeService aeaParThemeService;



    @GetMapping("projInfo/{projInfoId}")
    @ApiOperation(value = "阶段申报 --> 查询项目基础信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", required = true, dataType = "string")
    })
    public ContentResultForm<AeaProjInfo> getProjInfoByProjInfoId(@PathVariable("projInfoId")String projInfoId,HttpServletRequest request){
        try {

            AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
            if(StringUtils.isNotBlank(aeaProjInfo.getThemeId())){
                AeaParTheme theme = aeaParThemeService.getAeaParThemeByThemeId(aeaProjInfo.getThemeId());
                if(theme!=null) {
                    aeaProjInfo.setThemeType(theme.getThemeType());
                    AeaParThemeVer themeVer = aeaParThemeService.getAeaParThemeVerByThemeIdAndVerNum(theme.getThemeId(),null, SecurityContext.getCurrentOrgId());
                    theme.setThemeVerId(themeVer==null?"":themeVer.getThemeVerId());
                }
            }
            aeaProjInfo.setAeaUnitInfos(restApplyService.getAeaUnitInfosByProjInfoId(projInfoId,request));
            return new ContentResultForm<>(true,aeaProjInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","查询项目基础信息接口发生异常");
        }
    }

    @GetMapping("applyObject")
    @ApiOperation(value = "阶段申报/单项申报 --> 查询申报主体接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "项目ID(用于带出人员设置信息)", name = "projInfoId", required = false, dataType = "string")
    })
    public ContentResultForm<UserInfoVo> getApplyObject(HttpServletRequest request, String projInfoId) {
        try {
            return new ContentResultForm<>(true, restApplyService.getApplyObject(request, projInfoId));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","查询申报主体接口发生异常");
        }
    }




    @PostMapping("/completioninfo/saveOrUpdate")
    @ApiOperation(value = "并联申报/单项申报 --> 补全信息保存领件人、项目信息、单位项目关联", httpMethod = "POST")
    public ContentResultForm saveOrUpdateSmsInfo(@RequestBody SmsInfoVo smsInfoVo) {
        Map<String,Object> resultMap=new HashMap();
        AeaProjInfo aeaProjInfo= new AeaProjInfo();
        BeanUtils.copyProperties(smsInfoVo,aeaProjInfo);
        String applyinstId=smsInfoVo.getApplyinstId();//有值则表示之前已经暂存过
        try {
            //保存或修改领件人信息
            Map<String, Object> map = restApplyCommonService.getStringObjectMap(smsInfoVo, resultMap, aeaProjInfo);
            if(StringUtils.isNotBlank(applyinstId)){//已暂存过
                restApplyCommonService.insertAeaApplyinstUnitProj(applyinstId,(List<String>)map.get("unitProjIds"));
            }
            return new ContentResultForm<>(true, resultMap, "保存成功!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "", e.getMessage());
        }
    }

    @PostMapping("/completioninfo/saveOrUpdate/temporary")
    @ApiOperation(value = "并联申报/单项申报 --> 暂存补全信息保存领件人、项目信息、单位项目关联", httpMethod = "POST")
    public ContentResultForm temporarySaveOrUpdateSmsInfo(@RequestBody SmsInfoVo smsInfoVo,HttpServletRequest request) {
        Map<String,Object> resultMap=new HashMap();
        AeaProjInfo aeaProjInfo= new AeaProjInfo();
        BeanUtils.copyProperties(smsInfoVo,aeaProjInfo);
        String applyinstId=smsInfoVo.getApplyinstId();//有值则表示之前已经暂存过
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
        try {
            Map<String, Object> map = restApplyCommonService.getStringObjectMap(smsInfoVo, resultMap, aeaProjInfo);
            if(StringUtils.isNotBlank(applyinstId)){//已暂存过，需要删除历史记录，重新插入数据
                restApplyCommonService.deleteReInsertAeaApplyinstUnitProj(applyinstId,(List<String>)map.get("unitProjIds"));
            }else{//第一次暂存
                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.createAeaHiApplyinst("net", smsInfoVo.getApplySubject(), smsInfoVo.getLinkmanInfoId(), smsInfoVo.getIsSeriesApprove(), null, ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue(),"1");
                applyinstId=aeaHiApplyinst==null?"":aeaHiApplyinst.getApplyinstId();
                restApplyCommonService.insertAeaApplyinstUnitProj(applyinstId,(List<String>)map.get("unitProjIds"));
                if("1".equals(smsInfoVo.getIsSeriesApprove()) && StringUtils.isNotBlank(smsInfoVo.getItemVerId())){
                    restApplyCommonService.insertSeriesIteminst(applyinstId,smsInfoVo.getItemVerId());
                }
            }
            resultMap.put("applyinstId", applyinstId);
            if(StringUtils.isNotBlank(applyinstId)){//回填sms表的申请实例ID
                String smsId=(String) resultMap.get("smsId");
                if(StringUtils.isNotBlank(smsId)) {
                    AeaHiSmsInfo sms=aeaHiSmsInfoService.getAeaHiSmsInfoById(smsId);
                    sms.setApplyinstId(applyinstId);
                    aeaHiSmsInfoService.updateAeaHiSmsInfo(sms);
                }
                if(StringUtils.isNotBlank(loginVo.getUnitId())){
                    restApplyCommonService.deleteReInsertAeaApplyinstUnitProjCurrentLogin(applyinstId,loginVo.getUnitId(),smsInfoVo.getLinkmanInfoId(),aeaProjInfo.getProjInfoId());
                }else{
                    restApplyCommonService.deleteReInsertAeaProjLinkmanCurrentLogin(applyinstId,loginVo.getUserId(),aeaProjInfo.getProjInfoId());
                }
                restApplyCommonService.saveOrUpdateAeaApplyinstProj(applyinstId,aeaProjInfo.getProjInfoId());
            }
            return new ContentResultForm<>(true, resultMap, "暂存成功!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "", e.getMessage());
        }
    }


    @PostMapping("/itemList/temporary")
    @ApiOperation(value = "阶段申报-->暂存阶段，情形及事项(含事项情形)")
    public ContentResultForm itemListTemporary(@Valid @RequestBody ItemListTemporaryParamVo itemListTemporaryParamVo, @RequestBody SmsInfoVo smsInfoVo,HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        ContentResultForm firstStepResult = temporarySaveOrUpdateSmsInfo(smsInfoVo, request);
        String applyinstId="";
        if(firstStepResult.isSuccess()){
            Map<String,Object> firstStepResultContent=(Map<String,Object>)(firstStepResult.getContent());
            BeanUtils.copyProperties(firstStepResultContent,map);
            applyinstId=(String)firstStepResultContent.get("applyinstId");
        }else {
            return new ContentResultForm(false,"",firstStepResult.getMessage());
        }
        try{
            itemListTemporaryParamVo.setApplyinstId(applyinstId);
            map=restApplyCommonService.submitItemList(itemListTemporaryParamVo,map);
            map.put("applyinstId",applyinstId);
        }catch (Exception e){
            return new ContentResultForm(false,"",e.getMessage());
        }
        return new ContentResultForm(true,map,"暂存成功！");
    }

    @PostMapping("/matList/temporary")
    @ApiOperation(value = "阶段申报-->暂存材料)")
    public ContentResultForm matListTemporary(@Valid @RequestBody ItemListTemporaryParamVo itemListTemporaryParamVo, @RequestBody SmsInfoVo smsInfoVo,@RequestBody MatListTemporaryParamVo matListTemporaryParamVo,HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        ContentResultForm secondStepResult = itemListTemporary(itemListTemporaryParamVo,smsInfoVo, request);
        String applyinstId="";
        if(secondStepResult.isSuccess()){
            Map<String,Object> secondStepResultContent=(Map<String,Object>)(secondStepResult.getContent());
            BeanUtils.copyProperties(secondStepResultContent,map);
            applyinstId=(String)secondStepResultContent.get("applyinstId");
        }else {
            return new ContentResultForm(false,"",secondStepResult.getMessage());
        }
        try{
            matListTemporaryParamVo.setApplyinstId(applyinstId);
            restApplyCommonService.submitMatmList(matListTemporaryParamVo);
            map.put("applyinstId",applyinstId);
        }catch (Exception e){
            return new ContentResultForm(false,"",e.getMessage());
        }
        return new ContentResultForm(true,map,"暂存成功！");
    }


    @GetMapping("itemState/findByParentItemStateId/{itemStateId}/{itemVerId}")
    @ApiOperation("并联申报/单项申报 --> 根据父情形ID查找(事项)子情形列表")
    @ApiImplicitParams({
        @ApiImplicitParam(value = "事项版本ID",name = "itemVerId",required = true,dataType = "string"),
        @ApiImplicitParam(value = "情形ID",name = "itemStateId",required = true,dataType = "string")
    })
    public ContentResultForm<List<AeaItemState>> findByParentItemStateId(@PathVariable("itemVerId") String itemVerId,@PathVariable("itemStateId") String itemStateId ){
        try {
            return new ContentResultForm<>(true,aeaItemStateService.listAeaItemStateByParentId(itemVerId,"",itemStateId, SecurityContext.getCurrentOrgId()));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","根据父情形ID查找(事项)子情形列表失败!");
        }
    }

    @PostMapping("/att/upload/auto")
    @ApiOperation(value = "并联申报/单项申报--> 一键自动分拣")
    public ResultForm saveFilesAuto(@ModelAttribute AutoImportParamVo autoImportVo, HttpServletRequest request) throws Exception {
        if (!restFileService.isAllowFileType(request))return new ResultForm(false, "不允许的文件类型");
        //单位id为空时，将用户id赋值给单位id，保存至表
        if (StringUtils.isEmpty(autoImportVo.getUnitInfoId())) autoImportVo.setUnitInfoId(autoImportVo.getUserInfoId());
        List<UploadMatReturnVo> list = restApplyMatService.saveFilesAuto(autoImportVo, request);
        return new ContentResultForm<>(true, list);
    }

//    @PostMapping("/item/frontItemIsDone/{itemVerId}/{projInfoId}")
//    @ApiOperation(value = "并联申报/单项申报--> 检查前置事项是否已办(已办或不存在前置事项返回空数组，存在返回未办的前置事项)")
//    public ResultForm frontItemIsDone(@PathVariable("itemVerId") String itemVerId, @PathVariable("projInfoId") String projInfoId) {
//        try {
//            List<AeaItemBasic> list = aeaItemBasicService.frontItemIsDone(itemVerId, projInfoId);
//            return new ContentResultForm<>(true, list);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            return new ContentResultForm(false, "", e.getMessage());
//        }
//    }
}
