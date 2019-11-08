package com.augurit.aplanmis.rest.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaItemCondService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.state.AeaParStateService;
import com.augurit.aplanmis.rest.index.service.vo.BaseInfoVo;
import com.augurit.aplanmis.rest.userCenter.service.RestApplyMatService;
import com.augurit.aplanmis.rest.userCenter.service.RestApplyService;
import com.augurit.aplanmis.rest.userCenter.service.RestSeriesApplyService;
import com.augurit.aplanmis.rest.userCenter.vo.SeriesApplyDataPageVo;
import com.augurit.aplanmis.rest.userCenter.vo.SeriesApplyResultVo;
import com.augurit.aplanmis.rest.userCenter.vo.SeriesMatParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//单项申报
@RestController
@RequestMapping("rest/userCenter/apply/series")
@Api(value = "申办", tags = "申报 --> 单项申报接口")
public class RestSeriesApplyController {
    Logger logger = LoggerFactory.getLogger(RestSeriesApplyController.class);

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
    RestApplyMatService restApplyMatService;
    @Autowired
    AeaItemMatService aeaItemMatService;
    @Autowired
    RestSeriesApplyService restSeriesApplyService;


    @GetMapping("/toSingleApplyPage")
    @ApiOperation(value = "单项申报-->跳转单项申报页面接口")
    public ModelAndView toSingleApplyPage() {
        return new ModelAndView("mall/userCenter/components/singleDeclar");
    }

    @GetMapping("itemState/list/{itemVerId}")
    @ApiOperation("单项申报 --> 根据事项版本号获取根情形列表")
    @ApiImplicitParam(value = "事项版本ID", name = "itemVerId", required = true, dataType = "string")
    public ContentResultForm<List<AeaItemState>> listTreeAeaItemStateByItemVerId(@PathVariable("itemVerId") String itemVerId) {
        try {
            return new ContentResultForm<>(true, restSeriesApplyService.listRootAeaItemStateByItemVerId(itemVerId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "根据事项版本号获取情形列表失败!");
        }
    }

    @GetMapping("baseInfo/{projInfoId}/{itemVerId}")
    @ApiOperation(value = "单项申报 --> 根据事项版本ID、项目ID获取基础信息数据")
    @ApiImplicitParams({@ApiImplicitParam(value = "事项版本ID", name = "itemVerId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "项目ID", name = "projInfoId", dataType = "string")})
    public ContentResultForm<BaseInfoVo> getBaseInfoByItemVerIdAndProjInfoId(@PathVariable("projInfoId") String projInfoId, @PathVariable("itemVerId") String itemVerId) {
        try {
            return new ContentResultForm<>(true, restSeriesApplyService.getBaseInfoByItemVerIdAndProjInfoId(itemVerId, projInfoId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "根据事项版本ID、项目ID获取基础信息数据异常");
        }
    }

    @PostMapping("mat/list")
    @ApiOperation(value = "单项申报 --> 根据情形ID集合、事项版本ID获取材料一单清列表数据")
    public ContentResultForm<List<AeaItemMat>> listMatForItem(@RequestBody SeriesMatParamVo vo) {
        try {
            //单项都是实施事项
            return new ContentResultForm<>(true, aeaItemMatService.getMatListByStateListAndItemListAndStageId(vo.getStateIds(), null, new String[]{vo.getItemVerId()}, null, null, null, null), "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "根据情形ID、事项版本ID集合获取材料一单清列表数据异常");
        }
    }

    @GetMapping("itemState/getTreeCondByItemVerId/{itemVerId}")
    @ApiOperation("单项申报 --> 根据事项版本号获取root前置条件列表(包含子条件)")
    @ApiImplicitParam(value = "事项版本ID", name = "itemVerId", required = true, dataType = "string")
    public ContentResultForm<List<AeaItemCond>> getTreeCondByItemVerId(@PathVariable("itemVerId") String itemVerId) {
        try {
            return new ContentResultForm<>(true, restSeriesApplyService.getTreeCondByItemVerId(itemVerId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "根据事项版本号获取前置条件失败!");
        }
    }

    @GetMapping("itemState/getChildAeaItemCondListByCondId/{condId}")
    @ApiOperation("单项申报 --> 根据父条件ID查询子前置条件列表")
    @ApiImplicitParam(value = "前置条件ID", name = "condId", required = true, dataType = "string")
    public ContentResultForm<List<AeaItemCond>> getChildAeaItemCondListByCondId(@PathVariable String condId) {
        try {
            return new ContentResultForm<>(true, aeaItemCondService.getChildAeaItemCondListByCondId(condId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "根据父条件ID查询子前置条件列表失败!");
        }
    }

    @PostMapping("/series/processflow/start")
    @ApiOperation(value = "单项申报 --> 提交申请", httpMethod = "POST")
    public ContentResultForm<SeriesApplyResultVo> startSeriesFlow(@RequestBody SeriesApplyDataPageVo seriesApplyDataPageVo) throws Exception {
        SeriesApplyResultVo vo = restApplyService.startSeriesFlow(seriesApplyDataPageVo);
        return new ContentResultForm<>(true, vo, "Series start process success");
    }

}
