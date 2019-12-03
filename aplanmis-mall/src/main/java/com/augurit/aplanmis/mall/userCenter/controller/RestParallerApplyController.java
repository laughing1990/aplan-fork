package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AeaHiApplyinstConstants;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParFactor;
import com.augurit.aplanmis.common.enumer.ThemeTypeEnum;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaItemCondService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.state.AeaParStateService;
import com.augurit.aplanmis.mall.main.service.RestMainService;
import com.augurit.aplanmis.mall.main.vo.ItemListVo;
import com.augurit.aplanmis.mall.main.vo.ThemeTypeVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyService;
import com.augurit.aplanmis.mall.userCenter.service.RestParallerApplyService;
import com.augurit.aplanmis.mall.userCenter.vo.MatListParamVo;
import com.augurit.aplanmis.mall.userCenter.vo.ParallelApplyResultVo;
import com.augurit.aplanmis.mall.userCenter.vo.StageApplyDataPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//并联申报
@RestController
@RequestMapping("rest/userCenter/apply")
@Api(value = "申办",tags = "申报 --> 并联申报接口")
public class RestParallerApplyController {
    Logger logger= LoggerFactory.getLogger(RestParallerApplyController.class);

    @Autowired
    RestApplyService restApplyService;
    @Autowired
    AeaProjInfoService aeaProjInfoService;
    @Autowired
    AeaItemBasicService aeaItemBasicService;
    @Autowired
    AeaParStateService aeaParStateService;
    @Autowired
    FileUtilsService fileUtilsService;
    @Autowired
    AeaHiSmsInfoService aeaHiSmsInfoService;
    @Autowired
    AeaItemStateService aeaItemStateService;
    @Autowired
    AeaItemCondService aeaItemCondService;
    @Autowired
    RestParallerApplyService restParallerApplyService;
    @Autowired
    AeaItemMatService aeaItemMatService;
    @Autowired
    AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private RestMainService restMainService;

    @GetMapping("/toParaApplyPage")
    @ApiOperation(value = "阶段申报-->跳转阶段申报页面接口")
    public ModelAndView toParaApplyPage(){
        return new ModelAndView("mall/userCenter/components/parallelDeclare");
    }

    @GetMapping("itemAndState/list/{stageId}/{projInfoId}/{regionalism}")
    @ApiOperation(value = "阶段申报 --> 根据阶段ID、项目ID获取事项一单清列表数据")
    @ApiImplicitParams({@ApiImplicitParam(value = "阶段ID",name = "stageId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "项目ID",name = "projInfoId",required = false,dataType = "string"),
            @ApiImplicitParam(value = "区域编码", name = "regionalism", required = false, dataType = "string"),
            @ApiImplicitParam(value = "建设地点", name = "projectAddress", required = false, dataType = "string")})
    public ContentResultForm<ItemListVo> listItemAndStateByStageId(@PathVariable("stageId") String stageId, @PathVariable("projInfoId") String projInfoId, @PathVariable("regionalism") String regionalism, String projectAddress) {
        try {
            logger.error("-----listItemAndStateByStageId----start--------");
            long l=System.currentTimeMillis();
            ItemListVo vo = restParallerApplyService.listItemAndStateByStageId(stageId, projInfoId, regionalism, projectAddress);
            logger.error("-----listItemAndStateByStageId----end--------耗时："+(System.currentTimeMillis()-l));
            return new ContentResultForm<>(true,vo);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","根据阶段id获取事项一单清列表数据异常");
        }
    }

    @PostMapping("mat/list")
    @ApiOperation(value = "阶段申报 --> 根据阶段ID、阶段情形ID集合、事项情形ID集合、事项版本ID集合获取材料一单清列表数据")
    public ContentResultForm<ItemListVo> listItemAndStateeByStageId(@RequestBody MatListParamVo matListParamVo){
        try {
            return new ContentResultForm(true,aeaItemMatService.getMatListByStateListAndItemListAndStageId(matListParamVo.getItemStateIds(),matListParamVo.getStageStateIds(),
                    matListParamVo.getCoreItemVerIds(),matListParamVo.getParallelItemVerIds(),matListParamVo.getCoreParentItemVerIds(),matListParamVo.getParaParentllelItemVerIds(),matListParamVo.getStageId()));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","根据阶段ID、阶段情形ID集合、事项情形ID集合、事项版本ID集合获取材料一单清列表数据异常");
        }
    }

    @PostMapping("net/process/start")
    @ApiOperation("阶段申报--> 提交申请")
    public ContentResultForm<ParallelApplyResultVo> startNetStageProcess(@Valid @RequestBody StageApplyDataPageVo stageApplyDataPageVo ){
        try {
            ParallelApplyResultVo vo = restApplyService.startStageProcess(stageApplyDataPageVo);
            return new ContentResultForm<>(true, vo, "申报成功!");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","申报出错!");
        }
    }
    @PostMapping("net/process/form/start")
    @ApiOperation("阶段申报--> 一张表单提前实例化申请")
    public ContentResultForm<String> startInstApply(String applySource,String applySubject,String  linkmanInfoId,String applyinstId){
        try {
            if(StringUtils.isNotBlank(applyinstId)) return new ContentResultForm<>(true, applyinstId, "申请实例已创建!");
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.createAeaHiApplyinst(applySource, applySubject, linkmanInfoId, AeaHiApplyinstConstants.STAGEINST_APPLY, null,ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue(),"0");
            return new ContentResultForm<>(true, aeaHiApplyinst.getApplyinstId(), "申报成功!");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"",e.getMessage());
        }
    }


