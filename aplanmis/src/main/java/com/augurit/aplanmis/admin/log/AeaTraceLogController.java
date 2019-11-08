package com.augurit.aplanmis.admin.log;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuLogUserLogin;
import com.augurit.aplanmis.common.domain.AeaTraceLog;
import com.augurit.aplanmis.common.service.admin.log.AeaTraceLogService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 *
 * 系统操作日志-Controller 页面控制转发类
 *
 * @author keriezhang
 * @date 2016/10/31
 *
 */
@Api(description = "系统跟踪日志接口",tags={"系统跟踪日志接口"})
@RestController
@RequestMapping("/aea/trace/log")
public class AeaTraceLogController {

    private static Logger logger = LoggerFactory.getLogger(AeaTraceLogController.class);

    @Autowired
    private AeaTraceLogService aeaTraceLogService;

    @ApiOperation(value = "分页查询用户登录日志数据", notes = "分页查询用户登录日志数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "keyword", value = "非必填", dataType = "String" ,paramType = "query"),
        @ApiImplicitParam(name = "startTime", value = "非必填", dataType = "Long" ,paramType = "query"),
        @ApiImplicitParam(name = "endTime", value = "非必填", dataType = "Long" ,paramType = "query"),
        @ApiImplicitParam(name = "isOnlyShowUser", value = "非必填", dataType = "Boolean" ,paramType = "query"),
        @ApiImplicitParam(name = "page", value = "分页信息",  dataType = "PageParam")
    })
    @RequestMapping(value = "/listByPage.do", method = {RequestMethod.GET, RequestMethod.POST})
    public EasyuiPageInfo<AeaTraceLog> listSysTraceLog(String keyword, Long startTime, Long endTime,
                                                       Boolean isOnlyShowUser, @ModelAttribute PageParam page){
        AeaTraceLog log = new AeaTraceLog();
        log.setKeyword(keyword);
        if(startTime!=null){
            log.setStartTime(new Date(startTime));
        }
        if(endTime!=null){
            log.setEndTime(new Date(endTime));
        }
        if(isOnlyShowUser){
            log.setUserName(SecurityContext.getCurrentUserName());
        }
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", log);
        PageInfo pageInfo = aeaTraceLogService.listAeaTraceLog(log, page.convertPage());
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @ApiOperation(value = "通过日志id获取日志数据",notes = "通过日志id获取日志数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "日志id, 默认必填", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getLogById.do")
    public AeaTraceLog getLoginLogById(String id)  {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        return aeaTraceLogService.getAeaTraceLogById(id);
    }

    @ApiOperation(value = "通过日志id删除数据",notes = "通过日志id删除数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "日志id, 默认必填", required = true, dataType = "String", paramType = "query")
    })
    @DeleteMapping("/deleteLogById.do")
    public ResultForm deleteLogById(String id)  {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaTraceLogService.deleteAeaTraceLogById(id);
        return new ResultForm(true);
    }

    @ApiOperation(value = "通过日志ids集合删除数据",notes = "通过日志ids集合删除数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "日志ids, 默认必填", required = true, dataType = "String", paramType = "query")
    })
    @DeleteMapping("/deleteLogMore.do")
    public ResultForm deleteLogMore(String[] ids)  {

        if(ids!=null&&ids.length>0){
            aeaTraceLogService.deleteLogMore(ids);
            return new ResultForm(true);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }
}