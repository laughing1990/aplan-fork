package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bpm.common.domain.ActTplApp;
import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppMapper;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppTriggerMapper;
import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.meta.domain.MetaDbColumn;
import com.augurit.agcloud.meta.domain.MetaDbConn;
import com.augurit.agcloud.meta.domain.MetaDbTable;
import com.augurit.agcloud.meta.mapper.MetaDbColumnMapper;
import com.augurit.agcloud.meta.mapper.MetaDbConnMapper;
import com.augurit.agcloud.meta.mapper.MetaDbTableMapper;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.InOutStatus;
import com.augurit.aplanmis.common.constants.InOutType;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.convert.BscAttDetailConvert;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.exception.ResultFormException;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemCondAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import com.augurit.aplanmis.common.service.admin.tpl.DgActTplAppAdminService;
import com.augurit.aplanmis.common.vo.ActTplAppTriggerAdminVo;
import com.augurit.aplanmis.common.vo.AeaItemMatAttr;
import com.augurit.aplanmis.common.vo.AppProcCaseDefPlusAdminVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
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
import java.util.stream.Collectors;


/**
 * 行政审批事项表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaItemBasicAdminServiceImpl implements AeaItemBasicAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemBasicAdminServiceImpl.class);

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaItemMapper aeaItemMapper;
    @Autowired
    private MetaDbConnMapper metaDbConnMapper;
    @Autowired
    private MetaDbTableMapper metaDbTableMapper;
    @Autowired
    private MetaDbColumnMapper metaDbColumnMapper;
    @Autowired
    private AeaItemExeorgMapper aeaItemExeorgMapper;
    @Autowired
    private AeaItemServiceServeMapper aeaItemServiceServeMapper;
    @Autowired
    private AeaItemAcceptRangeMapper aeaItemAcceptRangeMapper;
    @Autowired
    private AeaItemServiceFlowMapper aeaItemServiceFlowMapper;
    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;
    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    private AeaCertMapper aeaItemCertMapper;
    @Autowired
    private AeaItemServiceConsultingMapper aeaItemServiceConsultingMapper;
    @Autowired
    private AeaServiceWindowMapper aeaServiceWindowMapper;
    @Autowired
    private AeaItemServiceWindowRelMapper aeaItemServiceWindowRelMapper;
    @Autowired
    private AeaItemServiceChargeMapper aeaItemServiceChargeMapper;
    @Autowired
    private AeaItemAgencyMapper aeaItemAgencyMapper;
    @Autowired
    private AeaItemAgencyAuxMapper aeaItemAgencyAuxMapper;
    @Autowired
    private AeaItemAuxServiceMapper aeaItemAuxServiceMapper;
    @Autowired
    private AeaServiceLegalClauseMapper aeaServiceLegalClauseMapper;
    @Autowired
    private AeaItemRightsObligationsMapper aeaItemRightsObligationsMapper;
    @Autowired
    private AeaItemLegalRemedyMapper aeaItemLegalRemedyMapper;
    @Autowired
    private DgActTplAppAdminService dgActTplAppAdminService;
    @Autowired
    private ActTplAppTriggerMapper actTplAppTriggerMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private AeaItemStateAdminService aeaItemStateAdminService;

    @Autowired
    private BscAttMapper bscAttMapper;

    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;

    @Autowired
    private AeaItemVerMapper aeaItemVerMapper;

    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;

    @Autowired
    private AeaItemCondAdminService aeaItemCondAdminService;

    @Autowired
    private AeaItemVerAdminService aeaItemVerAdminService;

    @Autowired
    private AeaItemRelevanceMapper aeaItemRelevanceMapper;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Autowired
    private AeaParThemeVerAdminService aeaParThemeVerAdminService;

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String jdbcUserName;

    @Value("${spring.datasource.password}")
    private String jdbcPwd;

    @Autowired
    private ActTplAppMapper actTplAppMapper;

    @Override
    public String createAppIdByItemName(String itemName) {

        try {
            ActTplApp actTplAppAllInfo = dgActTplAppAdminService.createActTplAppAllInfo(itemName, Status.ON);
            return actTplAppAllInfo.getAppId();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取流程模板的appId失败!" + e.getMessage(), e);
            throw new ResultFormException("获取流程模板的appId失败");
        }
    }

    @Override
    public void saveAeaItemBasic(AeaItemBasic aeaItemBasic) {

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaItemBasic.setRootOrgId(rootOrgId);
        aeaItemBasic.setAppId(createAppIdByItemName(aeaItemBasic.getItemName()));
        //如果item_id为空，就创建在aea_item表中创建一条数据
        if (StringUtils.isBlank(aeaItemBasic.getItemId())) {
            AeaItem aeaItem = new AeaItem();
            aeaItem.setItemId(UUID.randomUUID().toString());
            if (StringUtils.isBlank(aeaItemBasic.getParentItemId())) {
                aeaItem.setParentItemId("root");
            } else {
                aeaItem.setParentItemId(aeaItemBasic.getParentItemId());
            }
            String itemSeq = getItemSeq(aeaItem.getItemId(), aeaItem.getParentItemId());
            aeaItem.setItemSeq(itemSeq);
            aeaItem.setRootOrgId(rootOrgId);
            aeaItem.setCreater(userId);
            aeaItem.setCreateTime(new Date());
            aeaItemMapper.insertAeaItem(aeaItem);
            aeaItemBasic.setItemId(aeaItem.getItemId());
        }
        //如果item_ver_id为空，就在aea_item_ver表中创建一条数据
        if (StringUtils.isBlank(aeaItemBasic.getItemVerId())) {
            AeaItemVer aeaItemVer = aeaItemVerAdminService.initAeaItemVer(aeaItemBasic.getItemId(), aeaItemBasic.getIsCatalog(), userId, rootOrgId);
            aeaItemBasic.setItemVerId(aeaItemVer.getItemVerId());

            //创建情形初始版本
            aeaItemStateAdminService.createUnpublishedVersion(aeaItemBasic.getItemVerId(), rootOrgId, userId);
        }
        aeaItemBasic.setCreater(userId);
        aeaItemBasic.setCreateTime(new Date());
        aeaItemBasicMapper.insertAeaItemBasic(aeaItemBasic);
    }

    @Override
    public void updateAeaItemBasic(AeaItemBasic aeaItemBasic) {

        // 同步更新模板名称
        if (StringUtils.isNotBlank(aeaItemBasic.getAppId())) {
            ActTplApp tplApp = new ActTplApp();
            tplApp.setAppId(aeaItemBasic.getAppId());
            tplApp.setAppComment(aeaItemBasic.getItemName());
            actTplAppMapper.updateActTplApp(tplApp);
        }
        try {
            AeaItemBasic oldBaisc = aeaItemBasicMapper.getAeaItemBasicById(aeaItemBasic.getItemBasicId());
            if(oldBaisc != null ){
                if((oldBaisc.getItemName() != null && aeaItemBasic.getItemName() != null &&!aeaItemBasic.getItemName().equals(oldBaisc.getItemName())) || (aeaItemBasic.getDueNum().intValue() != oldBaisc.getDueNum().intValue())){
                    aeaParThemeVerAdminService.updateDiagramActivityName(aeaItemBasic);
                }
            }
        } catch (Exception e) {
            logger.error("",e);
        }
        aeaItemBasic.setModifier(SecurityContext.getCurrentUserId());
        aeaItemBasic.setModifyTime(new Date());
        aeaItemBasicMapper.updateAeaItemBasic(aeaItemBasic);
    }

    @Override
    public void updateItemNeedCondType(AeaItemBasic aeaItemBasic) {
        aeaItemBasic.setModifier(SecurityContext.getCurrentUserId());
        aeaItemBasic.setModifyTime(new Date());
        aeaItemBasicMapper.updateAeaItemBasic(aeaItemBasic);
    }

    // 上过上级Id获取当前事项的排序
    @Override
    public Long getItemAcceptMode(String parentItemId) {

        Long acceptMode = aeaItemBasicMapper.getItemAcceptModeByParentId(parentItemId);
        if (acceptMode == null) {
            acceptMode = 1L;
        }
        return acceptMode + 1;
    }

    /**
     * 通过上级事项Id获取当前事项的序列
     *
     * @param itemId
     * @param parentItemId
     * @return
     */
    @Override
    public String getItemSeq(String itemId, String parentItemId) {

        if (StringUtils.isNotBlank(parentItemId)) {
            //获取上级菜单的序列
            AeaItem parentItem = aeaItemMapper.getAeaItemById(parentItemId);
            String parentItemSeq = CommonConstant.SEQ_SEPARATOR;
            if (parentItem != null && StringUtils.isNotBlank(parentItem.getItemSeq())) {
                parentItemSeq = parentItem.getItemSeq();
            }
            return parentItemSeq + itemId + CommonConstant.SEQ_SEPARATOR;
        } else {
            return CommonConstant.SEQ_SEPARATOR + itemId + CommonConstant.SEQ_SEPARATOR;
        }
    }

    @Override
    public void deleteAeaItemBasicById(String id) {

        if (StringUtils.isNotBlank(id)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            aeaItemVerMapper.deleteAeaItemVerByItemId(id, rootOrgId);
            aeaParThemeVerAdminService.removeActivityFromDiagramInAllAeaThemeVer(id);
            List<AeaItemBasic> allChildItems = aeaItemBasicMapper.listAllChildItems(id);
            if (allChildItems != null && allChildItems.size() > 0) {
                for (AeaItemBasic item : allChildItems) {
                    aeaItemVerMapper.deleteAeaItemVerByItemId(item.getItemId(), rootOrgId);
                    aeaParThemeVerAdminService.removeActivityFromDiagramInAllAeaThemeVer(item.getItemId());
                }
            }
        }
    }

    @Override
    public PageInfo<AeaItemBasic> listAeaItemBasic(AeaItemBasic aeaItemBasic, Page page) {

        PageHelper.startPage(page);
        List<AeaItemBasic> list = aeaItemBasicMapper.listAeaItemBasicWithOrg(aeaItemBasic);

        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemBasic>(list);
    }

    @Override
    public PageInfo<AeaItemBasic> listLatestAeaItemBasic(AeaItemBasic aeaItemBasic, Page page) {

        PageHelper.startPage(page);
        List<AeaItemBasic> list = aeaItemBasicMapper.listLatestAeaItemBasic(aeaItemBasic);
        if (list != null && list.size() > 0) {
            addVprefix(list);
        }

        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemBasic>(list);
    }

    @Override
    public AeaItemBasic getAeaItemBasicById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取Form对象，ID为：{}", id);
            AeaItemBasic basic = aeaItemBasicMapper.getAeaItemBasicWithOrgById(id);
            return basic;
        }
        return null;
    }

    @Override
    public List<AeaItemBasic> listAeaItemBasic(AeaItemBasic aeaItemBasic) {

        List<AeaItemBasic> list = aeaItemBasicMapper.listAeaItemBasicWithOrg(aeaItemBasic);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaItemBasic> listAeaItemBasicByThemeVerId(String themeId) {
        List<AeaItemBasic> list = aeaItemBasicMapper.listAeaItemBasicByThemeVerId(themeId);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaItemBasic> latestAeaItemBasicTree(AeaItemBasic aeaItemBasic, Page page) {

        List<AeaItemBasic> list = null;
        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaItemBasic.setRootOrgId(rootOrgId);
        // 涉及到查询的时候
        if (aeaItemBasic != null && (
                StringUtils.isNotBlank(aeaItemBasic.getOrgId()) ||
                        StringUtils.isNotBlank(aeaItemBasic.getOrgName()) ||
                        StringUtils.isNotBlank(aeaItemBasic.getType()) ||
                        StringUtils.isNotBlank(aeaItemBasic.getKeyword()))) {
            PageHelper.startPage(page);
            list = aeaItemBasicMapper.listLatestAeaItemBasic(aeaItemBasic);
            addVprefix(list);

            // 第一次查询的时候
        } else {
            aeaItemBasic.setParentItemId("root");
            PageHelper.startPage(page);
            list = aeaItemBasicMapper.listLatestAeaItemBasic(aeaItemBasic);
            logger.debug("成功执行分页查询！！");
            List<AeaItemBasic> nodeItems = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (AeaItemBasic itemBasic : list) {
                    if (itemBasic.getVerNum() != null) {
                        itemBasic.setVerNumVo("V" + itemBasic.getVerNum());
                    }
                    AeaItemBasic searchItem = new AeaItemBasic();
                    searchItem.setRootOrgId(rootOrgId);
                    searchItem.setItemSeq(itemBasic.getItemId());
                    List<AeaItemBasic> childItems = aeaItemBasicMapper.listLatestAeaItemBasic(searchItem);
                    if (childItems != null && childItems.size() > 0) {
                        addVprefix(childItems);
                        nodeItems.addAll(childItems);
                    }
                }
                list.addAll(nodeItems);
            }
        }
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AeaItemBasic> listAeaItemBasicByOrgIds(AeaItemBasic aeaItemBasic, Page page) {

        List<String> orgIds = new ArrayList<String>();
        String[] orgIdArr = aeaItemBasic == null ? null : aeaItemBasic.getOrgIds();
        List<AeaItemBasic> list = null;
        if (orgIdArr != null && orgIdArr.length > 0) {
            orgIds.addAll(Arrays.asList(aeaItemBasic.getOrgIds()));
            if (orgIds == null) {
                throw new InvalidParameterException("无法获取当前用户所在组织!");
            }
            if (orgIds != null && orgIds.size() == 0) {
                throw new InvalidParameterException("无法获取当前用户所在组织!");
            }
            aeaItemBasic.setIsCatalog(Status.OFF);
            aeaItemBasic.setRootOrgId(SecurityContext.getCurrentOrgId());
            PageHelper.startPage(page);
            list = aeaItemBasicMapper.listAeaItemBasicByOrgIds(aeaItemBasic, orgIds);
            logger.debug("成功执行分页查询！！");
            addVprefix(list);
        }
        return new PageInfo<AeaItemBasic>(list);
    }

    @Override
    public EasyuiPageInfo<AeaItemBasic> listUsedAeaItemBasicTreeByPage(AeaItemBasic aeaItemBasic, Page page) {

        if (StringUtils.isBlank(aeaItemBasic.getItemId())) {
            aeaItemBasic.setParentItemId("root");
        } else {
            aeaItemBasic.setParentItemId(aeaItemBasic.getItemId());
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaItemBasic.setRootOrgId(rootOrgId);
        PageHelper.startPage(page);
        List<AeaItemBasic> list = aeaItemBasicMapper.listLatestOkAeaItemBasic(aeaItemBasic);
        logger.debug("成功执行分页查询！！");
        List<AeaItemBasic> nodeItems = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (AeaItemBasic itemBasic : list) {
                if (itemBasic.getVerNum() != null) {
                    itemBasic.setVerNumVo("V" + itemBasic.getVerNum());
                }
                AeaItemBasic searchItem = new AeaItemBasic();
                searchItem.setRootOrgId(rootOrgId);
                searchItem.setItemSeq(itemBasic.getItemId());
                List<AeaItemBasic> childItems = aeaItemBasicMapper.listLatestAeaItemBasic(searchItem);
                if (childItems != null && childItems.size() > 0) {
                    addVprefix(childItems);
                    nodeItems.addAll(childItems);
                }
            }
            list.addAll(nodeItems);
        }

        EasyuiPageInfo<AeaItemBasic> pageInfo = PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
        return pageInfo;
    }

    @Override
    public List<AeaItemBasic> listUsedAeaItemBasicTree(AeaItemBasic aeaItemBasic) {

        /**
         * 获取可用事项
         */
        List<AeaItemBasic> list = aeaItemBasicMapper.listLatestOkAeaItemBasic(aeaItemBasic);
        List<AeaItemBasic> nodeItems = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (AeaItemBasic itemBasic : list) {
                if (itemBasic.getVerNum() != null) {
                    itemBasic.setVerNumVo("V" + itemBasic.getVerNum());
                }
                AeaItemBasic searchItem = new AeaItemBasic();
                searchItem.setItemSeq(itemBasic.getItemId());
                List<AeaItemBasic> childItems = aeaItemBasicMapper.listLatestAeaItemBasic(searchItem);
                if (childItems != null && childItems.size() > 0) {
                    addVprefix(childItems);
                    nodeItems.addAll(childItems);
                }
            }
            list.addAll(nodeItems);
        }
        return list;
    }


    private void removeDuplication(List<String> list) {
        if (list != null && list.size() > 0) {
            List temp = new ArrayList();
            temp.add(list.get(0));
            for (int i = 0, len = list.size(); i < len; i++) {
                String ele = list.get(i);
                if (!temp.contains(ele)) {
                    temp.add(ele);
                }
            }
            list.clear();
            list.addAll(temp);
        }
    }

    @Override
    public List<AeaItemCond> gtreeItemCond(String itemId) {

        List<AeaItemCond> listNode = new ArrayList<AeaItemCond>();
        AeaItemBasic item = aeaItemBasicMapper.getAeaItemBasicWithOrgById(itemId);
        if (item != null) {
            AeaItemCond rootNode = new AeaItemCond();
            rootNode.setId(itemId);
            rootNode.setName(item.getItemName());
            rootNode.setType("root");
            rootNode.setOpen(true);
            rootNode.setIsParent(true);
            rootNode.setNocheck(true);
            listNode.add(rootNode);
            AeaItemCond searchCond = new AeaItemCond();
            searchCond.setItemId(itemId);
            List<AeaItemCond> condList = aeaItemCondAdminService.listAeaItemCond(searchCond);
            if (condList != null && condList.size() > 0) {
                for (AeaItemCond cond : condList) {
                    AeaItemCond condNode = new AeaItemCond();
                    condNode.setId(cond.getItemCondId());
                    String sfzz = "";
                    String muiltSelect = "";
                    if (StringUtils.isNotBlank(cond.getSfzz()) && "1".equals(cond.getSfzz())) {
                        sfzz = "[终止]";
                    }
                    if (cond.getMuiltSelect() != null && cond.getMuiltSelect() > 0) {
                        muiltSelect = "[" + cond.getMuiltSelect() + "]";
                    }
                    condNode.setName(cond.getCondName() + sfzz + muiltSelect);
                    condNode.setpId(itemId);
                    if (StringUtils.isNotBlank(cond.getParentCondId())) {
                        condNode.setpId(cond.getParentCondId());
                    }
                    condNode.setType("cond");
                    condNode.setOpen(true);
                    condNode.setIsParent(true);
                    condNode.setNocheck(true);
                    listNode.add(condNode);
                }
            }
        } else {
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
    @Deprecated
    public void batchDeleteAeaItemBasic(String[] ids) {
        if (ids != null && ids.length > 0) {
            //            aeaItemBasicMapper.batchDeleteAeaItemBasic(ids);
        }
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
    public AeaItemBasic getAeaItemBasicByItemCode(String itemCode) {
        if (StringUtils.isNotBlank(itemCode)) {
            return aeaItemBasicMapper.getAeaItemBasicByItemCode(itemCode);
        }
        return null;
    }

    @Override
    public AeaItemBasic viewItemOperaGuideByItemIdOrCode(String itemId, String itemCode) {
        AeaItemBasic item = null;
        if (StringUtils.isNotBlank(itemId)) {
            item = aeaItemBasicMapper.getAeaItemBasicWithOrgById(itemId);
        } else if (StringUtils.isNotBlank(itemCode)) {
            item = aeaItemBasicMapper.getAeaItemBasicByItemCode(itemCode);
        }
        if (item != null) { // 查询其他数据
            item.setAeaItemExeorg(aeaItemExeorgMapper.getAeaItemExeorgByItemBasicId(itemId));
            item.setAeaItemServiceServe(aeaItemServiceServeMapper.getAeaItemServiceServeByitemBasicId(itemId));
            item.setAeaItemAcceptRange(aeaItemAcceptRangeMapper.getAeaItemAcceptRangeByItemBasicId(itemId));
            item.setAeaItemServiceFlow(aeaItemServiceFlowMapper.getAeaItemServiceFlowByItemBasicId(itemId));

            AeaItemInout aeaItemInout = new AeaItemInout();
            aeaItemInout.setItemId(itemId);
            aeaItemInout.setIsOwner("1");
            aeaItemInout.setIsInput("1");
            aeaItemInout.setIsStateIn("0");
            aeaItemInout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            List<AeaItemInout> inoutlist = aeaItemInoutMapper.listAeaItemInout(aeaItemInout);
            inoutlist.forEach(inout -> {
                if (MindType.MATERIAL.getValue().equals(inout.getFileType())) {
                    inout.setAeaItemMat(aeaItemMatMapper.selectOneById(inout.getMatId()));
                    inout.setAeaMatCertName(aeaItemMatMapper.selectOneById(inout.getMatId()).getMatName());
                } else if (MindType.CERTIFICATE.getValue().equals(inout.getFileType())) {
                    inout.setAeaCert(aeaItemCertMapper.selectOneById(inout.getCertId()));
                    inout.setAeaMatCertName(aeaItemCertMapper.selectOneById(inout.getCertId()).getCertName());
                }
            });
            item.setAeaItemInout(inoutlist);
            item.setAeaItemServiceConsulting(aeaItemServiceConsultingMapper.getAeaItemServiceConsultingByItemId(itemId));

            if (item.getAeaItemServiceServe() != null) {
                AeaItemServiceWindowRel aeaItemServiceWindowRel = new AeaItemServiceWindowRel();
                aeaItemServiceWindowRel.setTableName("AEA_ITEM_SERVICE_SERVE");
                aeaItemServiceWindowRel.setPkName("SERVICE_SERVE_ID");
                aeaItemServiceWindowRel.setRecordId(item.getAeaItemServiceServe().getServiceServeId());
                List<AeaItemServiceWindowRel> windowRellist = aeaItemServiceWindowRelMapper.listAeaItemServiceWindowRelAndWindowInfoByKeyword(aeaItemServiceWindowRel);
                if (windowRellist != null && windowRellist.size() > 0) {
                    aeaItemServiceWindowRel = windowRellist.get(0);
                    AeaServiceWindow aeaServiceWindow = aeaServiceWindowMapper.getAeaServiceWindowById(aeaItemServiceWindowRel.getWindowId());
                    item.setAeaServiceWindow(aeaServiceWindow);
                }
            }

            item.setAeaItemServiceCharge(aeaItemServiceChargeMapper.getAeaItemServiceChargeByItemId(itemId));

            AeaItemAgency aeaItemAgency = aeaItemAgencyMapper.getAeaItemAgencyByItemId(itemId);
            List<AeaItemAuxService> auxServiceList = new ArrayList<>();
            if (aeaItemAgency != null && ("1").equals(aeaItemAgency.getSfxyzjfw())) {
                List<AeaItemAgencyAux> auxservicelist = aeaItemAgencyAuxMapper.getAeaItemAgencyAuxByAgencyId(aeaItemAgency.getAgencyId());
                auxservicelist.forEach(a -> auxServiceList.add(aeaItemAuxServiceMapper.getAeaItemAuxServiceById(a.getAuxServiceId())));
                item.setAeaItemAuxService(auxServiceList);
            }

            AeaItemServiceBasic aeaItemServiceBasic = new AeaItemServiceBasic();
            aeaItemServiceBasic.setTableName("AEA_ITEM");
            aeaItemServiceBasic.setPkName("ITEM_ID");
            aeaItemServiceBasic.setRecordId(itemId);
            List<AeaServiceLegalClause> legalClauselist = aeaServiceLegalClauseMapper.listAeaServiceLegalClauseByChargeGroupId(aeaItemServiceBasic);
            item.setAeaServiceLegalClause(legalClauselist);

            item.setAeaItemRightsObligations(aeaItemRightsObligationsMapper.getAeaItemRightsObligationsByItemId(itemId));
            item.setAeaItemLegalRemedy(aeaItemLegalRemedyMapper.getAeaItemLegalRemedyByItemId(itemId));
        }
        return item;
    }

    /**
     * 根据itemId删除inout表中IsDeleted='0'的输入数据，并更新修改时间
     *
     * @param itemId
     */
    private void batchDeleteAeaItemInoutByItemId(String itemId) {

        AeaItemInout deleteInout = new AeaItemInout();
        deleteInout.setItemId(itemId);
        deleteInout.setFileType(InOutType.MAT.getValue());
        deleteInout.setIsOwner(Status.OFF);
        deleteInout.setIsInput(InOutStatus.IN.getValue());
        deleteInout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaItemInoutMapper.batchDeleteAeaItemInout(deleteInout);
    }


    private List<AeaItemInout> getAeaItemInoutList(String itemId, String matId, String isStateIn) {
        AeaItemInout aeaItemInout = new AeaItemInout();
        aeaItemInout.setIsOwner(Status.OFF);
        aeaItemInout.setIsInput(InOutStatus.IN.getValue());
        aeaItemInout.setItemId(itemId);
        aeaItemInout.setFileType(InOutType.MAT.getValue());
        aeaItemInout.setMatId(matId);
        aeaItemInout.setIsStateIn(isStateIn);
        return aeaItemInoutMapper.listAeaItemInout(aeaItemInout);
    }

    /**
     * 选择的优先级:
     * 1、有关联aeaHiItemInoutinst
     * 2、修改时间最晚
     *
     * @param list
     * @return
     * @throws Exception
     */
    private AeaItemInout getNeedAeaItemInout(List<AeaItemInout> list) throws Exception {
        if (list != null && !list.isEmpty()) {
            AeaItemInout need = null;
            if (list.size() == 1) {
                need = list.get(0);
            } else {
                AeaItemInout lastModify = null;
                for (AeaItemInout inout : list) {
                    AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
                    aeaHiItemInoutinst.setItemInoutId(inout.getInoutId());
                    List<AeaHiItemInoutinst> aeaHiItemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);

                    if (aeaHiItemInoutinsts != null && !aeaHiItemInoutinsts.isEmpty()) {

                        if (need == null) {
                            need = inout;
                        } else {
                            need = getLastModifyAeaItemInout(need, inout);
                        }
                    } else {
                        if (lastModify == null) {
                            lastModify = inout;
                        } else {
                            lastModify = getLastModifyAeaItemInout(lastModify, inout);
                        }
                    }
                }

                if (need == null) {
                    need = lastModify;
                }
            }

            return need;


        }

        return null;
    }


    /**
     * 获取最后修改的那个对象
     *
     * @param inout1
     * @param inout2
     * @return
     */
    private AeaItemInout getLastModifyAeaItemInout(AeaItemInout inout1, AeaItemInout inout2) {

        if (inout1.getModifyTime() != null && inout2.getModifyTime() != null) {
            if (inout2.getModifyTime().compareTo(inout1.getModifyTime()) > 1) {
                return inout2;
            }
            /*if(inout2.getModifyTime().after(inout1.getModifyTime())){
                return inout2;
            }*/
        } else if (inout1.getModifyTime() == null && inout2.getModifyTime() != null) {
            return inout2;
        }

        return inout1;
    }

    @Override
    public void handleMatAttachments(AeaItemMat aeaItemMat) throws Exception {

        // 处理模板、样本、审查附件
        // 先删除
        // 模板
        String ORG_ID = SecurityContext.getCurrentOrgId();
        List<BscAttForm> kbList = bscAttMapper.listAttLinkAndDetail("AEA_ITEM_MAT", "TEMPLATE_DOC",
                aeaItemMat.getMatId(), "KP", ORG_ID, null);
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
                aeaItemMat.getMatId(), "KP", ORG_ID, null);
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
                aeaItemMat.getMatId(), "KP", ORG_ID, null);
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

    private String buildItemClqdDataParam(String itemCode, String ywqxId) {

        StringBuilder sb = new StringBuilder("itemcode=");
        sb.append(StringUtils.isNotBlank(itemCode) ? itemCode : "");
        if (StringUtils.isNotBlank(ywqxId)) {
            sb.append("&ywqxId=").append(ywqxId);
        }
        return sb.toString();
    }

    private String buildItemQXDataParam(String itemcode) {
        return "itemcode=" + (StringUtils.isNotBlank(itemcode) ? itemcode : "");
    }

    private String buildBsznDataParam(String taskCode) {
        return "tcode=" + (StringUtils.isNotBlank(taskCode) ? taskCode : "");
    }

    private String buildDownFileDataParam(String filePath) {
        return "url=" + (StringUtils.isNotBlank(filePath) ? filePath : "");
    }

    private String buildItemQztjDataParam(String itemcode, String itemId, String versionId) {

        if (StringUtils.isBlank(itemcode)) {
            itemcode = "";
        }
        if (StringUtils.isBlank(itemId)) {
            itemId = "";
        }
        if (StringUtils.isBlank(versionId)) {
            versionId = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("itemcode=" + itemcode)
                .append("&itemId=" + itemId)
                .append("&versionId=" + versionId);
        return sb.toString();
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
    public List<ActTplAppTriggerAdminVo> getActTplAppTriggerByAppFlowDefIds(String appFlowDefId, String appId, String nodeId, String keyword) {
        List<ActTplAppTriggerAdminVo> vos = new ArrayList<>();
        try {
            List<ActTplAppTrigger> list = actTplAppTriggerMapper.getActTplAppTriggerByAppFlowDefIds(Arrays.asList(appFlowDefId), appId);
            if (CollectionUtils.isNotEmpty(list)) {
                for (ActTplAppTrigger appTrigger : list) {
                    if (StringUtils.isNotBlank(nodeId) && !appTrigger.getTriggerElementId().equals(nodeId)) {
                        //20190829如果触发节点id不为空，则只过滤当前节点触发的流程
                        continue;
                    }
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
                    //20190909修改，回显绑定的事项名称
                    if (StringUtils.isNotBlank(appTrigger.getBusRecordId())) {
                        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(appTrigger.getBusRecordId(), SecurityContext.getCurrentOrgId());
                        if (aeaItemBasic != null) {
                            String itemName = aeaItemBasic.getItemName();
                            OpuOmOrg activeOrg = opuOmOrgMapper.getActiveOrg(aeaItemBasic.getOrgId());
                            if (activeOrg != null) {
                                itemName += "【" + activeOrg.getOrgName() + "】";
                            }
                            vo.setItemName(itemName);
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
    public void saveOrUpdateTrigger(ActTplAppTrigger actTplAppTrigger) throws Exception {
        actTplAppTrigger.setCreater(SecurityContext.getCurrentUserName());
        actTplAppTrigger.setCreateTime(new Date());
        actTplAppTrigger.setPriorityOrder(1L);
        actTplAppTrigger.setTriggerType("1");
        actTplAppTrigger.setPkName("ITEM_ID");
        actTplAppTrigger.setBusTableName("AEA_ITEM");
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
    public Collection<FlowElement> getTaskNodeList(String defKey) {
        Collection<FlowElement> flowElements = null;
        ProcessDefinition def = repositoryService.createProcessDefinitionQuery().processDefinitionKey(defKey).latestVersion().singleResult();
        if (def != null) {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(def.getId());
            Process process = bpmnModel.getProcesses().get(0);
            flowElements = process.getFlowElements();
            flowElements.removeIf(flowElement -> !(flowElement instanceof UserTask));
        }
        return flowElements;
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
    public ActTplAppTrigger getSubTriggerById(String id) throws Exception {
        return actTplAppTriggerMapper.getActTplAppTriggerById(id);
    }

    @Override
    public void delSubTrigger(String id) throws Exception {
        actTplAppTriggerMapper.deleteActTplAppTrigger(id);
    }

    @Override
    public EasyuiPageInfo<AeaItemBasic> listAeaItemBasicForSupermaket(AeaItemBasic aeaItemBasic, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaItemBasic> list = aeaItemBasicMapper.listLatestAeaItemBasic(aeaItemBasic);
        logger.debug("成功执行分页查询！！");
        if (list != null && list.size() > 0) {
            addVprefix(list);
            if (aeaItemBasic != null && StringUtils.isNotBlank(aeaItemBasic.getCurrItemId())) {
                List<String> idList = this.getItemIdList(aeaItemBasic.getCurrItemId());
                if (idList.size() > 0) {
                    for (int i = 0, len = list.size(); i < len; i++) {
                        AeaItemBasic itemBasic = list.get(i);
                        if (idList.contains(itemBasic.getItemId())) {
                            itemBasic.setIsCheck(true);
                        }
                    }
                }
            }
        }
        EasyuiPageInfo<AeaItemBasic> pageInfo = PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
        return pageInfo;
    }

    /**
     * VerNumVo添加V前缀
     *
     * @param list
     */
    private void addVprefix(List<AeaItemBasic> list) {

        if (list != null && list.size() > 0) {
            for (AeaItemBasic itemBasic : list) {
                if (itemBasic.getVerNum() != null) {
                    itemBasic.setVerNumVo("V" + itemBasic.getVerNum());
                }
            }
        }
    }

    @Override
    public List<AeaItemBasic> listAeaItemBasicForSupermaketNoPage(String itemId) throws Exception {
        List<AeaItemBasic> result = new ArrayList<>();
        if (StringUtils.isNotBlank(itemId)) {
            List<String> idList = this.getItemIdList(itemId);
            if (idList.size() > 0) {
                AeaItemBasic aeaItemBasic = new AeaItemBasic();
                aeaItemBasic.setSearchItemIds(idList.toArray(new String[idList.size()]));
                aeaItemBasic.setRootOrgId(SecurityContext.getCurrentOrgId());
                result = aeaItemBasicMapper.listLatestAeaItemBasic(aeaItemBasic);
            }
        }
        return result;
    }

    private List<String> getItemIdList(String itemId) throws Exception {
        List<String> idList = new ArrayList<>();
        if (StringUtils.isNotBlank(itemId)) {
            AeaItemRelevance search = new AeaItemRelevance();
            search.setChildItemId(itemId);
            List<AeaItemRelevance> relevances = aeaItemRelevanceMapper.listAeaItemRelevance(search);
            if (relevances != null && relevances.size() > 0) {
                for (AeaItemRelevance relevance : relevances) {
                    idList.add(relevance.getParentItemId());
                }
            }
        }
        return idList;
    }

    @Override
    public ResultForm saveConfigItem(String itemId, String[] saveItemIds, String[] cancelItemIds) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        if (StringUtils.isNotBlank(itemId)) {
            this.saveItemRelation(itemId, saveItemIds);
            this.cancelItemRelation(itemId, cancelItemIds);
            resultForm.setSuccess(true);
        } else {
            resultForm.setMessage("事项ID不能为空");
        }
        return resultForm;
    }

    private void saveItemRelation(String itemId, String[] saveItemIds) throws Exception {
        if (StringUtils.isNotBlank(itemId) && saveItemIds != null && saveItemIds.length > 0) {
            AeaItemRelevance itemRelevance = new AeaItemRelevance();
            for (int i = 0, len = saveItemIds.length; i < len; i++) {
                String pId = saveItemIds[i];
                itemRelevance.setRelevanceId(UUID.randomUUID().toString());
                itemRelevance.setChildItemId(itemId);
                itemRelevance.setParentItemId(pId);
                itemRelevance.setCreater(SecurityContext.getCurrentUserName());
                itemRelevance.setCreateTime(new Date());
                aeaItemRelevanceMapper.insertAeaItemRelevance(itemRelevance);
                logger.debug("成功插入记录{}！！", JsonUtils.toJson(itemRelevance));
            }
        }
    }

    private void cancelItemRelation(String itemId, String[] cancelItemIds) throws Exception {
        if (StringUtils.isNotBlank(itemId) && cancelItemIds != null && cancelItemIds.length > 0) {
            aeaItemRelevanceMapper.cancelItemRelation(itemId, cancelItemIds);
            logger.debug("成功取消关联：{}！！", JsonUtils.toJson(cancelItemIds));
        }
    }

    @Override
    public AeaItemBasic getOneByItemVerId(String itemVerId, String rootOrgId) {

        if (StringUtils.isBlank(itemVerId)) {
            throw new InvalidParameterException("参数itemVerId为空!");
        }
        if (StringUtils.isBlank(rootOrgId)) {
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        return aeaItemBasicMapper.getOneByItemVerId(itemVerId, rootOrgId);
    }

    /**
     * 递归查询所有的实施事项
     *
     * @param parentItemId 父事项id
     * @param rootOrgId    租户id
     * @return 实施事项列表
     */
    @Override
    public List<AeaItemBasic> findCarryOutItemsByItemId(String parentItemId, String rootOrgId) {
        List<AeaItemBasic> carryOutItems = new ArrayList<>();
        if (StringUtils.isBlank(rootOrgId)) {
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        List<String> parentItemIds = new ArrayList<>();
        parentItemIds.add(parentItemId);
        while (!parentItemIds.isEmpty()) {
            List<AeaItemBasic> aeaItemBasics = aeaItemBasicMapper.listCarryOutAeaItemBasicByParentItemIds(parentItemIds, rootOrgId);
            parentItemIds.clear();
            if (aeaItemBasics != null && aeaItemBasics.size() > 0) {
                carryOutItems.addAll(aeaItemBasics);
                parentItemIds.addAll(aeaItemBasics.stream().map(AeaItemBasic::getItemId).collect(Collectors.toList()));
            }
        }
        return carryOutItems;
    }

    /**
     * 检查唯一分类标记编号是否唯一
     *
     * @param itemId
     * @param isCatalog
     * @param itemCategoryMark
     * @param rootOrgId
     * @return
     */
    @Override
    public boolean checkUniqueItemCategoryMark(String itemId, String isCatalog, String itemCategoryMark, String rootOrgId) {

        List<AeaItemBasic> itemBasicList = aeaItemBasicMapper.checkUniqueItemCategoryMark(itemId, isCatalog, itemCategoryMark, rootOrgId);
        if (itemBasicList != null && itemBasicList.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据输出材料ID获取中介服务事项
     *
     * @param matId
     * @return
     * @throws Exception
     */
    @Override
    public List<AeaItemBasic> getAeaItemBasicsListByOutputMatId(String matId) throws Exception {
        List<AeaItemBasic> itemBasics = new ArrayList();
        if (StringUtils.isBlank(matId)) return itemBasics;
        itemBasics.addAll(aeaItemBasicMapper.getAeaItemBasicsListByOutputMatId(matId, SecurityContext.getCurrentOrgId()));
        return itemBasics;
    }

    /**
     * 根据项目ID获取已经办结通过或者办结容缺通过的事项
     *
     * @param projInfoId
     * @return
     */
    @Override
    public List<AeaItemBasic> getCompletedItemBasicByProjInfoId(String projInfoId) {
        List<AeaItemBasic> itemBasics = new ArrayList();
        if (StringUtils.isBlank(projInfoId)) return itemBasics;
        itemBasics.addAll(aeaItemBasicMapper.getCompletedItemBasicByProjInfoId(projInfoId, SecurityContext.getCurrentOrgId()));
        return itemBasics;
    }
}
