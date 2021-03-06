package com.augurit.aplanmis.front.correct.service;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.sc.day.service.WorkdayHolidayService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.bpm.common.timeCalculate.RestTimeruleinstCalService;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.dto.CorrectinstDto;
import com.augurit.aplanmis.common.dto.MatCorrectDto;
import com.augurit.aplanmis.common.dto.MatCorrectInfoDto;
import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.matCorrect.MatCorrectBaseService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Author: lucas Chan
 * Date: 2019-8-5
 * Description: 材料补正接口
 */
@Service
@Transactional
@Slf4j
public class RestMatCorrectService {

    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;

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
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;

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
    private MatCorrectBaseService matCorrectBaseService;

    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;

    @Autowired
    private RestTimeruleinstCalService restTimeruleinstCalService;

    /**
     * 计算事项少交或者未交的材料
     *
     * @param applyinstId
     * @param iteminstId
     * @throws Exception
     */
    public ContentResultForm getLackMatsByApplyinstIdAndIteminstId(String applyinstId, String iteminstId) throws Exception {

        if (StringUtils.isBlank(applyinstId)) return new ContentResultForm(false, null, "缺少参数：applyinstId");
        if (StringUtils.isBlank(iteminstId)) return new ContentResultForm(false, null, "缺少参数：iteminstId");
        MatCorrectInfoDto matCorrectInfoDto = new MatCorrectInfoDto();
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (aeaHiApplyinst != null) {

            matCorrectInfoDto.setApplyinstCode(aeaHiApplyinst.getApplyinstCode());
            //获取材料实例列表
            List<AeaHiItemMatinst> matinsts = new ArrayList();
            //材料定义列表
            List<AeaItemMat> mats = new ArrayList();

            AeaHiIteminst aeaHiIteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);

            if (aeaHiIteminst == null) return new ContentResultForm(false, null, "找不到该事项实例！");

            matCorrectInfoDto.setIteminstName(aeaHiIteminst.getIteminstName());
            matCorrectInfoDto.setChargeOrgName(aeaHiIteminst.getApproveOrgName());

            mats.addAll(aeaItemMatService.getMatListByApplyinstIdContainsMatinst(applyinstId, iteminstId));

            Map<String, List> map = matCorrectBaseService.getCorrectMatsAndSubmittedMats(mats);
            matCorrectInfoDto.setSubmittedMats(map.get("SubmittedMats"));
            matCorrectInfoDto.setMatCorrectDtos(map.get("MatCorrectDtos"));

            //目前申报实例和项目是一对一关系
            List<AeaProjInfo> projInfos = aeaProjInfoService.findApplyProj(applyinstId);
            if (projInfos.size() < 1) return new ContentResultForm(false, null, "找不到项目信息！");
            AeaProjInfo projInfo = projInfos.get(0);
            matCorrectInfoDto.setProjInfoName(projInfo.getProjName());
            matCorrectInfoDto.setLocalCode(projInfo.getLocalCode());
            matCorrectInfoDto.setProjInfoId(projInfo.getProjInfoId());
            String owner = null;

            if ("1".equals(aeaHiApplyinst.getApplySubject())) {
                List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(applyinstId, projInfos.get(0).getProjInfoId());
                if (aeaUnitInfos.size() < 1) return new ContentResultForm(false, null, "找不到业主单位信息！");
                owner = aeaUnitInfos.get(0).getApplicant();
            } else {
                AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
                owner = aeaLinkmanInfo.getLinkmanName();
            }
            matCorrectInfoDto.setOwner(owner);
        }

