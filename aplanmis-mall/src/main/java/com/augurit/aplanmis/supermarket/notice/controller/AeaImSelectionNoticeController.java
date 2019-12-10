package com.augurit.aplanmis.supermarket.notice.controller;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.augurit.aplanmis.supermarket.notice.service.AeaImSelectionNoticeService;
import com.augurit.aplanmis.supermarket.notice.service.SelectionNoticeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/19/019 18:16
 */
@Api(description = "中选公告接口")
@RequestMapping("/aea/supermarket/notice")
@RestController
public class AeaImSelectionNoticeController {
    private  static Logger log = LoggerFactory.getLogger(AeaImSelectionNoticeController.class);
    @Autowired
    private AeaImSelectionNoticeService selectionNoticeService;

    @ApiOperation(value = "查询中选公告列表", notes = "查询中选公告列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projName", value = "采购项目名称"),
            @ApiImplicitParam(name = "applicant", value = "项目业主名称"),
            @ApiImplicitParam(name = "biddingType", value = "竞价类型：1 随机中标，2 自主选择"),
            @ApiImplicitParam(name = "startTime", value = "开始时间,yyyy-MM-dd"),
            @ApiImplicitParam(name = "endTime", value = "结束时间,yyyy-MM-dd"),
            @ApiImplicitParam(name = "pageNum", value = "当前页"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数"),
            @ApiImplicitParam(name = "serviceId", value = "服务id['a','b']字符串")
    })
    @PostMapping("/listSelectionNotice")
    public ResultForm listSelectionNotice(QueryProjPurchaseVo queryParam){
        try {
            Page page = new Page(queryParam.getPageNum(), queryParam.getPageSize() > 0 ? queryParam.getPageSize()  : 10);
            List<AeaImProjPurchase> list = selectionNoticeService.listSelectionNotice(queryParam,page);
            return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
        } catch (Exception e) {
            log.error("列表查询中选公告列表出错！", e);
            return new ContentResultForm<>(false);
        }
    }


    @ApiOperation(value = "根据采购项目id查询中选公告详情", notes = "根据采购项目id查询中选公告详情。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "采购项目id")
    })
    @GetMapping("/getSelectionNoticeDetail")
    public ResultForm getSelectionNoticeDetail(String  projPurchaseId){
        try {
            if (Strings.isNullOrEmpty(projPurchaseId)) {
                throw new Exception("传入参数projPurchaseId不能为空！");
            }
            SelectionNoticeVo purchaseDetailVo = selectionNoticeService.getSelectionNoticeByProjPurchaseId(projPurchaseId);
            return new ContentResultForm<>(true, purchaseDetailVo);
        } catch (Exception e) {
            log.error("查询中选公告详情出错！", e);
            return new ContentResultForm<>(false);
        }
    }
}
