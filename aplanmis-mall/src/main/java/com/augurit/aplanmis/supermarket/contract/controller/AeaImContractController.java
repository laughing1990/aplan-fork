package com.augurit.aplanmis.supermarket.contract.controller;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaImContract;
import com.augurit.aplanmis.common.utils.BusinessUtils;
import com.augurit.aplanmis.common.vo.ChosenProjInfoVo;
import com.augurit.aplanmis.supermarket.contract.service.AeaImContractService;
import com.augurit.aplanmis.supermarket.contract.vo.ContractVo;
import com.augurit.aplanmis.supermarket.utils.ContentRestResult;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(description = "服务合同相关接口")
@RequestMapping("/supermarket/contract")
public class AeaImContractController {

    private static Logger logger = LoggerFactory.getLogger(AeaImContractController.class);

    @Autowired
    private AeaImContractService aeaImContractService;


    @ApiOperation(value = "上传服务合同附件及保存服务合同信息", notes = "上传服务合同附件及保存服务合同信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "合同附件", required = true)
    })
    @PostMapping("/postContract")
    public ContentRestResult postContract(@RequestParam(name = "file", required = false) List<MultipartFile> files, @Valid ContractVo contractVo,
                                          BindingResult bindingResult, HttpServletRequest request) throws Exception {
        String errorMsg = BusinessUtils.checkBindingResult(bindingResult);

        if (StringUtils.isNotBlank(errorMsg)) {
            return new ContentRestResult(false, null, errorMsg);
        }
        AeaImContract aeaImContract = aeaImContractService.saveOrUpdateContract(contractVo, request);
        /*try {
            if (StringUtils.isNotBlank(aeaImContract.getContractId())){
                aeaImContractService.updateAeaImContract(aeaImContract,files,request);
            } else {
                restImApplyService.uploadContract(aeaImContract,null,request);
            }

            return new ContentRestResult(true,null,aeaImContract.getContractId());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentRestResult(false,null,e.getMessage());
        }*/
        return new ContentRestResult(true, aeaImContract.getContractId(), "success");

    }

    @ApiOperation(value = "删除服务合同附件", notes = "删除服务合同附件", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailId", value = "附件id,多个以,隔开", required = true)
    })
    @PostMapping("/deleteContractFile")
    public RestResult deleteContractFile(String detailId) {
        try {
            if (StringUtils.isBlank(detailId)) {
                return new RestResult(false, "删除失败：没有可删除文件!");
            }
            if (aeaImContractService.deleteContractFile(detailId)) {
                return new RestResult(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RestResult(false, e.getMessage());
        }
        return new RestResult(false, "删除失败!");
    }

    @ApiOperation(value = "根据id获取合同信息", notes = "根据id获取合同信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contractId", value = "合同id", required = true)
    })
    @PostMapping("/getAeaImContract/{contractId}")
    public ContentRestResult<AeaImContract> getAeaImContract(@PathVariable("contractId") String contractId) throws Exception {
        if (StringUtils.isNotBlank(contractId)) {
            AeaImContract aeaImContract = aeaImContractService.getAeaImContractById(contractId);
            if (aeaImContract != null) {
                return new ContentRestResult(true, aeaImContract);
            }
        }
        return new ContentRestResult(false);
    }

    @ApiOperation(value = "确认合同", notes = "确认合同", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contractId", value = "合同id", required = true)
    })
    @PostMapping("/confirmContractByContractId/{contractId}")
    public ContentRestResult<AeaImContract> confirmContractByContractId(@PathVariable String contractId, String auditFlag, String auditOpinion, Date postponeServiceEndTime, HttpServletRequest request) throws Exception {
        aeaImContractService.confirmContract(contractId, auditFlag, auditOpinion, postponeServiceEndTime, request);
        return new ContentRestResult(true, "aeaImContract", "success");

        /*if (loginInfoVo != null && StringUtils.isNotBlank(contractId)) {


            aeaImContract = aeaImContractService.confirmContractByContractId(aeaImContract, request);
            if (aeaImContract != null) {
                return new ContentRestResult(true, "aeaImContract","success");
            }
        }

        return new ContentRestResult(false);*/
    }

    @ApiOperation(value = "删除合同信息", notes = "删除合同信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contractId", value = "合同id", required = true)
    })
    @PostMapping("/deleteAeaImContract/{contractId}")
    public RestResult deleteAeaImContractById(@PathVariable("contractId") String contractId) throws Exception {
        aeaImContractService.deleteAeaImContractById(contractId);
        return new RestResult(true);
    }

    @ApiOperation(value = "查询新增合同的默认信息", notes = "查询新增合同的默认信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "采购需求id", required = true)
    })
    @PostMapping("/getNewContract/{projPurchaseId}")
    public ContentRestResult<AeaImContract> getNewContract(@PathVariable("projPurchaseId") String projPurchaseId, HttpServletRequest request) throws Exception {
        AeaImContract aeaImContract = aeaImContractService.getNewContract(projPurchaseId, request);

        if (aeaImContract != null) {
            return new ContentRestResult(true, aeaImContract);
        } else {
            return new ContentRestResult(false, null, "查询不到相关的记录");
        }
    }

    @ApiOperation(value = "查询中介机构中选的项目", notes = "查询中介机构中选的项目", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字"),
            @ApiImplicitParam(name = "auditFlag", value = "项目状态"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", dataType = "int", required = true)
    })
    @PostMapping("/getChosenProjInfoList")
    public ContentRestResult<EasyuiPageInfo<ChosenProjInfoVo>> getChosenProjInfoList(String keyword, String auditFlag, int pageNum, int pageSize, HttpServletRequest request) throws Exception {

        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);

        List<ChosenProjInfoVo> list = aeaImContractService.getChosenProjInfoList(keyword, auditFlag, page, request);

        return new ContentRestResult<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
    }

}
