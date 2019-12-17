package com.augurit.aplanmis.admin.market.register.controller;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.register.service.RegisterService;
import com.augurit.aplanmis.admin.market.register.vo.*;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/ownerDetail.html")
    @ApiOperation(value = "详情页面")
    public ModelAndView ownerRegisterDetail() {
        return new ModelAndView("admin/supermarket/register/owner_register_detail");
    }

    @PostMapping("/listRegister")
    @ApiOperation(value = "查询入驻机构列表")
    public ResultForm listRegister(RegisterSearch registerSearch, Integer pageSize, Integer pageNum) throws Exception {
        pageNum = (pageNum == null ? 1 : pageNum);
        pageSize = (pageSize == null ? 10 : pageSize);
        Page page = new Page(pageNum, pageSize);
        List<AeaUnitInfo> list = registerService.getRegisterUnitList(registerSearch, page);
        List<AgentUnitVo> volist = AgentUnitVo.changeToVoList(list);
        return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(volist)), "success");
    }

    @RequestMapping("/getRegisterDetail")
    @ApiOperation(value = "获取入驻的中介机构详情信息")
    public ResultForm getAgentRegisterDetail(String unitInfoId) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(unitInfoId), "unitInfoId不能为空！");
        RegisterResultVo registerResultVo = registerService.getRegisterUnitDetail(unitInfoId);
        if (registerResultVo != null && registerResultVo.getUnitInfo() != null) {
            return new ContentResultForm<RegisterResultVo>(true, registerResultVo, "success");
        } else {
            return new ContentResultForm<RegisterResultVo>(false, registerResultVo, "根据单位id查询不到该单位信息！");
        }
    }

    @RequestMapping("/examineUnitService")
    @ApiOperation(value = "审批入驻的中介机构")
    public ResultForm examineAgentUnitService(RegisterAuditVo registerAuditVo) throws Exception {
        registerService.examineService(registerAuditVo);
        return new ResultForm(true);

    }

    @RequestMapping("/getOwnerRegisterDetail")
    @ApiOperation(value = "获取入驻的业主机构详情信息")
    public ResultForm getOwnerRegisterDetail(String unitInfoId) throws Exception {
        OwnerRegisterResultVo registerResultVo = registerService.getOwnerRegisterDetail(unitInfoId);
        return new ContentResultForm<OwnerRegisterResultVo>(true, registerResultVo, "success");
    }

    @RequestMapping("/examineOwnerUnitService")
    @ApiOperation(value = "审批入驻的业主机构")
    public ResultForm examineOwnerUnitService(RegisterAuditVo registerAuditVo) throws Exception {
        registerService.examineService(registerAuditVo);
        return new ResultForm(true);

    }

    @RequestMapping("/delete/{unitInfoId}")
    @ApiOperation(value = "删除中介机构")
    public ResultForm deleteAgentUnit(@PathVariable String unitInfoId) throws Exception {
        registerService.deleteOrEnableAgentUnit(unitInfoId, "1");
        return new ResultForm(true);
    }

    @RequestMapping("/enable/{unitInfoId}")
    @ApiOperation(value = "启用中介机构")
    public ResultForm enableAgentUnit(@PathVariable String unitInfoId) throws Exception {
        registerService.deleteOrEnableAgentUnit(unitInfoId, "0");
        return new ResultForm(true);
    }


}
