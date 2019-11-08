package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaItemFrontPartform;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontPartformAdminService;
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


    @RequestMapping("/indexAeaItemFrontPartform.do")
    public ModelAndView indexAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform, String infoType){
        return new ModelAndView("aea/item/front/front_partform_index");
    }

    @RequestMapping("/listAeaItemFrontPartform.do")
    public PageInfo<AeaItemFrontPartform> listAeaItemFrontPartform(  AeaItemFrontPartform aeaItemFrontPartform, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemFrontPartform);
        return aeaItemFrontPartformService.listAeaItemFrontPartform(aeaItemFrontPartform,page);
    }

    @RequestMapping("/getAeaItemFrontPartform.do")
    public AeaItemFrontPartform getAeaItemFrontPartform(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaItemFrontPartform对象，ID为：{}", id);
            return aeaItemFrontPartformService.getAeaItemFrontPartformById(id);
        }
        else {
            logger.debug("构建新的AeaItemFrontPartform对象");
            return new AeaItemFrontPartform();
        }
    }

    @RequestMapping("/updateAeaItemFrontPartform.do")
        public ResultForm updateAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemFrontPartform);
        aeaItemFrontPartformService.updateAeaItemFrontPartform(aeaItemFrontPartform);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑事项的前置检查事项
    * @param aeaItemFrontPartform 事项的前置检查事项
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaItemFrontPartform.do")
    public ResultForm saveAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存事项的前置检查事项Form对象出错");
            throw new InvalidParameterException(aeaItemFrontPartform);
        }

        if(aeaItemFrontPartform.getFrontPartformId()!=null&&!"".equals(aeaItemFrontPartform.getFrontPartformId())){
            aeaItemFrontPartformService.updateAeaItemFrontPartform(aeaItemFrontPartform);
        }else{
        if(aeaItemFrontPartform.getFrontPartformId()==null||"".equals(aeaItemFrontPartform.getFrontPartformId()))
            aeaItemFrontPartform.setFrontPartformId(UUID.randomUUID().toString());
            aeaItemFrontPartformService.saveAeaItemFrontPartform(aeaItemFrontPartform);
        }

        return  new ContentResultForm<AeaItemFrontPartform>(true,aeaItemFrontPartform);
    }

    @RequestMapping("/deleteAeaItemFrontPartformById.do")
    public ResultForm deleteAeaItemFrontPartformById(String id) throws Exception{
        logger.debug("删除事项的前置检查事项Form对象，对象id为：{}", id);
        if(id!=null)
            aeaItemFrontPartformService.deleteAeaItemFrontPartformById(id);
        return new ResultForm(true);
    }

}
