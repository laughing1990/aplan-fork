package com.augurit.aplanmis.admin.solicit;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaSolicitItem;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitItemService;
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
* 按事项征求配置表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/solicit/item")
public class AeaSolicitItemController {

private static Logger logger = LoggerFactory.getLogger(AeaSolicitItemController.class);

    @Autowired
    private AeaSolicitItemService aeaSolicitItemService;

    @RequestMapping("/index.do")
    public ModelAndView index(AeaSolicitItem aeaSolicitItem){

        return new ModelAndView("ui-jsp/solicit/item/index");
    }

    @RequestMapping("/listAeaSolicitItem.do")
    public PageInfo<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaSolicitItem);
        return aeaSolicitItemService.listAeaSolicitItem(aeaSolicitItem,page);
    }

    @RequestMapping("/getAeaSolicitItem.do")
    public AeaSolicitItem getAeaSolicitItem(String id) throws Exception {

        if (id != null){
            logger.debug("根据ID获取AeaSolicitItem对象，ID为：{}", id);
            return aeaSolicitItemService.getAeaSolicitItemById(id);
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

        if(aeaSolicitItem.getSolicitItemId()!=null&&!"".equals(aeaSolicitItem.getSolicitItemId())){
            aeaSolicitItemService.updateAeaSolicitItem(aeaSolicitItem);
        }else{
            aeaSolicitItem.setSolicitItemId(UUID.randomUUID().toString());
            aeaSolicitItemService.saveAeaSolicitItem(aeaSolicitItem);
        }
        return  new ContentResultForm<AeaSolicitItem>(true,aeaSolicitItem);
    }

    @RequestMapping("/deleteAeaSolicitItemById.do")
    public ResultForm deleteAeaSolicitItemById(String id) throws Exception{
        logger.debug("删除按事项征求配置表Form对象，对象id为：{}", id);
        if(id!=null) {
            aeaSolicitItemService.deleteAeaSolicitItemById(id);
        }
        return new ResultForm(true);
    }

}
