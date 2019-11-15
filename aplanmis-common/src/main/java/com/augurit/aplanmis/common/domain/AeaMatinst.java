package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaohutu
 */
@Data
@ApiModel
public class AeaMatinst implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "材料输入输出实例ID")
    private String inoutinstId;

    @ApiModelProperty(value = "事项实例ID")
    private String iteminstId;
    @ApiModelProperty(value = "事项实例ID")
    private String iteminstName;
    private String itemVerId;

    @ApiModelProperty(value = "输入输出定义ID")
    private String itemInoutId;

    @ApiModelProperty(value = "材料ID")
    private String matId;
    private String matCode;
    private String matFrom;
    @ApiModelProperty(value = "是否通用材料  1 是 0 不是")
    private String isCommon;

    @ApiModelProperty(value = "材料实例ID")
    private String matinstId;

    @ApiModelProperty(value = "材料实例名称")
    private String matinstName;

    @ApiModelProperty(value = "输入输出定义ID")
    private String paperIsRequire;

    @ApiModelProperty(value = "纸质件要求数量")
    private Long duePaperCount;

    @ApiModelProperty(value = "实收纸质件数量")
    private Long realPaperCount;

    @ApiModelProperty(value = "复印件要求数量")
    private Long dueCopyCount;

    @ApiModelProperty(value = "实收复印件数量")
    private Long realCopyCount;

    @ApiModelProperty(value = "电子材料是否必需，0表示容缺，1表示必须")
    private String attIsRequire;

    @ApiModelProperty(value = "电子件数量")
    private Long attCount;

    @ApiModelProperty(value = "实收纸质件材料实例ID")
    private String paperMatinstId;

    @ApiModelProperty(value = "实收复印件材料实例ID")
    private String copyMatinstId;

    @ApiModelProperty(value = "电子件材料实例ID")
    private String attMatinstId;
    @ApiModelProperty(value = "是否批复文件，0表示否，1表示是")
    private String isOfficeDoc;
    private String officialDocNo;
    private String officialDocDueDate;
    private String officialDocTitle;

    private String isCollected;
    private String isParIn;
    private String parInId;
    @ApiModelProperty(value = "上传的文件附件列表")
    private List<BscAttFileAndDir> attFileList;


}
