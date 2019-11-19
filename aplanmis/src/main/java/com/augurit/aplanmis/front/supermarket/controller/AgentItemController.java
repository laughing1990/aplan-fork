package com.augurit.aplanmis.front.supermarket.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.service.projPurchase.AeaImProjPurchaseService;
import com.augurit.aplanmis.common.utils.BusinessUtils;
import com.augurit.aplanmis.common.vo.AeaImServiceVo;
import com.augurit.aplanmis.common.vo.QueryAgentUnitInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/market")
@RestController
@Api(tags = "中介事项相关接口")
public class AgentItemController {

    private static Logger logger = LoggerFactory.getLogger(AgentItemController.class);

    @Autowired
    private AeaImProjPurchaseService aeaImProjPurchaseService;

    @ApiOperation(value = "根据中介服务事项获取中介服务", notes = "根据中介服务事项获取中介服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemVerId", value = "事项版本id")
    })
    @PostMapping(value = "/getItemServiceList/{itemVerId}")
    public ContentResultForm<List<AeaImServiceVo>> getItemServiceList(@PathVariable("itemVerId") String itemVerId) {
        try {
            List<AeaImServiceVo> list = aeaImProjPurchaseService.getItemServiceListByItemVerId(itemVerId);
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, null, e.getMessage());
        }
    }


    @ApiOperation(value = "上传批文文件或者要求说明文件", notes = "上传批文文件或者要求说明文件")
    @PostMapping(value = "/uploadFiles")
    @ApiImplicitParam(name = "detailId", value = "附件关联ID", dataType = "string")
    public ContentResultForm<String> uploadFiles(HttpServletRequest request, String detailId) {
        try {
            String recordId = aeaImProjPurchaseService.uploadFiles(request, detailId);
            return new ContentResultForm<String>(true, recordId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, null, e.getMessage());
        }
    }


    @ApiOperation(value = "获取自动生成的项目编码", notes = "获取自动生成的项目编码")
    @PostMapping(value = "/getAutoProjCode")
    public ContentResultForm getAutoProjCode() {
        return new ContentResultForm<>(true, BusinessUtils.getAutoProjCode());
    }


    @ApiOperation(value = "查询符合条件的中介机构", notes = "查询符合条件的中介机构")
    @PostMapping(value = "/getAgentUnitInfoList", produces = "application/json;charset=UTF-8")
    public ResultForm getAgentUnitInfoList(@RequestBody QueryAgentUnitInfoVo queryAgentUnitInfo) {
        try {
            return new ContentResultForm<>(true, aeaImProjPurchaseService.getAgentUnitInfoList(queryAgentUnitInfo));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }

}
