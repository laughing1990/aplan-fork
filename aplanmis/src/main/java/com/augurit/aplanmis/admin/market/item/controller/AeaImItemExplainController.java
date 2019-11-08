package com.augurit.aplanmis.admin.market.item.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.item.service.AeaImItemExplainService;
import com.augurit.aplanmis.common.domain.AeaImItemExplain;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
* -Controller 页面控制转发类
*/
@Api(description = "中介服务事项拓展表相关接口")
@RestController
@RequestMapping("/supermarket/explain")
public class AeaImItemExplainController {

private static Logger logger = LoggerFactory.getLogger(AeaImItemExplainController.class);

    @Autowired
    private AeaImItemExplainService aeaImItemExplainService;


    @RequestMapping("/indexAeaImItemExplain.do")
    public ModelAndView indexAeaImItemExplain(AeaImItemExplain aeaImItemExplain, String infoType){
        return new ModelAndView("aea/im/item/item_explain_index");
    }

    @RequestMapping("/listAeaImItemExplain.do")
    public PageInfo<AeaImItemExplain> listAeaImItemExplain(AeaImItemExplain aeaImItemExplain, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImItemExplain);
        return aeaImItemExplainService.listAeaImItemExplain(aeaImItemExplain,page);
    }

    @RequestMapping("/getAeaImItemExplain.do")
    public AeaImItemExplain getAeaImItemExplain(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaImItemExplain对象，ID为：{}", id);
            return aeaImItemExplainService.getAeaImItemExplainById(id);
        }
        else {
            logger.debug("构建新的AeaImItemExplain对象");
            return new AeaImItemExplain();
        }
    }

    @RequestMapping("/updateAeaImItemExplain.do")
        public ResultForm updateAeaImItemExplain(AeaImItemExplain aeaImItemExplain) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaImItemExplain);
        aeaImItemExplainService.updateAeaImItemExplain(aeaImItemExplain);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑
    * @param aeaImItemExplain 
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaImItemExplain.do")
    public ResultForm saveAeaImItemExplain(AeaImItemExplain aeaImItemExplain, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaImItemExplain);
        }

        if(aeaImItemExplain.getItemExplainId()!=null&&!"".equals(aeaImItemExplain.getItemExplainId())){
            aeaImItemExplainService.updateAeaImItemExplain(aeaImItemExplain);
        }else{
        if(aeaImItemExplain.getItemExplainId()==null||"".equals(aeaImItemExplain.getItemExplainId()))
            aeaImItemExplainService.saveAeaImItemExplain(aeaImItemExplain);
        }

        return  new ContentResultForm<AeaImItemExplain>(true,aeaImItemExplain);
    }

    @RequestMapping("/deleteAeaImItemExplainById.do")
    public ResultForm deleteAeaImItemExplainById(String id) throws Exception{
        logger.debug("删除Form对象，对象id为：{}", id);
        if(id!=null)
            aeaImItemExplainService.deleteAeaImItemExplainById(id);
        return new ResultForm(true);
    }

    @RequestMapping("/getAeaImItemExplainByItemVerId.do")
    public AeaImItemExplain getAeaImItemExplainByItemVerId(String itemVerId) throws Exception {
        if (StringUtils.isNotBlank(itemVerId)){
            logger.debug("根据itemVerId获取AeaImItemExplain对象，itemVerId为：{}", itemVerId);
            return aeaImItemExplainService.getAeaImItemExplainByItemVerId(itemVerId);
        }else {
            logger.debug("构建新的AeaImItemExplain对象");
            return new AeaImItemExplain();
        }
    }

}
