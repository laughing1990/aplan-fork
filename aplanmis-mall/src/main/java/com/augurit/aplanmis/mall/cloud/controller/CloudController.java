package com.augurit.aplanmis.mall.cloud.controller;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.cloud.BscCloudCommonService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.BscAttDirParamVo;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.cloud.service.CloudService;
import com.augurit.aplanmis.mall.cloud.vo.AttAndDirVo;
import com.augurit.aplanmis.mall.cloud.vo.AttAndDirWithChildVo;
import com.augurit.aplanmis.mall.cloud.vo.CloudDirListVo;
import com.augurit.aplanmis.mall.cloud.vo.MoveFilesParamVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/rest/cloud")
@Api(value = "我的云盘", tags = "法人空间 --> 我的云盘相关接口")
public class CloudController {
    Logger logger = LoggerFactory.getLogger(CloudController.class);

    @Autowired
    CloudService cloudService;
    @Autowired
    private BscCloudCommonService bscCloudCommonService;

    @Autowired
    private FileUtilsService fileUtilsService;


    @PostMapping("/uploadCloudFiles")
    @ApiOperation("云盘 --> 云盘电子文件上传")
    @ApiImplicitParams({@ApiImplicitParam(name = "dirId", value = "文件夹ID", required = false, type = "string")})
    public ResultForm uploadCloudFiles(String dirId, HttpServletRequest request) {
        try {
            cloudService.uploadCloudFiles(dirId, request);
            return new ContentResultForm(true,"上传成功!" );
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }


    @GetMapping("dirAndAtt/list/{dirId}")
    @ApiOperation(value = "云盘 --> 获取云盘文件目录及文件列表(dirId为'root'查询根目录及文件,其他值则查询子目录及文件列表)")
    @ApiImplicitParam(value = "目录ID" , name = "dirId" ,required = true ,dataType = "String")
    public ContentResultForm<AttAndDirVo> getMyCloudChildDirAndAttList(@PathVariable("dirId") String dirId,HttpServletRequest request){
        try {
            if ("root".equals(dirId)){//查询根目录及文件列表
                LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
                if("1".equals(loginInfo.getIsPersonAccount())){//个人
                    return new ContentResultForm(true,cloudService.getMyCloudRootDirAndAttList("",loginInfo.getUserId()),"查询成功");
                }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                    return new ContentResultForm(true,cloudService.getMyCloudRootDirAndAttList("",loginInfo.getUserId()),"查询成功");
                }else{//企业
                    return new ContentResultForm(true,cloudService.getMyCloudRootDirAndAttList(loginInfo.getUnitId(),""),"查询成功");
                }
            }
            return new ContentResultForm(true,cloudService.getMyCloudChildDirAndAttList(dirId),"查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","获取云盘文件子目录及文件列表发生异常");
        }
    }

    @GetMapping("all/dirAndAtt/list")
    @ApiOperation(value = "云盘 --> 获取云盘文件目录及文件列表(全部层级)")
    public ContentResultForm<AttAndDirWithChildVo> getMyCloudDirAndAttList(HttpServletRequest request){
        try {
            return new ContentResultForm(true,cloudService.getMyCloudDirAndAttList(request),"查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","获取云盘文件子目录及文件列表发生异常");
        }
    }

