package com.augurit.aplanmis.common.service.receive.vo;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/***
 * @description
 * @author mohaoqi
 * @date 2019/7/29 0029
 ***/
@Data
@ApiModel("材料实体vo")
public class AeaHiItemMatinstVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id", required = true, dataType="string")
    private String matinstId; // (ID)
    @ApiModelProperty(value = "材料定义ID", required = true, dataType="string")
    private String matId; // (材料定义ID)
    @ApiModelProperty(value = "实交纸质原件份数，默认为0", required = true, dataType="long")
    @Size(max = 10)
    private Long realPaperCount; // (实交纸质原件份数，默认为0)
    @ApiModelProperty(value = "实交复印件份数，默认为0", required = true, dataType="long")
    @Size(max = 10)
    private Long realCopyCount; // (实交复印件份数，默认为0)
    @ApiModelProperty(value = "电子附件数量，默认为0", required = true, dataType="long")
    @Size(max = 10)
    private Long attCount; // (电子附件数量，默认为0)
    @ApiModelProperty(value = "电子附件数量，默认为0", required = true, dataType="string")
    private String creater; // (创建人)
    @ApiModelProperty(value = "创建时间", required = true, dataType="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    @ApiModelProperty(value = "修改人", required = true, dataType="string")
    private String modifier; // (修改人)
    @ApiModelProperty(value = "修改时间", required = true, dataType="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    @ApiModelProperty(value = "材料实例编号", required = true, dataType="string")
    private String matinstCode; // (材料实例编号)
    @ApiModelProperty(value = "材料实例名称", required = true, dataType="string")
    private String matinstName; // (材料实例名称)
    @ApiModelProperty(value = "业主单位ID", required = true, dataType="string")
    private String unitInfoId; // (业主单位ID)
    @ApiModelProperty(value = "建设项目ID", required = true, dataType="string")
    private String projInfoId; // (建设项目ID)
    @ApiModelProperty(value = "批复文件文号【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】", required = true, dataType="string")
    private String officialDocNo; // (批复文件文号【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】)
    @ApiModelProperty(value = "批复文件标题【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】", required = true, dataType="string")
    private String officialDocTitle; // (批复文件标题【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】)
    @ApiModelProperty(value = "批复文件有效期限【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】", required = true, dataType="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date officialDocDueDate; // (批复文件有效期限【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】)
    @ApiModelProperty(value = "批复文件日期【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】", required = true, dataType="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date officialDocPublishDate; // (批复文件日期【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】)

    //扩展字段
    @ApiModelProperty(value = "附件列表", required = true, dataType="list")
    private List<BscAttForm> attFormList;   //附件列表
    //是否补正材料
    @ApiModelProperty(value = "是否补正材料", required = true, dataType="string")
    private String isMakeUp;
    //部门简称
    @ApiModelProperty(value = "部门简称", required = true, dataType="string")
    private String orgShortName;
    //部门名称
    @ApiModelProperty(value = "部门名称", required = true, dataType="string")
    private String orgName;

}
