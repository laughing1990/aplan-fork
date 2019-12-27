package com.augurit.aplanmis.admin.legal.controller;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaServiceLegalClause;
import com.augurit.aplanmis.common.service.admin.item.AeaItemServiceBasicAdminService;
import com.augurit.aplanmis.common.service.admin.legal.AeaServiceLegalClauseAdminService;
import com.github.pagehelper.Page;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author jjt
 *
 * @date 2019/7/25 025 15:09
 * @desc 法律法规条款
 *
 **/
@Api(description = "前缀：/aea/service/legal/clause",tags={"法律法规条款"})
@RestController
@RequestMapping("/aea/service/legal/clause")
public class AeaServiceLegalClauseAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaServiceLegalClauseAdminController.class);

    @Autowired
    private IBscAttService bscAttService;

    @Autowired
    private AeaServiceLegalClauseAdminService legalClauseAdminService;

    @Autowired
    AeaItemServiceBasicAdminService aeaItemServiceBasicAdminService;

    @ApiOperation(value = "通过id获取法律法规条款数据", notes = "通过id获取法律法规条款数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/getAeaServiceLegalClause.do", method = {RequestMethod.GET, RequestMethod.POST})
    public AeaServiceLegalClause getAeaServiceLegalClause(String id) throws Exception {

        if (StringUtils.isNotBlank(id)) {
            AeaServiceLegalClause legalClause = legalClauseAdminService.getAeaServiceLegalClauseById(id);
            if(legalClause!=null){
                String orgId = SecurityContext.getCurrentOrgId();
                List<BscAttForm> attList = bscAttService.listAttLinkAndDetailNoPage("AEA_SERVICE_LEGAL_CLAUSE", "CLAUSE_ATT", id, null, orgId, null);
                legalClause.setLegalClauseAtts(attList);
                legalClause.setClauseAttCount(attList == null ? 0L : attList.size());
            }
            return legalClause;
        } else {
            logger.debug("构建新的AeaServiceLegalClause对象");
            return new AeaServiceLegalClause();
        }
    }

    /**
     * 保存或编辑法律法规条款
     *
     * @param legalClause 法律法规条款
     * @return 返回结果对象 包含结果信息
     */
    @ApiOperation(value = "保存/更新法律法规条款", notes = "保存/更新法律法规条款")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "legalClause", value = "必填" , dataType = "AeaServiceLegalClause" ,paramType = "body"),
    })
    @RequestMapping(value = "/saveAeaServiceLegalClause.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ContentResultForm<AeaServiceLegalClause> saveAeaServiceLegalClause(AeaServiceLegalClause legalClause, HttpServletRequest request) throws Exception {

        if (StringUtils.isNotBlank(legalClause.getLegalClauseId())) {
            legalClauseAdminService.updateAeaServiceLegalClauseAndAtt(request, legalClause);
        } else {
            legalClause.setLegalClauseId(UUID.randomUUID().toString());
            legalClauseAdminService.saveAeaServiceLegalClauseAndAtt(request, legalClause);
        }
        return new ContentResultForm<>(true, legalClause);
    }

    @ApiOperation(value = "通过id删除法律法规条款", notes = "通过id删除法律法规条款")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "必填" , required = true, dataType = "String" ,paramType = "query"),
    })
    @RequestMapping(value = "/deleteAeaServiceLegalClauseById.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm deleteAeaServiceLegalClauseById(String id) throws Exception {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除法律法规条款Form对象，对象id为：{}", id);
            legalClauseAdminService.deleteAeaServiceLegalClauseById(id);
        }
        return new ResultForm(true);
    }

    @ApiOperation(value = "获取条款最大排序号", notes = "获取条款最大排序号")
    @RequestMapping(value = "/getMaxSortNo.do", method = {RequestMethod.GET, RequestMethod.POST})
    public Long getMaxSortNo() throws Exception{

        return legalClauseAdminService.getMaxSortNo(SecurityContext.getCurrentOrgId());
    }

    /**
     * 根据事项ID查询中介服务事项关联的设立依据
     * @param itemVerId
     * @return
     * @throws Exception
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemVerId", value = "事项版本id" , required = true, dataType = "String"),
    })
    @RequestMapping("/listItemServiceLegalNoPage.do")
    public List<AeaServiceLegalClause> listItemServiceLegalNoPage(String itemVerId) throws Exception {

        List<AeaServiceLegalClause> list = legalClauseAdminService.listItemServiceLegalNoPage(itemVerId);
        return list;
    }

    /**
     * 根据事项ID查询中介服务事项关联的设立依据
     * @param itemVerId
     * @param keyword
     * @return
     * @throws Exception
     */
    @RequestMapping("/listAeaItemServiceLegalClause.do")
    public EasyuiPageInfo<AeaServiceLegalClause> listAeaItemServiceLegalClause(String itemVerId,String keyword, Page page) throws Exception {

        EasyuiPageInfo<AeaServiceLegalClause> list = legalClauseAdminService.listAeaItemServiceLegalClause(itemVerId,keyword,page);
        return list;
    }


    @RequestMapping("/batchDeleteLegalClause.do")
    public ResultForm batchDeleteLegalClause(String[] ids) {

        try {
            logger.debug("删除设立依据，对象id为：{}", (Object) ids);
            if (ids != null && ids.length > 0) {
                aeaItemServiceBasicAdminService.batchDeleteServiceBasic(ids);
            }
            return new ResultForm(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultForm(false,"删除失败");
        }
    }

}
