package com.augurit.aplanmis.mall.cert.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.cert.service.AeaCertMallService;
import com.augurit.aplanmis.mall.cert.vo.BindForminstVo;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author QinJianping
 * @date 2019/11/15  13:58
 * @desc
 **/
@Api(description = "电子证照",tags={"申报 --> 电子证照"})
@RestController
@RequestMapping("/aea/cert")
public class AeaCertMallController {

    private static Logger logger = LoggerFactory.getLogger(AeaCertMallController.class);

    @Autowired
    private AeaCertMallService aeaCertService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;


    @ApiOperation(value = "通过查询条件获取电子证照库数据", notes = "通过查询条件获取电子证照库数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemVerIds", value = "事项版本IDs", required = true , dataType = "String" ,paramType = "query"),
            @ApiImplicitParam(name = "identityNumber", value = "申办证件号码", required = true , dataType = "String" ,paramType = "query"),
    })
    @GetMapping("/getLicenseAuthRes")
    public ContentResultForm getLicenseAuthRes(String itemVerIds, String identityNumber,HttpServletRequest request) throws Exception {
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
        return new ContentResultForm(true,aeaCertService.getLicenseAuthRes(itemVerIds, identityNumber,loginVo),"success");
    }

    @ApiOperation(value = "通过电子证照编码获取证照显示地址", notes = "通过电子证照编码获取证照显示地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authCode", value = "证照编码", required = true , dataType = "String" ,paramType = "query"),
    })
    @GetMapping("/getViewLicenseURL")
    public ContentResultForm getViewLicenseURL(String authCode) throws Exception {
        return new ContentResultForm(true,aeaCertService.getViewLicenseURL(authCode),"success");
    }


    @PostMapping("/bind/cert")
    @ApiOperation(value = "关联电子证照材料")
    @ApiImplicitParam(value = "电子证照")
    public ContentResultForm<AeaHiCertinst> bindCertinst(@RequestBody AeaHiCertinst aeaHiCertinst,HttpServletRequest request) {
        try {
            LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
            if (StringUtils.isNotBlank(aeaHiCertinst.getMatId())) {
                aeaHiCertinst = aeaHiItemMatinstService.bindCertinst(aeaHiCertinst, loginInfoVo.getUserId());
                Assert.hasText(aeaHiCertinst.getCertinstId(), "certinstId is null");
                return new ContentResultForm<>(true, aeaHiCertinst, "Bind success");
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(),e);
            return new ContentResultForm<>(false, null, "关联证照库材料失败: " + e.getMessage());
        }
        return new ContentResultForm<>(false, null, "关联证照库材料失败: ");
    }

    @PostMapping("/unbind/cert")
    @ApiImplicitParam(value = "证照材料实例id", name = "matinstId")
    @ApiOperation(value = "解除关联电子证照材料")
    public ResultForm bindCertinst(String matinstId) {
        Assert.hasText(matinstId, "证照材料实例id不能为空");

        try {
            aeaHiItemMatinstService.unbindCertinst(matinstId);
        } catch (Exception e) {
            logger.debug(e.getMessage(),e);
            return new ResultForm(false, "解绑失败: " + e.getMessage());
        }
        return new ResultForm(true, "解绑成功");
    }

    @PostMapping("/bind/form")
    @ApiOperation(value = "关联表单材料")
    public ContentResultForm<AeaHiItemMatinst> bindForminst(@RequestBody BindForminstVo bindForminstVo) {
        Assert.hasText(bindForminstVo.getMatId(), "matId must not null.");
        Assert.hasText(bindForminstVo.getStoForminstId(), "stoForminstId must not null.");

        try {
            AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
            BeanUtils.copyProperties(bindForminstVo, aeaHiItemMatinst);
            aeaHiItemMatinst = aeaHiItemMatinstService.bindForminst(aeaHiItemMatinst, SecurityContext.getCurrentUserId());
            return new ContentResultForm<>(true, aeaHiItemMatinst, "bind forminst success");
        } catch (Exception e) {
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }
}
