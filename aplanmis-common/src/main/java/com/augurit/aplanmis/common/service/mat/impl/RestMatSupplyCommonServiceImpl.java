package com.augurit.aplanmis.common.service.mat.impl;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.sc.day.service.WorkdayHolidayService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrect;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrectDueIninst;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrectRealIninst;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrectStateHist;
import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaHiParStateinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.domain.AeaParIn;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.dto.CorrectinstDto;
import com.augurit.aplanmis.common.dto.MatCorrectDto;
import com.augurit.aplanmis.common.dto.MatCorrectInfoDto;
import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaItemInoutMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaParInMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectDueIninstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectRealIninstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectStateHistService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStateinstService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.mat.RestMatCorrectCommonService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class RestMatSupplyCommonServiceImpl implements RestMatCorrectCommonService {

    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

    @Autowired
    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;

    @Autowired
    private AeaHiParStateinstService aeaHiParStateinstService;

    @Autowired
    private AeaItemMatService aeaItemMatService;

    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;

    @Autowired
    private AeaHiItemCorrectService aeaHiItemCorrectService;

    @Autowired
    private AeaProjInfoService aeaProjInfoService;

    @Autowired
    private ActStoAppinstService actStoAppinstService;

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    @Autowired
    private AeaHiItemCorrectDueIninstService aeaHiItemCorrectDueIninstService;

    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;

    @Autowired
    private AeaHiItemCorrectStateHistService aeaHiItemCorrectStateHistService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private BpmProcessService bpmProcessService;

    @Autowired
    private AeaParInMapper aeaParInMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private AeaHiItemCorrectRealIninstService aeaHiItemCorrectRealIninstService;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    @Autowired
    private WorkdayHolidayService workdayHolidayService;

    @Autowired
    private FileUtilsService fileUtilsService;

    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;

    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    /**
     * 计算事项少交或者未交的材料
     *
     * @param applyinstId
     * @param iteminstId
     * @throws Exception
     */
    @Override
    public MatCorrectInfoDto getLackMatsByApplyinstIdAndIteminstId(String applyinstId, String iteminstId) throws Exception {

        if (StringUtils.isBlank(applyinstId)) Assert.notNull(iteminstId, "applyinstId不能为空");
        if (StringUtils.isBlank(iteminstId)) Assert.notNull(iteminstId, "iteminstId不能为空");
        MatCorrectInfoDto matCorrectInfoDto = new MatCorrectInfoDto();
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (aeaHiApplyinst != null) {

            matCorrectInfoDto.setApplyinstCode(aeaHiApplyinst.getApplyinstCode());
            //获取材料实例列表
            List<AeaHiItemMatinst> matinsts = new ArrayList();
            //材料定义列表
            List<AeaItemMat> mats = new ArrayList();

            AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);
            matCorrectInfoDto.setIteminstName(aeaHiIteminst.getIteminstName());
            matCorrectInfoDto.setChargeOrgName(aeaHiIteminst.getApproveOrgName());

            if ("1".equals(aeaHiApplyinst.getIsSeriesApprove())) {

                matinsts.addAll(aeaHiItemMatinstMapper.getMatinstListByIteminstIds(new String[]{iteminstId}, "1"));

                //获取材料定义列表（不包括情形材料）
                mats.addAll(aeaItemMatService.getMatListByIteminstId(iteminstId, "1", "0"));
                //获取情形实例
                List<AeaHiItemStateinst> stateinsts = aeaHiItemStateinstMapper.listAeaHiItemStateinstByApplyinstIdOrSeriesinstId(applyinstId, null);

                if (stateinsts.size() > 0) {
                    String[] stateIds = new String[stateinsts.size()];
                    for (int i = 0; i < stateinsts.size(); i++) {
                        stateIds[i] = stateinsts.get(i).getExecStateId();
                    }
                    //获取情形绑定的材料
                    mats.addAll(aeaItemMatService.getMatListByItemStateIds(stateIds));
                }

            } else {

                matinsts.addAll(aeaHiItemMatinstMapper.getMatinstListByStageIteminstId(iteminstId));

                AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstById(aeaHiIteminst.getStageinstId());

                if (aeaHiIteminst == null) throw new Exception("找不到该事项实例！");

                if (aeaHiParStageinst == null) throw new Exception("找不到该阶段实例！");

                //获取阶段下的事项材料（不包含情形材料）
                mats.addAll(aeaItemMatService.getMatListByItemVerIdAndStageId(aeaHiIteminst.getItemVerId(), aeaHiParStageinst.getStageId(), null, null));

                //获取情形实例
                List<AeaHiParStateinst> stateinsts = aeaHiParStateinstService.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, null);
                if (stateinsts.size() > 0) {
                    String[] stateIds = new String[stateinsts.size()];
                    for (int i = 0; i < stateinsts.size(); i++) {
                        stateIds[i] = stateinsts.get(i).getExecStateId();
                    }
                    //获取事项情形绑定的材料
                    mats.addAll(aeaItemMatService.getMatListByStageStateIdsAndItemVerId(stateIds, aeaHiIteminst.getItemVerId()));
                }
            }

            Map<String, List> map = this.getCorrectMatsAndSubmittedMats(mats, matinsts);
            matCorrectInfoDto.setSubmittedMats(map.get("SubmittedMats"));
            matCorrectInfoDto.setMatCorrectDtos(map.get("MatCorrectDtos"));

            //目前申报实例和项目是一对一关系
            List<AeaProjInfo> projInfos = aeaProjInfoService.findApplyProj(applyinstId);
            if (projInfos.size() < 1) throw new Exception("找不到项目信息！");
            matCorrectInfoDto.setProjInfoName(projInfos.get(0).getProjName());
            matCorrectInfoDto.setLocalCode(projInfos.get(0).getLocalCode());

            String owner = null;
            if ("1".equals(aeaHiApplyinst.getApplySubject())) {
                List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(applyinstId, projInfos.get(0).getProjInfoId());
                if (aeaUnitInfos.size() < 1) throw new Exception("找不到业主单位信息！");
                owner = aeaUnitInfos.get(0).getApplicant();
            } else {
                AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
                owner = aeaLinkmanInfo.getLinkmanName();
            }
            matCorrectInfoDto.setOwner(owner);
        }

        return matCorrectInfoDto;
    }

    private Map<String, List> getCorrectMatsAndSubmittedMats(List<AeaItemMat> mats, List<AeaHiItemMatinst> matinsts) {

        //少交或者未提交的材料列表
        List<MatCorrectDto> matCorrectDtos = new ArrayList();
        //已经提交的材料列表（包括少交的材料）
        List<MatCorrectDto> submittedMats = new ArrayList();
        Map<String, List> map = new HashMap();
        Set<String> matIds = new HashSet();
        for (AeaItemMat mat : mats) {

            if ("1".equals(mat.getIsOfficialDoc()) || matIds.contains(mat.getMatId())) continue;
            MatCorrectDto matCorrectDto = new MatCorrectDto();
            matCorrectDto.setMatId(mat.getMatId());
            matCorrectDto.setMatName(mat.getMatName());
            matCorrectDto.setMatCode(mat.getMatCode());

            MatCorrectDto matCorrectDto1 = new MatCorrectDto();
            BeanUtils.copyProperties(matCorrectDto, matCorrectDto1);
            for (AeaHiItemMatinst matinst : matinsts) {
                if (mat.getMatId().equals(matinst.getMatId())) {

                    //判断必须的电子材料是否已经上传
                    if ("1".equals(mat.getAttIsRequire())) {
                        if (StringUtils.isBlank(matCorrectDto.getIsNeedAtt())) {
                            matCorrectDto.setIsNeedAtt("1");
                            matCorrectDto.setAttIsCollected("0");
                        }

                        if (matinst.getAttCount() != null && matinst.getAttCount() > 0) {
                            matCorrectDto.setAttMatinstId(matinst.getMatinstId());
                            matCorrectDto.setAttIsCollected("1");
                            matCorrectDto.setIsNeedAtt("0");
                        }
                    }

                    //判断原件材料提交的数量是否少于规定份数
                    if (mat.getDuePaperCount() > 0l && matinst.getRealPaperCount() != null) {
                        if (matinst.getRealPaperCount() != 0l && matinst.getRealPaperCount() - mat.getDuePaperCount() < 0l) {
                            matCorrectDto.setPaperCount(Math.abs(matinst.getRealPaperCount() - mat.getDuePaperCount()));
                            matCorrectDto.setPaperIsCollected("1");
                            matCorrectDto.setPaperMatinstId(matinst.getMatinstId());
                        } else if (matinst.getRealPaperCount() != 0l && matinst.getRealPaperCount() - mat.getDuePaperCount() >= 0l) {
                            matCorrectDto.setPaperCount(0l);
                            matCorrectDto.setPaperIsCollected("1");
                            matCorrectDto.setPaperMatinstId(matinst.getMatinstId());
                        }
                    }

                    //判断复印件材料提交的数量是否少于规定份数
                    if (mat.getDueCopyCount() > 0l && matinst.getRealCopyCount() != null) {
                        if (matinst.getRealCopyCount() != 0l && matinst.getRealCopyCount() - mat.getDueCopyCount() < 0l) {
                            matCorrectDto.setCopyCount(Math.abs(matinst.getRealCopyCount() - mat.getDueCopyCount()));
                            matCorrectDto.setCopyIsCollected("1");
                            matCorrectDto.setCopyMatinstId(matinst.getMatinstId());
                        } else if (matinst.getRealCopyCount() != 0l && matinst.getRealCopyCount() - mat.getDueCopyCount() >= 0l) {
                            matCorrectDto.setCopyCount(0l);
                            matCorrectDto.setCopyIsCollected("1");
                            matCorrectDto.setCopyMatinstId(matinst.getMatinstId());
                        }
                    }

                    if (matinst.getRealCopyCount() != null && matinst.getRealCopyCount() > 0) {
                        matCorrectDto1.setCopyCount(mat.getDueCopyCount());
                        matCorrectDto1.setRealCopyCount(matinst.getRealCopyCount());
                        matCorrectDto1.setCopyIsCollected("1");
                        matCorrectDto1.setCopyMatinstId(matinst.getMatinstId());
                    }

                    if (matinst.getRealPaperCount() != null && matinst.getRealPaperCount() > 0) {
                        matCorrectDto1.setPaperCount(mat.getDuePaperCount());
                        matCorrectDto1.setRealPaperCount(matinst.getRealPaperCount());
                        matCorrectDto1.setPaperIsCollected("1");
                        matCorrectDto1.setPaperMatinstId(matinst.getMatinstId());
                    }

                    if (matinst.getAttCount() != null && matinst.getAttCount() > 0) {
                        matCorrectDto1.setAttCount(matinst.getAttCount());
                        matCorrectDto1.setAttIsCollected("1");
                        matCorrectDto1.setAttMatinstId(matinst.getMatinstId());
                        matCorrectDto1.setIsNeedAtt(mat.getAttIsRequire());
                    }
                }
            }

            if (matCorrectDto.getPaperCount() == null && mat.getDuePaperCount() > 0 && StringUtils.isBlank(matCorrectDto.getPaperIsCollected())) {
                matCorrectDto.setPaperCount(mat.getDuePaperCount());
                matCorrectDto.setPaperIsCollected("0");
            }

            if (matCorrectDto.getCopyCount() == null && mat.getDueCopyCount() > 0 && StringUtils.isBlank(matCorrectDto.getCopyIsCollected())) {
                matCorrectDto.setCopyCount(mat.getDueCopyCount());
                matCorrectDto.setCopyIsCollected("0");
            }

            if ("1".equals(matCorrectDto1.getAttIsCollected()) || "1".equals(matCorrectDto1.getCopyIsCollected()) || "1".equals(matCorrectDto1.getPaperIsCollected())) {
                submittedMats.add(matCorrectDto1);
            }

            if ("0".equals(matCorrectDto.getAttIsCollected()) || "0".equals(matCorrectDto.getPaperIsCollected()) || "0".equals(matCorrectDto.getCopyIsCollected())) {
                matCorrectDtos.add(matCorrectDto);
            }

            matIds.add(mat.getMatId());
        }

        map.put("MatCorrectDtos", matCorrectDtos);
        map.put("SubmittedMats", submittedMats);
        return map;
    }

    /**
     * 创建材料补正实例
     *
     * @throws Exception
     */
    @Override
    public void createMatCorrectinst(CorrectinstDto correctinstDto) throws Exception {

        if (StringUtils.isBlank(correctinstDto.getApplyinstId())) throw new Exception("申请实例ID为空！");
        if (StringUtils.isBlank(correctinstDto.getIteminstId())) throw new Exception("事项实例ID为空！");
        if (StringUtils.isBlank(correctinstDto.getIsFlowTrigger())) throw new Exception("补正触发类型为空！");
        if ("1".equals(correctinstDto.getIsFlowTrigger()) && StringUtils.isBlank(correctinstDto.getTaskinstId()))
            throw new Exception("节点实例ID为空！");
        if (correctinstDto.getCorrectDueDays() == null || correctinstDto.getCorrectDueDays() <= 0)
            throw new Exception("补正期限不符合要求！");
        if (StringUtils.isBlank(correctinstDto.getMatCorrectDtosJson())) throw new Exception("材料补正清单为空！");

        List<AeaProjInfo> aeaProjInfos = aeaProjInfoService.findApplyProj(correctinstDto.getApplyinstId());
        if (aeaProjInfos.size() > 0)
            correctinstDto.setProjInfoId(aeaProjInfos.get(0).getProjInfoId());
        else
            throw new Exception("找不到项目信息！");

        AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(correctinstDto.getIteminstId());

        if ("6".equals(iteminst.getIteminstState()) || "7".equals(iteminst.getIteminstState()) || "9".equals(iteminst.getIteminstState()) || "10".equals(iteminst.getIteminstState()))
            throw new Exception("该事项当前状态处于补件或特别程序中！");

        correctinstDto.setCorrectState("6");//表示待补正

        List<String> matinstIds = new ArrayList();

        //将该事项的情形实例保存在内存中
        List<AeaHiItemStateinst> stateinsts = new ArrayList();
        List<AeaHiParStateinst> parStateinsts = new ArrayList();

        List<AeaHiItemCorrectDueIninst> correctDueIninsts = new ArrayList();
        String correctId = UUID.randomUUID().toString();
        JSONArray jsonArray = JSONArray.parseArray(correctinstDto.getMatCorrectDtosJson());
        List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
        if (matCorrectDtos.size() < 1) throw new Exception("材料补正清单为空");

        for (MatCorrectDto matCorrectDto : matCorrectDtos) {
            if (matCorrectDto.getCopyCount() != null && matCorrectDto.getCopyCount() > 0) {
                AeaHiItemCorrectDueIninst correctDueIninst = new AeaHiItemCorrectDueIninst();
                correctDueIninst.setDueIninstId(UUID.randomUUID().toString());
                correctDueIninst.setCorrectId(correctId);
                correctDueIninst.setCreater(SecurityContext.getCurrentUserName());
                correctDueIninst.setCreateTime(new Date());
                correctDueIninst.setCopyCount(matCorrectDto.getCopyCount());
                correctDueIninst.setIsNewInoutinst("1");
                correctDueIninst.setCorrectOpinion(matCorrectDto.getCopyDueIninstOpinion());

                //该复印件材料已经实例化
                if (StringUtils.isNotBlank(matCorrectDto.getCopyIsCollected()) && "1".equals(matCorrectDto.getCopyIsCollected())) {

                    if (StringUtils.isBlank(matCorrectDto.getCopyMatinstId())) throw new Exception("找不到材料实例Id！");
                    AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
                    aeaHiItemInoutinst.setIteminstId(correctinstDto.getIteminstId());
                    aeaHiItemInoutinst.setIsCollected("1");
                    aeaHiItemInoutinst.setMatinstId(matCorrectDto.getCopyMatinstId());
                    aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                    List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);
                    if (itemInoutinsts.size() < 1) throw new Exception("找不到材料输入输出实例信息！");

                    correctDueIninst.setInoutinstId(itemInoutinsts.get(0).getInoutinstId());
                    correctDueIninst.setIsNewInoutinst("0");
                    matinstIds.add(matCorrectDto.getCopyMatinstId());
                } else {
                    String matinstId = UUID.randomUUID().toString();
                    matinstIds.add(matinstId);
                    String inoutinstId = UUID.randomUUID().toString();
                    correctDueIninst.setInoutinstId(inoutinstId);
                    //创建材料实例和输入输出实例
                    this.creatMatinstAndInoutinst(matCorrectDto, iteminst, matinstId, inoutinstId, correctinstDto.getApplyinstId(), correctinstDto.getProjInfoId(), stateinsts, parStateinsts);
                }

                correctDueIninsts.add(correctDueIninst);
            }

            if (matCorrectDto.getPaperCount() != null && matCorrectDto.getPaperCount() > 0) {
                AeaHiItemCorrectDueIninst correctDueIninst = new AeaHiItemCorrectDueIninst();
                correctDueIninst.setDueIninstId(UUID.randomUUID().toString());
                correctDueIninst.setCorrectId(correctId);
                correctDueIninst.setCreater(SecurityContext.getCurrentUserName());
                correctDueIninst.setCreateTime(new Date());
                correctDueIninst.setPaperCount(matCorrectDto.getPaperCount());
                correctDueIninst.setIsNewInoutinst("1");
                correctDueIninst.setCorrectOpinion(matCorrectDto.getPaperDueIninstOpinion());

                //该纸质原件材料已经实例化
                if (StringUtils.isNotBlank(matCorrectDto.getPaperIsCollected()) && "1".equals(matCorrectDto.getPaperIsCollected())) {

                    if (StringUtils.isBlank(matCorrectDto.getPaperMatinstId())) throw new Exception("找不到材料实例Id！");
                    AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
                    aeaHiItemInoutinst.setIteminstId(correctinstDto.getIteminstId());
                    aeaHiItemInoutinst.setIsCollected("1");
                    aeaHiItemInoutinst.setMatinstId(matCorrectDto.getPaperMatinstId());
                    aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                    List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);
                    if (itemInoutinsts.size() < 1) throw new Exception("找不到材料输入输出实例信息！");
                    correctDueIninst.setInoutinstId(itemInoutinsts.get(0).getInoutinstId());
                    correctDueIninst.setIsNewInoutinst("0");
                    matinstIds.add(matCorrectDto.getPaperMatinstId());
                } else {

                    String matinstId = UUID.randomUUID().toString();
                    String inoutinstId = UUID.randomUUID().toString();
                    matinstIds.add(matinstId);
                    correctDueIninst.setInoutinstId(inoutinstId);
                    //创建材料实例和输入输出实例
                    this.creatMatinstAndInoutinst(matCorrectDto, iteminst, matinstId, inoutinstId, correctinstDto.getApplyinstId(), correctinstDto.getProjInfoId(), stateinsts, parStateinsts);
                }

                correctDueIninsts.add(correctDueIninst);
            }

            if (StringUtils.isNotBlank(matCorrectDto.getIsNeedAtt()) && "1".equals(matCorrectDto.getIsNeedAtt())) {
                AeaHiItemCorrectDueIninst correctDueIninst = new AeaHiItemCorrectDueIninst();
                correctDueIninst.setDueIninstId(UUID.randomUUID().toString());
                correctDueIninst.setCorrectId(correctId);
                correctDueIninst.setCreater(SecurityContext.getCurrentUserName());
                correctDueIninst.setCreateTime(new Date());
                correctDueIninst.setIsNeedAtt("1");
                correctDueIninst.setIsNewInoutinst("1");
                correctDueIninst.setCorrectOpinion(matCorrectDto.getAttDueIninstOpinion());

                //该电子件材料已经实例化
                if (StringUtils.isNotBlank(matCorrectDto.getAttIsCollected()) && "1".equals(matCorrectDto.getAttIsCollected())) {

                    if (StringUtils.isBlank(matCorrectDto.getAttMatinstId())) throw new Exception("找不到材料实例Id！");
                    AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
                    aeaHiItemInoutinst.setIteminstId(correctinstDto.getIteminstId());
                    aeaHiItemInoutinst.setIsCollected("1");
                    aeaHiItemInoutinst.setMatinstId(matCorrectDto.getAttMatinstId());
                    List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);
                    if (itemInoutinsts.size() < 1) throw new Exception("找不到材料输入输出实例信息！");

                    correctDueIninst.setInoutinstId(itemInoutinsts.get(0).getInoutinstId());
                    correctDueIninst.setIsNewInoutinst("0");
                    matinstIds.add(matCorrectDto.getAttMatinstId());

                } else {
                    String matinstId = UUID.randomUUID().toString();
                    String inoutinstId = UUID.randomUUID().toString();
                    correctDueIninst.setInoutinstId(inoutinstId);
                    matinstIds.add(matinstId);
                    //创建材料实例和输入输出实例
                    this.creatMatinstAndInoutinst(matCorrectDto, iteminst, matinstId, inoutinstId, correctinstDto.getApplyinstId(), correctinstDto.getProjInfoId(), stateinsts, parStateinsts);
                }

                correctDueIninsts.add(correctDueIninst);
            }
        }

        //实例化材料补正实例
        AeaHiItemCorrect aeaHiItemCorrect = new AeaHiItemCorrect();
        aeaHiItemCorrect.setCorrectId(correctId);
        BeanUtils.copyProperties(correctinstDto, aeaHiItemCorrect);

        if ("1".equals(correctinstDto.getIsFlowTrigger())) {
            ActStoAppinst appinst = new ActStoAppinst();
            appinst.setMasterRecordId(correctinstDto.getApplyinstId());
            List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(appinst);
            if (appinsts.size() < 1) throw new Exception("找不到流程模板实例！");
            aeaHiItemCorrect.setAppinstId(appinsts.get(0).getAppinstId());
        }
        if (iteminst == null) throw new Exception("找不到事项实例！");
        aeaHiItemCorrect.setChargeOrgId(iteminst.getApproveOrgId());
        aeaHiItemCorrect.setChargeOrgName(iteminst.getApproveOrgName());

        Date nextDay = workdayHolidayService.nextDay(new Date());
        Date correctDueDay = workdayHolidayService.calWorkdayFrom(nextDay, aeaHiItemCorrect.getCorrectDueDays().intValue(), SecurityContext.getCurrentOrgId());
        aeaHiItemCorrect.setCorrectDueTime(correctDueDay);

        aeaHiItemCorrect.setMatinstIds(matinstIds.toArray().toString());
        aeaHiItemCorrectService.saveAeaHiItemCorrect(aeaHiItemCorrect);

        //实例化补正清单的材料份数
        aeaHiItemCorrectDueIninstService.batchSaveAeaHiItemCorrectDueIninst(correctDueIninsts);

        //更新事项状态和新增历史记录
        if ("1".equals(correctinstDto.getIsFlowTrigger())) {
            aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), correctinstDto.getTaskinstId(), aeaHiItemCorrect.getAppinstId(), ItemStatus.CORRECT_MATERIAL_START.getValue(),null);

            HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(correctinstDto.getTaskinstId()).singleResult();
            if (taskInstance == null) throw new Exception("找不到节点信息！");

            HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId()).singleResult();

            //当前流程未结束
            if (processInstance.getEndTime() != null) {
                //挂起当前流程
                bpmProcessService.suspendProcessInstanceByIdAndTaskinstId(taskInstance.getProcessInstanceId(), correctinstDto.getTaskinstId());
            }
        } else {
            aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(iteminst.getIteminstId(), correctinstDto.getCorrectMemo(), "材料补正", null, ItemStatus.CORRECT_MATERIAL_START.getValue(),null);
        }

        //获取该事项实例的所有补正历史状态根据时间降序
        AeaLogItemStateHist aeaLogItemStateHist = new AeaLogItemStateHist();
        aeaLogItemStateHist.setIteminstId(iteminst.getIteminstId());
        aeaLogItemStateHist.setNewState(ItemStatus.CORRECT_MATERIAL_START.getValue());
        aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaLogItemStateHist> aeaLogItemStateHists = aeaLogItemStateHistService.findAeaLogItemStateHist(aeaLogItemStateHist);

        //创建材料补正和事项历史记录关联表
        AeaHiItemCorrectStateHist aeaHiItemCorrectStateHist = new AeaHiItemCorrectStateHist();
        aeaHiItemCorrectStateHist.setCorrectStateHistId(UUID.randomUUID().toString());
        aeaHiItemCorrectStateHist.setCorrectId(correctId);
        aeaHiItemCorrectStateHist.setCorrectStateHistId(aeaLogItemStateHists.get(0).getStateHistId());
        aeaHiItemCorrectStateHistService.saveAeaHiItemCorrectStateHist(aeaHiItemCorrectStateHist);

    }

    //创建材料实例和输入输出实例
    private void creatMatinstAndInoutinst(MatCorrectDto matCorrectDto, AeaHiIteminst iteminst, String matinstId, String inoutinstId, String applyinstId, String projInfoId, List<AeaHiItemStateinst> stateinsts, List<AeaHiParStateinst> parStateinsts) throws Exception {

        List<AeaUnitInfo> unitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(applyinstId, projInfoId);
        if (unitInfos.size() < 1) throw new Exception("找不到业务单位信息！");
        String ownnerUnitInfoId = unitInfos.get(0).getUnitInfoId();

        //创建材料实例
        AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
        aeaHiItemMatinst.setMatinstId(matinstId);
        aeaHiItemMatinst.setMatId(matCorrectDto.getMatId());
        aeaHiItemMatinst.setMatinstName(matCorrectDto.getMatName());
        aeaHiItemMatinst.setMatinstCode(matCorrectDto.getMatCode());
        aeaHiItemMatinst.setProjInfoId(projInfoId);
        aeaHiItemMatinst.setUnitInfoId(ownnerUnitInfoId);
        aeaHiItemMatinst.setCreateTime(new Date());
        aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
        aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);

        //创建输入输出实例
        AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
        aeaHiItemInoutinst.setInoutinstId(inoutinstId);

        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if ("1".equals(aeaHiApplyinst.getIsSeriesApprove())) {
            String inoutId = null;
            AeaItemInout aeaItemInout = new AeaItemInout();
            aeaItemInout.setIsInput("1");
            aeaItemInout.setIsDeleted("0");
            aeaItemInout.setMatId(matCorrectDto.getMatId());
            aeaItemInout.setItemVerId(iteminst.getItemVerId());
            aeaItemInout.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaItemInout> aeaItemInouts = aeaItemInoutMapper.listAeaItemInout(aeaItemInout);

            for (AeaItemInout inout : aeaItemInouts) {
                if ("0".equals(inout.getIsStateIn())) {
                    inoutId = inout.getInoutId();
                    break;
                }
            }

            if (StringUtils.isBlank(inoutId)) {
                if (stateinsts.size() < 1)
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
            if (StringUtils.isBlank(inoutId)) throw new Exception("找不到材料的输入输出关联！");

            aeaHiItemInoutinst.setItemInoutId(inoutId);
            aeaHiItemInoutinst.setIteminstId(iteminst.getIteminstId());
            aeaHiItemInoutinst.setIsCollected("0");
            aeaHiItemInoutinst.setMatinstId(matinstId);
            aeaHiItemInoutinst.setCreateTime(new Date());
            aeaHiItemInoutinst.setCreater(SecurityContext.getCurrentUserName());
            aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(aeaHiItemInoutinst);

        } else {

            //获取情形实例
            if (parStateinsts.size() < 1)
                parStateinsts.addAll(aeaHiParStateinstService.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, null));

            aeaHiItemInoutinst.setIsCollected("0");
            aeaHiItemInoutinst.setMatinstId(matinstId);
            aeaHiItemInoutinst.setCreateTime(new Date());
            aeaHiItemInoutinst.setCreater(SecurityContext.getCurrentUserName());
            aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiItemInoutinst.setIsParIn("1");

            AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstById(iteminst.getStageinstId());
            AeaParIn parIn = new AeaParIn();
            parIn.setMatId(matCorrectDto.getMatId());
            parIn.setStageId(aeaHiParStageinst.getStageId());
            parIn.setIsDeleted("0");
            parIn.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaParIn> parIns = aeaParInMapper.listAeaParIn(parIn);
            if (parIns.size() < 1) throw new Exception("找不到材料的输入关联！");

            List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByStageinstId(aeaHiParStageinst.getStageinstId());
            //凡是需要该材料的事项都创建输入输出实例
            for (AeaHiIteminst aeaHiIteminst : iteminsts) {
                for (AeaParIn aeaParIn : parIns) {
                    //判断该阶段输入是否属于该阶段下的事项
                    Integer count = aeaParInMapper.isCheckParInBelong2Item(aeaHiParStageinst.getStageId(), aeaHiIteminst.getItemVerId(), aeaParIn.getInId());
                    if (count != null && count > 0) {
                        if ("1".equals(aeaParIn.getIsStateIn())) {
                            for (AeaHiParStateinst stateinst : parStateinsts) {
                                if (stateinst.getExecStateId().equals(aeaParIn.getParStateId())) {
                                    aeaHiItemInoutinst.setParInId(aeaParIn.getInId());
                                    aeaHiItemInoutinst.setIteminstId(aeaHiIteminst.getIteminstId());
                                    aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(aeaHiItemInoutinst);
                                    break;
                                }
                            }
                        } else {
                            aeaHiItemInoutinst.setParInId(aeaParIn.getInId());
                            aeaHiItemInoutinst.setIteminstId(aeaHiIteminst.getIteminstId());
                            aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(aeaHiItemInoutinst);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 材料补正信息
     *
     * @param correctId
     * @return
     * @throws Exception
     */
    @Override
    public AeaHiItemCorrect getMatCorrectInfo(String correctId) throws Exception {

        if (StringUtils.isBlank(correctId)) throw new Exception("材料补正实例ID为空！");

        AeaHiItemCorrect aeaHiItemCorrect = aeaHiItemCorrectService.getAeaHiItemCorrectById(correctId);
        if (aeaHiItemCorrect == null) throw new Exception("找不到材料补正实例信息！");
        if (StringUtils.isNotBlank(aeaHiItemCorrect.getIteminstId())) {
            //BscDicRegion bscDicRegion = bscDicRegionMapper.getBscDicRegionById(aeaHiItemCorrect.getRegionalism());
            List<AeaItemBasic> list = aeaItemBasicMapper.listAeaItemBasicByIteminstId(aeaHiItemCorrect.getIteminstId());
            if (list.size() > 0) aeaHiItemCorrect.setRegionName(list.get(0).getRegionName());
        }
        if ("1".equals(aeaHiItemCorrect.getApplySubject())) {
            List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(aeaHiItemCorrect.getApplyinstId(), aeaHiItemCorrect.getProjInfoId());
            if (aeaUnitInfos.size() < 1) throw new Exception("找不到业务单位信息！");
            aeaHiItemCorrect.setOwner(aeaUnitInfos.get(0).getApplicant());
        } else {
            if (StringUtils.isBlank(aeaHiItemCorrect.getLinkmanInfoId())) throw new Exception("找不到经办人ID！");
            AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(aeaHiItemCorrect.getLinkmanInfoId());
            if (aeaLinkmanInfo == null) throw new Exception("找不到经办人信息！");
            aeaHiItemCorrect.setOwner(aeaLinkmanInfo.getLinkmanName());
        }

        //回填窗口补件负责人信息
        if (StringUtils.isBlank(aeaHiItemCorrect.getWindowUserId())) {
            aeaHiItemCorrect.setWindowUserId(SecurityContext.getCurrentUserId());
            aeaHiItemCorrect.setWindowUserName(SecurityContext.getCurrentUser().getUserName());
        }

        //回填窗口补件经办人信息
        if (StringUtils.isBlank(aeaHiItemCorrect.getOpsUserId())) {
            aeaHiItemCorrect.setOpsUserId(SecurityContext.getCurrentUserId());
            aeaHiItemCorrect.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
            aeaHiItemCorrect.setOpsTime(new Date());
        }
        //需补正的材料清单
        List<AeaHiItemCorrectDueIninst> aeaHiItemCorrectDueIninsts = aeaHiItemCorrectDueIninstService.getCorrectDueIninstByCorrectId(correctId);
        if (aeaHiItemCorrectDueIninsts.size() < 1) throw new Exception("没有需补正的材料清单");

        Map<String, MatCorrectDto> map = new HashMap();
        for (AeaHiItemCorrectDueIninst correctDueIninst : aeaHiItemCorrectDueIninsts) {
            MatCorrectDto matCorrectDto = map.get(correctDueIninst.getMatId()) != null ? map.get(correctDueIninst.getMatId()) : new MatCorrectDto();

            if (StringUtils.isBlank(matCorrectDto.getMatinstName())) {
                matCorrectDto.setMatinstName(correctDueIninst.getMatinstName());
            }

            if (correctDueIninst.getCopyCount() != null && correctDueIninst.getCopyCount() > 0) {
                matCorrectDto.setCopyMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setCopyInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setCopyCount(correctDueIninst.getCopyCount());
                matCorrectDto.setCopyDueIninstId(correctDueIninst.getDueIninstId());
            }

            if (correctDueIninst.getPaperCount() != null && correctDueIninst.getPaperCount() > 0) {
                matCorrectDto.setPaperMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setPaperInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setPaperCount(correctDueIninst.getPaperCount());
                matCorrectDto.setPaperDueIninstId(correctDueIninst.getDueIninstId());
            }

            if (StringUtils.isNotBlank(correctDueIninst.getIsNeedAtt()) && "1".equals(correctDueIninst.getIsNeedAtt())) {
                matCorrectDto.setIsNeedAtt("1");
                matCorrectDto.setAttInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setAttMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setAttDueIninstId(correctDueIninst.getDueIninstId());
            }

            map.put(correctDueIninst.getMatId(), matCorrectDto);
        }
        //已补正的材料清单
        List<AeaHiItemCorrectRealIninst> aeaHiItemCorrectRealIninsts = aeaHiItemCorrectRealIninstService.getCorrectRealIninstByCorrectId(correctId);
        for (AeaHiItemCorrectRealIninst correctRealIninst : aeaHiItemCorrectRealIninsts) {

            if (map.get(correctRealIninst.getMatId()) == null) continue;
            MatCorrectDto matCorrectDto = map.get(correctRealIninst.getMatId());

            if (correctRealIninst.getAttCount() != null && correctRealIninst.getAttCount() > 0) {
                matCorrectDto.setAttCount(correctRealIninst.getAttCount());
                matCorrectDto.setAttRealIninstId(correctRealIninst.getRealIninstId());
            }

            if (correctRealIninst.getRealPaperCount() != null && correctRealIninst.getRealPaperCount() > 0) {
                matCorrectDto.setRealPaperCount(correctRealIninst.getRealPaperCount());
                matCorrectDto.setPaperRealIninstId(correctRealIninst.getRealIninstId());
            }

            if (correctRealIninst.getRealCopyCount() != null && correctRealIninst.getRealCopyCount() > 0) {
                matCorrectDto.setRealCopyCount(correctRealIninst.getRealCopyCount());
                matCorrectDto.setCopyRealIninstId(correctRealIninst.getRealIninstId());
            }

            map.put(correctRealIninst.getMatId(), matCorrectDto);
        }

        List<MatCorrectDto> matCorrectDtos = new ArrayList();
        matCorrectDtos.addAll(map.values());

        for (MatCorrectDto matCorrectDto : matCorrectDtos) {

            if ("1".equals(matCorrectDto.getIsNeedAtt()) && StringUtils.isBlank(matCorrectDto.getAttRealIninstId())) {
                AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
                String realInstId = UUID.randomUUID().toString();
                aeaHiItemCorrectRealIninst.setRealIninstId(realInstId);
                aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getAttDueIninstId());
                aeaHiItemCorrectRealIninst.setCorrectId(correctId);
                aeaHiItemCorrectRealIninst.setIsPass("0");
                aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getAttInoutinstId());
                aeaHiItemCorrectRealIninst.setAttCount(matCorrectDto.getAttCount());
                aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);

                matCorrectDto.setAttRealIninstId(realInstId);
                matCorrectDto.setAttCount(0l);
            }
        }

        aeaHiItemCorrect.setMatCorrectDtos(matCorrectDtos);

        return aeaHiItemCorrect;
    }

    /**
     * 保存补正的材料
     *
     * @param matCorrectinstDto
     * @throws Exception
     */
    @Override
    public void saveMatCorrectInfo(MatCorrectinstDto matCorrectinstDto) throws Exception {

        if (StringUtils.isBlank(matCorrectinstDto.getCorrectId())) throw new Exception("材料补正实例ID为空！");
        if (StringUtils.isBlank(matCorrectinstDto.getCorrectState())) throw new Exception("材料补正实例状态为空！");

        AeaHiItemCorrect aeaHiItemCorrect = aeaHiItemCorrectService.getAeaHiItemCorrectById(matCorrectinstDto.getCorrectId());
        if (aeaHiItemCorrect == null) throw new Exception("找不到材料补正实例信息！");

        aeaHiItemCorrect.setCorrectDesc(matCorrectinstDto.getCorrectDesc());
        String state = matCorrectinstDto.getCorrectState();

        if ("6".equals(state)) {    //待补正

            JSONArray jsonArray = JSONArray.parseArray(matCorrectinstDto.getMatCorrectDtosJson());
            List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
            for (MatCorrectDto matCorrectDto : matCorrectDtos) {

                if (matCorrectDto.getRealPaperCount() != null && matCorrectDto.getRealPaperCount() > 0) {

                    if (StringUtils.isNotBlank(matCorrectDto.getPaperRealIninstId())) {
                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {
                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
                        aeaHiItemCorrectRealIninst.setRealIninstId(UUID.randomUUID().toString());
                        aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getPaperDueIninstId());
                        aeaHiItemCorrectRealIninst.setCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiItemCorrectRealIninst.setIsPass("0");
                        aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getPaperInoutinstId());
                        aeaHiItemCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
                        aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    }
                }

                if (matCorrectDto.getRealCopyCount() != null && matCorrectDto.getRealCopyCount() > 0) {

                    if (StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {
                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
                        aeaHiItemCorrectRealIninst.setRealIninstId(UUID.randomUUID().toString());
                        aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getCopyDueIninstId());
                        aeaHiItemCorrectRealIninst.setCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiItemCorrectRealIninst.setIsPass("0");
                        aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getCopyInoutinstId());
                        aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
                        aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    }
                }
            }

        } else if ("7".equals(state)) {     //已补正
            aeaHiItemCorrect.setCorrectUserName(SecurityContext.getCurrentUser().getUserName());
            aeaHiItemCorrect.setCorrectEndTime(new Date());

            JSONArray jsonArray = JSONArray.parseArray(matCorrectinstDto.getMatCorrectDtosJson());
            List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
            for (MatCorrectDto matCorrectDto : matCorrectDtos) {

                if (matCorrectDto.getPaperCount() != null && matCorrectDto.getPaperCount() > 0) {
                    if (matCorrectDto.getRealPaperCount() == null || matCorrectDto.getRealPaperCount() < matCorrectDto.getPaperCount())
                        throw new Exception("缺少纸质材料（" + matCorrectDto.getMatinstName() + "）");

                    if (StringUtils.isNotBlank(matCorrectDto.getPaperRealIninstId())) {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
                        aeaHiItemCorrectRealIninst.setRealIninstId(UUID.randomUUID().toString());
                        aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getPaperDueIninstId());
                        aeaHiItemCorrectRealIninst.setCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getPaperInoutinstId());
                        aeaHiItemCorrectRealIninst.setIsPass("0");
                        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
                        aeaHiItemCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                        aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    }
                }

                if (matCorrectDto.getCopyCount() != null && matCorrectDto.getCopyCount() > 0) {
                    if (matCorrectDto.getRealCopyCount() == null || matCorrectDto.getRealCopyCount() < matCorrectDto.getCopyCount())
                        throw new Exception("缺少复印件（" + matCorrectDto.getMatinstName() + "）");

                    if (StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
                        aeaHiItemCorrectRealIninst.setRealIninstId(UUID.randomUUID().toString());
                        aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getCopyDueIninstId());
                        aeaHiItemCorrectRealIninst.setCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getCopyInoutinstId());
                        aeaHiItemCorrectRealIninst.setIsPass("0");
                        aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
                        aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    }
                }

                if (StringUtils.isNotBlank(matCorrectDto.getIsNeedAtt()) && "1".equals(matCorrectDto.getIsNeedAtt())) {
                    if (matCorrectDto.getAttCount() == null || matCorrectDto.getAttCount() < 1)
                        throw new Exception("缺少电子件（" + matCorrectDto.getMatinstName() + "）");
                }
            }

            //检查材料是否已全部补正
            if (!this.isCheckCorrect(matCorrectinstDto.getCorrectId())) throw new Exception("还有材料未补正！");

        } else {    //不予受理
            aeaHiItemCorrect.setCorrectUserName(SecurityContext.getCurrentUser().getUserName());
            aeaHiItemCorrect.setCorrectEndTime(new Date());
        }

        if (StringUtils.isBlank(aeaHiItemCorrect.getOpsUserId()) || !SecurityContext.getCurrentUserId().equals(StringUtils.isBlank(aeaHiItemCorrect.getOpsUserId()))) {
            aeaHiItemCorrect.setOpsUserId(SecurityContext.getCurrentUserId());
            aeaHiItemCorrect.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
        }
        aeaHiItemCorrect.setOpsTime(new Date());
        aeaHiItemCorrect.setCorrectState(state);
        aeaHiItemCorrectService.updateAeaHiItemCorrect(aeaHiItemCorrect);

        if ("7".equals(state) || "5".equals(state)) {
            AeaLogItemStateHist aeaLogItemStateHist = aeaLogItemStateHistService.getAeaLogItemStateHistByCorrectId(aeaHiItemCorrect.getCorrectId());
            //更新事项状态和新增历史记录
            if ("1".equals(aeaLogItemStateHist.getIsFlowTrigger())) {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getTaskinstId(), aeaHiItemCorrect.getAppinstId(), ItemStatus.CORRECT_MATERIAL_END.getValue(),null);
            } else {
                String option = "7".equals(state) ? "材料已补正" : "不予受理";
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), aeaHiItemCorrect.getCorrectDesc(), option, null, ItemStatus.CORRECT_MATERIAL_END.getValue(),null);
            }
        }
    }

    //判断材料补正是否已收齐
    private boolean isCheckCorrect(String correctId) throws Exception {

        if (StringUtils.isBlank(correctId)) throw new Exception("材料补正实例ID为空！");
        //需要补正的材料清单
        List<AeaHiItemCorrectDueIninst> aeaHiItemCorrectDueIninsts = aeaHiItemCorrectDueIninstService.getCorrectDueIninstByCorrectId(correctId);
        if (aeaHiItemCorrectDueIninsts.size() < 1) return true;

        //已补正的材料清单
        List<AeaHiItemCorrectRealIninst> aeaHiItemCorrectRealIninsts = aeaHiItemCorrectRealIninstService.getCorrectRealIninstByCorrectId(correctId);
        if (aeaHiItemCorrectRealIninsts.size() < 1) return false;

        List correctedList = new ArrayList();
        for (AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst : aeaHiItemCorrectRealIninsts) {

            for (AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst : aeaHiItemCorrectDueIninsts) {

                if (aeaHiItemCorrectDueIninst.getDueIninstId().equals(aeaHiItemCorrectRealIninst.getDueIninstId())) {

                    if (aeaHiItemCorrectDueIninst.getPaperCount() != null && aeaHiItemCorrectDueIninst.getPaperCount() > 0 && aeaHiItemCorrectRealIninst.getRealPaperCount() != null && aeaHiItemCorrectRealIninst.getRealPaperCount() >= aeaHiItemCorrectDueIninst.getPaperCount()) {
                        correctedList.add(aeaHiItemCorrectRealIninst.getRealIninstId());
                        break;
                    }

                    if (aeaHiItemCorrectDueIninst.getCopyCount() != null && aeaHiItemCorrectDueIninst.getCopyCount() > 0 && aeaHiItemCorrectRealIninst.getRealCopyCount() != null && aeaHiItemCorrectRealIninst.getRealCopyCount() >= aeaHiItemCorrectDueIninst.getCopyCount()) {
                        correctedList.add(aeaHiItemCorrectRealIninst.getRealIninstId());
                        break;
                    }

                    if (StringUtils.isNotBlank(aeaHiItemCorrectDueIninst.getIsNeedAtt()) && "1".equals(aeaHiItemCorrectDueIninst.getIsNeedAtt()) && aeaHiItemCorrectRealIninst.getAttCount() != null && aeaHiItemCorrectRealIninst.getAttCount() > 0) {
                        correctedList.add(aeaHiItemCorrectRealIninst.getRealIninstId());
                        break;
                    }
                }
            }
        }

        if (correctedList.size() == aeaHiItemCorrectDueIninsts.size())
            return true;
        else
            return false;
    }

    /**
     * 材料补正待确认列表
     *
     * @param correctId
     * @throws Exception
     */
    @Override
    public AeaHiItemCorrect getItemCorrectRealIninst(String correctId) throws Exception {
        if (StringUtils.isBlank(correctId)) throw new Exception("材料补正实例ID为空！");

        AeaHiItemCorrect aeaHiItemCorrect = aeaHiItemCorrectService.getAeaHiItemCorrectById(correctId);
        if (aeaHiItemCorrect == null) throw new Exception("找不到材料补正实例信息！");

        List<AeaHiItemCorrectDueIninst> aeaHiItemCorrectDueIninsts = aeaHiItemCorrectDueIninstService.getCorrectDueIninstByCorrectId(correctId);
        if (aeaHiItemCorrectDueIninsts.size() < 1) throw new Exception("没有需补正的材料清单");

        this.getOwner(aeaHiItemCorrect); //获取业主名称

        Map<String, MatCorrectDto> map = new HashMap();
        for (AeaHiItemCorrectDueIninst correctDueIninst : aeaHiItemCorrectDueIninsts) {
            MatCorrectDto matCorrectDto = map.get(correctDueIninst.getMatId()) != null ? map.get(correctDueIninst.getMatId()) : new MatCorrectDto();

            if (StringUtils.isBlank(matCorrectDto.getMatinstName())) {
                matCorrectDto.setMatinstName(correctDueIninst.getMatinstName());
                matCorrectDto.setMatId(correctDueIninst.getMatId());
            }

            if (correctDueIninst.getCopyCount() != null && correctDueIninst.getCopyCount() > 0) {
                matCorrectDto.setCopyMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setCopyInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setCopyCount(correctDueIninst.getCopyCount());
            }

            if (correctDueIninst.getPaperCount() != null && correctDueIninst.getPaperCount() > 0) {
                matCorrectDto.setPaperMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setPaperInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setPaperCount(correctDueIninst.getPaperCount());
            }

            if (StringUtils.isNotBlank(correctDueIninst.getIsNeedAtt()) && "1".equals(correctDueIninst.getIsNeedAtt())) {
                matCorrectDto.setIsNeedAtt("1");
                matCorrectDto.setAttInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setAttMatinstId(correctDueIninst.getMatinstId());
            }

            map.put(correctDueIninst.getMatId(), matCorrectDto);
        }

        List<AeaHiItemCorrectRealIninst> aeaHiItemCorrectRealIninsts = aeaHiItemCorrectRealIninstService.getCorrectRealIninstByCorrectId(correctId);
        if (aeaHiItemCorrectRealIninsts.size() < 1) throw new Exception("找不到补正的材料列表！");

        for (AeaHiItemCorrectRealIninst correctRealIninst : aeaHiItemCorrectRealIninsts) {
            if (map.get(correctRealIninst.getMatId()) == null) continue;
            MatCorrectDto matCorrectDto = map.get(correctRealIninst.getMatId());

            if (correctRealIninst.getAttCount() != null && correctRealIninst.getAttCount() > 0) {
                matCorrectDto.setAttCount(correctRealIninst.getAttCount());
                matCorrectDto.setAttRealIninstId(correctRealIninst.getRealIninstId());
                matCorrectDto.setAttIsPass(correctRealIninst.getIsPass());
            }

            if (correctRealIninst.getRealPaperCount() != null && correctRealIninst.getRealPaperCount() > 0) {
                matCorrectDto.setRealPaperCount(correctRealIninst.getRealPaperCount());
                matCorrectDto.setPaperRealIninstId(correctRealIninst.getRealIninstId());
                matCorrectDto.setPaperIsPass(correctRealIninst.getIsPass());
            }

            if (correctRealIninst.getRealCopyCount() != null && correctRealIninst.getRealCopyCount() > 0) {
                matCorrectDto.setRealCopyCount(correctRealIninst.getRealCopyCount());
                matCorrectDto.setCopyRealIninstId(correctRealIninst.getRealIninstId());
                matCorrectDto.setCopyIsPass(correctRealIninst.getIsPass());
            }

            map.put(correctRealIninst.getMatId(), matCorrectDto);
        }

        List<MatCorrectDto> matCorrectDtos = new ArrayList();
        matCorrectDtos.addAll(map.values());
        aeaHiItemCorrect.setMatCorrectDtos(matCorrectDtos);

        return aeaHiItemCorrect;
    }

    /**
     * 材料补正确认
     *
     * @param correctId
     * @param matCorrectDtosJson
     * @throws Exception
     */
    @Override
    public void matCorrectConfirm(String correctId, String correctState, String correctMemo, String matCorrectDtosJson) throws Exception {

        if (StringUtils.isBlank(correctId)) throw new Exception("材料补正实例id为空！");

        JSONArray jsonArray = JSONArray.parseArray(matCorrectDtosJson);
        List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
        if (matCorrectDtos.size() < 1) throw new Exception("材料补正列表为空！");

        AeaHiItemCorrect aeaHiItemCorrect = aeaHiItemCorrectService.getAeaHiItemCorrectById(correctId);
        if (aeaHiItemCorrect == null) throw new Exception("找不到材料补正实例信息！");

        List<AeaHiItemCorrectDueIninst> correctDueIninsts = new ArrayList();
        List<String> matinstIds = new ArrayList();
        if (!"5".equals(correctState)) {
            for (MatCorrectDto matCorrectDto : matCorrectDtos) {

                if ("1".equals(matCorrectDto.getAttIsPass())) {
                    AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getAttRealIninstId());
                    aeaHiItemCorrectRealIninst.setIsPass("1");
                    aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);

                    AeaHiItemInoutinst aeaHiItemInoutinst = aeaHiItemInoutinstMapper.getAeaHiItemInoutinstById(matCorrectDto.getAttInoutinstId());
                    aeaHiItemInoutinst.setIsCollected("1");
                    aeaHiItemInoutinst.setModifier(SecurityContext.getCurrentUserName());
                    aeaHiItemInoutinst.setModifyTime(new Date());
                    aeaHiItemInoutinstMapper.updateAeaHiItemInoutinst(aeaHiItemInoutinst);

                    AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matCorrectDto.getAttMatinstId());
                    matinst.setAttCount(matinst.getAttCount() == null ? matCorrectDto.getAttCount() : matinst.getAttCount() + matCorrectDto.getAttCount());
                    matinst.setModifier(SecurityContext.getCurrentUserId());
                    matinst.setModifyTime(new Date());
                    aeaHiItemMatinstMapper.updateAeaHiItemMatinst(matinst);

                    List<BscAttForm> bscAttForms = bscAttMapper.listAttLinkAndDetailByTablePKRecordId("AEA_HI_ITEM_CORRECT_REAL_ININST", "REAL_ININST_ID", matCorrectDto.getAttRealIninstId(), SecurityContext.getCurrentOrgId());
                    for (BscAttForm bscAttForm : bscAttForms) {
                        BscAttLink attLink = new BscAttLink();
                        BeanUtils.copyProperties(bscAttForm, attLink);
                        attLink.setTableName("AEA_HI_ITEM_MATINST");
                        attLink.setPkName("MATINST_ID");
                        attLink.setRecordId(matCorrectDto.getAttMatinstId());
                        bscAttMapper.updateLink(attLink);
                    }
                }

                if ("1".equals(matCorrectDto.getCopyIsPass())) {
                    AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                    aeaHiItemCorrectRealIninst.setIsPass("1");
                    aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);

                    AeaHiItemInoutinst aeaHiItemInoutinst = aeaHiItemInoutinstMapper.getAeaHiItemInoutinstById(matCorrectDto.getCopyInoutinstId());
                    aeaHiItemInoutinst.setIsCollected("1");
                    aeaHiItemInoutinst.setModifier(SecurityContext.getCurrentUserName());
                    aeaHiItemInoutinst.setModifyTime(new Date());
                    aeaHiItemInoutinstMapper.updateAeaHiItemInoutinst(aeaHiItemInoutinst);

                    AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matCorrectDto.getCopyMatinstId());
                    matinst.setRealCopyCount(matinst.getRealCopyCount() == null ? matCorrectDto.getRealCopyCount() : matinst.getRealCopyCount() + matCorrectDto.getRealCopyCount());
                    matinst.setModifier(SecurityContext.getCurrentUserId());
                    matinst.setModifyTime(new Date());
                    aeaHiItemMatinstMapper.updateAeaHiItemMatinst(matinst);
                }

                if ("1".equals(matCorrectDto.getPaperIsPass())) {
                    AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                    aeaHiItemCorrectRealIninst.setIsPass("1");
                    aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);

                    AeaHiItemInoutinst aeaHiItemInoutinst = aeaHiItemInoutinstMapper.getAeaHiItemInoutinstById(matCorrectDto.getPaperInoutinstId());
                    aeaHiItemInoutinst.setIsCollected("1");
                    aeaHiItemInoutinst.setModifier(SecurityContext.getCurrentUserName());
                    aeaHiItemInoutinst.setModifyTime(new Date());
                    aeaHiItemInoutinstMapper.updateAeaHiItemInoutinst(aeaHiItemInoutinst);

                    AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matCorrectDto.getPaperMatinstId());
                    matinst.setRealPaperCount(matinst.getRealPaperCount() == null ? matCorrectDto.getRealPaperCount() : matinst.getRealPaperCount() + matCorrectDto.getRealPaperCount());
                    matinst.setModifier(SecurityContext.getCurrentUserId());
                    matinst.setModifyTime(new Date());
                    aeaHiItemMatinstMapper.updateAeaHiItemMatinst(matinst);
                }

                if ("0".equals(matCorrectDto.getPaperIsPass()) && StringUtils.isNotBlank(matCorrectDto.getPaperRealIninstId())) {
                    AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst = new AeaHiItemCorrectDueIninst();
                    aeaHiItemCorrectDueIninst.setDueIninstId(UUID.randomUUID().toString());
                    aeaHiItemCorrectDueIninst.setInoutinstId(matCorrectDto.getPaperInoutinstId());
                    aeaHiItemCorrectDueIninst.setIsNewInoutinst("0");
                    aeaHiItemCorrectDueIninst.setCorrectOpinion(matCorrectDto.getPaperDueIninstOpinion());
                    aeaHiItemCorrectDueIninst.setPaperCount(matCorrectDto.getPaperCount());
                    correctDueIninsts.add(aeaHiItemCorrectDueIninst);
                    matinstIds.add(matCorrectDto.getPaperMatinstId());
                }

                if ("0".equals(matCorrectDto.getCopyIsPass()) && StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {
                    AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst = new AeaHiItemCorrectDueIninst();
                    aeaHiItemCorrectDueIninst.setDueIninstId(UUID.randomUUID().toString());
                    aeaHiItemCorrectDueIninst.setInoutinstId(matCorrectDto.getCopyInoutinstId());
                    aeaHiItemCorrectDueIninst.setIsNewInoutinst("0");
                    aeaHiItemCorrectDueIninst.setCorrectOpinion(matCorrectDto.getCopyDueIninstOpinion());
                    aeaHiItemCorrectDueIninst.setCopyCount(matCorrectDto.getCopyCount());
                    correctDueIninsts.add(aeaHiItemCorrectDueIninst);
                    matinstIds.add(matCorrectDto.getCopyMatinstId());
                }

                if ("0".equals(matCorrectDto.getAttIsPass()) && StringUtils.isNotBlank(matCorrectDto.getAttRealIninstId())) {
                    AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst = new AeaHiItemCorrectDueIninst();
                    aeaHiItemCorrectDueIninst.setDueIninstId(UUID.randomUUID().toString());
                    aeaHiItemCorrectDueIninst.setInoutinstId(matCorrectDto.getAttInoutinstId());
                    aeaHiItemCorrectDueIninst.setIsNewInoutinst("0");
                    aeaHiItemCorrectDueIninst.setCorrectOpinion(matCorrectDto.getAttDueIninstOpinion());
                    aeaHiItemCorrectDueIninst.setIsNeedAtt("1");
                    correctDueIninsts.add(aeaHiItemCorrectDueIninst);
                    matinstIds.add(matCorrectDto.getAttMatinstId());
                }

            }
        }

        AeaLogItemStateHist aeaLogItemStateHist = aeaLogItemStateHistService.getAeaLogItemStateHistByCorrectId(aeaHiItemCorrect.getCorrectId());

        //继续补正
        if (correctDueIninsts.size() > 0) {

            String correctId1 = UUID.randomUUID().toString();
            AeaHiItemCorrect aeaHiItemCorrect1 = new AeaHiItemCorrect();
            aeaHiItemCorrect1.setCorrectId(correctId1);
            aeaHiItemCorrect1.setIteminstId(aeaHiItemCorrect.getIteminstId());
            aeaHiItemCorrect1.setAppinstId(aeaHiItemCorrect.getAppinstId());
            aeaHiItemCorrect1.setApplyinstId(aeaHiItemCorrect.getApplyinstId());
            aeaHiItemCorrect1.setProjInfoId(aeaHiItemCorrect.getProjInfoId());
            aeaHiItemCorrect1.setIsFlowTrigger(aeaHiItemCorrect.getIsFlowTrigger());
            aeaHiItemCorrect1.setCorrectDueDays(aeaHiItemCorrect.getCorrectDueDays());

            Date nextDay = workdayHolidayService.nextDay(new Date());
            Date correctDueDay = workdayHolidayService.calWorkdayFrom(nextDay, aeaHiItemCorrect.getCorrectDueDays().intValue(), SecurityContext.getCurrentOrgId());
            aeaHiItemCorrect1.setCorrectDueTime(correctDueDay);
            aeaHiItemCorrect1.setMatinstIds(String.join(",", matinstIds));
            aeaHiItemCorrect1.setCorrectState("6");
            aeaHiItemCorrect1.setCorrectMemo(correctMemo);
            aeaHiItemCorrect1.setChargeOrgId(aeaHiItemCorrect.getChargeOrgId());
            aeaHiItemCorrect1.setChargeOrgName(aeaHiItemCorrect.getChargeOrgName());
            aeaHiItemCorrect1.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiItemCorrectService.saveAeaHiItemCorrect(aeaHiItemCorrect1);

            for (AeaHiItemCorrectDueIninst correctDueIninst : correctDueIninsts) {
                correctDueIninst.setCorrectId(correctId1);
            }
            aeaHiItemCorrectDueIninstService.batchSaveAeaHiItemCorrectDueIninst(correctDueIninsts);

            if ("1".equals(aeaHiItemCorrect.getIsFlowTrigger())) {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getTaskinstId(), aeaHiItemCorrect.getAppinstId(), ItemStatus.CORRECT_MATERIAL_START.getValue(),null);
            } else {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), correctMemo, "材料补正", null, ItemStatus.CORRECT_MATERIAL_START.getValue(),null);
            }

            //获取该事项实例的所有补正历史状态根据时间降序
            AeaLogItemStateHist logItemStateHist = new AeaLogItemStateHist();
            logItemStateHist.setIteminstId(aeaHiItemCorrect.getIteminstId());
            logItemStateHist.setNewState(ItemStatus.CORRECT_MATERIAL_START.getValue());
            logItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaLogItemStateHist> aeaLogItemStateHists = aeaLogItemStateHistService.findAeaLogItemStateHist(logItemStateHist);

            //创建材料补正和事项历史记录关联表
            AeaHiItemCorrectStateHist aeaHiItemCorrectStateHist = new AeaHiItemCorrectStateHist();
            aeaHiItemCorrectStateHist.setCorrectStateHistId(UUID.randomUUID().toString());
            aeaHiItemCorrectStateHist.setCorrectId(correctId1);
            aeaHiItemCorrectStateHist.setStateHistId(aeaLogItemStateHists.get(0).getStateHistId());
            aeaHiItemCorrectStateHistService.saveAeaHiItemCorrectStateHist(aeaHiItemCorrectStateHist);

        } else {
            if ("1".equals(aeaHiItemCorrect.getIsFlowTrigger())) {
                //补正正常结束
                String oldState = null;
                if ("7".equals(aeaLogItemStateHist.getOldState())) {
                    //获取该事项实例的所有补正历史状态根据时间降序
                    AeaLogItemStateHist itemStateHist = new AeaLogItemStateHist();
                    aeaLogItemStateHist.setIteminstId(aeaHiItemCorrect.getIteminstId());
                    aeaLogItemStateHist.setNewState(ItemStatus.CORRECT_MATERIAL_START.getValue());
                    aeaLogItemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
                    List<AeaLogItemStateHist> aeaLogItemStateHists = aeaLogItemStateHistService.findAeaLogItemStateHist(aeaLogItemStateHist);
                    for (AeaLogItemStateHist AeaLogItemStateHist : aeaLogItemStateHists) {
                        //判断是否有重复补正的记录
                        if ("7".equals(AeaLogItemStateHist.getOldState()))
                            continue;
                        else {
                            oldState = AeaLogItemStateHist.getOldState();
                            break;
                        }
                    }
                } else {
                    oldState = aeaLogItemStateHist.getOldState();
                }
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getTaskinstId(), aeaHiItemCorrect.getAppinstId(), oldState,null);
                HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(aeaLogItemStateHist.getTaskinstId()).singleResult();
                if (taskInstance == null) throw new Exception("找不到节点信息！");
                HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId()).singleResult();
                if (processInstance.getEndTime() != null)//当前流程未结束
                    bpmProcessService.activateProcessInstanceById(taskInstance.getProcessInstanceId());//激活当前流程

            } else {
                String option = "8".equals(correctState) ? "材料已补正" : "不予受理";
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), null, option, null, aeaLogItemStateHist.getOldState(),null);
            }
        }

        aeaHiItemCorrect.setCorrectState(correctState);
        aeaHiItemCorrectService.updateAeaHiItemCorrect(aeaHiItemCorrect);
    }

    /**
     * 材料删除
     *
     * @param detailIds
     * @throws Exception
     */
    @Override
    public void delelteAttFile(String detailIds, String attRealIninstId) throws Exception {
        AeaHiItemCorrectRealIninst realIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(attRealIninstId);
        if (realIninst != null) {
            fileUtilsService.deleteAttachments(detailIds.split(","));
            List<BscAttFileAndDir> matAttDetail = bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_ITEM_CORRECT_REAL_ININST", "REAL_ININST_ID", new String[]{attRealIninstId});
            realIninst.setAttCount(Long.valueOf(matAttDetail.size()));
            realIninst.setModifyTime(new Date());
            realIninst.setModifier(SecurityContext.getCurrentUserName());
            aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(realIninst);
        }
    }

    @Override
    public void uploadFile(String attRealIninstId, HttpServletRequest request) throws Exception {
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles("file");
        if (files.size() > 0) {
            AeaHiItemCorrectRealIninst realIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(attRealIninstId);
            if (realIninst == null) throw new Exception("材料实例不存在！");
            fileUtilsService.uploadAttachments("AEA_HI_ITEM_CORRECT_REAL_ININST", "REAL_ININST_ID", attRealIninstId, files);
            List<BscAttFileAndDir> matAttDetail = bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_ITEM_CORRECT_REAL_ININST", "REAL_ININST_ID", new String[]{attRealIninstId});
            realIninst.setAttCount(Long.valueOf(matAttDetail.size()));
            realIninst.setModifyTime(new Date());
            realIninst.setModifier(SecurityContext.getCurrentUserName());
            aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(realIninst);
        }
    }

    @Override
    public List<BscAttFileAndDir> getAttFiles(String attRealIninstId) throws Exception {
        if (StringUtils.isBlank(attRealIninstId)) throw new Exception("补正材料ID为空！");
        return bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_ITEM_CORRECT_REAL_ININST", "REAL_ININST_ID", new String[]{attRealIninstId});
    }

    private void getOwner(AeaHiItemCorrect aeaHiItemCorrect) throws Exception {

        if ("1".equals(aeaHiItemCorrect.getApplySubject())) {
            List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(aeaHiItemCorrect.getApplyinstId(), aeaHiItemCorrect.getProjInfoId());
            if (aeaUnitInfos.size() < 1) throw new Exception("找不到业务单位信息！");
            aeaHiItemCorrect.setOwner(aeaUnitInfos.get(0).getApplicant());
        } else {
            if (StringUtils.isBlank(aeaHiItemCorrect.getLinkmanInfoId())) throw new Exception("找不到经办人ID！");
            AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(aeaHiItemCorrect.getLinkmanInfoId());
            if (aeaLinkmanInfo == null) throw new Exception("找不到经办人信息！");
            aeaHiItemCorrect.setOwner(aeaLinkmanInfo.getLinkmanName());
        }
    }
}
