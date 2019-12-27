package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;
import com.augurit.aplanmis.common.service.admin.item.AeaItemServiceBasicAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * 设立依据-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/item/service/basic")
public class AeaItemServiceBasicAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemServiceBasicAdminController.class);

    @Autowired
    private AeaItemServiceBasicAdminService aeaItemServiceBasicAdminService;

    @RequestMapping("/indexAeaItemServiceBasic.do")
    public ModelAndView indexAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic, ModelMap modelMap) {

        modelMap.addAttribute("recordId", aeaItemServiceBasic.getRecordId());
        return new ModelAndView("ui-jsp/aplanmis/item/service_basic_index");
    }

    @RequestMapping("/viewAeaItemServiceBasic.do")
    public ModelAndView viewAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic, ModelMap modelMap) {

        modelMap.addAttribute("recordId", aeaItemServiceBasic.getRecordId());
        return new ModelAndView("ui-jsp/aplanmis/item/view_service_basic_index");
    }

    @RequestMapping("/listAeaItemServiceBasic.do")
    public EasyuiPageInfo<AeaItemServiceBasic> listAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemServiceBasic);
        PageInfo<AeaItemServiceBasic> pageList = aeaItemServiceBasicAdminService.listAeaItemServiceBasic(aeaItemServiceBasic, page);
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @ApiOperation(value = "查询设立依据列表,带分页", notes = "查询设立依据列表,带分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "basic", value = "必填" , dataType = "AeaItemServiceBasic" ,paramType = "body"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping("/listItemBasicByPageForEui.do")
    public EasyuiPageInfo<AeaItemServiceBasic> listItemBasicByPageForEui(AeaItemServiceBasic basic, @ModelAttribute PageParam page)throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", basic);
        PageInfo<AeaItemServiceBasic> pageList = aeaItemServiceBasicAdminService.listAeaItemServiceBasic(basic, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @RequestMapping("/getAeaItemServiceBasic.do")
    public AeaItemServiceBasic getAeaItemServiceBasic(String id) throws Exception {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取AeaItemServiceBasic对象，ID为：{}", id);
            return aeaItemServiceBasicAdminService.getAeaItemServiceBasicById(id);
        }else {
            logger.debug("构建新的AeaItemServiceBasic对象");
            return new AeaItemServiceBasic();
        }
    }

    @RequestMapping("/updateAeaItemServiceBasic.do")
    public ResultForm updateAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaItemServiceBasic);
        aeaItemServiceBasicAdminService.updateAeaItemServiceBasic(aeaItemServiceBasic);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑设立依据
     *
     * @param aeaItemServiceBasic 设立依据
     * @return 返回结果对象 包含结果信息
     */
    @RequestMapping("/saveAeaItemServiceBasic.do")
    public ResultForm saveAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception {

        if (StringUtils.isNotBlank(aeaItemServiceBasic.getBasicId())) {
            aeaItemServiceBasicAdminService.updateAeaItemServiceBasic(aeaItemServiceBasic);
        } else {
            aeaItemServiceBasic.setBasicId(UUID.randomUUID().toString());
            aeaItemServiceBasicAdminService.saveAeaItemServiceBasic(aeaItemServiceBasic);
        }
        return new ContentResultForm<>(true, aeaItemServiceBasic);
    }

    @RequestMapping("/deleteAeaItemServiceBasicById.do")
    public ResultForm deleteAeaItemServiceBasicById(String id) throws Exception {

        logger.debug("删除设立依据Form对象，对象id为：{}", id);
        if (id != null) {
            aeaItemServiceBasicAdminService.deleteAeaItemServiceBasicById(id);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/batchDeleteServiceBasic.do")
    public ResultForm batchDeleteServiceBasic(String[] ids) throws Exception {

        logger.debug("删除材料信息表Form对象，对象id为：{}", (Object[]) ids);
        if (ids != null && ids.length > 0) {
            aeaItemServiceBasicAdminService.batchDeleteServiceBasic(ids);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/batchSaveServiceBasic.do")
    public ResultForm batchSaveServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception {

        aeaItemServiceBasicAdminService.batchSaveServiceBasic(aeaItemServiceBasic);
        return new ResultForm(true);
    }

    @RequestMapping("/listAeaItemServiceBasicByRecordId.do")
    public List<AeaItemServiceBasic> listAeaItemServiceBasicByRecordId(AeaItemServiceBasic aeaItemServiceBasic) throws Exception {

        return aeaItemServiceBasicAdminService.listAeaItemServiceBasic(aeaItemServiceBasic);
    }
}
