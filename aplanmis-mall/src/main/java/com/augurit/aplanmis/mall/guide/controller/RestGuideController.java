package com.augurit.aplanmis.mall.guide.controller;


import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.att.service.impl.BscAttServiceImpl;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.constants.AeaItemBasicContants;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.mall.guide.service.RestGuideService;
import com.augurit.aplanmis.mall.guide.vo.RestGuideMatVo;
import com.augurit.aplanmis.mall.guide.vo.RestGuideVo;
import com.augurit.aplanmis.mall.guide.vo.RestSingleGuideVo;
import com.augurit.aplanmis.mall.main.vo.ItemListVo;
import com.augurit.aplanmis.mall.userCenter.vo.MatListParamVo;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rest/guide")
@Api(value = "办事指南",tags = "办事指南 --> 相关接口")
public class RestGuideController {
    Logger logger= LoggerFactory.getLogger(RestGuideController.class);

    @Autowired
    RestGuideService restGuideService;
    @Autowired
    AeaItemBasicService aeaItemBasicService;
    @Autowired
    AeaItemMatService aeaItemMatService;
    @Autowired
    AeaItemStateService aeaItemStateService;
    @Autowired
    IBscAttService bscAttService;
    @Autowired
    AeaParThemeService aeaParThemeService;
    @Autowired
    BscAttServiceImpl bscAttServiceImpl;
    @Autowired
    private FileUtilsService fileUtilsService;

    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    protected String topOrgId;


    @GetMapping("/toGuideIndexPage")
    @ApiOperation(value = "首页-->跳转办事指南页面接口")
    @ApiImplicitParams({@ApiImplicitParam(value = "部门ID",name = "chooseOrgId",required = false,dataType = "string"),
            @ApiImplicitParam(value = "主题ID",name = "themeId",required = false,dataType = "string"),
            @ApiImplicitParam(value = "主线阶段ID",name = "mainLineStageId",required = false,dataType = "string"),
            @ApiImplicitParam(value = "辅线阶段ID",name = "auxiliaryStageId",required = false,dataType = "string"),
            @ApiImplicitParam(value = "项目ID",name = "projInfoId",required = false,dataType = "string")} )
    public ModelAndView toMainIndexPage(ModelMap modelMap, String themeId, String mainLineStageId, String auxiliaryStageId, String chooseOrgId,String projInfoId){
        modelMap.put("themeId",themeId);
        modelMap.put("mainLineStageId",mainLineStageId);
        modelMap.put("auxiliaryStageId",auxiliaryStageId);
        modelMap.put("chooseOrgId",chooseOrgId);
        modelMap.put("projInfoId",projInfoId);
        return new ModelAndView("mall/guide/guideIndex");
    }

    @GetMapping("/tolistmatterPage")
    public ModelAndView tolistmatterPage(){
        return new ModelAndView("mall/listmatter/listmatter");
    }

    @GetMapping("/toSinglePage")
    public ModelAndView toSinglePage(){
        return new ModelAndView("mall/guide/components/singlePage");
    }

