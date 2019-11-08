package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.DateUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.constants.NeedStateStatus;
import com.augurit.aplanmis.common.domain.AeaParIn;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.common.domain.AeaParStateItem;
import com.augurit.aplanmis.common.mapper.AeaParInMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaParStateItemMapper;
import com.augurit.aplanmis.common.mapper.AeaParStateMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateAdminService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmind.core.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author: Michael
 * @date: 2018/11/13 18:49
 * @version: v0.1
 **/

@Service
@Transactional
public class AeaParStateXmindAdminServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(AeaParStateXmindAdminServiceImpl.class);

    @Autowired
    private AeaItemStateXmindAdminServiceImpl aeaItemStateXmindService;

    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    @Autowired
    private AeaParStateAdminService aeaParStateAdminService;

    @Autowired
    private AeaParStateMapper aeaParStateMapper;

    @Autowired
    private AeaParInMapper aeaParInMapper;

    @Autowired
    private AeaParStateItemMapper aeaParStateItemMapper;

    public void loadXmindFileToParSate(InputStream inputStream, String stageId){

        IWorkbookBuilder builder = Core.getWorkbookBuilder();
        IWorkbook book = null;
        try {
            book = builder.loadFromStream(inputStream);
            ISheet sheet = book.getPrimarySheet();
            ITopic rootTopic = sheet.getRootTopic();
            if(StringUtils.isBlank(stageId)) {
                return;
            }
            String rootOrgId = SecurityContext.getCurrentOrgId();
            AeaParStage stage = aeaParStageMapper.getAeaParStageById(stageId);
            deleteAllParStateByStageId(stageId, rootOrgId);
            if(CollectionUtils.isNotEmpty(rootTopic.getAllChildren())){
                for (ITopic topic : rootTopic.getAllChildren()){

                    //根情形直接保存
                    String stateName = topic.getTitleText();
                    AeaParState rootState = new AeaParState();
                    rootState.setStateSeq(".");
                    AeaParState parState = initialAeaParState(stage, stateName, rootState, rootOrgId);
                    parState.setIsQuestion("1");
                    parState.setMustAnswer("0");
                    parState.setAnswerType("s");
                    if(CollectionUtils.isNotEmpty(topic.getAllChildren()) && ArrayUtils.contains(new String[]{"是", "否"}, topic.getAllChildren().get(0).getTitleText())){
                        parState.setMustAnswer("1");
                        parState.setAnswerType("y");
                    }

                    if(CollectionUtils.isEmpty(topic.getAllChildren())){
                        parState.setIsQuestion("1");
                        parState.setMustAnswer("0");
                        parState.setAnswerType("s");
                    }

                    aeaParStateAdminService.saveAeaParState(parState);

                    //遍历子情形
                    if(CollectionUtils.isNotEmpty(rootTopic.getAllChildren())){
                        for (ITopic subTopic : topic.getAllChildren()){
                            saveParState(subTopic, stage, parState, rootOrgId);
                        }
                    }
                }
            }
        }catch (Exception e){
            log.warn("Exception: " + e.getMessage());
        }
    }

    /**
     * 删除阶段下已存在的情形
     *
     * @param stageId
     * @throws Exception
     */
    private void deleteAllParStateByStageId(String stageId, String rootOrgId) throws Exception {

        AeaParState state = new AeaParState();
        state.setStageId(stageId);
        state.setRootOrgId(rootOrgId);
        List<String> parStateIds = new ArrayList<String>();
        List<AeaParState> listParState = aeaParStateAdminService.listAeaParState(state);
        if(CollectionUtils.isEmpty(listParState)) {
            return;
        }
        log.info("阶段下共有 {} 条已存在情形", CollectionUtils.size(listParState));
        listParState.stream().forEach(parState -> parStateIds.add(parState.getParStateId()));
        aeaParStateAdminService.batchDeleteAeaParStateByIds(parStateIds.toArray(new String[parStateIds.size()]));

    }

    private void saveParState(ITopic topic, AeaParStage stage, AeaParState parState, String rootOrgId) throws Exception{

        AeaParState subParState = parState;
        String stateName = topic.getTitleText();
        subParState = initialAeaParState(stage, stateName, parState, rootOrgId);
        if("1".equals(subParState.getIsQuestion()) || (CollectionUtils.isNotEmpty(topic.getAllChildren())
                && ArrayUtils.contains(new String[]{"是", "否"}, topic.getAllChildren().get(0).getTitleText()))){
            subParState.setIsQuestion("1");
            subParState.setMustAnswer("0");
            subParState.setAnswerType("m");
            if(CollectionUtils.isNotEmpty(topic.getAllChildren())
                    && ArrayUtils.contains(new String[]{"是", "否"}, topic.getAllChildren().get(0).getTitleText())){
                subParState.setMustAnswer("1");
                subParState.setAnswerType("y");
            }

            if(CollectionUtils.isEmpty(topic.getAllChildren())){
                subParState.setIsQuestion("0");
                subParState.setMustAnswer(null);
                subParState.setAnswerType(null);
            }
        }

        if(ArrayUtils.contains(new String[]{"是", "否"}, stateName)){
            subParState.setIsQuestion("0");
            subParState.setMustAnswer(null);
            subParState.setAnswerType(null);
        }
        aeaParStateAdminService.saveAeaParState(subParState);
        log.info("当前子情形为：{}", stateName);

        if(CollectionUtils.isNotEmpty(topic.getAllChildren())) {
            for(ITopic subTopic : topic.getAllChildren()){
                saveParState(subTopic, stage, subParState, rootOrgId);
            }
        }
    }

    private AeaParState initialAeaParState(AeaParStage stage, String stateName, AeaParState parState, String rootOrgId) throws Exception{

        AeaParState aeaParSate = new AeaParState();
        Long maxSortNo = aeaParStateAdminService.getMaxSortNo(rootOrgId);
        aeaParSate.setSortNo(++maxSortNo);
        aeaParSate.setStateName(stateName);
        aeaParSate.setParStateId(UUID.randomUUID().toString());
        aeaParSate.setStageId(stage.getStageId());
        aeaParSate.setCreater(stage.getCreater());
        aeaParSate.setCreateTime(new Date());
        aeaParSate.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaParSate.setUseEl("0");
        aeaParSate.setIsQuestion("1");
        if("1".equals(parState.getIsQuestion())){
            aeaParSate.setIsQuestion("0");
        }
        aeaParSate.setParentStateId(parState.getParStateId());
        aeaParSate.setStateSeq(parState.getStateSeq() + aeaParSate.getParStateId() + ".");
        return aeaParSate;
    }

    public boolean downloadXmindFileByParStage(AeaParStage stage, HttpServletRequest request, HttpServletResponse response) throws Exception{

        String fileName = stage.getStageName() + "_" + DateUtils.parseToFormatDateString(new Date(), DateUtils.LOG_DATE_TIME) + ".xmind";
        File xmindFile = initialXmindFileByStage(stage);
        if (aeaItemStateXmindService.downloadXmindFile(request, response, fileName, xmindFile)) {
            return false;
        }
        return true;
    }

    private File initialXmindFileByStage(AeaParStage aeaParStage) throws Exception {

        String xmindFilePath = "./" + aeaParStage.getStageName() + DateUtils.parseToFormatDateString(new Date(), DateUtils.LOG_DATE_TIME) + ".xmind";
        IWorkbookBuilder builder = Core.getWorkbookBuilder();
        IWorkbook workbook = builder.createWorkbook(xmindFilePath);
        ISheet sheet = workbook.getPrimarySheet();

        //中心主题
        ITopic rootTopic = sheet.getRootTopic();
        rootTopic.setTitleText(aeaParStage.getStageName());

        //设置子节点右展开
        rootTopic.setStructureClass("org.xmind.ui.logic.right");

        String rootOrgId = SecurityContext.getCurrentOrgId();

        // 处理事项通用材料、证照、表单
        fillStageCommonMatCertFormTopic(rootTopic, workbook, aeaParStage.getStageId(), rootOrgId);

        // 获取根节点
        AeaParState state = new AeaParState();
        state.setRootOrgId(rootOrgId);
        state.setStageId(aeaParStage.getStageId());
        state.setIsDeleted(Status.OFF);
        fillParStateItemTopic(state, rootTopic, workbook);

        // 保存文件数据
        workbook.save(xmindFilePath);
        return new File(xmindFilePath);
    }

    /**
     * 处理公共材料、证照、表单
     *
     * @param rootTopic
     * @param workbook
     * @param stageId
     */
    private void fillStageCommonMatCertFormTopic(ITopic rootTopic, IWorkbook workbook, String stageId, String rootOrgId) {

        AeaParIn in = new AeaParIn();
        in.setRootOrgId(rootOrgId);
        in.setStageId(stageId);
        in.setIsStateIn(NeedStateStatus.NOT_NEED_STATE.getValue());
        in.setIsDeleted(Status.OFF);
        handleMatAndCertFormTopic(rootTopic, workbook, in);
    }

    private void fillParStateItemTopic(AeaParState rootParState, ITopic topic, IWorkbook workbook) throws Exception {

        AeaParState state = new AeaParState();
        state.setRootOrgId(rootParState.getRootOrgId());
        state.setStageId(rootParState.getStageId());
        state.setParentStateId(rootParState.getParStateId());
        state.setIsDeleted(Status.OFF);
        List<AeaParState> listParState = aeaParStateMapper.listAeaParState(state);
        if(CollectionUtils.isNotEmpty(listParState)) {
            for (AeaParState parState : listParState) {
                if(StringUtils.isBlank(state.getParentStateId()) && StringUtils.isBlank(parState.getParentStateId())){
                    saveSubParStateTopic(topic, workbook, parState);
                }
                if(StringUtils.isNotBlank(state.getParentStateId())){
                    saveSubParStateTopic(topic, workbook, parState);
                }
            }
        }
    }

    private void saveSubParStateTopic(ITopic topic, IWorkbook workbook, AeaParState parState) throws Exception {

        ITopic subTopic = workbook.createTopic();
        StringBuffer title = new StringBuffer();
        if(StringUtils.isNotBlank(parState.getIsQuestion())){
            title.append("【");
            // 问题
            if(parState.getIsQuestion().equals(Status.ON)){
                title.append("问");
                // 是否需要回答
                if(StringUtils.isNotBlank(parState.getMustAnswer())) {
                    if (parState.getMustAnswer().equals(Status.ON)) {
                        title.append("、必");
                    }
                }
                // 回答方式: y表示是否选择，s表示单选答案，m表示多选答案
                if(StringUtils.isNotBlank(parState.getAnswerType())){
                    if(parState.getAnswerType().equals("m")){
                        title.append("、多");
                    }else {
                        title.append("、单");
                    }
                }
            // 答案
            }else{
                title.append("答");
            }
            title.append("】");
        }
        subTopic.setTitleText(title.toString() + parState.getStateName());
        subTopic.setFolded(false);
        topic.add(subTopic, ITopic.ATTACHED);

        // 处理子情形数据
        fillParStateItemTopic(parState, subTopic, workbook);

        // 处理情形材料、证照、表单
        AeaParIn in = new AeaParIn();
        in.setRootOrgId(parState.getRootOrgId());
        in.setStageId(parState.getStageId());
        in.setIsStateIn(NeedStateStatus.NEED_STATE.getValue());
        in.setParStateId(parState.getParStateId());
        in.setIsDeleted(Status.OFF);
        handleMatAndCertFormTopic(subTopic, workbook, in);

        // 处理情形事项
        List<AeaParStateItem> stateItemList = aeaParStateItemMapper.listStageStateItem(parState.getParStateId(), parState.getStageId(), parState.getRootOrgId());
        if(stateItemList!=null&&stateItemList.size()>0){
            stateItemList.stream().forEach(item -> {
                ITopic stateFormTopic = workbook.createTopic();
                stateFormTopic.setTitleText("【事】" +item.getItemName());
                stateFormTopic.setFolded(false);
                subTopic.add(stateFormTopic, ITopic.ATTACHED);
            });
        }
    }

    private void handleMatAndCertFormTopic(ITopic topic, IWorkbook workbook, AeaParIn in) {

        if(in!=null){
            List<AeaParIn> ins = aeaParInMapper.listStageInMatCertForm(in);
            if (CollectionUtils.isNotEmpty(ins)) {
                ins.stream().forEach(item -> {
                    saveStageMatCertFormTopic(topic, workbook, item);
                });
            }
        }
    }

    /**
     * 处理证照
     *
     * @param topic
     * @param workbook
     * @param in
     */
    private void saveStageMatCertFormTopic(ITopic topic, IWorkbook workbook, AeaParIn in) {

        if (in!=null) {
            ITopic matCertFormTopic = workbook.createTopic();
            matCertFormTopic.setTitleText(in.getAeaMatCertName());
            if(StringUtils.isNotBlank(in.getFileType())){
                // 材料
                if(in.getFileType().equals(MindType.MATERIAL.getValue())){
                    matCertFormTopic.setTitleText("【材】" + in.getAeaMatCertName());
                    // 证照
                }else if(in.getFileType().equals(MindType.CERTIFICATE.getValue())){
                    matCertFormTopic.setTitleText("【证】" + in.getAeaMatCertName());
                    // 表单
                }else if(in.getFileType().equals("form")){
                    matCertFormTopic.setTitleText("【表】" + in.getAeaMatCertName());
                }
            }
            matCertFormTopic.setFolded(false);
            topic.add(matCertFormTopic, ITopic.ATTACHED);
        }
    }
}
