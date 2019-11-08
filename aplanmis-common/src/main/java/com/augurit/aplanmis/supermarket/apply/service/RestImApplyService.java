package com.augurit.aplanmis.supermarket.apply.service;

import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AeaUnitConstants;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.AuditFlagStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.process.AeaBpmProcessService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.augurit.aplanmis.supermarket.apply.vo.ApplyinstResult;
import com.augurit.aplanmis.supermarket.apply.vo.ImItemApplyData;
import com.augurit.aplanmis.supermarket.apply.vo.ImPurchaseData;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Author: lucas Chan
 * Date: 2019-11-6
 * Description: 工建审批系统和中介超市发布采购需求，并启动流程相关接口
 */
@Service
@Transactional
public class RestImApplyService {

    @Autowired
    private RestTimeruleinstService restTimeruleinstService;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    @Autowired
    private AeaHiSeriesinstService aeaHiSeriesinstService;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    private AeaHiItemInoutinstService aeaHiItemInoutinstService;

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;

    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;

    @Autowired
    private AeaBpmProcessService aeaBpmProcessService;

    @Autowired
    private BpmProcessService bpmProcessService;

    @Autowired
    private AeaParentProjMapper aeaParentProjMapper;

    @Autowired
    private AeaImPurchaseinstMapper aeaImPurchaseinstMapper;

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaImUnitRequireMapper aeaImUnitRequireMapper;
    @Autowired
    AeaProjLinkmanMapper aeaProjLinkmanMapper;

    @Autowired
    AeaImBiddingPriceMapper aeaImBiddingPriceMapper;

    @Autowired
    AeaImAvoidUnitMapper aeaImAvoidUnitMapper;

    @Autowired
    private AeaImMajorQualMapper aeaImMajorQualMapper;


    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;


    @Autowired
    private AeaImUnitServiceMapper aeaImUnitServiceMapper;

    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;


    /**
     * 唐山模式---发起申报，挂起流程，选取中介后 流程流转到部门审批
     *
     * @param applyData
     */
    public ApplyinstResult purchaseStartProcess(ImItemApplyData applyData) throws Exception {
        ApplyinstResult result = new ApplyinstResult();
        String itemVerId = applyData.getItemVerId();
        AeaItemBasic itemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, SecurityContext.getCurrentOrgId());

        String appinstId = UUID.randomUUID().toString();//预先生成流程模板实例ID
        result.setAppinstId(appinstId);
//        String branchOrgMap = applyData.getBranchOrgMap();
        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        AeaHiApplyinst seriesApplyinst = aeaHiApplyinstService.createAeaHiApplyinstAndTriggerAeaLogApplyStateHist(applyData.getApplySource(),
                applyData.getApplySubject(), applyData.getLinkmanInfoId(), "1", null, null, appinstId, ApplyState.IM_MILESTONE_CHOOSE_IMUNIT.getValue(), opuWinId);//实例化串联申请实例
        result.setApplyinstId(seriesApplyinst.getApplyinstId());
        result.setApplyinstCode(seriesApplyinst.getApplyinstCode());

        String seriesApplyinstId = seriesApplyinst.getApplyinstId();//申报实例ID

        seriesApplyinst.setProjInfoId(applyData.getProjInfoId());

        //1、保存单项实例
        AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(seriesApplyinstId, appinstId, null, null);

        //2、事项实例
        AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), itemVerId, null, null, appinstId);


        //暂时把中介事项设置为不分情形 2019.11.7

        //把所有情形丢到变量里，用于流程启动情形
        /* if (stateIds != null && stateIds.length > 0) {
            Map<String, Boolean> stateinsts = new HashMap();
            for (String stateId : stateIds) {
                stateinsts.put(stateId, true);
            }
            if (stateinsts.size() > 0)
                seriesApplyinst.setStateinsts(stateinsts);
        }
        */

        //3、情形实例---中介事项默认不分情形，不需要实例化
