package com.augurit.aplanmis.front.subject.unit.controller;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.front.subject.unit.service.GlobalApplicantService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 全局单位库-Controller 页面控制转发类
 */

@RestController
@RequestMapping("/rest/applicant")
@Api(value = "全局单位库", tags = "全局单位库接口")
@Slf4j
public class GlobalApplicantController {

    //public static final String BACKEND_APPLICANT_GLOBAL_VIEW = "ui-jsp/global_view/global_applicant/global_applicant_view";//全局单位库
    public static final String BACKEND_APPLICANT_GLOBAL_VIEW = "global/applicantview";//全局单位库
    private static Logger logger = LoggerFactory.getLogger(GlobalApplicantController.class);

    @Autowired
    private GlobalApplicantService globalApplicantService;
    /*   @Autowired
       private BpmProcessRestService bpmProcessRestService;
       @Autowired
       private AeaBusinessService aeaBusinessService;*/
    @Autowired
    private FileUtilsService fileUtilsService;

    @GetMapping("/listApplicants")
    @ApiOperation("全局单位库 --> 分页查询全局单位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询字段：企业名称，企业证照号", required = false, dataType = "string", paramType = "query", readOnly = true),

    })
//    public ResultForm listApplicants(String keyword, Integer pageSize, Integer pageNum) {
    public ResultForm listApplicants(String keyword, Page page) {
//        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
        PageInfo<AeaUnitInfo> pageList = globalApplicantService.listApplicants(keyword, page);
        return new ContentResultForm<>(true, pageList);
//        return PageHelper.toEasyuiPageInfo(pageList);
    }

    @GetMapping("/getApplicantById")
    @ApiOperation("全局单位库 -->根据ID查询单位信息（含证照文件列表）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单位id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getApplicantById(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            List<BscAttFileAndDir> list = globalApplicantService.getAttFiles(id, "AEA_UNIT_INFO", "UNIT_INFO_ID");
            AeaUnitInfo aeaUnitInfo = globalApplicantService.getApplicantById(id);
            aeaUnitInfo.setFileList(list);
            return new ContentResultForm<>(true, aeaUnitInfo);
        } else {
            return new ContentResultForm<>(true, new AeaUnitInfo());
        }

    }


    @PostMapping("/saveGlobalApplicant")
    @ApiOperation("全局单位库 -->保存单位信息")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "tableName", value = "业务表名", required = true, type = "string"),
                    @ApiImplicitParam(name = "pkName", value = "业务主键", required = true, type = "string")}
    )
    public ResultForm saveGlobalApplicant(AeaUnitInfo aeaUnitInfo, HttpServletRequest request, String tableName, String pkName) {
        try {
            String unitInfoId = "";//单位ID，在新增时返回给后台
            aeaUnitInfo.setLoginName(aeaUnitInfo.getUnifiedSocialCreditCode());
            if (StringUtils.isBlank(aeaUnitInfo.getIsImUnit()) || "null".equalsIgnoreCase(aeaUnitInfo.getIsImUnit())) {
                aeaUnitInfo.setIsImUnit("0");
            }
            if (StringUtils.isBlank(aeaUnitInfo.getIsOwnerUnit()) || "null".equalsIgnoreCase(aeaUnitInfo.getIsOwnerUnit())) {
                aeaUnitInfo.setIsOwnerUnit("0");
            }
            if (StringUtils.isBlank(aeaUnitInfo.getLoginName())) {
                aeaUnitInfo.setLoginName(aeaUnitInfo.getUnifiedSocialCreditCode());//证照号作为登录名
            }
            if (StringUtils.isBlank(aeaUnitInfo.getUnitInfoId()) || "null".equalsIgnoreCase(aeaUnitInfo.getUnitInfoId())) {
                unitInfoId = globalApplicantService.insertAeaApplicant(aeaUnitInfo);
            } else {
                globalApplicantService.updateAeaApplicant(aeaUnitInfo);
            }
            fileUtilsService.uploadAttachments(tableName, pkName, aeaUnitInfo.getUnitInfoId(), null, request);
            return new ResultForm(true, unitInfoId);
        } catch (Exception e) {
            e.getStackTrace();
            logger.debug(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/deleteGlobalApplicant")
    @ApiOperation("全局单位库 -->根据ID删除单位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单位id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm deleteGlobalApplicant(String id) {
        AeaUnitInfo aeaUnitInfo = globalApplicantService.getApplicantById(id);
        if (aeaUnitInfo != null) {
            globalApplicantService.deleteAeaApplicant(id);
            return new ResultForm(true, "删除成功!");
        } else {
            return new ResultForm(false, "没有相关单位信息");
        }

    }

   /* @RequestMapping("/addGloabalApplicant.do")
    public ResultForm addGlobalApplicant(AeaUnitInfo aeaUnitInfo){
        try {
            globalApplicantService.insertAeaApplicant(aeaUnitInfo);
        }catch(Exception e) {
            e.getStackTrace();
            logger.debug(e.getMessage());
        }
        return new ContentResultForm<>(true,aeaUnitInfo);
    }*/

    @GetMapping("/batchDeleteAeaApplicantByIds")
    @ApiOperation("批量删除单位库")
    @ApiImplicitParam(name = "ids", value = "需要删除的单位IDs", required = true, type = "list")
    public ResultForm batchDeleteAeaApplicantByIds(String[] ids) {

        if (ids != null && ids.length > 0) {
            try {
                List<String> canDelIds = new ArrayList<String>();
                List<String> notCanDelIds = new ArrayList<String>();
                for (int i = 0; i < ids.length; i++) {
                    AeaUnitInfo aeaUnitInfo = globalApplicantService.getApplicantById(ids[i]);
                    if (aeaUnitInfo != null) {
                        canDelIds.add(ids[i]);
                    } else {
                        notCanDelIds.add((ids[i]));
                    }
                }
                int len = canDelIds.size();
                ids = canDelIds.toArray(new String[len]);
                if (ids != null && ids.length > 0) {
                    globalApplicantService.batchDeleteApplicantByIds(ids);
                }
            } catch (Exception e) {
                e.getStackTrace();
                logger.debug(e.getMessage());
            }
        } else {
            return new ResultForm(false, "操作对象集合ids为空!");
        }
        return new ResultForm(true, "删除成功!");
    }

    @GetMapping("/generatePassword")
    @ApiOperation("全局单位库 -->生成密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unitInfoId", value = "单位id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm generatePassWord(String unitInfoId, HttpServletRequest request) {
        try {
            Map<String,Object> map=globalApplicantService.generatePassWord(unitInfoId, request);
            return new ContentResultForm<>(true, map,"密码生成成功!");
        } catch (Exception e) {
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/listApplicantsNoPage")
    @ApiOperation("获取单位信息")
    public List<AeaUnitInfo> listApplicantsNoPage(AeaUnitInfo aeaUnitInfo) {
        return globalApplicantService.listApplicantsNoPage(aeaUnitInfo);
    }

    @GetMapping("/deleteFile")
    @ApiOperation("批量删除单位证照文件")
    @ApiImplicitParam(name = "detailIds", value = "需要删除的证照文件IDs", required = true, type = "string")
    public ResultForm deleteFile(String detailIds) {
        try {
            if (StringUtils.isNotBlank(detailIds)) {
                String[] str = detailIds.split(",");
                fileUtilsService.deleteAttachments(str);
                return new ResultForm(true);
            } else {
                return new ResultForm(false, "缺少参数");

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }

    }
}
