package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.constants.DeletedStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 电子证照定义表-模型
 *
 * @author jjt
 * @date 2019/08/26
 *
 */
@Data
@ApiModel(description = " 电子证照定义")
public class AeaCert implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String certId; 

    @ApiModelProperty(value = "证照证件编号，颁证单位编制的证照文号")
    private String certCode; 

    @ApiModelProperty(value = "证照证件名称")
    private String certName; 

    @ApiModelProperty(value = "所属文件库ID")
    private String attDirId; 

    @ApiModelProperty(value = "证照证件类型")
    private String certTypeId; 

    @ApiModelProperty(value = "备注说明")
    private String certMemo; 

    @ApiModelProperty(value = "是否删除，0表示未删除，1表示已删除")
    private DeletedStatus isDeleted; 

    @ApiModelProperty(value = "证照所属，c表示企业，u表示个人，p表示工程项目")
    private String certHolder; 

    @ApiModelProperty(value = "软件环境")
    private String softwareEnv; 

    @ApiModelProperty(value = "业务行为")
    private String busAction; 

    @ApiModelProperty(value = "有效期限数字")
    private Long dueNum; 

    @ApiModelProperty(value = "有效期限单位")
    private String dueUnit; 

    @ApiModelProperty(value = "排序")
    private Long sortNo;

    @ApiModelProperty(value = "创建人")
    private String creater; 

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; 

    @ApiModelProperty(value = "修改人")
    private String modifier; 

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime; 

    @ApiModelProperty(value = "根组织ID")
    private String rootOrgId;

    @ApiModelProperty(value = "扩展字段：关键字查询")
    private String keyword;

    @ApiModelProperty(value = "扩展字段：证照类型名称")
    private String typeName; 

    @ApiModelProperty(value = "扩展字段：所属文件库名称")
    private String attDirName; 

    @ApiModelProperty(value = "扩展字段：单位信息id")
    private String unitInfoId;

    @ApiModelProperty(value = "扩展字段：关键字查询")
    private List<AeaHiItemInoutinst> aeaHiItemInoutinsts;

    @ApiModelProperty(value = "配置证书使用")
    private String configCertRecordId;

    private Boolean isConfig = false;

    private String[] checkedCertIds;

    private String matId;
}
