package com.augurit.aplanmis.mall.credit.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaCreditRedblack;
import com.augurit.aplanmis.common.dto.AeaCreditSummaryAllDto;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditRedblackService;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditSummaryService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.credit.vo.CreditRedblackVo;
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

import javax.servlet.http.HttpServletRequest;
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
            @ApiImplicitParam(name = "bizType", value = "业务类型,如: u 企业 l 联系人", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "bizCode", value = "企业统一社会信用代码或者个人身份证号", dataType = "String", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/getPersonOrUnitBlackAndCreditListByBizCode", method = {RequestMethod.GET})
    public ResultForm getPersonOrUnitBlackAndCreditListByBizCode(String bizType, String bizCode, HttpServletRequest request) {
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
        if (loginVo == null) return new ResultForm(false, "未登录，获取信用记录失败！");
        if (StringUtils.isBlank(bizCode) || StringUtils.isBlank(bizType)) {
            if (StringUtils.isNotBlank(loginVo.getUnitId())) {//企业用户或者委托人用户
                bizType = "u";
                bizCode = loginVo.getUnifiedSocialCreditCode();
            } else {//个人用户
                bizType = "l";
                bizCode = loginVo.getIdCard();
            }
        }
        if ("1".equals(loginVo.getIsPersonAccount())){//个人时，由于身份id脱敏，用session的idCard
            bizType = "l";
            bizCode = loginVo.getIdCard();
        }
        CreditRedblackVo creditRedblackVo = new CreditRedblackVo();
        try {
            List<AeaCreditRedblack> list = aeaCreditRedblackService.listPersonOrUnitBlackByBizCode(bizType, bizCode);
            if (list.size() > 0) BeanUtils.copyProperties(list.get(0), creditRedblackVo);
            List<AeaCreditSummaryAllDto> creditList = summaryService.listCreditSummaryDetailByByBizCode(bizType, bizCode);
            Boolean flag=false;
            if(creditList.size()>0){
                for (AeaCreditSummaryAllDto dto:creditList){
                    if(dto.getSummaries()!=null && dto.getSummaries().size()>0){
                        flag=true;
                        break;
                    }
                }
            }
            creditRedblackVo.setCreditList((creditList.size() > 0 && flag)? creditList : new ArrayList<>());
        } catch (Exception e) {
            logger.error("getPersonOrUnitBlackAndCreditListByBizCode:" + e.getMessage());
            return new ResultForm(false, "获取信用记录失败！," + e.getMessage());
        }
        return new ContentResultForm<CreditRedblackVo>(true, creditRedblackVo, "查询成功！");
    }


    @ApiOperation(value = "根据企业统一社会信用代码或者个人身份证号查询是否属于黑名单", notes = "根据企业统一社会信用代码或者个人身份证号查询是否属于黑名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizType", value = "业务类型,如: u 企业 l 联系人", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bizCode", value = "企业统一社会信用代码或者个人身份证号", dataType = "String", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/getPersonOrUnitBlackByBizCode", method = {RequestMethod.GET})
    public ResultForm getPersonOrUnitBlackByBizCode(String bizType, String bizCode, HttpServletRequest request) {
        if (StringUtils.isBlank(bizCode)) {
            LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
            bizCode = loginVo.getUnifiedSocialCreditCode();
            if (StringUtils.isBlank(bizCode)) return new ResultForm(false, "企业统一社会信用代码信息不完整！");
        }
        try {
            CreditRedblackVo creditRedblackVo = new CreditRedblackVo();
            List<AeaCreditRedblack> list = aeaCreditRedblackService.listPersonOrUnitBlackByBizCode(bizType, bizCode);
            if (list.size() > 0) BeanUtils.copyProperties(list.get(0), creditRedblackVo);
            return new ContentResultForm<CreditRedblackVo>(true, creditRedblackVo, "查询成功！");
        } catch (Exception e) {
            logger.error("getPersonOrUnitBlackByBizCode:" + e.getMessage());
            return new ResultForm(false, "获取是否黑名单失败！," + e.getMessage());
        }
    }

}
