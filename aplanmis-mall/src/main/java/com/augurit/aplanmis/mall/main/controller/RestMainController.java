package com.augurit.aplanmis.mall.main.controller;


import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.dto.AnnounceDataDto;
import com.augurit.aplanmis.common.dto.ApproveDataDto;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.search.ApproveDataService;
import com.augurit.aplanmis.mall.main.service.RestMainService;
import com.augurit.aplanmis.mall.main.vo.AeaRegionVo;
import com.augurit.aplanmis.mall.main.vo.StaticticsVo;
import com.augurit.aplanmis.mall.main.vo.ThemeTypeVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("rest/main")
@Api(value = "首页", tags = "首页 --> 相关接口")
public class RestMainController {
    Logger logger = LoggerFactory.getLogger(RestMainController.class);

    @Autowired
    RestMainService restMainService;
    @Autowired
    AeaItemBasicService aeaItemBasicService;
    @Autowired
    ApproveDataService approveDataService;
    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    protected String topOrgId;
    @Value("${aplanmis.mall.skin:skin_v4.1/}/")
    private String skin;

    @GetMapping("/toIndexPage")
    @ApiOperation(value = "首页-->跳转index页面接口")
    public ModelAndView toIndexPage() {
        return new ModelAndView("mall/"+skin+"index");
    }

    @GetMapping("/toRegulationIndexPage")
    @ApiOperation(value = "首页-->跳转RegulationIndex页面接口")
    public ModelAndView toRegulationIndexPage() {
        return new ModelAndView("mall/"+skin+"regulation/regulationIndex");
    }

    @GetMapping("/tovideoTeachingPage")
    @ApiOperation(value = "首页-->跳转tovideoTeachingPage页面接口")
    public ModelAndView tovideoTeachingPage() {
        return new ModelAndView("mall/"+skin+"main/components/videoTeaching");
    }

    @GetMapping("/toMainIndexPage")
    @ApiOperation(value = "首页-->跳转mainIndex页面接口")
    public ModelAndView toMainIndexPage() {
        return new ModelAndView("mall/"+skin+"main/mainIndex");
    }

    @GetMapping("/toDeclarGuidePage")
    @ApiOperation(value = "跳转申报指引")
    public ModelAndView toDeclarGuidePage() {
        return new ModelAndView("mall/"+skin+"declarGuide/index");
    }

    @GetMapping("/toEntryPage")
    @ApiOperation(value = "首页-->跳转首页入口页面接口")
    public ModelAndView toEntryPage() {
        return new ModelAndView("mall/"+skin+"entryPage");
    }

