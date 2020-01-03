package com.augurit.aplanmis.front.listener.execution;

import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiParStageinstMapper;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.util.CommandContextUtil;

import java.util.List;
import java.util.Map;

/**
 * 竣工验收阶段事项审查结果计算监听器
 */
public class ProjcheckCalculateItemApproveStateExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        AeaHiParStageinstMapper aeaHiParStageinstMapper = SpringContextHolder.getBean(AeaHiParStageinstMapper.class);
        AeaHiIteminstMapper aeaHiIteminstMapper = SpringContextHolder.getBean(AeaHiIteminstMapper.class);

        String procInstId = delegateExecution.getProcessInstanceId();

        try {
            AeaHiApplyinst aeaHiApplyinst = (AeaHiApplyinst) runtimeService.getVariable(procInstId, "form");

            AeaHiParStageinst parStageinst = new AeaHiParStageinst();
            parStageinst.setApplyinstId(aeaHiApplyinst.getApplyinstId());
            parStageinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaHiParStageinst> stageinstList = aeaHiParStageinstMapper.listAeaHiParStageinst(parStageinst);
            if(stageinstList!=null&&stageinstList.size()>0){
                AeaHiParStageinst currParStage = stageinstList.get(0);

                List<AeaHiIteminst> hiIteminstList = aeaHiIteminstMapper.getAeaHiIteminstListByStageinstId(currParStage.getStageinstId());

                int count = 0;
                if(hiIteminstList!=null&&hiIteminstList.size()>0){
                    for(AeaHiIteminst iteminst:hiIteminstList){
                        Map<String,Boolean> itemStates = aeaHiApplyinst.getProjCheck().getItemStates();

                        //办结（通过）、办结（容缺通过）
                        if(ItemStatus.AGREE.getValue().equals(iteminst.getIteminstState())
                                ||ItemStatus.AGREE_TOLERANCE.getValue().equals(iteminst.getIteminstState())){

                            itemStates.put(iteminst.getItemCategoryMark(),true);

                            count++;
                        }

                        //手动排除房屋建筑工程和市政基础设施工程竣工验收备案事项（每个地方不一样 需要修改这个唯一分类标识符）
                        if("FWJZGCHSZJCSSGCJGYSBA1".equals(iteminst.getItemCategoryMark().trim())) {
                            itemStates.remove(iteminst.getItemCategoryMark());
                            count++;
                        }
                    }

                    if(count==hiIteminstList.size()){
                        aeaHiApplyinst.getProjCheck().setAllItemState(true);
//                        aeaHiApplyinst.getProjCheck().setAllMatchState(true);
                    }else{
                        aeaHiApplyinst.getProjCheck().setAllItemState(false);
//                        aeaHiApplyinst.getProjCheck().setAllMatchState(true);
                    }

                    System.out.println("@##########################aeaHiApplyinst="+aeaHiApplyinst);
                    runtimeService.setVariable(procInstId, "form",aeaHiApplyinst);//把计算结果封装到流程里
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
