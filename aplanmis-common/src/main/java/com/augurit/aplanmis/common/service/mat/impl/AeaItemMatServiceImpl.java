package com.augurit.aplanmis.common.service.mat.impl;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaItemInoutMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMatMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageItemMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import com.augurit.aplanmis.common.utils.CommonTools;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 事项材料定义表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:chenzh</li>
 * <li>创建时间：2019-07-08 16:31:14</li>
 * </ul>
 */
@Service
@Transactional
public class AeaItemMatServiceImpl implements AeaItemMatService {

    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;

    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;

    @Autowired
    private AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    private AeaParStageService aeaParStageService;

    @Autowired
    private AeaHiParStateinstService aeaHiParStateinstService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;
    @Autowired
    private BscAttMapper bscAttMapper;

    //根据事项ID获取（输入或输出）材料列表
    @Override
    public List<AeaItemMat> getMatListByItemVerIds(String[] itemVerIds, String isInput, String isNeedStateMat) throws Exception {
        if (itemVerIds.length < 1) throw new Exception("事项版本ID数组长度为0");
        if (StringUtils.isBlank(isInput)) throw new Exception("isInput不能为空");
        return aeaItemMatMapper.getMatListByItemVerIds(itemVerIds, isInput, isNeedStateMat);
    }

