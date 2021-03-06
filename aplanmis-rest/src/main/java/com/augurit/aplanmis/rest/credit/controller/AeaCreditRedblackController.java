package com.augurit.aplanmis.rest.credit.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaCreditRedblack;
import com.augurit.aplanmis.common.dto.AeaCreditSummaryAllDto;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditRedblackService;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditSummaryService;
import com.augurit.aplanmis.rest.credit.vo.CreditRedblackVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 信用管理-红黑名单管理-Controller 页面控制转发类
 */
@RestController
@RequestMapping("rest/user/credit/redblack")
@Api(value = "信用管理接口", tags = "法人空间 --> 信用管理接口")
public class AeaCreditRedblackController {

    private static Logger logger = LoggerFactory.getLogger(AeaCreditRedblackController.class);

    @Autowired
    private AeaCreditRedblackService aeaCreditRedblackService;

    @Autowired
    private AeaCreditSummaryService summaryService;

    @ApiOperation(value = "根据企业统一社会信用代码或者个人身份证号查询是否属于黑名单及信用记录", notes = "根据企业统一社会信用代码或者个人身份证号查询是否属于黑名单及信用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizType", value = "业务类型,如: u 企业 l 联系人", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bizCode", value = "企业统一社会信用代码或者个人身份证号", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/getPersonOrUnitBlackAndCreditListByBizCode", method = {RequestMethod.GET})
    public ResultForm listPersonOrUnitBlackByBizId(String bizType, String bizCode) {

        if (StringUtils.isBlank(bizType)) {
            throw new InvalidParameterException("参数bizType为空!");
        }
        if (StringUtils.isBlank(bizCode)) {
            throw new InvalidParameterException("参数bizCode为空!");
        }
        List<AeaCreditRedblack> list = aeaCreditRedblackService.listPersonOrUnitBlackByBizCode(bizType, bizCode);
        CreditRedblackVo creditRedblackVo = new CreditRedblackVo();
        if (list.size() > 0) BeanUtils.copyProperties(list.get(0), creditRedblackVo);
        try {
            List<AeaCreditSummaryAllDto> creditList = summaryService.listCreditSummaryDetailByByBizCode(bizType, bizCode);
            creditRedblackVo.setCreditList(creditList.size() > 0 ? creditList : new ArrayList<>());
        } catch (Exception e) {
            logger.error("getPersonOrUnitBlackAndCreditListByBizCode:" + e.getMessage());
            return new ResultForm(false, "获取信用记录失败！," + e.getMessage());
        }
        return new ContentResultForm<CreditRedblackVo>(true, creditRedblackVo, "查询成功！");
    }

}