    @GetMapping("itemByState/list/{stateId}/{stageId}/{regionalism}")
    @ApiOperation(value = "阶段申报 --> 根据情形ID和阶段ID获取绑定事项及(阶段)子情形列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "情形ID",name = "stateId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "阶段ID",name = "stageId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "区域编码", name = "regionalism", required = false, dataType = "string"),
            @ApiImplicitParam(value = "建设地点", name = "projectAddress", required = false, dataType = "string")

    })
    public ContentResultForm<Map<String, Object>> listItemByStateId(@PathVariable("stateId") String stateId, @PathVariable("stageId") String stageId, @PathVariable("regionalism") String regionalism, String projectAddress) {
        try {
            Map<String,Object> map = new HashMap<>(3);
            List<AeaItemBasic> itemList = aeaItemBasicService.getAeaItemBasicListByStageIdAndStateId(stageId, stateId, "0", SecurityContext.getCurrentOrgId());
            aeaItemBasicService.conversionBasicItemToSssx(itemList, regionalism, projectAddress, SecurityContext.getCurrentOrgId());
            List<AeaItemBasic> coreItemList = aeaItemBasicService.getAeaItemBasicListByStageIdAndStateId(stageId, stateId, "1", SecurityContext.getCurrentOrgId());
            aeaItemBasicService.conversionBasicItemToSssx(coreItemList, regionalism, projectAddress, SecurityContext.getCurrentOrgId());
            for (AeaItemBasic item:itemList){
                item.setParaStateList(aeaItemStateService.listAeaItemStateByParentId(item.getItemVerId(),"","ROOT",SecurityContext.getCurrentOrgId()));
            }
            for (AeaItemBasic coreItem:coreItemList){
                coreItem.setCoreStateList(aeaItemStateService.listAeaItemStateByParentId(coreItem.getItemVerId(),"","ROOT",SecurityContext.getCurrentOrgId()));
            }
            map.put("itemList",itemList);
            map.put("coreItemList",coreItemList);
            map.put("stateList",aeaParStateService.listAeaParStateByParentStateId(stageId,stateId,SecurityContext.getCurrentOrgId()));
            return new ContentResultForm(true,map);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","根据情形ID和阶段ID获取绑定事项及(阶段)子情形列表异常");
        }
    }

    @GetMapping("factor/list")
    @ApiOperation(value = "阶段申报 --> 获取根因子列表")
    public ContentResultForm<List<AeaParFactor>> listFactorByRootOrgId(){
        try {
            String topOrgId = SecurityContext.getCurrentOrgId();
            return new ContentResultForm<>(true,restParallerApplyService.listFactorByRootOrgId(topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","获取根因子列表数据异常");
        }
    }

    @GetMapping("factor/child/list/{factorId}")
    @ApiOperation(value = "阶段申报 --> 根据父因子获取子因子列表")
    @ApiImplicitParam(value = "父因子ID",name = "factorId",required = false,dataType = "string")
    public ContentResultForm<List<AeaParFactor>> listChildFactorByParentFactorId(@PathVariable("factorId") String factorId){
        try {
            String topOrgId = SecurityContext.getCurrentOrgId();
            return new ContentResultForm<>(true,restParallerApplyService.listChildFactorByParentFactorId(factorId));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","根据父根因子获取子因子列表数据异常");
        }
    }

    @GetMapping("intelligent/theme/list/{factorIds}")
    @ApiOperation(value = "阶段申报 --> 根据选择的因子智能推荐主题")
    @ApiImplicitParam(value = "父因子ID集合",name = "factorIds",required = false,dataType = "array")
    public ContentResultForm<Map<String,Object>> getThemeByFactorIds(@PathVariable("factorIds")String[] factorIds){
        try {
            String topOrgId = SecurityContext.getCurrentOrgId();
            return new ContentResultForm<>(true,restParallerApplyService.getThemeByFactorIds(factorIds));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","根据选择的因子智能推荐主题异常");
        }
    }

    @GetMapping("/theme/list")
    @ApiOperation(value = "阶段申报-->获取主题列表接口")
    public ContentResultForm<Map> getThemeList() {
        try {
            List<ThemeTypeVo> themeList = restMainService.getThemeTypeList(SecurityContext.getCurrentOrgId());
            Map<String,List<ThemeTypeVo>> map=new HashMap<>(2);
            map.put("mainLine",themeList.size()>0?themeList.stream().filter(v-> !"A00002".equals(v.getThemeTypeCode())).collect(Collectors.toList()) : new ArrayList<>());
            map.put("auxiLine",themeList.size()>0?themeList.stream().filter(v-> "A00002".equals(v.getThemeTypeCode())).collect(Collectors.toList()) : new ArrayList<>());
            return new ContentResultForm<>(true, map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm(false, "获取主题列表接口异常");
        }
    }


}
