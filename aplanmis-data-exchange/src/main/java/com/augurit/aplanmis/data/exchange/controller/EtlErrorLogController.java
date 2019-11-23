package com.augurit.aplanmis.data.exchange.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlErrorLog;
import com.augurit.aplanmis.data.exchange.service.aplanmis.EtlErrorLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/10/21
 */
@RestController
@RequestMapping("/etl/error/log")
@Api(value = "错误日志接口", tags = "日志-错误详情")
public class EtlErrorLogController {

    @Autowired
    EtlErrorLogService etlErrorLogService;

    @GetMapping("/list")
    @ApiOperation(value = "分页查询错误日志", tags = "错误日志列表")
    public ContentResultForm<PageInfo<EtlErrorLog>> findEltErrorlog(EtlErrorLog etlErrorLog, Page page) {
        PageInfo<EtlErrorLog> etlErrorLogPageInfo = etlErrorLogService.listEtlErrorLog(etlErrorLog, page);
        return new ContentResultForm<>(true, etlErrorLogPageInfo);
    }
}