    //获取阶段下某个事项所关联的材料列表
    @Override
    public List<AeaItemMat> getMatListByItemVerIdAndStageId(String itemVerId, String stageId, String isNeedStateMat, String rootOrgId) throws Exception {
        if (StringUtils.isBlank(itemVerId)) throw new Exception("事项版本ID不能为空！");
        if (StringUtils.isBlank(stageId)) throw new Exception("阶段ID不能为空！");
        if (StringUtils.isBlank(rootOrgId)) {
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        return aeaItemMatMapper.getMatListByItemVerIdAndStageId(itemVerId, stageId, isNeedStateMat, rootOrgId);
    }

    @Override
    public List<AeaItemMat> getMatListByItemVerIdsAndStageId(String[] itemVerIds, String stageId, String isNeedStateMat) throws Exception {
        if (itemVerIds == null || itemVerIds.length == 0) throw new Exception("事项版本ID不能为空！");
        if (StringUtils.isBlank(stageId)) throw new Exception("阶段ID不能为空！");
        return aeaItemMatMapper.getMatListByItemVerIdsAndStageId(itemVerIds, stageId, isNeedStateMat);
    }


    //根据事项实例ID获取（输入或输出）材料列表
    @Override
    public List<AeaItemMat> getMatListByIteminstId(String iteminstId, String isInput, String isNeedStateMat) throws Exception {
        if (StringUtils.isBlank(iteminstId)) throw new Exception("事项实例ID不能为空");
        if (StringUtils.isBlank(isInput)) throw new Exception("isInput不能为空");
        return aeaItemMatMapper.getMatListByIteminstId(iteminstId, isInput, isNeedStateMat);
    }

    //根据阶段ID获取材料列表
    @Override
    public List<AeaItemMat> getMatListByStageId(String stageId, String isNeedStateMat) throws Exception {
        if (StringUtils.isBlank(stageId)) throw new Exception("阶段ID不能为空");
        return aeaItemMatMapper.getMatListByStageId(stageId, isNeedStateMat);
    }

    // 获取直接挂在阶段下的材料
    @Override
    public List<AeaItemMat> listMatListByStageId(String stageId, List<String> itemVerIds) throws Exception {
        if (StringUtils.isBlank(stageId)) throw new Exception("阶段ID不能为空");
        return aeaItemMatMapper.listMatListByStageId(stageId, itemVerIds);
    }

    //根据并联情形ID获取材料列表
    @Override
    public List<AeaItemMat> getMatListByStageStateIds(String[] StageStateId) throws Exception {
        if (StageStateId.length < 1) throw new Exception("并联情形数组长度为0");
        return aeaItemMatMapper.getMatListByStageStateIds(StageStateId);
    }

    //根据并联情形ID和事项版本ID获取（输入或输出）材料列表
    @Override
    public List<AeaItemMat> getMatListByStageStateIdsAndItemVerId(String[] StageStateId, String itemVerId) throws Exception {
        if (StageStateId.length < 1) throw new Exception("并联情形数组长度为0");
        if (StringUtils.isBlank(itemVerId)) throw new Exception("事项版本ID不能为空！");
        return aeaItemMatMapper.getMatListByStageStateIdsAndItemVerId(StageStateId, itemVerId);

    }

    //根据并联情形ID和材料实例ID获取（输入或输出）材料列表
    @Override
    public List<AeaItemMat> getMatListByStageStateIdsAndMatinstId(String[] StageStateId, String matinstId) throws Exception {
        if (StageStateId.length < 1) throw new Exception("并联情形数组长度为0");
        if (StringUtils.isBlank(matinstId)) throw new Exception("材料实例ID不能为空！");
        return aeaItemMatMapper.getMatListByStageStateIdsAndMatinstId(StageStateId, matinstId);

    }

    //根据串联情形ID获取材料列表
    @Override
    public List<AeaItemMat> getMatListByItemStateIds(String[] ItemStateId) throws Exception {
        if (ItemStateId.length < 1) throw new Exception("单项情形数组长度为0");
        return aeaItemMatMapper.getMatListByItemStateIds(ItemStateId);
    }

    @Override
    public List<AeaItemMat> getMatListByApplyinstId(String applyinstId, String iteminstId) throws Exception {
        String stageId = "";
        List<AeaItemState> itemStateList = new ArrayList<>();
        String[] stageStateIds = null;
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstIdAndIteminstId(applyinstId, iteminstId);
        List<String> baseItemVerIds = new ArrayList<>();
        List<String> coreItemVerIds = new ArrayList<>();
        List<String> parallelItemVerIds = new ArrayList<>();
        if ("1".equals(applyinst.getIsSeriesApprove())) {//单项
            itemStateList = aeaHiItemStateinstService.listAeaItemStateByApplyinstIdOrSeriesinstId(applyinstId, null);
            coreItemVerIds = iteminstList.size() > 0 ? iteminstList.stream().map(AeaHiIteminst::getItemVerId).collect(Collectors.toList()) : new ArrayList<>();
        } else {//并联
            AeaHiParStageinst stageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinstId);
            stageId = stageinst == null ? "" : stageinst.getStageId();
            AeaParStage stage = aeaParStageService.getAeaParStageById(stageId);
            if ("1".equals(stage.getHandWay())) {// 按阶段多级情形组织事项办理
                List<AeaHiParStateinst> parStateinstList = aeaHiParStateinstService.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, stageinst.getStageinstId());
                stageStateIds = parStateinstList.size() > 0 ? parStateinstList.stream().map(AeaHiParStateinst::getExecStateId).toArray(String[]::new) : new String[]{};
                parallelItemVerIds = iteminstList.size() > 0 ? iteminstList.stream().map(AeaHiIteminst::getItemVerId).collect(Collectors.toList()) : new ArrayList<>();
            } else {//多事项直接合并办理
                itemStateList = aeaHiItemStateinstService.listAeaItemStateByApplyinstIdOrSeriesinstId(applyinstId, null);
                //当iteminstId不为Null时，情形ID需要过滤
                if (StringUtils.isNotBlank(iteminstId)) {
                    AeaHiIteminst iteminst = iteminstList.get(0);
                    itemStateList = itemStateList.stream().filter(AeaItemState -> iteminst.getItemVerId().contains(AeaItemState.getItemVerId())).collect(Collectors.toList());
                }
                coreItemVerIds = iteminstList.size() > 0 ? iteminstList.stream().map(AeaHiIteminst::getItemVerId).collect(Collectors.toList()) : new ArrayList<>();
            }

            AeaParStageItem aeaParStageItem = new AeaParStageItem();
            aeaParStageItem.setStageId(stageinst.getStageId());
            List<AeaParStageItem> stageItems = aeaParStageItemMapper.listAeaParStageItem(aeaParStageItem);

            iteminstList.stream().forEach(iteminst -> {
                boolean flag = false;
                for (AeaParStageItem stageItem : stageItems) {
                    if (stageItem.getItemVerId().equals(iteminst.getItemVerId())) {
                        baseItemVerIds.add(iteminst.getItemVerId());
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    //根据实施事项Id获取标准事项
                    AeaItemBasic parentAeaItemBasic = aeaItemBasicService.getCatalogItemByCarryOutItemId(iteminst.getItemId(),null);
                    if (parentAeaItemBasic != null) baseItemVerIds.add(parentAeaItemBasic.getItemVerId());
                }
            });
        }
        String[] itemStateIds = itemStateList.size() > 0 ? itemStateList.stream().map(AeaItemState::getItemStateId).toArray(String[]::new) : new String[]{};
        return getMatListByStateListAndItemListAndStageId(itemStateIds, stageStateIds, coreItemVerIds.toArray(new String[coreItemVerIds.size()]), parallelItemVerIds.toArray(new String[parallelItemVerIds.size()]), null, baseItemVerIds.toArray(new String[baseItemVerIds.size()]), stageId,null);
    }

