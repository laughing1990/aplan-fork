package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaItemFrontProj;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontProjAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("/indexAeaItemFrontProj.do")
    public ModelAndView indexAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj, String infoType){
        return new ModelAndView("aea/item/front/front_proj_index");
    }

    @RequestMapping("/listAeaItemFrontProj.do")
    public PageInfo<AeaItemFrontProj> listAeaItemFrontProj(  AeaItemFrontProj aeaItemFrontProj, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemFrontProj);
        return aeaItemFrontProjService.listAeaItemFrontProj(aeaItemFrontProj,page);
    }

    @RequestMapping("/getAeaItemFrontProj.do")
    public AeaItemFrontProj getAeaItemFrontProj(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaItemFrontProj对象，ID为：{}", id);
            return aeaItemFrontProjService.getAeaItemFrontProjById(id);
        }
        else {
            logger.debug("构建新的AeaItemFrontProj对象");
            return new AeaItemFrontProj();
        }
    }

    @RequestMapping("/updateAeaItemFrontProj.do")
        public ResultForm updateAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemFrontProj);
        aeaItemFrontProjService.updateAeaItemFrontProj(aeaItemFrontProj);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑项目信息前置检测
    * @param aeaItemFrontProj 项目信息前置检测
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaItemFrontProj.do")
    public ResultForm saveAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存项目信息前置检测Form对象出错");
            throw new InvalidParameterException(aeaItemFrontProj);
        }

        if(aeaItemFrontProj.getFrontProjId()!=null&&!"".equals(aeaItemFrontProj.getFrontProjId())){
            aeaItemFrontProjService.updateAeaItemFrontProj(aeaItemFrontProj);
        }else{
        if(aeaItemFrontProj.getFrontProjId()==null||"".equals(aeaItemFrontProj.getFrontProjId()))
            aeaItemFrontProj.setFrontProjId(UUID.randomUUID().toString());
            aeaItemFrontProjService.saveAeaItemFrontProj(aeaItemFrontProj);
        }

        return  new ContentResultForm<AeaItemFrontProj>(true,aeaItemFrontProj);
    }

    @RequestMapping("/deleteAeaItemFrontProjById.do")
    public ResultForm deleteAeaItemFrontProjById(String id) throws Exception{

        if(id!=null) {
            logger.debug("删除项目信息前置检测Form对象，对象id为：{}", id);
            aeaItemFrontProjService.deleteAeaItemFrontProjById(id);
        }
        return new ResultForm(true);
    }

}
