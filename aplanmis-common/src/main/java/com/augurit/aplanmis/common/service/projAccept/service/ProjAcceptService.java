package com.augurit.aplanmis.common.service.projAccept.service;

import com.augurit.agcloud.bpm.common.domain.BpmHistoryCommentForm;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.projAccept.vo.ProjAcceptOpinionSummaryVo;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 竣工验收阶段相关逻辑
 */
@Service
@Transactional
public class ProjAcceptService {
    private static String[] itemCategoryMarks = new String[]{"GHTJHS1","JSGCCJDAYS1","GCJGYSJD1","FWJZGCHSZJCSSGCJGYSBA1"};//规划条件核实、建设工程城建档案验收、工程竣工验收监督、房屋建筑工程和市政基础设施工程竣工验收备案

    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private BpmTaskService bpmTaskService;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    /**
     * 根据申报实例ID，获取竣工验收阶段汇总意见信息（只适合于竣工验收阶段）
     * @param applyinstId
     * @return
     * @throws Exception
     */
    public ProjAcceptOpinionSummaryVo caculateProjAcceptOpinionSummary(String applyinstId) throws Exception {
        if(StringUtils.isBlank(applyinstId))
            throw new RuntimeException("申报实例ID参数不能为空！");

        List<AeaApplyinstProj> applyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);

        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);

        if(applyinstProjs==null||applyinstProjs.size()==0)
            throw new RuntimeException("获取申报项目信息失败！");

        AeaApplyinstProj applyinstProj = applyinstProjs.get(0);
        String projInfoId = applyinstProj.getProjInfoId();

        String buildUnit = null;
        String jianliUnit = null;
        String kanchaUnit = null;
        String designUnit = null;
        String shigongUnit = null;
        String linkman = null;
        String linkmanPhone = null;

        //联系人信息
        AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
        if(linkmanInfo!=null){
            linkman = linkmanInfo.getLinkmanName();
            linkmanPhone = linkmanInfo.getLinkmanMobilePhone();
        }


        AeaUnitInfo buildUnitInfo = null;//获取建设单位
        AeaUnitInfo jianliUnitInfo = null;//获取监理单位
        AeaUnitInfo kanchaUnitInfo = null;//获取勘查单位
        AeaUnitInfo designUnitInfo = null;//获取设计单位
        AeaUnitInfo shigongUnitInfo = null;//获取施工单位（总包）

        List<AeaUnitInfo> unitInfoList = aeaUnitInfoService.findApplyUnitProj(applyinstId,projInfoId);

        if(unitInfoList!=null&&unitInfoList.size()>0){
            for(AeaUnitInfo aeaUnitInfo:unitInfoList){
                if("1".equals(aeaUnitInfo.getUnitType()))
                    buildUnitInfo = aeaUnitInfo;
                if("5".equals(aeaUnitInfo.getUnitType()))
                    jianliUnitInfo = aeaUnitInfo;
                if("3".equals(aeaUnitInfo.getUnitType()))
                    kanchaUnitInfo = aeaUnitInfo;
                if("4".equals(aeaUnitInfo.getUnitType()))
                    designUnitInfo = aeaUnitInfo;
                if("22".equals(aeaUnitInfo.getUnitType()))
                    shigongUnitInfo = aeaUnitInfo;
            }
        }

        if(buildUnitInfo!=null) {
            buildUnit = buildUnitInfo.getApplicant();

            /*List<LinkmanTypeVo> linkmanTypeVos = aeaLinkmanInfoService.findLinkmanTypes(projInfoId,buildUnitInfo.getUnitInfoId());
            if(linkmanTypeVos!=null&&linkmanTypeVos.size()>0) {
                linkman = linkmanTypeVos.get(0).getLinkmanName();
                linkmanPhone = linkmanTypeVos.get(0).getLinkmanMobilePhone();
            }*/
        }

        if(jianliUnitInfo!=null)
            jianliUnit = jianliUnitInfo.getApplicant();

        if(kanchaUnitInfo!=null)
            kanchaUnit = kanchaUnitInfo.getApplicant();

        if(designUnitInfo!=null)
            designUnit = designUnitInfo.getApplicant();

        if(shigongUnitInfo!=null)
            shigongUnit = shigongUnitInfo.getApplicant();

        ProjAcceptOpinionSummaryVo acceptOpinionSummaryVo = new ProjAcceptOpinionSummaryVo();
        acceptOpinionSummaryVo.setCentralCode(applyinstProj.getCentralCode());
        acceptOpinionSummaryVo.setProjName(applyinstProj.getProjName());
        acceptOpinionSummaryVo.setDocumentNum(null);
        acceptOpinionSummaryVo.setBuildArea(applyinstProj.getCentralCode());
        acceptOpinionSummaryVo.setProjAddr(applyinstProj.getProjAddr());
        acceptOpinionSummaryVo.setBuildArea(applyinstProj.getBuildAreaSum());
        if(applyinstProj.getAboveFloor()!=null)
        acceptOpinionSummaryVo.setAboveFloor(applyinstProj.getAboveFloor().toString());
        acceptOpinionSummaryVo.setBuildUnitName(buildUnit);
        acceptOpinionSummaryVo.setJianliUnitName(jianliUnit);
        acceptOpinionSummaryVo.setDesignUnitName(designUnit);
        acceptOpinionSummaryVo.setKanchaUnitName(kanchaUnit);
        acceptOpinionSummaryVo.setShigongUnitName(shigongUnit);
        acceptOpinionSummaryVo.setLinkman(linkman);
        acceptOpinionSummaryVo.setLinkmanPhone(linkmanPhone);

        AeaHiParStageinst stageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinstId);

        if(stageinst==null)
            throw new RuntimeException("获取申报阶段实例信息失败，请检查当前申报实例ID是否正确！");

        List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByStageinstId(stageinst.getStageinstId());

        if(iteminstList!=null&&iteminstList.size()>0){
            Map<String,String> deptOpinions = new HashMap<>();
            for(AeaHiIteminst iteminst:iteminstList){
                String itemCategoryMark = iteminst.getItemCategoryMark();
                //获取符合要求的事项节点的审批意见
                if(Arrays.asList(itemCategoryMarks).contains(itemCategoryMark)){
                    String procinstId = iteminst.getProcinstId();

                    if(StringUtils.isNotBlank(procinstId)){
                        List<BpmHistoryCommentForm> commentFormList = bpmTaskService.getHistoryCommentsByProcessInstanceId(procinstId);

                        if(commentFormList!=null&&commentFormList.size()>0) {
                            //倒序排序，获取最后一个节点的意见
                            Collections.sort(commentFormList, new Comparator<BpmHistoryCommentForm>() {
                                @Override
                                public int compare(BpmHistoryCommentForm o1, BpmHistoryCommentForm o2) {
                                    return o1.getEndDate().compareTo(o2.getEndDate());
                                }
                            });

                            deptOpinions.put(iteminst.getApproveOrgName()+"（"+iteminst.getIteminstName()+"）", commentFormList.get(0).getCommentMessage());
                        }
                    }
                }
            }

            acceptOpinionSummaryVo.setDeptOpinions(deptOpinions);
        }

        return acceptOpinionSummaryVo;
    }
}
