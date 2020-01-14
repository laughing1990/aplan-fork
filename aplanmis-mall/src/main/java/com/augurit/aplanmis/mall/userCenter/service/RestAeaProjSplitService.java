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
import com.augurit.aplanmis.mall.main.service.RestMainService;
import com.augurit.aplanmis.mall.userCenter.vo.SplitProjInfoParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Autowired
    private RestMainService restMainService;

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
        aeaSolicitOrg.setLatestStageId(splitProjInfoParamVo.getStageId());
        aeaSolicitOrg.setIsBusSolicit("0");
        List<AeaSolicitOrg> solicitOrgs = aeaSolicitOrgService.listAeaSolicitOrgRelOrgInfo(aeaSolicitOrg);
        if(solicitOrgs.size()==0) throw new Exception("找不到牵头部门");
        String regionId=splitProjInfoParamVo.getRegionalism();
        if(StringUtils.isNotBlank(regionId)){
            solicitOrgs=solicitOrgs.stream().filter(v->regionId.equals(v.getRegionId())).collect(Collectors.toList());
            if(solicitOrgs.size()==0) throw new Exception("找不到与当前工程的行政区划匹配的牵头部门");
        }
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

    public AeaProjInfo getFrontStageProjInfo(String stageNo, String themeId, String localCode,HttpServletRequest request) throws Exception {
        List<AeaParStage> stageList = restMainService.getStageByThemeId(themeId, null, SecurityContext.getCurrentOrgId(), null, null, request, "0");
        String stageId="";
        for (AeaParStage aeaParStage:stageList){
            String gjbz=aeaParStage.getDybzspjdxh();
            if(StringUtils.isBlank(gjbz)) continue;
            if(("1".equals(stageNo) && gjbz.contains("1"))||("2".equals(stageNo) && gjbz.contains("2"))){//第二阶段，前阶段为第一阶段
                stageId=aeaParStage.getStageId();
                break;
            }
        }
        if(StringUtils.isBlank(stageId)) return null;
        List<AeaProjInfo> aeaProjInfoList=aeaProjInfoService.getAeaProjInfosByStageIdAndLocalCode(stageId,localCode);
        if(aeaProjInfoList.size()>0){
            return aeaProjInfoList.get(0);
        }
        return  null;
    }
}
