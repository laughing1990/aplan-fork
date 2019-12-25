package com.augurit.aplanmis.admin.solicit;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitItemUser;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitItemUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
* 按事项征求的人员名单表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/solicit/item/user")
public class AeaSolicitItemUserController {

private static Logger logger = LoggerFactory.getLogger(AeaSolicitItemUserController.class);

    @Autowired
    private AeaSolicitItemUserService aeaSolicitItemUserService;

    @RequestMapping("/listAeaSolicitItemUserByPage.do")
    public EasyuiPageInfo<AeaSolicitItemUser> listAeaSolicitOrg(AeaSolicitItemUser aeaSolicitItemUser, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitItemUser);
        PageInfo<AeaSolicitItemUser> pageInfo = aeaSolicitItemUserService.listAeaSolicitItemUser(aeaSolicitItemUser, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaSolicitItemUserRelInfoByPage.do")
    public EasyuiPageInfo<AeaSolicitItemUser> listAeaSolicitOrgRelOrgInfo(AeaSolicitItemUser aeaSolicitItemUser, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitItemUser);
        PageInfo<AeaSolicitItemUser> pageInfo = aeaSolicitItemUserService.listAeaSolicitItemUserRelInfo(aeaSolicitItemUser, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaSolicitItemUser.do")
    public List<AeaSolicitItemUser> listAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitItemUser);
        List<AeaSolicitItemUser> list = aeaSolicitItemUserService.listAeaSolicitItemUser(aeaSolicitItemUser);
        return list;
    }

    @RequestMapping("/listAeaSolicitItemUserRelInfo.do")
    public List<AeaSolicitItemUser> listAeaSolicitItemUserRelInfo(AeaSolicitItemUser aeaSolicitItemUser) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitItemUser);
        List<AeaSolicitItemUser> list = aeaSolicitItemUserService.listAeaSolicitItemUserRelInfo(aeaSolicitItemUser);
        return list;
    }

    @RequestMapping("/getAeaSolicitItemUser.do")
    public AeaSolicitItemUser getAeaSolicitItemUser(String id) throws Exception {

        if (StringUtils.isNotBlank(id)){
            logger.debug("根据ID获取AeaSolicitItemUser对象，ID为：{}", id);
            return aeaSolicitItemUserService.getAeaSolicitItemUserById(id);
        }
        else {
            logger.debug("构建新的AeaSolicitItemUser对象");
            return new AeaSolicitItemUser();
        }
    }

    @RequestMapping("/updateAeaSolicitItemUser.do")
    public ResultForm updateAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) throws Exception {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaSolicitItemUser);
        aeaSolicitItemUserService.updateAeaSolicitItemUser(aeaSolicitItemUser);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑按事项征求的人员名单表
     *
    * @param aeaSolicitItemUser 按事项征求的人员名单表
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaSolicitItemUser.do")
    public ResultForm saveAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) throws Exception {

        if(StringUtils.isNotBlank(aeaSolicitItemUser.getItemUserId())){
            aeaSolicitItemUserService.updateAeaSolicitItemUser(aeaSolicitItemUser);
        }else{
            aeaSolicitItemUser.setItemUserId(UUID.randomUUID().toString());
            aeaSolicitItemUserService.saveAeaSolicitItemUser(aeaSolicitItemUser);
        }
        return  new ContentResultForm<AeaSolicitItemUser>(true,aeaSolicitItemUser);
    }

    @RequestMapping("/delSolicitItemUserById.do")
    public ResultForm deleteAeaSolicitItemUserById(String id) throws Exception{

        if(id!=null) {
            aeaSolicitItemUserService.deleteAeaSolicitItemUserById(id);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/batchDelSolicitItemUserByIds.do")
    public ResultForm batchDelSolicitItemUserByIds(String[] ids) throws Exception{

        if(ids!=null&&ids.length>0){
            aeaSolicitItemUserService.batchDelSolicitItemUserByIds(ids);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @RequestMapping("/batchSaveSolicitItemUser.do")
    public ResultForm batchSaveSolicitItemUser(String solicitItemId, String[] userIds, String[] sortNos) throws Exception{

        if(StringUtils.isBlank(solicitItemId)){
            throw new InvalidParameterException("参数solicitItemId为空!");
        }
        if (userIds != null && userIds.length > 0) {
            aeaSolicitItemUserService.batchSaveSolicitItemUser(solicitItemId, userIds, sortNos);
        } else {
            aeaSolicitItemUserService.batchDelSolicitItemUserBySolicitItemId(solicitItemId);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/changeIsActive.do")
    public ResultForm changeIsActive(String id) throws Exception{

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaSolicitItemUserService.changeIsActive(id);
        return new ResultForm(true);
    }
}
