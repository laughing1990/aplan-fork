package com.augurit.aplanmis.front.agency.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.window.AeaProjApplyAgentService;
import com.augurit.aplanmis.common.vo.agency.AgencyDetailVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 项目代办申请表-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/proj/apply/agent")
public class AeaProjApplyAgentController {

private static Logger logger = LoggerFactory.getLogger(AeaProjApplyAgentController.class);

    @Autowired
    private AeaProjApplyAgentService aeaProjApplyAgentService;


    @GetMapping("/getAgencyDetail")
    @ApiOperation(value = "项目代办 --> 代办详情", notes = "项目代办 --> 代办详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyAgentId",value = "代办申请ID",required = true,dataType = "String")
    })
    public ResultForm getAgencyDetail(String applyAgentId){
        logger.debug("查询代办详情，查询关键字为{}", applyAgentId);
        ContentResultForm resultForm = new ContentResultForm(false);
        try {
            if(StringUtils.isNotBlank(applyAgentId)){
                AgencyDetailVo vo = aeaProjApplyAgentService.getAgencyDetail(applyAgentId);
                resultForm.setSuccess(true);
                resultForm.setContent(vo);
                resultForm.setMessage("查询成功。");
            }else {
                resultForm.setMessage("代办申请ID参数不能为空。");
            }
            return resultForm;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

}
