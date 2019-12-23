package com.augurit.aplanmis.admin.solicit;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgService;
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
* 按组织征求配置表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/solicit/org")
public class AeaSolicitOrgController {

private static Logger logger = LoggerFactory.getLogger(AeaSolicitOrgController.class);

    @Autowired
    private AeaSolicitOrgService aeaSolicitOrgService;

    @RequestMapping("/indexAeaSolicitOrg.do")
    public ModelAndView indexAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg, String infoType){

        return new ModelAndView("aea/solicit/org_index");
    }

    @RequestMapping("/listAeaSolicitOrg.do")
    public PageInfo<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitOrg);
        return aeaSolicitOrgService.listAeaSolicitOrg(aeaSolicitOrg,page);
    }

    @RequestMapping("/getAeaSolicitOrg.do")
    public AeaSolicitOrg getAeaSolicitOrg(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaSolicitOrg对象，ID为：{}", id);
            return aeaSolicitOrgService.getAeaSolicitOrgById(id);
        }
        else {
            logger.debug("构建新的AeaSolicitOrg对象");
            return new AeaSolicitOrg();
        }
    }

    @RequestMapping("/updateAeaSolicitOrg.do")
        public ResultForm updateAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaSolicitOrg);
        aeaSolicitOrgService.updateAeaSolicitOrg(aeaSolicitOrg);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑按组织征求配置表
    * @param aeaSolicitOrg 按组织征求配置表
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaSolicitOrg.do")
    public ResultForm saveAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存按组织征求配置表Form对象出错");
            throw new InvalidParameterException(aeaSolicitOrg);
        }

        if(aeaSolicitOrg.getSolicitOrgId()!=null&&!"".equals(aeaSolicitOrg.getSolicitOrgId())){
            aeaSolicitOrgService.updateAeaSolicitOrg(aeaSolicitOrg);
        }else{
        if(aeaSolicitOrg.getSolicitOrgId()==null||"".equals(aeaSolicitOrg.getSolicitOrgId()))
            aeaSolicitOrg.setSolicitOrgId(UUID.randomUUID().toString());
            aeaSolicitOrgService.saveAeaSolicitOrg(aeaSolicitOrg);
        }

        return  new ContentResultForm<AeaSolicitOrg>(true,aeaSolicitOrg);
    }

    @RequestMapping("/deleteAeaSolicitOrgById.do")
    public ResultForm deleteAeaSolicitOrgById(String id) throws Exception{
        logger.debug("删除按组织征求配置表Form对象，对象id为：{}", id);
        if(id!=null)
            aeaSolicitOrgService.deleteAeaSolicitOrgById(id);
        return new ResultForm(true);
    }

}
