package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaHiParStateinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaParIn;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiParStageinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiParStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemInoutMapper;
import com.augurit.aplanmis.common.mapper.AeaParInMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageItemMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutinstService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author xiaohutu
 */
@Service
public class AeaHiItemInoutinstServiceImpl implements AeaHiItemInoutinstService {
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaHiParStateinstMapper aeaHiParStateinstMapper;
    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;
    @Autowired
    private AeaParInMapper aeaParInMapper;
    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;
    @Autowired
    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    /**
     * 材料输入输出实例
     *
     * @param matinstsIds 材料实例ID
     * @param applyinstId 申报实例id
     * @param creater     创建人
     * @return 插入的条数
     */
    @Override
    public int batchInsertAeaHiItemInoutinst(String[] matinstsIds, String applyinstId, String creater) throws Exception {
        if (StringUtils.isEmpty(applyinstId) || null == matinstsIds || matinstsIds.length == 0) {
            return 0;
        }
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        if (null == aeaHiApplyinst) {
            return 0;
        }
        String isSeriesApprove = aeaHiApplyinst.getIsSeriesApprove();
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applyinstId);
        if (aeaHiIteminstList.size() == 0) {
            return 0;
        }
        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.listAeaHiItemMatinstByIds(matinstsIds);

        List<AeaHiItemInoutinst> list = new ArrayList<>();

