package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 事项材料实例表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:14</li>
 * </ul>
 */
@Data
@ApiModel("事项材料实例表-模型")
public class AeaHiItemMatinst implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "ID")
    private String matinstId; // (ID)
    @ApiModelProperty(value = "材料定义ID")
    private String matId; // (材料定义ID)
    @Size(max = 10)
    @ApiModelProperty(value = "实交纸质原件份数")
    private Long realPaperCount; // (实交纸质原件份数，默认为0)
    @Size(max = 10)
    @ApiModelProperty(value = "实交复印件份数")
    private Long realCopyCount; // (实交复印件份数，默认为0)
    @Size(max = 10)
    @ApiModelProperty(value = "电子附件数量")
    private Long attCount; // (电子附件数量，默认为0)
    @ApiModelProperty(value = "创建人")
    private String creater; // (创建人)
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    @ApiModelProperty(value = "修改人")
    private String modifier; // (修改人)
    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    @ApiModelProperty(value = "材料实例编号")
    private String matinstCode; // (材料实例编号)
    @ApiModelProperty(value = "材料实例名称")
    private String matinstName; // (材料实例名称)
    @ApiModelProperty(value = "业主单位ID")
    private String unitInfoId; // (业主单位ID)
    @ApiModelProperty(value = "建设项目ID")
    private String projInfoId; // (建设项目ID)
    @ApiModelProperty(value = "批复文件文号")
    private String officialDocNo; // (批复文件文号【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】)
    @ApiModelProperty(value = "批复文件标题")
    private String officialDocTitle; // (批复文件标题【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】)
    @ApiModelProperty(value = "批复文件有效期限")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date officialDocDueDate; // (批复文件有效期限【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】)
    @ApiModelProperty(value = "批复文件日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date officialDocPublishDate; // (批复文件日期【MAT_ID所对应的材料定义记录的IS_OFFICIAL_DOC必须等于1】)
    private String rootOrgId;//根组织ID
    //扩展字段
    private List<BscAttForm> attFormList;   //附件列表
    //是否补正材料
    @ApiModelProperty(value = "是否补正材料")
    private String isMakeUp;
    //部门简称
    @ApiModelProperty(value = "部门简称")
    private String orgShortName;
    //部门名称
    @ApiModelProperty(value = "部门名称")
    private String orgName;

    @ApiModelProperty(value = "部门ID")
    private String orgId;

    @ApiModelProperty(value = "是否批复文件，0表示否，1表示是")
    private String isOfficialDoc;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "材料性质，m表示普通材料，c表示证照材料，f表示在线表单")
    private String matProp;

    @ApiModelProperty(value = "证照实例id")
    private String certinstId;

    @ApiModelProperty(value = "表单实例id")
    private String forminstId;

    @ApiModelProperty(value = "业主个人ID")
    private String linkmanInfoId;

    @ApiModelProperty(value = "材料来源，u表示单位，l表示个人，默认是u")
    private String matinstSource;
    @ApiModelProperty(value = "文件ID")
    private String detailId;
    @ApiModelProperty(value = "文件ID（英文状态逗号分隔）")
    private String detailIds;
    @ApiModelProperty(value = "文件夹ID")
    private String dirId;
}
