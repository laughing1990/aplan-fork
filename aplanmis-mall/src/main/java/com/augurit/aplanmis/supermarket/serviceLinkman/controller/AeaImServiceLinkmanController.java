package com.augurit.aplanmis.supermarket.serviceLinkman.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaImServiceLinkman;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.supermarket.serviceLinkman.service.AeaImServiceLinkmanService;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.UUID;

@Api(description = "中介服务与从业人员关联关系操作接口")
@RestController
@RequestMapping("/supermarket/serviceLinkman")
public class AeaImServiceLinkmanController {

private static Logger logger = LoggerFactory.getLogger(AeaImServiceLinkmanController.class);

    @Autowired
    private AeaImServiceLinkmanService aeaImServiceLinkmanService;


    @RequestMapping("/indexAeaImServiceLinkman.do")
    public ModelAndView indexAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman, String infoType){
        return new ModelAndView("aea/im/service/service_linkman_index");
    }

    @ApiOperation(value = "查询关联信息列表", notes = "查询关联信息列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceLinkmanId", value = "关联信息ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "从业人员ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isDelete", value = "是否逻辑删除，0表示未删除，1表示已删除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isHead", value = "是否负责人，0表示否，1表示是", required = false, dataType = "String"),
            @ApiImplicitParam(name = "auditFlag", value = "0 审核失败，1 已审核，2 审核中", required = false, dataType = "String"),
            @ApiImplicitParam(name = "practiseDate", value = "从业时间", required = true, dataType = "Date")
    })
    @RequestMapping("/listAeaImServiceLinkman.do")
    public EasyuiPageInfo<AeaImServiceLinkman> listAeaImServiceLinkman(  AeaImServiceLinkman aeaImServiceLinkman, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaImServiceLinkman);
        return aeaImServiceLinkmanService.listAeaImServiceLinkman(aeaImServiceLinkman,page);
    }

    @ApiOperation(value = "获取关联信息", notes = "获取关联信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceLinkmanId", value = "关联信息ID", required = true, dataType = "String")
    })
    @RequestMapping("/getAeaImServiceLinkman.do")
    public AeaImServiceLinkman getAeaImServiceLinkman(String serviceLinkmanId) throws Exception {
        if (serviceLinkmanId != null){
            logger.debug("根据ID获取AeaImServiceLinkman对象，ID为：{}", serviceLinkmanId);
            return aeaImServiceLinkmanService.getAeaImServiceLinkmanById(serviceLinkmanId);
        }
        else {
            logger.debug("构建新的AeaImServiceLinkman对象");
            return new AeaImServiceLinkman();
        }
    }

    @ApiOperation(value = "根据服务Id查询从业人员信息列表", notes = "根据服务Id查询从业人员信息列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String")
    })
    @RequestMapping("/listAeaImServiceLinkmanByServiceId.do")
    public EasyuiPageInfo<AeaLinkmanInfo> listAeaImServiceLinkmanByServiceId(String serviceId, Page page) throws Exception {

        return aeaImServiceLinkmanService.listAeaImServiceLinkmanByServiceId(serviceId,page);
    }



    @ApiOperation(value = "根据企业Id查询从业人员信息列表", notes = "根据企业Id查询从业人员信息列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitInfoId", value = "企业ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "cardNo", value = "身份证号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "auditFlag", value = "0 审核失败，1 已审核，2 审核中", required = true, dataType = "String")
    })
    @RequestMapping("/listAeaImServiceLinkmanByUnitInfoId.do")
    public EasyuiPageInfo<AeaLinkmanInfo> listAeaImServiceLinkmanByUnitInfoId(String unitInfoId, String cardNo, String auditFlag, Integer pageNum, Integer pageSize) throws Exception {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
        return aeaImServiceLinkmanService.listAeaImServiceLinkmanByUnitInfoId(unitInfoId,cardNo,auditFlag,page);
    }


    @ApiOperation(value = "修改中介服务与从业人员关联信息", notes = "修改中介服务与从业人员关联信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceLinkmanId", value = "关联信息ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "从业人员ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isDelete", value = "是否逻辑删除，0表示未删除，1表示已删除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isHead", value = "是否负责人，0表示否，1表示是", required = false, dataType = "String"),
            @ApiImplicitParam(name = "auditFlag", value = "0 审核失败，1 已审核，2 审核中", required = false, dataType = "String"),
            @ApiImplicitParam(name = "practiseDate", value = "从业时间", required = true, dataType = "Date")
    })
    @RequestMapping("/updateAeaImServiceLinkman.do")
        public ResultForm updateAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaImServiceLinkman);
        aeaImServiceLinkmanService.updateAeaImServiceLinkman(aeaImServiceLinkman);
        return new ResultForm(true);
    }



    @ApiOperation(value = "保存中介服务与从业人员关联信息", notes = "保存中介服务与从业人员关联信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceLinkmanId", value = "关联信息ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "从业人员ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isDelete", value = "是否逻辑删除，0表示未删除，1表示已删除", required = false, dataType = "String"),
            @ApiImplicitParam(name = "isHead", value = "是否负责人，0表示否，1表示是", required = false, dataType = "String"),
            @ApiImplicitParam(name = "auditFlag", value = "0 审核失败，1 已审核，2 审核中", required = false, dataType = "String"),
            @ApiImplicitParam(name = "practiseDate", value = "从业时间", required = true, dataType = "Date")
    })
    @RequestMapping("/saveAeaImServiceLinkman.do")
    public ResultForm saveAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaImServiceLinkman);
        }

        if(aeaImServiceLinkman.getServiceLinkmanId()!=null&&!"".equals(aeaImServiceLinkman.getServiceLinkmanId())){
            aeaImServiceLinkmanService.updateAeaImServiceLinkman(aeaImServiceLinkman);
        }else{
        if(aeaImServiceLinkman.getServiceLinkmanId()==null||"".equals(aeaImServiceLinkman.getServiceLinkmanId()))
            aeaImServiceLinkman.setServiceLinkmanId(UUID.randomUUID().toString());
            aeaImServiceLinkman.setIsDelete("0");
            aeaImServiceLinkman.setCreateTime(new Date());
            aeaImServiceLinkman.setCreater(SecurityContext.getCurrentUserName());
            aeaImServiceLinkmanService.saveAeaImServiceLinkman(aeaImServiceLinkman);
        }

        return  new ContentResultForm<AeaImServiceLinkman>(true,aeaImServiceLinkman);
    }

    @ApiOperation(value = "删除关联信息", notes = "删除关联信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceLinkmanId", value = "关联信息ID", required = true, dataType = "String"),
    })
    @RequestMapping("/deleteAeaImServiceLinkmanById.do")
    public ResultForm deleteAeaImServiceLinkmanById(String serviceLinkmanId) throws Exception{
        logger.debug("删除Form对象，对象id为：{}", serviceLinkmanId);
        if(serviceLinkmanId!=null)
            aeaImServiceLinkmanService.deleteAeaImServiceLinkmanById(serviceLinkmanId);
        return new ResultForm(true);
    }




}
