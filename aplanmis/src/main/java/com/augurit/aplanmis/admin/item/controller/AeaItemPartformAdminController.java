package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaItemPartform;
import com.augurit.aplanmis.common.service.admin.item.AeaItemPartformAdminService;
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
* 事项与扩展表单关联表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/item/partform")
public class AeaItemPartformAdminController {

private static Logger logger = LoggerFactory.getLogger(AeaItemPartformAdminController.class);

    @Autowired
    private AeaItemPartformAdminService aeaItemPartformService;


    @RequestMapping("/indexAeaItemPartform.do")
    public ModelAndView indexAeaItemPartform(AeaItemPartform aeaItemPartform, String infoType){
        return new ModelAndView("aea/item/partform_index");
    }

    @RequestMapping("/listAeaItemPartform.do")
    public PageInfo<AeaItemPartform> listAeaItemPartform(AeaItemPartform aeaItemPartform, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemPartform);
        return aeaItemPartformService.listAeaItemPartform(aeaItemPartform,page);
    }

    @RequestMapping("/getAeaItemPartform.do")
    public AeaItemPartform getAeaItemPartform(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaItemPartform对象，ID为：{}", id);
            return aeaItemPartformService.getAeaItemPartformById(id);
        }
        else {
            logger.debug("构建新的AeaItemPartform对象");
            return new AeaItemPartform();
        }
    }

    @RequestMapping("/updateAeaItemPartform.do")
        public ResultForm updateAeaItemPartform(AeaItemPartform aeaItemPartform) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemPartform);
        aeaItemPartformService.updateAeaItemPartform(aeaItemPartform);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑事项与扩展表单关联表
    * @param aeaItemPartform 事项与扩展表单关联表
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaItemPartform.do")
    public ResultForm saveAeaItemPartform(AeaItemPartform aeaItemPartform, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存事项与扩展表单关联表Form对象出错");
            throw new InvalidParameterException(aeaItemPartform);
        }

        if(aeaItemPartform.getItemPartformId()!=null&&!"".equals(aeaItemPartform.getItemPartformId())){
            aeaItemPartformService.updateAeaItemPartform(aeaItemPartform);
        }else{
        if(aeaItemPartform.getItemPartformId()==null||"".equals(aeaItemPartform.getItemPartformId()))
            aeaItemPartform.setItemPartformId(UUID.randomUUID().toString());
            aeaItemPartformService.saveAeaItemPartform(aeaItemPartform);
        }

        return  new ContentResultForm<AeaItemPartform>(true,aeaItemPartform);
    }

    @RequestMapping("/deleteAeaItemPartformById.do")
    public ResultForm deleteAeaItemPartformById(String id) throws Exception{
        logger.debug("删除事项与扩展表单关联表Form对象，对象id为：{}", id);
        if(id!=null)
            aeaItemPartformService.deleteAeaItemPartformById(id);
        return new ResultForm(true);
    }

}
