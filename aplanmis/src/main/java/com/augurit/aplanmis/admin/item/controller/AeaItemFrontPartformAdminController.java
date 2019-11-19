package com.augurit.aplanmis.admin.item.controller;


import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemFrontPartform;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontPartformAdminService;
import com.augurit.aplanmis.common.vo.AeaItemFrontPartformVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
* 事项的前置检查事项-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/item/front/partform")
public class AeaItemFrontPartformAdminController {

private static Logger logger = LoggerFactory.getLogger(AeaItemFrontPartformAdminController.class);

    @Autowired
    private AeaItemFrontPartformAdminService aeaItemFrontPartformService;

    @RequestMapping("/listAeaItemFrontPartformByPage.do")
    public EasyuiPageInfo<AeaItemFrontPartformVo> listAeaItemFrontPartformByPage(AeaItemFrontPartform aeaItemFrontPartform, Page page){

        PageInfo<AeaItemFrontPartformVo> pageInfo =  aeaItemFrontPartformService.listAeaItemFrontPartformVoByPage(aeaItemFrontPartform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaItemFrontPartformByNoPage.do")
    public List<AeaItemFrontPartformVo> listAeaItemFrontPartformByNoPage(AeaItemFrontPartform aeaItemFrontPartform){

        return aeaItemFrontPartformService.listAeaItemFrontPartformVo(aeaItemFrontPartform);
    }

    @RequestMapping("/getAeaItemFrontPartform.do")
    public ResultForm getAeaItemFrontPartform(String frontPartformId){

        try {
            if (StringUtils.isNotBlank(frontPartformId)) {
                return new ContentResultForm<>(true, aeaItemFrontPartformService.getAeaItemFrontPartformVoById(frontPartformId));
            } else {
                return new ResultForm(false, "frontPartformId不能为空");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/saveOrUpdateAeaItemFrontPartform.do")
    public ResultForm saveOrUpdateAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform){

        try {
            if (StringUtils.isNotBlank(aeaItemFrontPartform.getFrontPartformId())) {
                aeaItemFrontPartformService.updateAeaItemFrontPartform(aeaItemFrontPartform);
            } else {
                aeaItemFrontPartform.setFrontPartformId(UUID.randomUUID().toString());
                aeaItemFrontPartformService.saveAeaItemFrontPartform(aeaItemFrontPartform);
            }
            return new ContentResultForm<>(true, aeaItemFrontPartform);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/deleteAeaItemFrontPartformById.do")
    public ResultForm deleteAeaItemFrontPartformById(String id)  {

        try {
            logger.debug("删除事项的扩展表单前置检测表Form对象，对象id为：{}", id);
            if (StringUtils.isNotBlank(id)) {
                aeaItemFrontPartformService.deleteAeaItemFrontPartformById(id);
            }
            return new ResultForm(true);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/batchDelItemFrontPartformByIds.do")
    public ResultForm batchDelItemFrontPartformByIds(String[] ids)  {

        try {
            if (ids!=null&&ids.length>0) {
                aeaItemFrontPartformService.deleteAeaItemFrontPartformByIds(ids);
            }
            return new ResultForm(true);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(String itemVerId) {

        try {
            return new ContentResultForm<>(true, aeaItemFrontPartformService.getMaxSortNo(itemVerId, SecurityContext.getCurrentOrgId()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/listItemNoSelectPartformByPage.do")
    public EasyuiPageInfo<AeaItemFrontPartformVo> listItemNoSelectPartformByPage(AeaItemFrontPartform frontPartform, Page page) {

        PageInfo<AeaItemFrontPartformVo> pageInfo = aeaItemFrontPartformService.listItemNoSelectPartform(frontPartform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/batchSaveAeaItemFrontPartform.do")
    public ResultForm batchSaveAeaItemFrontPartform(String itemVerId, String[] itemPartformIds){

        if(StringUtils.isBlank(itemVerId)){
            throw new InvalidParameterException("参数itemVerId为空!");
        }
        if(itemPartformIds==null||(itemPartformIds!=null&&itemPartformIds.length==0)){
            throw new InvalidParameterException("参数itemPartformIds为空!");
        }
        try {
            aeaItemFrontPartformService.batchSaveAeaItemFrontPartform(itemVerId, itemPartformIds);
            return new ResultForm(true);
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
        aeaItemFrontPartformService.changIsActive(id, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }
}
