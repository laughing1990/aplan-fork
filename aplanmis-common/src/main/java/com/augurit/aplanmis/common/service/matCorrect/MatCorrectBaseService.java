package com.augurit.aplanmis.common.service.matCorrect;


import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.dto.MatCorrectDto;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStateinstService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.receive.AeaHiReceiveService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Author: lucas Chan
 * Date: 2019-9-25
 * Description: 材料补正、补全公共接口
 */

@Service
@Transactional
public class MatCorrectBaseService {

    @Autowired
    private ActStoAppinstService actStoAppinstService;

    @Autowired
    private AeaHiReceiveService aeaHiReceiveService;

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    @Autowired
    private BscAttMapper bscAttMapper;

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    @Autowired
    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;

    @Autowired
    private AeaHiParStateinstService aeaHiParStateinstService;

    @Autowired
    private AeaParInMapper aeaParInMapper;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;

    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;

    @Autowired
    private AeaItemBasicService aeaItemBasicService;

    @Autowired
    private AeaItemBasicAdminService aeaItemBasicAdminService;

    //根据申请实例ID获取模板实例ID（补全|补正）
    public String getAppinstIdByApplyinstId(String applyinstId) throws Exception {
        if (StringUtils.isBlank(applyinstId)) return null;

        ActStoAppinst appinst = new ActStoAppinst();
        appinst.setMasterRecordId(applyinstId);
        List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(appinst);
        return appinsts.size() < 1 ? null : appinsts.get(0).getAppinstId();
    }

