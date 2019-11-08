package com.augurit.aplanmis.admin.cert.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaCertType;
import com.augurit.aplanmis.common.service.admin.cert.AeaCertTypeAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * @author ZhangXinhui
 * @date 2019/7/29 029 11:47
 * @desc
 **/
@Api(description = "前缀：/aea/cert/type",tags={"电子证照类型"})
@RestController
@RequestMapping("/aea/cert/type")
public class AeaCertTypeAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaCertTypeAdminController.class);

    @Autowired
    private AeaCertTypeAdminService aeaCertTypeAdminService;

    @RequestMapping("/index.do")
    public ModelAndView indexAeaCertType() {

        return new ModelAndView("aea/cert/type_index");
    }

    @ApiOperation(value = "证照类型分页列表", notes = "证照类型分页列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Page", dataType = "Page", required = true),
        @ApiImplicitParam(name = "certType", value = "非必填", dataType = "AeaCertType" ,paramType = "body")
    })
    @RequestMapping(value = "/listAeaCertType.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaCertType> listAeaCertType(AeaCertType certType, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", certType);
        PageInfo<AeaCertType> pageInfo = aeaCertTypeAdminService.listAeaCertType(certType, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @ApiOperation(value = "获取电子证照分类最大排序号",notes = "获取电子证照分类最大排序号")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "parentId", value = "父级分类id", required = true , dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getMaxSortNo.do", method = {RequestMethod.POST, RequestMethod.GET})
    public Long getMaxSortNo(String parentId) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaCertTypeAdminService.getMaxSortNo(parentId, rootOrgId);
    }

    @ApiOperation(value = "检查电子证照分类编号是否唯一",notes = "检查电子证照分类编号是否唯一")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "certTypeId", value = "证照分类id,必填", required = true , dataType = "String" ,paramType = "query"),
        @ApiImplicitParam(name = "typeCode", value = "证照分类编号,必填", required = true , dataType = "String" ,paramType = "query")
    })
    @RequestMapping(value = "/checkUniqueCertTypeCode.do", method = {RequestMethod.POST, RequestMethod.GET})
    public String checkUniqueCertTypeCode(String certTypeId, String typeCode) {

        if (StringUtils.isBlank(typeCode)) {
            return "false";
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaCertTypeAdminService.checkUniqueCertTypeCode(certTypeId, typeCode, rootOrgId) + "";
    }

    @ApiOperation(value = "保存或更新电子证照分类",notes = "保存或更新电子证照分类")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "certType", value = "非必填", dataType = "AeaCertType" ,paramType = "body")
    })
    @RequestMapping(value = "/saveAeaCertType.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ContentResultForm<AeaCertType> saveAeaCertType(AeaCertType certType) {

        if (StringUtils.isNotBlank(certType.getCertTypeId())) {
            aeaCertTypeAdminService.updateAeaCertType(certType);
        } else {
            certType.setCertTypeId(UUID.randomUUID().toString());
            aeaCertTypeAdminService.saveAeaCertType(certType);
        }
        return new ContentResultForm<>(true, certType);
    }

    @ApiOperation(value = "通过电子证照类型id删除电子证照分类",notes = "通过电子证照类型id删除电子证照分类")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填", required = true , dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/deleteAeaCertTypeById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm deleteAeaCertTypeById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除电子证照类型表Form对象，对象id为：{}", id);
            aeaCertTypeAdminService.deleteAeaCertTypeById(id);
            return new ResultForm(true);
        }
        return new ResultForm(false, "参数id为空!");
    }

    @ApiOperation(value = "通过电子证照类型ids批量删除电子证照分类",notes = "通过电子证照类型ids批量删除电子证照分类")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "必填", required = true , allowMultiple = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/batchDeleteCertType.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm batchDeleteCertType(String[] ids) {

        if (ids != null && ids.length > 0) {
            aeaCertTypeAdminService.batchDeleteCertType(ids);
        }
        return new ResultForm(true);
    }

    @ApiOperation(value = "通过id启用或禁用证电子证照类型",notes = "通过id启用或禁用电子证照类型")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "电子证照类型id, 默认必填", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/changIsActiveState.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm changIsActiveState(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaCertTypeAdminService.changIsActiveState(id);
        }
        return new ResultForm(true);
    }

    @ApiOperation(value = "通过id获取电子证照类型数据",notes = "通过id获取电子证照类型数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "电子证照类型id, 默认必填", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/getAeaCertType.do", method = {RequestMethod.POST, RequestMethod.GET})
    public AeaCertType getAeaCertType(String id) {

        if (StringUtils.isNotBlank(id)) {
            return aeaCertTypeAdminService.getAeaCertTypeById(id);
        } else {
            logger.debug("构建新的AeaCertType对象");
        }
        return new AeaCertType();
    }

    //===================  前后端分离新方法 ==================

    @ApiOperation(value = "获取电子证照类型树结构", notes = "获取电子证照类型树结构")
    @RequestMapping(value ="/gtreeCertType.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List<ElementUiRsTreeNode> gtreeCertType() {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaCertTypeAdminService.gtreeCertType(rootOrgId);
    }

    @ApiOperation(value = "查询电子证照类型列表,带分页", notes = "查询电子证照类型列表,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "certType", value = "非必填", dataType = "AeaCertType" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping(value = "/page.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaCertType> listAeaCertTypeByPage(AeaCertType certType, @ModelAttribute PageParam page){

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", certType);
        PageInfo<AeaCertType> pageInfo = aeaCertTypeAdminService.listAeaCertType(certType, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @ApiOperation(value = "检查电子证照分类编号是否唯一",notes = "检查电子证照分类编号是否唯一")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "certTypeId", value = "证照分类id,必填", required = true , dataType = "String" ,paramType = "query"),
        @ApiImplicitParam(name = "typeCode", value = "证照分类编号,必填", required = true , dataType = "String" ,paramType = "query")
    })
    @RequestMapping(value = "/checkUniqueCode.do", method = {RequestMethod.POST, RequestMethod.GET})
    public Boolean checkUniqueCertTypeCodeNew(String certTypeId, String typeCode) {

        if (StringUtils.isBlank(typeCode)) {
            return false;
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaCertTypeAdminService.checkUniqueCertTypeCode(certTypeId, typeCode, rootOrgId);
    }

    @ApiOperation(value = "获取某组织下的所有证照类型（不包括自己以及包含的自己子类型）",notes = "获取某组织下的所有证照类型（不包括自己以及包含的自己子类型）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "certTypeId", value = "当前类型id", required = true, dataType = "String", paramType = "String"),
    })
    @RequestMapping(value ="/other/tree.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List<ElementUiRsTreeNode> listOtherCertTypesByCertTypeId(String certTypeId){

        if(StringUtils.isBlank(certTypeId)){
            throw new InvalidParameterException("当前操作对象certTypeId为空！");
        }
        return aeaCertTypeAdminService.listOtherCertTypesByCertTypeId(certTypeId);
    }

    @ApiOperation(value = "设置父级类型", notes = "设置父级类型")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "curTypeId", value = "当前材料类型id", required = true , dataType = "String" ,paramType = "query"),
        @ApiImplicitParam(name = "targetTypeId", value = "新父级类型id", required = true , dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/set/parent", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm setParentMatType(String curTypeId, String targetTypeId){

        if(StringUtils.isBlank(curTypeId)){
            throw new InvalidParameterException("传递参数curTypeId为空！");
        }
        if(StringUtils.isBlank(targetTypeId)){
            throw new InvalidParameterException("传递参数targetTypeId为空！");
        }
        AeaCertType newForm = aeaCertTypeAdminService.setParentCertType(curTypeId, targetTypeId);
        return new ContentResultForm<AeaCertType>(true, newForm);
    }
}
