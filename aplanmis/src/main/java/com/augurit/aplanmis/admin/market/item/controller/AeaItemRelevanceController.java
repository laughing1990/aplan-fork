package com.augurit.aplanmis.admin.market.item.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.admin.market.item.service.AeaItemRelevanceService;
import com.augurit.aplanmis.common.domain.AeaItemRelevance;
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
* -Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/item/relevance")
public class AeaItemRelevanceController {

private static Logger logger = LoggerFactory.getLogger(AeaItemRelevanceController.class);

    @Autowired
    private AeaItemRelevanceService aeaItemRelevanceService;


    @RequestMapping("/indexAeaItemRelevance.do")
    public ModelAndView indexAeaItemRelevance(AeaItemRelevance aeaItemRelevance, String infoType){
        return new ModelAndView("aea/item/relevance_index");
    }

    @RequestMapping("/listAeaItemRelevance.do")
    public PageInfo<AeaItemRelevance> listAeaItemRelevance(AeaItemRelevance aeaItemRelevance, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemRelevance);
        return aeaItemRelevanceService.listAeaItemRelevance(aeaItemRelevance,page);
    }

    @RequestMapping("/getAeaItemRelevance.do")
    public AeaItemRelevance getAeaItemRelevance(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaItemRelevance对象，ID为：{}", id);
            return aeaItemRelevanceService.getAeaItemRelevanceById(id);
        }
        else {
            logger.debug("构建新的AeaItemRelevance对象");
            return new AeaItemRelevance();
        }
    }

    @RequestMapping("/updateAeaItemRelevance.do")
        public ResultForm updateAeaItemRelevance(AeaItemRelevance aeaItemRelevance) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemRelevance);
        aeaItemRelevanceService.updateAeaItemRelevance(aeaItemRelevance);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑
    * @param aeaItemRelevance 
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaItemRelevance.do")
    public ResultForm saveAeaItemRelevance(AeaItemRelevance aeaItemRelevance, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaItemRelevance);
        }

        if(aeaItemRelevance.getRelevanceId()!=null&&!"".equals(aeaItemRelevance.getRelevanceId())){
            aeaItemRelevanceService.updateAeaItemRelevance(aeaItemRelevance);
        }else{
        if(aeaItemRelevance.getRelevanceId()==null||"".equals(aeaItemRelevance.getRelevanceId()))
            aeaItemRelevance.setRelevanceId(UUID.randomUUID().toString());
            aeaItemRelevanceService.saveAeaItemRelevance(aeaItemRelevance);
        }

        return  new ContentResultForm<AeaItemRelevance>(true,aeaItemRelevance);
    }

    @RequestMapping("/deleteAeaItemRelevanceById.do")
    public ResultForm deleteAeaItemRelevanceById(String id) throws Exception{
        logger.debug("删除Form对象，对象id为：{}", id);
        if(id!=null)
            aeaItemRelevanceService.deleteAeaItemRelevanceById(id);
        return new ResultForm(true);
    }

}