    @GetMapping("/theme/list")
    @ApiOperation(value = "首页-->按主题申报-->获取主题列表接口")
    public ContentResultForm<List<ThemeTypeVo>> getThemeList() {
        try {
            return new ContentResultForm<>(true, restMainService.getThemeTypeList(topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "获取主题列表接口异常");
        }
    }

    @GetMapping("/stage/list/{themeId}")
    @ApiOperation(value = "首页-->按主题申报-->根据主题获取阶段接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "themeId", value = "主题id", dataType = "String", required = true),
            @ApiImplicitParam(name = "projInfoId", value = "项目id", dataType = "String", required = false),
            @ApiImplicitParam(name = "unitInfoId", value = "委托人申报时选择的单位id", dataType = "String", required = false),
            @ApiImplicitParam(name = "dygjbzfxfw", value = "对应国家标准辅线服务:多评合一（51）、方案联审（52）、联合审图（53）、联合测绘（54C）、联合验收（54Y）", dataType = "String", required = false)})
    public ContentResultForm<List<AeaParStage>> getStageByThemeId(@PathVariable("themeId") String themeId, String projInfoId, String unitInfoId,String dygjbzfxfw, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty(themeId)) return new ContentResultForm(false, "主题id为必填项!");
            return new ContentResultForm<>(true, restMainService.getStageByThemeId(themeId, projInfoId,topOrgId,unitInfoId,dygjbzfxfw,request));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "根据主题获取阶段接口异常");
        }
    }

    @GetMapping("/org/list")
    @ApiOperation(value = "首页-->按部门申报-->获取所有部门列表")
    public ContentResultForm<List<OpuOmOrg>> getOrgList(String orgId) {
        try {
            logger.error("获取所有部门列表/org/list---start---");
            long l=System.currentTimeMillis();
            List<OpuOmOrg> orgList = aeaItemBasicService.listOpuOmOrgByAeaItemBasic1(StringUtils.isBlank(orgId) ? topOrgId : orgId);
            logger.error("获取所有部门列表/org/list---end---耗时："+(System.currentTimeMillis()-l));
            return new ContentResultForm<>(true, orgList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "获取所有部门列表异常");
        }
    }

    @GetMapping("/region/list")
    @ApiOperation(value = "首页-->按部门申报-->获取所有区划列表")
    public ContentResultForm<AeaRegionVo> getRegionList() {
        try {
            return new ContentResultForm<>(true, restMainService.getAeaRegionVo(topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "获取所有部门列表异常");
        }
    }




    @GetMapping("/statictics/apply")
    @ApiOperation(value = "首页-->获取申报件统计数据")
    public ContentResultForm<StaticticsVo> getApplyStatistics() {
        try {
            return new ContentResultForm<>(true, restMainService.getApplyStatistics(topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "获取申报件统计数据接口异常");
        }
    }

    @GetMapping("/statictics/item")
    @ApiOperation(value = "首页-->获取办件统计数据")
    public ContentResultForm<StaticticsVo> getItemStatistics() {
        try {
            return new ContentResultForm<>(true, restMainService.getItemStatistics(topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "获取办件统计数据接口异常");
        }
    }

    @GetMapping("/approve/list")
    @ApiOperation(value = "首页-->获取审批情况列表数据")
    public ContentResultForm<List<AnnounceDataDto>> getApproveList() {
        try {
            return new ContentResultForm<>(true, approveDataService.searchAnnounceDataList(StringUtils.EMPTY,topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "首页-->获取审批情况列表数据异常");
        }
    }

    @GetMapping("/applyinst/list")
    @ApiOperation(value = "首页-->根据项目名称编码流水号查询功能接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "搜索关键字", name = "keyword", required = false, dataType = "string"),
            @ApiImplicitParam(value = "页数", name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(value = "页面大小", name = "pageSize", dataType = "Integer")})
    public PageInfo<AnnounceDataDto> getApplyInstList(String keyword, int pageNum, int pageSize) {
        try {
            return approveDataService.searchAnnounceDataList(keyword, pageNum, pageSize,topOrgId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @GetMapping("/item/main/list")
    @ApiOperation(value = "首页-->根据事项名称部门名称事项编号等查询事项列表接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "搜索关键字", name = "keyword", required = false, dataType = "string"),
            @ApiImplicitParam(value = "页数", name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(value = "页面大小", name = "pageSize", dataType = "Integer")})
    public PageInfo<AeaItemBasic> getItemMainList(String keyword,int pageNum,int pageSize) {
        try {
            return restMainService.getItemMainList(keyword,topOrgId,pageNum,pageSize);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @GetMapping("/theme/main/list")
    @ApiOperation(value = "首页-->根据主题名称，阶段名称等查询主题列表接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "搜索关键字", name = "keyword", required = false, dataType = "string"),
            @ApiImplicitParam(value = "页数", name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(value = "页面大小", name = "pageSize", dataType = "Integer")})
    public PageInfo<AeaParTheme> getthemeMainList(String keyword, int pageNum, int pageSize) {
        try {
            return restMainService.getThemeMainList(keyword,topOrgId,pageNum,pageSize);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @GetMapping("/approveData/list")
    @ApiOperation(value = "首页-->查询受理情况、拟审批意见、审批决定数据接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "受理号", name = "applyinstCode", required = false, dataType = "string"),
            @ApiImplicitParam(value = "项目名称", name = "projInfoName", required = false, dataType = "string"),
            @ApiImplicitParam(value = "审批状态(0:受理情况,1:拟审批意见,2:审批决定)", name = "applyState", required = false, dataType = "string"),
            @ApiImplicitParam(value = "页数", name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(value = "页面大小", name = "pageSize", dataType = "Integer")})
    public PageInfo<ApproveDataDto> searchApproveDataList(String applyinstCode, String projInfoName, int pageNum, int pageSize, String applyState) {
        try {//0,1,2,5 受理情况  3,4,6审批情况 7办结
            List<String> applyStates = new ArrayList<>();
            if ("0".equals(applyState)) {//受理情况
                String[] applyStateArr = {
                        ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue(),
                        ApplyState.RECEIVE_APPROVED_APPLY.getValue(),
                        ApplyState.ACCEPT_DEAL.getValue(),
                        ApplyState.OUT_SCOPE.getValue()};
                applyStates = Arrays.asList(applyStateArr);
            } else if ("1".equals(applyState)) {//拟审批数据
                String[] applyStateArr = {
                        ApplyState.IN_THE_SUPPLEMENT.getValue(),
                        ApplyState.SUPPLEMENTARY.getValue()};
                applyStates = Arrays.asList(applyStateArr);
            } else if ("2".equals(applyState)) {//审批决定数据
                String[] applyStateArr = {ApplyState.COMPLETED.getValue()};
                applyStates = Arrays.asList(applyStateArr);
            }
            return approveDataService.searchApproveDataList(applyinstCode, projInfoName, pageNum, pageSize, applyStates,topOrgId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

}
