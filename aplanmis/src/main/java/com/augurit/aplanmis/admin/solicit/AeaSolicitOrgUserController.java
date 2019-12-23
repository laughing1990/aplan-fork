package com.augurit.aplanmis.admin.solicit;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
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

    @RequestMapping("/indexAeaSolicitOrgUser.do")
    public ModelAndView indexAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser){

        return new ModelAndView("aea/solicit/org/org_user_index");
    }

    @RequestMapping("/listAeaSolicitOrgUser.do")
    public PageInfo<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitOrgUser);
        return aeaSolicitOrgUserService.listAeaSolicitOrgUser(aeaSolicitOrgUser,page);
    }

    @RequestMapping("/getAeaSolicitOrgUser.do")
    public AeaSolicitOrgUser getAeaSolicitOrgUser(String id) throws Exception {

        if (id != null){
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

    @RequestMapping("/deleteAeaSolicitOrgUserById.do")
    public ResultForm deleteAeaSolicitOrgUserById(String id) throws Exception{

        if(id!=null) {
            aeaSolicitOrgUserService.deleteAeaSolicitOrgUserById(id);
        }
        return new ResultForm(true);
    }
}