    @Override
    public List<AeaItemMat> getMatListByApplyinstIdContainsMatinst(String applyinstId, String iteminstId) throws Exception {
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        List<AeaItemMat> matList = new ArrayList<>();
        List<AeaItemMat> preItemInout = new ArrayList<>();

        if ("undefined".equals(iteminstId) || StringUtils.isBlank(iteminstId) || "null".equals(iteminstId))
            iteminstId = null;

        if (applyinst != null) {
            List<AeaHiItemMatinst> matinstList = new ArrayList<>();
            if ("1".equals(applyinst.getIsSeriesApprove())) {//单项
                if (StringUtils.isBlank(iteminstId)) {
                    List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
                    if (iteminsts.size() == 0) return matList;
                    iteminstId = iteminsts.get(0).getIteminstId();
                }
                matinstList = aeaHiItemMatinstService.getMatinstListByIteminstIds(new String[]{iteminstId}, "1");
            } else {//并联
                AeaHiParStageinst stageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinstId);
                if (stageinst == null) return matList;
                matinstList = aeaHiItemMatinstService.getMatinstListByStageinstId(stageinst.getStageinstId(), "1");

                AeaParStage stage = aeaParStageMapper.getAeaParStageById(stageinst.getStageId());

                if (stage != null && "1".equals(stage.getHandWay())) {
                    if (StringUtils.isNotBlank(iteminstId)) {
                        AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);
                        if (iteminst != null)
                            preItemInout.addAll(this.getOfficeMatsByStageItemVerIds(stage.getStageId(), new String[]{iteminst.getItemVerId()}));
                    } else {
                        List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
                        String[] itemVerIds = iteminsts.stream().map(AeaHiIteminst::getItemVerId).toArray(String[]::new);
                        preItemInout.addAll(this.getOfficeMatsByStageItemVerIds(stage.getStageId(), itemVerIds));
                    }
                }
            }

            //获取材料定义
            matList = getMatListByApplyinstId(applyinstId, iteminstId);

            if (preItemInout.size() > 0) {
                List<String> matIds = matList.stream().map(AeaItemMat::getMatId).collect(Collectors.toList());
                for (AeaItemMat mat : preItemInout) {
                    if (!matIds.contains(mat.getMatId())) {
                        matList.add(mat);
                    }
                }
            }

