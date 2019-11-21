package com.augurit.aplanmis.rest.index.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.dto.AnnounceDataDto;
import com.augurit.aplanmis.common.dto.ApproveDataDto;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.search.ApproveDataService;
import com.augurit.aplanmis.rest.index.service.RestMainService;
import com.augurit.aplanmis.rest.index.service.vo.AeaRegionVo;
import com.augurit.aplanmis.rest.index.service.vo.StaticticsVo;
import com.augurit.aplanmis.rest.index.service.vo.ThemeTypeVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "首页 --> 公开接口")
@RequestMapping("/rest/index")
public class IndexController {

    @Autowired
    RestMainService restMainService;
    @Autowired
    AeaItemBasicService aeaItemBasicService;
    @Autowired
    ApproveDataService approveDataService;
    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    protected String topOrgId;

    @GetMapping("/theme/list")
    @ApiOperation(value = "首页-->按主题申报-->获取主题列表接口")
    public ContentResultForm<List<ThemeTypeVo>> getThemeList() {
        try {
            return new ContentResultForm<>(true, restMainService.getThemeTypeList(topOrgId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "获取主题列表接口异常");
        }
    }

    @GetMapping("/stage/list/{themeId}")
    @ApiOperation(value = "首页-->按主题申报-->根据主题获取阶段接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "themeId", value = "主题id", dataType = "String", required = true),
            @ApiImplicitParam(name = "projInfoId", value = "项目id", dataType = "String"),
            @ApiImplicitParam(name = "unitInfoId", value = "委托人申报时选择的单位id", dataType = "String")})
    public ContentResultForm<List<AeaParStage>> getStageByThemeId(@PathVariable("themeId") String themeId, String projInfoId, String unitInfoId) {
        try {
            if (StringUtils.isEmpty(themeId)) return new ContentResultForm<>(false, null, "主题id为必填项!");
            return new ContentResultForm<>(true, restMainService.getStageByThemeId(themeId, projInfoId, topOrgId, unitInfoId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "根据主题获取阶段接口异常");
        }
    }

    @GetMapping("/approve/details")
    @Transactional(readOnly = true)
    @ApiOperation(value = "首页 --> 审批情况", notes = "首页中查询所有事项的审批情况", httpMethod = "GET")
    public ContentResultForm<List<AnnounceDataDto>> listApproveDetails() {
        try {
            return new ContentResultForm<>(true, approveDataService.searchAnnounceDataList(StringUtils.EMPTY, topOrgId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "首页-->获取审批情况列表数据异常");
        }
    }

    @GetMapping("/approve/stats")
    @ApiOperation(value = "获取办件统计", notes = "获取办件统计", httpMethod = "GET")
    public ContentResultForm<StaticticsVo> getItemStatistics() {
        try {
            return new ContentResultForm<>(true, restMainService.getItemStatistics(topOrgId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "获取办件统计数据接口异常");
        }
    }

    @GetMapping("/statictics/apply")
    @ApiOperation(value = "首页-->获取申报件统计数据")
    public ContentResultForm<StaticticsVo> getApplyStatistics() {
        try {
            return new ContentResultForm<>(true, restMainService.getApplyStatistics(topOrgId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "获取申报件统计数据接口异常");
        }
    }

    @GetMapping("/applyinst/list")
    @ApiOperation(value = "首页-->根据项目名称编码流水号查询功能接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "搜索关键字", name = "keyword", required = false, dataType = "string"),
            @ApiImplicitParam(value = "页数", name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(value = "页面大小", name = "pageSize", dataType = "Integer")})
    public PageInfo<AnnounceDataDto> getApplyInstList(String keyword, @RequestParam(required = false) int pageNum, @RequestParam(required = false) int pageSize) {
        try {
            return approveDataService.searchAnnounceDataList(keyword, pageNum, pageSize, topOrgId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @GetMapping("/org/list")
    @ApiOperation(value = "首页-->按部门申报-->获取所有部门列表")
    @ApiImplicitParam(name = "orgId", value = "父组织id")
    public ContentResultForm<List<OpuOmOrg>> getOrgList(String orgId) {
        try {
            log.info("获取所有部门列表/org/list---start---");
            long l = System.currentTimeMillis();
            List<OpuOmOrg> orgList = aeaItemBasicService.listOpuOmOrgByAeaItemBasic1(StringUtils.isBlank(orgId) ? topOrgId : orgId);
            log.info("获取所有部门列表/org/list---end---耗时：" + (System.currentTimeMillis() - l));
            return new ContentResultForm<>(true, orgList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "获取所有部门列表异常");
        }
    }

    @GetMapping("/region/list")
    @ApiOperation(value = "首页-->按部门申报-->获取所有区划列表")
    public ContentResultForm<AeaRegionVo> getRegionList() {
        try {
            return new ContentResultForm<>(true, restMainService.getAeaRegionVo(topOrgId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "获取所有区划列表");
        }
    }

    @GetMapping("/approveData/list")
    @ApiOperation(value = "首页-->查询受理情况、拟审批意见、审批决定数据接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "受理号", name = "applyinstCode", dataType = "string"),
            @ApiImplicitParam(value = "项目名称", name = "projInfoName", dataType = "string"),
            @ApiImplicitParam(value = "审批状态(0:受理情况,1:拟审批意见,2:审批决定)", name = "applyState", dataType = "string"),
            @ApiImplicitParam(value = "页数", name = "pageNum", dataType = "Integer"),
            @ApiImplicitParam(value = "页面大小", name = "pageSize", dataType = "Integer")})
    public PageInfo<ApproveDataDto> searchApproveDataList(String applyinstCode, String projInfoName, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, String applyState) {
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
            return approveDataService.searchApproveDataList(applyinstCode, projInfoName, pageNum, pageSize, applyStates, topOrgId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @GetMapping("/policy/pdf")
    @ApiOperation(value = "政策法规", notes = "政策法规文件", httpMethod = "GET")
    public ResultForm policies() {
        Map<String, Map<String, String>> pdfs = new HashMap<>();
        Map<String, String> guojiaRegulation = new LinkedHashMap<>();
        Map<String, String> hebeiRegulation = new LinkedHashMap<>();
        Map<String, String> tangshanRegulation = new LinkedHashMap<>();
        Map<String, String> plan = new LinkedHashMap<>();

        guojiaRegulation.put("/docs/policy/ssxz/ssxz_guojia_1.pdf", "【国办发[2019]11号】国务院办公厅关于全面开展工程建设项目审批制度改革的实施意见");

        hebeiRegulation.put("/docs/policy/ssxz/ssxz_heibei_1.pdf", "【国办发[2019]11号】国务院办公厅关于全面开展工程建设项目审批制度改革的实施意见");
        hebeiRegulation.put("/docs/policy/ssxz/ssxz_heibei_2.pdf", "【国办发[2019]11号】国务院办公厅关于全面开展工程建设项目审批制度改革的实施意见");
        hebeiRegulation.put("/docs/policy/ssxz/ssxz_heibei_3.pdf", "【国办发[2019]11号】国务院办公厅关于全面开展工程建设项目审批制度改革的实施意见");

        tangshanRegulation.put("/docs/policy/ssxz/ssxz_1.pdf", "（唐港城办发﹝2019﹞8号）关于印发《政府投资房屋建筑类项目审批流程图》等11类工程建设项目审批流程图的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_3.pdf", "（唐港城办发﹝2019﹞9号）关于印发《唐山市工程建设项目“多评合一”实施办法的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_4.pdf", "（唐港城办发﹝2019﹞10号）关于进一步规范市中心区市政设施建设类审批、工程建设涉及城市绿地树木审批事项合并办理工作的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_5.pdf", "（唐港城办发﹝2019﹞11号）关于印发《唐山市建设工程规划设计方案联合审查实施细则》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_6.pdf", "（唐港城办发﹝2019﹞12号）关于印发《唐山市告知承诺制办理建设工程规划预许可管理办法(试行)》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_7.pdf", "（唐港城办发﹝2019﹞13号）关于印发《全面推进工程建设项目验收阶段“多测合一”改革的实施意见》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_8.pdf", "（唐港城办发﹝2019﹞14号）关于进一步加强工程建设项目施工图投术审查工作的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_9.pdf", "（唐港城办发﹝2019﹞15号）关于进一步规范工程建设项目施工图设计文件联合审查有关工作的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_10.pdf", "（唐港城办发﹝2019﹞16号）关于印发《唐山市工程建设项目施工预许可审批实施细则》的通知");

        tangshanRegulation.put("/docs/policy/ssxz/ssxz_11.pdf", "（唐港城办发﹝2019﹞17号）关于印发《唐山市工程建设项目联合验收工作规程》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_12.pdf", "（唐港城办发﹝2019﹞18号）关于印发《唐山市工程建设项目立项用地规划许可阶段牵头协调管理实施意见》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_13.pdf", "（唐港城办发﹝2019﹞19号）关于印发《唐山市工程建设项目工程建设许可阶段牵头协调管理实施意见》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_14.pdf", "（唐港城办发﹝2019﹞20号）关于印发《唐山市工程建设项目施工许可阶段牵头协调管理实施意见》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_15.pdf", "（唐港城办发﹝2019﹞21号）关于印发《唐山市工程建设项目竣工验收阶段牵头协调管理实施意见》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_16.pdf", "（唐港城办发﹝2019﹞22号）关于印发《工程建设项目各阶段业务协同管理实施意见》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_17.pdf", "（唐港城办发﹝2019﹞23号）关于进一步加强市区交通护栏清洗管理有关工作的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_18.pdf", "（唐港城办发﹝2019﹞24号）关于进一步加强市中心区道路分隔带绿化冲洗管理有关工作的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_19.pdf", "（唐港城办发﹝2019﹞25号）关于印发《唐山市中心区新建道路、既有道路改造与渠化路口改造同步实施的意见》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_20.pdf", "（唐港城办发﹝2019﹞26号）关于印发《唐山市“多规合一”编制指导意见》的通知");

        tangshanRegulation.put("/docs/policy/ssxz/ssxz_21.pdf", "（唐港城办发﹝2019﹞27号）关于印发《唐山市“多规合一”差异图斑处理指导意见》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_22.pdf", "（唐港城办发﹝2019﹞28号）关于印发《唐山市“多规合一”规划编制工作行业需求清单》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_23.pdf", "（唐港城办发﹝2019﹞29号）关于印发《唐山市“一张蓝图”数据动态更新维护操作规范》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_24.pdf", "（唐港城办发﹝2019﹞30号）关于印发《唐山市工程建设项目施工许可事中事后监督管理实施细则（试行）》的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_25.pdf", "国务院办公厅关于全面开展工程建设项目审批制度改革的实施意见（国办发〔2019〕11号）(1)");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_26.pdf", "关于印发《关于推行工程建设项目区域评估工作的实施办法》（唐优化营商办﹝2019﹞10号)");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_27.pdf", "唐山市人民政府办公厅关于印发唐山市深化工程建设项目审批制度改革实施方案（试行）的通知");
        tangshanRegulation.put("/docs/policy/ssxz/ssxz_28.pdf", "印发工程建设项目审批事项清单的通知（唐优化营商办﹝2019﹞2号)");


        pdfs.put("tangshanRegulation", tangshanRegulation);
        pdfs.put("hebeiRegulation", hebeiRegulation);
        pdfs.put("guojiaRegulation", guojiaRegulation);
        pdfs.put("plan", plan);

        return new ContentResultForm<>(true, pdfs);
    }

    @GetMapping("/policy/handingGuides")
    @ApiOperation(value = "办事指南", notes = "办事指南", httpMethod = "GET")
    public ResultForm handingGuides() {
        Map<String, Map<String, String>> docGuides = new HashMap<>();
        Map<String, String> guides = new LinkedHashMap<>();

        guides.put("/docs/policy/handingGuides/政府投资工程建设项目（一般房屋建筑类）办事指南.docx", "政府投资工程建设项目（一般房屋建筑类）办事指南");
        guides.put("/docs/policy/handingGuides/政府投资工程建设项目（不新增用地不改变市政管线、管径、走向的既有城市道路、园林绿化、市政管线翻修改造项目）办事指南.docx", "政府投资工程建设项目（不新增用地不改变市政管线、管径、走向的既有城市道路、园林绿化、市政管线翻修改造项目）办事指南");
        guides.put("/docs/policy/handingGuides/政府投资工程建设项目（房屋内部装饰装修不改变建筑功能或性质）办事指南.docx", "政府投资工程建设项目（房屋内部装饰装修不改变建筑功能或性质）办事指南");
        guides.put("/docs/policy/handingGuides/政府投资工程建设项目（现状改建项目老旧小区改造等）办事指南.docx", "政府投资工程建设项目（现状改建项目老旧小区改造等）办事指南");
        guides.put("/docs/policy/handingGuides/政府投资工程建设项目（线性工程类--不新增用地改变市政管线、管径、走向的既有城市道路、园林绿化、市政管线翻修改造项目）办事指南.docx", "政府投资工程建设项目（线性工程类--不新增用地改变市政管线、管径、走向的既有城市道路、园林绿化、市政管线翻修改造项目）办事指南");
        guides.put("/docs/policy/handingGuides/政府投资工程建设项目（线性工程类--新建城市道路、园林绿化、市政管线工程）办事指南.docx", "政府投资工程建设项目（线性工程类--新建城市道路、园林绿化、市政管线工程）办事指南");
        guides.put("/docs/policy/handingGuides/社会投资工程建设项目（一般房屋建筑类）办事指南.docx", "社会投资工程建设项目（一般房屋建筑类）办事指南");
        guides.put("/docs/policy/handingGuides/社会投资工程建设项目（不新增用地不改变市政管线、管径、走向的既有城市道路、园林绿化、市政管线翻修改造项目）办事指南.docx", "社会投资工程建设项目（不新增用地不改变市政管线、管径、走向的既有城市道路、园林绿化、市政管线翻修改造项目）办事指南");
        guides.put("/docs/policy/handingGuides/社会投资工程建设项目（房屋内部装饰装修不改变建筑功能或性质）办事指南.docx", "社会投资工程建设项目（房屋内部装饰装修不改变建筑功能或性质）办事指南");
        guides.put("/docs/policy/handingGuides/社会投资工程建设项目（现状改建项目老旧小区改造等）办事指南.docx", "社会投资工程建设项目（现状改建项目老旧小区改造等）办事指南");
        guides.put("/docs/policy/handingGuides/社会投资工程建设项目（线性工程类--不新增用地改变市政管线、管径、走向的既有城市道路、园林绿化、市政管线翻修改造项目）办事指南.docx", "社会投资工程建设项目（线性工程类--不新增用地改变市政管线、管径、走向的既有城市道路、园林绿化、市政管线翻修改造项目）办事指南");
        guides.put("/docs/policy/handingGuides/社会投资工程建设项目（线性工程类--新建城市道路、园林绿化、市政管线工程）办事指南.docx", "社会投资工程建设项目（线性工程类--新建城市道路、园林绿化、市政管线工程）办事指南");
        guides.put("/docs/policy/handingGuides/小型社会投资项目（符合区域环评、不涉及环境敏感区的建筑面积20000平方米以下的仓储、厂房、研发楼项目）办事指南.docx", "小型社会投资项目（符合区域环评、不涉及环境敏感区的建筑面积20000平方米以下的仓储、厂房、研发楼项目）办事指南");
        guides.put("/docs/policy/handingGuides/建设工程设计方案联合审查办事指南.docx", "建设工程设计方案联合审查办事指南");
        guides.put("/docs/policy/handingGuides/施工图联合审查办事指南.docx", "施工图联合审查办事指南");
        guides.put("/docs/policy/handingGuides/竣工联合验收办事指南.docx", "竣工联合验收办事指南");
        docGuides.put("bszn", guides);

        return new ContentResultForm<>(true, docGuides);
    }
}
