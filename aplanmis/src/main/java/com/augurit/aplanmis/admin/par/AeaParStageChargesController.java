package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStageCharges;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageChargesAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
* 阶段办事指南收费项目信息-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/par/stage/charges")
public class AeaParStageChargesController {

private static Logger logger = LoggerFactory.getLogger(AeaParStageChargesController.class);

    @Autowired
    private AeaParStageChargesAdminService aeaParStageChargesService;

    @RequestMapping("/indexAeaParStageCharges.do")
    public ModelAndView indexAeaParStageCharges(AeaParStageCharges aeaParStageCharges){

        return new ModelAndView("aea/par/stage/stage_charges_index");
    }

    @RequestMapping("/listChargesByPage.do")
    public EasyuiPageInfo<AeaParStageCharges> listAeaParStageCharges(AeaParStageCharges aeaParStageCharges, Page page){

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParStageCharges);
        PageInfo pageInfo = aeaParStageChargesService.listAeaParStageCharges(aeaParStageCharges,page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @ApiOperation(value = "查询阶段收费项目数据,带分页", notes = "查询阶段收费项目数据,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "charges", value = "必填" , dataType = "AeaParStageCharges" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping(value = "/page.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaParStageCharges> listStageChargesByPage(AeaParStageCharges charges, Page page){

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", charges);
        PageInfo pageInfo = aeaParStageChargesService.listAeaParStageCharges(charges, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @ApiOperation(value = "通过id获取收费项目数据", notes = "通过id获取收费项目数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getChargesById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public AeaParStageCharges getChargesById(String id){

        if (StringUtils.isNotBlank(id)){
            logger.debug("根据ID获取AeaParStageCharges对象，ID为：{}", id);
            return aeaParStageChargesService.getAeaParStageChargesById(id);
        }else {
            logger.debug("构建新的AeaParStageCharges对象");
            return new AeaParStageCharges();
        }
    }

    @ApiOperation(value = "通过id更新收费项目数据", notes = "通过id更新收费项目数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "charges", value = "必填" , dataType = "AeaParStageCharges" ,paramType = "body"),
    })
    @RequestMapping(value = "/updateAeaParStageCharges.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm updateAeaParStageCharges(AeaParStageCharges charges){

        logger.debug("更新客户档案信息Form对象，对象为：{}", charges);
        aeaParStageChargesService.updateAeaParStageCharges(charges);
        return new ResultForm(true);
    }

    /**
    * 保存或编辑阶段办事指南收费项目信息
     *
    * @param charges 阶段办事指南收费项目信息
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @ApiOperation(value = "通过id更新收费项目数据", notes = "通过id更新收费项目数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "charges", value = "必填" , dataType = "AeaParStageCharges" ,paramType = "body"),
    })
    @RequestMapping(value = "/saveCharges.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm saveAeaParStageCharges(AeaParStageCharges charges){

        if(StringUtils.isNotBlank(charges.getChargeId())){
            aeaParStageChargesService.updateAeaParStageCharges(charges);
        }else{
            charges.setChargeId(UUID.randomUUID().toString());
            aeaParStageChargesService.saveAeaParStageCharges(charges);
        }
        return  new ContentResultForm<AeaParStageCharges>(true, charges);
    }

    @ApiOperation(value = "通过id删除收费项目数据", notes = "通过id删除收费项目数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/delChargesById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm delChargesById(String id) throws Exception{

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("删除阶段办事指南收费项目信息Form对象，对象id为：{}", id);
        aeaParStageChargesService.deleteAeaParStageChargesById(id);
        return new ResultForm(true);
    }

    @ApiOperation(value = "通过ids批量删除收费项目数据", notes = "通过ids批量删除收费项目数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "必填" , required = true, allowMultiple = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/batchDelChargesByIds.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm batchDelChargesByIds(String[] ids) throws Exception{

        if(ids!=null&&ids.length>0) {
            logger.debug("批量删除阶段办事指南收费项目信息Form对象，对象ids为：{}", ids);
            aeaParStageChargesService.batchDelChargesByIds(ids);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数id为空!");
        }
    }

    @ApiOperation(value = "通过stageId获取收费项目最大排序", notes = "通过stageId获取收费项目最大排序")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stageId", value = "必填" , required = true, dataType = "String" , paramType = "query"),
    })
    @RequestMapping(value = "/getMaxSortNo.do", method = {RequestMethod.POST, RequestMethod.GET})
    public Long getMaxSortNo(String stageId){

        if(StringUtils.isBlank(stageId)) {
            throw new InvalidParameterException("参数stageId为空!");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaParStageChargesService.getMaxSortNo(stageId, rootOrgId);
    }
}
