package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppTriggerMapper;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.InOutStatus;
import com.augurit.aplanmis.common.constants.NeedStateStatus;
import com.augurit.aplanmis.common.constants.PublishStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.qo.state.AeaItemStateVersionQo;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主题定义版本表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaItemVerAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemVerAdminService.class);

    @Autowired
    private AeaItemMapper aeaItemMapper;

    @Autowired
    private AeaItemVerMapper aeaItemVerMapper;

    @Autowired
    private AeaItemSeqMapper aeaItemSeqMapper;

    @Autowired
    private AeaItemBasicAdminService aeaItemBasicService;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaItemStateAdminService aeaItemStateAdminService;

    @Autowired
    private AeaItemStateVersionAdminService aeaItemStateVersionAdminService;

    @Autowired
    private AeaItemGuideMapper aeaItemGuideMapper;

    @Autowired
    private AeaItemGuideChargesMapper aeaItemGuideChargesMapper;

    @Autowired
    private AeaItemGuideDepartmentsMapper aeaItemGuideDepartmentsMapper;

    @Autowired
    private AeaItemGuideExtendMapper aeaItemGuideExtendMapper;

    @Autowired
    private AeaItemGuideQuestionsMapper aeaItemGuideQuestionsMapper;

    @Autowired
    private AeaItemGuideSpecialsMapper aeaItemGuideSpecialsMapper;

    @Autowired
    private AeaItemCondMapper aeaItemCondMapper;

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    @Autowired
    private AeaItemStateVerMapper aeaItemStateVersionMapper;

    @Autowired
    private ActTplAppTriggerMapper actTplAppTriggerMapper;

    @Autowired
    private AeaItemServiceBasicMapper aeaItemServiceBasicMapper;

    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;

    @Autowired
    private AeaParThemeSeqMapper aeaParThemeSeqMapper;

    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;

    @Autowired
    private AeaParThemeVerAdminService aeaParThemeVerAdminService;

    @Autowired
    private AeaServiceWindowItemMapper aeaServiceWindowItemMapper;

    @Autowired
    private AeaItemOneformMapper itemOneformMapper;

    @Autowired
    private AeaItemPartformMapper itemPartformMapper;

    @Autowired
    private AeaItemFrontItemMapper frontItemMapper;

    @Autowired
    private AeaItemFrontPartformMapper frontPartformMapper;

    @Autowired
    private AeaItemFrontProjMapper frontProjMapper;

    @Autowired
    private AeaParFrontItemMapper parFrontItemMapper;

    public AeaItemVer initAeaItemVer(String itemId, String isCatalog, String userId, String rootOrgId) {

        Assert.notNull(itemId, "itemId不能为空");

        // 创建版本
        AeaItemVer aeaItemVer = AeaItemVer.initOne(itemId, userId, rootOrgId);
        if(StringUtils.isNotBlank(isCatalog)&&isCatalog.equals(Status.ON)){
            aeaItemVer.setVerNum(1.0D);
            aeaItemVer.setGdsVerNum(1.0D);
            aeaItemVer.setItemVerStatus(PublishStatus.TEST_RUN.getValue());
        }
        aeaItemVerMapper.insertAeaItemVer(aeaItemVer);

        // 创建序号
        AeaItemSeq aeaItemSeq = AeaItemSeq.initOne(itemId, aeaItemVer.getItemVerId(), userId, rootOrgId);
        // jjt修改-1909301032
        if(StringUtils.isNotBlank(isCatalog)&&isCatalog.equals(Status.ON)){
            aeaItemSeq.setItemVerMax(1.0D);
        }
        aeaItemSeq.setStateVerMax(1.0D);
        aeaItemSeqMapper.insertAeaItemSeq(aeaItemSeq);
        return aeaItemVer;
    }

    public void updateAeaItemVer(AeaItemVer aeaItemVer) {

        aeaItemVer.setModifier(SecurityContext.getCurrentUserId());
        aeaItemVer.setModifyTime(new Date());
        aeaItemVerMapper.updateAeaItemVer(aeaItemVer);
    }

    public void deleteAeaItemVerById(String id) {

        if(StringUtils.isNotBlank(id)){
            aeaItemVerMapper.deleteAeaItemVer(id);
        }
    }

    public PageInfo<AeaItemVer> listAeaItemVer(AeaItemVer aeaItemVer, Page page) {

        aeaItemVer.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemVer> list = aeaItemVerMapper.listAeaItemVerWithItemBasic(aeaItemVer);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemVer>(list);
    }

    public AeaItemVer getAeaItemVerById(String id) {

        if(StringUtils.isNotBlank(id)){
            logger.debug("根据ID获取Form对象，ID为：{}", id);
            return aeaItemVerMapper.getAeaItemVerWithItemBasicById(id);
        }
        return null;
    }

    public List<AeaItemVer> listAeaItemVer(AeaItemVer aeaItemVer) {

        aeaItemVer.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemVer> list = aeaItemVerMapper.listAeaItemVerWithItemBasic(aeaItemVer);
        logger.debug("成功执行查询list！！");
        return list;
    }

    public AeaItemVer getMaxNumActiveVerByItemId(String itemId, String rootOrgId){

        if(StringUtils.isNotBlank(itemId)){
            AeaItemVer sVer = new AeaItemVer();
            sVer.setItemId(itemId);
            sVer.setRootOrgId(rootOrgId);
            //已发布版本
            sVer.setItemVerStatus(PublishStatus.PUBLISHED.getValue());
            sVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            List<AeaItemVer> verList = aeaItemVerMapper.listAeaItemVerWithItemBasic(sVer);
            if(verList!=null&&verList.size()>0){
                return verList.get(0);
            }
        }
        return null;
    }

    public String copyMaxPublishedOrTestRunVer(String itemId){

        if(StringUtils.isBlank(itemId)){
            throw new IllegalArgumentException("事项itemId为空!");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaItemVer> vers = aeaItemVerMapper.listTestRunOrPublishedItemVer(itemId, rootOrgId);
        AeaItemVer lastVer;
        if(vers!=null&&vers.size()>0){
            lastVer = vers.get(0);
        }else{
            throw new IllegalArgumentException("无法获取最新试运行或已发布版本!");
        }
        return copyItemVerRelData(itemId, lastVer.getItemVerId(), rootOrgId);
    }

    /**
     * 复制事项版本下的数据，正常情况下都是数据都是正常，而且都是复制试运行或者已发布版本
     *
     * @param itemId
     * @param itemVerId
     * @return
     */
    public String copyItemVerRelData(String itemId, String itemVerId, String rootOrgId){

        if(StringUtils.isBlank(itemId)){
            throw new IllegalArgumentException("事项itemId为空!");
        }
        if(StringUtils.isBlank(itemVerId)){
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }

        String userId = SecurityContext.getCurrentUserId();
        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        // 需要复制的事项版本序列
        AeaItemSeq lastSeq = null;
        List<AeaItemSeq> itemSeqList = aeaItemSeqMapper.listSeqByItemId(itemId, rootOrgId);
        if(itemSeqList!=null && itemSeqList.size()>0){
            lastSeq = itemSeqList.get(0);
        }
        Assert.notNull(lastSeq,"无法获取当前'itemVerId="+ itemVerId +"'事项版本序号!");

        // 生成最大版本号
        Double itemVerNum = lastSeq.getItemVerMax() + AeaItemStateVer.INIT_VER;
        // 最新版本
        AeaItemVer newItemVer = AeaItemVer.initOne(itemId, userId, rootOrgId);
        String newItemVerId = newItemVer.getItemVerId();
        newItemVer.setVerNum(itemVerNum);
        aeaItemVerMapper.insertAeaItemVer(newItemVer);

        // 最新序列
        AeaItemSeq newSeq = AeaItemSeq.initOne(itemId, newItemVerId, userId, rootOrgId);
        newSeq.setItemVerMax(itemVerNum);
        newSeq.setStateVerMax(lastSeq.getStateVerMax());
        aeaItemSeqMapper.insertAeaItemSeq(newSeq);

        // 1、复制事项基本信息
        copyItemBasic(true, true, itemVerId, newItemVerId, rootOrgId);

        // 2、复制事项操作指南
        copyItemGuide(itemVerId, newItemVerId, rootOrgId);

        // 3、复制事项前置条件
        copyItemCond(null, itemVerId, newItemVerId, rootOrgId);

        // 4、复制事项版本对应的情形思维导图
        copyItemVerStateRelData(itemVerId, newItemVerId, rootOrgId, null);

        // 5、复制事项版本下不分非情形下数据
        copyItemVerNoStateIns(itemVerId, newItemVerId, rootOrgId);

        // 6、复制事项版本下输出材料数据
        copyItemVerOuts(itemVerId, newItemVerId, rootOrgId);

        // 7、复制事项一张表单
        copyItemVerOneform(itemVerId, newItemVerId, rootOrgId);

        // 8、复制事项前置检查事项数据
        copyItemFrontCheck(itemVerId, newItemVerId, rootOrgId);

        return newItemVerId;
    }

    public void copyAndCreateNew(String itemId, String itemVerId, String rootOrgId){

        if(StringUtils.isBlank(itemId)){
            throw new IllegalArgumentException("事项itemId为空!");
        }
        if(StringUtils.isBlank(itemVerId)){
            throw new IllegalArgumentException("事项版本itemVerId为空!");
        }

        String userId = SecurityContext.getCurrentUserId();
        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }

        // 创建新的事项树结构
        AeaItem item = aeaItemMapper.getAeaItemById(itemId);
        if(item==null){
            throw new IllegalArgumentException("无法通过itemId获取到事项树结构数据!");
        }
        String newItemId = UUID.randomUUID().toString();
        String parentId = item.getParentItemId();
        String itemSeq = aeaItemBasicService.getItemSeq(newItemId, parentId);
        item.setItemSeq(itemSeq);
        item.setItemId(newItemId);
        aeaItemMapper.insertAeaItem(item);

        // 需要复制的事项版本序列
        AeaItemSeq lastSeq = null;
        List<AeaItemSeq> itemSeqList = aeaItemSeqMapper.listSeqByItemId(itemId, rootOrgId);
        if(itemSeqList!=null && itemSeqList.size()>0){
            lastSeq = itemSeqList.get(0);
        }
        Assert.notNull(lastSeq,"无法获取当前'itemVerId="+ itemVerId +"'事项版本序号!");

        // 生成最大版本号
        Double itemVerNum = AeaItemVer.initVer;
        // 最新版本
        AeaItemVer newItemVer = AeaItemVer.initOne(newItemId, userId, rootOrgId);
        String newItemVerId = newItemVer.getItemVerId();
        newItemVer.setVerNum(itemVerNum);
        aeaItemVerMapper.insertAeaItemVer(newItemVer);

        // 最新序列
        AeaItemSeq newSeq = AeaItemSeq.initOne(newItemId, newItemVerId, userId, rootOrgId);
        newSeq.setItemVerMax(itemVerNum);
        newSeq.setStateVerMax(lastSeq.getStateVerMax());
        aeaItemSeqMapper.insertAeaItemSeq(newSeq);

        // 1、复制事项基本信息
        AeaItemBasic basic = aeaItemBasicMapper.getAeaItemBasicByItemVerId2(itemVerId, rootOrgId);
        if(basic!=null){
            // 14个字符
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String dateStr = df.format(new Date());
            String reg = "[0-9]{4}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}[0-9]{1,2}[0-9]{1,5}";
            String itemName = basic.getItemName();
            String itemCode = basic.getItemCode();
            // 事项名称
            Pattern pattern = Pattern.compile("【"+ reg +"复制】");
            Matcher matcher = pattern.matcher(itemName);
            while(matcher.find()){
                itemName = itemName.replaceAll(matcher.group(),"");
            }
            // 事项编号
            pattern = Pattern.compile("【"+ reg +"】");
            matcher = pattern.matcher(itemCode);
            while(matcher.find()){
                itemCode = itemCode.replaceAll(matcher.group(),"");
            }
            basic.setItemName(itemName+"【"+ dateStr +"复制】");
            basic.setItemCode(itemCode+"【"+ dateStr +"】");
            basic.setAppId(aeaItemBasicService.createAppIdByItemName(basic.getItemName()));
            basic.setItemBasicId(UuidUtil.generateUuid());
            basic.setItemId(newItemId);
            basic.setItemVerId(newItemVerId);
            basic.setRootOrgId(rootOrgId);
            String itemCategoryMark = basic.getItemCategoryMark();
            if(StringUtils.isNotBlank(parentId)&&"root".equals(parentId)){
                pattern = Pattern.compile(reg);
                matcher = pattern.matcher(itemCategoryMark);
                while(matcher.find()){
                    itemCategoryMark = itemCategoryMark.replaceAll(matcher.group(),"");
                }
                basic.setItemCategoryMark(itemCategoryMark + dateStr);
            }
            basic.setCreater(SecurityContext.getCurrentUserId());
            basic.setCreateTime(new Date());
            aeaItemBasicMapper.insertAeaItemBasic(basic);
        }

        // 2、复制事项操作指南
        copyItemGuide(itemVerId, newItemVerId, rootOrgId);

        // 3、复制事项前置条件
        copyItemCond(newItemId, itemVerId, newItemVerId, rootOrgId);

        // 4、复制事项版本对应的情形思维导图
        copyItemVerStateRelData(itemVerId, newItemVerId, rootOrgId, newItemId);

        // 5、复制事项版本下不分非情形下数据
        copyItemVerNoStateIns(itemVerId, newItemVerId, rootOrgId);

        // 6、复制事项版本下输出材料数据
        copyItemVerOuts(itemVerId, newItemVerId, rootOrgId);

        // 7、复制事项一张表单
        copyItemVerOneform(itemVerId, newItemVerId, rootOrgId);

        // 8、复制事项前置检查事项数据
        copyItemFrontCheck(itemVerId, newItemVerId, rootOrgId);
    }

    /**
     * 复制事项版本情形相关数据
     *
     * @param itemVerId
     * @param newItemVerId
     * @param rootOrgId
     *
     */
    private void copyItemVerStateRelData(String itemVerId, String newItemVerId, String rootOrgId, String newItemId){

        AeaItemStateVersionQo qo = AeaItemStateVersionQo.createQuery().itemVerIdEq(itemVerId).rootOrgIdEq(rootOrgId);
        List<AeaItemStateVer> stateVerList = aeaItemStateVersionMapper.listAeaItemStateVersionByQueryCriteria(qo);
        if(stateVerList!=null && stateVerList.size()>0){
            for(AeaItemStateVer stateVer : stateVerList){
                aeaItemStateAdminService.copyItemStateVerRelData(itemVerId, newItemVerId, stateVer.getItemStateVerId(), newItemId);
            }
        }
    }

    /**
     * 复制事项版本下输出材料数据
     *
     * @param itemVerId
     * @param newItemVerId
     *
     */
    private void copyItemVerOuts(String itemVerId, String newItemVerId, String rootOrgId){

        AeaItemInout out = new AeaItemInout();
        out.setItemVerId(itemVerId);
        out.setRootOrgId(rootOrgId);
        out.setIsStateIn(NeedStateStatus.NOT_NEED_STATE.getValue());
        out.setIsInput(InOutStatus.OUT.getValue());
        out.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        List<AeaItemInout> outList = aeaItemInoutMapper.listAeaItemInout(out);
        if (outList != null && outList.size() > 0) {
            for (AeaItemInout ouvo : outList) {
                AeaItemInout newOut = ouvo.copyOne();
                newOut.setItemVerId(newItemVerId);
                aeaItemInoutMapper.insertAeaItemInout(newOut);
            }
        }
    }

    /**
     * 复制事项版本下不分非情形下数据
     *
     * @param itemVerId
     * @param newItemVerId
     *
     */
    private void copyItemVerNoStateIns(String itemVerId, String newItemVerId, String rootOrgId){

        AeaItemInout in = new AeaItemInout();
        in.setRootOrgId(rootOrgId);
        in.setItemVerId(itemVerId);
        in.setIsStateIn(NeedStateStatus.NOT_NEED_STATE.getValue());
        in.setIsInput(InOutStatus.IN.getValue());
        in.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        List<AeaItemInout> inList = aeaItemInoutMapper.listAeaItemInout(in);
        if (inList != null && inList.size() > 0) {
            for (AeaItemInout invo : inList) {
                AeaItemInout newIn = invo.copyOne();
                newIn.setItemVerId(newItemVerId);
                aeaItemInoutMapper.insertAeaItemInout(newIn);
            }
        }
    }

    /**
     * 复制事项基本信息
     *
     * @param isCreate
     * @param isNewVer
     * @param oldItemVerId
     * @param newItemVerId
     */
    private AeaItemBasic copyItemBasic(boolean isCreate, boolean isNewVer, String oldItemVerId, String newItemVerId, String rootOrgId){


        AeaItemBasic basic = aeaItemBasicMapper.getAeaItemBasicByItemVerId2(oldItemVerId, rootOrgId);
        if(basic!=null){
            if(isCreate){
                basic.setItemBasicId(UuidUtil.generateUuid());
                basic.setRootOrgId(rootOrgId);
            }
            if(isNewVer){
                basic.setItemVerId(newItemVerId);
            }
            if(isCreate){
                aeaItemBasicMapper.insertAeaItemBasic(basic);
            }else{
                aeaItemBasicMapper.updateAeaItemBasic(basic);
            }
        }
        return basic;
    }

    /**
     * 复制事项前置条件
     *
     * @param newItemId 新的事项itemId
     * @param oldItemVerId 旧的事项版本id
     * @param newItemVerId 新的事项版本id
     * @param rootOrgId
     */
    private void copyItemCond(String newItemId, String oldItemVerId, String newItemVerId, String rootOrgId){

        // 用来保存新旧的stateId的对应关系
        Map<String, String> map = new HashMap<String, String>();
        AeaItemCond cond = new AeaItemCond();
        cond.setItemVerId(oldItemVerId);
        cond.setRootOrgId(rootOrgId);
        List<AeaItemCond> condList = aeaItemCondMapper.listAeaItemCond(cond);
        if(condList!=null&&condList.size()>0){
            for (AeaItemCond vo : condList) {
                String oldItemCondId = vo.getItemCondId();
                String newItemCondId = UUID.randomUUID().toString();
                vo.setItemCondId(newItemCondId);
                if(StringUtils.isNotBlank(newItemId)){
                    vo.setItemId(newItemId);
                }
                vo.setItemVerId(newItemVerId);
                vo.setCondSeq(vo.getCondSeq().replace(oldItemCondId, newItemCondId));
                map.put(oldItemCondId, newItemCondId);
            }
            // 替换新的父级以及序列
            for (String key : map.keySet()) {
                for (AeaItemCond vo : condList) {
                    if (StringUtils.isNotBlank(vo.getParentCondId())&& vo.getParentCondId().equals(key)) {
                        vo.setParentCondId(map.get(key));
                    }
                    if (StringUtils.isNotBlank(vo.getCondSeq())&& vo.getCondSeq().indexOf(key) != -1) {
                        vo.getCondSeq().replace(key, map.get(key));
                    }
                }
            }
            for (AeaItemCond vo : condList) {
                aeaItemCondMapper.insertAeaItemCond(vo);
            }
        }
    }

    /**
     * 复制一张表单
     *
     * @param itemVerId
     * @param newItemVerId
     * @param rootOrgId
     */
    private void copyItemVerOneform(String itemVerId, String newItemVerId, String rootOrgId){

        // 事项总表
        AeaItemOneform sitemOneform = new AeaItemOneform();
        sitemOneform.setItemVerId(itemVerId);
        List<AeaItemOneform> itemOneformList = itemOneformMapper.listAeaItemOneform(sitemOneform);
        if(itemOneformList!=null&&itemOneformList.size()>0){
            for(AeaItemOneform itemOneform : itemOneformList){
                itemOneform.setItemOneformId(UuidUtil.generateUuid());
                itemOneform.setItemVerId(newItemVerId);
                itemOneformMapper.insertAeaItemOneform(itemOneform);
            }
        }

        // 事项扩展表
        AeaItemPartform sitemPartform = new AeaItemPartform();
        sitemPartform.setItemVerId(itemVerId);
        List<AeaItemPartform> itemPartformList = itemPartformMapper.listAeaItemPartform(sitemPartform);
        if(itemPartformList!=null&&itemPartformList.size()>0){
            for(AeaItemPartform itemPartform : itemPartformList){
                itemPartform.setItemPartformId(UuidUtil.generateUuid());
                itemPartform.setItemVerId(newItemVerId);
                itemPartformMapper.insertAeaItemPartform(itemPartform);
            }
        }
    }

    /**
     * 复制事项的前置检查事项
     *
     * @param itemVerId
     * @param newItemVerId
     * @param rootOrgId
     */
    private void copyItemFrontCheck(String itemVerId, String newItemVerId, String rootOrgId){

        // 前置事项
        AeaItemFrontItem frontItem = new AeaItemFrontItem();
        frontItem.setRootOrgId(rootOrgId);
        frontItem.setItemVerId(itemVerId);
        List<AeaItemFrontItem> frontItems = frontItemMapper.listAeaItemFront(frontItem);
        if(frontItems!=null&&frontItems.size()>0){
            for(AeaItemFrontItem vo : frontItems){
                vo.setFrontItemId(UuidUtil.generateUuid());
                vo.setItemVerId(newItemVerId);
                frontItemMapper.insertAeaItemFront(vo);
            }
        }

        // 前置扩展表
        AeaItemFrontPartform frontPartform = new AeaItemFrontPartform();
        frontPartform.setRootOrgId(rootOrgId);
        frontPartform.setItemVerId(itemVerId);
        List<AeaItemFrontPartform> frontPartForms = frontPartformMapper.listAeaItemFrontPartform(frontPartform);
        if(frontPartForms!=null&&frontPartForms.size()>0){
            for(AeaItemFrontPartform vo : frontPartForms){
                vo.setFrontPartformId(UuidUtil.generateUuid());
                vo.setItemVerId(newItemVerId);
                frontPartformMapper.insertAeaItemFrontPartform(vo);
            }
        }

        // 前置项目信息
        AeaItemFrontProj frontProj = new AeaItemFrontProj();
        frontProj.setRootOrgId(rootOrgId);
        frontProj.setItemVerId(itemVerId);
        List<AeaItemFrontProj> frontProjs = frontProjMapper.listAeaItemFrontProj(frontProj);
        if(frontProjs!=null&&frontProjs.size()>0){
            for(AeaItemFrontProj vo : frontProjs){
                vo.setFrontProjId(UuidUtil.generateUuid());
                vo.setItemVerId(newItemVerId);
                frontProjMapper.insertAeaItemFrontProj(vo);
            }
        }
    }

    /**
     * 复制操作指南数据
     *
     * @param oldItemVerId
     * @param newItemVerId
     */
    private void copyItemGuide(String oldItemVerId, String newItemVerId, String rootOrgId){

        // 基本信息
        AeaItemGuide guide = new AeaItemGuide();
        guide.setItemVerId(oldItemVerId);
        guide.setRootOrgId(rootOrgId);
        List<AeaItemGuide> guideList = aeaItemGuideMapper.listAeaItemGuide(guide);
        if(guideList!=null&&guideList.size()>0){
            for(AeaItemGuide vo:guideList){
                vo.setId(UuidUtil.generateUuid());
                vo.setItemVerId(newItemVerId);
                aeaItemGuideMapper.insertAeaItemGuide(vo);
            }
        }

        // 事项收费项目信息
        AeaItemGuideCharges charges = new AeaItemGuideCharges();
        charges.setItemVerId(oldItemVerId);
        charges.setRootOrgId(rootOrgId);
        List<AeaItemGuideCharges> chargesList = aeaItemGuideChargesMapper.listAeaItemGuideCharges(charges);
        if(chargesList!=null&&chargesList.size()>0){
            for(AeaItemGuideCharges vo:chargesList){
                vo.setRowguid(UuidUtil.generateUuid());
                vo.setItemVerId(newItemVerId);
                aeaItemGuideChargesMapper.listAeaItemGuideCharges(vo);
            }
        }

        // 窗口信息
        AeaItemGuideDepartments departments = new AeaItemGuideDepartments();
        departments.setItemVerId(oldItemVerId);
        departments.setRootOrgId(rootOrgId);
        List<AeaItemGuideDepartments> departmentsList = aeaItemGuideDepartmentsMapper.listAeaItemGuideDepartments(departments);
        if(departmentsList!=null&&departmentsList.size()>0){
            for(AeaItemGuideDepartments vo:departmentsList){
                vo.setId(UuidUtil.generateUuid());
                vo.setItemVerId(newItemVerId);
                aeaItemGuideDepartmentsMapper.insertAeaItemGuideDepartments(vo);
            }
        }

        // 扩展字段
        AeaItemGuideExtend extend = new AeaItemGuideExtend();
        extend.setItemVerId(oldItemVerId);
        extend.setRootOrgId(rootOrgId);
        List<AeaItemGuideExtend> extendList = aeaItemGuideExtendMapper.listAeaItemGuideExtend(extend);
        if(extendList!=null&&extendList.size()>0){
            for(AeaItemGuideExtend vo:extendList){
                vo.setId(UuidUtil.generateUuid());
                vo.setItemVerId(newItemVerId);
                aeaItemGuideExtendMapper.insertAeaItemGuideExtend(vo);
            }
        }

        // 问题
        AeaItemGuideQuestions questions = new AeaItemGuideQuestions();
        questions.setItemVerId(oldItemVerId);
        questions.setRootOrgId(rootOrgId);
        List<AeaItemGuideQuestions> questionsList = aeaItemGuideQuestionsMapper.listAeaItemGuideQuestions(questions);
        if(questionsList!=null&&questionsList.size()>0){
            for( AeaItemGuideQuestions vo:questionsList){
                vo.setId(UuidUtil.generateUuid());
                vo.setItemVerId(newItemVerId);
                aeaItemGuideQuestionsMapper.insertAeaItemGuideQuestions(vo);
            }
        }

        // 特殊
        AeaItemGuideSpecials specials = new AeaItemGuideSpecials();
        specials.setItemVerId(oldItemVerId);
        specials.setRootOrgId(rootOrgId);
        List<AeaItemGuideSpecials> specialsList = aeaItemGuideSpecialsMapper.listAeaItemGuideSpecials(specials);
        if(specialsList!=null&&specialsList.size()>0){
            for( AeaItemGuideSpecials vo:specialsList){
                vo.setId(UuidUtil.generateUuid());
                vo.setItemVerId(newItemVerId);
                aeaItemGuideSpecialsMapper.insertAeaItemGuideSpecials(vo);
            }
        }

        // 设置依据
        AeaItemServiceBasic basic = new AeaItemServiceBasic();
        basic.setTableName("AEA_ITEM_VER");
        basic.setPkName("ITEM_VER_ID");
        basic.setRecordId(oldItemVerId);
        basic.setRootOrgId(rootOrgId);
        List<AeaItemServiceBasic> basicList = aeaItemServiceBasicMapper.listAeaItemServiceBasicWithLegal(basic);
        if(basicList!=null&&basicList.size()>0){
            for( AeaItemServiceBasic vo : basicList){
                vo.setBasicId(UuidUtil.generateUuid());
                vo.setRecordId(newItemVerId);
                aeaItemServiceBasicMapper.insertAeaItemServiceBasic(vo);
            }
        }

        // 窗口办理
        AeaServiceWindowItem swindowItem = new AeaServiceWindowItem();
        swindowItem.setItemVerId(oldItemVerId);
        swindowItem.setRootOrgId(rootOrgId);
        List<AeaServiceWindowItem> winItemList = aeaServiceWindowItemMapper.listAeaServiceWindowItem(swindowItem);
        if(winItemList!=null && winItemList.size()>0) {
            for(AeaServiceWindowItem item : winItemList){
                item.setWindItemId(UuidUtil.generateUuid());
                item.setItemVerId(newItemVerId);
                aeaServiceWindowItemMapper.insertAeaServiceWindowItem(item);
            }
        }
    }

    public void deprecateAllTestRunAndPublishedVersion(String itemId, String itemVerId){

        aeaItemVerMapper.deprecateAllTestRunAndPublishedVersion(itemId, itemVerId, SecurityContext.getCurrentOrgId());
    }

    public void testRunOrPublished(String itemId, String itemVerId, Double verNum, String type, String oldVerStatus)throws Exception{

        if(StringUtils.isBlank(itemId)){
            throw new IllegalArgumentException("事项itemId为空！");
        }
        if(StringUtils.isBlank(itemVerId)){
            throw new IllegalArgumentException("事项版本itemVerId为空！");
        }
        Assert.notNull(verNum, "事项版本序号verNum为空!");
        if(StringUtils.isBlank(type)){
            throw new IllegalArgumentException("当前操作不明确！");
        }
        if(!(type.equals("2")||type.equals("1"))){
            throw new IllegalArgumentException("当前操作不明确,可以是试运行或者发布！");
        }

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        PublishStatus itemVerStatus = PublishStatus.TEST_RUN;
        if(type.equals("1")){
            itemVerStatus = PublishStatus.PUBLISHED;
        }
        if (PublishStatus.PUBLISHED.equals(itemVerStatus)) {
            //判断情形版本是否已发布,未发布不通过
            AeaItemStateVer specificVersion = aeaItemStateVersionAdminService.getSpecificVersion(AeaItemStateVersionAdminService.StateVersionStrategy.UN_PUBLISHED, itemVerId, rootOrgId);
            if (specificVersion != null) {
                throw new IllegalArgumentException("当前事项版本下存在最新情形版本未发布！");
            }
        }

        // 获取上个有效事项版本
        AeaItemVer preItemVer = null;
        if(verNum>1.0) {
            List<AeaItemVer> preItemVerList = aeaItemVerMapper.listTestRunOrPublishedItemVer(itemId, rootOrgId);
            if (preItemVerList != null && preItemVerList.size() > 0) {
                preItemVer = preItemVerList.get(0);
            }
        }

        // 当前版本下
        AeaItemVer itemVer = getAeaItemVerById(itemVerId);
        Assert.notNull(itemVer, "无法找到对应的事项版本, itemVerId: " + itemVerId);
        itemVer.setItemVerStatus(itemVerStatus.getValue());
        itemVer.setVerNum(verNum);
        itemVer.setModifier(userId);
        itemVer.setModifyTime(new Date());
        aeaItemVerMapper.updateAeaItemVer(itemVer);

        // 事项序号
        AeaItemSeq seq = aeaItemSeqMapper.getSeqByItemIdAndVerId(itemId, itemVerId, rootOrgId);
        Assert.notNull(seq, "无法找到对应的事项版本序号, itemVerId: " + itemVerId);
        seq.setItemVerMax(verNum);
        seq.setModifier(userId);
        seq.setModifyTime(new Date());
        aeaItemSeqMapper.updateAeaItemSeq(seq);

        if(oldVerStatus.equals(PublishStatus.UNPUBLISHED.getValue())){

            // 从未发布版本更改为试运行或已发布时，其他试运行或已发布版本就要变成已过时
            if(verNum>1.0){
                deprecateAllTestRunAndPublishedVersion(itemId, itemVerId);
            }

            if(preItemVer!=null){
                String oldItemVerId = preItemVer.getItemVerId();

                // 处理阶段事项关联数据
                handleStageItemChaneRelVerData(itemId, oldItemVerId, itemVerId, rootOrgId, userId);

                // 处理阶段前置检测事项 = 此处仅仅更新数据, 更新所有
                AeaParFrontItem parFrontItem = new AeaParFrontItem();
                parFrontItem.setRootOrgId(rootOrgId);
                parFrontItem.setItemVerId(oldItemVerId);
                List<AeaParFrontItem> parFrontItems = parFrontItemMapper.listAeaParFrontItem(parFrontItem);
                if(parFrontItems!=null&&parFrontItems.size()>0){
                    for(AeaParFrontItem vo:parFrontItems){
                        vo.setItemVerId(itemVerId);
                        parFrontItemMapper.updateAeaParFrontItem(vo);
                    }
                }

                // 处理共享材料 == 不好处理,暂不处理

                // 处理事项的前置检查事项 = 此处仅仅更新数据, 事项版本过时不更新
                List<AeaItemFrontItem> fronts = frontItemMapper.listNoDeprecatedItemFront(oldItemVerId, rootOrgId);
                if(fronts!=null&&fronts.size()>0){
                    for(AeaItemFrontItem itemFront:fronts){
                        itemFront.setFrontCkItemVerId(itemVerId);
                        frontItemMapper.updateAeaItemFront(itemFront);
                    }
                }

                // 从未发布版本更改为试运行或已发布时,复制ACT_TPL_APP_TRIGGER相关记录
                ActTplAppTrigger tplAppTrigger = new ActTplAppTrigger();
                tplAppTrigger.setBusTableName("AEA_ITEM_BASIC");
                tplAppTrigger.setPkName("ITEM_VER_ID");
                tplAppTrigger.setBusRecordId(oldItemVerId);
                List<ActTplAppTrigger> triggers = actTplAppTriggerMapper.listActTplAppTrigger(tplAppTrigger);
                if(triggers!=null && triggers.size()>0){
                    for(ActTplAppTrigger trigger : triggers){
                        trigger.setBusRecordId(itemVerId);
                        actTplAppTriggerMapper.updateActTplAppTrigger(trigger);
                    }
                }
            }
        }
    }

    /**
     * 处理阶段事项涉及的主题版本
     *
     * @param itemId 事项id
     * @param oldItemVerId 事项旧版本id
     * @param itemVerId 事项新版本id
     * @param rootOrgId 根组织id
     * @param userId 用户id
     */
    private void handleStageItemChaneRelVerData(String itemId, String oldItemVerId, String itemVerId, String rootOrgId, String userId)throws Exception{

        List<AeaParThemeVer> themeVers = aeaParThemeVerMapper.listThemeVerByItemIdAndItemVerId(itemId, oldItemVerId, rootOrgId);
        if(themeVers!=null&&themeVers.size()>0){
            Map<String,String> themeIds = new LinkedHashMap<String,String>();
            for(AeaParThemeVer themeVer:themeVers){
                themeIds.put(themeVer.getThemeId(), themeVer.getThemeId());
            }
            for (String key : themeIds.keySet()) {
                AeaParThemeVer unpublishVer = null;
                AeaParThemeVer usedVer = null;
                for(AeaParThemeVer themeVer : themeVers){
                    if(key.equals(themeVer.getThemeId())){
                        // 未发布版本
                        if(PublishStatus.UNPUBLISHED.getValue().equals(themeVer.getIsPublish())){
                            unpublishVer = themeVer;
                            // 可用版本
                        }else if(PublishStatus.TEST_RUN.getValue().equals(themeVer.getIsPublish())||PublishStatus.PUBLISHED.getValue().equals(themeVer.getIsPublish())){
                            usedVer = themeVer;
                        }
                    }
                }
                // 1、主题版本存在未发布且不存在可用版本，更新数据就可以
                if(unpublishVer!=null&&usedVer==null){
                    aeaParStageItemMapper.batchUpdateStageItemByItemVerChange(unpublishVer.getThemeVerId(), itemId, oldItemVerId, itemVerId, rootOrgId);
                }
                // 2、主题版本存在未发布且存在可用版本
                if(unpublishVer!=null&&usedVer!=null){
                    // 将未发布版本更新阶段事项数据
                    aeaParStageItemMapper.batchUpdateStageItemByItemVerChange(unpublishVer.getThemeVerId(), itemId, oldItemVerId, itemVerId, rootOrgId);
                    // 试运行可用版本更新阶段事项数据
                    if(PublishStatus.TEST_RUN.getValue().equals(usedVer.getIsPublish())){
                        aeaParStageItemMapper.batchUpdateStageItemByItemVerChange(usedVer.getThemeVerId(), itemId, oldItemVerId, itemVerId, rootOrgId);
                    // 已发布可用版本需要重新生成版本数据
                    }else{
                        // 未发布版本更新阶段事项数据版本状态改为试运行,已用版本改为过时
                        handNewAndOldThemeVer(key, userId, rootOrgId, unpublishVer, usedVer);
                        // 产生新的版本数据
                        AeaParThemeVer newThemeVer = aeaParThemeVerAdminService.copyThemeVerRelData(key, usedVer.getThemeVerId());
                        // 将未发布版本更新阶段事项数据
                        aeaParStageItemMapper.batchUpdateStageItemByItemVerChange(newThemeVer.getThemeVerId(), itemId, oldItemVerId, itemVerId, rootOrgId);
                        // 处理主题版本数据
                        handNewAndOldThemeVer(key, userId, rootOrgId, newThemeVer, unpublishVer);
                    }
                }
                // 3、主题版本仅仅只存在可用版本，需要产生新的版本数据，并将当前版本状态修改为过时，新版本改为试运行
                if(unpublishVer==null&&usedVer!=null){
                    // 产生新的版本数据
                    AeaParThemeVer newThemeVer = aeaParThemeVerAdminService.copyThemeVerRelData(key, usedVer.getThemeVerId());
                    // 将未发布版本更新阶段事项数据
                    aeaParStageItemMapper.batchUpdateStageItemByItemVerChange(newThemeVer.getThemeVerId(), itemId, oldItemVerId, itemVerId, rootOrgId);
                    // 处理主题版本数据
                    handNewAndOldThemeVer(key, userId, rootOrgId, newThemeVer, usedVer);
                }
                // 移除元素
                if(unpublishVer!=null){
                    themeVers.remove(unpublishVer);
                }
                // 移除元素
                if(usedVer!=null){
                    themeVers.remove(usedVer);
                }
            }
        }
    }

    /**
     * 处理新版本与旧版本
     *
     * @param unpublishVer 未发布版本
     * @param usedVer  已发布版本
     * @param themeId 主题id
     * @param userId 用户id
     */
    private void handNewAndOldThemeVer(String themeId, String userId, String rootOrgId, AeaParThemeVer unpublishVer, AeaParThemeVer usedVer){

        // 更新主题版本号
        Double newVerNum = new Double(unpublishVer.getVerNum().intValue()+1);
        unpublishVer.setVerNum(newVerNum);
        unpublishVer.setIsPublish(PublishStatus.TEST_RUN.getValue());
        unpublishVer.setModifier(userId);
        unpublishVer.setModifyTime(new Date());
        aeaParThemeVerMapper.updateOne(unpublishVer);

        // 更新主题版本系列
        AeaParThemeSeq themeSeq = aeaParThemeSeqMapper.getAeaParThemeSeqByThemeId(themeId, rootOrgId);
        if(themeSeq != null){
            themeSeq.setMaxNum(newVerNum);
            themeSeq.setModifier(userId);
            themeSeq.setModifyTime(new Date());
            aeaParThemeSeqMapper.updateOne(themeSeq);
        }else{
            AeaParThemeSeq newThemeSeq = AeaParThemeSeq.initOne(themeId, userId, rootOrgId);
            newThemeSeq.setMaxNum(newVerNum);
            aeaParThemeSeqMapper.insertOne(newThemeSeq);
        }

        // 处理可用版本
        usedVer.setIsPublish(PublishStatus.DEPRECATED.getValue());
        usedVer.setModifier(userId);
        usedVer.setModifyTime(new Date());
        aeaParThemeVerMapper.updateOne(usedVer);
    }
}