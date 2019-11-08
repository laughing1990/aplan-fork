package com.augurit.aplanmis.common.service.admin.item.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppTriggerMapper;
import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.meta.domain.MetaDbColumn;
import com.augurit.agcloud.meta.domain.MetaDbConn;
import com.augurit.agcloud.meta.domain.MetaDbTable;
import com.augurit.agcloud.meta.mapper.MetaDbColumnMapper;
import com.augurit.agcloud.meta.mapper.MetaDbConnMapper;
import com.augurit.agcloud.meta.mapper.MetaDbTableMapper;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.CommonConstant;
import com.augurit.aplanmis.common.constants.PublishStatus;
import com.augurit.aplanmis.common.convert.BscAttDetailConvert;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.helper.SyncItemInfoFromKPHelper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemCondAdminService;
import com.augurit.aplanmis.common.service.admin.opus.AplanmisOpuOmOrgAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageItemAdminService;
import com.augurit.aplanmis.common.service.admin.tpl.DgActTplAppAdminService;
import com.augurit.aplanmis.common.utils.AuthHelper;
import com.augurit.aplanmis.common.utils.DESUtil;
import com.augurit.aplanmis.common.vo.*;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author ZhangXinhui
 * @date 2019/8/2 002 17:03
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaItemAdminServiceImpl implements AeaItemAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemAdminServiceImpl.class);

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String jdbcUserName;

    @Value("${spring.datasource.password}")
    private String jdbcPwd;

    @Autowired
    AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private BscAttMapper bscAttMapper;

    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;

    @Autowired
    private AeaItemVerMapper aeaItemVerMapper;

    @Autowired
    private AeaItemSeqMapper aeaItemSeqMapper;

    @Autowired
    private AeaItemStateVerMapper aeaItemStateVerMapper;

    @Autowired
    private AeaParStageItemAdminService aeaParStageItemAdminService;

    @Autowired
    private DgActTplAppAdminService dgActTplAppAdminService;

    @Autowired
    private AeaItemMapper aeaItemMapper;

    @Autowired
    private AeaItemCondAdminService aeaItemCondAdminService;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Autowired
    private AplanmisOpuOmOrgAdminService opuOmOrgService;

    @Autowired
    private MetaDbConnMapper metaDbConnMapper;

    @Autowired
    private MetaDbTableMapper metaDbTableMapper;

    @Autowired
    private MetaDbColumnMapper metaDbColumnMapper;

    @Autowired
    private ActTplAppTriggerMapper actTplAppTriggerMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;

    @Autowired
    private AeaItemBasicAdminService aeaItemBasicAdminService;

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void syncActTplAppDefLimitTime(String appId,Long dueNum,String dueUnit){

        try {
            if(dueNum!=null) {
                int limitTime = Integer.parseInt(dueNum+"");
                String timeUnit = "d";
                String isWorkDay = "1";

                if("2".equals(dueUnit)){//自然日
                    isWorkDay = "0";
                }

                if("4".equals(dueUnit)){//分钟
                    timeUnit = "m";
                }else if("3".equals(dueUnit)){//小时
                    timeUnit = "m";
                    limitTime = limitTime * 60;
                }
                dgActTplAppAdminService.syncActTplAppDefLimitTime(appId,limitTime,timeUnit, isWorkDay);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    @Override
    public void handleMatAttachments(AeaItemMat aeaItemMat) throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();

        // 处理模板、样本、审查附件
        // 先删除
        // 模板
        List<BscAttForm> kbList = bscAttMapper.listAttLinkAndDetail("AEA_ITEM_MAT", "TEMPLATE_DOC",
                aeaItemMat.getMatId(), "KP", rootOrgId, null);
        if (kbList != null && kbList.size() > 0) {
            for (BscAttForm form : kbList) {
                String id = (form.getDetailId() + ",");
                bscAttMapper.deleteAttLinkByDetailId(form.getDetailId());
                bscAttDetailMapper.deleteBscAttDetail(form.getDetailId());
                if (StringUtils.isNotBlank(aeaItemMat.getTemplateDoc())) {
                    aeaItemMat.setTemplateDoc(aeaItemMat.getTemplateDoc().replace(id, ""));
                }
            }
        }
        // 样表
        List<BscAttForm> ybList = bscAttMapper.listAttLinkAndDetail("AEA_ITEM_MAT", "SAMPLE_DOC",
                aeaItemMat.getMatId(), "KP", rootOrgId, null);
        if (ybList != null && ybList.size() > 0) {
            for (BscAttForm form : ybList) {
                String id = (form.getDetailId() + ",");
                bscAttMapper.deleteAttLinkByDetailId(form.getDetailId());
                bscAttDetailMapper.deleteBscAttDetail(form.getDetailId());
                if (StringUtils.isNotBlank(aeaItemMat.getSampleDoc())) {
                    aeaItemMat.setSampleDoc(aeaItemMat.getSampleDoc().replace(id, ""));
                }
            }
        }
        // 审查
        List<BscAttForm> sjList = bscAttMapper.listAttLinkAndDetail("AEA_ITEM_MAT", "REVIEW_SAMPLE_DOC",
                aeaItemMat.getMatId(), "KP", rootOrgId, null);
        if (sjList != null && sjList.size() > 0) {
            for (BscAttForm form : sjList) {
                String id = (form.getDetailId() + ",");
                bscAttMapper.deleteAttLinkByDetailId(form.getDetailId());
                bscAttDetailMapper.deleteBscAttDetail(form.getDetailId());
                if (StringUtils.isNotBlank(aeaItemMat.getReviewSampleDoc())) {
                    aeaItemMat.setReviewSampleDoc(aeaItemMat.getReviewSampleDoc().replace(id, ""));
                }
            }
        }

        // 创建模板
        if (aeaItemMat.getKbslAttachList() != null && aeaItemMat.getKbslAttachList().size() > 0) {
            StringBuilder ids = new StringBuilder();
            for (AeaItemMatAttr attr : aeaItemMat.getKbslAttachList()) {

                if (StringUtils.isBlank(attr.getIdNum())) {
                    attr.setIdNum(UUID.randomUUID().toString());
                }
                BscAttDetail detail = BscAttDetailConvert.createBscAttDetail(attr);
                detail.setAttType("KP");
                bscAttDetailMapper.insertBscAttDetail(detail);
                BscAttLink link = BscAttDetailConvert.createBscAttLink("AEA_ITEM_MAT",
                        "TEMPLATE_DOC", aeaItemMat.getMatId(), detail.getDetailId());
                bscAttMapper.insertLink(link);
                ids.append(detail.getDetailId()).append(",");

            }
            aeaItemMat.setTemplateDoc((StringUtils.isBlank(aeaItemMat.getTemplateDoc()) ? "" : aeaItemMat.getTemplateDoc()) + ids);
        }
        // 创建样表附件
        if (aeaItemMat.getYbslAttachList() != null && aeaItemMat.getYbslAttachList().size() > 0) {
            StringBuilder ids = new StringBuilder();
            for (AeaItemMatAttr attr : aeaItemMat.getYbslAttachList()) {

                if (StringUtils.isBlank(attr.getIdNum())) {
                    attr.setIdNum(UUID.randomUUID().toString());
                }
                BscAttDetail detail = BscAttDetailConvert.createBscAttDetail(attr);
                detail.setAttType("KP");
                bscAttDetailMapper.insertBscAttDetail(detail);
                BscAttLink link = BscAttDetailConvert.createBscAttLink("AEA_ITEM_MAT",
                        "SAMPLE_DOC", aeaItemMat.getMatId(), detail.getDetailId());
                bscAttMapper.insertLink(link);
                ids.append(detail.getDetailId()).append(",");
            }
            aeaItemMat.setSampleDoc((StringUtils.isBlank(aeaItemMat.getSampleDoc()) ? "" : aeaItemMat.getSampleDoc()) + ids);
        }
        // 创建审查要点附件
        if (aeaItemMat.getSjydsAttachList() != null && aeaItemMat.getSjydsAttachList().size() > 0) {
            StringBuilder ids = new StringBuilder();
            for (AeaItemMatAttr attr : aeaItemMat.getSjydsAttachList()) {

                if (StringUtils.isBlank(attr.getIdNum())) {
                    attr.setIdNum(UUID.randomUUID().toString());
                }
                BscAttDetail detail = BscAttDetailConvert.createBscAttDetail(attr);
                detail.setAttType("KP");
                bscAttDetailMapper.insertBscAttDetail(detail);
                BscAttLink link = BscAttDetailConvert.createBscAttLink("AEA_ITEM_MAT",
                        "REVIEW_SAMPLE_DOC", aeaItemMat.getMatId(), detail.getDetailId());
                bscAttMapper.insertLink(link);
                ids.append(detail.getDetailId()).append(",");
            }
            aeaItemMat.setReviewSampleDoc((StringUtils.isBlank(aeaItemMat.getReviewSampleDoc()) ? "" : aeaItemMat.getReviewSampleDoc()) + ids);
        }
    }

    @Override
    public List<AeaItem> gtreeTestRunOrPublishedItem(AeaItemBasic basic){

        List<AeaItem> allNodes = new ArrayList<>();
        String title = "工程建设审批事项库";
        AeaItem rootNode = new AeaItem();
        rootNode.setId("root");
        rootNode.setName(title);
        rootNode.setType("root");
        rootNode.setOpen(true);
        rootNode.setIsParent(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        // 所有最新版本事项,没有考虑是否发布
        basic.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemBasic> allItems = aeaItemBasicMapper.listLatestOkAeaItemBasic(basic);
        if(allItems!=null&&allItems.size()>0){
            for(AeaItemBasic itemBasic : allItems){
                AeaItem item = convertItemNode(itemBasic);
                allNodes.add(item);
            }
        }
        return allNodes;
    }

    /**
     * 转换事项节点
     *
     * @param itemBasic
     * @return
     */
    private AeaItem convertItemNode(AeaItemBasic itemBasic){

        AeaItem item = new AeaItem();
        item.setItemId(itemBasic.getItemId());
        item.setItemName(itemBasic.getItemName());
        item.setDueNum(itemBasic.getDueNum().longValue());
        item.setBjType(itemBasic.getBjType());
        item.setItemVerId(itemBasic.getItemVerId());
        item.setOrgId(itemBasic.getOrgId());
        item.setId(itemBasic.getItemId());
        item.setItemBasicId(itemBasic.getItemBasicId());
        item.setName(itemBasic.getItemName());
        item.setNocheck(false);
        item.setItemCode(itemBasic.getItemCode());
        item.setOrgName(itemBasic.getOrgName());
        item.setItemNature(itemBasic.getItemNature());
        if (StringUtils.isNotBlank(itemBasic.getIsCatalog())) {
            // 标准事项
            if(itemBasic.getIsCatalog().equals(Status.ON)){
                item.setName("【标准事项】" + item.getName());
                if(StringUtils.isNotBlank(itemBasic.getGuideOrgName())){
                    item.setName(item.getName() + "【" + itemBasic.getGuideOrgName() + "】");
                }
            // 实施事项
            }else{
                item.setName("【实施事项】" + item.getName());
                if(StringUtils.isNotBlank(itemBasic.getOrgName())){
                    item.setName(item.getName() + "【" + itemBasic.getOrgName() + "】");
                }
            }
        }
        item.setOpen(true);
        item.setType("item");
        item.setIsParent(false);
        item.setpId(itemBasic.getParentItemId());
        return item;
    }

    @Override
    public void initItemVerSeq(AeaItemBasic basic){

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        basic.setRootOrgId(rootOrgId);
        List<AeaItemBasic> itemList = aeaItemBasicMapper.listLatestAeaItemBasic(basic);
        if(itemList!=null&&itemList.size()>0){
            for(AeaItemBasic item : itemList){
                String itemId = item.getItemId();
                AeaItemVer ver = new AeaItemVer();
                ver.setItemId(itemId);
                ver.setRootOrgId(rootOrgId);
                List<AeaItemVer> vers = aeaItemVerMapper.listAeaItemVer(ver);
                if(vers!=null&&vers.size()>0){
                    // 查询那些事项版本已经用到的
                    List<AeaItemVer> oldOkVerList = new ArrayList<AeaItemVer>();
                    for(AeaItemVer vo : vers){
                        List<AeaParThemeVer> themeVers = aeaParThemeVerMapper.listThemeVerByItemIdAndItemVerId(itemId, vo.getItemVerId(), rootOrgId);
                        if(themeVers!=null&&themeVers.size()>0){
                            oldOkVerList.add(vo);
                            break;
                        }
                    }
                    for(AeaItemVer vo : vers){
                        double d = vo.getVerNum();
                        String dstr = String.valueOf(d);
                        // 存在小数
                        if(dstr.indexOf(".01")>-1){
                            dstr = dstr.substring(0, dstr.indexOf(".01"));
                            d = Double.parseDouble(dstr) + 1;
                        }
                        vo.setVerNum(d);

                        // 当前自己是最大版本
                        if(item.getItemVerId().equals(vo.getItemVerId())){
                            vo.setItemVerStatus(PublishStatus.TEST_RUN.getValue());

                            //更新阶段事项关联表单的事项版本id
                            if(oldOkVerList!=null&&oldOkVerList.size()>0){
                                for(AeaItemVer oldVo : oldOkVerList){
                                    AeaParStageItem aeaParStageItem = new AeaParStageItem();
                                    aeaParStageItem.setItemId(itemId);
                                    aeaParStageItem.setItemVerId(oldVo.getItemVerId());
                                    List<AeaParStageItem> parStageItems = aeaParStageItemAdminService.listAeaParStageItem(aeaParStageItem);
                                    if(parStageItems != null && parStageItems.size() > 0){
                                        parStageItems.forEach(parStageItem -> {
                                            parStageItem.setItemVerId(vo.getItemVerId());
                                            aeaParStageItemAdminService.updateAeaParStageItem(parStageItem);
                                        });
                                    }
                                }
                            }
                        }else{
                            // 先前的版本都要过时掉
                            vo.setItemVerStatus(PublishStatus.DEPRECATED.getValue());
                        }
                        aeaItemVerMapper.updateAeaItemVer(vo);

                        // 情形版本
                        double stateVerMaxNum = 0.01;
                        AeaItemStateVer sstateVer = new AeaItemStateVer();
                        sstateVer.setItemId(itemId);
                        sstateVer.setItemVerId(vo.getItemVerId());
                        sstateVer.setRootOrgId(rootOrgId);
                        List<AeaItemStateVer> stateVerList = aeaItemStateVerMapper.listAeaItemStateVersion(sstateVer);
                        if(stateVerList!=null&&stateVerList.size()>0){
                            double nextstateVerNum = stateVerList.size();
                            int num = stateVerList.size();
                            // 未发布版本
                            AeaItemStateVer unPublishedVer = null;
                            for(AeaItemStateVer entity : stateVerList) {
                                if (entity.isUnPublishedVer()) {
                                    unPublishedVer = entity;
                                    break;
                                }
                            }
                            // 试运行或者已发布版本
                            AeaItemStateVer okVer = null;
                            for(AeaItemStateVer entity : stateVerList) {
                                if(entity.isPublishedVer()||entity.isTestRunVer()){
                                    okVer = entity;
                                    break;
                                }
                            }
                            // 存在未发布版本且存在试运行或者已发布版本
                            if(unPublishedVer!=null){
                                stateVerList.remove(unPublishedVer);
                                nextstateVerNum = num-1;
                                stateVerMaxNum = stateVerMaxNum + nextstateVerNum;
                                unPublishedVer.setVerNum(stateVerMaxNum);
                                unPublishedVer.setModifier(userId);
                                unPublishedVer.setModifyTime(new Date());
                                aeaItemStateVerMapper.updateAeaItemStateVer(unPublishedVer);
                                // 存在试运行或者已发布版本
                                if(okVer!=null){
                                    stateVerList.remove(okVer);
                                    okVer.setVerNum(nextstateVerNum);
                                    okVer.setModifier(userId);
                                    okVer.setModifyTime(new Date());
                                    aeaItemStateVerMapper.updateAeaItemStateVer(okVer);
                                    nextstateVerNum = num-1;
                                }
                                // 不存在未发布版本仅存在试运行或者已发布版本
                            }else if(okVer!=null){
                                stateVerList.remove(okVer);
                                nextstateVerNum = num-1;
                                stateVerMaxNum = num;
                                okVer.setVerNum(stateVerMaxNum);
                                okVer.setModifier(userId);
                                okVer.setModifyTime(new Date());
                                aeaItemStateVerMapper.updateAeaItemStateVer(okVer);
                            }

                            // 处理其他版本数据
                            if(stateVerList!=null&&stateVerList.size()>0){
                                for(AeaItemStateVer entity : stateVerList){
                                    entity.setVerNum(nextstateVerNum);
                                    entity.setVerStatus(PublishStatus.DEPRECATED.getValue());
                                    entity.setModifier(userId);
                                    entity.setModifyTime(new Date());
                                    aeaItemStateVerMapper.updateAeaItemStateVer(entity);
                                    nextstateVerNum--;
                                }
                            }
                        }else{
                            AuditVo auditVo = AuditVo.newOne(userId, rootOrgId);
                            AeaItemStateVer initStateVer = AeaItemStateVer.initVer(itemId, vo.getItemVerId(), auditVo);
                            aeaItemStateVerMapper.insertAeaItemStateVer(initStateVer);
                        }

                        // 更新序列
                        AeaItemSeq seq = aeaItemSeqMapper.getSeqByItemIdAndVerId(itemId, vo.getItemVerId(), rootOrgId);
                        if(seq == null){
                            AeaItemSeq aeaItemSeq = AeaItemSeq.initOne(itemId, vo.getItemVerId(), userId, rootOrgId);
                            aeaItemSeq.setItemVerMax(vo.getVerNum());
                            aeaItemSeqMapper.insertAeaItemSeq(aeaItemSeq);
                        }else{
                            seq.setItemVerMax(vo.getVerNum());
                            seq.setStateVerMax(stateVerMaxNum);
                            seq.setModifier(userId);
                            seq.setModifyTime(new Date());
                            aeaItemSeqMapper.updateAeaItemSeq(seq);
                        }
                    }
                }else{

                    // 不存在版本把版本序列也删除
                    aeaItemSeqMapper.deleteSeqByItemId(itemId, rootOrgId);

                    // 创建版本
                    AeaItemVer aeaItemVer = AeaItemVer.initOne(itemId, userId, rootOrgId);
                    aeaItemVerMapper.insertAeaItemVer(aeaItemVer);

                    // 创建序号
                    AeaItemSeq aeaItemSeq = AeaItemSeq.initOne(itemId, aeaItemVer.getItemVerId(), userId, rootOrgId);
                    aeaItemSeqMapper.insertAeaItemSeq(aeaItemSeq);

                    // 事项基本信息
                    if(StringUtils.isBlank(item.getItemBasicId())){
                        item.setItemBasicId(UuidUtil.generateUuid());
                    }
                    item.setItemVerId(aeaItemVer.getItemVerId());
                    aeaItemBasicMapper.updateAeaItemBasic(item);
                }
            }
        }
    }

    @Override
    public void initStandItem(AeaItemBasic basic){

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        // 获取顶级实施事项
        basic.setParentItemId("root");
        basic.setRootOrgId(rootOrgId);
        basic.setIsCatalog(Status.OFF);
        List<AeaItemBasic> list = aeaItemBasicMapper.listLatestAeaItemBasic(basic);
        if (list != null && list.size() > 0) {
            for (AeaItemBasic itemBasic : list) {
                 // 旧的关系
                 AeaItem oldItem = new AeaItem();
                 oldItem.setItemId(itemBasic.getItemId());
                 oldItem.setParentItemId(itemBasic.getParentItemId());
                 oldItem.setRootOrgId(itemBasic.getRootOrgId());

                 // 复制成新的标准事项
                 itemBasic.setItemBasicId(UUID.randomUUID().toString());
                 itemBasic.setOrgId(null);
                 itemBasic.setItemId(null);
                 itemBasic.setParentItemId(null);
                 itemBasic.setItemVerId(null);
                 itemBasic.setIsCatalog(Status.ON);
                 aeaItemBasicAdminService.saveAeaItemBasic(itemBasic);

                // 更新旧的顶级实施事项
                 String newItemId = itemBasic.getItemId();
                 // 新的顶级实施事项序号
                 String newExtItemSeq = CommonConstant.SEQ_SEPARATOR + newItemId + CommonConstant.SEQ_SEPARATOR + oldItem.getItemId() + CommonConstant.SEQ_SEPARATOR;
                 oldItem.setParentItemId(newItemId);
                 oldItem.setItemSeq(newExtItemSeq);
                 oldItem.setRootOrgId(rootOrgId);
                 oldItem.setModifier(userId);
                 oldItem.setModifyTime(new Date());
                 aeaItemMapper.updateAeaItem(oldItem);

                 // 处理子集序列
                 handChildItemSeq(oldItem.getItemId(), newExtItemSeq, rootOrgId, userId);
            }
        }
    }

    @Override
    public void syncItemRegion(AeaItemBasic basic){

        basic.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemBasicMapper.syncItemRegion(basic);
    }

    /**
     * 处理子集
     *
     * @param oldParentItemId
     * @param parentItemSeq
     * @param rootOrgId
     */
    private void handChildItemSeq(String oldParentItemId, String parentItemSeq, String rootOrgId, String userId){

        // 获取子级事项
        List<AeaItem> childItems = aeaItemMapper.listAllChildItem(oldParentItemId, rootOrgId);
        if (childItems != null && childItems.size() > 0) {
            for (AeaItem item : childItems) {
                String oldParentItemSeq = CommonConstant.SEQ_SEPARATOR + oldParentItemId + CommonConstant.SEQ_SEPARATOR;
                if(StringUtils.isNotBlank(item.getItemSeq())){
                    item.setItemSeq(item.getItemSeq().replaceAll(oldParentItemSeq, parentItemSeq));
                }
                item.setModifier(userId);
                item.setModifyTime(new Date());
                aeaItemMapper.updateAeaItem(item);
            }
        }
    }

    @Override
    public AeaItem getAeaItemById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取Form对象，ID为：{}", id);
            return aeaItemMapper.getAeaItemById(id);
        }
        return null;
    }


    @Override
    public ResultForm asynKpItems(String taskName, String catalogCode,
                                  String taskCode, String taskType,
                                  String deptCode, String qxfbqx,
                                  String areacode, String taskState,
                                  String isLocal, String sftzspsx, String page, String limit) throws Exception {

        String dataParam = buildAllItemDataParam(taskName, catalogCode, taskCode, taskType, deptCode,
                qxfbqx, areacode, taskState, isLocal, sftzspsx, page, limit);

        JSONObject result = SyncItemInfoFromKPHelper.getDataFromKP(SyncItemInfoFromKPHelper.GET_ALL_ITEM_LIST_API, dataParam);

        if (Objects.isNull(result)) {
            return new ResultForm(false, "请求接口'" + SyncItemInfoFromKPHelper.GET_ALL_ITEMS + "',无返回结果!");
        }

        if (result.getInteger("code") != 1) {
            return new ResultForm(false, "返回编码：" + result.getInteger("code") + ",返回信息" + result.getString("msg"));
        }

        // 返回成功
        String decryptDataStr = DESUtil.decrypt(result.getString("data"), AuthHelper.LOGIN_PWD);
        JSONObject dataListJson = JSON.parseObject(decryptDataStr);

        // 处理返回结果数据
        if (Objects.isNull(dataListJson)) {
            return new ResultForm(false, "请求接口'" + SyncItemInfoFromKPHelper.GET_ALL_ITEMS + "',数据解密无返回结果!");
        }

        // 处理返回结果数据
        if (dataListJson.getInteger("code") != 1) {
            return new ResultForm(false, "返回编码：" + dataListJson.getInteger("code") + ",返回信息" +
                    dataListJson.getString("msg"));
        }

        String dataListStr = "";
        if (dataListJson != null) {  // 数据集合
            dataListStr = dataListJson.getString("data");
            if (StringUtils.isNotBlank(dataListStr)) {
                List<AeaItemVo> list = JSONObject.parseArray(dataListStr, AeaItemVo.class);
                // 获取后台数据
                if (list != null && list.size() > 0) {
                    int count = 0;
                    for (AeaItemVo vo : list) {
                        count++;
                        System.out.println("同步第" + count + "个编码为：" + vo.getTaskCode() + "的事项数据,开始时间为：" +
                                df.format(new Date()));
//                        AeaItem item = aeaItemMapper.getAeaItemByItemCode(vo.getTaskCode());
//                        AeaItem item = aeaItemMapper.getAeaItemByKpItemId(vo.getItemId());//todo 需要修改 暂时屏蔽

                        String orgId = "0368948a-1cdf-4bf8-a828-71d796ba89f6";
                        if (StringUtils.isNotBlank(vo.getDeptName())) {
                            List<OpuOmOrg> orgList = opuOmOrgMapper.getOMOrgsByOrgName(vo.getDeptName(), null);
                            if (orgList != null && orgList.size() > 0) {
                                orgId = orgList.get(0).getOrgId();
                            }
                        }
                        /*if (item != null) { // 编辑信息
                            AeaItemConvert.useByGetAllItemList(false, item, vo);
                            item.setOrgId(orgId);
                            Long sortNo = getItemAcceptMode(item.getParentItemId());
                            item.setAcceptMode(sortNo);
                            item.setIsAsyn("1");
                            item.setAsynTime(new Date());
                            updateAeaItemUseByKp(item);
                        } else { // 新增事项
                            item = new AeaItem();
                            AeaItemConvert.useByGetAllItemList(true, item, vo);
                            item.setOrgId(orgId);
                            item.setIsAsyn("1");
                            item.setAsynTime(new Date());
                            saveAeaItemUseByKp(item);
                        }*///todo 需要修改 暂时屏蔽
                        System.out.println("同步第" + count + "个编码为：" + vo.getTaskCode() + "的事项数据,结束时间为：" +
                                df.format(new Date()));
                    }
                }
            }
        }
        return new ResultForm(true, dataListStr);
    }

    private String buildAllItemDataParam(String taskName, String catalogCode, String taskCode, String taskType, String deptCode, String qxfbqx, String areacode, String taskState, String isLocal, String sftzspsx, String page, String limit) {

        if (StringUtils.isBlank(taskName)) {
            taskName = "";
        }
        if (StringUtils.isBlank(catalogCode)) {
            catalogCode = "";
        }
        if (StringUtils.isBlank(taskCode)) {
            taskCode = "";
        }
        if (StringUtils.isBlank(taskType)) {
            taskType = "";
        }
        if (StringUtils.isBlank(deptCode)) {
            deptCode = "";
        }
        if (StringUtils.isBlank(qxfbqx)) {
            qxfbqx = "";
        }
        if (StringUtils.isBlank(areacode)) {
            areacode = "";
        }
        if (StringUtils.isBlank(taskState)) {
            taskState = "1";
        }
        if (StringUtils.isBlank(isLocal)) {
            isLocal = "";
        }
        if (StringUtils.isBlank(sftzspsx)) {
            sftzspsx = "1";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("taskName=").append(taskName)
                .append("&catalogCode=").append(catalogCode)
                .append("&taskCode=").append(taskCode)
                .append("&taskType=").append(taskType)
                .append("&deptCode=").append(deptCode)
                .append("&qxfbqx=").append(qxfbqx)
                .append("&areacode=").append(areacode)
                .append("&taskState=").append(taskState)
                .append("&isLocal=").append(isLocal)
                .append("&sftzspsx=").append(sftzspsx);
        if (StringUtils.isNotBlank(page) && StringUtils.isNotBlank(limit)) {
            sb.append("&page=").append(page)
                    .append("&limit=").append(limit);
        } else {
            sb.append("&page=1&limit=1000");
        }
        return sb.toString();
    }

    @Override
    public ActTplAppTrigger getSubTriggerById(String id) throws Exception {
        return actTplAppTriggerMapper.getActTplAppTriggerById(id);
    }

    @Override
    public List<ActTplAppTriggerAdminVo> getActTplAppTriggerByAppFlowDefIds(String appFlowDefId, String appId, String keyword) {
        List<ActTplAppTriggerAdminVo> vos = new ArrayList<>();
        try {
            List<ActTplAppTrigger> list = actTplAppTriggerMapper.getActTplAppTriggerByAppFlowDefIds(Arrays.asList(appFlowDefId), appId);
            if (CollectionUtils.isNotEmpty(list)) {
                for (ActTplAppTrigger appTrigger : list) {
                    ActTplAppTriggerAdminVo vo = new ActTplAppTriggerAdminVo();
                    BeanUtils.copyProperties(appTrigger, vo);
                    Map<String, String> map = getTriggerAppFlowdefName(vo.getAppFlowdefId(), vo.getTriggerAppFlowdefId(), appId);
                    if (map != null) {
                        String defKey = map.get("defKey");
                        ProcessDefinition node = repositoryService.createProcessDefinitionQuery().processDefinitionKey(defKey).latestVersion().singleResult();
                        if (node != null) {
                            vo.setProcName(map.get("defName"));
                            BpmnModel bpmnModel = repositoryService.getBpmnModel(node.getId());
                            Process process = bpmnModel.getProcesses().get(0);
                            Collection<FlowElement> flowElements = process.getFlowElements();

                            for (FlowElement element : flowElements) {
                                String elementId = element.getId();
                                if (StringUtils.isNotBlank(elementId) && elementId.equals(appTrigger.getTriggerElementId())) {
                                    vo.setNodeName(StringUtils.isNotBlank(element.getName()) ? element.getName() : element.getId());
                                    break;
                                }
                            }
                        }

                    }

                    if (StringUtils.isNotBlank(keyword)) {
                        if (!vo.getNodeName().contains(keyword) && !vo.getProcName().contains(keyword)) {
                            continue;
                        }
                    }

                    vos.add(vo);
                }
            }
            return vos;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取子流程配置失败!" + e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String, String> getTriggerAppFlowdefName(String flowDefId, String triggerFlowDefId, String appId) throws Exception {
        Map<String, String> map = new HashMap<>();
        List<AppProcCaseDefPlusAdminVo> list = dgActTplAppAdminService.getAppProcCaseDefVo(appId, null);
        for (AppProcCaseDefPlusAdminVo vo : list) {
            //当前流程的defKey
            if (StringUtils.isNotBlank(flowDefId) && flowDefId.equals(vo.getId())) {
                map.put("defKey", vo.getDefKey());
            }
            //子流程的defName
            if (StringUtils.isNotBlank(triggerFlowDefId) && triggerFlowDefId.equals(vo.getId())) {
                map.put("defName", vo.getDefName());
            }
        }
        return map;
    }

    @Override
    public void batchDelSubTrigger(String[] triggerIds) throws Exception {
        for (String triggerId : triggerIds) {
            actTplAppTriggerMapper.deleteActTplAppTrigger(triggerId);
        }
    }

    @Override
    public void saveOrUpdateTrigger(ActTplAppTrigger actTplAppTrigger) throws Exception {
        actTplAppTrigger.setCreater(SecurityContext.getCurrentUserName());
        actTplAppTrigger.setCreateTime(new Date());
        actTplAppTrigger.setPriorityOrder(1L);
        actTplAppTrigger.setTriggerType("1");
        //fixme xiaohutu 需要修改为itemverID
        actTplAppTrigger.setPkName("ITEM_VER_ID");
        actTplAppTrigger.setBusTableName("AEA_ITEM_BASIC");
        actTplAppTrigger.setIsDeleted("0");
        if (StringUtils.isNotBlank(actTplAppTrigger.getTriggerId())) {
            actTplAppTrigger.setModifier(SecurityContext.getCurrentUserName());
            actTplAppTrigger.setModifyTime(new Date());
            actTplAppTriggerMapper.updateActTplAppTrigger(actTplAppTrigger);
        } else {
            actTplAppTrigger.setTriggerId(UUID.randomUUID().toString());

            actTplAppTriggerMapper.insertActTplAppTrigger(actTplAppTrigger);
        }
    }

    @Override
    public boolean checkHadTrigger(ActTplAppTrigger actTplAppTrigger) throws Exception {
        if(StringUtils.isBlank(actTplAppTrigger.getTriggerId())){
            ActTplAppTrigger query = new ActTplAppTrigger();
            query.setTriggerAppFlowdefId(actTplAppTrigger.getTriggerAppFlowdefId());
            query.setAppFlowdefId(actTplAppTrigger.getAppFlowdefId());
            query.setTriggerElementId(actTplAppTrigger.getTriggerElementId());
            List<ActTplAppTrigger> actTplAppTriggers = actTplAppTriggerMapper.listActTplAppTrigger(query);
            if(actTplAppTriggers != null && actTplAppTriggers.size() > 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public void delSubTrigger(String id) throws Exception {
        actTplAppTriggerMapper.deleteActTplAppTrigger(id);
    }

    @Override
    public Collection<FlowElement> getTaskNodeList(String defKey) {
        Collection<FlowElement> flowElements = null;
        ProcessDefinition def = repositoryService.createProcessDefinitionQuery().processDefinitionKey(defKey).latestVersion().singleResult();
        if (def != null) {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(def.getId());
            Process process = bpmnModel.getProcesses().get(0);
            flowElements = process.getFlowElements();
            flowElements.removeIf(flowElement -> !(flowElement instanceof UserTask));
            flowElements.stream().sorted(new Comparator<FlowElement>() {
                @Override
                public int compare(FlowElement o1, FlowElement o2) {
                    UserTask u1 = (UserTask)o1;
                    UserTask u2 = (UserTask)o2;
                    if(StringUtils.isNotBlank(u1.getPriority()) && StringUtils.isNotBlank(u2.getPriority())){
                        return u1.getPriority().compareTo(u2.getPriority());
                    }else if(StringUtils.isNotBlank(u1.getPriority()) && StringUtils.isBlank(u2.getPriority())){
                        return 1;
                    }else if(StringUtils.isBlank(u1.getPriority()) && StringUtils.isNotBlank(u2.getPriority())){
                        return -1;
                    }else{
                        return 0;
                    }
                }
            });
        }
        return flowElements;
    }

    @Override
    public List<AeaItem> gtreeItem() {

        List<AeaItem> allNodes = new ArrayList<>();
        AeaItem rootNode = new AeaItem();
        rootNode.setId("root");
        rootNode.setName("工程建设审批事项库");
        rootNode.setType("root");
        rootNode.setOpen(true);
        rootNode.setIsParent(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        AeaItemBasic aeaItemBasic = new AeaItemBasic();
        aeaItemBasic.setParentItemId("root");
        // 所有最新版本事项,没有考虑是否发布
        List<AeaItemBasic> allItems = aeaItemBasicMapper.listLatestAeaItemBasic(new AeaItemBasic());
        if(allItems!=null&&allItems.size()>0){
            for(AeaItemBasic itemBasic : allItems){
                AeaItem item = convertItemNode(itemBasic);
                allNodes.add(item);
            }
        }
        return allNodes;
    }

    @Override
    public List<AeaItemCond> gtreeItemCond(String itemId){

        List<AeaItemCond> listNode = new ArrayList<AeaItemCond>();
        AeaItem item = aeaItemMapper.getAeaItemById(itemId);
        if(item!=null){
            AeaItemCond rootNode = new AeaItemCond();
            rootNode.setId(itemId);
            rootNode.setType("root");
            rootNode.setOpen(true);
            rootNode.setIsParent(true);
            rootNode.setNocheck(true);
            listNode.add(rootNode);
            AeaItemCond searchCond = new AeaItemCond();
            searchCond.setItemId(itemId);
            searchCond.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaItemCond> condList = aeaItemCondAdminService.listAeaItemCond(searchCond);
            if(condList!=null&&condList.size()>0){
                for(AeaItemCond cond:condList){
                    AeaItemCond condNode = new AeaItemCond();
                    condNode.setId(cond.getItemCondId());
                    String sfzz = "";
                    String muiltSelect = "";
                    if(StringUtils.isNotBlank(cond.getSfzz())&& "1".equals(cond.getSfzz())){
                        sfzz = "[终止]";
                    }
                    if(cond.getMuiltSelect()!=null&&cond.getMuiltSelect()>0){
                        muiltSelect = "["+ cond.getMuiltSelect() +"]";
                    }
                    condNode.setName(cond.getCondName()+ sfzz + muiltSelect);
                    condNode.setpId(itemId);
                    if(StringUtils.isNotBlank(cond.getParentCondId())){
                        condNode.setpId(cond.getParentCondId());
                    }
                    condNode.setType("cond");
                    condNode.setOpen(true);
                    condNode.setIsParent(true);
                    condNode.setNocheck(true);
                    listNode.add(condNode);
                }
            }
        }else {
            AeaItemCond rootNode = new AeaItemCond();
            rootNode.setId(itemId);
            rootNode.setName("事项数据为空!");
            rootNode.setType("noData");
            rootNode.setOpen(true);
            rootNode.setIsParent(true);
            rootNode.setNocheck(true);
            listNode.add(rootNode);
        }
        return listNode;
    }

    @Override
    public List<AeaItem> gtreeItemAsyncZTree(String id, String name, String type) {
        List<AeaItem> listNode = new ArrayList<>();
        if (StringUtils.isBlank(id)) {
            id = SecurityContext.getCurrentOrgId();
            OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(id);
            if (topOrg != null) {
                id = topOrg.getOrgId();
            }
            OpuOmOrg pOrg = opuOmOrgMapper.getOrg(id);
            if (pOrg != null) {
                // 添加父级节点
                AeaItem pOrgNode = new AeaItem();
                pOrgNode.setId(pOrg.getOrgId());
                pOrgNode.setName(pOrg.getOrgName());
                pOrgNode.setpId(pOrg.getParentOrgId());
                pOrgNode.setType("org");
                pOrgNode.setIconSkin("department");
                pOrgNode.setOpen(true);
                pOrgNode.setIsParent(true);
                pOrgNode.setNocheck(true);
                listNode.add(pOrgNode);
            }
        } else {
            if (StringUtils.isNotBlank(type) && "org".equals(type)) {
                // 查询下级组织
                List<OpuOmOrg> childOrgs = opuOmOrgMapper.getChildActiveOrgsByParentOrgId(id);
                // 添加子级组织节点
                if (childOrgs != null && childOrgs.size() > 0) {
                    for (OpuOmOrg childOrg : childOrgs) {
                        AeaItem childOrgNode = new AeaItem();
                        childOrgNode.setId(childOrg.getOrgId());
                        childOrgNode.setName(childOrg.getOrgName());
                        childOrgNode.setpId(id);
                        childOrgNode.setType("org");
                        childOrgNode.setIconSkin("department");
                        childOrgNode.setOpen(false);
                        childOrgNode.setIsParent(true);
                        childOrgNode.setNocheck(true);
                        // 查询下级子组织
                        List<OpuOmOrg> nchildOrgs = opuOmOrgMapper.getChildActiveOrgsByParentOrgId(childOrg.getOrgId());
                        if (nchildOrgs != null && nchildOrgs.size() > 0) {
                            childOrgNode.setIsParent(true);
                        }
                        // 查询事项
                        AeaItemBasic itemBasic = new AeaItemBasic();
                        itemBasic.setOrgId(childOrg.getOrgId());
                        List<AeaItemBasic> items = aeaItemBasicMapper.listLatestAeaItemBasic(itemBasic);
                        if (items != null && items.size() > 0) {
                            childOrgNode.setIsParent(true);
                        }
                        listNode.add(childOrgNode);
                    }
                }
            }

            if (StringUtils.isNotBlank(type)) {
                // 查询组织相关的事项
                AeaItemBasic searchItem = new AeaItemBasic();
                if ("org".equals(type)) {
                    searchItem.setOrgId(id);
                } else if ("item".equals(type)) {
                    searchItem.setParentItemId(id);
                }
                //此处获取的是事项的最新版本，没有区分是否发布
                List<AeaItemBasic> childItems = aeaItemBasicMapper.listLatestAeaItemBasic(searchItem);
                // 添加子级事项节点
                if (childItems != null && childItems.size() > 0) {
                    for (AeaItemBasic childItemBasic : childItems) {
                        if ("org".equals(type)) {
                            if (StringUtils.isNotBlank(childItemBasic.getParentItemId())) {
                                continue;
                            }
                        }
                        AeaItem childItem = new AeaItem();
                        childItem.setItemId(childItemBasic.getItemId());
                        childItem.setItemName(childItemBasic.getItemName());
                        childItem.setItemVerId(childItemBasic.getItemVerId());
                        childItem.setOrgId(childItemBasic.getOrgId());
                        childItem.setId(childItemBasic.getItemId());
                        childItem.setName(childItemBasic.getItemName());
                        childItem.setpId(id);
                        childItem.setType("item");
                        childItem.setOpen(false);
                        childItem.setNocheck(false);
                        childItem.setIsParent(false);
                        childItem.setOrgName(name);
                        AeaItemBasic nitem = new AeaItemBasic();
                        nitem.setParentItemId(childItemBasic.getItemId());
                        //此处获取的是事项的最新版本，没有区分是否发布
                        List<AeaItemBasic> nitems = aeaItemBasicMapper.listLatestAeaItemBasic(nitem);
                        if (nitems != null && nitems.size() > 0) {
                            childItem.setIsParent(true);
                        }
                        listNode.add(childItem);
                    }
                }
            }
        }
        return listNode;
    }

    @Override
    public List<ZtreeNode> gtreeTableColumnAsyncZTree(String id) {

        List<ZtreeNode> allNodes = new ArrayList<>();
        if (StringUtils.isBlank(id)) {
            MetaDbConn metaDbConn = metaDbConnMapper.getMetaDbConnByConInfo(jdbcUrl, jdbcUserName, jdbcPwd);
            if (metaDbConn != null) {
                // 获取表数据
                List<MetaDbTable> tableList = metaDbTableMapper.listMetaDbTableByConnId(metaDbConn.getConnId());
                if (tableList != null && tableList.size() > 0) {
                    for (MetaDbTable table : tableList) {
                        if (StringUtils.isNotBlank(table.getTableName()) && table.getTableName().toUpperCase().startsWith("AEA_")) {
                            // 添加表节点
                            ZtreeNode tableNode = new ZtreeNode();
                            tableNode.setId(table.getTableId());
                            tableNode.setName(table.getTableName());
                            if (StringUtils.isNotBlank(table.getTableComment())) {
                                tableNode.setName(table.getTableName() + "【" + table.getTableComment() + "】");
                            }
                            tableNode.setType("table");
                            tableNode.setOpen(true);
                            tableNode.setIsParent(true);
                            tableNode.setNocheck(true);
                            allNodes.add(tableNode);
                        }
                    }
                }
            }
        } else {
            // 获取字段数据
            List<MetaDbColumn> columnList = metaDbColumnMapper.listMetaDbTbColByTableId(id);
            if (columnList != null && columnList.size() > 0) {
                for (MetaDbColumn column : columnList) {
                    if (StringUtils.isNotBlank(column.getColumnName())) {
                        // 添加字段节点
                        ZtreeNode columnNode = new ZtreeNode();
                        columnNode.setId(column.getColumnId());
                        columnNode.setName(column.getColumnName());
                        if (StringUtils.isNotBlank(column.getColumnComment())) {
                            columnNode.setName(column.getColumnName() + "【" + column.getColumnComment() + "】");
                        }
                        columnNode.setpId(id);
                        columnNode.setType("column");
                        columnNode.setOpen(false);
                        columnNode.setIsParent(false);
                        columnNode.setNocheck(false);
                        allNodes.add(columnNode);
                    }
                }
            }
        }
        return allNodes;
    }

    @Override
    public List<ZtreeNode> gtreeTableColumnSyncZTree() {

        List<ZtreeNode> allNodes = new ArrayList<>();
        MetaDbConn metaDbConn = metaDbConnMapper.getMetaDbConnByConInfo(jdbcUrl, jdbcUserName, jdbcPwd);
        if (metaDbConn != null) {
            // 获取表数据
            List<MetaDbTable> tableList = metaDbTableMapper.listMetaDbTableByConnId(metaDbConn.getConnId());
            if (tableList != null && tableList.size() > 0) {
                for (MetaDbTable table : tableList) {
                    if (StringUtils.isNotBlank(table.getTableName()) && table.getTableName().toUpperCase().startsWith("AEA_")) {
                        // 添加表节点
                        ZtreeNode tableNode = new ZtreeNode();
                        tableNode.setId(table.getTableId());
                        tableNode.setName(table.getTableName());
                        if (StringUtils.isNotBlank(table.getTableComment())) {
                            tableNode.setName(table.getTableName() + "【" + table.getTableComment() + "】");
                        }
                        tableNode.setType("table");
                        tableNode.setOpen(true);
                        tableNode.setIsParent(true);
                        tableNode.setNocheck(true);
                        allNodes.add(tableNode);

                        // 获取字段数据
                        List<MetaDbColumn> columnList = metaDbColumnMapper.listMetaDbTbColByTableId(table.getTableId());
                        if (columnList != null && columnList.size() > 0) {
                            for (MetaDbColumn column : columnList) {
                                if (StringUtils.isNotBlank(column.getColumnName())) {
                                    // 添加字段节点
                                    ZtreeNode columnNode = new ZtreeNode();
                                    columnNode.setId(column.getColumnId());
                                    columnNode.setName(column.getColumnName());
                                    if (StringUtils.isNotBlank(column.getColumnComment())) {
                                        columnNode.setName(column.getColumnName() + "【" + column.getColumnComment() + "】");
                                    }
                                    columnNode.setpId(column.getTableId());
                                    columnNode.setType("column");
                                    columnNode.setOpen(false);
                                    columnNode.setIsParent(false);
                                    columnNode.setNocheck(false);
                                    allNodes.add(columnNode);
                                }
                            }
                        }
                    }
                }
            }
        }
        return allNodes;
    }

    @Override
    public Set<String> getItemParentIdsByItemId(String parentItemId, Set<String> sets) {
        AeaItem item = aeaItemMapper.getAeaItemById(parentItemId);
        if(item != null && item.getParentItemId() != null){
            if(!item.getParentItemId().equalsIgnoreCase("root")){
                sets.add(item.getItemId());
                getItemParentIdsByItemId(item.getParentItemId(), sets);
            }else{
                sets.add(item.getItemId());
            }
        }

        return sets;
    }
}
