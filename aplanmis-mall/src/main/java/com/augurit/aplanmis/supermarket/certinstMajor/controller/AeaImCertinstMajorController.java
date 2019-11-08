package com.augurit.aplanmis.supermarket.certinstMajor.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaImCertinstMajor;
import com.augurit.aplanmis.supermarket.certinstMajor.service.AeaImCertinstMajorService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.UUID;

@Api(description = "资质专业和证照实例关联关系操作接口")
@RestController
@RequestMapping("/supermarket/certinstMajor")
public class AeaImCertinstMajorController {

private static Logger logger = LoggerFactory.getLogger(AeaImCertinstMajorController.class);

    @Autowired
    private AeaImCertinstMajorService aeaImCertinstMajorService;


    @RequestMapping("/indexAeaImCertinstMajor.do")
    public ModelAndView indexAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor, String infoType){
        return new ModelAndView("aea/im/certinst/certinst_major_index");
    }

    @ApiOperation(value = "查询资质专业和证照实例关联信息列表", notes = "查询资质专业和证照实例关联信息列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstMajorId", value = "关联关系Id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "certinstId", value = "证照实例ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "majorId", value = "专业ID", required = false, dataType = "String")
    })
    @RequestMapping("/listAeaImCertinstMajor.do")
    public PageInfo<AeaImCertinstMajor> listAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImCertinstMajor);
        return aeaImCertinstMajorService.listAeaImCertinstMajor(aeaImCertinstMajor,page);
    }

    @ApiOperation(value = "获取关联信息", notes = "获取关联信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstMajorId", value = "关联关系ID", required = true, dataType = "String")
    })
    @RequestMapping("/getAeaImCertinstMajor.do")
    public AeaImCertinstMajor getAeaImCertinstMajor(String certinstMajorId) throws Exception {
        if (certinstMajorId != null){
            logger.debug("根据ID获取AeaImCertinstMajor对象，ID为：{}", certinstMajorId);
            return aeaImCertinstMajorService.getAeaImCertinstMajorById(certinstMajorId);
        }
        else {
            logger.debug("构建新的AeaImCertinstMajor对象");
            return new AeaImCertinstMajor();
        }
    }

    @ApiOperation(value = "修改资质专业和证照实例关联信息", notes = "修改资质专业和证照实例关联信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstMajorId", value = "关联关系Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "certinstId", value = "证照实例ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "majorId", value = "专业ID", required = true, dataType = "String")
    })
    @RequestMapping("/updateAeaImCertinstMajor.do")
        public ResultForm updateAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaImCertinstMajor);
        aeaImCertinstMajorService.updateAeaImCertinstMajor(aeaImCertinstMajor);
        return new ResultForm(true);
    }


    @ApiOperation(value = "保存资质专业和证照实例关联信息", notes = "保存资质专业和证照实例关联信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstMajorId", value = "关联关系Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "certinstId", value = "证照实例ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "majorId", value = "专业ID", required = true, dataType = "String")
    })
    @RequestMapping("/saveAeaImCertinstMajor.do")
    public ResultForm saveAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaImCertinstMajor);
        }

        if(aeaImCertinstMajor.getCertinstMajorId()!=null&&!"".equals(aeaImCertinstMajor.getCertinstMajorId())){
            aeaImCertinstMajorService.updateAeaImCertinstMajor(aeaImCertinstMajor);
        }else{
        if(aeaImCertinstMajor.getCertinstMajorId()==null||"".equals(aeaImCertinstMajor.getCertinstMajorId()))
            aeaImCertinstMajor.setCertinstMajorId(UUID.randomUUID().toString());
            aeaImCertinstMajor.setCreater(SecurityContext.getCurrentUserName());
            aeaImCertinstMajor.setCreateTime(new Date());
            aeaImCertinstMajor.setIsDelete("0");
            aeaImCertinstMajorService.saveAeaImCertinstMajor(aeaImCertinstMajor);
        }

        return  new ContentResultForm<AeaImCertinstMajor>(true,aeaImCertinstMajor);
    }

    @ApiOperation(value = "删除关联信息", notes = "删除关联信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstMajorId", value = "关联信息ID", required = true, dataType = "String"),
    })
    @RequestMapping("/deleteAeaImCertinstMajorById.do")
    public ResultForm deleteAeaImCertinstMajorById(String certinstMajorId) throws Exception{
        logger.debug("删除Form对象，对象id为：{}", certinstMajorId);
        if(certinstMajorId!=null)
            aeaImCertinstMajorService.deleteAeaImCertinstMajorById(certinstMajorId);
        return new ResultForm(true);
    }


}
