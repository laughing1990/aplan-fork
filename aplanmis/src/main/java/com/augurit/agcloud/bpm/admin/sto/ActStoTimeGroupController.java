package com.augurit.agcloud.bpm.admin.sto;

import com.augurit.agcloud.bpm.common.domain.ActStoTimegroup;
import com.augurit.agcloud.bpm.common.domain.ActStoTimegroupAct;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimegroupActMapper;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimegroupMapper;
import com.augurit.agcloud.bpm.common.service.ActStoTimegroupService;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.fasterxml.uuid.impl.UUIDUtil;
import org.apache.catalina.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/bpm/admin/sto")
public class ActStoTimeGroupController {
    private static final Logger log = LoggerFactory.getLogger(ActStoTimeGroupController.class);
    @Autowired
    private ActStoTimegroupService actStoTimegroupService;
    @Autowired
    private ActStoTimegroupMapper actStoTimegroupMapper;
    @Autowired
    private ActStoTimegroupActMapper actStoTimegroupActMapper;

    @RequestMapping("/save")
    @ResponseBody
    public ResultForm insertModel(ActStoTimegroup timegroup,String accId){
        ResultForm form = new ResultForm(false);
        String[] accIds = null;
        if(accId != null ){
            accIds = accId.split("`~");
        }
        try {

            if(StringUtils.isEmpty(timegroup.getTimegroupId())){
                timegroup.setTimegroupId(UuidUtil.generateUuid());
                timegroup.setCreater(SecurityContext.getCurrentUserName());
                timegroup.setTimegroupSeq(timegroup.getTimegroupId());
                timegroup.setCreateTime(new Date());
                actStoTimegroupService.saveActStoTimegroup(timegroup);
                insertActStoTimegroupAct(accIds, timegroup);
            }else{
                ActStoTimegroupAct act = new ActStoTimegroupAct();
                act.setTimegroupId(timegroup.getTimegroupId());
                List<ActStoTimegroupAct> timegroupActs = actStoTimegroupActMapper.listActStoTimegroupAct(act);
                if(timegroupActs != null){
                    for(ActStoTimegroupAct a: timegroupActs){
                        actStoTimegroupActMapper.deleteActStoTimegroupAct(a.getTimegroupActId());
                    }
                }
                timegroup.setModifier(SecurityContext.getCurrentUserName());
                timegroup.setModifyTime(new Date());
                actStoTimegroupService.updateActStoTimegroup(timegroup);
                insertActStoTimegroupAct(accIds, timegroup);
            }
            form.setSuccess(true);
        } catch (Exception e) {
            log.error("",e);
            form.setMessage(e.getMessage());
        }
        return form;
    }

    private void insertActStoTimegroupAct(String[] accIds, ActStoTimegroup timegroup) throws Exception {
        if(accIds != null){
            for (String accid: accIds){
                ActStoTimegroupAct act = new ActStoTimegroupAct();
                act.setTimegroupActId(UuidUtil.generateUuid());
                act.setActId(accid);
                act.setAppFlowdefId(timegroup.getAppFlowdefId());
                act.setTimegroupId(timegroup.getTimegroupId());
                act.setTimeLimit(timegroup.getTimeLimit());
                act.setTimeruleId(timegroup.getTimeruleId());
                act.setCreater(SecurityContext.getCurrentUserName());
                act.setCreateTime(new Date());
                actStoTimegroupActMapper.insertActStoTimegroupAct(act);
            }
        }
    }

    @RequestMapping("/get")
    @ResponseBody
    public ActStoTimegroup getModel(String timegroupId) throws Exception {

        ActStoTimegroup timegroup = actStoTimegroupService.getActStoTimegroupById(timegroupId);
        if(timegroup != null){
            ActStoTimegroupAct act = new ActStoTimegroupAct();
            act.setTimegroupId(timegroupId);
            List<ActStoTimegroupAct> timegroupActs = actStoTimegroupActMapper.listActStoTimegroupAct(act);
            if(timegroupActs != null){
                StringBuffer sb = new StringBuffer();
                for(ActStoTimegroupAct a: timegroupActs){
                    sb.append(a.getActId()).append("`~");
                }
                timegroup.setModifier(sb.toString());
            }
        }
        return timegroup;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultForm deleteModel(String[] timegroupId) throws Exception {
        for(String timeGroupId: timegroupId){
            actStoTimegroupService.deleteActStoTimegroupById(timeGroupId);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/list")
    @ResponseBody
    public ResultForm listModel(String appFlowdefId) throws Exception {
        ActStoTimegroup timegroup = new ActStoTimegroup();
        timegroup.setAppFlowdefId(appFlowdefId);
//        List<ActStoTimegroup> list =  actStoTimegroupService.listActStoTimegroup(timegroup);
        List<Map> mapList = actStoTimegroupMapper.getActStoTimegroupByAppFlowdefId(appFlowdefId);
//        List<ActStoTimegroup> list =  actStoTimegroupService.listActStoTimegroupByTimegroupId(appFlowdefId);

        return new ContentResultForm(true, mapList);
    }
}
