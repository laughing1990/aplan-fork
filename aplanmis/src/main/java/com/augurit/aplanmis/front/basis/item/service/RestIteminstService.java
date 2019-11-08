package com.augurit.aplanmis.front.basis.item.service;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleInstMapper;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaLogItemStateHistMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.front.approve.service.RestBpmService;
import com.augurit.aplanmis.front.approve.vo.BpmHistoryCommentFormVo;
import com.augurit.aplanmis.front.approve.vo.official.OfficialDocumentInfoVo;
import com.augurit.aplanmis.front.basis.item.vo.AeaHiIteminstDetailVo;
import com.augurit.aplanmis.front.basis.item.vo.AeaHiIteminstVo;
import com.augurit.aplanmis.front.correct.service.RestMatCorrectService;
import com.augurit.aplanmis.front.specialProcedures.service.RestSpecialRrocedureService;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:chendx
 * @date: 2019-10-30
 * @time: 16:32
 */
@Service
@Transactional
public class RestIteminstService {

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private RestBpmService restBpmService;
    @Autowired
    private RestSpecialRrocedureService restSpecialRrocedureService;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private AeaLogItemStateHistMapper aeaLogItemStateHistMapper;
    @Autowired
    private ActStoTimeruleInstMapper actStoTimeruleInstMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RestMatCorrectService restMatCorrectService;

    /**
     * 通过事项实例id,查询事项申报审批过程的所有关联数据，包括基本信息、材料、补正、特殊程序、批文批复等
     *
     * @param iteminstId
     * @return
     * @throws Exception
     */
    public AeaHiIteminstDetailVo getIteminstDetailInfo(String iteminstId) throws Exception {
        AeaHiIteminstDetailVo vo = new AeaHiIteminstDetailVo();
        //事项实例信息
        AeaHiIteminst aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
        AeaHiIteminstVo aeaHiIteminstVo = new AeaHiIteminstVo();
        if (aeaHiIteminst != null) {
            BeanUtils.copyProperties(aeaHiIteminst, aeaHiIteminstVo);
            //查询事项实例当前状态的办理意见，如果在历史记录中没有，则通过taskId关联查询办理意见
            AeaLogItemStateHist query = new AeaLogItemStateHist();
            query.setIteminstId(aeaHiIteminst.getIteminstId());
            query.setNewState(aeaHiIteminst.getIteminstState());
            List<AeaLogItemStateHist> aeaLogItemStateHists = aeaLogItemStateHistMapper.listAeaLogItemStateHist(query);
            if (aeaLogItemStateHists.size() > 0) {
                AeaLogItemStateHist aeaLogItemStateHist = aeaLogItemStateHists.get(0);
                if (StringUtils.isNotBlank(aeaLogItemStateHist.getOpsUserOpinion())) {
                    aeaHiIteminstVo.setReason(aeaLogItemStateHist.getOpsUserOpinion());
                } else {
                    List<Comment> taskComments = taskService.getTaskComments(aeaLogItemStateHist.getTaskinstId());
                    if (taskComments != null && taskComments.size() > 0) {
                        aeaHiIteminstVo.setReason(taskComments.get(0).getFullMessage());
                    }
                }
            }
            //查询事项实例实际审批时长
            if (aeaHiIteminst.getEndTime() != null) {
                ActStoTimeruleInst inst = actStoTimeruleInstMapper.getProcessinstTimeruleInstByIteminstId(iteminstId, SecurityContext.getCurrentOrgId());
                if (inst != null) {
                    aeaHiIteminstVo.setRealTime(inst.getUseLimitTime());
                }
            }
            vo.setAeaHiIteminst(aeaHiIteminstVo);
            //事项审批过程
            String procinstId = aeaHiIteminst.getProcinstId();
            List<BpmHistoryCommentFormVo> bpmHistoryCommentFormVos = restBpmService.listHistoryComment(procinstId, false, null);
            vo.setCommentList(bpmHistoryCommentFormVos);
            //事项补正信息
            List<AeaHiItemCorrect> supplyList = restMatCorrectService.getMatCorrectInfoByApplyinstOrIteminst(null, iteminstId);
            vo.setSupplyList(supplyList);
            //事项特殊程序信息
            Map<String, Object> currentItemSpecialList = restSpecialRrocedureService.getCurrentItemSpecialList(aeaHiIteminst);
            List<AeaHiItemSpecial> specialList = (List<AeaHiItemSpecial>) currentItemSpecialList.get("specialList");
            vo.setSpecialList(specialList);
            //事项批文批复信息
            List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.listOfficialDocsByIteminstId(aeaHiIteminst.getIteminstId()); //查询批文批复
            List<OfficialDocumentInfoVo> collect = new ArrayList();
            for (AeaHiItemMatinst matinst : aeaHiItemMatinsts) {
                OfficialDocumentInfoVo officialDocumentInfo = OfficialDocumentInfoVo.from(matinst);
                //好像上传附件那里，没保存到上传者信息createrName，这里附件上传应该和批文上传的创建者是一个人
                List<BscAttFileAndDir> bscAtts = fileUtilsService.getMatAttDetailByMatinstId(matinst.getMatinstId());
                for (int i = 0, len = bscAtts.size(); i < len; i++) {
                    bscAtts.get(i).setCreaterName(officialDocumentInfo.getCreator());
                }
                officialDocumentInfo.setAttFiles(bscAtts);
                collect.add(officialDocumentInfo);
            }
            vo.setOfficialDocumentList(collect);
        }
        return vo;
    }
}
