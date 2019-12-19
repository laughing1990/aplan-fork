package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AeaHiApplyinstConstants;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaLogApplyStateHist;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.ApplyAcceptAplanmisEvent;
import com.augurit.aplanmis.common.event.vo.ApplyEventVo;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.service.dic.ApplyinstCodeService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class AeaHiApplyinstServiceImpl implements AeaHiApplyinstService {
    private static Logger logger = LoggerFactory.getLogger(AeaHiApplyinstServiceImpl.class);

    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;

    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;

    @Autowired
    private AplanmisEventPublisher publisher;

    @Autowired
    private ApplyinstCodeService applyinstCodeService;

    @Override
    public AeaHiApplyinst createAeaHiApplyinst(String applySource, String applySubject, String linkmanInfoId, String isSeriesApprove, String branchOrgMap, String applyState,String isTemporarySubmit,String parentApplyinstId) throws Exception {
        AeaHiApplyinst aeaHiApplyinst = new AeaHiApplyinst();

        if (applySource == null)
            throw new InvalidParameterException("申报来源applySource参数不能为空！");

        if (applySubject == null)
            throw new InvalidParameterException("申报主体applySubject参数不能为空！");

        if (linkmanInfoId == null)
            throw new InvalidParameterException("联系人IDlinkmanInfoId参数不能为空！");

        if (isSeriesApprove == null)
            throw new InvalidParameterException("是否串联isSeriesApprove参数不能为空！");

        if (applyState == null)
            throw new InvalidParameterException("事项状态参数不能为空！");

        aeaHiApplyinst.setApplyinstId(UUID.randomUUID().toString());
        aeaHiApplyinst.setApplyinstCode(applyinstCodeService.getApplyinstCodeCurrentDay());
        aeaHiApplyinst.setIsSeriesApprove(isSeriesApprove);
        aeaHiApplyinst.setApplyinstSource(applySource);
        aeaHiApplyinst.setApplyinstState(applyState);//申报状态
        aeaHiApplyinst.setApplySubject(applySubject);//申报主体
        aeaHiApplyinst.setLinkmanInfoId(linkmanInfoId);
        aeaHiApplyinst.setIsDeleted(AeaHiApplyinstConstants.IS_DELETED_NO);
        if (StringUtils.isNotBlank(branchOrgMap)) {
            //AeaHiApplyinst增加branchOrg字段控制分局承办
            aeaHiApplyinst.setBranchOrg(branchOrgMap);
        }
        aeaHiApplyinst.setCreater(SecurityContext.getCurrentUserName());
        aeaHiApplyinst.setCreateTime(new Date());
        aeaHiApplyinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiApplyinst.setIsTemporarySubmit(isTemporarySubmit);
        aeaHiApplyinst.setParentApplyinstId(parentApplyinstId);
        aeaHiApplyinstMapper.insertAeaHiApplyinst(aeaHiApplyinst);

        return aeaHiApplyinst;
    }

    @Override
    public AeaHiApplyinst createAeaHiApplyinstAndTriggerAeaLogApplyStateHist(String applySource, String applySubject, String linkmanInfoId, String isSeriesApprove, String branchOrgMap, String taskId, String appinstId, String applyState, String opuWindowId,String parentApplyinstId) throws Exception {
        AeaHiApplyinst aeaHiApplyinst = this.createAeaHiApplyinst(applySource, applySubject, linkmanInfoId, isSeriesApprove, branchOrgMap, applyState,"0",parentApplyinstId);
        if (aeaHiApplyinst != null && StringUtils.isNotBlank(aeaHiApplyinst.getApplyinstState())) {
            aeaLogApplyStateHistService.insertTriggerAeaLogApplyStateHist(aeaHiApplyinst.getApplyinstId(), taskId, appinstId, null, applyState, opuWindowId);
        }
        return aeaHiApplyinst;
    }

    @Override
    public void updateAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst) throws Exception {
        if (aeaHiApplyinst == null)
            throw new NullPointerException("需要更新的申请对象为空！");

        if (aeaHiApplyinst.getApplyinstId() == null)
            throw new InvalidParameterException("更新对象的主键ID为空!");

        aeaHiApplyinst.setModifier(SecurityContext.getCurrentUserName());
        aeaHiApplyinst.setModifyTime(new Date());

        aeaHiApplyinstMapper.updateAeaHiApplyinst(aeaHiApplyinst);
    }

    @Override
    public void updateAeaHiApplyinstAndInsertTriggerAeaLogApplyStateHist(AeaHiApplyinst aeaHiApplyinst, AeaLogApplyStateHist aeaLogApplyStateHist) throws Exception {
        this.updateAeaHiApplyinst(aeaHiApplyinst);
        aeaLogApplyStateHistService.insertAeaLogApplyStateHist(aeaLogApplyStateHist);
    }

    @Override
    public void updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(String applyinstId, String taskinstId, String appinstId, String applyinstState, String opuWindowId) throws Exception {
        if (applyinstId == null) {
            throw new InvalidParameterException("主键ID为空!");
        }
        AeaHiApplyinst old = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        if (old != null) {
            AeaHiApplyinst aeaHiApplyinst = new AeaHiApplyinst();
            aeaHiApplyinst.setApplyinstId(applyinstId);
            aeaHiApplyinst.setApplyinstState(applyinstState);
            // 窗口受理时间
            if (ApplyState.ACCEPT_DEAL.getValue().equals(applyinstState)) {
                aeaHiApplyinst.setAcceptTime(new Date());
            }
            // 窗口办结时间
            if (ApplyState.COMPLETED.getValue().equals(applyinstState)) {
                aeaHiApplyinst.setEndTime(new Date());
            }
            this.updateAeaHiApplyinst(aeaHiApplyinst);
            aeaLogApplyStateHistService.insertTriggerAeaLogApplyStateHist(applyinstId, taskinstId, appinstId, old.getApplyinstState(), applyinstState, opuWindowId);

            // 根据条件判断是否发送事件
            publisher.conditionalPublishEvent(new ApplyEventVo(applyinstId, appinstId, taskinstId, applyinstState, old.getApplyinstState(), opuWindowId));
        }
    }

    @Override
    public void updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(String applyinstId, String opsUserOpinion, String opsAction, String opsMemo, String applyinstState, String opuWindowId) throws Exception {
        if (applyinstId == null) {
            throw new InvalidParameterException("主键ID为空!");
        }
        AeaHiApplyinst old = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        if (old != null) {
            AeaHiApplyinst aeaHiApplyinst = new AeaHiApplyinst();
            aeaHiApplyinst.setApplyinstId(applyinstId);
            aeaHiApplyinst.setApplyinstState(applyinstState);
            this.updateAeaHiApplyinst(aeaHiApplyinst);
            aeaLogApplyStateHistService.insertOpsAeaLogApplyStateHist(applyinstId, opsUserOpinion, opsAction, opsMemo, old.getApplyinstState(), applyinstState, opuWindowId);
        }
    }

    @Override
    public void updateAeaHiApplyinstStateAndInsertOpsLinkBusAeaLogItemStateHist(String applyinstId, String opsUserOpinion, String opsAction, String opsMemo, String applyinstState, String opuWindowId, String busTableName, String busPkName, String busRecordId) throws Exception {
        if (applyinstId == null) {
            throw new InvalidParameterException("主键ID为空!");
        }
        AeaHiApplyinst old = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        if (old != null) {
            AeaHiApplyinst aeaHiApplyinst = new AeaHiApplyinst();
            aeaHiApplyinst.setApplyinstId(applyinstId);
            aeaHiApplyinst.setApplyinstState(applyinstState);
            this.updateAeaHiApplyinst(aeaHiApplyinst);
            aeaLogApplyStateHistService.insertOpsLinkBusAeaLogApplyStateHist(applyinstId, opsUserOpinion, opsAction, opsMemo, old.getApplyinstState(), applyinstState, opuWindowId, busTableName, busPkName, busRecordId);
        }
    }

    @Override
    public void deleteAeaHiApplyinstById(String applyinstId) throws Exception {
        if (applyinstId == null)
            throw new InvalidParameterException("需要删除的申请信息主键ID为空！");

        aeaHiApplyinstMapper.deleteAeaHiApplyinst(applyinstId);
    }

    @Override
    public List<AeaHiApplyinst> listAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst) throws Exception {
        aeaHiApplyinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaHiApplyinstMapper.listAeaHiApplyinst(aeaHiApplyinst);
    }

    @Override
    public PageInfo<AeaHiApplyinst> listAeaHiApplyinst(AeaHiApplyinst aeaHiApplyinst, Page page) throws Exception {
        aeaHiApplyinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaHiApplyinst> list = aeaHiApplyinstMapper.listAeaHiApplyinst(aeaHiApplyinst);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiApplyinst>(list);
    }

    @Override
    public AeaHiApplyinst getAeaHiApplyinstById(String applyinstId) throws Exception {
        if (applyinstId == null)
            throw new InvalidParameterException("参数applyinstId为空！");
        return aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
    }

    @Override
    public AeaHiApplyinst getAeaHiApplyinstByCode(String applyinstCode) throws Exception {
        if (applyinstCode == null)
            throw new InvalidParameterException("参数applyinstCode为空！");
        return aeaHiApplyinstMapper.getAeaHiApplyinstByCode(applyinstCode);
    }

    @Override
    public List<AeaHiApplyinst> listAeaHiApplyinstByLinkmanInfoId(String linkmanInfoId) throws Exception {
        if (linkmanInfoId == null)
            throw new InvalidParameterException("参数linkmanInfoId为空！");

        return aeaHiApplyinstMapper.listAeaHiApplyinstByLinkmanInfoId(linkmanInfoId);
    }

    @Override
    public List<AeaHiApplyinst> listAeaHiApplyinstByState(String state, String rootOrgId) throws Exception {
        if (state == null)
            throw new InvalidParameterException("参数state为空！");
        return aeaHiApplyinstMapper.listAeaHiApplyinstByState(state, rootOrgId);
    }

    @Override
    public List<AeaHiApplyinst> listAeaHiApplyinstByStates(List<String> states, String rootOrgId) throws Exception {
        if (states == null || states.size() == 0)
            throw new InvalidParameterException("参数集合states为空！");

        return aeaHiApplyinstMapper.listAeaHiApplyinstByStates(states, rootOrgId);
    }

    /*public String generateApplyinstCode() throws Exception {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        StringBuffer code = new StringBuffer();
        code.append(simpleDateFormat.format(date));
        Random random = new Random();
        code.append(random.nextInt(10)).append(random.nextInt(10)).append(random.nextInt(10)).append(random.nextInt(10));
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstByCode(code.toString());
        if (aeaHiApplyinst != null) {
            return generateApplyinstCode();
        }
        return code.toString();
    }*/

    @Override
    public int countApplyinstByApplyinstState(String applyinstState, String rootOrgId) throws Exception {
        return listAeaHiApplyinstByState(applyinstState, rootOrgId).size();
    }

    @Override
    public int countApplyinstByApplyinstStates(List<String> applyinstStates, String rootOrgId) throws Exception {
        return listAeaHiApplyinstByStates(applyinstStates, rootOrgId).size();
    }

    @Override
    public int countCurrentMonthApplyinstByApplyinstStates(List<String> applyinstStates, String rootOrgId) throws Exception {
        return aeaHiApplyinstMapper.countCurrentMonthApplyinstByApplyinstStates(applyinstStates, rootOrgId);
    }

    @Override
    public List<AeaHiApplyinst> listApplyInstIdAndStateByProjInfoIdAndUser(String projInfoId, String isSeriesApprove, String unitInfoId, String userInfoId) {
        return aeaHiApplyinstMapper.listApplyInstIdAndStateByProjInfoIdAndUser(projInfoId, isSeriesApprove, unitInfoId, userInfoId);
    }

    @Override
    public List<AeaHiApplyinst> getAllApplyinstesByProjInfoId(String projInfoId, String rootOrgId) throws Exception {
        if (StringUtils.isBlank(projInfoId)) throw new Exception("项目ID为空！");
        return aeaHiApplyinstMapper.getAllApplyinstesByProjInfoId(projInfoId, rootOrgId);
    }

    @Override
    public List<AeaHiApplyinst> getSeriesAeaHiApplyinstListByParentApplyinstId(String applyinstId) throws Exception {
        if (StringUtils.isBlank(applyinstId)) throw new Exception("申报实例ID为空！");
        return aeaHiApplyinstMapper.getSeriesAeaHiApplyinstListByParentApplyinstId(applyinstId);
    }
}
