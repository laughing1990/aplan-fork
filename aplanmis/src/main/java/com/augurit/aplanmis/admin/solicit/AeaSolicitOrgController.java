package com.augurit.aplanmis.admin.solicit;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
* 按组织征求配置表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/solicit/org")
public class AeaSolicitOrgController {

    private static Logger logger = LoggerFactory.getLogger(AeaSolicitOrgController.class);

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaSolicitOrgService aeaSolicitOrgService;

    @RequestMapping("/index.do")
    public ModelAndView indexAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg, ModelMap modelMap){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        // 意见征求类型
        List<BscDicCodeItem> solicitBusTypes = bscDicCodeService.getActiveItemsByTypeCode("SOLICIT_BUS_TYPE", rootOrgId);
        modelMap.put("solicitBusTypes", solicitBusTypes);
        return new ModelAndView("ui-jsp/solicit/org/index");
    }

    @RequestMapping("/listAeaSolicitOrgByPage.do")
    public EasyuiPageInfo<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitOrg);
        PageInfo<AeaSolicitOrg> pageInfo = aeaSolicitOrgService.listAeaSolicitOrg(aeaSolicitOrg,page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaSolicitOrgRelOrgInfoByPage.do")
    public EasyuiPageInfo<AeaSolicitOrg> listAeaSolicitOrgRelOrgInfo(AeaSolicitOrg aeaSolicitOrg, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitOrg);
        PageInfo<AeaSolicitOrg> pageInfo = aeaSolicitOrgService.listAeaSolicitOrgRelOrgInfo(aeaSolicitOrg,page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaSolicitOrg.do")
    public List<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitOrg);
        List<AeaSolicitOrg> list = aeaSolicitOrgService.listAeaSolicitOrg(aeaSolicitOrg);
        return list;
    }

    @RequestMapping("/listAeaSolicitOrgRelOrgInfo.do")
    public List<AeaSolicitOrg> listAeaSolicitOrgRelOrgInfo(AeaSolicitOrg aeaSolicitOrg) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitOrg);
        List<AeaSolicitOrg> list = aeaSolicitOrgService.listAeaSolicitOrgRelOrgInfo(aeaSolicitOrg);
        return list;
    }

    @RequestMapping("/getSolicitOrgById.do")
    public AeaSolicitOrg getAeaSolicitOrgById(String id) throws Exception {

        if (StringUtils.isNotBlank(id)){
            logger.debug("根据ID获取AeaSolicitOrg对象，ID为：{}", id);
            return aeaSolicitOrgService.getAeaSolicitOrgById(id);
        }else {
            logger.debug("构建新的AeaSolicitOrg对象");
            return new AeaSolicitOrg();
        }
    }

    @RequestMapping("/getSolicitOrgRelOrgInfoById.do")
    public AeaSolicitOrg getSolicitOrgRelOrgInfoById(String id) throws Exception {

        if (StringUtils.isNotBlank(id)){

            logger.debug("根据ID获取AeaSolicitOrg对象，ID为：{}", id);
            return aeaSolicitOrgService.getSolicitOrgRelOrgInfoById(id);
        }else {

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
     *
    * @param aeaSolicitOrg 按组织征求配置表
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaSolicitOrg.do")
    public ResultForm saveAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) throws Exception {

        if(StringUtils.isNotBlank(aeaSolicitOrg.getSolicitOrgId())){
            aeaSolicitOrgService.updateAeaSolicitOrg(aeaSolicitOrg);
        }else{
            aeaSolicitOrg.setSolicitOrgId(UUID.randomUUID().toString());
            aeaSolicitOrgService.saveAeaSolicitOrg(aeaSolicitOrg);
        }
        return  new ContentResultForm<AeaSolicitOrg>(true,aeaSolicitOrg);
    }

    @RequestMapping("/delSolicitOrgById.do")
    public ResultForm delSolicitOrgById(String id) throws Exception{

        if(StringUtils.isBlank(id)){
             throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("删除按组织征求配置表Form对象，对象id为：{}", id);
        aeaSolicitOrgService.deleteAeaSolicitOrgById(id);
        return new ResultForm(true);
    }

    @RequestMapping("/batchDelSolicitOrgByIds.do")
    public ResultForm batchDelSolicitOrgByIds(String[] ids) throws Exception{

        if(ids!=null&&ids.length>0){

            logger.debug("批量删除按组织征求配置表Form对象，对象id为：{}", ids);
            aeaSolicitOrgService.batchDelSolicitOrgByIds(ids);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }
}
