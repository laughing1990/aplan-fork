package com.augurit.aplanmis.common.service.admin.par.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadFileStrategy;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.BpmnDiagramType;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.PublishStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.vo.diagram.BpmnDiagram;
import com.augurit.aplanmis.common.vo.diagram.BpmnDiagramAttrs;
import com.augurit.aplanmis.common.vo.diagram.BpmnDiagramCell;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 主题定义版本表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaParThemeVerAdminServiceImpl implements AeaParThemeVerAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParThemeVerAdminServiceImpl.class);

    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;

    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;

    @Autowired
    private AeaParThemeSeqMapper aeaParThemeSeqMapper;

    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;

    @Autowired
    private AeaParStateMapper aeaParStateMapper;

    @Autowired
    private AeaParInMapper aeaParInMapper;

    @Autowired
    private AeaParStageItemInMapper aeaParStageItemInMapper;

    @Autowired
    private AeaParStateItemMapper aeaParStateItemMapper;

    @Autowired
    private AeaParStateFormMapper aeaParStateFormMapper;

    @Autowired
    private UploaderFactory uploaderFactory;

    @Autowired
    private BscAttMapper bscAttMapper;

    @Autowired
    private AeaParStageGuideMapper aeaParStageGuideMapper;

    @Autowired
    private AeaParStageChargesMapper aeaParStageChargesMapper;

    @Autowired
    private AeaParStageQuestionsMapper aeaParStageQuestionsMapper;

    @Autowired
    private AeaItemServiceBasicMapper aeaItemServiceBasicMapper;

    @Autowired
    private AeaServiceWindowStageMapper aeaServiceWindowStageMapper;

    @Autowired
    private AeaParShareMatMapper aeaParShareMatMapper;

    @Autowired
    private AeaParStageAdminService aeaParStageAdminService;

    @Autowired
    private AeaParStageOneformMapper oneformMapper;

    @Autowired
    ServletContext servletContext;

    @Autowired
    AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Override
    public void saveAeaParThemeVer(AeaParThemeVer aeaParThemeVer) throws Exception {

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();

        // 获取最大编号信息
        Double maxNum = new Double(0.01);
        AeaParThemeSeq themeSeq = aeaParThemeSeqMapper.getAeaParThemeSeqByThemeId(aeaParThemeVer.getThemeId(), rootOrgId);
        if (themeSeq != null) {
            if (themeSeq.getMaxNum() != null) {
                maxNum = themeSeq.getMaxNum() + 0.01;
            }
            themeSeq.setMaxNum(maxNum);
            themeSeq.setModifier(userId);
            themeSeq.setModifyTime(new Date());
            // 更新最大编号
            aeaParThemeSeqMapper.updateOne(themeSeq);
        } else {
            themeSeq = new AeaParThemeSeq();
            themeSeq.setThemeSeqId(UUID.randomUUID().toString());
            themeSeq.setThemeId(aeaParThemeVer.getThemeId());
            themeSeq.setMaxNum(maxNum);
            themeSeq.setCreater(userId);
            themeSeq.setCreateTime(new Date());
            themeSeq.setRootOrgId(rootOrgId);
            // 保存最大编号信息
            aeaParThemeSeqMapper.insertOne(themeSeq);
        }

        // 保存版本信息
        aeaParThemeVer.setVerNum(maxNum);
        aeaParThemeVer.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaParThemeVer.setIsPublish(PublishStatus.UNPUBLISHED.getValue());
        aeaParThemeVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaParThemeVer.setCreater(userId);
        aeaParThemeVer.setCreateTime(new Date());
        aeaParThemeVer.setRootOrgId(rootOrgId);
        aeaParThemeVerMapper.insertOne(aeaParThemeVer);

        // 复制上一个发布版本相关数据
        if (maxNum != 0.01) {
            AeaParThemeVer sThemeVer = new AeaParThemeVer();
            sThemeVer.setThemeId(aeaParThemeVer.getThemeId());
            sThemeVer.setRootOrgId(rootOrgId);
            sThemeVer.setVerNum(maxNum - 0.01);
            sThemeVer.setIsActive(ActiveStatus.ACTIVE.getValue());
            sThemeVer.setIsPublish(PublishStatus.PUBLISHED.getValue());
            sThemeVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            List<AeaParThemeVer> themeVerList = aeaParThemeVerMapper.listAeaParThemeVer(sThemeVer);
            AeaParThemeVer preThemeVer = null;
            if (themeVerList != null && themeVerList.size() > 0) {
                preThemeVer = themeVerList.get(0);
            } else {
                preThemeVer = getMaxNumActiveVerByThemeId(aeaParThemeVer.getThemeId(), rootOrgId);
            }
            if (preThemeVer != null) {
                copyPreThemeVerRelData(preThemeVer.getThemeVerId(), aeaParThemeVer, rootOrgId);
            }
        }
    }

    /**
     * 主题版本下的流程图
     *
     * @param oldThemeVerId
     * @param newThemeVerId
     * @throws Exception
     */
    private void copyPreThemeVerRelAtt(String oldThemeVerId, String newThemeVerId, String topOrgId) throws Exception {

        List<BscAttForm> forms = bscAttMapper.listAttLinkAndDetail("AEA_PAR_THEME_VER", "THEME_VER_ID", oldThemeVerId, null, topOrgId, null);
        copyAndCreateAttlink(forms, newThemeVerId);
    }

    /**
     * 复制并产生新的附件数据
     *
     * @param forms
     * @param newBizId
     * @return
     * @throws Exception
     */
    private String copyAndCreateAttlink(List<BscAttForm> forms, String newBizId) throws Exception {

        StringBuffer sb = new StringBuffer();
        if (forms != null && forms.size() > 0) {
            for (BscAttForm form : forms) {
                String newDetailId = UuidUtil.generateUuid();
                String newObjectId = form.getObjectId();
                UploadFileStrategy uploadFileStrategy = uploaderFactory.create(form.getStoreType());
                InputStream in = uploadFileStrategy.download(form.getDetailId());
                if (in != null) {
                    newObjectId = ((MongoDbAchieve) uploadFileStrategy).uploadFile(in, form.getAttName());
                }
                form.setDetailId(newDetailId);
                form.setObjectId(newObjectId);
                bscAttMapper.insertDetail(form);
                BscAttLink newLink = BscAttLink.from(form);
                newLink.setRecordId(newBizId);
                bscAttMapper.insertLink(newLink);
                sb.append(newDetailId).append(",");
            }
        }
        return sb.toString();
    }

    /**
     * 复制主题版本下数据
     *
     * @param oldThemeVerId
     * @param newThemeVer
     */
    private void copyPreThemeVerRelData(String oldThemeVerId, AeaParThemeVer newThemeVer, String rootOrgId) throws Exception {

        String newThemeVerId = newThemeVer.getThemeVerId();

        // 复制主题版本下流程图
        copyPreThemeVerRelAtt(oldThemeVerId, newThemeVerId, rootOrgId);

        // 复制主题版本下共享材料
        copyPreThemeVerRelShareMat(oldThemeVerId, newThemeVerId, rootOrgId);

        // 处理阶段数据
        Map<String, String> stageIdsMap = copyPreThemeVerStage(oldThemeVerId, newThemeVerId, rootOrgId);
        if (stageIdsMap != null && stageIdsMap.size() > 0) {
            for (String oldStageId : stageIdsMap.keySet()) {
                String newStageId = stageIdsMap.get(oldStageId);
                // 处理阶段关联数据
                copyPreThemeVerStageRelData(oldStageId, newStageId, newThemeVer, rootOrgId);
            }
        }
    }

    /**
     * 复制主题版本下的共享材料
     *
     * @param oldThemeVerId
     * @param newThemeVerId
     * @param rootOrgId
     */
    private void copyPreThemeVerRelShareMat(String oldThemeVerId, String newThemeVerId, String rootOrgId) {

        AeaParShareMat shareMat = new AeaParShareMat();
        shareMat.setRootOrgId(rootOrgId);
        shareMat.setThemeVerId(oldThemeVerId);
        List<AeaParShareMat> shareMats = aeaParShareMatMapper.listAeaParShareMat(shareMat);
        if (shareMats != null && shareMats.size() > 0) {
            for (AeaParShareMat item : shareMats) {
                item.setShareMatId(UuidUtil.generateUuid());
                item.setThemeVerId(newThemeVerId);
                aeaParShareMatMapper.insertAeaParShareMat(item);
            }
        }
    }

    /**
     * 复制主题版本单阶段下的数据
     *
     * @param oldStageId
     * @param newStageId
     * @param newThemeVer
     */
    private void copyPreThemeVerStageRelData(String oldStageId, String newStageId, AeaParThemeVer newThemeVer, String rootOrgId) throws Exception {

        // 1、阶段事项
        Map<String, String> stageItemMap = copyPreThemeVerStageItem(oldStageId, newStageId, newThemeVer, rootOrgId);
        // 2、阶段情形
        Map<String, String> stageStateMap = copyPreThemeVerStageState(oldStageId, newStageId, rootOrgId);
        // 3、阶段情形输入材料、证照
        Map<String, String> stageStateInMap = copyPreThemeVerStageStateIn(oldStageId, newStageId, stageStateMap, rootOrgId);
        // 4、阶段情形表单
        copyPreThemeVerStageStateForm(oldStageId, newStageId, stageStateMap);
        // 5、阶段非情形输入材料、证照
        Map<String, String> stageNoStateInMap = copyPreThemeVerStageNoStateIn(oldStageId, newStageId, rootOrgId);
        // 6、阶段非情形表单
        copyPreThemeVerStageNoStateForm(oldStageId, newStageId);
        // 7、阶段情形事项
        copyPreThemeVerStageStateItem(oldStageId, stageItemMap, stageStateMap, rootOrgId);
        // 8、阶段材料事项
        Map<String, String> allInMap = new HashMap<>();
        if (stageStateInMap != null && stageStateInMap.size() > 0) {
            allInMap.putAll(stageStateInMap);
        }
        if (stageNoStateInMap != null && stageNoStateInMap.size() > 0) {
            allInMap.putAll(stageNoStateInMap);
        }
        copyPreThemeVerStageStateItemIn(oldStageId, stageItemMap, allInMap);
        // 9、阶段办事指南
        copyPreThemeVerStageGuide(oldStageId, newStageId, rootOrgId);
        // 10、阶段总表关联复制
        copyPreThemeVerStageOneForm(oldStageId, newStageId);
    }

    /**
     * 阶段总表关联复制
     *
     * @param oldStageId
     * @param newStageId
     */
    private void copyPreThemeVerStageOneForm(String oldStageId, String newStageId) {

        AeaParStageOneform soneform = new AeaParStageOneform();
        soneform.setParStageId(oldStageId);
        List<AeaParStageOneform> oneFormList = oneformMapper.listAeaParStageOneformNoRel(soneform);
        if (oneFormList != null && oneFormList.size() > 0) {
            for (AeaParStageOneform oneForm : oneFormList) {
                oneForm.setStageOneformId(UUID.randomUUID().toString());
                oneForm.setParStageId(newStageId);
                oneformMapper.insertAeaParStageOneform(oneForm);
            }
        }
    }

    /**
     * 复制阶段办事指南
     *
     * @param oldStageId
     * @param newStageId
     */
    private void copyPreThemeVerStageGuide(String oldStageId, String newStageId, String topOrgId) throws Exception {

        // 1、基本信息
        AeaParStageGuide guide = aeaParStageGuideMapper.getAeaParStageGuideByStageId(oldStageId, topOrgId);
        if (guide != null) {
            String oldGuideId = guide.getGuideId();
            String newGuideId = UuidUtil.generateUuid();
            // 复制附件--网上办理附件
            List<BscAttForm> forms1 = bscAttMapper.listAttLinkAndDetail("AEA_PAR_STAGE_GUIDE", "WSBLLCT", oldGuideId, null, topOrgId, null);
            String s1 = copyAndCreateAttlink(forms1, newGuideId);
            // 复制附件--网上办理附件
            List<BscAttForm> forms2 = bscAttMapper.listAttLinkAndDetail("AEA_PAR_STAGE_GUIDE", "CKBLLCT", oldGuideId, null, topOrgId, null);
            String s2 = copyAndCreateAttlink(forms2, newGuideId);
            // 复制附件--流程图附件
            List<BscAttForm> forms3 = bscAttMapper.listAttLinkAndDetail("AEA_PAR_STAGE_GUIDE", "GUIDE_ATT", oldGuideId, null, topOrgId, null);
            String s3 = copyAndCreateAttlink(forms3, newGuideId);
            // 复制基本信息
            guide.setGuideId(newGuideId);
            guide.setStageId(newStageId);
            guide.setWsbllct(s1);
            guide.setCkbllct(s2);
            guide.setGuideAtt(s3);
            guide.setRootOrgId(topOrgId);
            aeaParStageGuideMapper.insertAeaParStageGuide(guide);
        }

        // 2、设立依据
        AeaItemServiceBasic basic = new AeaItemServiceBasic();
        basic.setTableName("AEA_PAR_STAGE");
        basic.setPkName("STAGE_ID");
        basic.setRecordId(oldStageId);
        basic.setRootOrgId(topOrgId);
        List<AeaItemServiceBasic> basicList = aeaItemServiceBasicMapper.listAeaItemServiceBasic(basic);
        if (basicList != null && basicList.size() > 0) {
            for (AeaItemServiceBasic item : basicList) {
                item.setBasicId(UuidUtil.generateUuid());
                item.setRecordId(newStageId);
                item.setRootOrgId(topOrgId);
                aeaItemServiceBasicMapper.insertAeaItemServiceBasic(item);
            }
        }

        // 3、窗口办理
        AeaServiceWindowStage swindowStage = new AeaServiceWindowStage();
        swindowStage.setStageId(oldStageId);
        swindowStage.setRootOrgId(topOrgId);
        List<AeaServiceWindowStage> windowStageList = aeaServiceWindowStageMapper.listAeaServiceWindowStage(swindowStage);
        if (windowStageList != null && windowStageList.size() > 0) {
            for (AeaServiceWindowStage item : windowStageList) {
                item.setWindStageId(UuidUtil.generateUuid());
                item.setStageId(newStageId);
                item.setRootOrgId(topOrgId);
                aeaServiceWindowStageMapper.insertAeaServiceWindowStage(item);
            }
        }

        // 4、收费项目
        AeaParStageCharges charges = new AeaParStageCharges();
        charges.setStageId(oldStageId);
        charges.setRootOrgId(topOrgId);
        List<AeaParStageCharges> chargesList = aeaParStageChargesMapper.listAeaParStageCharges(charges);
        if (chargesList != null && chargesList.size() > 0) {
            for (AeaParStageCharges item : chargesList) {
                item.setChargeId(UuidUtil.generateUuid());
                item.setStageId(newStageId);
                item.setRootOrgId(topOrgId);
                aeaParStageChargesMapper.insertAeaParStageCharges(item);
            }
        }

        // 5、常见问题
        AeaParStageQuestions questions = new AeaParStageQuestions();
        questions.setStageId(oldStageId);
        questions.setRootOrgId(topOrgId);
        List<AeaParStageQuestions> questionsList = aeaParStageQuestionsMapper.listAeaParStageQuestions(questions);
        if (questionsList != null && questionsList.size() > 0) {
            for (AeaParStageQuestions item : questionsList) {
                item.setQuestionId(UuidUtil.generateUuid());
                item.setStageId(newStageId);
                item.setRootOrgId(topOrgId);
                aeaParStageQuestionsMapper.insertAeaParStageQuestions(item);
            }
        }
    }

    /**
     * 处理阶段数据
     *
     * @param oldThemeVerId
     * @param newThemeVerId
     * @return 阶段旧新主键集合
     */
    private Map<String, String> copyPreThemeVerStage(String oldThemeVerId, String newThemeVerId, String rootOrgId) {

        AeaParStage sstage = new AeaParStage();
        sstage.setThemeVerId(oldThemeVerId);
        sstage.setRootOrgId(rootOrgId);
        List<AeaParStage> relStage = aeaParStageMapper.listAeaParStage(sstage);
        if (relStage != null && relStage.size() > 0) {
            Map<String, String> stageIdsMap = new HashMap<String, String>();
            for (AeaParStage s : relStage) {
                String oldStageId = s.getStageId();
                String newStageId = UUID.randomUUID().toString();
                s.setStageId(newStageId);
                s.setThemeVerId(newThemeVerId);
                stageIdsMap.put(oldStageId, newStageId);
            }
            // 替换阶段parentId字段数据
            for (String key : stageIdsMap.keySet()) {
                for (AeaParStage s : relStage) {
                    // 辅线需要更新parentId
                    if (StringUtils.isNotBlank(s.getIsNode()) && !s.getIsNode().equals("1")
                            && StringUtils.isNotBlank(s.getParentId()) && s.getParentId().equals(key)) {
                        s.setParentId(stageIdsMap.get(key));
                    }
                }
            }
            // 保存阶段数据
            for (AeaParStage ss : relStage) {
                ss.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaParStageMapper.insertAeaParStage(ss);
            }
            return stageIdsMap;
        }
        return null;
    }

    /**
     * 复制阶段事项数据
     *
     * @param oldStageId
     * @param newStageId
     * @param newThemeVer
     * @return
     */
    private Map<String, String> copyPreThemeVerStageItem(String oldStageId, String newStageId, AeaParThemeVer newThemeVer, String rootOrgId) {

        String themeVerDiagram = newThemeVer.getThemeVerDiagram();
        Map<String, String> map = new HashMap<String, String>();
        List<AeaParStageItem> relItems = aeaParStageItemMapper.listAeaStageItemByStageId(oldStageId, "", rootOrgId);
        if (relItems != null && relItems.size() > 0) {
            for (AeaParStageItem si : relItems) {
                String oldId = si.getStageItemId();
                String newId = UUID.randomUUID().toString();
                si.setStageItemId(newId);
                si.setStageId(newStageId);
                // 此处暂时处理只返回必选事项、可选事项
                if (StringUtils.isNotBlank(si.getIsOptionItem())) {
                    if (si.getIsOptionItem().equals(Status.OFF) || si.getIsOptionItem().equals(Status.ON)) {
                        map.put(oldId, newId);
                    }
                }
                aeaParStageItemMapper.insertAeaParStageItem(si);
                if (StringUtils.isNotBlank(themeVerDiagram)) {
                    themeVerDiagram = themeVerDiagram.replaceAll(oldId, newId);
                }
            }
        }
        // 更新拖拽视图json数据
        if (StringUtils.isNotBlank(themeVerDiagram)) {
            themeVerDiagram = themeVerDiagram.replaceAll(oldStageId, newStageId);
            newThemeVer.setThemeVerDiagram(themeVerDiagram);
            aeaParThemeVerMapper.updateOne(newThemeVer);
        }
        return map;
    }

    /**
     * 复制阶段情形数据
     *
     * @param oldStageId
     * @param newStageId
     * @return
     */
    private Map<String, String> copyPreThemeVerStageState(String oldStageId, String newStageId, String rootOrgId) {

        Map<String, String> map = new HashMap<String, String>();
        List<AeaParState> stateList = aeaParStateMapper.listAeaParStateByStageId(oldStageId, rootOrgId);
        if (stateList != null && stateList.size() > 0) {
            for (AeaParState state : stateList) {
                String oldStateId = state.getParStateId();
                String newStateId = UUID.randomUUID().toString();
                state.setParStateId(newStateId);
                state.setStageId(newStageId);
                state.setStateSeq(state.getStateSeq().replace(oldStateId, newStateId));
                map.put(oldStateId, newStateId);
            }
            // 替换新的父级以及序列
            for (String key : map.keySet()) {
                for (AeaParState s : stateList) {
                    if (StringUtils.isNotBlank(s.getParentStateId()) && s.getParentStateId().equals(key)) {
                        s.setParentStateId(map.get(key));
                    }
                    if (StringUtils.isNotBlank(s.getStateSeq()) && s.getStateSeq().indexOf(key) != -1) {
                        s.getStateSeq().replace(key, map.get(key));
                    }
                }
            }
            for (AeaParState ss : stateList) {
                aeaParStateMapper.insertAeaParState(ss);
            }
        }
        return map;
    }

    /**
     * 复制主题版本某阶段情形输入材料、证照
     *
     * @param oldStageId
     * @param newStageId
     * @param stateMap
     * @return
     */
    private Map<String, String> copyPreThemeVerStageStateIn(String oldStageId, String newStageId, Map<String, String> stateMap, String rootOrgId) {

        if (stateMap != null && stateMap.size() > 0) {
            Map<String, String> map = new HashMap<String, String>();
            AeaParIn aeaParIn = new AeaParIn();
            aeaParIn.setStageId(oldStageId);
            aeaParIn.setRootOrgId(rootOrgId);
            aeaParIn.setIsStateIn("1");
            aeaParIn.setIsDeleted("0");
            List<AeaParIn> inList = aeaParInMapper.listAeaParIn(aeaParIn);
            if (inList != null && inList.size() > 0) {
                for (AeaParIn in : inList) {
                    String newInId = UUID.randomUUID().toString();
                    String oldInId = in.getInId();
                    in.setInId(newInId);
                    in.setStageId(newStageId);
                    map.put(oldInId, newInId);
                    for (String key : stateMap.keySet()) {
                        if (StringUtils.isNotBlank(in.getParStateId()) && in.getParStateId().equals(key)) {
                            // 替换情形id字段
                            in.setParStateId(stateMap.get(key));
                            break;
                        }
                    }
                    in.setRootOrgId(rootOrgId);
                    aeaParInMapper.insertAeaParIn(in);
                }
            }
            return map;
        }
        return null;
    }

    /**
     * 复制主题版本某阶段情形输入表单
     *
     * @param oldStageId
     * @param newStageId
     * @param stateMap
     * @return
     */
    private Map<String, String> copyPreThemeVerStageStateForm(String oldStageId, String newStageId, Map<String, String> stateMap) {

        if (stateMap != null && stateMap.size() > 0) {
            Map<String, String> map = new HashMap<String, String>();
            AeaParStateForm stateForm = new AeaParStateForm();
            stateForm.setParStageId(oldStageId);
            stateForm.setIsStateForm("1");
            List<AeaParStateForm> formList = aeaParStateFormMapper.listAeaParStateForm(stateForm);
            if (formList != null && formList.size() > 0) {
                for (AeaParStateForm form : formList) {
                    String newId = UUID.randomUUID().toString();
                    String oldId = form.getStateFormId();
                    form.setStateFormId(newId);
                    form.setParStageId(newStageId);
                    map.put(oldId, newId);
                    for (String key : stateMap.keySet()) {
                        if (StringUtils.isNotBlank(form.getParStateId()) && form.getParStateId().equals(key)) {
                            // 替换情形id字段
                            form.setParStateId(stateMap.get(key));
                            break;
                        }
                    }
                    aeaParStateFormMapper.insertAeaParStateForm(form);
                }
            }
            return map;
        }
        return null;
    }

    /**
     * 复制主题版本某阶段非情形输入
     *
     * @param oldStageId
     * @param newStageId
     * @return
     */
    private Map<String, String> copyPreThemeVerStageNoStateIn(String oldStageId, String newStageId, String rootOrgId) {

        Map<String, String> map = new HashMap<String, String>();
        AeaParIn aeaParIn = new AeaParIn();
        aeaParIn.setStageId(oldStageId);
        aeaParIn.setRootOrgId(rootOrgId);
        aeaParIn.setIsStateIn("0");
        aeaParIn.setIsDeleted("0");
        List<AeaParIn> inList = aeaParInMapper.listAeaParIn(aeaParIn);
        if (inList != null && inList.size() > 0) {
            for (AeaParIn in : inList) {
                String newInId = UUID.randomUUID().toString();
                String oldInId = in.getInId();
                in.setInId(newInId);
                in.setStageId(newStageId);
                map.put(oldInId, newInId);
                in.setRootOrgId(rootOrgId);
                aeaParInMapper.insertAeaParIn(in);
            }
        }
        return map;
    }

    /**
     * 复制主题版本某阶段非情形输入表单
     *
     * @param oldStageId
     * @param newStageId
     * @return
     */
    private Map<String, String> copyPreThemeVerStageNoStateForm(String oldStageId, String newStageId) {

        Map<String, String> map = new HashMap<String, String>();
        AeaParStateForm stateForm = new AeaParStateForm();
        stateForm.setParStageId(oldStageId);
        stateForm.setIsStateForm("0");
        List<AeaParStateForm> formList = aeaParStateFormMapper.listAeaParStateForm(stateForm);
        if (formList != null && formList.size() > 0) {
            for (AeaParStateForm form : formList) {
                String newId = UUID.randomUUID().toString();
                String oldId = form.getStateFormId();
                form.setStateFormId(newId);
                form.setParStageId(newStageId);
                map.put(oldId, newId);
                aeaParStateFormMapper.insertAeaParStateForm(form);
            }
        }
        return map;
    }

    /**
     * 复制主题版本某阶情形事项
     *
     * @return
     */
    private Map<String, String> copyPreThemeVerStageStateItem(String oldStageId, Map<String, String> stageItemMap,
                                                              Map<String, String> stageStateMap, String rootOrgId) {

        if (StringUtils.isNotBlank(oldStageId)
                && stageItemMap != null && stageItemMap.size() > 0
                && stageStateMap != null && stageStateMap.size() > 0) {

            // 获取阶段所有的情形事项
            Map<String, String> map = new HashMap<String, String>();
            List<AeaParStateItem> stateItemList = aeaParStateItemMapper.listStageStateItemByStateAndOption(oldStageId, "0", null, rootOrgId);
            if (stateItemList != null && stateItemList.size() > 0) {
                for (AeaParStateItem stateItem : stateItemList) {
                    String oldId = stateItem.getStateItemId();
                    String newId = UUID.randomUUID().toString();
                    stateItem.setStateItemId(newId);
                    map.put(oldId, newId);
                    // 替换阶段事项id
                    for (String key : stageItemMap.keySet()) {
                        if (StringUtils.isNotBlank(stateItem.getStageItemId()) && stateItem.getStageItemId().equals(key)) {
                            // 替换情形id字段
                            stateItem.setStageItemId(stageItemMap.get(key));
                            break;
                        }
                    }
                    // 替换阶段情形id
                    for (String key : stageStateMap.keySet()) {
                        if (StringUtils.isNotBlank(stateItem.getParStateId()) && stateItem.getParStateId().equals(key)) {
                            // 替换情形id字段
                            stateItem.setParStateId(stageStateMap.get(key));
                            break;
                        }
                    }
                    stateItem.setRootOrgId(rootOrgId);
                    aeaParStateItemMapper.insertAeaParStateItem(stateItem);
                }
            }
            return map;
        }
        return null;
    }

    /**
     * 复制主题版本某阶段材料事项
     *
     * @return
     */
    private Map<String, String> copyPreThemeVerStageStateItemIn(String oldStageId, Map<String, String> stageItemMap,
                                                                Map<String, String> inMap) {

        if (StringUtils.isNotBlank(oldStageId)
                && stageItemMap != null && stageItemMap.size() > 0
                && inMap != null && inMap.size() > 0) {

            // 获取阶段所有的情形事项
            Map<String, String> map = new HashMap<String, String>();
            List<AeaParStageItemIn> stageItemInList = aeaParStageItemInMapper.listStageItemInByStageIdAndOption(oldStageId, "0");
            if (stageItemInList != null && stageItemInList.size() > 0) {
                for (AeaParStageItemIn stateItemIn : stageItemInList) {
                    String oldId = stateItemIn.getItemInId();
                    String newId = UUID.randomUUID().toString();
                    stateItemIn.setItemInId(newId);
                    map.put(oldId, newId);
                    // 替换阶段事项id
                    for (String key : stageItemMap.keySet()) {
                        if (StringUtils.isNotBlank(stateItemIn.getStageItemId()) && stateItemIn.getStageItemId().equals(key)) {
                            // 替换情形id字段
                            stateItemIn.setStageItemId(stageItemMap.get(key));
                            break;
                        }
                    }
                    // 替换阶段情形id
                    for (String key : inMap.keySet()) {
                        if (StringUtils.isNotBlank(stateItemIn.getInId()) && stateItemIn.getInId().equals(key)) {
                            // 替换情形id字段
                            stateItemIn.setInId(inMap.get(key));
                            break;
                        }
                    }
                    aeaParStageItemInMapper.insertAeaParStageItemIn(stateItemIn);
                }
            }
            return map;
        }
        return null;
    }

    @Override
    public void updateAeaParThemeVer(AeaParThemeVer aeaParThemeVer) {

        aeaParThemeVer.setModifier(SecurityContext.getCurrentUserId());
        aeaParThemeVer.setModifyTime(new Date());
        aeaParThemeVerMapper.updateOne(aeaParThemeVer);
    }

    @Override
    public void deleteAeaParThemeVerById(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaParThemeVerMapper.deleteById(id);
        }
    }

    @Override
    public PageInfo<AeaParThemeVer> listAeaParThemeVer(AeaParThemeVer aeaParThemeVer, Page page) {

        PageHelper.startPage(page);
        List<AeaParThemeVer> list = aeaParThemeVerMapper.listAeaParThemeVer(aeaParThemeVer);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParThemeVer>(list);
    }

    @Override
    public AeaParThemeVer getAeaParThemeVerById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取Form对象，ID为：{}", id);
            return aeaParThemeVerMapper.selectOneById(id);
        }
        return null;
    }

    @Override
    public List<AeaParThemeVer> listAeaParThemeVer(AeaParThemeVer aeaParThemeVer) {

        List<AeaParThemeVer> list = aeaParThemeVerMapper.listAeaParThemeVer(aeaParThemeVer);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaParThemeVer> listThemeVerSyncZTree(String themeId) {

        List<AeaParThemeVer> allNodes = new ArrayList<AeaParThemeVer>();
        AeaParThemeVer rootNode = new AeaParThemeVer();
        rootNode.setId(themeId);
        rootNode.setName("主题版本定义库");
        rootNode.setIsParent(true);
        rootNode.setOpen(true);
        rootNode.setType("root");
        allNodes.add(rootNode);
        if (StringUtils.isNotBlank(themeId)) {
            AeaParThemeVer themeVer = new AeaParThemeVer();
            themeVer.setThemeId(themeId);
            themeVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            AeaParThemeSeq themeSeq = aeaParThemeSeqMapper.getAeaParThemeSeqByThemeId(themeId, SecurityContext.getCurrentOrgId());
            List<AeaParThemeVer> list = aeaParThemeVerMapper.listAeaParThemeVer(themeVer);
            if (list != null && list.size() > 0) {
                for (AeaParThemeVer e : list) {
                    e.setId(e.getThemeVerId());
                    e.setName("V" + e.getVerNum());
                    if (e.getIsActive() != null
                            && e.getIsActive() == ActiveStatus.ACTIVE.getValue()) {
                        e.setName("V" + e.getVerNum());
                    }
                    e.setpId(themeId);
                    e.setIsParent(true);
                    e.setOpen(true);
                    e.setType("item");
                    allNodes.add(e);
                }
            }
        }
        return allNodes;
    }

    @Override
    public AeaParThemeVer getMaxNumActiveVerByThemeId(String themeId, String rootOrgId) {

        if (StringUtils.isNotBlank(themeId)) {
            AeaParThemeVer sVer = new AeaParThemeVer();
            sVer.setThemeId(themeId);
            sVer.setRootOrgId(rootOrgId);
            sVer.setIsActive(ActiveStatus.ACTIVE.getValue());
            sVer.setIsPublish(PublishStatus.TEST_RUN.getValue());
            sVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            List<AeaParThemeVer> verList = aeaParThemeVerMapper.listAeaParThemeVer(sVer);
            if (verList != null && verList.size() > 0) {
                return verList.get(0);
            }
        }
        return null;
    }

    @Override
    public AeaParThemeVer copyThemeVerRelData(String themeId, String themeVerId) throws Exception {

        if (StringUtils.isBlank(themeId)) {
            throw new IllegalArgumentException("主题themeId为空！");
        }
        if (StringUtils.isBlank(themeVerId)) {
            throw new IllegalArgumentException("主题版本themeVerId为空！");
        }

        // 需要复制的主题版本序列
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParThemeSeq seq = aeaParThemeSeqMapper.getAeaParThemeSeqByThemeId(themeId, rootOrgId);
        Assert.notNull(seq, "无法获取当前'themeId=" + themeId + "'主题版本序号!");

        // 生成最大版本号
        Double verNum = seq.getMaxNum() + AeaItemStateVer.INIT_VER;

        // 最新序列
        seq.setMaxNum(verNum);
        seq.setModifier(userId);
        seq.setModifyTime(new Date());
        aeaParThemeSeqMapper.updateOne(seq);

        // 最新版本
        AeaParThemeVer oldThemeVer = aeaParThemeVerMapper.selectOneById(themeVerId);
        AeaParThemeVer newThemeVer = AeaParThemeVer.initOne(themeId, userId, rootOrgId);
        newThemeVer.setVerNum(verNum);
        if (oldThemeVer != null) {
            newThemeVer.setThemeVerDiagram(oldThemeVer.getThemeVerDiagram());
        }
        newThemeVer.setRootOrgId(rootOrgId);
        aeaParThemeVerMapper.insertOne(newThemeVer);

        // 复制数据
        copyPreThemeVerRelData(themeVerId, newThemeVer, rootOrgId);
        return newThemeVer;
    }

    @Override
    public void testRunOrPublished(String themeId, String themeVerId, Double verNum, String type) {

        if (StringUtils.isBlank(themeId)) {
            throw new IllegalArgumentException("主题themeId为空！");
        }
        if (StringUtils.isBlank(themeVerId)) {
            throw new IllegalArgumentException("主题版本themeVerId为空！");
        }
        Assert.notNull(verNum, "主题版本序号verNum为空!");
        if (StringUtils.isBlank(type)) {
            throw new IllegalArgumentException("当前操作不明确！");
        }
        if (!(type.equals("2") || type.equals("1"))) {
            throw new IllegalArgumentException("当前操作不明确,可以是试运行或者发布！");
        }

        PublishStatus verStatus = PublishStatus.TEST_RUN;
        if (type.equals("1")) {
            verStatus = PublishStatus.PUBLISHED;
        }

        // 当前版本下
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParThemeVer themeVer = getAeaParThemeVerById(themeVerId);
        Assert.notNull(themeVer, "无法找到对应的主题版本, themeVerId: " + themeVerId);
        themeVer.setIsPublish(verStatus.getValue());
        themeVer.setVerNum(verNum);
        updateAeaParThemeVer(themeVer);

        // 主题版本序号
        AeaParThemeSeq seq = aeaParThemeSeqMapper.getAeaParThemeSeqByThemeId(themeId, rootOrgId);
        Assert.notNull(seq, "无法找到对应的主题版本序号, themeId: " + themeId);
        seq.setMaxNum(verNum);
        seq.setModifier(SecurityContext.getCurrentUserId());
        seq.setModifyTime(new Date());
        aeaParThemeSeqMapper.updateOne(seq);

        // 当点击试运行或已发布时，其他试运行或已发布版本就要变成已过时
        deprecateAllTestRunAndPublishedVersion(themeId, themeVerId, rootOrgId);
    }

    public void deprecateAllTestRunAndPublishedVersion(String themeId, String themeVerId, String rootOrgId) {

        aeaParThemeVerMapper.deprecateAllTestRunAndPublishedVersion(themeId, themeVerId, rootOrgId);
    }

    @Autowired
    private FileUtilsService fileUtilsService;

    @Override
    public void handleThemeVerAtt(String themeVerId, HttpServletRequest request) throws Exception {

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> themeVerAtts = req.getFiles("themeVerAtt");
        if (themeVerAtts != null && themeVerAtts.size() > 0) {
            for (MultipartFile file : themeVerAtts) {
                if (!file.isEmpty()) {
                    fileUtilsService.upload("AEA_PAR_THEME_VER", "THEME_VER_ID", themeVerId, null, file);

                    /*UploadFileCmd uploadFileCmd = uploaderFactory.createDefaultUpload(file, themeVerId, "AEA_PAR_THEME_VER", "THEME_VER_ID", UploadType.MONGODB.getValue());
                    attachmentAdminService.uploadFileStrategy(uploadFileCmd);*/
                }
            }
        }

    }

    /**
     * 保存拖拉拽视图
     *
     * @param themeVerId
     * @param themeVerDiagram
     */
    @Override
    public void saveThemeVerDiagram(String themeVerId, String themeVerDiagram) throws Exception {

        if (StringUtils.isBlank(themeVerId)) {
            throw new IllegalArgumentException("参数themeVerId为空!");
        }
        if (StringUtils.isBlank(themeVerDiagram)) {
            throw new IllegalArgumentException("参数themeVerDiagram为空!");
        }
        //解析数据
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        BpmnDiagram rootNoe = JSONObject.parseObject(themeVerDiagram, BpmnDiagram.class);
        if (rootNoe != null) {
            List<BpmnDiagramCell> cells = rootNoe.getCells();
            List<BpmnDiagramCell> stageCells = new ArrayList<BpmnDiagramCell>();
            List<String> realStageCellIds = new ArrayList<String>();
            List<BpmnDiagramCell> itemCells = new ArrayList<BpmnDiagramCell>();
            List<String> itemCellIds = new ArrayList<String>();
            if (cells != null && cells.size() > 0) {
                // 分类出阶段、事项
                for (BpmnDiagramCell cell : cells) {
                    if (StringUtils.isBlank(cell.getType())) {
                        throw new IllegalArgumentException("阶段或者事项类型没有指定!");
                    }
                    // 主线阶段、并行推进虚拟阶段、辅线阶段、
                    if (cell.getType().contains(BpmnDiagramType.REALSTAGE.getValue())||cell.getType().contains(BpmnDiagramType.AUXSTAGE.getValue())||cell.getType().contains(BpmnDiagramType.VIRTSTAGE.getValue())) {
                        stageCells.add(cell);
                        // 真实主线和辅线
                        if(cell.getType().contains(BpmnDiagramType.REALSTAGE.getValue())||cell.getType().contains(BpmnDiagramType.AUXSTAGE.getValue())){
                            realStageCellIds.add(cell.getId());
                        }
                    }
                    // 阶段事项节点数据
                    if (cell.getType().equals(BpmnDiagramType.ITEM.getValue())) {
                        itemCells.add(cell);
                        itemCellIds.add(cell.getId());
                    }
                }
                // 处理旧的阶段数据
                if(realStageCellIds!=null&&realStageCellIds.size()>0){
                    AeaParStage sstage = new AeaParStage();
                    sstage.setRootOrgId(rootOrgId);
                    sstage.setThemeVerId(themeVerId);
                    List<AeaParStage> relStage = aeaParStageMapper.listAeaParStage(sstage);
                    if (relStage != null && relStage.size() > 0) {
                        List<AeaParStage> needDelStage = new ArrayList<>();
                        for (AeaParStage stage : relStage) {
                            int i = 0;
                            for (String stageCellId : realStageCellIds) {
                                if(stage.getStageId().equals(stageCellId)){
                                    break;
                                }else{
                                    i++;
                                }
                            }
                            // 数据库真实阶段数据与传递阶段数据没有一个相同的
                            if(i==realStageCellIds.size()){
                                needDelStage.add(stage);
                            }
                        }
                        if(needDelStage!=null&&needDelStage.size()>0){
                            for (AeaParStage stage : needDelStage) {
                                stage.setIsDeleted(DeletedStatus.DELETED.getValue());
                                aeaParStageMapper.updateAeaParStage(stage);
                            }
                        }
                    }
                }
                // 处理旧的阶段事项数据
                if(itemCellIds!=null&&itemCellIds.size()>0){
                    List<AeaParStageItem> relStageItemList = aeaParStageItemMapper.listStageItemByThemeVerId(themeVerId, null, rootOrgId);
                    if (relStageItemList != null && relStageItemList.size() > 0) {
                        List<AeaParStageItem> needDelStageItem = new ArrayList<>();
                        for (AeaParStageItem stageItem : relStageItemList) {
                            int i = 0;
                            for (String itemCellId : itemCellIds) {
                                if(stageItem.getStageItemId().equals(itemCellId)){
                                    break;
                                }else{
                                    i++;
                                }
                            }
                            // 数据库真实阶段事项数据与传递阶段事项数据没有一个相同的
                            if(i==itemCellIds.size()){
                                needDelStageItem.add(stageItem);
                            }
                        }
                        if(needDelStageItem!=null&&needDelStageItem.size()>0){
                            for (AeaParStageItem stageItem : needDelStageItem) {
                                aeaParStageItemMapper.deleteAeaParStageItem(stageItem.getStageItemId());
                            }
                        }
                    }
                }

                // 处理阶段数据
                if (stageCells != null && stageCells.size() > 0) {
                    for (BpmnDiagramCell stageCell : stageCells) {
                        String stageId = stageCell.getId();
                        // 真实阶段数据 ==> 主线和辅线
                        if (stageCell.getType().contains(BpmnDiagramType.REALSTAGE.getValue())||stageCell.getType().contains(BpmnDiagramType.AUXSTAGE.getValue())) {
                            AeaParStage stageNodeData;
                            BpmnDiagramAttrs stageAttrs = stageCell.getAttrs();
                            if (stageAttrs != null && stageAttrs.getStage() != null) {
                                stageNodeData = stageAttrs.getStage();
                                stageNodeData.setStageId(stageId);
                                // 辅线数据
                                if(stageCell.getType().contains(BpmnDiagramType.AUXSTAGE.getValue())){
                                    stageNodeData.setParentId(stageCell.getParent());
                                }
                                stageNodeData.setStageName(stageCell.getAuEleName());
                            } else {
                                throw new IllegalArgumentException("阶段数据没有!");
                            }
                            AeaParStage stage = aeaParStageMapper.getAeaParStageById(stageId);
                            if (stage != null) {
                                BeanUtils.copyProperties(stage, stageNodeData);
                                aeaParStageAdminService.updateAeaParStage(stage);
                            } else {
                                String sortNo = "1";
                                // 主线
                                if(stageCell.getType().contains(BpmnDiagramType.REALSTAGE.getValue())){
                                    sortNo = stageCell.getType().replaceAll(BpmnDiagramType.REALSTAGE.getValue(), "");
                                    if(StringUtils.isBlank(sortNo)){
                                        sortNo = "1";
                                    }
                                    stageNodeData.setSortNo(new Long(sortNo));
                                    stageNodeData.setIsShowItem(Status.ON);
                                    stageNodeData.setIsNode("1");
                                // 辅线
                                }else if(stageCell.getType().contains(BpmnDiagramType.AUXSTAGE.getValue())){
                                    sortNo = stageCell.getType().replaceAll(BpmnDiagramType.AUXSTAGE.getValue(), "");
                                    if(StringUtils.isBlank(sortNo)){
                                        sortNo = "1";
                                    }
                                    stageNodeData.setSortNo(new Long(sortNo));
                                    stageNodeData.setIsShowItem(Status.OFF);
                                    stageNodeData.setIsNode("2");
                                    stageNodeData.setParentId(stageCell.getParent());
                                }
                                stageNodeData.setIsNeedState(Status.ON);
                                stageNodeData.setUseEl(Status.OFF);
                                stageNodeData.setNoptItemShowNum(0L);
                                stageNodeData.setOptItemShowNum(0L);
                                stageNodeData.setIsImgIcon(Status.ON);
                                stageNodeData.setHugeImgPath("/admin/theme/index/imgs/32/施工许可02.png");
                                stageNodeData.setBigImgPath("/stage/mainLine/images/立项用地许可.png");
                                stageNodeData.setThemeVerId(themeVerId);
                                aeaParStageAdminService.saveAeaParStage(stageNodeData);
                            }
                        }
                        // 处理阶段事项数据
                        if (itemCells != null && itemCells.size() > 0) {
                            List<BpmnDiagramCell> usedCells = new ArrayList<BpmnDiagramCell>();
                            for (BpmnDiagramCell itemCell : itemCells) {
                                if (StringUtils.isNotBlank(itemCell.getParent()) && itemCell.getParent().equals(stageId)) {
                                    usedCells.add(itemCell);
                                    List<String> realStageIds = new ArrayList<>();
                                    realStageIds.add(stageId);
                                    // 虚拟并行推进事项可以跨阶段
                                    if (stageCell.getType().contains(BpmnDiagramType.VIRTSTAGE.getValue())) {
                                        realStageIds.clear();
                                        realStageIds = Arrays.asList(itemCell.getRealParent());
                                    }
                                    if(realStageIds!=null&&realStageIds.size()>0){
                                        AeaParStageItem stageItemNodeData;
                                        BpmnDiagramAttrs attrs = itemCell.getAttrs();
                                        if (attrs != null && attrs.getItem() != null) {
                                            stageItemNodeData = attrs.getItem();
                                            // 实施事项Id
                                            String[] array = stageItemNodeData.getItemVerId().split("\\*");
                                            stageItemNodeData.setItemId(array[0]);
                                            stageItemNodeData.setItemVerId(array[1]);
                                        } else {
                                            throw new IllegalArgumentException("阶段下事项数据没有!");
                                        }
                                        int i=0;
                                        for(String realStageId: realStageIds){
                                            String stageItemId = itemCell.getId();
                                            stageItemNodeData.setStageItemId(stageItemId);
                                            i++;
                                            AeaParStageItem stageItem = aeaParStageItemMapper.getAeaParStageItemById(stageItemId);
                                            if (stageItem != null && stageItem.getStageId().equals(realStageId)) {
                                                BeanUtils.copyProperties(stageItem, stageItemNodeData);
                                                aeaParStageItemMapper.updateAeaParStageItem(stageItem);
                                            } else {
                                                stageItemId = UUID.randomUUID().toString();
                                                stageItemNodeData.setStageItemId(stageItemId);
                                                stageItemNodeData.setStageId(realStageId);
                                                Long sortNo = aeaParStageItemMapper.getMaxSortNoByStageId(realStageId);
                                                stageItemNodeData.setSortNo(sortNo == null ? 1L : (sortNo + 1L));
                                                stageItemNodeData.setCreater(userId);
                                                stageItemNodeData.setCreateTime(new Date());
                                                aeaParStageItemMapper.insertAeaParStageItem(stageItemNodeData);
                                            }
                                        }
                                    }
                                }
                            }
                            // 移除已经处理过的事项节点
                            itemCells.removeAll(usedCells);
                        }
                    }
                }
            }
        }

        /**
         * 更新保存拖拉拽视图json数据
         */
        AeaParThemeVer themeVer = new AeaParThemeVer();
        themeVer.setThemeVerId(themeVerId);
        themeVer.setThemeVerDiagram(themeVerDiagram);
        aeaParThemeVerMapper.updateOne(themeVer);
    }

    /**
     * 生成流程图思路：1.先生成阶段，
     * 2.生成事项
     * 3.合成json保存
     *
     * @param themeVerId
     * @return
     * @throws Exception
     */
    @Override
    public ResultForm createBpmnDiagram(String themeVerId) throws Exception {
        ResultForm form = new ResultForm(false);
        String realPath = servletContext.getRealPath(File.separator);
        String initBpmnDiagram = getInitBpmnDiagram("rappidWithParallelTemplate.json");
        if(initBpmnDiagram == null){
            form.setMessage("读取文件出错！");
            return form;
        }
        JSONObject bpmnDiaGram = (JSONObject) JSON.parse(initBpmnDiagram);
        logger.info("file json2:" + bpmnDiaGram.toJSONString());

        if (bpmnDiaGram.get("cells") == null || ((JSONArray) bpmnDiaGram.get("cells")).size() < 1) {
            form.setMessage("流程图模板不正确!");
            return form;
        }
        List<BscDicCodeItem> dueUnitType = bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", SecurityContext.getCurrentOrgId());
        if(dueUnitType == null || dueUnitType.size()<1){
            form.setMessage("未配置工作日数据字典!");
            return form;
        }

        Map unitMap = new HashMap(dueUnitType.size());
        for(BscDicCodeItem unit: dueUnitType){
            unitMap.put(unit.getItemCode(), unit.getItemName());
        }

        // 通过主题版本获取当前阶段数据 --> 此处暂时只处理主线
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParStage sstage = new AeaParStage();
        sstage.setThemeVerId(themeVerId);
        sstage.setRootOrgId(rootOrgId);
        sstage.setIsNode(Status.ON);
        List<AeaParStage> relStage= aeaParStageMapper.listAeaParStage(sstage);
        logger.info("before diaGramp String:"+bpmnDiaGram.toJSONString());

        AeaParTheme theme = aeaParThemeMapper.getAeaParThemeByThemeVerId(themeVerId);
        int totalStageNum = 4; //总阶段数
        int maxStageHeight = 0;    //几个并联阶段中的最高高度
        int maxParallelHeight = 0; //几个并行阶段中的最高高度

        if (relStage != null && relStage.size() > 0) {
            //并联移除, k从1开始
            int k = 1; boolean hasParallel = false;
            Map<Integer, String> poolIdMap = new HashMap<>();

            JSONArray cells = (JSONArray) bpmnDiaGram.get("cells");
            for(AeaParStage stage:relStage){
                int total = aeaParStageItemMapper.countStageItemByisOptionItem(stage.getStageId(), "1", rootOrgId);
                if(total > 0 ){ hasParallel = true; };
                removeOrAddParallelJson(total, cells, k++, poolIdMap);
            }

            //如果没有并行事项则调整阶段高度
            deleteRedundantParallelAndresizeHPoollHeightAndSetThemeName(cells, relStage.size(), totalStageNum, theme.getThemeName(), hasParallel, bpmnDiaGram);

            Map stageIdMap = removeOrAddStageJsonObj(relStage.size(), bpmnDiaGram);

            //一定先移除并联阶段和并行阶段再移除事项
            removeActivityByParentId(cells, bpmnDiaGram);
            logger.info("stageIdMap:" + JsonUtils.toJson(stageIdMap));

            int i = 1; //i从1开始
            for (AeaParStage stage : relStage) {
                // 阶段相关事项数据 ==> 查询全部 0 并联审批事项  1并行推进事项  2 前置检查事项
                // TODO: 2019/8/29 简单处理，实施事项对应的子实施事项未处理
                List<AeaParStageItem> relItems = aeaParStageItemMapper.listAeaStageItemByStageIdGroupByed(stage.getStageId(), "", rootOrgId);
                if(relItems != null && relItems.size() > 0){

                    int currentStageY = 0;
                    int currentParallelY = 0;

                    List<AeaParStageItem> stageItems = new ArrayList<>();
                    List<AeaParStageItem> parallelItems = new ArrayList<>();
//                    List<String> exeStageArr = new ArrayList<>();
//                    List<String> exeParallelArr = new ArrayList<>();
                    //分类事项
                    classifyItems(relItems, stageItems, parallelItems);

                    //并联事项
//                    if(stageItems.size() > 0){
                        //先从bpmnDiaGram json添加响应的阶段
                        handleStageJson(stage, cells, i, stageItems, rootOrgId);
                        //添加事项到json对象
                        addItemJson(stage.getStageId(), cells, stageItems, stageIdMap, i, "0", rootOrgId,currentStageY, unitMap);
//                    }

                    //并行事项
                    if(parallelItems.size() > 0){
                        handleParallelStage(stage, cells, i, parallelItems, rootOrgId);
                        addItemJson(stage.getStageId(), cells, parallelItems, poolIdMap, i, "1", rootOrgId, currentParallelY, unitMap);
                    }

                    //设置并联和并行阶段的最大高度
                    if(currentStageY != 0 && maxStageHeight < currentStageY){
                        maxStageHeight = currentStageY;
                    }
                    if(currentParallelY != 0 && maxParallelHeight < currentParallelY){
                        maxParallelHeight = currentParallelY;
                    }
                }
                i++;
            }

//            handleStageAndParallelWithHeigthAndY(cells, maxStageHeight, maxParallelHeight);

            //替换阶段名称， j从1开始
            int j = 1;
            String middleDiagram = JsonUtils.toJson(bpmnDiaGram);
            for (AeaParStage stage : relStage) {
                middleDiagram = replaceStageName(stage, bpmnDiaGram, middleDiagram, j);
                j++;
            }
            logger.info("final diaGramp String:" + middleDiagram);

//            middleDiagram = StringEscapeUtils.unescapeJava(middleDiagram);
//            middleDiagram = middleDiagram.replaceAll(" ","");
//            middleDiagram = middleDiagram.replaceAll("\\s*|\t|\r|\n","");

            //更新流程图字段
            AeaParThemeVer themeVer = new AeaParThemeVer();
            themeVer.setThemeVerId(themeVerId);
            themeVer.setThemeVerDiagram(middleDiagram);
            aeaParThemeVerMapper.updateOne(themeVer);
            form.setSuccess(true);
        }
        return form;
    }

    private void handleStageAndParallelWithHeigthAndY(JSONArray cells, int maxStageHeight, int maxParallelHeight) {
        Iterator<Object> iterator = cells.iterator();
        while (iterator.hasNext()){
            Map next = (Map) iterator.next();
            String type = (String) next.get("type");
            if(type.contains("bpmn.HPool")){
                Map sizeMap = new HashMap();
                sizeMap.put("width", ((Map) next.get("size")).get("width"));
                sizeMap.put("height", maxStageHeight - ((BigDecimal)((Map) next.get("position")).get("y")).intValue()+5);
                next.put("size", sizeMap);
            }
            if(type.equals("bpmn.Pool")){
                Map sizeMap = new HashMap();
                sizeMap.put("width", ((Map) next.get("size")).get("width"));
                sizeMap.put("height", maxStageHeight+5);
                next.put("size", sizeMap);
                //设置并行 y
                Map positionMap = new HashMap(2);
                positionMap.put("x", ((Map) next.get("position")).get("x"));
                positionMap.put("y", maxStageHeight+10);
                next.put("position", positionMap);
            }
        }
    }


    private void handleParallelStage(AeaParStage stage, JSONArray cells, int i, List<AeaParStageItem> parallelItems, String rootOrgId) {
        Iterator<Object> iterator = cells.iterator();
        while (iterator.hasNext()) {
            Map obj = (Map) iterator.next();
            String type = obj.get("type").toString();
//            JSONArray parallel = (JSONArray) obj.get("parallel");
            // 处理相应的阶段
            if (type.equals("bpmn.Pool") && obj.get("stageIndex").equals(i)) {
//            if (type.equals("bpmn.Pool") && obj.get("hPoolIds") != null && parallel.size() == 1 && Integer.valueOf(parallel.get(0).toString()).intValue() == i) {
                // 1. 先更换id
                obj.put("id", stage.getStageId()+"_"+i);
                // 2.事项id嵌入HPool
                List<String> embeds = new ArrayList<>();
                if (parallelItems != null && parallelItems.size() > 0) {
                    for (AeaParStageItem item : parallelItems) {
//                        if (item.getIsCatalog() != null && item.getIsCatalog().equals("1")) {
                            embeds.add(item.getStageItemId());
//                        }
                    }
                }
                obj.put("embeds", (embeds.toArray(new String[embeds.size()])));
            }
        }
    }

    //分类事项
    private void classifyItems(List<AeaParStageItem> relItems, List<AeaParStageItem> stageItems, List<AeaParStageItem> parallelItems) {
        for(AeaParStageItem item: relItems){
            if(item.getIsOptionItem() != null){
                if(item.getIsOptionItem().equals("0")){
                    stageItems.add(item);
                }else if(item.getIsOptionItem().equals("1")){
                    parallelItems.add(item);
                }
            }
        }
    }

    //调整阶段高度
    private void deleteRedundantParallelAndresizeHPoollHeightAndSetThemeName(JSONArray cells, int stageSize, int totalStageNum, String themeName, boolean hasParallel, JSONObject bpmnDiaGram) {
        //删除多余并行阶段
        int startSize = stageSize;

        Iterator<Object> iterator1 = cells.iterator();
        while (iterator1.hasNext()){
            Map next = (Map) iterator1.next();
            String type = next.get("type").toString();
            if(type.equals("bpmn.Text") && next.get("content") != null ){
                next.put("content", themeName);
                ((Map)next.get("lanes")).put("label", themeName);
                ((Map)((Map) next.get("attrs")).get(".content")).put("html", themeName);
                ((Map)((Map) next.get("attrs")).get(".label")).put("text", themeName);
            }
        }

        Iterator<Object> iterator = cells.iterator();
        for(;startSize < totalStageNum;startSize++) {
            while (iterator.hasNext()){
                Map next = (Map) iterator.next();
                String type = next.get("type").toString();
                if(type.equals("bpmn.Pool")){
                    JSONArray parallel = (JSONArray) next.get("parallel");
                    if(parallel != null && parallel.size() == 1 && (Integer.valueOf(parallel.get(0).toString())).equals(startSize+1)){
                        logger.info("delete pool bpmn.hpool removeid 1:"+next.get("id")+", startSize:"+(stageSize+1));
                        iterator.remove();
                        break;
                    }
                }
            }
        }

        if(!hasParallel){
            //调整阶段大小
            Iterator aiterator = cells.iterator();
            while (aiterator.hasNext()){
                Map next = (Map) aiterator.next();
                String type = next.get("type").toString();
                if(type.contains("bpmn.HPool")){
                    Map size = (Map) next.get("size");
                    size.put("height", 820);
                    next.put("size", size);
                    break;
                }
            }
        }
    }

    private void removeOrAddParallelJson(int total, JSONArray cells, int k, Map<Integer, String> poolIdMap) {
        //1.移除并行阶段
        if(total < 1){
            Iterator<Object> iterator = cells.iterator();
            while (iterator.hasNext()){
                Map next = (Map) iterator.next();
                if(next.get("type").toString().equals("bpmn.Pool")){
                    JSONArray parallel = (JSONArray) next.get("parallel");
                    if(parallel != null && parallel.size() == 1 && Integer.valueOf(parallel.get(0).toString()).equals(k)){
                        poolIdMap.put(k, next.get("id").toString());
                        logger.info("removeid 1:"+next.get("id")+", k:"+k);
                        iterator.remove();
                        break;
                    }
                }
            }
        }else{
            Iterator<Object> iterator = cells.iterator();
            while (iterator.hasNext()){
                Map next = (Map) iterator.next();
                if(next.get("type").toString().equals("bpmn.Pool")){
                    JSONArray parallel = (JSONArray) next.get("parallel");
                    if(parallel != null && parallel.size() == 1 && Integer.valueOf(parallel.get(0).toString()).equals(k)){
                        poolIdMap.put(k, next.get("id").toString());
                    }
                }
            }
        }
    }


    private void removeActivityByParentId(String parentId, JSONArray cells){
        Iterator<Object> aiterator = cells.iterator();
        while (aiterator.hasNext()){
            Map next = (Map) aiterator.next();
            if(next.get("type").toString().equals("bpmn.Activity") && next.get("parent") != null && next.get("parent").toString().equals(parentId)){
                logger.info("removeid 1:"+next.get("id"));
                aiterator.remove();
                break;
            }
        }
    }


    /**
     * 替换阶段名称
     *
     * @param stage
     * @param bpmnDiaGram
     * @param middleDiagram
     * @param j
     * @return
     */
    private String replaceStageName(AeaParStage stage, JSONObject bpmnDiaGram, String middleDiagram, int j) {
        JSONArray array = (JSONArray) bpmnDiaGram.get("cells");
        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            Map next = (Map) iterator.next();
            if (next.get("type").equals("bpmn.HPool" + j)) {
                String oldStageName = ((Map) next.get("lanes")).get("label").toString();
                middleDiagram = middleDiagram.replaceAll(oldStageName, stage.getStageName());
            }
        }
        return middleDiagram;
    }

    /**
     * 生成事项 生成事项并添加到流程图json
     * @param stageId
     * @param cells
     * @param relItems
     * @param stageIdMap
     * @param i
     * @param optionItemType 是并行阶段还是并联阶段 0：并联, 1:并行
     * @param rootOrgId
     * @param currentStageY
     * @param unitMap
     */
    private int addItemJson(String stageId, JSONArray cells, List<AeaParStageItem> relItems, Map stageIdMap, int i, String optionItemType, String rootOrgId, int currentStageY, Map unitMap) {
        Object oldStageId = stageIdMap.get(i);
        Iterator<Object> iterator = cells.iterator();

        //加入并联事项
        if(optionItemType.equals("1")){
            stageId = stageId+"_"+i;
        }

        Map activity = null;
        while (iterator.hasNext()) {
            Map next = (Map) iterator.next();
            //查找该阶段的第一个事项元素
            if (optionItemType.equals("0") && next.get("type").toString().equals("bpmn.Activity") && next.get("parent") != null && next.get("parent").toString().equals(oldStageId)) {
                activity = next;
                logger.info("acitvity hpool removeid 1:"+next.get("id")+activity.get("parent").toString());
                iterator.remove();
                break;
            }
            if (optionItemType.equals("1") && next.get("type").toString().equals("bpmn.Activity") && next.get("parent") != null && next.get("parent").toString().equals(oldStageId)) {
                activity = next;
                logger.info("acitvity pool removeid 1:"+next.get("id")+activity.get("parent").toString());
                iterator.remove();
                break;
            }
        }
        if (activity != null) {
            if (relItems != null && relItems.size() > 0) {
//                int prevY = 0;
                for (AeaParStageItem item : relItems) {
                    Map newActivity = new HashMap();
                    //只添加b标准事项， 1标准事项 0实施事项
                    if (item.getIsCatalog() != null ) {
                        newActivity.putAll(activity);
                        String dueStr = null;
                        if(item.getBjType() != null && item.getDueNum() != null){
                            dueStr = "【<span>"+item.getDueNum()+"</span>个"+(unitMap.get(item.getBjType())==null?"":unitMap.get(item.getBjType()))+"】";
                        }

                        currentStageY = builtNewActivity(newActivity, stageId, currentStageY,item.getStageItemId(),item.getItemName(),
                                item.getItemId()+"*"+item.getItemVerId(), item.getIsOptionItem(), dueStr);
                        cells.add(newActivity);
                    }
                }
            }
        }
        return currentStageY;
    }

    /**
     * 生成事项 生成事项并添加到流程图json
     * @param stageId
     * @param cells
     * @param relItems
     * @param stageIdMap
     * @param i
     * @param optionItemType 是并行阶段还是并联阶段 0：并联, 1:并行
     * @param rootOrgId
     * @param currentStageY
     * @param unitMap
     */
    private int addItemJson2(String stageId, JSONArray cells, List<AeaParStageItem> relItems, Map stageIdMap, int i, String optionItemType, String rootOrgId, int currentStageY, Map unitMap) {
        Object oldStageId = stageIdMap.get(i);
        Iterator<Object> iterator = cells.iterator();

        //加入并联事项
        if(optionItemType.equals("1")){
            stageId = stageId+"_"+i;
        }

        Map activity = null;
        while (iterator.hasNext()) {
            Map next = (Map) iterator.next();
            //查找该阶段的第一个事项元素
            if (optionItemType.equals("0") && next.get("type").toString().equals("bpmn.Activity") && next.get("parent") != null && next.get("parent").toString().equals(oldStageId)) {
                activity = next;
                logger.info("acitvity hpool removeid 1:"+next.get("id")+activity.get("parent").toString());
                iterator.remove();
                break;
            }
            if (optionItemType.equals("1") && next.get("type").toString().equals("bpmn.Activity") && next.get("parent") != null && next.get("parent").toString().equals(oldStageId)) {
                activity = next;
                logger.info("acitvity pool removeid 1:"+next.get("id")+activity.get("parent").toString());
                iterator.remove();
                break;
            }
        }
        if (activity != null) {
            if (relItems != null && relItems.size() > 0) {
//                int prevY = 0;
                for (AeaParStageItem item : relItems) {
                    Map newActivity = new HashMap();
                    //只添加b标准事项， 1标准事项 0实施事项
                    if (item.getIsCatalog() != null ) {
                        newActivity.putAll(activity);

                        String dueStr = "";
                        if(item.getBjType() != null && item.getDueNum() != null){
                            dueStr = "【<span>"+item.getDueNum()+"</span>个"+(unitMap.get(item.getBjType())==null?"":unitMap.get(item.getBjType()))+"】";
                        }

                        //设置id不重复
                        String stageItemId = item.getStageItemId();
                        currentStageY = builtNewActivity(newActivity, stageId, currentStageY,stageItemId,item.getItemName() ,
                                item.getItemId()+"*"+item.getItemVerId(), item.getIsOptionItem(),dueStr);
                        cells.add(newActivity);

                    }
                }
            }else{
                currentStageY = (int) ((Map)activity.get("position")).get("y");
            }
        }
        return currentStageY;
    }

    /**
     * 创建事项
     * @param newActivity
     * @param stageId
     * @param dueStr
     */
    private int builtNewActivity(Map newActivity, String stageId, int currentStageY, String stageItemId, String itemName, String itemVerId, String isOptionItem, String dueStr) {

        // 1.更换父id(stageId), 事项id
        newActivity.put("parent", stageId);
        newActivity.put("id", stageItemId);

        //2. 处理bpmn的属性
        newActivity.put("auEleName", itemName);
        newActivity.put("content", itemName);

        Map oldAttr = (Map) newActivity.get("attrs");
        Map attrs = new HashMap();
        attrs.putAll(oldAttr);

        // 3.先创建item属性
        Map itemMap = new HashMap();
        itemMap.put("isOptionItem", isOptionItem);
        itemMap.put("itemVerId", itemVerId);
        attrs.put("item", itemMap);

        Map htmlMap = new HashMap(1);
        String html = "<div><div>"+itemName+"</div><span >"+dueStr+"</span></div>";
        htmlMap.put("html", html);
//        htmlMap.put("html", itemName);
        attrs.put(".content", htmlMap);
        attrs.put(".title", itemName);

        //处理高度
        if(itemName == null) {itemName = "未设置事项名称";}
        double nameWidth = itemName.length() * 14;
//        int currentItemHeight = (int) ((Map)newActivity.get("size")).get("height");
        double currentItemWidth = Double.valueOf(((Map)newActivity.get("size")).get("width").toString());
        int rows = Double.valueOf(Math.ceil(nameWidth / currentItemWidth)).intValue();

        int currentItemHeight = ((int)((Map)newActivity.get("size")).get("height")*rows);  //自动调整后的高度
        if(((Integer)((Map)newActivity.get("size")).get("height")).compareTo(30) < 0){
            currentItemHeight = currentItemHeight + 10;
        }
        if(rows > 1){
            currentItemHeight = currentItemHeight-rows*15;
        }
        //高度
        Map newSizeMap = new HashMap(2);
        newSizeMap.put("height", currentItemHeight);
        newSizeMap.put("width", ((Map)newActivity.get("size")).get("width"));
        newActivity.put("size", newSizeMap);

        //y轴
        Map newPositionMap = new HashMap();

        int newheight = 0;
        if (currentStageY == 0) {
            newheight = new BigDecimal(((Map) newActivity.get("position")).get("y").toString()).intValue();
        } else {
            newheight = currentStageY ;
        }

        logger.info("newHeight:" + newheight);
        newPositionMap.put("y", newheight);
        newPositionMap.put("x", (((Map) newActivity.get("position")).get("x")));
        newActivity.put("position", newPositionMap);
        newActivity.put("attrs", attrs);
        return newheight+currentItemHeight+5; //事项div中的间距5px
    }

    public static void main(String[] args) {
        double a = 4, b=6;
        System.out.println(a/b);
        System.out.println(Double.valueOf(Math.ceil(a/b)).intValue());
    }


    // 先从bpmnDiaGram json移除或添加响应的阶段
    private void handleStageJson(AeaParStage stage, JSONArray cells, int i, List<AeaParStageItem> relItems, String rootOrgId) {

        Iterator citerator = cells.iterator();
        while (citerator.hasNext()) {
            Object jsonObj = citerator.next();
            Map obj = (Map) jsonObj;
            String type = obj.get("type").toString();
            // 处理相应的阶段
            if (type.indexOf("bpmn.HPool")>=0 && obj.get("stageIndex").equals(i)) {
                // 1. 先更换id
                obj.put("id", stage.getStageId());
                // 2.事项id嵌入HPool
                List<String> embeds = new ArrayList<>();
                if (relItems != null && relItems.size() > 0) {
                    for (AeaParStageItem item : relItems) {
//                        if(item.getIsCatalog() != null && item.getIsCatalog().equals("1")){
                            embeds.add(item.getStageItemId());
//                        }
                    }
                }
                obj.put("embeds", (embeds.toArray(new String[embeds.size()])));

                // 3.处理阶段stage属性。 阶段名称最后处理
                Map stages = new HashMap();
                stages.put("stageCode", stage.getStageCode());
                stages.put("dueNum", stage.getDueNum());
                stages.put("isNode", stage.getIsNode());
                stages.put("handWay", stage.getHandWay());
                stages.put("lcbsxlx", stage.getLcbsxlx());
                stages.put("isOptionItem", stage.getIsOptionItem());
                stages.put("isSelItem", stage.getIsSelItem());
                stages.put("isFrontCheckItem", stage.getIsCheckItem());
                stages.put("useOneForm", stage.getUseOneForm());
                stages.put("dueUnit", stage.getDueUnit());
                stages.put("dybzspjdxh", stage.getDybzspjdxh());
                stages.put("isShowItem", stage.getIsShowItem());

                String oldName =  stage.getStageName();// ((Map) obj.get("lanes")).get("label").toString();

                Map lanes = new HashMap();
                lanes.put("label", oldName);
                obj.put("lanes", lanes);
                obj.put("auEleName", oldName);

                Map attrs = (Map) obj.get("attrs");
                attrs.put("stage", stages);
                Map htmlMap = new HashMap(1);
                htmlMap.put("html", oldName);
                attrs.put(".content", htmlMap);
                obj.put("attrs", attrs);

                // 4.更换和添加中文

//                Map content = new HashMap();
//                obj.put("content", content);
            }
        }
    }


    /**
     * 移除多余的阶段
     *
     * @param size
     * @param diaGram
     */
    private Map removeOrAddStageJsonObj(int size, JSONObject diaGram) {
        JSONArray cells = (JSONArray) diaGram.get("cells");
        Iterator citerator = cells.iterator();

        if (size == 1) {
            while (citerator.hasNext()) {
                Map obj = (Map) citerator.next();
                String type = obj.get("type").toString();
                if (type.equals("bpmn.HPool2") || type.equals("bpmn.HPool3") || type.equals("bpmn.HPool4")) {
                    logger.info("acitvity bpmn.hpool removeid 1:"+obj.get("id"));
                    citerator.remove();
                }
            }
        } else if (size == 2) {
            while (citerator.hasNext()) {
                Map obj = (Map) citerator.next();
                String type = obj.get("type").toString();
                if (type.equals("bpmn.HPool3") || type.equals("bpmn.HPool4")) {
                    logger.info("acitvity bpmn.hpool removeid 1:"+obj.get("id"));
                    citerator.remove();
                }
            }
        } else if (size == 3) {
            while (citerator.hasNext()) {
                Map obj = (Map) citerator.next();
                String type = obj.get("type").toString();
                if (type.equals("bpmn.HPool4")) {
                    logger.info("acitvity bpmn.hpool removeid 1:"+obj.get("id"));
                    citerator.remove();
                }
            }
        }

        Map stageIdMap = new HashMap(size);
        Iterator iterator = cells.iterator();
        while (iterator.hasNext()) {
            Map obj = (Map) iterator.next();
            String type = obj.get("type").toString();
            if (type.equals("bpmn.HPool1")) {
                stageIdMap.put(1, obj.get("id"));
            } else if (type.equals("bpmn.HPool2")) {
                stageIdMap.put(2, obj.get("id"));
            } else if (type.equals("bpmn.HPool3")) {
                stageIdMap.put(3, obj.get("id"));
            } else if (type.equals("bpmn.HPool4")) {
                stageIdMap.put(4, obj.get("id"));
            }
        }
        return stageIdMap;
    }

    private void removeActivityByParentId(JSONArray cells, JSONObject diaGram ){
        Iterator itemIterator = cells.iterator();
        String diagramStr = JsonUtils.toJson(diaGram);
        while (itemIterator.hasNext()) {
            Map obj = (Map) itemIterator.next();
            String type = obj.get("type").toString();
            //移除阶段对应的内嵌元素 遍历当前事项
            if (type.equals("bpmn.Activity")) {
                Object parent = obj.get("parent");
                //事项id不为null以及该事项对应的parenid找不到则移除多余的事项
                logger.info("parentId:" + parent.toString());
                if (parent != null && (diagramStr.indexOf("id\":\"" + parent.toString()) < 0)) {
                    //移除没有阶段的事项
                    logger.info("stage acitvity  removeid 1:"+obj.get("id"));
                    itemIterator.remove();
                }
            }
        }
    }


    private String getInitBpmnDiagram(String rappidFilePath) throws Exception {

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 次数json文件位置需要填写正确
        Resource resource = resolver.getResource(rappidFilePath);
        if (resource != null) {
            InputStream in = resource.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int length = 0;
            byte[] buffer = new byte[1024];
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
            return out.toString();
        }
        return null;
    }

    @Override
    public List<ZtreeNode> gtreeOkThemeRelStage(String rootOrgId) {

        List<ZtreeNode> allNodes = new ArrayList<ZtreeNode>();
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("工程建设主题阶段库");
        rootNode.setType("root");
        rootNode.setOpen(true);
        rootNode.setIsParent(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        AeaParTheme theme = new AeaParTheme();
        theme.setRootOrgId(rootOrgId);
        List<AeaParTheme> themeList = aeaParThemeMapper.listAeaParTheme(theme);
        if (themeList != null && themeList.size() > 0) {
            List<AeaParStage> stageList = aeaParStageMapper.listTestOrPublishThemeVerRelStage(rootOrgId);
            for (AeaParTheme themeItem : themeList) {
                if (stageList != null && stageList.size() > 0) {
                    List<AeaParStage> needRemoveStageList = new ArrayList<>();
                    for (AeaParStage stage : stageList) {
                        if (StringUtils.isNotBlank(stage.getThemeId()) && stage.getThemeId().equals(themeItem.getThemeId())) {
                            needRemoveStageList.add(stage);
                            ZtreeNode stageNode = new ZtreeNode();
                            stageNode.setId(stage.getStageId());
                            stageNode.setName(stage.getStageName());
                            stageNode.setpId(themeItem.getThemeId());
                            stageNode.setType("stage");
                            stageNode.setOpen(true);
                            stageNode.setIsParent(false);
                            stageNode.setNocheck(false);
                            allNodes.add(stageNode);
                        }
                    }
                    if (needRemoveStageList != null && needRemoveStageList.size() > 0) {
                        ZtreeNode themeNode = new ZtreeNode();
                        themeNode.setId(themeItem.getThemeId());
                        themeNode.setName(themeItem.getThemeName());
                        themeNode.setpId("root");
                        themeNode.setType("theme");
                        themeNode.setOpen(true);
                        themeNode.setIsParent(true);
                        themeNode.setNocheck(true);
                        allNodes.add(themeNode);
                    }
                    stageList.removeAll(needRemoveStageList);
                }
            }
        }
        return allNodes;
    }


    public ResultForm createBpmnDiagram2(String themeVerId) throws Exception {
        ResultForm form = new ResultForm(false);
        String initBpmnDiagram = getInitBpmnDiagram("rappidWithParallelTemplate.json");
        if (initBpmnDiagram == null) {
            form.setMessage("读取文件出错！");
            return form;
        }
        JSONObject bpmnDiaGram = (JSONObject) JSON.parse(initBpmnDiagram);
        logger.info("file json2:" + bpmnDiaGram.toJSONString());

        if (bpmnDiaGram.get("cells") == null || ((JSONArray) bpmnDiaGram.get("cells")).size() < 1) {
            form.setMessage("流程图模板不正确!");
            return form;
        }
        List<BscDicCodeItem> dueUnitType = bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", SecurityContext.getCurrentOrgId());
        if (dueUnitType == null || dueUnitType.size() < 1) {
            form.setMessage("未配置工作日数据字典!");
            return form;
        }

        Map unitMap = new HashMap(dueUnitType.size());
        for (BscDicCodeItem unit : dueUnitType) {
            unitMap.put(unit.getItemCode(), unit.getItemName());
        }

        // 通过主题版本获取当前阶段数据 --> 此处暂时只处理主线
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParStage sstage = new AeaParStage();
        sstage.setThemeVerId(themeVerId);
        sstage.setRootOrgId(rootOrgId);
        sstage.setIsNode(Status.ON);
        List<AeaParStage> mainStages = aeaParStageMapper.listAeaParStage(sstage);
        logger.info("before diaGramp String:" + bpmnDiaGram.toJSONString());

        AeaParTheme theme = aeaParThemeMapper.getAeaParThemeByThemeVerId(themeVerId);
        String themeName = "未设置";
        if(theme != null && theme.getThemeName() != null){
            themeName = theme.getThemeName();
        }

        // -----------------------------------------------自动生成开始-----------------------------------------

        // 0 定义全局参数
        JSONArray cells = (JSONArray) bpmnDiaGram.get("cells");

        int maxStageY = 0; //所有阶段中的最高的Y
        int maxParallelStageY = 0; //所有并行阶段中的最高的Y
        Map<Integer, String> newMainStageIdMap = null;  //主线阶段id map
        Map<Integer, String> originalMainStageIdMap = new HashMap();  //主线阶段id map
        Map oldParallelStageIdMap = null; //并行阶段id map
        Map<Integer, String> oldAssistStageIdMap = new HashMap(); //辅线阶段id map


        // 1 查找主线
        if(mainStages != null && mainStages.size() > 0){
            int totalstage = mainStages.size();  //阶段数，目前支持4个，若前端已支持大于4个阶段请修改此参数
            newMainStageIdMap = new HashMap(mainStages.size());
            oldParallelStageIdMap = new HashMap(mainStages.size());
            Map<Integer, List<AeaParStageItem>> parallelItemMap = new HashMap<>();
            Map<String, Map<AeaParStage,List<AeaParStageItem>>> assistItemMap = new HashMap<>();

            // 1. 复制rappid json 阶段
            copyHPoolInCells(cells, mainStages);

            //2.删除多余主阶段、辅线、并行 以及对应的事项
            mdeleteMainAndAssistAndParallelStageAndActivity(cells,totalstage, mainStages.size(), bpmnDiaGram, originalMainStageIdMap, oldParallelStageIdMap, themeName, oldAssistStageIdMap);
            //2.1 装入存在阶段的id，
            setMainAndParallelAndAssistMap(totalstage, mainStages, newMainStageIdMap);
            //2.2查找并装载阶段下对应的 并行事项, 同时移除没有事项的并行阶段
            queryParallelItemAndRemoveParallelStage(mainStages, parallelItemMap, cells);
            //2.3查找并装载主线对应的 辅线及事项, 同时移除没有辅线的的辅线阶段
            queryAssistItemAndRemoveAssisStage(mainStages, assistItemMap, originalMainStageIdMap, cells, oldAssistStageIdMap);
            //2.4移除完所有的主线、辅线、并行阶段后，统一移除多余的事项
            //移除阶段下对应的事项
            removeActivityByParentId(cells, bpmnDiaGram);
            //3.查找主线并联事项，添加并记录当前最后一个事项的Y+height;
            int i=0;
            for(AeaParStage stage: mainStages){
                int currentHeightY = 0;
                logger.info("2stageid:"+stage.getStageId()+", i:"+i);
                List<AeaParStageItem> relItems = aeaParStageItemMapper.listAeaStageItemByStageIdGroupByed(stage.getStageId(), "0", rootOrgId);
                handleStageJson(stage, cells, i, relItems, rootOrgId);
                currentHeightY = addItemJson2(stage.getStageId(), cells, relItems, originalMainStageIdMap, i, "0", rootOrgId, currentHeightY, unitMap);
                //3.1添加辅线急辅线事项及重设起始y和辅线高度
                //3.1.查找所有辅线事项》复制辅线阶段》设置辅线阶段的初始Y为当前的Y+height, 添加辅线事项并记录当前最后高度
                currentHeightY = handleAssistStageJson(stage, cells, i, assistItemMap, oldAssistStageIdMap, currentHeightY, unitMap);

                //设置最大高度
                if(maxStageY < currentHeightY){
                    maxStageY = currentHeightY;
                }
                i++;
            }
            maxStageY = maxStageY+10;
            //5设置所有阶段的的高度为最高阶段的高度，同时查找并行阶段并设置初始y为 Y+height+间隔
            resizeMainStageHeightAndSetParallelPosition(cells, maxStageY);

            //6查找阶段并行事项，添加事项
            i = 0;
            for(AeaParStage stage: mainStages){
                int currentParallelY = maxStageY+70; //并行标题加空格高度70
                //并行事项
                logger.info("3stageid:"+stage.getStageId()+", i:"+i);
                handleParallelStage(stage, cells, i, parallelItemMap.get(i), rootOrgId);
                currentParallelY = addItemJson(stage.getStageId(), cells, parallelItemMap.get(i), oldParallelStageIdMap, i, "1", rootOrgId, currentParallelY, unitMap);
                //设置最大高度
                if(maxParallelStageY < currentParallelY){
                    maxParallelStageY = currentParallelY;
                }
                i++;
            }
            //重设 并行阶段高度
            resizeParallelHeight( cells, maxParallelStageY+10);

            //替换辅线阶段名称 暂不使用
            if(assistItemMap != null){
                replaceAssistStageName(assistItemMap,  cells);
            }

            //替换阶段名称， j从1开始
//            int j = 0;
            String middleDiagram = JsonUtils.toJson(bpmnDiaGram);
//            for (AeaParStage stage : mainStages) {
//                middleDiagram = replaceStageName(stage, bpmnDiaGram, middleDiagram, j);
//                j++;
//            }

            logger.info("final diaGramp String:" + middleDiagram);

            //更新流程图字段
            AeaParThemeVer themeVer = new AeaParThemeVer();
            themeVer.setThemeVerId(themeVerId);
            themeVer.setThemeVerDiagram(middleDiagram);
            aeaParThemeVerMapper.updateOne(themeVer);
            form.setSuccess(true);
        }

        return form;
    }

    // 复制模板 阶段1位置 2类型 3名称 4id 5 embeds 6.辅线 7并行阶段 8.与阶段、辅线、并行相关的事项 9事项：
    //事项 1 id 2位置 3父级
    private void copyHPoolInCells(JSONArray cells, List<AeaParStage> mainStages) {
//        Map hPoolIds = new HashMap();
        Iterator<Object> iterator = cells.iterator();
        Map hPool = new HashMap();
        while (iterator.hasNext()){
            Map next = (Map) iterator.next();
            if(next.get("type").toString().indexOf("bpmn.HPool") >= 0){
                next.put("stageIndex", 0);
                hPool.putAll(next);
                break;
            }
        }
        if(hPool == null){
            new Exception("模板未设置阶段，无法生成！");
        }

        Map activity = getCellsEleByTypeAndParent(hPool.get("id").toString(), "bpmn.Activity", cells);
        Map spool = getCellsEleByTypeAndParent(hPool.get("id").toString(), "bpmn.SPool", cells);
        Map spoolActivity = getCellsEleByTypeAndParent(spool.get("id").toString(), "bpmn.Activity", cells);
        hPool.put("stageIndex", 0);
        spool.put("stageIndex", 0);

        //1阶段
        //位置平移(x) 宽度+10像素间隔
        Integer traslationX = (Integer) ((Map)hPool.get("size")).get("width"); //
        for(int i=1; i<mainStages.size(); i++){
            String poolUuid = getUUID();
//            hPoolIds.put(i, poolUuid);

            Map newHPool = new HashMap();
            newHPool.putAll(hPool);
            newHPool.put("stageIndex", i);

            newHPool.put("type", "bpmn.HPool"+((i+1)>4?4:(i+1)));
            newHPool.put("id", poolUuid);
            Map position = new HashMap();
            position.put("x", (int)((Map)hPool.get("position")).get("x") + traslationX*i+10*i);
            int y = (int) ((Map)hPool.get("position")).get("y");
            position.put("y", y);
            newHPool.put("position", position);
            cells.add(newHPool);

            //hpool activity
            Map newActMap = new HashMap();
            newActMap.putAll(activity);
            String activityUUid = getUUID();
            newActMap.put("id", activityUUid);
            newActMap.put("parent", poolUuid);

            Map actPosition = new HashMap();
            actPosition.put("x", (int)((Map)activity.get("position")).get("x") + traslationX*i+10*i);
            int acty = (int) ((Map)activity.get("position")).get("y");
            actPosition.put("y", acty);
            newActMap.put("position", actPosition);
            cells.add(newActMap);

            //更新 pool embeds
            String[] arr = new String[2];
            arr[0] = activityUUid;

            //------------------------------辅线

            //辅线阶段
            String spoolUUid = getUUID();
            Map newSPool = new HashMap();
            newSPool.putAll(spool);
            newSPool.put("stageIndex", i);

            newSPool.put("id", spoolUUid);
            newSPool.put("parent",poolUuid);
            arr[1] = spoolUUid;
            newHPool.put("embeds", arr);

            Map spoolPosition = new HashMap();
            spoolPosition.put("x", (int)((Map)spool.get("position")).get("x") + traslationX*i+10*i);
            int sacty = (int) ((Map)spool.get("position")).get("y");
            spoolPosition.put("y", sacty);
            newSPool.put("position", spoolPosition);
            cells.add(newSPool);


            //spool activity
            Map newSpoolActivity = new HashMap();
            newSpoolActivity.putAll(spoolActivity);
            String spoolActivityUUid = getUUID();

            String[] spoolEmbeds = new String[1];
            spoolEmbeds[0] = spoolActivityUUid;
            newSPool.put("embeds", spoolEmbeds);

            newSpoolActivity.put("id", spoolActivityUUid);
            newSpoolActivity.put("parent", spoolUUid);

            Map spoolActPosition = new HashMap();
            spoolActPosition.put("x", (int)((Map)spoolActivity.get("position")).get("x") + traslationX*i+10*i);
            int spoolacty = (int) ((Map)spoolActivity.get("position")).get("y");
            spoolActPosition.put("y", spoolacty);
            newSpoolActivity.put("position", spoolActPosition);
            cells.add(newSpoolActivity);
        }
        //2并行
        copyParallel(cells, mainStages);


    }

    private void copyParallel(JSONArray cells, List<AeaParStage> mainStages) {
        Iterator<Object> iterator = cells.iterator();
        Map pool = new HashMap();
        while (iterator.hasNext()){
            Map next = (Map) iterator.next();
            if(next.get("type").toString().equals("bpmn.Pool")){
                pool.putAll(next);
                next.put("stageIndex", 0);
                String[] str = new String[1];
                str[0] = mainStages.get(0).getStageId();
                next.put("parallel", str);
                break;
            }
        }
        if(pool == null){
            new Exception("模板未设置并行阶段，无法生成！");
        }
        Map activity = getCellsEleByTypeAndParent(pool.get("id").toString(), "bpmn.Activity", cells);


        Integer traslationX = (Integer) ((Map)pool.get("size")).get("width"); //
        for(int i=1; i<mainStages.size(); i++){
            String poolUuid = getUUID();
            Map newPool = new HashMap();
            newPool.putAll(pool);
            newPool.put("stageIndex", i);
            String[] str = new String[1];
            str[0] = mainStages.get(i).getStageId();
            newPool.put("parallel", str);

            newPool.put("type", "bpmn.Pool");
            newPool.put("id", poolUuid);
            Map position = new HashMap();
            position.put("x", (int)((Map)pool.get("position")).get("x") + traslationX*i+10*i);
            int y = (int) ((Map)pool.get("position")).get("y");
            position.put("y", y);
            newPool.put("position", position);

//            newPool.put("hPoolIds", mainStages.get(i).getStageId());
            cells.add(newPool);

            //事项
            Map newActivity = new HashMap();
            newActivity.putAll(activity);
            String activityUUid = getUUID();
            newActivity.put("id", activityUUid);
            newActivity.put("parent", poolUuid);

            String[] relArr = new String[1];
            relArr[0] = mainStages.get(i).getStageId();
            newActivity.put("realParent", relArr);

            Map actMap = new HashMap();
            actMap.put("x", traslationX*i+10);
            actMap.put("x", (int)((Map)activity.get("position")).get("x") + traslationX*i+10*i);
            int acty = (int) ((Map)activity.get("position")).get("y");
            actMap.put("y", acty);
            newActivity.put("position", actMap);
            cells.add(newActivity);

            //更新 pool embeds
            String[] arr = new String[1];
            arr[0] = activityUUid;
            newPool.put("embeds", arr);
        }
    }

    private String getUUID(){
        return UuidUtil.generateUuid().replaceAll("-","");
    }

    private void replaceAssistStageName(Map<String, Map<AeaParStage,List<AeaParStageItem>>> assistItemMap, JSONArray cells) {
        Iterator<String> stringsIterator = assistItemMap.keySet().iterator();
        while (stringsIterator.hasNext()){
            String next = stringsIterator.next();
            Map<AeaParStage, List<AeaParStageItem>> stageListMap = assistItemMap.get(next);
            Iterator<AeaParStage> stageIterator = stageListMap.keySet().iterator();
            while (stageIterator.hasNext()){
                AeaParStage assistStage = stageIterator.next();
                Map assistMap = getCellsEleById(assistStage.getStageId(), cells);
                String name = assistStage.getStageName();
                assistMap.put("auEleName", name);

                Map lanes = new HashMap();
                lanes.put("label", name);
                assistMap.put("lanes", lanes);

                Map content = new HashMap();
                content.put("html", name);
                ((Map)assistMap.get("attrs")).put("content", content);
            }
        }
    }

    private void resizeMainStageHeightAndSetParallelPosition(JSONArray cells, int maxStageY) {
        Iterator<Object> iterator = cells.iterator();
        while (iterator.hasNext()){
            Map next = (Map) iterator.next();
            if(next.get("type") != null && next.get("type").equals("bpmn.Pool")){
                Map position = (Map) next.get("position");
                Map newPosition = new HashMap();
                newPosition.put("x", position.get("x"));
                newPosition.put("y", maxStageY+30);
                next.put("position", newPosition);
            }
            if(next.get("type") != null && next.get("type").toString().indexOf("bpmn.HPool") >= 0){
                Map size = (Map) next.get("size");
                Map newSize = new HashMap();
                newSize.put("width", size.get("width"));
                newSize.put("height", maxStageY-new BigDecimal(((Map) next.get("position")).get("y").toString()).intValue());
                next.put("size", newSize);
            }
        }
    }

    private void resizeParallelHeight(JSONArray cells, int maxStageY) {
        Iterator<Object> iterator = cells.iterator();
        while (iterator.hasNext()){
            Map next = (Map) iterator.next();
            if(next.get("type") != null && next.get("type").equals("bpmn.Pool")){
                Map size = (Map) next.get("size");
                Map newSize = new HashMap();
                newSize.put("width", size.get("width"));
                newSize.put("height", maxStageY-new BigDecimal(((Map) next.get("position")).get("y").toString()).intValue());
                next.put("size", newSize);
            }
        }
    }


    private int handleAssistStageJson(AeaParStage stage, JSONArray cells, int i, Map<String, Map<AeaParStage, List<AeaParStageItem>>> assistItemMap, Map<Integer, String> oldAssistStageIdMap, int currentHeightY, Map unitMap) {
        Map assistMap = getCellsEleById(oldAssistStageIdMap.get(i),  cells);
        if(assistMap != null){
            Map oldAssistItemMap = getCellsEleByTypeAndParent(assistMap.get("id").toString(), "bpmn.Activity",  cells);
            //移除原有的assist stage
            removeCellsEleById(assistMap.get("id").toString(), cells);
            removeCellsEleById(oldAssistItemMap.get("id").toString(), cells);

            //添加新的assist
            Map<AeaParStage, List<AeaParStageItem>> stageListMap = assistItemMap.get(stage.getStageId());
            if(stageListMap == null || stageListMap.size() <1 ){
                return currentHeightY;
            }

            Map stageCell = getCellsEleById(stage.getStageId(), cells);

            Iterator<AeaParStage> iterator = stageListMap.keySet().iterator();
            while (iterator.hasNext()){
                AeaParStage next = iterator.next();
                Map newAssistMap = new HashMap();
                newAssistMap.putAll(assistMap);
                newAssistMap.put("parent", stage.getStageId());
                //创建新的辅线阶段
                currentHeightY = addAssitStage(newAssistMap, next, cells, stageListMap.get(next), currentHeightY+20);

                String[] embeds = (String[]) stageCell.get("embeds");
                String[] newBeds = new String[embeds.length+1];
                System.arraycopy(embeds,0,newBeds,0,embeds.length);
                newBeds[embeds.length] = next.getStageId();
                stageCell.put("embeds", newBeds);

            //  添加辅线对应的事项
                currentHeightY = currentHeightY+30+5; //多加40标题高度添加辅线和事项间隔
//                AeaParStage next = iterator2.next();
                currentHeightY = addItemToJsonAssist(next.getStageId(), cells, stageListMap.get(next), oldAssistItemMap, currentHeightY, unitMap);

                //重设 辅线阶段高度
                resizeAssistHeight(next.getStageId(), cells, currentHeightY);
            }

            //添加辅线对应的事项
            /*Iterator<AeaParStage> iterator2 = stageListMap.keySet().iterator();
            while (iterator2.hasNext()){
                currentHeightY = currentHeightY+30+5; //多加40标题高度添加辅线和事项间隔
                AeaParStage next = iterator2.next();
                currentHeightY = addItemToJsonAssist(next.getStageId(), cells, stageListMap.get(next), oldAssistItemMap, currentHeightY, unitMap);
            }*/


        }
        return currentHeightY;
    }

    private void resizeAssistHeight(String stageId, JSONArray cells, int currentHeightY) {
        Map map = getCellsEleById(stageId, cells);
        if(map != null){
            Map size = (Map) map.get("size");
            BigDecimal y =  new BigDecimal(((Map)map.get("position")).get("y").toString());
            Map newsize = new HashMap();
            newsize.put("width", size.get("width"));
            newsize.put("height", currentHeightY - y.intValue());
            map.put("size", newsize);
        }
    }

    private int addItemToJsonAssist(String stageId, JSONArray cells, List<AeaParStageItem> relItems, Map oldAssistItemMap, int currentStageY, Map unitMap) {
        Map activity = oldAssistItemMap;
        if (activity != null) {
            if (relItems != null && relItems.size() > 0) {
                for (AeaParStageItem item : relItems) {
                    Map newActivity = new HashMap();
                    //只添加b标准事项， 1标准事项 0实施事项
                    if (item.getIsCatalog() != null ) {
                        newActivity.putAll(activity);

                        String dueStr = null;
                        if(item.getBjType() != null && item.getDueNum() != null){
                            dueStr = "【<span>"+item.getDueNum()+"</span>个"+(unitMap.get(item.getBjType())==null?"":unitMap.get(item.getBjType()))+"】";
                        }

                        currentStageY = builtNewActivity(newActivity, stageId, currentStageY,item.getStageItemId(),item.getItemName(),
                                item.getItemId()+"*"+item.getItemVerId(), item.getIsOptionItem(), dueStr);
                        cells.add(newActivity);
                    }
                }
            }
        }
        return currentStageY;
    }

    private int addAssitStage(Map obj, AeaParStage stage, JSONArray cells, List<AeaParStageItem> aeaParStageItems, int currentHeightY) {
        // 1. 先更换id
        obj.put("id", stage.getStageId());
        // 2.事项id嵌入HPool
        List<String> embeds = new ArrayList<>();
        if (aeaParStageItems != null && aeaParStageItems.size() > 0) {
            for (AeaParStageItem item : aeaParStageItems) {
                embeds.add(item.getStageItemId());
            }
        }
        obj.put("embeds", (embeds.toArray(new String[embeds.size()])));

        // 3.处理阶段stage属性。 阶段名称最后处理
        Map stages = new HashMap();
        stages.put("stageCode", stage.getStageCode());
        stages.put("dueNum", stage.getDueNum());
        stages.put("isNode", stage.getIsNode());
        stages.put("handWay", stage.getHandWay());
        stages.put("lcbsxlx", stage.getLcbsxlx());
        stages.put("isOptionItem", stage.getIsOptionItem());
        stages.put("isSelItem", stage.getIsSelItem());
        stages.put("isFrontCheckItem", stage.getIsCheckItem());
        stages.put("useOneForm", stage.getUseOneForm());
        stages.put("dueUnit", stage.getDueUnit());
        stages.put("dybzspjdxh", stage.getDybzspjdxh());
        stages.put("isShowItem", stage.getIsShowItem());

        Map attrs = (Map) obj.get("attrs");
        Map newAttrs = new HashMap();
        newAttrs.putAll(attrs);
        newAttrs.put("stage", stages);

        // 4.更换和添加中文
        String oldName = ((Map) obj.get("lanes")).get("label").toString();
        obj.put("auEleName", oldName);
        Map htmlMap = new HashMap(1);
        htmlMap.put("html", oldName);
        newAttrs.put(".content", htmlMap);
        obj.put("attrs", newAttrs);

        //5.设置新的起始高度Y
        Map newPositionMap = new HashMap();
        logger.info("newHeight:" + currentHeightY);
        newPositionMap.put("y", currentHeightY);
        newPositionMap.put("x", (((Map) obj.get("position")).get("x")));
        obj.put("position", newPositionMap);


        cells.add(obj);
        return currentHeightY;
    }

    @Override
    public Map getCellsEleById(String id, JSONArray cells){

        Iterator<Object> iterator = cells.iterator();
        Map ele = null;
        while (iterator.hasNext()){
            Map next = (Map) iterator.next();
            if(next.get("id").equals(id)){
                ele = next;
                break;
            }
        }
        return ele;
    }

    private void removeCellsEleById(String id, JSONArray cells){
        Iterator<Object> iterator = cells.iterator();
        while (iterator.hasNext()){
            Map next = (Map) iterator.next();
            if(next.get("id").equals(id)){
                iterator.remove();
                logger.info("remove cells ele by id:"+id);
                break;
            }
        }
    }

    private Map getCellsEleByTypeAndParent(String parentId, String type, JSONArray cells){
        Iterator<Object> iterator = cells.iterator();
        Map ele = null;
        while (iterator.hasNext()){
            Map next = (Map) iterator.next();
            Object type1 = next.get("type");
            if(type1 != null){
                String oldType = type1.toString();
                if((oldType.equals(type) || (oldType.indexOf(type)>=0)) && next.get("parent").equals(parentId)){
                    ele = next;
                    break;
                }
            }
        }
        return ele;
    }

    private void queryAssistItemAndRemoveAssisStage(List<AeaParStage> mainStages, Map<String, Map<AeaParStage, List<AeaParStageItem>>> assistItemMap, Map<Integer, String> originalMainStageIdMap, JSONArray cells, Map<Integer, String> oldAssistStageIdMap) {
        int i=0;
        for(AeaParStage stage: mainStages){
            AeaParStage query = new AeaParStage();
            query.setParentId(stage.getStageId());
            List<AeaParStage> aeaParStages = aeaParStageMapper.listAeaParStage(query);
            if(aeaParStages == null || aeaParStages.size()<1){
                removeAssitStage(cells, originalMainStageIdMap.get(i));
                i++;
                continue;
            }else{
                if(aeaParStages.size() > 0){
                    Map stageAndItemMap = new HashMap(aeaParStages.size());
                    boolean removeAssitActivity = true;
                    for(AeaParStage assistStage: aeaParStages){
                        List<AeaParStageItem> relItems = aeaParStageItemMapper.listAeaStageItemByStageIdGroupByed(assistStage.getStageId(), "", SecurityContext.getCurrentOrgId());
                        if(relItems != null ){
                            removeAssitActivity = false;
                            stageAndItemMap.put(assistStage, relItems);
                        }else{
                            stageAndItemMap.put(assistStage, null);
                        }
                    }
                    if(removeAssitActivity){
//                        removeActivityByParentId(oldAssistStageIdMap.get(i), cells);
                    }
                    assistItemMap.put(stage.getStageId(), stageAndItemMap);
                }
                i++;
            }
        }
    }

    private void removeAssitStage(JSONArray cells, String parentId) {
        //移除辅线阶段
        Iterator<Object> aiterator = cells.iterator();
        while (aiterator.hasNext()){
            Map next = (Map)aiterator.next();
            if(next.get("type").toString().equals("bpmn.SPool")){
                if(next.get("parent").toString().equals(parentId)){
                    logger.info("remove assist stage id:"+next.get("id"));
                    aiterator.remove();
                    break;
                }
            }
        }
    }

    private void queryParallelItemAndRemoveParallelStage(List<AeaParStage> mainStages, Map<Integer, List<AeaParStageItem>> parallelItemMap, JSONArray cells) {
        int i=0;
        for(AeaParStage stage: mainStages){
            List<AeaParStageItem> relItems = aeaParStageItemMapper.listAeaStageItemByStageIdGroupByed(stage.getStageId(), "1", SecurityContext.getCurrentOrgId());
            logger.info(" 1stageid:"+stage.getStageId()+", i:"+i);
            parallelItemMap.put(i, relItems);
            if(relItems == null || relItems.size() < 1){
                //移除没有的并行阶段
                removeParallelStage(cells, i);
            }
            i++;
        }
    }

    private void removeParallelStage(JSONArray cells, int i) {
        //移除并行阶段
        Iterator<Object> piterator = cells.iterator();
        while (piterator.hasNext()){
            Map next = (Map) piterator.next();
            if(next.get("type").toString().equals("bpmn.Pool")){
                    if(next.get("stageIndex").equals(i)){
                        logger.info("remove parallel stage id:"+next.get("id")+", i:"+i);
                        piterator.remove();
                        break;
                    }
            }
        }
    }

    private void setMainAndParallelAndAssistMap(int totalstage, List<AeaParStage> mainStages, Map mainStageIdMap) {
        int k=0;
        for(AeaParStage stage: mainStages){
            mainStageIdMap.put(k, stage.getStageId());
            k++;
        }
    }

    //删除多余主阶段、辅线、并行 以及对应的事项
    private void mdeleteMainAndAssistAndParallelStageAndActivity(JSONArray cells, int totalstage, int mainStageSize, JSONObject bpmnDiaGram, Map originalMainStageIdMap, Map oldParallelStageIdMap, String themeName, Map oldAssistStageIdMap) {
      /*  if(mainStageSize == totalstage){
            return;
        }*/

        Iterator<Object> iterator1 = cells.iterator();
        while (iterator1.hasNext()){
            Map next = (Map) iterator1.next();
            String type = next.get("type").toString();
            if(type.equals("bpmn.Text") && next.get("content") != null ){
                next.put("content", themeName);
                ((Map)next.get("lanes")).put("label", themeName);
                ((Map)((Map) next.get("attrs")).get(".content")).put("html", themeName);
                ((Map)((Map) next.get("attrs")).get(".label")).put("text", themeName);
            }
        }

        Iterator<Object> iterator0 = cells.iterator();
        while (iterator0.hasNext()){
            Map next = (Map) iterator0.next();
            if(next.get("type").equals("bpmn.Pool")){
//                String stageIndex = (String) next.get("stageIndex");

                oldParallelStageIdMap.put(next.get("stageIndex"), next.get("id"));
            }
            if(next.get("type").toString().indexOf("bpmn.HPool") >= 0){
//                String type = next.get("type").toString();
//                type = type.substring(type.indexOf("l")+1, type.length());
                originalMainStageIdMap.put(next.get("stageIndex"), next.get("id"));
            }

        }
        Iterator<Object> iterator2 = cells.iterator();
        while (iterator2.hasNext()){
            Map next = (Map) iterator2.next();
            if(next.get("type").toString().indexOf("bpmn.SPool") >= 0){
                String parent = next.get("parent").toString();
                if(parent != null){
                    Map ele = getCellsEleById(parent, cells);
                    if(ele != null){
//                        String type = ele.get("type").toString();
//                        type = type.substring(type.indexOf("l")+1, type.length());
                        oldAssistStageIdMap.put(next.get("stageIndex"), next.get("id"));
                    }
                }
            }
        }

        Set<String> remvoeMainStageIdSet = new HashSet<>(totalstage-mainStageSize);
        //移除主阶段
        Iterator<Object> iterator = cells.iterator();
        while (iterator.hasNext()){
            Map next = (Map)iterator.next();
            if(next.get("type").toString().indexOf("bpmn.HPool") >= 0){
                //i从下一个开始
                for(int i=mainStageSize+1; i<totalstage+1; i++){
                    if(next.get("type").toString().indexOf("bpmn.HPool")>=0 && next.get("stageIndex").equals(i)){
                        logger.info("remove main stage id:"+next.get("id"));
                        remvoeMainStageIdSet.add(next.get("id").toString());
                        iterator.remove();
                        break;
                    }
                }
            }
        }

        //移除并行阶段
        for(int i=mainStageSize+1; i<totalstage+1; i++){
            removeParallelStage(cells, i);
        }

        //移除辅线阶段
        for(String set: remvoeMainStageIdSet){
            removeAssitStage(cells, set);
        }
    }
}

