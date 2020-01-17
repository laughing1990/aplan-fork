package com.augurit.aplanmis.admin.solicit;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.domain.AeaSolicitItem;
import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitItemService;
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
* 按事项征求配置表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/solicit/item")
public class AeaSolicitItemController {

    private static Logger logger = LoggerFactory.getLogger(AeaSolicitItemController.class);

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaSolicitItemService aeaSolicitItemService;

    @RequestMapping("/index.do")
    public ModelAndView index(AeaSolicitItem aeaSolicitItem, ModelMap modelMap){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        // 意见征求类型
        List<BscDicCodeItem> solicitBusTypes = bscDicCodeService.getActiveItemsByTypeCode("SOLICIT_BUS_TYPE", rootOrgId);
        modelMap.put("solicitBusTypes", solicitBusTypes);
        return new ModelAndView("ui-jsp/solicit/item/index");
    }

    @RequestMapping("/listAeaSolicitItemByPage.do")
    public EasyuiPageInfo<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitItem);
        PageInfo<AeaSolicitItem> pageInfo = aeaSolicitItemService.listAeaSolicitItem(aeaSolicitItem,page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaSolicitItemRelInfoByPage.do")
    public EasyuiPageInfo<AeaSolicitItem> listAeaSolicitItemRelInfo(AeaSolicitItem aeaSolicitItem, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitItem);
        PageInfo<AeaSolicitItem> pageInfo = aeaSolicitItemService.listAeaSolicitItemRelInfo(aeaSolicitItem, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaSolicitItem.do")
    public List<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitItem);
        return aeaSolicitItemService.listAeaSolicitItem(aeaSolicitItem);
    }

    @RequestMapping("/listAeaSolicitItemRelInfo.do")
    public List<AeaSolicitItem> listAeaSolicitItemRelInfo(AeaSolicitItem aeaSolicitItem) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitItem);
        return aeaSolicitItemService.listAeaSolicitItemRelInfo(aeaSolicitItem);
    }

    @RequestMapping("/getAeaSolicitItem.do")
    public AeaSolicitItem getAeaSolicitItem(String id) throws Exception {

        if (StringUtils.isNotBlank(id)){
            logger.debug("根据ID获取AeaSolicitItem对象，ID为：{}", id);
            return aeaSolicitItemService.getAeaSolicitItemById(id);
        }else {
            logger.debug("构建新的AeaSolicitItem对象");
            return new AeaSolicitItem();
        }
    }

    @RequestMapping("/getAeaSolicitItemRelInfoById.do")
    public AeaSolicitItem getAeaSolicitItemRelInfoById(String id) throws Exception {

        if (StringUtils.isNotBlank(id)){

            logger.debug("根据ID获取AeaSolicitItem对象，ID为：{}", id);
            return aeaSolicitItemService.getAeaSolicitItemRelInfoById(id);
        }else {

            logger.debug("构建新的AeaSolicitItem对象");
            return new AeaSolicitItem();
        }
    }

    @RequestMapping("/updateAeaSolicitItem.do")
    public ResultForm updateAeaSolicitItem(AeaSolicitItem aeaSolicitItem) throws Exception {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaSolicitItem);
        aeaSolicitItemService.updateAeaSolicitItem(aeaSolicitItem);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑按事项征求配置表
     *
    * @param aeaSolicitItem 按事项征求配置表
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaSolicitItem.do")
    public ResultForm saveAeaSolicitItem(AeaSolicitItem aeaSolicitItem) throws Exception {

        if(StringUtils.isNotBlank(aeaSolicitItem.getSolicitItemId())){
            aeaSolicitItemService.updateAeaSolicitItem(aeaSolicitItem);
        }else{
            aeaSolicitItem.setSolicitItemId(UUID.randomUUID().toString());
            aeaSolicitItemService.saveAeaSolicitItem(aeaSolicitItem);
        }
        return  new ContentResultForm<AeaSolicitItem>(true,aeaSolicitItem);
    }

    @RequestMapping("/deleteAeaSolicitItemById.do")
    public ResultForm deleteAeaSolicitItemById(String id) throws Exception{

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("删除按事项征求配置表Form对象，对象id为：{}", id);
        aeaSolicitItemService.deleteAeaSolicitItemById(id);
        return new ResultForm(true);
    }

    @RequestMapping("/batchDelSolicitItemByIds.do")
    public ResultForm batchDelSolicitItemByIds(String[] ids) throws Exception{

        if(ids!=null&&ids.length>0){

            logger.debug("删除按事项征求配置表Form对象，对象id为：{}", ids);
            aeaSolicitItemService.batchDelSolicitItemByIds(ids);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @RequestMapping("/batchSaveSolicitItem.do")
    public ResultForm batchSaveSolicitItem(String busType, String solicitType, String[] itemIds) throws Exception{

        if(StringUtils.isBlank(busType)){
            throw new InvalidParameterException("参数busType为空!");
        }
        if(StringUtils.isBlank(solicitType)){
            throw new InvalidParameterException("参数solicitType为空!");
        }
        if (itemIds != null && itemIds.length > 0) {
            aeaSolicitItemService.batchSaveSolicitItem(busType, solicitType, itemIds);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/gtreeSolicitItem.do")
    public List<ZtreeNode> gtreeSolicitItem() throws Exception{

        return aeaSolicitItemService.gtreeSolicitItem(SecurityContext.getCurrentOrgId());
    }

    @RequestMapping("/getCountNotRelSelf.do")
    public ResultForm getCountNotRelSelf(AeaSolicitItem aeaSolicitItem) throws Exception {

        aeaSolicitItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        Long num = aeaSolicitItemService.getCountNotRelSelf(aeaSolicitItem);
        return num==null? new ContentResultForm<>(false, num): new ContentResultForm<>(true, num);
    }
}
