package com.augurit.aplanmis.admin.market.service.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.aplanmis.admin.market.service.service.AeaImServiceService;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.mapper.AeaImServiceMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * -Controller 页面控制转发类
 */
@Api(description = "中介服务相关接口")
@RestController
@RequestMapping("/supermarket/service")
public class AeaImServiceController {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceController.class);

    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;
    @Autowired
    private AeaImServiceService aeaImServiceService;


    @RequestMapping("/indexAeaImService.do")
    public ModelAndView indexAeaImService(AeaImService aeaImService, String infoType) {
        return new ModelAndView("aea/im/service_index");
    }

    @RequestMapping("/listAeaImService.do")
    public EasyuiPageInfo<AeaImService> listAeaImService(AeaImService aeaImService, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImService);
        PageInfo<AeaImService> pageInfo = aeaImServiceService.listAeaImService(aeaImService, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    /**
     * 根据事项版本ID查询中介服务事项关联的服务
     *
     * @param itemVerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/listItemServiceNoPage.do")
    public List<AeaImService> listItemServiceNoPage(String itemVerId) throws Exception {
        logger.debug("不分页查询，过滤条件为{}", JsonUtils.toJson(itemVerId));
        List<AeaImService> listNoPage = aeaImServiceService.listAeaImServiceNoPageByItemVerId(itemVerId);
        return listNoPage;
    }

    @RequestMapping("/getAeaImService.do")
    public AeaImService getAeaImService(String id) throws Exception {
        if (id != null) {
            logger.debug("根据ID获取AeaImService对象，ID为：{}", id);
            return aeaImServiceService.getAeaImServiceById(id);
        } else {
            logger.debug("构建新的AeaImService对象");
            return new AeaImService();
        }
    }

    @RequestMapping("/updateAeaImService.do")
    public ResultForm updateAeaImService(AeaImService aeaImService) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaImService);
        aeaImServiceService.updateAeaImService(aeaImService);
        return new ResultForm(true);
    }

    /**
     * 保存或编辑
     *
     * @param aeaImService
     * @param result       校验对象
     * @return 返回结果对象 包含结果信息
     * @throws Exception
     */
    @RequestMapping("/saveAeaImService.do")
    public ResultForm saveAeaImService(AeaImService aeaImService, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaImService);
        }

        if (aeaImService.getServiceId() != null && !"".equals(aeaImService.getServiceId())) {
            aeaImServiceService.updateAeaImService(aeaImService);
        } else {
            if (aeaImService.getServiceId() == null || "".equals(aeaImService.getServiceId()))
                aeaImService.setServiceId(UUID.randomUUID().toString());
            aeaImService.setCreateTime(new Date());
            aeaImService.setCreater(SecurityContext.getCurrentUserName());
            aeaImService.setIsDelete("0");
            aeaImServiceService.saveAeaImService(aeaImService);
        }

        return new ContentResultForm<AeaImService>(true, aeaImService);
    }

    @RequestMapping("/deleteAeaImServiceById.do")
    public ResultForm deleteAeaImServiceById(String id) throws Exception {
        logger.debug("删除Form对象，对象id为：{}", id);
        if (id != null)
            aeaImServiceService.deleteAeaImServiceById(id);
        return new ResultForm(true);
    }

    /**
     * 保存中介事项关联中介服务
     *
     * @param itemVerId        中介事项版本ID
     * @param saveServiceIds   要保存的服务ID数组
     * @param cancelServiceIds 要取消的服务ID数组
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveConfigService.do")
    public ResultForm saveConfigService(String itemVerId, String[] saveServiceIds, String[] cancelServiceIds) throws Exception {
        ResultForm resultForm = aeaImServiceService.saveConfigService(itemVerId, saveServiceIds, cancelServiceIds);
        return resultForm;
    }

    @GetMapping("/listAeaImServiceNoPage")
    @ApiOperation(value = "查询中介服务列表，不分页")
    public ResultForm listAeaImServiceNoPage(AeaImService aeaImService) throws Exception {
        aeaImService.setIsActive("1");
        aeaImService.setIsDelete("0");
        List<AeaImService> list = aeaImServiceMapper.listAeaImService(aeaImService);
        return new ContentResultForm<>(true, list, "success");
    }

}
