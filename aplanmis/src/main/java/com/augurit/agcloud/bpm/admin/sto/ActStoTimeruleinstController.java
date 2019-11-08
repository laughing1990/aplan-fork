package com.augurit.agcloud.bpm.admin.sto;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.common.service.ActStoTimeruleInstService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.bpm.common.timeCalculate.RestTimeruleinstCalService;
import com.augurit.aplanmis.common.service.instance.RestTimeruleinstService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/rest/timeruleinst")
public class ActStoTimeruleinstController {

    @Autowired
    private ActStoTimeruleInstService actStoTimeruleInstService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RestTimeruleinstService restTimeruleinstService;

    @Autowired
    private ActStoAppinstService actStoAppinstService;

    @Autowired
    private ActStoAppinstSubflowService actStoAppinstSubflowService;

    @Autowired
    private RestTimeruleinstCalService restTimeruleinstCalService;

    @RequestMapping("/create")
    public ResultForm createTimeruleinst() {

        try {
            List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery().list();
            for (HistoricProcessInstance processInstance : processInstances) {
                ActStoTimeruleInst actStoTimeruleInst = new ActStoTimeruleInst();
                actStoTimeruleInst.setTimeruleInstType("1");
                actStoTimeruleInst.setOrgId(SecurityContext.getCurrentOrgId());
                actStoTimeruleInst.setProcInstId(processInstance.getId());
                List<ActStoTimeruleInst> timeruleInsts = actStoTimeruleInstService.listActStoTimeruleInst(actStoTimeruleInst);
                if (timeruleInsts.size() > 0) continue;
                ActStoAppinst appinst = actStoAppinstService.getActStoAppinstByProcInstId(processInstance.getId());
                if (appinst == null) {
                    ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowService.getActStoAppinstSubflowBySubflowProcinstId(processInstance.getId());
                    appinst = actStoAppinstService.getActStoAppinstById(actStoAppinstSubflow.getAppinstId());
                }
                restTimeruleinstService.createTimeruleinstByProcinst(appinst.getAppId(), processInstance.getId(), processInstance.getProcessDefinitionKey());
            }
            return new ResultForm(true, "批量新增时限规则实例成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "批量新增时限规则实例失败！");
        }
    }

    @RequestMapping("/calculate")
    public ResultForm calculate(String id) {
        try {
            restTimeruleinstCalService.Startup(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统出错！");
        }
        return new ResultForm(true, "执行成功！");
    }
}
