package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemFrontProj;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontProjAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 项目信息前置检测-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/item/front/proj")
public class AeaItemFrontProjAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemFrontProjAdminController.class);

    @Autowired
    private AeaItemFrontProjAdminService aeaItemFrontProjService;

    @RequestMapping("/listAeaItemFrontProjByPage.do")
    public EasyuiPageInfo<AeaItemFrontProj> listAeaItemFrontProjByPage(AeaItemFrontProj aeaItemFrontProj, Page page) throws Exception {
        PageInfo<AeaItemFrontProj> pageInfo = aeaItemFrontProjService.listAeaItemFrontProj(aeaItemFrontProj, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getAeaItemFrontProj.do")
    public ResultForm getAeaItemFrontProj(String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                return new ContentResultForm<>(true, aeaItemFrontProjService.getAeaItemFrontProjById(id));
            } else {
                return new ResultForm(false, "id不能为空");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }

    }

    @RequestMapping("/saveOrUpdateAeaItemFrontProj.do")
    public ResultForm saveOrUpdateAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) {
        try {
            if (aeaItemFrontProj.getFrontProjId() != null && !"".equals(aeaItemFrontProj.getFrontProjId())) {
                aeaItemFrontProjService.updateAeaItemFrontProj(aeaItemFrontProj);
            } else {
                if (aeaItemFrontProj.getFrontProjId() == null || "".equals(aeaItemFrontProj.getFrontProjId()))
                    aeaItemFrontProj.setFrontProjId(UUID.randomUUID().toString());
                aeaItemFrontProjService.saveAeaItemFrontProj(aeaItemFrontProj);
            }

            return new ContentResultForm<>(true, aeaItemFrontProj);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/deleteAeaItemFrontProjById.do")
    public ResultForm deleteAeaItemFrontProjById(String id) {

        try {
            if (StringUtils.isNotBlank(id)) {
                aeaItemFrontProjService.deleteAeaItemFrontProjById(id);
            }
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(AeaItemFrontProj aeaItemFrontProj) {

        try {
            return new ContentResultForm<>(true, aeaItemFrontProjService.getMaxSortNo(aeaItemFrontProj));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/changIsActive.do")
    public ResultForm changIsActive(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaItemFrontProjService.changIsActive(id, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }
}
