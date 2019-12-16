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
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaItemRelevanceMapper;
import com.augurit.aplanmis.common.service.guide.*;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowItemService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowStageService;
import com.augurit.aplanmis.mall.guide.service.RestGuideService;
import com.augurit.aplanmis.mall.guide.vo.RestGuideVo;
import com.augurit.aplanmis.mall.guide.vo.RestSingleGuideVo;
import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

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
        List<AeaItemBasic> intermediaryServices = aeaItemRelevanceMapper.listAeaItemBasicByRelevance(itemBasic.getItemId());
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
