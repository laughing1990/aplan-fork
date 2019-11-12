package com.augurit.aplanmis.admin.item.controller;


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

import java.util.UUID;

/**
* 事项的前置检查事项-Controller 页面控制转发类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-11-01 10:49:22</li>
</ul>
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
            if (aeaItemFrontPartform.getFrontPartformId() != null && !"".equals(aeaItemFrontPartform.getFrontPartformId())) {
                aeaItemFrontPartformService.updateAeaItemFrontPartform(aeaItemFrontPartform);
            } else {
                if (aeaItemFrontPartform.getFrontPartformId() == null || "".equals(aeaItemFrontPartform.getFrontPartformId()))
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
            if (StringUtils.isNotBlank(id))
                aeaItemFrontPartformService.deleteAeaItemFrontPartformById(id);
            return new ResultForm(true);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(AeaItemFrontPartform aeaItemFrontPartform) {
        try {
            return new ContentResultForm<>(true, aeaItemFrontPartformService.getMaxSortNo(aeaItemFrontPartform));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/listSelectItemFrontPartformByPage.do")
    public EasyuiPageInfo<AeaItemFrontPartformVo> listSelectItemFrontPartformByPage(AeaItemFrontPartform aeaItemFrontPartform, Page page) {
        PageInfo<AeaItemFrontPartformVo> pageInfo = aeaItemFrontPartformService.listSelectItemFrontPartformByPage(aeaItemFrontPartform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

}
