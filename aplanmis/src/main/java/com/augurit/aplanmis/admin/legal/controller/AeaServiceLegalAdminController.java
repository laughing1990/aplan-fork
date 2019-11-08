package com.augurit.aplanmis.admin.legal.controller;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaServiceLegal;
import com.augurit.aplanmis.common.service.admin.legal.AeaServiceLegalAdminService;
import io.swagger.annotations.Api;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * @author ZhangXinhui
 * @date 2019/7/25 025 13:46
 * @desc
 **/
@Api(description = "前缀：/aea/service/legal",tags={"法律法规"})
@RestController
@RequestMapping("/aea/service/legal")
public class AeaServiceLegalAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaServiceLegalAdminController.class);

    @Autowired
    private AeaServiceLegalAdminService legalAdminService;

    @Autowired
    private IBscAttService bscAttService;

    @RequestMapping("/indexAeaServiceLegal.do")
    public ModelAndView indexAeaServiceLegal() {

        return new ModelAndView("ui-jsp/aplanmis/item/legal_index");
    }

    @RequestMapping("/viewAeaServiceLegal.do")
    public ModelAndView viewAeaServiceLegal() {

        return new ModelAndView("ui-jsp/aplanmis/item/view_legal_index");
    }

    @ApiOperation(value = "更新法律法规数据", notes = "更新法律法规数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aeaServiceLegal", value = "非必填" , dataType = "AeaServiceLegal" ,paramType = "body"),
    })
    @RequestMapping("/updateAeaServiceLegal.do")
    public ResultForm updateAeaServiceLegal(AeaServiceLegal aeaServiceLegal) throws Exception {

        aeaServiceLegal.setRootOrgId(SecurityContext.getCurrentOrgId());
        legalAdminService.updateAeaServiceLegal(aeaServiceLegal);
        return new ResultForm(true);
    }

    @ApiOperation(value = "获取法律法规树", notes = "获取法律法规树")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aeaServiceLegal", value = "非必填" , dataType = "AeaServiceLegal" ,paramType = "body"),
    })
    @RequestMapping(value = "/getListLegalZtreeNode.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List<ZtreeNode> getListLegalZtreeNode(AeaServiceLegal aeaServiceLegal) throws Exception {

        aeaServiceLegal.setRootOrgId(SecurityContext.getCurrentOrgId());
        return legalAdminService.getListLegalZtreeNode(aeaServiceLegal);
    }

    /**
     *   ==================================== 前后端新写法 ====================================
     */

    @ApiOperation(value = "获取法律法规树", notes = "获取法律法规树")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aeaServiceLegal", value = "非必填" , dataType = "AeaServiceLegal" ,paramType = "body"),
        @ApiImplicitParam(name = "isClauseCheck", value = "必填" , required = true, dataType = "Boolean" ,paramType = "query"),
    })
    @RequestMapping(value="/gtreeLegalAndClauseForEui.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List<ElementUiRsTreeNode> gtreeLegalAndClauseForEui(AeaServiceLegal aeaServiceLegal, Boolean isClauseCheck) throws Exception {

        return legalAdminService.gtreeLegalAndClauseForEui(aeaServiceLegal);
    }

    @ApiOperation(value = "更新/新增法律法规", notes = "更新/新增法律法规")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "aeaServiceLegal", value = "必填" , dataType = "AeaServiceLegal" ,paramType = "body"),
    })
    @RequestMapping(value = "/saveAeaServiceLegal.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ContentResultForm<AeaServiceLegal> saveAeaServiceLegal(AeaServiceLegal aeaServiceLegal, HttpServletRequest request) throws Exception {

        if (StringUtils.isNotBlank(aeaServiceLegal.getLegalId())) {
            legalAdminService.updateAeaServiceLegalAndAtt(request, aeaServiceLegal);
        } else {
            aeaServiceLegal.setLegalId(UUID.randomUUID().toString());
            aeaServiceLegal.setRootOrgId(SecurityContext.getCurrentOrgId());
            legalAdminService.saveAeaServiceLegalAndAtt(request, aeaServiceLegal);
        }
        return new ContentResultForm<>(true, aeaServiceLegal);
    }

    @ApiOperation(value = "通过id获取法律法规数据", notes = "通过id获取法律法规数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getAeaServiceLegal.do", method = {RequestMethod.GET, RequestMethod.POST})
    public AeaServiceLegal getAeaServiceLegal(String id) throws Exception {

        if (StringUtils.isNotBlank(id)) {
            AeaServiceLegal legal = legalAdminService.getAeaServiceLegalById(id);
            if(legal!=null){
                String orgId = SecurityContext.getCurrentOrgId();
                List<BscAttForm> attList = bscAttService.listAttLinkAndDetailNoPage("AEA_SERVICE_LEGAL", "SERVICE_LEGAL_ATT", id, null, orgId, null);
                legal.setLegalAtts(attList);
                legal.setServiceLegalAttCount(attList == null ? 0L : attList.size());
            }
            return legal;
        } else {
            logger.debug("构建新的AeaServiceLegal对象");
            return new AeaServiceLegal();
        }
    }

    @ApiOperation(value = "通过id删除法律法规数据", notes = "通过id删除法律法规数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/deleteAeaServiceLegalById.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm deleteAeaServiceLegalById(String id) throws Exception {

        if (StringUtils.isNotBlank(id)) {
            legalAdminService.deleteAeaServiceLegalById(id);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数id为空!");
        }
    }

    @ApiOperation(value = "删除法律法规附件", notes = "删除法律法规附件")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "type", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
        @ApiImplicitParam(name = "bizId", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
        @ApiImplicitParam(name = "detailId", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/deleteAtt.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm deleteAtt(String type, String bizId, String detailId) throws Exception {

        if (StringUtils.isBlank(type) || StringUtils.isBlank(detailId) || StringUtils.isBlank(bizId)) {
            return new ResultForm(false);
        }
        legalAdminService.delelteFile(type, bizId, detailId);
        return new ResultForm(true);
    }

    @ApiOperation(value = "获取某组织下的所有法律法规（不包括自己以及包含的自己子法律法规）",notes = "获取某组织下的所有法律法规（不包括自己以及包含的自己子法律法规）")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "legalId", value = "当前类型id", required = true, dataType = "String", paramType = "String"),
    })
    @RequestMapping(value ="/other/tree.do", method = {RequestMethod.GET, RequestMethod.POST})
    public List<ElementUiRsTreeNode> listOtherLegalByLegalId(String legalId){

        if(StringUtils.isBlank(legalId)){
            throw new InvalidParameterException("当前操作对象legalId为空！");
        }
        return legalAdminService.listOtherLegalByLegalId(legalId);
    }

    @ApiOperation(value = "设置父级类型", notes = "设置父级类型")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "curLegalId", value = "当前法律法规id", required = true , dataType = "String" ,paramType = "query"),
        @ApiImplicitParam(name = "targetLegalId", value = "新父级法律法规id", required = true , dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/set/parent", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm setParentLegal(String curLegalId, String targetLegalId){

        if(StringUtils.isBlank(curLegalId)){
            throw new InvalidParameterException("传递参数curLegalId为空！");
        }
        if(StringUtils.isBlank(targetLegalId)){
            throw new InvalidParameterException("传递参数targetLegalId为空！");
        }
        AeaServiceLegal newForm = legalAdminService.setParentLegal(curLegalId, targetLegalId);
        return new ContentResultForm<AeaServiceLegal>(true, newForm);
    }
}
