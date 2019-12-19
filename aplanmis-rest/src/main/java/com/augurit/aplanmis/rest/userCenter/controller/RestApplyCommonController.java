package com.augurit.aplanmis.rest.userCenter.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.rest.userCenter.service.RestApplyMatService;
import com.augurit.aplanmis.rest.userCenter.service.RestApplyService;
import com.augurit.aplanmis.rest.userCenter.vo.AutoImportParamVo;
import com.augurit.aplanmis.rest.userCenter.vo.SmsInfoVo;
import com.augurit.aplanmis.rest.userCenter.vo.UploadMatReturnVo;
import com.augurit.aplanmis.rest.userCenter.vo.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/user/apply/common")
@Api(value = "申报通用接口", tags = "申报 --> 申报通用接口")
public class RestApplyCommonController {
    Logger logger = LoggerFactory.getLogger(RestApplyCommonController.class);

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


    @GetMapping("/projInfo/{projInfoId}")
    @ApiOperation(value = "阶段申报 --> 查询项目基础信息接口")
    public ContentResultForm<AeaProjInfo> getProjInfoByProjInfoId(@PathVariable("projInfoId") String projInfoId) {
        try {
            AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
            if (StringUtils.isNotBlank(aeaProjInfo.getThemeId())) {
                AeaParTheme theme = aeaParThemeService.getAeaParThemeByThemeId(aeaProjInfo.getThemeId());
                if (theme != null) aeaProjInfo.setThemeType(theme.getThemeType());
            }
            return new ContentResultForm<>(true, aeaProjInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "查询项目基础信息接口发生异常");
        }
    }

    @GetMapping("/applyObject")
    @ApiOperation(value = "阶段申报/单项申报 --> 查询申报主体接口")
    public ContentResultForm<UserInfoVo> getApplyObject() {
        try {
            return new ContentResultForm<>(true, restApplyService.getApplyObject());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "查询申报主体接口发生异常");
        }
    }

    @PostMapping("/completioninfo/saveOrUpdate")
    @ApiOperation(value = "并联申报/单项申报 --> 补全信息保存领件人、项目信息", httpMethod = "POST")
    public ContentResultForm saveOrUpdateSmsInfo(@RequestBody SmsInfoVo smsInfoVo) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        BeanUtils.copyProperties(smsInfoVo, aeaProjInfo);
        String smsId;
        //保存或修改领件人信息
        if (StringUtils.isBlank(smsInfoVo.getId())) {
            AeaHiSmsInfo aeaHiSmsInfo=null;
            if(StringUtils.isNotBlank(smsInfoVo.getApplyinstId())){
                aeaHiSmsInfo =aeaHiSmsInfoService.getAeaHiSmsInfoByApplyinstId(smsInfoVo.getApplyinstId());
            }
            if(aeaHiSmsInfo==null) aeaHiSmsInfo = aeaHiSmsInfoService.createAeaHiSmsInfo(smsInfoVo.toSmsInfo());
            smsId = aeaHiSmsInfo.getId();
        } else {
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoService.getAeaHiSmsInfoById(smsInfoVo.getId());
            aeaHiSmsInfoService.updateAeaHiSmsInfo(smsInfoVo.merge(aeaHiSmsInfo));
            smsId = smsInfoVo.getId();
        }
        //修改项目信息
        if (StringUtils.isBlank(aeaProjInfo.getGcbm()))
            aeaProjInfo.setGcbm(aeaProjInfo.getLocalCode());
        aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);
        resultMap.put("smsId", smsId);
        resultMap.put("regionalism", aeaProjInfo.getRegionalism());
        return new ContentResultForm<>(true, resultMap, "保存成功!");
    }

    @GetMapping("/itemState/findByParentItemStateId/{itemStateId}/{itemVerId}")
    @ApiOperation("并联申报/单项申报 --> 根据父情形ID查找(事项)子情形列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "事项版本ID", name = "itemVerId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "情形ID", name = "itemStateId", required = true, dataType = "string")
    })
    public ContentResultForm<List<AeaItemState>> findByParentItemStateId(@PathVariable("itemVerId") String itemVerId, @PathVariable("itemStateId") String itemStateId) {
        try {
            return new ContentResultForm<>(true, aeaItemStateService.listAeaItemStateByParentId(itemVerId, "", itemStateId, SecurityContext.getCurrentOrgId()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "根据父情形ID查找(事项)子情形列表失败!");
        }
    }

    @PostMapping("/att/upload/auto")
    @ApiOperation(value = "并联申报/单项申报--> 一键自动分拣")
    public ResultForm saveFilesAuto(@ModelAttribute AutoImportParamVo autoImportVo, HttpServletRequest request) throws Exception {
        //单位id为空时，将用户id赋值给单位id，保存至表
        if (StringUtils.isEmpty(autoImportVo.getUnitInfoId())) autoImportVo.setUnitInfoId(autoImportVo.getUserInfoId());
        List<UploadMatReturnVo> list = restApplyMatService.saveFilesAuto(autoImportVo, request);
        return new ContentResultForm<>(true, list);
    }
}
