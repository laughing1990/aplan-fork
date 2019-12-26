package com.augurit.aplanmis.mall.log.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.mall.log.service.RestAeaLogThemeItemChangeService;
import com.augurit.aplanmis.mall.log.vo.ApplyIteminstConfirmVo;
import com.augurit.aplanmis.mall.userCenter.controller.RestApplyCommonController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("rest/user/stage/apply")
@Api(value = "并联申报",tags = "并联申报 --> 事项确认接口")
public class AeaLogThemeItemChangeController {
    Logger logger= LoggerFactory.getLogger(RestApplyCommonController.class);

    @Autowired
    private RestAeaLogThemeItemChangeService restAeaLogThemeItemChangeService;


    @GetMapping("getApplyIteminstConfirmDetail/{applyinstId}")
    @ApiOperation(value = "并联申报 --> 查询事项确认接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "申请实例ID", name = "applyinstId", required = true, dataType = "string")
    })
    public ContentResultForm<ApplyIteminstConfirmVo> getApplyIteminstConfirmDetail(@PathVariable("applyinstId")String applyinstId){
        try {
            ApplyIteminstConfirmVo applyIteminstConfirmDetail = restAeaLogThemeItemChangeService.getApplyIteminstConfirmDetail(applyinstId);
            return new ContentResultForm<>(true,applyIteminstConfirmDetail);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","查询事项确认接口发生异常");
        }
    }

}
