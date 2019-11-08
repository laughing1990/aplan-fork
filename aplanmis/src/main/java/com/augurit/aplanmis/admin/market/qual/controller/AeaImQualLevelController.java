package com.augurit.aplanmis.admin.market.qual.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.admin.market.qual.service.AeaImQualLevelService;
import com.augurit.aplanmis.common.domain.AeaImQualLevel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
* -Controller 页面控制转发类
*/
@RestController
@RequestMapping("/supermarket/qual/level")
public class AeaImQualLevelController {

private static Logger logger = LoggerFactory.getLogger(AeaImQualLevelController.class);

    @Autowired
    private AeaImQualLevelService aeaImQualLevelService;


    @RequestMapping("/indexAeaImQualLevel.do")
    public ModelAndView indexAeaImQualLevel(AeaImQualLevel aeaImQualLevel, String infoType){
        return new ModelAndView("aea/im/qual/qual_level_index");
    }

    @RequestMapping("/list.do")
    public PageInfo<AeaImQualLevel> listAeaImQualLevel(AeaImQualLevel aeaImQualLevel, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImQualLevel);
        return aeaImQualLevelService.listAeaImQualLevel(aeaImQualLevel,page);
    }

    @RequestMapping("/listAeaImQualLevelRootNode.do")
    public List<AeaImQualLevel> listAeaImQualLevelRootNode() throws Exception {
        logger.debug("查询资质等级根节点数据");
        AeaImQualLevel search = new AeaImQualLevel();
        search.setParentQualLevelId("root");
        return aeaImQualLevelService.listAeaImQualLevel(search);
    }

    @RequestMapping("/getAeaImQualLevel.do")
    public AeaImQualLevel getAeaImQualLevel(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaImQualLevel对象，ID为：{}", id);
            return aeaImQualLevelService.getAeaImQualLevelById(id);
        }
        else {
            logger.debug("构建新的AeaImQualLevel对象");
            return new AeaImQualLevel();
        }
    }

    @RequestMapping("/updateAeaImQualLevel.do")
        public ResultForm updateAeaImQualLevel(AeaImQualLevel aeaImQualLevel) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaImQualLevel);
        aeaImQualLevelService.updateAeaImQualLevel(aeaImQualLevel);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑
    * @param aeaImQualLevel 
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaImQualLevel.do")
    public ResultForm saveAeaImQualLevel(AeaImQualLevel aeaImQualLevel, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaImQualLevel);
        }

        if(aeaImQualLevel.getQualLevelId()!=null&&!"".equals(aeaImQualLevel.getQualLevelId())){
            aeaImQualLevelService.updateAeaImQualLevel(aeaImQualLevel);
        }else{
        if(aeaImQualLevel.getQualLevelId()==null||"".equals(aeaImQualLevel.getQualLevelId()))
            aeaImQualLevel.setQualLevelId(UUID.randomUUID().toString());
            aeaImQualLevelService.saveAeaImQualLevel(aeaImQualLevel);
        }

        return  new ContentResultForm<AeaImQualLevel>(true,aeaImQualLevel);
    }

    @RequestMapping("/deleteAeaImQualLevelById.do")
    public ResultForm deleteAeaImQualLevelById(String id) throws Exception{
        logger.debug("删除Form对象，对象id为：{}", id);
        if(id!=null)
            aeaImQualLevelService.deleteAeaImQualLevelById(id);
        return new ResultForm(true);
    }

}
