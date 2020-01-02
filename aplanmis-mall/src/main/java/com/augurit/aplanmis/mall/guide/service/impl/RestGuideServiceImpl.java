package com.augurit.aplanmis.mall.guide.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttStoreDb;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaItemRelevanceMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaParStateMapper;
import com.augurit.aplanmis.common.service.guide.*;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaItemPrivService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowItemService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowStageService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.mall.guide.service.RestGuideService;
import com.augurit.aplanmis.mall.guide.vo.*;
import com.augurit.aplanmis.mall.main.service.RestMainService;
import com.augurit.aplanmis.mall.main.vo.ItemListVo;
import com.augurit.aplanmis.mall.main.vo.ParallelApproveItemVo;
import com.augurit.aplanmis.mall.main.vo.ThemeTypeVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RestGuideServiceImpl implements RestGuideService {

    @Autowired
    AeaItemBasicService aeaItemBasicService;
    @Autowired
    AeaItemMatService aeaItemMatService;
    @Autowired
    AeaParStageService aeaParStageService;
    @Autowired
    AeaParStageGuideService aeaParStageGuideService;
    @Autowired
    AeaItemServiceBasicService aeaItemServiceBasicService;
    @Autowired
    AeaItemServiceWindowRelService aeaItemServiceWindowRelService;
    @Autowired
    AeaParStageChargesService aeaParStageChargesService;
    @Autowired
    AeaItemGuideService aeaItemGuideService;
    @Autowired
    AeaItemGuideQuestionsService aeaItemGuideQuestionsService;
    @Autowired
    AeaItemGuideExtendService aeaItemGuideExtendService;
    @Autowired
    AeaItemGuideMaterialsService aeaItemGuideMaterialsService;
    @Autowired
    private AeaItemStateService aeaItemStateService;
    @Autowired
    private BscAttMapper bscAttService;
    @Autowired
    private UploaderFactory uploaderFactory;
    @Autowired
    private BscAttDetailMapper bscAttDetailService;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;
    @Autowired
    AeaServiceWindowItemService aeaServiceWindowItemService;
    @Autowired
    private AeaServiceWindowStageService aeaServiceWindowStageService;
    @Autowired
    private AeaItemRelevanceMapper aeaItemRelevanceMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private RestMainService restMainService;

    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaParStateMapper aeaParStateMapper;
    @Autowired
    private AeaItemPrivService aeaItemPrivService;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private AeaHiItemInoutService aeaHiItemInoutService;

    @Override
    public RestGuideVo getGuideByStageId(String stageId,String rootOrgId) throws Exception {
        RestGuideVo restGuideVo = new RestGuideVo();
        //0表示并联审批事项
        List<AeaItemBasic> concList = aeaItemBasicService.getAeaItemBasicListByStageId(stageId,"0",null,rootOrgId);
        restGuideVo.setConcList(concList);

        AeaParStage aeaParStage = aeaParStageService.getAeaParStageById(stageId);
        if(aeaParStage==null){
            restGuideVo.setParallelList(new ArrayList<>());
            restGuideVo.setMatList(new ArrayList<>());
            restGuideVo.setAeaParStageGuide(new AeaParStageGuide());
            restGuideVo.setSbfsList(new ArrayList<>());
            restGuideVo.setBasicList(new ArrayList<>());
            restGuideVo.setBlddList(new ArrayList<>());
            return restGuideVo;
        }
        restGuideVo.setHandWay(aeaParStage.getHandWay());
        if ("1".equals(aeaParStage.getHandWay())){//这个字段为1，说明阶段配了情形和材料
            for (AeaItemBasic item : concList) {
                item.setMatList(aeaItemMatService.getMatListByItemVerIdAndStageId(item.getItemVerId(), stageId, "1", rootOrgId));
                if ("1".equals(item.getIsCatalog())) {//标准事项
                    //找到市级的实施事项,暂取第一条
                    List<AeaItemBasic> ssxxList = aeaItemBasicService.getSssxByItemIdAndRegionalism(item.getItemId(), null, null, rootOrgId);
                    if (ssxxList.size() > 0) {
                        item.setOrgName(ssxxList.get(0).getOrgName());
                    }
                }
            }
        }else {
            String[] concItemVerId = new String[1];
            for (AeaItemBasic item: concList) {
                if ("1".equals(item.getIsCatalog())) {//标准事项
                    //找到市级的实施事项,暂取第一条
                    List<AeaItemBasic> ssxxList = aeaItemBasicService.getSssxByItemIdAndRegionalism(item.getItemId(), null, null, rootOrgId);
                    if (ssxxList.size() > 0) {
                        concItemVerId[0] = ssxxList.get(0).getItemVerId();
                        item.setOrgName(ssxxList.get(0).getOrgName());
                    } else {
                        concItemVerId[0] = item.getItemVerId();
                    }
                } else {
                    concItemVerId[0] = item.getItemVerId();
                }
                List<AeaItemMat> matList = aeaItemMatService.getMatListByItemVerIds(concItemVerId,"1","1");
                if (matList!=null&&matList.size()>0){
                    item.setMatList(matList);
                }else {
                    List<AeaItemMat> matList1 =  new ArrayList<AeaItemMat>();
                    item.setMatList(matList1);
                }

            }
        }

        //1表示并行推进事项
        List<AeaItemBasic> parallelList = aeaItemBasicService.getAeaItemBasicListByStageId(stageId,"1",null,rootOrgId);
        String[] itemVerId=new String[1];
        for (AeaItemBasic item: parallelList) {
            if ("1".equals(item.getIsCatalog())) {//标准事项
                //找到市级的实施事项,暂取第一条
                List<AeaItemBasic> ssxxList = aeaItemBasicService.getSssxByItemIdAndRegionalism(item.getItemId(), null, null, rootOrgId);
                if (ssxxList.size() > 0) {
                    itemVerId[0] = ssxxList.get(0).getItemVerId();
                    item.setOrgName(ssxxList.get(0).getOrgName());
                } else {
                    itemVerId[0] = item.getItemVerId();
                }
            } else {
                itemVerId[0] = item.getItemVerId();
            }
            List<AeaItemMat> matList = aeaItemMatService.getMatListByItemVerIds(itemVerId,"1","1");
            if (matList!=null&&matList.size()>0){
                item.setMatList(matList);
            }else {
                List<AeaItemMat> matList1 =  new ArrayList<AeaItemMat>();
                item.setMatList(matList1);
            }
        }
        restGuideVo.setParallelList(parallelList);

        //收费依据
        List<AeaParStageCharges> chargesList = aeaParStageChargesService.getAeaParStageChargesListByStageId(stageId,rootOrgId);
        if (chargesList!=null&&chargesList.size()>0){
            restGuideVo.setByLaw(chargesList.get(0).getBylaw());
            restGuideVo.setFeeStand(chargesList.get(0).getFeeStand());
        }

        //办理地点
        List<String> blddList = new ArrayList<>(5);
        AeaServiceWindowStage query = new AeaServiceWindowStage();
        query.setRootOrgId(rootOrgId);
        query.setStageId(stageId);
        List<AeaServiceWindowStage> windowRelList = aeaServiceWindowStageService.listAeaServiceWindowStage(query);
        for (AeaServiceWindowStage aeaServiceWindowStage : windowRelList) {
            blddList.add(aeaServiceWindowStage.getWindowAddress() + "  电话：" + aeaServiceWindowStage.getLinkPhone());
        }
        restGuideVo.setBlddList(blddList);
        //审批依据
        restGuideVo.setBasicList(aeaItemServiceBasicService.getSubAeaItemServiceBasicListByStageId(stageId, rootOrgId));
        //设置阶段基础信息
        AeaParStageGuide aeaParStageGuide = aeaParStageGuideService.getAeaParStageGuideByStageId(stageId,rootOrgId);
        if(aeaParStageGuide==null) return restGuideVo;
        restGuideVo.setApplyObj(aeaParStageGuide.getApplyObj());

        List<String> sbfsList = new ArrayList<>(2);
        sbfsList.add("1."+aeaParStageGuide.getCkbllc());
        sbfsList.add("2."+aeaParStageGuide.getWsbllc());
        restGuideVo.setSbfsList(sbfsList);
        restGuideVo.setAcceptCond(aeaParStageGuide.getAcceptCond());
        restGuideVo.setHandleFlow(aeaParStageGuide.getHandleFlow());
        restGuideVo.setHandleTimelimitDesc(aeaParStageGuide.getHandleTimelimitDesc());
        //受理时间
        if (windowRelList!=null&&windowRelList.size()>0)
            restGuideVo.setSlsj(windowRelList.get(0).getWorkTime());
        restGuideVo.setComsupTel(aeaParStageGuide.getComsupTel());
        restGuideVo.setGuideDemo(aeaParStageGuide.getGuideDemo());
        restGuideVo.setWarmPrompt(aeaParStageGuide.getWarmPrompt());
        List<BscAttForm> attList = bscAttService.listAttLinkAndDetail("AEA_PAR_STAGE_GUIDE", "GUIDE_ATT", aeaParStageGuide.getGuideId(), "", rootOrgId, null);
        restGuideVo.setGuideAtt(attList.size() > 0 ? attList.get(0) : null);

        return restGuideVo;
    }

    @Override
    public RestSingleGuideVo getGuideByItemVerId(String itemVerId,String rootOrgId) throws Exception {
        //办事指南基本信息
        RestSingleGuideVo vo = new RestSingleGuideVo();
        AeaItemGuide aeaItemGuide = aeaItemGuideService.getAeaItemGuideListByItemVerId(itemVerId,rootOrgId);
        if(StringUtils.isNotBlank(aeaItemGuide.getWsbllc())) aeaItemGuide.setWsbllc(aeaItemGuide.getWsbllc().replaceAll("\\n","<br>"));
        if(StringUtils.isNotBlank(aeaItemGuide.getAcceptCondition())) aeaItemGuide.setAcceptCondition(aeaItemGuide.getAcceptCondition().replaceAll("\\n","<br>"));
        if(StringUtils.isNotBlank(aeaItemGuide.getCkbllc())) aeaItemGuide.setCkbllc(aeaItemGuide.getCkbllc().replaceAll("\\n","<br>"));
        if(StringUtils.isNotBlank(aeaItemGuide.getCkbllct())){
            String [] cklct = aeaItemGuide.getCkbllct().split(",");
            aeaItemGuide.setCkbllct(cklct[0]);
        }
        if(StringUtils.isNotBlank(aeaItemGuide.getWsbllct())){
            String [] wslct = aeaItemGuide.getWsbllct().split(",");
            aeaItemGuide.setWsbllct(wslct[0]);
        }
        vo.setAeaItemGuide(aeaItemGuide);
        //情形
        vo.setStateList(aeaItemStateService.listTreeAeaItemStateByItemVerId(itemVerId, null));
        //材料
        List<AeaItemMat> matList = aeaItemMatService.getMatListByItemVerIds(new String[]{itemVerId}, "1", "1");
        if(matList.size()>0){
            matList.stream().forEach(mat->{
                if(StringUtils.isNotBlank(mat.getMatFrom())){
                    BscDicCodeItem item = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("MAT_FROM", mat.getMatFrom(), rootOrgId);
                    mat.setMatFrom(item==null?"":item.getItemName());
                }
            });
        }
        vo.setMatList(matList);
        //设立依据
        List<AeaItemServiceBasic> basicList = aeaItemServiceBasicService.listAeaItemServiceBasicByitemVerId(itemVerId,rootOrgId);
        if (basicList!=null&&basicList.size()>0){
            vo.setBasicList(basicList);
        }else {
            vo.setBasicList(new ArrayList<>());
        }
        //办理窗口
        AeaServiceWindowItem aeaServiceWindowItem = new AeaServiceWindowItem();
        aeaServiceWindowItem.setItemVerId(itemVerId);
        aeaServiceWindowItem.setRootOrgId(rootOrgId);
        List<AeaServiceWindowItem> windowItems =  aeaServiceWindowItemService.listAeaServiceWindowItem(aeaServiceWindowItem);
        if (windowItems!=null&&windowItems.size()>0){
            vo.setWindowRelList(windowItems);
        }else {
            vo.setWindowRelList(new ArrayList<>());
        }
        //常见问题
        List<AeaItemGuideQuestions> questions =  aeaItemGuideQuestionsService.listAeaItemGuideQuestions(itemVerId);
        if (questions!=null&&questions.size()>0){
            vo.setQuestions(questions);
        }else {
            vo.setQuestions(new ArrayList<>());
        }
        //拓展信息
        AeaItemGuideExtend  aeaItemGuideExtend = aeaItemGuideExtendService.getAeaItemGuideExtendByItemVerId(itemVerId);
        if (aeaItemGuideExtend!=null){
            if(StringUtils.isNotBlank(aeaItemGuideExtend.getZzzResultGuid())){
                String[] guideArr = aeaItemGuideExtend.getZzzResultGuid().split(",");
                BscAttDetail bscAttDetail = bscAttDetailService.getBscAttDetailById(guideArr[0]);
                aeaItemGuideExtend.setZzzResultGuid(guideArr[0]);
                aeaItemGuideExtend.setZzzResultGuidName(bscAttDetail.getAttName());
            }
            vo.setAeaItemGuideExtend(aeaItemGuideExtend);
        }else {
            vo.setAeaItemGuideExtend(new AeaItemGuideExtend());
        }
        //中介服务
        AeaItemBasic itemBasic = aeaItemBasicMapper.getOneByItemVerId(itemVerId,rootOrgId);
        AeaItemBasic bzsx = aeaItemBasicService.getCatalogItemByCarryOutItemId(itemBasic.getItemId(),rootOrgId);//标准事项
        List<AeaItemBasic> intermediaryServices = aeaItemRelevanceMapper.listAeaItemBasicByRelevance(bzsx==null?itemBasic.getItemId():bzsx.getItemId());
        if (intermediaryServices!=null&&intermediaryServices.size()>0){
            vo.setIntermediaryServices(intermediaryServices);
        }else {
            vo.setIntermediaryServices(new ArrayList<>());
        }
        return vo;
    }



    /**
     * 电子件预览
     *
     * @return ModelAndView
     */
    public ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        BscAttDetail att = bscAttService.getAttDetailByDetailId(detailId);
        if (att != null) {
            //文档类型使用NTKO预览
            if (Pattern.matches("(?i).+\\.(doc|docx|xls|xlsx|pdf|ppt|pptx)$", att.getAttName())) {
                String url = "/rest/mats/att/read";
                String jumpUrl = "/rest/mats/att/ntko?online=true&suffixName=." + att.getAttFormat() + "&fileUrl=" + url + "?detailId=" + detailId;
                jumpUrl = URLEncoder.encode(jumpUrl, "UTF-8");
                modelAndView.addObject("jumpUrl", jumpUrl);
                modelAndView.setViewName("mall/ntko/ntkoOpenWin");
                return modelAndView;
            }
            if (Pattern.matches("(?i).+\\.(txt)$", att.getAttName())) {
                redirectAttributes.addAttribute("detailId", detailId);
                modelAndView.setViewName("redirect:/rest/mats/att/read");
                return modelAndView;
            }
            //其他类型直接下载
            if (!Pattern.matches("(?i).+\\.(png|jpg|jpeg|bmp|gif|svg|tiff|txt)$", att.getAttName())) {
                redirectAttributes.addAttribute("detailIds", detailId);
                modelAndView.setViewName("redirect:/rest/mats/att/download");
                return modelAndView;
            }
            //图片类型使用jquery-viewer预览
            modelAndView.setViewName("ui-jsp/agcloud/bsc/yunpan/showFile");
            String atName = att.getAttName();
            String attFormat = att.getAttFormat();
            if (false) {
                modelAndView.addObject("emtpyResult", "1");
            } else {
                byte[] content;
                String base64Content = null;
                String directoryPath = request.getServletContext().getRealPath("/");
                File file;
                try {
                    if (UploadType.MONGODB.getValue().equals(att.getStoreType())) {
                        //从MongoDB上下载
                        MongoDbAchieve mongoDbAchieve = (MongoDbAchieve) uploaderFactory.create(att.getStoreType());
                        content = mongoDbAchieve.getDownloadBytes(att.getDetailId());

                        if (content != null && content.length > 0) {
//                            base64Content = new BASE64Encoder().encode(content);
                            Base64.Encoder encoder = Base64.getEncoder();
                            base64Content = encoder.encodeToString(content);
                        } else {
                            modelAndView.addObject("emtpyResult", "1");
                        }
                    } else if (UploadType.DATABASE.getValue().equals(att.getStoreType())) {
                        List<BscAttStoreDb> stores = bscAttService.findAttStoreDbByIds(att.getDetailId());
                        content = stores.get(0).getAttContent();
                        if (content != null && content.length > 0) {
//                            base64Content = new BASE64Encoder().encode(content);
                            Base64.Encoder encoder = Base64.getEncoder();
                            base64Content = encoder.encodeToString(content);
                        } else {
                            modelAndView.addObject("emtpyResult", "1");
                        }
                    }
                    base64Content = base64Content.replaceAll("\r\n|\n", "");
                    String fullBase64 = "data:image/" + attFormat + ";base64," + base64Content;
                    modelAndView.addObject("fullBase64", fullBase64);
                    modelAndView.addObject("fileName", atName);
                    modelAndView.addObject("fileType", attFormat);
                    modelAndView.addObject("emtpyResult", "0");
                } catch (Exception e) {
                    modelAndView.addObject("emtpyResult", "1");
                }
            }
        } else {
            modelAndView.addObject("emtpyResult", "1");
        }
        return modelAndView;
    }

    @Override
    public RestStageAndItemVo getStageAndItemByThemeId(String themeId,String rootOrgId) throws Exception {
        try {
            RestStageAndItemVo restStageAndItemVo = new RestStageAndItemVo();
            AeaParTheme theme = aeaParThemeService.getAeaParThemeByThemeId(themeId);
            if (theme == null) return restStageAndItemVo;
            List<AeaParTheme> publishedThemes = aeaParThemeService.getTestRunOrPublishedVerAeaParTheme(theme.getThemeType(), rootOrgId);
            if (publishedThemes.size() == 0) return restStageAndItemVo;
            List<String> themeVerIds = publishedThemes.stream().filter(parTheme -> themeId.equals(parTheme.getThemeId())).map(AeaParTheme::getThemeVerId).collect(Collectors.toList());
            if (themeVerIds.size() == 0) return restStageAndItemVo;
            List<AeaParStage> stageList = aeaParStageService.listAeaParStageByThemeIdOrThemeVerId(StringUtils.EMPTY, themeVerIds.get(0), rootOrgId);
            List<RestStageVo> stageVos = stageList.stream().map(RestStageVo::build).collect(Collectors.toList());
            restStageAndItemVo.setStages(stageVos);

            AeaItemBasic searchItem = new AeaItemBasic();
            searchItem.setRootOrgId(rootOrgId);

            //并联事项
            List<RestItemListVo> paraItemListVos = new ArrayList<>();
            for (int i=0;i<stageVos.size();i++){
                List<AeaItemBasic> parallelItems = aeaItemBasicService.getAeaItemBasicListByStageId(stageVos.get(i).getStageId(),"0","", SecurityContext.getCurrentOrgId());
                RestItemListVo restItemListVo = setSssxInfo(searchItem, parallelItems);
                restItemListVo.setTitle("第"+NumToChinese.getChiness(i+1)+"阶段并联事项");
                restItemListVo.setIndex(i+1);
                paraItemListVos.add(restItemListVo);
            }

            //并行事项
            List<RestItemListVo> seriItemListVos = new ArrayList<>();
            for (int i=0;i<stageVos.size();i++){
                List<AeaItemBasic> seriItems = aeaItemBasicService.getAeaItemBasicListByStageId(stageVos.get(i).getStageId(),"1","", SecurityContext.getCurrentOrgId());
                RestItemListVo restItemListVo = setSssxInfo(searchItem, seriItems);
                restItemListVo.setTitle("第"+NumToChinese.getChiness(i+1)+"阶段并行事项");
                restItemListVo.setIndex(i+1);
                seriItemListVos.add(restItemListVo);
            }

            restStageAndItemVo.setParallelItems(paraItemListVos);
            restStageAndItemVo.setSeriItems(seriItemListVos);
            return restStageAndItemVo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    @Override
    public List<ThemeTypeVo> searchThemeAndStageAndItemByKeyword(String keyword,String rootOrgId) throws Exception {
        try {

            //1.从主题列表根据项目类型查找符合条件主题并标记
            List<ThemeTypeVo> themeTypeVos = restMainService.getThemeTypeList(rootOrgId);
            themeTypeVos.stream().forEach(themeTypeVo -> {
                themeTypeVo.getThemeList().stream().forEach(aeaParTheme -> {
                    if (StringUtils.isNotBlank(aeaParTheme.getThemeName())&&aeaParTheme.getThemeName().contains(keyword))
                        aeaParTheme.setIsSelected("1");
                });
            });

            //2.从事项及结果物反推查询符合条件主题并标记
            AeaItemBasic param = new AeaItemBasic();
            param.setItemName(keyword);
            List<AeaItemBasic> aeaItemBasics = aeaItemBasicService.getAeaItemBasicListByKeyword(keyword,"1");
            Set<AeaParStage> stages = new HashSet<>();
            if (aeaItemBasics.size()>0){
                for (AeaItemBasic aeaItemBasic : aeaItemBasics) {
                    stages.addAll(aeaParStageService.listAeaParStageByThemeIdOrThemeVerId(aeaItemBasic.getItemId(),aeaItemBasic.getBaseItemVerId(),rootOrgId));
                }
            }
            if (stages.size()>0){
                themeTypeVos.stream().forEach(themeTypeVo -> {
                    themeTypeVo.getThemeList().stream().forEach(aeaParTheme -> {
                        stages.stream().forEach(stage->{
                            if (aeaParTheme.getThemeVerId().equals(stage.getThemeVerId())){
                                aeaParTheme.setIsSelected("1");
                            }
                        });
                    });
                });
            }
            return themeTypeVos;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }


    @Override
    public ItemListVo listItemAndStateByStageId(String stageId,String rootOrgId,String isSelectState,String isFilterStateItem) throws Exception {
        ItemListVo vo = new ItemListVo();
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
        vo.setStageName(aeaParStage.getStageName());
        vo.setDueNum(aeaParStage.getDueNum());
        AeaParTheme aeaParTheme = aeaParThemeService.getAeaParThemeByThemeVerId(aeaParStage.getThemeVerId());
        vo.setThemeName(aeaParTheme.getThemeName());
        //情形
        List<AeaParState> stateList = null;
        //并联
        List<ParallelApproveItemVo> paraItemList = this.getRequiredItems(stageId,rootOrgId,isFilterStateItem);
        //并行
        List<ParallelApproveItemVo> coreItemList = this.getOptionalItems(stageId,rootOrgId,isFilterStateItem);
        if("1".equals(isSelectState)){
            for (ParallelApproveItemVo item:coreItemList){
                if ("1".equals(item.getIsCatalog())) {//标准事项
                    List<AeaItemBasic> carryOutItems = item.getCarryOutItems();//实施事项列表
                    AeaItemBasic currentCarryOutItem = item.getCurrentCarryOutItem();//默认实施事项
                    if (carryOutItems.size() > 0) {
                        for (AeaItemBasic basic : carryOutItems) {
                            List<AeaItemState> coreStateList = aeaItemStateService.listAeaItemStateByParentId(basic.getItemVerId(), "", "ROOT", rootOrgId);
                            basic.setCoreStateList(coreStateList.size() > 0 ? coreStateList : new ArrayList<>());
                            if (basic.getItemVerId().equals(currentCarryOutItem.getItemVerId())) {
                                currentCarryOutItem.setCoreStateList(coreStateList);
                            }
                        }
                    }
                } else {
                    item.setCoreStateList(aeaItemStateService.listAeaItemStateByParentId(item.getItemVerId(), "", "ROOT",rootOrgId));
                }
                if (item.getCarryOutItems()==null||item.getCarryOutItems().size()==0){
                    item.setCarryOutItems(new ArrayList<>());
                }
                if (item.getCurrentCarryOutItem()==null) item.setCurrentCarryOutItem(new AeaItemBasic());
            }
            //getIsNeedState 为1时为分情形
            if ("1".equals(aeaParStage.getIsNeedState())){
                stateList= aeaParStateMapper.listParStateByParentStateId(stageId, "ROOT",rootOrgId);
            }
            //hand_way为0时，需展示并联事项情形
            if ("0".equals(aeaParStage.getHandWay())){
                for (ParallelApproveItemVo item:paraItemList){
                    if ("1".equals(item.getIsCatalog())) {//标准事项
                        List<AeaItemBasic> carryOutItems = item.getCarryOutItems();//实施事项列表
                        AeaItemBasic currentCarryOutItem = item.getCurrentCarryOutItem();//默认实施事项
                        if (carryOutItems.size() > 0) {
                            for (AeaItemBasic basic : carryOutItems) {
                                List<AeaItemState> paraStateList = aeaItemStateService.listAeaItemStateByParentId(basic.getItemVerId(), "", "ROOT", rootOrgId);
                                basic.setCoreStateList(paraStateList.size() > 0 ? paraStateList : new ArrayList<>());
                                if (basic.getItemVerId().equals(currentCarryOutItem.getItemVerId())) {
                                    currentCarryOutItem.setParaStateList(paraStateList);
                                }
                            }
                        }
                    } else {
                        item.setParaStateList(aeaItemStateService.listAeaItemStateByParentId(item.getItemVerId(), "", "ROOT", rootOrgId));
                    }
                    if (item.getCarryOutItems()==null||item.getCarryOutItems().size()==0){
                        item.setCarryOutItems(new ArrayList<>());
                    }
                    if (item.getCurrentCarryOutItem()==null) item.setCurrentCarryOutItem(new AeaItemBasic());
                }
            }
        }
        vo.setParallelItemList(paraItemList==null?new ArrayList<>():paraItemList);
        vo.setCoreItemList(coreItemList==null?new ArrayList<>():coreItemList);
        vo.setStateList(stateList==null?new ArrayList<>():stateList);
        return vo;
    }

    @Override
    public RestGuideStateVo getRestGuideStateVo(String itemVerId, String topOrgId) throws Exception {

        RestGuideStateVo vo = new RestGuideStateVo();
        //办事指南基本信息
        AeaItemGuide aeaItemGuide = aeaItemGuideService.getAeaItemGuideListByItemVerId(itemVerId,topOrgId);
        BeanUtils.copyProperties(aeaItemGuide,vo);
        //情形
        List<AeaItemState> states = aeaItemStateService.listTreeAeaItemStateByItemVerId(itemVerId, null);
        List<AeaItemState> rootAndChildStates = new ArrayList<>();
        states.stream().forEach(sate->{
            if (sate.getAnswerStates()!=null && sate.getAnswerStates().size()>0){
                sate.getAnswerStates().stream().forEach(aeaItemState -> {
                    aeaItemState.setStateName(sate.getStateName()+"("+aeaItemState.getStateName()+")");
                    rootAndChildStates.add(aeaItemState);
                });
            }
        });

        List<RestGuideStateVo.RestStateInnerVo> stateInnerVos = rootAndChildStates.stream().map(RestGuideStateVo.RestStateInnerVo::build).collect(Collectors.toList());
        for (int i = 0; i < stateInnerVos.size(); i++) {
            stateInnerVos.get(i).setIndex("情形"+NumToChinese.getChiness(i+1));
        }

        for (RestGuideStateVo.RestStateInnerVo stateInnerVo : stateInnerVos) {
            //set情形下的材料
            List<AeaItemMat> mats = aeaItemMatService.getMatListByItemStateIds(new String[]{stateInnerVo.getStateId()});
            List<RestGuideStateVo.RestStateMatInnerVo> stateMatInnerVos
                    = mats.stream().map(RestGuideStateVo.RestStateMatInnerVo::build).collect(Collectors.toList());
            stateInnerVo.setMats(stateMatInnerVos);
        }
        vo.setStates(stateInnerVos);
        //材料
        vo.setMats(aeaItemMatService.getMatListByItemVerIds(new String[]{itemVerId}, "1", "1").stream().map(RestGuideStateVo.RestStateMatInnerVo::build).collect(Collectors.toList()));
        //拓展信息
        AeaItemGuideExtend  aeaItemGuideExtend = aeaItemGuideExtendService.getAeaItemGuideExtendByItemVerId(itemVerId);
        vo.setResultName(aeaItemGuideExtend.getResultName());

        return vo;
    }


    /**
     * 查询阶段必选事项
     *
     * @param stageId    阶段id
     */
    public List<ParallelApproveItemVo> getRequiredItems(String stageId,String rootOrgId,String isFilterStateItem) throws Exception {
        List<AeaItemBasic> coreItems = aeaItemBasicService.getAeaItemBasicListByStageId(stageId, "0", null,rootOrgId);
        if (coreItems.size() == 0) return new ArrayList<>();

        return postHandle(coreItems, stageId, "0",rootOrgId,isFilterStateItem);
    }

    /**
     * 查询阶段可选事项
     *
     * @param stageId    阶段id
     */
    public List<ParallelApproveItemVo> getOptionalItems(String stageId,String rootOrgId,String isFilterStateItem) throws Exception {
        List<AeaItemBasic> optionItems = aeaItemBasicService.getAeaItemBasicListByStageId(stageId, "1", null,rootOrgId);
        if (optionItems.size() == 0) return new ArrayList<>();
        return postHandle(optionItems, stageId, "1",rootOrgId,isFilterStateItem);
    }

    /**
     * 判断是否需要过滤情形事项，在不分情形时，不用过滤
     *
     * @param aeaItemBasics 阶段下的所有事项
     * @param stageId       阶段
     * @param isOptionItem  是并联审批还是并行推进
     * @return 返回事项vo
     * @throws Exception 查询事项异常
     */
    private List<ParallelApproveItemVo> postHandle(List<AeaItemBasic> aeaItemBasics, String stageId, String isOptionItem,String rootOrgId,String isFilterStateItem) throws Exception {
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);

        // 设置事项的审批组织信息
        List<String> itemVerIds = aeaItemBasics.stream().map(AeaItemBasic::getItemVerId).collect(Collectors.toList());
        List<String> orgIds = new ArrayList<>();
        final Map<String, String> orgNameMap = new HashMap<>();
        final Map<String, AeaItemPriv> privMap = new HashMap<>();

        if (itemVerIds.size() > 0) {
            List<AeaItemPriv> currentUserItemPriv = aeaItemPrivService.findCurrentUserItemPriv(itemVerIds.toArray(new String[0]));
            if (currentUserItemPriv != null) {
                currentUserItemPriv.forEach(priv -> {
                    orgIds.add(priv.getOrgId());
                    privMap.put(priv.getItemVerId(), priv);
                });

                if (orgIds.size() > 0) {
                    List<OpuOmOrg> orgNameByOrgIds = opuOmOrgMapper.getOrgNameByOrgIds(orgIds.toArray(new String[0]));
                    orgNameByOrgIds.forEach(org -> orgNameMap.put(org.getOrgId(), org.getOrgName()));
                }
            }
        }

        List<AeaItemBasic> resultItems = new ArrayList<>(aeaItemBasics.size());
        // 分情形时要过滤情形事项
        if ("1".equals(aeaParStage.getIsNeedState())&&"1".equals(isFilterStateItem)) {
            // 情形事项
            Set<String> stateItemVerIds = aeaItemBasicService.getAeaItemBasicListByStageIdAndStateId(stageId, null, isOptionItem,rootOrgId)
                    .stream().map(AeaItemBasic::getItemVerId).collect(Collectors.toSet());
            // 过滤情形事项
            aeaItemBasics.forEach(item -> {
                if (!stateItemVerIds.contains(item.getItemVerId())) {
                    resultItems.add(item);
                }
            });
        } else {
            resultItems.addAll(aeaItemBasics);
        }
        return  resultItems.stream().filter(CommonTools.distinctByKey(AeaItemBasic::getItemVerId)).map(ParallelApproveItemVo::build).peek(vo -> {
            String flag=vo.getIsCatalog();
            if("1".equals(flag)){//标准事项

                List<AeaItemBasic> sssxList = aeaItemBasicService.getSssxByItemIdAndRegionalism(vo.getItemId(), null, null, rootOrgId);
                vo.setCarryOutItems(sssxList);
                AeaItemBasic sssx=sssxList.size()>0?sssxList.get(0):null;
                if(sssx!=null){
                    vo.setCurrentCarryOutItem(sssx);
                    vo.setBaseItemVerId(vo.getItemVerId());
                    vo.setBaseItemName(vo.getItemName());
                }
            }
            AeaItemPriv aeaItemPriv = privMap.get(vo.getItemVerId());
            if (aeaItemPriv != null) {
                vo.setAllowManual(aeaItemPriv.getAllowManual());
                vo.setOrgId(aeaItemPriv.getOrgId());
                vo.setOrgName(orgNameMap.get(aeaItemPriv.getOrgId()));
            }
        })
                .collect(Collectors.toList());
    }


    @Override
    public RestStageAndItemVo searchStageAndItemByKeywordAndThemeId(String themeId, String keyword,String rootOrgId) throws Exception {
        try {
        //1.设置所有符合条件的事项及标记符合条件的阶段的index
        RestStageAndItemVo vo = this.getStageAndItemByThemeId(themeId,rootOrgId);
        Set<Integer> indexs = new HashSet<>();
        vo.getParallelItems().stream().forEach(parallelItem->{
            parallelItem.getItems().stream().forEach(item->{
                if (item.getItemName().contains(keyword)){
                    item.setIsSelected("1");
                    indexs.add(parallelItem.getIndex());
                }
                item.getResultMats().stream().forEach(resultMat->{
                    if (resultMat.getAeaMatCertName().contains(keyword)){
                        item.setIsSelected("1");
                        indexs.add(parallelItem.getIndex());
                    }
                });
            });

        });
        //2.循环步骤一符合条件的阶段index将阶段标记
        for (int i = 0; i < vo.getStages().size(); i++) {
            for (Integer j:indexs){
                if (j==i+1){
                    vo.getStages().get(i).setIsSelected("1");
                }
            }
        }
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    private RestItemListVo setSssxInfo(AeaItemBasic searchItem, List<AeaItemBasic> parallelItems) {
        RestItemListVo restItemListVo = new RestItemListVo();
        for (AeaItemBasic parallelItem : parallelItems) {
            searchItem.setItemSeq(parallelItem.getItemId());
            List<AeaItemBasic> childItems = aeaItemBasicMapper.listLatestAeaItemBasic(searchItem);
            if (childItems.size() > 0) parallelItem.setItemVerId(childItems.get(0).getItemVerId());
            List<AeaItemInout> resultMats=aeaHiItemInoutService.getAeaItemInoutMatCertByItemVerId(parallelItem.getItemVerId(),SecurityContext.getCurrentOrgId());
            parallelItem.setResultMats(resultMats);
        }
        restItemListVo.setItems(parallelItems.stream().map(RestItemListVo.RestitemVo::build).collect(Collectors.toList()));
        return restItemListVo;
    }

    public void readFile(String detailId,HttpServletRequest request, HttpServletResponse response)  throws Exception {
        if (com.augurit.agcloud.framework.util.StringUtils.isBlank(detailId)) {
            throw new InvalidParameterException(detailId);
        }
        BscAttDetail bscAttDetail = bscAttDetailService.getBscAttDetailById(detailId);
        if (bscAttDetail == null) {
            throw new InvalidParameterException(bscAttDetail);
        }
        OutputStream toClient = null;
        //从ftp获取数据
        byte[] buffer = null;
        if(UploadType.MONGODB.getValue().equals(bscAttDetail.getStoreType())){ //从MongoDB上下载
            MongoDbAchieve mongoDbAchieve = (MongoDbAchieve)uploaderFactory.create(bscAttDetail.getStoreType());
            buffer = mongoDbAchieve.getDownloadBytes(bscAttDetail.getDetailId());
        }
        try {
            response.reset();
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.addHeader("Content-Length", "" + buffer.length);   // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            if("txt".equals(bscAttDetail.getAttFormat())) {
                response.setContentType("text/plain");
            }
            toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (toClient != null) {
                toClient.close();
            }
        }
    }
}
