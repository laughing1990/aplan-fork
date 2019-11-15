package com.augurit.aplanmis.common.service.item.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.AeaItemBasicContants;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemFrontItem;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontItemAdminService;
import com.augurit.aplanmis.common.service.diagram.constant.HandleStatus;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.vo.PreItemCheckResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class AeaItemBasicServiceImpl implements AeaItemBasicService {

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    public static final String ORG_PROPERTY = "d";

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    private AeaItemFrontItemAdminService aeaItemFrontAdminService;

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Override
    public List<AeaItemBasic> getAeaItemBasicListByStageId(String stageId, String isOptionItem, String projInfoId, String rootOrgId) throws Exception {

        if (StringUtils.isBlank(stageId)){
            throw new InvalidParameterException("参数stageId为空！");
        }
        List<AeaItemBasic> list = aeaItemBasicMapper.getAeaItemBasicListByStageId(stageId, isOptionItem, rootOrgId);
        //增加核心事项是否已申报
        if (StringUtils.isNotBlank(projInfoId) && list.size() > 0) {//判断是否已办
            Map<String, HandleStatus> itemHandleStatus = aeaHiIteminstService.queryItemStatusByStageIdAndProjInfoId(Collections.singletonList(stageId), projInfoId, rootOrgId);
            for (AeaItemBasic basic : list) {
                HandleStatus handleStatus = itemHandleStatus.get(basic.getItemId());
                if (handleStatus != null) {
                    basic.setIsDone(handleStatus.getValue());
                } else {
                    basic.setIsDone(HandleStatus.UN_FINISHED.getValue());
                }
                if ("1".equals(isOptionItem)) {
                    // 是否推荐
                    basic.setIsRecommended(basic.getDueNum() > 5 ? "1" : "0");
                }
            }
        }
        return list;
    }

    @Override
    public List<AeaItemBasic> getAeaItemBasicListByStageIdAndStateId(String stageId, String stateId, String isOptionItem, String rootOrgId) throws Exception {

        if (StringUtils.isBlank(stageId)) {
            throw new InvalidParameterException("参数stageId为空！");
        }
        return aeaItemBasicMapper.getAeaItemBasicListByStageIdAndStateId(stageId, stateId, isOptionItem, rootOrgId);
    }

    @Override
    public AeaItemBasic getAeaItemBasicByItemVerId(String itemVerId) throws Exception {

        if (StringUtils.isBlank(itemVerId)) {
            throw new InvalidParameterException("参数itemVerId为空！");
        }
        return aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, SecurityContext.getCurrentOrgId());
    }

    @Override
    public List<AeaItemBasic> getAeaItemBasicListByOrgId(String orgId) throws Exception {
        return aeaItemBasicMapper.getAeaItemBasicListByOrgId(orgId);
    }

    @Override
    public List<AeaItemBasic> getAeaItemBasicListByOrgId(String orgId,String isCatalog) throws Exception {
        return aeaItemBasicMapper.getAeaItemBasicListByOrgIdAndIsCatalog(orgId,isCatalog);
    }

    @Override
    public void saveAeaItemBasic(AeaItemBasic aeaItemBasic) throws Exception {
        if (StringUtils.isBlank(aeaItemBasic.getItemBasicId())) {//新增
            aeaItemBasic.setItemBasicId(UUID.randomUUID().toString());
            aeaItemBasicMapper.insertAeaItemBasic(aeaItemBasic);
        } else {//编辑
            aeaItemBasicMapper.updateAeaItemBasic(aeaItemBasic);
        }
    }

    @Override
    public void deleteAeaItemBasic(String id) throws Exception {

        if (org.apache.commons.lang.StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空！");
        }
        aeaItemBasicMapper.deleteAeaItemBasic(id);
    }

    @Override
    public List<AeaItemBasic> listAeaItemBasic(AeaItemBasic aeaItemBasic) throws Exception {

        aeaItemBasic.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaItemBasicMapper.listAeaItemBasic(aeaItemBasic);
    }

    @Override
    public AeaItemBasic getAeaItemBasicById(String id) throws Exception {

        if (org.apache.commons.lang.StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空！");
        }
        return aeaItemBasicMapper.getAeaItemBasicById(id);
    }

    @Override
    public PageInfo<AeaItemBasic> listAeaItemBasic(AeaItemBasic aeaItemBasic, int pageNum, int pageSize, String rootOrgId) throws Exception {

        aeaItemBasic.setRootOrgId(rootOrgId);
        aeaItemBasic.setIsCatalog(AeaItemBasicContants.IS_CATALOG_NO);
        PageHelper.startPage(pageNum, pageSize);
        List<AeaItemBasic> list = aeaItemBasicMapper.listAeaItemBasic(aeaItemBasic);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AeaItemBasic> getAeaItemBasicListByOrgId(String orgId, int pageNum, int pageSize) throws Exception {

        PageHelper.startPage(pageNum, pageSize);
        List<AeaItemBasic> list = aeaItemBasicMapper.getAeaItemBasicListByOrgId(orgId);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AeaItemBasic> getAeaItemBasicListByOrgId(String orgId,String isCatalog, int pageNum, int pageSize) throws Exception {

        PageHelper.startPage(pageNum, pageSize);
        List<AeaItemBasic> list = aeaItemBasicMapper.getAeaItemBasicListByOrgIdAndIsCatalog(orgId,isCatalog);
        return new PageInfo<>(list);
    }

    /**
     * xiaohutu----2019-07-19新增
     * 根据情形ID插叙绑定的事项列表
     *
     * @param stateId 情形ID
     * @return
     */
    @Override
    public List<AeaItemBasic> listAeaParStateItemByStateId(String stateId) throws Exception {
        List<AeaItemBasic> list = aeaItemBasicMapper.listAeaParStateItemByStateId(stateId);
        return list;
    }

    @Override
    public List<AeaItemBasic> getSssxByItemIdAndRegionalism(String itemId, String regionalism, String[] projectAddressArr, String rootOrgId) {

        AeaItemBasic query=new AeaItemBasic();
        query.setParentItemId(itemId);
        query.setRootOrgId(rootOrgId);
        if (projectAddressArr != null && projectAddressArr.length > 0 && StringUtils.isNotBlank(regionalism)) {
            query.setRegionIds(ArrayUtils.addAll(projectAddressArr, new String[]{regionalism}));
        } else if (projectAddressArr != null && projectAddressArr.length > 0 && StringUtils.isBlank(regionalism)) {
            query.setRegionIds(projectAddressArr);
        } else if ((projectAddressArr == null || projectAddressArr.length == 0) && StringUtils.isNotBlank(regionalism)) {
            query.setRegionId(regionalism);
        }
        List<AeaItemBasic> list=aeaItemBasicMapper.listLatestOkAeaItemBasic(query);
        return list;
    }

    @Override
    public void conversionBasicItemToSssx(List<AeaItemBasic> itemList, String regionalism, String projectAddress, String rootOrgId) {

        if(itemList.size()==0 || StringUtils.isBlank(regionalism) ||"null".equalsIgnoreCase(regionalism)) {
            return;
        }
        for (AeaItemBasic aeaItemBasic:itemList){
            String flag=aeaItemBasic.getIsCatalog();
            if("1".equals(flag)){
                List<String> arrRegionIdList = new ArrayList<>();
                if ("0".equals(aeaItemBasic.getItemExchangeWay()) && StringUtils.isNotBlank(projectAddress)) {//itemExchangeWay 实施事项换算方式 0 按照审批行政区划和属地行政区划换算 1 仅按照审批行政区划换算
                    String[] arrRegionIds = projectAddress.split(",");
                    for (String id : arrRegionIds) {
                        arrRegionIdList.add(id);
                    }
                }
                List<AeaItemBasic> sssxList = this.getSssxByItemIdAndRegionalism(aeaItemBasic.getItemId(), regionalism, arrRegionIdList.size() == 0 ? null : CommonTools.ListToArr(arrRegionIdList), rootOrgId);
                aeaItemBasic.setCarryOutItems(sssxList);
                AeaItemBasic sssx=sssxList.size()>0?sssxList.get(0):null;
                if(sssx!=null){
                    aeaItemBasic.setCurrentCarryOutItem(sssx);
                    aeaItemBasic.setBaseItemVerId(aeaItemBasic.getItemVerId());
                    aeaItemBasic.setBaseItemName(aeaItemBasic.getItemName());
                    //BeanUtils.copyProperties(sssx,vo);
                    //vo.setIsCatalog(flag);
                }
            }
        }
    }

    @Override
    public List<OpuOmOrg> listOpuOmOrgByAeaItemBasic(String rootOrgId) throws Exception {

        // 顶级机构下的部门
        OpuOmOrg topOrg = opuOmOrgMapper.getActiveOrg(rootOrgId);
        OpuOmOrg opuOmOrg = new OpuOmOrg();
        if (topOrg == null) {
            return new ArrayList<>();
        }
        opuOmOrg.setIsActive(Status.ON);
        opuOmOrg.setOrgProperty(ORG_PROPERTY);
        opuOmOrg.setParentOrgId(topOrg.getOrgId());

        return aeaItemBasicMapper.listOpuOmOrgByAllPropJoinItem(opuOmOrg, rootOrgId);
        /*List<OpuOmOrg> orgTempList = opuOmOrgMapper.listOpuOmOrgByAllProp(opuOmOrg);
        //根据rootOrgId和opuOmOrg查询组织列表
        Set<OpuOmOrg> orgSet = new HashSet<>();
        List<OpuOmOrg> orgList = new ArrayList<>();
        AeaItemBasic basic = new AeaItemBasic();
        basic.setRootOrgId(rootOrgId);
        List<AeaItemBasic> itemList = aeaItemBasicMapper.listAeaItemBasic(basic);
        for (AeaItemBasic item : itemList) {
            for (OpuOmOrg org1 : orgTempList) {
                if (org1.getOrgId().equals(item.getOrgId())) {
                    if (orgSet.add(org1)) {
                        orgList.add(org1);
                    }
                }
            }
        }*/
        //return orgList;
    }

    /**
     * 根据实施事项的itemId查找标准事项
     *
     * @param itemId 事项id
     */
    @Override
    public AeaItemBasic getCatalogItemByCarryOutItemId(String itemId) {
        AeaItemBasic parentAeaItemBasic = aeaItemBasicMapper.getLatestParentAeaItemBasicByChildItemId(itemId, SecurityContext.getCurrentOrgId());
        while (parentAeaItemBasic != null && !"1".equals(parentAeaItemBasic.getIsCatalog())) {
            parentAeaItemBasic = aeaItemBasicMapper.getLatestParentAeaItemBasicByChildItemId(parentAeaItemBasic.getItemId(), SecurityContext.getCurrentOrgId());
        }
        return parentAeaItemBasic;
    }

    @Override
    public List<OpuOmOrg> listOpuOmOrgByAeaItemBasic1(String parentOrgId) throws Exception {

        if (StringUtils.isNotBlank(parentOrgId)) {
            OpuOmOrg search = new OpuOmOrg();
            search.setOrgProperty("d");
            search.setParentOrgId(parentOrgId);
            List<OpuOmOrg> opuOmOrgList = this.opuOmOrgMapper.listOpuOmOrgByAllProp(search);
            if (opuOmOrgList != null && opuOmOrgList.size() > 0) {
                for (int i = 0; i < opuOmOrgList.size(); ++i) {
                    this.convertOrgNameSeq((OpuOmOrg) opuOmOrgList.get(i));
                }
            }

            return opuOmOrgList;
        }
        return new ArrayList<>();
    }

    @Override
    public PreItemCheckResultVo checkPreItemsPassed(String itemVerId, String projInfoId) {

        Assert.notNull(itemVerId, "itemVerId must not null");
        Assert.notNull(projInfoId, "projInfoId must not null");

        PreItemCheckResultVo preItemCheckResultVo = new PreItemCheckResultVo();
        List<AeaItemFrontItem> aeaItemFronts = aeaItemFrontAdminService.listItemsByItemVerId(itemVerId);
        if (aeaItemFronts != null && aeaItemFronts.size() > 0) {
            List<String> frontItemVerIds = aeaItemFronts.stream().map(AeaItemFrontItem::getFrontCkItemVerId).collect(Collectors.toList());
            List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstMapper.listAeaHiIteminstByProjInfoIdAndItemVerIds(projInfoId, frontItemVerIds, SecurityContext.getCurrentOrgId());
            preItemCheckResultVo.buildPreItemStates(aeaHiIteminstList);
            preItemCheckResultVo.setPreItems(aeaItemFronts);
        }
        return preItemCheckResultVo;
    }

    @Override
    public List<AeaItemBasic> frontItemIsDone(String itemVerId, String projInfoId) {

        Assert.notNull(itemVerId, "itemVerId must not null");
        Assert.notNull(projInfoId, "projInfoId must not null");
        List<AeaItemFrontItem> aeaItemFronts = aeaItemFrontAdminService.listItemsByItemVerId(itemVerId);
        if (aeaItemFronts.size() == 0){
            return new ArrayList<>();
        }
        List<String> frontItemVerIds = aeaItemFronts.stream().map(AeaItemFrontItem::getFrontCkItemVerId).collect(Collectors.toList());
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstMapper.listAeaHiIteminstByProjInfoIdAndItemVerIds(projInfoId, frontItemVerIds, SecurityContext.getCurrentOrgId());
        List<String> unDoItemList = getUndoItemList(aeaHiIteminstList, frontItemVerIds);
        return unDoItemList.size() > 0 ? aeaItemBasicMapper.getAeaItemBasicListByItemVerIds(unDoItemList) : new ArrayList<>();
    }

    private List<String> getUndoItemList(List<AeaHiIteminst> aeaHiIteminstList, List<String> frontItemVerIds) {

        List<String> unDoItemList = frontItemVerIds;
        if (aeaHiIteminstList.size() == 0) {
            return unDoItemList;
        }
        if (aeaHiIteminstList.size() > 0 && frontItemVerIds.size() > 0) {
            for (AeaHiIteminst aeaHiIteminst : aeaHiIteminstList) {
                if (unDoItemList.contains(aeaHiIteminst.getItemVerId())) {
                    unDoItemList.remove(aeaHiIteminst.getItemVerId());
                }
            }
        }
        return unDoItemList;
    }


    private void convertOrgNameSeq(OpuOmOrg org) {
        if (org != null && StringUtils.isNotBlank(org.getOrgSeq())) {
            String[] ids = org.getOrgSeq().split("\\.");
            List<String> idList = new ArrayList();
            int i = 0;

            for (int len = ids.length; i < len; ++i) {
                String id = ids[i];
                if (StringUtils.isNotBlank(id)) {
                    idList.add(id);
                }

                if (id.equals(org.getOrgId())) {
                    break;
                }
            }

            List<OpuOmOrg> list = this.opuOmOrgMapper.getOrgNameByOrgIds((String[]) idList.toArray(new String[idList.size()]));
            if (list != null && list.size() > 0) {
                StringBuilder sb = new StringBuilder();
                int k = 0;

                for (int len = ids.length; k < len; ++k) {
                    String id = ids[k];
                    int j = 0;

                    for (int length = list.size(); j < length; ++j) {
                        if (((OpuOmOrg) list.get(j)).getOrgId().equals(id)) {
                            sb.append(((OpuOmOrg) list.get(j)).getOrgName()).append(k == len - 1 ? "" : ",");
                            break;
                        }
                    }
                }

                org.setOrgNameSeq(sb.toString());
            }
        }

    }
}
