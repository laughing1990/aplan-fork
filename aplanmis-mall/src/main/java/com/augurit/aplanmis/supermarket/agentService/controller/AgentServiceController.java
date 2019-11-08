package com.augurit.aplanmis.supermarket.agentService.controller;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemService;
import com.augurit.aplanmis.common.domain.AgentUnitService;
import com.augurit.aplanmis.supermarket.agentService.service.AgentServiceService;
import com.augurit.aplanmis.supermarket.agentService.vo.AgentServiceItemDetailVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 中介服务入口
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/supermarket/agentservice")
@Api(value = "中介服务接口", description = "中介服务接口")
public class AgentServiceController {
    @Autowired
    private AgentServiceService agentServiceService;

    /**
     * 中介入住机构列表接口，入住公示列表
     *
     * @param agentUnitService 查询条件实体
     * @param pageNum          查询页
     * @param pageSize         每页大小
     * @return
     */
    @GetMapping("/listCheckinUnit")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "查询页", required = true, dataType = "int")})
    @ApiOperation(value = "入住机构列表", notes = "入住机构列表", httpMethod = "GET")
    public ResultForm listCheckinUnit(AgentUnitService agentUnitService, Integer pageNum, Integer pageSize) {
        try {
            if (pageNum == null || pageSize == null) {
                return new ResultForm(false, "必填参数为空");
            }

            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AgentUnitService> list = agentServiceService.listCheckinUnit(agentUnitService, page);
            agentServiceService.getCredCreditSummary(list);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    /**
     * 入住服务列表接口
     *
     * @param agentUnitService
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/listCheckinService")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "查询页", required = true, dataType = "int")})
    @ApiOperation(value = "入住服务列表", notes = "入住服务列表", httpMethod = "GET")
    public ResultForm listCheckinService(AgentUnitService agentUnitService, Integer pageNum, Integer pageSize) {
        try {
            if (pageNum == null || pageSize == null) {
                return new ResultForm(false, "必填参数为空");
            }

            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AgentUnitService> list = agentServiceService.listCheckinService(agentUnitService, page);
            return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)), "成功查询入住服务列表");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    @GetMapping("/listServiceItem")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "查询页", required = true, dataType = "int")})
    @ApiOperation(value = "中介服务事项列表", notes = "中介服务事项列表", httpMethod = "GET")
    public ResultForm listServiceItem(AeaItemService aeaItemService, Integer pageNum, Integer pageSize) {
        try {
            if (pageNum == null || pageSize == null) {
                return new ResultForm(false, "必填参数为空");
            }

            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AeaItemService> list = agentServiceService.listServiceItem(aeaItemService, page);
            return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)), "成功查询中介服务事项列表");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    @GetMapping("/listServiceItemOrg")
    @ApiOperation(value = "查询中介机构服务事项对应的部门", notes = "查询中介机构服务事项对应的部门", httpMethod = "GET")
    public ResultForm listServiceItemOrg() {
        try {
            List<AeaItemService> list = agentServiceService.listServiceItemOrg();
            return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)), "成功查询中介事项所属部门");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    @GetMapping("/serviceItemDetail")
    @ApiOperation(value = "查询中介服务事项详情", notes = "查询中介服务事项详情", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "itemBasicId", value = "服务事项主键ID", required = true, dataType = "string")})
    public ResultForm serviceItemDetail(String itemBasicId) {
        try {
            if (StringUtils.isBlank(itemBasicId)) {
                return new ResultForm(false, "必填参数为空");
            }

            AgentServiceItemDetailVo agentItemDetail = agentServiceService.serviceItemDetail(itemBasicId);
            return new ContentResultForm<>(true, agentItemDetail, "成功查询中介服务事项详情");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }
}
