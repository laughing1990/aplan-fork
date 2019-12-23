package com.augurit.aplanmis.front.solicit.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.front.solicit.service.RestAeaHiSolicitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author:chendx
 * @date: 2019-12-23
 * @time: 13:47
 */
@RestController
@Slf4j
@RequestMapping("/rest/solicit")
@Api(tags = "意见征求入口")
public class RestAeaHiSolicitController {
    @Autowired
    private RestAeaHiSolicitService restAeaHiSolicitService;


    @GetMapping("/list/org")
    @ApiOperation(value = "意见征求 --> 获取部门征求部门信息列表", notes = "获取部门征求部门信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isRoot", value = "是否为根组织，1表示是，0表示否")
            , @ApiImplicitParam(name = "parentOrgId", value = "父组织ID，当isRoot=0时，非空")
    })
    public ResultForm listOrg(String isRoot, String parentOrgId){
        if(StringUtils.isBlank(isRoot))
            return new ResultForm(false,"参数isRoot不能为空！");
        if(StringUtils.isNotBlank(isRoot)&&"0".equals(isRoot)
                &&StringUtils.isBlank(parentOrgId))
            return new ResultForm(false,"非根组织，参数parentOrgId不能为空！");

        List<OpuOmOrg> list = null;
        try {
            list = restAeaHiSolicitService.listOrg(isRoot,parentOrgId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false,"错误信息："+e.getLocalizedMessage());
        }

        return new ContentResultForm<List<OpuOmOrg>>(true,list);
    }
}
