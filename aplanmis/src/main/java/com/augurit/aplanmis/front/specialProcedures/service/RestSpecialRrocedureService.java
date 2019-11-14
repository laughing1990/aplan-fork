package com.augurit.aplanmis.front.specialProcedures.service;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.day.service.WorkdayHolidayService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserInfoMapper;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserMapper;
import com.augurit.aplanmis.bpm.common.timeCalculate.RestTimeruleinstCalService;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.dic.BscDicCodeItemService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.front.specialProcedures.vo.SpecialDataVo;
import com.augurit.aplanmis.front.specialProcedures.vo.SpecialParamVo;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class RestSpecialRrocedureService {

    @Autowired
    private AeaHiIteminstMapper iteminstMapper;
    @Autowired
    private AeaHiItemSpecialMapper itemSpecialMapper;
    @Autowired
    private AeaHiApplyinstMapper applyinstMapper;
    @Autowired
    private AeaLinkmanInfoMapper linkmanInfoMapper;
    @Autowired
    private AeaProjInfoMapper projInfoMapper;
    @Autowired
    private AeaUnitInfoService unitInfoService;
    @Autowired
    private BscDicCodeItemService codeItemService;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private BpmProcessService bpmProcessService;
    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;
    @Autowired
    private AeaHiItemSpecialStateHistMapper itemSpecialStateHistMapper;
    @Autowired
    private OpuOmUserInfoMapper opuOmUserInfoMapper;
    @Autowired
    private OpuOmUserMapper opuOmUserMapper;
    @Autowired
    private WorkdayHolidayService workdayHolidayService;
    @Autowired
    private IBscAttService bscAttService;
    @Autowired
    private RestTimeruleinstCalService restTimeruleinstCalService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Map<String, Object> getCurrentSpecialStatus(String applyinstId, String iteminstId) throws Exception{
        AeaHiIteminst iteminst = iteminstMapper.getAeaHiIteminstById(iteminstId);
        AeaHiItemSpecial special = new AeaHiItemSpecial();
        special.setApplyinstId(applyinstId);
        special.setIteminstId(iteminstId);
        List<AeaHiItemSpecial> itemSpecials = itemSpecialMapper.listAeaHiItemSpecial(special);
        Map<String,Object> result = new HashMap<>();
        result.put("iteminst",iteminst);
        if(itemSpecials.size() == 0){
            result.put("special",new AeaHiItemSpecial());
        }else{
            result.put("special",itemSpecials.get(0));
        }
        return result;

    }

    public SpecialParamVo getBasicInfo(String applyinstId, String iteminstId) throws Exception{
        SpecialParamVo param = new SpecialParamVo();
        //如果开始了特殊程序回显信息
        AeaHiItemSpecial queryTmp = new AeaHiItemSpecial();
        queryTmp.setSpecialState(ItemStatus.SPECIFIC_PROC_START.getValue());
        queryTmp.setIteminstId(iteminstId);
        List<AeaHiItemSpecial> list = itemSpecialMapper.listAeaHiItemSpecial(queryTmp);
        if(list.size() > 0 && ItemStatus.SPECIFIC_PROC_START.getValue().equals(list.get(0).getSpecialState())){
            BeanUtils.copyProperties(list.get(0),param);
        }

        /*AeaHiItemSpecial itemSpecial = itemSpecialMapper.getAeaHiItemSpecialByIteminstId(iteminstId);
        if(itemSpecial!=null){
            BeanUtils.copyProperties(itemSpecial,param);
       } */
        AeaHiIteminst iteminst = iteminstMapper.getAeaHiIteminstById(iteminstId);
        AeaHiApplyinst applyinst = applyinstMapper.getAeaHiApplyinstById(applyinstId);


        //判断申办主体是单位还是个人1 单位，0 个人
        AeaLinkmanInfo linkmanInfo = linkmanInfoMapper.getAeaLinkmanInfoById(applyinst.getLinkmanInfoId());
        param.setLinkman(linkmanInfo.getLinkmanName());
        param.setLinkAddr(linkmanInfo.getLinkmanAddr());
        param.setLinkPhone(linkmanInfo.getLinkmanMobilePhone());
        if("1".equals(applyinst.getApplySubject())){
            AeaProjInfo projInfo = projInfoMapper.getAeaProjInfoByApplyinstId(applyinstId);
            List<AeaUnitInfo> unitInfos = unitInfoService.findApplyOwnerUnitProj(applyinstId, projInfo.getProjInfoId());
            if(unitInfos.size() > 0){
                AeaUnitInfo aeaUnitInfo = unitInfos.get(0);
                param.setCustomer(aeaUnitInfo.getApplicant());
                param.setLinkAddr(aeaUnitInfo.getApplicantDetailSite());
            }else{
                throw new IllegalArgumentException("查询不到客户主体信息！");
            }
        }else {
            param.setCustomer(linkmanInfo.getLinkmanName());
        }
        param.setIteminstCode(iteminst.getIteminstCode());
        param.setIteminstName(iteminst.getIteminstName());
        param.setChargeOrgId(iteminst.getApproveOrgId());
        param.setChargeOrgName(iteminst.getApproveOrgName());


        //当前用户信息
        OpuOmUserInfo userinfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(SecurityContext.getCurrentUserId());
        param.setCurrentUserName(userinfo.getUserName());
        param.setCurrentUserId(userinfo.getUserId());
        param.setCurrentUserPhone(userinfo.getUserMobile());
        return param;
    }

    public List<BscDicCodeItem> getDicItemByType(String codeType) throws Exception{
        List<BscDicCodeItem> codeItems = codeItemService.getActiveItemsByTypeCode(codeType, SecurityContext.getCurrentOrgId());
        return  codeItems;
    }

    public SpecialParamVo saveSpecial(SpecialParamVo specialVo) throws Exception {

        boolean isApproved = false;//是否审批通过特殊程序
        if("1".equals(specialVo.getApproveResult())){
            isApproved = true;
        }
        //插入special表
        if ("1".equals(specialVo.getIsFlowTrigger())) {
            ActStoAppinst appinst = new ActStoAppinst();
            appinst.setMasterRecordId(specialVo.getApplyinstId());
            List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(appinst);
            if (appinsts.size() < 1) throw new Exception("找不到流程模板实例！");
            specialVo.setAppinstId(appinsts.get(0).getAppinstId());
        }

        String specialId = UUID.randomUUID().toString();
        specialVo.setSpecialId(specialId);
        specialVo.setSpecialStartMatId(specialId + "-start");
        specialVo.setSpecialEndMatId(specialId + "-end");
        AeaProjInfo projInfo = projInfoMapper.getAeaProjInfoByApplyinstId(specialVo.getApplyinstId());
        specialVo.setProjInfoId(projInfo.getProjInfoId());
        String uid = SecurityContext.getCurrentUser().getUserName();
        Date insertDate = new Date();
        specialVo.setCreater(uid);
        specialVo.setCreateTime(insertDate);
        specialVo.setModifier(uid);
        specialVo.setModifyTime(insertDate);
        specialVo.setRootOrgId(SecurityContext.getCurrentOrgId());
        if(isApproved){
            specialVo.setSpecialState(ItemStatus.SPECIFIC_PROC_START.getValue());
        }else {
            specialVo.setSpecialState("");
        }

        itemSpecialMapper.insertAeaHiItemSpecial(specialVo);

        if(isApproved){//审批通过才挂起程序，修改对应字段的状态
            //更新事项状态和新增历史记录
            if ("1".equals(specialVo.getIsFlowTrigger())) {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(specialVo.getIteminstId(), specialVo.getTaskId(), specialVo.getAppinstId(), ItemStatus.SPECIFIC_PROC_START.getValue(),null);

                HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(specialVo.getTaskId()).singleResult();
                if (taskInstance == null) throw new Exception("找不到节点信息！");

                HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId()).singleResult();

                //当前流程未结束
                if (processInstance.getEndTime() == null) {
                    //挂起当前流程
                    bpmProcessService.suspendProcessInstanceByIdAndTaskinstId(taskInstance.getProcessInstanceId(), specialVo.getTaskId());
                }
            } else {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(specialVo.getIteminstId(), specialVo.getSpecialMemo(), "特殊程序开始", null, ItemStatus.SPECIFIC_PROC_START.getValue(),null);
            }

            //获取该事项实例的所有开始特殊程序状态根据时间降序
            AeaLogItemStateHist aeaLogItemStateHist = new AeaLogItemStateHist();
            aeaLogItemStateHist.setIteminstId(specialVo.getIteminstId());
            aeaLogItemStateHist.setNewState(ItemStatus.SPECIFIC_PROC_START.getValue());
            aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaLogItemStateHist> aeaLogItemStateHists = aeaLogItemStateHistService.findAeaLogItemStateHist(aeaLogItemStateHist);

            //创建特殊程序和事项历史记录关联表
            AeaHiItemSpecialStateHist itemSpecialStateHist = new AeaHiItemSpecialStateHist();
            itemSpecialStateHist.setSpecialStateHistId(UUID.randomUUID().toString());
            itemSpecialStateHist.setSpecialId(specialId);
            itemSpecialStateHist.setStateHistId(aeaLogItemStateHists.get(0).getStateHistId());
            itemSpecialStateHist.setCreater(SecurityContext.getCurrentUserId());
            itemSpecialStateHist.setCreateTime(new Date());
            itemSpecialStateHistMapper.insertAeaHiItemSpecialStateHist(itemSpecialStateHist);
        }
        return specialVo;

    }

    /**
     * 特殊程序详情页，特殊程序结束按钮停止
     * @param specialVo
     * @throws Exception
     */
    public void stopSpecial(SpecialParamVo specialVo) throws Exception{
        AeaHiItemSpecial tmp = new AeaHiItemSpecial();
        tmp.setIteminstId(specialVo.getIteminstId());
        tmp.setApplyinstId(specialVo.getApplyinstId());
        tmp.setIsFlowTrigger("1");
        tmp.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaHiItemSpecial> list = itemSpecialMapper.listAeaHiItemSpecial(tmp);

        if(list.size() == 0 ){
            throw new Exception("找不到该事项的特殊程序！！");
        }
        if(!ItemStatus.SPECIFIC_PROC_START.getValue().equals(list.get(0).getSpecialState())){
            throw new Exception("没有开始特殊程序，不能结束特殊程序！");
        }
        else{
            //更新特殊程序
            AeaHiItemSpecial special = new AeaHiItemSpecial();
            special.setSpecialId(specialVo.getSpecialId());
            special.setModifier(SecurityContext.getCurrentUserId());
            special.setModifyTime(new Date());
            special.setSpecialState(ItemStatus.SPECIFIC_PROC_END.getValue());
            special.setMoney(specialVo.getMoney());
            special.setSpecialResult(specialVo.getSpecialResult());
            special.setOpsUserName(specialVo.getOpsUserName());
            special.setOpsTime(specialVo.getOpsTime());
            itemSpecialMapper.updateAeaHiItemSpecial(special);

            //找到最新的那条特殊程序开始记录
            AeaLogItemStateHist aeaLogItemStateHist = new AeaLogItemStateHist();
            aeaLogItemStateHist.setIteminstId(specialVo.getIteminstId());
            aeaLogItemStateHist.setNewState(ItemStatus.SPECIFIC_PROC_START.getValue());
            aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaLogItemStateHist> aeaLogItemStateHists = aeaLogItemStateHistService.findAeaLogItemStateHist(aeaLogItemStateHist);
            if (aeaLogItemStateHists.size() > 0) {
                AeaLogItemStateHist logItemStateHist = aeaLogItemStateHists.get(0);

                if ("1".equals(specialVo.getIsFlowTrigger())) {
                    aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(specialVo.getIteminstId(), logItemStateHist.getTaskinstId(), specialVo.getAppinstId(), logItemStateHist.getOldState(), null);
                    HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(logItemStateHist.getTaskinstId()).singleResult();
                    if (taskInstance == null) throw new Exception("找不到节点信息！");
                    HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId()).singleResult();
                    //当前流程未结束
                    if (processInstance.getEndTime() == null) {
                        //激活当前流程
                        bpmProcessService.activateProcessInstanceById(taskInstance.getProcessInstanceId());
                        //更新时限计时
                        restTimeruleinstCalService.updateTimeruleinstByProcessinstId(taskInstance.getProcessInstanceId());
                    }
                } else {
                    aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(specialVo.getIteminstId(), specialVo.getSpecialResult(), "结束特殊程序", null, logItemStateHist.getOldState(), null);

                }
            }

            //找到最新的一条记录
            aeaLogItemStateHist.setIteminstId(specialVo.getIteminstId());
            aeaLogItemStateHist.setNewState(ItemStatus.SPECIFIC_PROC_START.getValue());
            aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaLogItemStateHist> logItemStateHists = aeaLogItemStateHistService.findAeaLogItemStateHist(aeaLogItemStateHist);
            AeaLogItemStateHist logItemStateHist2 = logItemStateHists.get(0);
            //创建特殊程序和事项历史记录关联表
            AeaHiItemSpecialStateHist itemSpecialStateHist = new AeaHiItemSpecialStateHist();
            itemSpecialStateHist.setSpecialStateHistId(UUID.randomUUID().toString());
            itemSpecialStateHist.setSpecialId(specialVo.getSpecialId());
            itemSpecialStateHist.setStateHistId(logItemStateHist2.getStateHistId());
            itemSpecialStateHist.setCreater(SecurityContext.getCurrentUserId());
            itemSpecialStateHist.setCreateTime(new Date());
            itemSpecialStateHistMapper.insertAeaHiItemSpecialStateHist(itemSpecialStateHist);
        }

    }

    /**
     * 审批详情页按钮停止特殊程序
     * @param applyinstId
     * @param iteminstId
     */
    public void btnStopSpecial(String applyinstId, String iteminstId,String isFlowTrigger) throws Exception{

            //找到当前特殊程序实例
            AeaHiItemSpecial special = new AeaHiItemSpecial();
            special.setIteminstId(iteminstId);
            special.setApplyinstId(applyinstId);
            special.setIsFlowTrigger("1");
            special.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaHiItemSpecial> list = itemSpecialMapper.listAeaHiItemSpecial(special);
            if(list.size() == 0 ){
                throw new Exception("找不到该事项的特殊程序！！");
            }
        AeaHiItemSpecial currentSpecial = list.get(0);
        if(!ItemStatus.SPECIFIC_PROC_START.getValue().equals(currentSpecial.getSpecialState())){
                throw new Exception("没有开始特殊程序，不能结束特殊程序！");
            }
            special.setModifier(SecurityContext.getCurrentUserId());
            special.setModifyTime(new Date());
            special.setSpecialState(ItemStatus.SPECIFIC_PROC_END.getValue());
            special.setSpecialId(currentSpecial.getSpecialId());
            itemSpecialMapper.updateAeaHiItemSpecial(special);


            //找到最新的那条特殊程序开始记录
            AeaLogItemStateHist aeaLogItemStateHist = new AeaLogItemStateHist();
            aeaLogItemStateHist.setIteminstId(iteminstId);
            aeaLogItemStateHist.setNewState(ItemStatus.SPECIFIC_PROC_START.getValue());
            aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaLogItemStateHist> aeaLogItemStateHists = aeaLogItemStateHistService.findAeaLogItemStateHist(aeaLogItemStateHist);
            AeaLogItemStateHist logItemStateHist = aeaLogItemStateHists.get(0);

            if("1".equals(isFlowTrigger)){
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(currentSpecial.getIteminstId(),logItemStateHist.getTaskinstId(),currentSpecial.getAppinstId(),logItemStateHist.getOldState(),null);
                HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(logItemStateHist.getTaskinstId()).singleResult();
                if (taskInstance == null) throw new Exception("找不到节点信息！");
                HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId()).singleResult();
                //当前流程未结束
                if (processInstance.getEndTime() == null) {
                    //激活当前流程
                    bpmProcessService.activateProcessInstanceById(taskInstance.getProcessInstanceId());
                    //更新时限计时
                    restTimeruleinstCalService.updateTimeruleinstByProcessinstId(taskInstance.getProcessInstanceId());
                }
            }else{
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(currentSpecial.getIteminstId(), currentSpecial.getApproveOpinion(), "结束特殊程序", null, aeaLogItemStateHist.getOldState(),null);

            }

        //找到最新的一条记录
        aeaLogItemStateHist.setIteminstId(iteminstId);
        aeaLogItemStateHist.setNewState(ItemStatus.SPECIFIC_PROC_START.getValue());
        aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaLogItemStateHist> logItemStateHists = aeaLogItemStateHistService.findAeaLogItemStateHist(aeaLogItemStateHist);
        AeaLogItemStateHist logItemStateHist2 = logItemStateHists.get(0);
        //创建特殊程序和事项历史记录关联表
        AeaHiItemSpecialStateHist itemSpecialStateHist = new AeaHiItemSpecialStateHist();
        itemSpecialStateHist.setSpecialStateHistId(UUID.randomUUID().toString());
        itemSpecialStateHist.setSpecialId(currentSpecial.getSpecialId());
        itemSpecialStateHist.setStateHistId(logItemStateHist2.getStateHistId());
        itemSpecialStateHist.setCreater(SecurityContext.getCurrentUserId());
        itemSpecialStateHist.setCreateTime(new Date());
        itemSpecialStateHistMapper.insertAeaHiItemSpecialStateHist(itemSpecialStateHist);
    }

    public List<OpuOmUser> getOrgUserList(String iteminstId) throws Exception{
        AeaHiIteminst iteminst = iteminstMapper.getAeaHiIteminstById(iteminstId);
        //获取审批部门下所有用户
        List<OpuOmUser> opuOmActiveUser = opuOmUserMapper.getOpuOmActiveUserByOrgId(iteminst.getApproveOrgId());
        return opuOmActiveUser;
    }

    /**
     * 根据applyinstId或者iteminstId获取特殊程序记录
     * @param applyinstId
     * @param iteminstId
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getSpecicalList(String applyinstId, String iteminstId) throws Exception{
        if(StringUtils.isBlank(applyinstId) && StringUtils.isBlank(iteminstId)){
            throw new Exception("applyinstId和iteminstId不能同时为空");
        }
        List<Map<String,Object>> result = new ArrayList<>();
        Map<String,Object> tmpObj = new HashMap<>();
        //如果iteminstId不为空，则查询该事项的所有特殊程序，否则显示所有事项的的所有特殊程序
        if (StringUtils.isNotBlank(iteminstId)) {
            AeaHiIteminst iteminst = iteminstMapper.getAeaHiIteminstById(iteminstId);
            tmpObj = getCurrentItemSpecialList(iteminst);
            result.add(tmpObj);
        }else{
            List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            for(int i =0,len = iteminsts.size();i<len;i++){
                tmpObj = getCurrentItemSpecialList(iteminsts.get(i));
                result.add(tmpObj);
            }
        }
        return result;
    }

    /**
     * 根据事项实例获取特殊程序记录
     * @param iteminst
     * @return
     * @throws Exception
     */
    public Map<String, Object> getCurrentItemSpecialList(AeaHiIteminst iteminst) throws Exception {
        Map<String, Object> specialInfo = new HashMap<>();
        specialInfo.put("iteminstId",iteminst.getIteminstId());
        specialInfo.put("iteminstName",iteminst.getIteminstName());
        List<AeaHiItemSpecial> specialList = itemSpecialMapper.getAeaHiItemSpecialByIteminstId(iteminst.getIteminstId());
        List<SpecialDataVo> result = new ArrayList<>();
        String tableName = "AEA_HI_ITEM_SPECIAL";
        String orgId = SecurityContext.getCurrentOrgId();
        String userName = SecurityContext.getCurrentUser().getUserName();
        for (AeaHiItemSpecial special : specialList) {
            SpecialDataVo temp = new SpecialDataVo();
            BeanUtils.copyProperties(special, temp);
            String[] recordIds1 = {special.getSpecialStartMatId()};
            List<BscAttForm> file1 = bscAttService.findByTableNameAndRecordIdsAndDirId(tableName, recordIds1, orgId, null, null);
            String[] recordIds2 = {special.getSpecialEndMatId()};
            List<BscAttForm> file2 = bscAttService.findByTableNameAndRecordIdsAndDirId(tableName, recordIds2, orgId, null, null);
            temp.setSpecialStartMatList(file1);
            temp.setSpecialEndMatList(file2);
            if(StringUtils.isBlank(temp.getOpsUserName())){
                temp.setOpsUserName(userName);
            }
            result.add(temp);
        }
        specialInfo.put("specialList", result);
        return specialInfo;
    }

    public String getSpecialEndDate(int dueNum, String startDate) throws Exception {
        String currentOrgId = SecurityContext.getCurrentOrgId();
        Date start = format.parse(startDate);
        start = workdayHolidayService.nextDay(start);
        Date specialEndDate = workdayHolidayService.calWorkdayFrom(start,dueNum, currentOrgId);
        String endDate = this.format.format(specialEndDate);
        return endDate;
    }
}