    @GetMapping("/item/list")
    @ApiOperation(value = "办事指南 --> 获取按部门申报时所有事项列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "部门ID",name = "orgId",required = false,dataType = "string"),
            @ApiImplicitParam(value = "页数",name = "pageNum",dataType = "Integer"),
            @ApiImplicitParam(value = "页面大小",name = "pageSize",dataType = "Integer")} )
    public ResultForm getAllItemList(String orgId,int  pageNum,int pageSize){
        try {
            AeaItemBasic aeaItemBasic = new AeaItemBasic();
            if (StringUtils.isEmpty(orgId)){
                return new ContentResultForm<PageInfo<AeaItemBasic>>(true,aeaItemBasicService.listAeaItemBasic(aeaItemBasic,pageNum,pageSize,topOrgId));
            }else{
                return new ContentResultForm<PageInfo<AeaItemBasic>>(true,aeaItemBasicService.getAeaItemBasicListByOrgId(orgId, AeaItemBasicContants.IS_CATALOG_NO,pageNum,pageSize));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"获取按部门申报时所有事项列表异常");
        }
    }

    @GetMapping("/guide/detailed/{stageId}")
    @ApiOperation(value = "办事指南 --> 并联申报 -->  获取阶段对应的办事指南数据")
    @ApiImplicitParam(value = "阶段ID",name = "stageId",required = false,dataType = "string" )
    public ContentResultForm<RestGuideVo> getGuideByStageId(@PathVariable("stageId") String stageId){
        try {
             return new ContentResultForm<RestGuideVo>(true,restGuideService.getGuideByStageId(stageId,topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","获取阶段对应的办事指南数据异常");
        }
    }

    @GetMapping("/guide/single/detailed/{itemVerId}")
    @ApiOperation(value = "办事指南 --> 单项申报 --> 获取事项对应的办事指南数据")
    @ApiImplicitParam(value = "事项版本ID",name = "itemVerId",required = true,dataType = "string" )
    public ContentResultForm<RestSingleGuideVo> getGuideByItemId(@PathVariable("itemVerId") String itemVerId){
        try {
            return new ContentResultForm<RestSingleGuideVo>(true,restGuideService.getGuideByItemVerId(itemVerId,topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","获取事项对应的办事指南数据异常");
        }
    }

    @GetMapping("commonMat/list/{itemVerId}")
    @ApiOperation(value = "单项办事指南 --> 根据事项版本id查询公共材料接口")
    @ApiImplicitParam(value = "事项版本ID",name = "itemVerId",required = true,dataType = "string" )
    public ResultForm getMatListByItemVerId(@PathVariable("itemVerId") String itemVerId){
        try {
            String[] itemVerIds = new String[2];
            itemVerIds[0] = itemVerId;
            return new ContentResultForm<>(true,aeaItemMatService.getMatListByItemVerIds(itemVerIds,"0","1"));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"根据事项版本id查询公共材料接口异常");
        }

    }

    @GetMapping("stateTree/list")
    @ApiOperation(value = "单项办事指南 -->根据事项版本id查询情形树")
    @ApiImplicitParam(value = "事项版本ID",name = "itemVerId",required = true,dataType = "string" )
    public ResultForm getStateTreeByItemVerId(String itemVerId){
        try {
            return new ContentResultForm<>(true,aeaItemStateService.listTreeAeaItemStateByItemVerId(itemVerId,""));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"根据事项版本id查询情形树异常");
        }
    }

    @GetMapping("matState/list")
    @ApiOperation(value = "根据事项情形id获取材料列表")
    @ApiImplicitParam(value = "itemStateIds",name = "事项情形id数组",required = true,dataType = "array")
    public ResultForm getMatByItemStateId(String[] itemStateIds){
        try {
            return new ContentResultForm<>(true,aeaItemMatService.getMatListByItemStateIds(itemStateIds));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"根据事项情形id获取材料列表异常");
        }
    }


    @GetMapping({"attLinkAndDetailNoPage/list"})
    @ApiOperation(value = "根据matId获取示例材料列表")
    @ApiImplicitParams({@ApiImplicitParam(value = "tableName",name = "tableName",required = true,dataType = "string"),
            @ApiImplicitParam(value = "pkName",name = "pkName",required = true,dataType = "string"),
            @ApiImplicitParam(value = "recordId",name = "recordId",required = true,dataType = "string"),
            @ApiImplicitParam(value = "attType",name = "attType",required = true,dataType = "string")})
    public ResultForm listAttLinkAndDetailNoPage(String tableName, String pkName, String recordId, String attType, String keyword) throws Exception {
        try {
            return new ContentResultForm<>(true,this.bscAttService.listAttLinkAndDetailNoPage(tableName, pkName, recordId, attType, topOrgId, keyword));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,"根据matId获取示例材料列表异常");
        }
    }

