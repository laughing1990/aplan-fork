package com.augurit.aplanmis.common.service.applyinst.impl;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.day.service.WorkdayHolidayService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserInfoMapper;
import com.augurit.aplanmis.bpm.common.timeCalculate.RestTimeruleinstCalService;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrect;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectDueIninst;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectRealIninst;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectStateHist;
import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaHiParStateinst;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.dto.ApplyinstCorrectinstDto;
import com.augurit.aplanmis.common.dto.MatCorrectDto;
import com.augurit.aplanmis.common.dto.MatCorrectInfoDto;
import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstCorrectMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectDueIninstService;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectRealIninstService;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectService;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectStateHistService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.matCorrect.MatCorrectBaseService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 材料补全实例表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:tiantian</li>
 * <li>创建时间：2019-08-28 17:33:44</li>
 * </ul>
 */
@Service
@Transactional
public class AeaHiApplyinstCorrectServiceImpl implements AeaHiApplyinstCorrectService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiApplyinstCorrectServiceImpl.class);

    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private WorkdayHolidayService workdayHolidayService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private BpmProcessService bpmProcessService;
    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;
    @Autowired
    private AeaHiApplyinstCorrectDueIninstService aeaHiApplyinstCorrectDueIninstService;
    @Autowired
    private AeaHiApplyinstCorrectStateHistService aeaHiApplyinstCorrectStateHistService;
    @Autowired
    private AeaHiApplyinstCorrectRealIninstService aeaHiApplyinstCorrectRealIninstService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private AeaHiApplyinstCorrectMapper aeaHiApplyinstCorrectMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private MatCorrectBaseService matCorrectBaseService;
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private OpuOmUserInfoMapper opuOmUserInfoMapper;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AeaServiceWindowService aeaServiceWindowService;
    @Autowired
    private BscDicRegionMapper bscDicRegionMapper;
    @Autowired
    private RestTimeruleinstCalService restTimeruleinstCalService;
    @Autowired
    private IBscAttService bscAttService;
    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    private String topOrgId;

    public void saveAeaHiApplyinstCorrect(AeaHiApplyinstCorrect aeaHiApplyinstCorrect) throws Exception {
        aeaHiApplyinstCorrectMapper.insertAeaHiApplyinstCorrect(aeaHiApplyinstCorrect);
    }

    public void updateAeaHiApplyinstCorrect(AeaHiApplyinstCorrect aeaHiApplyinstCorrect) throws Exception {
        aeaHiApplyinstCorrectMapper.updateAeaHiApplyinstCorrect(aeaHiApplyinstCorrect);
    }

    public void deleteAeaHiApplyinstCorrectById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiApplyinstCorrectMapper.deleteAeaHiApplyinstCorrect(id);
    }

    public PageInfo<AeaHiApplyinstCorrect> listAeaHiApplyinstCorrect(AeaHiApplyinstCorrect aeaHiApplyinstCorrect, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiApplyinstCorrect> list = aeaHiApplyinstCorrectMapper.listAeaHiApplyinstCorrect(aeaHiApplyinstCorrect);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiApplyinstCorrect>(list);
    }

    public AeaHiApplyinstCorrect getAeaHiApplyinstCorrectById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiApplyinstCorrectMapper.getAeaHiApplyinstCorrectById(id);
    }

    public List<AeaHiApplyinstCorrect> listAeaHiApplyinstCorrect(AeaHiApplyinstCorrect aeaHiApplyinstCorrect) throws Exception {
        List<AeaHiApplyinstCorrect> list = aeaHiApplyinstCorrectMapper.listAeaHiApplyinstCorrect(aeaHiApplyinstCorrect);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public AeaHiApplyinstCorrect getCurrentCorrectinst(String applyinstId) throws Exception {
        if (StringUtils.isBlank(applyinstId)) throw new InvalidParameterException(applyinstId);
        AeaHiApplyinstCorrect aeaHiApplyinstCorrect = new AeaHiApplyinstCorrect();
        aeaHiApplyinstCorrect.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiApplyinstCorrect.setApplyinstId(applyinstId);
        List<AeaHiApplyinstCorrect> applyinstCorrects = aeaHiApplyinstCorrectMapper.listAeaHiApplyinstCorrect(aeaHiApplyinstCorrect);
        return applyinstCorrects.size() > 0 ? applyinstCorrects.get(0) : null;
    }

/**-----------------------------------------------------------------------------------------------------------
 *
 * Author: lucas Chan
 * Date: 2019-9-25 修改
 * Description: 材料补全相关接口
 *
 * -----------------------------------------------------------------------------------------------------------
 */

    /**
     * 创建材料补全实例
     *
     * @throws Exception
     */
    @Override
    public String createMatCorrectinst(ApplyinstCorrectinstDto correctinstDto) throws Exception {

        if (StringUtils.isBlank(correctinstDto.getApplyinstId())) return "申请实例ID为空！";
        if (StringUtils.isBlank(correctinstDto.getIsFlowTrigger())) return "补全触发类型为空！";

        if ("1".equals(correctinstDto.getIsFlowTrigger())) {
            ActStoAppinst appinst = new ActStoAppinst();
            appinst.setMasterRecordId(correctinstDto.getApplyinstId());
            List<ActStoAppinst> appinsts = actStoAppinstService.listActStoAppinst(appinst);
            if (appinsts.size() < 1) throw new Exception("找不到流程实例信息！");
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(appinsts.get(0).getProcinstId()).list();
            if (tasks.size() < 1) throw new Exception("找不到节点信息！");
            correctinstDto.setTaskinstId(tasks.get(0).getId());
        }

        if (correctinstDto.getCorrectDueDays() == null || correctinstDto.getCorrectDueDays() <= 0)
            return "补全期限不符合要求！";
        if (StringUtils.isBlank(correctinstDto.getMatCorrectDtosJson())) return "材料补全清单为空！";

        List<AeaProjInfo> aeaProjInfos = aeaProjInfoService.findApplyProj(correctinstDto.getApplyinstId());
        if (aeaProjInfos.size() > 0)
            correctinstDto.setProjInfoId(aeaProjInfos.get(0).getProjInfoId());
        else
            return "找不到项目信息！";

        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(correctinstDto.getApplyinstId());
        if (ApplyState.IN_THE_SUPPLEMENT.getValue().equals(applyinst.getApplyinstState()) || ApplyState.SUPPLEMENTARY.getValue().equals(applyinst.getApplyinstState()) || ApplyState.OUT_SCOPE.getValue().equals(applyinst.getApplyinstState()) || ApplyState.COMPLETED.getValue().equals(applyinst.getApplyinstState())) {
            return "当前办件处于材料补全中或已办结";
        }

        List<AeaHiApplyinstCorrectDueIninst> correctDueIninsts = new ArrayList();
        String correctId = UUID.randomUUID().toString();
        JSONArray jsonArray = JSONArray.parseArray(correctinstDto.getMatCorrectDtosJson());
        List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
        if (matCorrectDtos.size() < 1) throw new Exception("材料补全清单为空");

        for (MatCorrectDto matCorrectDto : matCorrectDtos) {

            if (matCorrectDto.getCopyCount() != null && matCorrectDto.getCopyCount() > 0) {

                AeaHiApplyinstCorrectDueIninst correctDueIninst = new AeaHiApplyinstCorrectDueIninst();
                correctDueIninst.setApplyinstCorrectId(correctId);
                correctDueIninst.setIsNewMatinst("1");
                correctDueIninst.setMatinstId(matCorrectDto.getCopyMatinstId());
                correctDueIninst.setCopyCount(matCorrectDto.getCopyCount());
                correctDueIninst.setCorrectOpinion(matCorrectDto.getCopyDueIninstOpinion());

                //该复印件材料已经实例化
                if (StringUtils.isNotBlank(matCorrectDto.getCopyMatinstId())) {
                    correctDueIninst.setIsNewMatinst("0");
                } else {
                    String matinstId = UUID.randomUUID().toString();
                    correctDueIninst.setMatinstId(matinstId);
                    //创建材料实例
                    this.creatMatinst(matCorrectDto, matinstId, applyinst, correctinstDto.getProjInfoId());
                }

                correctDueIninsts.add(correctDueIninst);
            }

            if (matCorrectDto.getPaperCount() != null && matCorrectDto.getPaperCount() > 0) {

                AeaHiApplyinstCorrectDueIninst correctDueIninst = new AeaHiApplyinstCorrectDueIninst();
                correctDueIninst.setApplyinstCorrectId(correctId);
                correctDueIninst.setIsNewMatinst("1");
                correctDueIninst.setMatinstId(matCorrectDto.getPaperMatinstId());
                correctDueIninst.setPaperCount(matCorrectDto.getPaperCount());
                correctDueIninst.setCorrectOpinion(matCorrectDto.getPaperDueIninstOpinion());

                //该纸质原件材料已经实例化
                if (StringUtils.isNotBlank(matCorrectDto.getPaperMatinstId())) {
                    correctDueIninst.setIsNewMatinst("0");
                } else {
                    String matinstId = UUID.randomUUID().toString();
                    correctDueIninst.setMatinstId(matinstId);
                    //创建材料实例
                    this.creatMatinst(matCorrectDto, matinstId, applyinst, correctinstDto.getProjInfoId());
                }

                correctDueIninsts.add(correctDueIninst);
            }

            if (StringUtils.isNotBlank(matCorrectDto.getIsNeedAtt()) && "1".equals(matCorrectDto.getIsNeedAtt())) {

                AeaHiApplyinstCorrectDueIninst correctDueIninst = new AeaHiApplyinstCorrectDueIninst();
                correctDueIninst.setApplyinstCorrectId(correctId);
                correctDueIninst.setIsNewMatinst("1");
                correctDueIninst.setIsNeedAtt("1");
                correctDueIninst.setMatinstId(matCorrectDto.getAttMatinstId());
                correctDueIninst.setCorrectOpinion(matCorrectDto.getAttDueIninstOpinion());

                //该电子件材料已经实例化
                if (StringUtils.isNotBlank(matCorrectDto.getAttMatinstId())) {
                    correctDueIninst.setIsNewMatinst("0");
                } else {
                    String matinstId = UUID.randomUUID().toString();
                    correctDueIninst.setMatinstId(matinstId);
                    //创建材料实例
                    this.creatMatinst(matCorrectDto, matinstId, applyinst, correctinstDto.getProjInfoId());
                }

                correctDueIninsts.add(correctDueIninst);
            }
        }

        //实例化材料补全实例
        AeaHiApplyinstCorrect aeaHiApplyinstCorrect = new AeaHiApplyinstCorrect();
        aeaHiApplyinstCorrect.setApplyinstCorrectId(correctId);
        BeanUtils.copyProperties(correctinstDto, aeaHiApplyinstCorrect);

        if ("1".equals(correctinstDto.getIsFlowTrigger())) {
            String appinstId = matCorrectBaseService.getAppinstIdByApplyinstId(correctinstDto.getApplyinstId());
            if (StringUtils.isBlank(appinstId)) return "找不到模板实例ID！";
            aeaHiApplyinstCorrect.setAppinstId(appinstId);
        }

        Date nextDay = workdayHolidayService.nextDay(new Date());
        Date correctDueDay = workdayHolidayService.calWorkdayFrom(nextDay, aeaHiApplyinstCorrect.getCorrectDueDays().intValue(), SecurityContext.getCurrentOrgId());
        aeaHiApplyinstCorrect.setCorrectDueTime(correctDueDay);
        aeaHiApplyinstCorrect.setCreater(SecurityContext.getCurrentUserId());
        aeaHiApplyinstCorrect.setCreateTime(new Date());
        aeaHiApplyinstCorrect.setIsDeleted("0");
        aeaHiApplyinstCorrect.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiApplyinstCorrect.setWindowUserId(SecurityContext.getCurrentUserId());
        aeaHiApplyinstCorrect.setWindowUserName(SecurityContext.getCurrentUser().getUserName());
        OpuOmUserInfo userinfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(SecurityContext.getCurrentUserId());
        aeaHiApplyinstCorrect.setWindowPhone(userinfo == null ? null : userinfo.getUserMobile());
        aeaHiApplyinstCorrectMapper.insertAeaHiApplyinstCorrect(aeaHiApplyinstCorrect);

        //实例化补全清单的材料份数
        aeaHiApplyinstCorrectDueIninstService.batchSaveAeaHiApplyinstCorrectDueIninst(correctDueIninsts);

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        //更新申请实例状态和新增历史记录
        if ("1".equals(correctinstDto.getIsFlowTrigger())) {

            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinstCorrect.getApplyinstId(), correctinstDto.getTaskinstId(), aeaHiApplyinstCorrect.getAppinstId(), ApplyState.IN_THE_SUPPLEMENT.getValue(), opuWinId);

            HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(correctinstDto.getTaskinstId()).singleResult();
            if (taskInstance == null) return "找不到节点信息！";
            if (!bpmProcessService.isProcessSuspended(taskInstance.getProcessInstanceId())) {
                //挂起当前流程
                bpmProcessService.suspendProcessInstanceByIdAndTaskinstId(taskInstance.getProcessInstanceId(), correctinstDto.getTaskinstId());
            }
        } else {
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(aeaHiApplyinstCorrect.getApplyinstId(), correctinstDto.getCorrectDesc(), "材料补全", null, ApplyState.IN_THE_SUPPLEMENT.getValue(), opuWinId);
        }

        //获取该事项实例的所有补全历史状态根据时间降序
        AeaLogApplyStateHist aeaLogApplyStateHist = new AeaLogApplyStateHist();
        aeaLogApplyStateHist.setApplyinstId(aeaHiApplyinstCorrect.getApplyinstId());
        aeaLogApplyStateHist.setNewState(ApplyState.IN_THE_SUPPLEMENT.getValue());
        aeaLogApplyStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaLogApplyStateHist> aeaLogApplyStateHists = aeaLogApplyStateHistService.findAeaLogApplyStateHist(aeaLogApplyStateHist);

        //创建材料补全和事项历史记录关联表
        AeaHiApplyinstCorrectStateHist aeaHiApplyinstCorrectStateHist = new AeaHiApplyinstCorrectStateHist();
        aeaHiApplyinstCorrectStateHist.setApplyinstCorrectStateHistId(UUID.randomUUID().toString());
        aeaHiApplyinstCorrectStateHist.setApplyinstCorrectId(correctId);
        aeaHiApplyinstCorrectStateHist.setApplyinstStateHistId(aeaLogApplyStateHists.get(0).getStateHistId());
        aeaHiApplyinstCorrectStateHistService.saveAeaHiApplyinstCorrectStateHist(aeaHiApplyinstCorrectStateHist);

        List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(correctinstDto.getApplyinstId());
        String itemVerIds = iteminsts.stream().map(AeaHiIteminst::getItemVerId).collect(Collectors.joining(","));
        //创建回执记录
        matCorrectBaseService.createReceive(itemVerIds, correctinstDto.getApplyinstId(), correctId, "7");
        //物料接口回执
        matCorrectBaseService.createReceive(itemVerIds, correctinstDto.getApplyinstId(), correctId, "13");

        return "已创建材料补全实例！";
    }

    //创建材料实例
    private void creatMatinst(MatCorrectDto matCorrectDto, String matinstId, AeaHiApplyinst aeaHiApplyinst, String projInfoId) throws Exception {

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
            List<AeaUnitInfo> unitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(aeaHiApplyinst.getApplyinstId(), projInfoId);
            if (unitInfos.size() < 1) throw new Exception("找不到业务单位信息！");
            String ownnerUnitInfoId = unitInfos.get(0).getUnitInfoId();
            aeaHiItemMatinst.setUnitInfoId(ownnerUnitInfoId);
            aeaHiItemMatinst.setMatinstSource("u");
        } else {
            aeaHiItemMatinst.setLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
            aeaHiItemMatinst.setMatinstSource("l");
        }

        aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);
    }

    /**
     * 材料补全信息
     *
     * @param applyinstCorrectId
     * @return
     * @throws Exception
     */
    @Override
    public ResultForm getMatCorrectInfo(String applyinstCorrectId) throws Exception {

        if (StringUtils.isBlank(applyinstCorrectId)) return new ResultForm(false, "材料补全实例ID为空！");
        AeaHiApplyinstCorrect aeaHiApplyinstCorrect = aeaHiApplyinstCorrectMapper.getAeaHiApplyinstCorrectById(applyinstCorrectId);
        if (aeaHiApplyinstCorrect == null) return new ResultForm(false, "找不到材料补全实例信息！");
        if (StringUtils.isNotBlank(aeaHiApplyinstCorrect.getRegionalism())) {
            BscDicRegion bscDicRegion = bscDicRegionMapper.getBscDicRegionById(aeaHiApplyinstCorrect.getRegionalism());
            if (bscDicRegion != null) aeaHiApplyinstCorrect.setRegionName(bscDicRegion.getRegionName());
        }
        String applyinstId = aeaHiApplyinstCorrect.getApplyinstId();
        String itemNames = "";
        String chargeOrgName = "";
        //回填窗口补件经办人信息
        if (StringUtils.isBlank(aeaHiApplyinstCorrect.getOpsUserId())) {
            aeaHiApplyinstCorrect.setOpsUserId(SecurityContext.getCurrentUserId());
            aeaHiApplyinstCorrect.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
            aeaHiApplyinstCorrect.setOpsTime(new Date());
        }

        Map<String, MatCorrectDto> map = new HashMap();
        String itemVerIds = "";
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        for (AeaHiIteminst aeaHiIteminst : aeaHiIteminstList) {
            itemNames += aeaHiIteminst.getIteminstName() + "，";
            itemVerIds += aeaHiIteminst.getItemVerId()+"，";
            chargeOrgName += aeaHiIteminst.getApproveOrgName() + "，";
        }
        //事项版本ID
        aeaHiApplyinstCorrect.setItemVerIds(StringUtils.isNotBlank(itemVerIds)?itemVerIds.substring(0, itemVerIds.length() - 1):"");
        //获取业主名称
        this.getOwnerOfMatCorrectInfo(aeaHiApplyinstCorrect);

        //需补全的材料清单
        List<AeaHiApplyinstCorrectDueIninst> aeaHipplyinstCorrectDueIninsts = aeaHiApplyinstCorrectDueIninstService.getCorrectDueIninstByApplyinstCorrectId(applyinstCorrectId);
        if (aeaHipplyinstCorrectDueIninsts.size() < 1) return new ResultForm(false, "没有需补全的材料清单");

        for (AeaHiApplyinstCorrectDueIninst applyinstCorrectDueIninst : aeaHipplyinstCorrectDueIninsts) {
            MatCorrectDto matCorrectDto = map.get(applyinstCorrectDueIninst.getMatId()) != null ? map.get(applyinstCorrectDueIninst.getMatId()) : new MatCorrectDto();
            matCorrectDto.setMatProp(applyinstCorrectDueIninst.getMatProp());
            matCorrectDto.setCertId(applyinstCorrectDueIninst.getCertId());
            if (StringUtils.isBlank(matCorrectDto.getMatinstName())) {
                matCorrectDto.setMatId(applyinstCorrectDueIninst.getMatId());
                matCorrectDto.setMatName(applyinstCorrectDueIninst.getMatinstName());
                matCorrectDto.setMatinstName(applyinstCorrectDueIninst.getMatinstName());
                matCorrectDto.setReviewKeyPoints(applyinstCorrectDueIninst.getReviewKeyPoints());
            }

            if (applyinstCorrectDueIninst.getCopyCount() != null && applyinstCorrectDueIninst.getCopyCount() > 0) {
                matCorrectDto.setCopyMatinstId(applyinstCorrectDueIninst.getMatinstId());
                matCorrectDto.setCopyCount(applyinstCorrectDueIninst.getCopyCount());
                matCorrectDto.setCopyDueIninstId(applyinstCorrectDueIninst.getApplyinstDueIninstId());
                matCorrectDto.setCopyDueIninstOpinion(applyinstCorrectDueIninst.getCorrectOpinion());

            }

            if (applyinstCorrectDueIninst.getPaperCount() != null && applyinstCorrectDueIninst.getPaperCount() > 0) {
                matCorrectDto.setPaperMatinstId(applyinstCorrectDueIninst.getMatinstId());
                matCorrectDto.setPaperCount(applyinstCorrectDueIninst.getPaperCount());
                matCorrectDto.setPaperDueIninstId(applyinstCorrectDueIninst.getApplyinstDueIninstId());
                matCorrectDto.setPaperDueIninstOpinion(applyinstCorrectDueIninst.getCorrectOpinion());
            }

            if (StringUtils.isNotBlank(applyinstCorrectDueIninst.getIsNeedAtt()) && "1".equals(applyinstCorrectDueIninst.getIsNeedAtt())) {
                matCorrectDto.setIsNeedAtt("1");
                matCorrectDto.setAttMatinstId(applyinstCorrectDueIninst.getMatinstId());
                matCorrectDto.setAttDueIninstId(applyinstCorrectDueIninst.getApplyinstDueIninstId());
                matCorrectDto.setAttDueIninstOpinion(applyinstCorrectDueIninst.getCorrectOpinion());
            }

            map.put(applyinstCorrectDueIninst.getMatId(), matCorrectDto);
        }

        //已补全的材料清单
        List<AeaHiApplyinstCorrectRealIninst> aeaHiApplyinstCorrectRealIninsts = aeaHiApplyinstCorrectRealIninstService.getCorrectRealIninstByApplyinstCorrectId(applyinstCorrectId);
        for (AeaHiApplyinstCorrectRealIninst applyinstCorrectRealIninst : aeaHiApplyinstCorrectRealIninsts) {

            if (map.get(applyinstCorrectRealIninst.getMatId()) == null) continue;
            MatCorrectDto matCorrectDto = map.get(applyinstCorrectRealIninst.getMatId());
            if (applyinstCorrectRealIninst.getAttCount() != null && applyinstCorrectRealIninst.getAttCount() >= 0l) {
                matCorrectDto.setAttCount(applyinstCorrectRealIninst.getAttCount());
                matCorrectDto.setAttRealIninstId(applyinstCorrectRealIninst.getApplyinstRealIninstId());
            }

            if (applyinstCorrectRealIninst.getRealPaperCount() != null && applyinstCorrectRealIninst.getRealPaperCount() > 0) {
                matCorrectDto.setRealPaperCount(applyinstCorrectRealIninst.getRealPaperCount());
                matCorrectDto.setPaperRealIninstId(applyinstCorrectRealIninst.getApplyinstRealIninstId());
            }

            if (applyinstCorrectRealIninst.getRealCopyCount() != null && applyinstCorrectRealIninst.getRealCopyCount() > 0) {
                matCorrectDto.setRealCopyCount(applyinstCorrectRealIninst.getRealCopyCount());
                matCorrectDto.setCopyRealIninstId(applyinstCorrectRealIninst.getApplyinstRealIninstId());
            }

            map.put(applyinstCorrectRealIninst.getMatId(), matCorrectDto);
        }

        List<MatCorrectDto> matCorrectDtos = new ArrayList();
        matCorrectDtos.addAll(map.values());

        for (MatCorrectDto matCorrectDto : matCorrectDtos) {
            if ("1".equals(matCorrectDto.getIsNeedAtt()) && StringUtils.isBlank(matCorrectDto.getAttRealIninstId())) {
                AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = new AeaHiApplyinstCorrectRealIninst();
                aeaHiApplyinstCorrectRealIninst.setApplyinstCorrectId(applyinstCorrectId);
                aeaHiApplyinstCorrectRealIninst.setApplyinstDueIninstId(matCorrectDto.getAttDueIninstId());
                aeaHiApplyinstCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiApplyinstCorrectRealIninst.setMatinstId(matCorrectDto.getAttMatinstId());
                List<AeaHiApplyinstCorrectRealIninst> realIninsts = aeaHiApplyinstCorrectRealIninstService.listAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                if (realIninsts.size() > 0) {
                    matCorrectDto.setAttRealIninstId(realIninsts.get(0).getApplyinstRealIninstId());
                    matCorrectDto.setAttCount(realIninsts.get(0).getAttCount());
                } else {
                    String realInstId = UUID.randomUUID().toString();
                    aeaHiApplyinstCorrectRealIninst.setApplyinstRealIninstId(realInstId);
                    aeaHiApplyinstCorrectRealIninst.setIsPass("0");
                    aeaHiApplyinstCorrectRealIninst.setAttCount(0l);
                    aeaHiApplyinstCorrectRealIninstService.saveAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);

                    matCorrectDto.setAttRealIninstId(realInstId);
                    matCorrectDto.setAttCount(0l);
                }
            }

            List<BscAttForm> kbList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_MAT", "TEMPLATE_DOC", matCorrectDto.getMatId(), null, topOrgId, null);
            List<BscAttForm> ybList = bscAttService.listAttLinkAndDetailNoPage("AEA_ITEM_MAT", "SAMPLE_DOC", matCorrectDto.getMatId(), null, topOrgId, null);
            kbList.addAll(ybList);
            String ybKbDetailIds = kbList.stream().map(bscAttForm -> bscAttForm.getDetailId()).collect(Collectors.joining(","));
            matCorrectDto.setYbKbDetailIds(ybKbDetailIds);
        }
        //当前补全，根据项目ID查询申报主体
        String projInfoId = aeaHiApplyinstCorrect.getProjInfoId();
        aeaHiApplyinstCorrect.setUnitInfos(aeaUnitInfoService.findOwerUnitProj(projInfoId));
        aeaHiApplyinstCorrect.setIteminstName(itemNames.substring(0, itemNames.length() - 1));
        aeaHiApplyinstCorrect.setChargeOrgName(chargeOrgName.substring(0, chargeOrgName.length() - 1));
        aeaHiApplyinstCorrect.setMatCorrectDtos(matCorrectDtos);
        aeaHiApplyinstCorrect.setAeaHiIteminstList(aeaHiIteminstList);
        return new ContentResultForm(true, aeaHiApplyinstCorrect, "待补全材料清单查询成功！");
    }

    /**
     * 计算事项少交或者未交的材料
     *
     * @param applyinstId
     * @throws Exception
     */
    @Override
    public MatCorrectInfoDto getLackMatsByApplyinstId(String applyinstId) throws Exception {

        if (StringUtils.isBlank(applyinstId)) Assert.notNull(applyinstId, "applyinstId不能为空");
        MatCorrectInfoDto matCorrectInfoDto = new MatCorrectInfoDto();
        List<MatCorrectDto> matCorrectDtos = new ArrayList<>();
        List<MatCorrectDto> submittedMats = new ArrayList<>();
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        if (aeaHiApplyinst != null) {

            matCorrectInfoDto.setApplyinstCode(aeaHiApplyinst.getApplyinstCode());
            //材料定义列表
            List<AeaItemMat> mats = new ArrayList();
            String iteminstNames = "";
            String approveOrgNames = "";
            for (AeaHiIteminst aeaHiIteminst : aeaHiIteminstList) {
                iteminstNames += aeaHiIteminst.getIteminstName() + "，";
                approveOrgNames += aeaHiIteminst.getApproveOrgName() + "，";
            }

            matCorrectInfoDto.setIteminstName(iteminstNames.substring(0, iteminstNames.length() - 1));
            matCorrectInfoDto.setChargeOrgName(approveOrgNames.substring(0, approveOrgNames.length() - 1));

            mats.addAll(aeaItemMatService.getMatListByApplyinstIdContainsMatinst(applyinstId, null));

            Map<String, List> map = matCorrectBaseService.getCorrectMatsAndSubmittedMats(mats);
            matCorrectDtos.addAll(map.get("MatCorrectDtos"));
            submittedMats.addAll(map.get("SubmittedMats"));

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

        matCorrectInfoDto.setSubmittedMats(submittedMats);
        matCorrectInfoDto.setMatCorrectDtos(matCorrectDtos);
        return matCorrectInfoDto;
    }

    /**
     * 保存补全的材料
     *
     * @param matCorrectinstDto
     * @throws Exception
     */
    public ResultForm saveMatCorrectInfo(MatCorrectinstDto matCorrectinstDto) throws Exception {

        if (StringUtils.isBlank(matCorrectinstDto.getCorrectId())) return new ResultForm(false, "材料补全实例ID为空！");
        if (StringUtils.isBlank(matCorrectinstDto.getCorrectState())) return new ResultForm(false, "材料补全实例状态为空！");

        AeaHiApplyinstCorrect aeaHiApplyinstCorrect = aeaHiApplyinstCorrectMapper.getAeaHiApplyinstCorrectById(matCorrectinstDto.getCorrectId());
        if (aeaHiApplyinstCorrect == null) return new ResultForm(false, "找不到材料补全实例信息！");

        String state = matCorrectinstDto.getCorrectState();

        if ("6".equals(state)) {    //待补全

            JSONArray jsonArray = JSONArray.parseArray(matCorrectinstDto.getMatCorrectDtosJson());
            List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
            for (MatCorrectDto matCorrectDto : matCorrectDtos) {

                if (matCorrectDto.getRealPaperCount() != null && matCorrectDto.getRealPaperCount() > 0) {

                    if (StringUtils.isNotBlank(matCorrectDto.getPaperRealIninstId())) {
                        AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                        aeaHiApplyinstCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                        aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                    } else {
                        AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = new AeaHiApplyinstCorrectRealIninst();
                        aeaHiApplyinstCorrectRealIninst.setApplyinstDueIninstId(matCorrectDto.getPaperDueIninstId());
                        aeaHiApplyinstCorrectRealIninst.setApplyinstCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiApplyinstCorrectRealIninst.setMatinstId(matCorrectDto.getPaperMatinstId());

                        if (!this.findCorrectRealinst(aeaHiApplyinstCorrectRealIninst, "paper", matCorrectDto.getRealPaperCount())) {
                            aeaHiApplyinstCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                            aeaHiApplyinstCorrectRealIninst.setApplyinstRealIninstId(UUID.randomUUID().toString());
                            aeaHiApplyinstCorrectRealIninst.setIsPass("0");
                            aeaHiApplyinstCorrectRealIninstService.saveAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                        }
                    }
                }

                if (matCorrectDto.getRealCopyCount() != null && matCorrectDto.getRealCopyCount() > 0) {

                    if (StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {
                        AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                        aeaHiApplyinstCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                        aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                    } else {

                        AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = new AeaHiApplyinstCorrectRealIninst();
                        aeaHiApplyinstCorrectRealIninst.setApplyinstDueIninstId(matCorrectDto.getCopyDueIninstId());
                        aeaHiApplyinstCorrectRealIninst.setApplyinstCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiApplyinstCorrectRealIninst.setMatinstId(matCorrectDto.getCopyMatinstId());

                        if (!this.findCorrectRealinst(aeaHiApplyinstCorrectRealIninst, "copy", matCorrectDto.getRealCopyCount())) {
                            aeaHiApplyinstCorrectRealIninst.setApplyinstRealIninstId(UUID.randomUUID().toString());
                            aeaHiApplyinstCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                            aeaHiApplyinstCorrectRealIninst.setIsPass("0");
                            aeaHiApplyinstCorrectRealIninstService.saveAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                        }
                    }
                }
            }

        } else if ("7".equals(state)) {     //已补全
            aeaHiApplyinstCorrect.setCorrectUserName(SecurityContext.getCurrentUser().getUserName());
            aeaHiApplyinstCorrect.setCorrectEndTime(new Date());

            JSONArray jsonArray = JSONArray.parseArray(matCorrectinstDto.getMatCorrectDtosJson());
            List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
            for (MatCorrectDto matCorrectDto : matCorrectDtos) {

                if (matCorrectDto.getPaperCount() != null && matCorrectDto.getPaperCount() > 0) {
                    if (matCorrectDto.getRealPaperCount() == null || matCorrectDto.getRealPaperCount() < matCorrectDto.getPaperCount())
                        return new ResultForm(false, "缺少纸质材料（" + matCorrectDto.getMatinstName() + "）");

                    if (StringUtils.isNotBlank(matCorrectDto.getPaperRealIninstId())) {

                        AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                        aeaHiApplyinstCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                        aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                    } else {

                        AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = new AeaHiApplyinstCorrectRealIninst();
                        aeaHiApplyinstCorrectRealIninst.setApplyinstDueIninstId(matCorrectDto.getPaperDueIninstId());
                        aeaHiApplyinstCorrectRealIninst.setApplyinstCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiApplyinstCorrectRealIninst.setMatinstId(matCorrectDto.getPaperMatinstId());

                        if (!this.findCorrectRealinst(aeaHiApplyinstCorrectRealIninst, "paper", matCorrectDto.getRealPaperCount())) {
                            aeaHiApplyinstCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                            aeaHiApplyinstCorrectRealIninst.setIsPass("0");
                            aeaHiApplyinstCorrectRealIninst.setApplyinstRealIninstId(UUID.randomUUID().toString());
                            aeaHiApplyinstCorrectRealIninstService.saveAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                        }
                    }
                }

                if (matCorrectDto.getCopyCount() != null && matCorrectDto.getCopyCount() > 0) {
                    if (matCorrectDto.getRealCopyCount() == null || matCorrectDto.getRealCopyCount() < matCorrectDto.getCopyCount())
                        return new ResultForm(false, "缺少复印件（" + matCorrectDto.getMatinstName() + "）");

                    if (StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {

                        AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                        aeaHiApplyinstCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                        aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                    } else {

                        AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = new AeaHiApplyinstCorrectRealIninst();
                        aeaHiApplyinstCorrectRealIninst.setApplyinstDueIninstId(matCorrectDto.getCopyDueIninstId());
                        aeaHiApplyinstCorrectRealIninst.setApplyinstCorrectId(matCorrectinstDto.getCorrectId());
                        aeaHiApplyinstCorrectRealIninst.setMatinstId(matCorrectDto.getCopyMatinstId());

                        if (!this.findCorrectRealinst(aeaHiApplyinstCorrectRealIninst, "copy", matCorrectDto.getRealCopyCount())) {
                            aeaHiApplyinstCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                            aeaHiApplyinstCorrectRealIninst.setApplyinstRealIninstId(UUID.randomUUID().toString());
                            aeaHiApplyinstCorrectRealIninst.setIsPass("0");
                            aeaHiApplyinstCorrectRealIninstService.saveAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                        }
                    }
                }

                if (StringUtils.isNotBlank(matCorrectDto.getIsNeedAtt()) && "1".equals(matCorrectDto.getIsNeedAtt())) {
                    if (matCorrectDto.getAttCount() == null || matCorrectDto.getAttCount() < 1)
                        return new ResultForm(false, "缺少电子件（" + matCorrectDto.getMatinstName() + "）");
                }
            }

            //检查材料是否已全部补全
            if (!this.isCheckCorrect(matCorrectinstDto.getCorrectId())) return new ResultForm(false, "还有材料未补全！");

        } else {    //不予受理
            aeaHiApplyinstCorrect.setCorrectUserName(SecurityContext.getCurrentUser().getUserName());
            aeaHiApplyinstCorrect.setCorrectEndTime(new Date());
        }

        if (StringUtils.isBlank(aeaHiApplyinstCorrect.getOpsUserId()) || !SecurityContext.getCurrentUserId().equals(aeaHiApplyinstCorrect.getOpsUserId())) {
            aeaHiApplyinstCorrect.setOpsUserId(SecurityContext.getCurrentUserId());
            aeaHiApplyinstCorrect.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
        }
        aeaHiApplyinstCorrect.setOpsTime(new Date());
        aeaHiApplyinstCorrect.setCorrectState(state);
        aeaHiApplyinstCorrectMapper.updateAeaHiApplyinstCorrect(aeaHiApplyinstCorrect);

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        AeaLogApplyStateHist aeaLogApplyStateHist = aeaLogApplyStateHistService.getAeaLogApplyStateHistListByApplyinstCorrectId(aeaHiApplyinstCorrect.getApplyinstCorrectId());

        if ("7".equals(state)) {
            //更新事项状态和新增历史记录
            if ("1".equals(aeaLogApplyStateHist.getIsFlowTrigger())) {
                aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinstCorrect.getApplyinstId(), aeaLogApplyStateHist.getTaskinstId(), aeaHiApplyinstCorrect.getAppinstId(), ApplyState.SUPPLEMENTARY.getValue(), opuWinId);
            } else {
                String option = "7".equals(state) ? "材料已补全" : "不予受理";
                aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(aeaHiApplyinstCorrect.getApplyinstId(), option, option, null, ApplyState.SUPPLEMENTARY.getValue(), opuWinId);
            }
        } else if ("5".equals(state)) {
            //更改申请实例状态，并激活流程，节点不往下流转
            this.updateApplyinstState(aeaHiApplyinstCorrect.getApplyinstId(), aeaLogApplyStateHist.getOldState(), aeaLogApplyStateHist.getTaskinstId(), aeaHiApplyinstCorrect.getAppinstId(), aeaHiApplyinstCorrect.getIsFlowTrigger(), state);
        }

        return new ResultForm(true, "已保存材料补全实例！");
    }

    /**
     * 材料补全结束并已确认
     *
     * @param matCorrectinstDto
     * @throws Exception
     */
    public ResultForm saveMatCorrectInfoAndConfirm(MatCorrectinstDto matCorrectinstDto) throws Exception {

        if (StringUtils.isBlank(matCorrectinstDto.getCorrectId())) return new ResultForm(false, "材料补全实例ID为空！");

        AeaHiApplyinstCorrect aeaHiApplyinstCorrect = aeaHiApplyinstCorrectMapper.getAeaHiApplyinstCorrectById(matCorrectinstDto.getCorrectId());
        if (aeaHiApplyinstCorrect == null) return new ResultForm(false, "找不到材料补全实例信息！");

        aeaHiApplyinstCorrect.setCorrectUserName(SecurityContext.getCurrentUser().getUserName());
        aeaHiApplyinstCorrect.setCorrectEndTime(new Date());

        //将该事项的情形实例保存在内存中
        List<AeaHiItemStateinst> stateinsts = new ArrayList();
        List<AeaHiParStateinst> parStateinsts = new ArrayList();

        JSONArray jsonArray = JSONArray.parseArray(matCorrectinstDto.getMatCorrectDtosJson());
        List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
        for (MatCorrectDto matCorrectDto : matCorrectDtos) {

            if (matCorrectDto.getPaperCount() != null && matCorrectDto.getPaperCount() > 0) {
                if (matCorrectDto.getRealPaperCount() == null || matCorrectDto.getRealPaperCount() < matCorrectDto.getPaperCount())
                    return new ResultForm(false, "缺少纸质材料（" + matCorrectDto.getMatinstName() + "）");

                if (StringUtils.isNotBlank(matCorrectDto.getPaperRealIninstId())) {

                    AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                    aeaHiApplyinstCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                    aeaHiApplyinstCorrectRealIninst.setIsPass("1");
                    aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                } else {

                    AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = new AeaHiApplyinstCorrectRealIninst();
                    aeaHiApplyinstCorrectRealIninst.setApplyinstRealIninstId(UUID.randomUUID().toString());
                    aeaHiApplyinstCorrectRealIninst.setApplyinstDueIninstId(matCorrectDto.getPaperDueIninstId());
                    aeaHiApplyinstCorrectRealIninst.setApplyinstCorrectId(matCorrectinstDto.getCorrectId());
                    aeaHiApplyinstCorrectRealIninst.setMatinstId(matCorrectDto.getPaperMatinstId());
                    aeaHiApplyinstCorrectRealIninst.setIsPass("1");
                    aeaHiApplyinstCorrectRealIninst.setRealPaperCount(matCorrectDto.getRealPaperCount());
                    aeaHiApplyinstCorrectRealIninstService.saveAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                }

                //创建事项材料输入输出实例&更新材料实例信息
                this.createItemInoutinstAndUpdateMatinst("paper", matCorrectDto.getMatId(), matCorrectDto.getPaperMatinstId(), aeaHiApplyinstCorrect.getApplyinstId(), matCorrectDto.getRealPaperCount(), null, stateinsts, parStateinsts);
            }

            if (matCorrectDto.getCopyCount() != null && matCorrectDto.getCopyCount() > 0) {
                if (matCorrectDto.getRealCopyCount() == null || matCorrectDto.getRealCopyCount() < matCorrectDto.getCopyCount())
                    return new ResultForm(false, "缺少复印件（" + matCorrectDto.getMatinstName() + "）");

                if (StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {

                    AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                    aeaHiApplyinstCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                    aeaHiApplyinstCorrectRealIninst.setIsPass("1");
                    aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                } else {

                    AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = new AeaHiApplyinstCorrectRealIninst();
                    aeaHiApplyinstCorrectRealIninst.setApplyinstRealIninstId(UUID.randomUUID().toString());
                    aeaHiApplyinstCorrectRealIninst.setApplyinstDueIninstId(matCorrectDto.getCopyDueIninstId());
                    aeaHiApplyinstCorrectRealIninst.setApplyinstCorrectId(matCorrectinstDto.getCorrectId());
                    aeaHiApplyinstCorrectRealIninst.setMatinstId(matCorrectDto.getCopyMatinstId());
                    aeaHiApplyinstCorrectRealIninst.setIsPass("1");
                    aeaHiApplyinstCorrectRealIninst.setRealCopyCount(matCorrectDto.getRealCopyCount());
                    aeaHiApplyinstCorrectRealIninstService.saveAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
                }

                //创建事项材料输入输出实例&更新材料实例信息
                this.createItemInoutinstAndUpdateMatinst("copy", matCorrectDto.getMatId(), matCorrectDto.getCopyMatinstId(), aeaHiApplyinstCorrect.getApplyinstId(), matCorrectDto.getRealCopyCount(), null, stateinsts, parStateinsts);

            }

            if (StringUtils.isNotBlank(matCorrectDto.getIsNeedAtt()) && "1".equals(matCorrectDto.getIsNeedAtt())) {
                if (matCorrectDto.getAttCount() == null || matCorrectDto.getAttCount() < 1)
                    return new ResultForm(false, "缺少电子件（" + matCorrectDto.getMatinstName() + "）");

                AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(matCorrectDto.getAttRealIninstId());
                aeaHiApplyinstCorrectRealIninst.setAttCount(matCorrectDto.getAttCount());
                aeaHiApplyinstCorrectRealIninst.setIsPass("1");
                aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);

                //创建事项材料输入输出实例&更新材料实例信息
                this.createItemInoutinstAndUpdateMatinst("att", matCorrectDto.getMatId(), matCorrectDto.getAttMatinstId(), aeaHiApplyinstCorrect.getApplyinstId(), matCorrectDto.getAttCount(), matCorrectDto.getAttRealIninstId(), stateinsts, parStateinsts);
            }

        }

        //检查材料是否已全部补全
        if (!this.isCheckCorrect(matCorrectinstDto.getCorrectId())) return new ResultForm(false, "还有材料未补全！");

        if (StringUtils.isBlank(aeaHiApplyinstCorrect.getOpsUserId()) || !SecurityContext.getCurrentUserId().equals(aeaHiApplyinstCorrect.getOpsUserId())) {
            aeaHiApplyinstCorrect.setOpsUserId(SecurityContext.getCurrentUserId());
            aeaHiApplyinstCorrect.setOpsUserName(SecurityContext.getCurrentUser().getUserName());
        }
        aeaHiApplyinstCorrect.setOpsTime(new Date());
        aeaHiApplyinstCorrect.setCorrectState("8");
        aeaHiApplyinstCorrectMapper.updateAeaHiApplyinstCorrect(aeaHiApplyinstCorrect);

        AeaLogApplyStateHist aeaLogApplyStateHist = aeaLogApplyStateHistService.getAeaLogApplyStateHistListByApplyinstCorrectId(aeaHiApplyinstCorrect.getApplyinstCorrectId());

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

        //更新事项状态和新增历史记录
        if ("1".equals(aeaLogApplyStateHist.getIsFlowTrigger())) {
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinstCorrect.getApplyinstId(), aeaLogApplyStateHist.getTaskinstId(), aeaHiApplyinstCorrect.getAppinstId(), ApplyState.SUPPLEMENTARY.getValue(), opuWinId);
        } else {
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(aeaHiApplyinstCorrect.getApplyinstId(), "材料补全结束", "材料补全", null, ApplyState.SUPPLEMENTARY.getValue(), opuWinId);
        }

        //补全结束确认后更改申请实例状态
        this.updateApplyinstState(aeaHiApplyinstCorrect.getApplyinstId(), aeaLogApplyStateHist.getOldState(), aeaLogApplyStateHist.getTaskinstId(), aeaHiApplyinstCorrect.getAppinstId(), aeaHiApplyinstCorrect.getIsFlowTrigger(), "8");

        //如果是网上申报，补全确认后停留在当前受理节点。如果是窗口申报，补全确认后流转到下一节点
        if ("win".equals(aeaHiApplyinstCorrect.getApplyinstSource())) {
            this.matCorrectStartProcess(aeaHiApplyinstCorrect.getApplyinstId());

        }

        return new ResultForm(true, "材料补全结束和已确认！");
    }

    /**
     * 材料补全待确认列表
     *
     * @param applyinstCorrectId
     * @throws Exception
     */
    public AeaHiApplyinstCorrect getApplyinstCorrectRealIninst(String applyinstCorrectId) throws Exception {
        if (StringUtils.isBlank(applyinstCorrectId)) throw new Exception("材料补全实例ID为空！");

        AeaHiApplyinstCorrect aeaHiApplyinstCorrect = aeaHiApplyinstCorrectMapper.getAeaHiApplyinstCorrectById(applyinstCorrectId);
        if (aeaHiApplyinstCorrect == null) throw new Exception("找不到材料补全实例信息！");

        List<AeaHiApplyinstCorrectDueIninst> aeaHiApplyinstCorrectDueIninsts = aeaHiApplyinstCorrectDueIninstService.getCorrectDueIninstByApplyinstCorrectId(applyinstCorrectId);
        if (aeaHiApplyinstCorrectDueIninsts.size() < 1) throw new Exception("没有需补全的材料清单");

        this.getOwner(aeaHiApplyinstCorrect); //获取业主名称

        Map<String, MatCorrectDto> map = new HashMap();
        for (AeaHiApplyinstCorrectDueIninst applyinstCorrectDueIninst : aeaHiApplyinstCorrectDueIninsts) {
            MatCorrectDto matCorrectDto = map.get(applyinstCorrectDueIninst.getMatId()) != null ? map.get(applyinstCorrectDueIninst.getMatId()) : new MatCorrectDto();

            if (StringUtils.isBlank(matCorrectDto.getMatinstName())) {
                matCorrectDto.setMatinstName(applyinstCorrectDueIninst.getMatinstName());
                matCorrectDto.setMatId(applyinstCorrectDueIninst.getMatId());
            }

            if (applyinstCorrectDueIninst.getCopyCount() != null && applyinstCorrectDueIninst.getCopyCount() > 0) {
                matCorrectDto.setCopyMatinstId(applyinstCorrectDueIninst.getMatinstId());
                matCorrectDto.setCopyCount(applyinstCorrectDueIninst.getCopyCount());
                matCorrectDto.setCopyDueIninstOpinion(applyinstCorrectDueIninst.getCorrectOpinion());
            }

            if (applyinstCorrectDueIninst.getPaperCount() != null && applyinstCorrectDueIninst.getPaperCount() > 0) {
                matCorrectDto.setPaperMatinstId(applyinstCorrectDueIninst.getMatinstId());
                matCorrectDto.setPaperCount(applyinstCorrectDueIninst.getPaperCount());
                matCorrectDto.setPaperDueIninstOpinion(applyinstCorrectDueIninst.getCorrectOpinion());
            }

            if (StringUtils.isNotBlank(applyinstCorrectDueIninst.getIsNeedAtt()) && "1".equals(applyinstCorrectDueIninst.getIsNeedAtt())) {
                matCorrectDto.setIsNeedAtt("1");
                matCorrectDto.setAttMatinstId(applyinstCorrectDueIninst.getMatinstId());
                matCorrectDto.setAttDueIninstOpinion(applyinstCorrectDueIninst.getCorrectOpinion());
            }

            map.put(applyinstCorrectDueIninst.getMatId(), matCorrectDto);
        }

        List<AeaHiApplyinstCorrectRealIninst> aeaHiApplyinstCorrectRealIninsts = aeaHiApplyinstCorrectRealIninstService.getCorrectRealIninstByApplyinstCorrectId(applyinstCorrectId);
        if (aeaHiApplyinstCorrectRealIninsts.size() < 1) throw new Exception("找不到补全的材料列表！");

        for (AeaHiApplyinstCorrectRealIninst applyinstCorrectRealIninst : aeaHiApplyinstCorrectRealIninsts) {
            if (map.get(applyinstCorrectRealIninst.getMatId()) == null) continue;
            MatCorrectDto matCorrectDto = map.get(applyinstCorrectRealIninst.getMatId());

            if (applyinstCorrectRealIninst.getAttCount() != null && applyinstCorrectRealIninst.getAttCount() > 0) {
                matCorrectDto.setAttCount(applyinstCorrectRealIninst.getAttCount());
                matCorrectDto.setAttRealIninstId(applyinstCorrectRealIninst.getApplyinstRealIninstId());
                matCorrectDto.setAttIsPass(applyinstCorrectRealIninst.getIsPass());
            }

            if (applyinstCorrectRealIninst.getRealPaperCount() != null && applyinstCorrectRealIninst.getRealPaperCount() > 0) {
                matCorrectDto.setRealPaperCount(applyinstCorrectRealIninst.getRealPaperCount());
                matCorrectDto.setPaperRealIninstId(applyinstCorrectRealIninst.getApplyinstRealIninstId());
                matCorrectDto.setPaperIsPass(applyinstCorrectRealIninst.getIsPass());
            }

            if (applyinstCorrectRealIninst.getRealCopyCount() != null && applyinstCorrectRealIninst.getRealCopyCount() > 0) {
                matCorrectDto.setRealCopyCount(applyinstCorrectRealIninst.getRealCopyCount());
                matCorrectDto.setCopyRealIninstId(applyinstCorrectRealIninst.getApplyinstRealIninstId());
                matCorrectDto.setCopyIsPass(applyinstCorrectRealIninst.getIsPass());
            }

            map.put(applyinstCorrectRealIninst.getMatId(), matCorrectDto);
        }

        List<MatCorrectDto> matCorrectDtos = new ArrayList();
        matCorrectDtos.addAll(map.values());
        aeaHiApplyinstCorrect.setMatCorrectDtos(matCorrectDtos);

        return aeaHiApplyinstCorrect;
    }


    /**
     * 材料补全确认
     *
     * @param applyinstCorrectId
     * @param matCorrectDtosJson
     * @throws Exception
     */
    public void matCorrectConfirm(String applyinstCorrectId, String correctState, String correctMemo, String matCorrectDtosJson) throws Exception {

        if (StringUtils.isBlank(applyinstCorrectId)) throw new Exception("材料补全实例id为空！");

        JSONArray jsonArray = JSONArray.parseArray(matCorrectDtosJson);
        List<MatCorrectDto> matCorrectDtos = jsonArray.toJavaList(MatCorrectDto.class);
        if (matCorrectDtos.size() < 1) throw new Exception("材料补全列表为空！");

        AeaHiApplyinstCorrect aeaHiApplyinstCorrect = aeaHiApplyinstCorrectMapper.getAeaHiApplyinstCorrectById(applyinstCorrectId);
        if (aeaHiApplyinstCorrect == null) throw new Exception("找不到材料补全实例信息！");

        List<AeaHiApplyinstCorrectDueIninst> applyinstCorrectDueIninsts = new ArrayList();

        //将该事项的情形实例保存在内存中
        List<AeaHiItemStateinst> stateinsts = new ArrayList();
        List<AeaHiParStateinst> parStateinsts = new ArrayList();

        String applyinstCorrectId1 = UUID.randomUUID().toString();

        if (!"5".equals(correctState)) {
            for (MatCorrectDto matCorrectDto : matCorrectDtos) {

                if ("1".equals(matCorrectDto.getAttIsPass())) {
                    AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(matCorrectDto.getAttRealIninstId());
                    aeaHiApplyinstCorrectRealIninst.setIsPass("1");
                    aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);

                    //创建事项材料输入输出实例&更新材料实例信息
                    this.createItemInoutinstAndUpdateMatinst("att", matCorrectDto.getMatId(), matCorrectDto.getAttMatinstId(), aeaHiApplyinstCorrect.getApplyinstId(), matCorrectDto.getAttCount(), matCorrectDto.getAttRealIninstId(), stateinsts, parStateinsts);
                }

                if ("1".equals(matCorrectDto.getCopyIsPass())) {
                    AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(matCorrectDto.getCopyRealIninstId());
                    aeaHiApplyinstCorrectRealIninst.setIsPass("1");
                    aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);

                    //创建事项材料输入输出实例&更新材料实例信息
                    this.createItemInoutinstAndUpdateMatinst("copy", matCorrectDto.getMatId(), matCorrectDto.getCopyMatinstId(), aeaHiApplyinstCorrect.getApplyinstId(), matCorrectDto.getRealCopyCount(), null, stateinsts, parStateinsts);
                }

                if ("1".equals(matCorrectDto.getPaperIsPass())) {
                    AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(matCorrectDto.getPaperRealIninstId());
                    aeaHiApplyinstCorrectRealIninst.setIsPass("1");
                    aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);

                    //创建事项材料输入输出实例&更新材料实例信息
                    this.createItemInoutinstAndUpdateMatinst("paper", matCorrectDto.getMatId(), matCorrectDto.getPaperMatinstId(), aeaHiApplyinstCorrect.getApplyinstId(), matCorrectDto.getRealPaperCount(), null, stateinsts, parStateinsts);

                }

                if ("0".equals(matCorrectDto.getPaperIsPass()) && StringUtils.isNotBlank(matCorrectDto.getPaperRealIninstId())) {

                    AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst = new AeaHiApplyinstCorrectDueIninst();
                    aeaHiApplyinstCorrectDueIninst.setApplyinstCorrectId(applyinstCorrectId1);
                    aeaHiApplyinstCorrectDueIninst.setCorrectOpinion(matCorrectDto.getPaperDueIninstOpinion());
                    aeaHiApplyinstCorrectDueIninst.setPaperCount(matCorrectDto.getPaperCount());
                    aeaHiApplyinstCorrectDueIninst.setMatinstId(matCorrectDto.getPaperMatinstId());
                    aeaHiApplyinstCorrectDueIninst.setIsNewMatinst("0");
                    applyinstCorrectDueIninsts.add(aeaHiApplyinstCorrectDueIninst);
                }

                if ("0".equals(matCorrectDto.getCopyIsPass()) && StringUtils.isNotBlank(matCorrectDto.getCopyRealIninstId())) {

                    AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst = new AeaHiApplyinstCorrectDueIninst();
                    aeaHiApplyinstCorrectDueIninst.setApplyinstCorrectId(applyinstCorrectId1);
                    aeaHiApplyinstCorrectDueIninst.setCorrectOpinion(matCorrectDto.getCopyDueIninstOpinion());
                    aeaHiApplyinstCorrectDueIninst.setCopyCount(matCorrectDto.getCopyCount());
                    aeaHiApplyinstCorrectDueIninst.setMatinstId(matCorrectDto.getCopyMatinstId());
                    aeaHiApplyinstCorrectDueIninst.setIsNewMatinst("0");
                    applyinstCorrectDueIninsts.add(aeaHiApplyinstCorrectDueIninst);
                }

                if ("0".equals(matCorrectDto.getAttIsPass()) && StringUtils.isNotBlank(matCorrectDto.getAttRealIninstId())) {

                    AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst = new AeaHiApplyinstCorrectDueIninst();
                    aeaHiApplyinstCorrectDueIninst.setApplyinstCorrectId(applyinstCorrectId1);
                    aeaHiApplyinstCorrectDueIninst.setCorrectOpinion(matCorrectDto.getAttDueIninstOpinion());
                    aeaHiApplyinstCorrectDueIninst.setMatinstId(matCorrectDto.getAttMatinstId());
                    aeaHiApplyinstCorrectDueIninst.setIsNeedAtt("1");
                    aeaHiApplyinstCorrectDueIninst.setIsNewMatinst("0");
                    applyinstCorrectDueIninsts.add(aeaHiApplyinstCorrectDueIninst);
                }

            }
        }

        AeaLogApplyStateHist aeaLogApplyStateHist = aeaLogApplyStateHistService.getAeaLogApplyStateHistListByApplyinstCorrectId(aeaHiApplyinstCorrect.getApplyinstCorrectId());

        //继续补全
        if (!"5".equals(correctState) && applyinstCorrectDueIninsts.size() > 0) {

            AeaHiApplyinstCorrect aeaHiApplyinstCorrect1 = new AeaHiApplyinstCorrect();
            aeaHiApplyinstCorrect1.setApplyinstCorrectId(applyinstCorrectId1);
            aeaHiApplyinstCorrect1.setAppinstId(aeaHiApplyinstCorrect.getAppinstId());
            aeaHiApplyinstCorrect1.setApplyinstId(aeaHiApplyinstCorrect.getApplyinstId());
            aeaHiApplyinstCorrect1.setProjInfoId(aeaHiApplyinstCorrect.getProjInfoId());
            aeaHiApplyinstCorrect1.setIsFlowTrigger(aeaHiApplyinstCorrect.getIsFlowTrigger());
            aeaHiApplyinstCorrect1.setCorrectDueDays(aeaHiApplyinstCorrect.getCorrectDueDays());

            Date nextDay = workdayHolidayService.nextDay(new Date());
            Date correctDueDay = workdayHolidayService.calWorkdayFrom(nextDay, aeaHiApplyinstCorrect.getCorrectDueDays().intValue(), SecurityContext.getCurrentOrgId());
            aeaHiApplyinstCorrect1.setCorrectDueTime(correctDueDay);
            aeaHiApplyinstCorrect1.setCorrectState("6");
            aeaHiApplyinstCorrect1.setCorrectMemo(correctMemo);
            aeaHiApplyinstCorrect1.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiApplyinstCorrect1.setCreater(SecurityContext.getCurrentUserId());
            aeaHiApplyinstCorrect1.setCreateTime(new Date());
            aeaHiApplyinstCorrect1.setIsDeleted("0");

            aeaHiApplyinstCorrect1.setWindowUserId(SecurityContext.getCurrentUserId());
            aeaHiApplyinstCorrect1.setWindowUserName(SecurityContext.getCurrentUser().getUserName());
            OpuOmUserInfo userinfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(SecurityContext.getCurrentUserId());
            aeaHiApplyinstCorrect1.setWindowPhone(userinfo == null ? null : userinfo.getUserMobile());
            aeaHiApplyinstCorrectMapper.insertAeaHiApplyinstCorrect(aeaHiApplyinstCorrect1);

            aeaHiApplyinstCorrectDueIninstService.batchSaveAeaHiApplyinstCorrectDueIninst(applyinstCorrectDueIninsts);

            String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

            //更新申请实例状态和新增历史记录
            String applyinstId = aeaHiApplyinstCorrect.getApplyinstId();
            if ("1".equals(aeaHiApplyinstCorrect.getIsFlowTrigger())) {
                aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, aeaLogApplyStateHist.getTaskinstId(), aeaHiApplyinstCorrect.getAppinstId(), ApplyState.IN_THE_SUPPLEMENT.getValue(), opuWinId);
            } else {
                aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(applyinstId, correctMemo, "材料补全", null, ApplyState.IN_THE_SUPPLEMENT.getValue(), opuWinId);
            }

            //获取该事项实例的所有补全历史状态根据时间降序
            AeaLogApplyStateHist aeaLogApplyStateHist1 = new AeaLogApplyStateHist();
            aeaLogApplyStateHist1.setApplyinstId(applyinstId);
            aeaLogApplyStateHist1.setNewState(ApplyState.IN_THE_SUPPLEMENT.getValue());
            aeaLogApplyStateHist1.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaLogApplyStateHist> aeaLogApplyStateHists = aeaLogApplyStateHistService.findAeaLogApplyStateHist(aeaLogApplyStateHist1);

            //创建材料补全和事项历史记录关联表
            AeaHiApplyinstCorrectStateHist aeaHiApplyinstCorrectStateHist = new AeaHiApplyinstCorrectStateHist();
            aeaHiApplyinstCorrectStateHist.setApplyinstCorrectStateHistId(UUID.randomUUID().toString());
            aeaHiApplyinstCorrectStateHist.setApplyinstCorrectId(applyinstCorrectId1);
            aeaHiApplyinstCorrectStateHist.setApplyinstStateHistId(aeaLogApplyStateHists.get(0).getStateHistId());
            aeaHiApplyinstCorrectStateHistService.saveAeaHiApplyinstCorrectStateHist(aeaHiApplyinstCorrectStateHist);

            List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            String itemVerIds = iteminsts.stream().map(AeaHiIteminst::getItemVerId).collect(Collectors.joining(","));
            //创建回执记录
            matCorrectBaseService.createReceive(itemVerIds, applyinstId, applyinstCorrectId1, "7");
            //物料接口回执
            matCorrectBaseService.createReceive(itemVerIds, applyinstId, applyinstCorrectId1, "13");

        } else {
            //补全结束确认后更改申请实例状态
            this.updateApplyinstState(aeaHiApplyinstCorrect.getApplyinstId(), aeaLogApplyStateHist.getOldState(), aeaLogApplyStateHist.getTaskinstId(), aeaHiApplyinstCorrect.getAppinstId(), aeaHiApplyinstCorrect.getIsFlowTrigger(), correctState);

            //如果是网上申报，补全确认后停留在当前受理节点。如果是窗口申报，补全确认后流转到下一节点
            if ("win".equals(aeaHiApplyinstCorrect.getApplyinstSource())) {
                this.matCorrectStartProcess(aeaHiApplyinstCorrect.getApplyinstId());
            }
        }

        aeaHiApplyinstCorrect.setCorrectState(correctState);
        aeaHiApplyinstCorrectMapper.updateAeaHiApplyinstCorrect(aeaHiApplyinstCorrect);
    }

    private void getOwner(AeaHiApplyinstCorrect aeaHiApplyinstCorrect) throws Exception {

        if ("1".equals(aeaHiApplyinstCorrect.getApplySubject())) {
            String applyinstId = aeaHiApplyinstCorrect.getApplyinstId();
            List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(applyinstId, aeaHiApplyinstCorrect.getProjInfoId());
            if (aeaUnitInfos.size() < 1) {
                aeaHiApplyinstCorrect.setOwner("");
            } else {
                aeaHiApplyinstCorrect.setOwner(aeaUnitInfos.get(0).getApplicant());
            }
        } else {
            if (StringUtils.isBlank(aeaHiApplyinstCorrect.getOpsUserId())) throw new Exception("找不到经办人ID！");
            OpuOmUserInfo userInfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(aeaHiApplyinstCorrect.getOpsUserId());
            if (userInfo == null) {
                aeaHiApplyinstCorrect.setOwner("");
            } else {
                aeaHiApplyinstCorrect.setOwner(userInfo.getUserName());
            }
        }
    }


    private void getOwnerOfMatCorrectInfo(AeaHiApplyinstCorrect aeaHiApplyinstCorrect) throws Exception {

        if ("1".equals(aeaHiApplyinstCorrect.getApplySubject())) {
            String applyinstId = aeaHiApplyinstCorrect.getApplyinstId();
            List<AeaUnitInfo> aeaUnitInfos = aeaUnitInfoService.findApplyOwnerUnitProj(applyinstId, aeaHiApplyinstCorrect.getProjInfoId());
            if (aeaUnitInfos.size() < 1) throw new Exception("找不到业务单位信息！");
            aeaHiApplyinstCorrect.setOwner(aeaUnitInfos.get(0).getApplicant());
        } else {
            aeaHiApplyinstCorrect.setOwner(aeaHiApplyinstCorrect.getOpsUserName());
        }
    }


    //材料删除
    public void delelteAttFile(String detailIds, String attRealIninstId) throws Exception {
        AeaHiApplyinstCorrectRealIninst realIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(attRealIninstId);
        if (realIninst != null) {
            fileUtilsService.deleteAttachments(detailIds.split(","));
            List<BscAttFileAndDir> matAttDetail = bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_APPLYINST_CORRECT_REAL_ININST", "REAL_ININST_ID", new String[]{attRealIninstId});
            realIninst.setAttCount(Long.valueOf(matAttDetail.size()));
            realIninst.setModifyTime(new Date());
            realIninst.setModifier(SecurityContext.getCurrentUserName());
            aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(realIninst);
        }
    }

    public void uploadFile(String attRealIninstId, HttpServletRequest request) throws Exception {
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles("file");
        if (files.size() > 0) {
            AeaHiApplyinstCorrectRealIninst realIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(attRealIninstId);
            if (realIninst == null) throw new Exception("材料实例不存在！");
            fileUtilsService.uploadAttachments("AEA_HI_APPLYINST_CORRECT_REAL_ININST", "REAL_ININST_ID", attRealIninstId, files);
            List<BscAttFileAndDir> matAttDetail = bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_APPLYINST_CORRECT_REAL_ININST", "REAL_ININST_ID", new String[]{attRealIninstId});
            realIninst.setAttCount(Long.valueOf(matAttDetail.size()));
            realIninst.setModifyTime(new Date());
            realIninst.setModifier(SecurityContext.getCurrentUserName());
            aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(realIninst);
        }
    }

    public List<BscAttFileAndDir> getAttFiles(String attRealIninstId) throws Exception {
        if (StringUtils.isBlank(attRealIninstId)) throw new Exception("补全材料ID为空！");
        return bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_APPLYINST_CORRECT_REAL_ININST", "REAL_ININST_ID", new String[]{attRealIninstId});
    }

    //创建输入输出实例
    private void createInoutinst(String matId, String matinstId, String applyinstId, List<AeaHiItemStateinst> stateinsts, List<AeaHiParStateinst> parStateinsts) throws Exception {

        if (StringUtils.isBlank(matinstId)) throw new Exception("找不到材料实例Id！");
        if (StringUtils.isBlank(applyinstId)) throw new Exception("找不到申报实例Id！");


        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);

        boolean handWay = false;

        if ("0".equals(aeaHiApplyinst.getIsSeriesApprove())) {

            AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinstId);

            if ("1".equals(aeaHiParStageinst.getHandWay())) {
                handWay = true;
                //创建输入输出实例
                matCorrectBaseService.createStageIteminoutinst(matId, matinstId, null, null, aeaHiParStageinst.getStageId(), applyinstId, parStateinsts, aeaHiParStageinst.getIsNeedState(), "1");
            }
        }

        if ("1".equals(aeaHiApplyinst.getIsSeriesApprove()) || !handWay) {

            List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);

            //创建输入输出实例
            AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
            aeaHiItemInoutinst.setMatinstId(matinstId);

            for (AeaHiIteminst iteminst : iteminstList) {

                aeaHiItemInoutinst.setIteminstId(iteminst.getIteminstId());
                List<AeaHiItemInoutinst> itemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);
                //判断是否已经发起过“补正结束并确认”请求，并实例化输入输出实例
                if (itemInoutinsts.size() > 0) {

                    for (AeaHiItemInoutinst inoutinst : itemInoutinsts) {

                        if (StringUtils.isBlank(inoutinst.getIsCollected()) && "0".equals(inoutinst.getIsCollected())) {
                            inoutinst.setIsCollected("1");
                            inoutinst.setModifier(SecurityContext.getCurrentUserName());
                            inoutinst.setModifyTime(new Date());
                            aeaHiItemInoutinstMapper.updateAeaHiItemInoutinst(inoutinst);
                        }
                    }
                    continue;
                }


                String inoutId = matCorrectBaseService.getInoutIdByItemVerIdAndMatId(matId, iteminst.getItemVerId(), applyinstId, stateinsts);
                if (StringUtils.isBlank(inoutId)) continue;

                aeaHiItemInoutinst.setInoutinstId(UUID.randomUUID().toString());
                aeaHiItemInoutinst.setItemInoutId(inoutId);
                aeaHiItemInoutinst.setIsCollected("1");
                aeaHiItemInoutinst.setCreateTime(new Date());
                aeaHiItemInoutinst.setCreater(SecurityContext.getCurrentUserName());
                aeaHiItemInoutinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiItemInoutinstMapper.insertAeaHiItemInoutinst(aeaHiItemInoutinst);

            }
        }
    }

    //补全结束确认后更改申请实例状态
    private void updateApplyinstState(String applyinstId, String oldState, String taskId, String appinstId, String isFlowTrigger, String correctState) throws Exception {

        if (StringUtils.isBlank(applyinstId) || StringUtils.isBlank(oldState) || StringUtils.isBlank(isFlowTrigger))
            throw new Exception("缺少参数!");

        if (ApplyState.SUPPLEMENTARY.getValue().equals(oldState)) {
            //获取该事项实例的所有补全历史状态根据时间降序
            AeaLogApplyStateHist applyStateHist = new AeaLogApplyStateHist();
            applyStateHist.setApplyinstId(applyinstId);
            applyStateHist.setNewState(ApplyState.IN_THE_SUPPLEMENT.getValue());
            applyStateHist.setRootOrgId(SecurityContext.getCurrentOrgId());
            applyStateHist.setIsFlowTrigger(isFlowTrigger);
            List<AeaLogApplyStateHist> aeaLogApplyStateHists = aeaLogApplyStateHistService.findAeaLogApplyStateHist(applyStateHist);
            for (AeaLogApplyStateHist AeaLogApplyStateHist : aeaLogApplyStateHists) {
                //判断是否有重复补全的记录
                if (ApplyState.SUPPLEMENTARY.getValue().equals(AeaLogApplyStateHist.getOldState()))
                    continue;
                else {
                    if (ApplyState.IN_THE_SUPPLEMENT.getValue().equals(AeaLogApplyStateHist.getOldState())) {
                        continue;
                    } else {
                        oldState = AeaLogApplyStateHist.getOldState();
                        break;
                    }

                }
            }
        }

        String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();
        HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();

        if ("1".equals(isFlowTrigger)) {
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, taskId, appinstId, oldState, opuWinId);
            if (taskInstance == null) throw new Exception("找不到节点信息！");
            if (bpmProcessService.isProcessSuspended(taskInstance.getProcessInstanceId()))//判断流程是否已挂起
                bpmProcessService.activateProcessInstanceById(taskInstance.getProcessInstanceId());//激活当前流程
        } else {
            String option = "8".equals(correctState) ? "补全材料已确认" : "不予受理";
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(applyinstId, option, "材料补全", null, oldState, opuWinId);
        }

        //更新时限计时
        restTimeruleinstCalService.updateTimeruleinstByProcessinstId(taskInstance.getProcessInstanceId());
    }

    //创建事项材料输入输出实例&更新材料实例信息
    private void createItemInoutinstAndUpdateMatinst(String type, String matId, String matinstId, String applyinstId, Long count, String attRealIninstId, List<AeaHiItemStateinst> stateinsts, List<AeaHiParStateinst> parStateinsts) throws Exception {

        if (StringUtils.isBlank(type) || StringUtils.isBlank(matId) || StringUtils.isBlank(matinstId) || StringUtils.isBlank(applyinstId) || count == null)
            throw new Exception("缺少参数！");

        AeaHiItemMatinst matinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);

        if ("att".equals(type)) {
            matinst.setAttCount(matinst.getAttCount() == null ? count : matinst.getAttCount() + count);
            //修改附件的业务ID
            matCorrectBaseService.updateAttFileBusTableName(matinstId, "AEA_HI_APPLYINST_CORRECT_REAL_ININST", "REAL_ININST_ID", attRealIninstId);
        } else if ("copy".equals(type)) {
            matinst.setRealCopyCount(matinst.getRealCopyCount() == null ? count : matinst.getRealCopyCount() + count);
        } else {
            matinst.setRealPaperCount(matinst.getRealPaperCount() == null ? count : matinst.getRealPaperCount() + count);
        }

        matinst.setModifier(SecurityContext.getCurrentUserId());
        matinst.setModifyTime(new Date());
        aeaHiItemMatinstMapper.updateAeaHiItemMatinst(matinst);

        //创建事项材料输入输出实例
        this.createInoutinst(matId, matinstId, applyinstId, stateinsts, parStateinsts);

    }

    //判断材料补全是否已收齐
    private boolean isCheckCorrect(String applyinstCorrectId) throws Exception {

        if (StringUtils.isBlank(applyinstCorrectId)) throw new Exception("材料补全实例ID为空！");
        //需要补全的材料清单
        List<AeaHiApplyinstCorrectDueIninst> aeaHiApplyinstCorrectDueIninsts = aeaHiApplyinstCorrectDueIninstService.getCorrectDueIninstByApplyinstCorrectId(applyinstCorrectId);
        if (aeaHiApplyinstCorrectDueIninsts.size() < 1) return true;

        //已补全的材料清单
        List<AeaHiApplyinstCorrectRealIninst> aeaHiApplyinstCorrectRealIninsts = aeaHiApplyinstCorrectRealIninstService.getCorrectRealIninstByApplyinstCorrectId(applyinstCorrectId);
        if (aeaHiApplyinstCorrectRealIninsts.size() < 1) return false;

        Set<String> correctedList = new TreeSet();
        for (AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst : aeaHiApplyinstCorrectRealIninsts) {

            for (AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst : aeaHiApplyinstCorrectDueIninsts) {

                if (aeaHiApplyinstCorrectDueIninst.getApplyinstDueIninstId().equals(aeaHiApplyinstCorrectRealIninst.getApplyinstDueIninstId())) {

                    if (aeaHiApplyinstCorrectDueIninst.getPaperCount() != null && aeaHiApplyinstCorrectDueIninst.getPaperCount() > 0 && aeaHiApplyinstCorrectRealIninst.getRealPaperCount() != null && aeaHiApplyinstCorrectRealIninst.getRealPaperCount() >= aeaHiApplyinstCorrectDueIninst.getPaperCount()) {
                        correctedList.add(aeaHiApplyinstCorrectRealIninst.getApplyinstRealIninstId());
                        break;
                    }

                    if (aeaHiApplyinstCorrectDueIninst.getCopyCount() != null && aeaHiApplyinstCorrectDueIninst.getCopyCount() > 0 && aeaHiApplyinstCorrectRealIninst.getRealCopyCount() != null && aeaHiApplyinstCorrectRealIninst.getRealCopyCount() >= aeaHiApplyinstCorrectDueIninst.getCopyCount()) {
                        correctedList.add(aeaHiApplyinstCorrectRealIninst.getApplyinstRealIninstId());
                        break;
                    }

                    if (StringUtils.isNotBlank(aeaHiApplyinstCorrectDueIninst.getIsNeedAtt()) && "1".equals(aeaHiApplyinstCorrectDueIninst.getIsNeedAtt()) && aeaHiApplyinstCorrectRealIninst.getAttCount() != null && aeaHiApplyinstCorrectRealIninst.getAttCount() > 0) {
                        correctedList.add(aeaHiApplyinstCorrectRealIninst.getApplyinstRealIninstId());
                        break;
                    }
                }
            }
        }

        if (correctedList.size() == aeaHiApplyinstCorrectDueIninsts.size())
            return true;
        else
            return false;
    }

    private boolean findCorrectRealinst(AeaHiApplyinstCorrectRealIninst realIninst, String type, Long count) throws Exception {

        if (StringUtils.isBlank(realIninst.getApplyinstDueIninstId())) return false;

        List<AeaHiApplyinstCorrectRealIninst> realIninsts = aeaHiApplyinstCorrectRealIninstService.listAeaHiApplyinstCorrectRealIninst(realIninst);
        if (realIninsts.size() > 0) {
            realIninst = realIninsts.get(0);
            if ("paper".equals(type)) {
                realIninst.setRealPaperCount(count == null ? 0l : count);
            } else if ("copy".equals(type)) {
                realIninst.setRealCopyCount(count != null ? count : 0l);
            } else {
                realIninst.setAttCount(count != null ? count : 0l);
            }
            aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(realIninst);
            return true;
        }
        return false;
    }

    private void matCorrectStartProcess(String applyinstId) throws Exception {
        ActStoAppinst param = new ActStoAppinst();
        param.setMasterRecordId(applyinstId);
        List<ActStoAppinst> actStoAppinsts = actStoAppinstService.listActStoAppinst(param);
        if (actStoAppinsts != null && actStoAppinsts.size() == 1) {
            // 获取流程实例
            String procinstId = actStoAppinsts.get(0).getProcinstId();
            // 找到任务id
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(procinstId).list();
            if (tasks != null) {
                // 推动流程流转，这里固定窗口流程的部门审批节点的id必须为“bumenshenpi”，否则报错
                taskService.complete(tasks.get(0).getId(), new String[]{"bumenshenpi"}, (Map) null);

                List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);

                //更新事项状态
                for (AeaHiIteminst iteminst : aeaHiIteminsts) {
                    aeaHiIteminstService.updateAeaHiIteminstStateAndInsertTriggerAeaLogItemStateHist(iteminst.getIteminstId(), tasks.get(0).getId(), actStoAppinsts.get(0).getAppinstId(), ItemStatus.ACCEPT_DEAL.getValue(), SecurityContext.getCurrentOrgId());
                }

                String opuWinId = aeaServiceWindowService.getCurrentUserWindow() == null ? "" : aeaServiceWindowService.getCurrentUserWindow().getWindowId();

                //更新申请状态
                aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(applyinstId, tasks.get(0).getId(), actStoAppinsts.get(0).getAppinstId(), ApplyState.ACCEPT_DEAL.getValue(), opuWinId);
            }
        }
    }
}