    //获取已提交的材料列表、未交或少交的材料列表（补全|补正）
    public Map<String, List> getCorrectMatsAndSubmittedMats(List<AeaItemMat> mats) {

        //少交或者未提交的材料列表
        List<MatCorrectDto> matCorrectDtos = new ArrayList();
        //已经提交的材料列表（包括少交的材料）
        List<MatCorrectDto> submittedMats = new ArrayList();
        Map<String, List> map = new HashMap();
        Set<String> matIds = new HashSet();

        for (AeaItemMat mat : mats) {

            if (matIds.contains(mat.getMatId())) continue;
            MatCorrectDto matCorrectDto = new MatCorrectDto();
            matCorrectDto.setMatId(mat.getMatId());
            matCorrectDto.setMatName(mat.getMatName());
            matCorrectDto.setMatCode(mat.getMatCode());

            MatCorrectDto matCorrectDto1 = new MatCorrectDto();
            BeanUtils.copyProperties(matCorrectDto, matCorrectDto1);

            if (mat.getCopyMatinstList().size() > 0) {
                for (AeaHiItemMatinst matinst : mat.getCopyMatinstList()) {
                    if (matinst.getRealCopyCount() != null) {
                        if (matCorrectDto1.getRealCopyCount() == null || (matinst.getRealCopyCount() > matCorrectDto1.getRealCopyCount())) {
                            matCorrectDto1.setCopyCount(mat.getDueCopyCount());
                            matCorrectDto1.setRealCopyCount(matinst.getRealCopyCount());
                            matCorrectDto1.setCopyIsCollected(matinst.getRealCopyCount() > 0l ? "1" : "0");
                            matCorrectDto1.setCopyMatinstId(matinst.getMatinstId());
                        }
                    }
                }

                //判断复印件材料提交的数量是否少于规定份数
                if ((matCorrectDto1.getRealCopyCount() != null && mat.getDueCopyCount() != null && mat.getDueCopyCount() > 0l && matCorrectDto1.getRealCopyCount() < mat.getDueCopyCount()) || "0".equals(matCorrectDto1.getCopyIsCollected())) {
                    matCorrectDto.setCopyCount(Math.abs(matCorrectDto1.getRealCopyCount() - mat.getDueCopyCount()));
                    matCorrectDto.setCopyIsCollected("1");
                    matCorrectDto.setCopyMatinstId(matCorrectDto1.getCopyMatinstId());
                }
            }

            //判断复印件有没有提交
            if (StringUtils.isBlank(matCorrectDto1.getCopyMatinstId()) && mat.getDueCopyCount() != null && mat.getDueCopyCount() > 0l) {
                matCorrectDto.setCopyCount(mat.getDueCopyCount());
                matCorrectDto.setCopyIsCollected("0");
            }

            if (mat.getPageMatinstList().size() > 0) {
                for (AeaHiItemMatinst matinst : mat.getPageMatinstList()) {
                    if (matinst.getRealPaperCount() != null) {
                        if (matCorrectDto1.getRealPaperCount() == null || (matinst.getRealPaperCount() > matCorrectDto1.getRealPaperCount())) {
                            matCorrectDto1.setPaperCount(mat.getDuePaperCount());
                            matCorrectDto1.setRealPaperCount(matinst.getRealPaperCount());
                            matCorrectDto1.setPaperIsCollected(matinst.getRealPaperCount() > 0l ? "1" : "0");
                            matCorrectDto1.setPaperMatinstId(matinst.getMatinstId());
                        }
                    }
                }

                //判断原件材料提交的数量是否少于规定份数
                if (matCorrectDto1.getRealPaperCount() != null && mat.getDuePaperCount() != null && mat.getDuePaperCount() > 0l && matCorrectDto1.getRealPaperCount() < mat.getDuePaperCount() || "0".equals(matCorrectDto1.getPaperIsCollected())) {
                    matCorrectDto.setPaperCount(Math.abs(matCorrectDto1.getRealPaperCount() - mat.getDuePaperCount()));
                    matCorrectDto.setPaperIsCollected("1");
                    matCorrectDto.setPaperMatinstId(matCorrectDto1.getPaperMatinstId());
                }
            }

            //判断原件有没有提交
            if (StringUtils.isBlank(matCorrectDto1.getPaperMatinstId()) && mat.getDuePaperCount() != null && mat.getDuePaperCount() > 0l) {
                matCorrectDto.setPaperCount(mat.getDuePaperCount());
                matCorrectDto.setPaperIsCollected("0");
            }

            if (mat.getAttMatinstList().size() > 0) {
                for (AeaHiItemMatinst matinst : mat.getAttMatinstList()) {
                    if (matinst.getAttCount() != null) {
                        if (matCorrectDto1.getAttCount() != null || (matinst.getAttCount() > (matCorrectDto1.getAttCount() == null ? 0l : matCorrectDto1.getAttCount()))) {
                            matCorrectDto1.setAttCount(matinst.getAttCount());
                            matCorrectDto1.setAttIsCollected(matinst.getAttCount() > 0l ? "1" : "0");
                            matCorrectDto1.setAttMatinstId(matinst.getMatinstId());
                            matCorrectDto1.setIsNeedAtt(mat.getAttIsRequire());
                        }
                    }
                }

                //判断电子件是否已经上传
                if ("0".equals(matCorrectDto1.getAttIsCollected()) && "1".equals(mat.getAttIsRequire())) {
                    matCorrectDto.setIsNeedAtt("1");
                    matCorrectDto.setAttIsCollected("0");
                    matCorrectDto.setAttMatinstId(matCorrectDto1.getAttMatinstId());
                }
            }

            //判断必须的电子材料是否已经上传
            if (StringUtils.isBlank(matCorrectDto1.getAttMatinstId()) && "1".equals(mat.getAttIsRequire())) {
                matCorrectDto.setIsNeedAtt("1");
                matCorrectDto.setAttIsCollected("0");
            }

            if (StringUtils.isNotBlank(matCorrectDto1.getAttIsCollected()) || StringUtils.isNotBlank(matCorrectDto1.getCopyIsCollected()) || StringUtils.isNotBlank(matCorrectDto1.getPaperIsCollected())) {
                submittedMats.add(matCorrectDto1);
            }

            if (StringUtils.isNotBlank(matCorrectDto.getAttIsCollected()) || StringUtils.isNotBlank(matCorrectDto.getPaperIsCollected()) || StringUtils.isNotBlank(matCorrectDto.getCopyIsCollected())) {
                matCorrectDtos.add(matCorrectDto);
            }

            matIds.add(mat.getMatId());
        }

        map.put("MatCorrectDtos", matCorrectDtos);
        map.put("SubmittedMats", submittedMats);

        return map;
    }

