package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.service.window.AeaProjApplySplitService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyProjService;
import com.augurit.aplanmis.mall.userCenter.vo.ProjStatusTreeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.swing.StringUIClientPropertyKey;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class RestApplyProjServiceImp implements RestApplyProjService {

    @Autowired
    AeaProjInfoService aeaProjInfoService;
    @Autowired
    AeaParThemeService aeaParThemeService;
    @Autowired
    AeaParStageMapper aeaParStageMapper;
    @Autowired
    AeaParStageService aeaParStageService;
    @Autowired
    AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    AeaProjApplySplitService aeaProjApplySplitService;
    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    private String topOrgId;

    @Override
    public ProjStatusTreeVo getProjStatusTreeVo(String projInfoId, HttpServletRequest request) throws Exception {
        try {
            ProjStatusTreeVo vo = new ProjStatusTreeVo();
            //copy基本项目信息
            AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
            if (aeaProjInfo==null) throw new IllegalArgumentException("没有查到项目!");
            BeanUtils.copyProperties(aeaProjInfo,vo);
            //主题名称
            String themeId = aeaProjInfo.getThemeId();
            if (StringUtils.isNotBlank(themeId)) vo.setThemeName(aeaParThemeService.getAeaParThemeByThemeId(themeId).getThemeName());
            //set项目及子工程至VO
            BeanUtils.copyProperties(aeaProjInfo,vo.getProjStatusVo());
            setChildProj(vo.getProjStatusVo());
            //主题下的所有主线阶段
            List<AeaParStage> nodeStages = (aeaParStageService.listAeaParStageByThemeIdOrThemeVerId(themeId, "",topOrgId))
                    .stream().filter(stage->"1".equals(stage.getIsNode())).collect(Collectors.toList());
            if (nodeStages.size()<=0) throw new IllegalArgumentException("当前主题下无主线阶段");
            vo.setStagesVos(nodeStages.stream().map(ProjStatusTreeVo.ProjStatusTreeStageVo::build).collect(Collectors.toList()));
            //给项目树及阶段设置状态
            setApplyStatus2Proj(vo.getProjStatusVo(),vo.getStagesVos());
            return vo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("查询我的项目工程管理获取项目状态树异常");
        }
    }

    //给项目树及阶段设置状态
     void setApplyStatus2Proj(List<ProjStatusTreeVo.ProjStatusVo> vos, List<ProjStatusTreeVo.ProjStatusTreeStageVo> nodeStages){
         try {
             int stageStatus = 0;
             Boolean isDoned = true;//已办结
             String stageId = vos.get(0).getStageId();
             for (ProjStatusTreeVo.ProjStatusVo vo : vos) {
                 AeaHiApplyinst aeaHiApplyinst = getAeaHiApplyinstByProjInfoIdAndStageId(vo.getProjInfoId(),vo.getStageId());
                 if (aeaHiApplyinst!=null){
                     stageStatus = 2;
                     if (!"6".equals(aeaHiApplyinst.getApplyinstState())) {
                         stageStatus = 1;isDoned=false;
                     }
                     vo.setApplyStatus(stageStatus+"");
                 }
             }
             int finalStageStatus = stageStatus;
             Boolean finalIsDoned = isDoned;
             nodeStages.stream().forEach(nodeStage->{
                 if (stageId.equals(nodeStage.getStageId())){
                     if (finalStageStatus==0){
                         nodeStage.setApplyStatus("0"); ; return;//未申报
                     }
                     if (finalIsDoned) nodeStage.setApplyStatus("2");
                     nodeStage.setApplyStatus("1");
                 }
             });
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

   void setChildProj(List<ProjStatusTreeVo.ProjStatusVo> vos){
       try {
           AeaProjApplySplit aeaProjApplySplit = new AeaProjApplySplit();
           for (ProjStatusTreeVo.ProjStatusVo vo : vos) {
               aeaProjApplySplit.setFrontStageProjInfoId(vo.getProjInfoId());
               List<AeaProjApplySplit> aeaProjApplySplits = aeaProjApplySplitService.listAeaProjApplySplit(aeaProjApplySplit);
               if (aeaProjApplySplits.size()<=0) return;
               List<String> ids = aeaProjApplySplits.stream().map(AeaProjApplySplit::getProjInfoId).collect(Collectors.toList());
               String[] idArrays = ids.toArray(new String[ids.size()]);
               List<AeaProjInfo> aeaProjChildrenList = aeaProjInfoMapper.listAeaProjInfoByIds(idArrays);
               List<ProjStatusTreeVo.ProjStatusVo> projInfoVos =  aeaProjChildrenList.stream().map(ProjStatusTreeVo.ProjStatusVo::build).collect(Collectors.toList());
               projInfoVos.stream().forEach(projStatusVo -> {
                   aeaProjApplySplits.stream().forEach(aeaProjApplySplit1 -> {
                       if (projStatusVo.getProjInfoId().equals(aeaProjApplySplit1.getProjInfoId())) {
                           projStatusVo.setStageId(aeaProjApplySplit1.getStageId());
                            if ("3".equals(aeaProjApplySplit1.getApplyState())) projStatusVo.setApplyStatus("4");//已审核
                            if ("2".equals(aeaProjApplySplit1.getApplyState())) projStatusVo.setApplyStatus("3");//未审核
                       }
                   });
               });
               vo.setChildProjStatusVos(projInfoVos);
               setChildProj(vo.getChildProjStatusVos());
           }

       } catch (Exception e) {
           e.printStackTrace();
       }
   }


   AeaHiApplyinst getAeaHiApplyinstByProjInfoIdAndStageId(String projInfoId,String stageId)throws Exception{
       AeaHiApplyinst resultBean = new AeaHiApplyinst();
       try {
           //当前项目的所有申报实例
           List<AeaHiApplyinst> aeaHiApplyinsts = aeaHiApplyinstService.getAllApplyinstesByProjInfoId(projInfoId,topOrgId);
           List<AeaHiParStageinst> aeaHiParStageinsts = new ArrayList<>();
           AeaHiParStageinst aeaHiParStageinst = new AeaHiParStageinst();
           aeaHiParStageinst.setStageId(stageId);
           for (AeaHiApplyinst aeaHiApplyinst : aeaHiApplyinsts) {
               aeaHiParStageinst.setApplyinstId(aeaHiApplyinst.getApplyinstId());
               aeaHiParStageinsts.addAll(aeaHiParStageinstService.listAeaHiParStageinst(aeaHiParStageinst));
           }
            if (aeaHiParStageinsts.size()>0) resultBean = aeaHiApplyinstService.getAeaHiApplyinstById(aeaHiParStageinsts.get(0).getApplyinstId());
       } catch (Exception e) {
           e.printStackTrace();
           throw new Exception("根据阶段和项目查询唯一的申请实例异常");
       }
       return resultBean;
   }
}
