package com.augurit.aplanmis.common.vo;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ApiModel("并联||单项 申报材料列表")
public class MatinstVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID", required = true, dataType = "string")
    private String matId;

    @ApiModelProperty(value = "材料名称", required = true, dataType = "string")
    private String matName;

    @ApiModelProperty(value = "材料来源", required = true, dataType = "string")
    private String matFrom;

    @ApiModelProperty(value = "文件类型", required = true, dataType = "string")
    private String fileType;

    @ApiModelProperty(value = "实交纸质原件份数，默认为0", required = true, dataType = "long")
    private Long realPaperCount;

    @ApiModelProperty(value = "实交复印件份数，默认为0", required = true, dataType = "long")
    private Long realCopyCount;

    @ApiModelProperty(value = "电子附件数量，默认为0", required = true, dataType = "long")
    private Long attCount;

    @ApiModelProperty(value = "电子附件是否必须", required = true, dataType = "string")
    private String attIsRequire;

    @ApiModelProperty(value = "电子附件是否必须", required = true, dataType = "string")
    private String paperIsRequire;

    @ApiModelProperty(value = "实交纸质原件材料ID", required = true, dataType = "string")
    private String paperMatinstId;

    @ApiModelProperty(value = "实交复印件材料ID", required = true, dataType = "string")
    private String copyMatinstId;

    @ApiModelProperty(value = "电子附件材料ID", required = true, dataType = "string")
    private String attMatinstId;

    @ApiModelProperty(value = "是否有dwg附件", required = true, dataType = "string")
    private String hasDwg;

    @ApiModelProperty(value = "排序字段", required = true, dataType = "long")
    private Long sortNo;

    @ApiModelProperty(value = "材料附件", required = true, dataType = "List")
    private List<BscAttFileAndDir> fileList;

    @ApiModelProperty(value = "应收原件数")
    private Long duePaperCount;

    @ApiModelProperty(value = "应收复印件数")
    private Long dueCopyCount;
    private List<Map> certinstList;
    private String itemVerIds;
    @ApiModelProperty(value = "表单实例id")
    private String forminstId;

    @ApiModelProperty(value = "是否支持容缺 0 否 1是")
    private String zcqy;

}
