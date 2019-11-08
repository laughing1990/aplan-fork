package com.augurit.aplanmis.supermarket.notice.controller;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.augurit.aplanmis.supermarket.notice.service.AeaImContractNoticeService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "合同公告接口")
@RequestMapping("/aea/supermarket/notice")
@RestController
public class AeaImContractNoticeController {
    private  static Logger log = LoggerFactory.getLogger(AeaImContractNoticeController.class);
    @Autowired
    private AeaImContractNoticeService aeaImContractNoticeService;

    @ApiOperation(value = "查询合同公告列表", notes = "查询合同公告列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projName", value = "采购项目名称"),
            @ApiImplicitParam(name = "pageNum", value = "当前页"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数")
    })
    @GetMapping("/listContractNotice")
    public ResultForm listSelectionNotice(QueryProjPurchaseVo queryParam) {
        try {
            Page page = new Page(queryParam.getPageNum(), queryParam.getPageSize() > 0 ? queryParam.getPageSize()  : 10);
            List<AeaImProjPurchase> list = aeaImContractNoticeService.listContractNotice(queryParam, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
        } catch (Exception e) {
            log.error("列表查询合同公告列表出错！", e);
            return new ContentResultForm(false, e.getMessage());
        }
    }


    @ApiOperation(value = "根据采购项目id查询合同公告详情", notes = "根据采购项目id查询合同公告详情。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projPurchaseId", value = "采购项目id")
    })
    @GetMapping("/getContractNoticeDetail")
    public ResultForm getContractNoticeDetail(String  projPurchaseId) {
        try {
            if(Strings.isNullOrEmpty(projPurchaseId)) {
                return new ContentResultForm(false, "传入参数projPurchaseId不能为空！");
            }

            AeaImProjPurchase contractnNotice = aeaImContractNoticeService.getContractNoticeByProjPurchaseId(projPurchaseId);
            return new ContentResultForm(true, contractnNotice);
        } catch (Exception e) {
            log.error("查询中选公告详情出错！", e);
            return new ContentResultForm(false);
        }
    }
}
