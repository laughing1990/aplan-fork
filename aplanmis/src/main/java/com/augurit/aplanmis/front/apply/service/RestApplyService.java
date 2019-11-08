package com.augurit.aplanmis.front.apply.service;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.mapper.AeaHiSmsInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.utils.BusinessUtil;
import com.augurit.aplanmis.front.apply.vo.*;
import com.augurit.aplanmis.front.receive.service.ReceiveService;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private AeaHiApplyinstService aeaHiApplyinstService;

    /**
     * 现场登记 --> 收件，发起申报
     *
     * @param seriesApplyDataPageVo 申报参数
     * @return applyinstId 申请实例ID
     * @throws Exception
     */
    public String startSeriesFlow(SeriesApplyDataPageVo seriesApplyDataPageVo) throws Exception {
        String applyinstIdParam = seriesApplyDataPageVo.getApplyinstId();
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(seriesApplyDataPageVo.getItemVerId(), SecurityContext.getCurrentOrgId());
        SeriesApplyDataVo dataVo = seriesApplyDataPageVo.toSeriesApplyDataVo(aeaItemBasic.getAppId());
        String applyinstId = aeaSeriesService.stageApplay(dataVo);
        updateAeaSmsInfo(seriesApplyDataPageVo.getSmsInfoId(), new String[]{applyinstId});
        //保存受理回执，物料回执
        if (StringUtils.isBlank(applyinstIdParam)) {
            receiveService.saveReceive(new String[]{applyinstId}, new String[]{"1", "2"}, SecurityContext.getCurrentUserName(), "");
        }
        return applyinstId;
    }

    /**
     * 现场登记 --> 生成实例，打印回执
     *
     * @param seriesApplyDataPageVo 申报餐宿
     * @return applyinstId 申请实例ID
     * @throws Exception
     */
    public String instantiateSeriesFlow(SeriesApplyDataPageVo seriesApplyDataPageVo) throws Exception {
        // 如果是多次打印回执，直接返回申报实例
        if (StringUtils.isNotBlank(seriesApplyDataPageVo.getApplyinstId())) {
            return seriesApplyDataPageVo.getApplyinstId();
        }
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(seriesApplyDataPageVo.getItemVerId(), SecurityContext.getCurrentOrgId());
        SeriesApplyDataVo dataVo = seriesApplyDataPageVo.toSeriesApplyDataVo(aeaItemBasic.getAppId());
        dataVo.setIsParallel("0");
        String applyinstId = aeaSeriesService.stagingApply(dataVo);
        updateAeaSmsInfo(seriesApplyDataPageVo.getSmsInfoId(), new String[]{applyinstId});
        //保存受理回执，物料回执---fixme ?应该只有物料回执
        String[] receiveTypes = new String[]{"1", "2"};
        receiveService.saveReceive(new String[]{applyinstId}, receiveTypes, SecurityContext.getCurrentUserName(), "");
        return applyinstId;
    }

    /**
     * 现场登记 --> 不予受理，生成实例，启动流程，打印不受理回执
     *
     * @param seriesApplyDataPageVo 申报入参
     * @return applyinstId 申请实例ID
     */
    public String inadmissibleSeriesFlow(SeriesApplyDataPageVo seriesApplyDataPageVo) throws Exception {

        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(seriesApplyDataPageVo.getItemVerId(), SecurityContext.getCurrentOrgId());
        SeriesApplyDataVo seriesApplyDataVo = seriesApplyDataPageVo.toSeriesApplyDataVo(aeaItemBasic.getAppId());
        seriesApplyDataVo.setIsParallel("0");
        String applyinstId = aeaSeriesService.inadmissible(seriesApplyDataVo);
        updateAeaSmsInfo(seriesApplyDataPageVo.getSmsInfoId(), new String[]{applyinstId});
        //保存不受理回执
        String[] receiveTypes = new String[]{"3"};
        receiveService.saveReceive(new String[]{applyinstId}, receiveTypes, SecurityContext.getCurrentUserName(), seriesApplyDataPageVo.getComments());
        return applyinstId;
    }

    /**
     * 并联申报 --> 生成实例，打印回执
     * 并联申报可以支持只申报并行推进事项，不申报并联申报事项；
     *
     * @param stageApplyDataPageVo
     * @return
     * @throws Exception
     */
    public ApplyinstIdVo instantiateStageProcess(StageApplyDataPageVo stageApplyDataPageVo) throws Exception {
        ApplyinstIdVo applyinstIdVo = new ApplyinstIdVo();
        // 如果是多次打印回执，直接返回申报实例
        if ("1".equals(stageApplyDataPageVo.getIsPrintReceive())) {
            applyinstIdVo.setApplyinstIds(stageApplyDataPageVo.getApplyinstIds());
            applyinstIdVo.setParallelApplyinstId(stageApplyDataPageVo.getParallelApplyinstId());
            return applyinstIdVo;
        }
        String[] applyinstIds;
        //20190819 小糊涂 并联申报可以支持只申报并行推进事项，不申报并联申报事项；
        List<String> itemVerIds = stageApplyDataPageVo.getItemVerIds();
        if (null != itemVerIds || itemVerIds.size() > 0) {
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
                String applyinstId = aeaSeriesService.stagingApply(vo);
                list.add(applyinstId);
            }
            applyinstIds = list.toArray(new String[0]);
            applyinstIdVo.setApplyinstIds(applyinstIds);
        }
        if (null != applyinstIds && applyinstIds.length > 0) {
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
     * @param stageApplyDataPageVo
     * @return applyinstIds 申请实例集合
     * @throws Exception
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
                String applyinstId = aeaSeriesService.stageApplay(vo);
                ids.add(applyinstId);
            }
            applyinstIds = ids.toArray(new String[0]);
            applyinstIdVo.setApplyinstIds(applyinstIds);
        }
        if (null != applyinstIds) {
            updateAeaSmsInfo(stageApplyDataPageVo.getSmsInfoId(), applyinstIds);
            // 保存回执
            String[] receiptTypes = {"1", "2"};
            receiveService.saveReceive(applyinstIds, receiptTypes, SecurityContext.getCurrentUserName(), stageApplyDataPageVo.getComments());
        }

        return applyinstIdVo;
    }


    /**
     * 并联申报 --> 发起申报，不予受理
     *
     * @param stageApplyDataPageVo
     * @return
     * @throws Exception
     */
    public ApplyinstIdVo inadmissibleStageProcess(StageApplyDataPageVo stageApplyDataPageVo) throws Exception {
        ApplyinstIdVo applyinstIdVo = new ApplyinstIdVo();
        String[] applyinstIds;
        //20190820 小糊涂 并联申报可以支持只申报并行推进事项，不申报并联申报事项；
        List<String> itemVerIds = stageApplyDataPageVo.getItemVerIds();
        if (null != itemVerIds || itemVerIds.size() > 0) {
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
     * @param applyDataPageVo StageApplyDataPageVo
     * @return
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
                Map map = new HashMap();
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
        List<String> itemVerIds = stageApplyDataPageVo.getItemVerIds();
        if (null != itemVerIds && itemVerIds.size() > 0) {
            AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageApplyDataPageVo.getStageId());
            Assert.notNull(aeaParStage, "aeaParStage is null");
            String appId = aeaParStage.getAppId();
            String themeVerId = aeaParStage.getThemeVerId();
            StageApplyDataVo stageApplyDataVo = stageApplyDataPageVo.toStageApplyDataVo(appId, themeVerId);
            String applySource = stageApplyDataVo.getApplySource();
            String applySubject = stageApplyDataVo.getApplySubject();
            String linkmanInfoId = stageApplyDataVo.getLinkmanInfoId();
            String branchOrgMap = stageApplyDataVo.getBranchOrgMap();//是否分局承办，允许为空
            AeaHiApplyinst applyinst = aeaHiApplyinstService.createAeaHiApplyinst(applySource, applySubject, linkmanInfoId, "0", branchOrgMap, ApplyState.RECEIVE_APPROVED_APPLY.getValue());
            return applyinst == null ? null : applyinst.getApplyinstId();
        } else {
            throw new Exception("未选择并联事项");
        }
    }

}
