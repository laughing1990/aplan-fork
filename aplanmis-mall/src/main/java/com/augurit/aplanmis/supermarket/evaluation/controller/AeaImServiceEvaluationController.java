package com.augurit.aplanmis.supermarket.evaluation.controller;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaImServiceEvaluation;
import com.augurit.aplanmis.common.utils.BusinessUtils;
import com.augurit.aplanmis.supermarket.evaluation.service.AeaImServiceEvaluationService;
import com.augurit.aplanmis.supermarket.utils.ContentRestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
* -服务评价控制类
*/
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(description = "服务评价相关接口")
@RequestMapping("/supermarket/evaluation")
public class AeaImServiceEvaluationController {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceEvaluationController.class);

    @Autowired
    private AeaImServiceEvaluationService aeaImServiceEvaluationService;

    @ApiOperation(value = "根据id获取服务评价信息", notes = "根据id获取服务评价信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceEvaluationId", value = "服务评价id", required = true)
    })
    @PostMapping("/getAeaImServiceEvaluation/{serviceEvaluationId}")
    public ContentRestResult<AeaImServiceEvaluation> getAeaImServiceEvaluation(@PathVariable("serviceEvaluationId") String serviceEvaluationId) throws Exception {
        if (StringUtils.isNotBlank(serviceEvaluationId)) {
            AeaImServiceEvaluation aeaImServiceEvaluation = aeaImServiceEvaluationService.getAeaImServiceEvaluationById(serviceEvaluationId);

            if (aeaImServiceEvaluation != null) {
                new ContentRestResult(true, aeaImServiceEvaluation);
            }
        }
        return new ContentRestResult(false);
    }

    @ApiOperation(value = "上传服务评价", notes = "上传服务评价", httpMethod = "POST")
    @PostMapping("/postAeaImServiceEvaluation")
    public ContentRestResult postAeaImServiceEvaluation(@Valid AeaImServiceEvaluation aeaImServiceEvaluation, BindingResult bindingResult) throws Exception {
        String errorMsg = BusinessUtils.checkBindingResult(bindingResult);

        if (StringUtils.isNotBlank(errorMsg)) {
            return new ContentRestResult(false, null, errorMsg);
        }

        try {
            if(StringUtils.isNotBlank(aeaImServiceEvaluation.getServiceEvaluationId())){
                aeaImServiceEvaluationService.updateAeaImServiceEvaluation(aeaImServiceEvaluation);
            }else{
                aeaImServiceEvaluationService.saveAeaImServiceEvaluation(aeaImServiceEvaluation);
            }

            return new ContentRestResult(true, null, aeaImServiceEvaluation.getServiceEvaluationId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult(false, null, e.getMessage());
        }
    }

    @ApiOperation(value = "根据id删除服务评价", notes = "根据id删除服务评价", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceEvaluationId", value = "服务评价id", required = true)
    })
    @PostMapping("/deleteAeaImServiceEvaluation/{serviceEvaluationId}")
    public ResultForm deleteAeaImServiceEvaluationById(@PathVariable("serviceEvaluationId") String serviceEvaluationId) throws Exception{
        if (StringUtils.isNotBlank(serviceEvaluationId)) {
            aeaImServiceEvaluationService.deleteAeaImServiceEvaluationById(serviceEvaluationId);
        }
        return new ResultForm(true);
    }

}
