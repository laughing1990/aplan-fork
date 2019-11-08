package com.augurit.aplanmis.supermarket.linkmanInfo.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.supermarket.linkmanInfo.service.AeaLinkmanInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
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

import java.util.ArrayList;
import java.util.List;

@Api(description = "联系人操作接口")
@RestController
@RequestMapping("/supermarket/linkmanInfo")
public class AeaLinkmanInfoController {

private static Logger logger = LoggerFactory.getLogger(AeaLinkmanInfoController.class);

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;


    @RequestMapping("/indexAeaLinkmanInfo.do")
    public ModelAndView indexAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo, String infoType){
        return new ModelAndView("aea/linkman/info_index");
    }

    @RequestMapping("/listAeaLinkmanInfo.do")
    public PageInfo<AeaLinkmanInfo> listAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaLinkmanInfo);
        return aeaLinkmanInfoService.listAeaLinkmanInfo(aeaLinkmanInfo,page);
    }

    @RequestMapping("/getAeaLinkmanInfo.do")
    public List<AeaLinkmanInfo> getAeaLinkmanInfo(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaLinkmanInfo对象，ID为：{}", id);
            return aeaLinkmanInfoService.getAeaLinkmanInfoById(id);
        }
        else {
            logger.debug("构建新的AeaLinkmanInfo对象");
            return new ArrayList<AeaLinkmanInfo>();
        }
    }


    @ApiOperation(value = "添加联系人", notes = "添加联系人。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "linkmanInfoId", value = "联系人ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanType", value = "联系人类型", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanCate", value = "联系人类别", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanName", value = "联系人姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanAddr", value = "联系人住址", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanOfficePhon", value = "办公电话", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanMobilePhone", value = "手机号码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanFax", value = "传真", required = false, dataType = "Date"),
            @ApiImplicitParam(name = "linkmanMail", value = "电子邮件", required = false, dataType = "Date"),
            @ApiImplicitParam(name = "linkmanCertNo", value = "证件号", required = false, dataType = "Date"),
            @ApiImplicitParam(name = "isActive", value = "是否启用，0表示不启用，1表示启用", required = true, dataType = "Date"),
            @ApiImplicitParam(name = "linkmanMemo", value = "备注", required = false, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "所属单位ID", required = true, dataType = "String"),
    })
    @RequestMapping("/saveLinkman.do")
    public ResultForm saveLinkman(AeaLinkmanInfo aeaLinkmanInfo) throws Exception{

        if(aeaLinkmanInfo.getLinkmanInfoId()==null||"".equals(aeaLinkmanInfo.getLinkmanInfoId())){
            aeaLinkmanInfo = aeaLinkmanInfoService.insertLinkmanInfo(aeaLinkmanInfo);
        }else{
            aeaLinkmanInfo = aeaLinkmanInfoService.updateLinkmanInfo(aeaLinkmanInfo);
        }
        if(aeaLinkmanInfo!=null){
            return new ContentResultForm<AeaLinkmanInfo>(true,aeaLinkmanInfo);
        }else{
            return new ResultForm(false);
        }

    }


    @ApiOperation(value = "新增/修改从业人员信息", notes = "新增/修改从业人员信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "linkmanInfoId", value = "联系人ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanType", value = "联系人类型", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanCate", value = "联系人类别", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanName", value = "联系人姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanAddr", value = "联系人住址", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanOfficePhon", value = "办公电话", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanMobilePhone", value = "手机号码", required = false, dataType = "String"),
            @ApiImplicitParam(name = "linkmanFax", value = "传真", required = false, dataType = "Date"),
            @ApiImplicitParam(name = "linkmanMail", value = "电子邮件", required = false, dataType = "Date"),
            @ApiImplicitParam(name = "linkmanCertNo", value = "证件号", required = false, dataType = "Date"),
            @ApiImplicitParam(name = "isActive", value = "是否启用，0表示不启用，1表示启用", required = true, dataType = "Date"),
            @ApiImplicitParam(name = "linkmanMemo", value = "备注", required = false, dataType = "String"),
            @ApiImplicitParam(name = "serviceId", value = "服务Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "所属单位ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "isHead", value = "是否负责人，1为是，0为否", required = true, dataType = "String"),
            @ApiImplicitParam(name = "practiseDate", value = "从业时间", required = true, dataType = "Date"),
            @ApiImplicitParam(name = "serviceLinkmanId", value = "服务联系人关联Id", required = true, dataType = "String")
    })
    @RequestMapping("/saveAeaLinkmanInfo.do")
    public ResultForm saveAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo, BindingResult result) throws Exception {
        /*if(result.hasErrors()) {
            logger.error("联系人表Form对象出错");
            throw new InvalidParameterException(aeaLinkmanInfo);
        }*/

        if(aeaLinkmanInfo.getLinkmanInfoId()!=null&&!"".equals(aeaLinkmanInfo.getLinkmanInfoId())
                &&(aeaLinkmanInfo.getServiceLinkmanId()==null||"".equals(aeaLinkmanInfo.getServiceLinkmanId()))){
            try {

                aeaLinkmanInfoService.saveAeaLinkmanInfo(aeaLinkmanInfo);
                return  new ContentResultForm<AeaLinkmanInfo>(true,aeaLinkmanInfo);
            }catch(Exception e){
                return new ContentResultForm<AeaLinkmanInfo>(false,aeaLinkmanInfo);
            }
        }else if(aeaLinkmanInfo.getLinkmanInfoId()!=null&&!"".equals(aeaLinkmanInfo.getLinkmanInfoId())
                &&aeaLinkmanInfo.getServiceLinkmanId()!=null&&!"".equals(aeaLinkmanInfo.getServiceLinkmanId())){
            try {
                aeaLinkmanInfoService.updateAeaLinkmanInfo(aeaLinkmanInfo);
                return  new ContentResultForm<AeaLinkmanInfo>(true,aeaLinkmanInfo);
            }catch(Exception e){
                return new ContentResultForm<AeaLinkmanInfo>(false,aeaLinkmanInfo);
            }

        }else{
            return new ContentResultForm<AeaLinkmanInfo>(false,aeaLinkmanInfo);
        }
    }

    @RequestMapping("/deleteAeaLinkmanInfoById.do")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitInfoId", value = "关联信息ID", required = true, dataType = "String")
    })
    public ResultForm deleteAeaLinkmanInfoById(String serviceLinkmanId,String unitServiceId,String unitServiceLinkmanId) throws Exception{

        if(StringUtils.isNotBlank(serviceLinkmanId)&&StringUtils.isNotBlank(unitServiceId)&&StringUtils.isNotBlank(unitServiceLinkmanId))
            aeaLinkmanInfoService.deleteAeaLinkmanInfoById(serviceLinkmanId,unitServiceId,unitServiceLinkmanId);
        return new ResultForm(true);

    }

    @ApiOperation(value = "获取单位联系人信息", notes = "获取单位联系人信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitInfoId", value = "关联信息ID", required = true, dataType = "String")
    })
    @RequestMapping("/getAeaLinkmanInfoByUnitInfoId.do")
    public List<AeaLinkmanInfo> getAeaLinkmanInfoByUnitInfoId(String unitInfoId) throws Exception {
        if (unitInfoId != null){
            logger.debug("根据ID获取AeaLinkmanInfo对象，ID为：{}", unitInfoId);
            return aeaLinkmanInfoService.getAeaLinkmanInfoByUnitInfoId(unitInfoId,null);
        }
        else {
            logger.debug("构建新的AeaLinkmanInfo对象");
            return new ArrayList<AeaLinkmanInfo>();
        }
    }

}
