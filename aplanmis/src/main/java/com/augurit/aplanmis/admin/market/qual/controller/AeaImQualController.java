package com.augurit.aplanmis.admin.market.qual.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.qual.service.AeaImQualService;
import com.augurit.aplanmis.common.domain.AeaImQual;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* -Controller 页面控制转发类
*/
@Api(description = "中介超市资质页面相关接口")
@RestController
@RequestMapping("/supermarket/qual")
public class AeaImQualController {

private static Logger logger = LoggerFactory.getLogger(AeaImQualController.class);

    @Autowired
    private AeaImQualService aeaImQualService;


    @RequestMapping("/index.do")
    public ModelAndView indexAeaImQual(AeaImQual aeaImQual, String infoType){
        return new ModelAndView("ui-jsp/supermarket_manage/qual/qual_manage");
    }

    @RequestMapping("/list.do")
    public EasyuiPageInfo<AeaImQual> listAeaImQual(AeaImQual aeaImQual, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImQual);
        PageInfo<AeaImQual> pageInfo = aeaImQualService.listAeaImQual(aeaImQual, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaImNotServiceQual.do")
    public EasyuiPageInfo<AeaImQual> listAeaImNotServiceQual(AeaImQual aeaImQual, String servicequalIds, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImQual);
        List<String> str = new ArrayList<String>();
        if(servicequalIds!=null){

            str = Arrays.asList(servicequalIds.split(","));
        }
        PageInfo<AeaImQual> pageInfo = aeaImQualService.listAeaImNotInQual(aeaImQual,str,page);

        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaImServiceQual.do")
    public EasyuiPageInfo<AeaImQual> listAeaImServiceQual(AeaImQual aeaImQual, String servicequalIds, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImQual);

        List<String> qualIds = Arrays.asList(servicequalIds.split(","));
        PageInfo<AeaImQual> pageInfo = aeaImQualService.listAeaImInQual(aeaImQual,qualIds,page);

        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getAeaImQual.do")
    public AeaImQual getAeaImQual(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaImQual对象，ID为：{}", id);
            return aeaImQualService.getAeaImQualById(id);
        }
        else {
            logger.debug("构建新的AeaImQual对象");
            return new AeaImQual();
        }
    }

    @RequestMapping("/updateAeaImQual.do")
        public ResultForm updateAeaImQual(AeaImQual aeaImQual) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaImQual);
        aeaImQualService.updateAeaImQual(aeaImQual);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑
    * @param aeaImQual 
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaImQual.do")
    public ResultForm saveAeaImQual(AeaImQual aeaImQual, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaImQual);
        }

        if(StringUtils.isNotBlank(aeaImQual.getQualId())){
            aeaImQualService.updateAeaImQual(aeaImQual);
        }else{
            aeaImQualService.saveAeaImQual(aeaImQual);
        }
        return  new ContentResultForm<AeaImQual>(true,aeaImQual);
    }

    @RequestMapping("/deleteAeaImQualById.do")
    public ResultForm deleteAeaImQualById(String id) throws Exception{
        logger.debug("删除Form对象，对象id为：{}", id);
        if(id!=null)
            aeaImQualService.deleteAeaImQualById(id);
        return new ResultForm(true);
    }

    /**
     * 校验资质编码是否可用
     * @param qualId
     * @param qualCode
     * @return
     */
    @RequestMapping("/checkUniqueQualCode.do")
    public String checkUniqueQualCode(String qualId, String qualCode) {
        if (StringUtils.isBlank(qualCode)) {
            return "false";
        }
        return aeaImQualService.checkUniqueQualCode(qualId, qualCode) + "";
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/batchDeleteQual.do")
    public ResultForm batchDeleteQual(String[] ids) throws Exception{
        ResultForm resultForm = new ResultForm(false, "操作失败！");
        if (ids != null && ids.length > 0) {
            aeaImQualService.batchDeleteQual(ids);
            resultForm.setSuccess(true);
            resultForm.setMessage("操作成功！");
        }
        return resultForm;
    }

    /**
     *  保存资质等级类型配置
     * @param qualId            资质ID
     * @param qualLevelId      资质等级类型ID（根节点）
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveQualLevelCfg.do")
    public ResultForm saveQualLevelCfg(String qualId, String qualLevelId) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        if(StringUtils.isNotBlank(qualId)&& StringUtils.isNotBlank(qualLevelId)){
            resultForm = aeaImQualService.saveQualLevelCfg(qualId, qualLevelId);
        }
        return resultForm;
    }

    /**
     * 根据事项版本ID查询中介机构要求配置的资质
     * @param itemVerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/getQualIdsByItemVerId.do")
    public ContentResultForm getQualIdsByItemVerId(String itemVerId) throws Exception {
        ContentResultForm resultForm = aeaImQualService.getQualIdsByItemVerId(itemVerId);
        return resultForm;
    }

    /**
     * 根据ID序列查询资质
     * @param idSeq
     * @return
     * @throws Exception
     */
    @RequestMapping("/getQualNamesByIds.do")
    public List<AeaImQual> getQualNamesByIds(String idSeq) throws Exception {
        List<AeaImQual> list = aeaImQualService.getQualNamesByIds(idSeq);
        return list;
    }
}
