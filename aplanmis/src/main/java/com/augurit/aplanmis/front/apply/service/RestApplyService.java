package com.augurit.aplanmis.front.apply.service;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.item.service.AeaItemRelevanceService;
import com.augurit.aplanmis.admin.market.service.service.AeaImServiceService;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaImPurchaseinst;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemRelevance;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaHiSmsInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaImProjPurchaseMapper;
import com.augurit.aplanmis.common.mapper.AeaImPurchaseinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.receive.ReceiveService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.utils.BusinessUtil;
import com.augurit.aplanmis.front.apply.vo.ApplyinstIdVo;
import com.augurit.aplanmis.front.apply.vo.PropulsionItemStateVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyDataPageVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyDataVo;
import com.augurit.aplanmis.front.apply.vo.StageApplyDataPageVo;
import com.augurit.aplanmis.front.apply.vo.StageApplyDataVo;
import com.augurit.aplanmis.supermarket.apply.vo.ImServiceItemPurchaseVo;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaItemBasicAdminService aeaItemBasicAdminService;
    @Autowired
    private AeaImServiceService aeaImServiceService;
    @Autowired
    private AeaItemRelevanceService aeaItemRelevanceService;
    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;
    @Autowired
    private AeaImPurchaseinstMapper aeaImPurchaseinstMapper;

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
        if (StringUtils.isNotBlank(seriesApplyDataPageVo.getApplyinstId()) && !Status.ON.equals(seriesApplyDataPageVo.getIsJustApplyinst())) {
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

    /**
     * 根据材料ID获取相关中介服务事项信息和项目信息
     *
     * @return
     * @throws Exception
     */
    public Map getImServiceItemsAndProjInfo(String matId, String applyinstId) throws Exception {

        if (StringUtils.isBlank(matId) || StringUtils.isBlank(applyinstId)) return null;

        Map map = new HashMap();

        ImServiceItemPurchaseVo imServiceItemPurchaseVo = new ImServiceItemPurchaseVo();

        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (aeaHiApplyinst == null) return null;

        List<AeaProjInfo> projInfos = aeaProjInfoService.findApplyProj(applyinstId);
        if (projInfos.size() < 1) return null;
        AeaProjInfo projInfo = projInfos.get(0);

        imServiceItemPurchaseVo.setApplyinstId(applyinstId);
        imServiceItemPurchaseVo.setApplyinstCode(aeaHiApplyinst.getApplyinstCode());
        imServiceItemPurchaseVo.setPurchaseCode(projInfo.getDistrict() + aeaHiApplyinst.getApplyinstCode());

        imServiceItemPurchaseVo.setProjInfoId(projInfo.getProjInfoId());
        imServiceItemPurchaseVo.setLocalCode(projInfo.getLocalCode());
        imServiceItemPurchaseVo.setPurchaseName(projInfo.getProjName());

        Map projScale = new HashMap();
        projScale.put("buildAreaSum", projInfo.getBuildAreaSum());// 建筑面积
        projScale.put("investSum", projInfo.getInvestSum());// 总投资
        projScale.put("xmYdmj", projInfo.getXmYdmj());// 用地面积

        AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
        if (linkmanInfo == null) return null;
        imServiceItemPurchaseVo.setLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
        imServiceItemPurchaseVo.setLinkmanName(linkmanInfo.getLinkmanName());
        imServiceItemPurchaseVo.setLinkmanMobilePhone(linkmanInfo.getLinkmanMobilePhone());

        //申办主体为单位
        if ("1".equals(aeaHiApplyinst.getApplySubject())) {

            List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(applyinstId, projInfo.getProjInfoId());
            if (aeaUnitInfos.size() < 1) return null;
            AeaUnitInfo unitInfo = aeaUnitInfos.get(0);
            imServiceItemPurchaseVo.setUnitInfoId(unitInfo.getUnitInfoId());
            imServiceItemPurchaseVo.setApplicant(unitInfo.getApplicant());
            imServiceItemPurchaseVo.setApplySubject("1");
        } else {
            imServiceItemPurchaseVo.setApplySubject("0");
            imServiceItemPurchaseVo.setUnitInfoId(linkmanInfo.getLinkmanInfoId());
            imServiceItemPurchaseVo.setApplicant(linkmanInfo.getLinkmanName());
        }

        //根据输出材料ID获取对应的中介服务事项
        List<AeaItemBasic> itemBasics = aeaItemBasicAdminService.getAeaItemBasicsListByOutputMatId(matId);

        List<Map> serviceItemsInfo = new ArrayList();
        Map serviceMap = new HashMap();

        for (AeaItemBasic itemBasic : itemBasics) {

            AeaItemRelevance aeaItemRelevance = new AeaItemRelevance();
            aeaItemRelevance.setChildItemId(itemBasic.getItemId());
            aeaItemRelevance.setIsDelete("0");

            Map itemVo = new HashMap();
            itemVo.put("itemName", itemBasic.getItemName());
            itemVo.put("parItem", aeaItemRelevanceService.listAeaItemRelevance(aeaItemRelevance));// 获取关联行政事项

            //获取中介服务事项所关联的所有中介服务
            List<AeaImService> services = aeaImServiceService.listAeaImServiceNoPageByItemVerId(itemBasic.getItemVerId());
            for (AeaImService service : services) {

                serviceMap.put(service.getServiceId(), service);
                itemVo.put("serviceItemId", service.getServiceItemId());
                itemVo.put("serviceId", service.getServiceId());
                serviceItemsInfo.add(itemVo);
            }
        }

        List<Map> _services = new ArrayList();

        if (serviceItemsInfo.size() > 0) {

            Collection<AeaImService> valueCollection = serviceMap.values();
            List<AeaImService> services = new ArrayList(valueCollection);

            //按照中介服务来分类
            for (AeaImService service : services) {
                Map serviceVo = new HashMap();
                serviceVo.put("serviceId", service.getServiceId());
                serviceVo.put("serviceName", service.getServiceName());

                List<Map> temp = new ArrayList();
                for (Map itemVo : serviceItemsInfo) {
                    if (itemVo.get("serviceId").equals(service.getServiceId())) {
                        temp.add(itemVo);
                    }
                }
                serviceVo.put("serviceItem", temp);
                _services.add(serviceVo);
            }
        }

        map.put("purchaseInfo", imServiceItemPurchaseVo);
        map.put("projScale", projScale);
        map.put("serviceItemsInfo", _services);

        return map;
    }

    /**
     * 发布项目采购需求
     *
     * @param imServiceItemPurchaseVo
     * @throws Exception
     */
    public ResultForm createImServiceItemPurchase(ImServiceItemPurchaseVo imServiceItemPurchaseVo) throws Exception {

        try {

            if (StringUtils.isBlank(imServiceItemPurchaseVo.getServiceItemId()))
                return new ResultForm(false, "缺少参数：ServiceItemId ");
            if (StringUtils.isBlank(imServiceItemPurchaseVo.getProjInfoId()))
                return new ResultForm(false, "缺少参数：ProjInfoId ");
            if (StringUtils.isBlank(imServiceItemPurchaseVo.getLinkmanInfoId()))
                return new ResultForm(false, "缺少参数：LinkmanInfoId ");
            if (StringUtils.isBlank(imServiceItemPurchaseVo.getBiddingType()))
                return new ResultForm(false, "缺少参数：BiddingType ");
            if (imServiceItemPurchaseVo.getChoiceImunitTime() == null)
                return new ResultForm(false, "缺少参数：ChoiceImunitTime ");
            if (imServiceItemPurchaseVo.getExpirationDate() == null)
                return new ResultForm(false, "缺少参数：ExpirationDate ");

            //初始化采购需求实体信息
            AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
            aeaImProjPurchase.setProjPurchaseId(UUID.randomUUID().toString());
            aeaImProjPurchase.setProjInfoId(imServiceItemPurchaseVo.getProjInfoId());
            aeaImProjPurchase.setServiceItemId(imServiceItemPurchaseVo.getServiceItemId());// 服务和中介服务事项关联ID
            aeaImProjPurchase.setQuoteType("0");// 报价方式,0 金额 1 下浮率
            aeaImProjPurchase.setChoiceImunitTime(imServiceItemPurchaseVo.getChoiceImunitTime());// 选取中介时间
            aeaImProjPurchase.setExpirationDate(imServiceItemPurchaseVo.getExpirationDate());// 截止日期
            aeaImProjPurchase.setIsDefineAmount(imServiceItemPurchaseVo.getIsDefineAmount());// 是否确认金额，1 是 0 否
            aeaImProjPurchase.setSelectCondition(imServiceItemPurchaseVo.getSelectCondition());// 服务选择条件：1 多个服务具备其一，0 多个服务都具备
            aeaImProjPurchase.setOwnerComplaintPhone(imServiceItemPurchaseVo.getLinkmanMobilePhone());// 业主投诉电话
            aeaImProjPurchase.setIsDiscloseIm(imServiceItemPurchaseVo.getIsDiscloseIm());// 是否公示中选机构： 1 是， 0 否
            aeaImProjPurchase.setIsDiscloseBidding(imServiceItemPurchaseVo.getIsDiscloseBidding());// 是否公示中标公告：1 是， 0 否
            aeaImProjPurchase.setIsLiveWitness("0");// 是否现场见证：1 是， 0 否
            aeaImProjPurchase.setApplyinstCode(imServiceItemPurchaseVo.getApplyinstCode());// 关联的审批流水号
            aeaImProjPurchase.setIsApproveProj("1");// 是否为投资审批项目：1 是，0 否
            aeaImProjPurchase.setContacts(imServiceItemPurchaseVo.getLinkmanName());// 业主联系人
            aeaImProjPurchase.setMoblie(imServiceItemPurchaseVo.getLinkmanMobilePhone());// 联系电话
            aeaImProjPurchase.setBiddingType(imServiceItemPurchaseVo.getBiddingType());// 竞价类型：1 随机中标，2 自主选择
            aeaImProjPurchase.setAuditFlag("0");// 采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时
            aeaImProjPurchase.setBasePrice(imServiceItemPurchaseVo.getBasePrice());// 最低价格（万元）
            aeaImProjPurchase.setHighestPrice(imServiceItemPurchaseVo.getHighestPrice());// 最高价格（万元）
            aeaImProjPurchase.setServiceContent(imServiceItemPurchaseVo.getServiceContent());// 服务内容
            aeaImProjPurchase.setLinkmanInfoId(imServiceItemPurchaseVo.getLinkmanInfoId());// 业主委托人信息ID
            aeaImProjPurchase.setIsDelete("0");
            aeaImProjPurchase.setIsActive("1");
            aeaImProjPurchase.setCreater(SecurityContext.getCurrentUserId());
            aeaImProjPurchase.setCreateTime(new Date());
            aeaImProjPurchase.setRootOrgId(SecurityContext.getCurrentOrgId());
            //申报主体为单位时
            if ("1".equals(imServiceItemPurchaseVo.getApplySubject())) {
                aeaImProjPurchase.setPublishUnitInfoId(imServiceItemPurchaseVo.getUnitInfoId());// 业主单位ID
            }
            aeaImProjPurchaseMapper.insertAeaImProjPurchase(aeaImProjPurchase);

            //初始化采购需求历史状态信息
            AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
            aeaImPurchaseinst.setPurchaseinstId(UUID.randomUUID().toString());
            aeaImPurchaseinst.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
            aeaImPurchaseinst.setNewPurchaseFlag("0");// 采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时
            aeaImPurchaseinst.setOldPurchaseFlag("0");// 采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时
            aeaImPurchaseinst.setParentPurchaseinstId("root");
            aeaImPurchaseinst.setLinkmanInfoId(imServiceItemPurchaseVo.getLinkmanInfoId());
            aeaImPurchaseinst.setIsOwnFile("0");// 是否拥有附件
            aeaImPurchaseinst.setCreater(SecurityContext.getCurrentUserId());
            aeaImPurchaseinst.setCreateTime(new Date());
            aeaImPurchaseinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "采购需求发布失败！");
        }

        return new ResultForm(true, "采购需求发布成功！");
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

    public String seriesOnlyInstApply(SeriesApplyDataPageVo seriesApplyDataPageVo) throws Exception {
        String applySource = seriesApplyDataPageVo.getApplySource();
        String applySubject = seriesApplyDataPageVo.getApplySubject();
        String linkmanInfoId = seriesApplyDataPageVo.getLinkmanInfoId();
        String branchOrgMap = seriesApplyDataPageVo.getBranchOrgMap();//是否分局承办，允许为空
        AeaHiApplyinst applyinst = aeaHiApplyinstService.createAeaHiApplyinst(applySource, applySubject, linkmanInfoId, "1", branchOrgMap, ApplyState.RECEIVE_APPROVED_APPLY.getValue());
        return applyinst.getApplyinstId();
    }
}