        if ("1".equals(isSeriesApprove)) {
            // 单项申报输入材料实例
            list = buidItemInoutinst4Item(applyinstId, creater, aeaHiIteminstList, aeaHiItemMatinsts);

        } else if ("0".equals(isSeriesApprove)) {
            // 并联申报输入材料实例
            list = buildItemInoutinst4Stage(applyinstId, creater, aeaHiIteminstList, aeaHiItemMatinsts);
            AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
            if (aeaHiParStageinst != null && "0".equals(aeaHiParStageinst.getHandWay())) {
                // 如果阶段是简单合并办理， 此时还要保存属于事项的材料！！！
                // 因为简单合并的情况下， 事项的材料与阶段输入表【可能】是没关联的 aea_par_in， 仅仅与 aea_item_inout 关联
                // 如果既有阶段材料， 又有事项材料， 在前端传过来的应该是去重后的材料实例列表
                list.addAll(buidItemInoutinst4Item(applyinstId, creater, aeaHiIteminstList, aeaHiItemMatinsts));
            }
        }
        if (list.size() > 0) {
            return aeaHiItemInoutinstMapper.batchInsertAeaHiItemInoutinst(list);
        }
        return 0;
    }

    @Override
    public List<AeaHiItemInoutinst> getAeaHiItemInoutinstByIteminstIds(String[] oldIteminstIds) {
        if(oldIteminstIds.length>0) return aeaHiItemInoutinstMapper.getAeaHiItemInoutinstByIteminstIds(oldIteminstIds);
        return new ArrayList<>();
    }

    @Override
    public void batchDeleteAeaHiItemInoutinst(String[] outinstIds){
        if(outinstIds.length>0)
            aeaHiItemInoutinstMapper.batchDeleteAeaHiItemInoutinst(outinstIds);
    }

    @Override
    public void updateAeaHiItemInoutinst(AeaHiItemInoutinst inoutinst) throws Exception {
        if(StringUtils.hasText(inoutinst.getInoutinstId())){
            inoutinst.setModifier(SecurityContext.getCurrentUserName());
            inoutinst.setModifyTime(new Date());
            aeaHiItemInoutinstMapper.updateAeaHiItemInoutinst(inoutinst);
        }
    }

    private List<AeaHiItemInoutinst> buildItemInoutinst4Stage(String applyinstId, String creater, List<AeaHiIteminst> aeaHiIteminstList, List<AeaHiItemMatinst> aeaHiItemMatinsts) throws Exception {
        List<AeaHiItemInoutinst> list = new ArrayList<>();
        AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(aeaHiParStageinst.getStageId());

        //查询选择的情形实例数据
        List<AeaHiParStateinst> aeaHiParStateinsts = aeaHiParStateinstMapper.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, null);
        List<String> selectedStateIds = Optional.ofNullable(aeaHiParStateinsts)
                .orElse(new ArrayList<>()).stream().map(AeaHiParStateinst::getExecStateId).collect(Collectors.toList());

        String stageId = aeaHiParStageinst.getStageId();
        for (AeaHiIteminst aeaHiIteminst : aeaHiIteminstList) {
            String iteminstId = aeaHiIteminst.getIteminstId();
            String itemId = aeaHiIteminst.getItemId();
            // 查找对应的标准事项
            AeaItemBasic catalogItem = aeaItemBasicService.getCatalogItemByCarryOutItemId(aeaHiIteminst.getItemId(),null);
            if (catalogItem == null) continue;
            String catalogItemId = catalogItem.getItemId();

            for (AeaHiItemMatinst aeaHiItemMatinst : aeaHiItemMatinsts) {
                String matinstId = aeaHiItemMatinst.getMatinstId();
                String matId = aeaHiItemMatinst.getMatId();
                //查找并联输入输出定义
                Set<AeaParIn> parInList = new HashSet<>();
                AeaParIn parIn = new AeaParIn();
                parIn.setMatId(matId);
                parIn.setStageId(stageId);
                //直接事项输入材料
                parIn.setIsStateIn("0");
                parIn.setRootOrgId(SecurityContext.getCurrentOrgId());
                List<AeaParIn> aeaParIns = aeaParInMapper.listAeaParIn(parIn);
                if (aeaParIns.size() > 0) {
                    parInList.addAll(aeaParIns);
                }
                //情形材料输入---只保留已选择的情形下的aea_par_in数据
                parIn.setIsStateIn("1");
                List<AeaParIn> stateParInList = aeaParInMapper.listAeaParIn(parIn);

                // 如果是不分情形时，只要有情形材料就生成实例
                if ("0".equals(aeaParStage.getIsNeedState())) {
                    parInList.addAll(stateParInList);
                } else {
                    if (selectedStateIds.size() > 0) {
                        List<AeaParIn> stateParInListCopy = stateParInList.stream().filter(s -> selectedStateIds.contains(s.getParStateId())).collect(Collectors.toList());
                        if (stateParInListCopy.size() > 0) {
                            parInList.addAll(stateParInListCopy);
                        }
                    }
                }
                //查找对应的阶段事项 aea_par_stage_item
                if (parInList.size() > 0) {
                    String[] parInIds = parInList.stream().map(AeaParIn::getInId).toArray(String[]::new);
                    List<AeaParStageItem> stageItemList = aeaParStageItemMapper.listAeaStageItemByParInIds(parInIds);
                    for (AeaParStageItem aeaParStageItem : stageItemList) {
                        // 申报的都是实施事项，所以要判断 对应的事项版本 或 标准事项是否匹配
                        if (aeaParStageItem.getItemId().equals(itemId) || catalogItemId.equals(aeaParStageItem.getItemId())) {
                            AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
                            aeaHiItemInoutinst.setInoutinstId(UUID.randomUUID().toString());
                            aeaHiItemInoutinst.setIteminstId(iteminstId);
                            aeaHiItemInoutinst.setMatinstId(matinstId);
                            aeaHiItemInoutinst.setIsCollected("1");
                            aeaHiItemInoutinst.setIsParIn("1");
                            aeaHiItemInoutinst.setParInId(aeaParStageItem.getParInId());
                            aeaHiItemInoutinst.setCreater(creater);
                            aeaHiItemInoutinst.setCreateTime(new Date());
                            aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                            list.add(aeaHiItemInoutinst);
                        }
                    }
                }
            }
        }
        return list;
    }

    private List<AeaHiItemInoutinst> buidItemInoutinst4Item(String applyinstId, String creater, List<AeaHiIteminst> aeaHiIteminstList, List<AeaHiItemMatinst> aeaHiItemMatinsts) throws Exception {
        List<AeaHiItemInoutinst> list = new ArrayList<>();
        List<AeaHiItemStateinst> aeaHiItemStateinsts = aeaHiItemStateinstMapper.listAeaHiItemStateinstByApplyinstIdOrSeriesinstId(applyinstId, null);
        List<String> stateIdList = Optional.ofNullable(aeaHiItemStateinsts).orElse(new ArrayList<>()).stream().map(AeaHiItemStateinst::getExecStateId).collect(Collectors.toList());
        //单项申报-并联申报并行推进事项一个申请实例可能对应多个事项实例和单事项实例
        for (AeaHiIteminst aeaHiIteminst : aeaHiIteminstList) {
            String iteminstId = aeaHiIteminst.getIteminstId();
            String itemVerId = aeaHiIteminst.getItemVerId();

            //查询当前事项的aea_item_inout定义
            for (AeaHiItemMatinst aeaHiItemMatinst : aeaHiItemMatinsts) {
                Set<AeaItemInout> inoutSet = new HashSet<>();

                String matinstId = aeaHiItemMatinst.getMatinstId();
                String matId = aeaHiItemMatinst.getMatId();
                AeaItemInout inout = new AeaItemInout();
                inout.setIsInput("1");
                inout.setMatId(matId);
                inout.setItemVerId(itemVerId);
                inout.setIsDeleted("0");
                inout.setIsStateIn("0");//事项输入材料
                String currentOrgId = SecurityContext.getCurrentOrgId();
                inout.setRootOrgId(currentOrgId);
                List<AeaItemInout> aeaItemInouts = aeaItemInoutMapper.listAeaItemInout(inout);
                if (aeaItemInouts.size() > 0) {
                    inoutSet.addAll(aeaItemInouts);
                }
                inout.setIsStateIn("1");//情形输入材料
                List<AeaItemInout> aeaItemInouts1 = aeaItemInoutMapper.listAeaItemInout(inout);
                if (aeaItemInouts1.size() > 0) {
                    for (AeaItemInout inout1 : aeaItemInouts1) {
                        if (stateIdList.contains(inout1.getItemStateId()))
                            inoutSet.add(inout1);
                    }
                }

                for (AeaItemInout inoutTemp : inoutSet) {
                    AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
                    aeaHiItemInoutinst.setInoutinstId(UUID.randomUUID().toString());
                    aeaHiItemInoutinst.setIteminstId(iteminstId);
                    aeaHiItemInoutinst.setItemInoutId(inoutTemp.getInoutId());
                    aeaHiItemInoutinst.setMatinstId(matinstId);
                    aeaHiItemInoutinst.setIsCollected("1");
                    //是否阶段情形输入，0表示单个事项输入，1表示阶段情形输入
                    aeaHiItemInoutinst.setIsParIn("0");
                    aeaHiItemInoutinst.setCreater(creater);
                    aeaHiItemInoutinst.setCreateTime(new Date());
                    aeaHiItemInoutinst.setRootOrgId(currentOrgId);
                    list.add(aeaHiItemInoutinst);
                }
            }
        }
        return list;
    }
}
