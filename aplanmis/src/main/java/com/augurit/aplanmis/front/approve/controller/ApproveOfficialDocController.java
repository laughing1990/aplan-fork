package com.augurit.aplanmis.front.approve.controller;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.service.admin.cert.AeaCertAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.front.approve.service.OfficialDocumentService;
import com.augurit.aplanmis.front.approve.vo.UnitOfficialDocVo;
import com.augurit.aplanmis.front.approve.vo.certinst.CertVo;
import com.augurit.aplanmis.front.approve.vo.certinst.CertinstVo;
import com.augurit.aplanmis.front.approve.vo.official.OfficialDocumentEditVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 批文批复
 */
@RestController
@RequestMapping("/rest/approve")
@Api(value = "审批详情页", tags = "审批详情页-批文批复")
@Slf4j
public class ApproveOfficialDocController {

    @Autowired
    private OfficialDocumentService officialDocumentService;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private AeaCertAdminService aeaCertAdminService;

    @GetMapping("/official/doc")
    @ApiOperation(value = "业务审批 --> 首页")
    public ModelAndView OfficialDocIndex() {
        return new ModelAndView("agcloud/bpm/front/process/components/approveDoc");
    }

    @GetMapping("/official/attFile")
    @ApiOperation(value = "业务审批 --> 附件管理")
    public ModelAndView OfficialAttFileIndex() {
        return new ModelAndView("agcloud/bpm/front/process/components/attFile");
    }

    @PostMapping(value = "/docs/download")
    @ApiOperation(value = "审批详情页 --> 批文批复下载")
    @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", required = true)
    public ModelAndView downDocs(String detailIds, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        if (StringUtils.isNotBlank(detailIds)) {
            String[] _detailIds = detailIds.split(",");
            if (_detailIds.length > 0) {
                redirectAttributes.addAttribute("detailIds", detailIds);
                modelAndView.setViewName("redirect:/bsc/att/multiDownloadAttachment.do");
            }
        }
        return modelAndView;
    }

