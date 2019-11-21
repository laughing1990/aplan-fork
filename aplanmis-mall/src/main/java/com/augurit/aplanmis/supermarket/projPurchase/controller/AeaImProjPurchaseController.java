package com.augurit.aplanmis.supermarket.projPurchase.controller;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.constants.AuditFlagStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.projPurchase.AeaImProjPurchaseService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.utils.BusinessUtils;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.vo.*;
import com.augurit.aplanmis.supermarket.apply.service.RestImApplyService;
import com.augurit.aplanmis.supermarket.contract.service.AeaImContractService;
import com.augurit.aplanmis.supermarket.projPurchase.service.ProjPurchaseService;
import com.augurit.aplanmis.supermarket.projPurchase.vo.OwnerIndexData;
import com.augurit.aplanmis.supermarket.projPurchase.vo.QueryUnpublishedProjInfo;
import com.augurit.aplanmis.supermarket.projPurchase.vo.SelectedQualMajorRequire;
import com.augurit.aplanmis.supermarket.projPurchase.vo.purchase.ShowProjPurchaseVo;
import com.augurit.aplanmis.supermarket.utils.ContentRestResult;
import com.augurit.aplanmis.supermarket.utils.RestResult;
import com.augurit.aplanmis.thirdPlatform.service.ProjectCodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(description = "项目需求采购管理接口", value = "", tags = "中介超市---项目需求采购接口")
@RequestMapping("/supermarket/purchase")
@RestController
public class AeaImProjPurchaseController {

    private static Logger logger = LoggerFactory.getLogger(AeaImProjPurchaseController.class);

    @Autowired
    private ProjPurchaseService projPurchaseService;
    @Autowired
    private AeaImProjPurchaseService aeaImProjPurchaseService;
    @Autowired
    private FileUtilsService fileUtilsService;

    @Autowired
    AeaProjInfoService aeaProjInfoService;
    @Autowired
    ProjectCodeService projectCodeService;

    @Autowired
    AeaImContractService aeaImContractService;

    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;


    @ApiOperation(value = "获取业主单位未发布的项目列表", notes = "获取业主单位未发布的项目列表,用于新增采购需求", httpMethod = "POST")
    @PostMapping(value = "/getUnpublishedProjInfoList")
    public ContentRestResult<EasyuiPageInfo<AeaProjInfo>> getUnpublishedProjInfoList(QueryUnpublishedProjInfo queryUnpublishedProjInfo, HttpServletRequest request) {

        logger.info("请求信息：" + queryUnpublishedProjInfo.toString());
        try {
            int pageNum = queryUnpublishedProjInfo.getPageNum();
            int pageSize = queryUnpublishedProjInfo.getPageSize();
            String projCode = queryUnpublishedProjInfo.getProjCode();
            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AeaProjInfo> list = projPurchaseService.getUnpublishedProjInfoList(projCode, page, request);
            return new ContentRestResult<EasyuiPageInfo<AeaProjInfo>>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult(false, null, e.getMessage());
        }
    }