        return new ContentResultForm(true, matCorrectInfoDto, "查询成功");
    }

    private Map<String, List> getCorrectMatsAndSubmittedMats(List<AeaItemMat> mats, List<AeaHiItemMatinst> matinsts) {

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
            matCorrectDto.setMatCode(mat.getMatCode());
            matCorrectDto.setMatName(mat.getMatName());

            MatCorrectDto matCorrectDto1 = new MatCorrectDto();
            BeanUtils.copyProperties(matCorrectDto, matCorrectDto1);
            for (AeaHiItemMatinst matinst : matinsts) {
                if (mat.getMatId().equals(matinst.getMatId())) {

                    //判断必须的电子材料是否已经上传
                    if ("1".equals(mat.getAttIsRequire())) {
                        if (StringUtils.isBlank(matCorrectDto.getIsNeedAtt())) {
                            matCorrectDto.setAttIsCollected("0");
                            matCorrectDto.setIsNeedAtt("1");
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
                            matCorrectDto.setPaperMatinstId(matinst.getMatinstId());
                            matCorrectDto.setPaperIsCollected("1");
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
                            matCorrectDto.setCopyIsCollected("1");
                            matCorrectDto.setCopyMatinstId(matinst.getMatinstId());
                            matCorrectDto.setCopyCount(0l);
                        }
                    }

                    if (matinst.getRealCopyCount() != null && matinst.getRealCopyCount() > 0) {
                        matCorrectDto1.setCopyCount(mat.getDueCopyCount());
                        matCorrectDto1.setCopyIsCollected("1");
                        matCorrectDto1.setRealCopyCount(matinst.getRealCopyCount());
                        matCorrectDto1.setCopyMatinstId(matinst.getMatinstId());
                    }

                    if (matinst.getRealPaperCount() != null && matinst.getRealPaperCount() > 0) {
                        matCorrectDto1.setPaperCount(mat.getDuePaperCount());
                        matCorrectDto1.setRealPaperCount(matinst.getRealPaperCount());
                        matCorrectDto1.setPaperMatinstId(matinst.getMatinstId());
                        matCorrectDto1.setPaperIsCollected("1");
                    }

                    if (matinst.getAttCount() != null && matinst.getAttCount() > 0) {
                        matCorrectDto1.setAttIsCollected("1");
                        matCorrectDto1.setAttCount(matinst.getAttCount());
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

            if (StringUtils.isBlank(matCorrectDto.getAttIsCollected()) && "1".equals(mat.getAttIsRequire())) {
                matCorrectDto.setIsNeedAtt("1");
                matCorrectDto.setAttIsCollected("0");
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
    public ResultForm createMatCorrectinst(CorrectinstDto correctinstDto) throws Exception {

        if (StringUtils.isBlank(correctinstDto.getApplyinstId())) return new ResultForm(false, "申请实例ID为空！");
        if (StringUtils.isBlank(correctinstDto.getIteminstId())) return new ResultForm(false, "事项实例ID为空！");
        if (StringUtils.isBlank(correctinstDto.getIsFlowTrigger())) return new ResultForm(false, "补正触发类型为空！");
        if ("1".equals(correctinstDto.getIsFlowTrigger()) && StringUtils.isBlank(correctinstDto.getTaskinstId()))
            return new ResultForm(false, "节点实例ID为空！");
        if (correctinstDto.getCorrectDueDays() == null || correctinstDto.getCorrectDueDays() <= 0)
            return new ResultForm(false, "补正期限不符合要求！");
        if (StringUtils.isBlank(correctinstDto.getMatCorrectDtosJson()) || "[]".equals(correctinstDto.getMatCorrectDtosJson()))
            return new ResultForm(false, "请选择要补正的材料！");

        List<AeaProjInfo> aeaProjInfos = aeaProjInfoService.findApplyProj(correctinstDto.getApplyinstId());
        if (aeaProjInfos.size() > 0)
            correctinstDto.setProjInfoId(aeaProjInfos.get(0).getProjInfoId());
        else
            return new ResultForm(false, "找不到项目信息！");

        AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(correctinstDto.getIteminstId());
        if ("6".equals(iteminst.getIteminstState()) || "7".equals(iteminst.getIteminstState()) || "9".equals(iteminst.getIteminstState()) || "10".equals(iteminst.getIteminstState()))
            return new ResultForm(false, "该事项当前状态处于补件或特别程序中！");

        correctinstDto.setCorrectState("6");//表示待补正
        List<String> matinstIds = new ArrayList();

        //将该事项的情形实例保存在内存中
        List<AeaHiItemStateinst> stateinsts = new ArrayList();
        List<AeaHiParStateinst> parStateinsts = new ArrayList();

        List<AeaHiItemCorrectDueIninst> correctDueIninsts = new ArrayList();
        String correctId = UUID.randomUUID().toString();
        JSONArray jsonArray = JSONArray.parseArray(correctinstDto.getMatCorrectDtosJson());
        List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
        if (matCorrectDtos.size() < 1) return new ResultForm(false, "请选择要补正的材料！");

        for (MatCorrectDto matCorrectDto : matCorrectDtos) {
            if (matCorrectDto.getCopyCount() != null && matCorrectDto.getCopyCount() > 0) {
                AeaHiItemCorrectDueIninst correctDueIninst = new AeaHiItemCorrectDueIninst();
                correctDueIninst.setDueIninstId(UUID.randomUUID().toString());
                correctDueIninst.setCorrectId(correctId);
                correctDueIninst.setCopyCount(matCorrectDto.getCopyCount());
                correctDueIninst.setCorrectOpinion(matCorrectDto.getCopyDueIninstOpinion());
                correctDueIninst.setIsNewInoutinst("1");

                //该复印件材料已经实例化
                if (StringUtils.isNotBlank(matCorrectDto.getCopyIsCollected()) && "1".equals(matCorrectDto.getCopyIsCollected())) {

                    if (StringUtils.isBlank(matCorrectDto.getCopyMatinstId()))
                        return new ResultForm(false, "找不到材料（" + matCorrectDto.getMatName() + "）的复印件实例Id！");
                    AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
                    aeaHiItemInoutinst.setIteminstId(correctinstDto.getIteminstId());
                    aeaHiItemInoutinst.setIsCollected("1");
                    aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                    aeaHiItemInoutinst.setMatinstId(matCorrectDto.getCopyMatinstId());
                    List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);
                    if (itemInoutinsts.size() < 1)
                        return new ResultForm(false, "找不到材料（" + matCorrectDto.getMatName() + "）的复印件输入实例信息！");

                    correctDueIninst.setInoutinstId(itemInoutinsts.get(0).getInoutinstId());
                    correctDueIninst.setIsNewInoutinst("0");
                    matinstIds.add(matCorrectDto.getCopyMatinstId());
                } else {
                    String matinstId = UUID.randomUUID().toString();
                    matinstIds.add(matinstId);
                    String inoutinstId = UUID.randomUUID().toString();
                    correctDueIninst.setInoutinstId(inoutinstId);
                    //创建材料实例和输入输出实例
                    this.creatMatinstAndInoutinst(matCorrectDto, correctinstDto.getIteminstId(), matinstId, inoutinstId, correctinstDto.getApplyinstId(), correctinstDto.getProjInfoId(), stateinsts, parStateinsts);
                }

                correctDueIninsts.add(correctDueIninst);
            }

            if (matCorrectDto.getPaperCount() != null && matCorrectDto.getPaperCount() > 0) {
                AeaHiItemCorrectDueIninst correctDueIninst = new AeaHiItemCorrectDueIninst();
                correctDueIninst.setDueIninstId(UUID.randomUUID().toString());
                correctDueIninst.setCorrectId(correctId);
                correctDueIninst.setPaperCount(matCorrectDto.getPaperCount());
                correctDueIninst.setIsNewInoutinst("1");
                correctDueIninst.setCorrectOpinion(matCorrectDto.getPaperDueIninstOpinion());

                //该纸质原件材料已经实例化
                if (StringUtils.isNotBlank(matCorrectDto.getPaperIsCollected()) && "1".equals(matCorrectDto.getPaperIsCollected())) {

                    if (StringUtils.isBlank(matCorrectDto.getPaperMatinstId()))
                        return new ResultForm(false, "找不到材料（" + matCorrectDto.getMatName() + "）的原件实例Id！");
                    AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
                    aeaHiItemInoutinst.setIsCollected("1");
                    aeaHiItemInoutinst.setIteminstId(correctinstDto.getIteminstId());
                    aeaHiItemInoutinst.setMatinstId(matCorrectDto.getPaperMatinstId());
                    aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                    List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);
                    if (itemInoutinsts.size() < 1)
                        return new ResultForm(false, "找不到材料（" + matCorrectDto.getMatName() + "）的原件输入实例信息！");
                    correctDueIninst.setInoutinstId(itemInoutinsts.get(0).getInoutinstId());
                    correctDueIninst.setIsNewInoutinst("0");
                    matinstIds.add(matCorrectDto.getPaperMatinstId());
                } else {

                    String matinstId = UUID.randomUUID().toString();
                    String inoutinstId = UUID.randomUUID().toString();
                    matinstIds.add(matinstId);
                    correctDueIninst.setInoutinstId(inoutinstId);
                    //创建材料实例和输入输出实例
                    this.creatMatinstAndInoutinst(matCorrectDto, correctinstDto.getIteminstId(), matinstId, inoutinstId, correctinstDto.getApplyinstId(), correctinstDto.getProjInfoId(), stateinsts, parStateinsts);
                }

                correctDueIninsts.add(correctDueIninst);
            }

            if (StringUtils.isNotBlank(matCorrectDto.getIsNeedAtt()) && "1".equals(matCorrectDto.getIsNeedAtt())) {
                AeaHiItemCorrectDueIninst correctDueIninst = new AeaHiItemCorrectDueIninst();
                correctDueIninst.setDueIninstId(UUID.randomUUID().toString());
                correctDueIninst.setCorrectId(correctId);
                correctDueIninst.setIsNeedAtt("1");
                correctDueIninst.setIsNewInoutinst("1");
                correctDueIninst.setCorrectOpinion(matCorrectDto.getAttDueIninstOpinion());

                //该电子件材料已经实例化
                if (StringUtils.isNotBlank(matCorrectDto.getAttIsCollected()) && "1".equals(matCorrectDto.getAttIsCollected())) {

                    if (StringUtils.isBlank(matCorrectDto.getAttMatinstId()))
                        return new ResultForm(false, "找不到材料（" + matCorrectDto.getMatName() + "）的电子件实例Id！");
                    AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
                    aeaHiItemInoutinst.setIteminstId(correctinstDto.getIteminstId());
                    aeaHiItemInoutinst.setMatinstId(matCorrectDto.getAttMatinstId());
                    aeaHiItemInoutinst.setIsCollected("1");
                    List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);
                    if (itemInoutinsts.size() < 1)
                        return new ResultForm(false, "找不到材料（" + matCorrectDto.getMatName() + "）的输入实例信息！");

                    correctDueIninst.setInoutinstId(itemInoutinsts.get(0).getInoutinstId());
                    correctDueIninst.setIsNewInoutinst("0");
                    matinstIds.add(matCorrectDto.getAttMatinstId());

                } else {
                    String matinstId = UUID.randomUUID().toString();
                    String inoutinstId = UUID.randomUUID().toString();
                    correctDueIninst.setInoutinstId(inoutinstId);
                    matinstIds.add(matinstId);
                    //创建材料实例和输入输出实例
                    this.creatMatinstAndInoutinst(matCorrectDto, correctinstDto.getIteminstId(), matinstId, inoutinstId, correctinstDto.getApplyinstId(), correctinstDto.getProjInfoId(), stateinsts, parStateinsts);
                }

                correctDueIninsts.add(correctDueIninst);
            }
        }

        //实例化材料补正实例
        AeaHiItemCorrect aeaHiItemCorrect = new AeaHiItemCorrect();
        aeaHiItemCorrect.setCorrectId(correctId);
        BeanUtils.copyProperties(correctinstDto, aeaHiItemCorrect);

        if ("1".equals(correctinstDto.getIsFlowTrigger())) {
            String appinstId = matCorrectBaseService.getAppinstIdByApplyinstId(correctinstDto.getApplyinstId());
            if (StringUtils.isBlank(appinstId)) return new ResultForm(false, "找不到流程模板实例！");
            aeaHiItemCorrect.setAppinstId(appinstId);
        }
        if (iteminst == null) return new ResultForm(false, "找不到事项实例！");
        aeaHiItemCorrect.setChargeOrgId(iteminst.getApproveOrgId());
        aeaHiItemCorrect.setChargeOrgName(iteminst.getApproveOrgName());

        Date nextDay = workdayHolidayService.nextDay(new Date());
        Date correctDueDay = workdayHolidayService.calWorkdayFrom(nextDay, aeaHiItemCorrect.getCorrectDueDays().intValue(), SecurityContext.getCurrentOrgId());
        aeaHiItemCorrect.setCorrectDueTime(correctDueDay);
        aeaHiItemCorrect.setMatinstIds(String.join(",", matinstIds));
        aeaHiItemCorrectService.saveAeaHiItemCorrect(aeaHiItemCorrect);

        //实例化补正清单的材料份数
        aeaHiItemCorrectDueIninstService.batchSaveAeaHiItemCorrectDueIninst(correctDueIninsts);

        //更新事项状态和新增历史记录
        if ("1".equals(correctinstDto.getIsFlowTrigger())) {

            aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), correctinstDto.getTaskinstId(), aeaHiItemCorrect.getAppinstId(), ItemStatus.CORRECT_MATERIAL_START.getValue(), null);
            HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(correctinstDto.getTaskinstId()).singleResult();
            if (taskInstance == null) return new ResultForm(false, "找不到节点信息！");
            //当前流程未挂起
            if (!bpmProcessService.isProcessSuspended(taskInstance.getProcessInstanceId())) {
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId()).singleResult();
                //判断当前流程是否结束
                if (historicProcessInstance.getEndTime() == null) {
                    //挂起当前流程
                    bpmProcessService.suspendProcessInstanceByIdAndTaskinstId(taskInstance.getProcessInstanceId(), correctinstDto.getTaskinstId());
                }
            }
        } else {
            aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(iteminst.getIteminstId(), correctinstDto.getCorrectMemo(), "材料补正", null, ItemStatus.CORRECT_MATERIAL_START.getValue(), null);
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
        aeaHiItemCorrectStateHist.setStateHistId(aeaLogItemStateHists.get(0).getStateHistId());
        aeaHiItemCorrectStateHistService.saveAeaHiItemCorrectStateHist(aeaHiItemCorrectStateHist);

        //材料接收回执
        matCorrectBaseService.createReceive(iteminst.getItemVerId(), correctinstDto.getApplyinstId(), correctId, "6");
        //物料接收回执
        matCorrectBaseService.createReceive(iteminst.getItemVerId(), correctinstDto.getApplyinstId(), correctId, "12");

        return new ResultForm(true, "补正实例创建成功！");
    }

    //创建材料实例和输入输出实例
    private void creatMatinstAndInoutinst(MatCorrectDto matCorrectDto, String iteminstId, String matinstId, String inoutinstId, String applyinstId, String projInfoId, List<AeaHiItemStateinst> stateinsts, List<AeaHiParStateinst> parStateinsts) throws Exception {

        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);

        if (aeaHiApplyinst == null) throw new Exception("找不到申请实例信息！");

        //创建材料实例
        AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
        aeaHiItemMatinst.setMatinstId(matinstId);
        aeaHiItemMatinst.setMatId(matCorrectDto.getMatId());
        aeaHiItemMatinst.setMatinstName(matCorrectDto.getMatName());
        aeaHiItemMatinst.setMatinstCode(matCorrectDto.getMatCode());
        aeaHiItemMatinst.setProjInfoId(projInfoId);
        aeaHiItemMatinst.setCreateTime(new Date());
        aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
        aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());

        if ("1".equals(aeaHiApplyinst.getApplySubject())) {
            List<AeaUnitInfo> unitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(applyinstId, projInfoId);

            if (unitInfos.size() < 1) throw new Exception("找不到业务单位信息！");
            String ownnerUnitInfoId = unitInfos.get(0).getUnitInfoId();
            aeaHiItemMatinst.setUnitInfoId(ownnerUnitInfoId);
            aeaHiItemMatinst.setMatinstSource("u");
        } else {
            aeaHiItemMatinst.setLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
            aeaHiItemMatinst.setMatinstSource("l");
        }

        aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);

        boolean handWay = false;

        if ("0".equals(aeaHiApplyinst.getIsSeriesApprove())) {

            AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinstId);

            if ("1".equals(aeaHiParStageinst.getHandWay())) {
                handWay = true;
                //创建输入输出实例
                matCorrectBaseService.createStageIteminoutinst(matCorrectDto.getMatId(), matinstId, inoutinstId, iteminstId, aeaHiParStageinst.getStageId(), applyinstId, parStateinsts, aeaHiParStageinst.getIsNeedState(), "0");
            }
        }

        if ("1".equals(aeaHiApplyinst.getIsSeriesApprove()) || !handWay) {

            List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);

            for (AeaHiIteminst iteminst : iteminsts) {

                String inoutId = matCorrectBaseService.getInoutIdByItemVerIdAndMatId(matCorrectDto.getMatId(), iteminst.getItemVerId(), applyinstId, stateinsts);

                if (StringUtils.isBlank(inoutId)) continue;

                //创建输入输出实例
                AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();

                if (iteminstId.equals(iteminst.getIteminstId()))
                    aeaHiItemInoutinst.setInoutinstId(inoutinstId);
                else
                    aeaHiItemInoutinst.setInoutinstId(UUID.randomUUID().toString());

                aeaHiItemInoutinst.setItemInoutId(inoutId);
                aeaHiItemInoutinst.setIteminstId(iteminst.getIteminstId());
                aeaHiItemInoutinst.setIsCollected("0");
                aeaHiItemInoutinst.setMatinstId(matinstId);
                aeaHiItemInoutinst.setCreateTime(new Date());
                aeaHiItemInoutinst.setCreater(SecurityContext.getCurrentUserName());
                aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(aeaHiItemInoutinst);
            }
        }
    }

    /**
     * 根据申请实例id获取事项实例id获取材料补正列表
     *
     * @param applyinstId
     * @param iteminstId
     * @return
     * @throws Exception
     */
    public List<AeaHiItemCorrect> getMatCorrectInfoByApplyinstOrIteminst(String applyinstId, String iteminstId) throws Exception {
        if (StringUtils.isBlank(applyinstId) && StringUtils.isBlank(iteminstId)) {
            throw new InvalidParameterException("参数applyinstId和iteminstId不能同时为空！");
        }
        List<AeaHiItemCorrect> result = new ArrayList<>();
        AeaHiItemCorrect query = new AeaHiItemCorrect();
        query.setRootOrgId(SecurityContext.getCurrentOrgId());
        if (StringUtils.isNotBlank(iteminstId)) {
            query.setIteminstId(iteminstId);
        } else {
            query.setApplyinstId(applyinstId);
        }
        List<AeaHiItemCorrect> aeaHiItemCorrects = aeaHiItemCorrectService.listAeaHiItemCorrect(query);
        for (AeaHiItemCorrect correct : aeaHiItemCorrects) {
            result.add(getMatCorrectInfo(correct.getCorrectId()));
        }
        return result;
    }

    /**
     * 材料补正信息
     *
     * @param correctId
     * @return
     * @throws Exception
     */
    public AeaHiItemCorrect getMatCorrectInfo(String correctId) throws Exception {

        if (StringUtils.isBlank(correctId)) throw new Exception("材料补正实例ID为空！");

        AeaHiItemCorrect aeaHiItemCorrect = aeaHiItemCorrectService.getAeaHiItemCorrectById(correctId);
        if (aeaHiItemCorrect == null) throw new Exception("找不到材料补正实例信息！");

        this.getOwner(aeaHiItemCorrect); //获取业主名称

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
                matCorrectDto.setReviewKeyPoints(correctDueIninst.getReviewKeyPoints());
            }

            if (correctDueIninst.getCopyCount() != null && correctDueIninst.getCopyCount() > 0) {
                matCorrectDto.setCopyMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setCopyInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setCopyCount(correctDueIninst.getCopyCount());
                matCorrectDto.setCopyDueIninstOpinion(correctDueIninst.getCorrectOpinion());
                matCorrectDto.setCopyDueIninstId(correctDueIninst.getDueIninstId());
            }

            if (correctDueIninst.getPaperCount() != null && correctDueIninst.getPaperCount() > 0) {
                matCorrectDto.setPaperMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setPaperInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setPaperCount(correctDueIninst.getPaperCount());
                matCorrectDto.setPaperDueIninstOpinion(correctDueIninst.getCorrectOpinion());
                matCorrectDto.setPaperDueIninstId(correctDueIninst.getDueIninstId());
            }

            if (StringUtils.isNotBlank(correctDueIninst.getIsNeedAtt()) && "1".equals(correctDueIninst.getIsNeedAtt())) {
                matCorrectDto.setIsNeedAtt("1");
                matCorrectDto.setAttInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setAttMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setAttDueIninstOpinion(correctDueIninst.getCorrectOpinion());
                matCorrectDto.setAttDueIninstId(correctDueIninst.getDueIninstId());
            }

            map.put(correctDueIninst.getMatId(), matCorrectDto);
        }
        //已补正的材料清单
        List<AeaHiItemCorrectRealIninst> aeaHiItemCorrectRealIninsts = aeaHiItemCorrectRealIninstService.getCorrectRealIninstByCorrectId(correctId);
        for (AeaHiItemCorrectRealIninst correctRealIninst : aeaHiItemCorrectRealIninsts) {

            if (map.get(correctRealIninst.getMatId()) == null) continue;
            MatCorrectDto matCorrectDto = map.get(correctRealIninst.getMatId());

            if (correctRealIninst.getAttCount() != null && correctRealIninst.getAttCount() >= 0l) {
                matCorrectDto.setAttCount(correctRealIninst.getAttCount());
                matCorrectDto.setAttRealIninstId(correctRealIninst.getRealIninstId());
            }

            if (correctRealIninst.getRealPaperCount() != null && correctRealIninst.getRealPaperCount() >= 0l) {
                matCorrectDto.setRealPaperCount(correctRealIninst.getRealPaperCount());
                matCorrectDto.setPaperRealIninstId(correctRealIninst.getRealIninstId());
            }

            if (correctRealIninst.getRealCopyCount() != null && correctRealIninst.getRealCopyCount() >= 0l) {
                matCorrectDto.setRealCopyCount(correctRealIninst.getRealCopyCount());
                matCorrectDto.setCopyRealIninstId(correctRealIninst.getRealIninstId());
            }

            //查询材料实例对应的附件detailId
            List<BscAttFileAndDir> attFiles = getAttFiles(correctRealIninst.getRealIninstId());
            matCorrectDto.setAttFiles(attFiles);
            map.put(correctRealIninst.getMatId(), matCorrectDto);
        }

        List<MatCorrectDto> matCorrectDtos = new ArrayList();
        matCorrectDtos.addAll(map.values());

        for (MatCorrectDto matCorrectDto : matCorrectDtos) {
            if ("1".equals(matCorrectDto.getIsNeedAtt()) && StringUtils.isBlank(matCorrectDto.getAttRealIninstId())) {
                AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
                aeaHiItemCorrectRealIninst.setCorrectId(correctId);
                aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getAttInoutinstId());
                aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getAttDueIninstId());
                aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
                List<AeaHiItemCorrectRealIninst> realIninsts = aeaHiItemCorrectRealIninstService.listAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                if (realIninsts.size() > 0) {
                    matCorrectDto.setAttRealIninstId(realIninsts.get(0).getRealIninstId());
                    matCorrectDto.setAttCount(realIninsts.get(0).getAttCount());
                } else {
                    String realInstId = UUID.randomUUID().toString();
                    aeaHiItemCorrectRealIninst.setRealIninstId(realInstId);
                    aeaHiItemCorrectRealIninst.setIsPass("0");
                    aeaHiItemCorrectRealIninst.setAttCount(0l);
                    aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);

                    matCorrectDto.setAttRealIninstId(realInstId);
                    matCorrectDto.setAttCount(0l);
                }
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
    public ResultForm saveMatCorrectInfo(MatCorrectinstDto matCorrectinstDto) throws Exception {

        if (StringUtils.isBlank(matCorrectinstDto.getCorrectId())) return new ResultForm(false, "材料补正实例ID为空！");
        if (StringUtils.isBlank(matCorrectinstDto.getCorrectState())) return new ResultForm(false, "材料补正实例状态为空！");

        AeaHiItemCorrect aeaHiItemCorrect = aeaHiItemCorrectService.getAeaHiItemCorrectById(matCorrectinstDto.getCorrectId());
        if (aeaHiItemCorrect == null) return new ResultForm(false, "找不到材料补正实例信息！");

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
                        aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getPaperDueIninstId());
                        aeaHiItemCorrectRealIninst.setCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiItemCorrectRealIninst.setIsPass("0");
                        aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getPaperInoutinstId());
                        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());

                        if (!this.findCorrectRealinst(aeaHiItemCorrectRealIninst, "paper", matCorrectDto.getRealPaperCount())) {
                            aeaHiItemCorrectRealIninst.setRealIninstId(UUID.randomUUID().toString());
                            aeaHiItemCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                            aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                        }
                    }
                }

                if (matCorrectDto.getRealCopyCount() != null && matCorrectDto.getRealCopyCount() > 0) {

                    if (StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {
                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
                        aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getCopyDueIninstId());
                        aeaHiItemCorrectRealIninst.setCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiItemCorrectRealIninst.setIsPass("0");
                        aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getCopyInoutinstId());
                        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());

                        if (!this.findCorrectRealinst(aeaHiItemCorrectRealIninst, "copy", matCorrectDto.getRealCopyCount())) {
                            aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                            aeaHiItemCorrectRealIninst.setRealIninstId(UUID.randomUUID().toString());
                            aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                        }

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
                        return new ResultForm(false, "缺少纸质材料（" + matCorrectDto.getMatinstName() + "）");

                    if (StringUtils.isNotBlank(matCorrectDto.getPaperRealIninstId())) {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
                        aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getPaperDueIninstId());
                        aeaHiItemCorrectRealIninst.setCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getPaperInoutinstId());
                        aeaHiItemCorrectRealIninst.setIsPass("0");
                        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());

                        if (!this.findCorrectRealinst(aeaHiItemCorrectRealIninst, "paper", matCorrectDto.getRealPaperCount())) {
                            aeaHiItemCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                            aeaHiItemCorrectRealIninst.setRealIninstId(UUID.randomUUID().toString());
                            aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                        }
                    }
                }

                if (matCorrectDto.getCopyCount() != null && matCorrectDto.getCopyCount() > 0) {
                    if (matCorrectDto.getRealCopyCount() == null || matCorrectDto.getRealCopyCount() < matCorrectDto.getCopyCount())
                        return new ResultForm(false, "缺少复印件（" + matCorrectDto.getMatinstName() + "）");

                    if (StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                        aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                        aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                    } else {

                        AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = new AeaHiItemCorrectRealIninst();
                        aeaHiItemCorrectRealIninst.setDueIninstId(matCorrectDto.getCopyDueIninstId());
                        aeaHiItemCorrectRealIninst.setCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiItemCorrectRealIninst.setInoutinstId(matCorrectDto.getCopyInoutinstId());
                        aeaHiItemCorrectRealIninst.setIsPass("0");
                        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());

                        if (!this.findCorrectRealinst(aeaHiItemCorrectRealIninst, "copy", matCorrectDto.getCopyCount())) {
                            aeaHiItemCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                            aeaHiItemCorrectRealIninst.setRealIninstId(UUID.randomUUID().toString());
                            aeaHiItemCorrectRealIninstService.saveAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
                        }
                    }
                }

                if (StringUtils.isNotBlank(matCorrectDto.getIsNeedAtt()) && "1".equals(matCorrectDto.getIsNeedAtt())) {
                    if (matCorrectDto.getAttCount() == null || matCorrectDto.getAttCount() < 1)
                        return new ResultForm(false, "缺少电子件（" + matCorrectDto.getMatinstName() + "）");
                }
            }

            //检查材料是否已全部补正
            if (!this.isCheckCorrect(matCorrectinstDto.getCorrectId())) return new ResultForm(false, "还有材料未补正！");

        } else {    //不予受理
            aeaHiItemCorrect.setCorrectUserName(SecurityContext.getCurrentUser().getUserName());
            aeaHiItemCorrect.setCorrectEndTime(new Date());
        }

        if (StringUtils.isBlank(aeaHiItemCorrect.getOpsUserId()) || !SecurityContext.getCurrentUserId().equals(aeaHiItemCorrect.getOpsUserId())) {
            aeaHiItemCorrect.setOpsUserId(SecurityContext.getCurrentUserId());
            aeaHiItemCorrect.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
        }
        aeaHiItemCorrect.setOpsTime(new Date());
        aeaHiItemCorrect.setCorrectState(state);
        aeaHiItemCorrectService.updateAeaHiItemCorrect(aeaHiItemCorrect);

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        AeaLogItemStateHist aeaLogItemStateHist = aeaLogItemStateHistService.getAeaLogItemStateHistByCorrectId(aeaHiItemCorrect.getCorrectId());

        if ("7".equals(state)) {
            //更新事项状态和新增历史记录
            if ("1".equals(aeaLogItemStateHist.getIsFlowTrigger())) {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getTaskinstId(), aeaHiItemCorrect.getAppinstId(), ItemStatus.CORRECT_MATERIAL_END.getValue(), opuWinId);
            } else {
                String option = "材料已补正";
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), option, "材料补正", aeaHiItemCorrect.getCorrectDesc(), ItemStatus.CORRECT_MATERIAL_END.getValue(), opuWinId);
            }
        } else if ("5".equals(state)) {

            HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(aeaLogItemStateHist.getTaskinstId()).singleResult();

            if ("1".equals(aeaHiItemCorrect.getIsFlowTrigger())) {
                //补正正常结束
                String oldState = this.getPreMatCorrectState(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getOldState(), "1");
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getTaskinstId(), aeaHiItemCorrect.getAppinstId(), oldState, SecurityContext.getCurrentOrgId());
                if (taskInstance == null) throw new Exception("找不到节点信息！");
                if (bpmProcessService.isProcessSuspended(taskInstance.getProcessInstanceId())) {
                    //判断流程是否已挂起
                    HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId()).singleResult();
                    //判断当前流程是否结束
                    if (historicProcessInstance.getEndTime() == null) {
                        bpmProcessService.activateProcessInstanceById(taskInstance.getProcessInstanceId());//激活当前流程
                    }
                }
            } else {
                String oldState = this.getPreMatCorrectState(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getOldState(), "0");
                String option = "不予受理";
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), option, "材料补全", null, oldState, SecurityContext.getCurrentOrgId());
            }

            //更新时限计时
            restTimeruleinstCalService.updateTimeruleinstByProcessinstId(taskInstance.getProcessInstanceId());
        }

        return new ResultForm(true, "已保存材料补正实例！");
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

        Set<String> correctedList = new TreeSet();
        for (AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst : aeaHiItemCorrectRealIninsts) {

            for (AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst : aeaHiItemCorrectDueIninsts) {

                if (aeaHiItemCorrectDueIninst.getDueIninstId().equals(aeaHiItemCorrectRealIninst.getDueIninstId())) {

                    if (aeaHiItemCorrectDueIninst.getPaperCount() != null && aeaHiItemCorrectDueIninst.getPaperCount() > 0 && aeaHiItemCorrectRealIninst.getRealPaperCount() != null && aeaHiItemCorrectRealIninst.getRealPaperCount() >= aeaHiItemCorrectDueIninst.getPaperCount()) {
                        correctedList.add(aeaHiItemCorrectRealIninst.getDueIninstId());
                        break;
                    }

                    if (aeaHiItemCorrectDueIninst.getCopyCount() != null && aeaHiItemCorrectDueIninst.getCopyCount() > 0 && aeaHiItemCorrectRealIninst.getRealCopyCount() != null && aeaHiItemCorrectRealIninst.getRealCopyCount() >= aeaHiItemCorrectDueIninst.getCopyCount()) {
                        correctedList.add(aeaHiItemCorrectRealIninst.getDueIninstId());
                        break;
                    }

                    if (StringUtils.isNotBlank(aeaHiItemCorrectDueIninst.getIsNeedAtt()) && "1".equals(aeaHiItemCorrectDueIninst.getIsNeedAtt()) && aeaHiItemCorrectRealIninst.getAttCount() != null && aeaHiItemCorrectRealIninst.getAttCount() > 0) {
                        correctedList.add(aeaHiItemCorrectRealIninst.getDueIninstId());
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
                matCorrectDto.setCopyDueIninstOpinion(correctDueIninst.getCorrectOpinion());
            }

            if (correctDueIninst.getPaperCount() != null && correctDueIninst.getPaperCount() > 0) {
                matCorrectDto.setPaperMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setPaperInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setPaperCount(correctDueIninst.getPaperCount());
                matCorrectDto.setPaperDueIninstOpinion(correctDueIninst.getCorrectOpinion());
            }

            if (StringUtils.isNotBlank(correctDueIninst.getIsNeedAtt()) && "1".equals(correctDueIninst.getIsNeedAtt())) {
                matCorrectDto.setIsNeedAtt("1");
                matCorrectDto.setAttInoutinstId(correctDueIninst.getInoutinstId());
                matCorrectDto.setAttMatinstId(correctDueIninst.getMatinstId());
                matCorrectDto.setAttDueIninstOpinion(correctDueIninst.getCorrectOpinion());
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

                    AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matCorrectDto.getAttMatinstId());
                    matinst.setAttCount(matinst.getAttCount() == null ? matCorrectDto.getAttCount() : matinst.getAttCount() + matCorrectDto.getAttCount());
                    matinst.setModifier(SecurityContext.getCurrentUserId());
                    matinst.setModifyTime(new Date());
                    aeaHiItemMatinstMapper.updateAeaHiItemMatinst(matinst);

                    //修改附件的业务ID
                    matCorrectBaseService.updateAttFileBusTableName(matCorrectDto.getAttMatinstId(), "AEA_HI_ITEM_CORRECT_REAL_ININST", "REAL_ININST_ID", matCorrectDto.getAttRealIninstId());

                    //所有需要该材料的事项实例都更改已收状态
                    this.updateInoutinstState(matCorrectDto.getAttMatinstId());
                }

                if ("1".equals(matCorrectDto.getCopyIsPass())) {
                    AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                    aeaHiItemCorrectRealIninst.setIsPass("1");
                    aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);

                    AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matCorrectDto.getCopyMatinstId());
                    matinst.setRealCopyCount(matinst.getRealCopyCount() == null ? matCorrectDto.getRealCopyCount() : matinst.getRealCopyCount() + matCorrectDto.getRealCopyCount());
                    matinst.setModifier(SecurityContext.getCurrentUserId());
                    matinst.setModifyTime(new Date());
                    aeaHiItemMatinstMapper.updateAeaHiItemMatinst(matinst);

                    //所有需要该材料的事项实例都更改已收状态
                    this.updateInoutinstState(matCorrectDto.getCopyMatinstId());
                }

                if ("1".equals(matCorrectDto.getPaperIsPass())) {
                    AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst = aeaHiItemCorrectRealIninstService.getAeaHiItemCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                    aeaHiItemCorrectRealIninst.setIsPass("1");
                    aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);

                    AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matCorrectDto.getPaperMatinstId());
                    matinst.setRealPaperCount(matinst.getRealPaperCount() == null ? matCorrectDto.getRealPaperCount() : matinst.getRealPaperCount() + matCorrectDto.getRealPaperCount());
                    matinst.setModifier(SecurityContext.getCurrentUserId());
                    matinst.setModifyTime(new Date());
                    aeaHiItemMatinstMapper.updateAeaHiItemMatinst(matinst);

                    //所有需要该材料的事项实例都更改已收状态
                    this.updateInoutinstState(matCorrectDto.getPaperMatinstId());
                }

                //获取补正不符合要求的原件信息
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

                //获取补正不符合要求的复印件信息
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

                //获取补正不符合要求的电子件信息
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
        if (!"5".equals(correctState) && correctDueIninsts.size() > 0) {

            String correctId1 = UUID.randomUUID().toString();
            AeaHiItemCorrect aeaHiItemCorrect1 = new AeaHiItemCorrect();
            aeaHiItemCorrect1.setCorrectId(correctId1);
            aeaHiItemCorrect1.setIteminstId(aeaHiItemCorrect.getIteminstId());
            aeaHiItemCorrect1.setAppinstId(aeaHiItemCorrect.getAppinstId());
            aeaHiItemCorrect1.setApplyinstId(aeaHiItemCorrect.getApplyinstId());
            aeaHiItemCorrect1.setProjInfoId(aeaHiItemCorrect.getProjInfoId());
            aeaHiItemCorrect1.setIsFlowTrigger(aeaHiItemCorrect.getIsFlowTrigger());
            aeaHiItemCorrect1.setCorrectDueDays(aeaHiItemCorrect.getCorrectDueDays());

            //计算出补正截止日期（工作日）
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
                correctDueIninst.setDueIninstId(UUID.randomUUID().toString());
                correctDueIninst.setCorrectId(correctId1);
            }
            aeaHiItemCorrectDueIninstService.batchSaveAeaHiItemCorrectDueIninst(correctDueIninsts);

            if ("1".equals(aeaHiItemCorrect.getIsFlowTrigger())) {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getTaskinstId(), aeaHiItemCorrect.getAppinstId(), ItemStatus.CORRECT_MATERIAL_START.getValue(), null);
            } else {
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), correctMemo, "材料补正", null, ItemStatus.CORRECT_MATERIAL_START.getValue(), null);
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

            AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(aeaHiItemCorrect.getIteminstId());

            //创建回执记录
            matCorrectBaseService.createReceive(iteminst.getItemVerId(), aeaHiItemCorrect.getApplyinstId(), correctId1, "6");
            //物料接收回执
            matCorrectBaseService.createReceive(iteminst.getItemVerId(), aeaHiItemCorrect.getApplyinstId(), correctId1, "12");

        } else {

            HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(aeaLogItemStateHist.getTaskinstId()).singleResult();

            if ("1".equals(aeaHiItemCorrect.getIsFlowTrigger())) {
                //补正正常结束

                String oldState = this.getPreMatCorrectState(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getOldState(), "1");
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getTaskinstId(), aeaHiItemCorrect.getAppinstId(), oldState, null);
                if (taskInstance == null) throw new Exception("找不到节点信息！");
                if (bpmProcessService.isProcessSuspended(taskInstance.getProcessInstanceId())) {
                    //判断流程是否已挂起
                    HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(taskInstance.getProcessInstanceId()).singleResult();
                    //判断当前流程是否结束
                    if (historicProcessInstance.getEndTime() == null) {
                        bpmProcessService.activateProcessInstanceById(taskInstance.getProcessInstanceId());//激活当前流程
                    }
                }
            } else {

                String oldState = this.getPreMatCorrectState(aeaHiItemCorrect.getIteminstId(), aeaLogItemStateHist.getOldState(), "0");
                String option = "8".equals(correctState) ? "材料已补正" : "不予受理";
                aeaHiIteminstService.updateAeaHiIteminstStateAndInsertOpsAeaLogItemStateHist(aeaHiItemCorrect.getIteminstId(), option, "材料补全", null, oldState, null);

            }

            //更新时限计时
            restTimeruleinstCalService.updateTimeruleinstByProcessinstId(taskInstance.getProcessInstanceId());
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

    public List<BscAttFileAndDir> getAttFiles(String attRealIninstId) throws Exception {
        if (StringUtils.isBlank(attRealIninstId)) throw new Exception("补正材料ID为空！");
        return bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_ITEM_CORRECT_REAL_ININST", "REAL_ININST_ID", new String[]{attRealIninstId});
    }

    //获取项目业主
    private void getOwner(AeaHiItemCorrect aeaHiItemCorrect) throws Exception {
        if (StringUtils.isBlank(aeaHiItemCorrect.getLinkmanInfoId())) throw new Exception("找不到申报联系人ID！");
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(aeaHiItemCorrect.getLinkmanInfoId());
        if (aeaLinkmanInfo != null) {
            aeaHiItemCorrect.setLinkman(aeaLinkmanInfo.getLinkmanName());
            aeaHiItemCorrect.setLinkmanPhone(aeaLinkmanInfo.getLinkmanMobilePhone());
        }
        if ("1".equals(aeaHiItemCorrect.getApplySubject())) {
            List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(aeaHiItemCorrect.getApplyinstId(), aeaHiItemCorrect.getProjInfoId());
            if (aeaUnitInfos.size() < 1) {
                aeaHiItemCorrect.setOwner("");
            } else {
                aeaHiItemCorrect.setOwner(aeaUnitInfos.get(0).getApplicant());
            }
        } else {
            if (aeaLinkmanInfo == null) {
                aeaHiItemCorrect.setOwner("");
            } else {
                aeaHiItemCorrect.setOwner(aeaLinkmanInfo.getLinkmanName());
            }

        }
    }

    //更新补正实例已收数量
    private boolean findCorrectRealinst(AeaHiItemCorrectRealIninst realIninst, String type, Long count) throws Exception {

        if (StringUtils.isBlank(realIninst.getDueIninstId())) return false;

        List<AeaHiItemCorrectRealIninst> realIninsts = aeaHiItemCorrectRealIninstService.listAeaHiItemCorrectRealIninst(realIninst);
        if (realIninsts.size() > 0) {
            realIninst = realIninsts.get(0);

            if ("paper".equals(type)) {
                realIninst.setRealPaperCount(count != null ? count : 0l);
            } else if ("copy".equals(type)) {
                realIninst.setRealCopyCount(count != null ? count : 0l);
            } else {
                realIninst.setAttCount(count != null ? count : 0l);
            }
            aeaHiItemCorrectRealIninstService.updateAeaHiItemCorrectRealIninst(realIninst);
            return true;
        }
        return false;
    }

    //批量更新该材料实例的输入输出实例状态
    private void updateInoutinstState(String matinstId) throws Exception {
        AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
        aeaHiItemInoutinst.setMatinstId(matinstId);
        aeaHiItemInoutinst.setIsCollected("0");
        List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);
        if (itemInoutinsts.size() > 0) {
            for (AeaHiItemInoutinst inoutinst : itemInoutinsts) {
                inoutinst.setIsCollected("1");
                inoutinst.setModifier(SecurityContext.getCurrentUserName());
                inoutinst.setModifyTime(new Date());
            }
            aeaHiItemInoutinstMapper.batchUpdateAeaHiItemInoutinst(itemInoutinsts);
        }
    }

    //获取事项实例补正前的状态
    private String getPreMatCorrectState(String iteminstId, String oldState, String isFlowTrigger) throws Exception {

        if (StringUtils.isBlank(iteminstId)) throw new Exception("事项实例ID为空！");

        isFlowTrigger = StringUtils.isBlank(isFlowTrigger) ? "1" : isFlowTrigger;

        if (ItemStatus.CORRECT_MATERIAL_END.getValue().equals(oldState)) {
            //获取该事项实例的所有补正历史状态根据时间降序
            AeaLogItemStateHist itemStateHist = new AeaLogItemStateHist();
            itemStateHist.setIteminstId(iteminstId);
            itemStateHist.setNewState(ItemStatus.CORRECT_MATERIAL_START.getValue());
            itemStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
            itemStateHist.setIsFlowTrigger(isFlowTrigger);
            List<AeaLogItemStateHist> aeaLogItemStateHists = aeaLogItemStateHistService.findAeaLogItemStateHist(itemStateHist);
            for (AeaLogItemStateHist aeaLogItemStateHist : aeaLogItemStateHists) {
                //判断是否有重复补正的记录
                if (ItemStatus.CORRECT_MATERIAL_END.getValue().equals(aeaLogItemStateHist.getOldState()))
                    continue;
                else if (ItemStatus.CORRECT_MATERIAL_START.getValue().equals(aeaLogItemStateHist.getOldState())) {
                    continue;
                } else {
                    oldState = aeaLogItemStateHist.getOldState();
                    break;
                }
            }
        }

        return oldState;
    }
}
