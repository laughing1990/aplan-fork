package com.augurit.aplanmis.rest.userCenter.service.impl;

import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DicConstants;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaHiSmsInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemStateinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStateinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSeriesinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.process.AeaBpmProcessService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.service.receive.ReceiveService;
import com.augurit.aplanmis.rest.common.utils.SessionUtil;
import com.augurit.aplanmis.rest.common.vo.LoginInfoVo;
import com.augurit.aplanmis.rest.userCenter.constant.LoginUserRoleEnum;
import com.augurit.aplanmis.rest.userCenter.service.AeaParStageService;
import com.augurit.aplanmis.rest.userCenter.service.AeaSeriesService;
import com.augurit.aplanmis.rest.userCenter.service.RestApplyService;
import com.augurit.aplanmis.rest.userCenter.vo.ParallelApplyResultVo;
import com.augurit.aplanmis.rest.userCenter.vo.SeriesApplyDataPageVo;
import com.augurit.aplanmis.rest.userCenter.vo.SeriesApplyResultVo;
import com.augurit.aplanmis.rest.userCenter.vo.StageApplyDataPageVo;
import com.augurit.aplanmis.rest.userCenter.vo.StageApplyDataVo;
import com.augurit.aplanmis.rest.userCenter.vo.UserInfoVo;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.beanutils.BeanUtils;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RestApplyServiceImpl implements RestApplyService {

    @Autowired
    AeaProjInfoService aeaProjInfoService;
    @Autowired
    BscDicCodeService bscDicCodeService;
    @Autowired
    AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    ActStoAppinstService actStoAppinstService;
    @Autowired
    TaskService taskService;
    @Autowired
    BpmProcessService bpmProcessService;
    @Autowired
    BpmTaskService bpmTaskService;
    @Autowired
    AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    AeaHiParStateinstService aeaHiParStateinstService;
    @Autowired
    AeaHiItemInoutinstService aeaHiItemInoutinstService;
    @Autowired
    AeaItemBasicService aeaItemBasicService;
    @Autowired
    AeaHiSeriesinstService aeaHiSeriesinstService;
    @Autowired
    AeaBpmProcessService aeaBpmProcessService;
    @Autowired
    AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    AeaHiSmsInfoService aeaHiSmsInfoService;
    @Autowired
    AeaParStageMapper aeaParStageMapper;
    @Autowired
    AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    AeaHiSmsInfoMapper aeaHiSmsInfoMapper;
    @Autowired
    AeaSeriesService aeaSeriesService;
    @Autowired
    AeaParStageService aeaParStageService;
    @Autowired
    ReceiveService receiveService;

    @Override
    public UserInfoVo getApplyObject(HttpServletRequest request) throws Exception {
        UserInfoVo vo = new UserInfoVo();
        //申报主体
        LoginInfoVo user = SessionUtil.getLoginInfo(request);
        if (user != null) {
            if ("1".equals(user.getIsPersonAccount())) {//个人
                AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(user.getUserId());
                vo.setAeaLinkmanInfo(aeaLinkmanInfo == null ? new AeaLinkmanInfo() : aeaLinkmanInfo);
                vo.setRole(LoginUserRoleEnum.PERSONAL.getValue());
            } else if (StringUtils.isNotBlank(user.getUserId())) {//委托人
                AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(user.getUserId());
                vo.setAeaLinkmanInfo(aeaLinkmanInfo == null ? new AeaLinkmanInfo() : aeaLinkmanInfo);
                vo.setAeaUnitList(aeaUnitInfoService.getUnitInfoByLinkmanInfoId(user.getUserId()));
                vo.setRole(LoginUserRoleEnum.HANDLE.getValue());
            } else {//企业
                AeaUnitInfo aeaUnitInfo = aeaUnitInfoService.getAeaUnitInfoByUnitInfoId(user.getUnitId());
                vo.setAeaUnitInfo(aeaUnitInfo == null ? new AeaUnitInfo() : aeaUnitInfo);
                vo.setLinkmanInfoList(aeaLinkmanInfoService.getAeaLinkmanInfoByUnitInfoIdAndIsBindAccount(user.getUnitId(), ""));
                vo.setRole(LoginUserRoleEnum.UNIT.getValue());
            }
        } else {
            vo.setAeaLinkmanInfo(new AeaLinkmanInfo());
            vo.setAeaUnitInfo(new AeaUnitInfo());
            vo.setLinkmanInfoList(new ArrayList<>());
            vo.setAeaUnitList(new ArrayList<>());
        }
        return vo;
    }


    /**
     * 并联申报 --> 发起申报
     *
     * @param stageApplyDataPageVo
     * @return applyinstIds 申请实例集合
     * @throws Exception
     */
    public ParallelApplyResultVo startStageProcess(StageApplyDataPageVo stageApplyDataPageVo) throws Exception {
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageApplyDataPageVo.getStageId());
        Assert.notNull(aeaParStage, "aeaParStage is null");

        String appId = aeaParStage.getAppId();
        String themeVerId = aeaParStage.getThemeVerId();
        StageApplyDataVo stageApplyDataVo = stageApplyDataPageVo.toStageApplyDataVo(appId, themeVerId);
        ParallelApplyResultVo vo = aeaParStageService.stageApply(stageApplyDataVo);
        //updateAeaSmsInfo(stageApplyDataPageVo.getSmsInfoId(), applyinstIds);
        // 保存回执
        //String[] receiptTypes = new String[]{"1", "2"};
        //List<String> applyInstIds = vo.getApplyinstIds();
        //if (applyInstIds==null||applyInstIds.size()==0) return vo;
        //receiveService.saveReceive((String[]) applyInstIds.toArray(), receiptTypes, SecurityContext.getCurrentUserName(), "");
        return vo;
    }


    /**
     * 单项发起申报
     *
     * @param seriesApplyDataPageVo 申报参数
     * @return applyinstId 申请实例ID
     * @throws Exception
     */
    public SeriesApplyResultVo startSeriesFlow(SeriesApplyDataPageVo seriesApplyDataPageVo) throws Exception {
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(seriesApplyDataPageVo.getItemVerId(), SecurityContext.getCurrentOrgId());
        SeriesApplyResultVo seriesApplyResultVo = aeaSeriesService.stageApplay(seriesApplyDataPageVo.toSeriesApplyDataVo(aeaItemBasic.getAppId()));
        BscDicCodeItem itemProperty = bscDicCodeService.getItemByTypeCodeAndItemCode(DicConstants.ITEM_PROPERTY, aeaItemBasic.getItemProperty(), SecurityContext.getCurrentOrgId());
        seriesApplyResultVo.setItemProperty(itemProperty == null ? "" : itemProperty.getItemName());
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(seriesApplyResultVo.getApplyinstId());
        if (aeaHiIteminstList != null && aeaHiIteminstList.size() > 0) {
            seriesApplyResultVo.setItemName(aeaHiIteminstList.get(0).getIteminstName());
            seriesApplyResultVo.setApproveOrgName(aeaHiIteminstList.get(0).getApproveOrgName());
            seriesApplyResultVo.setIteminstCode(aeaHiIteminstList.get(0).getIteminstCode());
        }
        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(seriesApplyDataPageVo.getProjInfoIds()[0]);
        //项目类型
        BscDicCodeItem projType = bscDicCodeService.getItemByTypeCodeAndItemCode(DicConstants.PROJECT_CLASS, aeaProjInfo.getProjType(), SecurityContext.getCurrentOrgId());
        seriesApplyResultVo.setProjName(aeaProjInfo.getProjName());
        seriesApplyResultVo.setProjType(projType == null ? "" : projType.getItemName());
        seriesApplyResultVo.setLocalCode(aeaProjInfo.getLocalCode());
        seriesApplyResultVo.setGcbm(aeaProjInfo.getGcbm());

        updateAeaSmsInfo(seriesApplyDataPageVo.getSmsInfoId(), new String[]{seriesApplyResultVo.getApplyinstId()});
        //保存受理回执，物料回执
        receiveService.saveReceive(new String[]{seriesApplyResultVo.getApplyinstId()}, new String[]{"1", "2"}, SecurityContext.getCurrentUserName(), "");
        return seriesApplyResultVo;
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
                if (StringUtils.isBlank(aeaHiSmsInfo.getApplyinstId()) || !applyinstId.equals(aeaHiSmsInfo.getApplyinstId())) {
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
}
