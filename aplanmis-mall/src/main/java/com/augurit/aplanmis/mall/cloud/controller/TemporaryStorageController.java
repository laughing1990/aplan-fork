package com.augurit.aplanmis.mall.cloud.controller;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.cloud.BscCloudCommonService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.vo.BscAttDirParamVo;
import com.augurit.aplanmis.mall.cloud.service.TemporaryStorageService;
import com.augurit.aplanmis.mall.cloud.vo.CloudDirListVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/rest/temporary")
@Api(value = "申报指引", tags = "申报指引 --> 申报指引文件接口")
public class TemporaryStorageController {
    Logger logger = LoggerFactory.getLogger(TemporaryStorageController.class);

    @Autowired
    private TemporaryStorageService temporaryStorageService;
    @Autowired
    private BscCloudCommonService bscCloudCommonService;

    @Autowired
    private FileUtilsService fileUtilsService;
    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    protected String topOrgId;


    @PostMapping("/uploadCloudFiles")
    @ApiOperation("申报指引 --> 申报指引文件临时上传")
    @ApiImplicitParams({@ApiImplicitParam(name = "dirId", value = "文件夹ID", required = false, type = "string")})
    public ResultForm uploadCloudFiles(String dirId, HttpServletRequest request) {
        try {
            temporaryStorageService.uploadCloudFiles(dirId, request);
            return new ContentResultForm(true,"上传成功!" );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }
    
    @GetMapping("root/dir/tree")
    @ApiOperation(value = "申报指引 --> 根据用户信息查询文件夹Tree")
    public ContentResultForm<List<CloudDirListVo>> getDirTreeByUser(){
        try {
                return new ContentResultForm(true,temporaryStorageService.getDirTree(topOrgId),"查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","根据用户信息查询文件夹Tree发生异常");
        }
    }

    @GetMapping("att/list")
    @ApiOperation(value = "申报指引 --> 根据文件夹查询文件列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "目录ID" , name = "dirId" ,required = true ,dataType = "String"),
            @ApiImplicitParam(value = "当前页" , name = "pageNum" ,required = true ,dataType = "int"),
            @ApiImplicitParam(value = "每页记录数" , name = "pageSize" ,required = true ,dataType = "int"),
            @ApiImplicitParam(value = "搜索关键词" , name = "keyword" ,required = false ,dataType = "String")})
    public PageInfo<BscAttForm> getAttsByDirId(String dirId, int pageNum, int pageSize, String keyword,HttpServletRequest request){
        try {
            return temporaryStorageService.getAttsByDirId(dirId,pageNum,pageSize,keyword,topOrgId);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageInfo<>();
        }
    }

    @PostMapping("insertOrUpdateRootDir")
    @ApiOperation(value = "申报指引 --> 1.新增父级文件夹 2.新增子级文件夹 3.文件夹重命名")
    public ResultForm insertOrUpdateRootDir(BscAttDirParamVo bscAttDirParamVo) {
        try {
            bscCloudCommonService.insertOrUpdateRootDir(bscAttDirParamVo, "MALL_TEMPORARY_STORAGET", "MALL_TEMPORARY_STORAGET", "MALL_TEMPORARY_STORAGET");
            return new ContentResultForm<>(true, bscAttDirParamVo.getDirId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("deleteDirAndFiles/{dirId}")
    @ApiOperation(value = "申报指引 --> 删除文件夹(会把子级的文件夹及文件也删掉)")
    @ApiImplicitParam(value = "目录ID", name = "dirId", required = true, dataType = "String")
    public ResultForm deleteDirAndFiles(@PathVariable("dirId") String dirId) {
        try {
            temporaryStorageService.deleteDirAndFiles(dirId, "MALL_TEMPORARY_STORAGET",topOrgId);
            return new ContentResultForm<>(true, dirId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }


    @GetMapping("/att/download")
    @ApiOperation(value = "申报指引--> 批量下载电子件")
    @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", required = true)
    public ContentResultForm downloadAttachment(String detailIds, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(detailIds)) {
                return new ContentResultForm<>(false, null, "找不到文件!");
            }
            String[] str = detailIds.split(",");
            fileUtilsService.downloadAttachment(str, response, request, false);
            return new ContentResultForm<>(true);
        } catch (Exception e) {
            return new ContentResultForm<>(false, null, "找不到文件!");
        }
    }

}
