package com.augurit.aplanmis.front.third.multidiscipline.service;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.front.third.multidiscipline.constant.ThirdApproveState;
import com.augurit.aplanmis.front.third.multidiscipline.mapper.ThirdApproveInfoMapper;
import com.augurit.aplanmis.front.third.multidiscipline.vo.ThirdApproveInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ThirdApproveInfoService {

    @Autowired
    private ThirdApproveInfoMapper thirdApproveInfoMapper;

    @Transactional(readOnly = true)
    public ThirdApproveInfo getApproveInfo(String projCode) {
        log.info("根据项目编码查询审批信息， projCode: {}", projCode);

//        List<AeaProjInfo> projInfos = aeaProjInfoMapper.getProjInfoByCode(projCode);
        List<AeaProjInfo> projInfos = new ArrayList<>();
        if (!validate(projInfos)) return null;
        AeaProjInfo aeaProjInfo = projInfos.get(0);

        ThirdApproveInfo thirdApproveInfo = new ThirdApproveInfo();
        thirdApproveInfo.setProjCode(aeaProjInfo.getLocalCode());
        thirdApproveInfo.setProjInfoId(aeaProjInfo.getProjInfoId());
        thirdApproveInfo.setProjName(aeaProjInfo.getProjName());

        List<ThirdApproveInfo.ThirdApproveStage> thirdApproveStages = thirdApproveInfoMapper.listApproveStages(projCode);
        thirdApproveStages.forEach(s -> {
            switch (s.getStateValue()) {
                case "3":
                case "5":
                case "9":
                case "11":
                    thirdApproveInfo.getApproveStages().get(ThirdApproveState.UN_HANDLE.getName()).add(s);
                    break;
                case "1":
                case "2":
                case "4":
                case "6":
                case "7":
                case "8":
                case "10":
                case "13":
                    thirdApproveInfo.getApproveStages().get(ThirdApproveState.APPROVING.getName()).add(s);
                    break;
                case "12":// 办结
                    thirdApproveInfo.getApproveStages().get(ThirdApproveState.HANDLED.getName()).add(s);
                    break;
            }
            // 获取材料附件
            s.getMatinstVos().addAll(getMatinsts(s.getApplyinstId()));
        });
        return thirdApproveInfo;
    }

    // 材料附件列表
    private List<ThirdApproveInfo.ThirdMatinstVo> getMatinsts(String applyinstId) {
        /*if (StringUtils.isBlank(applyinstId)) return new ArrayList<>();
        try {
            return aeaBusinessService.getParMatinstList(applyinstId)
                    .stream()
                    .map(ThirdApproveInfo.ThirdMatinstVo::from)
                    .peek(vo -> {
                        if (StringUtils.isNotBlank(vo.getAttMatinstId())) {
                            try {
                                List<BscAttFileAndDir> attFiles = bpmProcessRestService.getAttFiles(vo.getAttMatinstId());
                                List<ThirdApproveInfo.ThirdFileVo> fileVos = attFiles.stream().map(f -> new ThirdApproveInfo.ThirdFileVo(f.getFileId(), f.getFileName())).collect(Collectors.toList());
                                vo.setFileVos(fileVos);
                                if (CollectionUtils.isEmpty(attFiles)) {
                                    vo.setAttMatinstId(null);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                vo.setAttMatinstId(null);
                            }
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取阶段材料失败");
        }*/
        return null;
    }

    private boolean validate(List<AeaProjInfo> projInfos) {
        if (projInfos.size() > 1) {
            log.warn("根据项目编码获取到多个相同的项目, 可能数据由冗余!!");
        }
        if (projInfos.size() < 1) {
            log.error("该项目编码不存在");
            return false;
        }
        return true;
    }
}
