package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.vo.BusinessZtreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
* -模型
*/
@Data
@ApiModel("专业表实体")
public class AeaImServiceMajor extends BusinessZtreeNode<AeaImServiceMajor> implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专业ID")
    private String majorId;
    @ApiModelProperty(value = "专业编码")
    private String majorCode; // (专业编码)
    @ApiModelProperty(value = "专业名称")
    private String majorName; // (专业名称)
    private String isDelete;
    private String creater;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;
    private String memo; // (备注)
    @ApiModelProperty(value = "父ID")
    private String parentMajorId; // (父ID)
    @ApiModelProperty(value = "序列")
    private String majorSeq; // (序列)
    @ApiModelProperty(value = "资质ID")
    private String qualId;//(资质ID)
    private String rootOrgId;//根组织ID

    //非表字段
    private String keyword; // (查询关键字)
}
