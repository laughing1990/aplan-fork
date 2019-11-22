package com.augurit.aplanmis.front.approve.vo;

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

    @ApiModelProperty(value = "材料性质，m表示普通材料，c表示证照材料，f表示在线表单")
    private String matProp;

    @ApiModelProperty(value = "证照实例id")
    private String certinstId;

    @ApiModelProperty(value = "表单实例id")
    private String forminstId;

    @ApiModelProperty(value = "业主个人ID")
    private String linkmanInfoId;

    private List<BscAttFileAndDir> certFileList;//证照电子附件

    private String certinstSource;//证照来源：local 表示当前系统创建的证照实例,external 表示外部系统对接的证照实例

    private String itemVerIds;

    private String certMatinstId;

    private String certId;

    private String matCode;

    private String stoFormId;

    private List<Map> certinstList;
}
