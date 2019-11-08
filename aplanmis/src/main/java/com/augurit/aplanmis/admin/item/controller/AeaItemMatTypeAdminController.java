package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaItemMatType;
import com.augurit.aplanmis.common.service.admin.item.AeaItemMatTypeAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.metadata.ValidateUnwrappedValue;
import java.util.List;
import java.util.UUID;

/**
 * @author ZhangXinhui
 * @date 2019/7/26 026 14:13
 * @desc
 **/
@RestController
@RequestMapping("/aea/item/mat/type")
@Api(tags = "材料类型库")
public class AeaItemMatTypeAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemMatTypeAdminController.class);

    @Autowired
    private AeaItemMatTypeAdminService aeaItemMatTypeAdminService;

    @RequestMapping("/index.do")
    public ModelAndView indexAeaItemMatType() {

        return new ModelAndView("ui-jsp/aplanmis/item/mat_type_index");
    }

    @ApiOperation(value = "材料类型分页列表", notes = "材料类型分页列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", dataType = "int", required = true),
        @ApiImplicitParam(name = "pageSize", dataType = "int", required = true),
        @ApiImplicitParam(name = "matType", value = "非必填", dataType = "AeaItemMatType" ,paramType = "body")
    })
    @RequestMapping(value ="/listAeaItemMatTypePage.do", method = {RequestMethod.GET, RequestMethod.POST})
    public EasyuiPageInfo<AeaItemMatType> listAeaItemMatTypePage(AeaItemMatType matType,
                                                                 @RequestParam int pageNum,
                                                                 @RequestParam int pageSize) throws Exception {
        Page page = new Page();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        PageInfo<AeaItemMatType> pageInfo = aeaItemMatTypeAdminService.listAeaItemMatType(matType, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @ApiOperation(value = "获取材料分类ztree树结构", notes = "获取材料分类ztree树结构")
    @RequestMapping(value ="/gtreeMatType.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List<ZtreeNode> gtreeMatType() throws Exception {

        return aeaItemMatTypeAdminService.gtreeMatType();
    }

    @ApiOperation(value = "查找是否已存在该分类编号", notes = "查找是否已存在该分类编号")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "matTypeId", value = "类型id", dataType = "String"),
        @ApiImplicitParam(name = "typeCode", value = "类型编码", dataType = "String", required = true)
    })
    @RequestMapping(value ="/checkUniqueTypeCode.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String checkUniqueTypeCode(String matTypeId, String typeCode) {

        if (StringUtils.isBlank(typeCode)) {
            return "false";
        }
        return aeaItemMatTypeAdminService.checkUniqueTypeCode(matTypeId, typeCode) + "";
    }

    /**
     * 保存或编辑材料类型表
     *
     * @param matType 材料类型表
     * @return 返回结果对象 包含结果信息
     * @
     */
    @ApiOperation(value = "更新保存分类数据", notes = "更新保存分类数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "matType", value = "非必填", dataType = "AeaItemMatType" ,paramType = "body")
    })
    @RequestMapping(value ="/saveAeaItemMatType.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ContentResultForm<AeaItemMatType> saveAeaItemMatType(AeaItemMatType matType) {

        if (StringUtils.isNotBlank(matType.getMatTypeId())) {
            aeaItemMatTypeAdminService.updateAeaItemMatType(matType);
        } else {
            matType.setMatTypeId(UUID.randomUUID().toString());
            aeaItemMatTypeAdminService.saveAeaItemMatType(matType);
        }
        return new ContentResultForm<>(true, matType);
    }

    @ApiOperation(value = "通过类型id获取分类数据", notes = "通过类型id获取分类数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填", required = true , dataType = "String" ,paramType = "query"),
    })
    @RequestMapping("/getAeaItemMatType.do")
    public AeaItemMatType getAeaItemMatType(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取AeaItemMatType对象，ID为：{}", id);
            return aeaItemMatTypeAdminService.getAeaItemMatTypeById(id);
        } else {
            logger.debug("构建新的AeaItemMatType对象");
            return new AeaItemMatType();
        }
    }

    @ApiOperation(value = "通过类型id删除分类", notes = "通过类型id删除分类")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填", required = true , dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value ="/deleteAeaItemMatTypeById.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm deleteAeaItemMatTypeById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除材料类型表Form对象，对象id为：{}", id);
                aeaItemMatTypeAdminService.deleteAeaItemMatTypeById(id);
            return new ResultForm(true);
        }
        return new ResultForm(false, "材料类型id为空!");
    }


    @ApiOperation(value = "通过类型id集合批量删除分类", notes = "通过类型id集合批量删除分类")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "必填", required = true ,allowMultiple = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value ="/batchDeleteAeaItemMatType.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm batchDeleteAeaItemMatType(String[] ids) {

        if (ids != null) {
            aeaItemMatTypeAdminService.batchDeleteAeaItemMatType(ids);
        }
        return new ResultForm(true);
    }

    /**
     * 获取类别树
     */
    @ApiOperation(value = "获取类别树", notes = "获取类别树")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "matType", value = "非必填", dataType = "AeaItemMatType" ,paramType = "body")
    })
    @RequestMapping(value = "/getListMatTypeZtreeNode.do", method = {RequestMethod.POST, RequestMethod.GET})
    public List<ZtreeNode> getListMatTypeZtreeNode(AeaItemMatType matType) {

        return aeaItemMatTypeAdminService.getListMatTypeZtreeNode(matType);
    }

    // ==========================  新的方法 ==========================

    @ApiOperation(value = "获取材料分类,树列表", notes = "获取材料分类,树列表")
    @RequestMapping(value ="/gtreeMatTypeForEUi.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List<ElementUiRsTreeNode> gtreeMatTypeForEUi() throws Exception {

        return aeaItemMatTypeAdminService.gtreeMatTypeForEUi();
    }

    @ApiOperation(value = "查找是否已存在该分类编号", notes = "查找是否已存在该分类编号")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "matTypeId", value = "类型id", dataType = "String"),
        @ApiImplicitParam(name = "typeCode", value = "类型编码", dataType = "String", required = true)
    })
    @RequestMapping(value ="/checkUniqueTypeCodeNew.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Boolean checkUniqueTypeCodeNew(String matTypeId, String typeCode) {

        if (StringUtils.isBlank(typeCode)) {
            return false;
        }
        return aeaItemMatTypeAdminService.checkUniqueTypeCode(matTypeId, typeCode);
    }

    @ApiOperation(value = "获取某组织下的所有材料类型（不包括自己以及包含的自己子类型）",notes = "获取某组织下的所有材料类型（不包括自己以及包含的自己子类型）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "matTypeId", value = "当前类型id", required = true, dataType = "String", paramType = "String"),
    })
    @RequestMapping(value ="/other/tree.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List<ElementUiRsTreeNode> listOtherMatTypesByMatTypeId(String matTypeId){

        if(StringUtils.isBlank(matTypeId)){
            throw new InvalidParameterException("当前操作对象matTypeId为空！");
        }
        logger.debug("获取某组织下的所有材料类型（不包括自己以及包含的自己子类型）");
        return aeaItemMatTypeAdminService.listOtherMatTypesByMatTypeId(matTypeId);
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
        logger.debug("设置父级菜单");
        AeaItemMatType newForm = aeaItemMatTypeAdminService.setParentMatType(curTypeId, targetTypeId);
        return new ContentResultForm<AeaItemMatType>(true, newForm);
    }
}
