package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.bpm.common.domain.ActTplApp;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import com.augurit.aplanmis.common.service.admin.tpl.DgActTplAppAdminService;
import com.augurit.aplanmis.common.vo.AppProcCaseDefPlusAdminVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * -Service服务接口实现类
 */
@Service
@Transactional
public class AeaParStageAdminServiceImpl implements AeaParStageAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageAdminServiceImpl.class);

    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    @Autowired
    private AeaParThemeSeqMapper aeaParThemeSeqMapper;

    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;

    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;

    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;

    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;

    @Autowired
    private AeaParStateMapper aeaParStateMapper;

    @Autowired
    private AeaParInMapper aeaParInMapper;

    @Autowired
    private DgActTplAppAdminService dgActTplAppAdminService;

    @Autowired
    private AeaParThemeVerAdminService aeaParThemeVerAdminService;

    @Override
    public void saveAeaParStage(AeaParStage aeaParStage) throws Exception {

        try {
            //20190909 修改，阶段模板名称为 阶段主题+阶段名称
            String appName = aeaParStage.getStageName();
            AeaParThemeVer aeaParThemeVer = aeaParThemeVerAdminService.getAeaParThemeVerById(aeaParStage.getThemeVerId());
            if(aeaParThemeVer != null){
                appName = aeaParThemeVer.getThemeName() + "—" + appName;
            }
            ActTplApp actTplAppAllInfo = dgActTplAppAdminService.createActTplAppAllInfo(appName, "0");
            aeaParStage.setAppId(actTplAppAllInfo.getAppId());
        } catch (Exception e) {
            logger.error("获取流程模板的appId失败!" + e.getMessage(), e);
            throw e;
        }
        aeaParStage.setIsDeleted(Status.OFF);
        aeaParStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParStage.setCreater(SecurityContext.getCurrentUserId());
        aeaParStage.setCreateTime(new Date());
        if (StringUtils.isBlank(aeaParStage.getIsNeedState())) {
            aeaParStage.setIsNeedState(Status.ON);
        }
        aeaParStageMapper.insertAeaParStage(aeaParStage);
    }


    @Override
    public Long getMaxSortNoByThemeVerId(String themeVerId, String rootOrgId) {

        Long sortNo = aeaParStageMapper.getStageMaxSortNoByThemeVerId(themeVerId, rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
    }

    @Override
    public void updateAeaParStage(AeaParStage aeaParStage) {

        aeaParStage.setModifier(SecurityContext.getCurrentUserId());
        aeaParStage.setModifyTime(new Date());
        aeaParStageMapper.updateAeaParStage(aeaParStage);
    }

    @Override
    public void deleteAeaParStageById(String id) {

        if (StringUtils.isNotBlank(id)) {
            setThemeverDiaGramComnNull(id);
            aeaParStageMapper.deleteAeaParStage(id);
        }
    }


    private void setThemeverDiaGramComnNull(String stageId){
        List<String> list = new ArrayList(1);
        list.add(stageId);
        List<AeaParStage> stages = aeaParStageMapper.listAeaParStageByIds(list);
        if(stages == null || stages.size() < 1 || stages.get(0).getThemeVerId() == null){
            return;
        }
        AeaParThemeVer themeVer = aeaParThemeVerMapper.getAeaParThemeVerById(stages.get(0).getThemeVerId());
        if(themeVer == null){
            return;
        }
        aeaParThemeVerMapper.setThemeVerDiagramNull(themeVer.getThemeVerId());
    }

    /**
     * 批量删除
     *
     */
    @Override
    public void deleteAeaParStageByIds(String[] stageIds) {

        Assert.notNull(stageIds, "stageIds is null.");
        aeaParStageMapper.deleteAeaParStageByIds(stageIds);

        if(stageIds != null && stageIds.length>0){
            setThemeverDiaGramComnNull(stageIds[0]);
        }
    }

    @Override
    public PageInfo<AeaParStage> listAeaParStage(AeaParStage aeaParStage, Page page) {

        if(aeaParStage==null){
            aeaParStage = new AeaParStage();
        }
        aeaParStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParStage> list = aeaParStageMapper.listAeaParStage(aeaParStage);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParStage getAeaParStageById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParStageMapper.getAeaParStageById(id);
    }

    @Override
    public List<AeaParStage> listAeaParStage(AeaParStage aeaParStage) {

        if(aeaParStage==null){
            aeaParStage = new AeaParStage();
        }
        aeaParStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParStage> list = aeaParStageMapper.listAeaParStage(aeaParStage);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaParStage> listStageByThemeId(String themeId) throws Exception {

        if (StringUtils.isNotBlank(themeId)) {
            // 获取最大编号
            String rootOrgId = SecurityContext.getCurrentOrgId();
            AeaParThemeSeq themeSeq = aeaParThemeSeqMapper.getAeaParThemeSeqByThemeId(themeId, rootOrgId);
            if (themeSeq != null && themeSeq.getMaxNum() != null) {
                AeaParThemeVer themeVer = new AeaParThemeVer();
                themeVer.setThemeId(themeId);
                themeVer.setVerNum(themeSeq.getMaxNum());
                themeVer.setRootOrgId(rootOrgId);
                themeVer.setIsActive(ActiveStatus.ACTIVE.getValue());
                themeVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
                List<AeaParThemeVer> themeVerList = aeaParThemeVerMapper.listAeaParThemeVer(themeVer);
                if (themeVerList != null && themeVerList.size() > 0) {
                    String themeVerId = themeVerList.get(0).getThemeVerId();
                    AeaParStage stage = new AeaParStage();
                    stage.setRootOrgId(rootOrgId);
                    stage.setThemeVerId(themeVerId);
                    stage.setIsDeleted(Status.OFF);
                    return aeaParStageMapper.listAeaParStage(stage);
                }
            }
        }
        return null;
    }

    @Override
    public List<InFormBill> getPrintInFormBillData(String stageinstId) throws Exception {

        List<InFormBill> result = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(stageinstId)) {
            result = aeaParStageMapper.getPrintInFormBillData(stageinstId); //找到阶段下的事项实例
        }
        if (result != null && result.size() > 0) {
            for (InFormBill ifb : result) {
                ifb.setInformDate(new Date());
                Map<String, List<String>> itemMatMap = new HashMap<>();
                List<AeaItemMat> aeaItemMats = aeaItemMatMapper.listAeaItemMatByItemId(ifb.getItemId());//通过事项实例找到材料定义
                if (aeaItemMats != null && aeaItemMats.size() > 0) {
                    List<String> list = new ArrayList<>();
                    for (AeaItemMat mat : aeaItemMats) {
                        AeaHiItemMatinst matinst = new AeaHiItemMatinst();
                        matinst.setMatId(mat.getMatId());
                        List<AeaHiItemMatinst> matinstList = aeaHiItemMatinstMapper.listAeaHiItemMatinst(matinst);//找到材料实例
                        if (matinstList == null || matinstList.size() == 0) {//没有实例，需要补正
                            list.add(mat.getMatName());
                        } else {
                            Boolean flag = true;
                            for (AeaHiItemMatinst ahim : matinstList) {
                                if (!"0".equals(ahim.getAttCount()) || !"0".equals(ahim.getRealPaperCount())) {  //电子材料不为0 或 纸质材料不为0，表示已提交
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag) {
                                list.add(mat.getMatName());
                            }
                        }
                    }
                    itemMatMap.put(ifb.getItemName(), list);
                }
                ifb.setItemMatMap(itemMatMap);
            }
        }
        return result;
    }

    @Override
    public EasyuiPageInfo<AeaParStage> listAeaParStagePage(AeaParStage aeaParStage, Page page) {

        if(aeaParStage==null){
            aeaParStage = new AeaParStage();
        }
        aeaParStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParStage> list = aeaParStageMapper.listAeaParStage(aeaParStage);
        logger.debug("成功执行分页查询！！");
        return PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
    }

    @Override
    public List<ZtreeNode> syncListStageStateMat(String stageId) {

        List<ZtreeNode> allNodes = new ArrayList<>();
        if (StringUtils.isNotBlank(stageId)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            AeaParStage stage = aeaParStageMapper.getAeaParStageById(stageId);
            if (stage != null) {
                ZtreeNode stageNode = new ZtreeNode();
                stageNode.setId(stage.getStageId());
                stageNode.setName(stage.getStageName());
                stageNode.setType("stage");
                stageNode.setOpen(true);
                stageNode.setIsParent(true);
                stageNode.setNocheck(true);
                stageNode.setIsHorizontal(false);
                allNodes.add(stageNode);
                // 获取情形
                AeaParState state = new AeaParState();
                state.setRootOrgId(rootOrgId);
                state.setStageId(stageId);
                List<AeaParState> stateList = aeaParStateMapper.listAeaParState(state);
                if (stateList != null && stateList.size() > 0) {
                    for (AeaParState stateVo : stateList) {
                        // 获取情形材料
                        List<AeaParIn> matInList = aeaParInMapper.listInStateMatByStageId(stageId, null, rootOrgId);
                        // 获取情形证照
                        List<AeaParIn> certInList = aeaParInMapper.listInStateCertByStageId(stageId, null, rootOrgId);
                        ZtreeNode stateNode = new ZtreeNode();
                        stateNode.setId(stateVo.getParStateId());
                        stateNode.setName(stateVo.getStateName());
                        stateNode.setIsHorizontal(true);
                        if (StringUtils.isNotBlank(stateVo.getParentStateId())) {
                            stateNode.setpId(stateVo.getParentStateId());
                            stageNode.setIsParent(false);
                        } else {
                            stateNode.setpId(stageId);
                            stageNode.setIsParent(true);
                        }
                        stateNode.setType("state");
                        stateNode.setOpen(true);
                        stateNode.setIsParent(true);
                        stateNode.setNocheck(true);
                        allNodes.add(stateNode);
                        if (matInList != null && matInList.size() > 0) {
                            for (AeaParIn inVo : matInList) {
                                if (stateVo.getParStateId().equals(inVo.getParStateId())) {
                                    ZtreeNode matNode = new ZtreeNode();
                                    matNode.setId(inVo.getMatId());
                                    matNode.setName(inVo.getAeaMatCertName());
                                    matNode.setpId(inVo.getParStateId());
                                    // 暂时存储材料id
                                    matNode.setpName(inVo.getInId());
                                    matNode.setType("mat");
                                    matNode.setOpen(true);
                                    matNode.setIsParent(true);
                                    matNode.setNocheck(true);
                                    matNode.setIsHorizontal(true);
                                    allNodes.add(matNode);
                                }
                            }
                        }
                        if (certInList != null && certInList.size() > 0) {
                            for (AeaParIn inVo : certInList) {
                                if (stateVo.getParStateId().equals(inVo.getParStateId())) {
                                    ZtreeNode certNode = new ZtreeNode();
                                    certNode.setId(inVo.getCertId());
                                    certNode.setName(inVo.getAeaMatCertName());
                                    certNode.setpId(inVo.getParStateId());
                                    // 暂时存储证照id
                                    certNode.setpName(inVo.getInId());
                                    certNode.setType("cert");
                                    certNode.setOpen(true);
                                    certNode.setIsParent(true);
                                    certNode.setNocheck(true);
                                    certNode.setIsHorizontal(true);
                                    allNodes.add(certNode);
                                }
                            }
                        }
                    }
                }
            }
        }
        // 为了防止信息报错
        if (allNodes.size() == 0) {
            ZtreeNode noNode = new ZtreeNode();
            noNode.setId("noStage");
            noNode.setName("不存在阶段数据");
            noNode.setType("noStage");
            noNode.setOpen(true);
            noNode.setIsParent(true);
            noNode.setNocheck(true);
            allNodes.add(noNode);
        }
        return allNodes;
    }


    @Override
    public List<AppProcCaseDefPlusAdminVo> listAppProcCaseDefVo(String stageId, String keyword) throws Exception {

        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
        if (aeaParStage != null && StringUtils.isNotBlank(aeaParStage.getAppId())) {
            return dgActTplAppAdminService.getAppProcCaseDefVo(aeaParStage.getAppId(), keyword);
        }
        return new ArrayList<>();
    }
}

