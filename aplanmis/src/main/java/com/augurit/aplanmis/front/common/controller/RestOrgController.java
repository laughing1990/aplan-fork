package com.augurit.aplanmis.front.common.controller;

import com.augurit.agcloud.bpm.admin.process.domain.BpmAssigneeOrgEntity;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.area.RegionService;
import com.augurit.aplanmis.front.common.service.RestOrgService;
import com.augurit.aplanmis.front.common.vo.BpmAssigneeOrgEntityVo;
import com.augurit.aplanmis.front.common.vo.BscDicRegionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/org")
@Api(tags = "部门组织树")
public class RestOrgController {

    @Autowired
    private RestOrgService restOrgService;

    @Autowired
    private RegionService regionService;

    @GetMapping("/tree")
    @ApiOperation("部门组织树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentOrgId", value = "父组织机构id， 不传则查询所有")
            ,@ApiImplicitParam(name = "dataType", value = "未知，不用传")
            ,@ApiImplicitParam(name = "orgName", value = "组织机构名称，模糊查询")
    })
    public ContentResultForm<List<BpmAssigneeOrgEntityVo>> getBranchOrgTree(String parentOrgId, String dataType, String orgName) {
        List<BpmAssigneeOrgEntity> orgTree = restOrgService.getOrgTree(parentOrgId, dataType, orgName);
        List<BpmAssigneeOrgEntityVo> orgTreeVos = BpmAssigneeOrgEntityVo.toBpmAssigneeOrgEntityVo(orgTree);
        return new ContentResultForm<>(true, orgTreeVos);
    }

    @GetMapping("/region/list")
    @ApiOperation(("根据顶级组织ID查询区划列表"))
    public ContentResultForm<List<BscDicRegionVo>> getBscDicRegionList(String rootOrgId) {
        if (StringUtils.isBlank(rootOrgId)) rootOrgId = SecurityContext.getCurrentOrgId();
        List<BscDicRegion> regions = regionService.getBscDicRegionList(rootOrgId);
        if (regions != null) {
            return new ContentResultForm<>(true, regions.stream().map(BscDicRegionVo::from).collect(Collectors.toList()), "Query success");
        }
        return new ContentResultForm<>(true, null, "Empty result.");
    }
}
