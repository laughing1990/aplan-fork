package com.augurit.aplanmis.admin.market.service.controller;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.admin.market.service.service.AeaImServiceExamineService;
import com.augurit.aplanmis.common.domain.AeaImUnitService;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.common.vo.ServiceMatterVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * -中介机构服务审批控制类
 */
@RestController
@RequestMapping("/supermarket/serviceExamine")
@Api(value = "中介超市管理-企业服务", tags = "中介超市管理-企业服务")
@Slf4j
public class AeaImServiceExamineController {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceExamineController.class);

    @Autowired
    private AeaImServiceExamineService serviceExamineService;

    @RequestMapping("/index.do")
    public ModelAndView indexAeaImServiceType(AeaImUnitService unitService) {
        return new ModelAndView("ui-jsp/supermarket_manage/service/service_examine");
    }

    @RequestMapping("/index.html")
    public ModelAndView indexAeaImServiceTypeHtml(AeaImUnitService unitService) {
        return new ModelAndView("admin/supermarket/service/service_examine");
    }

    /**
     * 分页显示机构中介服务
     *
     * @param serviceMatterVo
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/listServiceMatter.do")
    public EasyuiPageInfo<ServiceMatterVo> listAeaImServiceQual(ServiceMatterVo serviceMatterVo, Page page) throws Exception {
        try {
            logger.debug("分页查询，过滤条件为{}，查询关键字为{}", serviceMatterVo);
            PageInfo<ServiceMatterVo> pageInfo = serviceExamineService.listServiceMatter(serviceMatterVo, page);
            return PageHelper.toEasyuiPageInfo(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return PageHelper.toEasyuiPageInfo(new PageInfo());
        }
    }

    @GetMapping("/listServiceMatter")
    public ResultForm listAeaImServiceQualJson(ServiceMatterVo serviceMatterVo, Page page) throws Exception {
        try {
            logger.debug("分页查询，过滤条件为{}，查询关键字为{}", serviceMatterVo);
            PageInfo<ServiceMatterVo> pageInfo = serviceExamineService.listServiceMatter(serviceMatterVo, page);
            return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(pageInfo), "success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo()), "success");
        }
    }


    /**
     * 根据机构中介服务id查询详情
     *
     * @param unitServiceId
     * @return
     */
    @RequestMapping("/getServiceMatter.do")
    public ServiceMatterVo getServiceMatterByUnitServiceId(String unitServiceId) {
        try {
            ServiceMatterVo serviceMatterVo = serviceExamineService.getServiceMatterServiceByUnitServiceId(unitServiceId);

            return serviceMatterVo;
        } catch (Exception e) {
            return new ServiceMatterVo();
        }
    }

    /**
     * 根据中介服务id找出其所属中介服务事项
     *
     * @param serviceId
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("/listServiceItemServiceid.do")
    public EasyuiPageInfo<AeaItemBasic> listServiceItemServiceid(String serviceId, Page page) {
        try {
            logger.debug("分页查询，过滤条件为{}，查询关键字为{}", serviceId);
            PageInfo<AeaItemBasic> pageInfo = serviceExamineService.listServiceItemServiceid(serviceId, page);
            return PageHelper.toEasyuiPageInfo(pageInfo);
        } catch (Exception e) {
            logger.error("查询所属中介服务事项出差！", e);
            return PageHelper.toEasyuiPageInfo(new PageInfo<>());
        }
    }

    /**
     * 审核中介机构服务
     *
     * @param serviceMatterVo
     * @return
     * @throws Exception
     */
    @RequestMapping("/examineUnitService.do")
    public ResultForm examineUnitService(ServiceMatterVo serviceMatterVo) throws Exception {
        serviceExamineService.examineService(serviceMatterVo);
        return new ResultForm(true);

    }

    @RequestMapping("/unitServiceDetailIndex.do")
    public ModelAndView indexunitServiceDetail(String unitServiceId) {
        ModelAndView modelAndView = new ModelAndView("ui-jsp/supermarket_manage/service/service_examine_detail");
        modelAndView.addObject("unitServiceId", unitServiceId);
        return modelAndView;
    }

    @RequestMapping("/unitServiceDetailIndex.html")
    public ModelAndView indexunitServiceDetailHtml(String unitServiceId) {
        ModelAndView modelAndView = new ModelAndView("admin/supermarket/service/service_examine_detail");
        modelAndView.addObject("unitServiceId", unitServiceId);
        return modelAndView;
    }
    //==以上是用到的方法


    @GetMapping("queryUnitSeriveDetail")
    @ApiOperation("查询机构审核服务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitServiceId", value = "单位服务ID", required = true)
    })
    public ResultForm queryUnitServiceDetail(String unitServiceId) throws Exception {
        ServiceMatterVo serviceMatterVo = serviceExamineService.getServiceMatterServiceByUnitServiceId(unitServiceId);
        return new ContentResultForm<>(true, serviceMatterVo, "success");
    }

    @RequestMapping("/listCertinstByUnitServiceid.do")
    public EasyuiPageInfo<AeaHiCertinstBVo> listCertinstByUnitServiceid(String unitServiceId, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", unitServiceId);
        PageInfo<AeaHiCertinstBVo> pageInfo = serviceExamineService.listCertinstByUnitServiceid(unitServiceId, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getCertinstById.do")
    public AeaHiCertinstBVo getCertinstById(String certinstId) throws Exception {
        AeaHiCertinstBVo certinstDetail = serviceExamineService.getCertinstById(certinstId);
        //页面显示的测试数据
        /*if (certinstDetail.getCertinstDetail() == null || certinstDetail.getCertinstDetail().size() == 0) {
            List<BscAttFileAndDir> dirs = new ArrayList<>();
            BscAttFileAndDir att = new BscAttFileAndDir();
            att.setFileName("测试图片.jpg");
            dirs.add(att);
            certinstDetail.setCertinstDetail(dirs);

        }*/
        return certinstDetail;
    }

    @RequestMapping("/getMajorTreeByServiceId.do")
    public Map getMajorTreeByServiceId(String serviceId, String certinstId) {
        try {
            if (StringUtils.isBlank(serviceId)) return null;
            return serviceExamineService.getMajorTreeNode(serviceId, certinstId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
