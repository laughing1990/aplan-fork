package com.augurit.aplanmis.front.basis.mat.service;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class RestMatService {

    @Autowired
    private AeaItemMatService aeaItemMatService;

    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;

    @Autowired
    private FileUtilsService fileUtilsService;

    //根据多个事项版本ID来获取材料列表（包括已提交电子附件材料）
    public List<AeaItemMat> getMatListByItemVerId(String[] itemVerId, String isInput, String projInfoId) throws Exception {
        if (itemVerId.length < 1) throw new Exception("事项版本ID数组长度为0");
        if (StringUtils.isBlank(projInfoId)) throw new Exception("项目ID为空");
        if (StringUtils.isBlank(isInput)) isInput = "1";
        //获取事项的基础材料（默认包括父级情形材料）
        List<AeaItemMat> matList = aeaItemMatService.getMatListByItemVerIds(itemVerId, isInput, "0".equals(isInput) ? null : "1");
        //获取有电子附件的材料实例
        this.getAttMatinst(matList, projInfoId);
        return matList;
    }

    //根据阶段ID来获取材料列表（包括已提交电子附件材料）
    public List<AeaItemMat> getMatListByStageId(String stageId, String projInfoId) throws Exception {
        if (StringUtils.isBlank(stageId)) throw new Exception("阶段ID不能为空");
        if (StringUtils.isBlank(projInfoId)) throw new Exception("项目ID不能为空");
        //获取阶段的基础材料（默认包括父级情形材料）
        List<AeaItemMat> matList = aeaItemMatService.getMatListByStageId(stageId, "1");
        //获取有电子附件的材料实例
        this.getAttMatinst(matList, projInfoId);
        return matList;
    }

    //根据多个事项情形ID来获取材料列表（包括已提交电子附件材料）
    public List<AeaItemMat> getMatListByItemStateIds(String[] itemStateId, String projInfoId) throws Exception {
        if (StringUtils.isBlank(projInfoId)) throw new Exception("项目ID为空");
        if (itemStateId.length < 1) throw new Exception("事项情形数组长度为0");
        //获取多个情形的材料列表
        List<AeaItemMat> matList = aeaItemMatService.getMatListByItemStateIds(itemStateId);
        //获取有电子附件的材料实例
        this.getAttMatinst(matList, projInfoId);
        return matList;
    }

    //根据多个阶段情形ID来获取材料列表（包括已提交电子附件材料）
    public List<AeaItemMat> getMatListByStageStateIds(String[] stageStateId, String projInfoId) throws Exception {
        if (StringUtils.isBlank(projInfoId)) throw new Exception("项目ID为空");
        if (stageStateId.length < 1) throw new Exception("阶段情形数组长度为0");
        //获取多个情形的材料列表
        List<AeaItemMat> matList = aeaItemMatService.getMatListByStageStateIds(stageStateId);
        //获取有电子附件的材料实例
        this.getAttMatinst(matList, projInfoId);
        return matList;
    }

    private void getAttMatinst(List<AeaItemMat> matList, String projInfoId) throws Exception {
        if (matList.size() > 0) {

            String[] matIds = new String[matList.size()];
            for (int i = 0; i < matList.size(); i++) {
                matIds[i] = matList.get(i).getMatId();
            }

            //存在电子附件的材料实例
            List<AeaHiItemMatinst> attMatinstList = new ArrayList<>();
            List<AeaHiItemMatinst> matinstList = aeaHiItemMatinstService.getMatinstListByProjInfoIdAndMatIds(projInfoId, matIds);
            for (AeaHiItemMatinst matinst : matinstList) {
                if (matinst.getAttCount() > 0) {
                    matinst.setAttFormList(fileUtilsService.getAttachmentsByRecordId(new String[]{matinst.getMatinstId()}, "AEA_HI_ITEM_MATINST", "MATINST_ID"));
                    attMatinstList.add(matinst);
                }
            }

            if (attMatinstList.size() > 0) {
                for (AeaItemMat mat : matList) {
                    List<AeaHiItemMatinst> attMatinsts = new ArrayList<>();
                    for (AeaHiItemMatinst matinst : attMatinstList) {
                        if (matinst.getMatId().equals(mat.getMatId())) attMatinsts.add(matinst);
                    }
                    if (attMatinsts.size() > 0) mat.setAttMatinstList(attMatinsts);
                }
            }
        }
    }

}
