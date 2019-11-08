package com.augurit.aplanmis.supermarket.serviceMatter.controller;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.common.vo.ServiceMatterVo;
import com.augurit.aplanmis.supermarket.serviceMatter.service.ServiceMatterPublishService;
import com.augurit.aplanmis.supermarket.vo.ServiceQualVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/4/004 9:19
 */

@Api(description = "中介服务事项发布接口")
@RequestMapping("/aea/supermarket/serviceMatter")
@RestController
public class ServiceMatterController {

    private static final Logger log = LoggerFactory.getLogger(ServiceMatterController.class);
    @Autowired
    private UploaderFactory uploaderFactory;

    @Autowired
    ServiceMatterPublishService serviceMatterPublishService;


    @ApiOperation(value = "获取服务发布相关页面数据信息", notes = "获取服务发布相关页面数据信息")
    @GetMapping("/getBasicInfo")
    public ResultForm getBaseInfo() {
        try {
            JSONObject basicInfo = serviceMatterPublishService.getBasicInfo();
            return new ContentResultForm(true, basicInfo);
        } catch (Exception e) {
            log.debug("获取服务发布相关页面数据信息失败！" + e.getMessage());
            return new ContentResultForm(false);
        }
    }

    @ApiOperation(value = "根据单位id和服务id获取从业人员信息", notes = "根据单位id和服务id获取从业人员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitInfoId", value = "单位id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "serviceId", value = "服务id", required = true, dataType = "String")
    })
    @GetMapping("/getLinkManList")
    public ResultForm getLinkManList(String unitInfoId,String serviceId) {
        ContentResultForm<Object> resultForm = new ContentResultForm<>(false);
        if(StringUtils.isNotBlank(unitInfoId) && StringUtils.isNotBlank(unitInfoId)){
            try {
                List<AeaLinkmanInfo> linkmanInfos = serviceMatterPublishService.getLinkManList(unitInfoId,serviceId);
                resultForm.setSuccess(true);
                resultForm.setContent(linkmanInfos);
            } catch (Exception e) {
                log.debug("获取从业人员信息出错：" + e.getMessage());
                resultForm.setMessage("获取从业人员信息出错：" + e.getMessage());
                return resultForm;
            }
        }else{
            resultForm.setMessage("单位id或服务id参数缺少");
        }
        return resultForm;
    }


    @ApiOperation(value = "根据中介服务事项id获取中介服务事项数据信息", notes = "分页显示中介服务名称和对应部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "中介服务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "当前页码", dataType = "Integer", defaultValue = "10"),
    })
    @GetMapping("/getServiceMatter")
    public ResultForm getServiceMatter(String serviceId, int pageSize, int pageNum) {
        try {
            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AeaItemBasic> serviceMatterVos = serviceMatterPublishService.listItemBasicByServiceId(serviceId, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(new PageInfo(serviceMatterVos)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false);
        }
    }



    @ApiOperation(value = "根据中介服务事项id获取中介服务事项数据信息", notes = "分页显示中介服务名称和对应部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "中介服务ID", required = true, dataType = "String"),
    })
    @GetMapping("/getServiceMatterNoPage")
    public ResultForm getServiceMatterNoPage(String serviceId) {
        try {
            List<AeaItemBasic> serviceMatterVos = serviceMatterPublishService.listItemBasicByServiceIdNoPage(serviceId);
            return new ContentResultForm(true, serviceMatterVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false);
        }
    }


    @ApiOperation(value = "根据中介服务事项id获取证照实例类型列表", notes = "获取证照实例类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "中介服务ID", required = true, dataType = "String"),
    })
    @GetMapping("/getCertList")
    public ResultForm getCert(String serviceId) throws Exception {
        try {
            List<AeaCert> certList = serviceMatterPublishService.getCertListByserviceId(serviceId);
            return new ContentResultForm<>(true, certList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false);
        }
    }


    /**
     * 删除证照实例
     *
     * @param certinstId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据证照实例id删除证照实例", notes = "删除证照实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstId", value = "证照实例ID", required = true, dataType = "String"),
    })
    @GetMapping("/deleteCertinst")
    public ResultForm deleteCertinstById(String certinstId) {
        try {
            serviceMatterPublishService.deleteCertificateInfo(certinstId);
            return new ContentResultForm<>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false);
        }
    }

    @ApiOperation(value = "根据中介服务id获取专业树和资质等级", notes = "获取获取专业树VO")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "中介服务ID", required = true, dataType = "String"),
    })
    @PostMapping("/getMajorTreeByServiceId.do")
    public List<ServiceQualVo> getMajorTree(String serviceId) {
        try {
            if (StringUtils.isBlank(serviceId)) return null;
            return serviceMatterPublishService.getMajorTreeNode(serviceId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存证照实例
     *
     * @param certinstBVo
     * @return
     * @throws Exception
     */
    @PostMapping("/saveCertificate")
    @ApiOperation(value = "上传证照实例并保存", notes = "上传证照实例并保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstId", value = "证照实例id,编辑时必传", required = false, dataType = "String"),
            @ApiImplicitParam(name = "certId", value = "证照定义（类型）ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "certinstCode", value = "证照编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "certinstName", value = "证照名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "termStart", value = "有限期限——起", required = true, dataType = "String"),
            @ApiImplicitParam(name = "termEnd", value = "有限期限——止", required = true, dataType = "String"),
            @ApiImplicitParam(name = "qualLevelId", value = "等级id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "majorId", value = "专业id集合", required = true, dataType = "List"),
            @ApiImplicitParam(name = "managementScope", value = "业务范围", required = true, dataType = "String"),
            @ApiImplicitParam(name = "file", value = "上传附件列表", required = true, dataType = "MultipartFile"),
    })

    public ResultForm saveCertificate(AeaHiCertinstBVo certinstBVo, HttpServletRequest request) {
        try {
            AeaHiCertinstBVo certinstBVos = new AeaHiCertinstBVo();
            if (Strings.isNullOrEmpty(certinstBVo.getCertinstId())) {
                //前端要求保存后返回证照列表给它
                certinstBVos = serviceMatterPublishService.saveCertificateInfo(certinstBVo, request);
            } else {
                certinstBVos = serviceMatterPublishService.updateAeaHicertinstBvo(certinstBVo, request);
            }
            return new ContentResultForm(true, certinstBVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

    @ApiOperation(value = "根据证照实例附件id删除证照实例关联的照片", notes = "用于修改证照实例时删除证照附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailId", value = "附件detailID", required = true, dataType = "String"),
    })
    @GetMapping("/deleteCertinstAtt")
    public ResultForm deleteCertinstAtt(String detailId) throws Exception {
        try {
            serviceMatterPublishService.deleteCertinstAtt(detailId);
            return new ContentResultForm<>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false);
        }
    }

    @ApiOperation(value = "分页展示发布的中介服务", notes = "中介服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "中介服务id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer"),
    })
    @PostMapping("/listServiceMatter")
    public ResultForm listServiceMatter(ServiceMatterVo serviceMatterVo, int pageSize, int pageNum) throws Exception {
        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
        //TODO 只返回测试数据给前端，可以暂时注释下一行
        List<ServiceMatterVo> serviceMatterVos = serviceMatterPublishService.listServiceMatter(serviceMatterVo, page);
        /*List<ServiceMatterVo> serviceMatterVos = new ArrayList<>();
        ServiceMatterVo matterVo = new ServiceMatterVo();
        matterVo.setUnitServiceId("1");
        matterVo.setServiceName("测试中介服务");
        matterVo.setServiceId("s11");
        List<AeaLinkmanInfo> linkmanInfos = new ArrayList<>();
        AeaLinkmanInfo linkmanInfo = new AeaLinkmanInfo();
        linkmanInfo.setLinkmanInfoId("linkmanId1111");
        linkmanInfo.setLinkmanName("ceshi从业人员");
        AeaLinkmanInfo linkmanInfo2 = new AeaLinkmanInfo();
        linkmanInfo2.setLinkmanInfoId("linkmanId1112");
        linkmanInfo2.setLinkmanName("ceshi从业人员2");
        linkmanInfos.add(linkmanInfo);
        linkmanInfos.add(linkmanInfo2);
        matterVo.setLinkmanInfo(linkmanInfos);
        matterVo.setAuditFlag("0");
        matterVo.setIsDelete("0");
        matterVo.setServiceContent("xxx服务承诺");
        matterVo.setFeeReference("收费参考");
        matterVo.setManagementScope("jing经营范围");
        matterVo.setTermStart(new Date());
        matterVo.setTermEnd(new Date());

        List<AeaHiCertinstBVo> hiCertinstBVos = new ArrayList<>();
        AeaHiCertinstBVo certinstBVo = new AeaHiCertinstBVo();
        certinstBVo.setCertinstId("c111");
        certinstBVo.setCertId("证照实例类型id111");
        certinstBVo.setAttLinkId("证照连接地址xxxx");
        certinstBVo.setCertinstCode("bm121212");
        certinstBVo.setCertinstName("xxx证照");
        certinstBVo.setQualId("q1");
        certinstBVo.setQualName("工程咨询");
        certinstBVo.setQualLevelId("l1");
        certinstBVo.setQualLevelName("甲级");
        certinstBVo.setTermStart(new Date());
        certinstBVo.setTermEnd(new Date());

        List<BscAttFileAndDir> fileAndDirs = new ArrayList<>();
        BscAttFileAndDir file = new BscAttFileAndDir();
        file.setFileId("filexxxId");
        file.setFileName("xxx艳照.jpg");
        BscAttDetail attDetail = new BscAttDetail();
        attDetail.setDetailId("detail..id");
        file.setBscAttDetail(attDetail);
        fileAndDirs.add(file);
        certinstBVo.setBscAttFileAndDirs(fileAndDirs);
       // certinstBVo.setCertinstDetail(m);

        List<String> major = Arrays.asList("m1","m2","m3");
        certinstBVo.setMajorId(major);
        hiCertinstBVos.add(certinstBVo);
        matterVo.setCertinstBVos(hiCertinstBVos);
        serviceMatterVos.add(matterVo);*/
        return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(serviceMatterVos)));
    }

    /**
     * 保存服务事项
     *
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "保存发布的中介机构服务", notes = "保存中介机构服务")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "json", value = "中介机构服务JSON{serviceId：中介服务id，unitInfoId：所属单位ID，feeReference：收费参考，serviceContent：服务承诺，linkmanInfo：从业人员对象数组，certinstBVos：证照实例数组}", required = true, dataType = "String"),
    })
    @PostMapping("/saveServiceMatter")
    public ResultForm saveServiceMatter(String json) {

        try {
            if (StringUtils.isBlank(json)) return new ResultForm(false);
            ServiceMatterVo serviceMatterVo = JSONObject.parseObject(json, ServiceMatterVo.class);
            if (Strings.isNullOrEmpty(serviceMatterVo.getUnitServiceId())) {
                serviceMatterVo.setUnitServiceId(UUID.randomUUID().toString());
                serviceMatterPublishService.saveServiceMatter(serviceMatterVo);
            } else {
                serviceMatterPublishService.updateServiceMatter(serviceMatterVo);
            }
            return new ResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

    @ApiOperation(value = "根据中介服务id获取从业人员列表", notes = "根据中介服务id从业人员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "中介服务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true, dataType = "String")
    })
    @GetMapping("/listLinkmanByServiceUnitInfoId")
    public ResultForm listLinkmanByServiceUnitInfoId(String serviceId, String unitInfoId) {
        try {
            if (StringUtils.isBlank(serviceId) || StringUtils.isBlank(unitInfoId)) {
                return new ResultForm(false, "参数为空");
            }

            List<AeaLinkmanInfo> aeaLinkmanInfoList = serviceMatterPublishService.listLinkmanByServiceUnitInfoId(serviceId, unitInfoId);
            return new ContentResultForm(true, aeaLinkmanInfoList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }



}
