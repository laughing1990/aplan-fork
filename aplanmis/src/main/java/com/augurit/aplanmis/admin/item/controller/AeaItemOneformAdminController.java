package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemOneform;
import com.augurit.aplanmis.common.domain.AeaOneform;
import com.augurit.aplanmis.common.service.admin.item.AeaItemOneformAdminService;
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
* 事项与总表单关联表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/item/oneform")
public class AeaItemOneformAdminController {

private static Logger logger = LoggerFactory.getLogger(AeaItemOneformAdminController.class);

    @Autowired
    private AeaItemOneformAdminService aeaItemOneformService;

    @RequestMapping("/listOneform.do")
    public EasyuiPageInfo<AeaOneform> listAeaOneFormNotInItem(AeaOneform aeaOneform, Page page) {

        logger.debug("分页查询，过滤条件为{}，对象为{}", aeaOneform);
        aeaOneform.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageInfo<AeaOneform> res = aeaItemOneformService.listAeaOneFormNotInItem(aeaOneform, page);
        return PageHelper.toEasyuiPageInfo(res);
    }

    @RequestMapping("/addMultiplyItemOneform.do")
    public ResultForm addMultiplyItemOneform(String itemVerId,String oneformIds){

        aeaItemOneformService.addMultiplyItemOneform(itemVerId,oneformIds.split(","));
        return new ContentResultForm<>(true);
    }

    @RequestMapping("/listAeaItemOneformByItemVerId.do")
    public EasyuiPageInfo<AeaItemOneform> listAeaItemOneform(String itemVerId, Page page) {

        PageInfo<AeaItemOneform> res = aeaItemOneformService.listAeaItemOneFormByItemVerId(itemVerId, page);
        return PageHelper.toEasyuiPageInfo(res);
    }

    @RequestMapping("/listItemOneformNoPageByItemVerId.do")
    public List<AeaItemOneform> listItemOneformNoPageByItemVerId(String itemVerId) {

        List<AeaItemOneform> list = aeaItemOneformService.listAeaItemOneFormByItemVerId(itemVerId);
        return list;
    }

    @RequestMapping("/listActiveItemOneformNoPage.do")
    public List<AeaItemOneform> listItemOneformNoPageByItemVerId(AeaItemOneform aeaItemOneform) {

        aeaItemOneform.setIsActive(Status.ON);
        List<AeaItemOneform> list = aeaItemOneformService.listAeaItemOneform(aeaItemOneform);
        return list;
    }

    @RequestMapping("/changIsActiveState.do")
    public ResultForm changIsActiveState(String id){

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaItemOneformService.changIsActiveState(id);
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
}