    @ApiOperation(value = "获取项目信息", notes = "获取项目信息,包括单位信息和联系人信息,用于新增采购需求", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projInfoId", value = "项目主键ID")
    })
    @PostMapping(value = "/getProUnitLinkInfo/{projInfoId}")
    public ContentRestResult<ProUnitLinkVo> getProUnitLinkInfo(@PathVariable("projInfoId") String projInfoId, HttpServletRequest request) {
        return new ContentRestResult<>(true, projPurchaseService.getProUnitLinkInfo(projInfoId, request));
    }

    @ApiOperation(value = "获取项目信息", notes = "获取项目信息,包括单位信息和联系人信息,用于新增采购需求", httpMethod = "POST")
    @PostMapping(value = "/getProUnitLinkInfo")
    public ContentRestResult<ProUnitLinkVo> getProUnitLinkInfo(HttpServletRequest request) {
        return new ContentRestResult<>(true, projPurchaseService.getProUnitLinkInfo(null, request));
    }


    @ApiOperation(value = "获取中介服务事项列表", notes = "获取中介服务事项列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", dataType = "int"),
    })
    @PostMapping(value = "/getAgentServiceItemList")
    public ContentRestResult<EasyuiPageInfo<AeaItemServiceVo>> getAgentServiceItemList(String keyword, Integer pageNum, Integer pageSize) {

        try {
            Page page = null;
            if (pageNum != null && pageSize != null) {
                page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            }
            List<AeaItemServiceVo> list = projPurchaseService.getAgentServiceItemList(keyword, page);
            for (AeaItemServiceVo vo : list) {
                //设置事项办件类型
                BscDicCodeItem item_property = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("ITEM_PROPERTY", vo.getAgentItemProperty(), "012aa547-7104-418d-87cc-824f24f1a278");
                if (null != item_property) {
                    vo.setAgentItemPropertyName(item_property.getItemName());
                }

                BscDicCodeItem dueUnitType = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("DUE_UNIT_TYPE", vo.getAgentItemBjType(), "012aa547-7104-418d-87cc-824f24f1a278");
                if (null != dueUnitType) {
                    vo.setAgentItemDueUnitType(dueUnitType.getItemName());
                }
                String itemId = vo.getAgentItemId();
                String rootOrgId = vo.getRootOrgId();
                List<AeaItemBasic> parentItems = aeaItemBasicMapper.getAgentParentItem(itemId, rootOrgId);
                String names = parentItems.stream().map(AeaItemBasic::getItemName).collect(Collectors.joining(","));
                vo.setItemName(names);
            }
            return new ContentRestResult<EasyuiPageInfo<AeaItemServiceVo>>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult(false, null, e.getMessage());
        }
    }

    @ApiOperation(value = "根据中介服务事项获取中介服务", notes = "根据中介服务事项获取中介服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemVerId", value = "事项版本id")
    })
    @PostMapping(value = "/getItemServiceList/{itemVerId}")
    public ContentRestResult<List<AeaImServiceVo>> getItemServiceList(@PathVariable("itemVerId") String itemVerId) {
        try {
            List<AeaImServiceVo> list = projPurchaseService.getItemServiceListByItemVerId(itemVerId);
            return new ContentRestResult<>(true, list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult(false, null, e.getMessage());
        }
    }

    @ApiOperation(value = "新增采购需求", notes = "业主单位个人中心新增采购需求,批文文件上传officialRemarkFile，要求说明文件上传requireExplainFile")
    @PostMapping(value = "/postProjPurchase")
    @Deprecated
    public RestResult postProjPurchase(@Valid SaveAeaImProjPurchaseVo saveAeaImProjPurchaseVo, HttpServletRequest request, BindingResult bindingResult) {
        String errorMsg = BusinessUtils.checkBindingResult(bindingResult);

        if (StringUtils.isNotBlank(errorMsg)) {
            return new ContentRestResult<>(false, null, errorMsg);
        }

        try {
            AeaImProjPurchase aeaImProjPurchase = projPurchaseService.saveAeaImProjPurchase(saveAeaImProjPurchaseVo, request);
            return new ContentRestResult<>(true, aeaImProjPurchase.getProjPurchaseId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult<>(false, null, e.getMessage());
        }
    }

    @ApiOperation(value = "查询符合条件的中介机构", notes = "查询符合条件的中介机构")
    @PostMapping(value = "/getAgentUnitInfoList", produces = "application/json;charset=UTF-8")
    public ContentRestResult<List<AgentUnitInfoVo>> getAgentUnitInfoList(@RequestBody QueryAgentUnitInfoVo queryAgentUnitInfo) {
        try {
            return new ContentRestResult<>(true, aeaImProjPurchaseService.getAgentUnitInfoList(queryAgentUnitInfo));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult(false, null, e.getMessage());
        }
    }

    @ApiOperation(value = "获取采购公告列表", notes = "根据查询条件获取采购公告列表")
    @PostMapping(value = "/getPublicProjPurchaseList")
    public ContentRestResult<EasyuiPageInfo<AeaImProjPurchase>> getPublicProjPurchaseList(QueryProjPurchaseVo queryProjPurchaseVo) throws Exception {

        int pageNum = queryProjPurchaseVo.getPageNum();
        int pageSize = queryProjPurchaseVo.getPageSize();

        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
        List<AeaImProjPurchase> list = projPurchaseService.getPublicProjPurchaseList(queryProjPurchaseVo, page);
        return new ContentRestResult<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
    }

    @ApiOperation(value = "获取采购项目详细信息", notes = "获取采购项目详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "采购需求id")
    })
    @PostMapping(value = "/getPublicProjPurchaseDatail")
    public ContentRestResult<AeaImProjPurchaseDetailVo> getPublicProjPurchaseDatail(String projPurchaseId) throws Exception {
        return new ContentRestResult<>(true, projPurchaseService.getPublicProjPurchaseDatail(projPurchaseId));
    }

    @ApiOperation(value = "获取所有服务列表", notes = "获取所有服务列表")
    @PostMapping(value = "/getAllService")
    public ContentRestResult<List<AeaImService>> getAllService() throws Exception {
        return new ContentRestResult<>(true, projPurchaseService.getAllService());
    }

    @ApiOperation(value = "获取采购需求文件列表", notes = "获取采购需求文件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requireExplainFile", value = "采购需求文件id", required = true)
    })
    @PostMapping(value = "/getRequireExplainFileList/{requireExplainFile}")
    public ContentRestResult<List<BscAttForm>> getRequireExplainFileList(@PathVariable("requireExplainFile") String requireExplainFile) throws Exception {
        return new ContentRestResult<>(true, projPurchaseService.getRequireExplainFileList(requireExplainFile));
    }

    @ApiOperation(value = "获取采购需求批文文件列表", notes = "获取采购需求批文文件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "officialRemarkFile", value = "批文文件id", required = true)
    })
    @PostMapping(value = "/getOfficialRemarkFileList/{officialRemarkFile}")
    public ContentRestResult<List<BscAttForm>> getOfficialRemarkFileList(@PathVariable("officialRemarkFile") String officialRemarkFile) throws Exception {
        return new ContentRestResult<>(true, projPurchaseService.getOfficialRemarkFileList(officialRemarkFile));
    }

    @ApiOperation(value = "下载文件", notes = "下载文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailId", value = "文件id", required = true)
    })
    @RequestMapping(value = "/downloadAttachment/{detailId}", method = {RequestMethod.POST, RequestMethod.GET})
    public void downloadAttachment(@PathVariable("detailId") String detailId, HttpServletResponse response, HttpServletRequest request) throws Exception {
        if (StringUtils.isNotBlank(detailId)) {
            /* attachmentAdminService.downloadFileStrategy(detailId, response);*/
            fileUtilsService.downloadAttachment(detailId, response, request, false);
        }
    }

    @ApiOperation(value = "删除文件", notes = "删除文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailId", value = "文件id", required = true)
    })
    @RequestMapping(value = "/deleteAttachment/{detailId}", method = RequestMethod.POST)
    public RestResult deleteAttachment(@PathVariable("detailId") String detailId) {
        try {
            if (StringUtils.isBlank(detailId)) {
                return new RestResult(false, "删除失败：没有可删除文件!");
            }

            return new RestResult(FileUtils.deleteFiles(new String[]{detailId}));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RestResult(false, e.getMessage());
        }
    }

    @ApiOperation(value = "获取资质要求", notes = "获取资质要求")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "采购需求id", required = true)
    })
    @PostMapping("/getSelectedQualMajorRequire/{projPurchaseId}")
    public ContentRestResult<SelectedQualMajorRequire> getSelectedQualMajorRequire(@PathVariable("projPurchaseId") String projPurchaseId) throws Exception {
        return new ContentRestResult<>(true, projPurchaseService.getSelectedQualMajorRequire(projPurchaseId));
    }


    @ApiOperation(value = "获取业主列表", notes = "获取业主列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", dataType = "int", required = true)
    })
    @PostMapping("/getAeaUnitInfoByPage")
    public ContentRestResult<EasyuiPageInfo<AeaUnitInfo>> getAeaUnitInfoByPage(String keyword, int pageNum, int pageSize) throws Exception {
        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
        List<AeaUnitInfo> list = projPurchaseService.getAeaUnitInfoByPage(keyword, page);
        return new ContentRestResult<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
    }

    @ApiOperation(value = "获取自动生成的项目编码", notes = "获取自动生成的项目编码")
    @PostMapping(value = "/getAutoProjCode")
    public ContentRestResult getAutoProjCode() {
        return new ContentRestResult<>(true, BusinessUtils.getAutoProjCode());
    }

    @ApiOperation(value = "根据projPurchaseId查询采购要求项目已保存的信息", notes = "根据projPurchaseId查询采购要求项目已保存的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "项目需求发布ID", required = true, dataType = "String")
    })
    @PostMapping(value = "/showProjPurchaseByProjPurchaseId")
    public ContentRestResult<ShowProjPurchaseVo> showProjPurchaseByProjPurchaseId(String projPurchaseId) {
        try {
            ShowProjPurchaseVo showProjPurchaseVo = projPurchaseService.showProjPurchaseByProjPurchaseId(projPurchaseId);
            return new ContentRestResult<>(true, showProjPurchaseVo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult(false, null, e.getMessage());
        }
    }


    @ApiOperation(value = "提交采购项目", notes = "提交采购项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "项目需求发布ID", required = true, dataType = "String")
    })
    @PostMapping(value = "/submitProjPurchaseByProjPurchaseId")
    public RestResult submitProjPurchaseByProjPurchaseId(String projPurchaseId, HttpServletRequest request) {
        try {
            projPurchaseService.submitProjPurchaseByProjPurchaseId(projPurchaseId, request);
            return new ContentRestResult<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult(false, null, e.getMessage());
        }
    }

    @ApiOperation(value = "修改采购需求", notes = "修改采购需求,批文文件上传officialRemarkFile，要求说明文件上传requireExplainFile")
    @PostMapping(value = "/updateProjPurchase")
    public RestResult updateProjPurchase(@Valid SaveAeaImProjPurchaseVo saveAeaImProjPurchaseVo, HttpServletRequest request, BindingResult bindingResult) {

        String errorMsg = BusinessUtils.checkBindingResult(bindingResult);

        if (StringUtils.isNotBlank(errorMsg)) {
            return new ContentRestResult<>(false, null, errorMsg);
        }

        try {
            AeaImProjPurchase aeaImProjPurchase = projPurchaseService.updateProjPurchase(saveAeaImProjPurchaseVo, request);
            return new ContentRestResult<>(true, aeaImProjPurchase.getProjPurchaseId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult<>(false, null, e.getMessage());
        }
    }


    @ApiOperation(value = "修订采购需求", notes = "修订采购需求,批文文件上传officialRemarkFile，要求说明文件上传requireExplainFile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "operateDescribe", value = "修订备注", required = true, dataType = "String"),
            @ApiImplicitParam(name = "revisedFile", value = "修订文件列表")
    })
    @PostMapping(value = "/revisedProjPurchase")
    public RestResult revisedProjPurchase(@Valid SaveAeaImProjPurchaseVo saveAeaImProjPurchaseVo, String operateDescribe, @RequestParam(name = "revisedFile", required = false) List<MultipartFile> files, HttpServletRequest request, BindingResult bindingResult) {

        String errorMsg = BusinessUtils.checkBindingResult(bindingResult);

        if (StringUtils.isNotBlank(errorMsg)) {
            return new ContentRestResult<>(false, null, errorMsg);
        }

        try {
            projPurchaseService.revisedProjPurchase(saveAeaImProjPurchaseVo, operateDescribe, files, request);
            return new ContentRestResult<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult<>(false, null, e.getMessage());
        }
    }

    /**
     * ==========================================================================查询项目需求信息===========================================================================================
     **/
    @ApiOperation(value = "获取当前登录账户所有项目需求列表", notes = "获取当前登录账户所有项目需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishUnitInfoId", value = "业主单位ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业主委托人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "auditFlag", value = "项目状态", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/listAllProjPurchase")
    public ResultForm listAllProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, null);
    }

    @ApiOperation(value = "获取当前登录账户待审核项目需求列表", notes = "获取当前登录账户待审核项目需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishUnitInfoId", value = "业主单位ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业主委托人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/listApproveingProjPurchase")
    public ResultForm listApproveingProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        String[] auditFlags = {AuditFlagStatus.NO_COMMIT};
        return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
    }

    @ApiOperation(value = "获取当前登录账户待发布项目需求列表", notes = "获取当前登录账户待发布项目需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishUnitInfoId", value = "业主单位ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业主委托人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/listPublishingProjPurchase")
    public ResultForm listPublishingProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        String[] auditFlags = {AuditFlagStatus.AUDIT_PROGRESS};
        return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
    }

    @ApiOperation(value = "获取当前登录账户已退回项目需求列表", notes = "获取当前登录账户已退回项目需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishUnitInfoId", value = "业主单位ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业主委托人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/listReturnProjPurchase")
    public ResultForm listReturnProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        String[] auditFlags = {AuditFlagStatus.AUDIT_RETURN};
        return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
    }

    @ApiOperation(value = "获取当前登录账户已发布项目需求列表", notes = "获取当前登录账户已发布项目需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishUnitInfoId", value = "业主单位ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业主委托人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/listPublishProjPurchase")
    public ResultForm listPublishProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        String[] auditFlags = {AuditFlagStatus.REGISTRATION_PROGRESS, AuditFlagStatus.CHOOSE_PROGRESS,
                AuditFlagStatus.CHOOSE_START, AuditFlagStatus.CHOOSE_END, AuditFlagStatus.WAIT_CHOOSE, AuditFlagStatus.TIME_OUT};
        return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
    }

    @ApiOperation(value = "获取当前登录账户已中选项目需求列表", notes = "获取当前登录账户已中选项目需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishUnitInfoId", value = "业主单位ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业主委托人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/listChoosedProjPurchase")
    public ResultForm listChoosedProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        String[] auditFlags = {AuditFlagStatus.CHOOSE_END};
        return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
    }

    @ApiOperation(value = "获取当前登录账户无效项目需求列表", notes = "获取当前登录账户无效项目需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishUnitInfoId", value = "业主单位ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业主委托人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/listInvalidProjPurchase")
    public ResultForm listInvalidProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        String[] auditFlags = {AuditFlagStatus.INVALID};
        return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
    }


    @ApiOperation(value = "获取当前登录账户待处理项目需求列表", notes = "获取当前登录账户待处理项目需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishUnitInfoId", value = "业主单位ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业主委托人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/listPendingProjPurchase")
    public ResultForm listPendingProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        String[] auditFlags = {AuditFlagStatus.CHOOSE_START, AuditFlagStatus.CHOOSE_END,
                AuditFlagStatus.SERVICE_PROGRESS, AuditFlagStatus.SERVICE_FINISH, AuditFlagStatus.REGISTRATION_PROGRESS};
        return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
    }


    @ApiOperation(value = "获取当前登录账户待处理项目中介列表", notes = "获取当前登录账户待处理项目中介列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "项目竞标ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/selectIntermediaryList")
    public ResultForm selectIntermediaryList(String projPurchaseId, int pageSize, int pageNum) {
        return this.queryIntermediaryList(projPurchaseId, pageSize, pageNum);
    }


    @ApiOperation(value = "更新单位中标状态", notes = "更新单位中标状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitBiddingId", value = "单位竞标ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "projPurchaseId", value = "竞标项目ID", required = true, dataType = "String"),
    })
    @GetMapping(value = "/updateIntermediaryWonBidStatus")
    public ResultForm updateIntermediaryWonBidStatus(String unitBiddingId, String projPurchaseId) {
        return this.updateWonBidStatus(unitBiddingId, projPurchaseId);
    }

    @ApiOperation(value = "获取当前登录账户服务中项目列表", notes = "获取当前登录账户服务中项目列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishUnitInfoId", value = "业主单位ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业主委托人信息ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/listInServiceProjPurchase")
    public ResultForm listInServiceProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        String[] auditFlags = {AuditFlagStatus.SERVICE_PROGRESS};
        return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
    }


    @ApiOperation(value = "获取当前登录账户各项目状态列表", notes = "获取当前登录账户各项目状态列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishUnitInfoId", value = "业主单位ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业主委托人信息ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "serviceId", value = "服务Id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "biddingType", value = "选取方式", required = false, dataType = "String"),
            @ApiImplicitParam(name = "proType", value = "菜单编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "auditFlag", value = "菜单编号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "keyword", value = "查询关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int")
    })
    @GetMapping(value = "/listProjPurchase")
    public ResultForm listProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum) {
        String proType = aeaImProjPurchase.getProType();
        if ("1".equals(proType)) {//所有项目
            return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, null);
        } else if ("2".equals(proType)) {//待处理项目
            String[] auditFlags = {AuditFlagStatus.CHOOSE_START, AuditFlagStatus.CHOOSE_END,
                    AuditFlagStatus.SERVICE_PROGRESS, AuditFlagStatus.SERVICE_FINISH};
            return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
        } else if ("3".equals(proType)) {//待审核项目
            String[] auditFlags = {AuditFlagStatus.NO_COMMIT};
            return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
        } else if ("4".equals(proType)) {//待发布项目
            String[] auditFlags = {AuditFlagStatus.AUDIT_PROGRESS};
            return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
        } else if ("5".equals(proType)) {//已退回项目
            String[] auditFlags = {AuditFlagStatus.AUDIT_RETURN};
            return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
        } else if ("6".equals(proType)) {//已发布项目
            String[] auditFlags = {AuditFlagStatus.REGISTRATION_PROGRESS, AuditFlagStatus.CHOOSE_PROGRESS,
                    AuditFlagStatus.CHOOSE_START, AuditFlagStatus.CHOOSE_END, AuditFlagStatus.WAIT_CHOOSE,
                    AuditFlagStatus.TIME_OUT};
            return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
        } else if ("7".equals(proType)) {//已中选项目
            String[] auditFlags = {AuditFlagStatus.CHOOSE_END};
            return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
        } else if ("8".equals(proType)) {//无效项目
            String[] auditFlags = {AuditFlagStatus.INVALID};
            return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
        } else if ("9".equals(proType)) {//服务中项目
            String[] auditFlags = {AuditFlagStatus.SERVICE_PROGRESS};
            return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
        } else if ("10".equals(proType)) {//已完成项目
            String[] auditFlags = {AuditFlagStatus.SERVICE_FINISH};
            return this.queryProjPurchase(aeaImProjPurchase, pageSize, pageNum, auditFlags);
        } else {
            return new ContentResultForm(false);
        }


    }

    @ApiOperation(value = "根据projPurchaseId查询采购要求项目基本信息", notes = "根据projPurchaseId查询采购要求项目基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "项目需求发布ID", required = true, dataType = "String")
    })
    @GetMapping(value = "/getProjPurchaseInfoByProjPurchaseId")
    public ResultForm getProjPurchaseInfoByProjPurchaseId(String projPurchaseId) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            resultForm = projPurchaseService.getProjPurchaseInfoByProjPurchaseId(projPurchaseId);
        } catch (Exception e) {
            resultForm.setMessage("错误信息：" + e.getMessage());
            return resultForm;
        }
        return resultForm;
    }

    /**
     * 查询中介列表
     *
     * @param projPurchaseId
     * @param pageSize
     * @param pageNum
     * @return
     */
    @ApiIgnore
    private ResultForm queryIntermediaryList(String projPurchaseId, int pageSize, int pageNum) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            if (StringUtils.isNotBlank(projPurchaseId)) {
                resultForm = projPurchaseService.queryIntermediaryList(projPurchaseId, pageSize, pageNum);
            }
        } catch (Exception e) {
            resultForm.setMessage("错误信息：" + e.getMessage());
            return resultForm;
        }
        return resultForm;
    }

    /**
     * 更新单位中标状态
     *
     * @param unitBiddingId
     * @param projPurchaseId
     * @return
     */
    @ApiIgnore
    private ResultForm updateWonBidStatus(String unitBiddingId, String projPurchaseId) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            if (StringUtils.isNotBlank(unitBiddingId) && StringUtils.isNotBlank(projPurchaseId)) {
                resultForm = projPurchaseService.updateIntermediaryWonBidStatus(unitBiddingId, projPurchaseId);
            }
        } catch (Exception e) {
            resultForm.setMessage("错误信息：" + e.getMessage());
            return resultForm;
        }
        return resultForm;
    }


    /**
     * 查询采购项目列表
     *
     * @param aeaImProjPurchase
     * @param pageSize
     * @param pageNum
     * @param auditFlags
     * @return
     */
    @ApiIgnore
    private ResultForm queryProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum, String[] auditFlags) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            if (aeaImProjPurchase != null) {
                aeaImProjPurchase.setAuditFlags(auditFlags);
                resultForm = projPurchaseService.listProjPurchase(aeaImProjPurchase, pageSize, pageNum);
            }
        } catch (Exception e) {
            resultForm.setMessage("错误信息：" + e.getMessage());
            return resultForm;
        }
        return resultForm;
    }


    @ApiOperation(value = "申请置为无效项目", notes = "申请置为无效项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "项目需求ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "memo", value = "原因", required = true, dataType = "String"),
            @ApiImplicitParam(name = "file", value = "文件列表")

    })
    @PostMapping(value = "/applyProjPurchaseInvalid")
    public ResultForm applyProjPurchaseInvalid(String projPurchaseId, String memo, @RequestParam(name = "file", required = false) List<MultipartFile> files, HttpServletRequest request) {
        try {
            projPurchaseService.applyProjPurchaseInvalid(projPurchaseId, memo, files, request);
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @ApiOperation(value = "查询各菜单项目详细信息", notes = "查询各菜单项目详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "项目需求发布ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "企业Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "file", value = "文件列表")
    })
    @GetMapping(value = "/getPublishingInfoByProjPurchaseId")
    public ResultForm getPublishingInfoByProjPurchaseId(String projPurchaseId, String unitInfoId) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            resultForm = projPurchaseService.getPublishingInfoByProjPurchaseId(projPurchaseId, unitInfoId);
        } catch (Exception e) {
            resultForm.setMessage("错误信息：" + e.getMessage());
            return resultForm;
        }
        return resultForm;
    }

    @ApiOperation(value = "申请重新选取项目", notes = "申请重新选取项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "项目需求发布ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "memo", value = "申请原因", required = true, dataType = "String")
    })
    @PostMapping(value = "/reselectProjPurchase")
    public ResultForm reselectProjPurchase(String projPurchaseId, @RequestParam(name = "file", required = false) List<MultipartFile> files, String memo, HttpServletRequest request) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            AeaImProjPurchase aeaImProjPurchase = projPurchaseService.reselectProjPurchase(projPurchaseId, memo, files, request);
            if (aeaImProjPurchase != null) {
                resultForm.setSuccess(true);
            } else {
                return resultForm;
            }
        } catch (Exception e) {
            resultForm.setMessage("错误信息：" + e.getMessage());
            return resultForm;
        }
        return resultForm;
    }

    @ApiOperation(value = "获取采购项目服务类型", notes = "获取采购项目服务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitInfoId", value = "业主单位ID", required = false, dataType = "String")
    })
    @GetMapping(value = "/getServiceType")
    public ResultForm reselectProjPurchase(String unitInfoId) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            List<AeaImService> list = projPurchaseService.getServiceTypeList(unitInfoId);
            if (list != null) {
                resultForm.setSuccess(true);
                resultForm.setContent(list);
                return resultForm;
            } else {
                return resultForm;
            }
        } catch (Exception e) {
            resultForm.setMessage("错误信息：" + e.getMessage());
            return resultForm;
        }

    }


    @ApiOperation(value = "申请结束服务延期", notes = "申请结束服务延期")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "项目需求发布ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "memo", value = "申请原因", required = true, dataType = "String"),
            @ApiImplicitParam(name = "file", value = "文件列表"),
            @ApiImplicitParam(name = "serviceEndTime", value = "服务结束时间,格式yyyy-MM-dd", required = true),
    })
    @GetMapping(value = "/applyPostponeService")
    public RestResult applyPostponeService(@Valid ApplyPostponeServiceData applyPostponeServiceData, @RequestParam(name = "file", required = false) List<MultipartFile> files, String memo, HttpServletRequest request, BindingResult bindingResult) {
        String errorMsg = BusinessUtils.checkBindingResult(bindingResult);

        if (StringUtils.isNotBlank(errorMsg)) {
            return new ContentRestResult(false, null, errorMsg);
        }

        try {
            projPurchaseService.applyPostponeService(applyPostponeServiceData, memo, files, request);
            return new RestResult(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RestResult(false, e.getMessage());
        }
    }


    @ApiOperation(value = "统计综合数据展示", notes = "统计综合数据展示。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitInfoId", value = "单位Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "联系人Id", required = true, dataType = "String")
    })
    @GetMapping(value = "/showProjPurchaseData")
    public ResultForm showProjPurchaseData(String unitInfoId, String linkmanInfoId) {
        ContentResultForm resultForm = new ContentResultForm(false);
        try {

            if (StringUtils.isNotBlank(unitInfoId) || StringUtils.isNotBlank(linkmanInfoId)) {
                OwnerIndexData ownerIndexData = projPurchaseService.showProjPurchaseData(unitInfoId, linkmanInfoId);
                return new ContentResultForm<>(true, ownerIndexData, "success");

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, e.getMessage());
        }
        return resultForm;
    }

    @ApiOperation(value = "根据项目编码获取项目信息", notes = "根据项目编码获取项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "localCode", value = "项目编码", required = true, dataType = "String")
    })
    @GetMapping(value = "/getProjInfoByLocalCode")
    public ResultForm getProjInfoByLocalCode(String localCode) {
        if (StringUtils.isBlank(localCode)) {
            return new ResultForm(false, "项目编码不能为空");
        }

        try {
            return new ContentResultForm(true, projPurchaseService.getProjInfoByLocalCode(localCode));
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false, e.getMessage());
        }
    }

    @ApiOperation(value = "根据项目名称或者编码模糊查询项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "根据项目名称或者编码模糊查询项目", required = true, dataType = "String"),
    })
    @GetMapping("/info")
    public ResultForm getProjectInfo(@RequestParam String keyword) {
        Assert.isTrue(com.augurit.agcloud.framework.util.StringUtils.isNotBlank(keyword), "keyword is null");
        try {
            List<AeaProjInfo> aeaProjInfos = aeaProjInfoService.findAeaProjInfoByKeyword(keyword);
            if (aeaProjInfos.size() == 0 && !keyword.contains("#") && !keyword.contains("ZBM") && CommonTools.isComplianceWithRules(keyword)) {
                aeaProjInfos.addAll(projectCodeService.getProjInfoFromThirdPlatform(keyword, null,""));
            }
            return new ContentResultForm<>(true, aeaProjInfos, "Query success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    //20191120---新增接口---------------------------------------

    @Autowired
    private RestImApplyService restImApplyService;

    @ApiOperation(value = "新增采购需求并发起流程", notes = "业主单位个人中心新增采购需求,批文文件上传officialRemarkFile，要求说明文件上传requireExplainFile")
    @PostMapping(value = "/startProjPurchase")
    public RestResult startProjPurchase(@Valid SaveAeaImProjPurchaseVo saveAeaImProjPurchaseVo, HttpServletRequest request) {

        try {
            projPurchaseService.startProjPurchaseAndProcess(saveAeaImProjPurchaseVo, request);
            return new RestResult(true, "success");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult<>(false, null, e.getMessage());
        }
    }

    @PostMapping("/chooseWonBiddingUnit")
    @ApiOperation("选取中标机构")
    public ResultForm chooseWonBiddingUnit(String unitBiddingId, String projPurchaseId) throws Exception {
        restImApplyService.chooseImunit(projPurchaseId, unitBiddingId);
        return new ResultForm(true, "success");
    }

    @PostMapping("/confirmImunit")
    @ApiOperation("中选机构确认")
    public ResultForm confirmImunit(String projPurchaseId, String unitBiddingId, String opsLinkInfoId, String confirmFlag) throws Exception {
        restImApplyService.confirmImunit(projPurchaseId, unitBiddingId, confirmFlag);
        return new ResultForm(true, "success");
    }
}
