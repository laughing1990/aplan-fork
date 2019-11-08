package com.augurit.aplanmis.supermarket.cert.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.supermarket.cert.service.AeaCertService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Api(description = "证照定义操作接口")
@RestController
@RequestMapping("/supermarket/cert")
public class AeaCertController {

private static Logger logger = LoggerFactory.getLogger(AeaCertController.class);

    @Autowired
    private AeaCertService aeaCertService;


    @RequestMapping("/indexAeaCert.do")
    public ModelAndView indexAeaCert(AeaCert aeaCert, String infoType){
        return new ModelAndView("aea/aea_cert_index");
    }

    @RequestMapping("/listAeaCert.do")
    public PageInfo<AeaCert> listAeaCert(AeaCert aeaCert, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCert);
        return aeaCertService.listAeaCert(aeaCert,page);
    }

    @ApiOperation(value = "获取证照定义信息", notes = "获取证照定义信息。")
    @RequestMapping("/getAeaCertInfo.do")
    public List<AeaCert> getAeaCertInfo(AeaCert aeaCert) throws Exception {

            return aeaCertService.listAeaCert(aeaCert);

    }

    @RequestMapping("/updateAeaCert.do")
        public ResultForm updateAeaCert(AeaCert aeaCert) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaCert);
        aeaCertService.updateAeaCert(aeaCert);
        return new ResultForm(true);
    }


    /**
    * 保存或编辑电子证照定义表
    * @param aeaCert 电子证照定义表
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaCert.do")
    public ResultForm saveAeaCert(AeaCert aeaCert, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存电子证照定义表Form对象出错");
            throw new InvalidParameterException(aeaCert);
        }

        if(aeaCert.getCertId()!=null&&!"".equals(aeaCert.getCertId())){
            aeaCertService.updateAeaCert(aeaCert);
        }else{
        if(aeaCert.getCertId()==null||"".equals(aeaCert.getCertId()))
            aeaCert.setCertId(UUID.randomUUID().toString());
            aeaCertService.saveAeaCert(aeaCert);
        }

        return  new ContentResultForm<AeaCert>(true,aeaCert);
    }

    @RequestMapping("/deleteAeaCertById.do")
    public ResultForm deleteAeaCertById(String id) throws Exception{
        logger.debug("删除电子证照定义表Form对象，对象id为：{}", id);
        if(id!=null)
            aeaCertService.deleteAeaCertById(id);
        return new ResultForm(true);
    }

}
