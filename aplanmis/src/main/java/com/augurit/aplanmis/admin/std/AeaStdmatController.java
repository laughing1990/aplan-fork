package com.augurit.aplanmis.admin.std;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaStdmat;
import com.augurit.aplanmis.common.service.admin.std.AeaStdmatAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
* 标准材料定义表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/stdmat")
public class AeaStdmatController {

    private static Logger logger = LoggerFactory.getLogger(AeaStdmatController.class);

    @Autowired
    private AeaStdmatAdminService aeaStdmatService;

    @RequestMapping("/index.do")
    public ModelAndView indexAeaStdmat(AeaStdmat aeaStdmat, ModelMap modelMap){

        return new ModelAndView("ui-jsp/std/aea_stdmat_store");
    }

    @RequestMapping("/listAeaStdmat.do")
    public PageInfo<AeaStdmat> listAeaStdmat(AeaStdmat aeaStdmat, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaStdmat);
        return aeaStdmatService.listAeaStdmat(aeaStdmat,page);
    }

    @RequestMapping("/getAeaStdmat.do")
    public AeaStdmat getAeaStdmat(String id) throws Exception {

        if (StringUtils.isNotBlank(id)){
            logger.debug("根据ID获取AeaStdmat对象，ID为：{}", id);
            return aeaStdmatService.getAeaStdmatById(id);
        }else {
            logger.debug("构建新的AeaStdmat对象");
            return new AeaStdmat();
        }
    }

    @RequestMapping("/updateAeaStdmat.do")
    public ResultForm updateAeaStdmat(AeaStdmat aeaStdmat) throws Exception {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaStdmat);
        aeaStdmatService.updateAeaStdmat(aeaStdmat);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑标准材料定义表
     *
    * @param aeaStdmat 标准材料定义表
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaStdmat.do")
    public ResultForm saveAeaStdmat(AeaStdmat aeaStdmat) throws Exception {

        if (StringUtils.isNotBlank(aeaStdmat.getStdmatId())){
            aeaStdmatService.updateAeaStdmat(aeaStdmat);
        }else{
            aeaStdmat.setStdmatId(UUID.randomUUID().toString());
            aeaStdmatService.saveAeaStdmat(aeaStdmat);
        }
        return  new ContentResultForm<AeaStdmat>(true,aeaStdmat);
    }

    @RequestMapping("/deleteAeaStdmatById.do")
    public ResultForm deleteAeaStdmatById(String id) throws Exception{

        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除标准材料定义表Form对象，对象id为：{}", id);
            aeaStdmatService.deleteAeaStdmatById(id);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数id为空!");
        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public Long getMaxSortNo() throws Exception{

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaStdmatService.getMaxSortNo(rootOrgId);
    }

    @ApiOperation(value = "查找是否已存在该编号", notes = "查找是否已存在该编号")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "stdmatId", value = "id", dataType = "String"),
        @ApiImplicitParam(name = "stdmatCode", value = "编码", dataType = "String", required = true)
    })
    @RequestMapping(value ="/checkUniqueCode.do", method = {RequestMethod.GET, RequestMethod.POST})
    public String checkUniqueTypeCode(String stdmatId, String stdmatCode) {

        if (StringUtils.isBlank(stdmatCode)) {
            return "false";
        }
        return aeaStdmatService.checkUniqueCode(stdmatId, stdmatCode) + "";
    }
}
