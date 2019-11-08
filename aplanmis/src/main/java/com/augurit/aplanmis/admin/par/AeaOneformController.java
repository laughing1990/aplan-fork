package com.augurit.aplanmis.admin.par;


import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaOneform;
import com.augurit.aplanmis.common.service.admin.oneform.AeaOneformService;
import com.github.pagehelper.Page;
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

import java.util.UUID;

/**
 * 总表管理-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par")
public class AeaOneformController {

    private static Logger logger = LoggerFactory.getLogger(AeaOneformController.class);

    @Autowired
    private AeaOneformService aeaOneformService;

    @RequestMapping("/indexOneForm.do")
    public ModelAndView indexOneform() throws Exception{

        return new ModelAndView("ui-jsp/kitymind/stage/oneform/par_oneform");
    }

    @ApiOperation(value = "查询总表列表,带分页", notes = "查询总表列表,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aeaOneform", value = "必填" , dataType = "AeaOneform" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "Page")
    })
    @RequestMapping("/listOneform.do")
    public EasyuiPageInfo<AeaOneform> listUsedAeaItemBasicTreeByPage(AeaOneform aeaOneform, Page page) {

        aeaOneform.setRootOrgId(SecurityContext.getCurrentOrgId());
        logger.debug("分页查询，过滤条件为{}，对象为{}", aeaOneform);
        return aeaOneformService.getAeaOneformList(aeaOneform, page);
    }

    /**
     *   ==================================== 前后端新写法 ====================================
     */

    @ApiOperation(value = "查询总表列表,带分页", notes = "查询总表列表,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aeaParOneform", value = "必填" , dataType = "AeaParOneform" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping(value = "/oneform/page.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaOneform> listOneformByPage(AeaOneform aeaParOneform, @ModelAttribute PageParam page){

        aeaParOneform.setRootOrgId(SecurityContext.getCurrentOrgId());
        logger.debug("分页查询，过滤条件为{}，对象为{}", aeaParOneform);
        return aeaOneformService.getAeaOneformList(aeaParOneform, page.convertPage());
    }

    @ApiOperation(value = "通过id获取总表数据", notes = "通过id获取总表数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" ,required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getAeaParOneform.do", method = {RequestMethod.POST, RequestMethod.GET})
    public AeaOneform getAeaParOneform(String id) throws Exception {

        if (StringUtils.isNotBlank(id)){
            logger.debug("根据ID获取AeaParOneform对象，ID为：{}", id);
            return aeaOneformService.getAeaOneformById(id);
        }else {
            logger.debug("构建新的AeaParOneform对象");
            return new AeaOneform();
        }
    }

    @ApiOperation(value = "保存或者更新总表数据", notes = "保存或者更新总表数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aeaOneform", value = "必填" , dataType = "AeaOneform" ,paramType = "body"),
    })
    @RequestMapping(value = "/saveParOneform.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm saveParOneform(AeaOneform aeaOneform) throws Exception {

        if (StringUtils.isNotBlank(aeaOneform.getOneformId())){
            aeaOneformService.updateAeaOneform(aeaOneform);
        } else {
            aeaOneform.setOneformId(UUID.randomUUID().toString());
            aeaOneform.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaOneformService.saveAeaOneform(aeaOneform);
        }
        return new ContentResultForm<>(true, aeaOneform);
    }

    @ApiOperation(value = "通过id删除总表数据", notes = "通过id删除总表数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" ,required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/deleteOneformById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm deleteOneformById(String id) throws Exception{

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("删除总表Form对象，对象id为：{}", id);
        aeaOneformService.deleteAeaOneformById(id);
        return new ResultForm(true);
    }

    @ApiOperation(value = "通过ids批量删除总表数据", notes = "通过ids批量删除总表数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "必填" ,required = true, allowMultiple = true, dataType = "String" , paramType = "query"),
    })
    @RequestMapping(value = "/deleteMulOneformByIds.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm deleteMulOneformByIds(String[] ids) throws Exception{

        if(ids!=null&&ids.length>0){
            aeaOneformService.batchDelAeaOneformByIds(ids);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @ApiOperation(value = "获取总表最大排序", notes = "获取总表最大排序")
    @RequestMapping(value = "/getMaxSortNo.do", method = {RequestMethod.POST, RequestMethod.GET})
    public Long getMaxSortNo() throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaOneformService.getMaxSortNo(rootOrgId);
    }

    @ApiOperation(value = "通过id启用或者禁用总表数据", notes = "通过id启用或者禁用总表数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" ,required = true, dataType = "String" , paramType = "query"),
    })
    @RequestMapping(value = "/enOrDisableIsActive.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm enOrDisableIsActive(String id) {

        aeaOneformService.enOrDisableIsActive(id);
        return new ResultForm(true);
    }
}