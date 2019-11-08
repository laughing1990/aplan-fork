package com.augurit.aplanmis.admin.credit.controller;


import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaCreditUnitInfo;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditUnitInfoService;

import com.github.pagehelper.PageInfo;
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


import java.util.List;
import java.util.UUID;

/**
 * 单位表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/credit/unit/info")
public class AeaCreditUnitInfoController {

    private static Logger logger = LoggerFactory.getLogger(AeaCreditUnitInfoController.class);

    @Autowired
    private AeaCreditUnitInfoService aeaCreditUnitInfoService;


    @RequestMapping(value = "/listAeaCreditUnitInfo.do", method = {RequestMethod.POST, RequestMethod.GET})
    public PageInfo<AeaCreditUnitInfo> listAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo,
                                                             @ModelAttribute PageParam page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCreditUnitInfo);
        return aeaCreditUnitInfoService.listAeaCreditUnitInfoWithGlobalUnit(aeaCreditUnitInfo, page.convertPage());
    }

    @RequestMapping(value = "/getAeaCreditUnitInfo.do", method = {RequestMethod.POST, RequestMethod.GET})
    public AeaCreditUnitInfo getAeaCreditUnitInfo(String id) throws Exception {
        if (id != null) {
            logger.debug("根据ID获取AeaCreditUnitInfo对象，ID为：{}", id);
            return aeaCreditUnitInfoService.getAeaCreditUnitInfoById(id);
        } else {
            logger.debug("构建新的AeaCreditUnitInfo对象");
            return new AeaCreditUnitInfo();
        }
    }

    @RequestMapping(value = "/updateAeaCreditUnitInfo.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm updateAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaCreditUnitInfo);
        aeaCreditUnitInfoService.updateAeaCreditUnitInfo(aeaCreditUnitInfo);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑单位表
     *
     * @param aeaCreditUnitInfo 单位表
     * @return 返回结果对象 包含结果信息
     * @throws Exception
     */
    @RequestMapping(value = "/saveAeaCreditUnitInfo.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm saveAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo) throws Exception {

        if (StringUtils.isNotBlank(aeaCreditUnitInfo.getCreditUnitInfoId())) {
            aeaCreditUnitInfoService.updateAeaCreditUnitInfo(aeaCreditUnitInfo);
        } else {
            aeaCreditUnitInfo.setCreditUnitInfoId(UUID.randomUUID().toString());
            aeaCreditUnitInfoService.saveAeaCreditUnitInfo(aeaCreditUnitInfo);
        }

        return new ContentResultForm<AeaCreditUnitInfo>(true, aeaCreditUnitInfo);
    }

    @RequestMapping(value = "/deleteAeaCreditUnitInfoById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm deleteAeaCreditUnitInfoById(String id) throws Exception {
        logger.debug("删除单位表Form对象，对象id为：{}", id);
        if (id != null) {
            aeaCreditUnitInfoService.deleteAeaCreditUnitInfoById(id);
        }
        return new ResultForm(true);
    }

    @RequestMapping(value = "/batchDelAeaCreditUnitInfoByIds.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm batchDelAeaCreditUnitInfoByIds(String[] ids) {

        if (ids != null) {
            aeaCreditUnitInfoService.batchDelSelfAndAllChildByIds(ids);
            return new ResultForm(true);
        }
        return new ResultForm(false);
    }

    @ApiOperation(value = "获取信用单位信息树结构", notes = "获取信用单位信息树结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", dataType = "String", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/gtreeUnitInfoForEui.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultForm gtreeUnitInfoForEui(String keyword) {
        List<ElementUiRsTreeNode> elementUiRsTreeNodes = aeaCreditUnitInfoService.gtreeUnitInfoForEui(keyword, SecurityContext.getCurrentOrgId());
        return new ContentResultForm<>(true, elementUiRsTreeNodes, "查询成功！");
    }

    @RequestMapping(value = "/batchDelAeaCreditUnitInfoById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm batchDelAeaCreditUnitInfoById(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaCreditUnitInfoService.batchDelSelfAndAllChildById(id);
            return new ResultForm(true);
        }
        return new ResultForm(false);
    }
}
