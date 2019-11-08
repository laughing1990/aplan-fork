package com.augurit.aplanmis.front.third.multidiscipline.vo;

import com.augurit.aplanmis.front.third.multidiscipline.constant.ThirdApproveState;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ThirdApproveInfo {

    // 项目id
    private String projInfoId;
    // 项目编码
    private String projCode;
    // 项目名称
    private String projName;
    // 审批阶段
    private Map<String, List<ThirdApproveStage>> approveStages;

    public ThirdApproveInfo() {
        this.approveStages = new HashMap<>();
        approveStages.put(ThirdApproveState.UN_HANDLE.getName(), new ArrayList<>());
        approveStages.put(ThirdApproveState.APPROVING.getName(), new ArrayList<>());
        approveStages.put(ThirdApproveState.HANDLED.getName(), new ArrayList<>());
    }

    /**
     * 审批阶段信息
     */
    @Data
    public static class ThirdApproveStage {
        // 审批状态值
        private String stateName;
        private String stateValue;
        // 阶段名称
        private String stageName;
        // 申请实例
        private String applyinstId;
        // 申请实例创建时间
        private String applyinstCreateTime;
        // 阶段序号
        private String sortNo;
        // 材料附件
        private List<ThirdMatinstVo> matinstVos = new ArrayList<>();
    }

    /**
     * 材料实例和附件
     */
    @Data
    public static class ThirdMatinstVo {
        // 附件id
        private String attMatinstId;
        // 材料名称
        private String matName;
        // 附件列表
        private List<ThirdFileVo> fileVos;

        public ThirdMatinstVo() {
            fileVos = new ArrayList<>();
        }

        /*public static ThirdMatinstVo from(MatinstVo matinstVo) {
            ThirdMatinstVo thirdMatinstVo = new ThirdMatinstVo();
            thirdMatinstVo.setAttMatinstId(matinstVo.getAttMatinstId());
            thirdMatinstVo.setMatName(matinstVo.getMatName());
            return thirdMatinstVo;
        }*/
    }

    @Data
    public static class ThirdFileVo {

        private String fileId;

        private String fileName;

        public ThirdFileVo(String fileId, String fileName) {
            this.fileId = fileId;
            this.fileName = fileName;
        }
    }
}
