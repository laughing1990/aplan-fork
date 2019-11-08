package com.augurit.aplanmis.front.subject.linkman.controller;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.front.subject.linkman.serivce.GlobalLinkManService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 全局联系人库-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/rest/linkman")
@Api(value = "全局联系人库", tags = "全局联系人库接口")
@Slf4j
public class GlobalLinkmanController {

    private static Logger logger = LoggerFactory.getLogger(GlobalLinkmanController.class);

    @Autowired
    private GlobalLinkManService globalLinkManService;
    @Autowired
    private FileUtilsService fileUtilsService;

    @GetMapping("/listAeaLinkMans")
    @ApiOperation("全局联系人库 --> 分页查询全局联系人列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询字段：姓名，身份证号", required = false, dataType = "string", paramType = "query", readOnly = true),

    })
    public ResultForm listAeaLinkMans(String keyword, Integer pageSize, Integer pageNum) throws Exception {
        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
        PageInfo<AeaLinkmanInfo> pageList = globalLinkManService.listAeaLinkMans(keyword, page);
        return new ContentResultForm<>(true, pageList);
//        return PageHelper.toEasyuiPageInfo(pageList);
    }


    @GetMapping("/listAeaLinkMansByPage")
    @ApiOperation("全局联系人库 --> 分页查询全局联系人列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询字段：姓名，身份证号", required = false, dataType = "string", paramType = "query", readOnly = true),

    })
    public ResultForm listAeaLinkMansByPage(String keyword, Page page) throws Exception {
        PageInfo<AeaLinkmanInfo> pageList = globalLinkManService.listAeaLinkMans(keyword, page);
        return new ContentResultForm<>(true, pageList);
    }

    @GetMapping("/getAeaLinkManById")
    @ApiOperation("全局联系人库 -->根据ID查询联系人信息（含证照文件列表）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "联系人id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getAeaLinkmanInfo(String id) throws Exception {
        if (id != null) {
            logger.debug("根据ID获取AeaLinkmanInfo对象，ID为：{}", id);
            List<BscAttFileAndDir> list = globalLinkManService.getAttFiles(id, "AEA_LINKMAN_INFO", "LINKMAN_INFO_ID");
            AeaLinkmanInfo aeaLinkmanInfo = globalLinkManService.getAeaLinkmanInfoById(id);
            aeaLinkmanInfo.setFileList(list);
            return new ContentResultForm<>(true, aeaLinkmanInfo);
//            return aeaLinkmanInfo;
        } else {
            logger.debug("构建新的AeaLinkmanInfo对象");
            return new ContentResultForm<>(true, new AeaLinkmanInfo());
//            return new AeaLinkmanInfo();
        }
    }

    @PostMapping("/updateAeaLinkMan")
    @ApiOperation("全局联系人库 -->更新客户档案信息Form对象")
    public ResultForm updateAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaLinkmanInfo);
        globalLinkManService.updateAeaLinkmanInfo(aeaLinkmanInfo);
        return new ResultForm(true);
    }

    @PostMapping("/saveAeaLinkMan")
    @ApiOperation("全局联系人库 -->保存联系人表")
    @ApiImplicitParams({@ApiImplicitParam(name = "tableName", value = "业务表名", required = true, type = "string"),
            @ApiImplicitParam(name = "pkName", value = "业务主键", required = true, type = "string")})
    public ResultForm saveAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo, BindingResult result,
                                         HttpServletRequest request, String tableName, String pkName) {
        try {
            if (result.hasErrors()) {
                logger.error("保存联系人表Form对象出错");
                throw new InvalidParameterException(aeaLinkmanInfo);
            }
            String linkmanInfoId = "";//单位ID，在新增时返回给后台
            if (StringUtils.isBlank(aeaLinkmanInfo.getLoginName())) {
                aeaLinkmanInfo.setLoginName(aeaLinkmanInfo.getLinkmanCertNo());
            }
            globalLinkManService.beforeSaveOrUpdateAeaUnitLinkman(aeaLinkmanInfo);//保存前先进行单位判定
            if (StringUtils.isNotBlank(aeaLinkmanInfo.getLinkmanInfoId())) {
                aeaLinkmanInfo.setModifier(SecurityContext.getCurrentUserName());
                globalLinkManService.updateAeaLinkmanInfo(aeaLinkmanInfo);
            } else {
                aeaLinkmanInfo.setLinkmanInfoId(UUID.randomUUID().toString());
                aeaLinkmanInfo.setCreater(SecurityContext.getCurrentUserName());
                globalLinkManService.saveAeaLinkmanInfo(aeaLinkmanInfo);
                linkmanInfoId = aeaLinkmanInfo.getLinkmanInfoId();
            }
            globalLinkManService.saveOrUpdateAeaUnitLinkmanByAeaLinkmanInfo(aeaLinkmanInfo);
            fileUtilsService.uploadAttachments(tableName, pkName, aeaLinkmanInfo.getLinkmanInfoId(), null, request);
            return new ResultForm(true, linkmanInfoId);
        } catch (Exception e) {
            e.getStackTrace();
            return new ContentResultForm<>(false, aeaLinkmanInfo, e.getMessage());
//            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/deleteAeaLinkManById")
    @ApiOperation("全局联系人库 -->根据ID删除联系人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "联系人id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm deleteAeaLinkmanInfoById(String id) throws Exception {
        logger.debug("删除联系人表Form对象，对象id为：{}", id);
        if (id != null) {
            globalLinkManService.deleteAeaLinkmanInfoById(id);
        }
        return new ResultForm(true);
    }

    @GetMapping("/batchDeleteAeaLinkMan")
    @ApiOperation("批量删除联系人信息")
    @ApiImplicitParam(name = "ids", value = "需要删除的联系人IDs", required = true, type = "list")
    public ResultForm batchDeleteAeaLinkMan(String[] ids) {
        if (ids != null && ids.length > 0) {
            globalLinkManService.batchDeleteAeaLinkMan(ids);
        }
        return new ResultForm(true);
    }

    @GetMapping("/generatePassword")
    @ApiOperation("全局联系人库 -->生成密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "linkmanInfoId", value = "联系人id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm generatePassWord(String linkmanInfoId, HttpServletRequest request) {
        try {
            Map<String,Object> map=globalLinkManService.generatePassWord(linkmanInfoId, request);
            return new ContentResultForm<>(true, map,"密码生成成功!");
        } catch (Exception e) {
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/deleteFile")
    @ApiOperation("批量删除联系人证照文件")
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
