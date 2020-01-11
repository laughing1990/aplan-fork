package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("我的项目工程管理项目状态树VO")
public class ProjStatusTreeVo {
    @ApiModelProperty("项目/工程ID")
    private String projInfoId;
    @ApiModelProperty("项目/工程名称")
    private String projName;
    @ApiModelProperty("项目代码")
    private String localCode;
    @ApiModelProperty("项目类别")
    private String themeName;
    @ApiModelProperty("开始时间")
    private String nstartTime;
    @ApiModelProperty("审批时限")
    private Double dueNum;
    @ApiModelProperty("投资金额")
    private Double investSum;
    @ApiModelProperty("阶段数组")
    private List<ProjStatusTreeStageVo> stagesVos;
    @ApiModelProperty("工程状态树")
    private List<ProjStatusVo> projStatusVos;
    @ApiModelProperty("工程状态数组")
    private List<List<ProjStatusVo>> projStatusVoArrs;


    @Data
    @ApiModel("项目状态树VO下阶段数组")
    public static class ProjStatusTreeStageVo{
        @ApiModelProperty("阶段ID")
        private String stageId;
        @ApiModelProperty("阶段名称")
        private String stageName;
        @ApiModelProperty("阶段状态(0:未申报1:申报中2:已办结)")
        private String applyStatus;
        public static ProjStatusTreeStageVo build(AeaParStage aeaParStage){
            ProjStatusTreeStageVo vo = new ProjStatusTreeStageVo();
            vo.setStageId(aeaParStage.getStageId());
            vo.setStageName(aeaParStage.getStageName());
            return vo;
        }
    }
    public ProjStatusTreeStageVo getProjStatusTreeStageVo(){
        return new ProjStatusTreeStageVo();
    }

    @Data
    @ApiModel("工程状态树")
    public static class ProjStatusVo{
        @ApiModelProperty("项目/工程名称ID")
        private String projInfoId;
        @ApiModelProperty("项目/工程名称ID")
        private String parentProjInfoId;
        @ApiModelProperty("项目/工程名称")
        private String projName;
        @ApiModelProperty("项目代码")
        private String localCode;
        @ApiModelProperty("申报状态(0:未申报1:申报中2:已办结3:未审核4:已审核)")
        private String applyStatus;
        @ApiModelProperty("阶段ID")
        private String stageId;
        @ApiModelProperty("项目标识，r表示ROOT项目，p表示发改项目，c表示子项目或子子项目")
        private String projFlag;
        @ApiModelProperty("子工程")
        private List<ProjStatusVo> childProjStatusVos;

        public static ProjStatusVo build(AeaProjInfo aeaProjInfo){
            ProjStatusVo vo = new ProjStatusVo();
            vo.setProjInfoId(aeaProjInfo.getProjInfoId());
            vo.setProjName(aeaProjInfo.getProjName());
            vo.setLocalCode(aeaProjInfo.getLocalCode());
            return vo;
        }
    }

}
