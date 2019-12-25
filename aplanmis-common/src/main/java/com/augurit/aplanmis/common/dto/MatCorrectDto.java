package com.augurit.aplanmis.common.dto;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MatCorrectDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "matId", value = "材料定义ID")
    private String matId;
    @ApiModelProperty(name = "matName", value = "材料名称")
    private String matName;
    @ApiModelProperty(name = "matCode", value = "材料编码")
    private String matCode;

    @ApiModelProperty(name = "reviewKeyPoints", value = "收件要点")
    private String reviewKeyPoints;
    @ApiModelProperty(name = "reviewSampleDoc", value = "收件样本")
    private String reviewSampleDoc;
    @ApiModelProperty(name = "sampleDoc", value = "样本文档")
    private String sampleDoc;
    @ApiModelProperty(name = "templateDoc", value = "模板文档")
    private String templateDoc;

    @ApiModelProperty(name = "matinstName", value = "材料实例名称")
    private String matinstName;
    @ApiModelProperty(name = "attMatinstId", value = "电子件材料实例ID")
    private String attMatinstId;
    @ApiModelProperty(name = "paperMatinstId", value = "原件材料实例ID")
    private String paperMatinstId;
    @ApiModelProperty(name = "copyMatinstId", value = "复印件材料实例ID")
    private String copyMatinstId;

    @ApiModelProperty(name = "paperCount", value = "原件补正|补全份数")
    private Long paperCount;
    @ApiModelProperty(name = "copyCount", value = "复印件补正|补全份数")
    private Long copyCount;
    @ApiModelProperty(name = "isNeedAtt", value = "是否需要电子件")
    private String isNeedAtt;

    @ApiModelProperty(name = "attIsCollected", value = "电子件是否已收齐")
    private String attIsCollected;
    @ApiModelProperty(name = "paperIsCollected", value = "原件是否已收齐")
    private String paperIsCollected;
    @ApiModelProperty(name = "copyIsCollected", value = "复印件是否已收齐")
    private String copyIsCollected;

    @ApiModelProperty(name = "attCount", value = "电子件已上传份数")
    private Long attCount;
    @ApiModelProperty(name = "realPaperCount", value = "原件已补正|补全份数")
    private Long realPaperCount;
    @ApiModelProperty(name = "realCopyCount", value = "复印件已补正|补全份数")
    private Long realCopyCount;

    @ApiModelProperty(name = "attInoutinstId", value = "电子件输入输出实例ID")
    private String attInoutinstId;
    @ApiModelProperty(name = "paperInoutinstId", value = "原件输入输出实例ID")
    private String paperInoutinstId;
    @ApiModelProperty(name = "copyInoutinstId", value = "复印件输入输出实例ID")
    private String copyInoutinstId;

    @ApiModelProperty(name = "attDueIninstId", value = "电子件应补正|补全实例ID")
    private String attDueIninstId;
    @ApiModelProperty(name = "copyDueIninstId", value = "复印件应补正|补全实例ID")
    private String copyDueIninstId;
    @ApiModelProperty(name = "paperDueIninstId", value = "原件应补正|补全实例ID")
    private String paperDueIninstId;

    @ApiModelProperty(name = "attDueIninstOpinion", value = "电子件补正|补全意见")
    private String attDueIninstOpinion;
    @ApiModelProperty(name = "copyDueIninstOpinion", value = "复印件补正|补全意见")
    private String copyDueIninstOpinion;
    @ApiModelProperty(name = "paperDueIninstOpinion", value = "原件补正|补全意见")
    private String paperDueIninstOpinion;

    @ApiModelProperty(name = "attRealIninstId", value = "电子件已补正|补全实例ID")
    private String attRealIninstId;
    @ApiModelProperty(name = "copyRealIninstId", value = "复印件已补正|补全实例ID")
    private String copyRealIninstId;
    @ApiModelProperty(name = "paperRealIninstId", value = "原件已补正|补全实例ID")
    private String paperRealIninstId;

    @ApiModelProperty(name = "attIsPass", value = "电子件是否已确认")
    private String attIsPass;
    @ApiModelProperty(name = "copyIsPass", value = "复印件是否已确认")
    private String copyIsPass;
    @ApiModelProperty(name = "paperIsPass", value = "原件是否已确认")
    private String paperIsPass;

    @ApiModelProperty(name = "attFiles", value = "电子件附件，用于查看和下载")
    private List<BscAttFileAndDir> attFiles;
    @ApiModelProperty(value = "材料性质，m表示普通材料，c表示证照材料，f表示在线表单")
    private String matProp;
    private String certId;//证照ID
    @ApiModelProperty(name = "ybKbDetailIds", value = "样表空表detailIds")
    private String ybKbDetailIds;
}
