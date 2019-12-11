package com.augurit.aplanmis.bpm.common.timeCalculate;


import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.domain.ActStoTimerule;
import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleInstMapper;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleMapper;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.timeCalculate.TimeCalculateEngineBase;
import com.augurit.agcloud.bpm.common.timerule.BpmBaseTimeLimitRule;
import com.augurit.agcloud.bsc.domain.BscJobTimer;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.domain.AeaToleranceTimeInst;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.service.item.AeaToleranceTimeInstService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Author: lucas Chan
 * Date: 2019-7-30
 * Description: 工程建设系统流程实例计算已用时方法
 */
@Component
public class AplanmisTimeCalculateEngine extends TimeCalculateEngineBase {

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
    private AeaToleranceTimeInstService aeaToleranceTimeInstService;

    public static ApplicationContext applicationContext = null;

    private final static String IS_CONCLUDING_YES = "1";
    private final static String IS_CONCLUDING_NO = "0";

    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String topOrgId;

    /**
     * 流程用时计算方法
     *
     * @throws Exception
     */
    @Override
    public void Startup() {

        BscJobTimer jobTimer = this.getTimerCfg();
        try {


            if (!"0".equals(jobTimer.getRunEndStatus())) {

                //更新运行开始标志
                jobTimer.setRunEndStatus("0");
                jobTimer.setRunEndTime(new Date());
                this.bscJobTimerService.updateBscJobTimer(jobTimer);
                this.initialized(jobTimer);

//                String orgId = StringUtils.isBlank(SecurityContext.getCurrentOrgId()) ? topOrgId : SecurityContext.getCurrentOrgId();
                String orgId = StringUtils.isBlank(topOrgId) ? null : topOrgId;

                // 根据定时器Id获取当前未办结规则实例
                List<ActStoTimeruleInst> actStoTimeruleInsts = actStoTimeruleInstMapper.getActStoTimeruleInstsByJobTimerId(jobTimer.getTimerId(), orgId);
                List<ActStoTimeruleInst> temp = new ArrayList();
                Map<String, ActStoTimerule> timeTemp = new HashMap();// 存放定时器缓存

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
                                List<AeaLogApplyStateHist> applyStateHists = null;
                                AeaLogApplyStateHist applyStateHist = new AeaLogApplyStateHist();
                                applyStateHist.setAppinstId(actStoAppinst.getAppinstId());
                                applyStateHist.setApplyinstId(actStoAppinst.getMasterRecordId());
                                applyStateHist.setNewState(ApplyState.OUT_SCOPE.getValue());
                                applyStateHists = aeaLogApplyStateHistService.findAeaLogApplyStateHist(applyStateHist);//获取不予受理记录
                                if (applyStateHists.size() > 0) {
                                    startTime = processInstance.getStartTime();
                                    currentTime = applyStateHists.get(0).getTriggerTime();
                                } else {

                                    applyStateHist.setNewState(ApplyState.COMPLETED.getValue());
                                    applyStateHists = aeaLogApplyStateHistService.findAeaLogApplyStateHist(applyStateHist);//获取已办结记录
                                    //主流程的办结时间以申请实例办结时间为准
                                    if (applyStateHists.size() > 0)
                                        currentTime = applyStateHists.get(0).getTriggerTime();

                                    applyStateHist.setNewState(ApplyState.ACCEPT_DEAL.getValue());
                                    applyStateHists = aeaLogApplyStateHistService.findAeaLogApplyStateHist(applyStateHist);//获取已受理记录
                                    //主流程的开始审批时间以申请实例已受理时间为准
                                    if (applyStateHists.size() > 0 && applyStateHists.get(0).getTriggerTime() != null)
                                        startTime = applyStateHists.get(0).getTriggerTime();
                                    else
                                        startTime = processInstance.getStartTime();
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
                        } else if ("2".equals(timeruleInst.getTimeruleInstType())) {    //节点时限计算
                            if (StringUtils.isBlank(timeruleInst.getTaskinstId())) continue;
                            // 获取taskinst实例
                            HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(timeruleInst.getTaskinstId()).singleResult();
                            if (taskInstance == null) continue;
                            // 根据时限计算规则进行计算
                            timeCalculateResult = timeLimitRule.calculate(taskInstance.getStartTime(), taskInstance.getEndTime(), timeruleInst.getOrgId());
                            // 获取节点的总挂起时间
                            timeCalculateResult = this.getProcessinstHangUpTime(null, timeCalculateResult, timeruleInst.getTimeruleUnit(), taskInstance.getProcessInstanceId(), taskInstance.getId());
                            timeruleInst.setIsConcluding(taskInstance.getEndTime() == null ? IS_CONCLUDING_NO : IS_CONCLUDING_YES);
                        } else {    //虚拟组时限计算
                            if (StringUtils.isBlank(timeruleInst.getProcInstId())) continue;
                            if (StringUtils.isBlank(timeruleInst.getTimegroupActId())) continue;
                            // 根据流程实例ID获取全部task节点
                            List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(timeruleInst.getProcInstId()).list();
                            if (taskInstances.size() < 1) continue;
                            for (HistoricTaskInstance taskInstance : taskInstances) {
                                if (taskInstance == null) continue;
                                if (timeruleInst.getTimegroupActId().equals(taskInstance.getTaskDefinitionId())) {
                                    // 根据时限计算规则进行计算
                                    timeCalculateResult = timeLimitRule.calculate(taskInstance.getStartTime(), taskInstance.getEndTime(), timeruleInst.getOrgId());
                                    // 获取节点的总挂起时间
                                    timeCalculateResult = this.getProcessinstHangUpTime(null, timeCalculateResult, timeruleInst.getTimeruleUnit(), taskInstance.getProcessInstanceId(), taskInstance.getId());
                                    timeruleInst.setIsConcluding(taskInstance.getEndTime() == null ? IS_CONCLUDING_NO : IS_CONCLUDING_YES);
                                    break;
                                }
                            }
                        }

                        double timeCount = timeruleInst.getTimeLimit() - timeCalculateResult;   // 逾期时间：流程时限和已用时的时间差
                        double remainingTime = (timeCount > 0.0d) ? timeCount : 0.0d;   //剩余用时：如果已逾期，剩余用时为0.0d
                        remainingTime = this.timeAccurateCalculate(remainingTime);// 计算所得的已用时和剩余用时保留小数点后一位（其他位数舍弃），所得时间精确到0.5个（WD、ND、WH、NH）
                        timeCalculateResult = this.timeAccurateCalculate(timeCalculateResult);
                        timeruleInst.setUseLimitTime(timeCalculateResult);
                        timeruleInst.setRemainingTime(remainingTime);
                        timeruleInst.setOverdueTime(timeCount > 0.0d ? 0.0d : Math.abs(timeCount));// 未逾期取0.0，已逾期取timeCount的绝对值
                        timeruleInst.setModifyTime(new Date());
                        String instState = null;
                        if (timeCount > 0.0d) {
                            if ("WD".equals(timerule.getTimeruleUnit()) || "ND".equals(timerule.getTimeruleUnit()))
                                instState = timeCount > 2 ? "1" : "2";  //小于或等于2天时，为预警状态
                            else
                                instState = timeCount > 48 ? "1" : "2";//小于或等于48小时，为预警状态
                        } else {
                            instState = "3";
                            if (timeruleInst.getOverdueDate() == null)
                                timeruleInst.setOverdueDate(new Date());// 保存第一次逾期日期
                        }
                        timeruleInst.setInstState(instState); // 实例状态：1 正常，2 预警，3 逾期
                        temp.add(timeruleInst);
                    }
                }

                if (temp.size() > 0) {
                    actStoTimeruleInstMapper.batchUpdateActStoTimeruleInst(temp);
                }

                //20191205新增，办件容缺办结时限实例计算模块
                calculateToleranceTime(jobTimer.getTimerId(), timeTemp);

                //更新运行结束标志
                jobTimer.setRunEndStatus("1");
                jobTimer.setRunEndTime(new Date());
                this.bscJobTimerService.updateBscJobTimer(jobTimer);
                this.initialized(jobTimer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("------定时器运行出错------:" + e.getMessage());
            try {
                jobTimer.setRumLock("1");
                jobTimer.setRunEndStatus("2");
                jobTimer.setRunEndTime(new Date());
                jobTimer.setRunException(e.getMessage());
                this.bscJobTimerService.updateBscJobTimer(jobTimer);
                this.initialized(jobTimer);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * 计算办件容缺时限实例的接口，可以在后面的try块中添加其他业务逻辑
     *
     * @param jobTimerId
     * @param timeTemp
     * @throws Exception
     */
    private void calculateToleranceTime(String jobTimerId, Map<String, ActStoTimerule> timeTemp) throws Exception {
        //查出所有的为完成的办件容缺补正时限实例
        List<AeaToleranceTimeInst> timeinsts = aeaToleranceTimeInstService.getUnCompletedToleranceTimeinstsByJobTimerId(topOrgId, jobTimerId);
        Date currentTime = new Date(); //当前时间
        List<AeaToleranceTimeInst> temp = new ArrayList();
        for (int i = 0, len = timeinsts.size(); i < len; i++) {
            //容缺时限实例
            AeaToleranceTimeInst aeaToleranceTimeInst = timeinsts.get(i);
            // 获取时限计算规则
            ActStoTimerule timerule = timeTemp.get(aeaToleranceTimeInst.getTimeruleId()) == null ? actStoTimeruleMapper.getActStoTimeruleById(aeaToleranceTimeInst.getTimeruleId()) : timeTemp.get(aeaToleranceTimeInst.getTimeruleId());

            // 判断规则是否启用、是否已删除
            if ("1".equals(timerule.getIsActive()) && "0".equals(timerule.getIsDeleted())) {
                // 实例时限计算规则类
                BpmBaseTimeLimitRule timeLimitRule = (BpmBaseTimeLimitRule) AplanmisTimeCalculateEngine.applicationContext.getBean(timerule.getTimeruleClassBeanId());
                if (timeLimitRule == null) continue;
                //已经用时
                double timeCalculateResult = timeLimitRule.calculate(aeaToleranceTimeInst.getCreateTime(), currentTime, aeaToleranceTimeInst.getOrgId());
                double timeCount = aeaToleranceTimeInst.getTimeLimit() - timeCalculateResult;   // 逾期时间：流程时限和已用时的时间差
                double remainingTime = (timeCount > 0.0d) ? timeCount : 0.0d;   //剩余用时：如果已逾期，剩余用时为0.0d
                remainingTime = this.timeAccurateCalculate(remainingTime);// 计算所得的已用时和剩余用时保留小数点后一位（其他位数舍弃），所得时间精确到0.5个（WD、ND、WH、NH）
                timeCalculateResult = this.timeAccurateCalculate(timeCalculateResult);
                aeaToleranceTimeInst.setUseLimitTime(timeCalculateResult);
                aeaToleranceTimeInst.setRemainingTime(remainingTime);
                aeaToleranceTimeInst.setOverdueTime(timeCount > 0.0d ? 0.0d : Math.abs(timeCount));// 未逾期取0.0，已逾期取timeCount的绝对值
                String instState = null;
                if (timeCount >= 0.0d) {
                    if ("WD".equals(timerule.getTimeruleUnit()) || "ND".equals(timerule.getTimeruleUnit()))
                        instState = timeCount > 2 ? "1" : "2";  //小于或等于2天时，为预警状态
                    else
                        instState = timeCount > 48 ? "1" : "2";//小于或等于48小时，为预警状态
                } else {
                    instState = "3";
                    if (aeaToleranceTimeInst.getOverdueDate() == null)
                        aeaToleranceTimeInst.setOverdueDate(new Date());// 保存第一次逾期日期
                }
                aeaToleranceTimeInst.setInstState(instState); // 实例状态：1 正常，2 预警，3 逾期
                aeaToleranceTimeInst.setModifyTime(currentTime);
                temp.add(aeaToleranceTimeInst);
            }
        }
        if (temp.size() > 0) {
            aeaToleranceTimeInstService.batchUpdateAeaToleranceTimeInst(temp);
        }

        try {
            //可以在这里加入对应的业务逻辑，做短信发送或其他操作，对于预警的容缺补正实例，做短信通知
            for (int i = 0, len = temp.size(); i < len; i++) {
                AeaToleranceTimeInst aeaToleranceTimeInst = temp.get(i);
                if ("2".equals(aeaToleranceTimeInst.getInstState())) {
                    aeaToleranceTimeInstService.createToleranceSmsRemindInfo(aeaToleranceTimeInst);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (AplanmisTimeCalculateEngine.applicationContext != null) {
            System.out.println("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:"
                    + AplanmisTimeCalculateEngine.applicationContext);
        }
        AplanmisTimeCalculateEngine.applicationContext = applicationContext;
    }
}
