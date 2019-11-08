package com.augurit.aplanmis.admin.market.service.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.service.service.AeaImServiceMajorService;
import com.augurit.aplanmis.common.domain.AeaImServiceMajor;
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

import java.util.List;

/**
* -Controller 页面控制转发类
*/
@Api(description = "中介超市专业类别相关接口")
@RestController
@RequestMapping("/supermarket/major")
public class AeaImServiceMajorController {

private static Logger logger = LoggerFactory.getLogger(AeaImServiceMajorController.class);

    @Autowired
    private AeaImServiceMajorService aeaImServiceMajorService;


    @RequestMapping("/index.do")
    public ModelAndView indexAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor, String infoType){
        return new ModelAndView("ui-jsp/supermarket_manage/major/serviceMajorManage");
    }

    /**
     * 查询服务类型配置的专业类别列表
     * @param aeaImServiceMajor
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/listAeaImServiceMajor.do")
    public EasyuiPageInfo<AeaImServiceMajor> listAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImServiceMajor);
        PageInfo<AeaImServiceMajor> pageInfo = aeaImServiceMajorService.listAeaImServiceMajor(aeaImServiceMajor, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    /**
     * 根据资质ID查询专业类别树
     * @return
     */
    @RequestMapping("/getMajorTreeByQualId.do")
    public List<ZtreeNode> getMajorTreeByQualId(String QualId) throws Exception{
        return aeaImServiceMajorService.getMajorTreeByQualId(QualId);
    }

    /**
     * 校验专业类别编码是否可用
     * @param majorId
     * @param majorCode
     * @return
     */
    @RequestMapping("/checkUniqueMajorTypeCode.do")
    public String checkUniqueMajorTypeCode(String majorId, String majorCode) {
        if (StringUtils.isBlank(majorCode)) {
            return "false";
        }
        return aeaImServiceMajorService.checkUniqueMajorTypeCode(majorId, majorCode) + "";
    }

    @RequestMapping("/getAeaImServiceMajor.do")
    public AeaImServiceMajor getAeaImServiceMajor(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaImServiceMajor对象，ID为：{}", id);
            return aeaImServiceMajorService.getAeaImServiceMajorById(id);
        }
        else {
            logger.debug("构建新的AeaImServiceMajor对象");
            return new AeaImServiceMajor();
        }
    }

    @RequestMapping("/updateAeaImServiceMajor.do")
        public ResultForm updateAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaImServiceMajor);
        aeaImServiceMajorService.updateAeaImServiceMajor(aeaImServiceMajor);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑
    * @param aeaImServiceMajor 
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaImServiceMajor.do")
    public ResultForm saveAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaImServiceMajor);
        }

        if(StringUtils.isNotBlank(aeaImServiceMajor.getMajorId())){
            aeaImServiceMajorService.updateAeaImServiceMajor(aeaImServiceMajor);
        }else{
            aeaImServiceMajorService.saveAeaImServiceMajor(aeaImServiceMajor);
        }

        return  new ContentResultForm<AeaImServiceMajor>(true,aeaImServiceMajor);
    }

    /**
     * 根据专业类别id删除
     * @param id            要删除的专业
     * @param delChildren  是否关联删除子专业 1表示是
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteAeaImServiceMajorById.do")
    public ResultForm deleteAeaImServiceMajorById(String id,String delChildren) throws Exception{
        logger.debug("删除专业类别，参数为：{}，{}", id,delChildren);
        ResultForm resultForm = aeaImServiceMajorService.deleteAeaImServiceMajorById(id, "1".equals(delChildren));
        return resultForm;
    }
}
