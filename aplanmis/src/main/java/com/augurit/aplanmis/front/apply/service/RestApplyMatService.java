package com.augurit.aplanmis.front.apply.service;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.handler.ItemPrivilegeComputationHandler;
import com.augurit.aplanmis.common.mapper.AeaCertMapper;
import com.augurit.aplanmis.common.mapper.AeaHiCertinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMatMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageItemMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaParStateMapper;
import com.augurit.aplanmis.common.service.diagram.constant.HandleStatus;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.utils.SimFeatureUtil;
import com.augurit.aplanmis.front.apply.vo.Mat2MatInstVo;
import com.augurit.aplanmis.front.apply.vo.ParallelApplyHandleVo;
import com.augurit.aplanmis.front.apply.vo.SaveMatinstVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyHandleVo;
import com.augurit.aplanmis.front.apply.vo.attach.ApplyAbstractQueryVo;
import com.augurit.aplanmis.front.apply.vo.attach.ApplyImportListVo;
import com.augurit.aplanmis.front.apply.vo.attach.ApplyImportVo;
import com.augurit.aplanmis.front.apply.vo.attach.AttImportQueryVo;
import com.augurit.aplanmis.front.apply.vo.attach.AutoImportVo;
import com.augurit.aplanmis.front.apply.vo.attach.UploadMatReturnVo;
import com.augurit.aplanmis.front.constant.CommonConstant;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RestApplyMatService {

    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private AeaItemStateService aeaItemStateService;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaParStateMapper aeaParStateMapper;
    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaCertMapper aeaCertMapper;
    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;

    /**
     * 根据阶段ID获取root情形和材料列表---分情形
     *
     * @param stageId 阶段ID
     */
    public ParallelApplyHandleVo listStageNeedStateApplyStates(String stageId, String parentId, String projInfoId) throws Exception {
        ParallelApplyHandleVo vo = new ParallelApplyHandleVo();
        if (StringUtils.isBlank(stageId) || StringUtils.isBlank(parentId)) return vo;
        //答案实体
        ParallelApplyHandleVo.AnswerStateVo answerStateVo;
        // 父问题情形
        String parentQuestionStateId = null;
        //查询root情形下的材料
        String rootOrgId = SecurityContext.getCurrentOrgId();

        //阶段下的root情形或父情形下的父子子情形
        List<AeaParState> aeaParStates = aeaParStateMapper.listParStateByParentStateId(stageId, parentId, rootOrgId);

        //阶段下或情形下的材料
        List<AeaItemMat> itemMatList;
        if ("ROOT".equalsIgnoreCase(parentId)) {
            itemMatList = aeaItemMatService.getMatListByStageId(stageId, "1");
        } else {
            parentQuestionStateId = aeaParStateMapper.getAeaParStateById(parentId).getParentStateId();
            String[] ids = {parentId};
            itemMatList = aeaItemMatService.getMatListByStageStateIds(ids);
        }
        //转换材料数据
        List<ParallelApplyHandleVo.MatVo> rootMats = new ArrayList<>();
        for (AeaItemMat aeaItemMat : itemMatList) {
            //情形下材料
            addReplyIdentifier(aeaItemMat);
            ParallelApplyHandleVo.MatVo matVo = new ParallelApplyHandleVo.MatVo();
            BeanUtils.copyProperties(aeaItemMat, matVo);
            rootMats.add(matVo);
        }
        vo.setStateMats(rootMats);
        List<ParallelApplyHandleVo.QuestionStateVo> questionStateVos = new ArrayList<>();

        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
        AeaServiceWindow currentUserWindow = Optional.ofNullable(aeaServiceWindowService.getCurrentUserWindow()).orElse(new AeaServiceWindow());

        // 查询阶段下事项已办信息
        Map<String, HandleStatus> itemHandleStatusMap = aeaHiIteminstService.queryItemStatusByStageIdAndProjInfoId(Collections.singletonList(stageId), projInfoId, rootOrgId);
        for (AeaParState state : aeaParStates) {
            List<ParallelApplyHandleVo.AnswerStateVo> answerStateVos = new ArrayList<>();

            ParallelApplyHandleVo.QuestionStateVo questionStateVo = new ParallelApplyHandleVo.QuestionStateVo();
            BeanUtils.copyProperties(state, questionStateVo);
            //查询questionStateVo下的材料
            String[] quesStateId = {questionStateVo.getParStateId()};
            List<AeaItemMat> quesStateMats = aeaItemMatService.getMatListByStageStateIds(quesStateId);
            List<ParallelApplyHandleVo.MatVo> stateMats1 = new ArrayList<>();
            for (AeaItemMat mat : quesStateMats) {
                //情形下材料
                addReplyIdentifier(mat);
                ParallelApplyHandleVo.MatVo matVo = new ParallelApplyHandleVo.MatVo();
                BeanUtils.copyProperties(mat, matVo);
                stateMats1.add(matVo);
            }
            List<ParallelApplyHandleVo.MatVo> questionMats = new ArrayList<>();
            questionMats.addAll(stateMats1);
            questionStateVo.setStateMats(questionMats);
            questionStateVo.setParentQuestionStateId(parentQuestionStateId);
            List<AeaParState> answerStates = state.getAnswerStates();
            if (answerStates.size() > 0) {
                for (AeaParState anstate : answerStates) {
                    answerStateVo = new ParallelApplyHandleVo.AnswerStateVo();
                    //查询该答案下的事项
                    List<ItemPrivilegeComputationHandler.ComputedItem> stateParallelItems = new ArrayList<>();
                    List<ItemPrivilegeComputationHandler.ComputedItem> stateOptionItems = new ArrayList<>();
                    List<AeaItemBasic> aeaItemBasics = aeaItemBasicService.listAeaParStateItemByStateId(anstate.getParStateId());

                    List<ItemPrivilegeComputationHandler.ComputedItem> computedItems = new ItemPrivilegeComputationHandler(currentUserWindow, projInfoId, SecurityContext.getCurrentOrgId(), aeaParStage, aeaItemBasics, false)
                            .compute();

                    for (ItemPrivilegeComputationHandler.ComputedItem computedItem : computedItems) {
                        computedItem.setParStateId(anstate.getParStateId());
                        //当前事项是否已办
                        if (StringUtils.isNotBlank(projInfoId)) {
                            HandleStatus handleStatus = itemHandleStatusMap.get(computedItem.getItemId());
                            if (handleStatus != null) {
                                computedItem.setIsDone(handleStatus.getValue());
                            } else {
                                computedItem.setIsDone(HandleStatus.UN_FINISHED.getValue());
                            }
                        }

                        //是否推荐---是否推荐 0 不推荐 1推荐：未办理【isDone=0】且耗时超过10个工作日的 同上
                        // 判断是并联审批事项还是并行推进事项
                        if (isParallelItem(stageId, computedItem)) {
                            stateParallelItems.add(computedItem);
                        } else {
                            stateOptionItems.add(computedItem);
                        }
                    }
                    // 前置事项检查
                    stateOptionItems.forEach(optionItem -> {
                        if (optionItem.getCurrentCarryOutItem() != null) {
                            ItemPrivilegeComputationHandler.CarryOutItem currentCarryOutItem = optionItem.getCurrentCarryOutItem();
                            currentCarryOutItem.setPreItemCheckPassed(aeaItemBasicService.checkPreItemsPassed(currentCarryOutItem.getItemVerId(), projInfoId).isPassed());
                        }
                    });

                    BeanUtils.copyProperties(anstate, answerStateVo);

                    answerStateVo.setStateParallelItems(stateParallelItems);
                    answerStateVo.setStateOptionItems(stateOptionItems);

                    answerStateVos.add(answerStateVo);
                }
                questionStateVo.setAnswerStates(answerStateVos);
            }
            questionStateVos.add(questionStateVo);
        }
        vo.setQuestionStates(questionStateVos);

        return vo;
    }

    /**
     * 根据stageId和itemVerId判断事项是并联审批事项还是并行推进事项
     *
     * @param stageId 阶段id
     * @param itemVo  事项vo
     * @return true: 是, false: 否
     */
    private boolean isParallelItem(String stageId, ItemPrivilegeComputationHandler.ComputedItem itemVo) {
        AeaParStageItem params = new AeaParStageItem();
        params.setStageId(stageId);
        params.setItemVerId(itemVo.getItemVerId());
        List<AeaParStageItem> aeaParStageItems = aeaParStageItemMapper.listAeaParStageItem(params);
        if (aeaParStageItems.size() == 1) {
            // 设置sortNo
            itemVo.setSortNo(aeaParStageItems.get(0).getSortNo().toString());
            if ("0".equals(aeaParStageItems.get(0).getIsOptionItem())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 给文件类型是批复文件的在其文件名后加上批复标识
     *
     * @param itemMat
     */
    private void addReplyIdentifier(AeaItemMat itemMat) {
        if ("1".equals(itemMat.getIsOfficialDoc())) {
            itemMat.setMatName(itemMat.getMatName() + "(批复)");
        }
    }

    /**
     * 单项不分情形材料
     *
     * @param itemVerId 事项版本id
     */
    public SeriesApplyHandleVo listSeriesNostateApplyMats(String itemVerId) throws Exception {
        SeriesApplyHandleVo vo = new SeriesApplyHandleVo();
        List<SeriesApplyHandleVo.MatVo> stateMats = new ArrayList<>();
        String[] itemVerIds = {itemVerId};
        List<AeaItemMat> matList = aeaItemMatService.getMatListByItemVerIds(itemVerIds, "1", null);
        for (AeaItemMat mat : matList) {
            addReplyIdentifier(mat);
            SeriesApplyHandleVo.MatVo matVo = new SeriesApplyHandleVo.MatVo();
            BeanUtils.copyProperties(mat, matVo);
            stateMats.add(matVo);
        }
        vo.setStateMats(stateMats);
        return vo;
    }

    /**
     * 阶段不分情形阶段下材料
     *
     * @return ParallelApplyHandleVo.List<MatVo> stateMats;
     */
    public ParallelApplyHandleVo listStageNoStateApplyStates(String stageId, List<String> itemVerIds) throws Exception {
        ParallelApplyHandleVo vo = new ParallelApplyHandleVo();
        List<AeaItemMat> itemMatList = aeaItemMatService.listMatListByStageId(stageId, itemVerIds);
        List<ParallelApplyHandleVo.MatVo> stateMats = new ArrayList<>();
        for (AeaItemMat itemMat : itemMatList) {
            ParallelApplyHandleVo.MatVo matVo = new ParallelApplyHandleVo.MatVo();
            BeanUtils.copyProperties(itemMat, matVo);
            stateMats.add(matVo);
        }
        vo.setStateMats(stateMats);
        return vo;

    }

    @Autowired
    private AeaItemStateMapper aeaItemStateMapper;

    /**
     * 单项分情形 情形及材料
     */
    public SeriesApplyHandleVo listSeriesNeedStateApplyMats(String itemVerId, String parentId) throws Exception {
        SeriesApplyHandleVo vo = new SeriesApplyHandleVo();
        String[] itemVerIds = {itemVerId};
        //vo属性问答列表--questionStates
        List<SeriesApplyHandleVo.QuestionStateVo> questionStates = new ArrayList<>();
        //vo属性及QuestionStateVo属性：情形列表--stateMats---
        List<SeriesApplyHandleVo.MatVo> stateMats = new ArrayList<>();

        List<AeaItemMat> matList;

        // 父情形
        AeaItemState aeaItemStateById = aeaItemStateMapper.getAeaItemStateById(parentId);
        List<AeaItemState> aeaItemStates = aeaItemStateService.listAeaItemStateByParentId(itemVerId, null, parentId, SecurityContext.getCurrentOrgId());

        //先获取parentId材料
        if ("root".equalsIgnoreCase(parentId)) {
            //root材料
            matList = aeaItemMatService.getMatListByItemVerIds(itemVerIds, "1", "1");
        } else {
            //选择的情形材料
            String[] stateIds = {parentId};
            matList = aeaItemMatService.getMatListByItemStateIds(stateIds);
        }
        //处理材料
        for (AeaItemMat mat : matList) {
            addReplyIdentifier(mat);
            SeriesApplyHandleVo.MatVo matVo = new SeriesApplyHandleVo.MatVo();
            BeanUtils.copyProperties(mat, matVo);
            stateMats.add(matVo);
        }
        //赋值材料
        vo.setStateMats(stateMats);
        //处理情形
        SeriesApplyHandleVo.QuestionStateVo questionStateVo;
        SeriesApplyHandleVo.AnswerStateVo answerStateVo;
        for (AeaItemState itemState : aeaItemStates) {
            //QuestionStateVo属性----List<SeriesApplyHandleVo.AnswerStateVo> answerStates;
            List<SeriesApplyHandleVo.AnswerStateVo> answerStates = new ArrayList<>();
            questionStateVo = new SeriesApplyHandleVo.QuestionStateVo();
            BeanUtils.copyProperties(itemState, questionStateVo);
            if (null != aeaItemStateById) {
                questionStateVo.setParentQuestionStateId(aeaItemStateById.getParentStateId());
            }
            List<AeaItemState> answerStates1 = itemState.getAnswerStates();
            for (AeaItemState state : answerStates1) {
                answerStateVo = new SeriesApplyHandleVo.AnswerStateVo();
                BeanUtils.copyProperties(state, answerStateVo);
                answerStates.add(answerStateVo);
            }
            questionStates.add(questionStateVo);
        }
        //赋值
        vo.setQuestionStates(questionStates);

        return vo;
    }

    /**
     * 获取历史上传材料
     *
     * @param attImportQueryVo 查询条件
     * @param page             分页参数
     * @return List<ApplyImportListVo>
     * @throws Exception e
     */
    public List<ApplyImportListVo> getImportList(AttImportQueryVo attImportQueryVo, Page page) throws Exception {
        List<ApplyImportListVo> importList = new ArrayList<>();
        String[] unitInfoIds = attImportQueryVo.getUnitInfoId().split(",");
        String[] matCodes = attImportQueryVo.getMatCode().split(",");
        List<AeaHiItemMatinst> aeaHiItemMatinsts = new ArrayList();

        for (String matcode : matCodes) {
            aeaHiItemMatinsts.addAll(aeaHiItemMatinstMapper.listUnitAttMatinst(matcode, unitInfoIds, null));
        }

        if (aeaHiItemMatinsts != null && aeaHiItemMatinsts.size() > 0) {
            String[] recordIds = new String[aeaHiItemMatinsts.size()];
            for (int i = 0; i < aeaHiItemMatinsts.size(); i++) {
                recordIds[i] = aeaHiItemMatinsts.get(i).getMatinstId();
            }
            String[] excludeIds = null;
            if (StringUtils.isNotBlank(attImportQueryVo.getMatinstId())) {
                excludeIds = new String[1];
                excludeIds[0] = attImportQueryVo.getMatinstId();
            }
            PageHelper.startPage(page);
            List<BscAttDetail> list = aeaHiItemMatinstMapper.getAeaHiItemMatinstFile("AEA_HI_ITEM_MATINST", "MATINST_ID", attImportQueryVo.getKeyword(), recordIds, excludeIds);
            for (BscAttDetail detail : list) {
                String detailId = detail.getDetailId();
                String attName = detail.getAttName();
                ApplyImportListVo vo = new ApplyImportListVo(detailId, attName, detail.getAttFormat());
                importList.add(vo);
            }
        }

        return importList;
    }

    /**
     * 获取已上传的电子材料
     *
     * @param _matCodes    材料编码，多个时用逗号隔开
     * @param projInfoId 项目ID
     * @return
     * @throws Exception
     */
    public Map getHistoryAttMatList(String _matCodes, String projInfoId) throws Exception {

        String[] matCodes = _matCodes.split(",");
        List<AeaHiItemMatinst> aeaHiItemMatinsts = null;

        Map attMatList = new HashMap();
        List<ApplyImportListVo> attFileList = null;

        for (String matcode : matCodes) {

            aeaHiItemMatinsts = aeaHiItemMatinstMapper.listProjAeaHiItemMatinst(new String[]{matcode}, new String[]{projInfoId}, null);

            if (aeaHiItemMatinsts.size() > 0) {

                attFileList = new ArrayList();
                AeaHiItemMatinst matinst = aeaHiItemMatinsts.get(0);

                AeaHiItemMatinst copyMatinst = new AeaHiItemMatinst();// 复制电子材料对象
                BeanUtils.copyProperties(matinst, copyMatinst);
                copyMatinst.setMatinstId(UUID.randomUUID().toString());
                copyMatinst.setCreater(SecurityContext.getCurrentUserId());
                copyMatinst.setCreateTime(new Date());
                copyMatinst.setModifier(null);
                copyMatinst.setModifyTime(null);
                aeaHiItemMatinstMapper.insertAeaHiItemMatinst(copyMatinst);

                List<BscAttDetail> list = aeaHiItemMatinstMapper.getAeaHiItemMatinstFile("AEA_HI_ITEM_MATINST", "MATINST_ID", null, new String[]{matinst.getMatinstId()}, null);

                for (BscAttDetail attDetail : list) {

                    String detailId = attDetail.getDetailId();
                    String attName = attDetail.getAttName();
                    ApplyImportListVo vo = new ApplyImportListVo(detailId, attName, attDetail.getAttFormat());
                    attFileList.add(vo);

                    //复制电子件关联关系
                    BscAttLink attLink = new BscAttLink();
                    attLink.setLinkId(UUID.randomUUID().toString());
                    attLink.setTableName("AEA_HI_ITEM_MATINST");
                    attLink.setPkName("MATINST_ID");
                    attLink.setRecordId(copyMatinst.getMatinstId());
                    attLink.setDetailId(detailId);
                    bscAttMapper.insertLink(attLink);
                }

                if (attFileList.size() > 0) {
                    Map obj = new HashMap();
                    obj.put("matinstId", copyMatinst.getMatinstId());
                    obj.put("FileList", attFileList);
                    attMatList.put(matcode, obj);
                }
            }
        }

        return attMatList.size() > 0 ? attMatList : null;
    }


    /**
     * 导入历史材料
     *
     * @param matinstId 材料实例id
     * @param fileIds   detailId
     */
    public void importMatInstFile(String matinstId, String fileIds) throws Exception {
        String[] ids = fileIds.split(",");
        if (ids.length > 0) {
            for (String id : ids) {
                BscAttLink bscAttLink = new BscAttLink();
                bscAttLink.setDetailId(id);
                bscAttLink.setRecordId(matinstId);
                bscAttLink.setTableName("AEA_HI_ITEM_MATINST");
                bscAttLink.setPkName("MATINST_ID");
                bscAttLink.setLinkId(UUID.randomUUID().toString());
                bscAttMapper.insertLink(bscAttLink);
            }
            List<BscAttFileAndDir> bscAttFileAndDirs = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_ITEM_MATINST", "MATINST_ID", new String[]{matinstId});
            AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);
            aeaHiItemMatinst.setAttCount((long) bscAttFileAndDirs.size());
            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);
        }
    }

    public String importAtt(ApplyImportVo applyImportVo) {
        try {
            String matinstId = applyImportVo.getMatinstId();
            String fileIds = applyImportVo.getFileIds();
            AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
            if (StringUtils.isNotBlank(applyImportVo.getMatinstId())) {
                aeaHiItemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);
            }
            if (StringUtils.isBlank(aeaHiItemMatinst.getMatinstId())) {
                String matId = applyImportVo.getMatId();
                AeaItemMat aeaItemMat = aeaItemMatMapper.getAeaItemMatById(matId);
                aeaHiItemMatinst.setMatinstId(UUID.randomUUID().toString());
                aeaHiItemMatinst.setMatId(applyImportVo.getMatId());
                aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
                aeaHiItemMatinst.setMatinstName(aeaItemMat.getMatName());
                aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserName());
                aeaHiItemMatinst.setCreateTime(new Date());
                aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());

                aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);
            }
            this.importMatInstFile(aeaHiItemMatinst.getMatinstId(), fileIds);
            return aeaHiItemMatinst.getMatinstId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Import matinst file failed. ", e);
        }
    }

    /**
     * 一键提取功能
     */
    public List<UploadMatReturnVo> getAeaHiItemMatinstFile(ApplyAbstractQueryVo applyAbstractQueryVo) throws Exception {
        String[] matCodes = applyAbstractQueryVo.getMatCodes().split(",");
        String[] projInfoIds = applyAbstractQueryVo.getProjInfoIds().split(",");
        String unitInfoIds = applyAbstractQueryVo.getUnitInfoIds();
        String[] unitIds = null;
        if (StringUtils.isNotBlank(unitInfoIds)) {
            unitIds = unitInfoIds.split(",");
        }
        //可能有父子项目，所以需要先查出所有的父子项目ID，然后在查询材料
        List<String> projectIds = new ArrayList<>(Arrays.asList(projInfoIds));
        for (String projId : projInfoIds) {
            //查询项目树 aeaProjInfoSer
            List<AeaProjInfo> listProjZtreeNodeMysql = aeaProjInfoService.getListProjZtreeNodeMysql(projId);
            if (listProjZtreeNodeMysql.size() > 0) {
                Set<String> projArray = listProjZtreeNodeMysql.stream().map(AeaProjInfo::getProjInfoId).collect(Collectors.toSet());
                projectIds.addAll(projArray);
            }
        }
        projInfoIds = projectIds.toArray(new String[0]);
        //---包括历史上传的和本次上传还未发起申报的数据
        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.listProjAeaHiItemMatinst(matCodes, projInfoIds, unitIds);
        //fixme ----待定是否需要过滤掉当前申报页已上传的文件

        /*if (aeaHiItemMatinsts != null && aeaHiItemMatinsts.size() > 0) {
            String[] recordIds = new String[aeaHiItemMatinsts.size()];
            for (int i = 0; i < aeaHiItemMatinsts.size(); i++) {
                recordIds[i] = aeaHiItemMatinsts.get(i).getMatinstId();
            }
            String[] excludeIds = null;
            if (null != matinstIds) {
                excludeIds = matinstIds.split(",");
            }
            List<BscAttDetail> fileList = aeaHiItemMatinstMapper.getAeaHiItemMatinstFile("AEA_HI_ITEM_MATINST", "MATINST_ID", null, recordIds, excludeIds);

        }*/
        return getUploadMatReturnVos(aeaHiItemMatinsts);
    }


    /**
     * 一件分拣功能
     */
    public List<UploadMatReturnVo> saveFilesAuto(AutoImportVo autoImportVo, HttpServletRequest request) throws Exception {
        String matIdtemp = autoImportVo.getMatIds();
        String projInfoId = autoImportVo.getProjInfoId();
        String unitInfoId = autoImportVo.getUnitInfoId();
        String matinstId = autoImportVo.getMatinstIds();
        Assert.notNull(matIdtemp, "matIds 不能为空");
        Assert.notNull(projInfoId, "projInfoId 不能为空");
        String[] matIds = matIdtemp.split(",");
        String[] matinstIds = matinstId.split(",");
        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.listAeaHiItemMatinstByIds(matinstIds);

        List<AeaItemMat> itemMatList = aeaItemMatMapper.listAeaItemMatByIds(matIds);
        List<AeaHiItemMatinst> returnList = new ArrayList<>();
        // 防止重复
        Set<String> matinstIdSet = new HashSet<>();
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<String> fileNames = new ArrayList<>();
        List<MultipartFile> files = req.getFiles("file");
        //获取文件根据文件名，与材料定义匹配新建对应的材料实例保存，如果存在材料实例跟新attcount字段每次+1
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            boolean flag1 = true;
            for (AeaItemMat aeaItemMat : itemMatList) {
                //判断当前材料是否已经存在材料实例
                for (int i = 0; i < aeaHiItemMatinsts.size(); i++) {
                    AeaHiItemMatinst itemMatinst = aeaHiItemMatinsts.get(i);
                    if (itemMatinst.getMatId().equals(aeaItemMat.getMatId())) {
                        if (!matinstIdSet.contains(itemMatinst.getMatinstId())) {
                            returnList.add(itemMatinst);
                            matinstIdSet.add(itemMatinst.getMatinstId());
                        }
                    }
                }
                double length;
                AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
                //只勾选了一个材料不用名称匹配
                if (itemMatList.size() == 1) {
                    length = 1;
                } else {
                    //ie下上传文件名带真实路径
                    if (filename.lastIndexOf(File.separator) != -1) {
                        filename = filename.substring(filename.lastIndexOf(File.separator) + 1);
                    }
                    String[] filenames = StringUtils.split(filename, ".");
                    //如果材料实例名称包含材料定义名称则直接通过匹配  ---- 2019.7.22 czh
                    length = StringUtils.isNotBlank(filenames[0]) ? (filenames[0].trim().indexOf(aeaItemMat.getMatName().trim()) != -1 ? 1 : SimFeatureUtil.sim(filenames[0], aeaItemMat.getMatName())) : 0;
                }
                //大于0.8则存入数据库，否则把没有存入的返回给前端
                if (length > 0.8) {
                    boolean flag = true;
                    flag1 = false;
                    for (AeaHiItemMatinst temp : returnList) {
                        if (temp.getMatId().equals(aeaItemMat.getMatId())) {
                            flag = false;
                            aeaHiItemMatinst = temp;
                            aeaHiItemMatinst.setAttCount(aeaHiItemMatinst.getAttCount() + 1);
                            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);
                        }
                    }
                    if (flag) {
                        aeaHiItemMatinst.setMatId(aeaItemMat.getMatId());
                        aeaHiItemMatinst.setProjInfoId(projInfoId);
                        aeaHiItemMatinst.setUnitInfoId(unitInfoId);
                        aeaHiItemMatinst.setMatinstId(UUID.randomUUID().toString());
                        aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
                        aeaHiItemMatinst.setMatinstName(aeaItemMat.getMatName());
                        aeaHiItemMatinst.setAttCount(1L);
                        aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
                        aeaHiItemMatinst.setCreateTime(new Date());
                        aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());

                        aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);
                        returnList.add(aeaHiItemMatinst);
                    }
                    List<MultipartFile> multipartFiles = new ArrayList<>();
                    multipartFiles.add(file);
                    fileUtilsService.uploadAttachments("AEA_HI_ITEM_MATINST", "MATINST_ID", aeaHiItemMatinst.getMatinstId(), multipartFiles);
                }
            }

            if (flag1) {
                fileNames.add(filename);
            }
        }

        return getUploadMatReturnVos(returnList);
    }

    private List<UploadMatReturnVo> getUploadMatReturnVos(List<AeaHiItemMatinst> returnList) throws Exception {
        List<UploadMatReturnVo> list = new ArrayList<>();
        for (AeaHiItemMatinst matinst : returnList) {
            String matId = matinst.getMatId();
            String matinstId = matinst.getMatinstId();
            List<BscAttFileAndDir> matAttDetailByMatinstId = fileUtilsService.getMatAttDetailByMatinstId(matinstId);
            UploadMatReturnVo vo = new UploadMatReturnVo(matId, matAttDetailByMatinstId, matinstId);
            list.add(vo);
        }
        return list;
    }

    public void delelteAttFile(String detailIds, String matinstId) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(detailIds), "detailIds is null");
        Assert.isTrue(StringUtils.isNotBlank(matinstId), "matinstId is null");

        String[] _detailIds = detailIds.split(",");
        //判断该附件是否存在共享，如果存在共享，则删除link表关联关系，否则返回不存在共享的附件ID
        List<String> detailIds_ = this.getOnlyOneRecord(_detailIds, matinstId);
        fileUtilsService.deleteAttachments(detailIds_.toArray(new String[detailIds_.size()]));

        //查询材料实例是否还有附件
        List<BscAttFileAndDir> matAttDetail = fileUtilsService.getMatAttDetailByMatinstId(matinstId);
        int size = matAttDetail.size();
        if (size == 0) {
            //删除材料实例，输入输出实例
            aeaHiItemInoutinstMapper.deleteAeaHiItemInoutinstByMatinstId(matinstId);
            aeaHiItemMatinstService.deleteAeaHiItemMatinstById(matinstId);

        } else {
            //更新matinst电子件数量
            AeaHiItemMatinst aeaHiItemMatinstById = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);
            aeaHiItemMatinstById.setAttCount((long) size);
            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinstById);
        }
    }

    public void deleteMatinst(String matinstId) {
        Assert.isTrue(StringUtils.isNotBlank(matinstId), "matinstId is null");

        String[] matinstIds = matinstId.split(CommonConstant.COMMA_SEPARATOR);
        for (String id : matinstIds) {
            try {
                aeaHiItemMatinstService.deleteAeaHiItemMatinstById(id);
                //XIAOHUTU 需要同步删除已上传的附件
                List<BscAttFileAndDir> matAttDetailByMatinstId = fileUtilsService.getMatAttDetailByMatinstId(id);
                if (matAttDetailByMatinstId.size() > 0) {
                    String[] recordIds = matAttDetailByMatinstId.stream().map(BscAttFileAndDir::getFileId).toArray(String[]::new);

                    //判断该附件是否存在共享，如果存在共享，则删除link表关联关系，否则返回不存在共享的附件ID
                    List<String> detailIds_ = this.getOnlyOneRecord(recordIds, matinstId);
                    fileUtilsService.deleteAttachments(detailIds_.toArray(new String[detailIds_.size()]));
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Delete aea_item_matinst by matinstId failed, matinstId: {}", id);
            }
        }
    }

    //判断该附件是否存在共享，如果存在共享，则只删除LINK关联表
    private List<String> getOnlyOneRecord(String[] recordIds, String matinstId) throws Exception {

        List<String> detailIds_ = new ArrayList();

        for (int i = 0; i < recordIds.length; i++) {
            BscAttLink attLink = new BscAttLink();
            attLink.setDetailId(recordIds[i]);
            attLink.setPkName("MATINST_ID");
            List<BscAttLink> attLinks = bscAttMapper.listBscAttLink(attLink);
            if (attLinks.size() > 1) {
                for (BscAttLink bscAttLink : attLinks) {
                    if (bscAttLink.getRecordId().equals(matinstId))
                        bscAttMapper.deleteAttLinkBylinkId(bscAttLink.getLinkId());
                }
            } else {
                detailIds_.add(recordIds[i]);
            }
        }

        return detailIds_;
    }

    public List<Mat2MatInstVo> saveMatinsts(SaveMatinstVo saveMatinstVo) {
        Assert.isTrue(saveMatinstVo.getMatCountVos().size() > 0, "matCountVos is empty");

        // matId与材料份数的对应关系
        Map<String, SaveMatinstVo.MatCountVo> matCountMap = saveMatinstVo.buildMatCountMap();

        String projectInfoId = saveMatinstVo.getProjInfoId();
        String unitInfoId = saveMatinstVo.getUnitInfoId();
        List<AeaHiItemMatinst> matinsts = new ArrayList<>();
        List<AeaHiCertinst> certinsts = new ArrayList<>();
        List<Mat2MatInstVo> mat2MatInstVos = new ArrayList<>();
        final String currentOrgId = SecurityContext.getCurrentOrgId();
        aeaItemMatMapper.listAeaItemMatByIds(matCountMap.keySet().toArray(new String[0]))
                .forEach(mat -> {
                    List<String> matinstIds = new ArrayList<>();

                    // 普通材料
                    // 这里将 null作为if条件主要是为了与旧数据兼容
                    if (StringUtils.isBlank(mat.getMatProp()) || "m".equals(mat.getMatProp())) {
                        // 纸质件不为0
                        int paperCnt = matCountMap.get(mat.getMatId()).getPaperCnt();
                        int copyCnt = matCountMap.get(mat.getMatId()).getCopyCnt();

                        if (paperCnt > 0) {
                            AeaHiItemMatinst aeaHiItemMatinst = mat2Matinst(mat, unitInfoId, projectInfoId, currentOrgId);
                            aeaHiItemMatinst.setRealPaperCount((long) paperCnt);
                            matinsts.add(aeaHiItemMatinst);
                            // 返回关联关系
                            matinstIds.add(aeaHiItemMatinst.getMatinstId());
                        }
                        // 复印件不为0
                        if (copyCnt > 0) {
                            AeaHiItemMatinst aeaHiItemMatinst = mat2Matinst(mat, unitInfoId, projectInfoId, currentOrgId);
                            aeaHiItemMatinst.setRealCopyCount((long) copyCnt);
                            matinsts.add(aeaHiItemMatinst);
                            matinstIds.add(aeaHiItemMatinst.getMatinstId());
                        }
                        if (matinstIds.size() > 0) {
                            mat2MatInstVos.add(new Mat2MatInstVo(mat.getMatId(), matinstIds));
                        }
                    }
                    // 证照
                    else if ("c".equals(mat.getMatProp())) {
                        AeaHiItemMatinst aeaHiItemMatinst = mat2Matinst(mat, unitInfoId, projectInfoId, currentOrgId);
                        matinstIds.add(aeaHiItemMatinst.getMatinstId());
                        AeaCert aeaCert = aeaCertMapper.getAeaCertById(mat.getCertId(), currentOrgId);
                        AeaHiCertinst aeaHiCertinst = cert2Certinst(aeaCert, unitInfoId, projectInfoId, currentOrgId);
                        aeaHiItemMatinst.setCertinstId(aeaHiCertinst.getCertinstId());
                        certinsts.add(aeaHiCertinst);
                        matinstIds.add(aeaHiItemMatinst.getMatinstId());
                        mat2MatInstVos.add(new Mat2MatInstVo(mat.getMatId(), matinstIds));
                    }
                    // 表单
                    else if ("f".equals(mat.getMatProp())) {
                        // todo
                        /*AeaHiItemMatinst aeaHiItemMatinst = mat2Matinst(mat, unitInfoId, projectInfoId, currentOrgId);
                        matinstIds.add(aeaHiItemMatinst.getMatinstId());*/
                    }
                });

        if (certinsts.size() > 0) {
            log.info("batch insert certinst, size: {}", certinsts.size());
            aeaHiCertinstMapper.batchInsertAeaHiCertinst(certinsts);
        }

        if (matinsts.size() > 0) {
            try {
                aeaHiItemMatinstMapper.batchInsertAeaHiItemMatinst(matinsts);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Batch save matinst error", e);
            }
        }
        return mat2MatInstVos;
    }

    private AeaHiItemMatinst mat2Matinst(AeaItemMat aeaItemMat, String unitInfoId, String projInfoId, String rootOrgId) {
        AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
        aeaHiItemMatinst.setMatinstId(UuidUtil.generateUuid());
        aeaHiItemMatinst.setMatinstName(aeaItemMat.getMatName());
        aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
        aeaHiItemMatinst.setCreateTime(new Date());
        aeaHiItemMatinst.setMatId(aeaItemMat.getMatId());
        aeaHiItemMatinst.setUnitInfoId(unitInfoId);
        aeaHiItemMatinst.setProjInfoId(projInfoId);
        aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
        aeaHiItemMatinst.setMatProp(aeaItemMat.getMatProp());
        aeaHiItemMatinst.setRootOrgId(rootOrgId);
        return aeaHiItemMatinst;
    }

    private AeaHiCertinst cert2Certinst(AeaCert aeaCert, String unitInfoId, String projInfoId, String rootOrgId) {
        AeaHiCertinst aeaHiCertinst = new AeaHiCertinst();
        aeaHiCertinst.setCertinstId(UuidUtil.generateUuid());
        aeaHiCertinst.setCertId(aeaCert.getCertId());
        aeaHiCertinst.setUnitInfoId(aeaCert.getUnitInfoId());
        aeaHiCertinst.setProjInfoId(projInfoId);
        aeaHiCertinst.setAttLinkId(unitInfoId);
        aeaHiCertinst.setCreater(SecurityContext.getCurrentUserId());
        aeaHiCertinst.setCreateTime(new Date());
        aeaHiCertinst.setCertinstCode(aeaCert.getCertCode());
        aeaHiCertinst.setCertinstName(aeaCert.getCertName());
        aeaHiCertinst.setRootOrgId(rootOrgId);
        aeaHiCertinst.setMemo(aeaCert.getCertMemo());
        return aeaHiCertinst;
    }

    public List<AeaItemMat> getOfficeMatsByStageItemVerIds(String stageId, String itemVerIds) throws Exception {

        String[] itemVerIdsArr = itemVerIds.split(",");
        if (StringUtils.isBlank(stageId) || StringUtils.isBlank(itemVerIds) || itemVerIdsArr.length < 1)
            return new ArrayList();

        List<AeaItemMat> mats = aeaItemMatService.getOfficeMatsByStageItemVerIds(stageId, itemVerIdsArr);
        return mats.stream()
                .filter(CommonTools.distinctByKey(AeaItemMat::getMatId))
                .collect(Collectors.toList());
    }

    public List<AeaItemMat> getMatsByStageIdAndItemVerIds(String stageId, String itemVerIds) {
        String[] itemVerIdsArr = itemVerIds.split(",");
        if (StringUtils.isBlank(stageId) || StringUtils.isBlank(itemVerIds) || itemVerIdsArr.length < 1) {
            return new ArrayList<>();
        }

        List<AeaItemMat> mats = aeaItemMatMapper.getParInMatsByStageItemVerIds(stageId, itemVerIdsArr, SecurityContext.getCurrentOrgId());
        return mats.stream()
                .filter(CommonTools.distinctByKey(AeaItemMat::getMatId))
                .collect(Collectors.toList());
    }
}
