package com.augurit.aplanmis.mall.cert.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaCertType;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.mapper.AeaCertMapper;
import com.augurit.aplanmis.common.mapper.AeaCertTypeMapper;
import com.augurit.aplanmis.common.mapper.AeaHiCertinstMapper;
import com.augurit.aplanmis.common.service.admin.cert.AeaCertAdminService;
import com.augurit.aplanmis.common.service.admin.cert.AeaCertTypeAdminService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.cert.service.AeaCertMallService;
import com.augurit.aplanmis.mall.cert.vo.AeaCertVo;
import com.augurit.aplanmis.mall.cert.vo.BindForminstVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
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
import java.util.List;


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
    @Autowired
    private AeaCertMapper aeaCertMapper;
    @Autowired
    private AeaCertTypeMapper aeaCertTypeMapper;

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
            logger.debug(e.getMessage(),e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

    @GetMapping("/getCertTypes")
    @ApiOperation(value = "获取系统证照类型列表")
    public List<AeaCertType> getCertTypes(){
        AeaCertType query = new AeaCertType();
        query.setRootOrgId(SecurityContext.getCurrentOrgId());
        query.setIsActive("1");
        List<AeaCertType> list = aeaCertTypeMapper.listAeaCertType(query);
        return list;
    }

    @GetMapping("/getCertListByType")
    @ApiOperation(value = "根据证照类型ID获取系统证照定义列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certTypeId", value = "证照类型ID", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "certHolder", value = "证照所属，c表示企业，u表示个人", required = false, dataType = "String", paramType = "query"),
    })
    public List<AeaCert> getCertListByType(String certTypeId,String certHolder){
        Assert.hasText(certTypeId, "certTypeId must not null.");
        AeaCert query=new AeaCert();
        query.setRootOrgId(SecurityContext.getCurrentOrgId());
        query.setCertTypeId(certTypeId);
        query.setCertHolder(StringUtils.isNotBlank(certHolder)?certHolder:null);
        List<AeaCert> list = aeaCertMapper.listAeaCert(query);
        return list;
    }

    @GetMapping("/getCertTypesAndCertList")
    @ApiOperation(value = "获取系统证照类型及其证照定义列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certHolder", value = "证照所属，c表示企业，u表示个人", required = false, dataType = "String", paramType = "query"),
    })
    public List<AeaCertVo> getCertTypesAndCertList(String certHolder){
        List<AeaCertVo> list = aeaCertService.getCertTypesAndCertList(certHolder);
        return list;
    }

    @GetMapping("/getCertintListByCertHolder")
    @ApiOperation(value = "获取系统证照实例列表（分页）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certHolder", value = "证照所属，c表示企业，u表示个人，p表示项目", required = true , dataType = "String" ,paramType = "query"),
            @ApiImplicitParam(name = "keyword", value = "关键字(证照名称/证照类型)", required = false, dataType = "String" ,paramType = "query"),
            @ApiImplicitParam(name = "pageNum",value = "页面数量",required = true,dataType = "string"),
            @ApiImplicitParam(name = "pageSize",value = "页面页数",required = true,dataType = "string")
    })
    public ContentResultForm<AeaHiCertinst> getCertintListByCertHolder(String certHolder, String keyword,int pageNum, int pageSize,HttpServletRequest request){
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
        try{
            Assert.hasText(certHolder, "certHolder must not null.");
            List<AeaHiCertinst> list = aeaCertService.getCertintListByCertHolder(certHolder,keyword,pageNum,pageSize,loginVo);
            return new ContentResultForm(true,new PageInfo<>(list),"query success！");
        }catch (Exception e){
            logger.debug(e.getMessage(),e);
            return new ContentResultForm(false,null,"query error！");
        }
    }



}
