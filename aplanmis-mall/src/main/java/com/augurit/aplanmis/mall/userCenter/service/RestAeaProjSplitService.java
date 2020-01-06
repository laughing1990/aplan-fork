package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.SplitApplyState;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaParentProjMapper;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgService;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgUserService;
import com.augurit.aplanmis.common.service.dic.GcbmBscRuleCodeStrategy;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.window.AeaProjApplySplitService;
import com.augurit.aplanmis.mall.userCenter.vo.SplitProjInfoParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RestAeaProjSplitService {
    @Autowired
    private AeaProjApplySplitService aeaProjApplySplitService;
    @Autowired
    private GcbmBscRuleCodeStrategy gcbmBscRuleCodeStrategy;
    @Autowired
    private AeaSolicitOrgService aeaSolicitOrgService;
    @Autowired
    private AeaSolicitOrgUserService aeaSolicitOrgUserService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaParentProjMapper aeaParentProjMapper;

    public AeaProjApplySplit saveProjInfoAndInitProjApplySplit(SplitProjInfoParamVo splitProjInfoParamVo) throws Exception {
        String parentProjInfoId=splitProjInfoParamVo.getParentProjInfoId();
        if(StringUtils.isBlank(parentProjInfoId)) throw new Exception("项目ID不能为空");
        AeaProjInfo projInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(parentProjInfoId);
        AeaProjInfo gcInfo=SplitProjInfoParamVo.formatProjInfo(projInfo,splitProjInfoParamVo);
        String gcbm = gcbmBscRuleCodeStrategy.generateCode(gcInfo.getLocalCode(), gcInfo.getLocalCode(), "工程编码", gcInfo.getRootOrgId());
        gcInfo.setGcbm(gcbm);
        aeaProjInfoService.insertAeaProjInfo(gcInfo);//插入工程信息
        //插入父子项目关系
        AeaParentProj aeaParentProj=initAeaParentProj(parentProjInfoId,gcInfo.getProjInfoId());
        aeaParentProjMapper.insertAeaParentProj(aeaParentProj);
        //初始化拆分申请
        AeaProjApplySplit aeaProjApplySplit=initAeaProjApplySplit(splitProjInfoParamVo,gcInfo.getProjInfoId());
        aeaProjApplySplitService.saveAeaProjApplySplit(aeaProjApplySplit);//实例化拆分申请
        return aeaProjApplySplit;

    }

    private AeaParentProj initAeaParentProj(String parentProjInfoId,String projInfoId){
        AeaParentProj aeaParentProj=new AeaParentProj();
        aeaParentProj.setNodeProjId(UUID.randomUUID().toString());
        aeaParentProj.setParentProjId(parentProjInfoId);
        aeaParentProj.setChildProjId(projInfoId);
        aeaParentProj.setCreater(SecurityContext.getCurrentUserName());
        aeaParentProj.setCreateTime(new Date());
        aeaParentProj.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParentProj.setProjSeq(parentProjInfoId+"."+projInfoId);
        return aeaParentProj;
    }

    private AeaProjApplySplit initAeaProjApplySplit(SplitProjInfoParamVo splitProjInfoParamVo, String projInfoId) throws Exception {
        AeaProjApplySplit aeaProjApplySplit=new AeaProjApplySplit();
        aeaProjApplySplit.setApplySplitId(UUID.randomUUID().toString());
        aeaProjApplySplit.setProjInfoId(projInfoId);
        aeaProjApplySplit.setUnitInfoId(splitProjInfoParamVo.getUnitInfoId());
        aeaProjApplySplit.setApplyUserId(splitProjInfoParamVo.getLinkmanInfoId());
        aeaProjApplySplit.setStageNo(splitProjInfoParamVo.getStageNo());
        aeaProjApplySplit.setStageId(splitProjInfoParamVo.getStageId());
        aeaProjApplySplit.setFrontStageProjInfoId(splitProjInfoParamVo.getFrontStageProjInfoId());
        AeaSolicitOrg aeaSolicitOrg=new AeaSolicitOrg();
        aeaSolicitOrg.setStageId(splitProjInfoParamVo.getStageId());
        aeaSolicitOrg.setIsBusSolicit("0");
        List<AeaSolicitOrg> solicitOrgs = aeaSolicitOrgService.listAeaSolicitOrg(aeaSolicitOrg);
        if(solicitOrgs.size()==0) throw new Exception("找不到牵头部门");
        aeaProjApplySplit.setLeaderOrgId(solicitOrgs.size()>0?solicitOrgs.get(0).getOrgId():null);
        if(solicitOrgs.size()>0){
            AeaSolicitOrgUser aeaSolicitOrgUser=new AeaSolicitOrgUser();
            aeaSolicitOrgUser.setSolicitOrgId(solicitOrgs.get(0).getSolicitOrgId());
            aeaSolicitOrgUser.setIsActive(Status.ON);
            List<AeaSolicitOrgUser> solicitOrgUsers = aeaSolicitOrgUserService.listAeaSolicitOrgUser(aeaSolicitOrgUser);
            aeaProjApplySplit.setLeaderUserId(solicitOrgUsers.size()>0?solicitOrgUsers.get(0).getUserId():null);
        }
        aeaProjApplySplit.setApplyState(SplitApplyState.APPROVING.getValue());
        aeaProjApplySplit.setStartTime(new Date());
        aeaProjApplySplit.setIsDeleted(Status.OFF);
        aeaProjApplySplit.setCreater(SecurityContext.getCurrentUserName());
        aeaProjApplySplit.setCreateTime(new Date());
        aeaProjApplySplit.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaProjApplySplit;
    }

    @Autowired
    private AeaParStageService aeaParStageService;

    public AeaProjInfo getFrontStageProjInfo(String stageNo, String themeVerId, String localCode) {
        //aeaParStageService.stageApply()
        return  null;
    }
}
