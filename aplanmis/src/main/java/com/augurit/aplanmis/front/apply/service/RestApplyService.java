package com.augurit.aplanmis.front.apply.service;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiReceive;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiReceiveMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSmsInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.receive.ReceiveService;
import com.augurit.aplanmis.common.service.receive.constant.ReceiveConstant;
import com.augurit.aplanmis.common.utils.BusinessUtil;
import com.augurit.aplanmis.front.apply.vo.ApplyinstIdVo;
import com.augurit.aplanmis.front.apply.vo.PropulsionItemStateVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyDataVo;
import com.augurit.aplanmis.front.apply.vo.StageApplyDataPageVo;
import com.augurit.aplanmis.front.apply.vo.StageApplyDataVo;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class RestApplyService {

    @Autowired
    private AeaSeriesService aeaSeriesService;
    @Autowired
    private AeaParStageService aeaParStageService;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaHiSmsInfoMapper aeaHiSmsInfoMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private AeaHiReceiveMapper aeaHiReceiveMapper;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;

    /**
     * 单项申报 --> 收件，发起申报
     *
     * @param seriesApplyDataPageVo 申报参数
     * @return applyinstId 申请实例ID
     */
    public String startSeriesFlow(SeriesApplyDataVo seriesApplyDataPageVo) throws Exception {
        String applyinstId = aeaSeriesService.startApply(seriesApplyDataPageVo);
        clearHistoryReceiptsAndSaveAgain(seriesApplyDataPageVo, applyinstId, new String[]{ReceiveConstant.ReceiveTypeEnum.MAT_TYPE.getCode(), ReceiveConstant.ReceiveTypeEnum.ACCEPT_TYPE.getCode()});
        return applyinstId;
    }

    /**
     * 单项申报 --> 生成实例，打印回执
     *
     * @param seriesApplyDataVo 申报餐宿
     * @return applyinstId 申请实例ID
     */
    public String printReceipts(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        seriesApplyDataVo.setIsParallel("0");
        String applyinstId = aeaSeriesService.printReceipts(seriesApplyDataVo);
        // 先删除以前的 receiveType=1, 2 的回执实例
        clearHistoryReceiptsAndSaveAgain(seriesApplyDataVo, applyinstId, new String[]{ReceiveConstant.ReceiveTypeEnum.MAT_TYPE.getCode(), ReceiveConstant.ReceiveTypeEnum.ACCEPT_TYPE.getCode()});
        return applyinstId;
    }

    private void clearHistoryReceiptsAndSaveAgain(SeriesApplyDataVo seriesApplyDataPageVo, String applyinstId, String[] receiveTypes) throws Exception {
        Assert.isTrue(receiveTypes.length > 0, "回执类型不能为空");
        if (StringUtils.isNotBlank(seriesApplyDataPageVo.getApplyinstId())) {
            // 先删除以前的 receiveType=1, 2 的回执实例
            List<AeaHiReceive> aeaHiReceives = aeaHiReceiveMapper.listReceiveByApplyinstIdAndTypes(new String[]{seriesApplyDataPageVo.getApplyinstId()}, new String[]{ReceiveConstant.ReceiveTypeEnum.MAT_TYPE.getCode(), ReceiveConstant.ReceiveTypeEnum.ACCEPT_TYPE.getCode()});
            if (CollectionUtils.isNotEmpty(aeaHiReceives)) {
                aeaHiReceiveMapper.deleteAeaHiReceives(aeaHiReceives.stream().map(AeaHiReceive::getReceiveId).collect(Collectors.toList()));
            }
        }
        updateAeaSmsInfo(seriesApplyDataPageVo.getSmsInfoId(), new String[]{applyinstId});
        //保存受理回执，物料回执
        receiveService.saveReceive(new String[]{applyinstId}, receiveTypes, SecurityContext.getCurrentUserName(), "");
    }

    /**
     * 单项申报 --> 不予受理
     *
     * @param seriesApplyDataVo 申报入参
     * @return applyinstId 申请实例ID
     */
    public String inadmissibleSeriesFlow(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(seriesApplyDataVo.getItemVerId(), SecurityContext.getCurrentOrgId());
        seriesApplyDataVo.setAppId(aeaItemBasic.getAppId());
        seriesApplyDataVo.setIsParallel("0");
        String applyinstId = aeaSeriesService.inadmissible(seriesApplyDataVo);

        // 打印不受理回执
        clearHistoryReceiptsAndSaveAgain(seriesApplyDataVo, applyinstId, new String[]{ReceiveConstant.ReceiveTypeEnum.REJECT_TYPE.getCode()});
        return applyinstId;
    }

    /**
     * 并联申报 --> 生成实例，打印回执
     * 并联申报可以支持只申报并行推进事项，不申报并联申报事项；
     *
     */
    public ApplyinstIdVo instantiateStageProcess(StageApplyDataPageVo stageApplyDataPageVo) throws Exception {
        ApplyinstIdVo applyinstIdVo = new ApplyinstIdVo();
        // 先删除以前的 receiveType=1, 2 的回执实例
        if (StringUtils.isNotBlank(stageApplyDataPageVo.getParallelApplyinstId()) || (stageApplyDataPageVo.getApplyinstIds() != null && stageApplyDataPageVo.getApplyinstIds().length > 0)) {
            String[] applyinstIds = ArrayUtils.addAll(stageApplyDataPageVo.getApplyinstIds(), stageApplyDataPageVo.getParallelApplyinstId());
            List<AeaHiReceive> aeaHiReceives = aeaHiReceiveMapper.listReceiveByApplyinstIdAndTypes(applyinstIds, new String[]{"1", "2"});
            if (CollectionUtils.isNotEmpty(aeaHiReceives)) {
                aeaHiReceiveMapper.deleteAeaHiReceives(aeaHiReceives.stream().map(AeaHiReceive::getReceiveId).collect(Collectors.toList()));
            }
        }
        String[] applyinstIds;
        //20190819 小糊涂 并联申报可以支持只申报并行推进事项，不申报并联申报事项；
        List<String> itemVerIds = stageApplyDataPageVo.getItemVerIds();
        if (null != itemVerIds && itemVerIds.size() > 0) {
            AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageApplyDataPageVo.getStageId());
            Assert.notNull(aeaParStage, "aeaParStage is null");
            String appId = aeaParStage.getAppId();
            String themeVerId = aeaParStage.getThemeVerId();
            StageApplyDataVo stageApplyDataVo = stageApplyDataPageVo.toStageApplyDataVo(appId, themeVerId);
            applyinstIdVo = aeaParStageService.stagingApply(stageApplyDataVo);
            applyinstIds = ArrayUtils.addAll(applyinstIdVo.getApplyinstIds(), applyinstIdVo.getParallelApplyinstId());
        } else {
            //并联下只申报并行事项（单事项申报）
            List<SeriesApplyDataVo> applyDataVos = this.getSeriesApplyDataVoListFromStage(stageApplyDataPageVo);
            List<String> list = new ArrayList<>(applyDataVos.size());
            for (SeriesApplyDataVo vo : applyDataVos) {
                vo.setIsParallel("1");
                vo.setStageId(stageApplyDataPageVo.getStageId());
                String applyinstId = aeaSeriesService.printReceipts(vo);
                list.add(applyinstId);
            }
            applyinstIds = list.toArray(new String[0]);
            applyinstIdVo.setApplyinstIds(applyinstIds);
        }
        if (null != applyinstIds && applyinstIds.length > 0) {
            List<AeaHiApplyinst> aeaHiApplyinsts = aeaHiApplyinstMapper.listAeaHiApplyinstByIds(Arrays.asList(applyinstIds));
            for (AeaHiApplyinst ahi : aeaHiApplyinsts) {
                ahi.setIsTemporarySubmit("2");
                aeaHiApplyinstMapper.updateAeaHiApplyinst(ahi);
            }
            updateAeaSmsInfo(stageApplyDataPageVo.getSmsInfoId(), applyinstIds);
            // 保存回执
            String[] receiptTypes = {"1", "2"};
            if (!receiveService.saveReceive(applyinstIds, receiptTypes, SecurityContext.getCurrentUserName(), stageApplyDataPageVo.getComments())) {
                log.error("Save aea_hi_receive error. applyinstIds: {}", Arrays.asList(applyinstIds));
            }
        }

        return applyinstIdVo;
    }

    /**
     * 并联申报 --> 发起申报
     *
     * @return applyinstIds 申请实例集合
     */
    public ApplyinstIdVo startStageProcess(StageApplyDataPageVo stageApplyDataPageVo) throws Exception {
        ApplyinstIdVo applyinstIdVo = new ApplyinstIdVo();
        String[] applyinstIds;
        //20190819 小糊涂 并联申报可以支持只申报并行推进事项，不申报并联申报事项；
        List<String> itemVerIds = stageApplyDataPageVo.getItemVerIds();
        if (null != itemVerIds && itemVerIds.size() > 0) {
            AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageApplyDataPageVo.getStageId());
            Assert.notNull(aeaParStage, "aeaParStage is null");
            String appId = aeaParStage.getAppId();
            String themeVerId = aeaParStage.getThemeVerId();
            StageApplyDataVo stageApplyDataVo = stageApplyDataPageVo.toStageApplyDataVo(appId, themeVerId);
            applyinstIdVo = aeaParStageService.stageApply(stageApplyDataVo);
            applyinstIds = ArrayUtils.addAll(applyinstIdVo.getApplyinstIds(), applyinstIdVo.getParallelApplyinstId());
        } else { //仅进行并行事项申报
            List<SeriesApplyDataVo> applyDataVos = this.getSeriesApplyDataVoListFromStage(stageApplyDataPageVo);
            List<String> ids = new ArrayList<>(applyDataVos.size());
            for (SeriesApplyDataVo vo : applyDataVos) {
                vo.setIsParallel("1");
                vo.setStageId(stageApplyDataPageVo.getStageId());
                String applyinstId = aeaSeriesService.startApply(vo);
                ids.add(applyinstId);
            }
            applyinstIds = ids.toArray(new String[0]);
            applyinstIdVo.setApplyinstIds(applyinstIds);
        }
        if (null != applyinstIds) {
            for (String applyinstId : applyinstIds) {
                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
                if (!Status.OFF.equals(aeaHiApplyinst.getIsTemporarySubmit())) {
                    aeaHiApplyinst.setIsTemporarySubmit(Status.OFF);
                    aeaHiApplyinstMapper.updateAeaHiApplyinst(aeaHiApplyinst);
                }
            }
            updateAeaSmsInfo(stageApplyDataPageVo.getSmsInfoId(), applyinstIds);
            // 先删除以前的 receiveType=1, 2 的回执实例
            List<AeaHiReceive> aeaHiReceives = aeaHiReceiveMapper.listReceiveByApplyinstIdAndTypes(applyinstIds, new String[]{"1", "2"});
            if (CollectionUtils.isNotEmpty(aeaHiReceives)) {
                aeaHiReceiveMapper.deleteAeaHiReceives(aeaHiReceives.stream().map(AeaHiReceive::getReceiveId).collect(Collectors.toList()));
            }
            // 保存回执
            String[] receiptTypes = {"1", "2"};
            receiveService.saveReceive(applyinstIds, receiptTypes, SecurityContext.getCurrentUserName(), stageApplyDataPageVo.getComments());
        }

        return applyinstIdVo;
    }


    /**
     * 并联申报 --> 发起申报，不予受理
     *
     */
    public ApplyinstIdVo inadmissibleStageProcess(StageApplyDataPageVo stageApplyDataPageVo) throws Exception {
        ApplyinstIdVo applyinstIdVo = new ApplyinstIdVo();
        String[] applyinstIds;
        //20190820 小糊涂 并联申报可以支持只申报并行推进事项，不申报并联申报事项；
        List<String> itemVerIds = stageApplyDataPageVo.getItemVerIds();
        if (null != itemVerIds && itemVerIds.size() > 0) {
            AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageApplyDataPageVo.getStageId());
            Assert.notNull(aeaParStage, "aeaParStage is null");
            String appId = aeaParStage.getAppId();
            String themeVerId = aeaParStage.getThemeVerId();
            StageApplyDataVo stageApplyDataVo = stageApplyDataPageVo.toStageApplyDataVo(appId, themeVerId);
            applyinstIdVo = aeaParStageService.inadmissible(stageApplyDataVo);
            applyinstIds = ArrayUtils.addAll(applyinstIdVo.getApplyinstIds(), applyinstIdVo.getParallelApplyinstId());
        } else {//仅进行并行申报
            List<SeriesApplyDataVo> applyDataVos = this.getSeriesApplyDataVoListFromStage(stageApplyDataPageVo);
            List<String> ids = new ArrayList<>(applyDataVos.size());
            for (SeriesApplyDataVo vo : applyDataVos) {
                vo.setIsParallel("1");
                vo.setStageId(stageApplyDataPageVo.getStageId());
                String applyinstId = aeaSeriesService.inadmissible(vo);
                ids.add(applyinstId);
            }
            applyinstIds = ids.toArray(new String[0]);
            applyinstIdVo.setApplyinstIds(applyinstIds);
        }
        if (null != applyinstIds) {
            updateAeaSmsInfo(stageApplyDataPageVo.getSmsInfoId(), applyinstIds);
            // 保存不受理回执
            String[] receiptTypes = {"3"};
            receiveService.saveReceive(applyinstIds, receiptTypes, SecurityContext.getCurrentUserName(), stageApplyDataPageVo.getComments());
        }
        return applyinstIdVo;
    }


    /**
     * 并联申报时，如果只勾选并行事项，则转换参数到单项申报
     *
     */
    private List<SeriesApplyDataVo> getSeriesApplyDataVoListFromStage(StageApplyDataPageVo applyDataPageVo) {
        List<SeriesApplyDataVo> list = new ArrayList<>();
        List<String> itemVerIds = applyDataPageVo.getPropulsionItemVerIds();
        String branchOrgMap = applyDataPageVo.getPropulsionBranchOrgMap();
        List<PropulsionItemStateVo> itemStateIds = applyDataPageVo.getPropulsionItemStateIds();
        for (String itemVerId : itemVerIds) {
            AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, SecurityContext.getCurrentOrgId());
            if (null == aeaItemBasic) return list;

            SeriesApplyDataVo vo = new SeriesApplyDataVo();
            org.springframework.beans.BeanUtils.copyProperties(applyDataPageVo, vo);
            vo.setItemVerId(itemVerId);
            vo.setAppId(aeaItemBasic.getAppId());
            String orgId = BusinessUtil.getOrgIdFromBranchOrgMap(branchOrgMap, itemVerId);
            if (StringUtils.isNotBlank(orgId)) {
                Map<String, String> map = new HashMap<>();
                map.put("branchOrg", orgId);
                map.put("itemVerId", itemVerId);
                List<Map> temp = new ArrayList<>();
                temp.add(map);
                vo.setBranchOrgMap(JSON.toJSONString(temp));
            } else {
                vo.setBranchOrgMap("");
            }
            //获取当前并行事项下已选择的情形ID
            for (PropulsionItemStateVo stateVo : itemStateIds) {
                if (stateVo.getItemVerId().equals(itemVerId)) {
                    List<String> stateIds = stateVo.getStateIds();
                    if (null != stateIds) {
                        vo.setStateIds(stateIds.toArray(new String[0]));
                    } else {
                        vo.setStateIds(new String[0]);
                    }
                    break;
                }
            }
            list.add(vo);
        }
        return list;
    }

    /**
     * 目前一个申报实例对应一个领件人
     *
     * @param smsInfoId    领件人id
     * @param applyinstIds 申报实例id
     */
    private void updateAeaSmsInfo(String smsInfoId, String[] applyinstIds) throws Exception {
        if (applyinstIds.length > 0) {
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoMapper.getAeaHiSmsInfoById(smsInfoId);
            for (String applyinstId : applyinstIds) {
                //先判断是否存在申请号或者申请实例ID与传过来的不一致：因为数据库applyisnt_id不能为空，默认第一次是item_code
                if (StringUtils.isBlank(aeaHiSmsInfo.getApplyinstId())) {
                    aeaHiSmsInfo.setApplyinstId(applyinstId);
                    aeaHiSmsInfoMapper.updateAeaHiSmsInfo(aeaHiSmsInfo);
                    continue;
                }
                if (applyinstId.equals(aeaHiSmsInfo.getApplyinstId())) {
                    continue;
                }
                AeaHiSmsInfo newOne = new AeaHiSmsInfo();
                BeanUtils.copyProperties(newOne, aeaHiSmsInfo);
                newOne.setId(UuidUtil.generateUuid());
                newOne.setApplyinstId(applyinstId);
                newOne.setCreater(SecurityContext.getCurrentUserId());
                newOne.setCreateTime(new Date());
                newOne.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiSmsInfoMapper.insertAeaHiSmsInfo(newOne);
            }
        }
    }

    public String onlyInstApply(StageApplyDataPageVo stageApplyDataPageVo) throws Exception {
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageApplyDataPageVo.getStageId());
        Assert.notNull(aeaParStage, "aeaParStage is null");
        String appId = aeaParStage.getAppId();
        String themeVerId = aeaParStage.getThemeVerId();
        StageApplyDataVo stageApplyDataVo = stageApplyDataPageVo.toStageApplyDataVo(appId, themeVerId);
        String applySource = stageApplyDataVo.getApplySource();
        String applySubject = stageApplyDataVo.getApplySubject();
        String linkmanInfoId = stageApplyDataVo.getLinkmanInfoId();
        String branchOrgMap = stageApplyDataVo.getBranchOrgMap();//是否分局承办，允许为空
        AeaHiApplyinst applyinst = aeaHiApplyinstService.createAeaHiApplyinst(applySource, applySubject, linkmanInfoId, "0", branchOrgMap, ApplyState.RECEIVE_APPROVED_APPLY.getValue(), "0",null);
        return applyinst == null ? null : applyinst.getApplyinstId();
    }

    public String seriesOnlyInstApply(SeriesApplyDataVo seriesApplyDataVo) throws Exception {
        String applySource = seriesApplyDataVo.getApplySource();
        String applySubject = seriesApplyDataVo.getApplySubject();
        String linkmanInfoId = seriesApplyDataVo.getLinkmanInfoId();
        String branchOrgMap = seriesApplyDataVo.getBranchOrgMap();//是否分局承办，允许为空
        AeaHiApplyinst applyinst = aeaHiApplyinstService.createAeaHiApplyinst(applySource, applySubject, linkmanInfoId, "1", branchOrgMap, ApplyState.RECEIVE_APPROVED_APPLY.getValue(),"0",null);
        return applyinst.getApplyinstId();
    }
}
