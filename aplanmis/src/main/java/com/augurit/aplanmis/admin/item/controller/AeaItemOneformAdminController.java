package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemOneform;
import com.augurit.aplanmis.common.service.admin.item.AeaItemOneformAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import sun.swing.StringUIClientPropertyKey;

import java.util.UUID;

/**
* 事项与总表单关联表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/item/oneform")
public class AeaItemOneformAdminController {

private static Logger logger = LoggerFactory.getLogger(AeaItemOneformAdminController.class);

    @Autowired
    private AeaItemOneformAdminService aeaItemOneformService;

    @RequestMapping("/listAeaItemOneform.do")
    public PageInfo<AeaItemOneform> listAeaItemOneform(AeaItemOneform aeaItemOneform, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemOneform);
        return aeaItemOneformService.listAeaItemOneform(aeaItemOneform,page);
    }

    @RequestMapping("/getAeaItemOneform.do")
    public AeaItemOneform getAeaItemOneform(String id) throws Exception {

        if (id != null){
            logger.debug("根据ID获取AeaItemOneform对象，ID为：{}", id);
            return aeaItemOneformService.getAeaItemOneformById(id);
        }
        else {
            logger.debug("构建新的AeaItemOneform对象");
            return new AeaItemOneform();
        }
    }

    @RequestMapping("/updateAeaItemOneform.do")
    public ResultForm updateAeaItemOneform(AeaItemOneform aeaItemOneform) throws Exception {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemOneform);
        aeaItemOneformService.updateAeaItemOneform(aeaItemOneform);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑事项与总表单关联表
     *
    * @param aeaItemOneform 事项与总表单关联表
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaItemOneform.do")
    public ResultForm saveAeaItemOneform(AeaItemOneform aeaItemOneform) throws Exception {

        if(StringUtils.isNotBlank(aeaItemOneform.getItemOneformId())){
            aeaItemOneformService.updateAeaItemOneform(aeaItemOneform);
        }else{
            aeaItemOneform.setItemOneformId(UUID.randomUUID().toString());
            aeaItemOneformService.saveAeaItemOneform(aeaItemOneform);
        }
        return  new ContentResultForm<AeaItemOneform>(true,aeaItemOneform);
    }

    @RequestMapping("/deleteById.do")
    public ResultForm deleteById(String id) throws Exception{

        if(StringUtils.isNotBlank(id)) {
            logger.debug("删除事项与总表单关联表Form对象，对象id为：{}", id);
            aeaItemOneformService.deleteAeaItemOneformById(id);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数id为空!");
        }
    }
}
