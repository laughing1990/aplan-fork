package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.par.AeaParShareMatService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AeaParShareMatServiceImpl implements AeaParShareMatService {

    private static Logger logger = LoggerFactory.getLogger(AeaParShareMatServiceImpl.class);

    @Autowired
    private AeaParShareMatMapper aeaParShareMatMapper;

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;

    @Autowired
    private AeaItemStateVerMapper itemStateVerMapper;

    @Autowired
    private AeaItemBasicMapper itemBasicMapper;

    /**
     * 获取事项输出材料列表
     *
     * @param aeaItemInout
     * @param page
     * @return
     *
     */
    @Override
    public EasyuiPageInfo<AeaItemInout> listOutItemMat(AeaItemInout aeaItemInout, Page page){

        aeaItemInout.setIsInput(Status.OFF);
        aeaItemInout.setIsStateIn(Status.OFF);
        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemInout> list = new ArrayList<>();
        if(StringUtils.isNotBlank(aeaItemInout.getItemVerId())){
            list = aeaItemInoutMapper.listItemInoutOfficeMat(aeaItemInout);
        }
        logger.debug("成功执行分页查询！！");
        return PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
    }

    @Override
    public EasyuiPageInfo<AeaItemInout> listInItemMat(AeaItemInout aeaItemInout, Page page){

        aeaItemInout.setIsInput(Status.ON);
        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemInout> list = new ArrayList<>();
        if(StringUtils.isNotBlank(aeaItemInout.getItemVerId())&&StringUtils.isNotBlank(aeaItemInout.getStateVerId())){
            list = aeaItemInoutMapper.listItemInoutOfficeMat(aeaItemInout);
        }
        logger.debug("成功执行分页查询！！");
        EasyuiPageInfo<AeaItemInout> pageInfo = PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
        return pageInfo;
    }

    @Override
    public List<ZtreeNode> listOutItemTreeByThemeVerId(String themeVerId){

        List<ZtreeNode> allNodes = new ArrayList<>();
        if(StringUtils.isNotBlank(themeVerId)) {
            ZtreeNode rootNode = new ZtreeNode();
            rootNode.setId(themeVerId);
            rootNode.setName("事项输出树");
            rootNode.setType("root");
            rootNode.setOpen(true);
            rootNode.setIsParent(true);
            rootNode.setNocheck(true);
            allNodes.add(rootNode);

            String rootOrgId = SecurityContext.getCurrentOrgId();
            List<AeaItemInout> officeMatList = new ArrayList<>();
            List<String> allItemVerIds = listThemeVerItemVerIds(themeVerId, rootOrgId);
            //  查询事项的批文批复
            if (allItemVerIds != null && allItemVerIds.size()>0){
                AeaItemInout itemInout = new AeaItemInout();
                itemInout.setIsInput(Status.OFF);
                itemInout.setItemVerIds(allItemVerIds);
                itemInout.setRootOrgId(rootOrgId);
                officeMatList = aeaItemInoutMapper.listInoutOfficeMatRelItemInfo(itemInout);
                convertInoutToNode(themeVerId, officeMatList, allNodes);
            }

        }
        return allNodes;
    }

    @Override
    public List<ZtreeNode> listInItemTreeByThemeVerId(String themeVerId){

        List<ZtreeNode> allNodes = new ArrayList<>();
        if(StringUtils.isNotBlank(themeVerId)) {
            ZtreeNode rootNode = new ZtreeNode();
            rootNode.setId(themeVerId);
            rootNode.setName("事项输入树");
            rootNode.setType("root");
            rootNode.setOpen(true);
            rootNode.setIsParent(true);
            rootNode.setNocheck(true);
            allNodes.add(rootNode);

            String rootOrgId = SecurityContext.getCurrentOrgId();
            List<AeaItemInout> officeMatList = new ArrayList<>();
            List<String> allItemVerIds = listThemeVerItemVerIds(themeVerId, rootOrgId);
            //  查询事项的批文批复
            if (allItemVerIds != null && allItemVerIds.size()>0){
                AeaItemInout itemInout = new AeaItemInout();
                itemInout.setIsInput(Status.ON);
                itemInout.setItemVerIds(allItemVerIds);
                itemInout.setRootOrgId(rootOrgId);
                officeMatList = aeaItemInoutMapper.listInOfficeMatRelItemInfo(itemInout);
                convertInoutToNode(themeVerId, officeMatList, allNodes);
            }

        }
        return allNodes;
    }

    private List<String> listThemeVerItemVerIds(String themeVerId, String rootOrgId){

        List<String> allItemVerIds = new ArrayList<>();
        // 阶段标准事项
        List<String> standItemIds = aeaParStageItemMapper.listItemIdsNoRepeatByThemeVerId(themeVerId, null, Status.ON, rootOrgId);
        // 转换阶段标准事项成实施事项
        if(standItemIds!=null&&standItemIds.size()>0){
            List<String> exeItemVerIds2 = itemBasicMapper.listSefAndChildLatestOkItemVerIds(rootOrgId, Status.OFF, standItemIds);
            if (exeItemVerIds2 != null && exeItemVerIds2.size()>0){
                allItemVerIds.addAll(exeItemVerIds2);
            }
        }
        // 阶段实施事项
        List<String> exeItemVerIds = aeaParStageItemMapper.listItemVerIdsNoRepeatByThemeVerId(themeVerId, null, Status.OFF, rootOrgId);
        if (exeItemVerIds != null && exeItemVerIds.size()>0){
            allItemVerIds.addAll(exeItemVerIds);
        }
        return allItemVerIds;
    }

    private void convertInoutToNode(String themeVerId, List<AeaItemInout> inouts, List<ZtreeNode> allNodes){

        if(inouts!=null&&inouts.size()>0){
            ZtreeNode itemNode;
            for(AeaItemInout inout:inouts){
                itemNode = new ZtreeNode();
                itemNode.setId(inout.getItemVerId());
                itemNode.setIcon(inout.getStateVerId());
                itemNode.setName("【"+ inout.getIsCatalog()+ "】" + inout.getItemName()+ "【"+ inout.getOrgName()+ "】");
                itemNode.setpId(themeVerId);
                itemNode.setType("item");
                itemNode.setOpen(true);
                itemNode.setIsParent(false);
                itemNode.setNocheck(true);
                allNodes.add(itemNode);
            }
        }
    }

    @Override
    public void saveSharemat(AeaParShareMat aeaParShareMat){

        if(aeaParShareMat!=null){
            if(StringUtils.isBlank(aeaParShareMat.getThemeVerId())){
                throw new InvalidParameterException("传递themeVerId参数!");
            }
            if(StringUtils.isBlank(aeaParShareMat.getOutItemVerId())){
                throw new InvalidParameterException("传递outItemVerId参数为空!");
            }
            if(StringUtils.isBlank(aeaParShareMat.getOutInoutId())){
                throw new InvalidParameterException("传递outInoutId参数为空!");
            }
            if(StringUtils.isBlank(aeaParShareMat.getInItemVerId())){
                throw new InvalidParameterException("传递inItemVerId参数为空!");
            }
            if(StringUtils.isBlank(aeaParShareMat.getInItemStateVerId())){
                throw new InvalidParameterException("传递inItemStateVerId参数为空!");
            }
//            if(aeaParShareMat.getInInoutIds()==null){
//                throw new InvalidParameterException("传递inInoutIds参数为空!");
//            }
//            if(aeaParShareMat.getInInoutIds()!=null&&aeaParShareMat.getInInoutIds().length==0){
//                throw new InvalidParameterException("传递inInoutIds参数为空!");
//            }
            String[] inInoutIds = aeaParShareMat.getInInoutIds();
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            // 先删除
            AeaParShareMat shareMatMat = new AeaParShareMat();
            shareMatMat.setRootOrgId(rootOrgId);
            shareMatMat.setThemeVerId(aeaParShareMat.getThemeVerId());
            shareMatMat.setOutItemVerId(aeaParShareMat.getOutItemVerId());
            shareMatMat.setOutInoutId(aeaParShareMat.getOutInoutId());
            shareMatMat.setInItemVerId(aeaParShareMat.getInItemVerId());
            shareMatMat.setInItemStateVerId(aeaParShareMat.getInItemStateVerId());
            aeaParShareMatMapper.batchDelAeaParShareMatByCond(shareMatMat);
            // 后创建
            if(inInoutIds!=null&&inInoutIds.length>0){
                for(String inInoutId : inInoutIds){
                    shareMatMat = new AeaParShareMat();
                    shareMatMat.setShareMatId(UUID.randomUUID().toString());
                    shareMatMat.setThemeVerId(aeaParShareMat.getThemeVerId());
                    shareMatMat.setOutItemVerId(aeaParShareMat.getOutItemVerId());
                    shareMatMat.setOutInoutId(aeaParShareMat.getOutInoutId());
                    shareMatMat.setInItemVerId(aeaParShareMat.getInItemVerId());
                    shareMatMat.setInItemStateVerId(aeaParShareMat.getInItemStateVerId());
                    shareMatMat.setInInoutId(inInoutId);
                    shareMatMat.setIsActive(Status.ON);
                    shareMatMat.setCreater(userId);
                    shareMatMat.setCreateTime(new Date());
                    shareMatMat.setRootOrgId(rootOrgId);
                    aeaParShareMatMapper.saveAeaParShareMat(shareMatMat);
                }
            }
        }else{
            throw new InvalidParameterException("未传递参数!");
        }
    }

    @Override
    public List<AeaParShareMat> inOutCheckboxList(String themeVerId, String outInoutId){

        return  aeaParShareMatMapper.inOutCheckboxList(themeVerId, outInoutId);
    }
}
