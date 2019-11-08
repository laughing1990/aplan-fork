package com.augurit.aplanmis.front.approve.controller;

import com.augurit.agcloud.bsc.domain.BscAttDir;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.sc.att.service.BscAttDirService;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.front.approve.service.ApproveAttFileService;
import com.augurit.aplanmis.front.approve.vo.AttDirsVo;
import com.augurit.aplanmis.front.approve.vo.BscAttFormVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/approve/att")
@Api(value = "审批详情页-附件管理", tags = "审批详情页-附件管理")
@Slf4j
public class ApproveAttFileController {

    @Autowired
    private ApproveAttFileService approveAttFileService;
    @Autowired
    private IBscAttService bscAttService;
    @Autowired
    private BscAttDirService bscAttDirService;
    @Autowired
    private FileUtilsService fileUtilsService;

    @GetMapping("/dirs/file/list")
    @ApiOperation(value = "业务审批 --> 附件管理;获取附件管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isSeries", value = "1: 表示事项审批; 0: 表示阶段审批;1 单项审批", dataType = "string", required = true)
            , @ApiImplicitParam(name = "tableName", value = "表明", dataType = "string", required = true)
            , @ApiImplicitParam(name = "attType", value = "附件类型", dataType = "string")
            , @ApiImplicitParam(name = "dirId", value = "文件夹地址ID，为空时查询文件夹列表，", dataType = "string")
            , @ApiImplicitParam(name = "recordIds", value = "业务表主键ID", dataType = "array", required = true)
    })
    public ResultForm getDirsOrFileList(String dirId, String[] recordIds, String tableName, String attType, HttpServletRequest request, String isSeries) throws Exception {
        AttDirsVo vo = approveAttFileService.getDirsOrFileList(dirId, recordIds, tableName, attType, request, isSeries);
//        List<AttDirTreeVo> vo = approveAttFileService.getDirsOrFileListTree(dirId, recordIds, tableName, attType, request, isSeries);

        return new ContentResultForm<>(true, vo);
    }

    @GetMapping("/file/search")
    @ApiOperation(value = "业务审批 --> 附件管理;文件搜索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页参数", dataTypeClass = Page.class, required = true)
            , @ApiImplicitParam(name = "tableName", value = "表名", dataType = "string", required = true)
            , @ApiImplicitParam(name = "pkName", value = "附件类型", dataType = "string", required = true)
            , @ApiImplicitParam(name = "keyword", value = "搜索关键字", dataType = "string")
            , @ApiImplicitParam(name = "recordIds", value = "附件IDS", dataType = "array")
    })
    public ResultForm queryFileByKkeyword(String keyword, String tableName, String pkName, String[] recordIds, Page page) throws Exception {
        PageInfo<BscAttFileAndDir> pageList = new PageInfo<>();
        if (!StringUtils.isBlank(tableName) && !StringUtils.isBlank(pkName) && recordIds != null && recordIds.length > 0) {
            String orgId = SecurityContext.getCurrentOrgId();
            PageInfo<BscAttFileAndDir> pageInfo = approveAttFileService.searchFileAndDirsSimple(keyword, orgId, tableName, pkName, recordIds, page);
            List<BscAttFileAndDir> list = pageInfo.getList();
            List<BscAttForm> officForm = bscAttService.searchOfficAtt(recordIds, orgId, keyword);
            Iterator var10 = officForm.iterator();

            while (var10.hasNext()) {
                BscAttForm from = (BscAttForm) var10.next();
                BscAttFileAndDir fileAndDir = new BscAttFileAndDir();
                fileAndDir.setFileId(from.getDetailId());
                fileAndDir.setFileName(from.getAttName());
                fileAndDir.setFileType(from.getAttFormat());
                fileAndDir.setParentId("pwpf_2018_augurit");
                fileAndDir.setParentName("批文批复");
            }

            List<BscAttForm> applyForm = bscAttService.searchApplyAtt(recordIds, orgId, keyword);
            Iterator var15 = applyForm.iterator();

            while (var15.hasNext()) {
                BscAttForm from = (BscAttForm) var15.next();
                BscAttFileAndDir fileAndDir = new BscAttFileAndDir();
                fileAndDir.setFileId(from.getDetailId());
                fileAndDir.setFileName(from.getAttName());
                fileAndDir.setFileType(from.getAttFormat());
                fileAndDir.setParentId("sbcl_2018_augurit");
                fileAndDir.setParentName("申办材料");
            }
        }
        return new ContentResultForm<>(true, pageList);
    }

    /**
     * 新增文件夹
     *
     * @param attDirsVo
     * @return
     */
    @PostMapping("/file/create/dir")
    @ApiOperation(value = "业务审批 --> 附件管理-创建文件夹")
    public ResultForm addBscAttDirs(AttDirsVo attDirsVo) throws Exception {
        BscAttDir dir = new BscAttDir();
        BeanUtils.copyProperties(attDirsVo, dir);
        dir.setDirId(UUID.randomUUID().toString());
        dir.setDirCode(String.valueOf(System.currentTimeMillis()));
        dir.setCreater(SecurityContext.getCurrentUserName());
        dir.setCreateTime(new Date());
        dir.setStoreType("mongodb");
        dir.setOrgId(SecurityContext.getCurrentOrgId());
        if (StringUtils.isBlank(dir.getParentId())) {
            dir.setParentId((String) null);
            dir.setDirSeq(dir.getDirId());
            dir.setDirNameSeq(dir.getDirName());
            dir.setDirLevel("0");
        } else {
            BscAttDir parentDir = this.bscAttDirService.getBscAttDirById(dir.getParentId());
            if (parentDir != null) {
                dir.setParentId(parentDir.getDirId());
                dir.setDirSeq(parentDir.getDirSeq() + "." + dir.getDirId());
                dir.setDirNameSeq(parentDir.getDirNameSeq() + "." + dir.getDirName());
                dir.setDirLevel(parentDir.getDirLevel());
            }
        }
        BscAttLink link = new BscAttLink();
        link.setLinkId(UUID.randomUUID().toString());
        link.setDirId(dir.getDirId());
        link.setPkName(attDirsVo.getPkName() == null ? "APPLYINST_ID" : attDirsVo.getPkName());
        link.setTableName(attDirsVo.getTableName() == null ? "AEA_HI_APPLYINST" : attDirsVo.getTableName());
        link.setRecordId(attDirsVo.getRecordId());
        link.setLinkType("d");
        bscAttDirService.saveBscAttDirAndBscAttLink(dir, link);
        return new ContentResultForm<>(true, dir, "add success");
    }

    @PostMapping("/file/upload")
    @ApiOperation(value = "业务审批 --> 附件管理-文件夹下上传文件")
    public ResultForm uploadFileAtt(HttpServletRequest request, BscAttFormVo vo) throws Exception {
        fileUtilsService.uploadAttachments(vo.getTableName(), vo.getPkName(), vo.getRecordId(), vo.getDirId(), request);
        return new ResultForm(true);
    }

    @PostMapping("/file/dir/delete")
    @ApiOperation(value = "业务审批 --> 附件管理-删除附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailIds", value = "待删除附件IDS数组", required = true)
            , @ApiImplicitParam(name = "isSeries", value = "1: 表示事项审批; 0: 表示阶段审批;1 单项审批", allowableValues = "0,1")
    })
    public ResultForm deleteFileByDetailsId(String[] detailIds, BscAttFormVo vo, String isSeries, HttpServletRequest request) throws Exception {
        fileUtilsService.deleteAttachments(detailIds);
        if (null != vo && StringUtils.isNotBlank(isSeries)) {
            AttDirsVo dirsOrFileList = approveAttFileService.getDirsOrFileList(vo.getDirId(), vo.getRecordId().split(","), vo.getTableName(), vo.getPkName(), request, isSeries);
            return new ContentResultForm<>(true, dirsOrFileList, "success");
        }
        return new ContentResultForm<>(true, new AttDirsVo(), "success");
    }

    @PostMapping("/file/delete/dir")
    @ApiOperation(value = "业务审批 --> 附件管理-删除文件夹")
    public ResultForm deleteDirs(BscAttFormVo vo) throws Exception {
        if (null == vo || StringUtils.isBlank(vo.getDirId()))
            return new ContentResultForm<>(true, new AttDirsVo(), "success");
        String dirId = vo.getDirId();
        if ("sbcl_2018_augurit".equals(dirId) || "pwpf_2018_augurit".equals(dirId)) {
            return new ContentResultForm<>(false, new AttDirsVo(), "default folder can not delete");
        }
        bscAttDirService.deleteTreeDir(vo.getDirId());
        return new ContentResultForm<>(true, new AttDirsVo(), "success");
    }


}
