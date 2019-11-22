package com.augurit.aplanmis.supermarket.serviceResult.controller;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaImServiceResult;
import com.augurit.aplanmis.common.mapper.AeaImServiceResultMapper;
import com.augurit.aplanmis.common.utils.BusinessUtils;
import com.augurit.aplanmis.common.vo.MatinstVo;
import com.augurit.aplanmis.common.vo.ServiceProjInfoVo;
import com.augurit.aplanmis.supermarket.serviceResult.service.AeaImServiceResultService;
import com.augurit.aplanmis.supermarket.serviceResult.vo.ServiceResultVo;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tiantian
 * @date 2019/6/19
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(description = "服务结果相关接口")
@RequestMapping("/supermarket/result")
public class AeaImServiceResultController {

    @Autowired
    private AeaImServiceResultService aeaImServiceResultService;


    private static Logger logger = LoggerFactory.getLogger(AeaImServiceResultController.class);

    @Autowired
    private AeaImServiceResultMapper aeaImServiceResultMapper;

    @ApiOperation(value = "查询中介机构服务中的项目", notes = "查询中介机构服务中的项目", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字"),
            @ApiImplicitParam(name = "auditFlag", value = "项目状态"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", dataType = "int", required = true)
    })
    @PostMapping("/getServiceProjInfoList")
    public ContentRestResult<EasyuiPageInfo<ServiceProjInfoVo>> getServiceProjInfoList(String keyword, String auditFlag, int pageNum, int pageSize, HttpServletRequest request) throws Exception {

        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);

        List<ServiceProjInfoVo> list = aeaImServiceResultService.getServiceProjInfoList(keyword, auditFlag, page, request);

        return new ContentRestResult<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
    }

    @ApiOperation(value = "上传服务结果附件及保存服务结果信息", notes = "上传服务结果附件及保存服务结果信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "服务结果附件", required = true)
    })
    @PostMapping("/postServiceResult")
    public ContentRestResult postServiceResult(@RequestParam(name = "file", required = false) List<MultipartFile> files, @Valid AeaImServiceResult aeaImServiceResult,
                                               BindingResult bindingResult, HttpServletRequest request) {
        String errorMsg = BusinessUtils.checkBindingResult(bindingResult);

        if (StringUtils.isNotBlank(errorMsg)) {
            return new ContentRestResult(false, null, errorMsg);
        }

        try {
            if (StringUtils.isNotBlank(aeaImServiceResult.getServiceResultId())) {
                aeaImServiceResultService.updateAeaImServiceResult(aeaImServiceResult, files, request);
            } else {
                aeaImServiceResult.setIsExternalUpload("0");
                aeaImServiceResultService.saveAeaImServiceResult(aeaImServiceResult, files, request);
            }

            return new ContentRestResult(true, null, aeaImServiceResult.getServiceResultId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult(false, null, e.getMessage());
        }

    }

    @ApiOperation(value = "业主处理服务结果信息", notes = "业主处理服务结果信息", httpMethod = "POST")
    @PostMapping("/dealServiceResult")
    public ContentRestResult dealServiceResult(@Valid AeaImServiceResult aeaImServiceResult,
                                               BindingResult bindingResult, HttpServletRequest request) {
        String errorMsg = BusinessUtils.checkBindingResult(bindingResult);

        if (StringUtils.isNotBlank(errorMsg)) {
            return new ContentRestResult(false, null, errorMsg);
        }

        try {
            if (StringUtils.isNotBlank(aeaImServiceResult.getServiceResultId())) {
                if (aeaImServiceResultService.dealAeaImServiceResult(aeaImServiceResult, request)) {
                    return new ContentRestResult(true, null, aeaImServiceResult.getServiceResultId());
                }
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentRestResult(false, null, e.getMessage());
        }
        return new ContentRestResult(false);
    }

    @ApiOperation(value = "删除服务结果附件", notes = "删除服务结果附件", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailId", value = "附件id,多个以,隔开", required = true)
    })
    @PostMapping("/deleteServiceResultFile")
    public RestResult deleteServiceResultFile(String detailId) {
        try {
            if (StringUtils.isBlank(detailId)) {
                return new RestResult(false, "删除失败：没有可删除文件!");
            }
            if (aeaImServiceResultService.deleteServiceResultFile(detailId)) {
                return new RestResult(true);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new RestResult(false, e.getMessage());
        }
        return new RestResult(false, "删除失败!");
    }

    @ApiOperation(value = "根据id获取服务结果信息", notes = "根据id获取服务结果信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceResultId", value = "服务结果ID", required = true)
    })
    @PostMapping("/getAeaImServiceResult/{serviceResultId}")
    public ContentRestResult<AeaImServiceResult> getAeaImServiceResult(@PathVariable("serviceResultId") String serviceResultId) throws Exception {
        if (StringUtils.isNotBlank(serviceResultId)) {
            AeaImServiceResult aeaImServiceResult = aeaImServiceResultService.getAeaImServiceResultById(serviceResultId);
            if (aeaImServiceResult != null) {
                return new ContentRestResult(true, aeaImServiceResult);
            }
        }
        return new ContentRestResult(false);
    }

    @ApiOperation(value = "删除服务结果信息", notes = "删除服务结果信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceResultId", value = "服务结果ID", required = true)
    })
    @PostMapping("/deleteAeaImServiceResult/{serviceResultId}")
    public RestResult deleteAeaImServiceResultById(@PathVariable("serviceResultId") String serviceResultId) throws Exception {
        aeaImServiceResultService.deleteAeaImServiceResultById(serviceResultId);
        return new RestResult(true);
    }

    //20191122 新增接口
    @ApiOperation(value = "上传服务结果-新接口，需要判断是否推动流程", notes = "上传服务结果-新接口，需要判断是否推动流程", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "项目采购ID", required = true)
            , @ApiImplicitParam(name = "matinstIds", value = "材料实例ID", required = true)
            , @ApiImplicitParam(name = "aeaImServiceResult", value = "服务结果对象", required = true)
            , @ApiImplicitParam(name = "unitBiddingId", value = "单位竞价ID", required = true)
    })
    @PostMapping("/saveOrUpdateSerivceResult")
    public ResultForm uploadServiceResult(String[] matinstIds, @Valid ServiceResultVo serviceResultVo) throws Exception {
        aeaImServiceResultService.saveOrUpdateSerivceResult(matinstIds, serviceResultVo);

        return new ResultForm(true, "success");
    }

    @ApiOperation(value = "查询采购项目材料及实例列表", notes = "上传服务结果-新接口，需要判断是否推动流程")
    @ApiImplicitParam(name = "projPurchaseId", value = "项目采购ID", required = true)
    @GetMapping("/getProjPurchaseMatinstList")
    public ContentResultForm<MatinstVo> getProjPurchaseMatinstList(String projPurchaseId) throws Exception {
        //查询已经存在的服务结果，用来反显
        AeaImServiceResult serviceResult = aeaImServiceResultMapper.getServiceResultByProjPurchaseId(projPurchaseId);
        ServiceResultVo vo = new ServiceResultVo();
        BeanUtils.copyProperties(serviceResult, vo);
        List<MatinstVo> vos = aeaImServiceResultService.getProjPurchaseMatinstList(projPurchaseId);
        Map<String, Object> map = new HashMap<>();
        map.put("serviceResult", vo);
        map.put("matList", vos);
        return new ContentResultForm(true, map, "success");
    }

}