    @ApiOperation(value = "审批详情页 --> 申报批文批复列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "applyinstId", value = "并联申报实例id", dataType = "string", required = true)
            , @ApiImplicitParam(name = "iteminstId", value = "事项实例ID", dataType = "string", required = true)})
    @GetMapping("/docs/list")
    public ContentResultForm<List<UnitOfficialDocVo>> getOfficialDocsByApplyinstId(String applyinstId, String iteminstId) {
        if (StringUtils.isBlank(applyinstId)) {
            return new ContentResultForm<>(false, null, "并联申报实例ID为空");
        }
        try {
            List<Map<String, Object>> unitOfficialDocs = officialDocumentService.getUnitOfficialDocs(applyinstId, iteminstId);
            List<UnitOfficialDocVo> unitOfficialDocVos = UnitOfficialDocVo.toUnitOfficialDocVo(unitOfficialDocs);
            return new ContentResultForm<>(true, unitOfficialDocVos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(false, null);
    }

    @ApiOperation(value = "审批详情页 --> 新增批文批复")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "officialDocNo", value = "文件文号", dataType = "string")
            , @ApiImplicitParam(name = "officialDocTitle", value = "文件标题", dataType = "string")
            , @ApiImplicitParam(name = "officialDocDueDate", value = "有效期限", dataType = "date", format = "yyyy-MM-dd")
            , @ApiImplicitParam(name = "officialDocPublishDate", value = "批复日期", dataType = "date", format = "yyyy-MM-dd")
            , @ApiImplicitParam(name = "paperCount", value = "纸质份数", dataType = "Integer")
            , @ApiImplicitParam(name = "attCount", value = "电子件份数", dataType = "Integer")
            , @ApiImplicitParam(name = "iteminst", value = "事项实例, 并联时不用传", dataType = "string")
            , @ApiImplicitParam(name = "applyinst", value = "申请实例", dataType = "string")
            , @ApiImplicitParam(name = "taskId", value = "并联审批taskId", dataType = "string")
    })
    @PostMapping("/docs/create")
    public ResultForm createOfficialDoc(OfficialDocumentEditVo officialDocumentEditVo, HttpServletRequest request) {
        try {
            officialDocumentService.create(officialDocumentEditVo, request);
        } catch (Exception e) {
            e.printStackTrace();
            new ResultForm(false, "新增批文批复失败");
        }
        return new ResultForm(true, "新增批文批复成功");
    }

    @ApiOperation(value = "审批详情页 --> 删除批文批复")
    @ApiImplicitParam(name = "matinstId", value = "批文批复id", dataType = "string")
    @DeleteMapping("/docs/delete/{matinstId}")
    public ResultForm deleteOfficialDoc(@PathVariable String matinstId) {
        officialDocumentService.delete(matinstId, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }

    @ApiOperation(value = "审批详情页 --> 批量删除批文批复")
    @ApiImplicitParam(name = "matinstIds", value = "批文批复id", dataType = "array", required = true, allowMultiple = true)
    @PostMapping("/docs/batch_delete")
    @Transactional
    public ResultForm deleteOfficialDoc(String[] matinstIds) {
        if (matinstIds != null && matinstIds.length > 0) {
            Arrays.stream(matinstIds).forEach(matinstId -> officialDocumentService.delete(matinstId, SecurityContext.getCurrentOrgId()));
        }
        return new ResultForm(true);
    }

    @ApiOperation(value = "审批详情页 --> 编辑批文批复")
    @ApiImplicitParams(value = {@ApiImplicitParam(name = "matinstId", value = "批文批复id", dataType = "string")
            , @ApiImplicitParam(name = "officialDocNo", value = "文件文号", dataType = "string")
            , @ApiImplicitParam(name = "officialDocTitle", value = "文件标题", dataType = "string")
            , @ApiImplicitParam(name = "officialDocDueDate", value = "有效期限", format = "yyyy-MM-dd")
            , @ApiImplicitParam(name = "officialDocPublishDate", value = "批复日期", format = "yyyy-MM-dd")
            , @ApiImplicitParam(name = "attCount", value = "电子件份数", dataType = "Integer")
            , @ApiImplicitParam(name = "iteminst", value = "事项实例", dataType = "string")
            , @ApiImplicitParam(name = "applyinst", value = "申请实例", dataType = "string")
            , @ApiImplicitParam(name = "taskId", value = "并联审批taskId", dataType = "string")
    })
    @PostMapping("/docs/edit")
    public ContentResultForm editOfficialDoc(OfficialDocumentEditVo officialDocumentEditVo, HttpServletRequest request) {
        try {
            officialDocumentService.edit(request, officialDocumentEditVo);
        } catch (Exception e) {
            e.printStackTrace();
            new ContentResultForm<>(false, null, "编辑批文批复失败");
        }
        return new ContentResultForm<>(true, officialDocumentEditVo.getMatinstId(), "编辑批文批复成功");
    }

    @GetMapping("/getOfficeAttListByMatinstId/{matinstId}")
    @ApiOperation(value = "编辑批文批复-查询当前批复下附件列表")
    @ApiImplicitParam(value = "材料实例ID", name = "matinstId", required = true, dataType = "string")
    public ResultForm getOfficeAttListByMatinstId(@PathVariable String matinstId) throws Exception {
        List<BscAttFileAndDir> matAttDetailByMatinstId = fileUtilsService.getMatAttDetailByMatinstId(matinstId);
        return new ContentResultForm<>(true, matAttDetailByMatinstId);
    }

    @GetMapping("/getItemInOfficeMat")
    @ApiOperation(value = "获取当前事项实例下 批文批复定义 列表,如果事项实例id为空，申请实例id不为空，则查询申请实例下所有事项的批文批复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "iteminstId", value = "事项实例id", required = false, dataType = "string")
    })
    public ResultForm getItemInOfficeMat(String applyinstId, String iteminstId) {
        List<AeaItemMat> matList = new ArrayList<>();
        try {
            matList = officialDocumentService.getItemInOfficeMat(applyinstId, iteminstId);
            return new ContentResultForm<>(true, matList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, matList);
        }
    }

    @GetMapping("/getItemOutputCert")
    @ApiOperation(value = "获取当前事项实例下 输出证照定义 列表，如果事项实例id为空，申请实例id不为空，则查询申请实例下所有事项的证照")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "iteminstId", value = "事项实例id", required = false, dataType = "string")
    })
    public ResultForm getItemOutputCert(String applyinstId, String iteminstId) {
        if (StringUtils.isBlank(applyinstId) && StringUtils.isBlank(iteminstId)) {
            return new ResultForm(false, "参数applyinstId和iteminstId不能同时为空！");
        }
        try {
            CertVo vo = officialDocumentService.getItemOutputCertByIteminstId(applyinstId, iteminstId);
            return new ContentResultForm(true, vo, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null);
        }
    }

    @GetMapping("/getCertinstListByApplyinstIdAndIteminstId")
    @ApiOperation(value = "获取当前事项实例下或申报实例下 输出证照实例 列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iteminstId", value = "事项实例,窗口人员==null 或 '' 或 undefinded ", dataType = "string")
            , @ApiImplicitParam(name = "applyinstId", value = "申请实例ID", required = true, dataType = "string")
    })
    public ResultForm getCertinstListByApplyinstIdAndIteminstId(String iteminstId, String applyinstId) {
        try {
            List<CertinstVo> certinstList = officialDocumentService.getCertinstListByApplyinstIdAndIteminstId(applyinstId, iteminstId);
            return new ContentResultForm(true, certinstList, "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, null);
        }
    }

    @PostMapping("/saveAeaCertinst")
    @ApiOperation(value = "保存或更新证照实例")
    public ResultForm saveOrUpdateAeaCertinst(CertinstVo certinstVo) throws Exception {
        officialDocumentService.saveOrUpdateAeaCertinst(certinstVo);

        return new ResultForm(true, "success");
    }


    @PostMapping("/certinst/batch/delete")
    @ApiOperation(value = "单个/批量 删除证照")
    @Transactional
    public ResultForm batchDeleteCertinst(String certinstIds) throws Exception {
        if (StringUtils.isNotBlank(certinstIds)) {
            String[] split = certinstIds.split(",");
            officialDocumentService.batchDeleteCertinst(split);
        }

        return new ResultForm(true, "success");
    }


}
