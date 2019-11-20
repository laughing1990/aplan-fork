package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleInstMapper;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.dto.MatCorrectDto;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectDueIninstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectRealIninstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.mall.userCenter.vo.*;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private AeaHiItemCorrectService aeaHiItemCorrectService;
    @Autowired
    private AeaHiItemCorrectRealIninstService aeaHiItemCorrectRealIninstService;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaHiItemCorrectDueIninstService aeaHiItemCorrectDueIninstService;
    @Autowired
    private AeaHiItemSpecialMapper itemSpecialMapper;
    @Autowired
    private IBscAttService bscAttService;
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
//            //事项审批过程
//            String procinstId = aeaHiIteminst.getProcinstId();
//            List<BpmHistoryCommentFormVo> bpmHistoryCommentFormVos = restBpmService.listHistoryComment(procinstId, false, null);
//            vo.setCommentList(bpmHistoryCommentFormVos);
            //事项补正信息
            List<AeaHiItemCorrect> supplyList = this.getMatCorrectInfoByApplyinstOrIteminst(null, iteminstId);
            vo.setSupplyList(supplyList);
            //事项特殊程序信息
            Map<String, Object> currentItemSpecialList = this.getCurrentItemSpecialList(aeaHiIteminst);
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

    /**
     * 根据申请实例id获取事项实例id获取材料补正列表
     *
     * @param applyinstId
     * @param iteminstId
     * @return
     * @throws Exception
     */
    public List<AeaHiItemCorrect> getMatCorrectInfoByApplyinstOrIteminst(String applyinstId, String iteminstId) throws Exception {
        if (com.augurit.agcloud.framework.util.StringUtils.isBlank(applyinstId) && com.augurit.agcloud.framework.util.StringUtils.isBlank(iteminstId)) {
            throw new InvalidParameterException("参数applyinstId和iteminstId不能同时为空！");
        }
        List<AeaHiItemCorrect> result = new ArrayList<>();
        AeaHiItemCorrect query = new AeaHiItemCorrect();
        query.setRootOrgId(SecurityContext.getCurrentOrgId());
        if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(iteminstId)) {
            query.setIteminstId(iteminstId);
        } else {
            query.setApplyinstId(applyinstId);
        }
        List<AeaHiItemCorrect> aeaHiItemCorrects = aeaHiItemCorrectService.listAeaHiItemCorrect(query);
        for (AeaHiItemCorrect correct : aeaHiItemCorrects) {
            result.add(getMatCorrectInfo(correct.getCorrectId()));
        }
        return result;
    }

    /**
     * 材料补正信息
     *
     * @param correctId
     * @return
     * @throws Exception
     */
    public AeaHiItemCorrect getMatCorrectInfo(String correctId) throws Exception {

        if (com.augurit.agcloud.framework.util.StringUtils.isBlank(correctId)) throw new Exception("材料补正实例ID为空！");

        AeaHiItemCorrect aeaHiItemCorrect = aeaHiItemCorrectService.getAeaHiItemCorrectById(correctId);
        if (aeaHiItemCorrect == null) throw new Exception("找不到材料补正实例信息！");

        this.getOwner(aeaHiItemCorrect); //获取业主名称

        //回填窗口补件负责人信息
        if (com.augurit.agcloud.framework.util.StringUtils.isBlank(aeaHiItemCorrect.getWindowUserId())) {
            aeaHiItemCorrect.setWindowUserId(SecurityContext.getCurrentUserId());
            aeaHiItemCorrect.setWindowUserName(SecurityContext.getCurrentUser().getUserName());
        }

        //回填窗口补件经办人信息
        if (com.augurit.agcloud.framework.util.StringUtils.isBlank(aeaHiItemCorrect.getOpsUserId())) {
            aeaHiItemCorrect.setOpsUserId(SecurityContext.getCurrentUserId());
            aeaHiItemCorrect.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
            aeaHiItemCorrect.setOpsTime(new Date());
        }
        //需补正的材料清单
        List<AeaHiItemCorrectDueIninst> aeaHiItemCorrectDueIninsts = aeaHiItemCorrectDueIninstService.getCorrectDueIninstByCorrectId(correctId);
        if (aeaHiItemCorrectDueIninsts.size() < 1) throw new Exception("没有需补正的材料清单");

        Map<String, MatCorrectDto> map = new HashMap();
        for (AeaHiItemCorrectDueIninst correctDueIninst : aeaHiItemCorrectDueIninsts) {
            MatCorrectDto matCorrectDto = map.get(correctDueIninst.getMatId()) != null ? map.get(correctDueIninst.getMatId()) : new MatCorrectDto();

            if (com.augurit.agcloud.framework.util.StringUtils.isBlank(matCorrectDto.getMatinstName())) {
                matCorrectDto.setMatinstName(correctDueIninst.getMatinstName());
                matCorrectDto.setReviewKeyPoints(correctDueIninst.getReviewKeyPoints());
            }

            if (correctDueIninst.getCopyCount() != null && correctDueIninst.getCopyCount() > 0) {
                matCorrectDto.setCopyMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setCopyInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setCopyCount(correctDueIninst.getCopyCount());
                matCorrectDto.setCopyDueIninstOpinion(correctDueIninst.getCorrectOpinion());
                matCorrectDto.setCopyDueIninstId(correctDueIninst.getDueIninstId());
            }

            if (correctDueIninst.getPaperCount() != null && correctDueIninst.getPaperCount() > 0) {
                matCorrectDto.setPaperMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setPaperInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setPaperCount(correctDueIninst.getPaperCount());
                matCorrectDto.setPaperDueIninstOpinion(correctDueIninst.getCorrectOpinion());
                matCorrectDto.setPaperDueIninstId(correctDueIninst.getDueIninstId());
            }

            if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(correctDueIninst.getIsNeedAtt()) && "1".equals(correctDueIninst.getIsNeedAtt())) {
                matCorrectDto.setIsNeedAtt("1");
                matCorrectDto.setAttInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setAttMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setAttDueIninstOpinion(correctDueIninst.getCorrectOpinion());
                matCorrectDto.setAttDueIninstId(correctDueIninst.getDueIninstId());
            }

            map.put(correctDueIninst.getMatId(), matCorrectDto);
        }
        //已补正的材料清单
        List<AeaHiItemCorrectRealIninst> aeaHiItemCorrectRealIninsts = aeaHiItemCorrectRealIninstService.getCorrectRealIninstByCorrectId(correctId);
        for (AeaHiItemCorrectRealIninst correctRealIninst : aeaHiItemCorrectRealIninsts) {

            if (map.get(correctRealIninst.getMatId()) == null) continue;
            MatCorrectDto matCorrectDto = map.get(correctRealIninst.getMatId());

            if (correctRealIninst.getAttCount() != null && correctRealIninst.getAttCount() >= 0l) {
                matCorrectDto.setAttCount(correctRealIninst.getAttCount());
                matCorrectDto.setAttRealIninstId(correctRealIninst.getRealIninstId());
            }

            if (correctRealIninst.getRealPaperCount() != null && correctRealIninst.getRealPaperCount() >= 0l) {
                matCorrectDto.setRealPaperCount(correctRealIninst.getRealPaperCount());
                matCorrectDto.setPaperRealIninstId(correctRealIninst.getRealIninstId());
            }

            if (correctRealIninst.getRealCopyCount() != null && correctRealIninst.getRealCopyCount() >= 0l) {
                matCorrectDto.setRealCopyCount(correctRealIninst.getRealCopyCount());
                matCorrectDto.setCopyRealIninstId(correctRealIninst.getRealIninstId());
            }

            //查询材料实例对应的附件detailId
            List<BscAttFileAndDir> attFiles = getAttFiles(correctRealIninst.getRealIninstId());
            matCorrectDto.setAttFiles(attFiles);
            map.put(correctRealIninst.getMatId(), matCorrectDto);
        }

        List<MatCorrectDto> matCorrectDtos = new ArrayList();
        matCorrectDtos.addAll(map.values());

        for (MatCorrectDto matCorrectDto : matCorrectDtos) {
            if ("1".equals(matCorrectDto.getIsNeedAtt()) && com.augurit.agcloud.framework.util.StringUtils.isBlank(matCorrectDto.getAttRealIninstId())) {
                AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
                aeaHiItemCorrectRealIninst.setCorrectId(correctId);
                aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getAttInoutinstId());
                aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getAttDueIninstId());
                aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
                List<AeaHiItemCorrectRealIninst> realIninsts = aeaHiItemCorrectRealIninstService.listAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                if (realIninsts.size() > 0) {
                    matCorrectDto.setAttRealIninstId(realIninsts.get(0).getRealIninstId());
                    matCorrectDto.setAttCount(realIninsts.get(0).getAttCount());
                } else {
                    String realInstId = UUID.randomUUID().toString();
                    aeaHiItemCorrectRealIninst.setRealIninstId(realInstId);
                    aeaHiItemCorrectRealIninst.setIsPass("0");
                    aeaHiItemCorrectRealIninst.setAttCount(0l);
                    aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);

                    matCorrectDto.setAttRealIninstId(realInstId);
                    matCorrectDto.setAttCount(0l);
                }
            }
        }
        aeaHiItemCorrect.setMatCorrectDtos(matCorrectDtos);
        return aeaHiItemCorrect;
    }

    public List<BscAttFileAndDir> getAttFiles(String attRealIninstId) throws Exception {
        if (com.augurit.agcloud.framework.util.StringUtils.isBlank(attRealIninstId)) throw new Exception("补正材料ID为空！");
        return bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_ITEM_CORRECT_REAL_ININST", "REAL_ININST_ID", new String[]{attRealIninstId});
    }

    //获取项目业主
    private void getOwner(AeaHiItemCorrect aeaHiItemCorrect) throws Exception {
        if (com.augurit.agcloud.framework.util.StringUtils.isBlank(aeaHiItemCorrect.getLinkmanInfoId())) throw new Exception("找不到申报联系人ID！");
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(aeaHiItemCorrect.getLinkmanInfoId());
        if (aeaLinkmanInfo != null) {
            aeaHiItemCorrect.setLinkman(aeaLinkmanInfo.getLinkmanName());
            aeaHiItemCorrect.setLinkmanPhone(aeaLinkmanInfo.getLinkmanMobilePhone());
        }
        if ("1".equals(aeaHiItemCorrect.getApplySubject())) {
            List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(aeaHiItemCorrect.getApplyinstId(), aeaHiItemCorrect.getProjInfoId());
            if (aeaUnitInfos.size() < 1) {
                aeaHiItemCorrect.setOwner("");
            } else {
                aeaHiItemCorrect.setOwner(aeaUnitInfos.get(0).getApplicant());
            }
        } else {
            if (aeaLinkmanInfo == null) {
                aeaHiItemCorrect.setOwner("");
            } else {
                aeaHiItemCorrect.setOwner(aeaLinkmanInfo.getLinkmanName());
            }

        }
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
            if(com.augurit.agcloud.framework.util.StringUtils.isBlank(temp.getOpsUserName())){
                temp.setOpsUserName(userName);
            }
            result.add(temp);
        }
        specialInfo.put("specialList", result);
        return specialInfo;
    }
}
