package com.augurit.aplanmis.supermarket.certinst.controller;


import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaImServiceQual;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.supermarket.certinst.service.AeaHiCertinstService;
import com.augurit.aplanmis.supermarket.utils.RestResult;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "证照实例操作接口")
@RestController
@RequestMapping("/supermarket/certinst")
public class AeaCertinstController {

    private static Logger logger = LoggerFactory.getLogger(AeaCertinstController.class);

    @Autowired
    private AeaHiCertinstService aeaHiCertinstService;


    @RequestMapping("/indexAeaHiCertinst.do")
    public ModelAndView indexAeaHiCertinst(AeaHiCertinst aeaHiCertinst, String infoType){
        return new ModelAndView("aea/hi/certinst_index");
    }

    @RequestMapping("/listAeaHiCertinst.do")
    public PageInfo<AeaHiCertinst> listAeaHiCertinst(AeaHiCertinst aeaHiCertinst, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaHiCertinst);
        return aeaHiCertinstService.listAeaHiCertinst(aeaHiCertinst,page);
    }

    @RequestMapping("/getAeaHiCertinst.do")
    public AeaHiCertinst getAeaHiCertinst(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaHiCertinst对象，ID为：{}", id);
            return aeaHiCertinstService.getAeaHiCertinstById(id);
        }
        else {
            logger.debug("构建新的AeaHiCertinst对象");
            return new AeaHiCertinst();
        }
    }

    @RequestMapping("/updateAeaHiCertinst.do")
    public ResultForm updateAeaHiCertinst(AeaHiCertinst aeaHiCertinst) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaHiCertinst);
        aeaHiCertinstService.updateAeaHiCertinst(aeaHiCertinst);
        return new ResultForm(true);
    }

    @ApiOperation(value = "显示服务类型列表信息", notes = "显示服务类型列表信息。")
    @RequestMapping("/listAeaImService.do")
    public List<AeaImService> listAeaImService() throws Exception {
        return aeaHiCertinstService.listAeaImService();
    }


    @ApiOperation(value = "添加证书", notes = "添加证书。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstId", value = "证照实例Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "certId", value = "证照定义ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "业主单位ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "projInfoId", value = "建设项目ID", required = false, dataType = "String"),
            @ApiImplicitParam(name = "certinstCode", value = "证照编码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "certinstName", value = "证照名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "caInfo", value = "电子签章信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "certOwner", value = "持证者", required = true, dataType = "String"),
            @ApiImplicitParam(name = "issueOrgId", value = "颁发单位Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "termNum", value = "有效期限数字", required = true, dataType = "String"),
            @ApiImplicitParam(name = "termUnit", value = "有效期限单位", required = true, dataType = "String"),
            @ApiImplicitParam(name = "flxl", value = "法律效率", required = true, dataType = "String"),
            @ApiImplicitParam(name = "applyinstId", value = "申请Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "linkmanInfoId", value = "联系人Id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "qualLevelId", value = "资质等级ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "managementScope", value = "业务范围", required = false, dataType = "String"),
            @ApiImplicitParam(name = "serviceId", value = "服务Id", required = true, dataType = "String")
    })
    @RequestMapping("/uploadAeaHiCertinstFile.do")
    public ResultForm saveAeaHiCertinst(@RequestParam("file") MultipartFile file, AeaHiCertinstBVo aeaHiCertinst, HttpServletRequest request, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存电子证照实例表Form对象出错");
            return  new ContentResultForm<AeaHiCertinstBVo>(false,aeaHiCertinst);
        }
        try {
            String tableName = "aea_hi_certinst";
            String pkName = "CERTINST_ID";
            if(aeaHiCertinst.getCertinstId()!=null&&!"".equals(aeaHiCertinst.getCertinstId())){
                aeaHiCertinstService.updateAeaHiCertinst(aeaHiCertinst);
            }else{
                if(aeaHiCertinst.getCertinstId()==null||"".equals(aeaHiCertinst.getCertinstId()))

                aeaHiCertinstService.saveAeaHiCertinst(aeaHiCertinst);
            }
            if (StringUtils.isBlank(tableName) || StringUtils.isBlank(aeaHiCertinst.getCertinstId())
                    || StringUtils.isBlank(pkName)) {
                return new ResultForm(false);
            }

            FileUtils.uploadFile(tableName, pkName, aeaHiCertinst.getCertinstId(), request);
            return  new ContentResultForm<AeaHiCertinstBVo>(true,aeaHiCertinst);

        }catch(Exception e){
            return  new ContentResultForm<AeaHiCertinstBVo>(false,aeaHiCertinst);
        }


    }
    @ApiOperation(value = "删除证照实例附件", notes = "删除证照实例附件", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailId", value = "附件id,多个以,隔开", required = true)
    })
    @RequestMapping("/deleteAeaHiCertinstByDetailId.do")
    public RestResult deleteAeaHiCertinstByDetailId(String detailId) throws Exception{
        try {
            if (StringUtils.isBlank(detailId)) {
                return new RestResult(false, "删除失败：没有可删除文件!");
            }
            if (aeaHiCertinstService.deleteContractFile(detailId)) {
                return new RestResult(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new RestResult(false,e.getMessage());
        }
        return new RestResult(false, "删除失败!");
    }

    @ApiOperation(value = "删除证照实例", notes = "删除证照实例", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstId", value = "证照实例Id", required = true),
            @ApiImplicitParam(name = "detailId", value = "附件id,多个以,隔开", required = true)
    })
    @RequestMapping("/deleteAeaHiCertinstByCertinstId.do")
    public RestResult deleteAeaHiCertinstByCertinstId(String certinstId,String detailId) throws Exception{
        try {
            if (StringUtils.isBlank(certinstId)) {
                return new RestResult(false, "删除失败：没有可删除证照!");
            }else{
                aeaHiCertinstService.deleteAeaHiCertinstById(certinstId);
                aeaHiCertinstService.deleteContractFile(detailId);
                return new RestResult(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new RestResult(false,e.getMessage());
        }
    }

    @ApiOperation(value = "删除证照实例以及关联关系", notes = "删除证照实例以及关联关系", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certinstId", value = "证照实例Id", required = true)
    })
    @RequestMapping("/deleteCertinst")
    public RestResult deleteCertinst(String certinstId) throws Exception{
        try {
            if (StringUtils.isBlank(certinstId)) {
                return new RestResult(false, "删除失败：没有可删除证照!");
            }else{
                aeaHiCertinstService.deleteCertinst(certinstId);
                return new RestResult(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new RestResult(false, e.getMessage());
        }
    }



    @ApiOperation(value = "根据服务Id和企业Id获取专业等级证照定义信息", notes = "根据服务Id和企业Id获取专业等级证照定义信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "企业ID", required = true, dataType = "String")
    })
    @RequestMapping("/getAeaImMajorLevelAndCertByServiceId.do")
    public PageInfo<AeaHiCertinst> getAeaImMajorLevelAndCertByServiceId(String serviceId, String unitInfoId, Page page) throws Exception {
        PageInfo<AeaHiCertinst> pageinfo = new PageInfo<AeaHiCertinst>();
        if (serviceId != null){
            pageinfo = aeaHiCertinstService.getAeaImMajorLevelAndCertinstByServiceId(serviceId,unitInfoId,page);
        }else {
            pageinfo = null;

        }
        return pageinfo;
    }


    @ApiOperation(value = "获取企业证书", notes = "根据服务Id和企业Id获取专业等级证照定义信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "企业ID", required = true, dataType = "String")
    })
    @RequestMapping("/getCertinstListByUnitInfoId.do")
    public PageInfo<AeaHiCertinst> getCertinstListByUnitInfoId(String serviceId, String unitInfoId, Page page) throws Exception {
        PageInfo<AeaHiCertinst> pageinfo = new PageInfo<AeaHiCertinst>();
        if (serviceId != null){
            pageinfo = aeaHiCertinstService.getAeaImMajorLevelAndCertinstByServiceId(serviceId,unitInfoId,page);
        }else {
            pageinfo = null;

        }
        return pageinfo;
    }


    @ApiOperation(value = "根据服务Id获取资质列表信息", notes = "根据服务Id获取资质列表信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true, dataType = "String")
    })
    @RequestMapping("/listAeaImServiceQual.do")
    public List<AeaImServiceQual> listAeaImServiceQual(String serviceId) throws Exception {
        return aeaHiCertinstService.listAeaImServiceQual(serviceId);
    }

    @ApiOperation(value = "根据资质Id获取专业等级信息", notes = "根据资质Id获取专业等级信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qualId", value = "资质ID", required = true, dataType = "String")
    })
    @RequestMapping("/getMajorTreeByQualId.do")
    public List<ZtreeNode> getMajorTreeByQualId(String qualId) throws Exception{
        return aeaHiCertinstService.getMajorTreeByQualId(qualId);
    }

    @ApiOperation(value = "根据单位ID或者联系人ID获取证照列表", notes = "根据单位ID或者联系人ID获取证照列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型：1-根据单位ID获取证照列表；2-根据联系人ID获取证照列表", dataType = "String"),
            @ApiImplicitParam(name = "typeId", value = "type=1：unitInfoId；type=2：linkmanInfoId;", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer")
    })
    @RequestMapping("/listCertinstLibrary")
    public ResultForm listCertinstLibrary(String type, String typeId, Integer pageNum, Integer pageSize) throws Exception {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(typeId) || pageNum == null || pageSize == null) {
            return new ResultForm(false, "参数为空");
        }

        try {
            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AeaHiCertinstBVo> list = aeaHiCertinstService.listCertinstLibrary(type, typeId, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

}