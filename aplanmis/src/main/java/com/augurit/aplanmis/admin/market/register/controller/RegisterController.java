package com.augurit.aplanmis.admin.market.register.controller;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.register.service.RegisterService;
import com.augurit.aplanmis.admin.market.register.vo.RegisterAuditVo;
import com.augurit.aplanmis.admin.market.register.vo.RegisterResultVo;
import com.augurit.aplanmis.admin.market.register.vo.RegisterSearch;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.vo.AeaImProjPurchaseDetailVo;
import com.augurit.aplanmis.common.vo.AgentRegisterVo;
import com.augurit.aplanmis.common.vo.ServiceMatterVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/supermarket/register")
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @RequestMapping("/index.html")
    @ApiOperation(value = "列表页面")
    public ModelAndView registerIndex() {
        return new ModelAndView("admin/supermarket/register/register_index");
    }

    @RequestMapping("/detail.html")
    @ApiOperation(value = "详情页面")
    public ModelAndView registerDetail() {
        return new ModelAndView("admin/supermarket/register/register_detail");
    }

    @PostMapping("/listRegister")
    @ApiOperation(value = "查询入驻机构列表")
    public ResultForm listRegister(RegisterSearch registerSearch, Integer pageSize, Integer pageNum) throws Exception {
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        Page page = new Page(pageNum, pageSize);
        List<AeaUnitInfo> list = registerService.getRegisterUnitList(registerSearch, page);
        return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)), "success");
    }
    @RequestMapping("/getRegisterDetail")
    @ApiOperation(value = "获取入驻机构详情信息")
    public ResultForm getRegisterDetail(String unitInfoId) throws Exception {
        RegisterResultVo registerResultVo = registerService.getRegisterUnitDetail(unitInfoId);
        return new ContentResultForm<RegisterResultVo>(true, registerResultVo, "success");
    }

    @RequestMapping("/examineUnitService")
    @ApiOperation(value = "审批入驻机构")
    public ResultForm examineUnitService(RegisterAuditVo registerAuditVo) throws Exception {
        registerService.examineService(registerAuditVo);
        return new ResultForm(true);

    }


}