    @GetMapping("root/dir/tree")
    @ApiOperation(value = "云盘 --> 根据用户信息查询文件夹Tree")
    public ContentResultForm<List<CloudDirListVo>> getDirTreeByUser(HttpServletRequest request){
        LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
        try {
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                return new ContentResultForm(true,cloudService.getDirTreeByUser("",loginInfo.getUserId()),"查询成功");
            }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                return new ContentResultForm(true,cloudService.getDirTreeByUser("",loginInfo.getUserId()),"查询成功");
            }else{//企业
                return new ContentResultForm(true,cloudService.getDirTreeByUser(loginInfo.getUnitId(),""),"查询成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","根据用户信息查询文件夹Tree发生异常");
        }
    }

    @GetMapping("att/list")
    @ApiOperation(value = "云盘 --> 根据文件夹查询文件列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "目录ID" , name = "dirId" ,required = true ,dataType = "String"),
            @ApiImplicitParam(value = "当前页" , name = "pageNum" ,required = true ,dataType = "int"),
            @ApiImplicitParam(value = "每页记录数" , name = "pageSize" ,required = true ,dataType = "int"),
            @ApiImplicitParam(value = "搜索关键词" , name = "keyword" ,required = false ,dataType = "String")})
    public PageInfo<BscAttForm> getAttsByDirId(String dirId, int pageNum, int pageSize, String keyword,HttpServletRequest request){
        try {
            return cloudService.getAttsByDirId(dirId,pageNum,pageSize,keyword,SessionUtil.isPersonAccount(request));
        } catch (Exception e) {
            e.printStackTrace();
            return new PageInfo<>();
        }
    }

    @GetMapping("attByKeyword/list/{keyword}")
    @ApiOperation(value = "云盘 --> 根据搜索关键词查询文件列表")
    @ApiImplicitParam(value = "搜索关键词" , name = "keyword" ,required = false ,dataType = "String")
    public ContentResultForm<List<BscAttForm>> getAttsByDirId(@PathVariable("keyword") String keyword,HttpServletRequest request){
        try {
            return new ContentResultForm(true, cloudService.listAttLinkAndDetailList(keyword,request),"查询成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"查询发生异常");
        }
    }



    @PostMapping("insertOrUpdateRootDir")
    @ApiOperation(value = "云盘管理 --> 1.新增父级文件夹 2.新增子级文件夹 3.文件夹重命名")
    public ResultForm insertOrUpdateRootDir(BscAttDirParamVo bscAttDirParamVo, HttpServletRequest request) {
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
        try {
            String tableName = StringUtils.isNotBlank(loginVo.getUserId()) ? "AEA_LINKMAN_INFO" : "AEA_UNIT_INFO";
            String pkName = StringUtils.isNotBlank(loginVo.getUserId()) ? "LINKMAN_INFO_ID" : "UNIT_INFO_ID";
            String recordId = StringUtils.isNotBlank(loginVo.getUserId()) ? loginVo.getUserId() : loginVo.getUnitId();
            if (StringUtils.isBlank(recordId)) return new ResultForm(false, "缺少请求参数！");
            bscCloudCommonService.insertOrUpdateRootDir(bscAttDirParamVo, tableName, pkName, recordId);
            return new ContentResultForm<>(true, bscAttDirParamVo.getDirId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @PostMapping("renameCloudFile")
    @ApiOperation(value = "云盘管理 --> 云盘文件重命名")
    @ApiImplicitParams({@ApiImplicitParam(value = "文件ID" , name = "detailId" ,required = true ,dataType = "String"),
            @ApiImplicitParam(value = "重命名文件名" , name = "attName" ,required = true ,dataType = "String")})
    public ResultForm renameCloudFile(String detailId,String attName , HttpServletRequest request) {
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
        if (loginVo ==null) return new ResultForm(false,"未登录");
        try {
            cloudService.renameAttName(detailId, attName);
            return new ContentResultForm<>(true, "","重命名成功!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }


    @GetMapping("deleteDirAndFiles/{dirId}")
    @ApiOperation(value = "云盘管理 --> 删除文件夹(会把子级的文件夹及文件也删掉)")
    @ApiImplicitParam(value = "目录ID", name = "dirId", required = true, dataType = "String")
    public ResultForm deleteDirAndFiles(@PathVariable("dirId") String dirId, HttpServletRequest request) {
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
        try {
            String recordId = StringUtils.isNotBlank(loginVo.getUserId()) ? loginVo.getUserId() : loginVo.getUnitId();
            if (StringUtils.isBlank(dirId) || StringUtils.isBlank(recordId)) return new ResultForm(false, "缺少请求参数！");
            cloudService.deleteDirAndFiles(dirId, recordId);
            return new ContentResultForm<>(true, dirId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    //批量移动文件
    @GetMapping("moveFilesFromDir")
    @ApiOperation(value = "云盘管理 --> 批量移动文件")
    public ResultForm moveFilesFromDir(MoveFilesParamVo moveFilesParamVo, HttpServletRequest request) {
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
        try {
            String recordId = StringUtils.isNotBlank(loginVo.getUserId()) ? loginVo.getUserId() : loginVo.getUnitId();
            String currentDirId = moveFilesParamVo.getCurrentDirId();
            String targetDirId = moveFilesParamVo.getTargetDirId();
            String[] detailIds = moveFilesParamVo.getDetailIds().split(",");
            if (detailIds == null || detailIds.length == 0 || StringUtils.isBlank(targetDirId) || StringUtils.isBlank(currentDirId) || StringUtils.isBlank(recordId))
                return new ResultForm(false, "缺少请求参数！");
            cloudService.moveFilesFromDir(currentDirId, targetDirId, detailIds, recordId);
            return new ContentResultForm<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @GetMapping("/att/download")
    @ApiOperation(value = "云盘管理--> 批量下载电子件")
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

    @GetMapping("/att/downloadCovertPdf")
    @ApiOperation(value = "云盘管理--> 下载电子件转换的pdf文件")
    @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", required = true)
    public ContentResultForm downloadCovertPdf(String detailIds, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(detailIds)) {
                return new ContentResultForm<>(false, null, "找不到文件!");
            }
            String[] str = detailIds.split(",");
            fileUtilsService.downloadAttachment(str, response, request, true);
            return new ContentResultForm<>(true);
        } catch (Exception e) {
            return new ContentResultForm<>(false, null, "找不到文件!");
        }
    }
}