//        aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(seriesApplyinstId, aeaHiSeriesinst.getSeriesinstId(), null, stateIds, SecurityContext.getCurrentUserName());

        //4、材料输入输出实例
        aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(applyData.getMatinstsIds(), seriesApplyinstId, SecurityContext.getCurrentUserName());

        //5、启动主流程
        BpmProcessInstance processInstance = aeaBpmProcessService.startFlow(itemBasic.getAppId(), appinstId, seriesApplyinst);

        if (processInstance == null || processInstance.getProcessInstance() == null) {
            throw new RuntimeException("流程启动失败！");
        }

        //新增时限规则实例
        restTimeruleinstService.createTimeruleinstByProcinst(itemBasic.getAppId(), processInstance.getProcessInstance().getId(), processInstance.getProcessInstance().getProcessDefinitionKey());

        //查询出流程第一个节点
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstance().getId()).list();

        //挂起当前流程
        bpmProcessService.suspendProcessInstanceById(processInstance.getProcessInstance().getId());

        //6.流程发起后，更新初始事项历史的taskId
        AeaLogItemStateHist logItemStateHist = aeaLogItemStateHistService.getInitStateAeaLogItemStateHist(aeaHiIteminst.getIteminstId(), appinstId);
        logItemStateHist.setTaskinstId(tasks.get(0).getId());
        aeaLogItemStateHistService.updateAeaLogItemStateHist(logItemStateHist);

        //流程发起后，更新初始申请历史的taskId
        AeaLogApplyStateHist applyStateHist = aeaLogApplyStateHistService.getInitStateAeaLogApplyStateHist(seriesApplyinstId, appinstId);
        applyStateHist.setTaskinstId(tasks.get(0).getId());
        aeaLogApplyStateHistService.updateAeaLogApplyStateHist(applyStateHist);

        //8.保存申请实例与项目之间的关系 aea_applyinst_proj
        AeaApplyinstProj aeaApplyinstProj = new AeaApplyinstProj();
        aeaApplyinstProj.setApplyinstId(seriesApplyinst.getApplyinstId());
        aeaApplyinstProj.setApplyinstProjId(UUID.randomUUID().toString());
        aeaApplyinstProj.setProjInfoId(applyData.getProjInfoId());//4.0版本已废弃了多项目申报，所以只有一个值
        aeaApplyinstProj.setCreater(SecurityContext.getCurrentUserName());
        aeaApplyinstProj.setCreateTime(new Date());
        aeaApplyinstProjMapper.insertAeaApplyinstProj(aeaApplyinstProj);


        //9、申报主体
        if ("0".equals(applyData.getApplySubject())) { //申报主体为个人
            aeaLinkmanInfoService.insertApplyAndLinkProjLinkman(seriesApplyinstId, new String[]{applyData.getProjInfoId()}, applyData.getApplyLinkmanId(), applyData.getLinkmanInfoId());
        } else {

            //建设单位
            Map<String, List<String>> puMap = new HashMap();
            List<String> unitInfoIds = new ArrayList();
            unitInfoIds.add(applyData.getConstructionUnitId());
            puMap.put(applyData.getProjInfoId(), unitInfoIds);
            aeaUnitInfoService.insertApplyOwnerUnitProj(seriesApplyinstId, puMap);//创建采购需求项目和建设单位关联

            //如果是投资项目，则需要创建投资项目和建设单位关联
            if ("1".equals(applyData.getIsApproveProj())) {

                AeaUnitProj unitProj = aeaUnitProjMapper.findUnitPorojByProjInfoIdAndUnitInfoId(applyData.getProjInfoId(), applyData.getConstructionUnitId(), AeaUnitConstants.IS_OWNER_TRUE);
                if (unitProj == null) {
                    unitProj = new AeaUnitProj();
                    unitProj.setUnitInfoId(UUID.randomUUID().toString());
                    unitProj.setIsOwner(AeaUnitConstants.IS_OWNER_TRUE);
                    unitProj.setIsDeleted("0");
                    unitProj.setProjInfoId(applyData.getProjInfoId());
                    unitProj.setUnitInfoId(applyData.getConstructionUnitId());
                    unitProj.setUnitType("1");// 1 建设单位
                    unitProj.setCreater(SecurityContext.getCurrentUserId());
                    unitProj.setCreateTime(new Date());
                    aeaUnitProjMapper.insertAeaUnitProj(unitProj);
                }
            }
        }
        return result;
    }

    /**
     * 保存采购项目信息
     *
     * @param data
     * @throws Exception
     */
    public AeaImProjPurchase savePurchaseProjInfo(ImPurchaseData data, String applySubject) throws Exception {
        //参数校验
        this.validParams(data);
        String unitInfoId = data.getPublishUnitInfoId();
        //初始化采购需求实体信息
        AeaImProjPurchase aeaImProjPurchase = data.createAeaImProjPurchase();

        //保存机构要求
        AeaImUnitRequire aeaImUnitRequire = data.getAeaImUnitRequire();
        aeaImUnitRequire.setUnitRequireId(UuidUtil.generateUuid());
        aeaImUnitRequire.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaImUnitRequireMapper.insertAeaImUnitRequire(aeaImUnitRequire);

        aeaImProjPurchase.setUnitRequireId(aeaImUnitRequire.getUnitRequireId());

        //保存资质专业要求
        List<AeaImMajorQual> aeaImMajorQuals = data.getAeaImMajorQuals();
        if (aeaImMajorQuals != null && "1".equals(aeaImUnitRequire.getIsQualRequire())) {
            for (AeaImMajorQual aeaImMajorQual : aeaImMajorQuals) {
                aeaImMajorQual.buildImMajor(aeaImUnitRequire.getUnitRequireId(), data.getCreater());
                aeaImMajorQualMapper.insertAeaImMajorQual(aeaImMajorQual);
            }
        }
        //保存采购项目
        AeaProjInfo aeaProjInfo = data.createProjInfo(data);
        aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);
        aeaImProjPurchase.setProjInfoId(aeaProjInfo.getProjInfoId());

        if ("0".equals(data.getApplySubject())) {
            aeaImProjPurchase.setPublishLinkmanInfoId(data.getPublishLinkmanInfoId());
        } else {
            aeaImProjPurchase.setPublishUnitInfoId(unitInfoId);
        }

        //保存采购项目信息表
        aeaImProjPurchaseMapper.insertAeaImProjPurchase(aeaImProjPurchase);
        this.savePurchaseinst(data);

        //创建投资项目和采购需求项目ID关联
        if ("1".equals(data.getIsApproveProj())) {
            String parentProjId = data.getProjInfoId();
            String childProjId = aeaProjInfo.getProjInfoId();
            String projSeq = data.getProjInfoId() + "," + data.getProjPurchaseId();
            AeaParentProj aeaParentProj = new AeaParentProj(parentProjId, childProjId, projSeq, data.getCreater(), data.getRootOrgId());
            aeaParentProjMapper.insertAeaParentProj(aeaParentProj);
        }
        //保存采购项目与联系人关联
        String linkmanInfoId = data.getLinkmanInfoId();
        if (StringUtils.isBlank(linkmanInfoId)) {
            List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.findAllUnitLinkman(unitInfoId);

            for (AeaLinkmanInfo linkmanInfo : aeaLinkmanInfos) {
                if (linkmanInfo.getLinkmanType() != null && linkmanInfo.getLinkmanType().contains("u")) {
                    linkmanInfoId = linkmanInfo.getLinkmanInfoId();
                    break;
                }
            }
        }
        //保存项目联系人关系
        if (StringUtils.isNotBlank(linkmanInfoId)) {
            AeaProjLinkman aeaProjLinkman = new AeaProjLinkman(aeaProjInfo.getProjInfoId(), linkmanInfoId, "link", data.getApplyinstId(), data.getCreater());
            aeaProjLinkmanMapper.insertAeaProjLinkman(aeaProjLinkman);
        }


        //保存企业报价
        if (AeaImProjPurchase.BiddingType.自主选择.getType().equals(aeaImProjPurchase.getBiddingType())) {
            if (StringUtils.isNotBlank(data.getAgentUnitInfoId())) {
                saveImUnitBidding(data, aeaImProjPurchase.getProjPurchaseId());
            }
        }

        // 保存回避单位
        if (Status.ON.equals(data.getIsAvoid())) {
            String avoidUnitInfoIds = data.getAvoidUnitInfoIds();
            for (String avoidUnitInfoId : avoidUnitInfoIds.split(",")) {
                AeaImAvoidUnit aeaImAvoidUnit = new AeaImAvoidUnit();
                aeaImAvoidUnit.setAvoidUnitId(UuidUtil.generateUuid());
                aeaImAvoidUnit.setUnitInfoId(avoidUnitInfoId);
                aeaImAvoidUnit.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
                aeaImAvoidUnit.setCreater(SecurityContext.getCurrentUserName());
                aeaImAvoidUnit.setCreateTime(new Date());
                aeaImAvoidUnit.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                aeaImAvoidUnitMapper.insertAeaImAvoidUnit(aeaImAvoidUnit);
            }
        }


        return aeaImProjPurchase;
    }

    /**
     * 保存采购实例信息
     *
     * @param purchaseData
     * @throws Exception
     */
    public void savePurchaseinst(ImPurchaseData purchaseData) throws Exception {
        //初始化采购需求历史状态信息
        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.buildImPurchaseinst(purchaseData.getProjPurchaseId(), AuditFlagStatus.WAIT_CHOOSE, "root", purchaseData.getLinkmanInfoId(), "0", purchaseData.getCreater(), purchaseData.getRootOrgId());
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);
    }

    private void saveAeaImMajorQual(AeaImMajorQual aeaImMajorQual, String unitRequireId) throws Exception {
        aeaImMajorQual.setMajorQualId(UuidUtil.generateUuid());
        aeaImMajorQual.setCreateTime(new Date());
        aeaImMajorQual.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
        aeaImMajorQual.setUnitRequireId(unitRequireId);
        aeaImMajorQualMapper.insertAeaImMajorQual(aeaImMajorQual);
    }

    private void saveImUnitBidding(ImPurchaseData data, String projPurchaseId) throws Exception {
        AeaUnitInfo agentUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(data.getAgentUnitInfoId());

        if (agentUnitInfo != null) {

            AeaImUnitService aeaImUnitService = aeaImUnitServiceMapper.getUnitServiceByUnitInfoIdAndServiceItemId(agentUnitInfo.getUnitInfoId(), data.getServiceItemId());

            if (aeaImUnitService != null) {
                AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
                aeaImUnitBidding.buildImUnitBidding(projPurchaseId, data.getAgentUnitInfoId(), aeaImUnitService.getUnitServiceId(), data.getCreater(), data.getRootOrgId());
                // 查询已绑定联系人
                List<AeaLinkmanInfo> aeaLinkmanInfos = aeaLinkmanInfoMapper.listBindLinkmanByUnitId(data.getAgentUnitInfoId(), "1", "1", "");
                if (aeaLinkmanInfos != null && aeaLinkmanInfos.size() > 0) {
                    AeaLinkmanInfo linkmanInfo = aeaLinkmanInfos.get(0);
                    aeaImUnitBidding.setLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
                }
                aeaImUnitBiddingMapper.insertAeaImUnitBidding(aeaImUnitBidding);

                // 保存竞价价格
                AeaImBiddingPrice aeaImBiddingPrice = new AeaImBiddingPrice(aeaImUnitBidding.getUnitBiddingId(), data.getBasePrice(), "1", "0", data.getCreater(), data.getRootOrgId());
                aeaImBiddingPriceMapper.insertAeaImBiddingPrice(aeaImBiddingPrice);
            }
        }
    }

    private void validParams(ImPurchaseData data) throws Exception {
        if (StringUtils.isBlank(data.getServiceItemId()))
            throw new Exception("缺少参数：ServiceItemId ");
        if (StringUtils.isBlank(data.getLinkmanInfoId()))
            throw new Exception("缺少参数：LinkmanInfoId ");
        if (StringUtils.isBlank(data.getBiddingType()))
            throw new Exception("缺少参数：BiddingType ");
        if (data.getChoiceImunitTime() == null)
            throw new Exception("缺少参数：ChoiceImunitTime ");
        if (data.getExpirationDate() == null)
            throw new Exception("缺少参数：ExpirationDate ");

        AeaImUnitRequire aeaImUnitRequire = data.getAeaImUnitRequire();

        if (aeaImUnitRequire == null) {
            throw new RuntimeException("缺少中介机构要求信息");
        }
        AeaProjInfo aeaProjInfoCond = new AeaProjInfo();
        aeaProjInfoCond.setProjName(data.getProjName());
        List aeaProjInfoCondList = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfoCond);
        if (!aeaProjInfoCondList.isEmpty()) {
            throw new RuntimeException("项目名称已存在");
        }
    }
}
