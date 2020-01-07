package com.augurit.aplanmis.front.certificate.controller;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiSmsSendItem;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.front.certificate.service.RestCertificateService;
import com.augurit.aplanmis.front.certificate.vo.CertListAndUnitVo;
import com.augurit.aplanmis.front.certificate.vo.CertinstParamVo;
import com.augurit.aplanmis.front.certificate.vo.SmsSaveParam;
import com.augurit.aplanmis.front.certificate.vo.SmsSendBaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/rest/certificate")
@Api(tags = "制证、出证/发证、取件统一入口")
public class RestCertificateController {

    @Autowired
    private RestCertificateService certificateService;
    @Autowired
    private FileUtilsService fileUtilsService;


    /**
     * 已取证页面入口
     *
     * @return
     */
    @GetMapping("/hadSendIndex")
    @ApiOperation("领证页面入口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "path", required = true)

    })
    public ModelAndView hadSendIndex(String applyinstId) {
        ModelAndView modelAndView = new ModelAndView("certificate/hadSend");
        modelAndView.addObject("applyinstId", applyinstId);
        modelAndView.addObject("windowUserId", SecurityContext.getCurrentUserId());
        modelAndView.addObject("windowUserName", SecurityContext.getCurrentUserName());

        return modelAndView;
    }
    /**
     * 领证页面入口
     *
     * @return
     */
    @GetMapping("/registerIndex")
    @ApiOperation("领证页面入口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "path", required = true)

    })
    public ModelAndView singleApplyIndex(String applyinstId, String parentIfreamUrl) {
        ModelAndView modelAndView = new ModelAndView("certificate/index");
        modelAndView.addObject("applyinstId", applyinstId);
        modelAndView.addObject("windowUserId", SecurityContext.getCurrentUserId());
        modelAndView.addObject("windowUserName", SecurityContext.getCurrentUserName());

        return modelAndView;
    }
    /**
     * 获取事项实例情况
     *
     * @param applyinstId
     * @return
     * @throws Exception
     */
    @PostMapping("/listSmsCertDetailByApplyinstId")
    @ApiOperation(value = "根据申请实例ID获取事项出件出证信息")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例主键 ", name = "applyinstId", required = true)})
    public ResultForm getCertificationInfo(String applyinstId){
        Assert.notNull(applyinstId,"申请实例Id为空！");
        try {
            SmsSendBaseVo result = certificateService.getCertificationInfo(applyinstId);
            return  new ContentResultForm<>(true,result,"获取事项实例成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取事项实例失败！");
        }
    }


    @GetMapping("/getCertInfoByIteminstId")
    @ApiOperation(value = "根据事项实例id查询证照信息-弃用")
    @ApiImplicitParams({@ApiImplicitParam(value = "事项实例id ", name = "iteminstId", required = true)})
    public ContentResultForm getCertInfoByIteminstId(String iteminstId,String applyinstId){
        Assert.notNull(iteminstId,"iteminstId为空！");
        Assert.notNull(applyinstId,"applyinstId为空！");

        try {
            List<AeaHiCertinst> certinsts = certificateService.getCertInfoByIteminstId(iteminstId,applyinstId);
            return new ContentResultForm(true, certinsts,"query success!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"查询证照实例信息失败！");
        }
    }
    //保存领证信息
    @PostMapping("/saveSmsCertDetail")
    @ApiOperation(value = "保存出件领证相关信息")
    @ApiImplicitParams({@ApiImplicitParam(value = "领证信息（json字符串格式） ", name = "jsonstr", required = true)})
    public ContentResultForm insertSmsinfo(String jsonstr){
        Assert.notNull(jsonstr,"字符串参数为空！");
        try {
            SmsSaveParam smsSaveParam = JSONObject.parseObject(jsonstr, SmsSaveParam.class);
            boolean result = certificateService.saveSmsRelevance(smsSaveParam);
            return new ContentResultForm(true,result,"保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"保存领证记录失败！");
        }
    }

    @PostMapping("/consignerAtt/upload")
    @ApiOperation(value = "上传委托证明附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", dataType = "string", paramType = "query", required = true)
    })
    public ContentResultForm<String> uploadFile(String applyinstId, HttpServletRequest request) {
        Assert.notNull(applyinstId,"申请实例Id不能为空！");

        try {
            String consignerAttId = certificateService.uploadFile(applyinstId, request);
            return new ContentResultForm<>(true, consignerAttId, "委托证明附件 upload success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false,null,"上传委托证明附件失败！");
        }
    }

    @GetMapping("/consignerAtt/list")
    @ApiOperation(" 查询已上传委托证明列表")
    @ApiImplicitParam(name = "applyinstId", value = "申请实例id")
    public ContentResultForm<List<BscAttFileAndDir>> attFileList(@RequestParam(required = false) String applyinstId) {
        if (StringUtils.isBlank(applyinstId)) {
            return new ContentResultForm<>(true, null, "empty list");
        }
        try {
            List<BscAttFileAndDir> fileAndDirs = certificateService.getMatAttDetailByApplyinstId(applyinstId);
            return new ContentResultForm<>(true, fileAndDirs, "查询委托证明书列表 success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询委托证明书列表 list failed");
        }
    }

    @PostMapping("/att/delelte")
    @ApiOperation(value = "删除委托证明")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailIds", value = "附件详情id", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", dataType = "string", paramType = "query", required = true)
    })
    public ContentResultForm<String> delelteAttFile(String detailIds, String applyinstId){
        Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");

        try {
            certificateService.delelteAttFile(detailIds);
            return new ContentResultForm<>(true, applyinstId, "cert is deleted.");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false,null,"删除委托证明失败！");
        }
    }

    @GetMapping("/consignerAtt/preview")
    @ApiOperation(value = "预览委托证明")
    @ApiImplicitParam(name = "detailId", value = "附件ID", dataType = "string", required = true)
    public ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView modelAndView = certificateService.preview(detailId, request, response, redirectAttributes);
        return modelAndView;
    }

    @GetMapping("/getBasicInfo")
    @ApiOperation(value = "获取制证所需基本信息")
    public ContentResultForm getBasicInfo() throws Exception {
        Map result = certificateService.getCertBasicInfo();
        return new ContentResultForm(true, result);
    }


    @PostMapping("/updateCertInfo")
    @ApiOperation(value = "新增编辑制证")
    public ContentResultForm updateCertInfo(CertinstParamVo param) throws Exception {
        if (StringUtils.isBlank(param.getCertinstId())) {
            certificateService.createCertInfo(param);

        } else {
            certificateService.updateCertInfo(param);
        }
        return new ContentResultForm<>(true, "certinst 保存成功");
    }


    @PostMapping("/certinst/att/upload")
    @ApiOperation(value = "上传证件实例附件")
    public ResultForm uploadCert(CertinstParamVo vo, HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(vo.getCertinstId()) && StringUtils.isBlank(vo.getCertId()))
            return new ResultForm(false, "缺少必要参数");
        certificateService.uploadCert(vo, request);
        return new ContentResultForm<>(true, vo.getCertinstId(), "success");
    }

    @ApiOperation("根据事项实例查询证照实例列表")
    @GetMapping("/certinst/list")
    @ApiImplicitParam(name = "事项实例id", value = "iteminstId")
    public ResultForm getCertinstListByIteminstId(String iteminstId) throws Exception {
        List<AeaHiCertinst> list = certificateService.getCertinstListByIteminstId(iteminstId);
        return new ContentResultForm<>(true, list, "success");
    }

    @GetMapping("/cert/category/list")
    @ApiOperation(value = "根据事项实例ID获取事项定义证件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "事项实例id", value = "iteminstId", required = true),
            @ApiImplicitParam(name = "申请实例id", value = "applyinstId", required = true),

    })
    public ResultForm certCategoryList(String iteminstId, String applyinstId, Boolean isFilter) throws Exception {
        CertListAndUnitVo vo = certificateService.getcertCategoryList(iteminstId, applyinstId, isFilter, SecurityContext.getCurrentOrgId());
        return new ContentResultForm<>(true, vo, "success");
    }

    @PostMapping("/certinst/batch/delete")
    @ApiOperation(value = "批量删除证件实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstIds", value = "certinstIds", required = true, dataTypeClass = Array.class)

    })
    public ResultForm batchDeleteCertinst(String[] certinstIds) throws Exception {
        certificateService.deleteCertinst(certinstIds);
        return new ResultForm(true, "success");
    }

    @PostMapping("/certinst/one/delete")
    @ApiOperation(value = "单个删除证件实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstId", value = "certinstId", required = true, dataType = "String")

    })
    public ResultForm oneDeleteCertinst(String certinstId) throws Exception {
        //String[] ids=new String[]{certinstId};
        String[] ids = {certinstId};
        certificateService.deleteCertinst(ids);
        return new ResultForm(true, "success");
    }


    @PostMapping("/certinst/att/delelte")
    @ApiOperation(value = "批量删除证照附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailIds", value = "附件详情id", dataType = "string", paramType = "query", required = true)

    })
    public ResultForm batchDeleteAttach(String detailIds) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");
        certificateService.delelteAttFile(detailIds);
        return new ContentResultForm<>(true, "cert is deleted.");
    }


    @GetMapping("/certinst/att/list")
    @ApiOperation(value = " 查询证照实例附件列表", notes = "{查询-证照实例-附件列表}")
    @ApiImplicitParam(name = "certinstId", value = "证照实例id")
    public ResultForm certFileList(String certinstId) {
        if (StringUtils.isBlank(certinstId)) {
            return new ContentResultForm<>(true, new ArrayList<>(), "empty list");
        }
        try {
            List<BscAttFileAndDir> fileAndDirs = fileUtilsService.getBscAttFileAndDirListByinstId(certinstId, "CERTINST_ID", "AEA_HI_CERTINST");
            return new ContentResultForm<>(true, fileAndDirs, "Query attachment list success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "Query attachment list failed");
        }
    }

    @GetMapping("/cert/list")
    @ApiOperation(value = " 查询证照附件附件列表", notes = "{查询-事项实例-证照实例-附件列表}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "instId", value = "实例id"),
            @ApiImplicitParam(name = "type", value = "实例类型【cert输出证照，mat输出材料】")
    })

    public ResultForm certFileListByItemisntId(String instId,String type){
        if (StringUtils.isBlank(instId)) {
            return new ContentResultForm<>(true, new ArrayList<>(), "empty certinstId");
        }
        try {

            List<BscAttFileAndDir> result = new ArrayList<>();
            if("cert".equals(type)){
                result.addAll(fileUtilsService.getBscAttFileAndDirListByinstId(instId, "CERTINST_ID", "AEA_HI_CERTINST"));
            }else{
                result.addAll(fileUtilsService.getBscAttFileAndDirListByinstId(instId, "MATINST_ID", "AEA_HI_ITEM_MATINST"));
            }
            return new ContentResultForm<>(true, result, "Query attachment list success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false,"query list false;");
        }

    }
    @ApiOperation("获取事项出件详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "iteminstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "query", required = true)

    })
    @GetMapping("/getSmsSendItemDetail")
    public ResultForm getSmsSendItemDetail(String applyinstId , String iteminstId){
        try {
            AeaHiSmsSendItem sendItem = certificateService.getSmsSendItemDetail(applyinstId,iteminstId);
            return new ContentResultForm<>(true,sendItem,"获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false,null,"获取失败！"+e.getMessage());
        }
    }
    @RequestMapping("/updateSendItemInfo")
    public ResultForm updateSendItemInfo(String jsonstr){
        try {
            AeaHiSmsSendItem sendItem = JSONObject.parseObject(jsonstr, AeaHiSmsSendItem.class);
            certificateService.updateSendItemInfo(sendItem);
            return new ContentResultForm<>(true,null,"更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false,null,"更新失败！"+e.getMessage());
        }
    }

    @GetMapping("/getOutFileInfoByIteminstId")
    @ApiOperation(value = "根据事项实例id查询输出材料信息")
    @ApiImplicitParams({@ApiImplicitParam(value = "事项实例id ", name = "iteminstId", required = true)})
    public ContentResultForm getOutFileInfoByIteminstId(String iteminstId,String applyinstId){
        Assert.notNull(iteminstId,"iteminstId为空！");
        Assert.notNull(applyinstId,"applyinstId为空！");

        try {
            List<Map<String,Object>> result = certificateService.getOutFileInfoByIteminstId(iteminstId,applyinstId);
            return new ContentResultForm(true, result,"query success!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"查询证照实例信息失败！");
        }
    }
}
