package com.augurit.aplanmis.mall.log.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaHiItemStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateMapper;
import com.augurit.aplanmis.common.mapper.AeaLogThemeItemChangeMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import com.augurit.aplanmis.mall.log.vo.ApplyIteminstConfirmVo;
import com.augurit.aplanmis.mall.log.vo.IteminstConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestAeaLogThemeItemChangeService {
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaLogThemeItemChangeMapper aeaLogThemeItemChangeMapper;
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaParStageService aeaParStageService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;
    @Autowired
    private AeaItemStateMapper aeaItemStateMapper;
    /**
     *  1.根据applyinstId去aea_log_theme_item_change表找变更项目类型的数据，若有，则表示变更了项目类型，若没有，则表示没有变更项目类型。
     * 2.1若变更了项目类型，则只需根据applyinstId查出事项实例（关联log表带出部门意见）即可。
     * 2.2 若没有变更项目类型，则首先根据applyinstId查出所有事项实例(含删除的),然后去aea_log_theme_item_change表找出新增/删除的事项实例标记并附上部门意见，事项实例表有但日志表没有的事项则是没有变动的事项。
     */

    public ApplyIteminstConfirmVo getApplyIteminstConfirmDetail(String applyinstId) throws Exception {
        ApplyIteminstConfirmVo applyIteminstConfirmVo=new ApplyIteminstConfirmVo();
        List<AeaLogThemeItemChange> logs = getAeaLogThemeItemChangeByApplyinstId(applyinstId, "1",null);
        AeaHiParStageinst aeaHiParStateinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinstId);
        if(aeaHiParStateinst==null) throw new Exception("阶段没有实例化");
        applyIteminstConfirmVo.setStageId(aeaHiParStateinst.getStageId());
        applyIteminstConfirmVo.setStageinstId(aeaHiParStateinst.getStageinstId());
        applyIteminstConfirmVo.setStageName(aeaHiParStateinst.getStageName());
        AeaParStage stage = aeaParStageService.getAeaParStageById(aeaHiParStateinst.getStageId());
        if(logs.size()>0){//变更了项目类型
            AeaLogThemeItemChange aeaLogThemeItemChange=logs.get(0);
            applyIteminstConfirmVo.setIsThemeChange("1");
            applyIteminstConfirmVo.setDeptComments(aeaLogThemeItemChange.getOpsOpinion());
            applyIteminstConfirmVo.setOldThemeId(aeaLogThemeItemChange.getOldThemeId());
            applyIteminstConfirmVo.setOldThemeName(aeaLogThemeItemChange.getOldThemeName());
            applyIteminstConfirmVo.setThemeId(aeaLogThemeItemChange.getNewThemeId());
            applyIteminstConfirmVo.setThemeName(aeaLogThemeItemChange.getNewThemeName());
            List<AeaHiIteminst> parallelItems = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            //办理方式 0 多事项直接合并办理  1 按阶段多级情形组织事项办理
            if (stage != null && "0".equals(stage.getHandWay())) {
                setItemStateinstList(parallelItems, aeaHiParStateinst.getStageinstId(),applyinstId);
            }
            applyIteminstConfirmVo.setParallelIteminstList(parallelItems.size()>0?parallelItems.stream().map(IteminstConfirmVo::format).peek(vo->{
                vo.setIsApplySelected("0");
                vo.setIsDeptSelected("1");
                setIsMustSelectedAndDeptComments(applyinstId, vo);
            }).collect(Collectors.toList()):new ArrayList<>());
        }else{//没有变更项目类型
            applyIteminstConfirmVo.setIsThemeChange("0");
            applyIteminstConfirmVo.setThemeVerId(aeaHiParStateinst.getThemeVerId());
            applyIteminstConfirmVo.setThemeName(aeaHiParStateinst.getThemeName());
            List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstIds(Arrays.asList(new String[]{applyinstId}), ApplyType.UNIT.getValue(), null);
            if(iteminsts.size()==0) return applyIteminstConfirmVo;
            List<AeaLogThemeItemChange> itemLogs = getAeaLogThemeItemChangeByApplyinstId(applyinstId, "0", null);
            if (stage != null && "0".equals(stage.getHandWay())) {
                setItemStateinstList(iteminsts, aeaHiParStateinst.getStageinstId(),applyinstId);
            }
            List<String> addIteminstIds=itemLogs.stream().filter(v->("a".equals(v.getChangeAction())||("c".equals(v.getChangeAction())&&StringUtils.isNotBlank(v.getNewIteminstId())))).map(AeaLogThemeItemChange::getNewIteminstId).collect(Collectors.toList());
            List<String> delChangeIteminstIds=itemLogs.stream().filter(v->!"a".equals(v.getChangeAction())).map(AeaLogThemeItemChange::getOldIteminstId).collect(Collectors.toList());
            List<IteminstConfirmVo> parallelIteminsts = iteminsts.stream().map(IteminstConfirmVo::format).peek(vo -> {
                if(addIteminstIds.contains(vo.getIteminstId())){
                    vo.setIsApplySelected("0");
                    vo.setIsDeptSelected("1");
                }else if(delChangeIteminstIds.contains(vo.getIteminstId())){
                    vo.setIsDeptSelected("0");
                }else{
                    vo.setIsDeptSelected("1");
                    vo.setIsApplySelected("1");
                }
                setIsMustSelectedAndDeptComments(applyinstId, vo);
            }).collect(Collectors.toList());
            applyIteminstConfirmVo.setParallelIteminstList(parallelIteminsts.size()>0?parallelIteminsts:new ArrayList<>());
        }

        //并行申报
        List<AeaHiApplyinst> seriesApplyinsts = aeaHiApplyinstService.getSeriesAeaHiApplyinstListByParentApplyinstId(applyinstId, null);
        if(seriesApplyinsts.size()>0){//说明申报了并行事项
            List<IteminstConfirmVo> coreIteminsts=new ArrayList<>();
            for (AeaHiApplyinst applyinst:seriesApplyinsts){
               List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinst.getApplyinstId());
                setItemStateinstList(iteminsts,null,applyinst.getApplyinstId());
                AeaHiIteminst iteminst=iteminsts.get(0);
                IteminstConfirmVo vo=IteminstConfirmVo.format(iteminst);
                List<AeaLogThemeItemChange> seriesLogs = getAeaLogThemeItemChangeByApplyinstId(applyinst.getApplyinstId(),null,null);
                AeaItemBasic item = aeaItemBasicService.getAeaItemBasicByItemVerId(iteminst.getItemVerId());
                vo.setIsMustSelected(item.getIsDoneItem());
                if(seriesLogs.size()==1){//新增 或 删除 或变更主题
                    AeaLogThemeItemChange seriesLog=seriesLogs.get(0);
                    vo.setDeptComments(seriesLog.getOpsOpinion());
                    if("1".equals(seriesLog.getIsThemeChange())||"a".equals(seriesLog.getChangeAction())){
                        vo.setIsApplySelected("0");
                        vo.setIsDeptSelected("1");
                    }else {
                        vo.setIsApplySelected("1");
                        vo.setIsDeptSelected("0");
                    }
                }else if(seriesLogs.size()>1){//变更事项，比如从天河区的事项变为广州市的事项,那么将会有一条删除记录和新增记录
                    for (AeaLogThemeItemChange seriesLog:seriesLogs){
                        if(iteminst.getIteminstId().equals(seriesLog.getNewIteminstId())){//新增的
                            vo.setDeptComments(seriesLog.getOpsOpinion());
                            vo.setIsApplySelected("1");
                            vo.setIsDeptSelected("1");
                        }
                    }
                } else{
                    vo.setDeptComments("");
                    vo.setIsApplySelected("1");
                    vo.setIsDeptSelected("1");
                }
                coreIteminsts.add(vo);
            }
            applyIteminstConfirmVo.setCoreIteminstList(coreIteminsts.size()>0?coreIteminsts:new ArrayList<>());
        }else{
            applyIteminstConfirmVo.setCoreIteminstList(new ArrayList<>());
        }
        return applyIteminstConfirmVo;
    }

    private void setIsMustSelectedAndDeptComments(String applyinstId, IteminstConfirmVo vo) {
        try {
            AeaItemBasic item = aeaItemBasicService.getAeaItemBasicByItemVerId(vo.getItemVerId());
            if (item != null) {
                vo.setIsMustSelected(item.getIsDoneItem());
            }
            List<AeaLogThemeItemChange> itemlogs = getAeaLogThemeItemChangeByApplyinstId(applyinstId, ActiveStatus.ACTIVE.getValue(), vo.getIteminstId());
            vo.setDeptComments(itemlogs.size() > 0 ? itemlogs.get(0).getOpsOpinion() : "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setItemStateinstList(List<AeaHiIteminst> aeaHiIteminstList, String stageinstId,String applyinstId) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        for (AeaHiIteminst iteminst : aeaHiIteminstList) {
            AeaHiItemStateinst query = new AeaHiItemStateinst();
            query.setRootOrgId(SecurityContext.getCurrentOrgId());
            query.setApplyinstId(applyinstId);
            query.setStageinstId(stageinstId);
            List<AeaHiItemStateinst> stateinstList = aeaHiItemStateinstMapper.listAeaHiItemStateinst(query);
            String[] parentStateIds = stateinstList.size() > 0 ? stateinstList.stream().map(AeaHiItemStateinst::getParentStateinstId).toArray(String[]::new) : new String[0];
            String[] stateIds = stateinstList.size() > 0 ? stateinstList.stream().map(AeaHiItemStateinst::getExecStateId).toArray(String[]::new) : new String[0];
            if (parentStateIds.length == 0 || stateIds.length == 0) {
                iteminst.setItemStateinsts(list);
                continue;
            }
            List<AeaItemState> parentStateList = aeaItemStateMapper.listAeaItemStateByIds(parentStateIds);
            List<AeaItemState> stateList = aeaItemStateMapper.listAeaItemStateByIds(stateIds);
            for (AeaItemState parentState : parentStateList) {
                for (AeaItemState state : stateList) {
                    if (StringUtils.isNotBlank(state.getParentStateId()) && state.getParentStateId().equals(parentState.getItemStateId())) {
                        Map<String, String> map = new HashMap<>(4);
                        map.put("question", parentState.getStateName());
                        map.put("answer", state.getStateName());
                        map.put("questionId",parentState.getItemStateId());
                        map.put("answerId",state.getItemStateId());
                        list.add(map);
                    }
                }
            }
            iteminst.setItemStateinsts(list);
        }
    }

    public List<AeaLogThemeItemChange> getAeaLogThemeItemChangeByApplyinstId(String applyinstId,String isThemeChange,String iteminstId) throws Exception {
        AeaLogThemeItemChange aeaLogThemeItemChange=new AeaLogThemeItemChange();
        aeaLogThemeItemChange.setApplyinstId(applyinstId);
        aeaLogThemeItemChange.setIsThemeChange(isThemeChange);
        aeaLogThemeItemChange.setNewIteminstId(iteminstId);
        return aeaLogThemeItemChangeMapper.listAeaLogThemeItemChange(aeaLogThemeItemChange);
    }

    public List<AeaLogThemeItemChange> getAeaLogThemeItemChangeByApplyinstIds(List<String> applyinstIds) throws Exception {
        List<AeaLogThemeItemChange> logs=new ArrayList<>();
        for (String applyinstId:applyinstIds){
            AeaLogThemeItemChange aeaLogThemeItemChange=new AeaLogThemeItemChange();
            aeaLogThemeItemChange.setApplyinstId(applyinstId);
            logs.addAll(aeaLogThemeItemChangeMapper.listAeaLogThemeItemChange(aeaLogThemeItemChange));
        }
        return logs;
    }
}
