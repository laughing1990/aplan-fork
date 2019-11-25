package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.mat.RestMatCorrectCommonService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyMatService;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyService;
import com.augurit.aplanmis.mall.userCenter.service.RestApproveService;
import com.augurit.aplanmis.mall.userCenter.service.RestFileService;
import com.augurit.aplanmis.mall.userCenter.vo.MatUploadVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
@RequestMapping("rest/file")
@Api(value = "", tags = "法人空间 --> 电子文件管理")
public class RestFileController {
    Logger logger = LoggerFactory.getLogger(RestFileController.class);

    @Autowired
    RestApproveService restApproveService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    AeaProjInfoService aeaProjInfoService;
    @Autowired
    RestApplyService restApplyService;
    @Autowired
    RestApplyMatService restApplyMatService;
    @Autowired
    RestFileService restFileService;
    @Autowired
    FileUtilsService fileUtilsService;
    @Autowired
    private RestMatCorrectCommonService restMatCorrectCommonService;


    @PostMapping("/uploadFile")
    @ApiOperation("文件处理 --> 电子文件上传")
    @ApiImplicitParams({@ApiImplicitParam(name = "matId", value = "材料id", required = true, type = "string"),
            @ApiImplicitParam(name = "matinstCode", value = "材料实例编码", required = true, type = "string"),
            @ApiImplicitParam(name = "matinstName", value = "材料实例名称", required = true, type = "string"),
            @ApiImplicitParam(name = "projInfoId", value = "项目ID", required = true, type = "string"),
            @ApiImplicitParam(name = "matinstId", value = "材料实例ID", required = true, type = "string"),
            @ApiImplicitParam(name = "matProp", value = "材料性质，m表示普通材料，c表示证照材料，f表示在线表单", required = true, type = "string"),
            @ApiImplicitParam(name = "certId", value = "证照定义ID", required = false, type = "string"),
            @ApiImplicitParam(name = "stoFormId", value = "表单定义ID", required = false, type = "string")})
    public ResultForm uploadFile(MatUploadVo uploadVo, HttpServletRequest request) {
        try {
            if (!restFileService.isAllowFileType(request))return new ResultForm(false, "不允许上传的文件类型");
            AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
            BeanUtils.copyProperties(uploadVo, aeaHiItemMatinst);
            String matinstId = aeaHiItemMatinst.getMatinstId();
            aeaHiItemMatinst.setMatinstId(("null".equalsIgnoreCase(matinstId) || "undefined".equalsIgnoreCase(matinstId) || StringUtils.isBlank(matinstId)) ? null : matinstId);
            if (setMatSource(request, aeaHiItemMatinst)) return new ResultForm(false, "上传失败");
            return new ContentResultForm(true, aeaHiItemMatinstService.saveAeaHiItemMatinst(aeaHiItemMatinst, request));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false);
        }
    }

    private boolean setMatSource(HttpServletRequest request, AeaHiItemMatinst aeaHiItemMatinst) {
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        if (loginInfoVo == null) return true;
        if ("1".equals(loginInfoVo.getIsPersonAccount()) || StringUtils.isNotBlank(loginInfoVo.getUserId())) {//个人
            aeaHiItemMatinst.setMatinstSource("1");
            aeaHiItemMatinst.setLinkmanInfoId(loginInfoVo.getUserId());
        } else {//企业
            aeaHiItemMatinst.setMatinstSource("u");
            aeaHiItemMatinst.setUnitInfoId(loginInfoVo.getUnitId());
        }
        return false;
    }

    @PostMapping("cloud/uploadFile")
    @ApiOperation("文件处理 --> 从云盘或者材料库上传电子文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "matId", value = "材料id", required = true, type = "string"),
            @ApiImplicitParam(name = "matinstCode", value = "材料实例编码", required = true, type = "string"),
            @ApiImplicitParam(name = "matinstName", value = "材料实例名称", required = true, type = "string"),
            @ApiImplicitParam(name = "projInfoId", value = "项目ID", required = true, type = "string"),
            @ApiImplicitParam(name = "matinstId", value = "材料实例ID", required = false, type = "string"),
            @ApiImplicitParam(name = "detailIds", value = "文件ID（英文状态逗号分隔）", required = true, type = "string")})
    public ResultForm uploadFileByCloud(String detailIds,String matId, String matinstCode, String matinstName, String projInfoId, String matinstId, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(matId) || StringUtils.isBlank(matinstCode) || StringUtils.isBlank(matinstName) || StringUtils.isBlank(projInfoId))
                return new ResultForm(false, "缺少必须参数");
            AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
            aeaHiItemMatinst.setMatId(matId);
            aeaHiItemMatinst.setMatinstCode(matinstCode);
            aeaHiItemMatinst.setMatinstName(matinstName);
            aeaHiItemMatinst.setProjInfoId(projInfoId);
            aeaHiItemMatinst.setDetailIds(detailIds);
            aeaHiItemMatinst.setMatinstId(("null".equalsIgnoreCase(matinstId) || "undefined".equalsIgnoreCase(matinstId) || StringUtils.isBlank(matinstId)) ? null : matinstId);
            if (setMatSource(request, aeaHiItemMatinst)) return new ResultForm(false, "上传失败");
            return new ContentResultForm(true, aeaHiItemMatinstService.saveAeaHiItemMatinstByCloud(aeaHiItemMatinst));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false);
        }
    }

    @GetMapping("/getAttFiles/{matinstId}")
    @ApiOperation("文件处理 --> 获取电子文件列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "matinstId", value = "事项材料ID", required = true, type = "string")})
    public ResultForm getAttFiles(@PathVariable String matinstId,HttpServletRequest request) throws Exception {
        if ("null".equalsIgnoreCase(matinstId) || "undefined".equalsIgnoreCase(matinstId) || StringUtils.isBlank(matinstId))
            return new ContentResultForm<>(true, new ArrayList<>());
        if (!restFileService.isMatBelong(matinstId,request)) return new ContentResultForm<>(true,new ArrayList<BscAttFileAndDir>());
        return new ContentResultForm<>(true, restFileService.getAttFiles(matinstId));
    }

    @GetMapping("/delelteAttachment")
    @ApiOperation("文件处理 --> 列表中删除电子文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "detailIds", value = "文件id", required = true, type = "string"),
            @ApiImplicitParam(name = "matinstId", value = "事项材料ID", required = true, type = "string")})
    public ResultForm delelteAttachment(String detailIds, String matinstId,HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(matinstId)) {
                return new ResultForm(false, "删除失败：matinstId为空!");
            }
            if (StringUtils.isBlank(detailIds)) {
                return new ResultForm(false, "删除失败：没有可删除文件!");
            }
            if (!restFileService.isMatBelong(matinstId,request)) return new ResultForm(false,"删除出错");
            String[] str = detailIds.split(",");
            return restFileService.delelteAttachment(str, matinstId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "删除失败!");
        }
    }

    @GetMapping("/delelteAttachmentOfCloud")
    @ApiOperation("文件处理 --> 删除云盘列表中电子文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "detailIds", value = "文件id(多文件删除英文逗号分隔)", required = true, type = "string"),
            @ApiImplicitParam(name = "recordId", value = "业务主键ID", required = true, type = "string")})
    public ResultForm delelteAttachmentOfCloud(String detailIds, String recordId,HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(recordId)) {
                recordId=StringUtils.isNotBlank(SessionUtil.getSessionUserId(request))?SessionUtil.getSessionUserId(request):SessionUtil.getSessionUnitId(request);
            }
            if (StringUtils.isBlank(detailIds)) {
                return new ResultForm(false, "删除失败：没有可删除文件!");
            }
            String[] str = detailIds.split(",");
            restFileService.delelteAttachmentByCloud(str, recordId);
            return new ResultForm(true, "删除成功!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "删除失败!");
        }
    }

    @GetMapping("applydetail/mat/download/{detailId}")
    @ApiOperation(value = "文件处理--> 下载文件接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "文件Id", name = "detailId", required = true, dataType = "string")})
    public ResultForm downloadAttachment(@PathVariable("detailId") String detailId, HttpServletResponse response, HttpServletRequest request) {
        try {
            String[] detailIds = new String[1];
            detailIds[0] = detailId;
//            return new ContentResultForm<>(true,restApproveService.downloadAttachment(detailIds,response,request));
//            attachmentAdminService.downloadFileStrategy(detailId, response);
            if (!restFileService.isMatInstFileBelong(detailId,request))return new ResultForm(false, "下载出错");
            fileUtilsService.downloadAttachment(detailId, response, request, false);
            return new ResultForm(true, "下载成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, "下载出错");
        }
    }

    @GetMapping("/att/preview/{detailId}")
    @ApiOperation(value = "文件处理--> 预览电子件")
    @ApiImplicitParam(name = "detailId", value = "附件ID", dataType = "string", required = true)
    public ModelAndView preview_old(@PathVariable("detailId") String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
        if (!restFileService.isFileBelong(detailId,request)) throw new Exception("预览出错");
        ModelAndView modelAndView = restFileService.preview(detailId, request, response, redirectAttributes);
        return modelAndView;
    }

    @GetMapping("/att/preview")
    @ApiOperation(value = "申报页面--> 预览电子件")
    @ApiImplicitParam(name = "detailId", value = "附件ID", dataType = "string", required = true)
    public ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
        if (!restFileService.isFileBelong(detailId,request)) throw new Exception("预览出错");
        ModelAndView modelAndView = fileUtilsService.preview(detailId, request, response, redirectAttributes);
        return modelAndView;
    }

    @GetMapping("/view/{detailId}")
    @ApiOperation("在线预览pdf文件")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "detailId", value = "文件detailId", required = true, dataType = "String")
    )
    public ModelAndView previewPdf(@PathVariable String detailId) {
        ModelAndView mav = new ModelAndView("/preview/viewPdf");
        mav.addObject("detailId", detailId);
        return mav;
    }

    @GetMapping("/att/read")
    @ApiOperation(value = "申报页面--> 读取电子件")
    @ApiImplicitParam(name = "detailId", value = "附件ID", dataType = "string", required = true)
    public void readFile(String detailId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        fileUtilsService.readFile(detailId, request, response);
    }

    @PostMapping("/att/delelte")
    @ApiOperation(value = "材料补正页面--> 删除电子件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", paramType = "query", required = true)
    })
    public ResultForm delelteAttFile(String detailIds, String attRealIninstId,HttpServletRequest request) {
        try {
            Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");
            String[] detailIdArr = detailIds.split(",");
            for (String detailId:detailIdArr){
                if (!restFileService.isFileBelong(detailId,request)){
                    throw new Exception("文件删除出错");
                }
            }
            restMatCorrectCommonService.delelteAttFile(detailIds, attRealIninstId);
            return new ResultForm(true, "文件删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "文件删除失败！");
        }
    }

    @ApiOperation(value = "材料补正页面--> 上传电子件")
    @PostMapping("/att/upload")
    public ResultForm uploadFile(String attRealIninstId, HttpServletRequest request) {
        try {
            if (!restFileService.isAllowFileType(request))return new ResultForm(false, "不允许上传的文件类型");
            Assert.isTrue(StringUtils.isNotBlank(attRealIninstId), "attRealIninstId is null");
            restMatCorrectCommonService.uploadFile(attRealIninstId, request);
            return new ResultForm(false, "文件上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "文件上传失败！");
        }
    }
}
