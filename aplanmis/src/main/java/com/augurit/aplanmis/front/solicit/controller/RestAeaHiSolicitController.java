package com.augurit.aplanmis.front.solicit.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import com.augurit.aplanmis.common.domain.AeaHiSolicitDetailUser;
import com.augurit.aplanmis.common.vo.solicit.AeaHiSolicitVo;
import com.augurit.aplanmis.common.vo.solicit.QueryCondVo;
import com.augurit.aplanmis.front.solicit.service.RestAeaHiSolicitService;
import com.augurit.aplanmis.front.solicit.vo.AeaHiSolicitInfo;
import com.augurit.aplanmis.front.solicit.vo.SolicitDetailUserVo;
import com.augurit.aplanmis.front.solicit.vo.SolicitVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    public ResultForm listOrg(String isRoot, String parentOrgId) {
        if (StringUtils.isBlank(isRoot))
            return new ResultForm(false, "参数isRoot不能为空！");
        if (StringUtils.isNotBlank(isRoot) && "0".equals(isRoot)
                && StringUtils.isBlank(parentOrgId))
            return new ResultForm(false, "非根组织，参数parentOrgId不能为空！");

        List<OpuOmOrg> list = null;
        try {
            list = restAeaHiSolicitService.listOrg(isRoot, parentOrgId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "错误信息：" + e.getLocalizedMessage());
        }

        return new ContentResultForm<List<OpuOmOrg>>(true, list);
    }

    @GetMapping("/list/solicit")
    @ApiOperation(value = "意见征求 --> 获取意见征求列表", notes = "获取意见征求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busType", value = "查询类型，0 意见征求，1 一次征求，2 联合评审 ...")
            , @ApiImplicitParam(name = "page", value = "分页参数")
    })
    public ResultForm listSolicit(Page page, QueryCondVo condVo) throws Exception {

        List<AeaHiSolicitVo> listSolicit = restAeaHiSolicitService.listSolicit(condVo, page);
        return new ContentResultForm<>(true, new PageInfo<>(listSolicit), "success");
    }

    @ApiOperation("意见征求接口")
    @PostMapping("/create")
    public ResultForm create(@Valid @RequestBody SolicitVo solicitVo) throws Exception {
        if (StringUtils.isBlank(solicitVo.getDetailInfo())) {
            return new ResultForm(false, "参数征求详细信息detailInfo不能为空！");
        }
        try {
            AeaHiSolicit aeaHiSolicit = solicitVo.convertToAeaHiSolicit();
            restAeaHiSolicitService.createSolicit(aeaHiSolicit, solicitVo.getSolicitType(), solicitVo.getDetailInfo(), solicitVo.getBusType());
            return new ResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "发起失败！");
        }
    }
    @ApiOperation(value = "意见征求 --> 发起同时上传附件接口", notes = "获取意见征求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busType", value = "查询类型，0 意见征求，1 一次征求，2 联合评审 ...")
            , @ApiImplicitParam(name = "page", value = "分页参数")
    })
    @RequestMapping("/uploadAttFile")
    public ResultForm uploadAttFile(String solicitId, String tableName,String pkName, HttpServletRequest request) {
        try {
            solicitId = restAeaHiSolicitService.uploadAttFile(solicitId, tableName,pkName, request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "附件上传失败!");
        }
        return new ContentResultForm(true, solicitId, "保存成功!");
    }

    @ApiOperation("意见征求回复接口")
    @PostMapping("/answer")
    public ResultForm solicitAnswer(@Valid @RequestBody SolicitDetailUserVo solicitDetailUserVo) throws Exception {
        try {
            AeaHiSolicitDetailUser detailUser = solicitDetailUserVo.convertToAeaHiDetailUser();
            restAeaHiSolicitService.createSolicitOpinion(detailUser);
            return new ResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

    @ApiOperation("意见征求汇总意见接口")
    @PostMapping("/collect/opinion")
    public ResultForm solicitCollectOpinion(@Valid @RequestBody SolicitVo soliciVo) throws Exception {
        try {
            AeaHiSolicit aeaHiSolicit = soliciVo.convertToAeaHiSolicit();
            restAeaHiSolicitService.createSolicitCollectOpinion(aeaHiSolicit);
            return new ResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

    @ApiOperation("根据申报实例ID和业务类型获取所有的征求信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申报实例ID")
            , @ApiImplicitParam(name = "busType", value = "业务类型")
    })
    @GetMapping("/listAeaHiSolicitByApplyinstId")
    public ResultForm listAeaHiSolicitByApplyinstId(String applyinstId, String busType) {
        if (StringUtils.isBlank(applyinstId))
            return new ResultForm(false, "参数applyinstId不能为空！");
        if (StringUtils.isBlank(busType))
            return new ResultForm(false, "参数busType业务类型不能为空！");

        List<AeaHiSolicitInfo> list = null;
        try {
            list = restAeaHiSolicitService.listAeaHiSolicitByApplyinstId(applyinstId, busType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "请求出错了，错误信息为：" + e.getLocalizedMessage());
        }

        return new ContentResultForm<List<AeaHiSolicitInfo>>(true, list);
    }

    @ApiOperation("被征集人签收")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "solicitUserId", value = "被征集人主键")
    })
    @GetMapping("/sign/{detailUserId}")
    public ResultForm signSolicitUserDetail(@PathVariable String detailUserId) throws Exception {
        if (!StringUtils.isEmpty(detailUserId)) {
            restAeaHiSolicitService.signSolicitDetail(detailUserId);
        }
        return new ResultForm(true, "success");
    }

    @ApiOperation("")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "被征集人主键")
    })
    @GetMapping("/apply/items")
    public ResultForm getApplyItems(String applyinstId) throws Exception{
        return new ContentResultForm<>(true,restAeaHiSolicitService.getApplyItems(applyinstId));
    }
}