            for (AeaItemMat mat : matList) {
                List<AeaHiItemMatinst> attInstList = new ArrayList<>();
                List<AeaHiItemMatinst> pageInstList = new ArrayList<>();
                List<AeaHiItemMatinst> copyInstList = new ArrayList<>();
                List<AeaHiItemMatinst> forminstList = new ArrayList<>();
                List<AeaHiItemMatinst> certinstList = new ArrayList<>();

                for (AeaHiItemMatinst matinst : matinstList) {
                    if (mat.getMatCode().equals(matinst.getMatinstCode())) {

                        if ("f".equals(matinst.getMatProp()) && StringUtils.isNotBlank(matinst.getStoFormId())) {
                            forminstList.add(matinst);
                            continue;
                        }

                        if ("c".equals(matinst.getMatProp()) && StringUtils.isNotBlank(matinst.getCertinstId())) {
                            certinstList.add(matinst);
                            continue;
                        }

                        if (matinst.getAttCount() != null && matinst.getAttCount() > 0)
                            attInstList.add(matinst);
                        if (matinst.getRealPaperCount() != null && matinst.getRealPaperCount() > 0)
                            pageInstList.add(matinst);
                        if (matinst.getRealCopyCount() != null && matinst.getRealCopyCount() > 0)
                            copyInstList.add(matinst);
                    }
                }

                mat.setAttMatinstList(attInstList);
                mat.setCopyMatinstList(copyInstList);
                mat.setPageMatinstList(pageInstList);
                mat.setForminstList(forminstList);
                mat.setCertinstList(certinstList);
            }
        }
        return matList;
    }

    @Override
    public List<AeaItemMat> getMatListByStateListAndItemListAndStageId(String[] itemStateIds, String[] stageStateIds, String[] coreItemVerIdsDef, String[] parallelItemVerIdsDef, String[] coreParentItemVerIds, String[] parallelParentItemVerIds, String stageId,String rootOrgId) throws Exception {
        if(StringUtils.isNotBlank(rootOrgId)) rootOrgId=SecurityContext.getCurrentOrgId();
        List<AeaItemMat> list = new ArrayList<>();
        String[] parallelItemVerIds = (String[]) ArrayUtils.addAll(parallelItemVerIdsDef, parallelParentItemVerIds);
        String[] coreItemVerIds = (String[]) ArrayUtils.addAll(coreItemVerIdsDef, coreParentItemVerIds);
        if (StringUtils.isNotBlank(stageId)) {//说明是并联申报
            AeaParStage stage = aeaParStageService.getAeaParStageById(stageId);
            //并联的情形材料
            if (parallelItemVerIds != null && stageStateIds != null && parallelItemVerIds.length > 0 && stageStateIds.length > 0) {
                List<AeaItemMat> stageStateMatList = new ArrayList();
                for (String itemVerId : parallelItemVerIds) {
                    stageStateMatList.addAll(this.getMatListByStageStateIdsAndItemVerId(stageStateIds, itemVerId));
                }
                list.addAll(stageStateMatList);
            }

            //`HAND_WAY` char(1) DEFAULT NULL COMMENT '办理方式 0 多事项直接合并办理  1 按阶段多级情形组织事项办理',
            //如果该阶段是沿用情形组织事项办理，那么需要去掉对应的实施事项版本ID，免得带出了实施事项所配的材料

            if (parallelParentItemVerIds.length > 0) {//标准事项
                if (stage != null && "1".equals(stage.getHandWay())) {//沿用情形
                    List<String> allSssxList = new ArrayList<>();
                    for (String itemVerId : parallelParentItemVerIds) {
                        AeaItemBasic itemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
                        if (itemBasic != null) {
                            List<AeaItemBasic> sssxList = aeaItemBasicService.getSssxByItemIdAndRegionalism(itemBasic.getItemId(), null, null, rootOrgId);
                            allSssxList.addAll(sssxList.stream().map(AeaItemBasic::getItemVerId).collect(Collectors.toList()));
                        }
                    }
                    parallelItemVerIds = CommonTools.fitleElements(parallelItemVerIds, allSssxList);
                }
            }
            if (parallelItemVerIds.length > 0) {
                list.addAll(getMatListByItemVerIdsAndStageId(parallelItemVerIds, stageId, null));
            }
            if (stage != null && "0".equals(stage.getHandWay())) {//多事项直接合并
                //并联事项材料
                List<AeaItemMat> parallelItemMatList = (parallelItemVerIds != null && parallelItemVerIds.length > 0) ? getMatListByItemVerIds(parallelItemVerIds, "1", null) : new ArrayList<AeaItemMat>();
                list.addAll(parallelItemMatList);
            }
        }
        //并行事项材料,或者单项事项材料
        List<AeaItemMat> coreItemMatList = (coreItemVerIds != null && coreItemVerIds.length > 0) ? getMatListByItemVerIds(coreItemVerIds, "1", null) : new ArrayList<AeaItemMat>();

        List<AeaItemMat> itemStateMatList = (itemStateIds != null && itemStateIds.length > 0) ? getMatListByItemStateIds(itemStateIds) : new ArrayList<AeaItemMat>();//事项的情形材料
        list.addAll(coreItemMatList);
        list.addAll(itemStateMatList);
        if (list.size() > 0) {
            String finalRootOrgId = rootOrgId;
            return list.stream()
                    .filter(CommonTools.distinctByKey(AeaItemMat::getMatId)).peek(v->{
                        List<BscAttForm> kbList = bscAttMapper.listAttLinkAndDetail("AEA_ITEM_MAT", "TEMPLATE_DOC", v.getMatId(), null, finalRootOrgId, null);
                        List<BscAttForm> ybList = bscAttMapper.listAttLinkAndDetail("AEA_ITEM_MAT", "SAMPLE_DOC", v.getMatId(), null, finalRootOrgId, null);
                        kbList.addAll(ybList);
                        String ybKbDetailIds = kbList.stream().map(bscAttForm -> bscAttForm.getDetailId()).collect(Collectors.joining(","));
                        v.setYbKbDetailIds(ybKbDetailIds);
                    })
                    //.sorted(Comparator.comparing(AeaItemMat::getSortNo))
                    .collect(Collectors.toList());
        }
        return list;
    }

    public List<AeaItemMat> getOfficeMatsByStageItemVerIds(String stageId, String[] itemVerIds) throws Exception {
        if(itemVerIds==null||itemVerIds.length==0) return new ArrayList<>();
        return aeaItemMatMapper.getOfficeMatsByStageItemVerIds(stageId, itemVerIds, SecurityContext.getCurrentOrgId());
    }

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    /**
     * 查询单项不分情形下材料定义列表-中介超市用
     *
     * @param itemVerId 事项版本ID
     * @return List<AeaItemMat>
     */
    @Override
    public List<AeaItemMat> getSeriesNoStateMatList(String itemVerId) throws Exception {
        AeaItemBasic basic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
        if (null == basic) throw new Exception("can not find item info ");
        List<AeaItemMat> itemMats = aeaItemInoutMapper.getSeriesNoStateMatList(itemVerId);
        return itemMats;
    }
}

