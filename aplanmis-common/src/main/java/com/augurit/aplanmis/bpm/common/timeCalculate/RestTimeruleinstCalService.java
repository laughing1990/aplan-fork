package com.augurit.aplanmis.bpm.common.timeCalculate;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.domain.ActStoTimerule;
import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleInstMapper;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleMapper;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.service.ActStoTimeruleInstService;
import com.augurit.agcloud.bpm.common.timeCalculate.TimeCalculateEngineBase;
import com.augurit.agcloud.bpm.common.timerule.BpmBaseTimeLimitRule;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RestTimeruleinstCalService extends TimeCalculateEngineBase {

    @Autowired
    private ActStoTimeruleInstMapper actStoTimeruleInstMapper;

    @Autowired
    private ActStoTimeruleMapper actStoTimeruleMapper;

    @Autowired
    private ActStoAppinstService actStoAppinstService;

    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private BpmProcessService bpmProcessService;

    @Autowired
    private ActStoTimeruleInstService actStoTimeruleInstService;

    private final static String IS_CONCLUDING_YES = "1";
    private final static String IS_CONCLUDING_NO = "0";

    @Override
    public void Startup() {

    }

    public void Startup(String id) throws Exception {

        List<ActStoTimeruleInst> actStoTimeruleInsts = new ArrayList();
        List<ActStoTimeruleInst> temp = new ArrayList();
        Map<String, ActStoTimerule> timeTemp = new HashMap();
        List<AeaLogApplyStateHist> applyStateHists = null;
        String instState = null;

        if (StringUtils.isBlank(id)) {
            ActStoTimeruleInst actStoTimeruleInst = new ActStoTimeruleInst();
            actStoTimeruleInst.setIsConcluding("0");
            actStoTimeruleInst.setOrgId(SecurityContext.getCurrentOrgId());
            actStoTimeruleInsts.addAll(actStoTimeruleInstMapper.listActStoTimeruleInst(actStoTimeruleInst));
        } else {
            actStoTimeruleInsts.add(actStoTimeruleInstMapper.getActStoTimeruleInstById(id));
        }
        for (ActStoTimeruleInst timeruleInst : actStoTimeruleInsts) {
            if (StringUtils.isBlank(timeruleInst.getOrgId())) continue;

            //如果当前流程已经挂起，则不更新时间。
            if (StringUtils.isNotBlank(timeruleInst.getProcInstId())) {
                if (bpmProcessService.isProcessSuspended(timeruleInst.getProcInstId())) {
                    continue;
                }
            }

            // 获取时限计算规则
            ActStoTimerule timerule = timeTemp.get(timeruleInst.getTimeruleId()) == null ? actStoTimeruleMapper.getActStoTimeruleById(timeruleInst.getTimeruleId()) : timeTemp.get(timeruleInst.getTimeruleId());

            // 判断规则是否启用、是否已删除
            if ("1".equals(timerule.getIsActive()) && "0".equals(timerule.getIsDeleted())) {
                // 实例时限计算规则类
                BpmBaseTimeLimitRule timeLimitRule = (BpmBaseTimeLimitRule) AplanmisTimeCalculateEngine.applicationContext.getBean(timerule.getTimeruleClassBeanId());
                if (timeLimitRule == null) continue;
                double timeCalculateResult = 0.0d;  //已经用时
                if ("1".equals(timeruleInst.getTimeruleInstType())) {   //流程时限计算
                    if (StringUtils.isBlank(timeruleInst.getProcInstId())) continue;
                    // 获取流程实例
                    HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(timeruleInst.getProcInstId()).singleResult();
                    if (processInstance == null) continue;
                    //判断是否为主流程
                    ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstByProcInstId(timeruleInst.getProcInstId());
                    Date currentTime = null; //办结时间或当前时间
                    Date startTime = null;   //已受理时间
                    if (actStoAppinst != null) {
                        AeaLogApplyStateHist applyStateHist = new AeaLogApplyStateHist();
                        applyStateHist.setApplyinstId(actStoAppinst.getMasterRecordId());
                        applyStateHist.setAppinstId(actStoAppinst.getAppinstId());
                        applyStateHist.setNewState(ApplyState.OUT_SCOPE.getValue());
                        applyStateHists = aeaLogApplyStateHistService.findAeaLogApplyStateHist(applyStateHist);//获取不予受理记录
                        if (applyStateHists.size() > 0) {
                            currentTime = applyStateHists.get(0).getTriggerTime();
                            startTime = processInstance.getStartTime();
                        } else {
                            applyStateHist.setNewState(ApplyState.ACCEPT_DEAL.getValue());
                            applyStateHists = aeaLogApplyStateHistService.findAeaLogApplyStateHist(applyStateHist);//获取已受理记录
                            //主流程的开始审批时间以申请实例已受理时间为准
                            if (applyStateHists.size() > 0 && applyStateHists.get(0).getTriggerTime() != null)
                                startTime = applyStateHists.get(0).getTriggerTime();
                            else
                                startTime = processInstance.getStartTime();

                            applyStateHist.setNewState(ApplyState.COMPLETED.getValue());
                            applyStateHists = aeaLogApplyStateHistService.findAeaLogApplyStateHist(applyStateHist);//获取已办结记录
                            //主流程的办结时间以申请实例办结时间为准
                            if (applyStateHists.size() > 0)
                                currentTime = applyStateHists.get(0).getTriggerTime();
                        }
                    } else {
                        startTime = processInstance.getStartTime();
                        currentTime = processInstance.getEndTime();
                    }

                    // 根据时限计算规则进行计算
                    timeCalculateResult = timeLimitRule.calculate(startTime, currentTime, timeruleInst.getOrgId());
                    // 获取流程实例的总挂起时间
                    timeCalculateResult = this.getProcessinstHangUpTime(startTime, timeCalculateResult, timeruleInst.getTimeruleUnit(), processInstance.getId(), null);
                    timeruleInst.setIsConcluding(currentTime == null ? IS_CONCLUDING_NO : IS_CONCLUDING_YES);
                }

                double timeCount = timeruleInst.getTimeLimit() - timeCalculateResult;   // 逾期时间：流程时限和已用时的时间差
                double remainingTime = (timeCount > 0.0d) ? timeCount : 0.0d;   //剩余用时：如果已逾期，剩余用时为0.0d
                remainingTime = this.timeAccurateCalculate(remainingTime);// 计算所得的已用时和剩余用时保留小数点后一位（其他位数舍弃），所得时间精确到0.5个（WD、ND、WH、NH）
                timeCalculateResult = this.timeAccurateCalculate(timeCalculateResult);
                timeruleInst.setUseLimitTime(timeCalculateResult);
                timeruleInst.setRemainingTime(remainingTime);
                timeruleInst.setOverdueTime(timeCount > 0.0d ? 0.0d : Math.abs(timeCount));// 未逾期取0.0，已逾期取timeCount的绝对值
                timeruleInst.setModifyTime(new Date());

                if (timeCount > 0.0d) {
                    if ("ND".equals(timerule.getTimeruleUnit()) || "WD".equals(timerule.getTimeruleUnit()))
                        instState = timeCount > 2 ? "1" : "2";  //小于或等于2天时，为预警状态
                    else
                        instState = timeCount > 48 ? "1" : "2";//小于或等于48小时，为预警状态
                } else {
                    instState = "3";
                    if (timeruleInst.getOverdueDate() == null)
                        timeruleInst.setOverdueDate(new Date());
                }
                timeruleInst.setInstState(instState); // 实例状态：1 正常，2 预警，3 逾期
                temp.add(timeruleInst);
            }
        }

        if (temp.size() > 0) {
            actStoTimeruleInstMapper.batchUpdateActStoTimeruleInst(temp);
        }
    }

    public void updateTimeruleinstByProcessinstId(String processinstId) throws Exception {

        if (StringUtils.isBlank(processinstId)) return;
        ActStoTimeruleInst actStoTimeruleInst = new ActStoTimeruleInst();
        actStoTimeruleInst.setProcInstId(processinstId);
        actStoTimeruleInst.setOrgId(SecurityContext.getCurrentOrgId());
        List<ActStoTimeruleInst> timeruleInsts = actStoTimeruleInstService.listActStoTimeruleInst(actStoTimeruleInst);
        for (ActStoTimeruleInst timeruleInst : timeruleInsts) {
            this.Startup(timeruleInst.getTimeruleInstId());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {

    }
}