//    @GetMapping("/att/preview/{detailId}")
//    @ApiOperation(value = "办事指南--> 预览电子件")
//    @ApiImplicitParam(name = "detailId", value = "附件ID", dataType = "string", required = true)
//    public ModelAndView preview(@PathVariable("detailId") String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
//        ModelAndView modelAndView = fileUtilsService.preview(detailId, request, response, redirectAttributes);
//        return modelAndView;
//    }

    @GetMapping("/theme/file/list/{themeId}")
    @ApiOperation(value = "办事指南--> 根据主题ID获取流程图附件")
    @ApiImplicitParam(name = "themeId", value = "主题ID", dataType = "string", required = true)
    public ResultForm getFileList(@PathVariable("themeId") String themeId) {
        try {
            List<AeaParTheme> themeList = aeaParThemeService.getMaxVerAeaParTheme("",themeId);
            if (themeList==null||themeList.size()==0) return new ResultForm(false,"该主题未找到最大版本主题");
            String themeVerId = themeList.get(0).getThemeVerId();
            if(StringUtils.isNotBlank(themeVerId)){
                List<BscAttForm> attList = bscAttServiceImpl.listAttLinkAndDetailNoPage("AEA_PAR_THEME_VER", "THEME_VER_ID", themeVerId, "", topOrgId, "");
                return new ContentResultForm<>(true,attList.size()>0?attList.get(0):new ArrayList<>());//暂时取第一条数据
            }
            return new ResultForm(false,"该主题未上传流程图");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultForm(false,e.getMessage());
        }

    }
    @GetMapping("itemState/findByParentItemStateId/{itemStateId}")
        @ApiOperation("办事指南 --> 根据父情形ID查找(事项)子情形列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "情形ID",name = "itemStateId",required = true,dataType = "string")
    })
    public ContentResultForm<List<AeaItemState>> findByParentItemStateId(@PathVariable("itemStateId") String itemStateId ){
        try {
            return new ContentResultForm<>(true,aeaItemStateService.listAeaItemStateByParentId(null,"",itemStateId,topOrgId));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","根据父情形ID查找(事项)子情形列表失败!");
        }
    }


    /**********************************4.1版本办事指南接口************************************/

    @GetMapping("/stageAndItem/list/{themeId}")
    @ApiOperation("办事指南 --> 根据主题ID查询阶段及事项列表")
    @ApiImplicitParam()
    @ApiImplicitParams({
            @ApiImplicitParam(value = "主题ID",name = "themeId",required = true,dataType = "string")
    })
    public ContentResultForm getStageAndItemByThemeId(@PathVariable("themeId")String themeId){
        try {
            return new ContentResultForm<>(true,restGuideService.getStageAndItemByThemeId(themeId,topOrgId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","发生异常");
        }
    }

    @GetMapping("search/theme/list/{keyword}")
    @ApiOperation("办事指南 --> 先根据关键字查询符合条件主题")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键字",name = "keyword",required = true,dataType = "string")
    })
    public ContentResultForm searchThemeAndStageAndItemByKeyword(@PathVariable("keyword")String keyword){
        try {
            return new ContentResultForm<>(true,restGuideService.searchThemeAndStageAndItemByKeyword(keyword,topOrgId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","发生异常");
        }
    }

    @GetMapping("search/stageAndItem/list/{themeId}/{keyword}")
    @ApiOperation("办事指南 --> 再根据主题ID及关键字查询符合条件阶段事项")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "关键字",name = "keyword",required = true,dataType = "string"),
            @ApiImplicitParam(value = "主题ID",name = "themeId",required = true,dataType = "string")
    })
    public ContentResultForm searchStageAndItemByKeywordAndThemeId(@PathVariable("keyword")String keyword,@PathVariable("themeId")String themeId){
        try {
            return new ContentResultForm<>(true,restGuideService.searchStageAndItemByKeywordAndThemeId(themeId,keyword,topOrgId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","发生异常");
        }
    }

    @GetMapping("itemAndState/list/{stageId}")
    @ApiOperation(value = "办事指南 --> 根据阶段ID获取事项一单清列表数据")
    @ApiImplicitParams({@ApiImplicitParam(value = "阶段ID",name = "stageId",required = true,dataType = "string")})
    public ContentResultForm<ItemListVo> listItemAndStateByStageId(@PathVariable("stageId") String stageId) {
        try {
            logger.error("-----listItemAndStateByStageId----start--------");
            long l=System.currentTimeMillis();
            ItemListVo vo = restGuideService.listItemAndStateByStageId(stageId,topOrgId);
            logger.error("-----listItemAndStateByStageId----end--------耗时："+(System.currentTimeMillis()-l));
            return new ContentResultForm<>(true,vo);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","根据阶段id获取事项一单清列表数据异常");
        }
    }


    @PostMapping("mat/list")
    @ApiOperation(value = "办事指南 --> 根据阶段ID、阶段情形ID集合、事项情形ID集合、事项版本ID集合获取必选、可选材料列表")
    public ContentResultForm<RestGuideMatVo> listItemAndStateeByStageId(@RequestBody MatListParamVo matListParamVo){
        try {
            RestGuideMatVo  restGuideMatVo = new RestGuideMatVo();
            List<AeaItemMat> list = aeaItemMatService.getMatListByStateListAndItemListAndStageId(matListParamVo.getItemStateIds(),matListParamVo.getStageStateIds(),
                    matListParamVo.getCoreItemVerIds(),matListParamVo.getParallelItemVerIds(),matListParamVo.getCoreParentItemVerIds(),matListParamVo.getParaParentllelItemVerIds(),matListParamVo.getStageId(),null);
            //必选材料
            restGuideMatVo.setRequireMat(list.stream().filter(item->("1".equals(item.getAttIsRequire()))).collect(Collectors.toList()));
            //可选材料
            restGuideMatVo.setNoRequireMat(list.stream().filter(item->("0".equals(item.getAttIsRequire()))).collect(Collectors.toList()));
            return new ContentResultForm(true,restGuideMatVo);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","办事指南 --> 根据阶段ID、阶段情形ID集合、事项情形ID集合、事项版本ID集合获取必选、可选材料列表异常");
        }
    }
}
