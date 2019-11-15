package com.augurit.aplanmis.mall.cert.controller;

import com.augurit.aplanmis.integration.license.dto.LicenseAuthResDTO;
import com.augurit.aplanmis.mall.cert.service.AeaCertMallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author QinJianping
 * @date 2019/11/15  13:58
 * @desc
 **/
@Api(description = "网厅申报-->电子证照",tags={"电子证照"})
@RestController
@RequestMapping("/aea/cert")
public class AeaCertMallController {

    private static Logger logger = LoggerFactory.getLogger(AeaCertMallController.class);

    @Autowired
    private AeaCertMallService aeaCertService;
//    @RequestMapping("/index.do")
//    public ModelAndView index(ModelMap modelMap) {
//
//        getData(modelMap);
//        return new ModelAndView("ui-jsp/cert/cert_index");
//    }
//
//    /**
//     * 获取数据字典数据
//     *
//     * @param modelMap
//     */
//    private void getData(ModelMap modelMap) {
//
//        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(SecurityContext.getCurrentOrgId());
//        if (topOrg != null) {
//            // 数据字典承诺办结时限单位
//            List<BscDicCodeItem> dueUnits = bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", topOrg.getOrgId());
//            modelMap.put("dueUnits", dueUnits);
//
//            // 数据字典获取持证所属类型
//            List<BscDicCodeItem> certHolderTypes = bscDicCodeService.getActiveItemsByTypeCode("CERT_HOLDER_TYPE", topOrg.getOrgId());
//            modelMap.put("certHolderTypes", certHolderTypes);
//        }
//    }
//
//    @RequestMapping("/listAeaCertByPage.do")
//    public EasyuiPageInfo<AeaCert> listAeaCertByPage(AeaCert aeaCert, Page page) {
//
//        aeaCert.setRootOrgId(SecurityContext.getCurrentOrgId());
//        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCert);
//        PageInfo<AeaCert> pageInfo = aeaCertService.listAeaCert(aeaCert, page);
//        return PageHelper.toEasyuiPageInfo(pageInfo);
//    }
//
//    @RequestMapping(value = "/gtreeBscAttDir.do", method = {RequestMethod.GET, RequestMethod.POST})
//    public List<ZtreeNode> gtreeBscAttDir() throws Exception {
//
//        return aeaCertService.gtreeBscAttDir(SecurityContext.getCurrentOrgId());
//    }
//
//    @RequestMapping("/gtreeTypeCert.do")
//    public List<ZtreeNode> gtreeTypeCert() {
//
//        String rootOrgId = SecurityContext.getCurrentOrgId();
//        return aeaCertService.gtreeTypeCert(rootOrgId);
//    }
//
//    @ApiOperation(value = "获取证照最大排序号", notes = "获取证照最大排序号")
//    @RequestMapping("/getMaxCertSortNo.do")
//    public Long getMaxCertSortNo() {
//
//        String rootOrgId = SecurityContext.getCurrentOrgId();
//        return aeaCertService.getMaxCertSortNo(rootOrgId);
//    }
//
//    @ApiOperation(value = "检验证照是否存在")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "certId", value = "证照id", type = "string"),
//        @ApiImplicitParam(name = "certCode", value = "证照编号", type = "string")
//    })
//    @RequestMapping(value = "/checkUniqueCertCode.do", method = {RequestMethod.GET, RequestMethod.POST})
//    public String checkUniqueCertCode(String certId, String certCode) {
//
//        if (StringUtils.isBlank(certCode)) {
//            return "false";
//        }
//        String rootOrgId = SecurityContext.getCurrentOrgId();
//        return aeaCertService.checkUniqueCertCode(certId, certCode, rootOrgId) + "";
//    }
//
//    /**
//     * 保存或编辑电子证照定义表
//     *
//     * @param aeaCert 电子证照定义表
//     * @return 返回结果对象 包含结果信息
//     */
//    @ApiOperation(value = "保存或编辑电子证照", notes = "保存或编辑电子证照")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "aeaCert", value = "非必填", dataType = "AeaCert" ,paramType = "body")
//    })
//    @RequestMapping(value = "/saveAeaCert.do", method = {RequestMethod.GET, RequestMethod.POST})
//    public ResultForm saveAeaCert(AeaCert aeaCert) {
//
//        if (StringUtils.isNotBlank(aeaCert.getCertId())) {
//            aeaCertService.updateAeaCert(aeaCert);
//        } else {
//            aeaCert.setCertId(UUID.randomUUID().toString());
//            aeaCert.setRootOrgId(SecurityContext.getCurrentOrgId());
//            aeaCertService.saveAeaCert(aeaCert);
//        }
//        return new ContentResultForm<>(true, aeaCert);
//    }
//
//    @ApiOperation(value = "通过id删除电子证照数据", notes = "通过id删除电子证照数据")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "id", value = "电子证照id", required = true , dataType = "String" ,paramType = "query"),
//    })
//    @RequestMapping(value = "/deleteAeaCertById.do", method = {RequestMethod.GET, RequestMethod.POST})
//    public ResultForm deleteAeaCertById(String id) {
//
//        if (StringUtils.isNotBlank(id)) {
//            logger.debug("删除电子证照定义表Form对象，对象id为：{}", id);
//            aeaCertService.deleteAeaCertById(id);
//            return new ResultForm(true);
//        }
//        return new ResultForm(false, "参数id为空!");
//    }
//
//    @ApiOperation(value = "通过ids批量删除证照数据", notes = "通过ids批量删除证照数据")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "ids", value = "证照id集合,以逗号分隔", required = true , dataType = "String" , allowMultiple = true, paramType = "query"),
//    })
//    @RequestMapping(value="/batchDeleteCertByIds.do", method = {RequestMethod.GET, RequestMethod.POST})
//    public ResultForm batchDeleteCertByIds(String[] ids) {
//
//        if (ids != null && ids.length > 0) {
//            logger.debug("删除电子证照定义表Form对象，对象id为：{}", ids);
//            aeaCertService.batchDeleteCertByIds(ids);
//            return new ResultForm(true);
//        }
//        return new ResultForm(false, "参数id为空!");
//    }
//
//    @ApiOperation(value = "通过id获取电子证照数据", notes = "通过id获取电子证照数据")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "id", value = "电子证照id", required = true , dataType = "String" ,paramType = "query"),
//    })
//    @RequestMapping("/getAeaCert.do")
//    public AeaCert getAeaCert(String id) {
//
//        if (StringUtils.isNotBlank(id)) {
//            logger.debug("根据ID获取AeaCert对象，ID为：{}", id);
//            return aeaCertService.getAeaCertById(id);
//        } else {
//            logger.debug("构建新的AeaCert对象");
//            return new AeaCert();
//        }
//    }
//
//    @RequestMapping("/listStageNoSelectCertByPage.do")
//    public EasyuiPageInfo<AeaCert> listStageNoSelectCertByPage(AeaParIn in, Page page) {
//
//        PageInfo<AeaCert> pageInfo = aeaCertService.listStageNoSelectCertByPage(in, page);
//        return PageHelper.toEasyuiPageInfo(pageInfo);
//    }
//
//    @RequestMapping("/listStageNoSelectCert.do")
//    public List<AeaCert> listStageNoSelectCert(AeaParIn in) {
//
//        return aeaCertService.listStageNoSelectCert(in);
//    }
//
//    @RequestMapping("/listItemNoSelectCertByPage.do")
//    public EasyuiPageInfo<AeaCert> listItemNoSelectCertByPage(AeaItemInout inout, Page page) {
//
//        PageInfo<AeaCert> pageInfo = aeaCertService.listItemNoSelectCertByPage(inout, page);
//        return PageHelper.toEasyuiPageInfo(pageInfo);
//    }
//
//    @RequestMapping("/listItemNoSelectCert.do")
//    public List<AeaCert> listItemNoSelectCert(AeaItemInout inout) {
//
//        return aeaCertService.listItemNoSelectCert(inout);
//    }
//
//    //===================  前后端分离新方法 ==================
//
//    @ApiOperation(value = "查询电子证照列表,带分页", notes = "查询电子证照列表,带分页")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "cert", value = "非必填", dataType = "AeaCert" ,paramType = "body"),
//        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
//    })
//    @RequestMapping(value = "/page.do", method = {RequestMethod.POST, RequestMethod.GET})
//    public EasyuiPageInfo<AeaCert> listByPage(AeaCert cert, @ModelAttribute PageParam page){
//
//        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", cert);
//        PageInfo<AeaCert> pageInfo = aeaCertService.listAeaCert(cert, page.convertPage());
//        return PageHelper.toEasyuiPageInfo(pageInfo);
//    }
//
//    @ApiOperation(value = "检验证照是否存在", notes = "检验证照是否存在")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "certId", value = "证照id", type = "string"),
//        @ApiImplicitParam(name = "certCode", value = "证照编号", type = "string")
//    })
//    @RequestMapping(value = "/checkUniqueCode.do", method = {RequestMethod.GET, RequestMethod.POST})
//    public Boolean checkUniqueCode(String certId, String certCode) {
//
//        if (StringUtils.isBlank(certCode)) {
//            return false;
//        }
//        String rootOrgId = SecurityContext.getCurrentOrgId();
//        return aeaCertService.checkUniqueCertCode(certId, certCode, rootOrgId);
//    }
//
//    @ApiOperation(value = "获取文件库", notes = "获取文件库")
//    @RequestMapping(value = "/gtreeAttDirForEui.do", method = {RequestMethod.GET, RequestMethod.POST})
//    public List<ElementUiRsTreeNode> gtreeAttDirForEui() throws Exception {
//
//        return aeaCertService.gtreeAttDirForEui(SecurityContext.getCurrentOrgId());
//    }
//
//    @ApiOperation(value = "获取承诺办结时限单位数据字典", notes = "获取承诺办结时限单位数据字典")
//    @RequestMapping(value = "/listDueUnitType.do", method = {RequestMethod.GET, RequestMethod.POST})
//    public List<BscDicCodeItem> listDueUnitType() throws Exception {
//
//        return bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", SecurityContext.getCurrentOrgId());
//    }
//
//    @ApiOperation(value = "获取获取持证所属类型数据字典", notes = "获取获取持证所属类型数据字典")
//    @RequestMapping(value = "/listCertHolderType.do", method = {RequestMethod.GET, RequestMethod.POST})
//    public List<BscDicCodeItem> listCertHolderType() throws Exception {
//
//        return bscDicCodeService.getActiveItemsByTypeCode("CERT_HOLDER_TYPE", SecurityContext.getCurrentOrgId());
//    }

    @ApiOperation(value = "通过查询条件获取电子证照库数据", notes = "通过查询条件获取电子证照库数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemVerIds", value = "事项版本IDs", required = true , dataType = "String" ,paramType = "query"),
            @ApiImplicitParam(name = "identityNumber", value = "申办证件号码", required = true , dataType = "String" ,paramType = "query"),
    })
    @RequestMapping("/getLicenseAuthRes")
    public LicenseAuthResDTO getLicenseAuthRes(String itemVerIds, String identityNumber) throws Exception {
        return aeaCertService.getLicenseAuthRes(itemVerIds, identityNumber);
    }

    @ApiOperation(value = "通过电子证照编码获取证照显示地址", notes = "通过电子证照编码获取证照显示地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authCode", value = "证照编码", required = true , dataType = "String" ,paramType = "query"),
    })
    @RequestMapping("/getViewLicenseURL")
    public String getViewLicenseURL(String authCode) throws Exception {
        return aeaCertService.getViewLicenseURL(authCode);
    }
}
