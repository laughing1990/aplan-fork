package com.augurit.aplanmis.common.service.search.impl;

import com.augurit.agcloud.bpm.common.domain.BpmHistoryCommentForm;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.front.service.BpmProcessFrontService;
import com.augurit.agcloud.bpm.front.vo.ExtendBpmHistoryCommentForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.dto.*;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.search.ApproveDataService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApproveDataServiceImpl implements ApproveDataService {

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaParStageService aeaParStageService;

    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;

    @Autowired
    private AeaItemBasicService aeaItemBasicService;

    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;
    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;
    @Autowired
    private BpmProcessFrontService bpmProcessFrontService;
    @Autowired
    private AeaHiApplyinstCorrectService aeaHiApplyinstCorrectService;
    @Autowired
    private AeaHiItemCorrectService aeaHiItemCorrectService;
    @Autowired
    private BpmTaskService bpmTaskService;

    @Override
    public List<AnnounceDataDto> searchAnnounceDataList(String keyword,String rootOrgId) {
        return aeaItemBasicMapper.searchAnnounceDataList(keyword, rootOrgId);
    }

    @Override
    public PageInfo<AnnounceDataDto> searchAnnounceDataList(String keyword, int pageNum,int pageSize,String rootOrgId) {
        PageHelper.startPage(pageNum,pageSize);
        List<AnnounceDataDto> list= aeaItemBasicMapper.searchAnnounceDataList(keyword,rootOrgId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ApproveDataDto> searchApproveDataList(String applyinstCode,String projInfoName,int pageNum,int pageSize,List<String> applyStates,String rootOrgId) {
        PageHelper.startPage(pageNum,pageSize);
        List<ApproveDataDto> list = aeaHiApplyinstMapper.searchApproveDataList(applyinstCode,projInfoName,applyStates,rootOrgId);
//        for (ApproveDataDto to:list){
//            BscDicCodeItem item = bscDicCodeService.getItemByTypeCodeAndItemCode("APPLYINST_STATE",to.getApplyinstState(),rootOrgId);
//            if (item!=null){
//                to.setApplyinstState(item.getItemName());
//            }
//        }
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ApproveProjInfoDto> searchApproveProjInfoListByUnitOrLinkman(String unitInfoId, String userInfoId, String state,String applyinstState, String keyword,String[] filterStates ,int pageNum, int pageSize,String localCode,String stageId) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        List<ApproveProjInfoDto> list;
        if (StringUtils.isNotBlank(stageId)){
            list = aeaHiIteminstMapper.getApproveProjInfoListByStageIdAndLocalCodeAndUnitOrLinkman(unitInfoId, userInfoId, state,applyinstState, keyword,filterStates,localCode,stageId);
        }else {
            list = aeaHiIteminstMapper.getApproveProjInfoListByUnitOrLinkman(unitInfoId, userInfoId, state,applyinstState, keyword,filterStates);
        }
        //this.convertStateCodeToName(SecurityContext.getCurrentOrgId(), list);
        convertCommentByState(list);
//        if ("0".equals(state)) {//已办结
//            list.stream().forEach(itemInst -> {
//                if (itemInst.getEndTime() != null) {
//                    itemInst.setDueNum(new Long(DateUtils.getWorkingDay(itemInst.getStartTime(), itemInst.getEndTime())));
//                }
//            });
//        }
        return new PageInfo<>(list);
    }
    @Autowired
    private AeaHiSmsInfoService aeaHiSmsInfoService;

    private void convertCommentByState(List<ApproveProjInfoDto> list) throws Exception {
        for (ApproveProjInfoDto dto : list) {
            List<AeaHiIteminst> iteminst = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(dto.getApplyinstId(),"0");
            dto.setIteminst(iteminst.size()>0?ApproveProjInfoDto.formatList(iteminst):new ArrayList<>());
            if (ApplyState.OUT_SCOPE.getValue().equals(dto.getApplyinstState())) {
                AeaLogApplyStateHist query = new AeaLogApplyStateHist();
                query.setRootOrgId(SecurityContext.getCurrentOrgId());
                query.setApplyinstId(dto.getApplyinstId());
                query.setNewState(dto.getApplyinstState());
                List<AeaLogApplyStateHist> applyLogList = aeaLogApplyStateHistService.findAeaLogApplyStateHist(query);
                if (applyLogList.size() > 0) {
                    if (StringUtils.isNotBlank(applyLogList.get(0).getOpsUserOpinion())) {
                        dto.setApproveComments(applyLogList.get(0).getOpsUserOpinion());
                    } else {//日志表没有意见，则根据taskId去意见表查询
                        String taskId = applyLogList.get(0).getTaskinstId();
                        if (StringUtils.isNotBlank(taskId)) {
                            HistoricTaskInstance task = bpmTaskService.getHistoryTaskByTaskId(taskId);
                            if(task==null) {
                                dto.setApproveComments("");
                            }else{
                                List<BpmHistoryCommentForm> comments = bpmTaskService.getHistoryCommentsByTaskId(task.getProcessInstanceId(), taskId);
                                dto.setApproveComments(comments.size() > 0 ? comments.get(0).getCommentMessage() : "");
                            }
                        }
                    }
                }
            }else if(ApplyState.IN_THE_SUPPLEMENT.getValue().equals(dto.getApplyinstState())){
                AeaHiApplyinstCorrect correct = aeaHiApplyinstCorrectService.getCurrentCorrectinst(dto.getApplyinstId());
                dto.setApproveComments(correct!=null ? correct.getCorrectMemo(): "");
            }
            dto.setIsNetSignPrev(false);
            dto.setIsExistUnPass(false);
            AeaHiSmsInfo sms = aeaHiSmsInfoService.getAeaHiSmsInfoByApplyinstId(dto.getApplyinstId());
            List<String> states = iteminst.stream().map(AeaHiIteminst::getIteminstState).collect(Collectors.toList());
            if(ApplyState.COMPLETED.getValue().equals(dto.getApplyinstState())){//已办结
                if(states.contains(ItemStatus.DISAGREE.getValue())) dto.setIsExistUnPass(true);
            }
            if(sms==null) continue;
            if("2".equals(sms.getReceiveMode())){//网上签收
                if("0".equals(sms.getReceiveType())){//多次领取，只要有办结的事项就能领取
                    if(states.contains(ItemStatus.AGREE_TOLERANCE.getValue())||states.contains(ItemStatus.AGREE.getValue()))
                        dto.setIsNetSignPrev(true);
                }else{//一次领取，则需要申报办结才能领取
                    if(ApplyState.COMPLETED.getValue().equals(dto.getApplyinstState()))
                        dto.setIsNetSignPrev(true);
                }
            }

        }
    }

    @Override
    public PageInfo<ApproveProjInfoDto> searchScheduleInquireListByUnitInfoIdOrLinkman(String unitInfoId, String userInfoId,String keyword,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(aeaHiIteminstMapper.getScheduleInquireListByUnitInfoIdOrLinkman(unitInfoId,userInfoId,keyword));
    }

    @Override
    public List<AeaProjInfo> getScheduleProjListByUnitInfoIdOrLinkman(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return aeaHiIteminstMapper.getScheduleProjListByUnitInfoIdOrLinkman(keyword, unitInfoId, userInfoId,"root");
    }

    @Override
    public List<AeaProjInfo>  getScheduleProjListByUnitInfoIdOrLinkmanNoPage(String unitInfoId, String userInfoId, String keyword,String parentProjInfoId){
        return aeaHiIteminstMapper.getScheduleProjListByUnitInfoIdOrLinkman(keyword, unitInfoId, userInfoId,parentProjInfoId);
    }


    @Override
    public PageInfo<ApproveProjInfoDto> searchIteminstApproveInfoListByUnitIdAndUserId(String keyword, String unitInfoId, String userId,int pageNum,int pageSize) throws Exception {
//        if (StringUtils.isNotBlank(unitInfoId)&&StringUtils.isNotBlank(userId)){
//            unitInfoId = "";//委托人要保存项目单位信息及项目联系人信息，查找时至unitId为空，根据userId查找
//        }
        Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
        com.augurit.agcloud.framework.ui.pager.PageHelper.startPage(page);
        List<ApproveProjInfoDto> list = aeaHiIteminstMapper.getIteminstApproveInfoListByUnitIdAndUserId(keyword,unitInfoId,userId);
        for (ApproveProjInfoDto dto : list) {
            //AeaParStage stage = aeaParStageService.getAeaParStageByIteminstId(dto.getIteminstId());
//            if ("0".equals(dto.getIsSeriesApprove()) && StringUtils.isNotBlank(dto.getStageName())) {
//                dto.setStageIteminstName("【" + dto.getStageName() + "】" + dto.getStageIteminstName());
//            }
            String promiseTime = DateUtils.addDateByWorkDay(dto.getStartTime(), Math.toIntExact(Long.valueOf(dto.getDueNum())));
            dto.setPromiseTime(promiseTime);

            if (ItemStatus.DISAGREE.getValue().equals(dto.getIteminstState())
                    || ItemStatus.OUT_SCOPE.getValue().equals(dto.getIteminstState())
                    || ItemStatus.REFUSE_DEAL.getValue().equals(dto.getIteminstState())
                    || ItemStatus.SPECIFIC_PROC_START.getValue().equals(dto.getIteminstState())) {
                AeaLogItemStateHist query = new AeaLogItemStateHist();
                query.setRootOrgId(SecurityContext.getCurrentOrgId());
                query.setIteminstId(dto.getIteminstId());
                query.setNewState(dto.getIteminstState());
                List<AeaLogItemStateHist> itemLogList = aeaLogItemStateHistService.findAeaLogItemStateHist(query);
                if (itemLogList.size() > 0) {
                    if (StringUtils.isNotBlank(itemLogList.get(0).getOpsUserOpinion())) {
                        dto.setApproveComments(itemLogList.get(0).getOpsUserOpinion());
                    } else {//日志表没有意见，则根据taskId去意见表查询
                        String taskId = itemLogList.get(0).getTaskinstId();
                        if (StringUtils.isNotBlank(taskId)) {
                            HistoricTaskInstance task = bpmTaskService.getHistoryTaskByTaskId(taskId);
                            if(task==null) {
                                dto.setApproveComments("");
                            }else{
                                List<BpmHistoryCommentForm> comments = bpmTaskService.getHistoryCommentsByTaskId(task.getProcessInstanceId(), taskId);
                                dto.setApproveComments(comments.size() > 0 ? comments.get(0).getCommentMessage() : "");
                            }
                        }
                    }
                }
            }else if(ItemStatus.CORRECT_MATERIAL_START.getValue().equals(dto.getIteminstState())){//补正
                AeaHiItemCorrect correct = aeaHiItemCorrectService.getCurrentCorrectinst(dto.getIteminstId());
                dto.setApproveComments(correct!=null ? correct.getCorrectMemo(): "");
            }
        }
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SupplementInfoDto> searchMatComplet(String unitInfoId, String userInfoId,String supplyState, int pageNum, int pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        //List<SupplementInfoDto> list=aeaHiIteminstMapper.searchMatComplet(unitInfoId,userInfoId,supplyState);
        //return new PageInfo<>(list);
        return null;
    }

    @Override
    public PageInfo<SupplementInfoDto> searchMatCompletByUser(String unitInfoId, String userInfoId,String keyword, int pageNum, int pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        List<SupplementInfoDto> list=aeaHiIteminstMapper.searchMatCompletByUser(unitInfoId,userInfoId,keyword);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SupplementInfoDto> searchSupplementInfo(String unitInfoId, String userInfoId,String supplyState, int pageNum, int pageSize) throws Exception {
        PageHelper.startPage(pageNum,pageSize);
        List<SupplementInfoDto> list=aeaHiIteminstMapper.searchSupplementInfo(unitInfoId,userInfoId,supplyState);
        return new PageInfo<>(list);
    }

    @Override
    public ProjStateDto searchProjStateDtoByApplyinstId(String applyinstId) throws Exception {
        ProjStateDto projStateDto=new ProjStateDto();
        if(StringUtils.isBlank(applyinstId)) return projStateDto;
        AeaHiApplyinst applyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        if(applyinst==null) return projStateDto;
        projStateDto.setApplyinstCode(applyinst.getApplyinstCode());
        BscDicCodeItem dicApplyinstSource = bscDicCodeService.getItemByTypeCodeAndItemCode("APPLYINST_SOURCE", applyinst.getApplyinstSource(), SecurityContext.getCurrentOrgId());
        projStateDto.setApplyinstSource(dicApplyinstSource==null?"":dicApplyinstSource.getItemName());
        //BscDicCodeItem dicApplyinstState = bscDicCodeService.getItemByTypeCodeAndItemCode("APPLYINST_STATE",applyinst.getApplyinstState(), SecurityContext.getCurrentOrgId());
        //projStateDto.setApplyinstState(dicApplyinstState==null?"":dicApplyinstState.getItemName());
        projStateDto.setIsSeriesApprove(applyinst.getIsSeriesApprove());
        if("1".equals(applyinst.getIsSeriesApprove())){//单项
            List<AeaHiIteminst> iteminstList = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applyinstId,"0");
            if(iteminstList.size()==0) return projStateDto;
            AeaItemBasic itemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(iteminstList.get(0).getItemVerId());
            if(itemBasic==null) return projStateDto;
            projStateDto.setDueNum(itemBasic.getDueNum()+"工作日");
            projStateDto.setRestProcessingTime("");//待定 TODO
            projStateDto.setStageItemName(itemBasic.getItemName());
        }else{//并联
            AeaHiParStageinst stageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinstId);
            if(stageinst==null) return projStateDto;
            AeaParStage stage = aeaParStageService.getAeaParStageById(stageinst.getStageId());
            if(stage==null) return projStateDto;
            projStateDto.setDueNum(stage.getDueNum()+"工作日");
            projStateDto.setRestProcessingTime("");//待定 TODO
            projStateDto.setStageItemName(stage.getStageName());
            projStateDto.setStageId(stage.getStageId());
            projStateDto.setStageInstId(stageinst.getStageinstId());
        }
        List<AeaProjInfo> projList = aeaProjInfoService.findApplyProj(applyinstId);
        projStateDto.setProjInfoId(projList.size()>0?projList.get(0).getProjInfoId():"");
        projStateDto.setProjName(projList.size()>0?projList.get(0).getProjName():"");
        return projStateDto;
    }

    /**
     * 转换状态码为状态描述
     */
    private void convertStateCodeToName(String currentOrgId, List<ApproveProjInfoDto> list) {
        List<BscDicCodeItem> listItemCode=new ArrayList<>();
        if (StringUtils.isNotBlank(currentOrgId)) {
            listItemCode = bscDicCodeService.getActiveItemsByTypeCode("APPLYINST_STATE", currentOrgId);
        }
        for (ApproveProjInfoDto itemInst : list) {
            for (BscDicCodeItem codeItem : listItemCode) {
                if (org.apache.commons.lang3.StringUtils.equals(org.apache.commons.lang3.StringUtils.trim(itemInst.getApplyinstState()), codeItem.getItemCode())) {
                    itemInst.setApplyinstState(codeItem.getItemName());
                    break;
                }
            }
        }
    }
}