    //创建补全|补正回执（补全|补正）
    public void createReceive(String itemVerIds, String applyinstId, String correctId, String type) throws Exception {

        if (StringUtils.isBlank(itemVerIds) || StringUtils.isBlank(applyinstId) || StringUtils.isBlank(correctId))
            throw new Exception("创建回执参数(itemVerIds、applyinstId、correctId)为空！");

        //创建回执记录
        AeaHiReceive aeaHiReceive = new AeaHiReceive();
        aeaHiReceive.setOutinstId(itemVerIds);
        aeaHiReceive.setApplyinstId(applyinstId);
        aeaHiReceive.setCreater(SecurityContext.getCurrentUserName());
        aeaHiReceive.setReceiptType(type);
        aeaHiReceive.setReceiveMemo(correctId);
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (aeaHiApplyinst == null) {
            aeaHiReceive.setReceiveUserName(SecurityContext.getCurrentUserName());
        } else {
            AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
            aeaHiReceive.setReceiveUserName(aeaLinkmanInfo.getLinkmanName());
            aeaHiReceive.setReceiveUserMobile(aeaLinkmanInfo.getLinkmanMobilePhone());
        }
        aeaHiReceive.setCreateTime(new Date());
        aeaHiReceiveService.saveAeaHiReceive(aeaHiReceive);
    }

    //修改附件的业务ID（补全|补正）
    public void updateAttFileBusTableName(String matinstId, String oldTableName, String oldPkName, String oldRecordId) throws Exception {

        List<BscAttForm> bscAttForms = bscAttMapper.listAttLinkAndDetailByTablePKRecordId(oldTableName, oldPkName, oldRecordId, SecurityContext.getCurrentOrgId());
        for (BscAttForm bscAttForm : bscAttForms) {
            BscAttLink attLink = new BscAttLink();
            BeanUtils.copyProperties(bscAttForm, attLink);
            attLink.setLinkId(UUID.randomUUID().toString());
            attLink.setTableName("AEA_HI_ITEM_MATINST");
            attLink.setPkName("MATINST_ID");
            attLink.setRecordId(matinstId);
            //20191106 修改，不再将不再的附件关联更新为材料实例，而是保留并创建新的关联到申报材料实例
            bscAttMapper.insertLink(attLink);
//            bscAttMapper.updateLink(attLink);
        }

    }

    //查询单事项的输入定义ID{inoutId}（补全|补正）
    public String getInoutIdByItemVerIdAndMatId(String matId, String itemVerId, String applyinstId, List<AeaHiItemStateinst> stateinsts) throws Exception {

        String inoutId = null;

        if (StringUtils.isBlank(matId) || StringUtils.isBlank(itemVerId) || StringUtils.isBlank(applyinstId))
            return inoutId;

        AeaItemInout aeaItemInout = new AeaItemInout();
        aeaItemInout.setIsInput("1");
        aeaItemInout.setMatId(matId);
        aeaItemInout.setIsDeleted("0");
        aeaItemInout.setItemVerId(itemVerId);
        aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemInout> aeaItemInouts = aeaItemInoutMapper.listAeaItemInout(aeaItemInout);

        AeaItemBasic itemBasic = aeaItemBasicAdminService.getOneByItemVerId(itemVerId, SecurityContext.getCurrentOrgId());

        for (AeaItemInout inout : aeaItemInouts) {
            if ("0".equals(inout.getIsStateIn()) || "0".equals(itemBasic.getIsNeedState())) {
                inoutId = inout.getInoutId();
                break;
            }
        }

        if (StringUtils.isBlank(inoutId)) {
            if (stateinsts.size() < 1 && "1".equals(itemBasic.getIsNeedState()))
                stateinsts.addAll(aeaHiItemStateinstMapper.listAeaHiItemStateinstByApplyinstIdOrSeriesinstId(applyinstId, null));
            for (AeaItemInout inout : aeaItemInouts) {
                if ("1".equals(inout.getIsStateIn())) {
                    for (AeaHiItemStateinst stateinst : stateinsts) {
                        if (stateinst.getExecStateId().equals(inout.getItemStateId())) {
                            inoutId = inout.getInoutId();
                            break;
                        }
                    }
                    if (StringUtils.isNotBlank(inoutId)) break;
                }
            }
        }

        return inoutId;
    }

