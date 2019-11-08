package com.augurit.aplanmis.common.service.approve.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.approve.ApproveService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.vo.MatinstVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApproveServiceImpl implements ApproveService{

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
    @Autowired
    private AeaParInMapper aeaParInMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaItemMapper aeaItemMapper;
    @Autowired
    private AeaItemVerMapper aeaItemVerMapper;
    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private FileUtilsService fileUtilsService;
    /**
     * 获取并联申报材料列表
     *
     * @param applyinstId 申请实例ID
     * @param iteminstId  材料实例ID
     * @return
     * @throws Exception
     */
    public List<MatinstVo> getParMatinstList(String applyinstId, String iteminstId) throws Exception {
        List<MatinstVo> matinstVos = new ArrayList<>();
        AeaHiParStageinst aeaHiParStageinst = new AeaHiParStageinst();
        aeaHiParStageinst.setApplyinstId(applyinstId);
        aeaHiParStageinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaHiParStageinst> aeaHiParStageinstList = aeaHiParStageinstMapper.listAeaHiParStageinst(aeaHiParStageinst);
        if (aeaHiParStageinstList.isEmpty()) return matinstVos;
        AeaHiParStageinst stageinst = aeaHiParStageinstList.get(0);
        String stageId = stageinst.getStageId();
        AeaParIn aeaParIn = new AeaParIn();
        aeaParIn.setStageId(stageId);
        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        //当前阶段的材料列表
        List<AeaParIn> aeaParInList = aeaParInMapper.listAeaParIn(aeaParIn);

        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstMapper.listAllAeaHiIteminstByApplyinstId(applyinstId, SecurityContext.getCurrentOrgId());
        //增加事项过滤---审批人员只查看自己事项下的材料列表
        if (StringUtils.isNotBlank(iteminstId) && !"undefined".equalsIgnoreCase(iteminstId)) {
            AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
            String itemVerId = iteminst.getItemVerId();
            //20190904改为，通过事项的基本事项版本id进行查询，因为阶段关联的是基本事项。
            AeaItem aeaItem = aeaItemMapper.getAeaItemById(iteminst.getItemId());
            AeaItemVer query = new AeaItemVer();
            query.setItemId(aeaItem.getParentItemId());
            List<AeaItemVer> aeaItemVers = aeaItemVerMapper.listAeaItemVer(query);
            for(AeaItemVer aeaItemVer : aeaItemVers){
                AeaParStageItem query1 = new AeaParStageItem();
                query1.setStageId(stageId);
                query1.setItemVerId(aeaItemVer.getItemVerId());
                List<AeaParStageItem> aeaParStageItems = aeaParStageItemMapper.listAeaParStageItem(query1);
                if (aeaParStageItems.size() > 0) {
                    itemVerId = aeaItemVer.getItemVerId();
                    break;
                }
            }
            aeaParInList = aeaParInMapper.listItemAeaParIn(stageId,itemVerId , SecurityContext.getCurrentOrgId());
            //如果有事项实例，需要过滤，
            if (!aeaHiIteminstList.isEmpty()) {

                aeaHiIteminstList = aeaHiIteminstList.stream().filter(item -> item.getIteminstId().equals(iteminstId)).collect(Collectors.toList());
            }

        }
        List<AeaHiItemInoutinst> aeaHiItemInoutinstList = new ArrayList<>();
        for (AeaHiIteminst temp2 : aeaHiIteminstList) {
            AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
            aeaHiItemInoutinst.setIsParIn("1");
            aeaHiItemInoutinst.setIsCollected("1");
            aeaHiItemInoutinst.setIteminstId(temp2.getIteminstId());
            aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            //事项实例的材料列表
            aeaHiItemInoutinstList.addAll(aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst));
        }


        for (AeaParIn aeaParIn1 : aeaParInList) {
            if ("mat".equals(aeaParIn1.getFileType())) {
                MatinstVo matinstVo = new MatinstVo();
                AeaItemMat aeaItemMat = aeaItemMatMapper.getAeaItemMatById(aeaParIn1.getMatId());
                if (aeaItemMat == null) continue;
                matinstVo.setMatFrom(aeaItemMat.getMatFrom());
                matinstVo.setMatName(aeaItemMat.getMatName());
                matinstVo.setFileType(aeaParIn1.getFileType());
                matinstVo.setSortNo(aeaParIn1.getSortNo());
                matinstVo.setDuePaperCount(aeaItemMat.getDuePaperCount());
                matinstVo.setDueCopyCount(aeaItemMat.getDueCopyCount());

                for (AeaHiItemInoutinst aeaHiItemInoutinst1 : aeaHiItemInoutinstList) {
                    if (aeaParIn1.getInId().equals(aeaHiItemInoutinst1.getParInId())) {
                        AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstService.getAeaHiItemMatinstById(aeaHiItemInoutinst1.getMatinstId());
                        if (aeaHiItemMatinst.getRealCopyCount() != null && aeaHiItemMatinst.getRealCopyCount() > 0) {
                            matinstVo.setRealCopyCount(aeaHiItemMatinst.getRealCopyCount());
                            matinstVo.setCopyMatinstId(aeaHiItemMatinst.getMatinstId());
                        } else if (aeaHiItemMatinst.getRealPaperCount() != null && aeaHiItemMatinst.getRealPaperCount() > 0) {
                            matinstVo.setRealPaperCount(aeaHiItemMatinst.getRealPaperCount());
                            matinstVo.setPaperMatinstId(aeaHiItemMatinst.getMatinstId());
                        } else if (aeaHiItemMatinst.getAttCount() != null && aeaHiItemMatinst.getAttCount() > 0) {
                            matinstVo.setAttCount(aeaHiItemMatinst.getAttCount());
                            matinstVo.setAttMatinstId(aeaHiItemMatinst.getMatinstId());
                            List<BscAttFileAndDir> fileAndDirList = fileUtilsService.getMatAttDetailByMatinstId(aeaHiItemInoutinst1.getMatinstId());
                            matinstVo.setFileList(fileAndDirList == null ? new ArrayList<BscAttFileAndDir>() : fileAndDirList);
                        }

                    }
                }
                if (matinstVo.getMatName() != null) {
                    matinstVos.add(matinstVo);
                }

            }
        }

        //获取前置事项输出作为后置事项的材料
        matinstVos.addAll(getPreOutSuffixInMat(aeaHiIteminstList));

        //结果去重
        if (matinstVos.size() > 0) {
            return matinstVos.stream().filter(CommonTools.distinctByKey(MatinstVo::getMatName)).sorted(Comparator.comparing(MatinstVo::getSortNo)).collect(Collectors.toList());
        } else {
            return matinstVos;
        }
    }

    /**
     * 获取前置事项输出作为后置事项的材料
     **/
    private List<MatinstVo> getPreOutSuffixInMat(List<AeaHiIteminst> aeaHiIteminstList) throws Exception {
        List<MatinstVo> result = new ArrayList<>();
        for (AeaHiIteminst temp2 : aeaHiIteminstList) {

            List<AeaItemInout> itemInouts = new ArrayList<>();
            itemInouts.addAll(aeaItemInoutMapper.getPreItemInItemInout(temp2));//获取后置输入的inout

            if (itemInouts.size() > 0) {
                for (AeaItemInout itemInout : itemInouts) {
                    MatinstVo matinstVo = new MatinstVo();
                    AeaItemMat aeaItemMat = aeaItemMatMapper.getAeaItemMatById(itemInout.getMatId());
                    if (aeaItemMat == null) continue;
                    matinstVo.setMatFrom(com.augurit.agcloud.framework.util.StringUtils.isBlank(aeaItemMat.getMatFrom()) ? "1" : aeaItemMat.getMatFrom());
                    matinstVo.setMatName(aeaItemMat.getMatName());
                    matinstVo.setFileType(itemInout.getFileType());
                    matinstVo.setSortNo(itemInout.getSortNo() == null ? 100L : itemInout.getSortNo());//如果没有排序号随便给一个么？

                    //查询是否有item_inoutinst实例
                    AeaHiItemInoutinst inst = new AeaHiItemInoutinst();
                    inst.setIteminstId(inst.getIteminstId());
                    inst.setItemInoutId(itemInout.getInoutId());
                    List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(inst);

                    if (itemInoutinsts.size() > 0) {
//                        for(AeaHiItemInoutinst itemInoutinst :itemInoutinsts){
                        //添加材料（如果有一个item_inout有多个item_inoutinst如何处理）
                        AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstService.getAeaHiItemMatinstById(itemInoutinsts.get(0).getMatinstId());
                        if (aeaHiItemMatinst.getRealCopyCount() != null && aeaHiItemMatinst.getRealCopyCount() > 0) {
                            matinstVo.setRealCopyCount(aeaHiItemMatinst.getRealCopyCount());
                            matinstVo.setCopyMatinstId(aeaHiItemMatinst.getMatinstId());
                        } else if (aeaHiItemMatinst.getRealPaperCount() != null && aeaHiItemMatinst.getRealPaperCount() > 0) {
                            matinstVo.setRealPaperCount(aeaHiItemMatinst.getRealPaperCount());
                            matinstVo.setPaperMatinstId(aeaHiItemMatinst.getMatinstId());
                        } else if (aeaHiItemMatinst.getAttCount() != null && aeaHiItemMatinst.getAttCount() > 0) {
                            matinstVo.setAttCount(aeaHiItemMatinst.getAttCount());
                            matinstVo.setAttMatinstId(aeaHiItemMatinst.getMatinstId());
                        }
//                        }

                    } else {
                        matinstVo.setRealCopyCount(0l);
                        matinstVo.setRealPaperCount(0l);
                        matinstVo.setAttCount(0l);
                    }
                    result.add(matinstVo);
                }
            }
        }

        return result;
    }
}
