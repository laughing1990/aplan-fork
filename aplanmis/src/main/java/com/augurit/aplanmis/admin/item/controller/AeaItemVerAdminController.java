package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.PublishStatus;
import com.augurit.aplanmis.common.domain.AeaItemVer;
import com.augurit.aplanmis.common.service.admin.item.impl.AeaItemVerAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/8/8 008 14:24
 * @desc
 **/
@RestController
@RequestMapping("/aea/item/ver")
public class AeaItemVerAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemVerAdminController.class);

    @Autowired
    private AeaItemVerAdminService aeaItemVerAdminService;

    @RequestMapping("/index.do")
    public ModelAndView indexAeaItemVer() {
        return new ModelAndView("aea/item/item_ver_index");
    }

    @RequestMapping("/listAeaItemVer.do")
    public PageInfo<AeaItemVer> listAeaItemVer(AeaItemVer aeaItemVer, Page page) {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemVer);
        return aeaItemVerAdminService.listAeaItemVer(aeaItemVer, page);
    }

    @RequestMapping("/listAeaItemVerNoPage.do")
    public List<AeaItemVer> listAeaItemVerNoPage(AeaItemVer aeaItemVer) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemVer);
        return aeaItemVerAdminService.listAeaItemVer(aeaItemVer);
    }

    @RequestMapping("/getAeaItemVer.do")
    public AeaItemVer getAeaItemVer(String id) {
        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取AeaItemVer对象，ID为：{}", id);
            return aeaItemVerAdminService.getAeaItemVerById(id);
        } else {
            logger.debug("构建新的AeaItemVer对象");
            return new AeaItemVer();
        }
    }

    @RequestMapping("/updateAeaItemVer.do")
    public ResultForm updateAeaItemVer(AeaItemVer aeaItemVer) {
        aeaItemVerAdminService.updateAeaItemVer(aeaItemVer);
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemVer);
        return new ResultForm(true);
    }

    @RequestMapping("/deleteAeaItemVerById.do")
    public ResultForm deleteAeaItemVerById(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaItemVerAdminService.deleteAeaItemVerById(id);
            logger.debug("删除主题定义版本表Form对象，对象id为：{}", id);
            return new ResultForm(true);
        }
        return new ResultForm(false, "参数id为空!");
    }

    @RequestMapping("/unpublished/num.do")
    public ResultForm getUnpublishedNum(String itemId) {

        if(StringUtils.isBlank(itemId)){
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }
        AeaItemVer ver = new AeaItemVer();
        ver.setItemId(itemId);
        ver.setItemVerStatus(PublishStatus.UNPUBLISHED.getValue());
        List<AeaItemVer> vers = aeaItemVerAdminService.listAeaItemVer(ver);
        if(vers!=null&&vers.size()>0){
            return new ResultForm(false,"存在最新未发布版本");
        }else{
            return new ResultForm(true, "不存在最新未发布版本!");
        }
    }

    @RequestMapping("/copy/max/version.do")
    public ResultForm copyMaxPublishedOrTestRunVer(String itemId) {

        if(StringUtils.isBlank(itemId)){
            throw new IllegalArgumentException("事项itemId为空!");
        }
        String newItemVerId = aeaItemVerAdminService.copyMaxPublishedOrTestRunVer(itemId);
        logger.debug("复制成功，newItemVerId: {}", newItemVerId);
        return new ContentResultForm<String>(true,newItemVerId,"复制成功!");
    }

    @RequestMapping("/copy/version.do")
    public ResultForm copyOnePublishedOrTestRunVer(String itemId, String itemVerId) {

        if(StringUtils.isBlank(itemId)){
            throw new IllegalArgumentException("事项itemId为空!");
        }
        if(StringUtils.isBlank(itemVerId)){
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }
        String newItemVerId = aeaItemVerAdminService.copyItemVerRelData(itemId, itemVerId, SecurityContext.getCurrentOrgId());
        logger.debug("复制成功，newItemVerId: {}", newItemVerId);
        return new ContentResultForm<String>(true,newItemVerId,"复制成功!");
    }

    /**
     * 复制事项版本并创建新的事项
     *
     * @param itemId
     * @param itemVerId
     * @return
     */
    @RequestMapping("/copyAndCreateNew.do")
    public ResultForm copyAndCreateNew(String itemId, String itemVerId) {

        if(StringUtils.isBlank(itemId)){
            throw new IllegalArgumentException("事项itemId为空!");
        }
        if(StringUtils.isBlank(itemVerId)){
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }
        aeaItemVerAdminService.copyAndCreateNew(itemId, itemVerId, SecurityContext.getCurrentOrgId());
        logger.debug("复制并创建新的事项成功!");
        return new ContentResultForm<String>(true, "复制成功!");
    }

    @RequestMapping("/testRunOrPublished.do")
    public ResultForm testRunOrPublished(String itemId, String itemVerId, Double verNum, String type, String oldVerStatus)throws Exception{

        if(StringUtils.isBlank(itemId)){
            throw new IllegalArgumentException("事项itemId为空！");
        }
        if(StringUtils.isBlank(itemVerId)){
            throw new IllegalArgumentException("事项版本itemVerId为空！");
        }
        Assert.notNull(verNum, "事项版本序号verNum为空!");
        if(StringUtils.isBlank(type)){
            throw new IllegalArgumentException("当前操作不明确！");
        }
        if(!(type.equals("2")||type.equals("1"))){
            throw new IllegalArgumentException("当前操作不明确,可以是试运行或者发布！");
        }
        aeaItemVerAdminService.testRunOrPublished(itemId, itemVerId, verNum, type, oldVerStatus);
        return new ResultForm(true, "操作成功！");
    }
}
