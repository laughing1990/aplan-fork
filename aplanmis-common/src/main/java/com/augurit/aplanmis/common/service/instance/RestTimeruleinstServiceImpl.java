package com.augurit.aplanmis.common.service.instance;

import com.augurit.agcloud.bpm.common.domain.ActStoTimerule;
import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.domain.ActTplAppFlowdef;
import com.augurit.agcloud.bpm.common.service.ActStoTimeruleInstService;
import com.augurit.agcloud.bpm.common.service.ActStoTimeruleService;
import com.augurit.agcloud.bpm.common.service.ActTplAppFlowdefService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RestTimeruleinstServiceImpl implements RestTimeruleinstService {


    @Autowired
    private ActStoTimeruleInstService actStoTimeruleInstService;

    @Autowired
    private ActStoTimeruleService actStoTimeruleService;

    @Autowired
    private ActTplAppFlowdefService actTplAppFlowdefService;

    @Override
    public void createTimeruleinstByProcinst(String appId, String procinstId, String procDefKey) throws Exception {

        if (StringUtils.isBlank(procinstId) || StringUtils.isBlank(procDefKey) || StringUtils.isBlank(appId)) return;
        ActTplAppFlowdef actTplAppFlowdef = new ActTplAppFlowdef();
        actTplAppFlowdef.setAppId(appId);
        actTplAppFlowdef.setProcdefKey(procDefKey);
        actTplAppFlowdef.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<ActTplAppFlowdef> appFlowdefList = actTplAppFlowdefService.listActTplAppFlowdef(actTplAppFlowdef);
        if (appFlowdefList.size() < 1) return;

        ActTplAppFlowdef appFlowdef = appFlowdefList.get(0);
        if (StringUtils.isBlank(appFlowdef.getTimeruleId())) return;
        ActStoTimerule actStoTimerule = actStoTimeruleService.getActStoTimeruleById(appFlowdef.getTimeruleId());

        if (actStoTimerule == null) return;
        ActStoTimeruleInst actStoTimeruleInst = new ActStoTimeruleInst();
        actStoTimeruleInst.setProcInstId(procinstId);
        actStoTimeruleInst.setAppFlowdefId(appFlowdef.getAppFlowdefId());
        actStoTimeruleInst.setTimeruleId(actStoTimerule.getTimeruleId());
        actStoTimeruleInst.setOrgId(SecurityContext.getCurrentOrgId());
        List<ActStoTimeruleInst> timeruleInsts = actStoTimeruleInstService.listActStoTimeruleInst(actStoTimeruleInst);
        if (timeruleInsts.size() > 0) return;

        //如果时限为NULL,则默认为1天
        actStoTimeruleInst.setTimeLimit(Long.valueOf(appFlowdef.getTimeLimit() == null ? 1 : appFlowdef.getTimeLimit()));
        actStoTimeruleInst.setTimeruleInstType("1");
        actStoTimeruleInst.setCreateTime(new Date());
        actStoTimeruleInst.setIsConcluding("0");
        actStoTimeruleInst.setTimeruleUnit(actStoTimerule.getTimeruleUnit());
        actStoTimeruleInst.setCreater(SecurityContext.getCurrentUserName());
        actStoTimeruleInst.setTimeruleInstId(UUID.randomUUID().toString());
        actStoTimeruleInst.setUseLimitTime(0d);
        actStoTimeruleInst.setOverdueTime(0d);
        actStoTimeruleInst.setInstState("1");
        actStoTimeruleInst.setRemainingTime(Double.valueOf(appFlowdef.getTimeLimit() == null ? 1 : appFlowdef.getTimeLimit()));
        actStoTimeruleInstService.saveActStoTimeruleInst(actStoTimeruleInst);
    }
}