    //创建阶段下事项输入输出实例（补全|补正）
    public void createStageIteminoutinst(String matId, String matinstId, String inoutinstId, String iteminstId, String stageId, String applyinstId, List<AeaHiParStateinst> parStateinsts, String isNeedState, String isCollected) throws Exception {

        //创建输入输出实例
        AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
        aeaHiItemInoutinst.setIsCollected(isCollected);
        aeaHiItemInoutinst.setMatinstId(matinstId);
        aeaHiItemInoutinst.setCreateTime(new Date());
        aeaHiItemInoutinst.setCreater(SecurityContext.getCurrentUserName());
        aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiItemInoutinst.setIsParIn("1");

        AeaParIn parIn = new AeaParIn();
        parIn.setMatId(matId);
        parIn.setStageId(stageId);
        parIn.setIsDeleted("0");
        parIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParIn> parIns = aeaParInMapper.listAeaParIn(parIn);

        AeaParStageItem aeaParStageItem = new AeaParStageItem();
        aeaParStageItem.setStageId(stageId);
        List<AeaParStageItem> stageItems = aeaParStageItemMapper.listAeaParStageItem(aeaParStageItem);

        List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);

        AeaHiItemInoutinst itemInoutinst = null;
        if ("1".equals(isCollected)) {
            itemInoutinst = new AeaHiItemInoutinst();
            itemInoutinst.setMatinstId(matinstId);
        }

        //凡是需要该材料的事项都创建输入输出实例
        for (AeaHiIteminst aeaHiIteminst : iteminsts) {

            if (itemInoutinst != null && "1".equals(isCollected)) {
                itemInoutinst.setIteminstId(aeaHiIteminst.getIteminstId());
                List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);
                if (itemInoutinsts.size() > 0) {
                    for (AeaHiItemInoutinst itemInoutinst1 : itemInoutinsts) {
                        if (StringUtils.isBlank(itemInoutinst1.getIsCollected()) && "0".equals(itemInoutinst1.getIsCollected())) {
                            itemInoutinst1.setModifier(SecurityContext.getCurrentUserName());
                            itemInoutinst1.setModifyTime(new Date());
                            itemInoutinst1.setIsCollected("1");
                            aeaHiItemInoutinstMapper.updateAeaHiItemInoutinst(itemInoutinst1);
                        }
                    }
                    continue;
                }
            }

            boolean flag = false;
            String itemVerId = null;
            for (AeaParStageItem stageItem : stageItems) {
                if (stageItem.getItemVerId().equals(aeaHiIteminst.getItemVerId())) {
                    itemVerId = aeaHiIteminst.getItemVerId();
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                //根据实施事项Id获取标准事项
                AeaItemBasic parentAeaItemBasic = aeaItemBasicService.getCatalogItemByCarryOutItemId(aeaHiIteminst.getItemId());
                if (parentAeaItemBasic == null) throw new Exception("找不到该阶段下的标准事项！");
                itemVerId = parentAeaItemBasic.getItemVerId();
            }

            if (aeaHiIteminst.getIteminstId().equals(iteminstId))
                aeaHiItemInoutinst.setInoutinstId(inoutinstId);
            else
                aeaHiItemInoutinst.setInoutinstId(UUID.randomUUID().toString());

            for (AeaParIn aeaParIn : parIns) {
                //判断该阶段输入是否属于该阶段下的事项
                Integer count = aeaParInMapper.isCheckParInBelong2Item(stageId, itemVerId, aeaParIn.getInId());

                if (count != null && count > 0) {
                    if ("0".equals(aeaParIn.getIsStateIn()) || "0".equals(isNeedState)) {
                        aeaHiItemInoutinst.setIteminstId(aeaHiIteminst.getIteminstId());
                        aeaHiItemInoutinst.setParInId(aeaParIn.getInId());
                        aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(aeaHiItemInoutinst);
                        break;
                    } else {

                        //获取情形实例
                        if (parStateinsts.size() < 1 && "1".equals(isNeedState))
                            parStateinsts.addAll(aeaHiParStateinstService.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, null));

                        boolean isExist = false;
                        for (AeaHiParStateinst stateinst : parStateinsts) {
                            if (stateinst.getExecStateId().equals(aeaParIn.getParStateId())) {
                                aeaHiItemInoutinst.setIteminstId(aeaHiIteminst.getIteminstId());
                                aeaHiItemInoutinst.setParInId(aeaParIn.getInId());
                                aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(aeaHiItemInoutinst);
                                isExist = true;
                                break;
                            }
                        }
                        if (isExist) break;
                    }
                }
            }
        }

    }
}
