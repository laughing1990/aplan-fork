package com.augurit.aplanmis.admin.solicit;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import com.augurit.aplanmis.common.domain.AeaSolicitOrgUser;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgUserService;
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
* 按组织征求的人员名单表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/solicit/org/user")
public class AeaSolicitOrgUserController {

private static Logger logger = LoggerFactory.getLogger(AeaSolicitOrgUserController.class);

    @Autowired
    private AeaSolicitOrgUserService aeaSolicitOrgUserService;

    @RequestMapping("/listAeaSolicitOrgUserByPage.do")
    public EasyuiPageInfo<AeaSolicitOrgUser> listAeaSolicitOrg(AeaSolicitOrgUser aeaSolicitOrgUser, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitOrgUser);
        PageInfo<AeaSolicitOrgUser> pageInfo = aeaSolicitOrgUserService.listAeaSolicitOrgUser(aeaSolicitOrgUser, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaSolicitOrgUserRelInfoByPage.do")
    public EasyuiPageInfo<AeaSolicitOrgUser> listAeaSolicitOrgRelOrgInfo(AeaSolicitOrgUser aeaSolicitOrgUser, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitOrgUser);
        PageInfo<AeaSolicitOrgUser> pageInfo = aeaSolicitOrgUserService.listAeaSolicitOrgUserRelInfo(aeaSolicitOrgUser, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaSolicitOrgUser.do")
    public List<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitOrgUser);
        List<AeaSolicitOrgUser> list = aeaSolicitOrgUserService.listAeaSolicitOrgUser(aeaSolicitOrgUser);
        return list;
    }

    @RequestMapping("/listAeaSolicitOrgUserRelInfo.do")
    public List<AeaSolicitOrgUser> listAeaSolicitOrgUserRelInfo(AeaSolicitOrgUser aeaSolicitOrgUser) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitOrgUser);
        List<AeaSolicitOrgUser> list = aeaSolicitOrgUserService.listAeaSolicitOrgUserRelInfo(aeaSolicitOrgUser);
        return list;
    }

    @RequestMapping("/getAeaSolicitOrgUser.do")
    public AeaSolicitOrgUser getAeaSolicitOrgUser(String id) throws Exception {

        if (StringUtils.isNotBlank(id)){
            logger.debug("根据ID获取AeaSolicitOrgUser对象，ID为：{}", id);
            return aeaSolicitOrgUserService.getAeaSolicitOrgUserById(id);
        }else {
            logger.debug("构建新的AeaSolicitOrgUser对象");
            return new AeaSolicitOrgUser();
        }
    }

    @RequestMapping("/updateAeaSolicitOrgUser.do")
    public ResultForm updateAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) throws Exception {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaSolicitOrgUser);
        aeaSolicitOrgUserService.updateAeaSolicitOrgUser(aeaSolicitOrgUser);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑按组织征求的人员名单表
     *
    * @param aeaSolicitOrgUser 按组织征求的人员名单表
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaSolicitOrgUser.do")
    public ResultForm saveAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) throws Exception {

        if(StringUtils.isNotBlank(aeaSolicitOrgUser.getOrgUserId())){
            aeaSolicitOrgUserService.updateAeaSolicitOrgUser(aeaSolicitOrgUser);
        }else{
            aeaSolicitOrgUser.setOrgUserId(UUID.randomUUID().toString());
            aeaSolicitOrgUserService.saveAeaSolicitOrgUser(aeaSolicitOrgUser);
        }
        return  new ContentResultForm<AeaSolicitOrgUser>(true,aeaSolicitOrgUser);
    }

    @RequestMapping("/delSolicitOrgUserById.do")
    public ResultForm deleteAeaSolicitOrgUserById(String id) throws Exception{

        if(id!=null) {
            aeaSolicitOrgUserService.deleteAeaSolicitOrgUserById(id);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/batchDelSolicitOrgUserByIds.do")
    public ResultForm batchDelSolicitOrgUserByIds(String[] ids) throws Exception{

        if(ids!=null&&ids.length>0){
            aeaSolicitOrgUserService.batchDelSolicitOrgUserByIds(ids);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @RequestMapping("/batchSaveSolicitOrgUser.do")
    public ResultForm batchSaveSolicitOrgUser(String solicitOrgId, String[] userIds, String[] sortNos) throws Exception{

        if(StringUtils.isBlank(solicitOrgId)){
            throw new InvalidParameterException("参数solicitOrgId为空!");
        }
        if (userIds != null && userIds.length > 0) {
            aeaSolicitOrgUserService.batchSaveSolicitOrgUser(solicitOrgId, userIds, sortNos);
        } else {
            aeaSolicitOrgUserService.batchDelSolicitOrgUserBySolicitOrgId(solicitOrgId);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/changeIsActive.do")
    public ResultForm changeIsActive(String id) throws Exception{

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaSolicitOrgUserService.changeIsActive(id);
        return new ResultForm(true);
    }
}
