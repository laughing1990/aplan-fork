package com.augurit.aplanmis.admin.market.unit.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.unit.service.AeaImUnitRequireService;
import com.augurit.aplanmis.common.domain.AeaImUnitRequire;
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
@Api(description = "中介机构要求表相关接口")
@RestController
@RequestMapping("/supermarket/unit/require")
public class AeaImUnitRequireController {

private static Logger logger = LoggerFactory.getLogger(AeaImUnitRequireController.class);

    @Autowired
    private AeaImUnitRequireService aeaImUnitRequireService;

    @RequestMapping("/indexAeaImUnitRequire.do")
    public ModelAndView indexAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire, String infoType){
        return new ModelAndView("aea/im/unit/unit_require_index");
    }

    @RequestMapping("/listAeaImUnitRequire.do")
    public PageInfo<AeaImUnitRequire> listAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImUnitRequire);
        return aeaImUnitRequireService.listAeaImUnitRequire(aeaImUnitRequire,page);
    }

    @RequestMapping("/getAeaImUnitRequire.do")
    public AeaImUnitRequire getAeaImUnitRequire(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaImUnitRequire对象，ID为：{}", id);
            return aeaImUnitRequireService.getAeaImUnitRequireById(id);
        }
        else {
            logger.debug("构建新的AeaImUnitRequire对象");
            return new AeaImUnitRequire();
        }
    }

    @RequestMapping("/updateAeaImUnitRequire.do")
        public ResultForm updateAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaImUnitRequire);
        aeaImUnitRequireService.updateAeaImUnitRequire(aeaImUnitRequire);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑
    * @param aeaImUnitRequire 
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaImUnitRequire.do")
    public ResultForm saveAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaImUnitRequire);
        }
        if(StringUtils.isNotBlank(aeaImUnitRequire.getUnitRequireId())){
            aeaImUnitRequireService.updateAeaImUnitRequire(aeaImUnitRequire);
        }else {
            aeaImUnitRequireService.saveAeaImUnitRequire(aeaImUnitRequire);
        }
        return  new ContentResultForm<AeaImUnitRequire>(true,aeaImUnitRequire);
    }

    @RequestMapping("/deleteAeaImUnitRequireById.do")
    public ResultForm deleteAeaImUnitRequireById(String id) throws Exception{
        logger.debug("删除Form对象，对象id为：{}", id);
        if(id!=null)
            aeaImUnitRequireService.deleteAeaImUnitRequireById(id);
        return new ResultForm(true);
    }

    /**
     * 根据事项版本ID查询配置的中介机构要求
     * @param itemVerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/getAeaImUnitRequireByItemVerId.do")
    public AeaImUnitRequire getAeaImUnitRequireByItemVerId(String itemVerId) throws Exception {
        AeaImUnitRequire aeaImUnitRequire = aeaImUnitRequireService.getAeaImUnitRequireByItemVerId(itemVerId);
        return aeaImUnitRequire;
    }

    /**
     * 中介服务事项保存对应的资质要求。
     * @param itemVerId
     * @param qualSeq
     * @return
     * @throws Exception
     */
    @RequestMapping("/autoSaveQualRequire.do")
    public ContentResultForm autoSaveQualRequire(String itemVerId,String qualSeq) throws Exception{
        ContentResultForm resultForm = new ContentResultForm(false);
        if(StringUtils.isNotBlank(itemVerId) && StringUtils.isNotBlank(qualSeq)){
            AeaImUnitRequire require = this.getAeaImUnitRequireByItemVerId(itemVerId);
            if(require != null){
                require.setQualRequire(qualSeq);
                aeaImUnitRequireService.updateAeaImUnitRequire(require);
                resultForm.setSuccess(true);
                resultForm.setContent(require);
            }
        }
        return resultForm;
    }
}
