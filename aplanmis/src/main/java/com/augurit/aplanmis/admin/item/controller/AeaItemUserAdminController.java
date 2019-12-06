package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.common.domain.AeaItemUser;
import com.augurit.aplanmis.common.service.admin.item.AeaItemUserAdminService;
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
* 用户事项管理-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/item/user")
public class AeaItemUserAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemUserAdminController.class);

    @Autowired
    private AeaItemUserAdminService aeaItemUserService;

    @RequestMapping("/gtreeAllUserRelOrgByOrgId.do")
    public List<OpuOmUser> gtreeAllUserRelOrgByOrgId() throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        logger.debug("获取组织用户数据，查询关键字为{}", rootOrgId);
        return aeaItemUserService.listAllUserRelOrgByOrgId(rootOrgId);
    }

    @RequestMapping("/listUserItemByPage.do")
    public EasyuiPageInfo<AeaItemUser> listUserItemByPage(AeaItemUser aeaItemUser, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemUser);
        PageInfo<AeaItemUser> pageInfo = aeaItemUserService.listUserItemRelItemInfo(aeaItemUser,page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listUserItemByNoPage.do")
    public List<AeaItemUser> listUserItemByNoPage(AeaItemUser aeaItemUser) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemUser);
        return aeaItemUserService.listUserItemRelItemInfo(aeaItemUser);
    }

    @RequestMapping("/getAeaItemUser.do")
    public AeaItemUser getAeaItemUser(String id) throws Exception {

        if (StringUtils.isNotBlank(id)){

            logger.debug("根据ID获取AeaItemUser对象，ID为：{}", id);
            return aeaItemUserService.getAeaItemUserById(id);
        }else {
            logger.debug("构建新的AeaItemUser对象");
            return new AeaItemUser();
        }
    }

    @RequestMapping("/updateAeaItemUser.do")
    public ResultForm updateAeaItemUser(AeaItemUser aeaItemUser) throws Exception {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemUser);
        aeaItemUserService.updateAeaItemUser(aeaItemUser);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑用户事项管理
     *
    * @param aeaItemUser 用户事项管理
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaItemUser.do")
    public ResultForm saveAeaItemUser(AeaItemUser aeaItemUser) throws Exception {

        if(StringUtils.isNotBlank(aeaItemUser.getItemUserId())){
            aeaItemUserService.updateAeaItemUser(aeaItemUser);
        }else{
            aeaItemUser.setItemUserId(UUID.randomUUID().toString());
            aeaItemUserService.saveAeaItemUser(aeaItemUser);
        }
        return  new ContentResultForm<AeaItemUser>(true,aeaItemUser);
    }

    @RequestMapping("/delItemUserById.do")
    public ResultForm delItemUserById(String id) throws Exception{

        if(StringUtils.isNotBlank(id)) {
            logger.debug("删除用户事项管理Form对象，对象id为：{}", id);
            aeaItemUserService.deleteAeaItemUserById(id);
            return new ResultForm(true);
        }else{
            return new ResultForm(false, "参数id为空!");
        }
    }

    @RequestMapping("/batchDelItemUserByIds.do")
    public ResultForm batchDelItemUserByIds(String[] ids) throws Exception{

        if(ids!=null&&ids.length>0){

            logger.debug("批量删除用户事项，对象ids为：{}", ids);
            aeaItemUserService.batchDelItemUserByIds(ids);
            return new ResultForm(true);
        }else{
            return new ResultForm(false, "参数ids为空!");
        }
    }

    @RequestMapping("/batchSaveUserItem.do")
    public ResultForm batchSaveUserItem(String userId, String[] itemIds, String[] sortNos) {

        if (itemIds != null && itemIds.length > 0) {
            if (StringUtils.isBlank(userId)) {
                return new ResultForm(false, "参数userId为空!");
            }
            aeaItemUserService.batchSaveUserItem(userId, itemIds, sortNos);
        } else {
            aeaItemUserService.batchDelItemByUserId(userId, SecurityContext.getCurrentOrgId());
        }
        return new ResultForm(true);
    }
}
