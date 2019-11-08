package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.mapper.ActStoFormMapper;
import com.augurit.agcloud.bsc.sc.att.utils.AttUtils;
import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.DateUtils;
import com.augurit.aplanmis.common.constants.InOutStatus;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.constants.NeedStateStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.item.AeaItemInoutAdminService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmind.core.*;
import org.xmind.core.io.DirectoryStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AeaItemStateXmindAdminServiceImpl {

    private static Logger log = LoggerFactory.getLogger(AeaItemStateXmindAdminServiceImpl.class);

    @Autowired
    private AeaItemStateAdminService aeaItemStateAdminService;

    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;

    @Autowired
    private AeaItemInoutAdminService aeaItemInoutAdminService;

    @Autowired
    private AeaCertMapper aeaCertMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private ActStoFormMapper actStoFormMapper;

    @Autowired
    private AeaItemStateFormMapper aeaItemStateFormMapper;

    @Autowired
    private AeaItemStateMapper aeaItemStateMapper;

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    public void loadXmindStateFileToPojo(InputStream inputStream, String itemVerId, String stateVerId) {

        IWorkbookBuilder builder = Core.getWorkbookBuilder();
        IWorkbook book = null;
        try {
            book = builder.loadFromStream(inputStream);
            ISheet sheet = book.getPrimarySheet();
            ITopic rootTopic = sheet.getRootTopic();
            if (StringUtils.isBlank(itemVerId)||StringUtils.isBlank(stateVerId)) {
                return;
            }

            String rootOrgId = SecurityContext.getCurrentOrgId();
            // 先删除某个事项版本下某情形版本情形数据
            aeaItemStateMapper.batchDelAeaItemStateVerState(itemVerId, stateVerId, rootOrgId);

            // 删除通用材料、证照
            AeaItemInout aeaItemInout = new AeaItemInout();
            aeaItemInout.setRootOrgId(rootOrgId);
            aeaItemInout.setItemVerId(itemVerId);
            aeaItemInout.setStateVerId(stateVerId);
            aeaItemInout.setIsInput(InOutStatus.IN.getValue());
            aeaItemInout.setIsDeleted(Status.OFF);
            aeaItemInout.setIsStateIn(NeedStateStatus.NOT_NEED_STATE.getValue());
            aeaItemInoutMapper.batchDeleteAeaItemInout(aeaItemInout);

            // 删除通用表单
            AeaItemStateForm stateForm = new AeaItemStateForm();
            stateForm.setItemVerId(itemVerId);
            stateForm.setItemStateVerId(stateVerId);
            stateForm.setIsStateForm(NeedStateStatus.NOT_NEED_STATE.getValue());
            aeaItemStateFormMapper.deleteAeaItemStateFormByCond(stateForm);

            if (CollectionUtils.isNotEmpty(rootTopic.getAllChildren())) {
                AeaItemBasic oneBasic = aeaItemBasicMapper.getOneByItemVerId(itemVerId, rootOrgId);
                for (ITopic topic : rootTopic.getAllChildren()) {
                    //根情形先保存
                    String stateName = topic.getTitleText();
                    AeaItemState rootState = new AeaItemState();
                    rootState.setStateSeq(CommonConstant.SEQ_SEPARATOR);
                    rootState.setStateVerId(stateVerId);
                    AeaItemState itemState = initialAeaItemState(oneBasic, stateName, rootState);
                    if (StringUtils.isBlank(stateVerId)) {
                        stateVerId = itemState.getStateVerId();
                    }
                    itemState.setIsQuestion(Status.ON);
                    itemState.setMustAnswer(Status.OFF);
                    itemState.setAnswerType("s");
                    if (CollectionUtils.isNotEmpty(topic.getAllChildren()) && ArrayUtils.contains(new String[]{"是", "否"}, topic.getAllChildren().get(0).getTitleText())) {
                        itemState.setMustAnswer(Status.ON);
                        itemState.setAnswerType("y");
                    }
                    if (CollectionUtils.isEmpty(topic.getAllChildren())) {
                        itemState.setIsQuestion(Status.ON);
                        itemState.setMustAnswer(Status.OFF);
                        itemState.setAnswerType("s");
                    }
                    aeaItemStateAdminService.saveAeaItemState(itemState);
                    //遍历子情形
                    if (CollectionUtils.isNotEmpty(topic.getAllChildren())) {
                        for (ITopic subTopic : topic.getAllChildren()) {
                            saveItemState(subTopic, oneBasic, itemState);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Exception: " + e.getMessage());
        }
    }

    private void saveItemState(ITopic topic, AeaItemBasic itemBasic, AeaItemState itemState) {

        AeaItemState subItemState;
        String stateName = topic.getTitleText();
        subItemState = initialAeaItemState(itemBasic, stateName, itemState);
        if (Status.ON.equals(subItemState.getIsQuestion()) || (CollectionUtils.isNotEmpty(topic.getAllChildren())
                && ArrayUtils.contains(new String[]{"是", "否"}, topic.getAllChildren().get(0).getTitleText()))) {
            subItemState.setIsQuestion(Status.ON);
            subItemState.setMustAnswer(Status.OFF);
            subItemState.setAnswerType("s");
            if (CollectionUtils.isNotEmpty(topic.getAllChildren())
                    && ArrayUtils.contains(new String[]{"是", "否"}, topic.getAllChildren().get(0).getTitleText())) {
                subItemState.setMustAnswer(Status.ON);
                subItemState.setAnswerType("y");
            }

            if (CollectionUtils.isEmpty(topic.getAllChildren())) {
                subItemState.setIsQuestion(Status.OFF);
                subItemState.setMustAnswer(null);
                subItemState.setAnswerType(null);
            }
        }
        if (ArrayUtils.contains(new String[]{"是", "否"}, stateName)) {
            subItemState.setIsQuestion(Status.OFF);
            subItemState.setMustAnswer(null);
            subItemState.setAnswerType(null);
        }
        aeaItemStateAdminService.saveAeaItemState(subItemState);
        log.info("当前子情形为：{}", stateName);

        if (CollectionUtils.isNotEmpty(topic.getAllChildren())) {
            for (ITopic subTopic : topic.getAllChildren()) {
                saveItemState(subTopic, itemBasic, subItemState);
            }
        }
    }

    private AeaItemState initialAeaItemState(AeaItemBasic itemBasic, String stateName, AeaItemState itemState) {

        AeaItemState aeaItemState = new AeaItemState();
        aeaItemState.setItemStateId(UUID.randomUUID().toString());
        aeaItemState.setItemId(itemBasic.getItemId());
        aeaItemState.setItemVerId(itemBasic.getItemVerId());
        aeaItemState.setStateVerId(itemState.getStateVerId());
        Long maxSortNo = aeaItemStateAdminService.getMaxSortNo();
        aeaItemState.setSortNo(++maxSortNo);
        aeaItemState.setStateName(stateName);
        aeaItemState.setIsActive(Status.ON);
        aeaItemState.setUseEl(Status.OFF);
        aeaItemState.setIsQuestion(Status.ON);
        if (Status.ON.equals(itemState.getIsQuestion())) {
            aeaItemState.setIsQuestion(Status.OFF);
        }
        aeaItemState.setParentStateId(itemState.getItemStateId());
        aeaItemState.setStateSeq(itemState.getStateSeq() + aeaItemState.getItemStateId() + ".");
        aeaItemState.setCreater(itemBasic.getCreater());
        aeaItemState.setCreateTime(itemBasic.getCreateTime());
        return aeaItemState;
    }

    public boolean downloadXmindFileByItem(AeaItemBasic aeaItemBasic, String stateVerId, String rootOrgId, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String fileName = aeaItemBasic.getItemName() + "_" + DateUtils.parseToFormatDateString(new Date(), DateUtils.LOG_DATE_TIME) + ".xmind";
        File xmindFile = initialXmindFileByAeaItem(aeaItemBasic, stateVerId, rootOrgId);
        return !downloadXmindFile(request, response, fileName, xmindFile);
    }

    public boolean downloadXmindFile(HttpServletRequest request, HttpServletResponse response, String fileName, File xmindFile) throws Exception {

        BufferedInputStream bis = null;
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + AttUtils.reEncodeChineseFileName(fileName, request));
            byte[] buff = new byte[1024];
            os = response.getOutputStream();
            fis = new FileInputStream(xmindFile);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (os != null) {
                os.close();
            }
            if (fis != null) {
                fis.close();
            }
            xmindFile.delete();
        }
        return false;
    }

    private File initialXmindFileByAeaItem(AeaItemBasic aeaItemBasic, String stateVerId, String rootOrgId) throws IOException, CoreException {

        File file = File.createTempFile("temp", ".xmind");
        String filePath = file.getAbsolutePath();
        IWorkbookBuilder builder = Core.getWorkbookBuilder();
        IWorkbook workbook = builder.createWorkbook(new DirectoryStorage(file));
        ISheet sheet = workbook.getPrimarySheet();

        //中心主题
        ITopic rootTopic = sheet.getRootTopic();
        rootTopic.setTitleText(aeaItemBasic.getItemName());

        //设置子节点右展开
        rootTopic.setStructureClass("org.xmind.ui.logic.right");

        // 处理事项通用材料、证照、表单
        fillItemCommonMatCertFormTopic(rootTopic, workbook, aeaItemBasic.getItemVerId(), stateVerId, rootOrgId);

        // 根情形节点
        AeaItemState state = new AeaItemState();
        state.setRootOrgId(rootOrgId);
        state.setItemVerId(aeaItemBasic.getItemVerId());
        state.setStateVerId(stateVerId);
        state.setParentStateId(null);
        state.setIsDeleted("0");
        fillItemStateTopic(state, rootTopic, workbook);

        workbook.save(filePath);
        File tmpFile = file;
        file.deleteOnExit();
        return tmpFile;
    }

    private void fillItemCommonMatCertFormTopic(ITopic rootTopic, IWorkbook workbook, String itemVerId, String stateVerId, String rootOrgId) {

        // 处理公共材料、证照
        AeaItemInout aeaItemInout = new AeaItemInout();
        aeaItemInout.setRootOrgId(rootOrgId);
        aeaItemInout.setItemVerId(itemVerId);
        aeaItemInout.setStateVerId(stateVerId);
        aeaItemInout.setIsInput(InOutStatus.IN.getValue());
        aeaItemInout.setIsDeleted(Status.OFF);
        aeaItemInout.setIsStateIn(NeedStateStatus.NOT_NEED_STATE.getValue());

        // 处理公共表单
        AeaItemStateForm aeaItemStateForm = new AeaItemStateForm();
        aeaItemStateForm.setItemVerId(itemVerId);
        aeaItemStateForm.setItemStateVerId(stateVerId);
        aeaItemStateForm.setIsStateForm(NeedStateStatus.NOT_NEED_STATE.getValue());

        // 处理公共材料、证照、表单
        handleMatAndCertFormTopic(rootTopic, workbook, aeaItemInout, aeaItemStateForm);
    }

    private void fillItemStateTopic(AeaItemState rootState, ITopic topic, IWorkbook workbook) {

        AeaItemState state = new AeaItemState();
        state.setRootOrgId(rootState.getRootOrgId());
        state.setItemVerId(rootState.getItemVerId());
        state.setStateVerId(rootState.getStateVerId());
        state.setParentStateId(rootState.getItemStateId());
        state.setIsDeleted(Status.OFF);
        List<AeaItemState> listSubItemState = aeaItemStateAdminService.listAeaItemState(state);
        if (CollectionUtils.isNotEmpty(listSubItemState)) {
            for (AeaItemState itemState : listSubItemState) {
                if (StringUtils.isEmpty(state.getParentStateId()) && StringUtils.isEmpty(itemState.getParentStateId())) {
                    saveSubItemStateTopic(topic, workbook, itemState);
                }
                if (StringUtils.isNotBlank(state.getParentStateId())) {
                    saveSubItemStateTopic(topic, workbook, itemState);
                }
            }
        }
    }

    private void saveSubItemStateTopic(ITopic topic, IWorkbook workbook, AeaItemState itemState) {

        ITopic subStateTopic = workbook.createTopic();
        StringBuffer title = new StringBuffer();
        if(StringUtils.isNotBlank(itemState.getIsQuestion())){
            title.append("【");
            // 问题
            if(itemState.getIsQuestion().equals(Status.ON)){
                title.append("问");
                // 是否需要回答
                if(StringUtils.isNotBlank(itemState.getMustAnswer())) {
                    if (itemState.getMustAnswer().equals(Status.ON)) {
                        title.append("、必");
                    }
                }
                // 回答方式: y表示是否选择，s表示单选答案，m表示多选答案
                if(StringUtils.isNotBlank(itemState.getAnswerType())){
                    if(itemState.getAnswerType().equals("m")){
                        title.append("、多");
                    }else {
                        title.append("、单");
                    }
                }
                if(StringUtils.isNotBlank(itemState.getIsProcStartCond())){
                    if(itemState.getIsProcStartCond().equals(Status.ON)) {
                        title.append("、流");
                    }
                }
            // 答案
            }else{
                title.append("答");
            }
            title.append("】");
        }
        subStateTopic.setTitleText(title.toString() + itemState.getStateName());
        subStateTopic.setFolded(false);
        topic.add(subStateTopic, ITopic.ATTACHED);
        fillItemStateTopic(itemState, subStateTopic, workbook);
        implementMaterialTopic(itemState, subStateTopic, workbook);
    }

    private void implementMaterialTopic(AeaItemState itemState, ITopic topic, IWorkbook workbook) {

        // 处理情形材料、证照
        AeaItemInout aeaItemInout = new AeaItemInout();
        aeaItemInout.setRootOrgId(itemState.getRootOrgId());
        aeaItemInout.setItemVerId(itemState.getItemVerId());
        aeaItemInout.setStateVerId(itemState.getStateVerId());
        aeaItemInout.setIsInput(InOutStatus.IN.getValue());
        aeaItemInout.setIsStateIn(NeedStateStatus.NEED_STATE.getValue());
        aeaItemInout.setItemStateId(itemState.getItemStateId());
        aeaItemInout.setIsDeleted(Status.OFF);

        // 处理情形表单
        AeaItemStateForm aeaItemStateForm = new AeaItemStateForm();
        aeaItemStateForm.setItemVerId(itemState.getItemVerId());
        aeaItemStateForm.setItemStateVerId(itemState.getStateVerId());
        aeaItemStateForm.setIsStateForm(NeedStateStatus.NEED_STATE.getValue());
        aeaItemStateForm.setItemStateId(itemState.getItemStateId());

        // 处理情形材料、证照、表单
        handleMatAndCertFormTopic(topic, workbook, aeaItemInout, aeaItemStateForm);
    }

    private void handleMatAndCertFormTopic(ITopic topic, IWorkbook workbook, AeaItemInout aeaItemInout, AeaItemStateForm stateForm) {

        if(aeaItemInout!=null){
            List<AeaItemInout> itemInouts = aeaItemInoutAdminService.listAeaItemInout(aeaItemInout);
            if (CollectionUtils.isNotEmpty(itemInouts)) {
                itemInouts.stream().forEach(itemInout -> {
                    if(StringUtils.isNotBlank(itemInout.getFileType())){
                        // 材料
                        if(itemInout.getFileType().equals(MindType.MATERIAL.getValue())){
                            saveItemMatTopic(topic, workbook, itemInout.getMatId());
                            // 证照
                        }else{
                            saveItemCertTopic(topic, workbook, itemInout.getCertId());
                        }
                    }
                });
            }
        }

        if(stateForm!=null){
            List<AeaItemStateForm> itemFormList = aeaItemStateFormMapper.listItemStateFormRelInfo(stateForm);
            if (CollectionUtils.isNotEmpty(itemFormList)) {
                itemFormList.stream().forEach(itemForm -> {
                    saveItemFormTopic(topic, workbook, itemForm.getFormId());
                });
            }
        }
    }

    /**
     * 处理证照
     *
     * @param topic
     * @param workbook
     * @param certId
     */
    private void saveItemCertTopic(ITopic topic, IWorkbook workbook, String certId) {

        AeaCert cert = aeaCertMapper.selectOneById(certId);
        if (cert!=null) {
            ITopic certTopic = workbook.createTopic();
            certTopic.setTitleText("【证】" + cert.getCertName());
            certTopic.setFolded(false);
            topic.add(certTopic, ITopic.ATTACHED);
        }
    }

    /**
     *
     * 处理材料
     *
     * @param topic
     * @param workbook
     * @param matId
     */
    private void saveItemMatTopic(ITopic topic, IWorkbook workbook, String matId)  {

        AeaItemMat mat = aeaItemMatMapper.selectOneById(matId);
        if (mat!=null) {
            ITopic matTopic = workbook.createTopic();
            matTopic.setTitleText("【材】" + mat.getMatName());
            matTopic.setFolded(false);
            topic.add(matTopic, ITopic.ATTACHED);
        }
    }

    /**
     * 处理表单
     *
     * @param topic
     * @param workbook
     * @param formId
     * @
     */
    private void saveItemFormTopic(ITopic topic, IWorkbook workbook, String formId)  {

        ActStoForm form = actStoFormMapper.getActStoFormById(formId);
        if (form!=null) {
            ITopic matTopic = workbook.createTopic();
            matTopic.setTitleText("【表】" + form.getFormName());
            matTopic.setFolded(false);
            topic.add(matTopic, ITopic.ATTACHED);
        }
    }
}
